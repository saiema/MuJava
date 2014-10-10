////////////////////////////////////////////////////////////////////////////
// Module : COI.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import openjava.mop.FileEnvironment;
import openjava.mop.OJSystem;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.BinaryExpression;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionList;
import openjava.ptree.FieldAccess;
import openjava.ptree.Literal;
import openjava.ptree.MethodCall;
import openjava.ptree.ParseTreeException;
import openjava.ptree.UnaryExpression;
import openjava.ptree.Variable;

/**
 * <p>Generate COI (Conditional Operator Insertion) mutants --
 *    insert logical operators (and-&&, or-||, 
 *    and with no conditional evaluation-&, 
 *    or with no conditional evaluation-|, not equivalent-^)
 * </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class COI extends MethodLevelMutator {
	private boolean ignoreBinary = false;

	public COI(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super( file_env, comp_unit );
		this.ignoreBinary = false;
	}

	public void visit(UnaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if (getType(p) == OJSystem.BOOLEAN) {
			UnaryExpression originalCopy = (UnaryExpression) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.NOT, originalCopy);
			outputToFile(p, mutant);
			this.ignoreBinary = true;
		}
		if (p.getExpression() instanceof BinaryExpression) p.getExpression().accept(this);
	}


	public void visit( Variable p ) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if (getType(p) == OJSystem.BOOLEAN) {
			Variable originalCopy = (Variable) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.NOT, originalCopy);
			outputToFile(p, mutant);
		}
	}

	public void visit(Literal p) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if (getType(p) == OJSystem.BOOLEAN) {
			Literal originalCopy = (Literal) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.NOT, originalCopy);
			outputToFile(p, mutant);
		}
	}

	public void visit( FieldAccess p ) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if (getType(p) == OJSystem.BOOLEAN) {
			FieldAccess originalCopy = (FieldAccess) p.makeRecursiveCopy_keepOriginalID();
			UnaryExpression mutant = new UnaryExpression(UnaryExpression.NOT, originalCopy);
			outputToFile(p, mutant);
		}
	}

	public void visit( BinaryExpression p ) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return; 
		if (getType(p) == OJSystem.BOOLEAN) {
			if (!this.ignoreBinary) {
				BinaryExpression originalCopy = (BinaryExpression) p.makeRecursiveCopy_keepOriginalID();
				UnaryExpression mutant = new UnaryExpression(UnaryExpression.NOT, originalCopy);
				outputToFile(p, mutant);
			}
			p.getLeft().accept(this);
			p.getRight().accept(this);
		}
	}
	
	public void visit( AssignmentExpression p ) throws ParseTreeException {
		if (!(getMutationsLeft(p)>0)) return;
		if (getType(p) == OJSystem.BOOLEAN) {
			p.getRight().accept(this);
		}
	}
	
	public void visit (MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		ExpressionList args = p.getArguments();
		for (int a = 0; a < args.size(); a++) {
			Expression exp = args.get(a);
			exp.accept(this);
		}
	}

	private void outputToFile(UnaryExpression original, UnaryExpression mutant) {
		if (comp_unit == null) return;
		MutantsInformationHolder.mainHolder().addMutation(Mutant.COI, original, mutant);
	}
	
	private void outputToFile(Variable original, UnaryExpression mutant) {
		if (comp_unit == null) return;
		MutantsInformationHolder.mainHolder().addMutation(Mutant.COI, original, mutant);
	}
	
	private void outputToFile(Literal original, UnaryExpression mutant) {
		if (comp_unit == null) return;
		MutantsInformationHolder.mainHolder().addMutation(Mutant.COI, original, mutant);
	}
	
	private void outputToFile(FieldAccess original, UnaryExpression mutant) {
		if (comp_unit == null) return;
		MutantsInformationHolder.mainHolder().addMutation(Mutant.COI, original, mutant);
	}
	
	private void outputToFile(BinaryExpression original, UnaryExpression mutant) {
		if (comp_unit == null) return;
		MutantsInformationHolder.mainHolder().addMutation(Mutant.COI, original, mutant);
	}
	
}
