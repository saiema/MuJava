package mujava.op;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mujava.api.Api;
import mujava.api.Configuration;
import mujava.api.MutantsInformationHolder;
import mujava.api.MutationOperator;
import mujava.op.util.Mutator;
import openjava.mop.CannotAlterException;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJClassNotFoundException;
import openjava.mop.OJField;
import openjava.mop.OJMethod;
import openjava.ptree.ArrayAccess;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.BinaryExpression;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionList;
import openjava.ptree.FieldAccess;
import openjava.ptree.FieldDeclaration;
import openjava.ptree.ForStatement;
import openjava.ptree.IfStatement;
import openjava.ptree.Literal;
import openjava.ptree.MethodCall;
import openjava.ptree.MethodDeclaration;
import openjava.ptree.ModifierList;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.Statement;
import openjava.ptree.StatementList;
import openjava.ptree.UnaryExpression;
import openjava.ptree.Variable;
import openjava.ptree.VariableDeclaration;
import openjava.ptree.VariableDeclarator;
import openjava.ptree.VariableInitializer;
import openjava.ptree.WhileStatement;

/**
 * <b>Boolean Expression Expander</b>
 * <p>
 * This operator produce the following 
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.3.3u
 */
public class BEE extends Mutator {
	
	/**
	 * This option Enables/Disables scanning for boolean expressions to be used by this operator
	 * <p>
	 * This option is disabled by default 
	 */
	public static final String SCAN_FOR_EXPRESSIONS = "bee_scan_for_expressions";
	
	/**
	 * This option Enables/Disables skipping equivalent mutations ({@code && true}, {@code || false}, and {@code ^false})
	 * <p>
	 * This option is disabled by default
	 */
	public static final String DISABLE_NEUTRAL_TRUE_FALSE = "bee_disable_neutral_true_false";
	
	/**
	 * This option Enables/Disables skipping boolean constants initialized with {@code true} or {@code false} 
	 * <p>
	 * This option is disabled by default
	 */
	public static final String DISABLE_NEUTRAL_CONSTANTS = "bee_disable_neutral_constants";
	
	/**
	 * This option Enables/Disables skipping the same expression as the one being expanded, for example
	 * {@code P -> P & P} will not be generated.
	 * <p>
	 * This option is disabled by defaults
	 */
	public static final String SKIP_SAME_EXPRESSION = "bee_skip_same_expression";
	
	private List<Object> fieldsMethodsAndVarsCache = null;
	private List<OJField> finalFieldsCache = null;
	private String classCache = null;
	private Map<OJClass, Map<String, OJField>> fieldDeclarationCache;

	public BEE(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
		this.fieldDeclarationCache = new TreeMap<>();
	}
	
