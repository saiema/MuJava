package mujava2.api;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import mujava.OpenJavaException;
import mujava.api.Configuration;
import mujava.api.MutantsInformationHolder;
import mujava.api.MutationOperator;
import mujava.app.MutationInformation;
import mujava2.api.mutator.MutationRequest;
import mujava2.api.mutator.Mutator;
import mujava2.api.program.JavaAST;
import mujava2.api.program.MutatedAST;
import openjava.ptree.ParseTreeException;
import static mujava.api.MutationOperator.*;

public class TestMJ2 {

	public static void main(String[] args) throws OpenJavaException, IOException, ParseTreeException {
		Configuration.add(Configuration.USE_MUTGENLIMIT, Boolean.FALSE);
		
		
		JavaAST utilBooleanOpsAST = JavaAST.fromFile("test", "utils.BooleanOps");
//		byte[] hash = utilBooleanOpsAST.writeInFolder(new File("/home/stein/Desktop/TEST/original/"), true);
//		System.out.println("utils.BooleanOps AST written with hash : " + Arrays.toString(hash));
//		
//		Collection<MutationOperator> ops = Arrays.asList(COI);
//		Collection<String> methods = Arrays.asList("or");
//		MutationRequest req = new MutationRequest("test", "utils.BooleanOps", ops, methods, false, false, 1);
//		Mutator mutator = new Mutator(utilBooleanOpsAST, req);
//		Collection<MutationInformation> mutations = mutator.generateMutations();
//		System.out.println("Mutations for utils.BooleanOps:\n");
//		for (MutationInformation minfo : mutations) {
//			System.out.println(minfo.toString()+"\n");
//		}
//		MutatedAST mutatedAST = new MutatedAST(utilBooleanOpsAST, mutations.iterator().next());
//		JavaAST mast = mutatedAST.applyMutations();
//		
//		Collection<MutationOperator> ops2 = Arrays.asList(COD);
//		MutationRequest reqCOD = new MutationRequest("test", "utils.BooleanOps", ops2, methods, false, false, 1);
//		Mutator mutator2 = new Mutator(mast, reqCOD);
//		mutations = mutator2.generateMutations();
//		System.out.println("Mutations for utils.BooleanOps after undoing mutation (or, line 1, a -> !a):\n");
//		for (MutationInformation minfo : mutations) {
//			System.out.println(minfo.toString()+"\n");
//		}
//		
//		MutatedAST coiCodMutatedAST = new MutatedAST(mast, mutations.iterator().next());
//		JavaAST coiCodAST = coiCodMutatedAST.applyMutations();
//		
//		Collection<MutationOperator> ops3 = Arrays.asList(PRVOU_REFINED);
//		MutationRequest reqPRVOU = new MutationRequest("test", "utils.BooleanOps", ops3, methods, false, false, 1);
//		Mutator mutator3 = new Mutator(coiCodAST, reqPRVOU);
//		mutations = mutator3.generateMutations();
//		System.out.println("Mutations for utils.BooleanOps after undoing mutation (or, line 1, !a -> a):\n");
//		for (MutationInformation minfo : mutations) {
//			System.out.println(minfo.toString()+"\n");
//		}
		
		MutantsInformationHolder.usePrototypeChecking(true);
		MutantsInformationHolder.setVerbose(true);
		
		Collection<MutationOperator> ops = Arrays.asList(/*COR, ROR, */COI, COD, AORU, PRVOU_REFINED);
		Collection<String> methods = Arrays.asList("and", "or");
		MutationRequest req = new MutationRequest("test", "utils.BooleanOps", ops, methods, false, false, 3);
		List<MutatedAST> mutants = Api.generateLastGeneration(req);
		for (MutatedAST m : mutants) {
			System.out.println(m.toString());
		}
		Mutator mutator = new Mutator(utilBooleanOpsAST, req);
		Collection<MutationInformation> mutations = mutator.generateMutations();
		System.out.println("Mutations for utils.BooleanOps:\n");
		for (MutationInformation minfo : mutations) {
			System.out.println(minfo.toString()+"\n");
		}
//		//0, 4, 5, 6, 18, 40, 44, 45, 46, 47
//		List<MutationInformation> mutationsAsList = new LinkedList<>(mutations);
//		List<MutationInformation> multiMutantMutations = new LinkedList<>();
//		multiMutantMutations.add(mutationsAsList.get(0));
//		multiMutantMutations.add(mutationsAsList.get(4));
//		multiMutantMutations.add(mutationsAsList.get(5));
//		multiMutantMutations.add(mutationsAsList.get(6));
//		multiMutantMutations.add(mutationsAsList.get(18));
//		multiMutantMutations.add(mutationsAsList.get(40));
//		multiMutantMutations.add(mutationsAsList.get(44));
//		multiMutantMutations.add(mutationsAsList.get(45));
//		multiMutantMutations.add(mutationsAsList.get(46));
//		multiMutantMutations.add(mutationsAsList.get(47));
//		MutatedAST mutatedAST = new MutatedAST(utilBooleanOpsAST, multiMutantMutations);
//		mutatedAST.writeInFolder(new File("/home/stein/Desktop/TEST/nonASTalteringMutants/"));
//		mutatedAST.applyMutations().writeInFolder(new File("/home/stein/Desktop/TEST/ASTalteringMutants/"), true);
//		mutatedAST.undoMutations().writeInFolder(new File("/home/stein/Desktop/TEST/undoMutations/"), true);
	}

}
