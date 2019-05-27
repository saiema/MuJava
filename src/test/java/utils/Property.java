package test.java.utils;

import java.util.List;
import java.util.regex.Pattern;

import mujava.api.MutationOperator;
import mujava.app.Core;

public class Property {
	public final static String MUTATE_FIELDS= "0_mutateFields";
	public final static String MUTATE_CLASS= "0_mutateClass";
	
	public Property(MutationOperator op, String clazz, String method, Integer mutantsExpected, Integer compilingMutantsExpected, List<Pattern> mutantCodeExpected, List<Pattern> mutantCodeNotExpected) {
		this.op 						= op;
		this.clazz 						= fixBackslash(clazz);
		this.method 					= method;
		this.mutantsExpected 			= mutantsExpected;
		this.compilingMutantsExpected 	= compilingMutantsExpected;
		this.mutantCodeExpected 		= mutantCodeExpected;
		this.mutantCodeNotExpected 		= mutantCodeNotExpected;
		this.testingObtainingMutants	= false;
		this.testingMultiMutations		= false;
		this.testingMutationMerging		= false;
	}
	
	public Property(List<MutationOperator> ops, String clazz, String method, Integer mutantsExpected, Integer compilingMutantsExpected, List<Pattern> mutantCodeExpected, List<Pattern> mutantCodeNotExpected) {
		this(ops, clazz, method, mutantsExpected, compilingMutantsExpected, mutantCodeExpected, mutantCodeNotExpected, null, false);
	}
	
	public Property(List<MutationOperator> ops, String clazz, String method, Integer mutantsExpected, Integer compilingMutantsExpected, List<Pattern> mutantCodeExpected, List<Pattern> mutantCodeNotExpected, MutationsFilter filter, boolean writeAllToSameFile) {
		this.ops 						= ops;
		this.clazz 						= fixBackslash(clazz);
		this.method 					= method;
		this.mutantsExpected 			= mutantsExpected;
		this.compilingMutantsExpected 	= compilingMutantsExpected;
		this.mutantCodeExpected 		= mutantCodeExpected;
		this.mutantCodeNotExpected 		= mutantCodeNotExpected;
		this.filter 					= filter;
		this.writeAllToSameFile			= writeAllToSameFile;
		this.testingObtainingMutants	= false;
		this.testingMultiMutations		= false;
		this.testingMutationMerging		= false;
	}
	
	public Property(List<MutationOperator> ops, String clazz, String method, List<MutationExpected> mutationsExpected, List<Integer> linesAffectedExpected) {
		this.ops 						= ops;
		this.clazz 						= fixBackslash(clazz);
		this.method 					= method;
		this.mutationsExpected 			= mutationsExpected;
		this.affectedLinesExpected		= linesAffectedExpected;
		this.testingObtainingMutants	= true;
		this.testingMultiMutations		= false;
		this.testingMutationMerging		= false;
	}
	
	public Property(List<MutationOperator> ops, String clazz, String method, List<MutationExpected> mutationsExpected, List<Integer> linesAffectedExpected, MutationsFilter filter) {
		this.ops 						= ops;
		this.clazz 						= fixBackslash(clazz);
		this.method 					= method;
		this.mutationsExpected 			= mutationsExpected;
		this.affectedLinesExpected		= linesAffectedExpected;
		this.testingObtainingMutants	= true;
		this.testingMultiMutations		= false;
		this.testingMutationMerging		= false;
		this.filter						= filter;
	}
	
	public Property(List<MutationOperator> ops, String clazz, String method, MutationsFilter filter, boolean canBeMerged) {
		this.ops 						= ops;
		this.clazz 						= fixBackslash(clazz);
		this.method 					= method;
		this.filter						= filter;
		this.canBeMerged				= canBeMerged;
		this.testingObtainingMutants	= false;
		this.testingMultiMutations		= false;
		this.testingMutationMerging		= true;
	}
	
	
	private String fixBackslash(String orig) {
		return orig.replaceAll("/", Core.SEPARATOR);
	}
	
	public List<MutationOperator> ops;
	public MutationOperator op;
	public String clazz;
	public String method;
	public Integer mutantsExpected;
	public Integer compilingMutantsExpected;
	public List<Pattern> mutantCodeExpected;
	public List<Pattern> mutantCodeNotExpected;
	public List<MutationExpected> mutationsExpected;
	public List<Integer> affectedLinesExpected;
	public MutationsFilter filter;
	public boolean writeAllToSameFile = false;
	public boolean canBeMerged;
	public boolean testingObtainingMutants = false;
	public boolean testingMultiMutations = false;
	public boolean testingMutationMerging = false;
}
