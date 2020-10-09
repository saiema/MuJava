package mujava.app;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import mujava.api.MutationOperator;
import mujava.api.Mutation;


/**
 * This class represent the result of a mutation
 * <p>
 * The information represented in this class is the following:
 * <p>
 * <li>name 				: the name of the mutated class</li>
 * <li>method 				: the last method mutated*</li>
 * <li>path 				: the path to the mutated file</li>
 * <li>md5digest			: an md5 digest of the mutant</li>
 * <li>Mutations information: a collection of {method, line, mutation} applied to the original file</li>
 * <p>
 * <b>*if this value is {@code null} then the mutation was made to the class fields</b>
 * <hr>
 * <b> note: mutatedLines, opsUsed and Mutations will hold only one value except when writing several mutations at once </b>
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 2.0
 */
public class MutantInfo {
	private final String name;
	private String method;
	private final String path;
	private final byte[] md5digest;
	private final boolean severalMutations;
	private boolean isExternal;
	private List<MutationInformation> mutations;
	private Mutation lastMutation;
	
	
	/**
	 * Constructs a new object to contain the information related to a mutated class
	 * 
	 * @param name          : 	the mutated class name : {@code String}
	 * @param method        :	the method mutated ({@code null} if the mutation was on the class fields or if this is an external mutant) : {@code String}
	 * @param path          :	the path to the mutated file : {@code String}
	 * @param md5digest        :	the md5 hash of the mutated file
	 * @param mi            :	the mutant identifier that generated this mutant, {@code null} if this is an external mutant : {@code Mutation}
	 * @param isExternal    :	if the mutant was not created by mujava
	 */
	public MutantInfo(String name, String method, String path, byte[] md5digest, Mutation mi, boolean isExternal) {
		this.name = name;
		if (isExternal && method != null) throw new IllegalArgumentException("Can't create an external mutation info with a method");
		if (method != null) this.method = method;
		this.path = path;
		initializeLists();
		if (isExternal && mi != null) throw new IllegalArgumentException("Can't create an external mutation info with a non-null mutation");
		addMutation(mi, method);
		this.md5digest = md5digest;
		this.severalMutations = isExternal;
		this.isExternal = isExternal;
	}
	
	/**
	 * Constructs a new object to contain the information related to a mutated class
	 * 
	 * @param name			: 	the mutated class name : {@code String}
	 * @param method		:	the method mutated ({@code null} if the mutation was on the class fields) : {@code String}
	 * @param path			:	the path to the mutated file : {@code String}
	 * @param md5digest		:	the md5 hash of the mutated file
	 * @param mi			:	the mutant identifier that generated this mutant : {@code Mutation}
	 */
	public MutantInfo(String name, String method, String path, byte[] md5digest, Mutation mi) {
		this(name, method, path, md5digest, mi, false);
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
		if (method != null) this.method = method;
		this.path = path;
		this.severalMutations = true;
		this.md5digest = digest;
	}
	
	private void initializeLists() {
		this.mutations = new LinkedList<>();
	}
	
	/**
	 * Add a mutation
	 * @param mi : the mutation to add : {@code Mutation}
	 */
	public void addMutation(Mutation mi, String method) {
		addMutation(mi, method, false);
	}
	
	private void addMutation(Mutation mi, String method, boolean prepend) {
		if (mi != null) {
			MutationInformation minfo = new MutationInformation(method, mi);
			if (prepend) this.mutations.add(0, minfo); else this.mutations.add(minfo);
			if (!prepend) this.lastMutation = mi;
		}
	}
	
	/**
	 * Adds a list of mutations
	 * @param mutationsInfo	:	the list of mutations to add
	 * @param prepend	:	if {@code true} new mutations will be prepended to current ones, else they will be appended
	 */
	public void addMutations(List<MutationInformation> mutationsInfo, boolean prepend) {
		for (int i = mutationsInfo.size()-1; i >= 0; i--) {
			MutationInformation m = mutationsInfo.get(i);
			addMutation(m.getMutation(), m.getMethod(), prepend);
		}
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
	
	public String getClassRootFolder() {
		String res = "";
		String classAsPath = this.name.replaceAll("\\.", File.separator);
		int classIdx = this.path.lastIndexOf(classAsPath);
		if (classIdx > 0) {
			res += this.path.substring(0, classIdx);
		}
		return res;
	}

	/**
	 * @return the last mutated line : {@code int}
	 */
	public int getMutatedLine() {
		int mutatedLine = -1;
		if (!this.severalMutations && this.lastMutation != null) {
			mutatedLine = this.lastMutation.getAffectedLine();
		}
		return mutatedLine;
	}
	
	/**
	 * @return {@code true} iff for every {@code Mutation mi} in {@code getMutations()} {@code mi.isGuardMutation()} is true : {@code boolean}
	 */
	public boolean isGuardMutation() {
		boolean isGuardMutation = true;
		for (int i = 0; isGuardMutation && (i < this.mutations.size()); i++) {
			isGuardMutation = this.mutations.get(i).getMutation().isGuardMutation();
		}
		return isGuardMutation;
	}

	/**
	 * @return the last mutation operator used : {@code MutationOperator}
	 */
	public MutationOperator getOpUsed() {
		MutationOperator opUsed = MutationOperator.NONE;
		if (!this.severalMutations && this.lastMutation != null) {
			opUsed = this.lastMutation.getMutOp();
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
	 * @return the last mutant identifier that generated this mutant : {@code Mutation}
	 */
	public Mutation getMutation() {
		Mutation mi = null;
		if (!this.severalMutations && this.mutations.size() == 1) {
			mi = this.lastMutation;
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
		List<Integer> lines = new LinkedList<>();
		for (MutationInformation minfo : this.mutations) {
			lines.add(minfo.getMutatedLine());
		}
		return lines;
	}
	
	/**
	 * @return the mutant operators used
	 */
	public List<MutationOperator> getMutantOperators() {
		List<MutationOperator> ops = new LinkedList<>();
		for (MutationInformation minfo : this.mutations) {
			ops.add(minfo.getMutation().getMutOp());
		}
		return ops;
	}
	
	/**
	 * @return the list of Mutation used
	 */
	public List<Mutation> getMutations() {
		List<Mutation> mutations = new LinkedList<>();
		for (MutationInformation minfo : this.mutations) {
			mutations.add(minfo.getMutation());
		}
		return mutations;
	}
	
	public List<MutationInformation> getMutationsInformation() {
		return this.mutations;
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("name: " + this.name
				+ "\nPath                   : " + this.path
				+ "\nMD5 hash               : " + Arrays.toString(this.md5digest));
		if (!isExternal) {
			res.append("\nSeveral mutations      : ").append(this.severalMutationsApplied());
			res.append("\nMutations              : " + "\n");
			for (MutationInformation minfo : this.mutations) {
				res.append(indentedMutations(minfo));
			}
		} else {
			res.append("\nis external");
		}
		return res.toString();
	}
	
	private String indentedMutations(MutationInformation minfo) {
		StringBuilder res = new StringBuilder();
		String[] lines = minfo.toString().split("\n");
		for (String line : lines) {
			res.append("    ").append(line).append("\n");
		}
		return res.toString();
	}

}
