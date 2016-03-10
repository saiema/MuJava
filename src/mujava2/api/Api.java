package mujava2.api;

import java.util.LinkedList;
import java.util.List;

import mujava.app.MutationInformation;
import mujava2.api.mutator.MutationRequest;
import mujava2.api.program.MutatedAST;

public class Api {
	
	public static List<MutatedAST> generateMutants(MutationRequest request) {
		throw new UnsupportedOperationException("Simon will be implementing this method \"soon\"");
	}
	
	public static List<MutationInformation> generateOneGenerationMutations(MutationRequest request) {
		MutationRequest oneGenRequest = request.clone();
		oneGenRequest.changeGenerations(1);
		List<List<MutationInformation>> oneGenMutations = generateManyGenerationsMutations(oneGenRequest);
		return oneGenMutations.isEmpty()?new LinkedList<MutationInformation>():oneGenMutations.get(0);
	}
	
	public static List<List<MutationInformation>> generateManyGenerationsMutations(MutationRequest request) {
		throw new UnsupportedOperationException("Simon will be implementing this method \"soon\"");
	}

}
