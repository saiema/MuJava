////////////////////////////////////////////////////////////////////////////
// Module : AMC.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

//import java.io.*;

import mujava.api.Api;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.app.MutationRequest;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>
 * Generate AMC (Access modifier change) mutants -- change the access level for
 * instance variables and methods to other access levels. The purpose is to
 * guide testers to generate test cases that ensure that accessibility is
 * correct.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class AMC extends mujava.op.util.Mutator {

	public AMC(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}

	private void changeModifier(ModifierList original, int del_mod,
			int insert_mod) {
		ModifierList mutant;
		mutant = new ModifierList(ModifierList.EMPTY);
		mutant = (ModifierList) original.makeRecursiveCopy_keepOriginalID();

		int mod = mutant.getRegular();

		if (del_mod >= 0) {
			mod &= (~del_mod);
			mutant.setRegular(mod);
		}
		if (insert_mod >= 0) {
			mutant.add(insert_mod);
		}
		outputToFile(original, mutant);
		mutant = null;
	}

	/**
	 * Generate AMC mutants by altering the access level for each instance
	 * variable and method to other access levels (PRIVATE, PROTECTED, PUBLIC,
	 * PACKAGE or default)
	 * 
	 * @param mod
	 */
	public void genMutants(ModifierList mod) {
		if (mod.contains(ModifierList.PRIVATE)) {
			changeModifier(mod, ModifierList.PRIVATE, -1);
			changeModifier(mod, ModifierList.PRIVATE, ModifierList.PROTECTED);
			changeModifier(mod, ModifierList.PRIVATE, ModifierList.PUBLIC);

		} else if (mod.contains(ModifierList.PROTECTED)) {
			changeModifier(mod, ModifierList.PROTECTED, -1);
			changeModifier(mod, ModifierList.PROTECTED, ModifierList.PRIVATE);
			changeModifier(mod, ModifierList.PROTECTED, ModifierList.PUBLIC);
		} else if (mod.contains(ModifierList.PUBLIC)) {
			changeModifier(mod, ModifierList.PUBLIC, -1);
			changeModifier(mod, ModifierList.PUBLIC, ModifierList.PRIVATE);
			changeModifier(mod, ModifierList.PUBLIC, ModifierList.PROTECTED);
		} else { // Friendly
			changeModifier(mod, -1, ModifierList.PRIVATE);
			changeModifier(mod, -1, ModifierList.PROTECTED);
			changeModifier(mod, -1, ModifierList.PUBLIC);
		}
	}

	public void visit(MethodDeclaration p) throws ParseTreeException {
		if (Api.usingApi() && !p.getName().equals(Api.getMethodUnderConsideration())) {
			return;
		}
		if (getMutationsLeft(p) <= 0) return;
		genMutants(p.getModifiers());
	}

	public void visit(FieldDeclaration p) throws ParseTreeException {
		if (Api.usingApi() && (Api.getMethodUnderConsideration().compareTo(MutationRequest.MUTATE_FIELDS)!=0)) {
			return;
		}
		if (getMutationsLeft(p) <= 0) return;
		genMutants(p.getModifiers());
	}

	/**
	 * Output AMC mutants to files
	 * 
	 * @param original
	 * @param mutant
	 */
	public void outputToFile(ModifierList original, ModifierList mutant) {
		MutantsInformationHolder.mainHolder().addMutation(Mutant.AMC, original, mutant);
	}
}
