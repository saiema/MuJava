package mujava.app;

import mujava.api.Mutation;

/**
 * This class describes the information of a mutation
 * <p>
 * <li>mutated method	:	the method mutated</li>
 * <li>mutated line		:	the mutated line inside that method or -1 if it's a class or field mutation</li>
 * <li>mutation			:	the mutation</li>
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1
 */
public class MutationInformation {
	
	private int mutatedLine;
	private String method;
	private Mutation mutation;
	
	public MutationInformation(String method, Mutation mutation) {
		this.method = method;
		this.mutatedLine = mutation.getAffectedLine();
		this.mutation = mutation;
	}

	public int getMutatedLine() {
		return mutatedLine;
	}

	public String getMethod() {
		return method;
	}

	public Mutation getMutation() {
		return mutation;
	}
	
	public boolean isMethodMutation() {
		return this.mutatedLine >= 0;
	}
	
	@Override
	public String toString() {
		String res = "";
		res += "Method mutation  : " + isMethodMutation() + "\n";
		res += "Method           : " + this.method + "\n";
		res += "Line             : " + this.mutatedLine + "\n";
		res += "Mutation         : " + this.mutation.toString();
		return res;
	}

}
