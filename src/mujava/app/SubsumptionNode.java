package mujava.app;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import mujava.api.MutationOperator;
import static mujava.api.MutationOperator.*;
import mujava.util.JustCodeDigest;

public class SubsumptionNode implements Comparable<SubsumptionNode>{
	
	public static boolean fullPrint = false;
	private Map<String, boolean[]> failedTestsPerClass;
	private final String ID;
	private List<MutantInfo> mutants;
	private Set<SubsumptionNode> subsumedNodes;
	private Set<SubsumptionNode> subsumingNodes;
	private Map<String,Integer> failingTestsCountPerClass = new TreeMap<>();
	private boolean hasAtleastOneFailingTests = false;
	
	public SubsumptionNode(MutantInfo mi, Map<String, boolean[]> failedTestsPerClass) {
		mutants = new LinkedList<>();
		subsumedNodes = new HashSet<>();
		subsumingNodes = new HashSet<>();
		mutants.add(mi);
		StringBuilder sb = new StringBuilder("");
		Iterator<MutationOperator> it = mi.getMutantOperators().iterator();
		while (it.hasNext()) {
			sb.append(it.next().name());
			if (it.hasNext()) sb.append("_");
		}
		ID = sb.toString();
		this.failedTestsPerClass = failedTestsPerClass;
		for (Entry<String, boolean[]> entry : failedTestsPerClass.entrySet()) {
			int count = 0;
			for (boolean b : entry.getValue()) if (b) {
				count++;
				hasAtleastOneFailingTests = true;
			}
			failingTestsCountPerClass.put(entry.getKey(), count);
		}
	}
	
	public boolean hasFailingTests() {
		return hasAtleastOneFailingTests;
	}
	
	public boolean subsume(SubsumptionNode other) {
		if (other == null) {
			throw new IllegalArgumentException("mujava.app.SubsumptionNode#subsume(SubsumptionNode) : null argument");
		}
		if (!hasFailingTests() || !other.hasFailingTests()) return false;
		for (String testClass : failedTestsPerClass.keySet()) {
			if (failingTestsCountPerClass.containsKey(testClass) && failingTestsCountPerClass.get(testClass) > 0) {
				if (!other.failingTestsCountPerClass.containsKey(testClass)) return false;
				if (other.failingTestsCountPerClass.get(testClass) == 0) return false;
				if (!testClassResultsSubsumed(other, testClass)) return false;
			}
		}
		return true;
	}
	
	private boolean testClassResultsSubsumed(SubsumptionNode other, String testClass) {
		boolean[] failedTestsForClass = failedTestsPerClass.get(testClass);
		boolean[] ofailedTestsForClass = other.failedTestsPerClass.get(testClass);
		if (failedTestsForClass.length != ofailedTestsForClass.length) {
			throw new IllegalArgumentException("mujava.app.SubsumptionNode#testClassResultsSubsumed(SubsumptionNode, String) : nodes have different amount of tests for test class " + testClass);
		}
		for (int i = 0; i < failedTestsForClass.length; i++) {
			if (failedTestsForClass[i]) {
				if (!ofailedTestsForClass[i]) return false;
			}
		}
		return true;
	}
	
	public boolean equivalent(SubsumptionNode other) {
		return subsume(other) && other.subsume(this);
	}
	
	public boolean isDominatorWithRespectTo(List<SubsumptionNode> others) {
		if (others == null || others.isEmpty()) throw new IllegalArgumentException("mujava.app.SubsumptionNode#isDominator(List<SubsumptionNode>) : list can't be empty or null");
		boolean subsumesAtLeastOne = false;
		for (SubsumptionNode o : others) {
			if (this == o) continue;
			if (o.subsume(this)) return false;
			if (!subsumesAtLeastOne && subsume(o)) {
				subsumesAtLeastOne = true;
			}
		}
		return subsumesAtLeastOne;
	}
	
	public boolean mergeIfEquivalent(SubsumptionNode other) {
		if (equivalent(other)) {
			mutants.addAll(other.mutants);
			return true;
		}
		return false;
	}
	
	public void merge(SubsumptionNode other) {
		for (Entry<String, boolean[]> oentry : other.failedTestsPerClass.entrySet()) {
			String testClass = oentry.getKey();
			if (!failedTestsPerClass.containsKey(testClass)) {
				failedTestsPerClass.put(testClass, oentry.getValue());
				int ocount = other.failingTestsCountPerClass.get(testClass);
				failingTestsCountPerClass.put(testClass, ocount);
				if (!hasAtleastOneFailingTests && ocount > 0) hasAtleastOneFailingTests = true;
			} else {
				boolean[] failedTests = failedTestsPerClass.get(testClass);
				boolean[] ofailedTests = other.failedTestsPerClass.get(testClass);
				if (failedTests.length != ofailedTests.length) {
					throw new IllegalArgumentException("mujava.app.SubsumptionNode#merge(SubsumptionNode) : nodes have different amount of tests for test class " + testClass);
				}
				int currentCount = failingTestsCountPerClass.get(testClass);
				for (int i = 0; i < failedTests.length; i++) {
					if (!failedTests[i] && ofailedTests[i]) {
						currentCount++;
						failedTests[i] = true;
					}
				}
				hasAtleastOneFailingTests = true;
				failingTestsCountPerClass.put(testClass, currentCount);
			}
		}
	}
	
