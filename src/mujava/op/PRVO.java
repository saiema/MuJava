package mujava.op;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Comparator;

import mujava.api.Api;
import mujava.api.Configuration;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.openjava.extension.ExtendedClosedEnvironment;
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
import openjava.ptree.CatchList;
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
 * @version 2.6
 */
public class PRVO extends mujava.op.util.Mutator {
	
	public static final String ENABLE_SUPER = "prvo_enable_super";
	public static final String ENABLE_THIS = "prvo_enable_this";
	public static final String ENABLE_LITERAL_NULL = "prvo_enable_literal_null";
	public static final String ENABLE_LITERAL_TRUE = "prvo_enable_literal_true";
	public static final String ENABLE_LITERAL_FALSE = "prvo_enable_literal_false";
	public static final String ENABLE_LITERAL_EMPTY_STRING = "prvo_enable_literal_empty_string";
	public static final String ENABLE_LITERAL_ZERO = "prvo_enable_literal_zero";
	public static final String ENABLE_LITERAL_ONE = "prvo_enable_literal_one";
	public static final String ENABLE_LITERAL_STRINGS = "prvo_enable_literal_strings";
	
	ParseTreeObject parent = null;
	
	private boolean allowNonStatic = true;

	private Set<OJField> fields = new HashSet<OJField>(); // Collection of ALL fields (not the local ones, which are variables)

	private Set<OJMethod> methods = new HashSet<OJMethod>(); // Collection of ALL parameterless methods, including local p'less methods
	
	private Set<Variable> variables = new HashSet<Variable>(); // Collection of ALL variables (local fields, parameters and variables in the
	                                                           // method of interest, i.e., that to be mutated)
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
	
	private boolean computedAccessibleVariables = false;
	
