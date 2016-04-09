////////////////////////////////////////////////////////////////////////////
// Module : LOI.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;


/*
 * MuJava++ NOTICE: THIS COMMENT BELOW IS INCORRECT. 
 * THIS MUTANT ACTUALLY INSERTS BITWISE ~ OPERATOR ONLY.
 * 
 */

/**
 * <p>Generate LOI (Logical Operator Insertion) mutants --
 *    insert bitwise logical operators (bitwise and-&, bitwise or-|,
 *    exclusive or-^)
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class LOI extends Arithmetic_OP {
	private boolean ignoreBinary;

	public LOI(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super( file_env, comp_unit );
		this.ignoreBinary = false;
	}

	public void visit( Variable p) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if (isArithmeticType(p)) {
			Variable originalCopy = (Variable) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.BIT_NOT, originalCopy);
			outputToFile(p, mutant);
		}
	}

	public void visit( FieldAccess p ) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if (isArithmeticType(p)) {
			FieldAccess originalCopy = (FieldAccess) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.BIT_NOT, originalCopy);
			outputToFile(p, mutant);
		}
	}
	
	public void visit(Literal p) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if (isArithmeticType(p)) {
			Literal originalCopy = (Literal) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.BIT_NOT, originalCopy);
			outputToFile(p, mutant);
		}
	}
	
	public void visit(UnaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if (isArithmeticType(p) && !(p.getParent() instanceof Statement)) {
			UnaryExpression originalCopy = (UnaryExpression) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.BIT_NOT, originalCopy);
			outputToFile(p, mutant);
			this.ignoreBinary = true;
		}
		if (p.getExpression() instanceof BinaryExpression) {
			p.getExpression().accept(this);
		}
	}
	
	public void visit(BinaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if (isArithmeticType(p) && !this.ignoreBinary) {
			BinaryExpression originalCopy = (BinaryExpression) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.BIT_NOT, originalCopy);
			outputToFile(p, mutant);
		}
		p.getLeft().accept(this);
		p.getRight().accept(this);
	}
	
	public void visit(AssignmentExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		p.getRight().accept(this);
	}
	
	public void visit(VariableDeclarator p) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if (p.getInitializer() != null) p.getInitializer().accept(this);
	}
	
	public void visit (MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		ExpressionList args = p.getArguments();
		for (int a = 0; a < args.size(); a++) {
			Expression exp = args.get(a);
			exp.accept(this);
		}
	}

	private void outputToFile(BinaryExpression original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;
		
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.LOI, original, mutant);
	}

	private void outputToFile(UnaryExpression original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.LOI, original, mutant);
	}

	private void outputToFile(Literal original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;
		
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.LOI, original, mutant);
	}

	public void outputToFile(FieldAccess original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.LOI, original, mutant);	
	}

	public void outputToFile(Variable original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;
		
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.LOI, original, mutant);
	}
}
