////////////////////////////////////////////////////////////////////////////
// Module : JSI.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import mujava.api.Api;
import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import mujava.app.MutationRequest;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>
 * Generate JSI (Java-specific static modifier insertion) -- add the
 * <i>static</i> modifier to instance variables
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class JSI extends mujava.op.util.Mutator {
	private boolean smartMode;

	public JSI(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
		this.smartMode = false;
	}
	
	public void setSmartMode() {
		this.smartMode = true;
	}
	
	public void setDumbMode() {
		this.smartMode = false;
	}

	public void visit(FieldDeclaration p) throws ParseTreeException {
		if (Api.usingApi() && (!Api.insideClassToMutate() || (Api.getMethodUnderConsideration().compareTo(MutationRequest.MUTATE_FIELDS)!=0))) {
			return;
		}
		if (getMutationsLeft(p) <= 0) return;
		mutate(p);
	}

	private void mutate(FieldDeclaration original) {
		if (!original.getModifiers().contains(ModifierList.STATIC)) {
			FieldDeclaration mutant = (FieldDeclaration) nodeCopyOf(original);
			ModifierList newModifiers = mutant.getModifiers();
			newModifiers.add(ModifierList.STATIC);
			outputToFile(original, mutant);
		}
	}

	private void outputToFile(FieldDeclaration original, FieldDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.JSI, original, mutant);
	}
	
	public void visit(MethodDeclaration p) throws ParseTreeException {
		if (Api.usingApi() && (!Api.insideClassToMutate() || !p.getName().equals(Api.getMethodUnderConsideration()))) {
			return;
		}
		if (getMutationsLeft(p) <= 0) return;
		if (this.smartMode) {
			mutate_smart(p);
		} else {
			mutate_dumb(p);
		}
	}

	private void mutate_dumb(MethodDeclaration original) {
		if (!original.getModifiers().contains(ModifierList.STATIC)) {
			MethodDeclaration mutant = (MethodDeclaration) nodeCopyOf(original);
			ModifierList newModifiers = mutant.getModifiers();
			newModifiers.add(ModifierList.STATIC);
			outputToFile(original, mutant);
		}
	}

	private void mutate_smart(MethodDeclaration original) {
		if ((!original.getModifiers().contains(ModifierList.STATIC)) && willCompile(original)) {
			MethodDeclaration mutant = (MethodDeclaration) nodeCopyOf(original);
			ModifierList newModifiers = mutant.getModifiers();
			newModifiers.add(ModifierList.STATIC);
			outputToFile(original, mutant);
		}
	}
	
	private boolean willCompile(MethodDeclaration p) {
		//TODO: code
		//this will check if the new method declaration will compile
		//adding static to a method that calls non-static methods will
		//result in a compiling error
		return true;
	}
	
	private void outputToFile(MethodDeclaration original,MethodDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.JSI, original, mutant);
	}

}
