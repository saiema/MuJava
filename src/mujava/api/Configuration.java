package mujava.api;

import java.util.HashMap;
import java.util.Map;

import mujava.app.Core;
import mujava.op.BEE;
import mujava.op.PRVO;
import mujava.op.basic.COR;
import mujava.op.basic.ROR;
import mujava.op.util.MutantCodeWriter;
import mujava.op.util.OLMO;

/**
 * This class allows to specify certain arguments to alter muJava++ behaviour
 * It's just a relation of {@code argument -> value} to be used by any class during runtime
 * e.g.: MutationOperator operators, Writers, Filters, etc. 
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 0.2
 */
public final class Configuration {
	private static Map<String, Object> arguments = new HashMap<String, Object>();
	/**
	 * {@link mujava.api.Api#USE_MUTGENLIMIT}
	 */
	public static final String USE_MUTGENLIMIT = Api.USE_MUTGENLIMIT;
	
	/**
	 * {@link mujava.op.basic.ROR#REPLACE_WITH_TRUE}
	 */
	public static final String REPLACE_WITH_TRUE = ROR.REPLACE_WITH_TRUE;
	
	/**
	 * {@link mujava.op.basic.ROR#REPLACE_WITH_FALSE}
	 */
	public static final String REPLACE_WITH_FALSE = ROR.REPLACE_WITH_FALSE;
	
	/**
	 * {@link mujava.op.basic.COR#ALLOW_LOGICAL_AND}
	 */
	public static final String ALLOW_LOGICAL_AND = COR.ALLOW_LOGICAL_AND;
	
	/**
	 * {@link mujava.op.basic.COR#ALLOW_LOGICAL_OR}
	 */
	public static final String ALLOW_LOGICAL_OR = COR.ALLOW_LOGICAL_OR;
	
	/**
	 * {@link mujava.op.basic.COR#ALLOW_XOR}
	 */
	public static final String ALLOW_XOR = COR.ALLOW_XOR;
	
	/**
	 * {@link mujava.op.basic.COR#ALLOW_BIT_AND}
	 */
	public static final String ALLOW_BIT_AND = COR.ALLOW_BIT_AND;
	
