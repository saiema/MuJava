////////////////////////////////////////////////////////////////////////////
// Module : COR.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate COR (Conditional Operator Replacement mutants --
 *    replace each logical operator by each of the other operators 
 *    (and-&&, or-||, and with no conditional evaluation-&, 
 *    or with no conditional evaluation-|, not equivalent-^)    
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class COR extends MethodLevelMutator
{


	public COR(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
	{
		super( file_env, comp_unit );
	}

	/**
	 * If the operator is one of logical operators, replace it with
	 * each of the other logical operators 
	 */
	public void visit( BinaryExpression p ) throws ParseTreeException 
	{
		if (!(getMutationsLeft(p)>0)) return;
		Expression left = p.getLeft();
		left.accept(this);
		Expression right = p.getRight();
		right.accept(this);

		if ( (getType(p.getLeft()) == OJSystem.BOOLEAN) && 
				(getType(p.getRight()) == OJSystem.BOOLEAN))
		{
			int op_type = p.getOperator();
			if ( (op_type == BinaryExpression.LOGICAL_AND) ||
					(op_type == BinaryExpression.LOGICAL_OR) ||
					(op_type == BinaryExpression.BITAND) ||
					(op_type == BinaryExpression.BITOR) ||
					(op_type == BinaryExpression.XOR))
			{
				corMutantGen(p, op_type);
			}
		}
	}

	private void corMutantGen(BinaryExpression exp, int op)
	{
		BinaryExpression mutant;
		if ((op != BinaryExpression.LOGICAL_AND))
		{
			mutant = (BinaryExpression)(exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.LOGICAL_AND);
			outputToFile(exp, mutant);
		}

		if ((op != BinaryExpression.LOGICAL_OR))
		{
			mutant = (BinaryExpression)(exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.LOGICAL_OR);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.XOR)
		{
			mutant = (BinaryExpression)(exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.XOR);
			outputToFile(exp, mutant);
		}

		if ((op != BinaryExpression.BITAND))
		{
			mutant = (BinaryExpression)(exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.BITAND);
			outputToFile(exp, mutant);
		}

		if ((op != BinaryExpression.BITOR))
		{
			mutant = (BinaryExpression)(exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.BITOR);
			outputToFile(exp, mutant);
		}
	}

	/**
	 * Output COR mutants to files
	 * @param original
	 * @param mutant
	 */
	 public void outputToFile(BinaryExpression original, BinaryExpression mutant)
	{
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.COR, original, mutant);

	}
}
