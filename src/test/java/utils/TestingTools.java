package test.java.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import openjava.ptree.ParseTreeException;

import mujava.OpenJavaException;
import mujava.api.MutationOperator;
import mujava.api.Mutation;
import mujava.api.MutantsInformationHolder;
import mujava.app.MutantInfo;
import mujava.app.MutationRequest;
import mujava.app.Mutator;
import mujava.util.JustCodeDigest;

public class TestingTools {

	private static boolean verbose = false;
	public final static int NO_MUTANTS_EXPECTED = -1;
	public final static List<Pattern> NO_PATTERN_EXPECTED = null;
	public final static List<MutationExpected> NO_MUTATIONS_EXPECTED = null;
	static private String basePathOriginals = "test/";
	static private String basePathMutants = "test/mutantExamples/";
	static private Mutator mutator = new Mutator();
	
	public static void changeBasePathOriginals(String newPath) {
		basePathOriginals = newPath;
	}
	
	public static void changeBasePathMutants(String newPath) {
		basePathMutants = newPath;
	}
	
	public static String currentBasePathOriginals() {
		return basePathOriginals;
	}
	
	public static String currentBasePathMutants() {
		return basePathMutants;
	}
	
	public static void setVerbose(boolean enable) {
		verbose = enable;
	}
	
