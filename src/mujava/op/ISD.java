package mujava.op;

import java.util.List;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJField;
import openjava.mop.OJMethod;
import openjava.ptree.BinaryExpression;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionList;
import openjava.ptree.FieldAccess;
import openjava.ptree.Literal;
import openjava.ptree.MethodCall;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.ReturnStatement;
import openjava.ptree.SelfAccess;
import openjava.ptree.Variable;
import openjava.ptree.VariableDeclarator;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.op.util.Mutator;

public class ISD extends Mutator {
	private boolean smartMode;
	
	public ISD(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super(file_env, comp_unit);
		this.smartMode = false;
	}
	
	public void smartMode() {
		this.smartMode = true;
	}
	
	public void dumbMode() {
		this.smartMode = false;
	}
	

	public void visit(FieldAccess p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression mutant = this.smartMode?deleteSuper(p):deleteSuper_dumb(p);
		if (mutant != null) {
			outputToFile(p, mutant);
		}
	}

	public void visit(MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression mutant = this.smartMode?deleteSuper(p):deleteSuper_dumb(p);
		if (mutant != null) {
			outputToFile(p, mutant);
		}
		ExpressionList args = p.getArguments();
		for (int a = 0; a < args.size(); a++) {
			Expression exp = args.get(a);
			exp.accept(this);
		}
	}
	
	private Expression deleteSuper_dumb(Expression p) throws ParseTreeException {
		Expression copy = (Expression) p.makeRecursiveCopy_keepOriginalID();
		Expression prev;
		Expression current = copy;
		prev = getPreviousExpression(current);
		while (prev != null) {
			if (prev instanceof SelfAccess && ((SelfAccess)prev).isSuperAccess()) {
				break;
			} else if (prev instanceof SelfAccess) {
				return null;
			}
			current = prev;
			prev = getPreviousExpression(current);
		}
		if (prev != null) {
			if (current instanceof FieldAccess) {
				((FieldAccess) current).setReferenceExpr(null);
				return copy;
			} else if (current instanceof MethodCall) {
				((MethodCall) current).setReferenceExpr(null);
				return copy;
			}
		}
		return null;
	}

	private Expression deleteSuper(Expression exp) throws ParseTreeException {
		Expression copy = (Expression) exp.makeRecursiveCopy_keepOriginalID();
		Expression prev;
		Expression current = copy;
		prev = getPreviousExpression(current);
		while (prev != null) {
			if (prev instanceof SelfAccess && ((SelfAccess)prev).isSuperAccess()) {
				break;
			} else if (prev instanceof SelfAccess) {
				return null;
			}
			current = prev;
			prev = getPreviousExpression(current);
		}
		if (prev == null) {
			return null;
		}
		OJClass currentType = getType(current);
		String currentName;
		if (current instanceof FieldAccess) {
			currentName = ((FieldAccess) current).getName();
			List<Variable> reachableVars = getReachableVariables((ParseTreeObject) current).get(currentType);
			for (Variable var : reachableVars) {
				String varName = var.toString();
				if (compareNamesWithoutPackage(currentName, varName)) {
					((FieldAccess) current).setReferenceExpr(null);
					return copy;
				}
			}
			OJField[] declaredFields = getSelfType().getDeclaredFields();
			for (OJField f : declaredFields) {
				String fieldName = f.getName();
				if (compareNamesWithoutPackage(currentName, fieldName)) {
					OJClass fieldType = f.getType();
					if (currentType.getName().equals(fieldType.getName())) {
						prev.replace(SelfAccess.makeThis());
						return copy;
					}
				}
			}
		} else if (current instanceof MethodCall) {
			currentName = ((MethodCall) current).getName();
			OJMethod[] declaredMethods = getSelfType().getDeclaredMethods();
			for (OJMethod m : declaredMethods) {
				if (compatibleSignatures((MethodCall) current, m)) {
					((MethodCall) current).setReferenceExpr(null);
					return copy;
				}
			}
		} else {
			// it should never reach this
		}
		return null;
	}

	private boolean compatibleSignatures(MethodCall methodCall, OJMethod m) throws ParseTreeException {
		String currentName = methodCall.getName();
		String methodName = m.getName();
		if (compareNamesWithoutPackage(currentName, methodName)) {
			OJClass methodType = m.getReturnType();
			OJClass methodCallType = getType(methodCall);
			if (methodCallType.getName().equals(methodType.getName())) {
				OJClass[] formalArguments = m.getParameterTypes();
				ExpressionList actualArguments = methodCall.getArguments();
				if (formalArguments.length == actualArguments.size()) {
					for (int a = 0; a < formalArguments.length; a++) {
						if (!compatibleAssignType(formalArguments[a], getType(actualArguments.get(a))))
							return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	private Expression getPreviousExpression(Expression e) {
		if (e instanceof MethodCall) {
			return ((MethodCall) e).getReferenceExpr();
		} else if (e instanceof FieldAccess) {
			return ((FieldAccess) e).getReferenceExpr();
		} else if (e instanceof Variable) {
			return null;
		} else if (e instanceof Literal) {
			return null;
		} else {
			// should never reach this point
			// throw an excepcion maybe
			return null;
		}
	}
	

	private void outputToFile(FieldAccess original, Expression mutant) {
		MutantsInformationHolder.mainHolder().addMutation(this.smartMode?Mutant.ISD_SMART:Mutant.ISD, original, (ParseTreeObject) mutant);
	}

	private void outputToFile(MethodCall original, Expression mutant) {
		MutantsInformationHolder.mainHolder().addMutation(this.smartMode?Mutant.ISD_SMART:Mutant.ISD, original, (ParseTreeObject) mutant);
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

}
