package mujava.generations;

import mujava.app.MutationRequest;

/**
 * This interface is used in conjuntion with the class {@code Generator} and is used to obtain the
 * {@code MutationRequest} that will be used to generate the next mutant generation
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 0.1
 */
public interface RequestGenerator {

	/**
	 * @return the request that will be used to generate the next mutant generation : {@code MutationRequest}
	 */
	public MutationRequest nextRequest();
	
	/**
	 * updates this class with the information of all the mutants on each generation and the last generation produced
	 * 
	 * @param generationsInformation	:	the information of all generations produced and the mutants on every one of them	: {@code GenerationsInformation}
	 * @param lastGeneration			:	the last generation produced	: {@code Integer}
	 */
	public void update(GenerationsInformation generationsInformation, Integer lastGeneration);
	
}
