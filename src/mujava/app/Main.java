package mujava.app;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.exception.ExceptionUtils;

import mujava.api.Api;
import mujava.api.Configuration;
import mujava.api.MutationOperator;
import mujava.op.BEE;
import mujava.op.PRVO;
import mujava.op.basic.AODS;
import mujava.op.basic.AOIS;
import mujava.op.basic.ROR;
import mujava.op.util.MutantCodeWriter;
import mujava.util.Config;
import mujava.util.ConfigReader;

public class Main {

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
	static boolean useExternalMutants = false;
	
	public static void main(String[] args) {
		
		Options options = new Options();
		
		Option propPathOption = new Option("p", "properties", true, "qualified path e.g.: src/prop.properties or /Users/ppargento/Documents/workspace/stryker/src/prop.properties");
		propPathOption.setRequired(false);
		propPathOption.setArgs(1);
		propPathOption.setType(String.class);
		
		Option operatorsInfoOption = new Option("o", "operators", false, "Shows operators information");
		operatorsInfoOption.setRequired(false);
		
		Option help = new Option("h", "help", false, "print commands");
		help.setRequired(false);
		
		OptionGroup optionsGroup = new OptionGroup();
		optionsGroup.addOption(propPathOption);
		optionsGroup.addOption(operatorsInfoOption);
		optionsGroup.addOption(help);
		
		options.addOptionGroup(optionsGroup);
		
		Config config = null;
		
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption('p')) {
				ConfigReader.getInstance(cmd.getOptionValue('p'));
			}
			if (cmd.hasOption('h')) {
				System.out.println("Mujava++");
				System.out.println("Version : "+ Core.mujavappVersion);
				System.out.println("Console version");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("muJava++", options );
				return;
			}
			if (cmd.hasOption('o')) {
				mutopsHelp();
				return;
			}
		} catch (ParseException e) {
			System.err.println( "Incorrect options.  Reason: " + e.getMessage() );
			return;
		}
		
		config = ConfigReader.getInstance().buildConfig();
		
		System.out.println("Validating parameters...");
		//================================validate input================================================//
		String confValidation = config.validate();
		
		if (confValidation != null) {
			System.err.println("Invalid Configuration");
			System.err.println(confValidation);
			return;
		}
		
		mutationScore = config.runMutationScore();
		System.out.println(mutationScore?"Will calculate mutation score":"");
		useExternalMutants = config.useExternalMutants();
		String[] externalClasses = config.externalClassesToMutate();
		boolean usingPrototypeExternalClasses = externalClasses != null && externalClasses.length > 0;
		System.out.println(useExternalMutants?"Will use external mutants(in mutants directory)":"");
		
		//===========================validate mutation operators========================================//
		List<MutationOperator> ops = new LinkedList<MutationOperator>();
		if (!useExternalMutants) {
			Set<MutationOperator> operators = config.operators();
			if (!operators.isEmpty()) {
				ops.addAll(operators);
			} else {
				ops.addAll(mi.allOps());
			}
			
			System.out.println("Operators: " + ops.toString());
		}
		//=============================verify directories==============================================//
		//mutant source dir
		mutantsSourceDir = config.mutantsOutputFolder();
		if (verifyDirectory(mutantsSourceDir)) {
			System.out.println("Warning : mutants output directory ("+mutantsSourceDir+") already exist");
			deleteDirectory(mutantsSourceDir);
		}
		System.out.println("Mutants dir: "+mutantsSourceDir);
		
		if (!useExternalMutants) {
			//original source dir
			originalSourceDir = config.originalSourceDir();
			if (!verifyDirectory(originalSourceDir)) {
				System.err.println("Original source directory ("+originalSourceDir+") doesn't exist");
				return;
			}
			System.out.println("Original source dir: " + originalSourceDir);
		}
		
		//original bin dir
		originalBinDir = config.originalBinDir();
		if (!verifyDirectory(originalBinDir)) {
			System.err.println("Original bin directory ("+originalBinDir+") doesn't exist");
			return;
		}
		System.out.println("Original bin dir: " + originalBinDir);
		
		//tests source dir
		if (mutationScore) {
			testBinDir = config.testsBinDir();
			if (!verifyDirectory(testBinDir)) {
				System.err.println("Tests directory ("+testBinDir+") doesn't exist");
				return;
			}
			System.out.println("Tests bin dir: " + testBinDir);
		}
		//=============================verify class to mutate============================================//
		if (!useExternalMutants || !usingPrototypeExternalClasses) { 
			classToMutate = config.classToMutate();
			System.out.println("\n\nVerifying class to mutate...");
			if (!useExternalMutants) System.out.println("Original source dir: " +originalSourceDir);
			System.out.println("Class to mutate: "+classToMutate);
			System.out.println("Separator: "+Core.SEPARATOR);
		}
		if (!useExternalMutants) {
			System.out.println("Class to mutate as path: "+classToMutate.replaceAll("\\.", Core.SEPARATOR));
			System.out.println("Class to mutate full path: "+originalSourceDir+classToMutate.replaceAll("\\.", Core.SEPARATOR)+".java\n");
			if (!verifyClassFile(originalSourceDir+classToMutate.replaceAll("\\.", Core.SEPARATOR)+".java")) {
				System.err.println("Class to mutate ("+(originalSourceDir+classToMutate.replaceAll("\\.", Core.SEPARATOR)+".java")+") doesn't exist");
				return;
			}
		}
		if (useExternalMutants && usingPrototypeExternalClasses) {
			System.out.println("External classes (PROTOTYPE) : " + Arrays.toString(externalClasses));
		} else {
			System.out.println("Class to mutate: "+ classToMutate);
		}
		//============================verify test classes================================================//
		if (mutationScore) {
			Set<String> tclasses = config.testClasses();
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
		if (!useExternalMutants) {
			Set<String> mtm = config.methodToMutate();
			if (mtm.isEmpty()) {
				System.out.println("no methods set in configuration, using class methods");
				for (Method m : config.getClassMethods()) {
					mtm.add(m.getName());
				}
			}
			System.out.println("Methods to mutate: "+stringListToString(mtm));
			
			if (config.allowFieldMutations()) {
				mtm.add(MutationRequest.MUTATE_FIELDS);
				System.out.println("Allow field mutations");
			}
			
			if (config.allowClassMutations()) {
				mtm.add(MutationRequest.MUTATE_CLASS);
				System.out.println("Allow class mutations");
			}
			
			methodsToMutate = mtm.toArray(new String[mtm.size()]);
		}
		//==================================add banned methods============================================//
		if (!useExternalMutants) {
			
			boolean allowedMembersExists = !config.allowedMembers().isEmpty();
			boolean bannedMethodsExists = !config.bannedMethods().isEmpty();
			boolean bannedFieldsExists = !config.bannedFields().isEmpty();
			if (allowedMembersExists) {
				List<Pattern> allowedMembers = new LinkedList<>();
				for (String am : config.allowedMembers()) allowedMembers.add(Pattern.compile(am));
				System.out.print("allowed members: [");
				Iterator<Pattern> amIt = allowedMembers.iterator();
				while (amIt.hasNext()) {
					System.out.print(amIt.next());
					if (amIt.hasNext()) {
						System.out.println(", ");
					}
				}
				System.out.println("]");
				
				Configuration.add(PRVO.ALLOWED_METHODS_AND_FIELDS, allowedMembers);
			} else {
				if (bannedMethodsExists) {
					List<Pattern> bannedMethods = new LinkedList<>();
					for (String bm : config.bannedMethods()) bannedMethods.add(Pattern.compile(bm));
					System.out.print("banned methods: [");
					Iterator<Pattern> bmIt = bannedMethods.iterator();
					while (bmIt.hasNext()) {
						System.out.print(bmIt.next());
						if (bmIt.hasNext()) {
							System.out.println(", ");
						}
					}
					System.out.println("]");
					
					Configuration.add(PRVO.PROHIBITED_METHODS, bannedMethods);
				}
				if (bannedFieldsExists) {
					List<Pattern> bannedFields = new LinkedList<>();
					for (String bf : config.bannedFields()) bannedFields.add(Pattern.compile(bf));
					System.out.print("banned fields: [");
					Iterator<Pattern> bfIt = bannedFields.iterator();
					while (bfIt.hasNext()) {
						System.out.print(bfIt.next());
						if (bfIt.hasNext()) {
							System.out.println(", ");
						}
					}
					System.out.println("]");
					
					Configuration.add(PRVO.PROHIBITED_FIELDS, bannedFields);
				}
			}
			
		}
		
		//================================IGNORE MUTGENLIMIT ANNOTATIONS==================================//
		
		if (!useExternalMutants && config.ignoreMutGenLimit()) {
			System.out.println("Ignoring mutGenLimit annotations");
			Configuration.add(Api.USE_MUTGENLIMIT, Boolean.FALSE);
		}
		
		//==========================ALLOW NUMERIC LITERAL VARIATIONS IN PRVO==============================//
		
		if (!useExternalMutants && config.allowNumericLiteralVariations()) {
			System.out.println("Allow numeric literal variations in PRVO");
			Configuration.add(PRVO.ENABLE_NUMBER_LITERALS_VARIATIONS, Boolean.TRUE);
		}
		
		if (config.quickDeath()) {
			System.out.println("Quick death enabled");
			MutationScore.quickDeath = true;
		} else {
			System.out.println("Quick death disabled");
			MutationScore.quickDeath = false;
		}
		
		if (config.dynamicSubsumption()) {
			System.out.println("Dynamic subsumption analysis enabled");
			Configuration.add(Core.DYNAMIC_SUBSUMPTION, Boolean.TRUE);
			MutationScore.dynamicSubsumption = true;
			if (config.reduceDynamicSubsumptionGraph()) {
				System.out.println("Dynamic subsumption graph will be reduced");
			}
			System.out.println("Dynamic subsumption analysis will be stored at: " + config.dynamicSubsumptionOutput());
		} else {
			System.out.println("Dynamic subsumption analysis disabled");
			Configuration.add(Core.DYNAMIC_SUBSUMPTION, Boolean.FALSE);
			MutationScore.dynamicSubsumption = false;
		}
		
		if (config.fullVerboseMode()) {
			System.out.println("Full verbose mode enabled");
			Core.fullVerbose = true;
		} else {
			Core.fullVerbose = false;
		}
		
		if (!useExternalMutants && config.outputMutationsInfo()) {
			System.out.println("Mutations information enabled on mutation score analysis");
			MutationScore.outputMutationsInfo = true;
		} else {
			MutationScore.outputMutationsInfo = false;
		}
		
		if (config.toughnessAnalysis()) {
			System.out.println("Toughness analysis enabled");
			Configuration.add(Core.ENABLE_TOUGHNESS, Boolean.TRUE);
		} else {
			Configuration.add(Core.ENABLE_TOUGHNESS, Boolean.FALSE);
		}
		
		if (!useExternalMutants) {
		
			if (config.useSimpleClassNames()) {
				System.out.println("Using simple class names when writing mutants");
				Configuration.add(Configuration.USE_SIMPLE_CLASS_NAMES, Boolean.TRUE);
			} else {
				Configuration.add(Configuration.USE_SIMPLE_CLASS_NAMES, Boolean.FALSE);
			}
			
			if (config.writePrologue()) {
				Configuration.add(MutantCodeWriter.WRITE_PROLOGUE, Boolean.TRUE);
				System.out.println("Write mutant prologue enabled");
			} else {
				Configuration.add(MutantCodeWriter.WRITE_PROLOGUE, Boolean.FALSE);
			}
			
			if (config.allowPrimitiveToObjectAssignments()) {
				System.out.println("Primitive to Object assignments are disabled");
				Configuration.add(PRVO.ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS, Boolean.FALSE);
			} else {
				Configuration.add(PRVO.ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS, Boolean.TRUE);
			}
			
			if (config.wrapPrimitiveToObjectAssignments()) {
				System.out.println("Primitive to Object assignments will be wrapped");
				Configuration.add(PRVO.ENABLE_PRIMITIVE_WRAPPING, Boolean.TRUE);
			} else {
				Configuration.add(PRVO.ENABLE_PRIMITIVE_WRAPPING, Boolean.FALSE);
			}
			
			if (config.applyRefinedPRVOInMethodCallStatements()) {
				System.out.println("Apply PRVOX_REFINED to mutate arguments in statements containing only a method call");
				Configuration.add(Configuration.ENABLE_REFINEMENT_IN_METHOD_CALL_STATEMENTS, Boolean.TRUE);
			} else {
				Configuration.add(Configuration.ENABLE_REFINEMENT_IN_METHOD_CALL_STATEMENTS, Boolean.FALSE);
			}
		
			generations = config.generation();
			System.out.println("Generations to generate: " + generations);
		}
		
		if (config.showSurvivingMutants()) {
			System.out.println("Showing surviving mutants at the end of mutation score");
			Core.showSurvivingMutants = true;
		} else {
			Core.showSurvivingMutants = false;
		}
		
		if (config.useParallelExternalJUnitRunner()) {
			System.out.println("Parallel mutant analysis");
			Core.useParallelJUnitRunner = true;
		} else {
			System.out.println("Sequential mutant analysis");
			Core.useParallelJUnitRunner = false;
		}
		
		if (config.useParallelExternalJUnitRunner()) {
			int threads = config.parallelExternalJUnitRunnerThreads();
			System.out.println("Parallel mutant analysis using " + threads + " threads");
			Core.parallelJUnitRunnerThreads = threads;
		}
		
		MutationScore.junitPath = config.junitPath();
		MutationScore.hamcrestPath = config.hamcrestPath();
		System.out.println("JUnit jar path: " + config.junitPath());
		System.out.println("hamcrest jar path: " + config.hamcrestPath());
		
		if (!useExternalMutants) {
			
			//PRVO CONFIG
			if (config.prvoSameLenght()) {
				Configuration.add(Configuration.ENABLE_SAME_LENGTH_MUTANTS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO same length enabled");
			} else {
				Configuration.add(Configuration.ENABLE_SAME_LENGTH_MUTANTS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO same length disabled");
			}
			
			if (config.prvoIncreaseLength()) {
				Configuration.add(Configuration.ENABLE_INCREASE_LENGTH_MUTANTS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO increase length enabled");
			} else {
				Configuration.add(Configuration.ENABLE_INCREASE_LENGTH_MUTANTS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO increase length disabled");
			}
			
			if (config.prvoDecreaseLength()) {
				Configuration.add(Configuration.ENABLE_DECREASE_LENGTH_MUTANTS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO decrease length enabled");
			} else {
				Configuration.add(Configuration.ENABLE_DECREASE_LENGTH_MUTANTS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO decrease length disabled");
			}
			
			if (config.prvoOneByTwo()) {
				Configuration.add(Configuration.ENABLE_ONE_BY_TWO_MUTANTS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO one by two enabled");
			} else {
				Configuration.add(Configuration.ENABLE_ONE_BY_TWO_MUTANTS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO one by two disabled");
			}
			
			if (config.prvoTwoByOne()) {
				Configuration.add(Configuration.ENABLE_TWO_BY_ONE_MUTANTS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO two by one enabled");
			} else {
				Configuration.add(Configuration.ENABLE_TWO_BY_ONE_MUTANTS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO two by one disabled");
			}
			
			if (config.prvoAllByOneLeft()) {
				Configuration.add(Configuration.ENABLE_ALL_BY_ONE_MUTANTS_LEFT, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO all by one left enabled");
			} else {
				Configuration.add(Configuration.ENABLE_ALL_BY_ONE_MUTANTS_LEFT, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO all by one left disabled");
			}
			
			if (config.prvoAllByOneRight()) {
				Configuration.add(Configuration.ENABLE_ALL_BY_ONE_MUTANTS_RIGHT, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO all by one right enabled");
			} else {
				Configuration.add(Configuration.ENABLE_ALL_BY_ONE_MUTANTS_RIGHT, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO all by one right disabled");
			}
			
			//NON NAVIGATION EXPRESSION MUTATIONS
			if (config.prvoNonNavigationExpressionMutations()) {
				Configuration.add(PRVO.ENABLE_NON_NAVIGATION_EXPRESSION__MUTATION, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO Non Navigation Expression mutations enabled");
			} else {
				Configuration.add(PRVO.ENABLE_NON_NAVIGATION_EXPRESSION__MUTATION, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO Non Navigation Expression mutations disabled (this overrides other options)");
			}
			
			if (config.prvoLiteralMutations()) {
				Configuration.add(PRVO.ENABLE_LITERAL_MUTATION, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO literal mutations enabled");
			} else {
				Configuration.add(PRVO.ENABLE_LITERAL_MUTATION, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO literal mutations disabled");
			}
			
			if (config.prvoAllocationMutations()) {
				Configuration.add(PRVO.ENABLE_NEW_MUTATION, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO Object Allocation mutations enabled");
			} else {
				Configuration.add(PRVO.ENABLE_NEW_MUTATION, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO Object Allocation mutations disabled");
			}
			
			if (config.prvoArrayAllocationMutations()) {
				Configuration.add(PRVO.ENABLE_ARRAY_ALLOCATION_MUTATION, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO Array Allocation mutations enabled");
			} else {
				Configuration.add(PRVO.ENABLE_ARRAY_ALLOCATION_MUTATION, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO Array Allocation mutations disabled");
			}
			
			if (config.prvoUseSuper()) {
				Configuration.add(Configuration.ENABLE_SUPER, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO super use enabled");
			} else {
				Configuration.add(Configuration.ENABLE_SUPER, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO super use disabled");
			}
			
			if (config.prvoUseThis()) {
				Configuration.add(Configuration.ENABLE_THIS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO this use enabled");
			} else {
				Configuration.add(Configuration.ENABLE_THIS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO this use disabled");
			}
			
			if (config.prvoReplacementWithLiterals()) {
				Configuration.add(Configuration.ENABLE_REPLACEMENT_WITH_LITERALS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO replacement with literals enabled");
			} else {
				Configuration.add(Configuration.ENABLE_REPLACEMENT_WITH_LITERALS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO replacement with literals disabled");
			}
			
			if (config.prvoUseNullLiteral()) {
				Configuration.add(Configuration.ENABLE_LITERAL_NULL, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO null literal use enabled");
			} else {
				Configuration.add(Configuration.ENABLE_LITERAL_NULL, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO null literal use disabled");
			}
			
			if (config.prvoUseTrueLiteral()) {
				Configuration.add(Configuration.ENABLE_LITERAL_TRUE, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO true literal use enabled");
			} else {
				Configuration.add(Configuration.ENABLE_LITERAL_TRUE, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO true literal use disabled");
			}
			
			if (config.prvoUseFalseLiteral()) {
				Configuration.add(Configuration.ENABLE_LITERAL_FALSE, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO false literal use enabled");
			} else {
				Configuration.add(Configuration.ENABLE_LITERAL_FALSE, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO false literal use disabled");
			}
			
			if (config.prvoUseEmptyStringLiteral()) {
				Configuration.add(Configuration.ENABLE_LITERAL_EMPTY_STRING, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO empty string literal use enabled");
			} else {
				Configuration.add(Configuration.ENABLE_LITERAL_EMPTY_STRING, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO empty string literal use disabled");
			}
			
			if (config.prvoUseZeroLiteral()) {
				Configuration.add(Configuration.ENABLE_LITERAL_ZERO, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO zero literal use enabled");
			} else {
				Configuration.add(Configuration.ENABLE_LITERAL_ZERO, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO zero literal use disabled");
			}
			
			if (config.prvoUseOneLiteral()) {
				Configuration.add(Configuration.ENABLE_LITERAL_ONE, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO one literal use enabled");
			} else {
				Configuration.add(Configuration.ENABLE_LITERAL_ONE, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO one literal use disabled");
			}
			
			if (config.prvoUseStringLiterals()) {
				Configuration.add(Configuration.ENABLE_LITERAL_STRINGS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO string literals use enabled");
			} else {
				Configuration.add(Configuration.ENABLE_LITERAL_STRINGS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO string literals use disabled");
			}
			
			if (config.prvoAllowFinalMembers()) {
				Configuration.add(Configuration.ALLOW_FINAL_MEMBERS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO final members use enabled");
			} else {
				Configuration.add(Configuration.ALLOW_FINAL_MEMBERS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO final members use disabled");
			}
			
			if (config.prvoEnableRelaxedTypes()) {
				Configuration.add(Configuration.ENABLE_RELAXED_TYPES, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO relaxed type check enabled");
			} else {
				Configuration.add(Configuration.ENABLE_RELAXED_TYPES, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO relaxed type check disabled");
			}
			
			if (config.prvoEnableAutoboxing()) {
				Configuration.add(Configuration.ENABLE_AUTOBOXING, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO autobox in checking enabled");
			} else {
				Configuration.add(Configuration.ENABLE_AUTOBOXING, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO autobox in checking disabled");
			}
			
			if (config.prvoEnableInheritedElements()) {
				Configuration.add(Configuration.ENABLE_INHERITED_ELEMENTS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO inherited elements use enabled");
			} else {
				Configuration.add(Configuration.ENABLE_INHERITED_ELEMENTS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO inherited elements use disabled");
			}
			
			if (config.prvoSmartMode_arithmeticOpShortcuts()) {
				Configuration.add(PRVO.SMART_MODE_WITH_ARITHMETIC_OP_SHORTCUTS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO will not use methods or final fields or variables for arithmetic shortcut operators expressions");
			} else {
				Configuration.add(PRVO.SMART_MODE_WITH_ARITHMETIC_OP_SHORTCUTS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO will use methods or final fields or variables for arithmetic shortcut operators expressions (WARNING: this will generate non-compiling mutants)");
			}
			
			if (config.prvoSmartMode_assignments()) {
				Configuration.add(PRVO.SMART_MODE_WITH_ASSIGNMENTS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("PRVO will not use final fields or variables for left hand side of assignments, and will not mutate the left hand side if it's final");
			} else {
				Configuration.add(PRVO.SMART_MODE_WITH_ASSIGNMENTS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("PRVO will ignore final modifiers when mutating left hand side of assignments (WARNING: this will generate non-compiling mutants)");
			}
		
			//ROR CONFIG
			
			if (config.rorReplaceWithTrue()) {
				Configuration.add(Configuration.REPLACE_WITH_TRUE, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("ROR replacement with true enabled");
			} else {
				Configuration.add(Configuration.REPLACE_WITH_TRUE, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("ROR replacement with true disabled");
			}
	
			if (config.rorReplaceWithFalse()) {
				Configuration.add(Configuration.REPLACE_WITH_FALSE, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("ROR replacement with false enabled");
			} else {
				Configuration.add(Configuration.REPLACE_WITH_FALSE, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("ROR replacement with false disabled");
			}
			
			if (config.rorSmartLiteralReplace()) {
				Configuration.add(ROR.SMART_LITERAL_REPLACE, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("ROR smart literal replacement enabled");
			} else {
				Configuration.add(ROR.SMART_LITERAL_REPLACE, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("ROR smart literal replacement disabled (WARNING: this will generate non-compiling mutants)");
			}
		
			//COR CONFIG
			
			if (config.corUseAndOp()) {
				Configuration.add(Configuration.ALLOW_LOGICAL_AND, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("COR and op enabled");
			} else {
				Configuration.add(Configuration.ALLOW_LOGICAL_AND, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("COR and op disabled");
			}
			
			if (config.corUseOrOp()) {
				Configuration.add(Configuration.ALLOW_LOGICAL_OR, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("COR or op enabled");
			} else {
				Configuration.add(Configuration.ALLOW_LOGICAL_OR, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("COR or op disabled");
			}
			
			if (config.corUseXorOp()) {
				Configuration.add(Configuration.ALLOW_XOR, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("COR xor op enabled");
			} else {
				Configuration.add(Configuration.ALLOW_XOR, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("COR xor op disabled");
			}
			
			if (config.corUseBitAndOp()) {
				Configuration.add(Configuration.ALLOW_LOGICAL_AND, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("COR bit and op enabled");
			} else {
				Configuration.add(Configuration.ALLOW_LOGICAL_AND, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("COR bit and op disabled");
			}
			
			if (config.corUseBitOrOp()) {
				Configuration.add(Configuration.ALLOW_LOGICAL_OR, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("COR bit or op enabled");
			} else {
				Configuration.add(Configuration.ALLOW_LOGICAL_OR, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("COR bit or op disabled");
			}
			
			//BEE CONFIG
			if (config.beeSkipEquivalentMutations()) {
				Configuration.add(BEE.DISABLE_NEUTRAL_TRUE_FALSE, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("BEE skip equivalent constant mutations enabled");
			} else {
				Configuration.add(BEE.DISABLE_NEUTRAL_TRUE_FALSE, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("BEE skip equivalent constant mutations disabled");
			}
			
			if (config.beeSkipConstantsWithTrueAndFalse()) {
				Configuration.add(BEE.DISABLE_NEUTRAL_CONSTANTS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("BEE skip constants with true or false enabled");
			} else {
				Configuration.add(BEE.DISABLE_NEUTRAL_CONSTANTS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("BEE skip constants with true or false disabled");
			}
			
			if (config.beeScanExpressions()) {
				Configuration.add(BEE.SCAN_FOR_EXPRESSIONS, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("BEE scan for expressions enabled");
			} else {
				Configuration.add(BEE.SCAN_FOR_EXPRESSIONS, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("BEE scan for expressions disabled");
			}
			
			if (config.beeSkipSameExpression()) {
				Configuration.add(BEE.SKIP_SAME_EXPRESSION, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("BEE skip same expression enabled");
			} else {
				Configuration.add(BEE.SKIP_SAME_EXPRESSION, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("BEE skip same expression disabled");
			}
			
			//AOIS CONFIG
			if (config.aoisSkipFinal()) {
				Configuration.add(AOIS.AOIS_IGNORE_FINAL, Boolean.TRUE);
				if (config.fullVerboseMode()) System.out.println("AOIS will not mutate final variables or fields");
			} else {
				Configuration.add(AOIS.AOIS_IGNORE_FINAL, Boolean.FALSE);
				if (config.fullVerboseMode()) System.out.println("AOIS will mutate final variable and fields (WARNING: this will generate non-compiling mutants)");
			}
			
			//AODS CONFIG
			if (config.aodsSkipExpressionStatements()) {
				Configuration.add(AODS.AODS_AVOID_EXPRESSION_STATEMENTS, Boolean.TRUE);
				System.out.println("AODS will ignore arithmetic operator shortcuts when they are a statement");
			} else {
				Configuration.add(AODS.AODS_AVOID_EXPRESSION_STATEMENTS, Boolean.FALSE);
				System.out.println("AODS will not ignore arithmetic operator shortcuts when they are a statement (WARNING: this will generate non-compiling mutants)");
			}
		}
		
		//================================TIMEOUT==============================================//
		if (config.runMutationScore() && config.testTimeout() > 0) {
			if (config.fullVerboseMode()) System.out.println("Default test timeout set to : " + config.testTimeout());
			MutationScore.timeout = config.testTimeout();
		}
		if (config.runMutationScore() && config.discardTimeout() > 0) {
			if (config.fullVerboseMode()) System.out.println("Discard timeout set to : " + config.discardTimeout());
			MutationScore.discardTimeout = config.discardTimeout();
		}
		//=====================================================================================//
		
		
		System.out.println("Parameters validated\n\n");
		
		//================================Mutants generation==============================================//
		//TEMP HARDCODED CONFIGURATION
		//this configurations should be added to configurable values
		//ADD HERE
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		if (!useExternalMutants) {
			System.out.println("Generating mutants...\n");
			//List<MutationOperator> basicMutants = mi.listBasicOperators();
			boolean result = core.generateMutants(classToMutate, methodsToMutate, ops.toArray(new MutationOperator[ops.size()]), generations);
			if (!result) {
				core.lastError().printStackTrace(System.err);
			} else {
				System.out.println(core.lastMutantsFolder().toString());
			}
			System.out.println("\nMutants generated\n\n");
		} else {
			boolean externalParsingStatus = false;
			if (usingPrototypeExternalClasses) {
				externalParsingStatus = core.parseExternalSeveralMutants(externalClasses);
			} else {
				externalParsingStatus = core.parseExternalMutants(classToMutate);
			}
			if (!externalParsingStatus) {
				System.err.println(ExceptionUtils.getFullStackTrace(core.lastError()));
				return;
			} else {
				System.out.println("\nExternal mutants parsed\n\n");
			}
		}
		//==============================Mutation score====================================================//;
		if (mutationScore && ((core.lastMutantsFolder() != null && !core.lastMutantsFolder().isEmpty()) || useExternalMutants) ) {
			float mutationScoreResult = -1;
			MutationScore ms = MutationScore.newInstance(mutantsSourceDir, originalBinDir, testBinDir);
			core.setMutationScore(ms);
			SubsumptionAnalysis subsumptionAnalysis = null;
			if (config.dynamicSubsumption()) {
				subsumptionAnalysis = new SubsumptionAnalysis();
				core.setDynamicSubsumptionAnalysis(subsumptionAnalysis);
			}
			if (!useExternalMutants) {
				System.out.println("Calculating mutation score for generation (" + generations + ")\n");
				mutationScoreResult = core.calculateMutationScore(testClasses, classToMutate);
			} else {
				System.out.println("Calculating mutation score for external mutants\n");
				mutationScoreResult = core.evaluateExternalResults(testClasses, config.useParallelExternalJUnitRunner());
			}
			System.out.println();
			if (useExternalMutants && usingPrototypeExternalClasses) {
				System.out.print("All classes");
			} else {
				System.out.print(classToMutate);
			}
			System.out.println(" scored : " + mutationScoreResult + " with tests : " + Arrays.asList(testClasses).toString());
			Core.killStillRunningJUnitTestcaseThreads();
			if (config.dynamicSubsumption()) {
				//SubsumptionAnalysis.fullPrint = true;
				subsumptionAnalysis.analyse();
				Path output = Paths.get(config.dynamicSubsumptionOutput(), "dsg.dot");
				if (subsumptionAnalysis.generateDotFile(output.toString())
					&&
					subsumptionAnalysis.generateSVG(output.toString(), config.reduceDynamicSubsumptionGraph())) {
					System.out.println("Dynamic subsumption saved at " + config.dynamicSubsumptionOutput());
				} else {
					System.err.println("Error generating Dynamic Subsumption results");
				}
				Path domMutantsPerOp = Paths.get(config.dynamicSubsumptionOutput(), "domMutantsPerOperator.txt");
				if (subsumptionAnalysis.writeDominatorMutantsPerOperator(domMutantsPerOp.toString())) {
					System.out.println("Dominator mutants per operator saved at " + domMutantsPerOp.toString());
				} else {
					System.err.println("Error while writting dominator mutants per operator");
				}
		
//				if (subsumptionAnalysis.dumpSubsumptionNodes(domMutantsPerOp.toString())) {
//					System.out.println("Subsumption nodes dump saved at " + config.dynamicSubsumptionOutput() + "/DUMP");
//				}
			}
		}
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
		if (!useExternalMutants && directory.exists() && directory.isDirectory()) {
			if (!confirmDirectoryCleaning(directory)) {
				return;
			}
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
	
	private static boolean confirmDirectoryCleaning(File file) {
		String input;
		System.out.print("Please confirm that you want to delete everything inside " + file.getAbsolutePath() + " (Yes|Y|S|Si|N|No) : ");
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		input = in.next();
		if (input.toLowerCase().matches("(yes|y|si|s)")) {
			return true;
		}
		if (input.toLowerCase().matches("(no|n)")) {
			return false;
		}
		System.out.println();
		System.out.println("Invalid option ("+ input + ")");
		return confirmDirectoryCleaning(file);
	}

	private static boolean verifyFile(String f) {
		File file = new File(f);
		return file.exists() && file.isFile();
	}
	
	private static boolean verifyClassFile(String f) {
		int firstDolar = f.indexOf('$');
		if (firstDolar > 0) {
			f = f.substring(0, firstDolar) + ".java";
		}
		File file = new File(f);
		return file.exists() && file.isFile();
	}
	
	private static String stringListToString(Set<String> xs) {
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
