package test.java.utils;

import java.util.List;

import mujava.api.Mutation;
import mujava.app.Mutator;

public class BruteForceMutationsFilter extends SimpleMutationsFilter {

	@Override
	public List<Mutation> getFilteredMutations(List<Mutation> mutations) {
		return Mutator.getFilteredCompatibleMutants(mutations);
	}

	@Override
	public void filterMutations(List<Mutation> mutations) {
		Mutator.filterCompatibleMutants(mutations);
	}

}
