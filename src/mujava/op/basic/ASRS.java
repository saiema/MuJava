////////////////////////////////////////////////////////////////////////////
// Module : ASRS.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate ASRS (Assignment Operator Replacement (short-cut)) mutants --
 *    replace each occurrence of one of the assignment operators 
 *    (+=, -+, *=, /=, %=, &=, |=, ^=, <<=, >>=, >>>=) by each of the 
 *    other operators  
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

/* Each occurrence of one of the assignment operators
 *    (+=, -+, *=, /=, %=, &=, |=, ^=, <<=, >>=, >>>=),
 *    is replaced by each of the other operators 
 * 
 */

public class ASRS extends MethodLevelMutator
{

	private int mutationsLeft = -1;
	ParseTreeObject parent = null;

	public ASRS(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
	{
		super( file_env, comp_unit );
	}


	/**
	 * If the assignment operator is of arithmetic type (+=, -=, *=, /=, %=), 
	 *    replace it with each of the other arithmetic assignment operators. 
	 * If the assignment operator is of logical type (&=, |=, ^=), 
	 *    replace it with each of the other logical assignment operators.
	 * If the assignment operator is a shift operator (<<, >>, >>>)
	 *    replace it with each of the other shift operators. 
	 */
	public void visit( AssignmentExpression p ) throws ParseTreeException
	{
		if (!(getMutationsLeft(p)>0)) return;
		int op = p.getOperator();
		if ( (op == AssignmentExpression.ADD) || (op == AssignmentExpression.SUB)  ||
				(op == AssignmentExpression.MULT) || (op == AssignmentExpression.DIVIDE) ||
				(op == AssignmentExpression.MOD) )
		{
			genArithmeticMutants(p,op);
		}
		else if ( (op == AssignmentExpression.AND) || (op == AssignmentExpression.OR) ||
				(op == AssignmentExpression.XOR))
		{
			genLogicalMutants(p,op);
		}
		else if ( (op == AssignmentExpression.SHIFT_L) || (op == AssignmentExpression.SHIFT_R) ||
				(op == AssignmentExpression.SHIFT_RR))
		{
			genShiftMutants(p,op);
		}
	}

	/*
	 * Replace the arithmetic assignment operator (+=, -+, *=, /=, %=)
	 * by each of the other operators  
	 */   
	void genArithmeticMutants(AssignmentExpression p, int op)
	{
		AssignmentExpression mutant;
		if (!(op == AssignmentExpression.ADD))
		{
			mutant = (AssignmentExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(AssignmentExpression.ADD);
			outputToFile(p, mutant);
		}
		if (!(op == AssignmentExpression.DIVIDE))
		{
			mutant = (AssignmentExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(AssignmentExpression.DIVIDE);
			outputToFile(p, mutant);
		}
		if (!(op == AssignmentExpression.MULT))
		{
			mutant = (AssignmentExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(AssignmentExpression.MULT);
			outputToFile(p,mutant);
		}  
		if (!(op == AssignmentExpression.SUB))
		{
			mutant = (AssignmentExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(AssignmentExpression.SUB);
			outputToFile(p,mutant);
		}  
		if (!(op == AssignmentExpression.MOD))
		{
			mutant = (AssignmentExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(AssignmentExpression.MOD);
			outputToFile(p,mutant);
		}
	}

	/*
	 * Replace the logical assignment operator (&=, |+, ^=)
	 * by each of the other operators  
	 */   
	void genLogicalMutants(AssignmentExpression p, int op)
	{
		AssignmentExpression mutant;
		if (!(op == AssignmentExpression.AND))
		{
			mutant = (AssignmentExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(AssignmentExpression.AND);
			outputToFile(p, mutant);
		}
		if (!(op == AssignmentExpression.OR))
		{
			mutant = (AssignmentExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(AssignmentExpression.OR);
			outputToFile(p, mutant);
		}
		if (!(op == AssignmentExpression.XOR))
		{
			mutant = (AssignmentExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(AssignmentExpression.XOR);
			outputToFile(p, mutant);
		}
	}

	/*
	 * Replace the shift assignment operator (<<=, >>=, >>>=)
	 * by each of the other operators  
	 */   
	void genShiftMutants(AssignmentExpression p, int op)
	{
		AssignmentExpression mutant;
		if (!(op == AssignmentExpression.SHIFT_L))
		{
			mutant = (AssignmentExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(AssignmentExpression.SHIFT_L);
			outputToFile(p, mutant);
		}
		if (!(op == AssignmentExpression.SHIFT_R))
		{
			mutant = (AssignmentExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(AssignmentExpression.SHIFT_R);
			outputToFile(p, mutant);
		}
		if (!(op == AssignmentExpression.SHIFT_RR))
		{
			mutant = (AssignmentExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(AssignmentExpression.SHIFT_RR);
			outputToFile(p, mutant);
		}
	}

	/**
	 * Output ASRS mutants to file
	 * @param original
	 * @param mutant
	 */
	public void outputToFile(AssignmentExpression original,AssignmentExpression mutant)
	{
		if (comp_unit == null) 
			return;

		mutationsLeft = getMutationsLeft(original);

		if (!(mutationsLeft>0)) return;


		//********** MUJAVA++ MODIFICATION **********//
		//********** date: 7 Feb 2012     **********//
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.ASRS,
				original, mutant);
		return;
		//*******************************************//

	}
}
