////////////////////////////////////////////////////////////////////////////
// Module : AOIU.java
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
import openjava.ptree.ExpressionList;
import openjava.ptree.FieldAccess;
import openjava.ptree.MethodCall;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ReturnStatement;
import openjava.ptree.UnaryExpression;
import openjava.ptree.Variable;

/**
 * <p>Generate AOIU (Arithmetic Operator Insertion (Unary)) mutants --
 *    insert a unary operator (arithmetic -) before each variable or 
 *    expression      
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class AOIU extends Arithmetic_OP {

	public AOIU(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super( file_env, comp_unit );
	}

	public void visit( UnaryExpression p ) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		if (isArithmeticType(p)) {
			UnaryExpression originalCopy = (UnaryExpression) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.MINUS, originalCopy);
			outputToFile(p, mutant);
		}
	}

	public void visit( Variable p) throws ParseTreeException {	   
		if (!(getMutationsLeft(p) > 0)) return;
		if (isArithmeticType(p)) {
			Variable originalCopy = (Variable) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.MINUS, originalCopy);
			outputToFile(p, mutant);
		}
	}

	public void visit( FieldAccess p ) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		if (isArithmeticType(p)) {
			FieldAccess originalCopy = (FieldAccess) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.MINUS, originalCopy);
			outputToFile(p, mutant);
		}
	}

	public void visit( BinaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		if (isArithmeticType(p)) {
			p.getLeft().accept(this);
			p.getRight().accept(this);
			BinaryExpression originalCopy = (BinaryExpression) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.MINUS, originalCopy);
			outputToFile(p, mutant);
		}
	}

	public void visit( AssignmentExpression p ) throws ParseTreeException {
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
		Expression exp = p.getExpression();
		exp.accept(this);
	}
	
	public void outputToFile(BinaryExpression original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(Mutant.AOIU, original, mutant);
	}
	
	public void outputToFile(UnaryExpression original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(Mutant.AOIU, original, mutant);
	}
	
	
	public void outputToFile(FieldAccess original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(Mutant.AOIU, original, mutant);
	}

	public void outputToFile(Variable original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(Mutant.AOIU, original, mutant);
	}


}
