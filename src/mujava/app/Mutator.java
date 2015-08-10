package mujava.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import mujava.OpenJavaException;
import mujava.api.Api;
import mujava.api.Mutant;
import mujava.api.Mutation;
import mujava.api.MutantType;
import mujava.api.MutantsInformationHolder;
import mujava.op.util.OLMO;
import mujava.util.JustCodeDigest;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;

/**
 * This class is used to generate mutants from a {@code MutationRequest}, it supports three
 * different ways of generating mutants.
 * <p>
 * Creating a new instance of this class and calling {@code generateMutants()}
 * <pre>
 * 		Mutator mut = new Mutator(req);
 * 		List<MutantInfo> mil = mut.generateMutants();
 * 		HashMap<String, List<String>> mutantsFolder = mut.mutantsFolders;
 * 		mut.resetMutantFolders();
 * </pre>
 * <p>
 * Creating a new instance of this class, creating a thread and generate mutants calling {@code genNext()}
 * until the method returns {@code null}
 * <pre>
 * 		Mutator mut = new Mutator(req, 1); //this will generate 1 mutant until the method {@code genNext()} is called
 * 		new Thread(mut).start();
 * 		MutantInfo mi = mut.genNext();
 * 		while (mi != null) {
 * 			doSomething(mi);
 * 			mi = mut.getNext();
 * 		}
 * 		HashMap<String, List<String>> mutantsFolder = mut.mutantsFolders;
 * 		mut.resetMutantFolders();
 * </pre>
 * <p>
 * Obtaining mutations, filtering and applying selected mutations, this supports writting several mutations on the same file
 * <pre>
 * 		Mutator mut = new Mutator();
 * 		boolean writeAllMutationsOnTheSameFile = true;
 * 		mut.setRequest(req);
 * 		Map<String, MutantsInformationHolder> mutations = mut.obtainMutants();
 * 		for (Entry<String, MutantsInformationHolder> sameMethodMutations : mutations.entrySet()) {
 * 			String currentMethod = sameMethodMutations.getKey();
 *			List<Mutation> methodMutations = sameMethodMutations.getValue().getMutantsIdentifiers();
 *			mut.filterCompatibleMutants(methodMutations); //this should be replaced with a custom filtering method
 *			//---------------------------------------------------------------------------
 *			//when using a custom filtering method the above line should be replaced with
 *			//customFiltering(methodMutations);
 *			//and the writeMutants line should be replaced with
 *			//if (mut.checkCompatibility(methodMutations)) mut.writeMutants(currentMethod, methodMutations, writeAllMutationsOnTheSameFile);
 *			//---------------------------------------------------------------------------
 *			mut.writeMutants(currentMethod, methodMutations, writeAllMutationsOnTheSameFile);
 *		}
 * </pre>
 * <hr>
 * <b>all three ways are incompatible with each other</b>
 * <hr>
 * <p>
 * @see MutationRequest
 * @see MutantInfo
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 1.7
 */
public class Mutator implements Runnable{

	private BlockingQueue<MutantInfo> queue;
	private List<MutantsInformationHolder> mihs;
	private boolean finished = false;
	private OpenJavaException ojerror = null;
	private ClassNotFoundException cnferror = null;
	private static Integer index = 0;
	
	private List<List<Mutation>> Mutationss = null;
	/**
	 * for each clazz and method mutated there's a list of subfolders
	 * the key in this map is {@code <class name> + <System path separator> + <method || (fieldMutations || classMutations)>}
	 * the entry for a key is a list of subfolders 
	 */
	public HashMap<String, List<String>> mutantsFolders = new HashMap<String, List<String>>();
	private MutationRequest request;
	
	
	/**
	 * Creates a new instance of this class to generate mutants with {@code generateMutants()} method
	 * @param request : the request that will be used to generate mutants : {@code MutationRequest}
	 */
	public Mutator(MutationRequest request) {
		this.queue = null;
		this.request = request;
	}
	
	/**
	 * Creates a new instance of this class to generate mutants with a thread and the method {@code genNext()}
	 * @param request : the request that will be used to generate mutants : {@code MutationRequest}
	 * @param cap : the number of mutants generated until the next call to {@code genNext()} : {@code int}
	 */
	public Mutator(MutationRequest request, int cap) {
		this.queue = new LinkedBlockingQueue<MutantInfo>(cap);
		this.request = request;
	}
	
