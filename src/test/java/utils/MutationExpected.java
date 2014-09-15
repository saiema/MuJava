package test.java.utils;

import mujava.api.Mutant;

public class MutationExpected {
	private String original;
	private String mutant;
	private int mutatedLine;
	private Mutant op;
	
	public MutationExpected(String orig, String mut, int mutatedLine) {
		this.original		= orig;
		this.mutant			= mut;
		this.mutatedLine 	= mutatedLine;
	}
	
	public MutationExpected(String orig, String mut, int mutatedLine, Mutant op) {
		this(orig, mut, mutatedLine);
		this.op = op;
	}
	
	public boolean compareExpectedWithObtained(String orig, String mut, int line) {
		boolean originalMatches = this.original.trim().compareTo(orig.trim()) == 0;
		boolean mutantMatches = this.mutant.trim().compareTo(mut.trim()) == 0;
		boolean linesMatches = this.mutatedLine==-1 || this.mutatedLine == line;
		return originalMatches && mutantMatches && linesMatches;
	}
	
	public boolean compareExpectedWithObtained(String orig, String mut, int line, Mutant op) {
		boolean usualCompare = this.compareExpectedWithObtained(orig, mut, mutatedLine);
		boolean operatorsMatch = this.op.toString().compareTo(op.toString()) == 0;
		return usualCompare && operatorsMatch;
	}
	
	@Override
	public String toString() {
		return this.original + " => " + this.mutant + (this.mutatedLine==-1?"":(" at line " + this.mutatedLine) + (this.op==null?"":(" using operator " + this.op)));
	}
	
}
