package mujava2.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import mujava.OpenJavaException;
import mujava.app.MutationInformation;
import mujava2.api.mutator.MutationRequest;
import mujava2.api.mutator.Mutator;
import mujava2.api.program.JavaAST;
import mujava2.api.program.MutatedAST;
import openjava.ptree.ParseTreeException;

public class Api {
	
	private static boolean verbose = false;
	
	public static void setVerbose(boolean v) {
		Api.verbose = v;
	}
	
	public static List<MutatedAST> generateMutants(MutationRequest request) throws OpenJavaException, ParseTreeException {
		
		//GENERATE ORIGINAL AST
		//GENERATE MUTANT AST (generation 1)
		//FOR EACH MUTANT AST (generation 1)
		//  RECURSIVE MUTANT AST GENERATIONS
		
		List<MutatedAST> result = new LinkedList<>();
		
		JavaAST ast = JavaAST.fromFile(request.getLocation(), request.getClassToMutate());
		
		if (Api.verbose) {
			System.out.println("Mutating " + ast.toString() + "\n");
		}
		
		Mutator mutator = new Mutator(ast, request);
		Collection<MutationInformation> mutations = mutator.generateMutations();
		
		if (Api.verbose) {
			System.out.println("First generation mutations\n");
			for (MutationInformation minfo : mutations) {
				System.out.println(minfo.toString());
			}
			System.out.println();
		}
		
		for (MutationInformation minfo : mutations) {
			MutatedAST mast = new MutatedAST(ast, minfo);
			result.add(mast);
			generateMutants(mast, request, 2, result);
		}
		
		return result;
	}
	
	private static void generateMutants(MutatedAST mast, MutationRequest request, int currGen, List<MutatedAST> result) throws ParseTreeException, OpenJavaException {
		if (currGen <= request.getGenerations()) {
			
			//APPLY MUTATIONS TO mast
			JavaAST ast = mast.applyMutations();
			if (Api.verbose) {
				System.out.println("Generating " + currGen + " generation mutations for \n" + mast.toString() + "\n");
			}
			Mutator mutator = new Mutator(ast, request);
			Collection<MutationInformation> mutations = mutator.generateMutations();
			if (Api.verbose) {
				for (MutationInformation minfo : mutations) {
					System.out.println(minfo.toString());
				}
				System.out.println();
			}
			//UNDO MUTATIONS TO mast
			mast.undoMutations();
			if (Api.verbose) {
				System.out.println("Undo applied mutations\n" + mast.toString() + "\n");
				System.out.println(currGen + " generation mutants\n");
			}
			for (MutationInformation minfo : mutations) {
				MutatedAST newMast = new MutatedAST(mast, minfo);
				if (Api.verbose) System.out.println(newMast.toString());
				result.add(newMast);
				generateMutants(newMast, request, currGen+1, result);
			}
			
		}
	}
	
	public static List<MutatedAST> generateLastGeneration(MutationRequest request) throws OpenJavaException, ParseTreeException {
		List<MutatedAST> result = new LinkedList<>();
		
		JavaAST ast = JavaAST.fromFile(request.getLocation(), request.getClassToMutate());
		
		Mutator mutator = new Mutator(ast, request);
		Collection<MutationInformation> mutations = mutator.generateMutations();
		
		for (MutationInformation minfo : mutations) {
			MutatedAST mast = new MutatedAST(ast, minfo);
			generateLastGenerationMutants(mast, request, 2, result);
		}
		
		return result;
	}
	
	private static void generateLastGenerationMutants(MutatedAST mast, MutationRequest request, int currGen, List<MutatedAST> result) throws ParseTreeException, OpenJavaException {
		if (currGen <= request.getGenerations()) {
			
			//APPLY MUTATIONS TO mast
			JavaAST ast = mast.applyMutations();
			Mutator mutator = new Mutator(ast, request);
			Collection<MutationInformation> mutations = mutator.generateMutations();
			//UNDO MUTATIONS TO mast
			mast.undoMutations();
			for (MutationInformation minfo : mutations) {
				MutatedAST newMast = new MutatedAST(mast, minfo);
				generateLastGenerationMutants(newMast, request, currGen+1, result);
			}	
		} else {
			result.add(mast);
		}
	}
	
	public static List<List<MutationInformation>> generateMutationGenerations(MutationRequest request) throws OpenJavaException, ParseTreeException {
		JavaAST ast = JavaAST.fromFile(request.getLocation(), request.getClassToMutate());
		return generateMutationGenerations(ast, request);
	}
	
	private static Collection<MutationInformation> generateMutations(JavaAST ast, MutationRequest request) throws OpenJavaException, ParseTreeException {
		Mutator mutator = new Mutator(ast, request);
		Collection<MutationInformation> mutations = mutator.generateMutations();
		return mutations;
	}
	
	public static List<List<MutationInformation>> generateMutationGenerations(JavaAST ast, MutationRequest request) throws OpenJavaException, ParseTreeException {
		/*
		 * 
		 * Gen 1 
		 * 
		 * -----> Gen 2 - Gen G (0)
		 * ----------> Gen 3 - Gen G (0)
		 * ---------------> Gen 4 - Gen G (0)
		 * ---------------> ...
		 * ---------------> Gen 4 - Gen G (n'')
		 * ----------> ...
		 * ----------> Gen 3 - Gen G (n')
		 * -----> Gen 2 - Gen G (1)
		 * -----> ...
		 * -----> Gen 2 - Gen G (n)
		 * 
		 * n 	= # Gen 1
		 * n'	= # Gen 2(0)
		 * n''	= # Gen 3(0) from Gen 2(0)
		 * 
		*/
		List<List<MutationInformation>> generations = new LinkedList<>();
		List<MutationInformation> firstGeneration = new LinkedList<>();
		firstGeneration.addAll(generateMutations(ast, request));
		generations.add(firstGeneration);
		
		if (request.getGenerations() > 1) {
			Iterator<MutationInformation> lastGenIt = firstGeneration.iterator();
			while (lastGenIt.hasNext()) {
				MutatedAST mast = new MutatedAST(ast, lastGenIt.next());
				JavaAST newAST = mast.applyMutations();
				int gen = request.getGenerations();
				request.changeGenerations(request.getGenerations() - 1);
				merge(generations, generateMutationGenerations(newAST, request), 1);
				request.changeGenerations(gen);
				mast.undoMutations();
			}
		}
		
		return generations;
	}

	/**
	 * Merge that into thiz from thiz[offset]
	 * 
	 * @param thiz
	 * @param that
	 */
	private static void merge(List<List<MutationInformation>> thiz, List<List<MutationInformation>> that, int offset) {
		for (int i = 0; i < that.size(); i++) {
			if (thiz.size() < i + offset) {
				thiz.add(that.get(i));
			} else {
				thiz.get(i+offset).addAll(that.get(i));
			}
		}
		
	}

}
