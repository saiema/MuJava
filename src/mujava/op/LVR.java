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

/**
 * 
 * LVR (Literal Value Replacement)	Replaces a literal with a default value:
 * <p>
 * <li>A numerical literal is replaced with a positive number, a negative number, and zero.</li>
 * <li>A boolean literal is replaced with its logical complement.</li>
 * <li>A String literal is replaced with the empty String.</li>
 * <p>
 * Examples:
 * <li> 0 		=>  	1</li>
 * <li> 1 		=> 	   -1</li>
 * <li> 1 		=>  	0</li>
 * <li> true 	=> 	false</li>
 * <li> false 	=>	 true</li>
 * <li> "Hello"	=>	   ""</li>
 *
 */
public class LVR extends Arithmetic_OP {
	
	public LVR(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super( file_env, comp_unit );
	}
	
	public void visit(Literal p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		if (isArithmeticType(p)) {
			for (Expression m : getArithmeticMutations(p)) {
				outputToFile(p, m);
			}
		} else if (p.getLiteralType() == Literal.STRING) {
			if (p.toFlattenString().trim().isEmpty()) return;
			outputToFile(p, Literal.constantEmptyString());
		} else if (p.getLiteralType() == Literal.BOOLEAN) {
			outputToFile(p, getBooleanMutation(p));
		}
	}

	public void visit(UnaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		if (!(isArithmeticType(p) || isBoolean(getType(p)))) return;
		
		Expression asocExpr = p.getExpression();
		
		if (!(asocExpr instanceof Literal)) super.visit(p.getExpression());
		else if (p.getOperator() == UnaryExpression.PLUS) super.visit(p.getExpression());
		else if (isArithmeticType(p)) {
			for (Expression m : getArithmeticMutations(p)) {
				outputToFile(p, m);
			}
		} else if (isBoolean(getType(p))) {
			super.visit(p.getExpression());
		}
	}
	
	/**
	 * replace the unmutated value true with false and replace the unmutated value false with true.
	 * 
	 * @param from		: the unmutated value
	 * @return			: all mutations as stated in the description
	 * @throws ParseTreeException
	 */
	private Expression getBooleanMutation(Literal from) throws ParseTreeException {
		OJClass exprType = getType(from);
		if (compatibleAssignTypeStrict(OJClass.forClass(Boolean.class), exprType, true)) {
			if (isBoolean(from, true)) {
				return Literal.constantFalse();
			} else if (isBoolean(from, false)) {
				return Literal.constantTrue();
			} else {
				throw new ParseTreeException("getBooleanMutation couldn't get boolean value");
			}
		} else {
			throw new ParseTreeException("getBooleanMutation called with incompatible type");
		}
	}
	
	private List<Expression> getArithmeticMutations(UnaryExpression from) throws ParseTreeException {
		List<Expression> expressions = new LinkedList<>();
		OJClass exprType = getType(from);
		boolean isNeg = from.getOperator() == UnaryExpression.MINUS;
		boolean isZero = isZero(from.getExpression());
		boolean isOne = isOne(from.getExpression());
		if (compatibleAssignTypeStrict(OJClass.forClass(Integer.class), exprType, true)
			|| compatibleAssignTypeStrict(OJClass.forClass(Byte.class), exprType, true)
			|| compatibleAssignTypeStrict(OJClass.forClass(Short.class), exprType, true)
		) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Integer(0)));
			if (!(isOne && isNeg)) expressions.add(new UnaryExpression(Literal.makeLiteral(new Integer(1)), UnaryExpression.MINUS));
			if (!(isOne && !isNeg)) expressions.add(Literal.makeLiteral(new Integer(1)));
		
		} else if (compatibleAssignTypeStrict(OJClass.forClass(Float.class), exprType, true)) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Float(0.0f)));
			if (!(isOne && isNeg)) expressions.add(new UnaryExpression(Literal.makeLiteral(new Float(1.0f)), UnaryExpression.MINUS));
			if (!(isOne && !isNeg)) expressions.add(Literal.makeLiteral(new Float(1.0f)));
			
		} else if (compatibleAssignTypeStrict(OJClass.forClass(Double.class), exprType, true)) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Double(0.0d)));
			if (!(isOne && isNeg)) expressions.add(new UnaryExpression(Literal.makeLiteral(new Double(1.0d)), UnaryExpression.MINUS));
			if (!(isOne && !isNeg)) expressions.add(Literal.makeLiteral(new Double(1.0d)));
		
		} else if (compatibleAssignTypeStrict(OJClass.forClass(Long.class), exprType, true)) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Long(0l)));
			if (!(isOne && isNeg)) expressions.add(new UnaryExpression(Literal.makeLiteral(new Long(1l)), UnaryExpression.MINUS));
			if (!(isOne && !isNeg)) expressions.add(Literal.makeLiteral(new Long(1l)));
		} else {
			throw new ParseTreeException("getArithmeticMutations called with incompatible type");
		}
		return expressions;
	}
	
	private List<Expression> getArithmeticMutations(Literal from) throws ParseTreeException {
		List<Expression> expressions = new LinkedList<>();
		boolean isZero = isZero(from);
		boolean isOne = isOne(from);
		if (from.getLiteralType() == Literal.INTEGER) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Integer(0)));
			expressions.add(new UnaryExpression(Literal.makeLiteral(new Integer(1)), UnaryExpression.MINUS));
			if (!isOne) expressions.add(Literal.makeLiteral(new Integer(1)));
		} else if (from.getLiteralType() == Literal.DOUBLE) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Double(0.0d)));
			expressions.add(new UnaryExpression(Literal.makeLiteral(new Double(1.0d)), UnaryExpression.MINUS));
			if (!isOne) expressions.add(Literal.makeLiteral(new Double(1.0d)));
		} else if (from.getLiteralType() == Literal.FLOAT) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Float(0.0f)));
			expressions.add(new UnaryExpression(Literal.makeLiteral(new Float(1.0f)), UnaryExpression.MINUS));
			if (!isOne) expressions.add(Literal.makeLiteral(new Float(1.0f)));
		} else if (from.getLiteralType()==Literal.LONG) {
			if (!isZero) expressions.add(Literal.makeLiteral(new Long(0l)));
			expressions.add(new UnaryExpression(Literal.makeLiteral(new Long(1l)), UnaryExpression.MINUS));
			if (!isOne) expressions.add(Literal.makeLiteral(new Long(1l)));
		} else {
			throw new ParseTreeException("getArithmeticMutations called with incompatible type");
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
	
	private boolean isBoolean(OJClass e) throws ParseTreeException {
		return e.getName().compareToIgnoreCase("boolean") == 0;
	}
	
	private boolean isBoolean(Literal e, boolean b) {
		if (e.getLiteralType() == Literal.BOOLEAN)
			return e.toFlattenString().trim().compareTo(Boolean.toString(b)) == 0;
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

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.LVR, original, (ParseTreeObject) mutant);

	}
	
	public void outputToFile(UnaryExpression original, Expression mutant) {
		if (comp_unit == null) 
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.LVR, original, (ParseTreeObject) mutant);

	}

}
