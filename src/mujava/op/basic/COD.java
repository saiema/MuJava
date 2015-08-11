////////////////////////////////////////////////////////////////////////////
// Module : COD.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>Generate COD (Conditional Operator Deletion) mutants --
 *    delete each occurrence of logical operators (and-&&, or-||, 
 *    and with no conditional evaluation-&, 
 *    or with no conditional evaluation-|, not equivalent-^) 
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class COD extends MethodLevelMutator {

	public COD(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super( file_env, comp_unit );
	}

	public void visit( UnaryExpression p ) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		int op = p.getOperator();
		if ( op == UnaryExpression.NOT) {
			outputToFile(p, p.getExpression());
		}
		super.visit(p.getExpression());
	}
	
	public void visit( BinaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		p.getLeft().accept(this);
		p.getRight().accept(this);
	}

	public void visit( AssignmentExpression p ) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		Expression rexp = p.getRight();
		rexp.accept( this );
	}
	
	public void visit (MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		ExpressionList args = p.getArguments();
		for (int a = 0; a < args.size(); a++) {
			Expression exp = args.get(a);
			exp.accept(this);
		}
	}

	public void visit(ReturnStatement p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		Expression exp = p.getExpression();
		if (exp != null) exp.accept(this);
	}

	public void outputToFile(UnaryExpression original, Expression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(Mutant.COD,original, (ParseTreeObject) mutant);
	}
}