	/**
	 * Creates a new instance of this class, this should be used when constructing a {@code Mutator} object
	 * and reusing it to generate mutants.
	 * 
	 * this constructor is meant to be used with the method {@link Mutator#generateMutants(MutationRequest)}
	 */
	public Mutator() {
		this.queue = null;
		this.request = null;
	}
	
	/**
	 * Allows to set the mutation request, this is used when the user wants to obtaing mutations,
	 * filtering them and after that writing them to the same file.
	 * 
	 * @param req : the mutatio request to set : {@code MutationRequest}
	 * <hr>
	 * <b>note: this method should not be used with constructors {@link Mutator#Mutator(MutationRequest)} and {@link Mutator#Mutator(MutationRequest, int)}</b>
	 */
	public void setRequest(MutationRequest req) {
		this.request = req;
	}
	
	/**
	 * This method is equivalent to {@link Mutator#generateMutants()} when using the {@link Mutator#Mutator(MutationRequest)}
	 * constructor.
	 * 
	 * @param request : the request that will be used to generate mutants : {@code MutationRequest}
	 * @return a list of {@code MutantInfo} for each mutant generated : {@code List<MutantInfo>}
	 * @throws OpenJavaException
	 * @throws ClassNotFoundException
	 * <hr>
	 * <b>* if the constructor with the {@code cap} argument was used then this method shouldn't be called</b>
	 */
	public List<MutantInfo> generateMutants(MutationRequest request) throws OpenJavaException, ClassNotFoundException {
		this.request = request;
		return generateMutants();
	}
	
