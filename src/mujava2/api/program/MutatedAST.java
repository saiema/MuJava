package mujava2.api.program;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import mujava.api.Mutation;
import mujava.app.MutationInformation;
import static mujava.api.MutationOperator.*;
import mujava.op.util.OLMO;
import mujava.util.JustCodeDigest;
import mujava2.api.writer.ASTWriter;
import openjava.ptree.ParseTreeException;

public class MutatedAST {
	
	private MutatedAST parent;
	private JavaAST original;
	private List<MutationInformation> mutations;
	private Map<String, Map<Integer, List<MutationInformation>>> mutationsPerLinePerMethod;
	private boolean mutationsApplied;
	
	public MutatedAST(JavaAST original, List<MutationInformation> mutations) {
		if (original == null) throw new IllegalArgumentException("null JavaAST argument");
		if (mutations == null) throw new IllegalArgumentException("null List<MutationInformation> argument");
		if (mutations.isEmpty()) throw new IllegalArgumentException("no mutations in List<MutationInformation> argument");
		if (mutations.contains(null)) throw new IllegalArgumentException("null mutation in List<MutationInformation> argument");
		this.mutationsApplied = false;
		this.original = original;
		this.mutations = mutations;
		String error = validateMutations(mutations);
		if (!error.isEmpty()) throw new IllegalArgumentException("Invalid mutations : " + error);
	}
	
	public MutatedAST(MutatedAST parent, List<MutationInformation> mutations) {
		this.mutationsApplied = false;
		this.parent = parent;
		this.original = this.parent.original;
		String error = validateMutations(mutations);
		if (!error.isEmpty()) throw new IllegalArgumentException("Invalid mutations : " + error);
	}
	
	public MutatedAST(JavaAST original, MutationInformation mutation) {
		this(original, Arrays.asList(mutation));
	}
	
	public List<MutationInformation> getMutations(){
		return this.mutations;
	}
	
	public boolean mutationsApplied() {
		return this.mutationsApplied;
	}
	
	public JavaAST applyMutations() throws ParseTreeException {
		if (this.parent != null) {
			this.parent.applyMutations();
			this.original = this.parent.original;
		}
		Set<String> methods = this.mutationsPerLinePerMethod.keySet();
		for (String method : methods) {
			Set<Integer> lines = this.mutationsPerLinePerMethod.get(method).keySet();
			for (Integer line : lines) {
				applyMutations(method, line, false);
			}
		}
		return this.original;
	}
	
	public JavaAST applyMutations(String method, Integer line) throws ParseTreeException {
		return applyMutations(method, line, true);
	}
	
	private JavaAST applyMutations(String method, Integer line, boolean applyToParentFirst) throws ParseTreeException {
		if (this.parent != null && applyToParentFirst) {
			this.parent.applyMutations(method, line, true);
			this.original = this.parent.original;
		}
		MutationInformation mergedMinfo = mergeMutations(method, line); 
		if (mergedMinfo == null) throw new IllegalStateException("Couldn't merge mutations for method " + method + ", line " + line);
		Mutation merged = mergedMinfo.getMutation();
		OLMO olmo = new OLMO();
		olmo.modifyAST(this.original.getCompUnit(), merged, method);
		this.original.flagAsMutated();
		this.mutationsApplied = true;
		return this.original;
	}
	
	public JavaAST undoMutations() throws ParseTreeException {
		Set<String> methods = this.mutationsPerLinePerMethod.keySet();
		for (String method : methods) {
			Set<Integer> lines = this.mutationsPerLinePerMethod.get(method).keySet();
			for (Integer line : lines) {
				undoMutations(method, line, false);
			}
		}
		if (this.parent != null) {
			this.parent.undoMutations();
			this.original = this.parent.original;
		}
		this.mutationsApplied = false;
		this.original.unflagAsMutated();
		return this.original;
	}
	
	public JavaAST undoMutations(String method, Integer line) throws ParseTreeException {
		return undoMutations(method, line, true);
	}
	
	private JavaAST undoMutations(String method, Integer line, boolean applyToParentFirst) throws ParseTreeException {
		MutationInformation mergedMinfo = mergeMutations(method, line); 
		if (mergedMinfo == null) throw new IllegalStateException("Couldn't merge mutations for method " + method + ", line " + line);
		Mutation merged = mergedMinfo.getMutation();
		Mutation reversed = new Mutation(merged.getMutOp(), merged.getMutant(), merged.getOriginal());
		OLMO olmo = new OLMO();
		olmo.modifyAST(this.original.getCompUnit(), reversed, method);
		if (this.parent != null && applyToParentFirst) {
			this.parent.undoMutations(method, line, true);
			this.original = this.parent.original;
		}
		this.original.flagAsMutated();
		return this.original;
	}
	
