package mujava.generations;

import java.util.TreeMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mujava.api.MutationOperator;
import mujava.api.Mutation;
import mujava.app.MutantInfo;

/**
 * This class will contain information about the mutants in each generation
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 0.5
 */
public class GenerationsInformation {
	private Map<Integer, List<MutantInfo>> generations;
	private List<MutantInfo> lastGeneration;
	private Map<Integer, Map<Integer, Map<MutationOperator, Integer>>> mutationsNumberPerOperatorPerLinePerGeneration;
	private Map<Integer, Map<Integer, List<MutantInfo>>> mutationsPerLinePerGeneration;
	private Map<Integer, Map<Integer, Map<MutationOperator, List<MutantInfo>>>> mutationsPerOperatorPerLinePerGenerations;
	private Integer lastGenerationUpdated = -1;
	private List<MutantInfo> lastMutantsUpdated = null;
	private boolean lowMemoryMode;
	
	/**
	 * The constructor for the class
	 */
	public GenerationsInformation(boolean lowMemoryMode) {
		if (lowMemoryMode) {
			this.lastGeneration = new LinkedList<MutantInfo>();
			this.mutationsNumberPerOperatorPerLinePerGeneration = new TreeMap<Integer, Map<Integer, Map<MutationOperator, Integer>>>();
			this.lastMutantsUpdated = new LinkedList<MutantInfo>();
			this.lowMemoryMode = true;
		} else {
			this.generations = new TreeMap<Integer, List<MutantInfo>>();
			this.mutationsPerLinePerGeneration = new TreeMap<Integer, Map<Integer, List<MutantInfo>>>();
			this.mutationsPerOperatorPerLinePerGenerations = new TreeMap<Integer, Map<Integer, Map<MutationOperator, List<MutantInfo>>>>();
			this.lastMutantsUpdated = new LinkedList<MutantInfo>();
			this.lowMemoryMode = false;
		}
		
	}
	
	/**
	 * The constructor for the class
	 */
	public GenerationsInformation() {
		this.generations = new TreeMap<Integer, List<MutantInfo>>();
		this.mutationsPerLinePerGeneration = new TreeMap<Integer, Map<Integer, List<MutantInfo>>>();
		this.mutationsPerOperatorPerLinePerGenerations = new TreeMap<Integer, Map<Integer, Map<MutationOperator, List<MutantInfo>>>>();
		this.lastMutantsUpdated = new LinkedList<MutantInfo>();
		this.lowMemoryMode = false;
	}
	
	/**
	 * This method adds a mutant to a specific generation
	 * 
	 * @param generation	:	the generation the new mutant belongs to	: {@code Integer}
	 * @param mutant		:	the mutant to add							: {@code MutantInfo}
	 */
	public void add(Integer generation, MutantInfo mutant) {
		if (this.lowMemoryMode) {
			addLowMemory(generation, mutant);
			this.lastGenerationUpdated = generation;
			this.lastMutantsUpdated.clear();
			this.lastMutantsUpdated.add(mutant);
			return;
		}
		
		List<MutantInfo> genMutants;
		
		if (this.generations.containsKey(generation)) {
			genMutants = this.generations.get(generation);
		} else {
			genMutants = new LinkedList<MutantInfo>();
			this.generations.put(generation, genMutants);
		}
		addMutantPerLine(generation, mutant);
		addMutantPerLinePerOperator(generation, mutant);
		genMutants.add(mutant);
		this.lastGenerationUpdated = generation;
		this.lastMutantsUpdated.clear();
		this.lastMutantsUpdated.add(mutant);
	}
	
	public void addLowMemory(Integer generation, MutantInfo mutant) {
		if (this.lastGeneration == null) {
			this.lastGeneration = new LinkedList<MutantInfo>();
		}
		if (this.lastGenerationUpdated == generation) {
			this.lastGeneration.add(mutant);
			addMutantPerLinePerOperatorLowMemory(generation, mutant);
		} else if (this.lastGenerationUpdated + 1 == generation) {
			this.lastGeneration = new LinkedList<MutantInfo>();
			this.lastGenerationUpdated = generation;
			this.lastGeneration.add(mutant);
			addMutantPerLinePerOperatorLowMemory(generation, mutant);
		}
	}
	
	public void addLowMemory(Integer generation, List<MutantInfo> mutants) {
		if (this.lastGenerationUpdated == generation || this.lastGenerationUpdated + 1 == generation) {
			for (MutantInfo m : mutants) {
				addLowMemory(generation, m);
			}
		}
	}
	
