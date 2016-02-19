package mujava2.api.mutator;

import java.util.Collection;
import java.util.LinkedList;

import mujava.api.MutationOperator;

public class MutationRequest {
	
	public final static String MUTATE_FIELDS= "0_mutateFields";
	public final static String MUTATE_CLASS= "0_mutateClass";
	
	private final String fullyQualifiedClassName;
	private final Collection<MutationOperator> operators;
	private final Collection<String> methods;
	
	public MutationRequest (String classToMutate, Collection<MutationOperator> ops, Collection<String> methods, boolean mutateClassFields, boolean mutateClass) {
		if (classToMutate == null) throw new IllegalArgumentException("classToMutate is null");
		if (classToMutate.isEmpty()) throw new IllegalArgumentException("classToMutate is empty");
		if (ops == null) throw new IllegalArgumentException("Operators collection is null");
		if (ops.isEmpty()) throw new IllegalArgumentException("Empty operators collection");
		if (methods == null) throw new IllegalArgumentException("Methods collection is null");
		if (methods.isEmpty()) throw new IllegalArgumentException("Empty methods collection");
		if (!mutateClass && methods.contains(MUTATE_CLASS)) throw new IllegalArgumentException("mutateClass argument is false but methods collection is forcing to mutate class");
		if (!mutateClassFields && methods.contains(MUTATE_FIELDS)) throw new IllegalArgumentException("mutateClassFields argument is false but methods collection is forcing to mutate class fields");
		if (!methods.contains(MUTATE_CLASS) && mutateClass) methods.add(MUTATE_CLASS);
		if (!methods.contains(MUTATE_FIELDS) && mutateClassFields) methods.add(MUTATE_FIELDS);
		this.fullyQualifiedClassName = classToMutate;
		this.operators = new LinkedList<MutationOperator>(ops);
		this.methods = new LinkedList<String>(methods);
	}

	public Collection<MutationOperator> getOperators() {
		return operators;
	}

	public Collection<String> getMethods() {
		return methods;
	}
	
	public String getClassToMutate() {
		return this.fullyQualifiedClassName;
	}
	
}
