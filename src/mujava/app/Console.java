package mujava.app;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mujava.api.Configuration;
import mujava.api.Mutant;
import mujava.op.PRVO;



public class Console {
	
	static MutatorsInfo mi = MutatorsInfo.getInstance();
	static Core core;
	static List<Method> ctmMethods = null;
	
	static String mutantsSourceDir = "output/";
	static String classToMutate = "utils.BooleanOps";
	static String originalSourceDir = "test/";
	static String originalBinDir = "bin/";
	static String testBinDir = "bin/";
	static String[] methodsToMutate = null;
	static boolean mutationScore = false;
	static String[] testClasses = new String[]{"mutationScore.BooleanOpsAndTests", "mutationScore.BooleanOpsXorTests", "mutationScore.BooleanOpsXnorTests", "mutationScore.BooleanOpsOrTests"};

	public static void main(String[] args) {
		
		
		//=======================set required and optional flags=======================================//
		Flags flags = new Flags(System.err);
		flags.setRequiredFlag('M'); //mutantsSourceDir
		flags.setRequiredFlag('m'); //classToMutate
		flags.setRequiredFlag('O'); //originalSourceDir
		flags.setRequiredFlag('B'); //originalBinDir
		flags.setNoValueFlag('S'); //calculate mutations score
		flags.setOptionalFlag('T'); //testBinDir
		flags.setOptionalFlag('t'); //testsClasses
		flags.setOptionalFlag('s'); //methods to mutate
		flags.setOptionalFlag('x'); //mutation operators
		flags.setOverridingFlag('H'); //show full info (help)
		flags.setOverridingFlag('h'); //show all mutation operators
		flags.setNoValueFlag('F'); //activates field mutations
		flags.setNoValueFlag('C'); //activates class mutations
		flags.setOptionalFlag('N'); //define banned methods for PRVO
		flags.setOptionalFlag('J'); //define banned fields for PRVO
		flags.setDependence('T', 'S');
		flags.setDependence('S', 'T');
		flags.setDependence('t', 'T');
		flags.setDependence('x', 'B');
		flags.setDependence('N', 'm');
		flags.setDependence('s', 'B');
		flags.setDependence('F', 'm');
		flags.setDependence('C', 'm');
		
		
		System.out.println("Validating parameters...");
		//================================validate input================================================//
		if (!flags.validateInput(args)) {
			System.out.println("To get help on how to run mujava++ please run with only flag -H");
			return;
		}
		
		mutationScore = flags.flagExist('S');
		System.out.println(mutationScore?"Will calculate mutation score":"");
		
		//===========================validate mutation operators========================================//
		List<Mutant> ops = new LinkedList<Mutant>();
		if (flags.flagExist('x')) {
			for (String op : flags.getFlagValues('x')) {
				if (!mi.isSupported(Mutant.valueOf(op))) {
					System.out.println("Mutation operator ("+op+") is unsupported");
					System.out.println("To see a list with all mutation operators currently supported run mujava++ with only flag -h");
					return;
				}
				ops.add(Mutant.valueOf(op));
			}
		} else {
			ops.addAll(mi.listBasicOperators());
		}
		System.out.println("Operators: " + ops.toString());
		
		//====================================full help================================================//
		if (flags.overridingFlagFound() && flags.flagExist('H')) {
			fullHelp();
			return;
		}
		
		//============================mutation operators help==========================================//
		if (flags.overridingFlagFound() && flags.flagExist('h')) {
			mutopsHelp();
			return;
		}
		
		//=============================verify directories==============================================//
		//mutant source dir
		List<String> mutDir = flags.getFlagValues('M');
		if (mutDir.size() != 1) {
			System.out.println("-M flag must have only one value");
			return;
		}
		if (verifyDirectory(mutDir.get(0))) {
			System.out.println("Warning : mutants output directory ("+mutDir.get(0)+") already exist");
		}
		mutantsSourceDir = mutDir.get(0);
		
		System.out.println("Output dir: "+mutantsSourceDir);
		
		//original source dir
		List<String> sourceDir = flags.getFlagValues('O');
		if (sourceDir.size() != 1) {
			System.out.println("-O flag must have only one value");
			return;
		}
		if (!verifyDirectory(sourceDir.get(0))) {
			System.out.println("Original source directory ("+sourceDir.get(0)+") doesn't exist");
			return;
		}
		originalSourceDir = sourceDir.get(0);
		
		System.out.println("Original source dir: " + originalSourceDir);
		
		//original bin dir
		List<String> binDir = flags.getFlagValues('B');
		if (binDir.size() != 1) {
			System.out.println("-B flag must have only one value");
			return;
		}
		if (!verifyDirectory(binDir.get(0))) {
			System.out.println("Original bin directory ("+binDir.get(0)+") doesn't exist");
			return;
		}
		originalBinDir = binDir.get(0);
	
		System.out.println("Original bin dir: " + originalBinDir);
		
		//tests source dir
		if (mutationScore) {
			List<String> testDir = flags.getFlagValues('T');
			if (testDir.size() != 1) {
				System.out.println("-T flag must have only one value");
				return;
			}
			if (!verifyDirectory(testDir.get(0))) {
				System.out.println("Tests directory ("+testDir.get(0)+") doesn't exist");
				return;
			}
			testBinDir = testDir.get(0);
			
			System.out.println("Tests bin dir: " + testBinDir);
		}
		//=============================verify class to mutate============================================//
		List<String> ctm = flags.getFlagValues('m');
		if (ctm.size() != 1) {
			System.out.println("-m flag must have only one value");
			return;
		}
		System.out.println("\n\nVerifying class to mutate...");
		System.out.println("Original source dir: " +originalSourceDir);
		System.out.println("Class to mutate: "+ctm.get(0));
		System.out.println("Separator: "+Core.SEPARATOR);
		System.out.println("Class to mutate as path: "+ctm.get(0).replaceAll("\\.", Core.SEPARATOR));
		System.out.println("Class to mutate full path: "+originalSourceDir+ctm.get(0).replaceAll("\\.", Core.SEPARATOR)+".java\n");
		if (!verifyFile(originalSourceDir+ctm.get(0).replaceAll("\\.", Core.SEPARATOR)+".java")) {
			System.out.println("Class to mutate ("+(originalSourceDir+ctm.get(0).replaceAll("\\.", Core.SEPARATOR)+".java")+") doesn't exist");
			return;
		}
		classToMutate = ctm.get(0);
		
		System.out.println("Class to mutate: "+ classToMutate);
		
		//============================verify test classes================================================//
		if (mutationScore) {
			List<String> tclasses = flags.getFlagValues('t');
			for (String tc : tclasses) {
				if (!verifyFile(testBinDir+tc.replaceAll("\\.", Core.SEPARATOR)+".class")) {
					System.out.println("Test class ("+(testBinDir+tc.replaceAll("\\.", Core.SEPARATOR)+".class")+") doesn't exist");
					return;
				}
			}
			testClasses = tclasses.toArray(new String[tclasses.size()]);
			System.out.println("Tests classes: "+stringArrayToString(testClasses));
		}
		
		//so far so good, need to instanciate the core to verify methods
		core = Core.newInstance(originalSourceDir, mutantsSourceDir);
		core.setInputBinDir(originalBinDir);
		
		
		//==============================verify methods to mutate==========================================//
		List<String> mtm = new LinkedList<String>();
		if (flags.flagExist('s')) {
			mtm.addAll(flags.getFlagValues('s'));
			for (String m : mtm) {
				if (!verifyMethod(m)) {
					System.out.println("No such method ("+m+") on class to mutate ("+classToMutate+")");
					return;
				}
			}
			
			System.out.println("Methods to mutate: "+stringListToString(mtm));
		}
		
		if (flags.flagExist('F')) {
			mtm.add(MutationRequest.MUTATE_FIELDS);
			System.out.println("Allow field mutations");
		}
		if (flags.flagExist('C')) {
			mtm.add(MutationRequest.MUTATE_CLASS);
			System.out.println("Allow class mutations");
		}
		methodsToMutate = mtm.toArray(new String[mtm.size()]);
		
		//==================================add banned methods============================================//
		
		List<String> bannedMethods = new LinkedList<String>();
		if (flags.flagExist('N')) {
			bannedMethods.addAll(flags.getFlagValues('N'));
			System.out.print("banned methods: [");
			int i = 0;
			while (i < bannedMethods.size()) {
				System.out.print(bannedMethods.get(i));
				if (i + 1 < bannedMethods.size()) {
					System.out.println(", ");
				}
			}
			System.out.println("]");
		}
		
		Configuration.add(PRVO.PROHIBITED_METHODS, bannedMethods);
		
		List<String> bannedFields = new LinkedList<String>();
		if (flags.flagExist('J')) {
			bannedFields.addAll(flags.getFlagValues('J'));
			System.out.print("banned fields: [");
			int i = 0;
			while (i < bannedFields.size()) {
				System.out.print(bannedFields.get(i));
				if (i + 1 < bannedFields.size()) {
					System.out.println(", ");
				}
			}
			System.out.println("]");
		}
		
		Configuration.add(PRVO.PROHIBITED_FIELDS, bannedFields);
		
		System.out.println("Parameters validated\n\n");
		
		//================================Mutants generation==============================================//
		System.out.println("Generating mutants...\n");
		//List<Mutant> basicMutants = mi.listBasicOperators();
		boolean result = core.generateMutants(classToMutate, methodsToMutate, ops.toArray(new Mutant[ops.size()]));
		if (!result) {
			core.lastError().printStackTrace();
		} else {
			System.out.println(core.lastMutantsFolder().toString());
		}
		System.out.println("\nMutants generated\n\n");
		
		//==============================Mutation score====================================================//;
		if (mutationScore && !core.lastMutantsFolder().isEmpty()) {
			System.out.println("Calculating mutation score\n");
			MutationScore ms = MutationScore.newInstance(mutantsSourceDir, originalBinDir, testBinDir);
			core.setMutationScore(ms);
			float mutationScoreResult = core.calculateMutationScore(testClasses, classToMutate);
			System.out.println(classToMutate + " scored : " + mutationScoreResult + " with tests : " + Arrays.asList(testClasses).toString());
			killStillRunningJUnitTestcaseThreads();
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void killStillRunningJUnitTestcaseThreads() {
	    Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
	    for (Thread thread : threadSet) {
	        if (!(thread.isDaemon())) {
	            final StackTraceElement[] threadStackTrace = thread.getStackTrace();
	            if (threadStackTrace.length > 1) {
	                StackTraceElement firstMethodInvocation = threadStackTrace[threadStackTrace.length - 1];
	                if (firstMethodInvocation.getClassName().startsWith("org.junit")) {
	                    // HACK: must use deprecated method
	                    thread.stop();
	                }
	            }
	        }
	    }
	}
	
	private static void fullHelp() {
		System.out.println("Mujava++");
		System.out.println("Version : "+Core.mujavappVersion);
		System.out.println("Console version");
		System.out.println("Usage : mujava.app.Console <parameters>");
		System.out.println("parameters :");
		System.out.println("-M <mutants output directory> 	| required parameter");
		System.out.println("-m <class to mutate> 			| required parameter | e.g: utils.BooleanOps");
		System.out.println("-O <original source directory> 	| required parameter | e.g: if utils.BooleanOps.java is in directory test then this parameter would be -O test/");
		System.out.println("-B <original bin directory> 	| optional parameter | requires : -T and -t parameters");
		System.out.println("-T <tests classes directory> 	| optional parameter | requires : -B and -t | where the test classes are located");
		System.out.println("-t <test classes> 				| optional parameter | requires : -T");
		System.out.println("-s <methods name to mutate> 	| optional parameter | requires : -B 		| e.g: -s toString evaluate");
		System.out.println("-x <mutations operators to use> | optional parameter | requires : -B 		| e.g: -x COI COR LOR");
		System.out.println("-H 								| optional parameter | can't be used with any other parameter and no value must be passed | effect : show this info");
		System.out.println("-h 								| optional parameter | can't be used with any other parameter and no value must be passed | effect : show all mutation operators available");
		System.out.println("-S								| optional parameter | requires : -T		| effect : calculate mutation score");
		System.out.println("-F								| optional parameter | requires : -m		| effect : enable field mutations");
		System.out.println("-C								| optional parameter | requires : -m		| effect : enable class mutations");
		System.out.println("-N								| optional parameter | requires : -m		| effect : define methods that will not be used by PRVO while generating mutants e.g: -N toString getClass");
		System.out.println("-J								| optional parameter | required : -m		| effect : define fields that will not be used by PRVO while generating mutants e.g: -J serialID");
	}
	
	private static void mutopsHelp() {
		System.out.println("Mutation Operators");
		mi.showInfo();
	}
	
	private static boolean verifyDirectory(String dir) {
		File directory = new File(dir);
		return directory.exists() && directory.isDirectory();
	}
	
	private static boolean verifyFile(String f) {
		File file = new File(f);
		return file.exists() && file.isFile();
	}
	
	private static boolean verifyMethod(String method) {
		if (ctmMethods == null) ctmMethods = core.getMethods(classToMutate);
		boolean found = false;
		for (Method m : ctmMethods) {
			found = m.getName().compareTo(method) == 0;
			if (found) break;
		}
		return found;
	}
	
	private static String stringListToString(List<String> xs) {
		String result = "[";
		int index = 0;
		for (String x : xs) {
			result += x;
			index++;
			if (index < xs.size()) {
				result += ", ";
			}
		}
		result += "]";
		return result;
	}
	
	private static String stringArrayToString(String[] xs) {
		String result = "[";
		int index = 0;
		for (String x : xs) {
			result += x;
			index++;
			if (index < xs.length) {
				result += ", ";
			}
		}
		result += "]";
		return result;
	}
	
}
