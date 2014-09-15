package mujava.op;

import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJMethod;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionList;
import openjava.ptree.ExpressionStatement;
import openjava.ptree.MethodCall;
import openjava.ptree.MethodDeclaration;
import openjava.ptree.Parameter;
import openjava.ptree.ParameterList;
import openjava.ptree.ParseTreeException;
import openjava.ptree.SelfAccess;
import openjava.ptree.Statement;
import openjava.ptree.StatementList;
import openjava.ptree.TypeName;
import mujava.api.Api;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.op.util.Mutator;

public class IOP extends Mutator {
	
	public IOP(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}
	
	public void visit(MethodDeclaration md) throws ParseTreeException {
		if (Api.usingApi() && !md.getName().equals(Api.getMethodUnderConsideration())) {
			return;
		}
		if (getMutationsLeft(md) <= 0)
			return;
		if (md.getName().equals("main"))
			return;
		if (!isOverridingParent(md)) {
			return;
		}
		StatementList stments = md.getBody();
		for (int s = 0; s < stments.size(); s++) {
			Statement st = stments.get(s);
			if (st instanceof ExpressionStatement) {
				Expression ex = ((ExpressionStatement) st).getExpression();
				if (ex instanceof MethodCall) {
					if (isSuperCallOfSameMethod((MethodCall)ex, md)) {
						generateMutants(md, st, s);
					}
				}
			}
		}
	}

	private void generateMutants(MethodDeclaration md, Statement st, int s) {
		MethodDeclaration originalCopy = (MethodDeclaration) md.makeRecursiveCopy_keepOriginalID();
		if (md.getBody().size() == 2) {
			if (s == 0) {
				MethodDeclaration mutant = generateMutant(originalCopy, s, 1);
				outputToFile(md, mutant);
			} else {
				//s == 1
				MethodDeclaration mutant = generateMutant(originalCopy, s, 0);
				outputToFile(md, mutant);
			}
		} else if (md.getBody().size() == 3) {
			if (s == 0) {
				MethodDeclaration mutant1 = generateMutant(originalCopy, s, 1);
				MethodDeclaration mutant2 = generateMutant(originalCopy, s, 2);
				outputToFile(md, mutant1);
				outputToFile(md, mutant2);
			} else if (s == 1) {
				MethodDeclaration mutant1 = generateMutant(originalCopy, s, 0);
				MethodDeclaration mutant2 = generateMutant(originalCopy, s, 2);
				outputToFile(md, mutant1);
				outputToFile(md, mutant2);
			} else {
				//s == 2
				MethodDeclaration mutant1 = generateMutant(originalCopy, s, 0);
				MethodDeclaration mutant2 = generateMutant(originalCopy, s, 1);
				outputToFile(md, mutant1);
				outputToFile(md, mutant2);
			}
		} else if (md.getBody().size() > 3) {
			if (s == 0) {
				MethodDeclaration mutant1 = generateMutant(originalCopy, s, 1);
				MethodDeclaration mutant2 = generateMutant(originalCopy, s, md.getBody().size()-1);
				outputToFile(md, mutant1);
				outputToFile(md, mutant2);
			} else if (s == md.getBody().size() - 1) {
				MethodDeclaration mutant1 = generateMutant(originalCopy, s, 0);
				MethodDeclaration mutant2 = generateMutant(originalCopy, s, md.getBody().size()-2);
				outputToFile(md, mutant1);
				outputToFile(md, mutant2);
			} else if (s == 1) {
				MethodDeclaration mutant1 = generateMutant(originalCopy, s, 0);
				MethodDeclaration mutant2 = generateMutant(originalCopy, s, s+1);
				MethodDeclaration mutant3 = generateMutant(originalCopy, s, md.getBody().size()-1);
				outputToFile(md, mutant1);
				outputToFile(md, mutant2);
				outputToFile(md, mutant3);
			} else if (s == md.getBody().size() - 2) {
				MethodDeclaration mutant1 = generateMutant(originalCopy, s, 0);
				MethodDeclaration mutant2 = generateMutant(originalCopy, s, s-1);
				MethodDeclaration mutant3 = generateMutant(originalCopy, s, md.getBody().size()-1);
				outputToFile(md, mutant1);
				outputToFile(md, mutant2);
				outputToFile(md, mutant3);
			} else {
				MethodDeclaration mutant1 = generateMutant(originalCopy, s, 0);
				MethodDeclaration mutant2 = generateMutant(originalCopy, s, md.getBody().size() - 1);
				MethodDeclaration mutant3 = generateMutant(originalCopy, s, s+1);
				MethodDeclaration mutant4 = generateMutant(originalCopy, s, s-1);
				outputToFile(md, mutant1);
				outputToFile(md, mutant2);
				outputToFile(md, mutant3);
				outputToFile(md, mutant4);
			}
		}
	}

