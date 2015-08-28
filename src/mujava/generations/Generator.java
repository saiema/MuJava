package mujava.generations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import openjava.ptree.ParseTreeException;
import mujava.OpenJavaException;
import mujava.api.MutantsInformationHolder;
import mujava.app.Core;
import mujava.app.MutantInfo;
import mujava.app.MutationRequest;
import mujava.app.Mutator;
import mujava.util.JustCodeDigest;

/**
 * This class is used to generate several generations of mutants
 * <p>
 * The simplest way of using this class is by taking a {@code MutationRequest}, generate the first generations
 * of mutants and then re-apply the same request to every mutant. This will be repeated until a given condition is met.
 * </p>
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 0.3u
 */
public class Generator {
	private RequestGenerator reqGen;
	private GoalTester goalTester;
	private Mutator mutator;
	private Set<byte[]> mutantHashes;
	public static enum VERBOSE_LEVEL {NO_VERBOSE, BASIC_VERBOSE, FULL_VERBOSE, DEBUG}
	private VERBOSE_LEVEL verboseLevel = VERBOSE_LEVEL.NO_VERBOSE;
	private Integer currentGeneration = -1;
	private static boolean useLowMemoryMode = false;
	private Map<Integer, Map<String, List<String>>> mutantsFoldersPerGeneration;
	
	
	public static void useLowMemoryMode(boolean ulmm) {
		Generator.useLowMemoryMode = ulmm;
	}
	
	
	/**
	 * Construct the basic form of a Generator
	 * 
	 * @param requestGenerator	: 	the request generator that will be used	: 	{@code RequestGenerator}
	 * @param tester			:	the goal tester that will be used		:	{@code GoalTester}
	 * @param vl				:	the level of verbosity to use			:	{@code Generator.VERBOSE_LEVEL}
	 * @see RequestGenerator
	 * @see GoalTester
	 */
	public Generator(RequestGenerator requestGenerator, GoalTester tester, VERBOSE_LEVEL vl) {
		this(requestGenerator, tester);
		this.verboseLevel = vl;
	}
	
	/**
	 * Construct the basic form of a Generator
	 * 
	 * @param requestGenerator	: 	the request generator that will be used	: 	{@code RequestGenerator}
	 * @param tester			:	the goal tester that will be used		:	{@code GoalTester}
	 * @see RequestGenerator
	 * @see GoalTester
	 */
	public Generator(RequestGenerator requestGenerator, GoalTester tester) {
		this.reqGen = requestGenerator;
		this.goalTester = tester;
		this.mutator = new Mutator();
		this.mutantHashes = new HashSet<byte[]>();
		this.mutantsFoldersPerGeneration = new TreeMap<Integer, Map<String, List<String>>>();
	}
	
