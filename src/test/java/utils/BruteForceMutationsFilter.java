package test.java.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mujava.api.Mutation;
import mujava.app.Mutator;

public class BruteForceMutationsFilter extends SimpleMutationsFilter {

	@Override
	public List<Mutation> getFilteredMutations(List<Mutation> mutations) {
		return Mutator.getFilteredCompatibleMutants(mutations);
	}

	@Override
	public void filterMutations(List<Mutation> mutations) {
		List<Mutation> filteredMuts = getFilteredMutations(mutations);
		mutations.clear();
		mutations.addAll(sameLineFiltered(filteredMuts));
//		Mutator.filterCompatibleMutants(mutations);
	}
	
	private List<Mutation> sameLineFiltered(List<Mutation> filteredMutations) {
		Map<Integer, List<Mutation>> mutationsPerLine = new TreeMap<>();
		for (Mutation m : filteredMutations) {
			Integer affectedLine = m.getAffectedLine();
			List<Mutation> mutsPL = null;
			if (mutationsPerLine.containsKey(affectedLine)) {
				mutsPL = mutationsPerLine.get(affectedLine);
			} else {
				mutsPL = new LinkedList<Mutation>();
				mutationsPerLine.put(affectedLine, mutsPL);
			}
			mutsPL.add(m);
		}
		Iterator<List<Mutation>> itMuts = mutationsPerLine.values().iterator();
		List<Mutation> sameLineMuts = null;
		if (itMuts.hasNext()) sameLineMuts = itMuts.next();
		while (itMuts.hasNext()) {
			List<Mutation> currMuts = itMuts.next();
			if (currMuts.size() > sameLineMuts.size()) sameLineMuts = currMuts;
		}
		return sameLineMuts;
	}

}
