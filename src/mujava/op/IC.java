package mujava.op;

import java.lang.reflect.Modifier;

import mujava.api.MutantsInformationHolder;
import mujava.api.MutationOperator;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJSystem;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.UnaryExpression;
import openjava.ptree.VariableDeclaration;
import openjava.ptree.Literal;
import openjava.ptree.BinaryExpression;
import openjava.ptree.Statement;

public class IC extends mujava.op.util.Mutator {

	public IC(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}
	
	public void visit(Literal p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		if (!validate(p)) return;
		Literal unmutatedValue = p;
		OJClass initType = getDeclaringType(p);
		if (isArithmetic(initType)) {
			outputToFile(unmutatedValue, getArtihmeticMutation(unmutatedValue));
		} else if (isBoolean(initType)) {
			outputToFile(unmutatedValue, getBooleanMutation((Literal) unmutatedValue));
		}
	}
	
	public void visit(UnaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		if (!validate(p)) return;
		UnaryExpression unmutatedValue = p;
		if (!(unmutatedValue.getExpression() instanceof Literal)) return;
		if (unmutatedValue.getOperator() != UnaryExpression.MINUS) return;
		OJClass initType = getDeclaringType(p);
		if (isArithmetic(initType)) {
			outputToFile(unmutatedValue, getArtihmeticMutation(unmutatedValue));
		}
	}
	
	private boolean validate(Expression e) throws ParseTreeException {
		OJClass declType = getDeclaringType(e);
		if (declType == null) return false;
		VariableDeclaration vd = (VariableDeclaration) getStatement((ParseTreeObject) e);
		if (vd.getModifiers().contains(Modifier.FINAL)) return false;
		if (!(isArithmetic(declType) || isBoolean(declType))) return false;
		return true;
	}
	