	/**
	 * This method will generate several mutant generations, using a {@code RequestGenerator} to
	 * get the request for every generation, and using a {@code GoalTester} to know when to stop
	 * 
	 * @param checkOnEveryMutant	:	if {@code true} then the {@code GoalTester} will be used on every new mutant, if {@code false} it will be used on every new generation : {@code boolean}
	 * @return the information of the generated mutant generations : {@code GenerationsInformation}
	 * @throws ClassNotFoundException 
	 * @throws OpenJavaException 
	 * @throws ParseTreeException 
	 */
	public GenerationsInformation generate(boolean checkOnEveryMutant, boolean throwExceptions) throws ClassNotFoundException, OpenJavaException, ParseTreeException {
		boolean goalAchieved = false;
		GenerationsInformation ginfo = new GenerationsInformation(Generator.useLowMemoryMode);
		this.currentGeneration = 0;
		MutationRequest request = this.reqGen.nextRequest();
		File mutFile = null;
		try {
			File originalFile = getFileToMutate(request);
			byte[] digest = JustCodeDigest.digest(originalFile);
			MutantInfo original = new MutantInfo(request.clazz.replaceAll(Core.SEPARATOR, "."), null, originalFile.getPath(), -1, null, digest/*dis.getMessageDigest().digest()*/, null);
			ginfo.add(this.currentGeneration, original);
			this.goalTester.update(ginfo);
			goalAchieved = this.goalTester.goalAchieved();
			if (goalAchieved) {
				return ginfo;
			}
			backupOriginal(originalFile);
			this.mutantHashes.add(digest);
			
			do {
				this.reqGen.update(ginfo, this.currentGeneration);
				this.currentGeneration++;
				List<MutantInfo> lastGeneration = ginfo.getLastGeneration();//ginfo.getGeneration(this.currentGeneration-1);
				request = this.reqGen.nextRequest();
				int currentMutant = 0;
				String currentGenerationDir = request.outputDir;
				for (MutantInfo mut : lastGeneration) {
					String mutantDir = "mutant-"+Integer.toString(currentMutant);
					request.outputDir = appendDir(currentGenerationDir, mutantDir);
					mutFile = new File(mut.getPath());
					copyMutant(mutFile.getAbsolutePath(), originalFile.getAbsolutePath());
					List<MutantInfo> mutants = generateNextGeneration(request, checkOnEveryMutant, ginfo);
					updateFolders(this.currentGeneration, mutantDir);
					if (!checkOnEveryMutant) ginfo.add(this.currentGeneration, mutants);
					if (!checkOnEveryMutant) this.goalTester.update(ginfo);
					goalAchieved = this.goalTester.goalAchieved();
					if (goalAchieved && !this.goalTester.requireGenerationToFinish()) break;
					//delete(mutFile.getAbsolutePath());
					currentMutant++;
				}
				
				verbose(ginfo);
				
			} while (!goalAchieved);
			
			restoreOriginal(originalFile);
			
		} catch (ClassNotFoundException e) {
			if (this.verboseLevel.equals(Generator.VERBOSE_LEVEL.DEBUG) || this.verboseLevel.equals(Generator.VERBOSE_LEVEL.FULL_VERBOSE)) {
				System.err.println("ClassNotFoundException while generating generation " + this.currentGeneration);
				System.err.println("Current file: " + (mutFile!=null?(mutFile.getAbsolutePath()):("null")));
			}
			if (throwExceptions) {
				throw e;
			} else {
				e.printStackTrace();
			}
		} catch (OpenJavaException e) {
			if (this.verboseLevel.equals(Generator.VERBOSE_LEVEL.DEBUG) || this.verboseLevel.equals(Generator.VERBOSE_LEVEL.FULL_VERBOSE)) {
				System.err.println("OpenJavaException while generating generation " + this.currentGeneration);
				System.err.println("Current file: " + (mutFile!=null?(mutFile.getAbsolutePath()):("null")));
			}
			if (throwExceptions) {
				throw e;
			} else {
				e.printStackTrace();
			}
		} catch (ParseTreeException e) {
			if (this.verboseLevel.equals(Generator.VERBOSE_LEVEL.DEBUG) || this.verboseLevel.equals(Generator.VERBOSE_LEVEL.FULL_VERBOSE)) {
				System.err.println("ParseTreeException while generating generation " + this.currentGeneration);
				System.err.println("Current file: " + (mutFile!=null?(mutFile.getAbsolutePath()):("null")));
			}
			if (throwExceptions) {
				throw e;
			} else {
				e.printStackTrace();
			}
		}
		
		return ginfo;
	}
	
	private void updateFolders(Integer generation, String mutantDir) {
		Map<String, List<String>> mutantsFolders = this.getMutantsFolderForGeneration(generation);
		Map<String, List<String>> updatedMutantsFolders = new TreeMap<String, List<String>>();
		for (Entry<String, List<String>> entry : mutantsFolders.entrySet()) {
			if (entry.getKey().startsWith("mutant-")) {
				updatedMutantsFolders.put(entry.getKey(), entry.getValue());
			} else {
				updatedMutantsFolders.put(mutantDir + Core.SEPARATOR + entry.getKey(), entry.getValue());
			}
		}
		this.mutantsFoldersPerGeneration.put(generation, updatedMutantsFolders);
	}


	public String info(GenerationsInformation generationsInfo) {
		String status = "Generated generations : " + generationsInfo.getGenerations() + "\n";
		for (int g = 1; g <= generationsInfo.getGenerations(); g++) {
			status += "generation " + g + " have " + generationsInfo.getGeneration(g).size() + " mutants";
		}
		return status;
	}
	
	public Map<String, List<String>> getMutantsFolderForGeneration(int generation) {
		return this.mutantsFoldersPerGeneration.get(generation);
	}
	