	public void addSubsumedNode(SubsumptionNode n) {
		subsumedNodes.add(n);
	}
	
	public void addSubsumingdNode(SubsumptionNode n) {
		subsumingNodes.add(n);
	}
	
	public Set<SubsumptionNode> getSubsumedNodes() {
		return subsumedNodes;
	}
	
	public Set<SubsumptionNode> getSubsumingNodes() {
		return subsumingNodes;
	}
	
	public boolean isEquivalentToOriginal() {
		return !hasFailingTests();
	}
	
	public String getMutantsID() {
		StringBuilder sb = new StringBuilder();
		Iterator<MutantInfo> it = mutants.iterator();
		while (it.hasNext()) {
			MutantInfo mi = it.next();
			sb.append(Arrays.toString(mi.getMD5digest()));
			if (it.hasNext()) sb.append(".");
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o instanceof SubsumptionNode) {
			SubsumptionNode other = (SubsumptionNode) o;
			String[] hashes = getMutantsID().split(".");
			String omid = other.getMutantsID();
			for (String h : hashes) {
				omid.replace(h, "");
			}
			return omid.trim().isEmpty();
		}
		return false;
	}
	
	public List<MutantInfo> getMutants() {
		return mutants;
	}
	
	public String nodeName() {
		String hash =JustCodeDigest.toHex(JustCodeDigest.digest(getMutantsID(), false));
		StringBuilder sb = new StringBuilder();
		sb.append(ID).append("_").append(mutants.size()).append("_").append(hash);
		return sb.toString();
	}
	
	public String shortNodeName() {
		String hash =JustCodeDigest.toHex(JustCodeDigest.digest(getMutantsID(), false));
		StringBuilder sb = new StringBuilder();
		sb.append(ID).append("_").append(mutants.size()).append("_").append(hash.substring(0, 7));
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(nodeName());
		if (isDominator()) sb.append("-DOMINATOR");
		if (isPure()) sb.append("-PURE");
		if (isLeaf()) sb.append("-LEAF");
		if (SubsumptionNode.fullPrint) {
			sb.append("\n");
			Iterator<Entry<String, boolean[]>> it = failedTestsPerClass.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, boolean[]> curr = it.next();
				sb.append("Test class : ").append(curr.getKey()).append(Arrays.toString(curr.getValue()));
				sb.append("\n");
			}
			Iterator<MutantInfo> mit = mutants.iterator();
			while (mit.hasNext()) {
				sb.append(mit.next().toString());
				if (mit.hasNext()) sb.append("\n");
			}
			
		}
		if (SubsumptionNode.fullPrint) {
			boolean fullPrintBackup = SubsumptionNode.fullPrint;
			SubsumptionNode.fullPrint = false;
			sb.append("\n");
			if (!isDominator() &&  !subsumingNodes.isEmpty()) {
				sb.append("Subsuming nodes: \n");
				for (SubsumptionNode subsuming : subsumingNodes) {
					sb.append(subsuming.toString()).append("\n");
				}
			}
			if (!subsumedNodes.isEmpty()) {
				sb.append("Subsumed nodes: \n");
				for (SubsumptionNode subsumed : subsumedNodes) {
					sb.append(subsumed.toString()).append("\n");
				}
			}
			SubsumptionNode.fullPrint = fullPrintBackup;
		}
		return sb.toString();
	}

	@Override
	public int compareTo(SubsumptionNode o) {
		if (this == o) return 0;
		int failingTests = 0;
		int ofailingTests = 0;
		for (Entry<String, Integer> entry : failingTestsCountPerClass.entrySet()) {
			int fcount = o.failingTestsCountPerClass.get(entry.getKey());
			if (o.failingTestsCountPerClass.containsKey(entry.getKey())) {
				failingTests += (fcount - o.failingTestsCountPerClass.get(entry.getKey()));
			} else {
				failingTests += fcount;
			}
		}
		int subsumed = subsumedNodes.size();
		int subsuming = subsumingNodes.size();
		int osubsumed = o.subsumedNodes.size();
		int osubsuming = o.subsumingNodes.size();
		int value = failingTests + (subsumed - subsuming);
		int ovalue = ofailingTests + (osubsumed - osubsuming);
		return value - ovalue;
	}
	
	public boolean isDominator() {
		//return subsumingNodes.isEmpty() && !subsumedNodes.isEmpty();
		return subsumingNodes.isEmpty() && !isEquivalentToOriginal();
	}
	
	public boolean isPure() {
		MutationOperator first = null;
		for (MutantInfo m : mutants) {
			if (first == null) {
				first = m.getOpUsed();
			} else if (isPRVO(first) && !isPRVO(m.getOpUsed())) {
				return false;
			} else if (!isPRVO(first) && isPRVO(m.getOpUsed())) {
				return false;
			} else if (first.compareTo(m.getOpUsed()) != 0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isLeaf() {
		return subsumedNodes.isEmpty() && !subsumingNodes.isEmpty() && !isEquivalentToOriginal();
	}
	
}