	public void visit(BinaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		if (getType(p).getName().compareToIgnoreCase("boolean") != 0) return;
		try {
			List<BinaryExpression> mutants = generateMutants(p);
			for (BinaryExpression mut : mutants) {
				mut.forceParenthesis(true);
				outputToFile(p, mut);
			}
		} catch (OJClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.visit(p.getLeft());
		super.visit(p.getRight());
	}
	
	public void visit(UnaryExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		if (getType(p).getName().compareToIgnoreCase("boolean") != 0) return;
		try {
			List<BinaryExpression> mutants = generateMutants(p);
			for (BinaryExpression mut : mutants) {
				mut.forceParenthesis(true);
				outputToFile(p, mut);
			}
		} catch (OJClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.visit(p.getExpression());
	}
	
	public void visit(Variable p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		if (getType(p).getName().compareToIgnoreCase("boolean") != 0) return;
		try {
			List<BinaryExpression> mutants = generateMutants(p);
			for (BinaryExpression mut : mutants) {
				mut.forceParenthesis(true);
				outputToFile(p, mut);
			}
		} catch (OJClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void visit(FieldAccess p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		if (getType(p).getName().compareToIgnoreCase("boolean") != 0) return;
		try {
			List<BinaryExpression> mutants = generateMutants(p);
			for (BinaryExpression mut : mutants) {
				mut.forceParenthesis(true);
				outputToFile(p, mut);
			}
		} catch (OJClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void visit(MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		if (getType(p).getName().compareToIgnoreCase("boolean") != 0) return;
		try {
			List<BinaryExpression> mutants = generateMutants(p);
			for (BinaryExpression mut : mutants) {
				mut.forceParenthesis(true);
				outputToFile(p, mut);
			}
		} catch (OJClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void visit(AssignmentExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression rexp = p.getRight();
		if (rexp != null) rexp.accept(this);
	}
	
	public void visit(VariableDeclarator p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		Expression	rexp = (Expression) p.getInitializer();
		
		if( rexp != null ){
			rexp.accept(this);
		}
	}
	
	private List<BinaryExpression> generateMutants(Expression e) throws OJClassNotFoundException, ParseTreeException {
		List<BinaryExpression> mutants = new LinkedList<>();
		List<Object> expressions = getBooleanExpressions((ParseTreeObject)e);
		for (Object o : expressions) {
			boolean disableNeutralAnd = false;
			boolean disableNeutralOr = false;
			boolean disableNeutralXor = false;
			if (skipSameExpression()) {
				if (o == e) continue;
				Expression oae = (Expression) o;
				if (oae.toFlattenString().compareTo(e.toFlattenString()) == 0) continue;
			}
			if (o instanceof Literal && ((Literal)o).equals("true")) {
				disableNeutralAnd = disableNeutralAndOr();
			} else if (o instanceof Literal && ((Literal)o).equals("false")) {
				disableNeutralOr = disableNeutralAndOr();
				disableNeutralXor = disableNeutralAndOr();
			} else if (	o instanceof FieldAccess || o instanceof Variable ) {
				if (disableNeutralAndOrConstants() && isTrueOrFalseFinal(o, (ParseTreeObject)e)) {
					continue;
				}
			}
			
			Expression ecopyAND = (Expression) e.makeRecursiveCopy_keepOriginalID();
			ParseTreeObject eparent = ((ParseTreeObject) ecopyAND).getParent();
			
			if (!disableNeutralAnd) {
				Expression ocopyAND = (Expression) ((ParseTreeObject)o).makeRecursiveCopy_keepOriginalID();
				BinaryExpression mutantAND = new BinaryExpression(ecopyAND, BinaryExpression.LOGICAL_AND, ocopyAND);
				mutantAND.setParent(eparent);
				mutants.add(mutantAND);
			}
			
			if (!disableNeutralOr) {
				Expression ocopyOR = (Expression) ((ParseTreeObject)o).makeRecursiveCopy_keepOriginalID();
				Expression ecopyOR = (Expression) e.makeRecursiveCopy_keepOriginalID();
				BinaryExpression mutantOR = new BinaryExpression(ecopyOR, BinaryExpression.LOGICAL_OR, ocopyOR);
				mutantOR.setParent(eparent);
				mutants.add(mutantOR);
			}
			
			if (!disableNeutralAnd) {
				Expression ocopyBWAND = (Expression) ((ParseTreeObject)o).makeRecursiveCopy_keepOriginalID();
				Expression ecopyBWAND = (Expression) e.makeRecursiveCopy_keepOriginalID();
				BinaryExpression mutantBWAND = new BinaryExpression(ecopyBWAND, BinaryExpression.BITAND, ocopyBWAND);
				mutantBWAND.setParent(eparent);
				mutants.add(mutantBWAND);
			}
			
			if (!disableNeutralOr) {
				Expression ocopyBWIOR = (Expression) ((ParseTreeObject)o).makeRecursiveCopy_keepOriginalID();
				Expression ecopyBWIOR = (Expression) e.makeRecursiveCopy_keepOriginalID();
				BinaryExpression mutantBWIOR = new BinaryExpression(ecopyBWIOR, BinaryExpression.BITOR, ocopyBWIOR);
				mutantBWIOR.setParent(eparent);
				mutants.add(mutantBWIOR);
			}
			
			if (!disableNeutralXor) {
				Expression ocopyBWEOR = (Expression) ((ParseTreeObject)o).makeRecursiveCopy_keepOriginalID();
				Expression ecopyBWEOR = (Expression) e.makeRecursiveCopy_keepOriginalID();
				BinaryExpression mutantBWEOR = new BinaryExpression(ecopyBWEOR, BinaryExpression.XOR, ocopyBWEOR);
				mutantBWEOR.setParent(eparent);
				mutants.add(mutantBWEOR);
			}
		}
		return mutants;
	}
	
	private List<Object> getBooleanExpressions(ParseTreeObject from) throws OJClassNotFoundException, ParseTreeException {
		List<Object> booleanExpressions = new LinkedList<>();
		Map<OJClass, List<Variable>> reachableVars = getReachableVariables(from, ALLOW_FINAL);
		for (OJClass key : reachableVars.keySet()) {
			if (key.getSimpleName().toLowerCase().compareTo("boolean") == 0) {
				booleanExpressions.addAll(reachableVars.get(key));
			}
		}
		boolean refreshCache = false;
		if (this.classCache == null || (this.classCache.compareTo(getSelfType().getName()) != 0)) {
			this.classCache = getSelfType().getName();
			this.fieldsMethodsAndVarsCache = new LinkedList<>();
			refreshCache = true;
		}
		
		if (refreshCache) {
			int options = 0;
			options += ALLOW_FINAL;
			options += ALLOW_NON_STATIC;
			options += ALLOW_STATIC;
			options += ALLOW_PRIVATE;
			options += ALLOW_PUBLIC;
			List<Object> fmv = fieldsMethodsAndVars(from, getSelfType(), options);
			for (Object o : fmv) {
				OJClass oclass = null;
				if (o instanceof OJMethod) {
					oclass = ((OJMethod)o).getReturnType();
				} else if (o instanceof OJField) {
					oclass = ((OJField)o).getType();
				} else if (o instanceof Variable) {
					oclass = getType((Variable)o);
				}
				if (oclass == null) continue;
				if (oclass.getSimpleName().compareToIgnoreCase("boolean") != 0) continue;
				MethodDeclaration thisMethod = (MethodDeclaration) getMethodDeclaration(from);
				if (thisMethod != null && o instanceof OJMethod && isSameMethodAsMUC((OJMethod)o)) {
					continue;
				}
				if (o instanceof OJMethod) {
					MethodCall mutantMethod = new MethodCall(((OJMethod)o).getName(), new ExpressionList());
					this.fieldsMethodsAndVarsCache.add(mutantMethod);
				} else if (o instanceof OJField) {
					FieldAccess mutantField = new FieldAccess(((OJField)o).getName());
					this.fieldsMethodsAndVarsCache.add(mutantField);
				} else {
					this.fieldsMethodsAndVarsCache.add(o);
				}
			}
			this.fieldsMethodsAndVarsCache.add(Literal.makeLiteral(true));
			this.fieldsMethodsAndVarsCache.add(Literal.makeLiteral(false));
		}
		if (scanForExpressions()) {
			booleanExpressions.addAll(getReachableBooleanExpressions(from));
		}
		booleanExpressions.addAll(this.fieldsMethodsAndVarsCache);
		return booleanExpressions;
	}
	
	private boolean isSameMethodAsMUC(OJMethod o) {
		if (Api.getMethodUnderConsideration() != null && Api.getMethodUnderConsideration().compareToIgnoreCase(o.getName()) != 0) {
			return false;
		}
		if (Api.getExpectedArguments() != null) { //se definieron argumentos en la configuracion
			if (Api.getExpectedArguments().length != o.getParameterTypes().length) {
				return false;
			}
			for (int p = 0; p < Api.getExpectedArguments().length; p++) {
				OJClass mp = o.getParameterTypes()[p];
				String ep = Api.getExpectedArguments()[p];
				if (mp.getSimpleName().compareToIgnoreCase(ep) != 0) {
					return false;
				}
			}
			return true;
		} else if (o.getParameterTypes() == null || o.getParameterTypes().length == 0) { //no se definieron argumentos en la configuracion, asumir argumentos vacios
			return true;
		}
		return false;
	}
	
	private boolean scanForExpressions() {
		if (Configuration.argumentExist(SCAN_FOR_EXPRESSIONS)) {
			return (Boolean) Configuration.getValue(SCAN_FOR_EXPRESSIONS);
		}
		return false;
	}
	
	private boolean disableNeutralAndOr() {
		if (Configuration.argumentExist(DISABLE_NEUTRAL_TRUE_FALSE)) {
			return (Boolean) Configuration.getValue(DISABLE_NEUTRAL_TRUE_FALSE);
		}
		return false;
	}
	
	private boolean disableNeutralAndOrConstants() {
		if (Configuration.argumentExist(DISABLE_NEUTRAL_CONSTANTS)) {
			return (Boolean) Configuration.getValue(DISABLE_NEUTRAL_CONSTANTS);
		}
		return false;
	}
	
	private boolean skipSameExpression() {
		if (Configuration.argumentExist(SKIP_SAME_EXPRESSION)) {
			return (Boolean) Configuration.getValue(SKIP_SAME_EXPRESSION);
		}
		return false;
	}

	public void outputToFile(Expression original, BinaryExpression mutant) {
		if (comp_unit == null)
			return;

		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.BEE, (ParseTreeObject) original,
				mutant);

	}
	
	
	private void addForBooleanExpressions(ParseTreeObject from, List<Object> expressions) throws ParseTreeException {
		ParseTreeObject current = getStatement(from);
		if (current instanceof ForStatement) {
			VariableDeclarator[] init = ((ForStatement) current).getInitDecls();
			boolean found = false;
			for (int i = 0; init != null && i < init.length && !found; i++) {
				VariableDeclarator vd = init[i];
				if (vd == from) {
					found = true;
				} else {
					ParseTreeObject curr = current;
					while (curr != null && !found && !(curr instanceof VariableDeclarator)) {
						if (curr instanceof Statement) {
							curr = null;
						} else {
							found = vd == from;
							curr = curr.getParent();
						}
					}
				}
			}
			if (!found) {
				VariableDeclarator[] vds = ((((ForStatement) current).getInitDecls()));
				if (vds != null) {
					for (VariableDeclarator vd : vds) {
						addBooleanExpression((ParseTreeObject) vd.getInitializer(), expressions);
					}
				}
			}
			if (((ForStatement)current).getCondition() != null) {
				List<Object> splittedCondition = splitAtLogicalOperators(((ForStatement)current).getCondition());
				expressions.addAll(splittedCondition);
			}
		}
	}
	
	private List<Object> splitAtLogicalOperators(Expression e) throws ParseTreeException {
		List<Object> result = new LinkedList<>();
		List<Expression> subExpressions = new LinkedList<>();
		subExpressions.add(e);
		while (!subExpressions.isEmpty()) {
			Expression ce = subExpressions.remove(0);
			if (ce instanceof Variable
					|| ce instanceof FieldAccess
					|| ce instanceof MethodCall
					|| ce instanceof Literal
					|| ce instanceof ArrayAccess) {
				continue;
			} else if (ce instanceof UnaryExpression) {
				UnaryExpression ue = (UnaryExpression) ce;
				if (!isComplexExpression(ue)) {
					addBooleanExpression(ue, result);
				} else if (ue.getExpression() instanceof UnaryExpression) {
					subExpressions.add(ue.getExpression());
					continue;
				} else if (ue.getExpression() instanceof BinaryExpression) {
					if (!isRelationalBinaryExpression((BinaryExpression)ue.getExpression())) {
						subExpressions.add(((BinaryExpression) ue.getExpression()).getLeft());
						subExpressions.add(((BinaryExpression) ue.getExpression()).getRight());
						continue;
					} else {
						subExpressions.add(ue.getExpression());
					}
				}
			} else if (ce instanceof BinaryExpression) {
				if (!isComplexExpression(ce)) {
					addBooleanExpression((ParseTreeObject) ce, result);
				} else {
					subExpressions.add(((BinaryExpression) ce).getLeft());
					subExpressions.add(((BinaryExpression) ce).getRight());
					continue;
				}
				
			}
		}
		return result;
	}
	
	private boolean isComplexExpression(Expression e) {
		if (e instanceof Variable
				|| e instanceof FieldAccess
				|| e instanceof MethodCall
				|| e instanceof Literal
				|| e instanceof ArrayAccess) {
			return false;
		} else if (e instanceof UnaryExpression) {
			UnaryExpression ue = (UnaryExpression) e;
			return (ue.getExpression() instanceof UnaryExpression || ue.getExpression() instanceof BinaryExpression);
		} else if (e instanceof BinaryExpression) {
			BinaryExpression be = (BinaryExpression) e;
			boolean complexLeft = (be.getLeft() instanceof UnaryExpression || be.getLeft() instanceof BinaryExpression);
			boolean complexRight = (be.getRight() instanceof UnaryExpression || be.getRight() instanceof BinaryExpression);
			return complexLeft && complexRight;
		}
		return false;
	}
	
	
	private boolean isRelationalBinaryExpression(BinaryExpression be) {
		if ( 	be.getOperator() == BinaryExpression.EQUAL
				||
				be.getOperator() == BinaryExpression.GREATER
				||
				be.getOperator() == BinaryExpression.GREATEREQUAL
				||
				be.getOperator() == BinaryExpression.LESS
				||
				be.getOperator() == BinaryExpression.LESSEQUAL
				||
				be.getOperator() == BinaryExpression.NOTEQUAL
				) {
			return true;
		}
		return false;
	}
	
	private void addBooleanExpression(ParseTreeObject o, List<Object> expressions) throws ParseTreeException {
		if (o instanceof UnaryExpression) {
			expressions.add((Expression) o);
		} else if (o instanceof BinaryExpression) {
			BinaryExpression be = (BinaryExpression) o;
			if (isRelationalBinaryExpression(be)) {
				expressions.add(be);
			}
		} else if (o instanceof MethodCall && getType(((MethodCall)o)).getSimpleName().compareToIgnoreCase("boolean") == 0) {
			expressions.add(((MethodCall)o));
		}
	}
	
	/**
	 * This method will return a list of boolean expressions reachable from a specific node
	 * <p>
	 * The expressions returned will not include logical operators.
	 * 
	 * @param from
	 * @return
	 * @throws ParseTreeException
	 */
	private List<Object> getReachableBooleanExpressions(ParseTreeObject from) throws ParseTreeException {
		List<Object> expressions = new LinkedList<>();
		addForBooleanExpressions(from, expressions);
		ParseTreeObject current = (ParseTreeObject) from;
		ParseTreeObject limit = current;
		while (current != null && !(current instanceof MethodDeclaration)) {
			if (current instanceof VariableDeclarator) {
				VariableDeclarator vd = (VariableDeclarator) current;
				if (vd.getInitializer() != null && !isComplexExpression((Expression) vd.getInitializer()) && getType((Expression) vd.getInitializer()).getSimpleName().compareToIgnoreCase("boolean") == 0) {
					expressions.addAll(splitAtLogicalOperators((Expression) vd.getInitializer()));
				}
			} else if (current instanceof VariableDeclaration) {
				VariableDeclaration vd = (VariableDeclaration) current;
				if (vd.getInitializer() != null && !isComplexExpression((Expression) vd.getInitializer()) && getType((Expression) vd.getInitializer()).getSimpleName().compareToIgnoreCase("boolean") == 0) {
					expressions.addAll(splitAtLogicalOperators((Expression) vd.getInitializer()));
				}
			} else if (current instanceof ForStatement) {
				addForBooleanExpressions(current, expressions);
			} else if (current instanceof StatementList) {
				StatementList stList = (StatementList) current;
				for (int s = 0; s < stList.size(); s++) {
					Statement cst = stList.get(s);
					if (cst == limit) {
						break;
					}
					if (cst instanceof VariableDeclarator) {
						VariableDeclarator vd = (VariableDeclarator) cst;
						if (vd.getInitializer() != null && !isComplexExpression((Expression) vd.getInitializer()) && getType((Expression) vd.getInitializer()).getSimpleName().compareToIgnoreCase("boolean") == 0) {
							expressions.addAll(splitAtLogicalOperators((Expression) vd.getInitializer()));
						}
					} else if (cst instanceof VariableDeclaration) {
						VariableDeclaration vd = (VariableDeclaration) cst;
						if (vd.getInitializer() != null && !isComplexExpression((Expression) vd.getInitializer()) && getType((Expression) vd.getInitializer()).getSimpleName().compareToIgnoreCase("boolean") == 0) {
							expressions.addAll(splitAtLogicalOperators((Expression) vd.getInitializer()));
						}
					}
				}
			} else if (current instanceof IfStatement) {
				Expression cond = ((IfStatement)current).getExpression();
				if (cond != null) {
					expressions.addAll(splitAtLogicalOperators(cond));
				}
			} else if (current instanceof WhileStatement) {
				Expression cond = ((WhileStatement)current).getExpression();
				if (cond != null) {
					expressions.addAll(splitAtLogicalOperators(cond));
				}
			}
			if (!(current instanceof StatementList))
				limit = current;
			current = current.getParent();
		}
		return expressions;
	}
	
	protected boolean isTrueOrFalseFinal(Object o, ParseTreeObject n) {
		if (o instanceof Variable) {
			Variable v = (Variable) o;
			Statement previousStatement = (Statement)getStatement(n, -1);
			ParseTreeObject current = (ParseTreeObject) previousStatement;
			ParseTreeObject limit = current;
			while (current != null && !(current instanceof MethodDeclaration)) {
				if (current instanceof VariableDeclaration) {
					VariableDeclaration vd = (VariableDeclaration) current;
					if (vd.getVariable().compareTo(v.toString()) == 0) {
						if (!vd.getModifiers().contains(ModifierList.FINAL)) return false;
						if (vd.getInitializer() instanceof Literal) {
							Literal l = (Literal) vd.getInitializer();
							if (l.getLiteralType() == Literal.BOOLEAN) return true;
						}
					}
				} else if (current instanceof StatementList) {
					StatementList stList = (StatementList) current;
					for (int s = 0; s < stList.size(); s++) {
						Statement cst = stList.get(s);
						if (cst == limit) {
							break;
						}
						if (cst instanceof VariableDeclaration) {
							VariableDeclaration vd = (VariableDeclaration) cst;
							if (vd.getVariable().compareTo(v.toString()) == 0) {
								if (!vd.getModifiers().contains(ModifierList.FINAL)) return false;
								if (vd.getInitializer() instanceof Literal) {
									Literal l = (Literal) vd.getInitializer();
									if (l.getLiteralType() == Literal.BOOLEAN) return true;
								}
							}
						}
					}
				}
				if (!(current instanceof StatementList))
					limit = current;
				current = current.getParent();
			}
			return false;
		} else if (o instanceof OJField || o instanceof FieldAccess) {
			try {
				OJField f = null;
				if (o instanceof FieldAccess) {
					f = getFieldDeclaration((FieldAccess)o);
				} else {
					f = (OJField) o;
				}
				if (f == null) return false;
				List<OJField> finalFields = getFinalFields();
				for (OJField ff : finalFields) {
					if (f.equals(ff)) return true;
				}
				return false;
			} catch (CannotAlterException | OJClassNotFoundException | ParseTreeException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private List<OJField> getFinalFields() throws ParseTreeException, CannotAlterException, OJClassNotFoundException {
		if (this.finalFieldsCache != null) return this.finalFieldsCache;
		List<OJField> result = new LinkedList<>();
		for (OJField f : getAllFields(getSelfType(), ALLOW_NON_STATIC+ALLOW_STATIC+ALLOW_PRIVATE+ONLY_FINAL)) {
			FieldDeclaration fd = f.getSourceCode();
			VariableDeclarator vd = fd.getVariableDeclarator();
			if (vd != null) {
				VariableInitializer vi = vd.getInitializer();
				if (vi != null && vi instanceof Expression) {
					Expression initValue = (Expression) vi;
					OJClass type = getType(initValue);
					if (type.getSimpleName().compareToIgnoreCase("boolean") == 0) {
						result.add(f);
					}
				}
			}
			this.finalFieldsCache = result;
		}
		return result;
	}
	
	private OJField getFieldDeclaration(FieldAccess fa) throws ParseTreeException {
		OJField[] declaredFields = getSelfType().getDeclaredFields();
		OJField[] inheritedFields = getInheritedFields(getSelfType(), ALLOW_FINAL + IGNORE_PROTECTED + ALLOW_PROTECTED_INHERITED);
		OJField res = null;
		if (( res = findField(fa, declaredFields)) != null) {
			return res;
		} else {
			return findField(fa, inheritedFields);
		}
	}
	
	private OJField findField(FieldAccess m, OJField[] fields) throws ParseTreeException {
		OJClass mType = getType(m);
		String mName = m.getName();
		Map<String, OJField> fieldDeclarationsPerClass = null;
		if (this.fieldDeclarationCache.containsKey(mType)) {
			fieldDeclarationsPerClass = this.fieldDeclarationCache.get(mType);
		} else {
			fieldDeclarationsPerClass = new TreeMap<>();
			this.fieldDeclarationCache.put(mType, fieldDeclarationsPerClass);
		}
		if (fieldDeclarationsPerClass.containsKey(mName)) {
			return fieldDeclarationsPerClass.get(mName);
		}
		for (OJField im : fields) {
			String imName = im.getName();
			if (compareNamesWithoutPackage(imName, mName)) {
				OJClass imType = im.getType();
				if (mType.getName().compareTo(imType.getName())==0) {
					fieldDeclarationsPerClass.put(mName, im);
					return im;
				}
			}
		}
		return null;
	}

}
