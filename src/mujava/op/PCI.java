package mujava.op;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.ptree.BinaryExpression;
import openjava.ptree.CastExpression;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionList;
import openjava.ptree.FieldAccess;
import openjava.ptree.MethodCall;
import openjava.ptree.MethodDeclaration;
import openjava.ptree.ParseTreeException;
import openjava.ptree.StatementList;
import openjava.ptree.TypeName;
import openjava.ptree.Variable;
import openjava.ptree.VariableDeclaration;
import mujava.api.Api;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.op.util.Mutator;

public class PCI extends Mutator {
	private FileEnvironment lfile_env;
	
	public PCI(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super(file_env, comp_unit);
		this.lfile_env = file_env;
	}
	
	public void visit(Variable v) throws ParseTreeException {
		//MUTATE
		if (!(getMutationsLeft(v) > 0)) return;
		Variable originalCopy = v;//(Variable)v.makeRecursiveCopy();
		generateCast(originalCopy, false);
		generateCast(originalCopy, true);
	}
	
	public void visit(FieldAccess fa) throws ParseTreeException {
		//MUTATE
		if (!(getMutationsLeft(fa) > 0)) return;
		FieldAccess originalCopy = fa;//(FieldAccess)fa.makeRecursiveCopy();
		generateCast(originalCopy, false);
		generateCast(originalCopy, true);
	}
	
	private Class<?> javaClassForName(String name) throws ClassNotFoundException {
		if (isPrimitiveClass(name)) {
			return primitiveWrapper(name);
		} else {
			return Class.forName(name);
		}
	}
	
	private boolean isPrimitiveClass(String name) {
		return primitiveWrapper(name) != null;
	}
	
	private Class<?> primitiveWrapper(String name) {
		switch(name) {
			case "int" 		: return Integer.class;
			case "float"	: return Float.class;
			case "double"	: return Double.class;
			case "byte"		: return Byte.class;
			case "short"	: return Short.class;
			case "long"		: return Long.class;
			case "char"		: return Character.class;
			case "boolean"	: return Boolean.class;
			default			: return null;
		}
	}
	
	private void generateMutant(Expression exp, Class<?> c, List<String> generics) {
		OJClass castClass = OJClass.forClass(c);
		TypeName castType = TypeName.forOJClass(castClass);
		String genericsString = "";
		int i = 0;
		for (String g : generics) {
			genericsString += g;
			i++;
			if (i < generics.size() - 1) {
				genericsString += ", ";
			}
		}
		if (!genericsString.isEmpty()) castType.setGenerics("<"+genericsString+">");
		Expression originalCopy = (Expression)exp.makeRecursiveCopy_keepOriginalID();
		CastExpression mutant = new CastExpression(castType, originalCopy);
		if (exp instanceof Variable) {
			outputToFile((Variable)exp, mutant);
		} else {
			outputToFile((FieldAccess)exp, mutant);
		}
	}

	private void generateCast(Expression exp, boolean useChilds) throws ParseTreeException {
		OJClass varType = getType(exp);
		Class<?> varTypeAsClass;
		List<String> generics = new LinkedList<String>();
		try {
			varTypeAsClass = javaClassForName(varType.getName());
			List<Class<?>> childsOrParents = useChilds?getChilds(varTypeAsClass):getParents(varTypeAsClass);
			for (Class<?> cop : childsOrParents) {
				OJClass copAsOJClass = OJClass.forClass(cop);
				if (copAsOJClass.getName().compareTo(varType.getName())==0) continue;
				generics = new LinkedList<String>();
				Type cType = (Type) cop;
				if (cType instanceof ParameterizedType) {
					ParameterizedType parameterizedType = (ParameterizedType) cType;
					Type[] genericsTypes = parameterizedType.getActualTypeArguments();
					TypeVariable<?>[] gtypes = cop.getTypeParameters();
					if (genericsTypes.length != gtypes.length) {
						continue;
					}
					for (Type t : genericsTypes) {
						generics.add(getClassName(t.toString()));
					}
				}
				generateMutant(exp, cop, generics);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void visit(BinaryExpression be) throws ParseTreeException {
		if (!(getMutationsLeft(be) > 0)) return;
		be.getLeft().accept(this);
		be.getRight().accept(this);
	}
	
	public void visit(MethodCall mc) throws ParseTreeException {
		if (!(getMutationsLeft(mc) > 0)) return;
		ExpressionList args = mc.getArguments();
		for (int a = 0; a < args.size(); a++) {
			Expression exp = args.get(a);
			exp.accept(this);
		}
	}
	
	public void visit(MethodDeclaration md) throws ParseTreeException {
		if (Api.usingApi() && !md.getName().equals(Api.getMethodUnderConsideration())) {
			return;
		}
		bindMethodParams(md);
		bindLocalVariables(md.getBody());
		StatementList body = md.getBody();
		for (int s = 0; s < body.size(); s++) {
			body.get(s).accept(this);
		}
	}
	
	public void visit(VariableDeclaration vd) throws ParseTreeException {
		if (!(getMutationsLeft(vd) > 0)) return;
		vd.getInitializer().accept(this);
	}
	
	private List<Class<?>> getChilds(Class<?> clazz) {
		List<Class<?>> childs = new LinkedList<Class<?>>();
		Reflections reflections = new Reflections(new ConfigurationBuilder()
										.setUrls(ClasspathHelper.forPackage(this.lfile_env.getPackage()))
										.setScanners(new SubTypesScanner())
									);
		childs.addAll(reflections.getSubTypesOf(clazz));
		return childs;
	}
	
	private List<Class<?>> getParents(Class<?> clazz) {
		List<Class<?>> parents = new LinkedList<Class<?>>();
		Class<?> superClass = clazz.getSuperclass();
		while (superClass != null) {
			parents.add(superClass);
			superClass = superClass.getSuperclass();
		}
		return parents;
	}
	
	private String getClassName(String input) {
		if (input.startsWith("class")) {
			input = input.replace("class", "");
			input = input.trim();
		}
		return input;
	}
	
	private void outputToFile(Variable original, CastExpression mutant) {
		MutantsInformationHolder.mainHolder().addMutation(Mutant.PCI, original, mutant);
	}
	
	private void outputToFile(FieldAccess original, CastExpression mutant) {
		MutantsInformationHolder.mainHolder().addMutation(Mutant.PCI, original, mutant);
	}

}
