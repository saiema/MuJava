package mujava.util;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import static mujava.util.ConfigReader.Config_key.*;


/**
 * This class allows access to a configuration loaded from a properties file
 * but also allows access to some values that either doesn't belong to the
 * properties file (e.g.: file separator) and properties that need arguments to
 * from an usable value (e.g.: output dir and compilation sandbox that need the
 * file separator  
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 2.1b
 */
public class ConfigReader {
	
	//TODO: document
	public static enum Config_key {
		
		//DIRECTORIES
		ORIGINAL_SOURCE_DIR {
			public String getKey() {
				return "path.original.source";
			}
			public String getFlag() {
				return "-O";
			}
		},
		ORIGINAL_BIN_DIR {
			public String getKey() {
				return "path.original.bin";
			}
			public String getFlag() {
				return "-B";
			}
		},
		TESTS_BIN_DIR {
			public String getKey() {
				return "path.tests.bin";
			}
			public String getFlag() {
				return "-T";
			}
		},
		MUTANTS_DIR {
			public String getKey() {
				return "path.mutants";
			}
			public String getFlag() {
				return "-M";
			}
		},
		JUNIT_PATH {
			public String getKey() {
				return "path.junit";
			}
		},
		HAMCREST_PATH {
			public String getKey() {
				return "path.hamcrest";
			}
		},
		//DIRECTORIES
		//MUTATION BASIC
		CLASS {
			public String getKey() {
				return "mutation.basic.class";
			}
			public String getFlag() {
				return "-m";
			}
		},
		EXTERNAL_CLASSES_PROTOTYPE {
			public String getKey() {
				return "mutation.basic.prototype.classes";
			}
		},
		METHODS {
			public String getKey() {
				return "mutation.basic.methods";
			}
			public String getFlag() {
				return "-s";
			}
		},
		OPERATORS {
			public String getKey() {
				return "mutation.basic.operators";
			}
			public String getFlag() {
				return "-x";
			}
		},
		MUTATION_SCORE {
			public String getKey() {
				return "mutation.basic.mutationScore";
			}
			public String getFlag() {
				return "-S";
			}
		},
		TESTS {
			public String getKey() {
				return "mutation.basic.tests";
			}
			public String getFlag() {
				return "-t";
			}
		},
		SHOW_SURVIVING_MUTANTS {
			public String getKey() {
				return "mutation.basic.showSurvivingMutants";
			}
			public String getFlag() {
				return "-W";
			}
		},
		USE_EXTERNAL_MUTANTS {
			public String getKey() {
				return "mutation.basic.useExternalMutants";
			}
		},
		USE_SOCKETS {
			public String getKey() {
				return "mutation.basic.useSockets";
			}
		},
		WRITE_PROLOGUE {
			public String getKey() {
				return "mutation.basic.writePrologue";
			}
		},
		//MUTATION BASIC
		//MUTATION ADVANCED
		TEST_TIMEOUT {
			public String getKey() {
				return "mutation.advanced.timeout";
			}
		},
		DISCARD_TIMEOUT {
			public String getKey() {
				return "mutation.advanced.discardTimeout";
			}
		},
		ALLOWED_MEMBERS {
			public String getKey() {
				return "mutation.advanced.allowedMembers";
			}
		},
		BANNED_METHODS {
			public String getKey() {
				return "mutation.advanced.bannedMethods";
			}
			public String getFlag() {
				return "-N";
			}
		},
		BANNED_FIELDS {
			public String getKey() {
				return "mutation.advanced.bannedFields";
			}
			public String getFlag() {
				return "-J";
			}
		},
		MUTGENLIMIT {
			public String getKey() {
				return "mutation.advanced.ignoreMutGenLimit";
			}
			public String getFlag() {
				return "-A";
			}
		},
		ALLOW_FIELD_MUTATIONS {
			public String getKey() {
				return "mutation.advanced.allowFieldMutations";
			}
			public String getFlag() {
				return "-F";
			}
		},
		ALLOW_CLASS_MUTATIONS {
			public String getKey() {
				return "mutation.advanced.allowClassMutations";
			}
			public String getFlag() {
				return "-C";
			}
		},
		ALLOWED_PACKAGES_TO_RELOAD {
			public String getKey() {
				return "mutation.advanced.allowedPackagesToReload";
			}
			public String getFlag() {
				return "-P";
			}
		},
		QUICK_DEATH {
			public String getKey() {
				return "mutation.advanced.quickDeath";
			}
			public String getFlag() {
				return "-Q";
			}
		},
		FULL_VERBOSE {
			public String getKey() {
				return "mutation.advanced.fullVerbose";
			}
			public String getFlag() {
				return "-V";
			}
		},
		GENERATIONS {
			public String getKey() {
				return "mutation.advanced.generations";
			}
			public String getFlag() {
				return "-g";
			}
		},
		RELOADER_INSTANCES_LIMIT {
			public String getKey() {
				return "mutation.advanced.ReloaderCleanLimit";
			}
			public String getFlag() {
				return "-c";
			}
		},
		MUTATION_SCORE_TOUGHNESS_ANALYSIS {
			public String getKey() {
				return "mutation.advanced.toughness";
			}
		},
		DYNAMIC_SUBSUMPTION_ANALYSIS {
			public String getKey() {
				return "mutation.advanced.dynamicSubsumption";
			}
		},
		DYNAMIC_SUBSUMPTION_REDUCE_GRAPH {
			public String getKey() {
				return "mutation.advanced.dynamicSubsumption.reduceGraph";
			}
		},
		DYNAMIC_SUBSUMPTION_FOLDER {
			public String getKey() {
				return "mutation.advanced.dynamicSubsumption.output";
			}
		},
		USE_SIMPLE_CLASS_NAMES {
			public String getKey() {
				return "mutation.advanced.useSimpleClassNames";
			}
		},
		OUPUT_MUTANT_MUTATIONS {
			public String getKey() {
				return "mutation.advanced.outputMutationsInfoInMutationScore";
			}
		},
		USE_EXTERNAL_JUNIT_RUNNER {
			public String getKey() {
				return "mutation.advanced.useExternalJUnitRunner";
			}
		},
		USE_PARALLEL_JUNIT_RUNNER {
			public String getKey() {
				return "mutation.advanced.useParallelJUnitRunner";
			}
		},
		PARALLEL_JUNIT_RUNNER_THREADS {
			public String getKey() {
				return "mutation.advanced.parallelJUnitRunnerThreads";
			}
		},
		INHERITED_BASIC_CONFIG {
			public String getKey() {
				return "mutation.advanced.inheritedBasicConfig";
			}
		},
		/*
		USE_EXTERNAL_JUNIT_RUNNER
		USE_PARALLEL_JUNIT_RUNNER
		INHERITED_BASIC_CONFIG
		JUNIT_PATH
		HAMCREST_PATH
		*/
		//MUTATION ADVANCED
		//MUTATION ADVANCED PRVO
		ALLOW_NUMERIC_LITERAL_VARIATIONS {
			public String getKey() {
				return "mutation.advanced.prvo.allowNumericLiteralVariations";
			}
			public String getFlag() {
				return "-L";
			}
		},
		PRVO_ENABLE_SAME_LENGTH {
			public String getKey() {
				return "mutation.advanced.prvo.enableSameLenght";
			}
		},
		PRVO_ENABLE_INCREASE_LENGTH {
			public String getKey() {
				return "mutation.advanced.prvo.enableIncreaseLenght";
			}
		},
		PRVO_ENABLE_DECREASE_LENGTH {
			public String getKey() {
				return "mutation.advanced.prvo.enableDecreaseLenght";
			}
		},
		PRVO_ENABLE_ONE_BY_TWO {
			public String getKey() {
				return "mutation.advanced.prvo.enableOneByTwo";
			}
		},
		PRVO_ENABLE_TWO_BY_ONE {
			public String getKey() {
				return "mutation.advanced.prvo.enableTwoByOne";
			}
		},
		PRVO_ENABLE_ALL_BY_ONE_LEFT {
			public String getKey() {
				return "mutation.advanced.prvo.enableAllByOneLeft";
			}
		},
		PRVO_ENABLE_ALL_BY_ONE_RIGHT {
			public String getKey() {
				return "mutation.advanced.prvo.enableAllByOneRight";
			}
		},
		PRVO_ENABLE_LITERAL_MUTATIONS {
			public String getKey() {
				return "mutation.advanced.prvo.enableLiteralMutations";
			}
		},
		PRVO_ENABLE_OBJECT_ALLOCATION_MUTATIONS {
			public String getKey() {
				return "mutation.advanced.prvo.enableObjectAllocationMutations";
			}
		},
		PRVO_ENABLE_ARRAY_ALLOCATION_MUTATIONS {
			public String getKey() {
				return "mutation.advanced.prvo.enableArrayAllocationMutations";
			}
		},
		PRVO_ENABLE_NON_NAVIGATION_EXPRESSION_MUTATIONS {
			public String getKey() {
				return "mutation.advanced.prvo.enableNonNavigationExpressionMutations";
			}
		},
		PRVO_ENABLE_SUPER {
			public String getKey() {
				return "mutation.advanced.prvo.enableSuper";
			}
		},
		PRVO_ENABLE_THIS {
			public String getKey() {
				return "mutation.advanced.prvo.enableThis";
			}
		},
		PRVO_ENABLE_REPLACEMENT_WITH_LITERALS {
			public String getKey() {
				return "mutation.advanced.prvo.enableReplacementWithLiterals";
			}
		},
		PRVO_ENABLE_NULL_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableNullLiteral";
			}
		},
		PRVO_ENABLE_TRUE_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableTrueLiteral";
			}
		},
		PRVO_ENABLE_FALSE_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableFalseLiteral";
			}
		},
		PRVO_ENABLE_EMPTY_STRING_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableEmptyString";
			}
		},
		PRVO_ENABLE_ZERO_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableZeroLiteral";
			}
		},
		PRVO_ENABLE_ONE_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableOneLiteral";
			}
		},
		PRVO_ENABLE_STRING_LITERALS {
			public String getKey() {
				return "mutation.advanced.prvo.enableStringLiterals";
			}
		},
		DISABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS {
			public String getKey() {
				return "mutation.advanced.prvo.disablePrimitiveToObjectAssignments";
			}
			public String getFlag() {
				return "-p";
			}
		},
		WRAP_PRIMITIVE_TO_OBJECT_ASSIGNMENTS {
			public String getKey() {
				return "mutation.advanced.prvo.wrapPrimitiveToObjectAssignments";
			}
			public String getFlag() {
				return "-w";
			}
		},
		APPLY_REF_PRVO_TO_METHODCALLS_STATEMENTS {
			public String getKey() {
				return "mutation.advanced.prvo.applyRefinedPRVOInMethodCallStatements";
			}
			public String getFlag() {
				return "-r";
			}
		},
		PRVO_ALLOW_FINAL_MEMBERS {
			public String getKey() {
				return "mutation.advanced.prvo.allowFinalMembers";
			}
		},
		PRVO_ENABLE_RELAXED_TYPES {
			public String getKey() {
				return "mutation.advanced.prvo.enableRelaxedTypes";
			}
		},
		PRVO_ENABLE_AUTOBOXING {
			public String getKey() {
				return "mutation.advanced.prvo.enableAutoboxing";
			}
		},
		PRVO_ENABLE_INHERITED_ELEMENTS {
			public String getKey() {
				return "mutation.advanced.prvo.enableInheritedElements";
			}
		},
		PRVO_ENABLE_STATIC_FROM_NON_STATIC_EXP {
			public String getKey() {
				return "mutation.advanced.prvo.enableStaticFromNonStaticExp";
			}
		},
		PRVO_SMART_MODE_ARITHMETIC_OP_SHORTCUTS {
			public String getKey() {
				return "mutation.advanced.prvo.smartmode.arithmeticOpShortcuts";
			}
		},
		PRVO_SMART_MODE_ASSIGNMENTS {
			public String getKey() {
				return "mutation.advanced.prvo.smartmode.assignments";
			}
		},
		//MUTATION ADVANCED PRVO
		//MUTATION ADVANCED ROR
		ROR_REPLACE_WITH_TRUE {
			public String getKey() {
				return "mutation.advanced.ror.replaceWithTrue";
			}
		},
		ROR_REPLACE_WITH_FALSE {
			public String getKey() {
				return "mutation.advanced.ror.replaceWithFalse";
			}
		},
		ROR_SMART_LITERAL_REPLACE {
			public String getKey() {
				return "mutation.advanced.ror.smartLiteralReplacement";
			}
		},
		//MUTATION ADVANCED ROR
		//MUTATION ADVANCED COR
		COR_USE_AND_OP {
			public String getKey() {
				return "mutation.advanced.cor.andOperator";
			}
		},
		COR_USE_OR_OP {
			public String getKey() {
				return "mutation.advanced.cor.orOperator";
			}
		},
		COR_USE_XOR_OP {
			public String getKey() {
				return "mutation.advanced.cor.xorOperator";
			}
		},
		COR_USE_BIT_AND_OP {
			public String getKey() {
				return "mutation.advanced.cor.bitAndOperator";
			}
		},
		COR_USE_BIT_OR__OP {
			public String getKey() {
				return "mutation.advanced.cor.bitOrOperator";
			}
		},
		//MUTATION ADVANCED COR
		//MUTATION ADVANCED BEE
		BEE_SKIP_EQUIVALENT_MUTATION {
			public String getKey() {
				return "mutation.advanced.bee.skipEquivalentMutations";
			}
		},
		BEE_SKIP_CONSTANTS {
			public String getKey() {
				return "mutation.advanced.bee.skipConstants";
			}
		},
		BEE_SCAN_EXPRESSIONS {
			public String getKey() {
				return "mutation.advanced.bee.scanExpressions";
			}
		},
		//MUTATION ADVANCED BEE
		//MUTATION ADVANCED AOIS
		AOIS_SKIP_FINAL {
			public String getKey() {
				return "mutation.advanced.aois.skipFinal";
			}
		},
		//MUTATION ADVANCED AOIS
		//MUTATION ADVANCED AODS
		AODS_SKIP_EXPRESSION_STATEMENTS {
			public String getKey() {
				return "mutation.advanced.aods.skipExpressionStatements";
			}
		};
		//MUTATION ADVANCED AODS
		public static final String NO_FLAG="-?";
		
		public abstract String getKey();
		public String getFlag() {
			return NO_FLAG;
		}
	};
	
	/**
	 * The path to a default .properties file
	 */
	public static final String DEFAULT_PROPERTIES = "default.properties";
	
	public static final String DEFAULT_BASIC_PROPERTIES = "default_basic.properties";
	
	/**
	 * The {@code StrykerConfig} instance that will be returned by {@link ConfigReader#getInstance(String)}
	 */
	private static ConfigReader instance = null;

	/**
	 * The properties file that will be loaded
	 */
	private String propertiesFile;
	/**
	 * The configuration loaded, specified by {@link ConfigReader#propertiesFile}
	 */
	private Configuration config;
	
	/**
	 * Gets an instance of {@code StrykerConfig}
	 * 
	 * @param configFile	:	the properties file that will be loaded	:	{@code String}
	 * @return an instance of {@code StrykerConfig} that uses {@code configFile} to load a configuration
	 * @throws IllegalStateException if an instance is already built and this method is called with a different config file
	 */
	public static ConfigReader getInstance(String configFile) throws IllegalStateException {
		if (instance != null) {
			if (instance.propertiesFile.compareTo(configFile) != 0) {
				throw new IllegalStateException("Config instance is already built using config file : " + instance.propertiesFile);
			}
		} else {
			instance = new ConfigReader(configFile);
		}
		return instance;
	}
	
	/**
	 * @return a previously built instance or construct a new instance using {@code StrykerConfig#DEFAULT_PROPERTIES}
	 */
	public static ConfigReader getInstance() {
		if (instance == null) {
			instance = new ConfigReader(ConfigReader.DEFAULT_PROPERTIES);
		}
		return instance;
	}
	
	/**
	 * Private constructor
	 * 
	 * This will set the value of {@link ConfigReader#propertiesFile} and will load the configuration
	 * 
	 * @param configFile	:	the properties file that will be loaded	:	{@code String}
	 */
	private ConfigReader(String configFile) {
		this.propertiesFile = configFile;
		this.config = null;
		loadConfig();
	}

	/**
	 * loads the configuration defined in {@link ConfigReader#propertiesFile}
	 */
	private void loadConfig() {
		try {
			this.config = new PropertiesConfiguration(this.propertiesFile);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the configuration especified by {@link ConfigReader#propertiesFile}
	 */
	public Configuration getConfiguration() {
		return this.config;
	}
	
	/**
	 * @return the file separator for the current os (e.g.: "/" for unix)
	 */
	public String getFileSeparator() {
		return SystemUtils.FILE_SEPARATOR;
	}
	
	/**
	 * @return the path separator for the current os (e.g.: ":" for unix)
	 */
	public String getPathSeparator() {
		return SystemUtils.PATH_SEPARATOR;
	}
	
	public String[] configAsArgs() {
		List<String> args = new LinkedList<String>();
		Config_key[] configKeys = Config_key.values();
		for (Config_key ck : configKeys) {
			if (argumentExist(ck) && ck.getFlag().compareTo(Config_key.NO_FLAG) != 0) {
				if (isBooleanKey(ck)) {
					if (getBooleanArgument(ck)) args.add(ck.getFlag());
				} else if (isIntKey(ck)) {
					args.add(ck.getFlag());
					args.add(String.valueOf(getIntArgument(ck)));
				} else {
					args.add(ck.getFlag());
					for (String arg : this.stringArgumentsAsArray(getStringArgument(ck))) {
						args.add(arg);
					}
				}
			}
		}
		return args.toArray(new String[args.size()]);
	}
	
	public Config buildConfig() throws IllegalStateException {
		Config conf = new Config(getStringArgument(ORIGINAL_SOURCE_DIR), getStringArgument(ORIGINAL_BIN_DIR), getStringArgument(MUTANTS_DIR));
		boolean useExternalMutants = false;
		if (isDefined(USE_EXTERNAL_MUTANTS)) {
			useExternalMutants = getBooleanArgument(USE_EXTERNAL_MUTANTS);
			conf.useExternalMutants(useExternalMutants);
		}
		if (useExternalMutants && isDefined(EXTERNAL_CLASSES_PROTOTYPE)){
			conf.externalClassesToMutate(stringArgumentsAsArray(getStringArgument(EXTERNAL_CLASSES_PROTOTYPE)));
		} else {
			conf.classToMutate(getStringArgument(CLASS));
		}
		if (!useExternalMutants) {
			for (String methodToMutate : stringArgumentsAsArray(getStringArgument(METHODS))) {
				conf.addMethodToMutate(methodToMutate);
			}
			for (String op : stringArgumentsAsArray(getStringArgument(OPERATORS))) {
				conf.addOperator(op);
			}
		}
		if (isDefined(FULL_VERBOSE)) conf.fullVerboseMode(getBooleanArgument(FULL_VERBOSE)); else conf.fullVerboseMode(false);
		if (!useExternalMutants) {
			if (isDefined(ALLOW_CLASS_MUTATIONS)) conf.allowClassMutations(getBooleanArgument(ALLOW_CLASS_MUTATIONS));
			if (isDefined(ALLOW_FIELD_MUTATIONS)) conf.allowFieldMutations(getBooleanArgument(ALLOW_FIELD_MUTATIONS));
			if (isDefined(DISABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS)) conf.allowPrimitiveToObjectAssignments(getBooleanArgument(DISABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS));
			if (isDefined(WRAP_PRIMITIVE_TO_OBJECT_ASSIGNMENTS)) conf.wrapPrimitiveToObjectAssignments(getBooleanArgument(WRAP_PRIMITIVE_TO_OBJECT_ASSIGNMENTS));
			if (isDefined(APPLY_REF_PRVO_TO_METHODCALLS_STATEMENTS)) conf.applyRefinedPRVOInMethodCallStatements(getBooleanArgument(APPLY_REF_PRVO_TO_METHODCALLS_STATEMENTS));
			if (isDefined(GENERATIONS)) conf.generation(getIntArgument(GENERATIONS)); else conf.generation(1);
			if (isDefined(ROR_REPLACE_WITH_TRUE)) conf.rorReplaceWithTrue(getBooleanArgument(ROR_REPLACE_WITH_TRUE));
			if (isDefined(ROR_REPLACE_WITH_FALSE)) conf.rorReplaceWithFalse(getBooleanArgument(ROR_REPLACE_WITH_FALSE));
			if (isDefined(ROR_SMART_LITERAL_REPLACE)) conf.rorSmartLiteralReplace(getBooleanArgument(ROR_SMART_LITERAL_REPLACE));
			if (isDefined(COR_USE_AND_OP)) conf.corUseAndOp(getBooleanArgument(COR_USE_AND_OP));
			if (isDefined(COR_USE_OR_OP)) conf.corUseOrOp(getBooleanArgument(COR_USE_OR_OP));
			if (isDefined(COR_USE_XOR_OP)) conf.corUseXorOp(getBooleanArgument(COR_USE_XOR_OP));
			if (isDefined(COR_USE_BIT_AND_OP)) conf.corUseBitAndOp(getBooleanArgument(COR_USE_BIT_AND_OP));
			if (isDefined(COR_USE_BIT_OR__OP)) conf.corUseBitOrOp(getBooleanArgument(COR_USE_BIT_OR__OP));
			if (isDefined(PRVO_ENABLE_SAME_LENGTH)) conf.prvoSameLenght(getBooleanArgument(PRVO_ENABLE_SAME_LENGTH));
			if (isDefined(PRVO_ENABLE_INCREASE_LENGTH)) conf.prvoIncreaseLength(getBooleanArgument(PRVO_ENABLE_INCREASE_LENGTH));
			if (isDefined(PRVO_ENABLE_DECREASE_LENGTH)) conf.prvoDecreaseLength(getBooleanArgument(PRVO_ENABLE_DECREASE_LENGTH));
			if (isDefined(PRVO_ENABLE_ONE_BY_TWO)) conf.prvoOneByTwo(getBooleanArgument(PRVO_ENABLE_ONE_BY_TWO));
			if (isDefined(PRVO_ENABLE_TWO_BY_ONE)) conf.prvoTwoByOne(getBooleanArgument(PRVO_ENABLE_TWO_BY_ONE));
			if (isDefined(PRVO_ENABLE_ALL_BY_ONE_LEFT)) conf.prvoAllByOneLeft(getBooleanArgument(PRVO_ENABLE_ALL_BY_ONE_LEFT));
			if (isDefined(PRVO_ENABLE_ALL_BY_ONE_RIGHT)) conf.prvoAllByOneRight(getBooleanArgument(PRVO_ENABLE_ALL_BY_ONE_RIGHT));
			if (isDefined(PRVO_ENABLE_LITERAL_MUTATIONS)) conf.prvoLiteralMutations(getBooleanArgument(PRVO_ENABLE_LITERAL_MUTATIONS));
			if (isDefined(PRVO_ENABLE_OBJECT_ALLOCATION_MUTATIONS)) conf.prvoAllocationMutations(getBooleanArgument(PRVO_ENABLE_OBJECT_ALLOCATION_MUTATIONS));
			if (isDefined(PRVO_ENABLE_ARRAY_ALLOCATION_MUTATIONS)) conf.prvoArrayAllocationMutations(getBooleanArgument(PRVO_ENABLE_ARRAY_ALLOCATION_MUTATIONS));
			
			//THIS ONE MUST BE THE LAST TO LOAD
			if (isDefined(PRVO_ENABLE_NON_NAVIGATION_EXPRESSION_MUTATIONS)) conf.prvoNonNavigationExpressionMutations(getBooleanArgument(PRVO_ENABLE_NON_NAVIGATION_EXPRESSION_MUTATIONS));
			
			if (isDefined(PRVO_ENABLE_SUPER)) conf.prvoUseSuper(getBooleanArgument(PRVO_ENABLE_SUPER));
			if (isDefined(PRVO_ENABLE_THIS)) conf.prvoUseThis(getBooleanArgument(PRVO_ENABLE_THIS));
			if (isDefined(PRVO_ENABLE_REPLACEMENT_WITH_LITERALS)) conf.prvoReplacementWithLiterals(getBooleanArgument(PRVO_ENABLE_REPLACEMENT_WITH_LITERALS));
			if (isDefined(PRVO_ENABLE_NULL_LITERAL)) conf.prvoUseNullLiteral(getBooleanArgument(PRVO_ENABLE_NULL_LITERAL));
			if (isDefined(PRVO_ENABLE_TRUE_LITERAL)) conf.prvoUseTrueLiteral(getBooleanArgument(PRVO_ENABLE_TRUE_LITERAL));
			if (isDefined(PRVO_ENABLE_FALSE_LITERAL)) conf.prvoUseFalseLiteral(getBooleanArgument(PRVO_ENABLE_FALSE_LITERAL));
			if (isDefined(PRVO_ENABLE_EMPTY_STRING_LITERAL)) conf.prvoUseEmptyStringLiteral(getBooleanArgument(PRVO_ENABLE_EMPTY_STRING_LITERAL));
			if (isDefined(PRVO_ENABLE_ZERO_LITERAL)) conf.prvoUseZeroLiteral(getBooleanArgument(PRVO_ENABLE_ZERO_LITERAL));
			if (isDefined(PRVO_ENABLE_ONE_LITERAL)) conf.prvoUseOneLiteral(getBooleanArgument(PRVO_ENABLE_ONE_LITERAL));
			if (isDefined(PRVO_ENABLE_STRING_LITERALS)) conf.prvoUseStringLiterals(getBooleanArgument(PRVO_ENABLE_STRING_LITERALS));
			if (isDefined(PRVO_ALLOW_FINAL_MEMBERS)) conf.prvoAllowFinalMembers(getBooleanArgument(PRVO_ALLOW_FINAL_MEMBERS));
			if (isDefined(PRVO_ENABLE_RELAXED_TYPES)) conf.prvoEnableRelaxedTypes(getBooleanArgument(PRVO_ENABLE_RELAXED_TYPES));
			if (isDefined(PRVO_ENABLE_AUTOBOXING)) conf.prvoEnableAutoboxing(getBooleanArgument(PRVO_ENABLE_AUTOBOXING));
			if (isDefined(PRVO_ENABLE_INHERITED_ELEMENTS)) conf.prvoEnableInheritedElements(getBooleanArgument(PRVO_ENABLE_INHERITED_ELEMENTS));
			if (isDefined(PRVO_ENABLE_STATIC_FROM_NON_STATIC_EXP)) conf.prvoAllowStaticFromNonStaticExpression(getBooleanArgument(PRVO_ENABLE_STATIC_FROM_NON_STATIC_EXP));
			if (isDefined(PRVO_SMART_MODE_ARITHMETIC_OP_SHORTCUTS)) conf.prvoSmartMode_arithmeticOpShortcuts(getBooleanArgument(PRVO_SMART_MODE_ARITHMETIC_OP_SHORTCUTS));
			if (isDefined(PRVO_SMART_MODE_ASSIGNMENTS)) conf.prvoSmartMode_assignments(getBooleanArgument(PRVO_SMART_MODE_ASSIGNMENTS));
			if (isDefined(BEE_SKIP_EQUIVALENT_MUTATION)) conf.beeSkipEquivalentMutations(getBooleanArgument(BEE_SKIP_EQUIVALENT_MUTATION));
			if (isDefined(BEE_SKIP_CONSTANTS)) conf.beeSkipConstantsWithTrueAndFalse(getBooleanArgument(BEE_SKIP_CONSTANTS));
			if (isDefined(BEE_SCAN_EXPRESSIONS)) conf.beeScanExpressions(getBooleanArgument(BEE_SCAN_EXPRESSIONS));
			if (isDefined(AOIS_SKIP_FINAL)) conf.aoisSkipFinal(getBooleanArgument(AOIS_SKIP_FINAL));
			if (isDefined(AODS_SKIP_EXPRESSION_STATEMENTS)) conf.aodsSkipExpressionStatements(getBooleanArgument(AODS_SKIP_EXPRESSION_STATEMENTS));
			if (isDefined(USE_SIMPLE_CLASS_NAMES)) conf.useSimpleClassNames(getBooleanArgument(USE_SIMPLE_CLASS_NAMES));
		}
		if (isDefined(USE_EXTERNAL_JUNIT_RUNNER)) conf.useExternalJUnitRunner(getBooleanArgument(USE_EXTERNAL_JUNIT_RUNNER));
		if (isDefined(USE_PARALLEL_JUNIT_RUNNER)) conf.useParallelExternalJUnitRunner(getBooleanArgument(USE_PARALLEL_JUNIT_RUNNER));
		if (isDefined(PARALLEL_JUNIT_RUNNER_THREADS)) conf.parallelExternalJUnitRunnerThreads(getIntArgument(PARALLEL_JUNIT_RUNNER_THREADS));
		if (isDefined(USE_SOCKETS)) conf.useSockets(getBooleanArgument(USE_SOCKETS));
		boolean junitPathDefined = false;
		boolean hamcrestPathDefined = false;
		if (isDefined(JUNIT_PATH)) {
			junitPathDefined = true;
			conf.junitPath(getStringArgument(JUNIT_PATH));
		}
		if (isDefined(HAMCREST_PATH)) {
			hamcrestPathDefined = true;
			conf.hamcrestPath(getStringArgument(HAMCREST_PATH));
		}
		if (!useExternalMutants) {
			for (String bannedField : stringArgumentsAsArray(getStringArgument(BANNED_FIELDS))) {
				conf.addBannedField(bannedField);
			}
			for (String bannedMethod : stringArgumentsAsArray(getStringArgument(BANNED_METHODS))) {
				conf.addBannedMethod(bannedMethod);
			}
			for (String allowedMember : stringArgumentsAsArray(getStringArgument(ALLOWED_MEMBERS))) {
				conf.addAllowedMember(allowedMember);
			}
			conf.ignoreMutGenLimit(getBooleanArgument(MUTGENLIMIT));
			conf.allowNumericLiteralVariations(getBooleanArgument(ALLOW_NUMERIC_LITERAL_VARIATIONS));	
			if (isDefined(WRITE_PROLOGUE)) conf.writePrologue(getBooleanArgument(WRITE_PROLOGUE));
		}
		for (String allowedPackageToReload : stringArgumentsAsArray(getStringArgument(ALLOWED_PACKAGES_TO_RELOAD))) {
			conf.addPackageToReload(allowedPackageToReload);
		}
		conf.runMutationScore(getBooleanArgument(MUTATION_SCORE));
		if (conf.runMutationScore()) conf.testsBinDir(getStringArgument(TESTS_BIN_DIR));
		if (conf.runMutationScore()) conf.showSurvivingMutants(getBooleanArgument(SHOW_SURVIVING_MUTANTS));
		if (conf.runMutationScore()) {
			for (String testClass : stringArgumentsAsArray(getStringArgument(TESTS))) {
				conf.addTestClass(testClass);
			}
		}
		if (conf.runMutationScore()) conf.quickDeath(getBooleanArgument(QUICK_DEATH));
		if (conf.runMutationScore()) conf.showSurvivingMutants(getBooleanArgument(SHOW_SURVIVING_MUTANTS));
		if (conf.runMutationScore()) conf.toughnessAnalysis(getBooleanArgument(MUTATION_SCORE_TOUGHNESS_ANALYSIS));
		if (conf.runMutationScore()) conf.dynamicSubsumption(getBooleanArgument(DYNAMIC_SUBSUMPTION_ANALYSIS));
		if (conf.runMutationScore() && conf.dynamicSubsumption()) conf.dynamicSubsumptionOutput(getStringArgument(DYNAMIC_SUBSUMPTION_FOLDER));
		if (conf.runMutationScore() && conf.dynamicSubsumption()) conf.reduceDynamicSubsumptionGraph(getBooleanArgument(DYNAMIC_SUBSUMPTION_REDUCE_GRAPH));
		if (conf.runMutationScore() && isDefined(TEST_TIMEOUT)) conf.testTimeout(getLongArgument(TEST_TIMEOUT));
		if (conf.runMutationScore() && isDefined(DISCARD_TIMEOUT)) conf.discardTimeout(getLongArgument(DISCARD_TIMEOUT));
		if (!useExternalMutants && conf.runMutationScore()) conf.outputMutationsInfo(getBooleanArgument(OUPUT_MUTANT_MUTATIONS));	
		String e = loadInheritedBasicConfig(junitPathDefined, hamcrestPathDefined, conf);
		if (e != null) throw new IllegalStateException("Bad inherited configuration : " + e);
		String validationError = conf.validate();
		if (validationError != null) throw new IllegalStateException("Bad configuration : " + validationError);
		return conf;
	}
	
	private String loadInheritedBasicConfig(boolean junitDefined, boolean hamcrestDefined, Config conf) {
		if (junitDefined && hamcrestDefined) return null;
		Configuration inheritedConfig = null;
		String configFile = null;
		if (isDefined(Config_key.INHERITED_BASIC_CONFIG)) {
			configFile = getStringArgument(Config_key.INHERITED_BASIC_CONFIG);
		} else {
			configFile = DEFAULT_BASIC_PROPERTIES;
		}
		try {
			inheritedConfig = new PropertiesConfiguration(configFile);
		} catch (ConfigurationException e) {
			return ExceptionUtils.getFullStackTrace(e);
		}
		if (!junitDefined && isDefined(Config_key.JUNIT_PATH, inheritedConfig)) {
			conf.junitPath(getStringArgument(Config_key.JUNIT_PATH, inheritedConfig));
		}
		if (!hamcrestDefined && isDefined(Config_key.HAMCREST_PATH, inheritedConfig)) {
			conf.hamcrestPath(getStringArgument(Config_key.HAMCREST_PATH, inheritedConfig));
		}
		return null;
	}
	
	public void saveConfig(Config conf, String path) throws ConfigurationException {
		DefaultConfigurationBuilder confBuilder = new DefaultConfigurationBuilder(path);
		Config_key[] configKeys = Config_key.values();
		for (Config_key ck : configKeys) {
			if (argumentExist(ck)) {
				if (isBooleanKey(ck)) {
					confBuilder.addProperty(ck.getKey(), getBooleanArgument(ck));
				} else if (isIntKey(ck)) {
					confBuilder.addProperty(ck.getKey(), getIntArgument(ck));
				} else {
					confBuilder.addProperty(ck.getKey(), getStringArgument(ck));
				}
			}
		}
		confBuilder.save();
	}
	
	//TODO: comment
	public String getStringArgument(Config_key key) {
		return getStringArgument(key, this.config);
	}
	
	public String getStringArgument(Config_key key, Configuration config) {
		if (!argumentExist(key, config) || isBooleanKey(key) || isIntKey(key)) {
			return "";
		}
		return config.getString(key.getKey());
	}
	
	//TODO: comment
	public boolean getBooleanArgument(Config_key key) {
		return getBooleanArgument(key, this.config);
	}
	
	public boolean getBooleanArgument(Config_key key, Configuration config) {
		if (!argumentExist(key, config) || !isBooleanKey(key) || isIntKey(key)) return false;
		return config.getBoolean(key.getKey());
	}
	
	//TODO: comment
	public int getIntArgument(Config_key key) {
		return getIntArgument(key, this.config);
	}
	
	public int getIntArgument(Config_key key, Configuration config) {
		if (!argumentExist(key, config) || isBooleanKey(key) || !isIntKey(key)) return 0;
		return config.getInt(key.getKey());
	}
	
	//TODO: comment
	public long getLongArgument(Config_key key) {
		return getLongArgument(key, this.config);
	}
	
	public long getLongArgument(Config_key key, Configuration config) {
		if (!argumentExist(key, config) || isBooleanKey(key) || isIntKey(key)) return 0;
		return config.getLong(key.getKey());
	}
	
	//TODO: comment
	public boolean argumentExist(Config_key key) {
		return argumentExist(key, this.config);
	}
	
	public boolean argumentExist(Config_key key, Configuration config) {
		if (config.containsKey(key.getKey())) {
			if (isBooleanKey(key)) {
				try {
					config.getBoolean(key.getKey());
					return true;
				} catch (ConversionException ex) {
					return false;
				}
			} else {
				return !config.getString(key.getKey()).trim().isEmpty();
			}
		} else {
			return false;
		}
	}
	
	public boolean isDefined(Config_key key) {
		return isDefined(key, this.config);
	}
	
	public boolean isDefined(Config_key key, Configuration config) {
		return config.containsKey(key.getKey());
	}
	
	public boolean isBooleanKey(Config_key key) {
		switch (key) {
			case ALLOW_CLASS_MUTATIONS:
			case ALLOW_FIELD_MUTATIONS:
			case QUICK_DEATH:
			case FULL_VERBOSE:
			case DISABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS:
			case WRAP_PRIMITIVE_TO_OBJECT_ASSIGNMENTS:
			case APPLY_REF_PRVO_TO_METHODCALLS_STATEMENTS:
			case SHOW_SURVIVING_MUTANTS:
			case PRVO_ENABLE_SAME_LENGTH:
			case PRVO_ENABLE_INCREASE_LENGTH:
			case PRVO_ENABLE_DECREASE_LENGTH:
			case PRVO_ENABLE_ONE_BY_TWO:
			case PRVO_ENABLE_TWO_BY_ONE:
			case PRVO_ENABLE_ALL_BY_ONE_LEFT:
			case PRVO_ENABLE_ALL_BY_ONE_RIGHT:
			case PRVO_ENABLE_LITERAL_MUTATIONS:
			case PRVO_ENABLE_OBJECT_ALLOCATION_MUTATIONS:
			case PRVO_ENABLE_ARRAY_ALLOCATION_MUTATIONS:
			case PRVO_ENABLE_NON_NAVIGATION_EXPRESSION_MUTATIONS:
			case PRVO_ENABLE_SUPER:
			case PRVO_ENABLE_THIS:
			case PRVO_ENABLE_REPLACEMENT_WITH_LITERALS:
			case PRVO_ENABLE_NULL_LITERAL:
			case PRVO_ENABLE_TRUE_LITERAL:
			case PRVO_ENABLE_FALSE_LITERAL:
			case PRVO_ENABLE_EMPTY_STRING_LITERAL:
			case PRVO_ENABLE_ZERO_LITERAL:
			case PRVO_ENABLE_ONE_LITERAL:
			case PRVO_ENABLE_STRING_LITERALS:
			case ALLOW_NUMERIC_LITERAL_VARIATIONS:
			case PRVO_ALLOW_FINAL_MEMBERS:
			case PRVO_ENABLE_RELAXED_TYPES:
			case PRVO_ENABLE_AUTOBOXING:
			case PRVO_ENABLE_INHERITED_ELEMENTS:
			case PRVO_ENABLE_STATIC_FROM_NON_STATIC_EXP:
			case PRVO_SMART_MODE_ARITHMETIC_OP_SHORTCUTS:
			case PRVO_SMART_MODE_ASSIGNMENTS:
			case MUTATION_SCORE:
			case ROR_REPLACE_WITH_TRUE:
			case ROR_REPLACE_WITH_FALSE:
			case ROR_SMART_LITERAL_REPLACE:
			case COR_USE_AND_OP:
			case COR_USE_OR_OP:
			case COR_USE_XOR_OP:
			case COR_USE_BIT_AND_OP:
			case COR_USE_BIT_OR__OP:
			case MUTATION_SCORE_TOUGHNESS_ANALYSIS:
			case DYNAMIC_SUBSUMPTION_ANALYSIS:
			case DYNAMIC_SUBSUMPTION_REDUCE_GRAPH:
			case USE_SIMPLE_CLASS_NAMES:
			case OUPUT_MUTANT_MUTATIONS:
			case USE_EXTERNAL_JUNIT_RUNNER:
			case USE_PARALLEL_JUNIT_RUNNER:	
			case USE_EXTERNAL_MUTANTS:
			case USE_SOCKETS:
			case WRITE_PROLOGUE:
			case BEE_SKIP_EQUIVALENT_MUTATION:
			case BEE_SKIP_CONSTANTS:
			case BEE_SCAN_EXPRESSIONS:
			case AOIS_SKIP_FINAL:
			case AODS_SKIP_EXPRESSION_STATEMENTS:
			case MUTGENLIMIT: return true;
			default : return false;
		}
	}
	
	public boolean isIntKey(Config_key key) {
		switch (key) {
			case PARALLEL_JUNIT_RUNNER_THREADS:
			case RELOADER_INSTANCES_LIMIT:
			case GENERATIONS : return true;
			default : return false;
		}
	}
	
	public boolean isLongKey(Config_key key) {
		switch (key) {
			case TEST_TIMEOUT : return true;
			case DISCARD_TIMEOUT : return true;
			default : return false;
		}
	}
	
	public String[] stringArgumentsAsArray(String arguments) {
		String[] args = arguments.split(" ");
		List<String> filteredArgs = new LinkedList<>();
		for (int a = 0; a < args.length; a++) {
			String arg = args[a];
			if (!arg.trim().isEmpty()) {
				filteredArgs.add(arg);
			}
		}
		return filteredArgs.toArray(new String[filteredArgs.size()]);
	}
	
}
