package mujava.app;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import mujava.api.Api;
import mujava.api.Configuration;
import mujava.api.Mutant;
import mujava.op.PRVO;
import mujava.util.ConfigReader;



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
	static int generations = 1;
	
	public static void main(String[] args) {
		
		if (args.length > 1 && args[0] != "--args") {
			System.err.println("usage : mujava.app.Console [<path to .properties file>]");
		}
		
		if (args.length == 0 || args.length == 1) args = (args.length == 1?ConfigReader.getInstance(args[0]):ConfigReader.getInstance()).configAsArgs();
		
		if (args.length > 1 && args[0] == "--args") Arrays.copyOfRange(args, 1, args.length);
		
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
		flags.setOptionalFlag('P'); //define allowed packages to mark as reloadable by Reloader
		flags.setNoValueFlag('A'); //ignores mutGenLimit annotations 
		flags.setNoValueFlag('L'); //allows the use of numeric literal variations in PRVO
		flags.setNoValueFlag('Q'); //stop at the first failing test for each mutant
		flags.setNoValueFlag('V'); //enable full verbose mode
		flags.setNoValueFlag('p'); //disable mutations of the form a = b where a is of type Object and b is a primitive type expression
		flags.setNoValueFlag('w'); //wrap mutations of the form a = b to a = new T(b), where a is of type Object and b is a primitive type expression
		flags.setNoValueFlag('r'); //apply refined versions of PRVO to mutate arguments in statements containing only a method call
		flags.setOptionalFlag('g'); //define how many mutants generations to generate
		flags.setNoValueFlag('W'); //show surviving mutants
		flags.setDependence('T', 'S');
		flags.setDependence('S', 'T');
		flags.setDependence('t', 'T');
		flags.setDependence('x', 'B');
		flags.setDependence('N', 'm');
		flags.setDependence('s', 'B');
		flags.setDependence('F', 'm');
		flags.setDependence('C', 'm');
		flags.setDependence('P', 'm');
		flags.setDependence('A', 'm');
		flags.setDependence('L', 'm');
		flags.setDependence('Q', 'm');
		flags.setDependence('p', 'm');
		flags.setDependence('w', 'm');
		flags.setDependence('r', 'm');
		flags.setDependence('W', 'S');
		
		
		System.out.println("Validating parameters...");
		//================================validate input================================================//
		if (!flags.validateInput(args)) {
			System.err.println("Invalid Params");
			System.err.println("To get help on how to run mujava++ please run with only flag -H");
			return;
		}
		
		mutationScore = flags.flagExist('S');
		System.out.println(mutationScore?"Will calculate mutation score":"");
		
		//===========================validate mutation operators========================================//
		List<Mutant> ops = new LinkedList<Mutant>();
		if (flags.flagExist('x')) {
			for (String op : flags.getFlagValues('x')) {
				if (!mi.isSupported(Mutant.valueOf(op))) {
					System.err.println("Mutation operator ("+op+") is unsupported");
					System.err.println("To see a list with all mutation operators currently supported run mujava++ with only flag -h");
					return;
				}
				ops.add(Mutant.valueOf(op));
			}
		} else {
			ops.addAll(mi.allOps());
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
			System.err.println("-M flag must have only one value");
			return;
		}
		if (verifyDirectory(mutDir.get(0))) {
			System.out.println("Warning : mutants output directory ("+mutDir.get(0)+") already exist");
			deleteDirectory(mutDir.get(0));
		}
		mutantsSourceDir = mutDir.get(0);
		
		System.out.println("Output dir: "+mutantsSourceDir);
		
		//original source dir
		List<String> sourceDir = flags.getFlagValues('O');
		if (sourceDir.size() != 1) {
			System.err.println("-O flag must have only one value");
			return;
		}
		if (!verifyDirectory(sourceDir.get(0))) {
			System.err.println("Original source directory ("+sourceDir.get(0)+") doesn't exist");
			return;
		}
		originalSourceDir = sourceDir.get(0);
		
		System.out.println("Original source dir: " + originalSourceDir);
		
		//original bin dir
		List<String> binDir = flags.getFlagValues('B');
		if (binDir.size() != 1) {
			System.err.println("-B flag must have only one value");
			return;
		}
		if (!verifyDirectory(binDir.get(0))) {
			System.err.println("Original bin directory ("+binDir.get(0)+") doesn't exist");
			return;
		}
		originalBinDir = binDir.get(0);
	
		System.out.println("Original bin dir: " + originalBinDir);
		
		//tests source dir
		if (mutationScore) {
			List<String> testDir = flags.getFlagValues('T');
			if (testDir.size() != 1) {
				System.err.println("-T flag must have only one value");
				return;
			}
			if (!verifyDirectory(testDir.get(0))) {
				System.err.println("Tests directory ("+testDir.get(0)+") doesn't exist");
				return;
			}
			testBinDir = testDir.get(0);
			
			System.out.println("Tests bin dir: " + testBinDir);
		}
		//=============================verify class to mutate============================================//
		List<String> ctm = flags.getFlagValues('m');
		if (ctm.size() != 1) {
			System.err.println("-m flag must have only one value");
			return;
		}
		System.out.println("\n\nVerifying class to mutate...");
		System.out.println("Original source dir: " +originalSourceDir);
		System.out.println("Class to mutate: "+ctm.get(0));
		System.out.println("Separator: "+Core.SEPARATOR);
		System.out.println("Class to mutate as path: "+ctm.get(0).replaceAll("\\.", Core.SEPARATOR));
		System.out.println("Class to mutate full path: "+originalSourceDir+ctm.get(0).replaceAll("\\.", Core.SEPARATOR)+".java\n");
		if (!verifyFile(originalSourceDir+ctm.get(0).replaceAll("\\.", Core.SEPARATOR)+".java")) {
			System.err.println("Class to mutate ("+(originalSourceDir+ctm.get(0).replaceAll("\\.", Core.SEPARATOR)+".java")+") doesn't exist");
			return;
		}
		classToMutate = ctm.get(0);
		
		System.out.println("Class to mutate: "+ classToMutate);
		
		//============================verify test classes================================================//
		if (mutationScore) {
			List<String> tclasses = flags.getFlagValues('t');
			for (String tc : tclasses) {
				if (!verifyFile(testBinDir+tc.replaceAll("\\.", Core.SEPARATOR)+".class")) {
					System.err.println("Test class ("+(testBinDir+tc.replaceAll("\\.", Core.SEPARATOR)+".class")+") doesn't exist");
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
					System.err.println("No such method ("+m+") on class to mutate ("+classToMutate+")");
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
		
		//================================ALLOWED PACKAGES TO RELOAD======================================//
		
		Set<String> allowedPackages = null;
		if(flags.flagExist('P')) {
			allowedPackages = new TreeSet<String>();
			allowedPackages.addAll(flags.getFlagValues('P'));
			MutationScore.allowedPackages = allowedPackages;
		}
		System.out.print("Allowed packages to mark as reloadable: ");
		if (allowedPackages == null) {
			System.out.println("all");
		} else {
			String allowedPackagesAsString = "[";
			Iterator<String> it = allowedPackages.iterator();
			while(it.hasNext()) {
				allowedPackagesAsString += it.next();
				if (it.hasNext()) {
					allowedPackagesAsString += ", ";
				}
			}
			allowedPackagesAsString += "]";
			System.out.println(allowedPackagesAsString);
		}
		
		//================================IGNORE MUTGENLIMIT ANNOTATIONS==================================//
		
		if (flags.flagExist('A')) {
			System.out.println("Ignoring mutGenLimit annotations");
			Configuration.add(Api.USE_MUTGENLIMIT, Boolean.FALSE);
		}
		
		//==========================ALLOW NUMERIC LITERAL VARIATIONS IN PRVO==============================//
		
		if (flags.flagExist('L')) {
			System.out.println("Allow numeric literal variations in PRVO");
			Configuration.add(PRVO.ENABLE_NUMBER_LITERALS_VARIATIONS, Boolean.TRUE);
		}
		
		if (flags.flagExist('Q')) {
			System.out.println("Quick death enabled");
			MutationScore.quickDeath = true;
		} else {
			System.out.println("Quick death disabled");
			MutationScore.quickDeath = false;
		}
		
		if (flags.flagExist('V')) {
			System.out.println("Full verbose mode enabled");
			Core.fullVerbose = true;
		} else {
			Core.fullVerbose = false;
		}
		
		if (flags.flagExist('p')) {
			System.out.println("Primitive to Object assignments are disabled");
			Configuration.add(PRVO.ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS, Boolean.FALSE);
		} else {
			Configuration.add(PRVO.ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS, Boolean.TRUE);
		}
		
		if (flags.flagExist('p')) {
			System.out.println("Primitive to Object assignments will be wrapped");
			Configuration.add(PRVO.ENABLE_PRIMITIVE_WRAPPING, Boolean.TRUE);
		} else {
			Configuration.add(PRVO.ENABLE_PRIMITIVE_WRAPPING, Boolean.FALSE);
		}
		
		if (flags.flagExist('r')) {
			System.out.println("Apply PRVOX_REFINED to mutate arguments in statements containing only a method call");
			Configuration.add(Configuration.ENABLE_REFINEMENT_IN_METHOD_CALL_STATEMENTS, Boolean.TRUE);
		} else {
			Configuration.add(Configuration.ENABLE_REFINEMENT_IN_METHOD_CALL_STATEMENTS, Boolean.FALSE);
		}
		
		if (flags.flagExist('g')) {
			List<String> valuesForGeneration = flags.getFlagValues('g');
			if (valuesForGeneration.size() != 1) {
				System.err.println("Only one value is accepted for flag -g (values given : " + valuesForGeneration.size() + ")");
				return;
			}
			generations = Integer.valueOf(valuesForGeneration.get(0));
			if (generations <= 0) {
				System.err.println("Can't use a value <= 0 for generations (value used : " + generations + ")");
				return;
			}
		} else {
			generations = 1;
		}
		System.out.println("Generations to generate: " + generations);
		
		if (flags.flagExist('W')) {
			System.out.println("Showing surviving mutants at the end of mutation score");
			Core.showSurvivingMutants = true;
		} else {
			Core.showSurvivingMutants = false;
		}
		
		
		System.out.println("Parameters validated\n\n");
		
		//================================Mutants generation==============================================//
		System.out.println("Generating mutants...\n");
		//List<Mutant> basicMutants = mi.listBasicOperators();
		boolean result = core.generateMutants(classToMutate, methodsToMutate, ops.toArray(new Mutant[ops.size()]), generations);
		if (!result) {
			core.lastError().printStackTrace(System.err);
		} else {
			System.out.println(core.lastMutantsFolder().toString());
		}
		System.out.println("\nMutants generated\n\n");
		
		//==============================Mutation score====================================================//;
		if (mutationScore && core.lastMutantsFolder() != null && !core.lastMutantsFolder().isEmpty()) {
			System.out.println("Calculating mutation score for generation (" + generations + ")\n");
			MutationScore ms = MutationScore.newInstance(mutantsSourceDir, originalBinDir, testBinDir);
			core.setMutationScore(ms);
			float mutationScoreResult = core.calculateMutationScore(testClasses, classToMutate);
			System.out.println(classToMutate + " scored : " + mutationScoreResult + " with tests : " + Arrays.asList(testClasses).toString());
			Core.killStillRunningJUnitTestcaseThreads();
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
		System.out.println("-N <methods>					| optional parameter | requires : -m		| effect : define methods that will not be used by PRVO while generating mutants e.g: -N toString getClass");
		System.out.println("-J <fields>						| optional parameter | required : -m		| effect : define fields that will not be used by PRVO while generating mutants e.g: -J serialID");
		System.out.println("-P <packages>					| optional parameter | required : -m		| effect : define allowed packages to mark as reloadable by Reloader e.g: -P main.ui.tools");
		System.out.println("-A								| optional parameter | required : -m		| effect : ignores mutGenLimit annotations and mutates anywhere a mutations operator can");
		System.out.println("-L								| optional parameter | required : -m		| effect : allows the use of numeric literal variations in PRVO");
		System.out.println("-Q								| optional parameter | required : -m		| effect : stop at the first failing test for each mutant");
		System.out.println("-V								| optional parameter | effect : enable full verbose mode");
		System.out.println("-p								| optional parameter | effect : disable mutations of the form a = b where a is of type Object and b is a primitive type expression");
		System.out.println("-w								| optional parameter | effect : wrap mutations of the form a = b to a = new T(b), where a is of type Object and b is a primitive type expression");
		System.out.println("-r								| optional parameter | effect : apply refined versions of PRVO to mutate arguments in statements containing only a method call");
		System.out.println("-g <generations>				| optional parameter | effect : generate <generations> of mutants | e.g.: -g 2 will generate the first and second generations of mutants");
		System.out.println("-W								| optional parameter | required : -S | effect : shows which mutants that compile and were not killed by any test");
	}
	
	private static void mutopsHelp() {
		System.out.println("Mutation Operators");
		mi.showInfo();
	}
	
	private static boolean verifyDirectory(String dir) {
		File directory = new File(dir);
		return directory.exists() && directory.isDirectory();
	}
	
	private static void deleteDirectory(String dir) {
		File directory = new File(dir);
		if (directory.exists() && directory.isDirectory()) {
			deleteDirRecursively(directory);
		}
	}
	
	private static void deleteDirRecursively(File file){
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	        	deleteDirRecursively(f);
	        }
	    }
	    file.delete();
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
