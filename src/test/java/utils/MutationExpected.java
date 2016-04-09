package test.java.utils;

import mujava.api.MutationOperator;

public class MutationExpected {
	private String original;
	private String mutant;
	private int mutatedLine;
	private MutationOperator op;
	
	public MutationExpected(String orig, String mut, int mutatedLine) {
		this.original		= orig;
		this.mutant			= mut;
		this.mutatedLine 	= mutatedLine;
	}
	
	public MutationExpected(String orig, String mut, int mutatedLine, MutationOperator op) {
		this(orig, mut, mutatedLine);
		this.op = op;
	}
	
	public boolean compareExpectedWithObtained(String orig, String mut, int line) {
		boolean originalMatches = this.original.trim().compareTo(orig.trim()) == 0;
		boolean mutantMatches = this.mutant.trim().compareTo(mut.trim()) == 0;
		boolean linesMatches = this.mutatedLine==-1 || this.mutatedLine == line;
		return originalMatches && mutantMatches && linesMatches;
	}
	
	public boolean compareExpectedWithObtained(String orig, String mut, int line, MutationOperator op) {
		boolean usualCompare = this.compareExpectedWithObtained(orig, mut, line);
		boolean operatorsMatch = this.op.toString().compareTo(op.toString()) == 0;
		return usualCompare && operatorsMatch;
	}
	
	@Override
	public String toString() {
		return this.original + " => " + this.mutant + (this.mutatedLine==-1?"":(" at line " + this.mutatedLine) + (this.op==null?"":(" using operator " + this.op)));
	}
	
}
