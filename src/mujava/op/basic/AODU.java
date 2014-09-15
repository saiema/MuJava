////////////////////////////////////////////////////////////////////////////
// Module : AODU.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import openjava.mop.FileEnvironment;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.BinaryExpression;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.UnaryExpression;

/**
 * <p>Generate AODU (Arithmetic Operator Deletion (Unary)) mutants --
 *    delete a unary operator (arithmetic -) before each variable or 
 *    expression
 * </p> 
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class AODU extends Arithmetic_OP {


	public AODU(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super( file_env, comp_unit );
	}

	public void visit( BinaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		if (isArithmeticType(p))  {
			Expression e1 = p.getLeft();
			//super.visit(e1);
			e1.accept(this);
			Expression e2 = p.getRight();
			//super.visit(e2);
			e2.accept(this);
		}
	}

	public void visit( AssignmentExpression p ) throws ParseTreeException {
		Expression rexp = p.getRight();
		rexp.accept( this );
	}

	public void visit( UnaryExpression p ) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		if (isArithmeticType(p)) {
			int op = p.getOperator();
			if ( (op == UnaryExpression.MINUS) || (op == UnaryExpression.PLUS) ) {
				Expression mutant = ((UnaryExpression) p.makeRecursiveCopy_keepOriginalID()).getExpression();
				outputToFile(p, mutant);
			} 
		}
	}

	public void outputToFile(UnaryExpression original, Expression mutant) {
		if (comp_unit == null) 
			return;
		
		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.AODU, original, (ParseTreeObject) mutant);
	}
}