	public PRVO(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
		this.smartMode = false;
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
	
	private Expression prepend(Expression original, Expression toAdd) throws ParseTreeException {
		Expression modified = null;
		
		if (toAdd == null) return (Expression) original.makeRecursiveCopy_keepOriginalID();
		
		//first, get last part of toAdd
		Expression toAddCopy = (Expression) toAdd.makeRecursiveCopy_keepOriginalID();
		if (toAddCopy instanceof FieldAccess) {
			((FieldAccess)toAddCopy).setReferenceExpr(null);
		} else if (toAddCopy instanceof MethodCall) {
			((MethodCall)toAddCopy).setReferenceExpr(null);
		}
		//-------------------------------------
		
		if (original == null) return toAddCopy;
		
		Expression originalCopy = (Expression) original.makeRecursiveCopy_keepOriginalID();
		
		//second, if originalCopy is a Variable, change it to a FieldAccess
		if (originalCopy instanceof Variable) {
			originalCopy = fixStupidVariable((Variable)originalCopy);
		}
		//----------------------------------------------------------------
		
		Expression current = (Expression) originalCopy.makeRecursiveCopy_keepOriginalID();
		List<Expression> parts = new LinkedList<Expression>();
		while (current != null) {
			Expression currentLastPart = (Expression) current.makeRecursiveCopy_keepOriginalID();
			if (currentLastPart instanceof FieldAccess) {
				((FieldAccess)currentLastPart).setReferenceExpr(null);
			} else if (currentLastPart instanceof MethodCall) {
				((MethodCall)currentLastPart).setReferenceExpr(null);
			}
			parts.add(0, currentLastPart);
			current = getPreviousExpression(current);
		}
		parts.add(0, toAddCopy);
		
		for (Expression elem : parts) {
			if (modified == null) {
				modified = elem;
			} else {
				Expression elemCopy = (Expression) elem.makeRecursiveCopy_keepOriginalID();
				if (elemCopy instanceof Variable) elemCopy = fixStupidVariable((Variable)elemCopy);
				if (elemCopy instanceof FieldAccess) {
					((FieldAccess)elemCopy).setReferenceExpr(modified);
				} else if (elemCopy instanceof MethodCall) {
					((MethodCall)elemCopy).setReferenceExpr(modified);
				}
				modified = elemCopy;
			}
		}
		
		return modified;
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
		OJMember[] inheritedMembers = (m instanceof OJMethod)?getInheritedMethods(getSelfType(), false):getInheritedFields(getSelfType(), false);
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
		OJField[] inheritedFields = getInheritedFields(getSelfType(), false);
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
		OJMethod[] inheritedMethods = getInheritedMethods(getSelfType(), false);
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
	
	
	/**
	 * Calculates the variables local to a statement list. As a side effect, variables are bound to their corresponding types in the environment.
	 * @param st is the statement list for which the local variables are calculated
	 * @return the list of all local variables declared in the statement list
	 * @throws ParseTreeException
	 */
	private String[] getLocalVariables(StatementList statList) throws ParseTreeException {
		Enumeration<?> st = statList.elements();
		HashSet<String> result = new HashSet<String>();
		try {
			while (st.hasMoreElements()) {
				Object current = st.nextElement();
				if (VariableDeclaration.class.getName().equals(current.getClass().getName()) ){
					VariableDeclaration var = (VariableDeclaration) current;
					result.add(var.getVariable());
					getEnvironment().bindVariable(var.getVariable(), OJClass.forName(var.getTypeSpecifier().toString()));
				}
				else if (ForStatement.class.getName().equals(current.getClass().getName())) {
					ForStatement f = (ForStatement) current;
					if (f.getInitDeclType()!=null) {
						VariableDeclarator[] vars = f.getInitDecls();
						for (int i=0; i<vars.length; i++) {
							result.add(vars[i].getVariable());
							getEnvironment().bindVariable(vars[i].getVariable(), OJClass.forName(f.getInitDeclType().toString()));
						}
					}
					String[] blockLocalVars = getLocalVariables(f.getStatements());
					for (int i=0; i<blockLocalVars.length; i++) {
						result.add(blockLocalVars[i]);
					}
				}
				else if (WhileStatement.class.getName().equals(current.getClass().getName())) {
					WhileStatement f = (WhileStatement) current;
					String[] blockLocalVars = getLocalVariables(f.getStatements());
					for (int i=0; i<blockLocalVars.length; i++) {
						result.add(blockLocalVars[i]);
					}
				}
				else if (DoWhileStatement.class.getName().equals(current.getClass().getName())) {
					DoWhileStatement f = (DoWhileStatement) current;
					String[] blockLocalVars = getLocalVariables(f.getStatements());
					for (int i=0; i<blockLocalVars.length; i++) {
						result.add(blockLocalVars[i]);
					}
				}
				else if (IfStatement.class.getName().equals(current.getClass().getName())) {
					IfStatement f = (IfStatement) current;
					String[] blockLocalVars = getLocalVariables(f.getStatements());
					for (int i=0; i<blockLocalVars.length; i++) {
						result.add(blockLocalVars[i]);
					}
					blockLocalVars = getLocalVariables(f.getElseStatements());
					for (int i=0; i<blockLocalVars.length; i++) {
						result.add(blockLocalVars[i]);
					}
				}
				else if (SwitchStatement.class.getName().equals(current.getClass().getName())) {
					SwitchStatement f = (SwitchStatement) current;
					CaseGroupList list = f.getCaseGroupList();
					for (int x=0; x<list.size(); x++) {
						String[] blockLocalVars = getLocalVariables(list.get(x).getStatements());
						for (int i=0; i<blockLocalVars.length; i++) {
							result.add(blockLocalVars[i]);
						}
					}
				}
				else if (TryStatement.class.getName().equals(current.getClass().getName())) {
					TryStatement f = (TryStatement) current;
					String[] blockLocalVars = getLocalVariables(f.getBody());
					for (int i=0; i<blockLocalVars.length; i++) {
						result.add(blockLocalVars[i]);
					}
					CatchList list = f.getCatchList();
					for (int x=0; x<list.size(); x++) {
						blockLocalVars = getLocalVariables(list.get(x).getBody());
						for (int i=0; i<blockLocalVars.length; i++) {
							result.add(blockLocalVars[i]);
						}
					}
					blockLocalVars = getLocalVariables(f.getFinallyBody());
					for (int i=0; i<blockLocalVars.length; i++) {
						result.add(blockLocalVars[i]);
					}
				}
				


			}
		}
		catch (OJClassNotFoundException e) {
			throw new ParseTreeException(e);
		}


		return result.toArray(new String[result.size()]);

	}

	
	/**
	 * Retrieves all the variables available in the class, its superclasses, attributes, and variables local to the method under consideration. 
	 * These variables are used for mutating expressions (e.g., right hand side of assignments). As side effects, every variable is bound to
	 * its corresponding type in the environment. Also, fields are stored in attribute this.fields, parameterless methods are stored in 
	 * this.methods, and variables (all of them, local to method, parameters, etc.) are stored in attribute this.variables.
	 * Finally, if these expressions have been computed previously, they are reproduced from this.fields, this.variables and this.methods, as
	 * opposed to recalculating them.
	 * @return an array containing all the variables names (only the names, as strings, not their types).
	 * @throws ParseTreeException
	 */
	private String[] getAccessibleVariables(ParseTreeObject elem) throws ParseTreeException {
		HashSet<String> var_set = new HashSet<String>();

		if (!computedAccessibleVariables) {

			computedAccessibleVariables = true;

			if (env instanceof ClosedEnvironment) {
				ExtendedClosedEnvironment mujava_env = new ExtendedClosedEnvironment(env);
				var_set.addAll(mujava_env.getAccessibleVariables());
				mujava_env = null;
			}

			for (int i = (env_nest.size() - 1); i >= 0; i--) {
				Environment temp_env = env_nest.get(i);
				if (temp_env instanceof ClosedEnvironment) {
					ExtendedClosedEnvironment mujava_env = new ExtendedClosedEnvironment(temp_env);
					var_set.addAll(mujava_env.getAccessibleVariables());
					mujava_env = null;
				}
			}

			// add all fields of the class to var_set
			OJClass clazz = getSelfType();
//			OJField[] fs = clazz.getAllFields();//.getDeclaredFields();
//			if (fs != null) {
//				for (int k = 0; k < fs.length; k++) {
//					var_set.add(fs[k].getName());
//				}
//			}

			try {
				// obtain method under consideration (method)
				// It's used to get all variables locally declared in the method
				OJMethod[] methods = clazz.getMethods();
				OJMethod method = null;
				for(OJMethod m : methods) {
					if(m.getName().equalsIgnoreCase(Api.getMethodUnderConsideration())) {
						method = m;
						break;
					}
				}
				if(method != null) {
					// add variables locally declared in method under consideration, to var_set
					
					//get method parameters and do the bind stuff
					String param;
					OJClass type;
					int m = 0;
					while (m < method.getParameterVariables().size()) {
						param = method.getParameters()[m];
						type = method.getParameterTypes()[m];
						var_set.add(method.getParameterVariables().get(m).toString());
						getEnvironment().bindVariable(param, type);
						m++;
					}
					//-----------------------------------------------------
					
					String[] methodVars = getLocalVariables(method.getBody());
					for (int i=0; i<methodVars.length; i++) {
						var_set.add(methodVars[i]);
					}
				}
			} catch (CannotAlterException e) {
				throw new ParseTreeException(e);
			} 


			Set<String> fieldVars = new HashSet<String>();
			Set<String> methodVars = new HashSet<String>();
			/* Field access */
			for(String var: var_set){
				// add every variable in var_set, as an expression, to this.variables
				Variable v = new Variable(var);
				variables.add(v);


				// for every variable already in var_set, compute
				// fields and parameterless methods, and put in fieldVars and methodVars, resp.
				OJClass type = getType(v);
				boolean isArray = type.isArray();
				OJField[] fields = type.getFields();
				OJMethod[] methods = isArray?new OJMethod[]{}:type.getMethods();
				
				

				for(OJField field: fields){
					this.fields.add(field);
					String fieldAccessName = var + "." + field.getName();
					getEnvironment().bindVariable(fieldAccessName, field.getType());
					fieldVars.add(fieldAccessName);
				}

				for(OJMethod method: methods){
					/* Only consider methods with no parameters: i.e.: getters*/
					if( method.getParameterTypes().length == 0 ){
						this.methods.add(method);
						String methodAccessName = var + "." + method.getName() + "()";
						getEnvironment().bindVariable(methodAccessName, method.getReturnType());
						methodVars.add(methodAccessName);
					}
				}
			}

			// add all local parameterless methods to this.methods
			for (OJMethod m: getSelfType().getDeclaredMethods()) {
				if (m.getParameterTypes().length==0) {
					this.methods.add(m);
				}
			}

			// add all fields and parameterless methods of variables in var_set, to var_set
			var_set.addAll(fieldVars);
			var_set.addAll(methodVars);


			Set<String> unwantedPrimitiveVariables = new HashSet<String>();
			for(String var: var_set){
				Variable v = new Variable(var);
				OJClass type = getType(v);
				if( type.isPrimitive() || type.isPrimitiveWrapper() ){
					unwantedPrimitiveVariables.add(var);
					variables.remove(v);
				}
			}

			var_set.removeAll(unwantedPrimitiveVariables);
			fieldVars.removeAll(unwantedPrimitiveVariables);
			return var_set.toArray(new String[var_set.size()]);
		}
		else {
			for (Variable v: variables) {
				var_set.add(v.toString());
			}
			for (OJMethod m: methods) {
				var_set.add(m.toString());
			}
			for (OJField f: fields) {
				var_set.add(f.toString());
			}
		}
		return var_set.toArray(new String[var_set.size()]);
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
		boolean ignoreVars = forceIgnoreVars;
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
		return fieldsMethodsAndVars(limit, t, ignoreVars, true, true, this.allowNonStatic);
	}
	
	private java.util.List<Object> fieldAndMethods(Expression e) throws ParseTreeException {
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
		return fieldAndMethods(t,forceIgnoreVariables);
	}
	
	private java.util.List<Object> fieldAndMethods(OJClass t, boolean forceIgnoreVariables) throws ParseTreeException {
		java.util.List<Object> fnm = new LinkedList<Object>();
		if (t == null) {
			return fnm;
		}
		boolean addVariables = (t.getName().compareTo(getSelfType().getName()) == 0) && !forceIgnoreVariables;
		String publicCheck = "";
		boolean tPackageSameAsThisPackage = t.isInSamePackage(getSelfType());//t.getPackage().equals(getSelfType().getPackage());
		publicCheck += tPackageSameAsThisPackage?1:0;
		boolean tIsInnerClassOfThis = isInnerClassOf(getSelfType(), t, true);
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
                fnm.add(f);
            }
        }
        for (OJMethod m : t.getDeclaredMethods()) {
        	boolean allowNonStatic = (this.allowNonStatic && this.smartMode) || !this.smartMode;
            boolean isNonStatic = !m.getModifiers().isStatic();
        	if (m.getReturnType().getName().compareToIgnoreCase("void") == 0) continue;
            if ((m.signature().getParameterTypes().length == 0 && ((onlyPublic && m.getModifiers().isPublic())||(!onlyPublic))) && ((allowNonStatic && isNonStatic) || !isNonStatic)) {
                fnm.add(m);
            }
        }
        for (OJField inheritedField : t.getAllFields()) {
        	boolean allowNonStatic = (this.allowNonStatic && this.smartMode) || !this.smartMode;
            boolean isNonStatic = !inheritedField.getModifiers().isStatic();
        	if (((!inheritedField.getModifiers().isPrivate() && !onlyPublic) || inheritedField.getModifiers().isPublic() && onlyPublic) && ((allowNonStatic && isNonStatic) || !isNonStatic)) {
                if (!fnm.contains(inheritedField)) fnm.add(inheritedField);
            }
        }
        for (OJMethod inheritedMethod : t.getAllMethods()) {
        	boolean allowNonStatic = (this.allowNonStatic && this.smartMode) || !this.smartMode;
            boolean isNonStatic = !inheritedMethod.getModifiers().isStatic();
        	if (inheritedMethod.getReturnType().getName().compareToIgnoreCase("void") == 0) continue;
            if ((((inheritedMethod.getModifiers().isProtected() && !onlyPublic) || inheritedMethod.getModifiers().isPublic()) && inheritedMethod.signature().getParameterTypes().length == 0) && ((allowNonStatic && isNonStatic) || !isNonStatic)) {
                if (!fnm.contains(inheritedMethod)) fnm.add(inheritedMethod);
            }
        }
		if (addVariables) {
			for (Variable v : this.variables) {
					if (!fnm.contains(v)) fnm.add(v);
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
		for (Object m : fieldAndMethods(e1c, false)) {
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
		for (Object m : fieldAndMethods(e1c, false)) {
			if (m instanceof OJField || m instanceof OJMethod) {
				if (((OJMember) m).getName().compareTo(e2Name)==0) return true;
			}
		}
		return false;
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
		if (methodApplicable) {
			if (!this.smartMode) getAccessibleVariables(orig);
			Expression current = lor?e1:e2;
			Expression prev = null;
			Expression next = null;
			Expression rightPart = null;
			if (!lor && (e2 instanceof Variable) && compatibleAssignType(ltype, null) && this.allowLiteralNull() && ((refined && this.refModeAllowNullStack.peek()) || !refined)) outputToFile((ParseTreeObject)(lor?e1:e2), Literal.constantNull());
			do {
				prev = getPreviousExpression(current);
				java.util.List<Object> fnm = this.smartMode?fieldAndMethods(orig, prev, false):fieldAndMethods(prev);
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
						if (m instanceof OJField) {
							FieldAccess mutantField = new FieldAccess(prevCopy==null?(this.isInherited((OJField)m)?(SelfAccess.constantSuper()):(SelfAccess.constantThis())):(prevCopy), ((OJField)m).getName());
							mutantField.setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
							outputToFile((ParseTreeObject)(lor?e1:e2), mutantField);
						} else if (m instanceof OJMethod) {
							MethodCall mutantMethod = new MethodCall(prevCopy==null?(this.isInherited((OJMethod)m)?(SelfAccess.constantSuper()):(SelfAccess.constantThis())):(prevCopy), ((OJMethod)m).getName(), new ExpressionList());
							mutantMethod.setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
							outputToFile((ParseTreeObject)(lor?e1:e2), mutantMethod);
						} else if (m instanceof Variable) {
							Variable mutantVar = (Variable) ((Variable)m).makeRecursiveCopy_keepOriginalID();
							mutantVar.setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
							outputToFile((ParseTreeObject)(lor?e1:e2), mutantVar);
						}
					} else if ((next != null) && (!(m instanceof Variable) && (isFieldMethodOf((OJMember)m, next)))) {
						Expression nextCopy = rightPart==null?null:((Expression) rightPart.makeRecursiveCopy_keepOriginalID());
						Expression prevCopy = prev==null?null:((Expression) prev.makeRecursiveCopy_keepOriginalID());
						Expression mutantCurrent = null;
						if (m instanceof OJField) {
							mutantCurrent = new FieldAccess(prevCopy==null?(this.isInherited((OJField)m)?(SelfAccess.constantSuper()):(SelfAccess.constantThis())):(prevCopy), ((OJField)m).getName());
						} else if (m instanceof OJMethod) {
							mutantCurrent = new MethodCall(prevCopy==null?(this.isInherited((OJMethod)m)?(SelfAccess.constantSuper()):(SelfAccess.constantThis())):(prevCopy), ((OJMethod)m).getName(), new ExpressionList());
						}
						((ParseTreeObject) mutantCurrent).setParent(((ParseTreeObject)(lor?e1:e2)).getParent());
						if (nextCopy == null) {
							outputToFile((ParseTreeObject)(lor?e1:e2), (ParseTreeObject)mutantCurrent);
						} else {
							if (nextCopy instanceof FieldAccess) {
								((FieldAccess)nextCopy).setReferenceExpr(mutantCurrent);
							} else if (nextCopy instanceof MethodCall) {
								((MethodCall)nextCopy).setReferenceExpr(mutantCurrent);
							}
							outputToFile((ParseTreeObject)(lor?e1:e2), (ParseTreeObject)nextCopy);
						}	
					}
				}
				if (rightPart != null) {
					rightPart = prepend(rightPart, current);
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
		if (methodIsApplicable) {
			//decrease by 1
			if (!this.smartMode) getAccessibleVariables(orig);
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
						outputToFile((ParseTreeObject)(lor?e1:e2), (ParseTreeObject)prevCopy);
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
								outputToFile((ParseTreeObject)(lor?e1:e2), (ParseTreeObject)nextCopy);
							} else {
								Expression nextCopy = (Expression) rightPart.makeRecursiveCopy_keepOriginalID();
								Expression prevCopy = (Expression) prev.makeRecursiveCopy_keepOriginalID();
								if (nextCopy instanceof FieldAccess) {
									((FieldAccess)nextCopy).setReferenceExpr(prevCopy);
								} else if (nextCopy instanceof MethodCall) {
									((MethodCall)nextCopy).setReferenceExpr(prevCopy);
								}
								outputToFile((ParseTreeObject)(lor?e1:e2), (ParseTreeObject)nextCopy);
							}
						}
					} else {
						//this line should never be reached
						//throw an excepcion maybe
					}
				}
				if (rightPart != null) {
					rightPart = prepend(rightPart, current);
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
				modified = prepend(modified, isInherited((FieldAccess)current)?SelfAccess.constantSuper():SelfAccess.constantThis());
			}
		} else if (current instanceof MethodCall) {
			boolean isSuper = isInherited((MethodCall)current);
			boolean isThis = !isSuper;
			if (allowSuper() && isSuper || allowThis() && isThis) {
				modified = prepend(modified, isInherited((MethodCall)current)?SelfAccess.constantSuper():SelfAccess.constantThis());
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
		if (methodIsApplicable) {
			if (!this.smartMode) getAccessibleVariables(orig);
			Expression current = lor?e1:e2;
			Expression next = null;
			Expression rightPart = null;
			boolean stop = false;
			do {
				stop = current == null;
				java.util.List<Object> fnm = this.smartMode?fieldAndMethods(orig, current, false):fieldAndMethods(current);
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
							if (nextCopy instanceof FieldAccess) {
								((FieldAccess)nextCopy).setReferenceExpr(mutant);
							} else if (nextCopy instanceof MethodCall) {
								((MethodCall)nextCopy).setReferenceExpr(mutant);
							}
							outputToFile((ParseTreeObject)(lor?e1:e2), (ParseTreeObject)addThisSuper(nextCopy));
						} else {
							outputToFile((ParseTreeObject)(lor?e1:e2), (ParseTreeObject)addThisSuper(mutant));
						}
						
					}
				}
				if (rightPart != null) {
					rightPart = prepend(rightPart, current);
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
		if (methodIsApplicable) {
			if (!this.smartMode) getAccessibleVariables(orig);
			Expression current = lor?e1:e2;
			Expression next = null;
			Expression prev = null;
			Expression rightPart = null;
			do {
				prev = getPreviousExpression(current);
				Expression prevPrev = getPreviousExpression(prev);
				java.util.List<Object> fnm = this.smartMode?fieldAndMethods(orig, prevPrev, false):fieldAndMethods(prevPrev);
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
					boolean primitiveCheck = false;
					if (next == null) {
						boolean ltypeCheck = true; //compatibleAssignType(ltype, retType)
						pertCheck = true;
						typeCheck = lor?ltypeCheck&&compatibleAssignType(retType, rtype):compatibleAssignType(ltype, retType);
					} else {
						typeCheck = true;
						pertCheck = (m instanceof OJMember)?isFieldMethodOf((OJMember)m,next):((m instanceof Variable)?isFieldMethodOf((Variable)m, next):false);
					}
					primitiveCheck = (prevPrev == null) && !retType.isPrimitive();
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
							outputToFile((ParseTreeObject)(lor?e1:e2), (ParseTreeObject)nextCopy);
						} else {
							outputToFile((ParseTreeObject)(lor?e1:e2), (ParseTreeObject)mutant);
						}
					}
				}
				if (rightPart != null) {
					rightPart = prepend(rightPart, current);
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
	
	private void replaceOneByTwo(NonLeaf orig, Expression e1, Expression e2, boolean lor) throws ParseTreeException {
		OJClass ltype = getType(e1);
		OJClass rtype = getType(e2);
		boolean methodIsApplicable = (lor && !(e1 instanceof Literal) && !ltype.isPrimitive()) || (!lor && (e2 instanceof Variable || e2 instanceof FieldAccess || e2 instanceof MethodCall) && !rtype.isPrimitive());
		if (methodIsApplicable) {
			if (!this.smartMode) getAccessibleVariables(orig);
			Expression current = lor?e1:e2;
			Expression next = null;
			Expression prev = null;
			Expression rightPart = null;
			do {
				prev = getPreviousExpression(current);
				java.util.List<Object> fnm = this.smartMode?fieldAndMethods(orig, prev, false):fieldAndMethods(prev);;
				for (Object m : fnm) {
					java.util.List<Object> fnm2 = new LinkedList<Object>();
					if (this.smartMode) {
						fnm2 = fieldAndMethods(null, m, true);
					} else {
						fnm2 = (m instanceof OJMethod)?fieldAndMethods(((OJMethod)m).getReturnType(),false):(m instanceof OJField)?fieldAndMethods(((OJField)m).getType(),false):(m instanceof Variable)?fieldAndMethods((Variable)m):fnm2;
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
								outputToFile((ParseTreeObject)(lor?e1:e2), (ParseTreeObject)mutantPart2);
							}
						} else {
							if (isFieldMethodOf((OJMember)m2,next)){
								if (nextCopy instanceof FieldAccess) {
									((FieldAccess)nextCopy).setReferenceExpr(mutantPart2);
								} else if (nextCopy instanceof MethodCall) {
									((MethodCall)nextCopy).setReferenceExpr(mutantPart2);
								}
								outputToFile((ParseTreeObject)(lor?e1:e2), (ParseTreeObject)nextCopy);
							}
						}
					}
				}
				if (rightPart != null) {
					rightPart = prepend(rightPart, current);
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
		if (!this.smartMode) getAccessibleVariables(orig);
		sameLength(orig, e1, e2, lor, false);
		decreaseLenght(orig, e1, e2, lor);
		increaseLenght(orig, e1, e2, lor);
		replaceTwoByOne(orig, e1, e2, lor);
		replaceOneByTwo(orig, e1, e2, lor);
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
		super.visit(p);
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
		super.visit(p);
		if (this.refinedMode && getMutationsLeft(p) > 0) {
			ExpressionList init = p.getInit();
			for (int i = 0; init != null && i < init.size(); i++) {
				pushComplyType(p, init.get(i));
				pushAllowNull(p, false);
				init.get(i).accept(this);
				popAllowNull(p);
				popComplyType(p);
			}
			if (this.unary) {
				pushComplyType(p, p.getCondition());
				pushAllowNull(p, false);
				p.getCondition().accept(this);
				popAllowNull(p);
				popComplyType(p);
			}
			ExpressionList increments = p.getIncrement();
			for (int i = 0; i < increments.size(); i++) {
				increments.accept(this);
			}
		}
		p.getStatements().accept(this);
	}
	
	public void visit(DoWhileStatement p) throws ParseTreeException {
		p.getStatements().accept(this);
		if (this.unary && this.refinedMode && getMutationsLeft(p) > 0) {
			pushComplyType(p, p.getExpression());
			pushAllowNull(p, false);
			p.getExpression().accept(this);
			popAllowNull(p);
			popComplyType(p);
		}
	}
	
	public void visit(IfStatement p) throws ParseTreeException {
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
	
//	@Override
//	public ExpressionList evaluateUp(ExpressionList p) {
//		return p;
//	}
	
	@Override
	public Expression evaluateUp(AssignmentExpression p) {
		return p;
	}
	
	public void visit(AssignmentExpression p) throws ParseTreeException {
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
		if (!this.refinedMode) return;
		pushAllowNull(p, false);
		pushComplyType(p, p);
		Expression exp = p.getExpression();
		exp.accept(this);
		popComplyType(p);
		popAllowNull(p);
	}
	
	public void visit(MethodCall p) throws ParseTreeException {
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
		if (!this.refinedMode || getMutationsLeft(p) <= 0) return;
		pushAllowNull(p, false);
		pushComplyType(p, p);
		p.getIndexExpr().accept(this);
		popComplyType(p);
		popAllowNull(p);
	}
	
	public void visit(FieldAccess p) throws ParseTreeException {
		if (!this.refinedMode || getMutationsLeft(p) <= 0) return;
		unaryVisit(p, p, true);
	}
	
	public void visit(Variable p) throws ParseTreeException {
		if (!this.refinedMode || getMutationsLeft(p) <= 0) return;
		sameLength((NonLeaf) getStatement(p), this.refModeComplyTypeStack.peek(), p, false, true);
		increaseLenght((NonLeaf) getStatement(p), this.refModeComplyTypeStack.peek(), p, false);
		replaceOneByTwo((NonLeaf) getStatement(p), this.refModeComplyTypeStack.peek(), p, false);
		replaceByLiteral(this.refModeComplyTypeStack.peek(), p);
	}
	
	public void visit(Literal p) throws ParseTreeException {
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
		if (!this.smartMode) getAccessibleVariables(p);
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
		boolean mutGenLimitIsPositive = getMutationsLeft((ParseTreeObject)orig) > 0;
		boolean mutatingRightOrUnary = this.right || this.unary;
		boolean origIsValid = (orig instanceof MethodCall) || (orig instanceof FieldAccess) || (orig instanceof Variable) || (orig instanceof Literal);
		boolean noChainedMethodCall = ((orig instanceof MethodCall) && ((MethodCall)orig).getReferenceExpr() == null) || !(orig instanceof MethodCall);
		boolean noChainedFieldAccess = ((orig instanceof FieldAccess) && ((FieldAccess)orig).getReferenceExpr() == null) || !(orig instanceof FieldAccess);
		boolean origIsNotAChain = noChainedMethodCall && noChainedFieldAccess;
		if (this.useLiterals && mutGenLimitIsPositive && origIsValid && mutatingRightOrUnary && origIsNotAChain) {
			searchForLiteralsInMethod((ParseTreeObject) orig);
			for (Literal lit : this.literals) {
				if (complyWith != null) {
					OJClass typeToComply = getType(complyWith);
					OJClass litType = getType(lit);
					if (!compatibleAssignType(typeToComply, litType, false)) {
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
			boolean isNumber = ((Literal)node).getLiteralType()==Literal.INTEGER || ((Literal)node).getLiteralType()==Literal.DOUBLE || ((Literal)node).getLiteralType()==Literal.FLOAT || ((Literal)node).getLiteralType()==Literal.LONG;
			boolean isOne = isNumber && ((Literal)node).toFlattenString().compareTo("1") == 0;
			boolean isZero = isNumber && ((Literal)node).toFlattenString().compareTo("0") == 0;
			boolean oneCheck = (isOne && this.allowLiteralOne()) || !isOne;
			boolean zeroCheck = (isZero && this.allowLiteralZero()) || !isZero;
			if (nullCheck && stringCheck && emptyStringCheck && falseCheck && trueCheck && oneCheck && zeroCheck) {
				this.literals.add((Literal)node);
			}
		}
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
