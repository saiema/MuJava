////////////////////////////////////////////////////////////////////////////
// Module : EOA.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>
 * Generate EOA (Java-specific reference assignment and content assignment
 * replacement) mutants -- replace an assignment of a pointer reference with a
 * copy of the object, using the Java convention of a <i>clone()</i> method
 * </p>
 * <p>
 * <i>Example</i>: List list1, list2; list1 = new List(); list2 = list1; is
 * mutated to <br />
 * List list1, list2; list1 = new List(); list2 = list1.clone();
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class EOA extends mujava.op.util.Mutator {
	
	private static final int DUMB = 0;
	private static final int NORMAL = 1;
	private static final int STRICT = 2;
	private int mode = NORMAL;
	
	public EOA(FileEnvironment file_env, ClassDeclaration cdecl,
			CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}
	
	public void strictMode() {
		this.mode = STRICT;
	}
	
	public void normalMode() {
		this.mode = NORMAL;
	}
	
	public void dumbMode() {
		this.mode = DUMB;
	}
	
	public boolean isInStrictMode() {
		return this.mode == STRICT;
	}
	
	public boolean isInNormalMode() {
		return this.mode == NORMAL;
	}
	
	public boolean isInDumbMode() {
		return this.mode == DUMB;
	}

	public void visit(MethodCall p) throws ParseTreeException {
		if ((p.getName().equals("clone")) && (p.getArguments().isEmpty())) {
			// do nothing
		} else {
			super.visit(p);
		}
	}

	public void visit(AssignmentExpression p) throws ParseTreeException {

		if (!(getMutationsLeft(p) > 0)) return;

		Expression left = p.getLeft();
		Expression right = p.getRight();

		boolean isApplicable = !getType(left).isPrimitive()
				&& !getType(right).isPrimitive() && !(right instanceof Literal);
		if (isApplicable) {
			OJClass leftType = getType(left);
			OJClass rightType = getType(right);
			OJClass cloneType = cloneMethodReturnType(rightType);
			if (cloneType != null) {
				OJClass cast = getCast(leftType, cloneType);
				if (!validateClone(leftType, cloneType)) return;
				Expression originalRight = (Expression) right.makeRecursiveCopy_keepOriginalID();
				MethodCall mutatedRight = new MethodCall(originalRight, "clone", null);
				Expression mutatedRightCasted;
				if (cast != null) {
					mutatedRightCasted = new CastExpression(cast, mutatedRight);
				} else {
					mutatedRightCasted = (Expression) mutatedRight;
				}
				AssignmentExpression mutant = (AssignmentExpression)p.makeRecursiveCopy();
				mutant.setRight(mutatedRightCasted);
				outputToFile(p, mutant);
			}
		}
		super.visit(p.getRight());
	}

	public void visit(VariableDeclarator p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;

		Expression right = (Expression) p.getInitializer();
		Variable left = new Variable(p.getVariable());

		boolean isApplicable = !getType(right).isPrimitive()
				&& !(right instanceof Literal);

		if (isApplicable) {
			OJClass leftType = getType(left);
			OJClass rightType = getType(right);
			OJClass cloneType = cloneMethodReturnType(rightType);
			if (cloneType != null) {
				OJClass cast = getCast(leftType, cloneType);
				if (!validateClone(leftType, cloneType)) return;
				Expression originalRight = (Expression) right.makeRecursiveCopy_keepOriginalID();
				MethodCall mutatedRight = new MethodCall(originalRight, "clone", null);
				Expression mutatedRightCasted;
				if (cast != null) {
					mutatedRightCasted = new CastExpression(cast, mutatedRight);
				} else {
					mutatedRightCasted = (Expression) mutatedRight;
				}
				VariableDeclarator mutant = (VariableDeclarator)p.makeRecursiveCopy();
				mutant.setInitializer(mutatedRightCasted);
				outputToFile(p, mutant);
			}
		}
		super.visit(p.getInitializer());
	}

	private boolean validateClone(OJClass leftType, OJClass cloneType) {
		if (isInStrictMode()) {
			return leftType.getName().compareTo(cloneType.getName()) == 0;
		} else {
			return true;
		}
	}

	private OJClass getCast(OJClass t1, OJClass t2) {
		if (isInDumbMode() || isInStrictMode()) return null;
		if (t1.getName().compareTo(t2.getName()) == 0) {
			return null;
		} else {
			return t1;
		}
	}

	private OJClass cloneMethodReturnType(OJClass clazz) {
		OJMethod[] declared = clazz.getDeclaredMethods();
		OJMethod[] inherited = getInheritedMethods(clazz);
		for (int i = 0; i < declared.length; i++) {
			if (declared[i].getName().equals("clone")
					&& declared[i].getParameterTypes().length == 0
					&& declared[i].getModifiers().isPublic()) {
				return declared[i].getReturnType();
			}
		}
		for (int i = 0; i < inherited.length; i++) {
			if (inherited[i].getName().equals("clone")
					&& inherited[i].getParameterTypes().length == 0
					&& ((isInDumbMode()) || !inherited[i].getDeclaringClass().getName().equals("java.lang.Object"))
					&& inherited[i].getModifiers().isPublic()) {
				return inherited[i].getReturnType();
			}
		}
		return null;
	}
	

	private void outputToFile(AssignmentExpression original, AssignmentExpression mutant) {
		Mutant eoa_variant = isInNormalMode()?Mutant.EOA:(isInStrictMode()?Mutant.EOA_STRICT:Mutant.EOA_DUMB);
		MutantsInformationHolder.mainHolder().addMutation(eoa_variant, original, mutant);
	}
	
	private void outputToFile(VariableDeclarator original, VariableDeclarator mutant) {
		Mutant eoa_variant = isInNormalMode()?Mutant.EOA:(isInStrictMode()?Mutant.EOA_STRICT:Mutant.EOA_DUMB);
		MutantsInformationHolder.mainHolder().addMutation(eoa_variant, original, mutant);
	}
}
