////////////////////////////////////////////////////////////////////////////
// Module : ROR.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Configuration;
import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import mujava.op.util.Mutator;
import openjava.mop.FileEnvironment;
import openjava.ptree.BinaryExpression;
import openjava.ptree.BreakStatement;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.DoWhileStatement;
import openjava.ptree.Expression;
import openjava.ptree.ForStatement;
import openjava.ptree.IfStatement;
import openjava.ptree.Literal;
import openjava.ptree.ParseTreeException;
import openjava.ptree.Statement;
import openjava.ptree.StatementList;
import openjava.ptree.WhileStatement;

/**
 * <p>
 * Generate ROR (Rational Operator Replacement) mutants -- replace each
 * occurrence of one of the relational operators (<, <=, >, >=, =, <>) by each
 * of the other operators and by <i>falseOp</i> and <i>trueOp</i> where
 * <i>falseOp</i> always returns <i>false</i> and <i>trueOp</i> always returns
 * <i>true</i>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class ROR extends Arithmetic_OP {

	/**
	 * Option to enable/disable the replacement of boolean expressions with true
	 * <p>
	 * this option is enabled by default
	 */
	public static final String REPLACE_WITH_TRUE = "ror_replace_with_true";
	/**
	 * Option to enable/disable the replacement of boolean expressions with false
	 * <p>
	 * this option is enabled by default
	 */
	public static final String REPLACE_WITH_FALSE = "ror_replace_with_false";
	/**
	 * Option to enable/disable avoiding replacing with true or false in instances when
	 * the generated mutants will not compile
	 * <p>
	 * this option is disabled by default
	 */
	public static final String SMART_LITERAL_REPLACE = "ror_smart_literal_replace";
	
	private static enum Literals {NONE, TRUE, FALSE, BOTH};

	public ROR(FileEnvironment file_env, ClassDeclaration cdecl,
			CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}

	public void visit(BinaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression left = p.getLeft();
		left.accept(this);
		Expression right = p.getRight();
		right.accept(this);

		int op_type = p.getOperator();
		if (isArithmeticType(p.getLeft()) && isArithmeticType(p.getRight())) {
			if ((op_type == BinaryExpression.GREATER)
					|| (op_type == BinaryExpression.GREATEREQUAL)
					||
					// ********** MUJAVA++ MODIFICATION **********//
					// ********** date: 6 Dic 2011 **********//
					(op_type == BinaryExpression.LESS)
					||
					// *******************************************//
					(op_type == BinaryExpression.LESSEQUAL)
					|| (op_type == BinaryExpression.EQUAL)
					|| (op_type == BinaryExpression.NOTEQUAL)) {
				primitiveRORMutantGen(p, op_type);
			}
		} else if ((op_type == BinaryExpression.EQUAL)
				|| (op_type == BinaryExpression.NOTEQUAL)) {
			objectRORMutantGen(p, op_type);
		}
	}

	private void primitiveRORMutantGen(BinaryExpression exp, int op) {
		BinaryExpression mutant;
		if (op != BinaryExpression.GREATER) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.GREATER);
			Mutator.setParentOf(mutant, exp, true);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.GREATEREQUAL) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.GREATEREQUAL);
			Mutator.setParentOf(mutant, exp, true);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.LESS) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.LESS);
			Mutator.setParentOf(mutant, exp, true);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.LESSEQUAL) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.LESSEQUAL);
			Mutator.setParentOf(mutant, exp, true);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.EQUAL) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.EQUAL);
			Mutator.setParentOf(mutant, exp, true);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.NOTEQUAL) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.NOTEQUAL);
			Mutator.setParentOf(mutant, exp, true);
			outputToFile(exp, mutant);
		}
		Literals allowed = Literals.BOTH;
		if (allowReplacementWithFalse() || allowReplacementWithTrue()) {
			allowed = smartValidation(exp);
		}
		if (allowReplacementWithFalse() && (allowed.equals(Literals.FALSE) || allowed.equals(Literals.BOTH))) outputToFile(exp, Literal.constantFalse());
		if (allowReplacementWithTrue() && (allowed.equals(Literals.TRUE) || allowed.equals(Literals.BOTH))) outputToFile(exp, Literal.constantTrue());
	}

	private void objectRORMutantGen(BinaryExpression exp, int op) {
		BinaryExpression mutant;
		if (op != BinaryExpression.EQUAL) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.EQUAL);
			Mutator.setParentOf(mutant, exp, true);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.NOTEQUAL) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.NOTEQUAL);
			Mutator.setParentOf(mutant, exp, true);
			outputToFile(exp, mutant);
		}
		Literals allowed = Literals.BOTH;
		if (allowReplacementWithFalse() || allowReplacementWithTrue()) {
			allowed = smartValidation(exp);
		}
		if (allowReplacementWithFalse() && (allowed.equals(Literals.FALSE) || allowed.equals(Literals.BOTH))) outputToFile(exp, Literal.constantFalse());
		if (allowReplacementWithTrue() && (allowed.equals(Literals.TRUE) || allowed.equals(Literals.BOTH))) outputToFile(exp, Literal.constantTrue());
	}
	
	private boolean allowReplacementWithTrue() {
		if (Configuration.argumentExist(REPLACE_WITH_TRUE)) {
			return (Boolean) Configuration.getValue(REPLACE_WITH_TRUE);
		} else {
			return true;
		}
	}
	
	private boolean allowReplacementWithFalse() {
		if (Configuration.argumentExist(REPLACE_WITH_FALSE)) {
			return (Boolean) Configuration.getValue(REPLACE_WITH_FALSE);
		} else {
			return true;
		}
	}
	
	private boolean smartLiteralReplacement() {
		if (Configuration.argumentExist(SMART_LITERAL_REPLACE)) {
			return (Boolean) Configuration.getValue(SMART_LITERAL_REPLACE);
		} else {
			return false;
		}
	}
	
	private Literals smartValidation(BinaryExpression e) {
		if (!smartLiteralReplacement()) return Literals.BOTH;
		Statement from = (Statement) getStatement(e);
		if (from instanceof WhileStatement) {
			WhileStatement wst = (WhileStatement) from;
			Expression cond = wst.getExpression();
			if (e == cond) {
				if (hasBreakStatement(wst.getStatements())) return Literals.TRUE;
				else {
					return Literals.NONE;
				}
			}
		} else if (from instanceof ForStatement) {
			ForStatement fst = (ForStatement) from;
			Expression cond = fst.getCondition();
			if (e == cond) {
				if (hasBreakStatement(fst.getStatements())) return Literals.TRUE;
				else {
					return Literals.NONE;
				}
			}
		} else if (from instanceof DoWhileStatement) {
			DoWhileStatement dwst = (DoWhileStatement) from;
			Expression cond = dwst.getExpression();
			if (e == cond) {
				if (hasBreakStatement(dwst.getStatements())) return Literals.BOTH;
				else {
					return Literals.FALSE;
				}
			}
		}
		return Literals.BOTH;
	}
	
	private boolean hasBreakStatement(StatementList stList) {
		for (int s = 0; s < stList.size(); s++) {
			Statement st = stList.get(s);
			if (st instanceof BreakStatement) return true;
			if (st instanceof IfStatement) {
				IfStatement ifStatement = (IfStatement) st;
				if (hasBreakStatement(ifStatement.getStatements()) || hasBreakStatement(ifStatement.getElseStatements())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Output ROR mutants to files
	 * 
	 * @param original
	 * @param mutant
	 */
	private void outputToFile(BinaryExpression original, BinaryExpression mutant) {

		if (comp_unit == null)
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.ROR, original,
				mutant);

	}

	private void outputToFile(BinaryExpression original, Literal mutant) {
		if (comp_unit == null)
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.ROR, original,
				mutant);
	}

}
