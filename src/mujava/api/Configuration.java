package mujava.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This class allows to specify certain arguments to alter muJava++ behaviour
 * It's just a relation of {@code argument -> value} to be used by any class during runtime
 * e.g.: Mutant operators, Writers, Filters, etc. 
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 0.1u
 */
public final class Configuration {
	private static Map<String, Object> arguments = new HashMap<String, Object>();
	
	/**
	 * Adds an argument an its corresponding value to the configuration
	 * take notice that if the argument already exist it's value will be overwritten 
	 * 
	 * @param argument	:	the argument to add						:	{@code String}
	 * @param value		:	the value associated with the argument	:	{@code Object}
	 */
	public static void add(String argument, Object value) {
		Configuration.arguments.put(argument, value);
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
		if (argumentExist(argument)) {
			Configuration.arguments.remove(argument);
		}
	}
	
	/**
	 * Removes all arguments specified in the configuration
	 */
	public static void clear() {
		Configuration.arguments = new HashMap<String, Object>();
	}
}
