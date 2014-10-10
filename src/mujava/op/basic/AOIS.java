////////////////////////////////////////////////////////////////////////////
// Module : AOIS.java
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
import openjava.ptree.FieldAccess;
import openjava.ptree.ParseTreeException;
import openjava.ptree.UnaryExpression;
import openjava.ptree.Variable;

/**
 * <p>Generate AOIS (Arithmetic Operator Insertion (Short-cut)) mutants --
 *    insert unary operators (increment ++, decrement --) before and after
 *    each variable of an arithmetic type  
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class AOIS extends Arithmetic_OP {

	public AOIS(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super( file_env, comp_unit );
	}

	public void visit( UnaryExpression p ) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		if (	isArithmeticType(p) && (
				(p.getExpression() instanceof Variable) || 
				(p.getExpression() instanceof FieldAccess))) {
			Expression original = ((UnaryExpression) p.makeRecursiveCopy_keepOriginalID()).getExpression();
			int op = p.getOperator();
			if (	op == UnaryExpression.POST_DECREMENT
					||
					op == UnaryExpression.POST_INCREMENT
					||
					op == UnaryExpression.PRE_DECREMENT
					||
					op == UnaryExpression.PRE_INCREMENT) {
				return;
			}
			UnaryExpression pmutantPD = new UnaryExpression(UnaryExpression.POST_DECREMENT, original);
			UnaryExpression pmutantPI = new UnaryExpression(UnaryExpression.POST_INCREMENT, original);
			UnaryExpression pmutantPreD = new UnaryExpression(UnaryExpression.PRE_DECREMENT, original);
			UnaryExpression pmutantPreI = new UnaryExpression(UnaryExpression.PRE_INCREMENT, original);
			UnaryExpression mutantPD = new UnaryExpression(op, pmutantPD);
			UnaryExpression mutantPI = new UnaryExpression(op, pmutantPI);
			UnaryExpression mutantPreD = new UnaryExpression(op, pmutantPreD);
			UnaryExpression mutantPreI = new UnaryExpression(op, pmutantPreI);
			outputToFile(p, mutantPD);
			outputToFile(p, mutantPI);
			outputToFile(p, mutantPreD);
			outputToFile(p, mutantPreI);
		} else {
			p.getExpression().accept(this);
		}
	}
	

	public void visit(Variable p) throws ParseTreeException {
		
		if (!(getMutationsLeft(p) > 0)) return;
		if (isArithmeticType(p)) {
			Variable originalCopy = (Variable) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutantPD = new UnaryExpression(UnaryExpression.POST_DECREMENT, originalCopy);
			UnaryExpression mutantPI = new UnaryExpression(UnaryExpression.POST_INCREMENT, originalCopy);
			UnaryExpression mutantPreD = new UnaryExpression(UnaryExpression.PRE_DECREMENT, originalCopy);
			UnaryExpression mutantPreI = new UnaryExpression(UnaryExpression.PRE_INCREMENT, originalCopy);
			outputToFile(p, mutantPD);
			outputToFile(p, mutantPI);
			outputToFile(p, mutantPreD);
			outputToFile(p, mutantPreI);
		}
	}

	public void visit( FieldAccess p ) throws ParseTreeException {

		if (!(getMutationsLeft(p) > 0)) return;
		if (isArithmeticType(p)) {
			FieldAccess originalCopy = (FieldAccess) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutantPD = new UnaryExpression(UnaryExpression.POST_DECREMENT, originalCopy);
			UnaryExpression mutantPI = new UnaryExpression(UnaryExpression.POST_INCREMENT, originalCopy);
			UnaryExpression mutantPreD = new UnaryExpression(UnaryExpression.PRE_DECREMENT, originalCopy);
			UnaryExpression mutantPreI = new UnaryExpression(UnaryExpression.PRE_INCREMENT, originalCopy);
			outputToFile(p, mutantPD);
			outputToFile(p, mutantPI);
			outputToFile(p, mutantPreD);
			outputToFile(p, mutantPreI);
		}
	}

	public void visit( BinaryExpression p ) throws ParseTreeException  {
		if (!(getMutationsLeft(p) > 0)) return;
		Expression e1 = p.getLeft();
		e1.accept(this);
		Expression e2 = p.getRight();
		e2.accept(this);
	}

	public void visit( AssignmentExpression p ) throws ParseTreeException {
		Expression rexp = p.getRight();
		rexp.accept( this );
	}

	
	public void outputToFile(UnaryExpression original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;

		
		MutantsInformationHolder.mainHolder().addMutation(Mutant.AOIS, original, mutant);
		
	}
	
	public void outputToFile(FieldAccess original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;

		
		MutantsInformationHolder.mainHolder().addMutation(Mutant.AOIS, original, mutant);
		
	}

	public void outputToFile(Variable original, UnaryExpression mutant) {

		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(Mutant.AOIS, original, mutant);
	
	}

}
