package mujava.op;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.BinaryExpression;
import openjava.ptree.CastExpression;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionList;
import openjava.ptree.MethodCall;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ReturnStatement;
import openjava.ptree.TypeName;
import openjava.ptree.VariableDeclarator;
import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import mujava.op.util.Mutator;

public class PCC extends Mutator {
	
	public PCC(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}
	
	public void visit(CastExpression cast) throws ParseTreeException {
		if (getMutationsLeft(cast) <= 0) return;
		OJClass castType = getType(cast.getTypeSpecifier());
		OJClass superType = castType.getSuperclass();
		Class<?> castTypeAsClass;
		List<String> generics = new LinkedList<String>();
		try {
			castTypeAsClass = Class.forName(castType.getName());
			Type superClassType = castTypeAsClass.getGenericSuperclass();
			if (superClassType instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) superClassType;
				Type[] genericsTypes = parameterizedType.getActualTypeArguments();
				for (Type t : genericsTypes) {
					generics.add(getClassName(t.toString()));
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		generateMutants(cast, superType, generics);
	}
	
	private String getClassName(String input) {
		if (input.startsWith("class")) {
			input = input.replace("class", "");
			input = input.trim();
		}
		return input;
	}

	private void generateMutants(CastExpression original, OJClass superType, List<String> generics) {
		CastExpression mutant = (CastExpression) nodeCopyOf(original);
		TypeName newCast = TypeName.forOJClass(superType);
		String genericsString = "";
		int i = 0;
		for (String g : generics) {
			genericsString += g;
			i++;
			if (i < generics.size() - 1) {
				genericsString += ", ";
			}
		}
		if (!genericsString.isEmpty()) newCast.setGenerics("<"+genericsString+">");
		mutant.setTypeSpecifier(newCast);
		outputToFile(original, mutant);
	}

	private void outputToFile(CastExpression original, CastExpression mutant) {
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.PCC, original, mutant);
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
		if (p != null) exp.accept(this);
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
