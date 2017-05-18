////////////////////////////////////////////////////////////////////////////
// Module : JTI.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>
 * Generate JTI (Java-specific this keyword insertion) -- insert the keyword
 * <i>this</i> to instance variables or method parameters
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class JTI extends mujava.op.util.Mutator {
	private boolean smartMode;

	public JTI(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
		this.smartMode = false;
	}

	public void smartMode() {
		this.smartMode = true;
	}
	
	public void dumbMode() {
		this.smartMode = false;
	}

	public void visit(Variable p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression mutant = this.smartMode?insertThis_smart(p):insertThis_dumb(p);
		if (mutant != null) {
			outputToFile(p, mutant);
		}
	}
	

	public void visit(MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		ExpressionList args = p.getArguments();
		for (int a = 0; a < args.size(); a++) {
			Expression exp = args.get(a);
			exp.accept(this);
		}
	}
	
	private Expression insertThis_dumb(Variable var) throws ParseTreeException {
		SelfAccess sa = SelfAccess.makeThis();
		FieldAccess fa = new FieldAccess(sa, var.toString());
		return fa;
	}
	
	private Expression insertThis_smart(Variable var) throws ParseTreeException {
		int options = TARGET_IS_NULL;
		options += ALLOW_PRIVATE;
		options += ALLOW_NON_STATIC;
		options += ALLOW_PROTECTED_INHERITED;
		OJField[] allFields = getAllFields(getSelfType(), options);
		for (OJField f : allFields) {
			String nameVar = var.toString();
			String fieldName = f.getName();
			if (compareNamesWithoutPackage(nameVar, fieldName)) {
				OJClass varType = getType(var);
				OJClass fieldType = f.getType();
				if (varType.getName().equals(fieldType.getName())) {
					SelfAccess sa = SelfAccess.makeThis();
					FieldAccess fa = new FieldAccess(sa, f.getName());
					return fa;
				}
			}
		}
		return null;
	}

	private void outputToFile(Variable original, Expression mutant) {
		MutantsInformationHolder.mainHolder().addMutation(this.smartMode?MutationOperator.JTI_SMART:MutationOperator.JTI, original, (ParseTreeObject) mutant);
	}
	
	public void visit(BinaryExpression p) throws ParseTreeException {
		Expression lexp = p.getLeft();
		lexp.accept(this);
		Expression rexp = p.getRight();
		rexp.accept(this);
	}
	
	public void visit(ReturnStatement p) throws ParseTreeException {
		Expression exp = p.getExpression();
		if (exp != null) exp.accept(this);
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
	
	public void visit(AssignmentExpression p) throws ParseTreeException {
		if (getMutationsLeft(p) > 0) {
			Expression lexp = p.getLeft();
			Expression rexp = p.getRight();
			
			super.visit(lexp);
			super.visit(rexp);
		}
	}
}
