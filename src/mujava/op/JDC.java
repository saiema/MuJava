package mujava.op;

import mujava.api.Api;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.app.MutationRequest;
import openjava.mop.FileEnvironment;
import openjava.mop.OJConstructor;
import openjava.mop.OJModifier;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.ConstructorDeclaration;
import openjava.ptree.ModifierList;
import openjava.ptree.ParseTreeException;

/**
 * <p>Generate JDC (Java-supported default constructor creation) --
 *    delete each declaration of default constructor (with no 
 *    parameter)
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */ 
public class JDC extends mujava.op.util.Mutator {

	public JDC(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}


	@Override
	public ClassDeclaration evaluateUp(ClassDeclaration ptree)
			throws ParseTreeException {
		return ptree;
	}
	
	public void visit(ClassDeclaration cs) throws ParseTreeException {
		super.visit(cs);

		boolean enterInnerClass = false;
		if (Api.usingApi()) {
			enterInnerClass = Api.enterInnerClass(cs.getName());
		}
		
		if (Api.usingApi() && (!Api.insideClassToMutate() || (Api.getMethodUnderConsideration().compareTo(MutationRequest.MUTATE_CLASS) != 0))) {
			if (Api.usingApi()) {
				Api.leaveInnerClass(cs.getName(), enterInnerClass);
			}
			return;
		}
		if (!(getMutationsLeft(cs) > 0)) {
			if (Api.usingApi()) {
				Api.leaveInnerClass(cs.getName(), enterInnerClass);
			}
			return;
		}

		OJConstructor[] constructors = getSelfType().getDeclaredConstructors();
		for (int i = 0; i < constructors.length; i++) {
			boolean found = false;
			if (constructors[i].getParameterTypes().length == 0) {
				found = true;
			}
			if (found) {

				try {
					ModifierList modlist = new ModifierList();
					OJModifier modif = constructors[i].getModifiers();
					String name = constructors[i].getName();
					modlist.add(modif.toModifier());
					ConstructorDeclaration mutant = new ConstructorDeclaration(modlist,name, null, null, null, null);
					outputToFile(cs, mutant);
				} catch (Exception ex) {
					if (Api.usingApi()) {
						Api.leaveInnerClass(cs.getName(), enterInnerClass);
					}
					System.err.println("[Exception]  " + ex);
				}

			}
			if (found) break;
		}

	}


	private void outputToFile(ClassDeclaration original, ConstructorDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutation(Mutant.JDC,original, mutant);
	}
	
}
