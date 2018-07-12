////////////////////////////////////////////////////////////////////////////
// Module : AODS.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.MutationOperator;
import mujava.api.Configuration;
import mujava.api.MutantsInformationHolder;
import openjava.mop.FileEnvironment;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionStatement;
import openjava.ptree.ForStatement;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.StatementList;
import openjava.ptree.UnaryExpression;

/**
 * <p>Generate AODS (Arithmetic Operator Deletion (Short-cut)) mutants --
 *    delete each occurrence of an increment operator (++) or a decrement 
 *    operator (--)  
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class AODS extends MethodLevelMutator {
	
	/**
	 * Option to enable/disable the mutation of expression statements, e.g.: <pre>a++;</pre>
	 * <p>
	 * this option is disabled by default
	 */
	public static final String AODS_AVOID_EXPRESSION_STATEMENTS = "aods_dont_mutate_expression_statement";


	public AODS(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super( file_env, comp_unit );
	}

	
	public void visit( UnaryExpression p ) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		if (ignoreExpressionStatements() && (p.getParent() instanceof ExpressionStatement)) return;
		int op = p.getOperator();
		if ( 	(op == UnaryExpression.POST_DECREMENT) || (op == UnaryExpression.POST_INCREMENT)
			|| 	(op == UnaryExpression.PRE_DECREMENT) || (op == UnaryExpression.PRE_INCREMENT) ) {
			Expression mutant = ((UnaryExpression) p.makeRecursiveCopy_keepOriginalID()).getExpression();
			outputToFile(p, mutant);
		} 
	}

	public void visit( ForStatement p ) throws ParseTreeException {
		// Do not consider conditions for "FOR STMT"
		StatementList stmts = p.getStatements();
		super.visit(stmts);
	}
	
	private boolean ignoreExpressionStatements() {
		if (Configuration.argumentExist(AODS_AVOID_EXPRESSION_STATEMENTS)) {
			return (Boolean) Configuration.getValue(AODS_AVOID_EXPRESSION_STATEMENTS);
		} else {
			return false;
		}
	}

	/**
	 * Write AODS mutants to files
	 * @param original
	 */
	public void outputToFile(UnaryExpression original, Expression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.AODS, original, (ParseTreeObject) mutant);

	}
	
	
}
