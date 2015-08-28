package mujava.generations;

import java.util.Arrays;
import java.util.List;

import openjava.ptree.ParseTreeException;
import mujava.OpenJavaException;
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
	 * @throws ParseTreeException 
	 * @throws OpenJavaException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, OpenJavaException, ParseTreeException {
		//String clazz = "pldi" + Core.SEPARATOR + "nodecachinglinkedlist" + Core.SEPARATOR + "NodeCachingLinkedList";
		String clazz = "roops" + Core.SEPARATOR + "core" + Core.SEPARATOR + "objects" + Core.SEPARATOR + "SinglyLinkedList";
		//String clazz = "rfm" + Core.SEPARATOR + "testingtool" + Core.SEPARATOR + "structures" + Core.SEPARATOR + "bstree" + Core.SEPARATOR + "BSTree";
		String[] methods = {"getNodeBis"};
		//String[] methods = {"remove"};// newNode removeNode smallest"};
		Mutant[] ops = {	
//				Mutant.PRVOL_SMART,
//		        Mutant.PRVOR_REFINED,
//		        Mutant.PRVOU_REFINED,
				Mutant.PRVOL_SMART,
		        Mutant.PRVOR_REFINED,
		        Mutant.PRVOU_REFINED,
				Mutant.AODS,
				Mutant.AODU,
				Mutant.AOIS,
				Mutant.AOIU,
				Mutant.AORB,
				Mutant.AORS,
				Mutant.AORU,
				Mutant.ASRS,
				Mutant.COD,
				Mutant.COI,
				Mutant.COR,
				Mutant.LOD,
				Mutant.LOR,
				Mutant.ROR,
				Mutant.SOR,
//		        Mutant.AODS,
//		        Mutant.AODU,
//		        Mutant.AOIS,
//		        Mutant.AORB,
//		        Mutant.AORS,
//		        Mutant.AORU,
//		        Mutant.ASRS,
//		        Mutant.COI,
//		        Mutant.COR,
//		        Mutant.LOD,
//		        Mutant.LOI,
//		        Mutant.LOR,
//		        Mutant.ROR,
//		        Mutant.SOR,
//		        Mutant.EMM,
//		        Mutant.EOC,
//		        Mutant.IPC,
//		        Mutant.EAM,
//		        Mutant.IOP,
//		        Mutant.JTI_SMART,
//		        Mutant.JTD,
//		        Mutant.ISD_SMART,
//		        Mutant.ISI_SMART,
//		        Mutant.COD,
//		        Mutant.AOIU,
//		        Mutant.OAN_RELAXED,
//		        Mutant.EOA,
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
//				Mutant.NPER
						};
		String basePathOriginals = args[0]; //test/
		String basePathMutants = args[1];	//mutants/
		
		MutantsInformationHolder.setVerbose(false);
		
//		Configuration.add(PRVO.ENABLE_SUPER, Boolean.FALSE);
//		Configuration.add(PRVO.ENABLE_LITERAL_STRINGS, Boolean.FALSE);
//		List<String> bannedMethods = Arrays.asList(new String[]{"hashCode", "getClass", "toString", "toLowerCase", "intern", "toCharArray", "getBytes", "toUpperCase", "trim", "toLowerCase", "clone", "hash32", "serialPersistentFields", "serialVersionUID", "hash", "HASHING_SEED", "length", "isEmpty", "serialPersistentFields", "CASE_INSENSITIVE_ORDER"});
//        Configuration.add(PRVO.PROHIBITED_METHODS, bannedMethods);
//        Configuration.add(COR.ALLOW_BIT_AND, false);
//        Configuration.add(COR.ALLOW_BIT_OR, false);
//        Configuration.add(COR.ALLOW_LOGICAL_AND, false);
//        Configuration.add(COR.ALLOW_LOGICAL_OR, false);
//        Configuration.add(COR.ALLOW_XOR, false);
//        Configuration.add(Api.USE_MUTGENLIMIT, Boolean.TRUE);
//        Configuration.add(PRVO.ENABLE_PRIMITIVE_WRAPPING, Boolean.TRUE);
//        Configuration.add(PRVO.ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS, Boolean.FALSE);
//        Configuration.add(PRVO.ENABLE_ALL_BY_ONE_MUTANTS_LEFT, Boolean.TRUE);
//        Configuration.add(PRVO.ENABLE_ALL_BY_ONE_MUTANTS_RIGHT, Boolean.TRUE);
		
		List<String> bannedMethods = Arrays.asList(new String[]{"extractMin", "getClass", "toString", "toLowerCase", "intern", "toCharArray", "getBytes", "toUpperCase", "trim", "toLowerCase", "clone", "hash32", "serialPersistentFields", "hash", "hashCode"});
		List<String> bannedFields = Arrays.asList(new String[]{"serialVersionUID", "hash32", "HASHING_SEED", "CASE_INSENSITIVE_ORDER", "serialPersistentFields"});
		Configuration.add(PRVO.PROHIBITED_METHODS, bannedMethods);
        Configuration.add(PRVO.PROHIBITED_FIELDS, bannedFields);
		Configuration.add(COR.ALLOW_BIT_AND, false);
        Configuration.add(COR.ALLOW_BIT_OR, false);
        Configuration.add(COR.ALLOW_LOGICAL_AND, false);
        Configuration.add(COR.ALLOW_LOGICAL_OR, false);
        Configuration.add(COR.ALLOW_XOR, false);
        Configuration.add(PRVO.ENABLE_SUPER, Boolean.FALSE); //Boolean.FALSE para desactivar el uso de super
        //                    Configuration.add(PRVO.ENABLE_THIS, Boolean.FALSE);     //Boolean.FALSE para desactivar el uso de this
        Configuration.add(PRVO.ENABLE_LITERAL_EMPTY_STRING, Boolean.FALSE);
        Configuration.add(PRVO.ENABLE_ONE_BY_TWO_MUTANTS, Boolean.FALSE);
        Configuration.add(PRVO.ENABLE_PRIMITIVE_WRAPPING, Boolean.FALSE);
        Configuration.add(PRVO.ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS, Boolean.FALSE);
        
        generate(clazz, methods, ops, basePathOriginals, basePathMutants, 4, true);
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
	 * @throws ParseTreeException 
	 * @throws OpenJavaException 
	 * @throws ClassNotFoundException 
	 */
	public static void generate(String clazz, String[] methods, Mutant[] ops, String basePathOriginals, String basePathMutants, int generations, boolean lowMemory) throws ClassNotFoundException, OpenJavaException, ParseTreeException {
		MutationRequest originalRequest = new MutationRequest(clazz, methods, ops, basePathOriginals, basePathMutants);
		GoalTester goalTester = new GenerationsGoalTester(generations);
		RequestGenerator requestGenerator = new SameRequestGenerator(originalRequest);
		Generator.useLowMemoryMode(lowMemory);
		Generator generator = new Generator(requestGenerator, goalTester, Generator.VERBOSE_LEVEL.FULL_VERBOSE);
		GenerationsInformation generationsInfo= generator.generate(false, false);
		System.out.println(generationsInfo.showBasicInformation());
	}

}
