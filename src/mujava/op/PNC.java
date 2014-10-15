////////////////////////////////////////////////////////////////////////////
// Module : PNC.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import mujava.api.Api;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>
 * Generate PNC (New method call with child class type) mutants -- change the
 * instantiated type of an object reference to cause the object reference to
 * refer to an object of a type that is different from the declared type
 * </p>
 * <p>
 * <i>Example</i>: let class A be the parent of class B -- A a; a = new A(); is
 * mutated to A a; a = new B();
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class PNC extends mujava.op.util.Mutator {
	private FileEnvironment lfile_env;

	public PNC(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super(file_env, comp_unit);
		this.lfile_env = file_env;
	}

	public void visit(AllocationExpression p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0)) return;
		TypeName originalType = p.getClassType();
		Class<?> originalClass;
		try {
			originalClass = Class.forName(originalType.getName());
			for (Class<?> child : getChilds(originalClass)) {
				if (child.getName().compareTo(originalClass.getName())==0) continue; 
				generateMutant(p, child);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generateMutant(AllocationExpression original, Class<?> child) throws ParseTreeException {
		ExpressionList arguments = original.getArguments();
		Constructor<?>[] childConstructors = child.getConstructors();
		for (Constructor<?> c : childConstructors) {
			if (compatibleArguments(c.getParameterTypes(), arguments)) {
				OJClass newConstructorAsOJC = OJClass.forClass(child);
				AllocationExpression mutant = (AllocationExpression) original
						.makeRecursiveCopy_keepOriginalID();
				TypeName newConstructorAsTypeName = TypeName
						.forOJClass(newConstructorAsOJC);
				mutant.setClassType(newConstructorAsTypeName);
				if (c.getParameterTypes().length < arguments.size()) {
					arguments = arguments.subList(0,
							c.getParameterTypes().length - 1);
					mutant.setArguments(arguments);
				}
				outputToFile(original, mutant);
			}
		}
	}

	private boolean compatibleArguments(Class<?>[] parameterTypes, ExpressionList arguments) throws ParseTreeException {
		if (parameterTypes.length <= arguments.size()) {
			for (int p = 0; p < parameterTypes.length; p++) {
				OJClass argumentType = OJClass.forClass(parameterTypes[p]);
				if (!compatibleAssignType(argumentType,
						getType(arguments.get(p)))) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private List<Class<?>> getChilds(Class<?> clazz) {
		List<Class<?>> childs = new LinkedList<Class<?>>();
		Reflections reflections = new Reflections(
				new ConfigurationBuilder()
						.setUrls(
								ClasspathHelper.forPackage(this.lfile_env
										.getPackage())).setScanners(
								new SubTypesScanner()));
		childs.addAll(reflections.getSubTypesOf(clazz));
		return childs;
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
		if (Api.usingApi() && (!Api.insideClassToMutate() || !md.getName().equals(Api.getMethodUnderConsideration()))) {
			return;
		}
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

	
	private void outputToFile(AllocationExpression original, AllocationExpression mutant) {
		MutantsInformationHolder.mainHolder().addMutation(Mutant.PNC, original, mutant);
	}

}