	private void addMutantPerLinePerOperatorLowMemory(Integer generation, MutantInfo mutant) {
		Map<Integer, Map<MutationOperator, Integer>> mutationsPerOperatorPerLine;
		int affectedLine = mutant.getMutation() != null?mutant.getMutation().getAffectedLine():-1;
		if (this.mutationsNumberPerOperatorPerLinePerGeneration.containsKey(generation)) {
			mutationsPerOperatorPerLine = this.mutationsNumberPerOperatorPerLinePerGeneration.get(generation);
		} else {
			mutationsPerOperatorPerLine = new TreeMap<Integer, Map<MutationOperator, Integer>>();
			this.mutationsNumberPerOperatorPerLinePerGeneration.put(generation, mutationsPerOperatorPerLine);
		}
		Map<MutationOperator, Integer> mutationsPerOperator;
		if (mutationsPerOperatorPerLine.containsKey(affectedLine)) {
			mutationsPerOperator = mutationsPerOperatorPerLine.get(affectedLine);
		} else {
			mutationsPerOperator = new TreeMap<MutationOperator, Integer>();
			mutationsPerOperatorPerLine.put(affectedLine, mutationsPerOperator);
		}
		Integer mutations;
		if (mutant.getOpUsed() != null && mutationsPerOperator.containsKey(mutant.getOpUsed())) {
			mutations = mutationsPerOperator.get(mutant.getOpUsed()) + 1;
		} else {
			mutations = new Integer(1);
		}
		mutationsPerOperator.put(mutant.getOpUsed(), mutations);
	}
	
	private void addMutantPerLine(Integer generation, MutantInfo mutant) {
		Map<Integer, List<MutantInfo>> genMutantsPerLine;
		if (this.mutationsPerLinePerGeneration.containsKey(generation)) {
			genMutantsPerLine = this.mutationsPerLinePerGeneration.get(generation);
		} else {
			genMutantsPerLine = new TreeMap<Integer, List<MutantInfo>>();
			this.mutationsPerLinePerGeneration.put(generation, genMutantsPerLine);
		}
		List<MutantInfo> mutantsPerLine;
		if (genMutantsPerLine.containsKey(mutant.getMutatedLine())) {
			mutantsPerLine = genMutantsPerLine.get(mutant.getMutatedLine());
		} else {
			mutantsPerLine = new LinkedList<MutantInfo>();
			genMutantsPerLine.put(mutant.getMutatedLine(), mutantsPerLine);
		}
		mutantsPerLine.add(mutant);
	}
	
	private void addMutantPerLinePerOperator(Integer generation, MutantInfo mutant) {
		Map<Integer, Map<MutationOperator, List<MutantInfo>>> mutationsPerOperatorPerLine;
		if (this.mutationsPerOperatorPerLinePerGenerations.containsKey(generation)) {
			mutationsPerOperatorPerLine = this.mutationsPerOperatorPerLinePerGenerations.get(generation);
		} else {
			mutationsPerOperatorPerLine = new TreeMap<Integer, Map<MutationOperator, List<MutantInfo>>>();
			this.mutationsPerOperatorPerLinePerGenerations.put(generation, mutationsPerOperatorPerLine);
		}
		Map<MutationOperator, List<MutantInfo>> mutationsPerOperator;
		if (mutationsPerOperatorPerLine.containsKey(mutant.getMutatedLine())) {
			mutationsPerOperator = mutationsPerOperatorPerLine.get(mutant.getMutatedLine());
		} else {
			mutationsPerOperator = new TreeMap<MutationOperator, List<MutantInfo>>();
			mutationsPerOperatorPerLine.put(mutant.getMutatedLine(), mutationsPerOperator);
		}
		List<MutantInfo> mutations;
		if (mutationsPerOperator.containsKey(mutant.getOpUsed())) {
			mutations = mutationsPerOperator.get(mutant.getOpUsed());
		} else {
			mutations = new LinkedList<MutantInfo>();
			mutationsPerOperator.put(mutant.getOpUsed(), mutations);
		}
		mutations.add(mutant);
	}
	
	/**
	 * This method adds a list of mutants to a specific generation
	 * 
	 * @param generation	:	the generation the new mutants belongs to	: {@code Integer}
	 * @param mutant		:	the mutants to add							: {@code List<MutantInfo>}
	 */
	public void add(Integer generation, List<MutantInfo> mutants) {
		if (this.lowMemoryMode) {
			addLowMemory(generation, mutants);
			this.lastGenerationUpdated = generation;
			this.lastMutantsUpdated.clear();
			this.lastMutantsUpdated.addAll(mutants);
			return;
		}
		
		List<MutantInfo> genMutants;
		if (this.generations.containsKey(generation)) {
			genMutants = this.generations.get(generation);
		} else {
			genMutants = new LinkedList<MutantInfo>();
			this.generations.put(generation, genMutants);
		}
		for (MutantInfo mutant : mutants) {
			if (mutant.getMutatedLine() == -1) {
				System.out.println("generation " + generation + " | operator " + mutant.getOpUsed() + " | line " + mutant.getMutatedLine());
			}
			addMutantPerLine(generation, mutant);
			addMutantPerLinePerOperator(generation, mutant);
		}
		genMutants.addAll(mutants);
		this.lastGenerationUpdated = generation;
		this.lastMutantsUpdated.clear();
		this.lastMutantsUpdated.addAll(mutants);
	}
	
	/**
	 * @param generation : the generation to obtain the asociated mutants : {@code Integer}
	 * @return the mutants that belongs to the specified generation : {@code List<MutantInfo}
	 */
	public List<MutantInfo> getGeneration(Integer generation) {
		if (this.lowMemoryMode) return null;
		return this.generations.get(generation);
	}
	
