package mujava.op;

import mujava.api.MutantsInformationHolder;
import mujava.api.MutationOperator;
import mujava.op.basic.Arithmetic_OP;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.ptree.*;

import java.util.LinkedList;
import java.util.List;

/**
 * PIT operator.
 *
 * Generates CRCR (Constant Replacement Mutator) mutations.
 * <ul>
 * 		<li>{@code c} => {@code 1} : if {@code c} is not {@code 1}</li>
 * 		<li>{@code c} => {@code 0} : if {@code c} is not {@code 0}</li>
 * 		<li>{@code c} => {@code -1} : if {@code c} is not {@code -1}</li>
 * 		<li>{@code c} => {@code -c} : if {@code c} is not {@code 0} nor {@code 1}</li>
 * 		<li>{@code c} => {@code c + 1}</li>
 * 		<li>{@code c} => {@code c - 1}</li>
 * </ul>
 *
 * @see <a href="https://pitest.org/quickstart/mutators/#EXPERIMENTAL_CRCR">PIT CRCR Documentation</a>.
 */
public class CRCR extends Arithmetic_OP {
	
	public CRCR(FileEnvironment file_env, CompilationUnit comp_unit) {
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
				expressions.add(Literal.makeLiteral(new Long(0L)));
			}
			if (!(isOne && isPlus) && !(isOne && isNeg)) expressions.add(Literal.makeLiteral(new Long(1L)));
			if (!(isOne && isNeg)) {
				expressions.add(new UnaryExpression(Literal.makeLiteral(new Long(1L)), UnaryExpression.MINUS));
			}
			BinaryExpression mutPlusOne = new BinaryExpression(originalCopy, BinaryExpression.PLUS, Literal.makeLiteral(new Long(1L)));
			BinaryExpression mutMinusOne = new BinaryExpression(originalCopy, BinaryExpression.MINUS, Literal.makeLiteral(new Long(1L)));
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
		} else if (from.getLiteralType() == Literal.LONG) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Long(0L)));
			if (!isOne) expressions.add(Literal.makeLiteral(new Long(1L)));
			expressions.add(new UnaryExpression(Literal.makeLiteral(new Long(1L)), UnaryExpression.MINUS));
			BinaryExpression mutPlusOne = new BinaryExpression(originalCopy, BinaryExpression.PLUS, Literal.makeLiteral(new Long(1L)));
			BinaryExpression mutMinusOne = new BinaryExpression(originalCopy, BinaryExpression.MINUS, Literal.makeLiteral(new Long(1L)));
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
		String litRawValue = l.toFlattenString().trim();
		if (l.getLiteralType() == Literal.INTEGER) {
			int intLiteral;
			if (litRawValue.contains("x") || litRawValue.contains("X")) {
				try {
					String hexString = getHexString(litRawValue);
					intLiteral = Integer.parseInt(hexString, 16);
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
					return false;
				}
			} else {
				intLiteral = Integer.parseInt(removeEndingChar(litRawValue));
			}
			return intLiteral == 0;
		} else if (l.getLiteralType() == Literal.DOUBLE) {
			double doubleLiteral = Double.parseDouble(removeEndingChar(litRawValue));
			return doubleLiteral == 0.0d;
		} else if (l.getLiteralType() == Literal.FLOAT) {
			float floatLiteral = Float.parseFloat(removeEndingChar(litRawValue));
			return floatLiteral == 0.0f;
		} else if (l.getLiteralType()==Literal.LONG) {
			long longLiteral = Long.parseLong(removeEndingChar(litRawValue));
			return longLiteral == 0L;
		}
		return false;
	}
	
	private boolean isOne(Expression e) {
		if (e instanceof Literal) return isOne((Literal) e);
		return false;
	}
	
	private boolean isOne(Literal l) {
		String litRawValue = l.toFlattenString().trim();
		if (l.getLiteralType() == Literal.INTEGER) {
			int intLiteral;
			if (litRawValue.contains("x") || litRawValue.contains("X")) {
				try {
					String hexString = getHexString(litRawValue);
					intLiteral = Integer.parseInt(hexString, 16);
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
					return false;
				}
			} else {
				intLiteral = Integer.parseInt(removeEndingChar(litRawValue));
			}
			return intLiteral == 1;
		} else if (l.getLiteralType() == Literal.DOUBLE) {
			double doubleLiteral = Double.parseDouble(removeEndingChar(litRawValue));
			return doubleLiteral == 1.0d;
		} else if (l.getLiteralType() == Literal.FLOAT) {
			float floatLiteral = Float.parseFloat(removeEndingChar(litRawValue));
			return floatLiteral == 1.0f;
		} else if (l.getLiteralType()==Literal.LONG) {
			long longLiteral = Long.parseLong(removeEndingChar(litRawValue));
			return longLiteral == 1L;
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

	private String getHexString(String value) {
		if (!value.contains("x") && !value.contains("X"))
			throw new IllegalArgumentException("value is not an hex string (" + value + ")");
		int xIdx = Math.max(value.indexOf('x'), value.indexOf('X'));
		return value.substring(xIdx + 1);
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
