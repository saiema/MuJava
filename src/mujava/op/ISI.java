package mujava.op;

import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;

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

/**
 * <p>
 * Generate ISI (Super keyword insertion) mutants, insert the <i>super</i>
 * keyword
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */
public class ISI extends mujava.op.util.Mutator {
	
	private boolean smartMode;

	public ISI(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super(file_env, comp_unit);
		dumbMode();
	}
	
	public void smartMode() {
		this.smartMode = true;
	}
	
	public void dumbMode() {
		this.smartMode = false;
	}

	public void visit(Variable p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression mutant = this.smartMode?insertSuper_smart(p):insertSuper_dumb(p);
		if (mutant != null) {
			outputToFile(p, mutant);
		}
	}

	public void visit(FieldAccess p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression mutant = this.smartMode?insertSuper_smart(p):insertSuper_dumb(p);
		if (mutant != null) {
			outputToFile(p, mutant);
		}
	}

	public void visit(MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression mutant = this.smartMode?insertSuper_smart(p):insertSuper_dumb(p);
		if (mutant != null) {
			outputToFile(p, mutant);
		}
		ExpressionList args = p.getArguments();
		for (int a = 0; a < args.size(); a++) {
			Expression exp = args.get(a);
			exp.accept(this);
		}
	}
	
	private Expression insertSuper_dumb(Variable var) throws ParseTreeException {
		SelfAccess sa = SelfAccess.makeSuper();
		FieldAccess fa = new FieldAccess(sa, var.toString());
		return fa;
	}
	
	private Expression insertSuper_dumb(Expression exp) throws ParseTreeException {
		Expression copy = (Expression) exp.makeRecursiveCopy_keepOriginalID();
		Expression prev;
		Expression current = copy;
		prev = getPreviousExpression(current);
		while (prev != null) {
			if (prev instanceof SelfAccess && ((SelfAccess)prev).isSuperAccess()) {
				return null;
			} else if (prev instanceof SelfAccess) {
				break;
			}
			current = prev;
			prev = getPreviousExpression(current);
		}
		if (current instanceof Variable) {
			SelfAccess sa = SelfAccess.makeSuper();
			FieldAccess fa = new FieldAccess(sa, ((Variable) current).toString());
			current.replace(fa);
			return copy;
		} else if (current instanceof FieldAccess) {
			((FieldAccess) current).setReferenceExpr(SelfAccess.makeSuper());
			return copy;
		} else if (current instanceof MethodCall) {
			((MethodCall) current).setReferenceExpr(SelfAccess.makeSuper());
			return copy;
		}
		return null;
	}

	private Expression insertSuper_smart(Variable var) throws ParseTreeException {
		OJField[] inheritedFields = getInheritedFields(getSelfType());
		for (OJField f : inheritedFields) {
			String nameVar = var.toString();
			String fieldName = f.getName();
			if (compareNamesWithoutPackage(nameVar, fieldName)) {
				OJClass varType = getType(var);
				OJClass fieldType = f.getType();
				if (varType.getName().equals(fieldType.getName())) {
					SelfAccess sa = SelfAccess.makeSuper();
					FieldAccess fa = new FieldAccess(sa, f.getName());
					return fa;
				}
			}
		}
		return null;
	}

	private Expression insertSuper_smart(Expression field) throws ParseTreeException {
		Expression copy = (Expression) field.makeRecursiveCopy_keepOriginalID();
		Expression prev;
		Expression current = copy;
		prev = getPreviousExpression(current);
		while (prev != null) {
			if (prev instanceof SelfAccess && ((SelfAccess)prev).isSuperAccess()) {
				return null;
			} else if (prev instanceof SelfAccess) {
				break;
			}
			current = prev;
			prev = getPreviousExpression(current);
		}
		OJClass currentType = getType(current);
		if (current instanceof Variable) {
			OJField[] inheritedFields = getInheritedFields(getSelfType());
			for (OJField f : inheritedFields) {
				String currentName = ((Variable) current).toString();
				String fieldName = f.getName();
				if (compareNamesWithoutPackage(currentName, fieldName)) {
					OJClass fieldType = f.getType();
					if (currentType.getName().equals(fieldType.getName())) {
						SelfAccess sa = SelfAccess.makeSuper();
						FieldAccess fa = new FieldAccess(sa, f.getName());
						current.replace(fa);
						return copy;
					}
				}
			}
		} else if (current instanceof FieldAccess) {
			OJField[] inheritedFields = getInheritedFields(getSelfType());
			for (OJField f : inheritedFields) {
				String currentName = ((FieldAccess) current).getName();
				String fieldName = f.getName();
				if (compareNamesWithoutPackage(currentName, fieldName)) {
					OJClass fieldType = f.getType();
					if (currentType.getName().equals(fieldType.getName())) {
						((FieldAccess) current).setReferenceExpr(SelfAccess.makeSuper());
						return copy;
					}
				}
			}
		} else if (current instanceof MethodCall) {
			OJMethod[] inheritedMethods = getInheritedMethods(getSelfType());
			for (OJMethod m : inheritedMethods) {
				if (compatibleSignatures((MethodCall) current, m)) {
					((MethodCall) current).setReferenceExpr(SelfAccess.makeSuper());
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


	private void outputToFile(Variable original, Expression mutant) {
		MutantsInformationHolder.mainHolder().addMutantIdentifier(this.smartMode?Mutant.ISI_SMART:Mutant.ISI, original, (ParseTreeObject) mutant);
	}

	private void outputToFile(FieldAccess original, Expression mutant) {
		MutantsInformationHolder.mainHolder().addMutantIdentifier(this.smartMode?Mutant.ISI_SMART:Mutant.ISI, original, (ParseTreeObject) mutant);
	}

	private void outputToFile(MethodCall original, Expression mutant) {
		MutantsInformationHolder.mainHolder().addMutantIdentifier(this.smartMode?Mutant.ISI_SMART:Mutant.ISI, original, (ParseTreeObject) mutant);
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
