////////////////////////////////////////////////////////////////////////////
// Module : LOR.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate LOR (Logical Operator Replacement) mutants --
 *    replace each occurrence of each bitwise logical operator 
 *    (bitwise and-& ,bitwise or-|, exclusive or-^) by each of 
 *    the other operators
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class LOR extends MethodLevelMutator {


	public LOR(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
	{
		super( file_env, comp_unit );
	}


	public void visit( BinaryExpression p ) throws ParseTreeException 
	{
		if (!(getMutationsLeft(p)>0)) return;
		Expression left = p.getLeft();
		left.accept(this);
		Expression right = p.getRight();
		right.accept(this);

		if ( (getType(p.getLeft()) != OJSystem.BOOLEAN) && 
				(getType(p.getRight()) != OJSystem.BOOLEAN))
		{
			int op_type = p.getOperator();

			if ( (op_type == BinaryExpression.BITAND) || (op_type == BinaryExpression.BITOR)
					||(op_type == BinaryExpression.XOR))
			{
				corMutantGen(p, op_type);
			}
		}
	}

	private void corMutantGen(BinaryExpression exp, int op)
	{
		BinaryExpression mutant;

		if (op != BinaryExpression.BITAND)
		{
			mutant = (BinaryExpression)(exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.BITAND);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.BITOR)
		{
			mutant = (BinaryExpression)(exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.BITOR);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.XOR)
		{
			mutant = (BinaryExpression)(exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.XOR);
			outputToFile(exp, mutant);
		}
	}

	/**
	 * Output LOR mutants to files
	 * @param original
	 * @param mutant
	 */
	public void outputToFile(BinaryExpression original, BinaryExpression mutant) {
		if (comp_unit == null) 
			return;
		
		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.LOR, original, mutant);
	}
}
