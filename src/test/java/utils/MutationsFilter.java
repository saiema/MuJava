package test.java.utils;

import java.util.List;

import mujava.api.Mutation;
import mujava.api.MutantsInformationHolder;

public interface MutationsFilter {
	
	public List<Mutation> getFilteredMutations(List<Mutation> mutations);
	
	public List<Mutation> getFilteredMutations(MutantsInformationHolder mih);
	
	public void filterMutations(List<Mutation> mutations);
	
	public void filterMutations(MutantsInformationHolder mih);
	
}