	/**
	 * This method is to be used only when this class is being runned as a thread
	 * @return information related to a mutant or {@code null} if there's no more mutants to generate : {@code MutantInfo}
	 * @throws OpenJavaException
	 * @throws ClassNotFoundException
	 * @see Mutator#Mutator(MutationRequest, int)
	 */
	public MutantInfo getNext() throws OpenJavaException, ClassNotFoundException {
		try {
			if (finished) return null;
			if (ojerror != null) throw ojerror;
			if (cnferror != null) throw cnferror;
			return this.queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * This method resets the variable {@code mutantsFolders} to an empty Map
	 */
	public void resetMutantFolders() {
		mutantsFolders = new HashMap<String, List<String>>();
	}
	
	/**
	 * @return a list of {@code MutantsInformationHolder} that is constructed when generating mutants : {@code List<MutantsInformationHolder>}
	 * <hr>
	 * <i>for the moment this list has no use</i>
	 */
	public List<MutantsInformationHolder> getMIH() {
		return this.mihs;
	}
	

	/**
	 * Generates mutants based on the request {@code MutationRequest} used in the constructor,
	 * if the constructor used was the one with the {@code cap} argument then this method will generate
	 * {@code cap} mutants until the method {@code genNext()} is called*
	 * @return a list of {@code MutantInfo} for each mutant generated : {@code List<MutantInfo>}
	 * @throws OpenJavaException
	 * @throws ClassNotFoundException
	 * <hr>
	 * <b>* if the constructor with the {@code cap} argument was used then this method shouldn't be called</b>
	 */
	public List<MutantInfo> generateMutants() throws OpenJavaException, ClassNotFoundException {
		//|--------------------Initialization--------------------|
		List<MutantInfo> res = new LinkedList<MutantInfo>();
		this.mihs = new LinkedList<MutantsInformationHolder>();
		mutantsFolders = new HashMap<String, List<String>>();
		Mutationss = new LinkedList<List<Mutation>>();
		//String basePath = request.outputDir;
		//List<MutantsInformationHolder> mihs = null;
		Set<Mutant> mutOps = new HashSet<Mutant>();
		mihs = new LinkedList<MutantsInformationHolder>();
		MutantsInformationHolder mih;
		mutOps.addAll(Arrays.asList(request.ops));
		String className = request.clazz;
		String[] paks = className.split(Core.SEPARATOR);
		String pak = "";
		for (int i = 0; i < paks.length - 1;i++) {
			pak += paks[i] + Core.SEPARATOR;
		}
		className = paks[paks.length-1];
		String fileToMutateName = Api.getMainClassName(className) + ".java";
		File fileToMutate = new File(request.inputDir + pak + fileToMutateName);
		if (!fileToMutate.exists()) {
			throw new IllegalArgumentException("The specified file "
					+ fileToMutate.getAbsolutePath() + " does not exist");
		}
		//|--------------------Initialization--------------------|

		//|--------------------Mutation loop--------------------|
		for (String currentMethod : request.methods) {
			//|--------------------Mutants Generation--------------------|
			mih = Api.generateMutants(fileToMutate, className, currentMethod, mutOps);
			//|--------------------Mutants Generation--------------------|

			//System.out.println(mih);
			mihs.add(mih);

		}
		//|--------------------Mutation loop--------------------|


		for (MutantsInformationHolder m : mihs) {
			List<Mutation> Mutations = m
					.getMutantsIdentifiers();
			Mutationss.add(Mutations);
			//System.out.println(Mutations.size());
		}

		//|--------------------Mutants to file loop--------------------|
		for (int i = 0; i < mihs.size(); i++) {
			int j = 0;
			List<String> mF = new LinkedList<String>();
			String mutantID = "";
			for (Mutation mi : Mutationss.get(i)) {
				String methodName = "";
				if (request.methods[i].compareTo(MutationRequest.MUTATE_FIELDS)==0) {
					methodName = "fieldMutations";
				} else if (request.methods[i].compareTo(MutationRequest.MUTATE_CLASS)==0) {
					methodName = "classMutations";
				} else {
					methodName = request.methods[i];
				}
				File mutatedFile = new File(request.outputDir + request.clazz + Core.SEPARATOR + methodName + Core.SEPARATOR
						+ mi.getMutOp() + Core.SEPARATOR + i + j + Core.SEPARATOR + pak
						+ fileToMutateName);
				mutantID = request.clazz+Core.SEPARATOR+methodName;
				mF.add(""+mi.getMutOp() + Core.SEPARATOR + i + j + Core.SEPARATOR);
				PrintWriter pw = null;
				try {
					mutatedFile.getParentFile().mkdirs();
					mutatedFile.createNewFile();
					DigestOutputStream dos = null;
					try {
						dos = new DigestOutputStream(new FileOutputStream(mutatedFile, true), MessageDigest.getInstance("MD5"));
						pw = new PrintWriter(dos);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
					int mutatedLine = Api.writeMutant(mihs.get(i).getCompUnit(), mi, pw);
					MutantInfo minfo = new MutantInfo(request.clazz.replaceAll(Core.SEPARATOR, "."), (request.methods[i]==null?"fieldMutations":request.methods[i]), mutatedFile.getPath(), mutatedLine, mi.getMutOp(), dos.getMessageDigest().digest(), null);
					if (this.queue != null) this.queue.put(minfo);
					res.add(minfo);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ParseTreeException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (pw != null) {
						pw.close();
					}
				}
				j++;
			}
			mutantsFolders.put(mutantID, mF);
		}
		//|--------------------Mutants to file loop--------------------|
		finished = true;

		return res;
	}
	
	/**
	 * Allows to obtain mutations but this method does not write them, this mutations can be writen directly
	 * or can be filtered first and write only the desired mutations.
	 * <p>
	 * Mutations can be filtered accessing the mutations list with {@link MutantsInformationHolder#getMutantsIdentifiers()}
	 * 
	 * @return a map of (mutated method, MutantsInformationHolder) : {@code Map<String, MutantsInformationHolder>}
	 *
	 * @throws OpenJavaException
	 * @throws ClassNotFoundException
	 * @throws ParseTreeException
	 */
	public Map<String, MutantsInformationHolder> obtainMutants() throws OpenJavaException, ClassNotFoundException, ParseTreeException {
		Map<String, MutantsInformationHolder> mutants = new HashMap<String, MutantsInformationHolder>();
		//|--------------------Initialization--------------------|
		Set<Mutant> mutOps = new HashSet<Mutant>();
		mutOps.addAll(Arrays.asList(request.ops));
		String className = request.clazz;
		String[] paks = className.split(Core.SEPARATOR);
		String pak = "";
		for (int i = 0; i < paks.length - 1;i++) {
			pak += paks[i] + Core.SEPARATOR;
		}
		className = paks[paks.length-1];
		String fileToMutateName = Api.getMainClassName(className) + ".java";
		File fileToMutate = new File(request.inputDir + pak + fileToMutateName);
		if (!fileToMutate.exists()) {
			throw new IllegalArgumentException("The specified file "
					+ fileToMutate.getAbsolutePath() + " does not exist");
		}
		//|--------------------Initialization--------------------|
		
		MutatorsInfo mutatorsInfo = MutatorsInfo.getInstance();
		
		//|--------------------Generation loop--------------------|
		for (String currentMethod : request.methods) {
			//|--------------------Mutants Generation--------------------|
			ParseTreeObject.resetObjectID();
			MutantsInformationHolder mih = Api.generateMutants(fileToMutate, className, currentMethod, mutOps);
			//|--------------------Mutants Generation--------------------|

			mutants.put(currentMethod, mih);
			
			for (Mutation mi : mih.getMutantsIdentifiers()) {
				OLMO olmo;
				boolean affectsOneLine = mutatorsInfo.affectsOneLine(mi.getMutOp());
				boolean isMethodLevelMutation = false;
				if (mutatorsInfo.getMutantType(mi.getMutOp()) == MutantType.MethodLevel) {
					isMethodLevelMutation = true;
				} else if (mutatorsInfo.getMutantType(mi.getMutOp()) == MutantType.ClassAndMethodLevel) {
					isMethodLevelMutation = currentMethod != MutationRequest.MUTATE_CLASS && currentMethod != MutationRequest.MUTATE_FIELDS;
				}
				if (affectsOneLine && isMethodLevelMutation/*mi.isOneLineInMethodOp()*/) {
					olmo = new OLMO(mi);
					olmo.visit(mih.getCompUnit());
					mi.setAffectedLine(olmo.getAffectedLine());
				} else if (affectsOneLine) {
					mi.setAffectedLine(Mutation.SINGLE_LINE_OUTSIDE_METHOD_DECLARATION);
				}
			}

		}
		//|--------------------Generation loop--------------------|
		//finished = true;
		return mutants;
	}
	
	/*
	 * Just a test method, the client is the one who must filter the desired mutants
	 * and use the method checkCompatibility(List<Mutation> mis) to check if selected
	 * mutants are compatible and can be applied on the same file 
	 */
	public static void filterCompatibleMutants(MutantsInformationHolder mih) {
		filterCompatibleMutants(mih.getMutantsIdentifiers());
	}
	
	/**
	 * This method takes a mutations list and will remove any mutation that return false when calling {@Mutation#isOneLineInMethodOp()}
	 * and for all mutations affecting the same node only one will be selected at random.
	 * 
	 * @param mutations
	 */
	public static void filterCompatibleMutants(List<Mutation> mutations) {
		int current = 0;
		List<Mutation> mis = mutations;
		List<Mutation> filteredMutations = new LinkedList<Mutation>();
		Random random = new Random();
		Map<Integer, List<Mutation>> mutationsPerNodeID = new HashMap<Integer, List<Mutation>>();
		while (current < mis.size()) {
			if (!mis.get(current).isOneLineInMethodOp()) {
				mis.remove(current);
			} else {
				Mutation currMutation = mis.get(current);
				Integer mutationNodeID = currMutation.getOriginal().getObjectID();
				List<Mutation> mutationsSameID;
				if (mutationsPerNodeID.containsKey(mutationNodeID)) {
					mutationsSameID = mutationsPerNodeID.get(mutationNodeID);
				} else {
					mutationsSameID = new LinkedList<Mutation>();
					mutationsPerNodeID.put(mutationNodeID, mutationsSameID);
				}
				mutationsSameID.add(currMutation);
				current++;
			}
		}
		for (Entry<Integer, List<Mutation>> mutationsPerNode : mutationsPerNodeID.entrySet()) {
			Integer idx = random.nextInt(mutationsPerNode.getValue().size());
			Mutation selectedMutation = mutationsPerNode.getValue().get(idx);
			filteredMutations.add(selectedMutation);
		}
		mutations.clear();
		mutations.addAll(filteredMutations);
	}
	
	/*
	 * Just a test method, the client is the one who must filter the desired mutants
	 * and use the method checkCompatibility(List<Mutation> mis) to check if selected
	 * mutants are compatible and can be applied on the same file 
	 */
	public static List<Mutation> getFilteredCompatibleMutants(List<Mutation> mutations) {
		int current = 0;
		List<Mutation> mis = new LinkedList<Mutation>();
		mis.addAll(mutations);
		List<ParseTreeObject> nodesToMutate = new LinkedList<ParseTreeObject>();
		while (current < mis.size()) {
			if (!mis.get(current).isOneLineInMethodOp()) {
				mis.remove(current);
			} else if (alreadyMutated(mis.get(current).getOriginal(), nodesToMutate)) {
				mis.remove(current);
			} else {
				current++;
			}
		}
		return mis;
	}
	
	/**
	 * This method checks the following conditions for every mutation:
	 * <li> mutation affect only one line inside a method declaration </li>
	 * <li> no two mutations affect the same node </li>
	 *
	 * @param mis : the list of mutations to check : {@code List<Mutation>}
	 * @return true if the conditions are met
	 * <hr>
	 * <b> * important: if this method returns true it doesn't mean that mutations on the same line can be merged, use
	 * {@link Mutator#checkMergingCompatibility(List<Mutation>)} to check if mutations can be merged</b>
	 * @throws ParseTreeException
	 */
	public static boolean checkCompatibility(List<Mutation> mis) throws ParseTreeException {
		List<ParseTreeObject> nodesToMutate = new LinkedList<ParseTreeObject>();
		for (Mutation mi : mis) {
			if (!mi.isOneLineInMethodOp()) {
				return false;
			} else if (alreadyMutated(mi.getOriginal(), nodesToMutate)) {
				return false;
			}
		}
		return true;
	}
	
	private static boolean alreadyMutated(ParseTreeObject node, List<ParseTreeObject> nodesToMutate) {
		for (ParseTreeObject mutatedNode : nodesToMutate) {
			if (node.getObjectID() == mutatedNode.getObjectID()) {
				return true;
			}
		}
		nodesToMutate.add(node);
		return false;
	}
	
	/**
	 * This method checks the following conditions:
	 * <li> all mutations affect the same line </li>
	 * <li> mutations affect only one line inside a method declaration </li>
	 * <li> given two mutations m1 (ori1, mut1) and m2 (ori2, mut2) if m1 affects ori2 then it must affect mut2 </li>
	 *
	 * @param mutations : the mutations : {@code List<Mutation>}
	 * @return true if mutations can be merged : {@code boolean}
	 * @throws ParseTreeException
	 */
	public static boolean checkMergingCompatibility(List<Mutation> mutations) throws ParseTreeException {
		boolean canBeMerged = !mutations.isEmpty() && mutationsAreMethodLevel(mutations);
		if (!canBeMerged) return false;
		int affectedLine = 0;
		for (Mutation mi : mutations) {
			if (affectedLine == 0) {
				affectedLine = mi.getAffectedLine();
			} else if (mi.getAffectedLine() != affectedLine) {
				canBeMerged = false;
				break;
			}
			if (!mutationCanBeApplied(mi, mutations)) {
				canBeMerged = false;
				break;
			}
		}
		return canBeMerged;
	}
	
	private static boolean mutationsAreMethodLevel(List<Mutation> mutations) {
		for (Mutation mi : mutations) {
			if ((mi.getAffectedLine() == -1) || (mi.getAffectedLine() == Mutation.SINGLE_LINE_OUTSIDE_METHOD_DECLARATION)) {
				return false;
			}
		}
		return true;
	}
	
	private static boolean mutationCanBeApplied(Mutation mut, List<Mutation> mutations) throws ParseTreeException {
		OLMO olmo;
		boolean res = true;
		for (Mutation mi : mutations) {
			if (mut == mi) continue;
			olmo = new OLMO(mut);
			boolean canBeAppliedToOriginal = olmo.findOriginalNodeIn(mi.getOriginal());
			boolean canBeAppliedToMutant = olmo.findOriginalNodeIn(mi.getMutant());
			res = (canBeAppliedToOriginal == canBeAppliedToMutant);
		}
		return res;
	}
	
	/**
	 * Merge several mutations without writing it to a file
	 *
	 * @param mutations : the mutations to merge : {@code List<Mutation>}
	 * @return one mutation with all mutations merged if {@code checkMergingCompatibility(mutations) returns true, {@code null} otherwise : {@code Mutation}
	 * @throws ParseTreeException
	 */
	public static Mutation merge(List<Mutation> mutations) throws ParseTreeException {
		Mutation mergedMutation = null;
		if (checkMergingCompatibility(mutations)) {
			OLMO olmo = new OLMO(mutations);
			mergedMutation = olmo.mergeMutants();
		}
		return mergedMutation;
	}
	
	/**
	 * Write mutations to files, this method allows to write one file per mutation or apply all mutations to a same file
	 * 
	 * @param method						: the method to mutate : {@code String}
	 * @param mih							: MutantsInformationHolder which contains all mutations for {@code method} : {@code MutantsInformationHolder}
	 * @param applyAllMutantsToSameFile		: if true then all mutations will be writen on the same file : {@code boolean}
	 * @return
	 * @throws OpenJavaException
	 * @throws ClassNotFoundException
	 * @throws ParseTreeException
	 */
	public List<MutantInfo> writeMutants(String method, MutantsInformationHolder mih, boolean applyAllMutantsToSameFile) throws OpenJavaException, ClassNotFoundException, ParseTreeException {
		List<Mutation> mutationsToWrite;
		if (applyAllMutantsToSameFile) {
			OLMO olmo;
			Map<Integer, List<Mutation>> mutations = new HashMap<Integer, List<Mutation>>();
			for (Mutation mi : mih.getMutantsIdentifiers()) {
				Integer affectedLine = mi.getAffectedLine();
				if (affectedLine == -1) {
					System.err.println("Mutation dropped because it can't be used when writting several mutations to the same file");
					System.err.println("Mutation: " + mi.toString());
					continue;
				}
				if (mutations.containsKey(affectedLine)) {
					mutations.get(affectedLine).add(mi);
				} else {
					List<Mutation> mis = new LinkedList<Mutation>();
					mis.add(mi);
					mutations.put(affectedLine, mis);
				}
			}
			mutationsToWrite = new LinkedList<Mutation>();
			for (List<Mutation> equalMis : mutations.values()) {
				Mutation miToAdd = null;
				boolean addOneMutation = false;
				if (equalMis.size() == 1) {
					miToAdd = equalMis.get(0);
					addOneMutation = true;
				} else if (equalMis.get(0).getAffectedLine() == Mutation.SINGLE_LINE_OUTSIDE_METHOD_DECLARATION) {
					mutationsToWrite.addAll(equalMis);
				} else if (checkMergingCompatibility(equalMis)) {
					olmo = new OLMO(equalMis);
					miToAdd = olmo.mergeMutants();
					addOneMutation = true;
				}
				if (addOneMutation) mutationsToWrite.add(miToAdd);
			}
			olmo = new OLMO();
			for (Mutation mi : mutationsToWrite) {
				if (mi.getMutOp() == Mutant.MULTI) {
					olmo.modifyAST(mih.getCompUnit(), mi);
					olmo.decreaseMutationLimit(mih.getCompUnit(), mi);
				} else {
					olmo.decreaseMutationLimit(mih.getCompUnit(), mi);
					olmo.modifyAST(mih.getCompUnit(), mi);
				}
			}
		} else {
			mutationsToWrite = mih.getMutantsIdentifiers();
		}
		List<MutantInfo> res = new LinkedList<MutantInfo>();
		//|--------------------Mutants to file loop--------------------|
		List<String> mF = new LinkedList<String>();
		String className = this.request.clazz;
		String[] paks = className.split(Core.SEPARATOR);
		String pak = "";
		for (int i = 0; i < paks.length - 1;i++) {
			pak += paks[i] + Core.SEPARATOR;
		}
		className = paks[paks.length-1];
		String fileToMutateName = Api.getMainClassName(className) + ".java";
		if (!applyAllMutantsToSameFile) Mutator.index = 0;
		//--IF MUTANTS WILL BE APPLIED TO THE SAME FILE THEN THE FOLLOWING OBJECTS WILL BE 
		//--INSTANCIATED ONLY ONCE
		File mutatedFile = null;
		DigestOutputStream dos = null;
		PrintWriter pw = null;
		String mutantID = "";
		boolean mutantFileCreated = false;
		MutantInfo minfo = null;
		boolean mutationsWritten = false;
		//---------------------------------------------------------------------------------
		for (Mutation mi : mutationsToWrite) {
			String methodName = "";
			if (method.compareTo(MutationRequest.MUTATE_FIELDS)==0) {
				methodName = "fieldMutations";
			} else if (method.compareTo(MutationRequest.MUTATE_CLASS)==0) {
				methodName = "classMutations";
			} else {
				methodName = method;
			}
			Mutant op = applyAllMutantsToSameFile?Mutant.MULTI:mi.getMutOp();
			if ((applyAllMutantsToSameFile && mutatedFile == null) || !applyAllMutantsToSameFile) {
				mutatedFile = new File(	this.request.outputDir +
										this.request.clazz + Core.SEPARATOR +
										methodName + Core.SEPARATOR +
										op + Core.SEPARATOR +
										Mutator.index + Core.SEPARATOR +
										pak + fileToMutateName);
			}
			if ((applyAllMutantsToSameFile && mutantID.isEmpty()) || !applyAllMutantsToSameFile) {
				mutantID = this.request.clazz + Core.SEPARATOR + methodName;
				mF.add(""+op + Core.SEPARATOR + Mutator.index + Core.SEPARATOR);
			}
			try {
				if ((applyAllMutantsToSameFile && !mutantFileCreated) || !applyAllMutantsToSameFile) {
					mutatedFile.getParentFile().mkdirs();
					mutatedFile.createNewFile();
					try {
						dos = new DigestOutputStream(new FileOutputStream(mutatedFile, true), MessageDigest.getInstance("MD5"));
						pw = new PrintWriter(dos);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
					mutantFileCreated = true;
				}
				
				int mutatedLine = -1;
				
				if (applyAllMutantsToSameFile && !mutationsWritten) {
					Mutation dummy = new Mutation(Mutant.MULTI, null, null);
					Api.writeMutant(mih.getCompUnit(), dummy, pw);
					mutationsWritten = true;
				} else if (!applyAllMutantsToSameFile) {
					mutatedLine = Api.writeMutant(mih.getCompUnit(), mi, pw);
				}
				
				if ((applyAllMutantsToSameFile && minfo == null) || !applyAllMutantsToSameFile) {
					byte[] digest = JustCodeDigest.digest(mutatedFile);
					minfo = applyAllMutantsToSameFile?
							new MutantInfo(this.request.clazz.replaceAll(Core.SEPARATOR, "."), (method==null?"fieldMutations":method), mutatedFile.getPath(), digest/*dos.getMessageDigest().digest()*/)
							:
							new MutantInfo(this.request.clazz.replaceAll(Core.SEPARATOR, "."), (method==null?"fieldMutations":method), mutatedFile.getPath(), mutatedLine, mi.getMutOp(), digest/*dos.getMessageDigest().digest()*/, mi)
							;
				}
				
				if ((applyAllMutantsToSameFile && minfo != null)) {
					minfo.addMutatedLine(mi.getAffectedLine());//minfo.addMutatedLine(mutatedLine);
					minfo.addMutantOperator(mi.getMutOp());
					minfo.addMutation(mi);
				}
				
				if (!applyAllMutantsToSameFile) {
					if (this.queue != null) this.queue.put(minfo);
				}
				res.add(minfo);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ParseTreeException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (pw != null && !applyAllMutantsToSameFile) {
					pw.close();
				}
			}
			Mutator.index++;
		}
		if (applyAllMutantsToSameFile && pw != null) pw.close();
		mutantsFolders.put(mutantID, mF);
		//|--------------------Mutants to file loop--------------------|
		if (!applyAllMutantsToSameFile) Mutator.index = 0;
		return res;
	}
	
	public void run() {
		try {
			this.generateMutants();
		} catch (ClassNotFoundException e) {
			this.cnferror = e;
		} catch (OpenJavaException e) {
			this.ojerror = e;
		}
		
	}



}