	/**
	 * appends a directory to a path
	 * 
	 * @param original	:	the original path		:	{@code String}
	 * @param newDir	:	the directory to append	:	{@code String}
	 * @return the original path with the new directory appended : {@code String}
	 */
	private String appendDir(String original, String newDir) {
		String fixedDir = newDir.endsWith(Core.SEPARATOR)?newDir:(newDir+Core.SEPARATOR);
		if (original.endsWith(Core.SEPARATOR)) {
			return original + fixedDir;
		} else {
			return original + Core.SEPARATOR + fixedDir;
		}
	}

	/**
	 * This method generates mutants of the next generation
	 * 
	 * @param request				:	the request used to generate the mutants of the next generation	:	{@code MutationRequest}
	 * @param checkOnEveryMutant	:	if {@code true} then the {@code GoalTester} will be used on every new mutant, if {@code false} it will be used on every new generation : {@code boolean}
	 * @param ginfo 
	 * @return	a list of generated mutants	: List<MutantInfo>
	 * @throws ClassNotFoundException
	 * @throws OpenJavaException
	 * @throws ParseTreeException
	 */
	protected List<MutantInfo> generateNextGeneration(MutationRequest request, boolean checkOnEveryMutant, GenerationsInformation ginfo) throws ClassNotFoundException, OpenJavaException, ParseTreeException {
		List<MutantInfo> mutants;
		if (checkOnEveryMutant) {
			mutants = generateNewGenerationOneMutantAtATime(request, ginfo);
		} else {
			mutants = generateNewGenerationAtOnce(request);
		}
		return mutants;
	}
	
	/**
	 * This method generates mutants of the next generation all at once
	 * 
	 * @param request				:	the request used to generate the mutants of the next generation	:	{@code MutationRequest}
	 * @return	a list of generated mutants	: List<MutantInfo>
	 * @throws ClassNotFoundException
	 * @throws OpenJavaException
	 * @throws ParseTreeException
	 */
	protected List<MutantInfo> generateNewGenerationAtOnce(MutationRequest request) throws ClassNotFoundException, OpenJavaException, ParseTreeException {
		List<MutantInfo> mutantsInfo = new LinkedList<MutantInfo>();
		mutator.setRequest(request);
		Map<String, MutantsInformationHolder> mutationsPerMethod = mutator.obtainMutants();
		for (Entry<String, MutantsInformationHolder> mutations : mutationsPerMethod.entrySet()) {
			List<MutantInfo> newMutants = mutator.writeMutants(mutations.getKey(), mutations.getValue(), false);
			List<MutantInfo> filteredMutants = filterRepeatedMutants(newMutants);
			mutantsInfo.addAll(filteredMutants);
		}
		if (Generator.useLowMemoryMode) {
			clearPreviousGenerationsMutantsFolders();
		}
		addMutantsFoldersForCurrentGeneration(mutator.mutantsFolders);
		mutator.resetMutantFolders();
		return mutantsInfo;
	}
	
	/**
	 * Filters and deleted repeated mutants, uses {@code JustCodeDigest} to calculate mutant hashes
	 * 
	 * @param newMutants	:	the list of mutants to filter	:	{@code List<MutantInfo>}
	 * @return a list of filtered mutants
	 */
	private List<MutantInfo> filterRepeatedMutants(List<MutantInfo> newMutants) {
		List<MutantInfo> filteredMutants = new LinkedList<MutantInfo>();
		for (MutantInfo mut : newMutants) {
			String path = mut.getPath();
			File mutantFile = new File(path);
			byte[] digest = JustCodeDigest.digest(mutantFile);
			if (this.mutantHashes.add(digest)) {//if (!hashExists(digest)) {//
				filteredMutants.add(mut);
			} else {
				delete(path);					//deletes mutant
				delete(mutantFile.getParent());	//deletes the folder which contained the mutant
			}
		}
		return filteredMutants;
	}
	
//	private boolean hashExists(byte[] hash) {
//		for (byte[] other : this.mutantHashes) {
//			if (Arrays.equals(hash, other)) {
//				return true;
//			}
//		}
//		this.mutantHashes.add(hash);
//		return false;
//	}