	public List<MutantInfo> getLastGeneration() {
		if (this.lowMemoryMode) return this.lastGeneration;
		return this.generations.get(getGenerations() - 1);
	}
	
	/**
	 * @return the number of generations : {@code Integer}
	 */
	public Integer getGenerations() {
		if (this.lowMemoryMode) return this.mutationsNumberPerOperatorPerLinePerGeneration.size();
		return this.generations.size();
	}
	
	public Integer getLastGenerationUpdated() {
		return this.lastGenerationUpdated;
	}
	
	public List<MutantInfo> getLastMutantsUpdated() {
		return this.lastMutantsUpdated;
	}
	
	public String toString() {
		String res = "Generations : " + getGenerations() + " (includes generation 0 with original file)\n";
		for (int i = 1; i < getGenerations(); i++) {
			res += showGeneration(i, true) + "\n";
		}
		return res; 
	}
	
	
	public String showBasicInformation() {
		String res = "Generations : " + getGenerations() + " (includes generation 0 with original file)\n";
		return res;
	}
	
	public String showGeneration(Integer generation, boolean fullInfo) {
		if (this.lowMemoryMode) {
			return showGenerationLowMemory(generation, fullInfo);
		}
		
		String res = (this.generations.containsKey(generation)?("Generation : " + generation + " | mutants " + getGeneration(generation).size()):("There's no generation " + generation)) + "\n";
		if (this.generations.containsKey(generation) && fullInfo) {
			res += showFullGenerationInfo(generation);
		}
		return res;
	}
	
	private String showGenerationLowMemory(Integer generation, boolean fullInfo) {
		String res = (this.mutationsNumberPerOperatorPerLinePerGeneration.containsKey(generation)?("Generation : " + generation + " | mutants " + getGenerationMutationsNumber(generation)):("There's no generation " + generation)) + "\n";
		if (this.mutationsNumberPerOperatorPerLinePerGeneration.containsKey(generation) && fullInfo) {
			res += showFullGenerationInfo(generation);
		}
		return res;
	}
	
	private int getGenerationMutationsNumber(Integer generation) {
		if (this.lowMemoryMode) {
			Map<Integer, Map<MutationOperator, Integer>> mutationsForGeneration = this.mutationsNumberPerOperatorPerLinePerGeneration.get(generation);
			int mutations = 0;
			for (Entry<Integer, Map<MutationOperator, Integer>> entry : mutationsForGeneration.entrySet()) {
				for (Entry<MutationOperator, Integer> mutationsPerOperator : entry.getValue().entrySet()) {
					mutations += mutationsPerOperator.getValue();
				}
			}
			return mutations;
		} else {
			return this.generations.get(generation).size();
		}
	}
	
	public String showFullGenerationInfo(Integer generation) {
		if (this.lowMemoryMode) {
			return showFullGenerationInfoLowMemory(generation);
		}
		String res = "";
		Map<Integer, Map<MutationOperator, List<MutantInfo>>> mutationsPerOperatorPerLine = this.mutationsPerOperatorPerLinePerGenerations.get(generation);
		if (mutationsPerOperatorPerLine == null) {
			return "";
		}
		for (Entry<Integer, Map<MutationOperator, List<MutantInfo>>> mpopl : mutationsPerOperatorPerLine.entrySet()) {
			res += "mutations for line : " + mpopl.getKey() + "\n";
			for (Entry<MutationOperator, List<MutantInfo>> mpo : mpopl.getValue().entrySet()) {
				res += "\tmutations for operator " + (mpo.getKey()==null?"N/A":mpo.getKey().name()) + " : " + mpo.getValue().size() + "\n"; 
				for (MutantInfo mi : mpo.getValue()) {
//					res += "\t\tmutation: ";
					if (mi.severalMutationsApplied()) {
						res += "\n";
						for (Mutation mid : mi.getMutations()) {
							res += "\t\t\t" + mid.getOriginal().toFlattenString() + " => " + mid.getMutant().toFlattenString();
						}
					} else {
						res += "\t\t\t" + mi.getMutation().getOriginal().toFlattenString() + " => " + mi.getMutation().getMutant().toFlattenString();
					}
					res += "\t\t" + " affected line: " + mi.getMutation().getAffectedLine() + "\n";
				}
			}
		}
		return res;
	}
	
	private String showFullGenerationInfoLowMemory(Integer generation) {
		String res = "";
		Map<Integer, Map<MutationOperator, Integer>> mutationsPerOperatorPerLine = this.mutationsNumberPerOperatorPerLinePerGeneration.get(generation);
		if (mutationsPerOperatorPerLine == null) {
			return "";
		}
		for (Entry<Integer, Map<MutationOperator, Integer>> mpopl : mutationsPerOperatorPerLine.entrySet()) {
			res += "mutations for line : " + mpopl.getKey() + "\n";
			for (Entry<MutationOperator, Integer> mpo : mpopl.getValue().entrySet()) {
				res += "\tmutations for operator " + (mpo.getKey()==null?"N/A":mpo.getKey().name()) + " : " + mpo.getValue() + "\n"; 
			}
		}
		return res;
	}

}
