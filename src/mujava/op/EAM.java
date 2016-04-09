////////////////////////////////////////////////////////////////////////////
// Module : EAM.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Generate EAM (Java-specific accessor method change) mutants -- change the
 * accessor method name for other compatible accessor method names. Note:
 * <i>compatible</i> means that the signatures are the same except the method
 * name
 * </p>
 * <p>
 * <i>Example</i>: point.getX(); is mutated to point.getY();
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class EAM extends mujava.op.util.Mutator {

	public EAM(FileEnvironment file_env, ClassDeclaration cdecl,
			CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}

	public void visit(MethodCall p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		if (!isGetter(p))
			return;
		List<OJMethod> getters = getAllGetters(getSelfType());
		OJMethod original = getOriginalMethod(p, getters);
		for (OJMethod g : getters) {
			if (!isSameMethod(p, g) && compatibleMethods(original, g)) {
				MethodCall mutant = (MethodCall) p.makeRecursiveCopy_keepOriginalID();
				mutant.setName(g.getName());
				outputToFile(p, mutant);
			}
		}
	}

	private OJMethod getOriginalMethod(MethodCall p, List<OJMethod> getters) throws ParseTreeException {
		for (OJMethod g : getters) {
			if (isSameMethod(p, g)) {
				return g;
			}
		}
		return null;
	}

	private boolean isSameMethod(MethodCall m1, OJMethod m2)throws ParseTreeException {
		String nm1 = m1.getName();
		String nm2 = m2.getName();
		if (nm1.compareTo(nm2) != 0) {
			return false;
		}
		OJClass rtm1 = getType(m1);
		OJClass rtm2 = m2.getReturnType();
		if (rtm1.getName().compareTo(rtm2.getName()) != 0) {
			return false;
		}
		int argsM1 = m1.getArguments().size();
		int argsM2 = m2.getParameterTypes().length;
		if (argsM1 != argsM2) {
			return false;
		}
		return true;
	}

	private List<OJMethod> getAllGetters(OJClass t) {
		List<OJMethod> result = new LinkedList<OJMethod>();
		OJMethod[] allMethods = getAllMethods(t);
		for (OJMethod m : allMethods) {
			if (isGetter(m)) {
				result.add(m);
			}
		}
		return result;
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
				if (!(compatibleAssignType(c1[i], (c2[i]))))
					return false;
			}
		} else {
			return false;
		}
		OJClass retType1 = m1.getReturnType();
		OJClass retType2 = m2.getReturnType();
		if (!(compatibleAssignType(retType1, retType2))) {
			return false;
		}
		return true;
	}

	private boolean isGetter(OJMethod m) {
		return m.getName().startsWith("get") && m.getReturnType().getName().compareToIgnoreCase("void")!=0;
	}

	private boolean isGetter(MethodCall p) throws ParseTreeException {
		return p.getName().startsWith("get") && getType(p).getName().compareToIgnoreCase("void")!=0;
	}

	/**
	 * Output EAM mutants to files
	 * 
	 * @param original
	 * @param mutant
	 */
	public void outputToFile(MethodCall original, MethodCall mutant) {
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.EAM, original, mutant);
	}

}