	/**
	 * This method generates mutants of the next generation checking the {@code GoalTester} on each new mutant
	 * 
	 * @param request				:	the request used to generate the mutants of the next generation	:	{@code MutationRequest}
	 * @param ginfo 
	 * @return	a list of generated mutants	: List<MutantInfo>
	 * @throws ClassNotFoundException
	 * @throws OpenJavaException
	 */
	protected List<MutantInfo> generateNewGenerationOneMutantAtATime(MutationRequest request, GenerationsInformation ginfo) throws ClassNotFoundException, OpenJavaException {
		List<MutantInfo> mutantsInfo = new LinkedList<MutantInfo>();
		Mutator mut = new Mutator(request, 1);
		new Thread(mut).start();
		MutantInfo mi = mut.getNext();
		boolean goalAchieved = false;
		while (mi != null && !goalAchieved) {
			mutantsInfo.add(mi);
			ginfo.add(this.currentGeneration, mi);
			this.goalTester.update(ginfo);
			goalAchieved = this.goalTester.goalAchieved();
			if (!goalAchieved) mi = mut.getNext();
		}
		if (Generator.useLowMemoryMode) {
			clearPreviousGenerationsMutantsFolders();
		}
		addMutantsFoldersForCurrentGeneration(mutator.mutantsFolders);
		mut.resetMutantFolders();
		return filterRepeatedMutants(mutantsInfo);
	}
	
	//======================AUXILIARY METHODS======================
	
	private void clearPreviousGenerationsMutantsFolders() {
		for (int g = 1; g < this.currentGeneration; g++) {
			this.mutantsFoldersPerGeneration.remove(g);
		}
	}


	private void addMutantsFoldersForCurrentGeneration(Map<String, List<String>> mutantsFolders) {
		if (this.mutantsFoldersPerGeneration.containsKey(this.currentGeneration)) {
			Map<String, List<String>> mutantsFolderForCurrGen = this.mutantsFoldersPerGeneration.get(this.currentGeneration);
			for (Entry<String, List<String>> newEntry : mutantsFolders.entrySet()) {
				if (mutantsFolderForCurrGen.containsKey(newEntry.getKey())) {
					List<String> existingValues = mutantsFolderForCurrGen.get(newEntry.getKey());
					for (String newValue : newEntry.getValue()) {
						if (!exist(existingValues, newValue)) {
							existingValues.add(newValue);
						}
					}
				} else {
					mutantsFolderForCurrGen.put(newEntry.getKey(), newEntry.getValue());
				}
			}
		} else {
			this.mutantsFoldersPerGeneration.put(this.currentGeneration, mutantsFolders);
		}
	}


	private boolean exist(List<String> values, String val) {
		for (String v : values) {
			if (v.compareTo(val) == 0) {
				return true;
			}
		}
		return false;
	}


	private void verbose(GenerationsInformation ginfo) {
		switch (this.verboseLevel) {
			case BASIC_VERBOSE: {
				System.out.println(ginfo.showGeneration(this.currentGeneration, false));
				break;
			}
			case DEBUG:
				break;
			case FULL_VERBOSE: {
					System.out.println(ginfo.showGeneration(this.currentGeneration, true));
					break;
				}
			case NO_VERBOSE:
				break;
			}
	}
	
	private File getFileToMutate(MutationRequest request) {
		String className = request.clazz;
		String[] paks = className.split(Core.SEPARATOR);
		String pak = "";
		for (int i = 0; i < paks.length - 1;i++) {
			pak += paks[i] + Core.SEPARATOR;
		}
		className = paks[paks.length-1];
		String fileToMutateName = className + ".java";
		File fileToMutate = new File(request.inputDir + pak + fileToMutateName);
		return fileToMutate;
	}
	
	private void backupOriginal(File original) {
		String originalPath = original.getAbsolutePath();
		File backup = new File(originalPath+".backup");
		try {
			FileInputStream originalStream = new FileInputStream(original);
			Files.copy(originalStream, backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void restoreOriginal(File original) {
		File backup = new File(original.getAbsolutePath()+".backup");
		try {
			FileInputStream originalStream = new FileInputStream(backup);
			Files.copy(originalStream, original.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		delete(backup.getAbsolutePath());
	}
	
	private boolean copyMutant(String mutantPath, String originalPath) {
		String fixedMutantPath = mutantPath;
		String fixedOriginalPath = originalPath;
		File mutant = new File(fixedMutantPath);
		if (!mutant.exists()) {
			return false;
		}
		File original = new File(fixedOriginalPath);
		if (original.getParentFile()==null?!original.exists():!original.getParentFile().exists()) {
			return false;
		}
		try {
			Path dest = original.getParentFile()==null?original.toPath():original.getParentFile().toPath();
			Files.copy(mutant.toPath(), dest.resolve(mutant.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void delete(String path) {
		String fixedPath = path;
		File f = new File(fixedPath);
		if (f.exists()) {
			f.delete();
		}
	}

}
