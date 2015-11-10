package mujava.util;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.SystemUtils;

/**
 * This class allows access to a configuration loaded from a properties file
 * but also allows access to some values that either doesn't belong to the
 * properties file (e.g.: file separator) and properties that need arguments to
 * from an usable value (e.g.: output dir and compilation sandbox that need the
 * file separator  
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 2.0b
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
		//MUTATION BASIC
		//MUTATION ADVANCED
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
			public String getFlag() {
				return NO_FLAG;
			}
		},
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
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_INCREASE_LENGTH {
			public String getKey() {
				return "mutation.advanced.prvo.enableIncreaseLenght";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_DECREASE_LENGTH {
			public String getKey() {
				return "mutation.advanced.prvo.enableDecreaseLenght";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_ONE_BY_TWO {
			public String getKey() {
				return "mutation.advanced.prvo.enableOneByTwo";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_TWO_BY_ONE {
			public String getKey() {
				return "mutation.advanced.prvo.enableTwoByOne";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_ALL_BY_ONE_LEFT {
			public String getKey() {
				return "mutation.advanced.prvo.enableAllByOneLeft";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_ALL_BY_ONE_RIGHT {
			public String getKey() {
				return "mutation.advanced.prvo.enableAllByOneRight";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_SUPER {
			public String getKey() {
				return "mutation.advanced.prvo.enableSuper";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_THIS {
			public String getKey() {
				return "mutation.advanced.prvo.enableThis";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_REPLACEMENT_WITH_LITERALS {
			public String getKey() {
				return "mutation.advanced.prvo.enableReplacementWithLiterals";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_NULL_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableNullLiteral";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_TRUE_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableTrueLiteral";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_FALSE_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableFalseLiteral";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_EMPTY_STRING_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableEmptyString";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_ZERO_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableZeroLiteral";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_ONE_LITERAL {
			public String getKey() {
				return "mutation.advanced.prvo.enableOneLiteral";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		PRVO_ENABLE_STRING_LITERALS {
			public String getKey() {
				return "mutation.advanced.prvo.enableStringLiterals";
			}
			public String getFlag() {
				return NO_FLAG;
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
		//MUTATION ADVANCED PRVO
		//MUTATION ADVANCED ROR
		ROR_REPLACE_WITH_TRUE {
			public String getKey() {
				return "mutation.advanced.ror.replaceWithTrue";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		ROR_REPLACE_WITH_FALSE {
			public String getKey() {
				return "mutation.advanced.ror.replaceWithFalse";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		//MUTATION ADVANCED ROR
		//MUTATION ADVANCED COR
		COR_USE_AND_OP {
			public String getKey() {
				return "mutation.advanced.cor.andOperator";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		COR_USE_OR_OP {
			public String getKey() {
				return "mutation.advanced.cor.orOperator";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		COR_USE_XOR_OP {
			public String getKey() {
				return "mutation.advanced.cor.xorOperator";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		COR_USE_BIT_AND_OP {
			public String getKey() {
				return "mutation.advanced.cor.bitAndOperator";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		},
		COR_USE_BIT_OR__OP {
			public String getKey() {
				return "mutation.advanced.cor.bitOrOperator";
			}
			public String getFlag() {
				return NO_FLAG;
			}
		};
		//MUTATION ADVANCED COR
		public static final String NO_FLAG="-?";
		
		public abstract String getKey();
		public abstract String getFlag();
	};
	
	/**
	 * The path to a default .properties file
	 */
	public static final String DEFAULT_PROPERTIES = "default.properties";
	
	/**
	 * The {@code StrykerConfig} instance that will be returned by {@link ConfigReader#getInstance(String)}
	 */
	private static ConfigReader instance = null;

	/**
	 * The properties file that will be loaded
	 */
	private String propertiesFile;
	/**
	 * The configuration loaded, especified by {@link ConfigReader#propertiesFile}
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
		Config conf = new Config(getStringArgument(Config_key.ORIGINAL_SOURCE_DIR), getStringArgument(Config_key.ORIGINAL_BIN_DIR), getStringArgument(Config_key.MUTANTS_DIR));
		conf.classToMutate(getStringArgument(Config_key.CLASS));
		for (String methodToMutate : stringArgumentsAsArray(getStringArgument(Config_key.METHODS))) {
			conf.addMethodToMutate(methodToMutate);
		}
		for (String op : stringArgumentsAsArray(getStringArgument(Config_key.OPERATORS))) {
			conf.addOperator(op);
		}
		if (isDefined(Config_key.FULL_VERBOSE)) conf.fullVerboseMode(getBooleanArgument(Config_key.FULL_VERBOSE)); else conf.fullVerboseMode(false);
		if (isDefined(Config_key.ALLOW_CLASS_MUTATIONS)) conf.allowClassMutations(getBooleanArgument(Config_key.ALLOW_CLASS_MUTATIONS));
		if (isDefined(Config_key.ALLOW_FIELD_MUTATIONS)) conf.allowFieldMutations(getBooleanArgument(Config_key.ALLOW_FIELD_MUTATIONS));
		if (isDefined(Config_key.DISABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS)) conf.allowPrimitiveToObjectAssignments(getBooleanArgument(Config_key.DISABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS));
		if (isDefined(Config_key.WRAP_PRIMITIVE_TO_OBJECT_ASSIGNMENTS)) conf.wrapPrimitiveToObjectAssignments(getBooleanArgument(Config_key.WRAP_PRIMITIVE_TO_OBJECT_ASSIGNMENTS));
		if (isDefined(Config_key.APPLY_REF_PRVO_TO_METHODCALLS_STATEMENTS)) conf.applyRefinedPRVOInMethodCallStatements(getBooleanArgument(Config_key.APPLY_REF_PRVO_TO_METHODCALLS_STATEMENTS));
		if (isDefined(Config_key.GENERATIONS)) conf.generation(getIntArgument(Config_key.GENERATIONS)); else conf.generation(1);
		if (isDefined(Config_key.ROR_REPLACE_WITH_TRUE)) conf.rorReplaceWithTrue(getBooleanArgument(Config_key.ROR_REPLACE_WITH_TRUE));
		if (isDefined(Config_key.ROR_REPLACE_WITH_FALSE)) conf.rorReplaceWithFalse(getBooleanArgument(Config_key.ROR_REPLACE_WITH_FALSE));
		if (isDefined(Config_key.COR_USE_AND_OP)) conf.corUseAndOp(getBooleanArgument(Config_key.COR_USE_AND_OP));
		if (isDefined(Config_key.COR_USE_OR_OP)) conf.corUseOrOp(getBooleanArgument(Config_key.COR_USE_OR_OP));
		if (isDefined(Config_key.COR_USE_XOR_OP)) conf.corUseXorOp(getBooleanArgument(Config_key.COR_USE_XOR_OP));
		if (isDefined(Config_key.COR_USE_BIT_AND_OP)) conf.corUseBitAndOp(getBooleanArgument(Config_key.COR_USE_BIT_AND_OP));
		if (isDefined(Config_key.COR_USE_BIT_OR__OP)) conf.corUseBitOrOp(getBooleanArgument(Config_key.COR_USE_BIT_OR__OP));
		if (isDefined(Config_key.PRVO_ENABLE_SAME_LENGTH)) conf.prvoSameLenght(getBooleanArgument(Config_key.PRVO_ENABLE_SAME_LENGTH));
		if (isDefined(Config_key.PRVO_ENABLE_INCREASE_LENGTH)) conf.prvoIncreaseLength(getBooleanArgument(Config_key.PRVO_ENABLE_INCREASE_LENGTH));
		if (isDefined(Config_key.PRVO_ENABLE_DECREASE_LENGTH)) conf.prvoDecreaseLength(getBooleanArgument(Config_key.PRVO_ENABLE_DECREASE_LENGTH));
		if (isDefined(Config_key.PRVO_ENABLE_ONE_BY_TWO)) conf.prvoOneByTwo(getBooleanArgument(Config_key.PRVO_ENABLE_ONE_BY_TWO));
		if (isDefined(Config_key.PRVO_ENABLE_TWO_BY_ONE)) conf.prvoTwoByOne(getBooleanArgument(Config_key.PRVO_ENABLE_TWO_BY_ONE));
		if (isDefined(Config_key.PRVO_ENABLE_ALL_BY_ONE_LEFT)) conf.prvoAllByOneLeft(getBooleanArgument(Config_key.PRVO_ENABLE_ALL_BY_ONE_LEFT));
		if (isDefined(Config_key.PRVO_ENABLE_ALL_BY_ONE_RIGHT)) conf.prvoAllByOneRight(getBooleanArgument(Config_key.PRVO_ENABLE_ALL_BY_ONE_RIGHT));
		if (isDefined(Config_key.PRVO_ENABLE_SUPER)) conf.prvoUseSuper(getBooleanArgument(Config_key.PRVO_ENABLE_SUPER));
		if (isDefined(Config_key.PRVO_ENABLE_THIS)) conf.prvoUseThis(getBooleanArgument(Config_key.PRVO_ENABLE_THIS));
		if (isDefined(Config_key.PRVO_ENABLE_REPLACEMENT_WITH_LITERALS)) conf.prvoReplacementWithLiterals(getBooleanArgument(Config_key.PRVO_ENABLE_REPLACEMENT_WITH_LITERALS));
		if (isDefined(Config_key.PRVO_ENABLE_NULL_LITERAL)) conf.prvoUseNullLiteral(getBooleanArgument(Config_key.PRVO_ENABLE_NULL_LITERAL));
		if (isDefined(Config_key.PRVO_ENABLE_TRUE_LITERAL)) conf.prvoUseTrueLiteral(getBooleanArgument(Config_key.PRVO_ENABLE_TRUE_LITERAL));
		if (isDefined(Config_key.PRVO_ENABLE_FALSE_LITERAL)) conf.prvoUseFalseLiteral(getBooleanArgument(Config_key.PRVO_ENABLE_FALSE_LITERAL));
		if (isDefined(Config_key.PRVO_ENABLE_EMPTY_STRING_LITERAL)) conf.prvoUseEmptyStringLiteral(getBooleanArgument(Config_key.PRVO_ENABLE_EMPTY_STRING_LITERAL));
		if (isDefined(Config_key.PRVO_ENABLE_ZERO_LITERAL)) conf.prvoUseZeroLiteral(getBooleanArgument(Config_key.PRVO_ENABLE_ZERO_LITERAL));
		if (isDefined(Config_key.PRVO_ENABLE_ONE_LITERAL)) conf.prvoUseOneLiteral(getBooleanArgument(Config_key.PRVO_ENABLE_ONE_LITERAL));
		if (isDefined(Config_key.PRVO_ENABLE_STRING_LITERALS)) conf.prvoUseStringLiterals(getBooleanArgument(Config_key.PRVO_ENABLE_STRING_LITERALS));
		for (String bannedField : stringArgumentsAsArray(getStringArgument(Config_key.BANNED_FIELDS))) {
			conf.addBannedField(bannedField);
		}
		for (String bannedMethod : stringArgumentsAsArray(getStringArgument(Config_key.BANNED_METHODS))) {
			conf.addBannedMethod(bannedMethod);
		}
		conf.ignoreMutGenLimit(getBooleanArgument(Config_key.MUTGENLIMIT));
		for (String allowedPackageToReload : stringArgumentsAsArray(getStringArgument(Config_key.ALLOWED_PACKAGES_TO_RELOAD))) {
			conf.addPackageToReload(allowedPackageToReload);
		}
		conf.allowNumericLiteralVariations(getBooleanArgument(Config_key.ALLOW_NUMERIC_LITERAL_VARIATIONS));
		conf.runMutationScore(getBooleanArgument(Config_key.MUTATION_SCORE));
		if (conf.runMutationScore()) conf.testsBinDir(getStringArgument(Config_key.TESTS_BIN_DIR));
		if (conf.runMutationScore()) conf.showSurvivingMutants(getBooleanArgument(Config_key.SHOW_SURVIVING_MUTANTS));
		if (conf.runMutationScore()) {
			for (String testClass : stringArgumentsAsArray(getStringArgument(Config_key.TESTS))) {
				conf.addTestClass(testClass);
			}
		}
		if (conf.runMutationScore()) conf.quickDeath(getBooleanArgument(Config_key.QUICK_DEATH));
		if (conf.runMutationScore()) conf.showSurvivingMutants(getBooleanArgument(Config_key.SHOW_SURVIVING_MUTANTS));
		if (conf.runMutationScore()) conf.toughnessAnalysis(getBooleanArgument(Config_key.MUTATION_SCORE_TOUGHNESS_ANALYSIS));
		String validationError = conf.validate();
		if (validationError != null) throw new IllegalStateException("Bad configuration : " + validationError);
		return conf;
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
		if (!argumentExist(key) || isBooleanKey(key) || isIntKey(key)) {
			return "";
		}
		return this.config.getString(key.getKey());
	}
	
	//TODO: comment
	public boolean getBooleanArgument(Config_key key) {
		if (!argumentExist(key) || !isBooleanKey(key) || isIntKey(key)) return false;
		return this.config.getBoolean(key.getKey());
	}
	
	//TODO: comment
	public int getIntArgument(Config_key key) {
		if (!argumentExist(key) || isBooleanKey(key) || !isIntKey(key)) return 0;
		return this.config.getInt(key.getKey());
	}
	
	//TODO: comment
	public boolean argumentExist(Config_key key) {
		if (this.config.containsKey(key.getKey())) {
			if (isBooleanKey(key)) {
				try {
					this.config.getBoolean(key.getKey());
					return true;
				} catch (ConversionException ex) {
					return false;
				}
			} else {
				return !this.config.getString(key.getKey()).trim().isEmpty();
			}
		} else {
			return false;
		}
	}
	
	public boolean isDefined(Config_key key) {
		return this.config.containsKey(key.getKey());
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
			case MUTATION_SCORE:
			case ROR_REPLACE_WITH_TRUE:
			case ROR_REPLACE_WITH_FALSE:
			case COR_USE_AND_OP:
			case COR_USE_OR_OP:
			case COR_USE_XOR_OP:
			case COR_USE_BIT_AND_OP:
			case COR_USE_BIT_OR__OP:
			case MUTATION_SCORE_TOUGHNESS_ANALYSIS:
			case MUTGENLIMIT: return true;
			default : return false;
		}
	}
	
	public boolean isIntKey(Config_key key) {
		switch (key) {
			case RELOADER_INSTANCES_LIMIT:
			case GENERATIONS : return true;
			default : return false;
		}
	}
	
	public String[] stringArgumentsAsArray(String arguments) {
		return arguments.split(" ");
	}
	
}
