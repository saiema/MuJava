package mujava.op;

import java.util.LinkedList;
import java.util.List;

import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJMethod;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.BinaryExpression;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionList;
import openjava.ptree.MethodCall;
import openjava.ptree.MethodDeclaration;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ReturnStatement;
import openjava.ptree.SelfAccess;
import openjava.ptree.StatementList;
import openjava.ptree.VariableDeclarator;
import mujava.api.Api;
import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import mujava.op.util.Mutator;

public class OAN extends Mutator {
	private boolean relaxed;

	public OAN(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super(file_env, comp_unit);
		this.relaxed = false;
	}
	
	public void relaxed() {
		this.relaxed = true;
	}
	
	public void strict() {
		this.relaxed = false;
	}
	
	public void visit(MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		for (ExpressionList mutArgs : getCompatibleOverloadingCalls(p)) {
			MethodCall mutant = (MethodCall) p.makeRecursiveCopy_keepOriginalID();
			mutant.setArguments(mutArgs);
			outputToFile(p, mutant);
		}
		ExpressionList args = p.getArguments();
		for (int a = 0; a < args.size(); a++) {
			Expression exp = args.get(a);
			exp.accept(this);
		}
	}
	
	private void outputToFile(MethodCall original, MethodCall mutant) {
		MutantsInformationHolder.mainHolder().addMutation(this.relaxed?MutationOperator.OAN_RELAXED:MutationOperator.OAN, original, mutant);
	}
	
	
	private List<ExpressionList> getCompatibleOverloadingCalls(MethodCall mc) throws ParseTreeException {
		List<ExpressionList> result = new LinkedList<ExpressionList>();
		OJClass mcType = null;
		OJClass declaringClass = null;
		Expression refExpr = mc.getReferenceExpr();
		if (refExpr == null) {
			//search definition begining in this class
			mcType = lookupType(mc, getSelfType());
			declaringClass = getSelfType();
		} else if (refExpr instanceof SelfAccess  && ((SelfAccess)refExpr).isSuperAccess()) {
			//search definition begining in this class superclass
			mcType = lookupType(mc, getSelfType().getSuperclass());
			declaringClass = getSelfType().getSuperclass();
		} else if (refExpr instanceof SelfAccess) {
			//search definition begining in this class
			mcType = lookupType(mc, getSelfType());
			declaringClass = getSelfType();
		} else {
			//the method is not declared in this class, use getType() instead
			mcType = getType(mc);
			declaringClass = getType(refExpr);
		}
		String mcName = mc.getName();
		ExpressionList args = mc.getArguments();
		OJMethod[] allMethods = getAllMethods(declaringClass);
		for (OJMethod m : allMethods) {
			String methodName = m.getName();
			if (!compareNamesWithoutPackage(mcName, methodName)) {
				continue;
			}
			OJClass methodType = m.getReturnType();
//			if (!compareNamesWithoutPackage(mcType.getName(), methodType.getName())) {
//				continue;
//			}
			if (!sameType(mcType, methodType)) {
				continue;
			}
			List<Integer> compatibleArgs = compatibleArguments(args, m.getParameterTypes());
			if (compatibleArgs != null && compatibleArgs.size() != args.size()) {
				result.add(generateMutantCall(args, compatibleArgs));
			}
		}
		return result;
	}
	
	private ExpressionList generateMutantCall(ExpressionList args, List<Integer> compatibleArgs) {
		ExpressionList result = new ExpressionList();
		for (int a : compatibleArgs) {
			result.add(args.get(a));
		}
		return result;
	}
	
	private List<Integer> compatibleArguments(ExpressionList originalArgs, OJClass[] overloadingMethodParams) throws ParseTreeException {
		List<Integer> argsIndexes = null;
		if (overloadingMethodParams.length == 0) {
			argsIndexes = new LinkedList<Integer>();
		} else {
			for (int a = 0; a < originalArgs.size(); a++) {
				Expression currentArg = originalArgs.get(a);
				OJClass currentArgType = getType(currentArg);
				OJClass ompType = overloadingMethodParams[a];
				if (compatibleTypes(currentArgType, ompType)) {
					if (argsIndexes == null) {
						argsIndexes = new LinkedList<Integer>();
					}
					argsIndexes.add(a);
				} else {
					argsIndexes = null;
					break;
				}
				if (a == overloadingMethodParams.length - 1) {
					break;
				}
			}
		}
		return argsIndexes;
	}
	
	private boolean compatibleTypes(OJClass t1, OJClass t2) {
		if (this.relaxed) {
			return compatibleAssignType(t1, t2);
		} else {
			return t1.getName().compareTo(t2.getName()) == 0;
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
		if (exp != null) exp.accept(this);
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
	
	public void visit(MethodDeclaration md) throws ParseTreeException {
		if (Api.usingApi() && (!Api.insideClassToMutate() || !md.getName().equals(Api.getMethodUnderConsideration()))) {
			return;
		}
		bindMethodParams(md);
		bindLocalVariables(md.getBody());
		StatementList body = md.getBody();
		for (int s = 0; s < body.size(); s++) {
			body.get(s).accept(this);
		}
	}

}
