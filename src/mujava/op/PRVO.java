package mujava.op;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Comparator;

import mujava.api.Api;
import mujava.api.Configuration;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.AllocationExpression;
import openjava.ptree.ArrayAccess;
import openjava.ptree.ArrayInitializer;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.BinaryExpression;
import openjava.ptree.Block;
import openjava.ptree.CaseGroup;
import openjava.ptree.CaseGroupList;
import openjava.ptree.CastExpression;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.ConditionalExpression;
import openjava.ptree.ConstructorInvocation;
import openjava.ptree.DoWhileStatement;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionList;
import openjava.ptree.FieldAccess;
import openjava.ptree.ForStatement;
import openjava.ptree.IfStatement;
import openjava.ptree.Literal;
import openjava.ptree.MethodCall;
import openjava.ptree.MethodDeclaration;
import openjava.ptree.ModifierList;
import openjava.ptree.NonLeaf;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.ReturnStatement;
import openjava.ptree.SelfAccess;
import openjava.ptree.Statement;
import openjava.ptree.StatementList;
import openjava.ptree.SwitchStatement;
import openjava.ptree.TryStatement;
import openjava.ptree.TypeName;
import openjava.ptree.UnaryExpression;
import openjava.ptree.Variable;
import openjava.ptree.VariableDeclaration;
import openjava.ptree.VariableDeclarator;
import openjava.ptree.WhileStatement;

import java.util.List;



/**
 * <p>
 * Generates PRV and PRVO mutations, PRV mutations changes primitive variables and literals and PRVO mutations
 * changes not primitive variables, field access, literals and method calls.
 * </p>
 * 
 * @author Pablo Alejandro Giorgi
 * @author Nicolás Magni
 * @author Santiago José Samra
 * @author Matías Williams
 * <hr> rewritten by
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 3.3
 */
public class PRVO extends mujava.op.util.Mutator {

	/**
	 * Option to enable/disable the use of {@code super} in the generated mutations
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_SUPER = "prvo_enable_super";
	/**
	 * Option to enable/disable the use of {@code this} in the generated mutations
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_THIS = "prvo_enable_this";
	/**
	 * Option to enable/disable the use of literals to replace expressions of size one
	 * <p>
	 * this option is enabled by default but will only be used if a refined version of a PRVO operator is used
	 */
	public static final String ENABLE_REPLACEMENT_WITH_LITERALS = "prvo_enable_replacement_with_literals";
	/**
	 * Option to enable/disable the use of the literal {@code null} in the generated mutations
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_LITERAL_NULL = "prvo_enable_literal_null";
	/**
	 * Option to enable/disable the use of the literal {@code true} in the generated mutations
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_LITERAL_TRUE = "prvo_enable_literal_true";
	/**
	 * Option to enable/disable the use of the literal {@code false} in the generated mutations
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_LITERAL_FALSE = "prvo_enable_literal_false";
	/**
	 * Option to enable/disable the use of the empty string literal in the generated mutations
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_LITERAL_EMPTY_STRING = "prvo_enable_literal_empty_string";
	/**
	 * Option to enable/disable the use of the literal {@code 0} in the generated mutations
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_LITERAL_ZERO = "prvo_enable_literal_zero";
	/**
	 * Option to enable/disable the use of the literal {@code 1} in the generated mutations
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_LITERAL_ONE = "prvo_enable_literal_one";
	/**
	 * Option to enable/disable the use of the string literals (collected from the method to mutate code) in the generated mutations
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_LITERAL_STRINGS = "prvo_enable_literal_strings";
	/**
	 * Option to set a list of prohibited methods or a map of prohibited methods per class that will not be used to generated mutations
	 * <p>
	 * this option is not set by default
	 */
	public static final String PROHIBITED_METHODS = "prvo_prohibited_methods";
	/**
	 * Option to set a list of prohibited fields or a map of prohibited fields per class that will not be used to generated mutations
	 * <p>
	 * this option is not set by default
	 */
	public static final String PROHIBITED_FIELDS = "prvo_prohibited_fields";
	/**
	 * Option to enable/disable mutations that changes one element of an expression without affecting it's length
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_SAME_LENGTH_MUTANTS = "prvo_same_length_mutants";
	/**
	 * Option to enable/disable mutations that changes an expression by adding a new element
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_INCREASE_LENGTH_MUTANTS = "prvo_increase_length_mutants";
	/**
	 * Option to enable/disable mutations that removes an element from an expression
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_DECREASE_LENGTH_MUTANTS = "prvo_decrease_length_mutants";
	/**
	 * Option to enable/disable mutations that changes one field or method in a chained expression with a chained expression of size 2
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_ONE_BY_TWO_MUTANTS = "prvo_one_by_two_mutants";
	/**
	 * Option to enable/disable mutations that changes a sub chained expression of size 2 with one field or method
	 * <p>
	 * this option is enabled by default 
	 */
	public static final String ENABLE_TWO_BY_ONE_MUTANTS = "prvo_two_by_one_mutants";
	/**
	 * Option to enable/disable mutations affecting a chained expression on the left side of an assignment statement replacing this expression with a variable or field
	 * <p>
	 * this option is disabled by default
	 */
	public static final String ENABLE_ALL_BY_ONE_MUTANTS_LEFT = "prvo_all_by_one_mutants_left";
	/**
	 * Option to enable/disable mutations affecting a chained expression on the right side of an assignment statement replacing this expression with a variable, field or null
	 * <p>
	 * this option is disabled by default
	 */
	public static final String ENABLE_ALL_BY_ONE_MUTANTS_RIGHT = "prvo_all_by_one_mutants_right";
	/**
	 * Option to enable/disable number literals variations when generating mutants.
	 * A number literal variation is when given a number literal K add all it's variations within primitive types (integer, float, double, long)
	 * <p>
	 * this option is disabled by default
	 */
	public static final String ENABLE_NUMBER_LITERALS_VARIATIONS = "prvo_number_literals_variations";
	/**
	 * Option to enable/disable wrapping of mutants of the form {@Code Object var = primitive type expression}.
	 * Wrapping will generate {@code new T(primitive type expression)} where {@code T} is a wrapper type for the primitive type of the original expression.
	 * <p>
	 * this option is disabled by default
	 */
	public static final String ENABLE_PRIMITIVE_WRAPPING = "prvo_primitive_wrapping";
	/**
	 * Option to enable/disable mutants of the form {@Code Object var = primitive type expression}.
	 * <p>
	 * this option is enabled by default
	 */
	public static final String ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS = "prvo_primitive_to_object_assignments";
	
	ParseTreeObject parent = null;

	private boolean allowNonStatic = true;

	private boolean justEvaluating = false;

	private Set<Literal> literals = new TreeSet<Literal>(
			new Comparator<Literal>() {

				public int compare(Literal l1, Literal l2) {
					return l1.toString().compareTo(l2.toString());
				}

			}
			);

	private boolean left = false;
	private boolean right = false;
	private boolean unary = false;
	private boolean useLiterals = false;
	private boolean refinedMode = false;
	private Stack<Boolean> refModeAllowNullStack = new Stack<Boolean>();
	private Stack<Expression> refModeComplyTypeStack = new Stack<Expression>();
	private boolean smartMode;
	private Mutant op;

	private HashMap<String, java.util.List<Object>> fieldsAndMethodsPerClass = new HashMap<String, java.util.List<Object>>();

	@SuppressWarnings("unchecked")
	public PRVO(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
		this.smartMode = false;
		if (Configuration.argumentExist(PROHIBITED_METHODS)) {
			Object configValue = Configuration.getValue(PROHIBITED_METHODS);
			try {
				this.prohibitedMethodsPerClass = (Map<String, List<String>>) configValue;
			} catch (ClassCastException e) {
				try {
					this.prohibitedMethods = (List<String>) configValue;
				} catch (ClassCastException ex) {
					throw new IllegalStateException("The value for PROHIBITED_METHODS it's not a Map<String, List<String>> nor a List<String>");
				}
			}
		}
		if (Configuration.argumentExist(PROHIBITED_FIELDS)) {
			Object configValue = Configuration.getValue(PROHIBITED_FIELDS);
			try {
				this.prohibitedFieldsPerClass = (Map<String, List<String>>) configValue;
			} catch (ClassCastException e) {
				try {
					this.prohibitedFields = (List<String>) configValue;
				} catch (ClassCastException ex) {
					throw new IllegalStateException("The value for PROHIBITED_FIELDS it's not a Map<String, List<String>> nor a List<String>");
				}
			}
		}
	}

	public void smartMode() {
		this.smartMode = true;
	}

	public void dumbMode() {
		this.smartMode = false;
	}

	public void useLiterals(boolean l) {
		this.useLiterals = l;
	}

	public void setRefinedMode(boolean r) {
		this.refinedMode = r;
		useLiterals(r);
		if (r) {
			smartMode();
		}
	}

	public void setOP(Mutant op) {
		this.op = op;
	}

	public void setLeft() {
		this.left = true;
		this.right = false;
		this.unary = false;
	}

	public void setRight() {
		this.left = false;
		this.right = true;
		this.unary = false;
	}

	public void setUnary() {
		this.left = false;
		this.right = false;
		this.unary = true;
	}

	private Expression append(Expression toAdd, Expression original) throws ParseTreeException {
		if (toAdd == null) return original;
		if (original == null) return toAdd;
		Expression originalCopy = (Expression) original.makeRecursiveCopy_keepOriginalID();
		Expression toAddCopy = (Expression) toAdd.makeRecursiveCopy_keepOriginalID();
		List<Expression> parts = new LinkedList<Expression>();
		while (canGetPrev(toAddCopy)) {
			Expression currentPart = (Expression) toAddCopy.makeRecursiveCopy_keepOriginalID();
			removeReferencedExpr(currentPart);
			parts.add(0, currentPart);
			toAddCopy = this.getPreviousExpression(toAddCopy);
			if (toAddCopy instanceof SelfAccess) {
				return original; //you can't append (this|super).xs to another expression
			}
		}
		if (toAddCopy != null) parts.add(0, toAddCopy);
		while (canGetPrev(originalCopy)) {
			Expression currentPart = (Expression) originalCopy.makeRecursiveCopy_keepOriginalID();
			removeReferencedExpr(currentPart);
			parts.add(0, currentPart);
			originalCopy = this.getPreviousExpression(originalCopy);
		}
		if (originalCopy != null) parts.add(0, originalCopy);
		Expression result = null;
		for (int i = 0; i < parts.size(); i++) {
			if (result == null) {
				result = parts.get(i);
			} else {
				result = setReferencedExpr(parts.get(i), result);
			}
		}
		return result;
	}
	
	private boolean canGetPrev(Expression expr) {
		if (expr == null) {
			return false;
		} else if (expr instanceof Variable) {
			return false;
		} else if (expr instanceof SelfAccess) {
			return false;
		} else if (expr instanceof FieldAccess) {
			return ((FieldAccess)expr).getReferenceExpr() != null;
		} else if (expr instanceof MethodCall) {
			return ((MethodCall)expr).getReferenceExpr() != null;
		} else if (expr instanceof ArrayAccess) {
			return ((ArrayAccess)expr).getReferenceExpr() != null;
		} else {
			return false;
		}
	}
	
	private void removeReferencedExpr(Expression expr) {
		if (expr instanceof FieldAccess) {
			((FieldAccess)expr).setReferenceExpr(null);
		} else if (expr instanceof MethodCall) {
			((MethodCall)expr).setReferenceExpr(null);
		} else if (expr instanceof ArrayAccess) {
			((ArrayAccess)expr).setReferenceExpr(null);
		}
	}
	
	private Expression setReferencedExpr(Expression expr, Expression ref) {
		if (expr instanceof FieldAccess) {
			((FieldAccess)expr).setReferenceExpr(ref);
		} else if (expr instanceof MethodCall) {
			((MethodCall)expr).setReferenceExpr(ref);
		} else if (expr instanceof ArrayAccess) {
			((ArrayAccess)expr).setReferenceExpr(ref);
		} else if (expr instanceof Variable) {
			return new FieldAccess(ref, ((Variable)expr).toString());
		}
		return expr;
	}

