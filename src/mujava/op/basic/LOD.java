////////////////////////////////////////////////////////////////////////////
// Module : LOD.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/*
 * MuJava++ NOTICE: THIS COMMENT BELOW IS INCORRECT. 
 * THIS MUTANT ACTUALLY DELETES BITWISE ~ OPERATOR ONLY.
 * 
 */

/**
 * <p>Generate LOD (Logical Operator Deletion) mutants --
 *    delete each occurrence of bitwise logical operators 
 *    (bitwise and-&, bitwise or-|, exclusive or-^)
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class LOD extends MethodLevelMutator {

	public LOD(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super( file_env, comp_unit );
	}

	public void visit( UnaryExpression p ) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		int op = p.getOperator();
		if ( op == UnaryExpression.BIT_NOT) {
			UnaryExpression originalCopy = (UnaryExpression) p.makeRecursiveCopy_keepOriginalID();
			Expression mutant = originalCopy.getExpression();
			outputToFile(p, mutant);
		}
		if (p.getExpression() instanceof UnaryExpression || p.getExpression() instanceof BinaryExpression) super.visit(p.getExpression()); 
	}
	
	public void visit( BinaryExpression p ) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return; 
		p.getLeft().accept(this);
		p.getRight().accept(this);
	}
	
	public void visit (MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		ExpressionList args = p.getArguments();
		for (int a = 0; a < args.size(); a++) {
			Expression exp = args.get(a);
			exp.accept(this);
		}
	}

	public void outputToFile(UnaryExpression original, Expression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.LOD, original, (ParseTreeObject) mutant);
	}
}
