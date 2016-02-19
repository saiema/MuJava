////////////////////////////////////////////////////////////////////////////
// Module : EMM.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import java.util.Arrays;
import java.util.LinkedList;

import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>
 * Generate EMM (Java-specific modifier method change) mutants -- change the
 * modifier method name for other compatible modifier method names. Note:
 * <i>compatible</i> means that the signatures are the same except the method
 * name
 * </p>
 * <p>
 * <i>Example</i>: point.setX(2); is mutated to point.setY(2);
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class EMM extends mujava.op.util.Mutator {
	public EMM(FileEnvironment file_env, ClassDeclaration cdecl,
			CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}

	public void visit(MethodCall p) throws ParseTreeException {

		if (!(getMutationsLeft(p) > 0))
			return;
		int i;
		MethodCall mutant = null;
		String method_name = p.getName();

		if ((method_name.indexOf("set") == 0) && (p.getArguments().size() == 1)) {
			Expression ref = p.getReferenceExpr();
			java.util.List<OJMethod> ml = methods(ref);

			try {
				OJMethod[] m = ml.toArray(new OJMethod[ml.size()]);
				boolean[] find_flag = new boolean[m.length];
				int method_index = -1;
				for (i = 0; i < m.length; i++) {
					find_flag[i] = false;
					// find my method
					if (m[i].getName().equals(method_name)) {
						method_index = i;
						break;
					}
				}

				if (method_index == -1) {
					// m = bindedtype.getAllMethods();
					find_flag = new boolean[m.length];
					for (i = 0; i < m.length; i++) {
						find_flag[i] = false;
						// find my method
						if (m[i].getName().equals(method_name)) {
							method_index = i;
							break;
						}
					}
				}

				if (method_index == -1)
					return;

				int set_num = 0;
				for (i = 0; i < m.length; i++) {
					if ((i != method_index)
							&& (m[i].getName().indexOf("set") == 0)
							&& sameReturnType(m[i], m[method_index])
							&& compatibleMethods(m[i], m[method_index])) {
						find_flag[i] = true;
						set_num++;
					}
				}

				if (set_num > 0) {
					for (i = 0; i < m.length; i++) {
						if (find_flag[i]) {
							mutant = (MethodCall) p.makeRecursiveCopy_keepOriginalID();
							mutant.setName(m[i].getName());
							outputToFile(p, mutant);
						}
					}
					return;
				}
			} catch (Exception e) {
				System.err.println(" [error] " + e);
				e.printStackTrace();
			}
			// }
		}

		Expression newp = this.evaluateDown(p);
		if (newp != p) {
			p.replace(newp);
			return;
		}

		p.childrenAccept(this);
		newp = this.evaluateUp(p);
		if (newp != p)
			p.replace(newp);
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
		return true;
	}

	private java.util.List<OJMethod> methods(Expression e)
			throws ParseTreeException {
		OJClass t = null;
		if (e instanceof MethodCall || e instanceof FieldAccess
				|| e instanceof Variable) {
			t = getType(e);
		} else if (e instanceof SelfAccess) {
			t = getType(e);
		} else if (e == null) {
			t = getSelfType();
		}
		return methods(t);
	}

	private java.util.List<OJMethod> methods(OJClass t)
			throws ParseTreeException {
		java.util.List<OJMethod> fnm = new LinkedList<OJMethod>();
		fnm.addAll(Arrays.asList(getAllMethods(t)));
		return fnm;
	}

	/**
	 * Output EMM mutants to files
	 * 
	 * @param original
	 * @param mutant
	 */
	public void outputToFile(MethodCall original, MethodCall mutant) {
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.EMM, original, mutant);
	}

}
