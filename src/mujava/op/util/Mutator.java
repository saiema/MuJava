////////////////////////////////////////////////////////////////////////////
// Module : CallAnalyzer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.util;

import mujava.*;
import mujava.api.Api;
import mujava.api.Mutation;
import mujava.api.MutationOperator;
import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import openjava.mop.*;
import openjava.ptree.*;
import openjava.ptree.ParseTree.COPY_SCOPE;

/**
 * <p>
 * File Analyzer for generating mutants
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class Mutator extends mujava.openjava.extension.VariableBinder {
	protected static final int LAST_STATEMENT = Integer.MAX_VALUE;
	protected static final int ALLOW_PUBLIC = 1;
	protected static final int ALLOW_PRIVATE = 2;
	protected static final int ALLOW_STATIC = 4;
	protected static final int ALLOW_NON_STATIC = 8;
	protected static final int ALLOW_FINAL = 16;
	protected static final int INHERITED = 32;
	protected static final int ALLOW_VOID = 64;
	protected static final int ALLOW_PARAMS = 128;
	protected static final int VARIABLES = 256;
	protected static final int TARGET_IS_NULL = 512;
	protected static final int TARGET_IS_MUTATED_CLASS_OBJECT = 1024;
	protected static final int IGNORE_PROTECTED = 2048;
	protected static final int ALLOW_PROTECTED_INHERITED = 4096;
//	protected static final int ALLOW_PACKAGED = 1024;
//	protected static final int ALLOW_PROTECTED = 2048;
	private static boolean verbose = false;
	
	protected Map<String, List<String>> prohibitedMethodsPerClass = null;
	
	protected List<Pattern> prohibitedMethods = null;
	
	protected Map<String, List<String>> prohibitedFieldsPerClass = null;
	
	protected List<Pattern> prohibitedFields = null;
	
	public int num = 0;
	public CompilationUnit comp_unit = null;
	// -------------------------------------
	public FileEnvironment file_env = null;

	public Mutator(Environment env, CompilationUnit comp_unit) {
		super(env);
		this.comp_unit = comp_unit;
	}

	// --------------
	public OJClass getType(Expression p) throws ParseTreeException {
		OJClass result = null;
		try {
			result = p.getType(getEnvironment());
		} catch (Exception e) {
			throw new ParseTreeException(e);
		}

		if (result == null) {
			System.err.println("cannot resolve the type of expression");
			System.err.println(p.getClass() + " : " + p);
			System.err.println("From statement: " + getStatement((ParseTreeObject)p).toFlattenString());
			System.err.println(getEnvironment());
			/***** DebugOut.println(getEnvironment().toString()); */
			if (p instanceof ArrayAccess) {
				ArrayAccess aaexpr = (ArrayAccess) p;
				Expression refexpr = aaexpr.getReferenceExpr();
				OJClass refexprtype = null;
				OJClass comptype = null;
				try {
					refexprtype = refexpr.getType(getEnvironment());
					comptype = refexprtype.getComponentType();
				} catch (Exception ex) {
					// do nothing
				}
				System.err.println(refexpr + " : " + refexprtype + " : "
						+ comptype);
			}
		}
		return result;
	}

	protected OJClass getSelfType() throws ParseTreeException {
		OJClass result;
		try {
			Environment env = getEnvironment();
			String selfname = env.currentClassName();
			result = env.lookupClass(selfname);
		} catch (Exception ex) {
			throw new ParseTreeException(ex);
		}
		return result;
	}

	protected OJClass getType(TypeName typename) throws ParseTreeException {
		OJClass result = null;
		try {
			Environment env = getEnvironment();
			String qname = env.toQualifiedName(typename.toString());
			result = env.lookupClass(qname);
		} catch (Exception ex) {
			throw new ParseTreeException(ex);
		}

		if (result == null) {
			System.err.println("unknown type for a type name : " + typename);
		}
		return result;
	}

	protected OJClass computeRefType(TypeName typename, Expression expr)
			throws ParseTreeException {
		if (typename != null)
			return getType(typename);

		if (expr != null)
			return getType(expr);

		return getSelfType();
	}

	/**
	 * checks if a class is declared inside another
	 * 
	 * @param a : the class that could contain {@code b} : {@code OJClass}
	 * @param b : the class that could be contained in {@code a} : {@code OJClass}
	 * @param options : options used by this method, the options used by this method are:
	 * <p><li>ALLOW_PRIVATE: will include private inner classes while checking</li><p> : {@code int}
	 * @return {@code true} iff {@code b} is declared inside {@code a} :
	 *         {@code boolean}
	 */
	protected boolean isInnerClassOf(OJClass a, OJClass b, int options) {
		boolean ignorePrivateClasses = (ALLOW_PRIVATE & options) == 0;
		OJClass[] aClasses = a.getDeclaredClasses();
		for (OJClass ac : aClasses) {
			if (((ignorePrivateClasses && !ac.getModifiers().isPrivate() || !ignorePrivateClasses))
					&& ac.getName().compareTo(b.getName()) == 0) {
				return true;
			}
		}
		return false;
	}

	// -----------------
	/**
	 * Return a class name
	 */
	public String getClassName() {
		Class<?> cc = this.getClass();
		return exclude(cc.getName(), cc.getPackage().getName());
	}

	/**
	 * Remove a portion of string from a specific position
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public String exclude(String a, String b) {
		return a.substring(b.length() + 1, a.length());
	}

	/**
	 * Return an ID of a mutant
	 * 
	 * @return
	 */
	public String getMutantID() {
		String str = getClassName() + "_" + this.num;
		return str;
	}

	/**
	 * Return an ID of a given operator name
	 * 
	 * @param op_name
	 * @return
	 */
	public String getMutantID(String op_name) {
		String str = op_name + "_" + this.num;
		return str;
	}

	/**
	 * Return the source's file name
	 * 
	 * @param op_name
	 * @return
	 */
	public String getSourceName(String op_name) {
		// make directory for the mutant
		String dir_name = MutationSystem.MUTANT_PATH + "/" + op_name + "_"
				+ this.num;
		File f = new File(dir_name);
		f.mkdir();

		// return file name
		String name;
		name = dir_name + "/" + MutationSystem.CLASS_NAME + ".java";
		return name;
	}

	/**
	 * Return file name
	 * 
	 * @param clazz
	 * @return
	 */
	public String getSourceName(Mutator clazz) {
		// make directory for the mutant
		String dir_name = MutationSystem.MUTANT_PATH + "/" + getClassName()
				+ "_" + this.num;
		File f = new File(dir_name);
		f.mkdir();

		// return file name
		String name;
		name = dir_name + "/" + MutationSystem.CLASS_NAME + ".java";
		return name;
	}

	public PrintWriter getPrintWriter(String f_name) throws IOException {
		File outfile = new File(f_name);
		FileWriter fout = new FileWriter(outfile);
		PrintWriter out = new PrintWriter(fout);
		return out;
	}

	/**
	 * Determine whether two methods return the same type
	 * 
	 * @param m1
	 * @param m2
	 * @return true - same type
	 */
	public boolean sameReturnType(OJMethod m1, OJMethod m2) {
		OJClass c1 = m1.getReturnType();
		OJClass c2 = m2.getReturnType();
		return (c1.equals(c2));
	}

	public void visit(MethodDeclaration p) throws ParseTreeException {
		if (!(p.getName().equals("main")))
			super.visit(p);
	}

	/**
	 * Determine whether two method have the same parameter type
	 * 
	 * @param m1
	 * @param m2
	 * @return true - same type
	 */
	public boolean compatibleMethods(OJMethod m1, OJMethod m2) {
		OJClass[] c1 = m1.getParameterTypes();
		OJClass[] c2 = m2.getParameterTypes();

		if (c1.length == c2.length) {
			for (int i = 0; i < c1.length; i++) {
				if (!(c1[i].equals(c2[i])))
					return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public static int getMutationsLeft(ParseTreeObject nl) {
		if (!Api.useMutGenLimit()) return Integer.MAX_VALUE;
		ParseTreeObject parent = nl;
		while (parent != null) {
			if (parent instanceof ExpressionStatement) {
				if (((ExpressionStatement) parent).hasMutGenLimit())
					return ((ExpressionStatement) parent).getMutGenLimit();
				else
					return -1;
			}
			if (parent instanceof IfStatement) {
				if (((IfStatement) parent).hasMutGenLimit())
					return ((IfStatement) parent).getMutGenLimit();
				else
					return -1;
			}
			if (parent instanceof WhileStatement) {
				if (((WhileStatement) parent).hasMutGenLimit())
					return ((WhileStatement) parent).getMutGenLimit();
				else
					return -1;
			}
			if (parent instanceof DoWhileStatement) {
				if (((DoWhileStatement) parent).hasMutGenLimit())
					return ((DoWhileStatement) parent).getMutGenLimit();
				else
					return -1;
			}
			if (parent instanceof ForStatement) {
				if (((ForStatement) parent).hasMutGenLimit())
					return ((ForStatement) parent).getMutGenLimit();
				else
					return -1;
			}
			if (parent instanceof VariableDeclaration) {
				if (((VariableDeclaration) parent).hasMutGenLimit())
					return ((VariableDeclaration) parent).getMutGenLimit();
				else
					return -1;
			}
			if (parent instanceof ReturnStatement) {
				if (((ReturnStatement) parent).hasMutGenLimit())
					return ((ReturnStatement) parent).getMutGenLimit();
				else
					return -1;
			}
			if (parent instanceof MethodDeclaration) {
				if (((MethodDeclaration) parent).hasMutGenLimit())
					return ((MethodDeclaration) parent).getMutGenLimit();
				else
					return -1;
			}
			if (parent instanceof ConstructorDeclaration) {
				if (((ConstructorDeclaration) parent).hasMutGenLimit())
					return ((ConstructorDeclaration) parent).getMutGenLimit();
				else
					return -1;
			}
			if (parent instanceof FieldDeclaration) {
				if (((FieldDeclaration) parent).hasMutGenLimit())
					return ((FieldDeclaration) parent).getMutGenLimit();
				else
					return -1;
			}
			if (parent instanceof ClassDeclaration) {
				if (((ClassDeclaration) parent).hasMutGenLimit())
					return ((ClassDeclaration) parent).getMutGenLimit();
				else
					return -1;
			}
			if (parent instanceof ThrowStatement) {
				if (((ThrowStatement) parent).hasMutGenLimit())
					return ((ThrowStatement) parent).getMutGenLimit();
				else
					return -1;
			}
			parent = parent.getParent();
		}
		return -1;
	}

	public static ParseTreeObject getMutationsLimitParent(ParseTreeObject nl) {
		if (!Api.useMutGenLimit()) return null;
		ParseTreeObject parent = nl;
		while (parent != null) {
			if (parent instanceof ExpressionStatement) {
				if (((ExpressionStatement) parent).hasMutGenLimit())
					return ((ExpressionStatement) parent);
				else
					return null;
			}
			if (parent instanceof IfStatement) {
				if (((IfStatement) parent).hasMutGenLimit())
					return ((IfStatement) parent);
				else
					return null;
			}
			if (parent instanceof WhileStatement) {
				if (((WhileStatement) parent).hasMutGenLimit())
					return ((WhileStatement) parent);
				else
					return null;
			}
			if (parent instanceof DoWhileStatement) {
				if (((DoWhileStatement) parent).hasMutGenLimit())
					return ((DoWhileStatement) parent);
				else
					return null;
			}
			if (parent instanceof ForStatement) {
				if (((ForStatement) parent).hasMutGenLimit())
					return ((ForStatement) parent);
				else
					return null;
			}
			if (parent instanceof VariableDeclaration) {
				if (((VariableDeclaration) parent).hasMutGenLimit())
					return ((VariableDeclaration) parent);
				else
					return null;
			}
			if (parent instanceof ReturnStatement) {
				if (((ReturnStatement) parent).hasMutGenLimit())
					return ((ReturnStatement) parent);
				else
					return null;
			}
			if (parent instanceof MethodDeclaration) {
				if (((MethodDeclaration) parent).hasMutGenLimit())
					return ((MethodDeclaration) parent);
				else
					return null;
			}
			if (parent instanceof ConstructorDeclaration) {
				if (((ConstructorDeclaration) parent).hasMutGenLimit())
					return ((ConstructorDeclaration) parent);
				else
					return null;
			}
			if (parent instanceof FieldDeclaration) {
				if (((FieldDeclaration) parent).hasMutGenLimit())
					return ((FieldDeclaration) parent);
				else
					return null;
			}
			if (parent instanceof ClassDeclaration) {
				if (((ClassDeclaration) parent).hasMutGenLimit())
					return ((ClassDeclaration) parent);
				else
					return null;
			}
			parent = parent.getParent();
		}
		return null;
	}

	public Map<OJClass, List<Variable>> getReachableVariables(ParseTreeObject exp) throws ParseTreeException {
		Statement previousStatement = (Statement)getStatement(exp, -1);
		Map<OJClass, List<Variable>> variables = new HashMap<OJClass, List<Variable>>();
		List<Variable> allVars = new LinkedList<Variable>();
		getReachableVariables(previousStatement, variables, allVars);
		getMethodDeclarationVariables(exp, variables, allVars);
		return variables;
	}
	
	public Map<OJClass, List<Variable>> getReachableVariables(Statement exp) throws ParseTreeException {
		Map<OJClass, List<Variable>> variables = new HashMap<OJClass, List<Variable>>();
		List<Variable> allVars = new LinkedList<Variable>();
		getReachableVariables(exp, variables, allVars);
		getMethodDeclarationVariables((ParseTreeObject) exp, variables, allVars);
		return variables;
	}
	
	private void getReachableVariables(Statement exp, Map<OJClass, List<Variable>> variables, List<Variable> allVars ) throws ParseTreeException {
		addForVariables((ParseTreeObject) exp, variables, allVars);
		ParseTreeObject current = (ParseTreeObject) exp;
		ParseTreeObject limit = current;
		while (current != null && !(current instanceof MethodDeclaration)) {
			if (current instanceof VariableDeclarator) {
				addVar(variables, allVars, new Variable(((VariableDeclarator) current).getVariable()));
			} else if (current instanceof VariableDeclaration) {
				addVar(variables, allVars, new Variable(((VariableDeclaration) current).getVariable()));
			} else if (current instanceof ForStatement) {
				VariableDeclarator[] vds = ((ForStatement) current).getInitDecls();
				if (vds != null) {
					for (VariableDeclarator vd : vds) {
						addVar(variables, allVars, new Variable(vd.getVariable()));
					}
				}
			} else if (current instanceof StatementList) {
				StatementList stList = (StatementList) current;
				for (int s = 0; s < stList.size(); s++) {
					Statement cst = stList.get(s);
					if (cst == limit) {
						break;
					}
					if (cst instanceof VariableDeclarator) {
						addVar(variables, allVars, new Variable(
								((VariableDeclarator) cst).getVariable()));
					} else if (cst instanceof VariableDeclaration) {
						addVar(variables, allVars, new Variable(
								((VariableDeclaration) cst).getVariable()));
					}
				}
			}
			if (!(current instanceof StatementList))
				limit = current;
			current = current.getParent();
		}
	}
	
	private void getMethodDeclarationVariables(ParseTreeObject exp, Map<OJClass, List<Variable>> variables, List<Variable> allVars) throws ParseTreeException {
		ParseTreeObject methodDeclaration = getMethodDeclaration(exp);
		if (methodDeclaration != null) {
			ParameterList params = ((MethodDeclaration) methodDeclaration)
					.getParameters();
			for (int p = 0; p < params.size(); p++) {
				Parameter param = params.get(p);
				addVar(variables, allVars, new Variable(param.getVariable()));
			}
		}
	}
	

	private void addForVariables(ParseTreeObject exp, Map<OJClass, List<Variable>> variables, List<Variable> allVars) throws ParseTreeException {
		ParseTreeObject current = getStatement(exp);
		if (current instanceof ForStatement) {
			VariableDeclarator[] init = ((ForStatement) current).getInitDecls();
			boolean found = false;
			for (int i = 0; init != null && i < init.length && !found; i++) {
				VariableDeclarator vd = init[i];
				if (vd == exp) {
					found = true;
				} else {
					ParseTreeObject curr = current;
					while (curr != null && !found
							&& !(curr instanceof VariableDeclarator)) {
						if (curr instanceof Statement) {
							curr = null;
						} else {
							found = vd == exp;
							curr = curr.getParent();
						}
					}
				}
			}
			if (!found) {
				VariableDeclarator[] vds = ((((ForStatement) current).getInitDecls()));
				if (vds != null) {
					for (VariableDeclarator vd : vds) {
						addVar(variables, allVars, new Variable(vd.getVariable()));
					}
				}
			}
		}
	}

	private void addVar(Map<OJClass, List<Variable>> map, List<Variable> allVars, Variable var) throws ParseTreeException {
		if (!find(allVars, var)) {
			OJClass varType = getType(var);
			List<Variable> sameTypeVars = map.get(varType);
			if (sameTypeVars == null) {
				sameTypeVars = new LinkedList<Variable>();
				sameTypeVars.add(var);
				map.put(varType, sameTypeVars);
			} else if (!find(sameTypeVars, var)) {
				sameTypeVars.add(var);
			}
			allVars.add(var);
		}
	}

	private boolean find(List<Variable> vars, Variable var) {
		for (Variable v : vars) {
			if (v.toString().compareTo(var.toString()) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines whether two given types are compatible for assignment.
	 * 
	 * @param p
	 *            The type on the left side of the assignment
	 * @param c
	 *            The type on the right side of the assignment
	 * @return true if the types are compatible, false otherwise
	 */
	public boolean compatibleAssignTypeStrict(OJClass p, OJClass c) {
		return compatibleAssignTypeStrict(p, c, true);
	}

	/**
	 * Determines whether two given types are compatible for assignment.
	 * 
	 * @param p
	 *            The type on the left side of the assignment
	 * @param c
	 *            The type on the right side of the assignment
	 * @return true if the types are compatible, false otherwise
	 */
	public boolean compatibleAssignTypeStrict(OJClass p, OJClass c, boolean allowWrappers) {
		if (p == null) {// || c == null ){
			return false;
		}

		if (c == null || c.toString().compareTo("<type>null") == 0) {
			return p.isPrimitive() ? false : true;
		}

		if (p.toString().compareTo("<type>null") == 0) {
			return c == null || c.toString().compareTo("<type>null") == 0 || !c.isPrimitive();
		}

		if ("void".equalsIgnoreCase(c.getName())) {
			return false;
		}

		/* Supporting java auto-boxing */
		if (!p.isArray()
				&& !c.isArray()
				&& ((p.isPrimitive() && c.isPrimitiveWrapper()) || (p
						.isPrimitiveWrapper() && c.isPrimitive()))) {
			return p.unwrappedPrimitive().getName().compareTo(c.unwrappedPrimitive().getName()) == 0;
		}
		if (c.isPrimitive() && !p.isPrimitive()) {
			c = c.primitiveWrapper();
		}
		return p.getName().compareTo(c.getName())==0;
	}
	
	public boolean compatibleAssignTypeRelaxed(OJClass p, OJClass c) {
		return compatibleAssignTypeRelaxed(p, c, true);
	}
	
	public boolean compatibleAssignTypeRelaxed(OJClass p, OJClass c, boolean allowWrappers) {
		if (p == null) {// || c == null ){
			return false;
		}

		if (c == null || c.toString().compareTo("<type>null") == 0) {
			return !p.isPrimitive();
		}

		if (p.toString().compareTo("<type>null") == 0) {
			return c == null || c.toString().compareTo("<type>null") == 0 || !c.isPrimitive();
		}

		if ("void".equalsIgnoreCase(c.getName())) {
			return false;
		}

		// special case for assignments (Object = primitive)
		if (p.getName().compareToIgnoreCase("java.lang.object") == 0
				&& (c.isPrimitive() && allowWrappers)) {
			return true;
		}

		/* Supporting java auto-boxing */
		if (!p.isArray()
				&& !c.isArray()
				&& ((p.isPrimitive() && c.isPrimitiveWrapper()) || (p
						.isPrimitiveWrapper() && c.isPrimitive()))) {
			return (p.unwrappedPrimitive()).isAssignableFrom(c.unwrappedPrimitive());
		}
		if (c.isPrimitive() && !p.isPrimitive()) {
			c = c.primitiveWrapper();
		}
		return p.isAssignableFrom(c);
	}
	
	/**
	 * Determines whether a type inherits from another
	 * 
	 * @param a : the sub type
	 * @param b : the super type
	 * @return {@code true} if {@code a} inherits from {@code b}
	 */
	public boolean inherits(OJClass a, OJClass b) {
		if (a == null || b == null) return false;
		if (a.isPrimitive() || b.isPrimitive()) return false;
		if (a.isArray() || b.isArray()) return false;
		return b.isAssignableFrom(a);
	}

	/**
	 * 
	 * @param clazz : the class from which inherited methods will be searched : {@code OJClass}
	 * @return all non final inherited methods in {@code clazz}
	 */
	public OJMethod[] getInheritedMethods(OJClass clazz) {
		return getInheritedMethods(clazz, ALLOW_PROTECTED_INHERITED);
	}
	
	/**
	 * 
	 * @param clazz : the class from which inherited constructors will be searched : {@code OJClass}
	 * @return all non final inherited constructors in {@code clazz}
	 */
	public OJConstructor[] getInheritedConstructors(OJClass clazz) {
		return getInheritedConstructors(clazz, ALLOW_PROTECTED_INHERITED);
	}

	
	/**
	 * 
	 * @param clazz	  : the class from which inherited methods will be searched : {@code OJClass}
	 * @param options : options used by this method, the options used by this method are:
	 * <p><li>ALLOW_FINAL: will include methods with the final modifier</li><p> : {@code int}
	 * @return all inherited methods in {@code clazz}
	 */
	public OJMethod[] getInheritedMethods(OJClass clazz, int options) {
		boolean filterFinal = (options & ALLOW_FINAL) == 0;
//		boolean allowPackaged = (options & ALLOW_PACKAGED) != 0;
//		boolean allowProtected = (options & ALLOW_PROTECTED_INHERITED) != 0;
		boolean target_is_null = (options & TARGET_IS_NULL) != 0;
		boolean target_is_class_to_mutate = (options & TARGET_IS_MUTATED_CLASS_OBJECT) != 0;
		boolean allowProtected = target_is_null || target_is_class_to_mutate || ((options & IGNORE_PROTECTED) != 0);
		OJClass self = null;
		try {
			self = getSelfType();
		} catch (ParseTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OJClass base = clazz.getSuperclass();
		Map<Signature, OJMethod> table = new HashMap<Signature, OJMethod>();
		while (base != null && (options & ALLOW_PROTECTED_INHERITED) != 0) {
			OJMethod[] declaredMethods = filterPrivate(base.getDeclaredMethods(), filterFinal);
			for (int m = 0; m < declaredMethods.length; m++) {
				if (declaredMethods[m].getModifiers().isProtected() && !allowProtected) continue;
				if (declaredMethods[m].getModifiers().isPackaged() && (!base.isInSamePackage(self) && !isInnerClassOf(clazz, base, 0))) {
					continue;
				}
				if (!table.containsKey(declaredMethods[m].signature()))
					table.put(declaredMethods[m].signature(), declaredMethods[m]);
			}
			base = base.getSuperclass();
		}
		return table.values().toArray(new OJMethod[table.size()]);
	}
	
	/**
	 * 
	 * @param clazz	  : the class from which inherited constructors will be searched : {@code OJClass}
	 * @param options : options used by this method, the options used by this method are:
	 * <p><li>ALLOW_FINAL: will include constructors with the final modifier</li><p> : {@code int}
	 * @return all inherited constructors in {@code clazz}
	 */
	public OJConstructor[] getInheritedConstructors(OJClass clazz, int options) {
		boolean filterFinal = (options & ALLOW_FINAL) == 0;
		OJClass base = clazz.getSuperclass();
		Map<Signature, OJConstructor> table = new HashMap<Signature, OJConstructor>();
		while (base != null && (options & ALLOW_PROTECTED_INHERITED) != 0) {
			OJConstructor[] declaredConstructors = filterPrivate(base.getDeclaredConstructors(), filterFinal);
			for (int m = 0; m < declaredConstructors.length; m++) {
				if (!table.containsKey(declaredConstructors[m].signature()))
					table.put(declaredConstructors[m].signature(), declaredConstructors[m]);
			}
			base = base.getSuperclass();
		}
		return table.values().toArray(new OJConstructor[table.size()]);
	}

	private OJMethod[] filterPrivate(OJMethod[] methods, boolean filterFinal) {
		List<OJMethod> filtered = new LinkedList<OJMethod>();
		for (OJMethod m : methods) {
			if ((filterFinal && m.getModifiers().isFinal())
					|| m.getModifiers().isPrivate()) {
				continue;
			}
			filtered.add(m);
		}
		return filtered.toArray(new OJMethod[filtered.size()]);
	}
	
	private OJConstructor[] filterPrivate(OJConstructor[] constructors, boolean filterFinal) {
		List<OJConstructor> filtered = new LinkedList<OJConstructor>();
		for (OJConstructor c : constructors) {
			if ((filterFinal && c.getModifiers().isFinal())
					|| c.getModifiers().isPrivate()) {
				continue;
			}
			filtered.add(c);
		}
		return filtered.toArray(new OJConstructor[filtered.size()]);
	}

	/**
	 * 
	 * @param clazz : the class from which inherited fields will be searched : {@code OJClass}
	 * @return all non final inherited fields in {@code clazz}
	 */
	public OJField[] getInheritedFields(OJClass clazz) {
		return getInheritedFields(clazz, ALLOW_PROTECTED_INHERITED);
	}

	/**
	 * 
	 * @param clazz	  : the class from which inherited fields will be searched : {@code OJClass}
	 * @param options : options used by this method, the options used by this method are:
	 * <p><li>ALLOW_FINAL: will include fields with the final modifier</li><p> : {@code int}
	 * @return all inherited fields in {@code clazz}
	 */
	public OJField[] getInheritedFields(OJClass clazz, int options) {
		OJClass base = clazz.getSuperclass();
		Map<Signature, OJField> table = new HashMap<Signature, OJField>();
		boolean target_is_null = (options & TARGET_IS_NULL) != 0;
		boolean target_is_class_to_mutate = (options & TARGET_IS_MUTATED_CLASS_OBJECT) != 0;
		boolean allowProtected = target_is_null || target_is_class_to_mutate || ((options & IGNORE_PROTECTED) != 0);
		OJClass self = null;
		try {
			self = getSelfType();
		} catch (ParseTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (base != null && (options & ALLOW_PROTECTED_INHERITED) != 0) {
			OJField[] declaredFields = filterPrivate(base.getDeclaredFields(), options);
			for (int f = 0; f < declaredFields.length; f++) {
				if (declaredFields[f].getModifiers().isProtected() && !allowProtected) continue;
				if (declaredFields[f].getModifiers().isPackaged() && (!base.isInSamePackage(self) && !isInnerClassOf(clazz, base, 0))) {
					continue;
				}
				if (!table.containsKey(declaredFields[f].signature()))
					table.put(declaredFields[f].signature(), declaredFields[f]);
			}
			base = base.getSuperclass();
		}
		return table.values().toArray(new OJField[table.size()]);
	}

	private OJField[] filterPrivate(OJField[] fields, int options) {
		boolean filterFinal = (options & ALLOW_FINAL) == 0;
		List<OJField> filtered = new LinkedList<OJField>();
		for (OJField f : fields) {
			if ((filterFinal && f.getModifiers().isFinal()) || f.getModifiers().isPrivate()) {
				continue;
			}
			filtered.add(f);
		}
		return filtered.toArray(new OJField[filtered.size()]);
	}

	/**
	 * 
	 * @param clazz	  : the class from which fields will be searched : {@code OJClass}
	 * @param options : options used by this method, the options used by this method are:
	 * <p><li>ALLOW_NON_STATIC: will include fields without the static modifier</li><p> : {@code int}
	 * @return all fields in {@code clazz}
	 */
	public OJField[] getAllFields(OJClass clazz, int options) {
		boolean allowNonStatic = (options & ALLOW_NON_STATIC) > 0;
		boolean allowPrivate = (options & ALLOW_PRIVATE) > 0;
		boolean target_is_null = (options & TARGET_IS_NULL) != 0;
		boolean target_is_class_to_mutate = (options & TARGET_IS_MUTATED_CLASS_OBJECT) != 0;
		boolean allowProtected = target_is_null || target_is_class_to_mutate;
		OJClass self = null;
		try {
			self = getSelfType();
		} catch (ParseTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean isInnerClass = self==null?false:isInnerClassOf(self, clazz, 0);
		boolean sameClass = self==null?false:self.getName().compareTo(clazz.getName()) == 0;
		OJField[] declaredFields = clazz.getDeclaredFields();
		Map<Signature, OJField> table = new HashMap<Signature, OJField>();
		for (OJField f : declaredFields) {
			if (f.getModifiers().isPrivate() && !allowPrivate) {
				continue;
			}
			if (!isFieldAllowed(f)) {
				continue;
			}
			boolean isNonStatic = !f.getModifiers().isStatic();
			if (!allowNonStatic && isNonStatic) {
				continue;
			}
			if (f.getModifiers().isProtected() && !allowProtected) continue;
			if (f.getModifiers().isPackaged()) {
				if ((!sameClass && !isInnerClass) || (isInnerClass && target_is_null)) continue;
			}
			table.put(f.signature(), f);
		}
		OJField[] inheritedFields = getInheritedFields(clazz, options);
		for (OJField f : inheritedFields) {
			if (!isFieldAllowed(f)) {
				continue;
			}
			boolean isNonStatic = !f.getModifiers().isStatic();
			if (!allowNonStatic && isNonStatic) {
				continue;
			}
			if (!table.containsKey(f.signature()))
				table.put(f.signature(), f);
		}
		return table.values().toArray(new OJField[table.size()]);
	}

	/**
	 * 
	 * @param clazz	  : the class from which fields will be searched : {@code OJClass}
	 * @return all fields in {@code clazz} including those with the static modifier
	 */
	public OJField[] getAllFields(OJClass clazz) {
		return getAllFields(clazz, ALLOW_NON_STATIC + ALLOW_PROTECTED_INHERITED);
	}

	/**
	 * 
	 * @param clazz	  : the class from which methods will be searched : {@code OJClass}
	 * @param options : options used by this method, the options used by this method are:
	 * <p><li>ALLOW_NON_STATIC: will include methods without the static modifier</li><p> : {@code int}
	 * @return all methods in {@code clazz}
	 */
	public OJMethod[] getAllMethods(OJClass clazz, int options) {
		boolean allowNonStatic = (options & ALLOW_NON_STATIC) > 0;
		boolean allowPrivate = (options & ALLOW_PRIVATE) > 0;
		boolean target_is_null = (options & TARGET_IS_NULL) != 0;
		boolean target_is_class_to_mutate = (options & TARGET_IS_MUTATED_CLASS_OBJECT) != 0;
		boolean allowProtected = target_is_null || target_is_class_to_mutate;
		OJClass self = null;
		try {
			self = getSelfType();
		} catch (ParseTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean isInnerClass = self==null?false:isInnerClassOf(self, clazz, 0);
		boolean sameClass = self==null?false:self.getName().compareTo(clazz.getName()) == 0;
		OJMethod[] declaredMethods = clazz.getDeclaredMethods();
		Map<Signature, OJMethod> table = new HashMap<Signature, OJMethod>();
		for (OJMethod m : declaredMethods) {
			boolean isNonStatic = !m.getModifiers().isStatic();
			if (m.getModifiers().isPrivate() && !allowPrivate) {
				continue;
			}
			if (!allowNonStatic && isNonStatic) {
				continue;
			}
			if (m.getModifiers().isProtected() && !allowProtected) continue;
			if (m.getModifiers().isPackaged()) {
				if ((!sameClass && !isInnerClass) || (isInnerClass && target_is_null)) continue;
			}
			table.put(m.signature(), m);
		}
		OJMethod[] inheritedMethods = getInheritedMethods(clazz, options);
		for (OJMethod m : inheritedMethods) {
			boolean isNonStatic = !m.getModifiers().isStatic();
			if (!allowNonStatic && isNonStatic) {
				continue;
			}
			if (!table.containsKey(m.signature()))
				table.put(m.signature(), m);
		}
		return table.values().toArray(new OJMethod[table.size()]);
	}
	
	/**
	 * 
	 * @param clazz	  : the class from which constructors will be searched : {@code OJClass}
	 * @return all constructor in {@code clazz}
	 */
	public OJConstructor[] getAllConstructors(TypeName clazz) {
		OJClass constructorClass;
		try {
			constructorClass = OJClass.forName(clazz.getName());
		} catch (OJClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new OJConstructor[]{};
		}
		OJConstructor[] declaredConstructors = constructorClass.getDeclaredConstructors();
		Map<Signature, OJConstructor> table = new HashMap<Signature, OJConstructor>();
		for (OJConstructor c : declaredConstructors) {
			table.put(c.signature(), c);
		}
		OJConstructor[] inheritedConstructors = getInheritedConstructors(constructorClass);
		for (OJConstructor c : inheritedConstructors) {
			if (!table.containsKey(c.signature()))
				table.put(c.signature(), c);
		}
		return table.values().toArray(new OJConstructor[table.size()]);
	}

	/**
	 * 
	 * @param clazz	  : the class from which methods will be searched : {@code OJClass}
	 * @return all methods in {@code clazz} including those with the static modifier
	 */
	public OJMethod[] getAllMethods(OJClass clazz) {
		return getAllMethods(clazz, ALLOW_NON_STATIC + ALLOW_PROTECTED_INHERITED);
	}

	/**
	 * @param limit	  : variables will be added only if declared before this node	:	{@code ParseTreeObject}
	 * @param t	  : the class from which fields and methods will be searched 	: 	{@code OJClass}
	 * @param options : options used by this method, the options used by this method are:
	 * <p>
	 * <li>VARIABLES: will include declared variables in the result</li>
	 * <li>ALLOW_VOID: will include methods with a void return value</li>
	 * <li>ALLOW_PARAMS: will include methods with parameters</li>
	 * <li>ALLOW_NON_STATIC: will include methods and fields without the static modifier</li>
	 * <li>IGNORE_PROTECTED: will ignore methods and fields that are protected coming from a super class</li><p>
	 * @return all methods and fields in {@code clazz}, optionally the result will include declared variables
	 */
	public List<Object> fieldsMethodsAndVars(ParseTreeObject limit, OJClass t, int options) throws ParseTreeException {
		boolean ignoreVars = (options & VARIABLES) == 0;
		boolean ignoreVoidMethods = (options & ALLOW_VOID) == 0;
		boolean ignoreMethodsWithParams = (options & ALLOW_PARAMS) == 0;
		
		List<Object> result = new LinkedList<Object>();
		if (t != null) {
			if (!ignoreVoidMethods && !ignoreMethodsWithParams) {
				result.addAll(Arrays.asList(getAllMethods(t, options)));
			} else {
				OJMethod[] allMethods = getAllMethods(t, options);
				for (OJMethod m : allMethods) {
					if (!isMethodAllowed(m)) {
						continue;
					}
					if (ignoreVoidMethods && m.getReturnType().getName().compareToIgnoreCase("void") == 0) {
						continue;
					}
					if (ignoreMethodsWithParams && m.getParameterTypes().length != 0) {
						continue;
					}
					result.add(m);
				}
			}
			result.addAll(Arrays.asList(getAllFields(t, options)));
		}
		if (!ignoreVars) {
			for (List<Variable> vars : getReachableVariables(limit).values()) {
				result.addAll(vars);
			}
		}
		//checkPrivate(result);
		return result;
	}
	
//	private void checkPrivate(List<Object> elems) {
//		for (Object o : elems) {
//			if (o instanceof OJMethod) {
//				OJMethod om = (OJMethod) o;
//				if (om.getModifiers().isPrivate()) System.out.println("Found private method " + om.toString());
//			} else if (o instanceof OJField) {
//				OJField of = (OJField) o;
//				if (of.getModifiers().isPrivate()) System.out.println("Found private field " + of.toString());
//			} else if (o instanceof Variable) {
//				Variable ov = (Variable) o;
//				System.out.println("Found variable " + ov.toString());
//			} else {
//				System.out.println("Failed to check " + o.toString());
//			}
//		}
//	}
	
	protected boolean isMethodAllowed(OJMethod m) {
		if (this.prohibitedMethodsPerClass != null) {
			return isMethodAllowed_usingMap(m);
		} else if (this.prohibitedMethods != null) {
			return isMethodAllowed_usingList(m);
		}
		return true;
	}
	
	private boolean isMethodAllowed_usingMap(OJMethod m) {
		OJClass declaringClass = m.getDeclaringClass();
		String methodName = m.getName();
		if (this.prohibitedMethodsPerClass.containsKey(declaringClass.getName())) {
			List<String> bannedMethods = this.prohibitedMethodsPerClass.get(declaringClass.getName());
			if (bannedMethods != null && !bannedMethods.isEmpty()) {
				return !bannedMethods.contains(methodName);
			}
		}
		return true;
	}
	
	private boolean isMethodAllowed_usingList(OJMethod m) {
		String declaringClass = m.getDeclaringClass().getName();
		String methodName = m.getName();
		String fullName = declaringClass+"#"+methodName;
		for (Pattern pm : this.prohibitedMethods) {
			if (pm.matcher(fullName).find()) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean isFieldAllowed(OJField f) {
		if (this.prohibitedFieldsPerClass != null) {
			return isFieldAllowed_usingMap(f);
		} else if (this.prohibitedFields != null) {
			return isFieldAllowed_usingList(f);
		}
		return true;
	}
	
	private boolean isFieldAllowed_usingMap(OJField f) {
		OJClass declaringClass = f.getDeclaringClass();
		String fieldName = f.getName();
		if (this.prohibitedFieldsPerClass.containsKey(declaringClass.getName())) {
			List<String> bannedFields = this.prohibitedFieldsPerClass.get(declaringClass.getName());
			if (bannedFields != null && !bannedFields.isEmpty()) {
				return !bannedFields.contains(fieldName);
			}
		}
		return true;
	}
	
	private boolean isFieldAllowed_usingList(OJField f) {
		String declaringClass = f.getDeclaringClass().getName();
		String methodName = f.getName();
		String fullName = declaringClass+"#"+methodName;
		for (Pattern pf : this.prohibitedFields) {
			if (pf.matcher(fullName).find()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This will loop up the type of an expression starting with the first class
	 * in where to look if the type can't be determined then this will look up
	 * in the super class until the type is found or there's no more superclass
	 * to look
	 * 
	 * @param p
	 * @param c
	 * @return
	 */
	public OJClass lookupType(Expression p, OJClass c) {
		OJClass result = null;
		OJClass currentClass = c;
		boolean exceptionOcurred = false;
		do {
			try {
				result = p.getType(env);
			} catch (NoSuchMemberException ex) {
				exceptionOcurred = true;
			} catch (Exception e) {
				exceptionOcurred = true;
			}
			if (exceptionOcurred && result == null) {
				try {
					result = p.getType(currentClass.getEnvironment());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (result == null) {
				currentClass = currentClass.getSuperclass();
			}
		} while (currentClass != null && result == null);
		return result;
	}

	public void bindMethodParams(MethodDeclaration md)
			throws ParseTreeException {
		ParameterList args = md.getParameters();
		for (int a = 0; a < args.size(); a++) {
			Parameter arg = args.get(a);
			try {
				getEnvironment().bindVariable(arg.getVariable(),
						OJClass.forName(arg.getTypeSpecifier().toString()));
			} catch (OJClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Bound variables to their corresponding types in the environment.
	 * 
	 * @param st
	 *            is the statement list for which the local variables are
	 *            calculates
	 * @throws ParseTreeException
	 */
	@SuppressWarnings("rawtypes")
	public void bindLocalVariables(StatementList statList) throws ParseTreeException {
		Enumeration st = statList.elements();
		HashSet<String> result = new HashSet<String>();

		try {
			while (st.hasMoreElements()) {
				Object current = st.nextElement();
				if (VariableDeclaration.class.getName().equals(
						current.getClass().getName())) {
					VariableDeclaration var = (VariableDeclaration) current;
					result.add(var.getVariable());
					getEnvironment().bindVariable(var.getVariable(),
							OJClass.forName(var.getTypeSpecifier().toString()));
				} else if (ForStatement.class.getName().equals(
						current.getClass().getName())) {
					ForStatement f = (ForStatement) current;
					if (f.getInitDeclType() != null) {
						VariableDeclarator[] vars = f.getInitDecls();
						for (int i = 0; i < vars.length; i++) {
							result.add(vars[i].getVariable());
							getEnvironment().bindVariable(
									vars[i].getVariable(),
									OJClass.forName(f.getInitDeclType()
											.toString()));
						}
					}
					bindLocalVariables(f.getStatements());
				} else if (WhileStatement.class.getName().equals(
						current.getClass().getName())) {
					WhileStatement f = (WhileStatement) current;
					bindLocalVariables(f.getStatements());
				} else if (DoWhileStatement.class.getName().equals(
						current.getClass().getName())) {
					DoWhileStatement f = (DoWhileStatement) current;
					bindLocalVariables(f.getStatements());
				} else if (IfStatement.class.getName().equals(
						current.getClass().getName())) {
					IfStatement f = (IfStatement) current;
					bindLocalVariables(f.getStatements());
					bindLocalVariables(f.getElseStatements());
				} else if (SwitchStatement.class.getName().equals(
						current.getClass().getName())) {
					SwitchStatement f = (SwitchStatement) current;
					CaseGroupList list = f.getCaseGroupList();
					for (int x = 0; x < list.size(); x++) {
						bindLocalVariables(list.get(x).getStatements());
					}
				} else if (TryStatement.class.getName().equals(
						current.getClass().getName())) {
					TryStatement f = (TryStatement) current;
					bindLocalVariables(f.getBody());
					CatchList list = f.getCatchList();
					for (int x = 0; x < list.size(); x++) {
						bindLocalVariables(list.get(x).getBody());
					}
					bindLocalVariables(f.getFinallyBody());
				}

			}
		} catch (OJClassNotFoundException e) {
			throw new ParseTreeException(e);
		}

	}

	/**
	 * @param name	  : the name of the method to search :	{@code String}
	 * @param from	  : the class from which the method will be search 	: 	{@code OJClass}
	 * @param complyWith	:	actual arguments that the searched method needs to be able to accept : {@code ExpressionList} 
	 * @return the formal method declaration that matches with all the parameters
	 */
	public OJMethod getMethod(String name, OJClass from, ExpressionList complyWith) throws ParseTreeException {
		OJMethod[] allMethods = getAllMethods(from, ALLOW_NON_STATIC);
		for (OJMethod m : allMethods) {
			if (compareNamesWithoutPackage(m.getName(), name)) {
				int formalArgs = m.getParameterTypes().length;
				int actualParams = complyWith.size();
				if (formalArgs == actualParams) {
					for (int a = 0; a < formalArgs; a++) {
						OJClass formalType = m.getParameterTypes()[a];
						OJClass actualType = getType(complyWith.get(a));
						if (!compatibleAssignTypeRelaxed(formalType, actualType)) {
							break;
						}
					}
					return m;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param name	  : the name of the constructor to search :	{@code String}
	 * @param from	  : the class from which the constructor will be search 	: 	{@code OJClass}
	 * @param complyWith	:	actual arguments that the searched constructor needs to be able to accept : {@code ExpressionList} 
	 * @return the formal constructor declaration that matches with all the parameters
	 */
	public OJConstructor getConstructor(TypeName from, ExpressionList complyWith) throws ParseTreeException {
		OJConstructor[] allConstructors = getAllConstructors(from);
		for (OJConstructor c : allConstructors) {
			int formalArgs = c.getParameterTypes().length;
			int actualParams = complyWith.size();
			if (formalArgs == actualParams) {
				for (int a = 0; a < formalArgs; a++) {
					OJClass formalType = c.getParameterTypes()[a];
					OJClass actualType = getType(complyWith.get(a));
					if (!compatibleAssignTypeRelaxed(formalType, actualType)) {
						break;
					}
				}
				return c;
			}
		}
		return null;
	}

	public static boolean compareNamesWithoutPackage(String name1, String name2) {
		int pakLastIndex1 = name1.lastIndexOf(".") + 1;
		int pakLastIndex2 = name2.lastIndexOf(".") + 1;
		String name1NoPak = name1.substring(pakLastIndex1);
		String name2NoPak = name2.substring(pakLastIndex2);
		return name1NoPak.equals(name2NoPak);
	}
	
	public static boolean sameType(OJClass c1, OJClass c2) {
		if (c1 == null && c2 == null) return true;
		if (c1 == null || c2 == null) return false;
		if (c1 == null || c1.toString().compareTo("<type>null") == 0) {
			return c2 == null || c2.toString().compareTo("<type>null") == 0;
		}
		if (c1 == null || "void".equalsIgnoreCase(c1.getName())) {
			return c2 == null || "void".equalsIgnoreCase(c2.getName());
		}
		return compareNamesWithoutPackage(c1.getName(), c2.getName());
	}

	public static boolean compareNamesWithoutGenerics(String name1,
			String name2, boolean ignorePackage) {
		String name1woGenerics = name1.replaceAll("\\<.*\\>", "");
		String name2woGenerics = name2.replaceAll("\\<.*\\>", "");
		if (ignorePackage) {
			return compareNamesWithoutPackage(name1woGenerics, name2woGenerics);
		}
		return name1woGenerics.equals(name2woGenerics);
	}

	public OJClass max(OJClass c1, OJClass c2) {
		if (compatibleAssignTypeRelaxed(c1, c2)) {
			// c2 is assignable to c1
			return c1;
		} else if (compatibleAssignTypeRelaxed(c2, c1)) {
			// c1 is assignable to c2
			return c2;
		} else {
			// c1 and c2 are incompatible types
			return null;
		}
	}

	public static ParseTreeObject getStatement(ParseTreeObject node) {
		ParseTreeObject current = node;
		// node.makeCopy_keepOriginalID();
		while (current != null && !(current instanceof Statement)) {
			current = current.getParent();
		}
		return current;
	}

	public static ParseTreeObject getStatement(ParseTreeObject node, int offset) {
		ParseTreeObject currentWithOffset = getStatement(node);
		ParseTreeObject limit = currentWithOffset;
		boolean found = false;
		ParseTreeObject current = currentWithOffset;
		StatementList stmtList = (StatementList) getStatementList(current);
		if (stmtList == null) return null;
		while (current != null && !found) {
			int idx;
			for (idx = 0; idx < stmtList.size(); idx++) {
				Statement currSt = stmtList.get(idx);
				if (currSt == limit) {
					break;
				}
			}
			if (offset != LAST_STATEMENT && idx + offset < 0) {
				ParseTreeObject decl = stmtList.getParent();
				stmtList = getStatementList(decl);
				limit = decl;
				if (stmtList == null) {
					return null;
				} else {
					offset += Math.max(idx, 1);
				}
			} else if (offset == LAST_STATEMENT || idx + offset >= stmtList.size()) {
				int prevStLstSize = stmtList.size();
				ParseTreeObject decl = stmtList.getParent();
				if (decl instanceof MethodDeclaration && offset == LAST_STATEMENT) {
					return (ParseTreeObject) stmtList.get(stmtList.size()-1);
				}
				stmtList = getStatementList(decl);
				limit = decl;
				if (stmtList == null) {
					return null;
				}
				if (offset != LAST_STATEMENT) {
					int advancedStatements = prevStLstSize - idx;
					if (advancedStatements > 0) {
						offset -= advancedStatements;
					}
				}
			} else {
				currentWithOffset = (ParseTreeObject) stmtList.get(idx + offset);
				found = true;
			}
		}
		return currentWithOffset;
	}

	public static StatementList getStatementList(ParseTreeObject node) {
		if (node == null)
			return null;
		ParseTreeObject st = (node instanceof Statement) ? node : getStatement(node);
		ParseTreeObject current = st;
		while (current != null && !(current instanceof StatementList)) {
			current = current.getParent();
		}
		return (StatementList) current;
	}

	public static ParseTreeObject getMethodDeclaration(ParseTreeObject node) {
		ParseTreeObject nodeAsStatement = getStatement(node);
		ParseTreeObject current = nodeAsStatement;
		while (current != null && !(current instanceof MethodDeclaration)) {
			current = current.getParent();
		}
		if (current != null) {
			return current;
		} else {
			return null;
		}
	}
	
	protected static final boolean isSameObject(ParseTree p, ParseTree q) {
		if (p == null && q == null)
			return true;
		if (p == null || q == null)
			return false;
		return (p.getObjectID() == q.getObjectID());
	}
	
	public static Expression getPreviousExpression(Expression e) {
		if (e instanceof MethodCall) {
			return ((MethodCall) e).getReferenceExpr();
		} else if (e instanceof FieldAccess) {
			return ((FieldAccess) e).getReferenceExpr();
		} else if (e instanceof Variable) {
			return null;
		} else if (e instanceof Literal) {
			return null;
		} else if (e instanceof ArrayAccess) {
			return getPreviousExpression( ((ArrayAccess)e).getReferenceExpr() );
		} else {
			//should never reach this point
			//throw an excepcion maybe
			return null;
		}
	}
	
	
	public static CompilationUnit getCompilationUnit(ParseTreeObject o) {
		ParseTreeObject current = o;
		while (current != null && !(current instanceof CompilationUnit)) {
			current = current.getParent();
		}
		return (CompilationUnit) current;
	}
	
	/**
	 * This method will replace the parent of a {@code ParseTreeObject} with the parent of another
	 * the difference between this method and {@code ParseTreeObject#setParent(ParseTreeObject)} is that
	 * this one will detect special cases like when the new parent is an {@code ExpressionList} object
	 * and will make the necessary adjustments
	 * @param o :	the object whose parent will be changed
	 * @param o2 :	the object with the new parent to set
	 */
	public static void setParentOf(ParseTreeObject o, ParseTreeObject o2) {
		setParentOf(o, o2, false);
	}
	
	/**
	 * This method will replace the parent of a {@code ParseTreeObject} with the parent of another
	 * the difference between this method and {@code ParseTreeObject#setParent(ParseTreeObject)} is that
	 * this one will detect special cases like when the new parent is an {@code ExpressionList} object
	 * and will make the necessary adjustments
	 * @param o :	the object whose parent will be changed
	 * @param o2 :	the object with the new parent to set
	 * @param useOnlySetParent : if {@code true} then this method will simply call {@code o.setParent(o2)}
	 */
	public static void setParentOf(ParseTreeObject o, ParseTreeObject o2, boolean useOnlySetParent) {
		ParseTreeObject newParent = o2.getParent();
		if (newParent instanceof ExpressionList && !useOnlySetParent) {
			ExpressionList newParentAsList = (ExpressionList) boundedRecursiveCopyOf(newParent, COPY_SCOPE.NODE, true);
			int indexOfo2 = getArgumentIndex(newParentAsList, (Expression) o2);
			newParentAsList.remove(indexOfo2);
			newParentAsList.insertElementAt((Expression)o, indexOfo2);
		} else {
			o.setParent(newParent);
		}
	}
	
	private static int getArgumentIndex(ExpressionList args, Expression arg) {
		for (int a = 0; a < args.size(); a++) {
			if (isSameObject(args.get(a), arg)) return a;
		}
		return -1;
	}
	
	public static boolean compareAsStrings(ParseTreeObject o1, ParseTreeObject o2, boolean ignoreSelfAccess) {
		String o1AsString = o1.toFlattenString();
		String o2AsString = o2.toFlattenString();
		if (ignoreSelfAccess) {
			o1AsString = removeSelfAccess(o1AsString);
			o2AsString = removeSelfAccess(o2AsString);
		}
		return o1AsString.compareTo(o2AsString) == 0;
	}
	
	private static String removeSelfAccess(String original) {
		original = original.trim();
		if (original.startsWith("this.")) {
			return original.substring(5, original.length());
		} else if (original.startsWith("super.")) {
			return original.substring(6, original.length());
		} else {
			return original;
		}
	}
	
	
	private static boolean checkMethodToMutateAgainstNameAndParamList(String mtm, String[] expectedArgs, String name, ParameterList pl) {
		if (Mutator.verbose) {
			System.out.println("checkMethodToMutateAgainstNameAndParamList(" + mtm + ", " + Arrays.toString(expectedArgs) + ", " + name + ", " + pl.toFlattenString() + ")");
		}
		if (name.compareTo(mtm) != 0) {
			if (Mutator.verbose) {
				System.out.println("didn't match!");
			}
			return false;
		}
		if (expectedArgs != null) {
			ParameterList args = pl;
			if (args == null || args.size() != expectedArgs.length) {
				if (Mutator.verbose) {
					System.out.println("didn't match!");
				}
				return false;
			}
			for (int p = 0; p < args.size(); p++) {
				Parameter param = args.get(p);
				if (param.getTypeSpecifier().getNameWithoutGenerics().compareTo(expectedArgs[p]) != 0) {
					if (Mutator.verbose) {
						System.out.println("didn't match!");
					}
					return false;
				}
			}
		}
		if (Mutator.verbose) {
			System.out.println("matched!");
		}
		return true;
	}
	
	public static boolean checkApiConstructorNodeAgainstMethodToMutate(ConstructorDeclaration c) {
		return checkMethodToMutateAgainstNameAndParamList(Api.getMethodUnderConsideration(), Api.getExpectedArguments(), c.getName(), c.getParameters());
	}
	
	public static boolean checkApiMethodNodeAgainstMethodToMutate(MethodDeclaration m) {
		return checkMethodToMutateAgainstNameAndParamList(Api.getMethodUnderConsideration(), Api.getExpectedArguments(), m.getName(), m.getParameters());
	}
	
	public static boolean checkMethodNodeAgainstMethodToMutate(String methodToMutate, String[] expectedArgs, MethodDeclaration m) {
		return checkMethodToMutateAgainstNameAndParamList(methodToMutate, expectedArgs, m.getName(), m.getParameters());
	}

	public static boolean checkConstructorNodeAgainstMethodToMutate(String methodToMutate, String[] expectedArgs, ConstructorDeclaration c) {
		return checkMethodToMutateAgainstNameAndParamList(methodToMutate, expectedArgs, c.getName(), c.getParameters());
	}
	
	public final static ParseTreeObject boundedRecursiveCopyOf(ParseTreeObject o, COPY_SCOPE scope, boolean returnSameNode) {
		ParseTreeObject boundedCopy = ParseTreeObject.boundedRecursiveCopyOf(o, scope);
		if (returnSameNode) {
			Mutation dummyMut = new Mutation(MutationOperator.MULTI, o, o);
			OLMO olmo = new OLMO(dummyMut);
			try {
				return olmo.retrieveOriginalNodeIn(boundedCopy);
			} catch (ParseTreeException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return boundedCopy;
		}
	}
	
	public final static ParseTreeObject nodeCopyOf(ParseTreeObject o) {
		return boundedRecursiveCopyOf(o, COPY_SCOPE.NODE, false);
	}
	
	public final static boolean inSamePackage(OJClass a, OJClass b) {
		String pa = a.getPackage();
		String pb = b.getPackage();
		if (pa == null) {
			if (pb == null) return true;
			else return false;
		} else {
			if (pb == null) return false;
			else return pa.compareTo(pb) == 0;
		}
	}
	
	public final boolean isSelfClass(OJClass a) throws ParseTreeException {
		return getSelfType().getName().compareTo(a.getName()) == 0;
	}
	
}
