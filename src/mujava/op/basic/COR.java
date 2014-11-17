////////////////////////////////////////////////////////////////////////////
// Module : COR.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Configuration;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>
 * Generate COR (Conditional Operator Replacement mutants -- replace each
 * logical operator by each of the other operators (and-&&, or-||, and with no
 * conditional evaluation-&, or with no conditional evaluation-|, not
 * equivalent-^)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class COR extends MethodLevelMutator {
	public static final String ALLOW_LOGICAL_AND = "cor_allow_land";
	public static final String ALLOW_LOGICAL_OR = "cor_allow_lor";
	public static final String ALLOW_XOR = "cor_allow_xor";
	public static final String ALLOW_BIT_AND = "cor_allow_band";
	public static final String ALLOW_BIT_OR = "cor_allow_bor";

	public COR(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}

	/**
	 * If the operator is one of logical operators, replace it with each of the
	 * other logical operators
	 */
	public void visit(BinaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression left = p.getLeft();
		left.accept(this);
		Expression right = p.getRight();
		right.accept(this);

		if ((getType(p.getLeft()) == OJSystem.BOOLEAN)
				&& (getType(p.getRight()) == OJSystem.BOOLEAN)) {
			int op_type = p.getOperator();
			if ((op_type == BinaryExpression.LOGICAL_AND)
					|| (op_type == BinaryExpression.LOGICAL_OR)
					|| (op_type == BinaryExpression.BITAND)
					|| (op_type == BinaryExpression.BITOR)
					|| (op_type == BinaryExpression.XOR)) {
				corMutantGen(p, op_type);
			}
		}
	}

	private void corMutantGen(BinaryExpression exp, int op) {
		BinaryExpression mutant;
		if ((op != BinaryExpression.LOGICAL_AND && opAllowed(BinaryExpression.LOGICAL_AND))) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.LOGICAL_AND);
			outputToFile(exp, mutant);
		}

		if ((op != BinaryExpression.LOGICAL_OR && opAllowed(BinaryExpression.LOGICAL_OR))) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.LOGICAL_OR);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.XOR && opAllowed(BinaryExpression.XOR)) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.XOR);
			outputToFile(exp, mutant);
		}

		if ((op != BinaryExpression.BITAND && opAllowed(BinaryExpression.BITAND))) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.BITAND);
			outputToFile(exp, mutant);
		}

		if ((op != BinaryExpression.BITOR && opAllowed(BinaryExpression.BITOR))) {
			mutant = (BinaryExpression) (exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.BITOR);
			outputToFile(exp, mutant);
		}
	}
	
	private boolean opAllowed(int op) {
		switch (op) {
			case BinaryExpression.LOGICAL_AND :{
				if (Configuration.argumentExist(ALLOW_LOGICAL_AND)) {
					return (Boolean) Configuration.getValue(ALLOW_LOGICAL_AND);
				}
				return true;
			}
			case BinaryExpression.LOGICAL_OR : {
				if (Configuration.argumentExist(ALLOW_LOGICAL_OR)) {
					return (Boolean) Configuration.getValue(ALLOW_LOGICAL_OR);
				}
				return true;
			}
			case BinaryExpression.XOR : {
				if (Configuration.argumentExist(ALLOW_XOR)) {
					return (Boolean) Configuration.getValue(ALLOW_XOR);
				}
				return true;
			}
			case BinaryExpression.BITAND : {
				if (Configuration.argumentExist(ALLOW_BIT_AND)) {
					return (Boolean) Configuration.getValue(ALLOW_BIT_AND);
				}
				return true;
			}
			case BinaryExpression.BITOR : {
				if (Configuration.argumentExist(ALLOW_BIT_OR)) {
					return (Boolean) Configuration.getValue(ALLOW_BIT_OR);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Output COR mutants to files
	 * 
	 * @param original
	 * @param mutant
	 */
	public void outputToFile(BinaryExpression original, BinaryExpression mutant) {
		if (comp_unit == null)
			return;

		MutantsInformationHolder.mainHolder().addMutation(Mutant.COR, original,
				mutant);

	}
}
