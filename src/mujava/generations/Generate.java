package mujava.generations;

import java.util.Arrays;
import java.util.List;

import mujava.api.Api;
import mujava.api.Configuration;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.app.Core;
import mujava.app.MutationRequest;
import mujava.op.PRVO;
import mujava.op.basic.COR;

public class Generate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String clazz = "pldi" + Core.SEPARATOR + "nodecachinglinkedlist" + Core.SEPARATOR + "NodeCachingLinkedList";
		String[] methods = {"contains"};
		Mutant[] ops = {	
//				Mutant.PRVOL_SMART,
//		        Mutant.PRVOR_REFINED,
//		        Mutant.PRVOU_REFINED,
//				Mutant.AODS,
//				Mutant.AODU,
//				Mutant.AOIS,
//				Mutant.AOIU,
//				Mutant.AORB,
//				Mutant.AORS,
//				Mutant.AORU,
//				Mutant.ASRS,
//				Mutant.COD,
//				Mutant.COI,
//				Mutant.COR,
//				Mutant.LOD,
//				Mutant.LOR,
//				Mutant.ROR,
//				Mutant.SOR 	
				Mutant.NPER
						};
		String basePathOriginals = args[0]; //test/
		String basePathMutants = args[1];	//mutants/
		
		MutantsInformationHolder.setVerbose(false);
		
		Configuration.add(PRVO.ENABLE_SUPER, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_LITERAL_STRINGS, Boolean.FALSE);
		List<String> bannedMethods = Arrays.asList(new String[]{"hashCode", "getClass", "toString", "toLowerCase", "intern", "toCharArray", "getBytes", "toUpperCase", "trim", "toLowerCase", "clone", "hash32", "serialPersistentFields", "serialVersionUID", "hash", "HASHING_SEED", "length", "isEmpty", "serialPersistentFields", "CASE_INSENSITIVE_ORDER"});
        Configuration.add(PRVO.PROHIBITED_METHODS, bannedMethods);
        Configuration.add(COR.ALLOW_BIT_AND, false);
        Configuration.add(COR.ALLOW_BIT_OR, false);
        Configuration.add(COR.ALLOW_LOGICAL_AND, false);
        Configuration.add(COR.ALLOW_LOGICAL_OR, false);
        Configuration.add(COR.ALLOW_XOR, false);
        Configuration.add(Api.USE_MUTGENLIMIT, Boolean.TRUE);
        Configuration.add(PRVO.ENABLE_PRIMITIVE_WRAPPING, Boolean.TRUE);
        Configuration.add(PRVO.ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS, Boolean.FALSE);
        Configuration.add(PRVO.ENABLE_ALL_BY_ONE_MUTANTS_LEFT, Boolean.TRUE);
        Configuration.add(PRVO.ENABLE_ALL_BY_ONE_MUTANTS_RIGHT, Boolean.TRUE);
        
        generate(clazz, methods, ops, basePathOriginals, basePathMutants, 1, false);
	}
	
	/**
	 * Generate several generations of mutants.
	 * 
	 * @param clazz					:	the clazz to mutate																												:	{@code String}
	 * @param methods				:	the methods to mutate																											:	{@code String[]}
	 * @param ops					:	mutation operators to use																										:	{@code Mutant[]}
	 * @param basePathOriginals		:	the folder where original class is found (e.g.: src/)																			:	{@code String}
	 * @param basePathMutants		:	the folder where mutants will be stored (e.g.: mutants/)																		:	{@code String}
	 * @param generations			:	how many generations will be generated (keep in mind that this value will not overwrite mutGenLimit annotations)				:	{@code int}
	 * @param lowMemory				:	if {@code true} less info will be outputed but less memory will be used (for more than 2-3 generations this should be enabled)	:	{@code boolean}
	 */
	public static void generate(String clazz, String[] methods, Mutant[] ops, String basePathOriginals, String basePathMutants, int generations, boolean lowMemory) {
		MutationRequest originalRequest = new MutationRequest(clazz, methods, ops, basePathOriginals, basePathMutants);
		GoalTester goalTester = new GenerationsGoalTester(generations);
		RequestGenerator requestGenerator = new SameRequestGenerator(originalRequest);
		Generator.useLowMemoryMode(lowMemory);
		Generator generator = new Generator(requestGenerator, goalTester, Generator.VERBOSE_LEVEL.FULL_VERBOSE);
		GenerationsInformation generationsInfo= generator.generate(false);
		System.out.println(generationsInfo.showBasicInformation());
	}

}
