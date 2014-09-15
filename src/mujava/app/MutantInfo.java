package mujava.app;

import java.util.LinkedList;
import java.util.List;

import mujava.api.Mutant;
import mujava.api.Mutation;


/**
 * This class represent the result of a mutation
 * <p>
 * The information represented in this class is the following:
 * <p>
 * <li>name 				: the name of the mutated class</li>
 * <li>method 				: the method mutated*</li>
 * <li>path 				: the path to the mutated file</li>
 * <li>mutatedLines			: the lines mutated</li>
 * <li>opsUsed				: the mutation operators used</li>
 * <li>md5digest			: an md5 digest of the mutant</li>
 * <li>Mutations	: the mutant identifiers that generated this mutant</li>
 * <p>
 * <b>*if this value is {@code null} then the mutation was made to the class fields</b>
 * <hr>
 * <b> note: mutatedLines, opsUsed and Mutations will hold only one value except when writing several mutations at once </b>
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 1.3.1
 */
public class MutantInfo {
	private String name;
	private String method;
	private String path;
	private byte[] md5digest;
	private boolean severalMutations;
	private List<Integer> mutatedLines;
	private List<Mutation> Mutations;
	private List<Mutant> opsUsed;
	
	
	/**
	 * Constructs a new object to contain the information related to a mutated class
	 * 
	 * @param name			: 	the mutated class name : {@code String}
	 * @param method		:	the method mutated ({@code null} if the mutation was on the class fields) : {@code String}
	 * @param path			:	the path to the mutated file : {@code String}
	 * @param mutatedLine	:	the mutated line : {@code int}
	 * @param opUsed		:	the mutation operator used : {@code Mutant}
	 * @param mi			:	the mutant identifier that generated this mutant : {@code Mutation}
	 */
	public MutantInfo(String name, String method, String path, int mutatedLine, Mutant opUsed, byte[] md5digest, Mutation mi) {
		this.name = name;
		this.method = method;
		this.path = path;
		initializeLists();
		this.mutatedLines.add(mutatedLine);
		this.opsUsed.add(opUsed);
		this.Mutations.add(mi);
		this.md5digest = md5digest;
		this.severalMutations = false;
	}
	
	/**
	 * Constructor used when the same file has several mutations applied
	 * 
	 * @param name			: 	the mutated class name : {@code String}
	 * @param method		:	the method mutated ({@code null} if the mutation was on the class fields) : {@code String}
	 * @param path			:	the path to the mutated file : {@code String}
	 * @param digest		:	the md5 hash of the mutated file
	 *
	 */
	public MutantInfo(String name, String method, String path, byte[] digest) {
		initializeLists();
		this.name = name;
		this.method = method;
		this.path = path;
		this.severalMutations = true;
		this.md5digest = digest;
	}
	
	private void initializeLists() {
		this.mutatedLines = new LinkedList<Integer>();
		this.Mutations = new LinkedList<Mutation>();
		this.opsUsed = new LinkedList<Mutant>();
	}
	
	/**
	 * Add a mutation operator
	 * @param op : the operator to add : {@code Mutant}
	 */
	public void addMutantOperator(Mutant op) {
		this.opsUsed.add(op);
	}
	
	/**
	 * Add a mutated line
	 * @param ml : the mutated line to add : {@code Integer}
	 */
	public void addMutatedLine(Integer ml) {
		this.mutatedLines.add(ml);
	}
	
	/**
	 * Add a mutation
	 * @param mi : the mutation to add : {@code Mutation}
	 */
	public void addMutation(Mutation mi) {
		this.Mutations.add(mi);
	}


	/**
	 * @return the name of the mutated class : {@code String}
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the path to the mutated file : {@code String}
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the mutated line : {@code int}
	 */
	public int getMutatedLine() {
		int mutatedLine = -1;
		if (!this.severalMutations && this.mutatedLines.size() == 1) {
			mutatedLine = this.mutatedLines.get(0);
		}
		return mutatedLine;
	}
	
	/**
	 * @return {@code true} iff for every {@code Mutation mi} in {@code getMutations()} {@code mi.isGuardMutation()} is true : {@code boolean}
	 */
	public boolean isGuardMutation() {
		boolean isGuardMutation = true;
		for (int i = 0; isGuardMutation && (i < this.Mutations.size()); i++) {
			isGuardMutation &= this.Mutations.get(i).isGuardMutation();
		}
		return isGuardMutation;
	}

	/**
	 * @return the mutation operator used : {@code Mutant}
	 */
	public Mutant getOpUsed() {
		Mutant opUsed = null;
		if (!this.severalMutations && this.opsUsed.size() == 1) {
			opUsed = this.opsUsed.get(0);
		}
		return opUsed;
	}
	
	/**
	 * @return the mutated method or {@code null} if the mutation was made to a class field : {@code String}
	 */
	public String getMethod() {
		return this.method;
	}
	
	/**
	 * @return the md5 digest of the mutant : {@code byte[]}
	 */
	public byte[] getMD5digest() {
		return this.md5digest;
	}
	
	/**
	 * @return the mutant identifier that generated this mutant : {@code Mutation}
	 */
	public Mutation getMutation() {
		Mutation mi = null;
		if (!this.severalMutations && this.Mutations.size() == 1) {
			mi = this.Mutations.get(0);
		}
		return mi;
	}
	
	/**
	 * @return if this mutant has several mutations applied or not
	 */
	public boolean severalMutationsApplied() {
		return this.severalMutations;
	}
	
	/**
	 * @return the mutated lines
	 */
	public List<Integer> getMutatedLines() {
		return this.mutatedLines;
	}
	
	/**
	 * @return the mutant operators used
	 */
	public List<Mutant> getMutantOperators() {
		return this.opsUsed;
	}
	
	/**
	 * @return the list of Mutation used
	 */
	public List<Mutation> getMutations() {
		return this.Mutations;
	}
	
	@Override
	public String toString() {
		return "name: " + this.name
						+ "\nmethod: "				+ this.method
						+ "\npath: "				+ this.path
						+ "\nmutated lines: "		+ mutatedLinesToString()
						+ "\noperators used: "		+ operatorsUsedToString()
						+ "\nMD5 hash: " 			+ this.md5digest
						+ "\nSeveral mutations "	+ this.severalMutationsApplied() + '\n';
	}
	
	private String mutatedLinesToString() {
		String res = "";
		if (!this.severalMutations) {
			res += getMutatedLine();
		} else {
			res = "[";
			int i = 0;
			for (Integer line : this.mutatedLines) {
				res += line;
				if ((i + 1) < this.mutatedLines.size()) {
					res += ", ";
				}
			}
			res += "]";
		}
		return res;
	}
	
	private String operatorsUsedToString() {
		String res = "";
		if (this.severalMutations) {
			res += (getOpUsed()==null?"null":getOpUsed());
		} else {
			res = "[";
			int i = 0;
			for (Mutant line : this.opsUsed) {
				res += line;
				if ((i + 1) < this.opsUsed.size()) {
					res += ", ";
				}
			}
			res += "]";
		}
		return res;
	}

}