	public static List<MutantInfo> generateMutants(Property p, boolean writeAllToSameFile) {
		List<MutantInfo> mutantsInfo = new LinkedList<MutantInfo>();
		try {
			MutationOperator[] reqOps = p.ops!=null?p.ops.toArray(new MutationOperator[p.ops.size()]):new MutationOperator[]{p.op};
			MutationRequest request = new MutationRequest(	p.clazz,
															new String[] {p.method},
															reqOps,
															"test/",
															"test/mutantExamples/");
			//modified to allow filtering
			mutator.setRequest(request);
			Map<String, MutantsInformationHolder> mutationsPerMethod = mutator.obtainMutants();
			if (p.filter != null) {
				for (Entry<String, MutantsInformationHolder> entry : mutationsPerMethod.entrySet()) {
					p.filter.filterMutations(entry.getValue());
				}
			}
			for (Entry<String, MutantsInformationHolder> mutations : mutationsPerMethod.entrySet()) {
				mutantsInfo.addAll(mutator.writeMutants(p.method, mutations.getValue(), writeAllToSameFile));
			}
			//---------------------------
			//mutantsInfo = mutator.generateMutants(request); //old code
			mutator.resetMutantFolders();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (OpenJavaException e) {
			e.printStackTrace();
		} catch (ParseTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mutantsInfo;
	}
	
	public static List<MutantInfo> generateMutants(Property p) {
		return generateMutants(p, p.writeAllToSameFile);
	}
	
	public static Map<String, MutantsInformationHolder> generateMutations(Property p) {
		try {
			MutationRequest request = new MutationRequest(	p.clazz,
					new String[] {p.method},
					p.ops.toArray(new MutationOperator[p.ops.size()]),
					"test/",
					"test/mutantExamples/");
			mutator.setRequest(request);
			Map<String, MutantsInformationHolder> mutationsPerMethod = mutator.obtainMutants();
			if (p.filter != null) {
				for (Entry<String, MutantsInformationHolder> entry : mutationsPerMethod.entrySet()) {
					p.filter.filterMutations(entry.getValue());
				}
			}
			mutator.resetMutantFolders();
			return mutationsPerMethod;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (OpenJavaException e) {
			e.printStackTrace();
			return null;
		} catch (ParseTreeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Map<String, MutantsInformationHolder> generateAndMergeMutations(Property prop) {
		Map<String, MutantsInformationHolder> mutations = generateMutations(prop);
		if (mutations != null) {
			MutantsInformationHolder mutationsForSelectedMethod = mutations.get(prop.method);
			List<Mutation> muts = mutationsForSelectedMethod.getMutantsIdentifiers();
			try {
				Mutation merged = Mutator.merge(muts);
				muts.clear();
				if (merged != null) {
					muts.add(merged);
				}
			} catch (ParseTreeException e) {
				e.printStackTrace();
			}
		}
		return mutations;
	}
	
	////////////////////////////////////TEST METHODS
	
	static public boolean genTest(Property p) {
		try {
			MutationRequest request = new MutationRequest(	p.clazz,
					new String[] {p.method},
					new MutationOperator[] {p.op},
					"test/",
					"test/mutantExamples/");
			mutator.generateMutants(request);
			mutator.resetMutantFolders();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (OpenJavaException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	static public boolean obtainMutations(Property p) {
		if (p.ops == null) return false;
		try {
			MutationRequest request = new MutationRequest(	p.clazz,
					new String[] {p.method},
					p.ops.toArray(new MutationOperator[p.ops.size()]),
					"test/",
					"test/mutantExamples/");
			//modified to allow filtering
			mutator.setRequest(request);
			Map<String, MutantsInformationHolder> mutationsPerMethod = mutator.obtainMutants();
			if (p.filter != null) {
				for (Entry<String, MutantsInformationHolder> entry : mutationsPerMethod.entrySet()) {
					p.filter.filterMutations(entry.getValue());
				}
			}
			for (Entry<String, MutantsInformationHolder> mutations : mutationsPerMethod.entrySet()) {
				System.out.println("Mutations for method: " + mutations.getKey());
				for (Mutation mi : mutations.getValue().getMutantsIdentifiers()) {
					System.out.println("Operator: " + mi.getMutOp());
					System.out.println("Modify one line inside method: " + mi.isOneLineInMethodOp());
					System.out.println("Affected line inside method: " + mi.getAffectedLine());
					System.out.println("Original node: " + mi.getOriginal().toFlattenString());
					System.out.println("Mutated node: " + mi.getMutant().toFlattenString());
					System.out.println("Is guard mutation: " + mi.isGuardMutation());
					System.out.println("mutGenLimit line: " + mi.getMutGenLimitLine());
					boolean originalNodeIsChainedExpression = mi.isChainedExpression(Mutation.TARGET.ORIGINAL);
					boolean mutantNodeIsChainedExpression = mi.isChainedExpression(Mutation.TARGET.MUTANT);
					System.out.println("Original node is a chained expression: " + originalNodeIsChainedExpression);
					System.out.println("MutationOperator node is a chained expression: " + mutantNodeIsChainedExpression);
					String mutantNodeIsATailModificationOfOriginalNode = "N/A";
					if (originalNodeIsChainedExpression && mutantNodeIsChainedExpression) {
						mutantNodeIsATailModificationOfOriginalNode = Boolean.toString(mi.isMutantTailChangeOfOriginal());
					}
					System.out.println("MutationOperator node is a tail modification of original node: " + mutantNodeIsATailModificationOfOriginalNode);
					System.out.println("MutationOperator affects a variable declaration expression: " + mi.isVariableDeclarationMutation());
					System.out.println("MutationOperator affects an assignment expression:" + mi.isAssignmentMutation());
					System.out.println("\tMutant affects left side of an assignment expression:" + mi.isAssignmentMutationLeft());
					System.out.println("\t\tMutant affects left side of an assignment expression and the left side is a Variable:" + mi.isAssignmentMutationLeftVariable());
					System.out.println("\t\tMutant affects left side of an assignment expression and the left side is a FieldAccess:" + mi.isAssignmentMutationLeftFieldAccess());
					System.out.println("\t\tMutant affects left side of an assignment expression and the left side is an ArrayAccess:" + mi.isAssignmentMutationLeftArrayAccess());
					System.out.println("\tMutant affects right side of an assignment expression:" + mi.isAssignmentMutationRight());
				}
			}
			mutator.resetMutantFolders();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (OpenJavaException e) {
			e.printStackTrace();
			return false;
		} catch (ParseTreeException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	static public boolean testCorrectLinesAffected(Property prop, Map<String, MutantsInformationHolder> mutants) {
		if (!prop.testingObtainingMutants) {
			if (verbose) System.out.println("Property is not build for this test, Property#testingObtainingMutants is not true");
			return false;
		}
		if (prop.affectedLinesExpected == null) {
			if (verbose) System.out.println("Property is not correctly build for this test, Property#affectedLinesExpected is null");
			return false;
		}
		if (mutants.size() != 1) {
			if (verbose) System.out.println("Expected mutations only for one method, got mutations for " + mutants.size() + " methods");
			return false;
		}
		if (!mutants.containsKey(prop.method)) {
			if (verbose) System.out.println("Expected mutations for method " + prop.method);
			return false;
		}
		List<Mutation> mutations = mutants.get(prop.method).getMutantsIdentifiers();
		boolean[] affectedLinesFound = new boolean[prop.affectedLinesExpected.size()];
		Arrays.fill(affectedLinesFound, false);
		for (int i = 0; i < affectedLinesFound.length; i++) {
			int affectedLine = prop.affectedLinesExpected.get(i);
			for (Mutation mi : mutations) {
				if (mi.isOneLineInMethodOp() && mi.getAffectedLine() == affectedLine) {
					affectedLinesFound[i] = true;
					if (verbose) System.out.println("Affected line " + affectedLine + " found");
					break;
				}
			}
		}
		return arrayAnd(affectedLinesFound);
	}
	
	static public boolean testCorrectMutationsObtained(Property prop, Map<String, MutantsInformationHolder> mutants) {
		if (prop.mutationsExpected == TestingTools.NO_MUTATIONS_EXPECTED) return true;
		if (!prop.testingObtainingMutants) {
			if (verbose) System.out.println("Property is not build for this test, Property#testingObtainingMutants is not true");
			return false;
		}
		if (prop.mutationsExpected == null) {
			if (verbose) System.out.println("Property is not correctly build for this test, Property#mutantCodeExpected is null");
			return false;
		}
		if (mutants.size() != 1) {
			if (verbose) System.out.println("Expected mutations only for one method, got mutations for " + mutants.size() + " methods");
			return false;
		}
		if (!mutants.containsKey(prop.method)) {
			if (verbose) System.out.println("Expected mutations for method " + prop.method);
			return false;
		}
		List<Mutation> mutations = mutants.get(prop.method).getMutantsIdentifiers();
		if (prop.mutationsExpected.isEmpty()) {
			return mutations.isEmpty();
		}
//		boolean[] mutantsFound = new boolean[prop.mutationsExpected.size()];
//		Arrays.fill(mutantsFound, false);
		boolean allFound = true;
		Iterator<MutationExpected> mutExpectedIt = prop.mutationsExpected.iterator();
		while (mutExpectedIt.hasNext()) {
			MutationExpected me = mutExpectedIt.next();
			boolean found = false;
			for (Mutation mi : mutations) {
				if (me.compareExpectedWithObtained(mi.getOriginal().toFlattenString(), mi.getMutant().toFlattenString(), mi.getAffectedLine())) {
					if (verbose) System.out.println("mutation " + me.toString() + " found");
					found = true;
					break;
				}
			}
			if (!found) {
				if (verbose) System.out.println("mutation " + me.toString() + " not found");
				allFound = false;
			}
		}
//		List<Integer> mutationsIndexes = new LinkedList<Integer>(); //contains the indexes of the mutations expected, when one is found the corresponding index is removed
//		for (int ci = 0; ci < prop.mutationsExpected.size(); ci++) {
//			mutationsIndexes.add(ci);
//		}
//		for (int i = 0; i < mutantsFound.length; i++) {
//			MutationExpected me = prop.mutationsExpected.get(i);
//			int current = 0;
//			while (current < mutationsIndexes.size()) {
//				Integer currentIdx = mutationsIndexes.get(current);
//				Mutation mi = mutations.get(currentIdx);
//				if (me.compareExpectedWithObtained(mi.getOriginal().toFlattenString(), mi.getMutant().toFlattenString(), mi.getAffectedLine())) {
//					mutationsIndexes.remove(current);
//					mutantsFound[i] = true;
//					if (verbose) System.out.println("mutation " + me.toString() + " found");
//					break;
//				}
//				current++;
//			}
//		}
//		return arrayAnd(mutantsFound);
		return allFound;
	}
	
	static public boolean testMutationsShouldBeMerged(Property prop, List<Mutation> mutations) {
		if (!prop.testingMutationMerging) {
			if (verbose) System.out.println("Property is not build for this test, Property#testingMutationMerging is not true");
			return false;
		}
		boolean resultExpected;
		try {
			resultExpected = Mutator.checkMergingCompatibility(mutations) == prop.canBeMerged;
		} catch (ParseTreeException e) {
			e.printStackTrace();
			return false;
		}
		if (verbose && resultExpected) {
			System.out.println("the following mutations:");
			for (Mutation mi : mutations) {
				System.out.println(mi.toString());
			}
			System.out.println((prop.canBeMerged?"can be merged":"can't be merged") + " as expected");
		}
		return resultExpected;
	}
	
	static public boolean testThatMutantsCompile(Property prop, List<MutantInfo> mutantsInfo) {
		if (prop.compilingMutantsExpected == NO_MUTANTS_EXPECTED) {
			return true;
		}
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
    	int result = 0;
    	int mutantsCompiled = 0;
    	String[] params = new String[4]; //class to compile | -classpath | classpath
    	params[1] = "-Xlint:none";
    	params[2] = "-classpath";
    	params[3] = basePathOriginals;
    	FileOutputStream compileTestLog = null;
		try {
			compileTestLog = new FileOutputStream("compileTestLog.log", true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		for (MutantInfo mi : mutantsInfo) {
    		File mutant = new File(mi.getPath());
    		if (!mutant.exists()) {
    			if (verbose) System.out.println("testThatMutantsCompile\nmutant: " + mutant.getPath() + " doesn't exist\n");
    			return false;
    		}
    		params[0] = mutant.getPath();
        	result = javac.run(null, null, compileTestLog, params);
        	if (result != 0) {if (verbose) System.out.println("Class : " + params[0] + " didn't compile, read compileTestLog.log for details\n");}
        	else mutantsCompiled++;
        	if (prop.writeAllToSameFile) break;
		}
		if (verbose) System.out.println("Class : "+prop.clazz + " compiling mutants : (expected :" + prop.compilingMutantsExpected + " | obtained : " + mutantsCompiled +")\n");
    	try {
			compileTestLog.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return mutantsCompiled == prop.compilingMutantsExpected;
	}
	
	
	
	
	static public boolean testCorrectNumberOfMutants(Property prop, List<MutantInfo> mutantsInfo) {
		if (prop.mutantsExpected == NO_MUTANTS_EXPECTED) {
			return true;
		}
		Map<String, Integer> checksums = new TreeMap<String, Integer>();
		for (MutantInfo mi : mutantsInfo) {
			File mutant = new File(mi.getPath());
			if (mutant.exists() && mutant.isFile()) {
				String md5cs = checksum(mutant);
				if (verbose) System.out.println("File : "+mutant.getName()+" | md5 : "+md5cs+'\n');
				if (!checksums.containsKey(md5cs)) {
					checksums.put(md5cs, 1);
				} else {
					int currValue = checksums.get(md5cs);
					checksums.put(md5cs, currValue+1);
				}
			} else {
				if (verbose)  System.out.println("testCorrectNumberOfMutants\nmutant: " + mutant.getPath() + " doesn't exist\n");
				return false;
			}
			if (prop.writeAllToSameFile) break;
		}
		if (verbose) System.out.println("testCorrectNumberOfMutants: expected : " + prop.mutantsExpected + " | obtained : " + checksums.size());
		if (verbose) {
			for (Entry<String, Integer> cs : checksums.entrySet()) {
				System.out.println("hash : " + cs.getKey() + " | repeats : " + cs.getValue());
			}
		}
		return checksums.size() == prop.mutantsExpected;
	}
	
	static public boolean testExpectedMutantsFound(Property prop, List<MutantInfo> mutantsInfo) {
		if (prop.mutantCodeExpected==NO_PATTERN_EXPECTED) {
			return true;
		}
		boolean[] mutantCodeFound = new boolean[prop.mutantCodeExpected.size()];
		Arrays.fill(mutantCodeFound, false);
		int i = 0;
		for (MutantInfo mi : mutantsInfo) {
			File mutant = new File(mi.getPath());
			if (mutant.exists() && mutant.isFile()) {
				String fileAsString = readFile(mutant);
				while (i < prop.mutantCodeExpected.size()) {
					if (mutantCodeFound[i]) {
						i++;
						continue;
					}
					if (prop.mutantCodeExpected.get(i).matcher(fileAsString).find()) {
						if (verbose)  System.out.println(prop.mutantCodeExpected.get(i)+" found(as expected)\n");
						mutantCodeFound[i] = true;
					}
					i++;
				}
				i = 0;
			} else {
				if (verbose)  System.out.println("testExpectedMutantsFound\nmutant: " + mutant.getPath() + " doesn't exist\n");
				return false;
			}
			if (prop.writeAllToSameFile) break;
		}
		for (int nf = 0; nf < mutantCodeFound.length && verbose; nf++) {
			if (!mutantCodeFound[nf]) {
				System.out.println(prop.mutantCodeExpected.get(nf)+" not found\n");
			}
		}
		return arrayAnd(mutantCodeFound);
	}
	
	static public boolean testUnexpectedMutantsNotFound(Property prop, List<MutantInfo> mutantsInfo) {
		if (prop.mutantCodeNotExpected==NO_PATTERN_EXPECTED) {
			return true;
		}
		int i = 0;
		for (MutantInfo mi : mutantsInfo) {
			File mutant = new File(mi.getPath());
			if (mutant.exists() && mutant.isFile()) {
				boolean found = false;
				String fileAsString = readFile(mutant);
				while (!found && i < prop.mutantCodeNotExpected.size()) {
					if (prop.mutantCodeNotExpected.get(i).matcher(fileAsString).find()) {
						if (verbose)  System.out.println(prop.mutantCodeNotExpected.get(i)+" found(but not expected)\n");
						if (verbose)  System.out.println("unexpected mutant found in : " + mi.getPath());
						if (verbose)  System.out.println("unexpected mutant generated by : ");
						if (verbose)  printMutation(mi);
						found = true;
					}
					i++;
				}
				if (found) return false;
				i = 0;
			} else {
				if (verbose)  System.out.println("testUnexpectedMutantsNotFound\nmutant: " + mutant.getPath() + " doesn't exist\n");
				return false;
			}
			if (prop.writeAllToSameFile) break;
		}
		return true;
	}
	
	static public boolean testMD5Hash(List<MutantInfo> mutantsInfo) {
		for (MutantInfo mi : mutantsInfo) {
			File mutant = new File(mi.getPath());
			String calculatedMD5hash = checksum(mutant);
			String mutantMD5hash = convertByteArrayToHexString(mi.getMD5digest());
			if (calculatedMD5hash.compareTo(mutantMD5hash) != 0) {
				return false;
			}
		}
		return true;
	}
	
	
	////////////////////////////////////TEST METHODS
	
	
	
	
	////////////////////////////////////AUXILIARY METHODS
	
	
	static private String readFile(File f) {
		String result = null;
		FileReader fr = null;
		try {
			fr = new FileReader(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	static private boolean arrayAnd(boolean[] a) {
		boolean result = true;
		for (boolean b : a) {
			result &= b;
			if (!result) {
				break;
			}
		}
		return result;
	}
	
	
	private static void printMutation(MutantInfo mi) {
		if (mi.severalMutationsApplied()) {
			System.out.println("Several mutations used:");
			for (Mutation mutation : mi.getMutations()) {
				System.out.println(mutation.toString());
			}
		} else {
			System.out.println(mi.getMutation().toString());
		}
	}
	
	
	
	
	static private String checksum(File f) {
	    byte[] mdbytes = JustCodeDigest.digest(f);
	    if (verbose) System.out.println("md5hash for file " + f.getPath() + " : " + Arrays.toString(mdbytes));
	    String byteArrayAsHexString = convertByteArrayToHexString(mdbytes);
	    return byteArrayAsHexString;
	}
	
	static private String convertByteArrayToHexString(byte[] byteArray) {
		//convert the byte to hex format
	    StringBuffer sb = new StringBuffer("");
	    for (int i = 0; i < byteArray.length; i++) {
	    	sb.append(Integer.toString((byteArray[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    return sb.toString();
	}
	
	
	////////////////////////////////////AUXILIARY METHODS

}
