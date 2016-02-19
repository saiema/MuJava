////////////////////////////////////////////////////////////////////////////
// Module : AORU.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

// not use ??? (11/15/2009) 

/**
 * <p>Generate AORU (Arithmetic Operator Replacement (Unary)) mutants --
 *    replace each occurrence of one of the arithmetic operators + and - 
 *    by each of the other operators 
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class AORU extends MethodLevelMutator
{

	private int mutationsLeft = -1;
	ParseTreeObject parent = null;

	public AORU(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
	{
		super( file_env, comp_unit );
	}

	/**
	 * If a given unary expression contains an arithmetic operator + or -,
	 * generate an AORU mutant
	 */
	public void visit( UnaryExpression p ) throws ParseTreeException
	{
		if (!(getMutationsLeft(p) > 0)) return;
		int op = p.getOperator();
		if ( (op == UnaryExpression.MINUS) || (op == UnaryExpression.PLUS) ) 
		{
			genBasicUnaryMutants(p,op);
		}
		super.visit(p.getExpression());
	}

	void genBasicUnaryMutants(UnaryExpression p, int op)
	{
		UnaryExpression mutant;
		if ( op == UnaryExpression.PLUS )
		{
			mutant = (UnaryExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(UnaryExpression.MINUS);
			outputToFile(p, mutant);
		}
		else if ( op == UnaryExpression.MINUS )
		{
			mutant = (UnaryExpression)(p.makeRecursiveCopy_keepOriginalID());
			mutant.setOperator(UnaryExpression.PLUS);
			outputToFile(p, mutant);
		}
	}

	/**
	 * Output AORU mutants to files
	 * @param original
	 * @param mutant
	 */
	public void outputToFile(UnaryExpression original, UnaryExpression mutant)
	{
		if (comp_unit == null) 
			return;

		mutationsLeft = getMutationsLeft(original);

		if (!(mutationsLeft>0)) return;


		//********** MUJAVA++ MODIFICATION **********//
		//********** date: 6 Dic 2011     **********//
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.AORU,
				original, mutant);
		return;
		//*******************************************//

	}
}
