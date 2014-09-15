package mujava.app;

import mujava.api.Mutant;

/**
 * This class represents a request to generate mutants
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 1.2
 * @see Mutator
 */
public class MutationRequest {
	public final static String MUTATE_FIELDS= "0_mutateFields";
	public final static String MUTATE_CLASS= "0_mutateClass";
	
	/**
	 * @param clazz				: 	The class to be mutated, must include package if it has one (eg: util.List) : {@code String}
	 * @param methods			:	The methods to be mutated, just the name (eg: toString) : {@code String[]}
	 * @param ops				:	Mutation operators to be used : {@code Mutant[]}
	 * @param inputDir			:	Root directory of the source files to be mutated : {@code String}
	 * @param outputDir			:	Root directory where mutants will be created : {@code String}
	 * @param mutateClassFields	:	used to set if class fields should be mutated or not : {@code boolean}
	 * @param mutateClass		:	used to set if class should be mutated or not : {@code boolean}
	 */
	public MutationRequest(String clazz, String[] methods, Mutant[] ops, String inputDir, String outputDir, boolean mutateClassFields, boolean mutateClass) {
		this.clazz 					= clazz;		
		this.ops 					= ops;
		this.inputDir				= inputDir;
		this.outputDir				= outputDir;
		if (mutateClassFields || mutateClass) {
			int extra = (mutateClassFields?1:0) + (mutateClass?1:0);
			String[] nmethods = new String[methods.length+extra];
			int i = 0;
			for (String m : methods) {
				nmethods[i] = m;
				i++;
			}
			if (mutateClassFields) {
				nmethods[i] = MUTATE_FIELDS;
				i++;
			}
			if (mutateClass) {
				nmethods[i] = MUTATE_CLASS;
			}
		} else {
			this.methods			= methods;
		}
	}
	
	/**
	 * @param clazz				: 	The class to be mutated, must include package if it has one (eg: util.List) : {@code String}
	 * @param methods			:	The methods to be mutated, just the name (eg: toString) : {@code String[]}
	 * @param ops				:	Mutation operators to be used : {@code Mutant[]}
	 * @param inputDir			:	Root directory of the source files to be mutated : {@code String}
	 * @param outputDir			:	Root directory where mutants will be created : {@code String}
	 * @param mutateClassFields	:	used to set if class fields should be mutated or not : {@code boolean}
	 */
	public MutationRequest(String clazz, String[] methods, Mutant[] ops, String inputDir, String outputDir, boolean mutateClassFields) {
		this.clazz 					= clazz;		
		this.ops 					= ops;
		this.inputDir				= inputDir;
		this.outputDir				= outputDir;
		if (mutateClassFields) {
			String[] nmethods = new String[methods.length+1];
			int i = 0;
			for (String m : methods) {
				nmethods[i] = m;
				i++;
			}
			nmethods[methods.length] = MUTATE_FIELDS;
		} else {
			this.methods			= methods;
		}
	}
	
	/**
	 * @param clazz				: 	The class to be mutated, must include package if it has one (eg: util.List) : {@code String}
	 * @param methods			:	The methods to be mutated, just the name (eg: toString) : {@code String[]}
	 * @param ops				:	Mutation operators to be used : {@code Mutant[]}
	 * @param inputDir			:	Root directory of the source files to be mutated : {@code String}
	 * @param outputDir			:	Root directory where mutants will be created : {@code String}
	 */
	public MutationRequest(String clazz, String[] methods, Mutant[] ops, String inputDir, String outputDir) {
		this.clazz 					= clazz;
		this.methods				= methods;
		this.ops 					= ops;
		this.inputDir				= inputDir;
		this.outputDir				= outputDir;
	}
	
	/**
	 * The class to be mutated, must include package if it has one (eg: util.List)
	 */
	public String clazz;
	/**
	 * The methods to be mutated, just the name (eg: toString)
	 * <hr>
	 * <i>if fields and/or class will be mutated then this array must contain the constants {@code MUTATE_FIELDS} and/or {@code MUTATE_CLASS} respectively</i>
	 */
	public String[] methods;
	/**
	 * Mutation operators to be used
	 */
	public Mutant[] ops;
	/**
	 * root directory of the source files to be mutated
	 */
	public String inputDir;
	/**
	 * root directory where mutants will be created
	 */
	public String outputDir;
	
}