	private boolean findMember(OJMember m, OJMember[] members) {
		for (OJMember im : members) {
			String imName = im.getName();
			String mName = m.getName();
			if (compareNamesWithoutPackage(imName, mName)) {
				OJClass mType = (m instanceof OJMethod)?((OJMethod)m).getReturnType():((OJField)m).getType();
				OJClass imType = (im instanceof OJMethod)?((OJMethod)im).getReturnType():((OJField)im).getType();
				if (m instanceof OJMethod) {
					OJClass[] mArgsTypes = ((OJMethod)m).getParameterTypes();
					OJClass[] imArgsTypes = ((OJMethod)im).getParameterTypes();
					if (mArgsTypes.length != imArgsTypes.length) {
						return false;
					}
					for (int t = 0; t < mArgsTypes.length; t++) {
						OJClass mArg = mArgsTypes[t];
						OJClass imArg = imArgsTypes[t];
						if (mArg.getName().compareTo(imArg.getName()) != 0) {
							return false;
						}
					}
				}
				if (mType.getName().compareTo(imType.getName())==0) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isInherited(OJMember m) throws ParseTreeException {
		OJMember[] declaredMembers = (m instanceof OJMethod)?getSelfType().getDeclaredMethods():getSelfType().getDeclaredFields();
		OJMember[] inheritedMembers = (m instanceof OJMethod)?getInheritedMethods(getSelfType(), ALLOW_FINAL):getInheritedFields(getSelfType(), ALLOW_FINAL);
		if (findMember(m, declaredMembers)) {
			return false;
		} else {
			return findMember(m, inheritedMembers);
		}
	}

	private boolean findField(FieldAccess m, OJField[] fields) throws ParseTreeException {
		for (OJField im : fields) {
			String imName = im.getName();
			String mName = m.getName();
			if (compareNamesWithoutPackage(imName, mName)) {
				OJClass mType = getType(m);
				OJClass imType = im.getType();
				if (mType.getName().compareTo(imType.getName())==0) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isInherited(FieldAccess m) throws ParseTreeException {
		OJField[] declaredFields = getSelfType().getDeclaredFields();
		OJField[] inheritedFields = getInheritedFields(getSelfType(), ALLOW_FINAL);
		if (findField(m, declaredFields)) {
			return false;
		} else {
			return findField(m, inheritedFields);
		}

	}

	private boolean findMethod(MethodCall m, OJMethod[] methods) throws ParseTreeException {
		for (OJMethod im : methods) {
			String imName = im.getName();
			String mName = m.getName();
			if (compareNamesWithoutPackage(imName, mName)) {
				OJClass mType = getType(m);
				OJClass imType = im.getReturnType();
				ExpressionList mArgsTypes = m.getArguments();
				OJClass[] imArgsTypes = im.getParameterTypes();
				if (mArgsTypes.size() != imArgsTypes.length) {
					return false;
				}
				for (int t = 0; t < mArgsTypes.size(); t++) {
					OJClass mArg = getType(mArgsTypes.get(t));
					OJClass imArg = imArgsTypes[t];
					if (mArg.getName().compareTo(imArg.getName()) != 0) {
						return false;
					}
				}
				if (mType.getName().compareTo(imType.getName())==0) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isInherited(MethodCall m) throws ParseTreeException {
		OJMethod[] declaredMethods = getSelfType().getDeclaredMethods();
		OJMethod[] inheritedMethods = getInheritedMethods(getSelfType(), ALLOW_FINAL);
		if (findMethod(m, declaredMethods)) {
			return false;
		} else {
			return findMethod(m, inheritedMethods);
		}
	}

	private Expression fixStupidVariable(Variable v) throws ParseTreeException {
		Map<OJClass, List<Variable>> reachableVars = getReachableVariables(v.getParent());
		boolean found = false;
		OJClass vType = getType(v);
		for (List<Variable> vars : reachableVars.values()) {
			for (Variable var : vars) {
				String vName = v.toString();
				String varName = var.toString();
				if (vName.compareTo(varName)==0) {
					OJClass varType = getType(var);
					if (vType.getName().compareTo(varType.getName())==0) {
						found = true;
						break;
					}
				}
			}
			if (found) break;
		}
		if (found) {
			return v;
		} else {
			FieldAccess variableFixed = new FieldAccess((Expression)null, v.toString());
			variableFixed.setParent(v.getParent());
			return variableFixed;
		}
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
			//should never reach this point
			//throw an excepcion maybe
			return null;
		}
	}

	private String convertExpressionToString(Expression e, boolean recursive) {
		String conversion = "";
		Expression current = e;
		while (current != null) {
			if (current instanceof MethodCall) {
				conversion = ((MethodCall) current).getName()+'(' + ((MethodCall)current).getArguments().toString()+')' + conversion;
			} else if (current instanceof FieldAccess) {
				conversion = ((FieldAccess) current).getName() + conversion;
			} else if (current instanceof Variable) {
				conversion = ((Variable)current).toString() + conversion;
			} else if (current instanceof Literal) {
				conversion =  ((Literal)current).toString() + conversion;
			} else if (current instanceof SelfAccess) {
				conversion = "this" + conversion;
			}
			if (!recursive) break; 
			current = getPreviousExpression(current);
			if (current != null) conversion = '.' + conversion;
		}
		return conversion;
	}

	private OJClass expressionDeclaringClass(Expression e) throws ParseTreeException {
		if (e instanceof MethodCall) {
			return getType(((MethodCall) e).getReferenceExpr());
		} else if (e instanceof FieldAccess) {
			return getType(((FieldAccess) e).getReferenceExpr());
		} else if (e instanceof Variable) {
			return getType(e).getDeclaringClass();
		} else if (e instanceof Literal) {
			return null;
		} else {
			//this line should never be reached
			return null;
		}
	}

	private List<Object> fieldAndMethods(ParseTreeObject limit, Object elem, boolean forceIgnoreVars) throws ParseTreeException{
		boolean ignoreVars = forceIgnoreVars || limit == null;
		OJClass t = null;
		if (elem instanceof MethodCall || elem instanceof FieldAccess || elem instanceof Variable) {
			t = getType((Expression) elem);
			ignoreVars = true;
		} else if (elem instanceof OJMethod) {
			t = ((OJMethod)elem).getReturnType();
			ignoreVars = true;
		} else if (elem instanceof OJField) {
			t = ((OJField)elem).getType();
			ignoreVars = true;
		} else if (elem instanceof SelfAccess) {
			t = getType((Expression) elem);
			ignoreVars = true;
		} else if (elem == null) {
			t = getSelfType();
		}
		int options = 0;
		options += ignoreVars?0:VARIABLES;
		options += this.allowNonStatic?ALLOW_NON_STATIC:0;
		return fieldsMethodsAndVars(limit, t, options);
	}

	private java.util.List<Object> fieldAndMethods(Expression e, ParseTreeObject limit) throws ParseTreeException {
		OJClass t = null;
		boolean forceIgnoreVariables = false;
		if (e instanceof MethodCall || e instanceof FieldAccess || e instanceof Variable) {
			t = getType(e);
		} else if (e instanceof SelfAccess) {
			t = getType(e);
			forceIgnoreVariables = true;
		} else if (e == null) {
			t = getSelfType();
		}
		return fieldAndMethods(t,(ParseTreeObject) (forceIgnoreVariables?null:limit));
	}

	private java.util.List<Object> fieldAndMethods(OJClass t, ParseTreeObject limit) throws ParseTreeException {
		boolean forceIgnoreVariables = limit == null;
		java.util.List<Object> fnm = new LinkedList<Object>();
		if (t == null) {
			return fnm;
		}
		boolean addVariables = (t.getName().compareTo(getSelfType().getName()) == 0) && !forceIgnoreVariables;
		String publicCheck = "";
		boolean tPackageSameAsThisPackage = t.isInSamePackage(getSelfType());//t.getPackage().equals(getSelfType().getPackage());
		publicCheck += tPackageSameAsThisPackage?1:0;
		boolean tIsInnerClassOfThis = isInnerClassOf(getSelfType(), t, 0);
		publicCheck += tIsInnerClassOfThis?1:0;
		boolean canUseProtectedAndDefault = tPackageSameAsThisPackage || tIsInnerClassOfThis;
		publicCheck += canUseProtectedAndDefault?1:0;
		boolean onlyPublic = !canUseProtectedAndDefault;
		//boolean onlyPublic = t.getPackage() == null || !(t.getPackage().contains(getSelfType().getPackage()));
		if (this.fieldsAndMethodsPerClass.containsKey(t.getName()+addVariables+publicCheck)) {
			return this.fieldsAndMethodsPerClass.get(t.getName()+addVariables+publicCheck);
		}
		for (OJField f : t.getDeclaredFields()) {
			boolean allowNonStatic = (this.allowNonStatic && this.smartMode) || !this.smartMode;
			boolean isNonStatic = !f.getModifiers().isStatic();
			if (((onlyPublic && f.getModifiers().isPublic()) || !onlyPublic) && ((allowNonStatic && isNonStatic) || !isNonStatic)) {
				if (this.isFieldAllowed(f)) fnm.add(f);
			}
		}
		for (OJMethod m : t.getDeclaredMethods()) {
			boolean allowNonStatic = (this.allowNonStatic && this.smartMode) || !this.smartMode;
			boolean isNonStatic = !m.getModifiers().isStatic();
			if (m.getReturnType().getName().compareToIgnoreCase("void") == 0) continue;
			if ((m.signature().getParameterTypes().length == 0 && ((onlyPublic && m.getModifiers().isPublic())||(!onlyPublic))) && ((allowNonStatic && isNonStatic) || !isNonStatic)) {
				if (this.isMethodAllowed(m)) fnm.add(m);
			}
		}
		for (OJField inheritedField : t.getAllFields()) {
			boolean allowNonStatic = (this.allowNonStatic && this.smartMode) || !this.smartMode;
			boolean isNonStatic = !inheritedField.getModifiers().isStatic();
			if (((!inheritedField.getModifiers().isPrivate() && !onlyPublic) || inheritedField.getModifiers().isPublic() && onlyPublic) && ((allowNonStatic && isNonStatic) || !isNonStatic)) {
				if (!fnm.contains(inheritedField) && this.isFieldAllowed(inheritedField)) fnm.add(inheritedField);
			}
		}
		for (OJMethod inheritedMethod : t.getAllMethods()) {
			boolean allowNonStatic = (this.allowNonStatic && this.smartMode) || !this.smartMode;
			boolean isNonStatic = !inheritedMethod.getModifiers().isStatic();
			if (inheritedMethod.getReturnType().getName().compareToIgnoreCase("void") == 0) continue;
			if ((((inheritedMethod.getModifiers().isProtected() && !onlyPublic) || inheritedMethod.getModifiers().isPublic()) && inheritedMethod.signature().getParameterTypes().length == 0) && ((allowNonStatic && isNonStatic) || !isNonStatic)) {
				if (!fnm.contains(inheritedMethod) && this.isMethodAllowed(inheritedMethod)) fnm.add(inheritedMethod);
			}
		}
		if (addVariables) {
			if (this.smartMode) {
				Map<OJClass, List<Variable>> reachableVars = getReachableVariables(limit);
				for (Entry<OJClass, List<Variable>> vars : reachableVars.entrySet()) {
					for (Variable v : vars.getValue()) {
						if (!fnm.contains(v)) fnm.add(v);
					}
				}
			} else {
				ParseTreeObject lastStatement = getStatement(limit, LAST_STATEMENT);
				ParseTreeObject currentStatement = getStatement(limit);
				ParseTreeObject nextStatement = getStatement(limit, 1);
				Map<OJClass, List<Variable>> reachableVarsFromLastStatement = getReachableVariables(lastStatement);
				for (Entry<OJClass, List<Variable>> vars : reachableVarsFromLastStatement.entrySet()) {
					for (Variable v : vars.getValue()) {
						if (!fnm.contains(v)) fnm.add(v);
					}
				}
				Map<OJClass, List<Variable>> reachableVarsFromCurrentStatement = getReachableVariables(currentStatement);
				for (Entry<OJClass, List<Variable>> vars : reachableVarsFromCurrentStatement.entrySet()) {
					for (Variable v : vars.getValue()) {
						if (!fnm.contains(v)) fnm.add(v);
					}
				}
				Map<OJClass, List<Variable>> reachableVarsFromNextStatement = getReachableVariables(nextStatement);
				for (Entry<OJClass, List<Variable>> vars : reachableVarsFromNextStatement.entrySet()) {
					for (Variable v : vars.getValue()) {
						if (!fnm.contains(v)) fnm.add(v);
					}
				}
			}
		}
		this.fieldsAndMethodsPerClass.put(t.getName()+addVariables+publicCheck, fnm);
		return fnm;
	}

	private boolean isFieldMethodOf(OJMember e1, Expression e2) throws ParseTreeException {
		if (e2 instanceof Variable) return false;
		if (e2 instanceof Literal) return false;
		if (e2 instanceof SelfAccess) return false;
		OJClass e1c = null;
		if (e1 instanceof OJMethod) {
			e1c = ((OJMethod) e1).getReturnType();
		} else if (e1 instanceof OJField) {
			e1c = ((OJField) e1).getType();
		}
		if (e1c == null) return false;
		String e2Name = null;
		if (e2 instanceof MethodCall) e2Name = ((MethodCall) e2).getName();
		if (e2 instanceof FieldAccess) e2Name = ((FieldAccess) e2).getName();
		for (Object m : fieldAndMethods(e1c, null)) {
			if (m instanceof OJField || m instanceof OJMethod) {
				if (((OJMember) m).getName().compareTo(e2Name)==0) return true;
			}
		}
		return false;
	}

	private boolean isFieldMethodOf(Expression e1, Expression e2) throws ParseTreeException {
		if (e2 instanceof Variable) return false;
		if (e2 instanceof Literal) return false;
		if (e2 instanceof SelfAccess) return false;
		OJClass e1c = getType(e1);
		if (e1c == null) return false;
		String e2Name = null;
		if (e2 instanceof MethodCall) e2Name = ((MethodCall) e2).getName();
		if (e2 instanceof FieldAccess) e2Name = ((FieldAccess) e2).getName();
		for (Object m : fieldAndMethods(e1c, null)) {
			if (m instanceof OJField || m instanceof OJMethod) {
				if (((OJMember) m).getName().compareTo(e2Name)==0) return true;
			}
		}
		return false;
	}
	
	private boolean isFieldMethodOf(Object e1, Expression e2) throws ParseTreeException {
		if (e1 instanceof Expression) {
			return isFieldMethodOf((Expression)e1, e2);
		} else if (e1 instanceof OJMember) {
			return isFieldMethodOf((OJMember)e1, e2);
		} else {
			return false;
		}
	}

	/*
	 * This method will modify expressions like a = b
	 * if lor is set to true it will change a to x where x can be a variable or a field
	 * if lor is set to false it will channge b to y where y can be a variable, field, a method (with no parameters) or null
	 */
	private void sameLength(NonLeaf orig, Expression e1, Expression e2, boolean lor, boolean refined) throws ParseTreeException {
		OJClass ltype = getType(e1);
		OJClass rtype = getType(e2);
		boolean methodApplicable = (lor && !(e1 instanceof Literal)) || (!lor && (e2 instanceof Variable || e2 instanceof FieldAccess || e2 instanceof MethodCall || e2 instanceof Literal));
		if (methodApplicable && this.allowSameLengthMutations()) {
			Expression current = lor?e1:e2;
			Expression prev = null;
			Expression next = null;
			Expression rightPart = null;
			if (!lor && (e2 instanceof Variable) && compatibleAssignType(ltype, null) && this.allowLiteralNull() && ((refined && this.refModeAllowNullStack.peek()) || !refined)) outputToFile((ParseTreeObject)(lor?e1:e2), Literal.constantNull());
			do {
				prev = getPreviousExpression(current);
				java.util.List<Object> fnm = this.smartMode?fieldAndMethods(orig, prev, false):fieldAndMethods(prev, (ParseTreeObject) (lor?e1:e2));
				for (Object m : fnm) {
					boolean fieldSpecialCase = prev == null && m instanceof OJMember;
					if (!fieldSpecialCase && ((m instanceof OJMember && ((OJMember)m).getName().compareTo(convertExpressionToString(current,false))==0)
							|| m instanceof Variable && convertExpressionToString((Variable)m,false).compareTo(convertExpressionToString(current,false))==0)) {

						continue;
					}
					OJClass retType = null;
					if (m instanceof OJMethod) {
						if (lor && next == null) {
							continue;
						} else {
							retType = ((OJMethod) m).getReturnType();
						}
					} else if (m instanceof OJField) {
						retType = ((OJField) m).getType();
					} else if (m instanceof Variable) {
						retType = getType((Variable)m);
					}
					if (retType == null) {
						continue;
					}
					if (retType.isPrimitive() && next != null) continue;
					if (lor && prev==null && !getType(current).isPrimitive() && retType.isPrimitive()) continue;
					if ((next == null) && (lor?compatibleAssignType(retType, rtype):compatibleAssignType(ltype, retType, !refined))) {
						Expression prevCopy = prev==null?null:((Expression) prev.makeRecursiveCopy_keepOriginalID());
						ParseTreeObject mutantNode = null;
						if (m instanceof OJField) {
							FieldAccess mutantField = new FieldAccess(prevCopy==null?(this.isInherited((OJField)m)?(SelfAccess.constantSuper()):(SelfAccess.constantThis())):(prevCopy), ((OJField)m).getName());
							mutantField.setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
							if (isPrimitiveToObjectAssignment((lor?retType:ltype), (lor?rtype:retType))) {
								if (usePrimitiveWrapping() && !lor) {
									mutantNode = wrapPrimitiveExpression(retType, mutantField);
								} else if (allowPrimitiveToObjectMutants()) {
									mutantNode = mutantField;
								}
							} else {
								mutantNode = mutantField;
							}
							if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
						} else if (m instanceof OJMethod) {
							MethodCall mutantMethod = new MethodCall(prevCopy==null?(this.isInherited((OJMethod)m)?(SelfAccess.constantSuper()):(SelfAccess.constantThis())):(prevCopy), ((OJMethod)m).getName(), new ExpressionList());
							mutantMethod.setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
							if (isPrimitiveToObjectAssignment((lor?retType:ltype), (lor?rtype:retType))) {
								if (usePrimitiveWrapping() && !lor) {
									mutantNode = wrapPrimitiveExpression(retType, mutantMethod);
								} else if (allowPrimitiveToObjectMutants()) {
									mutantNode = mutantMethod;
								}
							} else {
								mutantNode = mutantMethod;
							}
							if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
						} else if (m instanceof Variable) {
							Variable mutantVar = (Variable) ((Variable)m).makeRecursiveCopy_keepOriginalID();
							mutantVar.setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
							if (isPrimitiveToObjectAssignment((lor?retType:ltype), (lor?rtype:retType))) {
								if (usePrimitiveWrapping() && !lor) {
									mutantNode = wrapPrimitiveExpression(retType, mutantVar);
								} else if (allowPrimitiveToObjectMutants()) {
									mutantNode = mutantVar;
								}
							} else {
								mutantNode = mutantVar;
							}
							if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
						}
					} else if ((next != null) && (!(m instanceof Variable && prev!=null) && (isFieldMethodOf(m, next)))) {
						Expression nextCopy = rightPart==null?null:((Expression) rightPart.makeRecursiveCopy_keepOriginalID());
						Expression prevCopy = prev==null?null:((Expression) prev.makeRecursiveCopy_keepOriginalID());
						Expression mutantCurrent = null;
						if (m instanceof OJField) {
							mutantCurrent = new FieldAccess(prevCopy==null?(this.isInherited((OJField)m)?(SelfAccess.constantSuper()):(SelfAccess.constantThis())):(prevCopy), ((OJField)m).getName());
						} else if (m instanceof OJMethod) {
							mutantCurrent = new MethodCall(prevCopy==null?(this.isInherited((OJMethod)m)?(SelfAccess.constantSuper()):(SelfAccess.constantThis())):(prevCopy), ((OJMethod)m).getName(), new ExpressionList());
						} else if (m instanceof Variable) {
							mutantCurrent = (Variable)m;
						}
						((ParseTreeObject) mutantCurrent).setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
						if (nextCopy == null) {
							ParseTreeObject mutantNode = null;
							if (isPrimitiveToObjectAssignment((lor?retType:ltype), (lor?rtype:retType))) {
								if (usePrimitiveWrapping() && !lor) {
									mutantNode = wrapPrimitiveExpression(retType, (ParseTreeObject) mutantCurrent);
								} else if (allowPrimitiveToObjectMutants()) {
									mutantNode = (ParseTreeObject) mutantCurrent;
								}
							} else {
								mutantNode = (ParseTreeObject) mutantCurrent;
							}
							if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
						} else {
							ParseTreeObject mutant = (ParseTreeObject) addThisSuper(append(nextCopy, mutantCurrent));
							ParseTreeObject mutantNode = null;
							if (isPrimitiveToObjectAssignment(ltype, rtype)) {
								if (usePrimitiveWrapping() && !lor) {
									mutantNode = wrapPrimitiveExpression(rtype, mutant);
								} else if (allowPrimitiveToObjectMutants()) {
									mutantNode = mutant;
								}
							} else {
								mutantNode = mutant;
							}
							if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
						}	
					}
				}
				if (rightPart != null) {
					Expression currentNode = (Expression) current.makeRecursiveCopy_keepOriginalID();
					removeReferencedExpr(currentNode);
					rightPart = append(rightPart, currentNode);
				} else {
					rightPart = (Expression) current.makeRecursiveCopy_keepOriginalID();
					removeReferencedExpr(rightPart);
				}
				next = current;
				current = getPreviousExpression(current);
			} while (current != null);
		}
	}

	/*
	 * This method will modify expresions like a = b
	 * if lor is set to true and a it's an expression with lenght greater than 1 then it will decrease the expression lenght by 1
	 * the same goes for lor set to false and b an expression with lenght greater than 1
	 */
	private void decreaseLenght(NonLeaf orig, Expression e1, Expression e2, boolean lor) throws ParseTreeException {
		//check conditions
		OJClass ltype = getType(e1);
		OJClass rtype = getType(e2);
		boolean methodIsApplicable = ((lor && ((e1 instanceof FieldAccess) || (e1 instanceof MethodCall))) || (!lor && ((e2 instanceof FieldAccess) || (e2 instanceof MethodCall)))); 
		if (methodIsApplicable && this.allowDecreaseLengthMutations()) {
			//decrease by 1
			Expression current = lor?e1:e2; //e1 it's an expression like xs.x, current will first take x value and try to eliminate it and then move throug xs doing the same
			Expression prev = null;
			Expression next = null;
			Expression rightPart = null;
			while (current != null) {
				if (current instanceof SelfAccess && next != null) {
					if (((SelfAccess)current).isSuperAccess()) {
						if (next instanceof FieldAccess) {
							if (!isInherited((FieldAccess)next)) return;
						} else if (next instanceof MethodCall) {
							if (!isInherited((MethodCall)next)) return;
						}
					}
				}
				prev = getPreviousExpression(current);
				if (next == null) {
					boolean leftEndCheck = (lor && !(prev instanceof MethodCall)) || !lor;
					boolean prevIsNotNull = prev != null;
					if (prevIsNotNull && leftEndCheck && compatibleAssignType(ltype, getType(prev)) && compatibleAssignType(getType(prev), rtype)) {
						Expression prevCopy = (Expression) prev.makeRecursiveCopy_keepOriginalID();
						ParseTreeObject mutantNode = null;
						if (isPrimitiveToObjectAssignment((lor?getType(prev):ltype), (lor?rtype:getType(prev)))) {
							if (usePrimitiveWrapping() && !lor) {
								mutantNode = wrapPrimitiveExpression(getType(prev), (ParseTreeObject) prevCopy);
							} else if (allowPrimitiveToObjectMutants()) {
								mutantNode = (ParseTreeObject) prevCopy;
							}
						} else {
							mutantNode = (ParseTreeObject) prevCopy;
						}
						if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
					}
				} else {
					OJClass prevType = prev == null?null:getType(prev);
					OJClass nextPrevType = expressionDeclaringClass(next);
					if (nextPrevType != null) {
						if (compatibleAssignType(prevType==null?getSelfType():prevType, nextPrevType)) {
							if (prev == null) {
								boolean thisCheck = true;
								if (this.smartMode && current instanceof SelfAccess && !((SelfAccess)current).isSuperAccess()) {
									Map<OJClass, List<Variable>> reachableVars = getReachableVariables(orig);
									for (List<Variable> vars : reachableVars.values()) {
										for (Variable var : vars) {
											if (var.toString().compareTo(current.toString())==0) {
												thisCheck = getType(var).getName().compareTo(current.toString())==0;
												break;
											}
										}
									}
								}
								if (!thisCheck) continue;
								Expression nextCopy = (Expression) rightPart.makeRecursiveCopy_keepOriginalID();
								ParseTreeObject mutantNode = null;
								if (isPrimitiveToObjectAssignment(ltype, rtype)) {
									if (usePrimitiveWrapping() && !lor) {
										mutantNode = wrapPrimitiveExpression(rtype, (ParseTreeObject) nextCopy);
									} else if (allowPrimitiveToObjectMutants()) {
										mutantNode = (ParseTreeObject) nextCopy;
									}
								} else {
									mutantNode = (ParseTreeObject) nextCopy;
								}
								if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
							} else {
								Expression nextCopy = (Expression) rightPart.makeRecursiveCopy_keepOriginalID();
								Expression prevCopy = (Expression) prev.makeRecursiveCopy_keepOriginalID();
								if (nextCopy instanceof FieldAccess) {
									((FieldAccess)nextCopy).setReferenceExpr(prevCopy);
								} else if (nextCopy instanceof MethodCall) {
									((MethodCall)nextCopy).setReferenceExpr(prevCopy);
								}
								ParseTreeObject mutantNode = null;
								if (isPrimitiveToObjectAssignment(ltype, rtype)) {
									if (usePrimitiveWrapping() && !lor) {
										mutantNode = wrapPrimitiveExpression(rtype, (ParseTreeObject) nextCopy);
									} else if (allowPrimitiveToObjectMutants()) {
										mutantNode = (ParseTreeObject) nextCopy;
									}
								} else {
									mutantNode = (ParseTreeObject) nextCopy;
								}
								if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
							}
						}
					} else {
						//this line should never be reached
						//throw an excepcion maybe
					}
				}
				if (rightPart != null && current != null) {
					Expression currentNode = (Expression) current.makeRecursiveCopy_keepOriginalID();
					removeReferencedExpr(currentNode);
					rightPart = append(rightPart, currentNode);
				} else if (rightPart == null) {
					rightPart = (Expression) current.makeRecursiveCopy_keepOriginalID();
					removeReferencedExpr(rightPart);
				}
				next = current;
				current = getPreviousExpression(current);
			}
		}

	}

	private boolean allowSuper() {
		if (Configuration.argumentExist(ENABLE_SUPER)) {
			return (Boolean) Configuration.getValue(ENABLE_SUPER);
		}
		return true;
	}

	private boolean allowThis() {
		if (Configuration.argumentExist(ENABLE_THIS)) {
			return (Boolean) Configuration.getValue(ENABLE_THIS);
		}
		return true;
	}
	
	private boolean allowReplacementWithLiterals() {
		if (Configuration.argumentExist(ENABLE_REPLACEMENT_WITH_LITERALS)) {
			return (Boolean) Configuration.getValue(ENABLE_REPLACEMENT_WITH_LITERALS);
		}
		return true;
	}

	private boolean allowLiteralOne() {
		if (Configuration.argumentExist(ENABLE_LITERAL_ONE)) {
			return (Boolean) Configuration.getValue(ENABLE_LITERAL_ONE);
		}
		return true;
	}

	private boolean allowLiteralZero() {
		if (Configuration.argumentExist(ENABLE_LITERAL_ZERO)) {
			return (Boolean) Configuration.getValue(ENABLE_LITERAL_ZERO);
		}
		return true;
	}

	private boolean allowLiteralFalse() {
		if (Configuration.argumentExist(ENABLE_LITERAL_FALSE)) {
			return (Boolean) Configuration.getValue(ENABLE_LITERAL_FALSE);
		}
		return true;
	}

	private boolean allowLiteralTrue() {
		if (Configuration.argumentExist(ENABLE_LITERAL_TRUE)) {
			return (Boolean) Configuration.getValue(ENABLE_LITERAL_TRUE);
		}
		return true;
	}

	private boolean allowLiteralNull() {
		if (Configuration.argumentExist(ENABLE_LITERAL_NULL)) {
			return (Boolean) Configuration.getValue(ENABLE_LITERAL_NULL);
		}
		return true;
	}

	private boolean allowLiteralEmptyString() {
		if (!allowLiteralStrings()) {
			return false;
		}
		if (Configuration.argumentExist(ENABLE_LITERAL_EMPTY_STRING)) {
			return (Boolean) Configuration.getValue(ENABLE_LITERAL_EMPTY_STRING);
		}
		return true;
	}

	private boolean allowLiteralStrings() {
		if (Configuration.argumentExist(ENABLE_LITERAL_STRINGS)) {
			return (Boolean) Configuration.getValue(ENABLE_LITERAL_STRINGS);
		}
		return true;
	}
	
	private boolean generateTwoByOneMutations() {
		if (Configuration.argumentExist(ENABLE_TWO_BY_ONE_MUTANTS)) {
			return (Boolean) Configuration.getValue(ENABLE_TWO_BY_ONE_MUTANTS);
		}
		return true;
	}
	
	private boolean allowSameLengthMutations() {
		if (Configuration.argumentExist(ENABLE_SAME_LENGTH_MUTANTS)) {
			return (Boolean) Configuration.getValue(ENABLE_SAME_LENGTH_MUTANTS);
		}
		return true;
	}
	
	private boolean allowIncreaseLengthMutations() {
		if (Configuration.argumentExist(ENABLE_INCREASE_LENGTH_MUTANTS)) {
			return (Boolean) Configuration.getValue(ENABLE_INCREASE_LENGTH_MUTANTS);
		}
		return true;
	}
	
	private boolean allowDecreaseLengthMutations() {
		if (Configuration.argumentExist(ENABLE_DECREASE_LENGTH_MUTANTS)) {
			return (Boolean) Configuration.getValue(ENABLE_DECREASE_LENGTH_MUTANTS);
		}
		return true;
	}
	
	private boolean allowOneByTwoMutations() {
		if (Configuration.argumentExist(ENABLE_ONE_BY_TWO_MUTANTS)) {
			return (Boolean) Configuration.getValue(ENABLE_ONE_BY_TWO_MUTANTS);
		}
		return true;
	}
	
	private boolean allowAllByOneMutantsRight() {
		if (Configuration.argumentExist(ENABLE_ALL_BY_ONE_MUTANTS_RIGHT)) {
			return (Boolean) Configuration.getValue(ENABLE_ALL_BY_ONE_MUTANTS_RIGHT);
		}
		return false;
	}
	
	private boolean allowAllByOneMutantsLeft() {
		if (Configuration.argumentExist(ENABLE_ALL_BY_ONE_MUTANTS_LEFT)) {
			return (Boolean) Configuration.getValue(ENABLE_ALL_BY_ONE_MUTANTS_LEFT);
		}
		return false;
	}
	
	private boolean allowNumberLiteralsVariations() {
		if (Configuration.argumentExist(ENABLE_NUMBER_LITERALS_VARIATIONS)) {
			return (Boolean) Configuration.getValue(ENABLE_NUMBER_LITERALS_VARIATIONS);
		}
		return false;
	}
	
	private boolean usePrimitiveWrapping() {
		if (Configuration.argumentExist(ENABLE_PRIMITIVE_WRAPPING)) {
			return (Boolean) Configuration.getValue(ENABLE_PRIMITIVE_WRAPPING);
		}
		return false;
	}
	
	private boolean allowPrimitiveToObjectMutants() {
		if (Configuration.argumentExist(ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS)) {
			return (Boolean) Configuration.getValue(ENABLE_PRIMITIVE_TO_OBJECT_ASSIGNMENTS);
		}
		return true;
	}

	private Expression addThisSuper(Expression exp) throws ParseTreeException {
		if (exp == null) return null;
		if (!allowSuper() && !allowThis()) return exp;
		Expression modified = (Expression) exp.makeRecursiveCopy_keepOriginalID();
		Expression current = modified;
		while (getPreviousExpression(current) != null) {
			current = getPreviousExpression(current);
		}
		if (current instanceof Variable) {
			current = fixStupidVariable((Variable) current);
		}
		if (current instanceof FieldAccess) {
			boolean isSuper = isInherited((FieldAccess)current);
			boolean isThis = !isSuper;
			if (allowSuper() && isSuper || allowThis() && isThis) {
				modified = append(modified, isSuper?SelfAccess.constantSuper():SelfAccess.constantThis());
			}
		} else if (current instanceof MethodCall) {
			boolean isSuper = isInherited((MethodCall)current);
			boolean isThis = !isSuper;
			if (allowSuper() && isSuper || allowThis() && isThis) {
				modified = append(modified, isSuper?SelfAccess.constantSuper():SelfAccess.constantThis());
			}
		}
		return modified;
	}

	private Expression removeSuperOrThis(Expression exp, boolean remSuper) throws ParseTreeException {
		if (exp == null) return null;
		Expression modified = (Expression) exp.makeCopy_keepOriginalID();
		Expression current = modified;
		while (getPreviousExpression(current) != null && !(getPreviousExpression(current) instanceof SelfAccess)) {
			current = getPreviousExpression(current);
		}
		if (current instanceof Variable) {
			current = fixStupidVariable((Variable) current);
		}
		if (getPreviousExpression(current) instanceof SelfAccess) {
			boolean remove = (remSuper && ((SelfAccess) getPreviousExpression(current)).isSuperAccess()) || (!remSuper && !((SelfAccess) getPreviousExpression(current)).isSuperAccess());
			if (remove) {
				if (current instanceof FieldAccess) {
					((FieldAccess)current).setReferenceExpr(null);
				} else if (current instanceof MethodCall) {
					((MethodCall)current).setReferenceExpr(null);
				}
			}
		}
		return modified;
	}

	/*
	 * This method will modify expresions like a = b with a' and b', each with a lenght equals to |a|+1 and |b|+1 respectively
	 */
	private void increaseLenght(NonLeaf orig, Expression e1, Expression e2, boolean lor) throws ParseTreeException {
		OJClass ltype = getType(e1);
		OJClass rtype = getType(e2);
		boolean methodIsApplicableLor = !(e1 instanceof Literal) && (e1 instanceof Variable) || (e1 instanceof MethodCall) || (e1 instanceof FieldAccess);
		boolean methodIsApplicableNLor = !(e2 instanceof Literal) && (e2 instanceof Variable) || (e2 instanceof MethodCall) || (e2 instanceof FieldAccess);
		boolean methodIsApplicable = (lor && methodIsApplicableLor) || (!lor && methodIsApplicableNLor);
		if (methodIsApplicable && this.allowIncreaseLengthMutations()) {
			Expression current = lor?e1:e2;
			Expression next = null;
			Expression rightPart = null;
			boolean stop = false;
			do {
				stop = current == null;
				java.util.List<Object> fnm = this.smartMode?fieldAndMethods(orig, current, false):fieldAndMethods(current, (ParseTreeObject) (lor?e1:e2));
				for (Object m : fnm) {
					OJClass retType = null;
					boolean nextTypeCheck = false;
					boolean retTypeCheck = false;
					boolean isOJMember = false;
					boolean methodCheck = false;
					if (m instanceof OJMethod) {
						isOJMember = true;
						retType = ((OJMethod) m).getReturnType();
					} else if (m instanceof OJField) {
						isOJMember = true;
						retType = ((OJField) m).getType();
					} else if (m instanceof Variable) {
						retType = getType((Variable)m);
					}
					if (next == null) {
						nextTypeCheck = true;
						methodCheck = !(m instanceof OJMethod) || !lor;
						retTypeCheck = lor? compatibleAssignType(retType, rtype):compatibleAssignType(ltype, retType);
					} else {
						methodCheck = true;
						nextTypeCheck = (isOJMember && isFieldMethodOf((OJMember)m,next)) || (!isOJMember && isFieldMethodOf((Variable)m, next));
						retTypeCheck = true;
					}
					if (nextTypeCheck && retTypeCheck && methodCheck) {
						Expression currentCopy = current==null?null:((Expression)current.makeRecursiveCopy_keepOriginalID());
						Expression nextCopy = rightPart==null?null:((Expression)rightPart.makeRecursiveCopy_keepOriginalID());
						Expression mutant = null;
						if (m instanceof OJMethod) {
							mutant = new MethodCall(currentCopy, ((OJMethod)m).getName(), new ExpressionList());
						} else if (m instanceof OJField) {
							mutant = new FieldAccess(currentCopy, ((OJField)m).getName());
						} else if (m instanceof Variable) {
							mutant = (Variable) ((Variable)m).makeRecursiveCopy_keepOriginalID();
						}
						((ParseTreeObject)mutant).setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
						if (nextCopy != null) {
							ParseTreeObject mutantPTO = (ParseTreeObject) addThisSuper(append(nextCopy, mutant));
							ParseTreeObject mutantNode = null;
							if (isPrimitiveToObjectAssignment(ltype, rtype)) {
								if (usePrimitiveWrapping() && !lor) {
									mutantNode = wrapPrimitiveExpression(rtype, mutantPTO);
								} else if (allowPrimitiveToObjectMutants()) {
									mutantNode = mutantPTO;
								}
							} else {
								mutantNode = mutantPTO;
							}
							if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
						} else {
							ParseTreeObject mutantNode = null;
							ParseTreeObject mutantToWrite = (ParseTreeObject)addThisSuper(mutant);
							if (isPrimitiveToObjectAssignment((lor?retType:ltype), (lor?rtype:retType))) {
								if (usePrimitiveWrapping() && !lor) {
									mutantNode = wrapPrimitiveExpression(rtype, mutantToWrite);
								} else if (allowPrimitiveToObjectMutants()) {
									mutantNode = mutantToWrite;
								}
							} else {
								mutantNode = mutantToWrite;
							}
							if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
						}

					}
				}
				if (rightPart != null && current != null) {
					Expression currentNode = (Expression) current.makeRecursiveCopy_keepOriginalID();
					removeReferencedExpr(currentNode);
					rightPart = append(rightPart, currentNode);
				} else if (rightPart == null) {
					rightPart = (Expression) current.makeRecursiveCopy_keepOriginalID();
					removeReferencedExpr(rightPart);
				}
				next = current;
				current = getPreviousExpression(current);
			} while (!stop);
		}
	}

	/*
	 * Replace chains of lenght 2 by chains with lenght 1
	 */
	private void replaceTwoByOne(NonLeaf orig, Expression e1, Expression e2, boolean lor) throws ParseTreeException {
		OJClass ltype = getType(e1);
		OJClass rtype = getType(e2);
		boolean methodIsApplicable = (lor && !((e1 instanceof Literal) || (e1 instanceof Variable))) || ((!lor && (e2 instanceof FieldAccess || e2 instanceof MethodCall)));
		if (methodIsApplicable && this.generateTwoByOneMutations()) {
			Expression current = lor?e1:e2;
			Expression next = null;
			Expression prev = null;
			Expression rightPart = null;
			do {
				prev = getPreviousExpression(current);
				Expression prevPrev = getPreviousExpression(prev);
				java.util.List<Object> fnm = this.smartMode?fieldAndMethods(orig, prevPrev, false):fieldAndMethods(prevPrev, (ParseTreeObject) (lor?e1:e2));
				for (Object m : fnm) {
					OJClass retType = null;
					if (m instanceof OJMethod) {
						if (lor && next == null) continue;
						retType = ((OJMethod) m).getReturnType();
					} else if (m instanceof OJField) {
						retType = ((OJField) m).getType();
					} else if (m instanceof Variable) {
						retType = getType((Variable)m);
					}
					if (retType == null && next == null) continue;
					boolean typeCheck = false;
					boolean pertCheck = false;
					boolean primitiveCheck = true;
					if (next == null) {
						boolean ltypeCheck = true;
						pertCheck = true;
						typeCheck = lor?ltypeCheck&&compatibleAssignType(retType, rtype):compatibleAssignType(ltype, retType);
					} else {
						typeCheck = true;
						pertCheck = (m instanceof OJMember)?isFieldMethodOf((OJMember)m,next):((m instanceof Variable)?isFieldMethodOf((Variable)m, next):false);
					}
					if (pertCheck && typeCheck && primitiveCheck) {
						Expression prevPrevCopy = prevPrev==null?null:((Expression)prevPrev.makeRecursiveCopy_keepOriginalID());
						Expression nextCopy = rightPart==null?null:((Expression)rightPart.makeRecursiveCopy_keepOriginalID());
						Expression mutant = null;
						if (m instanceof OJField) {
							mutant = new FieldAccess(prevPrevCopy==null?(this.isInherited((OJField)m)?(SelfAccess.constantSuper()):(SelfAccess.constantThis())):(prevPrevCopy), ((OJField)m).getName());
						} else if (m instanceof OJMethod) {
							mutant = new MethodCall(prevPrevCopy==null?(this.isInherited((OJMethod)m)?(SelfAccess.constantSuper()):(SelfAccess.constantThis())):(prevPrevCopy), ((OJMethod)m).getName(), new ExpressionList());
						} else if (m instanceof Variable) {
							mutant = (Variable) ((Variable)m).makeRecursiveCopy_keepOriginalID();
						}
						((ParseTreeObject)mutant).setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
						if (nextCopy != null) {
							if (nextCopy instanceof FieldAccess) {
								((FieldAccess)nextCopy).setReferenceExpr(mutant);
							} else if (nextCopy instanceof MethodCall) {
								((MethodCall)nextCopy).setReferenceExpr(mutant);
							}
							ParseTreeObject mutantNode = null;
							if (isPrimitiveToObjectAssignment(ltype, rtype)) {
								if (usePrimitiveWrapping() && !lor) {
									mutantNode = wrapPrimitiveExpression(rtype, (ParseTreeObject) nextCopy);
								} else if (allowPrimitiveToObjectMutants()) {
									mutantNode = (ParseTreeObject) nextCopy;
								}
							} else {
								mutantNode = (ParseTreeObject) nextCopy;
							}
							if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
						} else {
							ParseTreeObject mutantNode = null;
							if (isPrimitiveToObjectAssignment((lor?retType:ltype), (lor?rtype:retType))) {
								if (usePrimitiveWrapping() && !lor) {
									mutantNode = wrapPrimitiveExpression(retType, (ParseTreeObject) mutant);
								} else if (allowPrimitiveToObjectMutants()) {
									mutantNode = (ParseTreeObject) mutant;
								}
							} else {
								mutantNode = (ParseTreeObject) mutant;
							}
							if (mutantNode != null) outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
						}
					}
				}
				if (rightPart != null) {
					rightPart = append(rightPart, current);
				} else {
					rightPart = (Expression) current.makeRecursiveCopy_keepOriginalID();
					if (rightPart instanceof FieldAccess) {
						((FieldAccess)rightPart).setReferenceExpr(null);
					} else if (rightPart instanceof MethodCall) {
						((MethodCall)rightPart).setReferenceExpr(null);
					}
				}
				next = current;
				current = getPreviousExpression(current);
			} while (current != null && !(current instanceof Variable) && !(current instanceof SelfAccess) );
		}
	}

	private void replaceAllByOne(NonLeaf orig, Expression e1, Expression e2, boolean lor) throws ParseTreeException {
		OJClass ltype = getType(e1);
		OJClass rtype = getType(e1);
		boolean methodIsApplicableLeft = false;
		if (lor && this.allowAllByOneMutantsLeft()) {
			methodIsApplicableLeft = this.canGetPrev(e1);
		}
		boolean methodIsApplicableRight = false;
		if (!lor && this.allowAllByOneMutantsRight()) {
			methodIsApplicableRight = this.canGetPrev(e2);
		}
		boolean methodIsApplicable = methodIsApplicableLeft || methodIsApplicableRight;
		if (methodIsApplicable) {
			java.util.List<Object> fnm = this.smartMode?fieldAndMethods(orig, null, false):fieldAndMethods((Expression)null, (ParseTreeObject) (lor?e1:e2));
			if (!lor) {
				this.refModeAllowNullStack.push(Boolean.TRUE);
				searchForLiteralsInMethod((ParseTreeObject) orig);
				this.refModeAllowNullStack.pop();
				fnm.addAll(this.literals);
			}
			for (Object replacement : fnm) {
				if (replacement instanceof Literal) {
					if (!lor) {
						OJClass typeToComply = ltype;
						OJClass litType = getType((Literal)replacement);
						if (!compatibleAssignType(typeToComply, litType, true)) {
							continue;
						} else {
							((ParseTreeObject)replacement).setParent(((ParseTreeObject)e2).getParent());
							ParseTreeObject mutantNode = null;
							if (isPrimitiveToObjectAssignment(ltype, litType)) {
								if (usePrimitiveWrapping() && !lor) {
									mutantNode = wrapPrimitiveExpression(litType, (ParseTreeObject) replacement);
								} else if (allowPrimitiveToObjectMutants()) {
									mutantNode = (ParseTreeObject) replacement;
								}
							} else {
								mutantNode = (ParseTreeObject) replacement;
							}
							if (mutantNode != null) outputToFile((ParseTreeObject) e2, mutantNode);
						}
					} else {
						continue;
					}
				} else if (replacement instanceof Variable) {
					OJClass varType = getType((Variable)replacement);
					((ParseTreeObject)replacement).setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
					if (!lor && compatibleAssignType(ltype, varType, true)) {
						ParseTreeObject mutantNode = null;
						if (isPrimitiveToObjectAssignment(ltype, varType)) {
							if (usePrimitiveWrapping()) {
								mutantNode = wrapPrimitiveExpression(varType, (ParseTreeObject) replacement);
							} else if (allowPrimitiveToObjectMutants()) {
								mutantNode = (ParseTreeObject) replacement;
							}
						} else {
							mutantNode = (ParseTreeObject) replacement;
						}
						if (mutantNode != null) outputToFile((ParseTreeObject)(e2), mutantNode);
					} else if (lor && compatibleAssignType(varType, rtype, true)) {
						if (isPrimitiveToObjectAssignment(varType, rtype) && !usePrimitiveWrapping()) {
							continue;
						}
						outputToFile((ParseTreeObject)(e1), (Variable)replacement);
					} else {
						continue;
					}
				} else if (replacement instanceof OJField) {
					OJClass fieldType = ((OJField)replacement).getType();
					FieldAccess mutant = new FieldAccess((Expression)null, ((OJField)replacement).getName());
					if (!lor && compatibleAssignType(ltype, fieldType, true)) {
						((ParseTreeObject)mutant).setParent(((ParseTreeObject)e2).getParent());
						ParseTreeObject mutantNode = (ParseTreeObject) addThisSuper(mutant);
						if (isPrimitiveToObjectAssignment(ltype, fieldType)) {
							if (usePrimitiveWrapping()) {
								mutantNode = wrapPrimitiveExpression(fieldType, mutantNode);
							} else if (!allowPrimitiveToObjectMutants()) {
								continue;
							}
						}
						outputToFile((ParseTreeObject)(e2), mutantNode);
					} else if (lor && compatibleAssignType(fieldType, rtype, true)) {
						((ParseTreeObject)mutant).setParent(((ParseTreeObject)e1).getParent());
						if (isPrimitiveToObjectAssignment(fieldType, rtype) && !usePrimitiveWrapping()) {
							continue;
						}
						outputToFile((ParseTreeObject)(e1), (ParseTreeObject) addThisSuper(mutant));
					} else {
						continue;
					}
				} else if (replacement instanceof OJMethod && !lor) {
					OJClass methodType = ((OJMethod)replacement).getReturnType();
					if (compatibleAssignType(ltype, methodType, true)) {
						MethodCall mutant = new MethodCall((Expression)null, ((OJMethod)replacement).getName(), new ExpressionList());
						((ParseTreeObject)mutant).setParent(((ParseTreeObject)e2).getParent());
						ParseTreeObject mutantNode = (ParseTreeObject) addThisSuper(mutant);
						if (isPrimitiveToObjectAssignment(ltype, methodType)) {
							if (usePrimitiveWrapping()) {
								mutantNode = wrapPrimitiveExpression(methodType, mutantNode);
							} else if (!allowPrimitiveToObjectMutants()) {
								continue;
							}
						}
						outputToFile((ParseTreeObject)e2, mutantNode);
					} else {
						continue;
					}
				} else {
					continue;
				}
			}
		}
	}

	private void replaceOneByTwo(NonLeaf orig, Expression e1, Expression e2, boolean lor) throws ParseTreeException {
		OJClass ltype = getType(e1);
		OJClass rtype = getType(e2);
		boolean methodIsApplicable = (lor && !(e1 instanceof Literal) && !ltype.isPrimitive()) || (!lor && (e2 instanceof Variable || e2 instanceof FieldAccess || e2 instanceof MethodCall) && !rtype.isPrimitive());
		if (methodIsApplicable && this.allowOneByTwoMutations()) {
			Expression current = lor?e1:e2;
			Expression next = null;
			Expression prev = null;
			Expression rightPart = null;
			do {
				prev = getPreviousExpression(current);
				java.util.List<Object> fnm = this.smartMode?fieldAndMethods(orig, prev, false):fieldAndMethods(prev, (ParseTreeObject) (lor?e1:e2));
				for (Object m : fnm) {
					java.util.List<Object> fnm2 = new LinkedList<Object>();
					if (this.smartMode) {
						fnm2 = fieldAndMethods(null, m, true);
					} else {
						fnm2 = (m instanceof OJMethod)?fieldAndMethods(((OJMethod)m).getReturnType(),null):(m instanceof OJField)?fieldAndMethods(((OJField)m).getType(),null):(m instanceof Variable)?fieldAndMethods((Variable)m, (ParseTreeObject) (lor?e1:e2)):fnm2;
					}
					for (Object m2 : fnm2) {
						OJClass retType = null;
						if (m2 instanceof OJMethod) {
							if (lor && next == null) {
								retType = null;
							} else {
								retType = ((OJMethod) m2).getReturnType();
							}
						} else if (m2 instanceof OJField) {
							retType = ((OJField) m2).getType();
						}
						if (retType == null) {
							continue;
						}
						Expression mutantPart1 = null;
						Expression mutantPart2 = null;
						Expression prevCopy = prev == null?null:((Expression)prev.makeRecursiveCopy_keepOriginalID());
						Expression nextCopy = rightPart == null?null:((Expression)rightPart.makeRecursiveCopy_keepOriginalID());
						boolean addThis = prev == null;
						if (m instanceof OJField) {
							mutantPart1 = new FieldAccess(addThis?(isInherited((OJField) m)?SelfAccess.constantSuper():SelfAccess.constantThis()):prevCopy, ((OJField)m).getName());
						} else if (m instanceof OJMethod) {
							mutantPart1 = new MethodCall(addThis?(isInherited((OJMethod) m)?SelfAccess.constantSuper():SelfAccess.constantThis()):prevCopy, ((OJMethod)m).getName(), new ExpressionList());
						} else if (m instanceof Variable) {
							mutantPart1 = (Variable) ((Variable)m).makeRecursiveCopy_keepOriginalID();
							addThis = false;
						}
						if (m2 instanceof OJField) {
							mutantPart2 = new FieldAccess(mutantPart1, ((OJField)m2).getName());
						} else if (m2 instanceof OJMethod) {
							mutantPart2 = new MethodCall(mutantPart1, ((OJMethod)m2).getName(), new ExpressionList());
						}
						((ParseTreeObject)mutantPart1).setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
						if (next == null) {
							boolean ltypeCheck = (lor && (prev==null?(getType(current).isPrimitive()?retType.isPrimitive():!retType.isPrimitive()):true)) || !lor;
							if ((lor && (ltypeCheck && compatibleAssignType(retType, rtype))) || (!lor && (compatibleAssignType(ltype, retType)))) {
								ParseTreeObject mutantNode = (ParseTreeObject)mutantPart2;
								if (isPrimitiveToObjectAssignment((lor?retType:ltype), (lor?rtype:retType))) {
									if (usePrimitiveWrapping() && !lor) {
										mutantNode = wrapPrimitiveExpression(retType, mutantNode);
									} else if (!allowPrimitiveToObjectMutants()) {
										continue;
									}
								}
								outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
							}
						} else {
							if (isFieldMethodOf((OJMember)m2,next)){
								if (nextCopy instanceof FieldAccess) {
									((FieldAccess)nextCopy).setReferenceExpr(mutantPart2);
								} else if (nextCopy instanceof MethodCall) {
									((MethodCall)nextCopy).setReferenceExpr(mutantPart2);
								}
								ParseTreeObject mutantNode = (ParseTreeObject)nextCopy;
								if (isPrimitiveToObjectAssignment(ltype, rtype)) {
									if (usePrimitiveWrapping() && !lor) {
										mutantNode = wrapPrimitiveExpression(rtype, mutantNode);
									} else if (!allowPrimitiveToObjectMutants()) {
										continue;
									}
								}
								outputToFile((ParseTreeObject)(lor?e1:e2), mutantNode);
							}
						}
					}
				}
				if (rightPart != null) {
					rightPart = append(rightPart, current);
				} else {
					rightPart = (Expression) current.makeRecursiveCopy_keepOriginalID();
					if (rightPart instanceof FieldAccess) {
						((FieldAccess)rightPart).setReferenceExpr(null);
					} else if (rightPart instanceof MethodCall) {
						((MethodCall)rightPart).setReferenceExpr(null);
					}
				}
				next = current;
				current = getPreviousExpression(current);
			} while (current != null);
		}
	}

	private void binaryVisit(NonLeaf orig, Expression e1, Expression e2, boolean lor) throws ParseTreeException {
		sameLength(orig, e1, e2, lor, false);
		decreaseLenght(orig, e1, e2, lor);
		increaseLenght(orig, e1, e2, lor);
		replaceTwoByOne(orig, e1, e2, lor);
		replaceOneByTwo(orig, e1, e2, lor);
		if (orig instanceof AssignmentExpression) {
			replaceAllByOne(orig, e1, e2, lor);
		}
	}

	private boolean binExprSupportsNull(int operator) {
		boolean supportsNull;
		switch (operator) {
		case BinaryExpression.EQUAL : {supportsNull = true; break;}
		case BinaryExpression.NOTEQUAL : {supportsNull = true; break;}
		default : {supportsNull = false; break;}
		}
		return supportsNull;
	}

	//=========================VISIT METHODS================================

	public void visit(MethodDeclaration md) throws ParseTreeException {
		if (Api.usingApi() && (!Api.insideClassToMutate() || !md.getName().equals(Api.getMethodUnderConsideration()))) {
			return;
		} else {
			if (md.getModifiers().contains(ModifierList.STATIC)) {
				this.allowNonStatic = false;
			}
			this.justEvaluating = true;
			Api.disableClassesVerification();
			super.visit(md);
			this.justEvaluating = false;
			Api.enableClassesVerification();
			super.visit(md);
		}
	}

	public void visit(StatementList p) throws ParseTreeException {
		for (int s = 0; s < p.size(); s++) {
			p.get(s).accept(this);
		}
	}

	@Override
	public Statement evaluateUp(WhileStatement p) {
		return p;
	}

	public void visit(WhileStatement p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		if (this.unary && this.refinedMode && getMutationsLeft(p) > 0) {
			pushComplyType(p, p.getExpression());
			pushAllowNull(p, false);
			p.getExpression().accept(this);
			popAllowNull(p);
			popComplyType(p);
		}
		p.getStatements().accept(this);
	}

	@Override
	public Statement evaluateUp(ForStatement p) {
		return p;
	}

	public void visit(ForStatement p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		this.justEvaluating = true;
		super.visit(p);
		this.justEvaluating = false;
		if (this.refinedMode && getMutationsLeft(p) > 0) {
			if (this.unary) {
				VariableDeclarator[] varDecls = p.getInitDecls();
				for (VariableDeclarator vd : varDecls) {
					visit(vd);
				}
			}
			//FIXME: CHECK IF THE FOLLOWING CODE IS NECESSARY
			ExpressionList init = p.getInit();
			for (int i = 0; init != null && i < init.size(); i++) {
				pushComplyType(p, init.get(i));
				pushAllowNull(p, false);
				init.get(i).accept(this);
				popAllowNull(p);
				popComplyType(p);
			}
			//---------------------------------------------------
			
			if (this.unary) {
				pushComplyType(p, p.getCondition());
				pushAllowNull(p, false);
				p.getCondition().accept(this);
				popAllowNull(p);
				popComplyType(p);
			}
			if (this.unary) {
				ExpressionList increments = p.getIncrement();
				for (int i = 0; i < increments.size(); i++) {
					increments.accept(this);
				}
			}
		}
		p.getStatements().accept(this);
	}

	@Override
	public Statement evaluateUp(DoWhileStatement p) {
		return p;
	}

	public void visit(DoWhileStatement p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		p.getStatements().accept(this);
		if (this.unary && this.refinedMode && getMutationsLeft(p) > 0) {
			pushComplyType(p, p.getExpression());
			pushAllowNull(p, false);
			p.getExpression().accept(this);
			popAllowNull(p);
			popComplyType(p);
		}
	}

	@Override
	public Statement evaluateUp(IfStatement p) {
		return p;
	}

	public void visit(IfStatement p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		if (this.unary && this.refinedMode && getMutationsLeft(p) > 0) {
			pushAllowNull(p, false);
			pushComplyType(p, p.getExpression());
			p.getExpression().accept(this);
			popAllowNull(p);
			popComplyType(p);
		}
		p.getStatements().accept(this);
		p.getElseStatements().accept(this);

	}

	public void visit(ConditionalExpression p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		if (this.refinedMode && getMutationsLeft(p) > 0) {
			pushAllowNull(p, false);
			pushComplyType(p, p.getCondition());
			p.getCondition().accept(this);
			popAllowNull(p);
			popComplyType(p);
			OJClass trueCaseType = getType(p.getTrueCase());
			OJClass falseCaseType = getType(p.getFalseCase());
			if (max(trueCaseType, falseCaseType) == trueCaseType) {
				pushComplyType(p, p.getTrueCase());
			} else if (max(trueCaseType, falseCaseType) == falseCaseType) {
				pushComplyType(p, p.getFalseCase());
			} else {
				//TODO: it should never reach this point, however there should be some code to treat to case
			}
			pushAllowNull(p, compatibleAssignType(getType(p.getTrueCase()), null));
			p.getTrueCase().accept(this);
			popAllowNull(p);
			pushAllowNull(p, compatibleAssignType(getType(p.getFalseCase()), null));
			p.getFalseCase().accept(this);
			popAllowNull(p);
			popComplyType(p);
		}
	}

	@Override
	public Expression evaluateUp(AssignmentExpression p) {
		return p;
	}

	public void visit(AssignmentExpression p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		if (!this.unary && getMutationsLeft(p) > 0) {
			Expression lexp = p.getLeft();
			Expression rexp = p.getRight();

			if (this.right) {
				binaryVisit(p, lexp,rexp, false);
				if (this.refinedMode/*this.canBeRefined(rexp)*/) {
					pushAllowNull(p, compatibleAssignType(getType(lexp), null));
					pushComplyType(p, lexp);
					rexp.accept(this);
					popComplyType(p);
					popAllowNull(p);
				}
			}
			if (this.left) {
				binaryVisit(p, lexp, rexp, true);
			}
		}
	}

	public void visit(BinaryExpression p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
		}
		Expression lexp = p.getLeft();
		Expression rexp = p.getRight();
		if (this.refinedMode) {
			pushAllowNull(p, binExprSupportsNull(p.getOperator()));
			pushComplyType(p, rexp);
			lexp.accept(this);
			popAllowNull(p);
			popComplyType(p);
		}
		if (this.refinedMode) {
			pushAllowNull(p, binExprSupportsNull(p.getOperator()));
			pushComplyType(p, lexp);
			rexp.accept(this);
			popAllowNull(p);
			popComplyType(p);
		}
	}

	public void visit(ReturnStatement p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		Expression rexp = p.getExpression();

		if( rexp == null ){
			super.visit(p);
			return;
		}
		if (this.unary && getMutationsLeft(p) > 0) unaryVisit(p,rexp, false);

		if (this.unary && getMutationsLeft(p) > 0 && this.refinedMode && this.canBeReplacedByLiterals(rexp)) {
			Variable returnAuxVar = Variable.generateUniqueVariable();
			getEnvironment().bindVariable(returnAuxVar.toString(), getMethodUnderConsiderationType());
			Expression e1 = returnAuxVar;
			pushAllowNull(p, compatibleAssignType(getType(e1), null));
			pushComplyType(p, e1);
			this.replaceByLiteral(returnAuxVar, rexp);
			popAllowNull(p);
			popComplyType(p);
		}

		if (this.unary && this.refinedMode && this.canBeRefined(rexp) && getMutationsLeft(p) > 0) {
			Variable returnAuxVar = Variable.generateUniqueVariable();
			getEnvironment().bindVariable(returnAuxVar.toString(), getMethodUnderConsiderationType());
			Expression e1 = returnAuxVar;
			pushAllowNull(p, compatibleAssignType(getType(e1), null));
			pushComplyType(p, e1);
			rexp.accept(this);
			popAllowNull(p);
			popComplyType(p);
		}

	}

	private boolean canBeReplacedByLiterals(Expression exp) {
		boolean canBeRefined = this.canBeRefined(exp);
		if (!canBeRefined) {
			if (exp instanceof Literal) return true;
			if (exp instanceof Variable) return true;
			if (exp instanceof FieldAccess) {
				return ((FieldAccess)exp).getReferenceExpr() == null;
			}
			if (exp instanceof MethodCall) {
				return ((MethodCall)exp).getReferenceExpr() == null;
			}
		}
		return false;
	}

	public void visit(VariableDeclarator p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		Expression	rexp = (Expression) p.getInitializer();

		if( rexp == null ){
			super.visit(p);
			return;
		}
		if (this.unary && getMutationsLeft(p) > 0) unaryVisit(p,rexp, false);

		if (this.unary && this.refinedMode && getMutationsLeft(p) > 0) {
			OJClass varType = getEnvironment().lookupBind(p.getVariable());
			pushAllowNull(p, varType==null?false:(compatibleAssignType(varType, null)));
			Variable var = new Variable(p.getVariable());
			pushComplyType(p, varType==null?rexp:var);
			rexp.accept(this);
			popComplyType(p);
			popAllowNull(p);
		}

	}

	public void visit(UnaryExpression p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		if (!this.refinedMode) return;
		pushAllowNull(p, false);
		pushComplyType(p, p);
		Expression exp = p.getExpression();
		exp.accept(this);
		popComplyType(p);
		popAllowNull(p);
	}

	public void visit(MethodCall p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		if (!this.refinedMode || getMutationsLeft(p) <= 0) return;
		if (p.getParent() instanceof Statement) {
			return;
		}
		unaryVisit(p, p, true);
		ExpressionList args = p.getArguments();
		for (int a = 0; a < args.size(); a++) {
			pushAllowNull(p, compatibleAssignType(getType(args.get(a)), null));
			if (this.refinedMode) {
				OJMethod formalMethod = getMethod(p.getName(), getSelfType(), args);
				if (formalMethod != null) {
					Variable formalArgument = Variable.generateUniqueVariable();
					getEnvironment().bindVariable(formalArgument.toString(), formalMethod.getParameterTypes()[a]);
					pushComplyType(p, formalArgument);
				} else {
					pushComplyType(p, args.get(a));
				}
			}
			args.get(a).accept(this);
			popAllowNull(p);
		}
	}

	public void visit(ArrayAccess p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		if (!this.refinedMode || getMutationsLeft(p) <= 0) return;
		pushAllowNull(p, false);
		pushComplyType(p, p);
		p.getIndexExpr().accept(this);
		popComplyType(p);
		popAllowNull(p);
	}

	public void visit(FieldAccess p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		if (!this.refinedMode || getMutationsLeft(p) <= 0) return;
		unaryVisit(p, p, true);
	}

	public void visit(Variable p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		if (!this.refinedMode || getMutationsLeft(p) <= 0) return;
		sameLength((NonLeaf) getStatement(p), this.refModeComplyTypeStack.peek(), p, false, true);
		increaseLenght((NonLeaf) getStatement(p), this.refModeComplyTypeStack.peek(), p, false);
		replaceOneByTwo((NonLeaf) getStatement(p), this.refModeComplyTypeStack.peek(), p, false);
		replaceByLiteral(this.refModeComplyTypeStack.peek(), p);
	}

	public void visit(Literal p) throws ParseTreeException {
		if (this.justEvaluating) {
			super.visit(p);
			return;
		}
		if (!this.refinedMode || getMutationsLeft(p) <= 0) return;
		sameLength((NonLeaf) getStatement(p), this.refModeComplyTypeStack.peek(), p, false, true);
		increaseLenght((NonLeaf) getStatement(p), this.refModeComplyTypeStack.peek(), p, false);
		replaceOneByTwo((NonLeaf) getStatement(p), this.refModeComplyTypeStack.peek(), p, false);
		replaceByLiteral(this.refModeComplyTypeStack.peek(), p);
	}

	private boolean canBeRefined(Expression exp) {
		boolean isJustLiteral = (exp instanceof Literal);
		boolean isJustFieldAccess = (exp instanceof FieldAccess);
		boolean isJustVariable = (exp instanceof Variable);
		boolean isJustMethodCall = (exp instanceof MethodCall);
		boolean canBeRefined = !isJustLiteral && !isJustFieldAccess && !isJustVariable && !isJustMethodCall;
		return canBeRefined;
	}

	private void pushComplyType(ParseTreeObject p, Expression value) {
		if (this.refinedMode && getMutationsLeft(p) > 0) {
			this.refModeComplyTypeStack.push(value);
		}
	}

	private void popComplyType(ParseTreeObject p) {
		if (this.refinedMode && getMutationsLeft(p) > 0) {
			this.refModeComplyTypeStack.pop();
		}
	}

	private void pushAllowNull(ParseTreeObject p, boolean value) {
		if (this.refinedMode && getMutationsLeft(p) > 0) {
			this.refModeAllowNullStack.push(value);
		}
	}

	private void popAllowNull(ParseTreeObject p) {
		if (this.refinedMode && getMutationsLeft(p) > 0) {
			this.refModeAllowNullStack.pop();
		}
	}

	//=========================VISIT METHODS================================



	private OJClass getMethodUnderConsiderationType() throws ParseTreeException {
		OJClass methodType = null;
		String muc = Api.getMethodUnderConsideration();
		for (OJMethod m : getSelfType().getAllMethods()) {
			if (m.getName().equals(muc)) {
				methodType = m.getReturnType();
				break;
			}
		}
		return methodType;
	}


	private void unaryVisit(NonLeaf p, Expression rexp, boolean refined) throws ParseTreeException {
		if (rexp==null) throw new IllegalArgumentException("rexp is null in PRVO.unaryVisit invokation.");		
		Expression e1 = null;
		if (p instanceof VariableDeclarator) {
			e1 = new Variable(((VariableDeclarator) p).getVariable());
		} else if (p instanceof ReturnStatement) {
			Variable returnAuxVar = Variable.generateUniqueVariable();
			getEnvironment().bindVariable(returnAuxVar.toString(), getMethodUnderConsiderationType());
			e1 = returnAuxVar;
		}
		if (e1 == null && refined) {
			e1 = this.refModeComplyTypeStack.peek();
		}
		sameLength(p, e1, rexp, false, refined);
		decreaseLenght(p, e1, rexp, false);
		increaseLenght(p, e1, rexp, false);
		replaceTwoByOne(p, e1, rexp, false);
		replaceOneByTwo(p, e1, rexp, false);
		if (refined) {
			replaceByLiteral(e1, rexp);
		}
	}


	private void replaceByLiteral(Expression complyWith, Expression orig) throws ParseTreeException {
		boolean parentIsUnaryExpression = ((ParseTreeObject) orig).getParent() instanceof UnaryExpression;
		ParseTreeObject parent = ((ParseTreeObject)orig).getParent();
		boolean parentIsIncDecUnaryExpression = parentIsUnaryExpression && (((UnaryExpression)parent).getOperator() >= 0 && ((UnaryExpression)parent).getOperator() <= 3);
		boolean mutGenLimitIsPositive = getMutationsLeft((ParseTreeObject)orig) > 0;
		boolean mutatingRightOrUnary = this.right || this.unary;
		boolean origIsValid = !parentIsIncDecUnaryExpression && ((orig instanceof MethodCall) || (orig instanceof FieldAccess) || (orig instanceof Variable) || (orig instanceof Literal));
		boolean noChainedMethodCall = ((orig instanceof MethodCall) && ((MethodCall)orig).getReferenceExpr() == null) || !(orig instanceof MethodCall);
		boolean noChainedFieldAccess = ((orig instanceof FieldAccess) && ((FieldAccess)orig).getReferenceExpr() == null) || !(orig instanceof FieldAccess);
		boolean origIsNotAChain = noChainedMethodCall && noChainedFieldAccess;
		if (this.useLiterals && this.allowReplacementWithLiterals() && mutGenLimitIsPositive && origIsValid && mutatingRightOrUnary && origIsNotAChain) {
			searchForLiteralsInMethod((ParseTreeObject) orig);
			for (Literal lit : this.literals) {
				if (complyWith != null) {
					OJClass typeToComply = getType(complyWith);
					OJClass litType = getType(lit);
					boolean ignoreTypeCheckOnNumbers = this.allowNumberLiteralsVariations() && this.isNumber(lit) && this.isNumericExpression(complyWith);
					if (!ignoreTypeCheckOnNumbers && !compatibleAssignType(typeToComply, litType, false)) {
						continue;
					}
				}
				outputToFile((ParseTreeObject) orig.makeCopy_keepOriginalID(), lit);
			}
		}
	}

	private void searchForLiteralsInMethod(ParseTreeObject currentNode) {
		if (this.literals != null && !literals.isEmpty()) {
			if (this.refModeAllowNullStack.peek()) {
				this.literals.add(Literal.constantNull());
			} else {
				this.literals.remove(Literal.constantNull());
			}
		} else if (this.literals == null) {
			this.literals = new HashSet<Literal>();
		}
		ParseTreeObject current = (ParseTreeObject) currentNode.makeRecursiveCopy_keepOriginalID();
		while (current != null && !(current instanceof MethodDeclaration)) {
			current = current.getParent();
		}
		searchForLiterals(current);
		if (this.allowLiteralEmptyString()) this.literals.add(Literal.constantEmptyString());
		if (this.allowLiteralFalse()) this.literals.add(Literal.constantFalse());
		if (this.allowLiteralNull() && this.refModeAllowNullStack.peek()) this.literals.add(Literal.constantNull());
		if (this.allowLiteralOne()) this.literals.add(Literal.constantOne());
		if (this.allowLiteralTrue()) this.literals.add(Literal.constantTrue());
		if (this.allowLiteralZero()) this.literals.add(Literal.constantZero());
		if (this.allowLiteralOne() && this.allowNumberLiteralsVariations()) this.addNumberLiteralsVariations(Literal.constantOne());
		if (this.allowLiteralZero() && this.allowNumberLiteralsVariations()) this.addNumberLiteralsVariations(Literal.constantZero());
	}

	/**
	 * Recursively searches for literals starting form a AST node
	 * @param node
	 */
	private void searchForLiterals(ParseTreeObject node) {
		if (node == null) return;
		if (node instanceof MethodDeclaration) {
			searchForLiterals(((MethodDeclaration)node).getBody());
		} else if (node instanceof StatementList) {
			StatementList sl = (StatementList)node;
			for (int s = 0; s < sl.size(); s++) {
				searchForLiterals((ParseTreeObject) sl.get(s));
			}
		} else if (node instanceof ExpressionList) {
			ExpressionList el = (ExpressionList) node;
			for (int e = 0; e < el.size(); e++) {
				searchForLiterals((ParseTreeObject) el.get(e));
			}
		} else if (node instanceof MethodCall) {
			MethodCall mc = (MethodCall)node;
			ExpressionList arguments = mc.getArguments();
			for (int a = 0; a < arguments.size(); a++) {
				searchForLiterals((ParseTreeObject) arguments.get(a));
			}
		} else if (node instanceof WhileStatement) {
			WhileStatement ws = (WhileStatement) node;
			Expression condition = ws.getExpression();
			searchForLiterals((ParseTreeObject) condition);
			searchForLiterals(ws.getStatements());
		} else if (node instanceof ForStatement) {
			ForStatement fs = (ForStatement) node;
			searchForLiterals(fs.getInit());
			searchForLiterals((ParseTreeObject) fs.getCondition());
			searchForLiterals(fs.getIncrement());
			searchForLiterals(fs.getStatements());
		} else if (node instanceof TryStatement) {
			TryStatement ts = (TryStatement) node;
			searchForLiterals(ts.getBody());
			searchForLiterals(ts.getFinallyBody());
		} else if (node instanceof SwitchStatement) {
			SwitchStatement ss = (SwitchStatement) node;
			searchForLiterals((ParseTreeObject) ss.getExpression());
			searchForLiterals(ss.getCaseGroupList());
		} else if (node instanceof CaseGroupList) {
			CaseGroupList cgl = (CaseGroupList) node;
			for (int cg = 0; cg < cgl.size(); cg++) {
				searchForLiterals(cgl.get(cg));
			}
		} else if (node instanceof CaseGroup) {
			CaseGroup cg = (CaseGroup) node;
			searchForLiterals(cg.getLabels());
			searchForLiterals(cg.getStatements());
		} else if (node instanceof IfStatement) {
			IfStatement is = (IfStatement) node;
			searchForLiterals((ParseTreeObject) is.getExpression());
			searchForLiterals(is.getStatements());
			searchForLiterals(is.getElseStatements());
		} else if (node instanceof DoWhileStatement) {
			DoWhileStatement dws = (DoWhileStatement) node;
			searchForLiterals((ParseTreeObject) dws.getExpression());
			searchForLiterals(dws.getStatements());
		} else if (node instanceof AllocationExpression) {
			AllocationExpression ae = (AllocationExpression) node;
			searchForLiterals(ae.getArguments());
		} else if (node instanceof ArrayAccess) {
			ArrayAccess aa = (ArrayAccess) node;
			searchForLiterals((ParseTreeObject) aa.getIndexExpr());
		} else if (node instanceof ArrayInitializer) {
			ArrayInitializer ai = (ArrayInitializer) node;
			for (int i = 0; i < ai.size(); i++) {
				searchForLiterals((ParseTreeObject) ai.get(i));
			}
		} else if (node instanceof AssignmentExpression) {
			AssignmentExpression ae = (AssignmentExpression) node;
			searchForLiterals((ParseTreeObject) ae.getRight());
		} else if (node instanceof BinaryExpression) {
			BinaryExpression be = (BinaryExpression) node;
			searchForLiterals((ParseTreeObject) be.getLeft());
			searchForLiterals((ParseTreeObject) be.getRight());
		} else if (node instanceof Block) {
			Block b = (Block) node;
			searchForLiterals(b.getStatements());
		} else if (node instanceof CastExpression) {
			CastExpression ce = (CastExpression) node;
			searchForLiterals((ParseTreeObject) ce.getExpression());
		} else if (node instanceof ConditionalExpression) {
			ConditionalExpression ce = (ConditionalExpression) node;
			searchForLiterals((ParseTreeObject) ce.getCondition());
			searchForLiterals((ParseTreeObject) ce.getTrueCase());
			searchForLiterals((ParseTreeObject) ce.getFalseCase());
		} else if (node instanceof ConstructorInvocation) {
			ConstructorInvocation ci = (ConstructorInvocation) node;
			searchForLiterals(ci.getArguments());
		} else if (node instanceof ReturnStatement) {
			ReturnStatement rs = (ReturnStatement) node;
			searchForLiterals((ParseTreeObject) rs.getExpression());
		} else if (node instanceof UnaryExpression) {
			UnaryExpression ue = (UnaryExpression) node;
			searchForLiterals((ParseTreeObject) ue.getExpression());
		} else if (node instanceof VariableDeclaration) {
			VariableDeclaration vd = (VariableDeclaration) node;
			searchForLiterals((ParseTreeObject) vd.getInitializer());
		} else if (node instanceof VariableDeclarator) {
			VariableDeclarator vd = (VariableDeclarator) node;
			searchForLiterals((ParseTreeObject) vd.getInitializer());
		} else if (node instanceof Literal) {
			boolean isNull = ((Literal)node).getLiteralType()==Literal.NULL;
			boolean nullCheck = (isNull && this.allowLiteralNull() && this.refModeAllowNullStack.peek()) || !isNull;
			boolean isString = ((Literal)node).getLiteralType()==Literal.STRING;
			boolean stringCheck = (isString && this.allowLiteralStrings()) || !isString;
			boolean isEmptyString = isString && ((Literal)node).toFlattenString().compareTo("") == 0;
			boolean emptyStringCheck = ((isEmptyString && this.allowLiteralEmptyString()) || !isEmptyString);
			boolean isBoolean = ((Literal)node).getLiteralType()==Literal.BOOLEAN;
			boolean isFalse = isBoolean && ((Literal)node).toFlattenString().compareToIgnoreCase("false") == 0;
			boolean isTrue = isBoolean && ((Literal)node).toFlattenString().compareToIgnoreCase("true") == 0;
			boolean falseCheck = (isFalse && this.allowLiteralFalse()) || !isFalse;
			boolean trueCheck = (isTrue && this.allowLiteralTrue()) || !isTrue;
			boolean isNumber = this.isNumber((Literal)node);
			boolean isOne = isNumber && ((Literal)node).toFlattenString().compareTo("1") == 0;
			boolean isZero = isNumber && ((Literal)node).toFlattenString().compareTo("0") == 0;
			boolean oneCheck = (isOne && this.allowLiteralOne()) || !isOne;
			boolean zeroCheck = (isZero && this.allowLiteralZero()) || !isZero;
			if (nullCheck && stringCheck && emptyStringCheck && falseCheck && trueCheck && oneCheck && zeroCheck) {
				this.literals.add((Literal)node);
				if (isNumber && this.allowNumberLiteralsVariations()) addNumberLiteralsVariations((Literal)node);
			}
		}
	}
	
	private void addNumberLiteralsVariations(Literal literal) {
		if (literal.getLiteralType()==Literal.INTEGER) {
			Integer intLiteral = Integer.valueOf(literal.toFlattenString().trim());
			this.literals.add(Literal.makeLiteral(intLiteral.doubleValue()));
			this.literals.add(Literal.makeLiteral(intLiteral.floatValue()));
			this.literals.add(Literal.makeLiteral(intLiteral.longValue()));
		} else if (literal.getLiteralType()==Literal.DOUBLE) {
			Double doubleLiteral = Double.valueOf(literal.toFlattenString().trim());
			this.literals.add(Literal.makeLiteral(doubleLiteral.intValue()));
			this.literals.add(Literal.makeLiteral(doubleLiteral.floatValue()));
			this.literals.add(Literal.makeLiteral(doubleLiteral.longValue()));
		} else if (literal.getLiteralType()==Literal.FLOAT) {
			Float floatLiteral = Float.valueOf(literal.toFlattenString().trim());
			this.literals.add(Literal.makeLiteral(floatLiteral.intValue()));
			this.literals.add(Literal.makeLiteral(floatLiteral.doubleValue()));
			this.literals.add(Literal.makeLiteral(floatLiteral.longValue()));
		} else if (literal.getLiteralType()==Literal.LONG) {
			Long longLiteral = Long.valueOf(literal.toFlattenString().trim());
			this.literals.add(Literal.makeLiteral(longLiteral.intValue()));
			this.literals.add(Literal.makeLiteral(longLiteral.doubleValue()));
			this.literals.add(Literal.makeLiteral(longLiteral.floatValue()));
		}
	}
	
	private boolean isNumber(Literal literal) {
		return literal.getLiteralType()==Literal.INTEGER || literal.getLiteralType()==Literal.DOUBLE || literal.getLiteralType()==Literal.FLOAT || literal.getLiteralType()==Literal.LONG;
	}
	
	private boolean isPrimitiveToObjectAssignment(OJClass varType, OJClass valueType) {
		if (varType.getName().compareToIgnoreCase("java.lang.object") == 0
				&& (valueType.isPrimitive())) {
			return true;
		}
		return false;
	}
	
	private ParseTreeObject wrapPrimitiveExpression(OJClass primitiveClass, ParseTreeObject originalExpression) {
		if (primitiveClass.isPrimitive()) {
			TypeName constructorType = null;
			if (primitiveClass.getSimpleName().compareTo(openjava.mop.OJSystem.BOOLEAN.getSimpleName()) == 0) {
				constructorType = new TypeName("java.lang.Boolean");
			} else if (primitiveClass.getSimpleName().compareTo(openjava.mop.OJSystem.CHAR.getSimpleName()) == 0) {
				constructorType = new TypeName("java.lang.Character");
			} else if (primitiveClass.getSimpleName().compareTo(openjava.mop.OJSystem.BYTE.getSimpleName()) == 0) {
				constructorType = new TypeName("java.lang.Byte");
			} else if (primitiveClass.getSimpleName().compareTo(openjava.mop.OJSystem.SHORT.getSimpleName()) == 0) {
				constructorType = new TypeName("java.lang.Short");
			} else if (primitiveClass.getSimpleName().compareTo(openjava.mop.OJSystem.INT.getSimpleName()) == 0) {
				constructorType = new TypeName("java.lang.Integer");
			} else if (primitiveClass.getSimpleName().compareTo(openjava.mop.OJSystem.LONG.getSimpleName()) == 0) {
				constructorType = new TypeName("java.lang.Long");
			} else if (primitiveClass.getSimpleName().compareTo(openjava.mop.OJSystem.FLOAT.getSimpleName()) == 0) {
				constructorType = new TypeName("java.lang.Float");
			}
			ParseTreeObject originalExpressionParent = (ParseTreeObject) originalExpression.getParent().makeRecursiveCopy_keepOriginalID();
			ExpressionList params = new ExpressionList();
			params.add((Expression) originalExpression.makeRecursiveCopy_keepOriginalID());
			AllocationExpression wrappedExpression = new AllocationExpression(constructorType, params);
			wrappedExpression.setParent(originalExpressionParent);
			return wrappedExpression;
		} else {
			return originalExpression;
		}
	}
	
	private boolean isNumericExpression(Expression expr) throws ParseTreeException {
		if (isNumber(expr)) {
			return true;
		}
		OJClass numberClass = OJClass.forClass(Number.class);
		OJClass exprType = getType(expr);
		return compatibleAssignType(numberClass, exprType, true);
	}
	
	private boolean isNumber(Expression expr) {
		if (expr instanceof Literal) {
			return this.isNumber((Literal)expr);
		}
		return false;
	}

	private void outputToFile(ParseTreeObject original, ParseTreeObject mutant) {
		Expression modifiedMutant = (Expression) mutant.makeCopy_keepOriginalID();
		try {
			if (!allowSuper()) {
				modifiedMutant = removeSuperOrThis(modifiedMutant, true);
			}
			if (!allowThis()) {
				modifiedMutant = removeSuperOrThis(modifiedMutant, false);
			}
		} catch (ParseTreeException pte) {
			pte.printStackTrace();
		}
		MutantsInformationHolder.mainHolder().addMutation(this.op, original, (ParseTreeObject) modifiedMutant);
	}


}
