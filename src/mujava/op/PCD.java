package mujava.op;

import openjava.mop.FileEnvironment;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.BinaryExpression;
import openjava.ptree.CastExpression;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionList;
import openjava.ptree.MethodCall;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.ReturnStatement;
import openjava.ptree.VariableDeclarator;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.op.util.Mutator;

public class PCD extends Mutator {
	
	public PCD(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}
	
	public void visit(CastExpression cast) throws ParseTreeException {
		if (getMutationsLeft(cast) <= 0) return;
		outputToFile(cast, cast.getExpression());
	}

	private void outputToFile(CastExpression original, Expression mutant) {
		MutantsInformationHolder.mainHolder().addMutation(Mutant.PCD, original, (ParseTreeObject) mutant);
	}
	
	public void visit(MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		ExpressionList args = p.getArguments();
		for (int a = 0; a < args.size(); a++) {
			Expression exp = args.get(a);
			exp.accept(this);
		}
	}
	
	public void visit(BinaryExpression p) throws ParseTreeException {
		Expression lexp = p.getLeft();
		lexp.accept(this);
		Expression rexp = p.getRight();
		rexp.accept(this);
	}
	
	public void visit(ReturnStatement p) throws ParseTreeException {
		Expression exp = p.getExpression();
		exp.accept(this);
	}
	
	public void visit(VariableDeclarator p) throws ParseTreeException {
		Expression	rexp = (Expression) p.getInitializer();
		
		if( rexp == null ){
			super.visit(p);
			return;
		} else {
			rexp.accept(this);
		}
	}
	
	public void visit(AssignmentExpression p) throws ParseTreeException {
		if (getMutationsLeft(p) > 0) {
			Expression lexp = p.getLeft();
			Expression rexp = p.getRight();
			
			super.visit(lexp);
			super.visit(rexp);
		}
	}

}
