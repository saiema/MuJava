package mujava.op;

import mujava.api.Api;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.app.MutationRequest;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJMethod;
import openjava.mop.OJModifier;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.MethodDeclaration;
import openjava.ptree.ModifierList;
import openjava.ptree.Parameter;
import openjava.ptree.ParameterList;
import openjava.ptree.ParseTreeException;
import openjava.ptree.TypeName;

public class IOD extends mujava.op.util.Mutator {

	public IOD(FileEnvironment file_env, ClassDeclaration cdecl,
			CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}

	@Override
	public ClassDeclaration evaluateUp(ClassDeclaration ptree)
			throws ParseTreeException {
		return ptree;
	}

	public void visit(ClassDeclaration cs) throws ParseTreeException {
		super.visit(cs);

		if (Api.usingApi()
				&& (Api.getMethodUnderConsideration().compareTo(
						MutationRequest.MUTATE_CLASS) != 0)) {
			return;
		}
		if (!(getMutationsLeft(cs) > 0))
			return;
		
		OJMethod[] localMethods = getSelfType().getDeclaredMethods();
		OJMethod[] inheritedMethods = getInheritedMethods(getSelfType());
		for (int i = 0; i < inheritedMethods.length; i++) {
			boolean isHidden = false;
			for (int l = 0; l < localMethods.length; l++) {
				if (compareMethods(inheritedMethods[i], localMethods[l])) {
					isHidden = true;
					break;
				}
			}
			if (isHidden) {

				try {
					ModifierList modlist = new ModifierList();
					OJModifier modif = inheritedMethods[i].getModifiers();
					TypeName tname = TypeName.forOJClass(inheritedMethods[i].getReturnType());
					modlist.add(modif.toModifier());
					String name = inheritedMethods[i].getName();
					ParameterList params = getParams(inheritedMethods[i]);
					TypeName[] throwList = getThrowList(inheritedMethods[i]);
					MethodDeclaration mutant = new MethodDeclaration(modlist,
							tname, name, params, throwList, null, null);
					outputToFile(cs, mutant);
				} catch (Exception ex) {
					System.err.println("[Exception]  " + ex);
				}

			}
		}

	}

	private TypeName[] getThrowList(OJMethod m) {
		TypeName[] exceptions = new TypeName[m.getExceptionTypes().length];
		int i = 0;
		for (OJClass ex : m.getExceptionTypes()) {
			exceptions[i] = TypeName.forOJClass(ex);
			i++;
		}
		return null;
	}

	private ParameterList getParams(OJMethod m) {
		ParameterList params = new ParameterList();
		OJClass[] parameterTypes = m.signature().getParameterTypes();
		TypeName[] types = new TypeName[parameterTypes.length];
		String[] names = new String[parameterTypes.length];
		for (int p = 0; p < parameterTypes.length; p++) {
			types[p] = TypeName.forOJClass(parameterTypes[p]);
			names[p] = types[p].getName()+p;
			Parameter param = new Parameter(null, types[p], names[p]);
			params.add(param);
		}
		return params;
	}

	private boolean compareMethods(OJMethod m1, OJMethod m2) {
		String m1SimpleName = removePackages(m1.getName());
		String m2SimpleName = removePackages(m2.getName());
		boolean sameReturnType = isSameType(m1.getReturnType(), m2.getReturnType());
		boolean sameParams = sameParameters(m1.getParameterTypes(), m2.getParameterTypes());
		if (m1SimpleName.compareTo(m2SimpleName) != 0) {
			return false;
		}
		if (!sameReturnType) {
			return false;
		}
		if (!sameParams) {
			return false;
		}
		return true;
	}

	private boolean sameParameters(OJClass[] params1, OJClass[] params2) {
		boolean sameLength = params1.length == params2.length;
		if (!sameLength) {
			return false;
		}
		for (int p = 0; p < params1.length; p++) {
			if (!isSameType(params1[p], params2[p])) {
				return false;
			}
		}
		return true;
	}

	private boolean isSameType(OJClass t1, OJClass t2) {
		return t1.getName().compareTo(t2.getName())==0;
	}

	private String removePackages(String name) {
		String[] segments = name.split("\\.");
		return segments[segments.length-1];
	}

	private void outputToFile(ClassDeclaration original, MethodDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.IOD, original, mutant);
	}

}