	/**
	 * {@link mujava.op.basic.COR#ALLOW_BIT_OR}
	 */
	public static final String ALLOW_BIT_OR = COR.ALLOW_BIT_OR;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_SUPER}
	 */
	public static final String ENABLE_SUPER = PRVO.ENABLE_SUPER;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_THIS}
	 */
	public static final String ENABLE_THIS = PRVO.ENABLE_THIS;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_REPLACEMENT_WITH_LITERALS}
	 */
	public static final String ENABLE_REPLACEMENT_WITH_LITERALS = PRVO.ENABLE_REPLACEMENT_WITH_LITERALS;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_LITERAL_NULL}
	 */
	public static final String ENABLE_LITERAL_NULL = PRVO.ENABLE_LITERAL_NULL;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_LITERAL_TRUE}
	 */
	public static final String ENABLE_LITERAL_TRUE = PRVO.ENABLE_LITERAL_TRUE;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_LITERAL_FALSE}
	 */
	public static final String ENABLE_LITERAL_FALSE = PRVO.ENABLE_LITERAL_FALSE;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_LITERAL_EMPTY_STRING}
	 */
	public static final String ENABLE_LITERAL_EMPTY_STRING = PRVO.ENABLE_LITERAL_EMPTY_STRING;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_LITERAL_ZERO}
	 */
	public static final String ENABLE_LITERAL_ZERO = PRVO.ENABLE_LITERAL_ZERO;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_LITERAL_ONE}
	 */
	public static final String ENABLE_LITERAL_ONE = PRVO.ENABLE_LITERAL_ONE;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_LITERAL_STRINGS}
	 */
	public static final String ENABLE_LITERAL_STRINGS = PRVO.ENABLE_LITERAL_STRINGS;
	
	/**
	 * {@link mujava.op.PRVO#PROHIBITED_METHODS}
	 */
	public static final String PROHIBITED_METHODS = PRVO.PROHIBITED_METHODS;
	
	/**
	 * {@link mujava.op.PRVO#PROHIBITED_FIELDS}
	 */
	public static final String PROHIBITED_FIELDS = PRVO.PROHIBITED_FIELDS;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_SAME_LENGTH_MUTANTS}
	 */
	public static final String ENABLE_SAME_LENGTH_MUTANTS = PRVO.ENABLE_SAME_LENGTH_MUTANTS;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_INCREASE_LENGTH_MUTANTS}
	 */
	public static final String ENABLE_INCREASE_LENGTH_MUTANTS = PRVO.ENABLE_INCREASE_LENGTH_MUTANTS;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_DECREASE_LENGTH_MUTANTS}
	 */
	public static final String ENABLE_DECREASE_LENGTH_MUTANTS = PRVO.ENABLE_DECREASE_LENGTH_MUTANTS;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_ONE_BY_TWO_MUTANTS}
	 */
	public static final String ENABLE_ONE_BY_TWO_MUTANTS = PRVO.ENABLE_ONE_BY_TWO_MUTANTS;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_TWO_BY_ONE_MUTANTS}
	 */
	public static final String ENABLE_TWO_BY_ONE_MUTANTS = PRVO.ENABLE_TWO_BY_ONE_MUTANTS;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_ALL_BY_ONE_MUTANTS_LEFT}
	 */
	public static final String ENABLE_ALL_BY_ONE_MUTANTS_LEFT = PRVO.ENABLE_ALL_BY_ONE_MUTANTS_LEFT;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_ALL_BY_ONE_MUTANTS_RIGHT}
	 */
	public static final String ENABLE_ALL_BY_ONE_MUTANTS_RIGHT = PRVO.ENABLE_ALL_BY_ONE_MUTANTS_RIGHT;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_NUMBER_LITERALS_VARIATIONS}
	 */
	public static final String ENABLE_NUMBER_LITERALS_VARIATIONS = PRVO.ENABLE_NUMBER_LITERALS_VARIATIONS;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_PRIMITIVE_WRAPPING}
	 */
	public static final String ENABLE_PRIMITIVE_WRAPPING = PRVO.ENABLE_PRIMITIVE_WRAPPING;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS}
	 */
	public static final String ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS = PRVO.ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_REFINEMENT_IN_METHOD_CALL_STATEMENTS}
	 */
	public static final String ENABLE_REFINEMENT_IN_METHOD_CALL_STATEMENTS = PRVO.ENABLE_REFINEMENT_IN_METHOD_CALL_STATEMENTS;
	
	/**
	 * {@link mujava.op.PRVO#ALLOW_FINAL_MEMBERS}
	 */
	public static final String ALLOW_FINAL_MEMBERS = PRVO.ALLOW_FINAL_MEMBERS;
	
	/**
	 * {@link mujava.op.PRVO#ENABLE_RELAXED_TYPES}
	 */
	public static final String ENABLE_RELAXED_TYPES = PRVO.ENABLE_RELAXED_TYPES;
	
	/**
	 * Option to enable/disable priority evaluation of mutations
	 * <p>
	 * This option is disabled by default
	 */
	public static final String PRIORITY_EVALUATE = "mutations.priority.evaluate";
	
	/**
	 * Option to enable/disable the discard of low priority mutations
	 * <p>
	 * This option is disabled by default
	 * <p>
	 * <b>This option requires {@link PRIORITY_EVALUATE} to be enabled</b> 
	 */
	public static final String PRIORITY_LOW_DISCARD = "mutations.priority.low.discard";
	
	/**
	 * Option to enable/disable the discard of neutral priority mutations
	 * <p>
	 * This option is disabled by default
	 * <p>
	 * <b>This option requires {@link PRIORITY_EVALUATE} to be enabled</b>
	 */
	public static final String PRIORITY_NEUTRAL_DISCARD = "mutations.priority.neutral.discard";
	
	/**
	 * {@link mujava.op.util.OLMO#DEBUG}
	 */
	public static final String ENABLE_OLMO_DEBUG_MODE = OLMO.DEBUG;
	
	/**
	 * {@link mujava.app.Core#ENABLE_TOUGHNESS}
	 */
	public static final String ENABLE_TOUGHNESS = Core.ENABLE_TOUGHNESS;
	
	/**
	 * Option to enable/disable pretty print
	 * <p>
	 * This option is disabled by default
	 * <hr>
	 * This option affects
	 * <p>
	 * <li>Blocks (in If, While, and For statements) will not be printed with braces if there is only one statement
	 * <li>else blocks containing only an If statement will be printed as else if
	 */
	public static final String PRETTY_PRINT = "output.print.pretty";
	
	/**
	 * {@link mujava.op.util.MutantCodeWriter#USE_SIMPLE_CLASS_NAMES}
	 */
	public static final String USE_SIMPLE_CLASS_NAMES = MutantCodeWriter.USE_SIMPLE_CLASS_NAMES;
	
	
	/**
	 * {@link mujava.op.BEE#SCAN_FOR_EXPRESSIONS}
	 */
	public static final String SCAN_FOR_EXPRESSIONS = BEE.SCAN_FOR_EXPRESSIONS;
	
	/**
	 * {@link mujava.op.BEE#DISABLE_NEUTRAL_TRUE_FALSE}
	 */
	public static final String DISABLE_NEUTRAL_TRUE_FALSE = BEE.DISABLE_NEUTRAL_TRUE_FALSE;
	
	/**
	 * This options enable/disable the ability to set an argument that has already been set
	 * <p>
	 * This option is enabled by default
	 */
	private static boolean allowArgumentsModifications = true;
	
	/**
	 * Adds an argument an its corresponding value to the configuration
	 * take notice that if the argument already exist it's value will be overwritten 
	 * 
	 * @param argument	:	the argument to add						:	{@code String}
	 * @param value		:	the value associated with the argument	:	{@code Object}
	 */
	public static void add(String argument, Object value) {
		if ((argumentExist(argument) && allowArgumentsModifications) || !argumentExist(argument)) {
			Configuration.arguments.put(argument, value);
		}
	}
	
	/**
	 * Returns the value asociated with an argument, keep in mind that as {@code Map#get(K)} method,
	 * if this method return {@code null} it wont necessarily mean that the argument doesn't exist.
	 * To know if an argument is declared in the Configuration please use the {@code argumentExist(String)} method
	 * 
	 * @param argument	:	the argument whos value is being requested	: {@code String}
	 * @return the value asociated with {@code argument} : {@code Object}
	 * @see Configuration#argumentExist(String)
	 */
	public static Object getValue(String argument) {
		return Configuration.arguments.get(argument);
	}
	
	/**
	 * Used to know if an argument is specified in the configuration
	 * 
	 * @param argument	:	the argument to check	:	{@code String}
	 * @return {@code true} if {@code argument} is specified in the configuration : {@code boolean}
	 */
	public static boolean argumentExist(String argument) {
		return Configuration.arguments.containsKey(argument);
	}
	
	/**
	 * Removes an argument specification from the configuration only if it's specified
	 * 
	 * @param argument	:	the argument to remove from the configuration	:	{@code String}
	 * @see Configuration#argumentExist(String)
	 */
	public static void removeArgument(String argument) {
		if ((argumentExist(argument) && allowArgumentsModifications) || !argumentExist(argument)) {
			Configuration.arguments.remove(argument);
		}
	}
	
	/**
	 * Removes all arguments specified in the configuration
	 */
	public static void clear() {
		if (allowArgumentsModifications) {
			Configuration.arguments = new HashMap<String, Object>();
		}
	}
	
	/**
	 * enable/disable the ability to modify an argument that has already been set
	 * this includes clearing the Configuration
	 * <p>
	 * if modifications are disallowed then this method can't re-allow them
	 */
	public static void allowConfigurationModifications(boolean b) {
		if (allowArgumentsModifications) allowArgumentsModifications = b;
	}
}