	private OJClass getDeclaringType(Expression e) throws ParseTreeException {
		Statement declaringStatement = (Statement) getStatement((ParseTreeObject) e);
		if (declaringStatement == null) return null;
		if (declaringStatement instanceof VariableDeclaration) {
			VariableDeclaration vd = (VariableDeclaration) declaringStatement;
			OJClass declType = getType(vd.getTypeSpecifier());
			return declType;
		}
		return null;
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

	private Expression getArtihmeticMutation(Expression from) throws ParseTreeException {
		OJClass exprType = getType(from);
		if (compatibleAssignTypeStrict(OJClass.forClass(Integer.class), exprType, true)
				|| compatibleAssignTypeStrict(OJClass.forClass(Byte.class), exprType, true)
				|| compatibleAssignTypeStrict(OJClass.forClass(Short.class), exprType, true)) {
			return getIntegerByteShortMutation(from);
		} else if (compatibleAssignTypeStrict(OJClass.forClass(Long.class), exprType, true)) {
			return getLongMutation(from);
		} else if (compatibleAssignTypeStrict(OJClass.forClass(Float.class), exprType, true)) {
			return getFloatMutation(from);
		} else if (compatibleAssignTypeStrict(OJClass.forClass(Double.class), exprType, true)) {
			return getDoubleMutation(from);
		} else {
			throw new ParseTreeException("getArithmeticMutation called with incompatible type");
		}
	}

	/**
	 * replace the unmutated value 1.0 with 0.0 and replace any other value with 1.0.
	 * 
	 * @param from		: the unmutated value
	 * @return			: all mutations as stated in the description
	 * @throws ParseTreeException
	 */
	private Expression getDoubleMutation(Expression from) {
		boolean isOne = isOne(from);
		boolean isNeg = isNeg(from);
		if (!isNeg && isOne) {
			return Literal.makeLiteral(0.0d);
		} else {
			return Literal.makeLiteral(1.0d);
		}
	}

	/**
	 * 
	 * replace the unmutated values 1.0 and 2.0 with 0.0 and replace any other value with 1.0.
	 * 
	 * @param from		: the unmutated value
	 * @return			: all mutations as stated in the description
	 * @throws ParseTreeException
	 */
	private Expression getFloatMutation(Expression from) {
		boolean isOne = isOne(from);
		boolean isTwo = isTwo(from);
		boolean isNeg = isNeg(from);
		if (!isNeg && (isOne || isTwo)) {
			return Literal.makeLiteral(0.0f);
		} else {
			return Literal.makeLiteral(1.0f);
		}
	}

	/**
	 * 
	 * replace the unmutated value 1 with 0, otherwise increment the unmutated value by one.
	 * 
	 * @param from		: the unmutated value
	 * @return			: all mutations as stated in the description
	 * @throws ParseTreeException
	 */
	private Expression getLongMutation(Expression from) {
		boolean isOne = isOne(from);
		boolean isNeg = isNeg(from);
		if (isOne && !isNeg) {
			return Literal.constantZero();
		} else {
			Expression fromCopy = (Expression) from.makeRecursiveCopy_keepOriginalID();
			BinaryExpression mutatedValue = new BinaryExpression(fromCopy, BinaryExpression.PLUS, Literal.makeLiteral(1l)); 
			mutatedValue.forceParenthesis(true);
			return mutatedValue;
		}
	}

	/**
	 * 
	 * replace the unmutated value 1 with 0, -1 with 1, 5 with -1 or otherwise increment the unmutated value by one.
	 * 
	 * @param from		: the unmutated value
	 * @return			: all mutations as stated in the description
	 * @throws ParseTreeException
	 */
	private Expression getIntegerByteShortMutation(Expression from) {
		boolean isOne = isOne(from);
		boolean isNeg = isNeg(from);
		boolean isFive = isFive(from);
		if (isOne && !isNeg) {
			return Literal.constantZero();
		} else if (isOne && isNeg) {
			return new UnaryExpression(Literal.constantOne(), UnaryExpression.MINUS);
		} else if (isFive && !isNeg) {
			return new UnaryExpression(Literal.constantOne(), UnaryExpression.MINUS);
		} else {
			Expression fromCopy = (Expression) from.makeRecursiveCopy_keepOriginalID();
			BinaryExpression mutatedValue = new BinaryExpression(fromCopy, BinaryExpression.PLUS, Literal.makeLiteral(1)); 
			mutatedValue.forceParenthesis(true);
			return mutatedValue;
		}
	}
	
	private boolean isArithmetic(OJClass type) throws ParseTreeException {
		if (type.isPrimitiveWrapper()) type = type.unwrappedPrimitive();
	      if ( type == OJSystem.INT || type == OJSystem.DOUBLE || type == OJSystem.FLOAT
	         || type == OJSystem.LONG || type == OJSystem.SHORT 
	         || type == OJSystem.CHAR || type == OJSystem.BYTE )
	      {
	         return true;
	      }
	      return false;
	}

	private boolean isBoolean(OJClass e) throws ParseTreeException {
		return e.getName().compareToIgnoreCase("boolean") == 0;
	}

	private boolean isTwo(Expression e) {
		if (e instanceof Literal)
			return isTwo((Literal) e);
		else if (e instanceof UnaryExpression) {
			return isTwo(((UnaryExpression) e).getExpression());
		}
		return false;
	}

	private boolean isTwo(Literal l) {
		if (l.getLiteralType() == Literal.INTEGER) {
			Integer intLiteral = Integer.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return intLiteral == 2;
		} else if (l.getLiteralType() == Literal.DOUBLE) {
			Double doubleLiteral = Double.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return doubleLiteral == 2.0d;
		} else if (l.getLiteralType() == Literal.FLOAT) {
			Float floatLiteral = Float.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return floatLiteral == 2.0f;
		} else if (l.getLiteralType() == Literal.LONG) {
			Long longLiteral = Long.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return longLiteral == 2l;
		}
		return false;
	}

	private boolean isOne(Expression e) {
		if (e instanceof Literal)
			return isOne((Literal) e);
		else if (e instanceof UnaryExpression) {
			return isOne(((UnaryExpression) e).getExpression());
		}
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
		} else if (l.getLiteralType() == Literal.LONG) {
			Long longLiteral = Long.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return longLiteral == 1l;
		}
		return false;
	}
	
	private boolean isFive(Expression e) {
		if (e instanceof Literal)
			return isFive((Literal) e);
		else if (e instanceof UnaryExpression) {
			return isFive(((UnaryExpression) e).getExpression());
		}
		return false;
	}

	private boolean isFive(Literal l) {
		if (l.getLiteralType() == Literal.INTEGER) {
			Integer intLiteral = Integer.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return intLiteral == 5;
		} else if (l.getLiteralType() == Literal.DOUBLE) {
			Double doubleLiteral = Double.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return doubleLiteral == 5.0d;
		} else if (l.getLiteralType() == Literal.FLOAT) {
			Float floatLiteral = Float.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return floatLiteral == 5.0f;
		} else if (l.getLiteralType() == Literal.LONG) {
			Long longLiteral = Long.valueOf(removeEndingChar(l.toFlattenString().trim()));
			return longLiteral == 5l;
		}
		return false;
	}
	
	private boolean isNeg(Expression e) {
		if (!(e instanceof UnaryExpression)) return false;
		return ((UnaryExpression) e).getOperator() == UnaryExpression.MINUS;
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

	public void outputToFile(Expression original, Expression mutant) {
		if (comp_unit == null)
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.IC, (ParseTreeObject) original, (ParseTreeObject) mutant);

	}

}
