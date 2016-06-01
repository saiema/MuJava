package mujava2.api.mutator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;

import mujava.api.MutationOperator;

/**
 * A simple class to specify what class will be mutated, with which operators and on which methods.
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1
 */
public class MutationRequest {
	
	/**
	 * Internal use only, a constant to use when mutating class fields.
	 */
	public final static String MUTATE_FIELDS= "0_mutateFields";
	/**
	 * Internal use only, a constant to use when mutating class declarations.
	 */
	public final static String MUTATE_CLASS= "0_mutateClass";
	
	/**
	 * Where to look for the class (the root folder of the class)
	 */
	private final String location;
	
	/**
	 * The fully qualified name of the class to mutate, e.g.: java.util.List
	 */
	private final String fullyQualifiedClassName;
	/**
	 * The simple name of the class to mutate, e.g.: List
	 */
	private final String simpleClassName;
	/**
	 * The mutation operators to use
	 */
	private Collection<MutationOperator> operators;
	/**
	 * The methods to mutate
	 */
	private Collection<String> methods;
	/**
	 * How much mutation generations to create
	 */
	private int generations;
	
	/**
	 * Creates a new mutation request.
	 * 
	 * @param location					:	the root folder of the class
	 * @param classToMutate				:	the fully qualified name of the class to mutate
	 * @param ops						:	the mutation operators to use
	 * @param methods					:	the methods to mutate
	 * @param mutateClassFields			:	if class fields will be mutated or not
	 * @param mutateClass				:	if class declaration will be mutated or not
	 */
	public MutationRequest (String location, String classToMutate, Collection<MutationOperator> ops, Collection<String> methods, boolean mutateClassFields, boolean mutateClass) {
		this(location, classToMutate, ops, methods, mutateClassFields, mutateClass, 1);
	}
	
	/**
	 * Creates a new mutation request.
	 * 
	 * @param location					:	the root folder of the class
	 * @param classToMutate				:	the fully qualified name of the class to mutate
	 * @param ops						:	the mutation operators to use
	 * @param methods					:	the methods to mutate
	 * @param mutateClassFields			:	if class fields will be mutated or not
	 * @param mutateClass				:	if class declaration will be mutated or not
	 * @param generations				:	how many generations to generate
	 */
	public MutationRequest (String location, String classToMutate, Collection<MutationOperator> ops, Collection<String> methods, boolean mutateClassFields, boolean mutateClass, Integer generations) {
		if (classToMutate == null) throw new IllegalArgumentException("classToMutate is null");
		if (classToMutate.isEmpty()) throw new IllegalArgumentException("classToMutate is empty");
		if (ops == null) throw new IllegalArgumentException("Operators collection is null");
		if (ops.isEmpty()) throw new IllegalArgumentException("Empty operators collection");
		if (methods == null) throw new IllegalArgumentException("Methods collection is null");
		if (methods.isEmpty()) throw new IllegalArgumentException("Empty methods collection");
		if (generations <= 0) throw new IllegalArgumentException();
		if (!mutateClass && methods.contains(MUTATE_CLASS)) throw new IllegalArgumentException("mutateClass argument is false but methods collection is forcing to mutate class");
		if (!mutateClassFields && methods.contains(MUTATE_FIELDS)) throw new IllegalArgumentException("mutateClassFields argument is false but methods collection is forcing to mutate class fields");
		if (!methods.contains(MUTATE_CLASS) && mutateClass) methods.add(MUTATE_CLASS);
		if (!methods.contains(MUTATE_FIELDS) && mutateClassFields) methods.add(MUTATE_FIELDS);
		Path path = Paths.get(location);
		if (Files.notExists(path)) {
		  throw new IllegalArgumentException("location (" + location + ") doesn't exists");
		}
		this.location = location;
		this.fullyQualifiedClassName = classToMutate;
		String[] terms = this.fullyQualifiedClassName.split("\\.");
		this.simpleClassName = terms[terms.length - 1];
		this.operators = new LinkedList<MutationOperator>(ops);
		this.methods = new LinkedList<String>(methods);
		this.generations = generations;
	}
		
	/**
	 * @return The mutation operators to use
	 */
	public Collection<MutationOperator> getOperators() {
		return operators;
	}

	/**
	 * @return The methods to mutate
	 */
	public Collection<String> getMethods() {
		return methods;
	}
	
	/**
	 * @return the root folder of the class
	 */
	public String getLocation() {
		return this.location;
	}
	
	/**
	 * @return The fully qualified name of the class to mutate
	 */
	public String getClassToMutate() {
		return this.fullyQualifiedClassName;
	}
	
	/**
	 * @return The simple class name of the class to mutate
	 */
	public String getClassToMutateSimpleName() {
		return this.simpleClassName;
	}
	
	/**
	 * @return how much mutation generations to create
	 */
	public int getGenerations() {
		return this.generations;
	}
	
	public void changeGenerations(int generations) {
		if (generations <= 0) throw new IllegalArgumentException("generations argument must be greater than one (1)");
		this.generations = generations;
	}
	
	//TODO: implement setters for every non-final field
	
	@Override
	public MutationRequest clone() {
		Collection<MutationOperator> clonedOperators = new LinkedList<>();
		clonedOperators.addAll(operators);
		Collection<String> clonedMethods = new LinkedList<>();
		clonedMethods.addAll(methods);
		boolean mutateClass = clonedMethods.remove(MUTATE_CLASS);
		boolean mutateFields = clonedMethods.remove(MUTATE_FIELDS);
		String className = this.fullyQualifiedClassName;
		String location = this.location;
		int generations = this.generations;
		return new MutationRequest(location, className, clonedOperators, clonedMethods, mutateFields, mutateClass, generations);
	}
	
}
