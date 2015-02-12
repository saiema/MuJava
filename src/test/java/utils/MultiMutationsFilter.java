package test.java.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mujava.api.Mutation;

public class MultiMutationsFilter extends SimpleMutationsFilter {
	Map<Integer, List<Mutation>> mutationsPerLine = new HashMap<Integer, List<Mutation>>();

	@Override
	public List<Mutation> getFilteredMutations(List<Mutation> mutations) {
		this.mutationsPerLine.clear();
		List<Mutation> filteredMutations = new LinkedList<Mutation>();
		for (Mutation m : mutations) {
			List<Mutation> mpl;
			if (this.mutationsPerLine.containsKey(m.getAffectedLine())) {
				mpl = this.mutationsPerLine.get(m.getAffectedLine());
			} else {
				mpl = new LinkedList<Mutation>();
			}
			mpl.add(m);
			this.mutationsPerLine.put(m.getAffectedLine(), mpl);
		}
		for (Entry<Integer, List<Mutation>> mpl : this.mutationsPerLine.entrySet()) {
			if (mpl.getKey() >= 0) {
				if (mpl.getValue().size() == 1) {
					filteredMutations.add(mpl.getValue().get(0));
					continue;
				}
				List<Mutation> validMutations = new LinkedList<Mutation>();
				List<Mutation> muts = mpl.getValue();
				for (Mutation m : muts) {
					if (m.isOneLineInMethodOp()) {
						validMutations.add(m);
					}
				}
				BruteForceMutationsFilter bfmf = new BruteForceMutationsFilter();
				filteredMutations.addAll(bfmf.getFilteredMutations(validMutations));
			}
		}
		return filteredMutations;
	}

	@Override
	public void filterMutations(List<Mutation> mutations) {
		List<Mutation> filteredMutations = getFilteredMutations(mutations);
		mutations.clear();
		mutations.addAll(filteredMutations);
	}

}
