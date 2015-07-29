////////////////////////////////////////////////////////////////////////////
// Module : JTD.java
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
 * Generate JTD (Java-specific this keyword deletion) -- delete each occurrence
 * of the keyword <i>this</i>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class JTD extends mujava.op.util.Mutator {

	public JTD(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}

	public void visit(FieldAccess p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression mutant = mutate(p);
		if (mutant != null) {
			outputToFile(p, mutant);
		}
	}

	public void visit (MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression mutant = mutate(p);
		if (mutant != null) {
			outputToFile(p, mutant);
		}
		ExpressionList args = p.getArguments();
		for (int a = 0; a < args.size(); a++) {
			Expression exp = args.get(a);
			exp.accept(this);
		}
	}
	
	private Expression mutate(Expression p) throws ParseTreeException {
		Expression copy = (Expression) p.makeRecursiveCopy_keepOriginalID();
		Expression prev;
		Expression current = copy;
		prev = getPreviousExpression(current);
		while (prev != null) {
			if (prev instanceof SelfAccess && !((SelfAccess)prev).isSuperAccess()) {
				break;
			} else if (prev instanceof SelfAccess) {
				return null;
			}
			current = prev;
			prev = getPreviousExpression(current);
		}
		if (prev != null) {
			if (current instanceof FieldAccess) {
				((FieldAccess) current).setReferenceExpr(null);
				return copy;
			} else if (current instanceof MethodCall) {
				((MethodCall) current).setReferenceExpr(null);
				return copy;
			}
		}
		return null;
	}
	
	private void outputToFile(FieldAccess original, Expression mutant) {
		MutantsInformationHolder.mainHolder().addMutation(Mutant.JTD, original, (ParseTreeObject) mutant);
	}

	private void outputToFile(MethodCall original, Expression mutant) {
		MutantsInformationHolder.mainHolder().addMutation(Mutant.JTD, original, (ParseTreeObject) mutant);
	}
	
	public void visit(BinaryExpression p) throws ParseTreeException {
		Expression lexp = p.getLeft();
		lexp.accept(this);
		Expression rexp = p.getRight();
		rexp.accept(this);
	}
	
	public void visit(ReturnStatement p) throws ParseTreeException {
		Expression exp = p.getExpression();
		exp.accept(this);
	}
	
	public void visit(VariableDeclarator p) throws ParseTreeException {
		Expression	rexp = (Expression) p.getInitializer();
		
		if( rexp == null ){
			super.visit(p);
			return;
		} else {
			rexp.accept(this);
		}
	}
	
}