	private MethodDeclaration generateMutant(MethodDeclaration original, int origPos, int newPos) {
		MethodDeclaration mutant = (MethodDeclaration) original.makeRecursiveCopy_keepOriginalID();
		StatementList statements = mutant.getBody();
		replace(statements, origPos, newPos);
		return mutant;
	}
	
	private void replace(StatementList list, int origPos, int newPos) {
		int o = origPos;
		int move = origPos<newPos?1:(-1);
		int d;
		while (o != newPos) {
			d = o + move;
			swap(list, o, d);
			o += move;
		}
	}
	
	private void swap(StatementList list, int origPos, int newPos) {
		int o = origPos<newPos?origPos:newPos;
		int d = origPos<newPos?newPos:origPos;
		Statement dest = list.remove(d);
		list.insertElementAt(dest, o);
		Statement orig = list.remove(o+1);
		list.insertElementAt(orig, d);
	}
	
	private void outputToFile(MethodDeclaration original, MethodDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.IOP, original, mutant);
	}

	private boolean isSuperCallOfSameMethod(MethodCall mc, MethodDeclaration md) throws ParseTreeException {
		boolean isSameMethod = compare(mc, md);
		if (isSameMethod) {
			Expression lexp = mc.getReferenceExpr();
			if (lexp == null) {
				return false;
			}
			if (!(((SelfAccess) lexp).getAccessType() == SelfAccess.SUPER))
				return false;
			return true;
		}
		return false;
	}

	private boolean isOverridingParent(MethodDeclaration md) throws ParseTreeException {
		OJMethod[] inheritedMethods = getInheritedMethods(getSelfType());
		for (OJMethod m : inheritedMethods) {
			if (compare(m, md)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean compare(OJMethod md1, MethodDeclaration md2) {
		String f1Name = md1.getName();
		String f2Name = md2.getName();
		String f1RetType = md1.getReturnType().getName();
		String f2RetType = md2.getReturnType().getName();
		if (f1Name.equals(f2Name) && f1RetType.equals(f2RetType)) {
			OJClass[] fd1Params = md1.getParameterTypes();
			ParameterList fd2Params = md2.getParameters();
			if (fd1Params.length == fd2Params.size()) {
				for (int p = 0; p < fd1Params.length; p++) {
					OJClass fd1Param = fd1Params[p];
					Parameter fd2Param = fd2Params.get(p);
					if (fd1Param.getModifiers().toModifier() != fd2Param.getModifiers().getRegular()) {
						return false;
					}
					if (fd1Param.getName().compareTo(fd2Param.getTypeSpecifier().getName())!=0) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	private boolean compare(MethodCall mc, MethodDeclaration md) throws ParseTreeException {
		String mcName = mc.getName();
		String mdName = md.getName();
		OJClass mcType = getType(mc);
		TypeName mdType = md.getReturnType();
		if (mcName.compareTo(mdName) != 0) {
			return false;
		}
		if (mcType.getName().compareTo(mdType.getName()) != 0) {
			return false;
		}
		ExpressionList mcArgs = mc.getArguments();
		ParameterList mdArgs = md.getParameters();
		if (mcArgs.size() != mdArgs.size()) {
			return false;
		}
		for (int p = 0; p < mcArgs.size(); p++) {
			Expression mcArg = mcArgs.get(p);
			Parameter mdArg = mdArgs.get(p);
			OJClass mcArgType = getType(mcArg);
			TypeName mdArgType = mdArg.getTypeSpecifier();
			if (mcArgType.getName().compareTo(mdArgType.getName()) != 0) {
				return false;
			}
		}
		return true;
	}

}
