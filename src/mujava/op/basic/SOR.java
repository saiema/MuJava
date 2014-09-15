////////////////////////////////////////////////////////////////////////////
// Module : SOR.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate SOR (Shift Operator Replacement) mutants --
 *    replace each occurrence of one of the shift operators <<, >>, and >>>
 *    by each of the other operators
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class SOR extends MethodLevelMutator {
	public SOR(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
	{
		super( file_env, comp_unit );
	}

	public void visit( BinaryExpression p ) throws ParseTreeException 
	{
		if (!(getMutationsLeft(p)>0)) return;
		Expression left = p.getLeft();
		left.accept( this );

		int op_type = p.getOperator();
		if ( (op_type == BinaryExpression.SHIFT_L) || (op_type == BinaryExpression.SHIFT_R)
				||(op_type == BinaryExpression.SHIFT_RR) )
		{
			sorMutantGen(p, op_type);
		}

		Expression right = p.getRight();
		right.accept( this );
	}

	private void sorMutantGen(BinaryExpression exp, int op)
	{
		BinaryExpression mutant;

		if (op != BinaryExpression.SHIFT_L)
		{
			mutant = (BinaryExpression)(exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.SHIFT_L);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.SHIFT_R)
		{
			mutant = (BinaryExpression)(exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.SHIFT_R);
			outputToFile(exp, mutant);
		}

		if (op != BinaryExpression.SHIFT_RR)
		{
			mutant = (BinaryExpression)(exp.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(BinaryExpression.SHIFT_RR);
			outputToFile(exp, mutant);
		}
	}

	/**
	 * Output SOR mutants to files
	 * @param original
	 * @param mutant
	 */
	public void outputToFile(BinaryExpression original, BinaryExpression mutant) {
		if (comp_unit == null) 
			return;
		
		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.SOR, original, mutant);
		
	}
}
