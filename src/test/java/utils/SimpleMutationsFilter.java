package test.java.utils;

import java.util.List;

import mujava.api.Mutation;
import mujava.api.MutantsInformationHolder;

public abstract class SimpleMutationsFilter implements MutationsFilter {

	public abstract List<Mutation> getFilteredMutations(List<Mutation> mutations);

	public List<Mutation> getFilteredMutations(MutantsInformationHolder mih) {
		return getFilteredMutations(mih.getMutantsIdentifiers());
	}
	
	public abstract void filterMutations(List<Mutation> mutations);
	
	public void filterMutations(MutantsInformationHolder mih) {
		filterMutations(mih.getMutantsIdentifiers());
	}

}
