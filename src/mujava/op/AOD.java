package mujava.op;

import mujava.api.MutantsInformationHolder;
import mujava.api.MutationOperator;
import mujava.op.basic.Arithmetic_OP;
import openjava.mop.FileEnvironment;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.BinaryExpression;

public class AOD extends Arithmetic_OP {
	
	public AOD(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super( file_env, comp_unit );
	}
	
	public void visit(BinaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		boolean left = p.getLeft() != null;
		boolean right = p.getRight() != null;
		boolean isArith = isArithmeticType(p);
		
		if (isArith && left) {
			Expression leftCopy = (Expression) p.getLeft().makeRecursiveCopy_keepOriginalID();
			outputToFile(p, leftCopy);
		}
		
		if (left) super.visit(p.getLeft());
		
		
		if (isArith && right) {
			Expression rightCopy = (Expression) p.getRight().makeRecursiveCopy_keepOriginalID();
			outputToFile(p, rightCopy);
		}
		
		if (right) super.visit(p.getRight());
	}
	
	public void outputToFile(BinaryExpression original, Expression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.AOD, original, (ParseTreeObject) mutant);

	}
	

}
