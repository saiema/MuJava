package test.java.utils;

import java.util.LinkedList;
import java.util.List;

import openjava.ptree.ParseTreeObject;

import mujava.api.MutationOperator;
import mujava.api.Mutation;

public class SpecificMutationsFilter extends SimpleMutationsFilter {
	private List<MutationExpected> mutationsToAccept;
	
	public SpecificMutationsFilter(List<MutationExpected> mutationsToAccept) {
		this.mutationsToAccept = mutationsToAccept;
	}

	@Override
	public List<Mutation> getFilteredMutations(List<Mutation> mutations) {
		List<Mutation> muts = new LinkedList<Mutation>();
		if (this.mutationsToAccept != null && !this.mutationsToAccept.isEmpty()) {
			for (Mutation mi : mutations) {
				Integer affectedLine = mi.getAffectedLine();
				ParseTreeObject original = mi.getOriginal();
				ParseTreeObject mutant = mi.getMutant();
				MutationOperator opUsed = mi.getMutOp();
				for (MutationExpected me : this.mutationsToAccept) {
					if (me.compareExpectedWithObtained(original.toFlattenString(), mutant.toFlattenString(), affectedLine, opUsed)) {
						muts.add(mi);
						break;
					}
				}
			}
		}
		return muts;
	}

	@Override
	public void filterMutations(List<Mutation> mutations) {
		List<Mutation> filteredMutations = getFilteredMutations(mutations);
		mutations.clear();
		mutations.addAll(filteredMutations);
	}

}