	public MutationInformation mergeMutations(String method, Integer line) throws ParseTreeException {
		List<Mutation> mutations = getMutations(method, line);
		if (mutations == null || mutations.isEmpty()) return null;
		OLMO olmo = new OLMO(mutations);
		Mutation merged = olmo.mergeMutants();
		MutationInformation mergedMinfo = new MutationInformation(method, merged);
		return mergedMinfo;
	}
	
	public List<MutationInformation> mergeAllMutations() throws ParseTreeException {
		List<MutationInformation> mergedMutations = new LinkedList<>();
		Set<String> methods = this.mutationsPerLinePerMethod.keySet();
		for (String method : methods) {
			Set<Integer> lines = this.mutationsPerLinePerMethod.get(method).keySet();
			for (Integer line : lines) {
				mergedMutations.add(mergeMutations(method, line));
			}
		}
		return mergedMutations;
	}
	
	public List<MutationInformation> mergeMutationsAcrossGenerations() throws ParseTreeException {
		if (this.parent != null) {
			List<MutationInformation> mergedMutations = mergeAllMutations();
			List<MutationInformation> firstGenMergedMutations = getFirstGenMergedMutations();
			Map<String, Map<Integer, List<MutationInformation>>> firstGen = orderMergedMutations(firstGenMergedMutations);
			Map<String, Map<Integer, List<MutationInformation>>> thisGen = orderMergedMutations(mergedMutations);
			Map<String, Map<Integer, List<MutationInformation>>> mergedGens = mergeMergedOrderedMutations(firstGen, thisGen);
			return orderedMutationsAsList(mergedGens);
		} else {
			return mergeAllMutations();
		}
	}
	
	private List<MutationInformation> getFirstGenMergedMutations() throws ParseTreeException {
		MutatedAST current = this;
		while (current.parent != null) {
			current = current.parent;
		}
		return current.mergeAllMutations();
	}
	
	public byte[] writeToFile(File file) throws IOException, ParseTreeException {
		file.getParentFile().mkdirs();
		file.createNewFile();
		OutputStream os = new FileOutputStream(file, true);
		PrintWriter pw = new PrintWriter(os);
		ASTWriter writer = new ASTWriter(pw);
		if (!this.mutationsApplied) writer.setMutationsFromMutationInformation(mergeMutationsAcrossGenerations());//mergeAllMutations());
		this.original.getCompUnit().accept(writer);
		pw.flush();  
		pw.close();
		return JustCodeDigest.digest(file);
	}
	
	public byte[] writeInFolder(File rootFolder) throws IOException, ParseTreeException {
		String filePath = "";
		String pkgAsPath = dotsAsFileSep(this.original.getPkg());
		filePath = mergePaths(randomString(10),pkgAsPath);
		filePath = mergePaths(filePath, this.original.getSimpleClassName() + ".java");
		File outputFile = new File(rootFolder, filePath);
		return writeToFile(outputFile);
	}
	
	private List<Mutation> getMutations(String method, Integer line) {
		List<Mutation> mutations = new LinkedList<>();
		if (this.mutationsPerLinePerMethod.containsKey(method)) {
			Map<Integer, List<MutationInformation>> mutationsPerLine = this.mutationsPerLinePerMethod.get(method);
			if (mutationsPerLine.containsKey(line)) {
				List<MutationInformation> minfos = mutationsPerLine.get(line);
				for (MutationInformation minfo : minfos) mutations.add(minfo.getMutation());
			}
		}
		return mutations;
	}
	
	private Map<String, Map<Integer, List<MutationInformation>>> orderMergedMutations(List<MutationInformation> mutations) {
		Map<String, Map<Integer, List<MutationInformation>>> orderedMutations = new TreeMap<String, Map<Integer, List<MutationInformation>>>();
		for (MutationInformation minfo : mutations) {
			String method = minfo.getMethod();
			Map<Integer, List<MutationInformation>> mutationsPerLine = null;
			if (orderedMutations.containsKey(method)) {
				mutationsPerLine = orderedMutations.get(method);
			} else {
				mutationsPerLine = new TreeMap<Integer, List<MutationInformation>>();
				orderedMutations.put(method, mutationsPerLine);
			}
			List<MutationInformation> muts = null;
			Integer line = minfo.getMutatedLine();
			if (mutationsPerLine.containsKey(line)) {
				muts = mutationsPerLine.get(line);
			} else {
				muts = new LinkedList<MutationInformation>();
				mutationsPerLine.put(line, muts);
			}
			muts.add(minfo);
		}
		return orderedMutations;
	}
	
