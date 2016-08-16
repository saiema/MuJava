////////////////////////////////////////////////////////////////////////////
// Module : EOC.java
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
 * Generate EOC (Java-specific reference comparison and content assignment
 * replacement) mutants -- check whether the two references point to the same
 * data object in memory, using the Java convention of an <i>equals()</i> method
 * </p>
 * <p>
 * <i>Example</i>: boolean b = (f1 == f2); is mutated to boolean b =
 * (f1.equals(f2));
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class EOC extends mujava.op.util.Mutator {
	
	private static final int NORMAL = 0;
	private static final int SMART = 1;
	private static final int RELAXED = 2;
	private static final int STRICT = 3;
	
	private int mode = NORMAL;
	private int smartMode = RELAXED;
	
	public EOC(FileEnvironment file_env, ClassDeclaration cdecl,
			CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}
	
	public void normalMode() {
		this.mode = NORMAL;
		this.smartMode = -1;
	}
	
	public void smartMode(boolean strict) {
		this.mode = SMART;
		this.smartMode = strict?STRICT:RELAXED;
	}
	
	private boolean isInNormalMode() {
		return this.mode == NORMAL;
	}
	
	private boolean isInSmartMode() {
		return this.mode == SMART;
	}
	
	private boolean isInRelaxedSmartMode() {
		return this.smartMode == RELAXED;
	}
	
	private boolean isInStrictSmartMode() {
		return this.smartMode == STRICT;
	}
	
	private OJMethod findEquals(OJClass c, OJClass paramType) {
		OJMethod[] methods = c.getAllMethods();
		for (OJMethod m : methods) {
//			if (m.getName().compareTo("equals")==0 && m.getParameterTypes().length == 1) {
//				return m;
//			}
			if (isValidEqualMethod(m, c, paramType)) {
				return m;
			}
		}
		return null;
	}
	
	private boolean isValidEqualMethod(OJMethod m, OJClass callerType, OJClass paramType) {
		if (isInNormalMode()) {
			return m.getName().compareTo("equals")==0 && m.getParameterTypes().length == 1;
		} else {
			boolean validName = m.getName().compareTo("equals")==0;
			boolean validParamsNumber = m.getParameterTypes().length == 1;
			boolean validResultType = m.getReturnType().isPrimitive() && m.getReturnType().getName().compareTo("boolean") == 0;
			boolean validParamType = true;
			if (isInRelaxedSmartMode()) {
				validParamType = compatibleAssignType(m.getParameterTypes()[0], paramType);
			} else if (isInStrictSmartMode()) {
				validParamType = m.getParameterTypes()[0].getName().compareTo(paramType.getName()) == 0;
			}
			return validName && validParamsNumber && validResultType && validParamType;
		}
	}
	
	public void visit( MethodCall p ) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if ( (p.getName().equals("equals"))) {
	         // do nothing
		} else {
			ExpressionList args = p.getArguments();
			for (int a = 0; a < args.size(); a++) {
				Expression exp = args.get(a);
				exp.accept(this);
			}
		}
	}

	public void visit(BinaryExpression p) throws ParseTreeException {

		if (!(getMutationsLeft(p)>0)) return;
		
		if ((p.getOperator() == BinaryExpression.EQUAL)) {
			Expression left = p.getLeft();
			Expression right = p.getRight();
			boolean mutationIsApplicable = !(left instanceof Literal);
			if (mutationIsApplicable) {
				OJMethod equals = findEquals(getType(left), getType(right));
				if (equals != null && ((isInNormalMode())||(isInSmartMode() && isNotNull(right)))) {
					if (compatibleAssignType(equals.getParameterTypes()[0], getType(right))) {
						BinaryExpression origCopy = (BinaryExpression) nodeCopyOf(p);
						Expression leftCopy = origCopy.getLeft();
						Expression rightCopy = origCopy.getRight();
						ExpressionList args = new ExpressionList();
						args.add(rightCopy);
						MethodCall mutant = new MethodCall(leftCopy, "equals", args);
						mutant.setParent(p.getParent());//origCopy.replace(mutant);
						outputToFile(p, mutant);
					}
				}
			}
		} else {
			if (p.getLeft() instanceof BinaryExpression) {
				visit(p.getLeft());
			}
			if (p.getRight() instanceof BinaryExpression) {
				visit(p.getRight());
			}
		}
	}

	
	/**
	 * This will try to check if an expression is not null or
	 * in the case that the expression is a variable or a field this will try to check if it holds a non-null value
	 */
	private boolean isNotNull(Expression expr) {
		//use PRVO#fixStupidVariable method on expr if it's a Variable
		if (expr instanceof Literal) {
			return ((Literal)expr).getLiteralType() != Literal.NULL;
		} else if (expr instanceof Variable || expr instanceof FieldAccess) {
			Expression initialization = lookupInitialization(expr);
			if (initialization == null) return false;
			if (initialization instanceof Literal && ((Literal)initialization).getLiteralType() == Literal.NULL) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	private Expression lookupInitialization(Expression expr) {
		Expression current = (Expression) expr.makeRecursiveCopy();
		current = (Expression) ((ParseTreeObject)current).getParent();
		Statement statement = null;
		boolean stop = current == null;
		boolean exprIsVar = (expr instanceof Variable);
		while (!stop) {
			if (statement == null && current instanceof Statement) {
				statement = (Statement) current.makeCopy_keepOriginalID();
			}
			if (current instanceof MethodDeclaration) {
				//if expr is a Variable return null
				//else if expr is a Field check class fields declarations
				if (expr instanceof FieldAccess) {
					//get field declarations and check initializers
					//if the field is not declared in this class then return expr
				}
			} else if (current instanceof StatementList) {
				int s = 0;
				Statement currentStmnt = ((StatementList)current).get(s);
				while (currentStmnt.getObjectID() != statement.getObjectID() && s < ((StatementList)current).size()) {
					if (current instanceof VariableDeclaration) {
						String var = ((VariableDeclaration)current).getVariable();
						if (exprIsVar && ((Variable)expr).toString().compareTo(var)==0) {
							return (Expression) ((VariableDeclaration)current).getInitializer();
						}
					} else if (current instanceof VariableDeclarator) {
						String var = ((VariableDeclarator)current).getVariable();
						if (exprIsVar && ((Variable)expr).toString().compareTo(var)==0) {
							return (Expression) ((VariableDeclarator)current).getInitializer();
						}
					} else if (current instanceof AssignmentExpression) { 
						Expression leftSide = ((AssignmentExpression)current).getLeft();
						//check if leftSide is Variable or FieldAccess (use PRVO#fixStupidVariable)
						//if the leftSide is the same as expr then return the right side of the assignment
					}
					s++;
					currentStmnt = ((StatementList)current).get(s);
				}
			}
		}
		return null;
	}

	private void outputToFile(BinaryExpression original, MethodCall mutant) {
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.EOC, original, mutant);
	}
	
}
