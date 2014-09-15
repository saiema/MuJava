package test.java.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mujava.api.Mutation;

public class LineMutationsFilter extends SimpleMutationsFilter {
	private Set<Integer> acceptedLines;
	
	public void setAcceptedLines(Set<Integer> lines) {
		this.acceptedLines = lines;
	}

	@Override
	public List<Mutation> getFilteredMutations(List<Mutation> mutations) {
		List<Mutation> filteredMutations = new LinkedList<Mutation>();
		if (this.acceptedLines != null) {
			for (Mutation mi : mutations) {
				if (this.acceptedLines.contains(mi.getAffectedLine())) {
					filteredMutations.add(mi);
				}
			}
		}
		return filteredMutations;
	}

	@Override
	public void filterMutations(List<Mutation> mutations) {
		List<Mutation> filtered = getFilteredMutations(mutations);
		mutations.clear();
		mutations.addAll(filtered);
	}

}
