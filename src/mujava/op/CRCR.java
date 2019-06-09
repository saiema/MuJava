package mujava.op;

import java.util.LinkedList;
import java.util.List;

import mujava.api.MutantsInformationHolder;
import mujava.api.MutationOperator;
import mujava.op.basic.Arithmetic_OP;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.UnaryExpression;
import openjava.ptree.Literal;
import openjava.ptree.BinaryExpression;

public class CRCR extends Arithmetic_OP {
	
	public CRCR(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super( file_env, comp_unit );
	}
	
	public void visit(Literal p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		if (!isArithmeticType(p)) return;
		
		for (Expression m : getMutations(p)) {
			outputToFile(p, m);
		}
		
	}
	
	public void visit(UnaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		if (!isArithmeticType(p)) return;
		
		Expression asocExpr = p.getExpression();
		
		if (!(asocExpr instanceof Literal)) super.visit(p.getExpression());
		else if (p.getOperator() == UnaryExpression.PLUS) super.visit(p.getExpression());
		else {
			for (Expression m : getMutations(p)) {
				outputToFile(p, m);
			}
		}
	}
	
	private List<Expression> getMutations(UnaryExpression from) throws ParseTreeException {
		List<Expression> expressions = new LinkedList<>();
		OJClass exprType = getType(from);
		boolean isNeg = from.getOperator() == UnaryExpression.MINUS;
		boolean isPlus = from.getOperator() == UnaryExpression.PLUS;
		boolean isZero = isZero(from.getExpression());
		boolean isOne = isOne(from.getExpression());
		UnaryExpression originalCopy = (UnaryExpression) from.makeRecursiveCopy_keepOriginalID();
		if (compatibleAssignTypeStrict(OJClass.forClass(Integer.class), exprType, true)
			|| compatibleAssignTypeStrict(OJClass.forClass(Byte.class), exprType, true)
			|| compatibleAssignTypeStrict(OJClass.forClass(Short.class), exprType, true))
		{
			if (!(isZero && (isNeg || isPlus))) {
				expressions.add(Literal.makeLiteral(new Integer(0)));
			}
			if (!(isOne && isPlus) && !(isOne && isNeg)) expressions.add(Literal.makeLiteral(new Integer(1)));
			if (!(isOne && isNeg)) {
				expressions.add(new UnaryExpression(Literal.makeLiteral(new Integer(1)), UnaryExpression.MINUS));
			}
			BinaryExpression mutPlusOne = new BinaryExpression(originalCopy, BinaryExpression.PLUS, Literal.makeLiteral(new Integer(1)));
			BinaryExpression mutMinusOne = new BinaryExpression(originalCopy, BinaryExpression.MINUS, Literal.makeLiteral(new Integer(1)));
			mutPlusOne.forceParenthesis(true);
			mutMinusOne.forceParenthesis(true);
			expressions.add(mutPlusOne);
			expressions.add(mutMinusOne);
		
		} else if (compatibleAssignTypeStrict(OJClass.forClass(Float.class), exprType, true)) {
			if (!(isZero && (isNeg || isPlus))) {
				expressions.add(Literal.makeLiteral(new Float(0.0f)));
			}
			if (!(isOne && isPlus) && !(isOne && isNeg)) expressions.add(Literal.makeLiteral(new Float(1.0f)));
			if (!(isOne && isNeg)) {
				expressions.add(new UnaryExpression(Literal.makeLiteral(new Float(1.0f)), UnaryExpression.MINUS));
			}
			BinaryExpression mutPlusOne = new BinaryExpression(originalCopy, BinaryExpression.PLUS, Literal.makeLiteral(new Float(1.0f)));
			BinaryExpression mutMinusOne = new BinaryExpression(originalCopy, BinaryExpression.MINUS, Literal.makeLiteral(new Float(1.0f)));
			mutPlusOne.forceParenthesis(true);
			mutMinusOne.forceParenthesis(true);
			expressions.add(mutPlusOne);
			expressions.add(mutMinusOne);
			
		} else if (compatibleAssignTypeStrict(OJClass.forClass(Double.class), exprType, true)) {
			if (!(isZero && (isNeg || isPlus))) {
				expressions.add(Literal.makeLiteral(new Double(0.0d)));
			}
			if (!(isOne && isPlus) && !(isOne && isNeg)) expressions.add(Literal.makeLiteral(new Double(1.0d)));
			if (!(isOne && isNeg)) {
				expressions.add(new UnaryExpression(Literal.makeLiteral(new Double(1.0d)), UnaryExpression.MINUS));
			}
			BinaryExpression mutPlusOne = new BinaryExpression(originalCopy, BinaryExpression.PLUS, Literal.makeLiteral(new Double(1.0d)));
			BinaryExpression mutMinusOne = new BinaryExpression(originalCopy, BinaryExpression.MINUS, Literal.makeLiteral(new Double(1.0d)));
			mutPlusOne.forceParenthesis(true);
			mutMinusOne.forceParenthesis(true);
			expressions.add(mutPlusOne);
			expressions.add(mutMinusOne);
		
		} else if (compatibleAssignTypeStrict(OJClass.forClass(Long.class), exprType, true)) {
			if (!(isZero && (isNeg || isPlus))) {
				expressions.add(Literal.makeLiteral(new Long(0l)));
			}
			if (!(isOne && isPlus) && !(isOne && isNeg)) expressions.add(Literal.makeLiteral(new Long(1l)));
			if (!(isOne && isNeg)) {
				expressions.add(new UnaryExpression(Literal.makeLiteral(new Long(1l)), UnaryExpression.MINUS));
			}
			BinaryExpression mutPlusOne = new BinaryExpression(originalCopy, BinaryExpression.PLUS, Literal.makeLiteral(new Long(1l)));
			BinaryExpression mutMinusOne = new BinaryExpression(originalCopy, BinaryExpression.MINUS, Literal.makeLiteral(new Long(1l)));
			mutPlusOne.forceParenthesis(true);
			mutMinusOne.forceParenthesis(true);
			expressions.add(mutPlusOne);
			expressions.add(mutMinusOne);
		} else {
			throw new ParseTreeException("getMutations called with incompatible type");
		}
		if (!isZero) expressions.add(new UnaryExpression(originalCopy, UnaryExpression.MINUS));
		return expressions;
	}
	