	private Map<String, Map<Integer, List<MutationInformation>>> mergeMergedOrderedMutations(Map<String, Map<Integer, List<MutationInformation>>> a, Map<String, Map<Integer, List<MutationInformation>>> b) {
		Map<String, Map<Integer, List<MutationInformation>>> mergedMergedOrderedMutations = new TreeMap<String, Map<Integer, List<MutationInformation>>>();
		for (Entry<String, Map<Integer, List<MutationInformation>>> aMutsPerMethods : a.entrySet()) {
			String method = aMutsPerMethods.getKey();
			if (!b.containsKey(method)) {
				b.put(method, aMutsPerMethods.getValue());
				continue;
			}
			for (Entry<Integer, List<MutationInformation>> aMutsPerLine : aMutsPerMethods.getValue().entrySet()) {
				Integer line = aMutsPerLine.getKey();
				if (!b.get(method).containsKey(line)) {
					b.get(method).put(line, aMutsPerLine.getValue());
				} else {
					//there should be only one mutations for this method and line
					//TODO: add a check for this (maybe?)
					MutationInformation aMut = aMutsPerLine.getValue().get(0);
					MutationInformation bMut = b.get(method).get(line).get(0);
					Mutation mergedMutation = new Mutation(MULTI, aMut.getMutation().getOriginal(), bMut.getMutation().getMutant());
					MutationInformation minfo = new MutationInformation(method, mergedMutation);
					Map<Integer, List<MutationInformation>> newMPLPM = new TreeMap<>();
					List<MutationInformation> newMPL = new LinkedList<>();
					newMPL.add(minfo);
					newMPLPM.put(line, newMPL);
					mergedMergedOrderedMutations.put(method, newMPLPM);
				}
			}
		}
		return mergedMergedOrderedMutations;
	}

	private String validateMutations(List<MutationInformation> mutations) {
		String error = "";
		//DIVIDE MUTATIONS IN METHODS AND LINES
		this.mutationsPerLinePerMethod = new TreeMap<String, Map<Integer, List<MutationInformation>>>();
		for (MutationInformation minfo : mutations) {
			String method = minfo.getMethod();
			Map<Integer, List<MutationInformation>> mutationsPerLine = null;
			if (this.mutationsPerLinePerMethod.containsKey(method)) {
				mutationsPerLine = this.mutationsPerLinePerMethod.get(method);
			} else {
				mutationsPerLine = new TreeMap<Integer, List<MutationInformation>>();
				this.mutationsPerLinePerMethod.put(method, mutationsPerLine);
			}
			List<MutationInformation> muts = null;
			Integer line = minfo.getMutatedLine();
			if (mutationsPerLine.containsKey(line)) {
				muts = mutationsPerLine.get(line);
			} else {
				muts = new LinkedList<MutationInformation>();
				mutationsPerLine.put(line, muts);
			}
			muts.add(minfo);
		}
		//CHECK MUTATIONS FOR MERGING COMPATIBILITY
		boolean compatible = true;
		for (Entry<String, Map<Integer, List<MutationInformation>>> mutationsPerMethod : this.mutationsPerLinePerMethod.entrySet()) {
			if (!compatible) break;
			String method = mutationsPerMethod.getKey();
			Map<Integer, List<MutationInformation>> mutationsPerLine = mutationsPerMethod.getValue();
			for (Entry<Integer, List<MutationInformation>> mpl : mutationsPerLine.entrySet()) {
				if (!compatible) break;
				Integer line = mpl.getKey();
				List<MutationInformation> muts = mpl.getValue();
				if (muts.size() > 1) {
					List<Mutation> mutationsList = new LinkedList<>();
					for (MutationInformation minfo : muts) mutationsList.add(minfo.getMutation());
					try {
						compatible = mujava.app.Mutator.checkMergingCompatibility(mutationsList);
						if (!compatible) {
							error = "Incompatibilty found in mutations for method " + method + " line " + line;
						}
					} catch (ParseTreeException e) {
						compatible = false;
						error = e.getMessage();
					}
				}
			}
		}
		return error;
	}
	
	private String mergePaths(String p1, String p2) {
		if (p1.endsWith(File.separator)) {
			if (p2.startsWith(File.separator)) {
				return p1 + p2.substring(1);
			} else {
				return p1 + p2;
			}
		} else {
			if (p2.startsWith(File.separator)) {
				return p1 + p2;
			} else {
				return p1 + File.separator + p2;
			}
		}
	}
	
	private String dotsAsFileSep(String pathWithDots) {
		return pathWithDots.replaceAll("\\.", File.separator);
	}
	
	/**
	 * Construct a random String using upper case letters and digits
	 * 
	 * @param len	:	the lenght of the resulting string	:	{@code int}
	 * @return a random String of size {@code len} using upper case letters and digits
	 */
	private String randomString(int len) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}
	
	private List<MutationInformation> orderedMutationsAsList(Map<String, Map<Integer, List<MutationInformation>>> orderedMutations) {
		List<MutationInformation> mutationsAsList = new LinkedList<>();
		for (Entry<String, Map<Integer, List<MutationInformation>>> mpm : orderedMutations.entrySet()) {
			for (Entry<Integer, List<MutationInformation>> mpmpl : mpm.getValue().entrySet()) {
				mutationsAsList.addAll(mpmpl.getValue());
			}
		}
		return mutationsAsList;
	}

}
