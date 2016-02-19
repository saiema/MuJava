////////////////////////////////////////////////////////////////////////////
// Module : JSD.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import openjava.mop.*;
import openjava.ptree.*;
import mujava.api.Api;
import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import mujava.app.MutationRequest;

/**
 * <p>
 * Generate JSD (Java-specific static modifier deletion) -- remove each instance
 * of the <i>static</i> modifier
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class JSD extends mujava.op.util.Mutator {
	private boolean smartMode;

	public JSD(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
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
		if (this.smartMode) {
			mutate_smart(p);
		} else {
			mutate_dumb(p);
		}
	}

	private void mutate_dumb(FieldDeclaration original) {
		if (original.getModifiers().contains(ModifierList.STATIC)) {
			FieldDeclaration mutant = (FieldDeclaration) original.makeRecursiveCopy_keepOriginalID();
			ModifierList newModifiers = mutant.getModifiers();
			int mod = newModifiers.getRegular();
			mod &= (~ModifierList.STATIC);
			newModifiers.setRegular(mod);
			outputToFile(original, mutant);
		}
	}
	
	private void mutate_smart(FieldDeclaration original) {
		if (original.getModifiers().contains(ModifierList.STATIC) && willCompile(original)) {
			FieldDeclaration mutant = (FieldDeclaration) original.makeRecursiveCopy_keepOriginalID();
			ModifierList newModifiers = mutant.getModifiers();
			int mod = newModifiers.getRegular();
			mod &= (~ModifierList.STATIC);
			newModifiers.setRegular(mod);
			outputToFile(original, mutant);
		}
	}
	
	private boolean willCompile(FieldDeclaration p) {
		//TODO: code
		//This methods check if the field is used as CLASS.field anywhere in the code
		//if the previous is true then this method will return false
		//otherwise it will return true
		return true;
	}

	private void outputToFile(FieldDeclaration original, FieldDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.JSD, original, mutant);
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
		if (original.getModifiers().contains(ModifierList.STATIC)) {
			MethodDeclaration mutant = (MethodDeclaration) original.makeRecursiveCopy_keepOriginalID();
			ModifierList newModifiers = mutant.getModifiers();
			int mod = newModifiers.getRegular();
			mod &= (~ModifierList.STATIC);
			newModifiers.setRegular(mod);
			outputToFile(original, mutant);
		}
	}
	
	private void mutate_smart(MethodDeclaration original) {
		if (original.getModifiers().contains(ModifierList.STATIC) && willCompile(original)) {
			MethodDeclaration mutant = (MethodDeclaration) original.makeRecursiveCopy_keepOriginalID();
			ModifierList newModifiers = mutant.getModifiers();
			int mod = newModifiers.getRegular();
			mod &= (~ModifierList.STATIC);
			newModifiers.setRegular(mod);
			outputToFile(original, mutant);
		}
	}
	
	private boolean willCompile(MethodDeclaration p) {
		//TODO: code
		//This methods check if the method is used as CLASS.method anywhere in the code
		//if the previous is true then this method will return false
		//otherwise it will return true
		return true;
	}

	private void outputToFile(MethodDeclaration original, MethodDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.JSD, original, mutant);
	}

	
}
