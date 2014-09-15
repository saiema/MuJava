package test.java.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mujava.api.Mutation;

public class ManualMutationsFilter extends SimpleMutationsFilter {
	private Set<Integer> selectedMutations;
	
	public void selectMutations(Set<Integer> selectedMutations) {
		this.selectedMutations = selectedMutations;
	}

	@Override
	public List<Mutation> getFilteredMutations(List<Mutation> mutations) {
		List<Mutation> filteredMutations = new LinkedList<Mutation>();
		if (this.selectedMutations != null) {
			for (Integer idx : this.selectedMutations) {
				filteredMutations.add(mutations.get(idx));
			}
			return filteredMutations;
		}
		return null;
	}

	@Override
	public void filterMutations(List<Mutation> mutations) {
		List<Mutation> filtered = new LinkedList<Mutation>();
		if (this.selectedMutations != null) {
			for (Integer idx : this.selectedMutations) {
				filtered.add(mutations.get(idx));
			}
			mutations.clear();
			mutations.addAll(filtered);
		}
	}

}
