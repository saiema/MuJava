package mujava.generations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mujava.api.Mutant;
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
	private Map<Integer, Map<Integer, Map<Mutant, Integer>>> mutationsNumberPerOperatorPerLinePerGeneration;
	private Map<Integer, Map<Integer, List<MutantInfo>>> mutationsPerLinePerGeneration;
	private Map<Integer, Map<Integer, Map<Mutant, List<MutantInfo>>>> mutationsPerOperatorPerLinePerGenerations;
	private Integer lastGenerationUpdated = -1;
	private List<MutantInfo> lastMutantsUpdated = null;
	private boolean lowMemoryMode;
	
	/**
	 * The constructor for the class
	 */
	public GenerationsInformation(boolean lowMemoryMode) {
		if (lowMemoryMode) {
			this.lastGeneration = new LinkedList<MutantInfo>();
			this.mutationsNumberPerOperatorPerLinePerGeneration = new HashMap<Integer, Map<Integer, Map<Mutant, Integer>>>();
			this.lowMemoryMode = true;
		} else {
			this.generations = new HashMap<Integer, List<MutantInfo>>();
			this.mutationsPerLinePerGeneration = new HashMap<Integer, Map<Integer, List<MutantInfo>>>();
			this.mutationsPerOperatorPerLinePerGenerations = new HashMap<Integer, Map<Integer, Map<Mutant, List<MutantInfo>>>>();
			this.lastMutantsUpdated = new LinkedList<MutantInfo>();
			this.lowMemoryMode = false;
		}
		
	}
	
	/**
	 * The constructor for the class
	 */
	public GenerationsInformation() {
		this.generations = new HashMap<Integer, List<MutantInfo>>();
		this.mutationsPerLinePerGeneration = new HashMap<Integer, Map<Integer, List<MutantInfo>>>();
		this.mutationsPerOperatorPerLinePerGenerations = new HashMap<Integer, Map<Integer, Map<Mutant, List<MutantInfo>>>>();
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
		Map<Integer, Map<Mutant, Integer>> mutationsPerOperatorPerLine;
		if (this.mutationsNumberPerOperatorPerLinePerGeneration.containsKey(generation)) {
			mutationsPerOperatorPerLine = this.mutationsNumberPerOperatorPerLinePerGeneration.get(generation);
		} else {
			mutationsPerOperatorPerLine = new HashMap<Integer, Map<Mutant, Integer>>();
			this.mutationsNumberPerOperatorPerLinePerGeneration.put(generation, mutationsPerOperatorPerLine);
		}
		Map<Mutant, Integer> mutationsPerOperator;
		if (mutationsPerOperatorPerLine.containsKey(mutant.getMutatedLine())) {
			mutationsPerOperator = mutationsPerOperatorPerLine.get(mutant.getMutatedLine());
		} else {
			mutationsPerOperator = new HashMap<Mutant, Integer>();
			mutationsPerOperatorPerLine.put(mutant.getMutatedLine(), mutationsPerOperator);
		}
		Integer mutations;
		if (mutationsPerOperator.containsKey(mutant.getOpUsed())) {
			mutations = mutationsPerOperator.get(mutant.getOpUsed());
		} else {
			mutations = new Integer(0);
			mutationsPerOperator.put(mutant.getOpUsed(), mutations);
		}
		mutations++;
	}
	
	private void addMutantPerLine(Integer generation, MutantInfo mutant) {
		Map<Integer, List<MutantInfo>> genMutantsPerLine;
		if (this.mutationsPerLinePerGeneration.containsKey(generation)) {
			genMutantsPerLine = this.mutationsPerLinePerGeneration.get(generation);
		} else {
			genMutantsPerLine = new HashMap<Integer, List<MutantInfo>>();
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
		Map<Integer, Map<Mutant, List<MutantInfo>>> mutationsPerOperatorPerLine;
		if (this.mutationsPerOperatorPerLinePerGenerations.containsKey(generation)) {
			mutationsPerOperatorPerLine = this.mutationsPerOperatorPerLinePerGenerations.get(generation);
		} else {
			mutationsPerOperatorPerLine = new HashMap<Integer, Map<Mutant, List<MutantInfo>>>();
			this.mutationsPerOperatorPerLinePerGenerations.put(generation, mutationsPerOperatorPerLine);
		}
		Map<Mutant, List<MutantInfo>> mutationsPerOperator;
		if (mutationsPerOperatorPerLine.containsKey(mutant.getMutatedLine())) {
			mutationsPerOperator = mutationsPerOperatorPerLine.get(mutant.getMutatedLine());
		} else {
			mutationsPerOperator = new HashMap<Mutant, List<MutantInfo>>();
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
		int genSize = 0;
		Map<Integer, Map<Mutant, Integer>> mutationsPerOperatorPerLine = this.mutationsNumberPerOperatorPerLinePerGeneration.get(generation);
		if (mutationsPerOperatorPerLine == null) {
			return "";
		}
		for (Entry<Integer, Map<Mutant, Integer>> mpopl : mutationsPerOperatorPerLine.entrySet()) {
			for (Entry<Mutant, Integer> mpo : mpopl.getValue().entrySet()) {
				genSize += mpo.getValue(); 
			}
		}
		String res = (this.generations.containsKey(generation)?("Generation : " + generation + " | mutants " + genSize):("There's no generation " + generation)) + "\n";
		if (this.generations.containsKey(generation) && fullInfo) {
			res += showFullGenerationInfoLowMemory(generation);
		}
		return res;
	}
	
	public String showFullGenerationInfo(Integer generation) {
		if (this.lowMemoryMode) {
			return showFullGenerationInfoLowMemory(generation);
		}
		String res = "";
		Map<Integer, Map<Mutant, List<MutantInfo>>> mutationsPerOperatorPerLine = this.mutationsPerOperatorPerLinePerGenerations.get(generation);
		if (mutationsPerOperatorPerLine == null) {
			return "";
		}
		for (Entry<Integer, Map<Mutant, List<MutantInfo>>> mpopl : mutationsPerOperatorPerLine.entrySet()) {
			res += "mutations for line : " + mpopl.getKey() + "\n";
			for (Entry<Mutant, List<MutantInfo>> mpo : mpopl.getValue().entrySet()) {
				res += "\tmutations for operator " + (mpo.getKey()==null?"N/A":mpo.getKey().name()) + " : " + mpo.getValue().size() + "\n"; 
				for (MutantInfo mi : mpo.getValue()) {
					res += "\t\tmutation: ";
					if (mi.severalMutationsApplied()) {
						res += "\n";
						for (Mutation mid : mi.getMutations()) {
							res += "\t\t\t" + mid.getOriginal().toFlattenString() + " => " + mid.getMutant().toFlattenString() + "\n";
						}
					} else {
						res += "\t\t\t" + mi.getMutation().getOriginal().toFlattenString() + " => " + mi.getMutation().getMutant().toFlattenString() + "\n";
					}
				}
			}
		}
		return res;
	}
	
	private String showFullGenerationInfoLowMemory(Integer generation) {
		String res = "";
		Map<Integer, Map<Mutant, Integer>> mutationsPerOperatorPerLine = this.mutationsNumberPerOperatorPerLinePerGeneration.get(generation);
		if (mutationsPerOperatorPerLine == null) {
			return "";
		}
		for (Entry<Integer, Map<Mutant, Integer>> mpopl : mutationsPerOperatorPerLine.entrySet()) {
			res += "mutations for line : " + mpopl.getKey() + "\n";
			for (Entry<Mutant, Integer> mpo : mpopl.getValue().entrySet()) {
				res += "\tmutations for operator " + (mpo.getKey()==null?"N/A":mpo.getKey().name()) + " : " + mpo.getValue() + "\n"; 
			}
		}
		return res;
	}

}