	private List<Expression> getMutations(Literal from) {
		List<Expression> expressions = new LinkedList<>();
		boolean isZero = isZero(from);
		boolean isOne = isOne(from);
		Literal originalCopy = (Literal) from.makeRecursiveCopy_keepOriginalID();
		if (!isOne && !isZero) expressions.add(new UnaryExpression(originalCopy, UnaryExpression.MINUS));
		if (from.getLiteralType() == Literal.INTEGER) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Integer(0)));
			if (!isOne) expressions.add(Literal.makeLiteral(new Integer(1)));
			expressions.add(new UnaryExpression(Literal.makeLiteral(new Integer(1)), UnaryExpression.MINUS));
			BinaryExpression mutPlusOne = new BinaryExpression(originalCopy, BinaryExpression.PLUS, Literal.makeLiteral(new Integer(1)));
			BinaryExpression mutMinusOne = new BinaryExpression(originalCopy, BinaryExpression.MINUS, Literal.makeLiteral(new Integer(1)));
			mutPlusOne.forceParenthesis(true);
			mutMinusOne.forceParenthesis(true);
			expressions.add(mutPlusOne);
			expressions.add(mutMinusOne);
		} else if (from.getLiteralType() == Literal.DOUBLE) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Double(0.0d)));
			if (!isOne) expressions.add(Literal.makeLiteral(new Double(1.0d)));
			expressions.add(new UnaryExpression(Literal.makeLiteral(new Double(1.0d)), UnaryExpression.MINUS));
			BinaryExpression mutPlusOne = new BinaryExpression(originalCopy, BinaryExpression.PLUS, Literal.makeLiteral(new Double(1.0d)));
			BinaryExpression mutMinusOne = new BinaryExpression(originalCopy, BinaryExpression.MINUS, Literal.makeLiteral(new Double(1.0d)));
			mutPlusOne.forceParenthesis(true);
			mutMinusOne.forceParenthesis(true);
			expressions.add(mutPlusOne);
			expressions.add(mutMinusOne);
		} else if (from.getLiteralType() == Literal.FLOAT) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Float(0.0f)));
			if (!isOne) expressions.add(Literal.makeLiteral(new Float(1.0f)));
			expressions.add(new UnaryExpression(Literal.makeLiteral(new Float(1.0f)), UnaryExpression.MINUS));
			BinaryExpression mutPlusOne = new BinaryExpression(originalCopy, BinaryExpression.PLUS, Literal.makeLiteral(new Float(1.0f)));
			BinaryExpression mutMinusOne = new BinaryExpression(originalCopy, BinaryExpression.MINUS, Literal.makeLiteral(new Float(1.0f)));
			mutPlusOne.forceParenthesis(true);
			mutMinusOne.forceParenthesis(true);
			expressions.add(mutPlusOne);
			expressions.add(mutMinusOne);
		} else if (from.getLiteralType()==Literal.LONG) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Long(0l)));
			if (!isOne) expressions.add(Literal.makeLiteral(new Long(1l)));
			expressions.add(new UnaryExpression(Literal.makeLiteral(new Long(1l)), UnaryExpression.MINUS));
			BinaryExpression mutPlusOne = new BinaryExpression(originalCopy, BinaryExpression.PLUS, Literal.makeLiteral(new Long(1)));
			BinaryExpression mutMinusOne = new BinaryExpression(originalCopy, BinaryExpression.MINUS, Literal.makeLiteral(new Long(1l)));
			mutPlusOne.forceParenthesis(true);
			mutMinusOne.forceParenthesis(true);
			expressions.add(mutPlusOne);
			expressions.add(mutMinusOne);
		}
		return expressions;
	}
	
	private boolean isZero(Expression e) {
		if (e instanceof Literal) return isZero((Literal) e);
		return false;
	}
	
	private boolean isZero(Literal l) {
		if (l.getLiteralType() == Literal.INTEGER) {
			Integer intLiteral = Integer.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return intLiteral == 0;
		} else if (l.getLiteralType() == Literal.DOUBLE) {
			Double doubleLiteral = Double.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return doubleLiteral == 0.0d;
		} else if (l.getLiteralType() == Literal.FLOAT) {
			Float floatLiteral = Float.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return floatLiteral == 0.0f;
		} else if (l.getLiteralType()==Literal.LONG) {
			Long longLiteral = Long.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return longLiteral == 0l;
		}
		return false;
	}
	
	private boolean isOne(Expression e) {
		if (e instanceof Literal) return isOne((Literal) e);
		return false;
	}
	
	private boolean isOne(Literal l) {
		if (l.getLiteralType() == Literal.INTEGER) {
			Integer intLiteral = Integer.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return intLiteral == 1;
		} else if (l.getLiteralType() == Literal.DOUBLE) {
			Double doubleLiteral = Double.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return doubleLiteral == 1.0d;
		} else if (l.getLiteralType() == Literal.FLOAT) {
			Float floatLiteral = Float.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return floatLiteral == 1.0f;
		} else if (l.getLiteralType()==Literal.LONG) {
			Long longLiteral = Long.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return longLiteral == 1l;
		}
		return false;
	}
	
	private String removeEndingChar(String original) {
		char lastChar = original.charAt(original.length() - 1);
		if (!Character.isDigit(lastChar)) {
			return original.substring(0, original.length() - 1);
		} else {
			return original;
		}
	}
	
	public void outputToFile(Literal original, Expression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.CRCR, original, (ParseTreeObject) mutant);

	}
	
	public void outputToFile(UnaryExpression original, Expression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.CRCR, original, (ParseTreeObject) mutant);

	}

}
