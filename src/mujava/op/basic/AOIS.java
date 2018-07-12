////////////////////////////////////////////////////////////////////////////
// Module : AOIS.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.MutationOperator;

import java.util.List;
import java.util.Map;

import mujava.api.Configuration;
import mujava.api.MutantsInformationHolder;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJField;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.BinaryExpression;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.FieldAccess;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.ReturnStatement;
import openjava.ptree.Statement;
import openjava.ptree.UnaryExpression;
import openjava.ptree.Variable;
import mujava.api.Mutation.PRIORITY;

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
			//if (isFinal(p.getExpression())) return;
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
		//if (isArithmeticType(p) && !isFinal(p)) {
		if (isArithmeticType(p)) {
			Variable originalCopy = (Variable) nodeCopyOf(p); //p.makeRecursiveCopy_keepOriginalID();
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
		//if (isArithmeticType(p) && !isFinal(p)) {
		if (isArithmeticType(p)) {
			FieldAccess originalCopy = (FieldAccess) nodeCopyOf(p); //p.makeRecursiveCopy_keepOriginalID();
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

		
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.AOIS, original, mutant, evaluateMutation(original, mutant));
		
	}
	
	public void outputToFile(FieldAccess original, UnaryExpression mutant) {
		if (comp_unit == null) 
			return;

		
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.AOIS, original, mutant, evaluateMutation(original, mutant));
		
	}

	public void outputToFile(Variable original, UnaryExpression mutant) {

		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.AOIS, original, mutant, evaluateMutation(original, mutant));
	
	}
	
	private PRIORITY evaluateMutation(Expression e, UnaryExpression mut) {
		if (priorityEvaluation()) {
			if (mut.isPostfix() && isReturn(e) && isLocalVariable(e)) {
				return PRIORITY.NEUTRAL;
			}
		}
		return PRIORITY.NORMAL;
	}
	
	private boolean isReturn(Expression e) {
		Statement st = (Statement) getStatement((ParseTreeObject) e);
		if (st instanceof ReturnStatement) {
			return true;
		}
		return false;
	}
	
	private boolean isLocalVariable(Expression e) {
		if (e instanceof Variable) {
			Variable v = (Variable) e;
			try {
				OJClass vType = getType(v);
				Map<OJClass, List<Variable>> localVars = getReachableVariables((ParseTreeObject) e);
				if (localVars.containsKey(vType)) {
					for (Variable lv : localVars.get(vType)) {
						if (v.toFlattenString().compareTo(lv.toFlattenString()) == 0) {
							return true;
						}
					}
				} else {
					return false;
				}
			} catch (ParseTreeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return false;
	}
	
	private boolean isFinal(Expression e) {
		if (e instanceof Variable) {
			Variable v = (Variable) e;
			try {
				OJClass vType = getType(v);
				Map<OJClass, List<Variable>> localVars = getReachableFinalVariables((ParseTreeObject) e);
				if (localVars.containsKey(vType)) {
					for (Variable lv : localVars.get(vType)) {
						if (v.toFlattenString().compareTo(lv.toFlattenString()) == 0) {
							return true;
						}
					}
				} else {
					return false;
				}
			} catch (ParseTreeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e instanceof FieldAccess) {
			FieldAccess fa = (FieldAccess) e;
			try {
				OJClass fType = getType(fa);
				OJField[] finalFields = getAllFields(fType, ALLOW_NON_STATIC + ALLOW_PROTECTED_INHERITED + ALLOW_STATIC + ONLY_FINAL);
				for (OJField f : finalFields) {
					String faName = fa.getName();
					String fName = f.getName();
					if (faName.compareTo(fName) != 0) continue;
					OJClass faClass = fa.getReferenceExpr() == null?getSelfType():getType(fa.getReferenceExpr());
					OJClass fClass = f.getDeclaringClass();
					if (faClass.isAssignableFrom(fClass)) {
						return true;
					}
				}
			} catch (ParseTreeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return false;
	}
	
	private boolean priorityEvaluation() {
		if (Configuration.argumentExist(Configuration.PRIORITY_EVALUATE)) {
			return (Boolean) Configuration.getValue(Configuration.PRIORITY_EVALUATE);
		}
		return false;
	}

}
