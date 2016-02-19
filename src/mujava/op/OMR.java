package mujava.op;

import java.util.LinkedList;
import java.util.List;

import openjava.mop.FileEnvironment;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.MemberDeclaration;
import openjava.ptree.MemberDeclarationList;
import openjava.ptree.MethodDeclaration;
import openjava.ptree.Parameter;
import openjava.ptree.ParameterList;
import openjava.ptree.ParseTreeException;
import openjava.ptree.StatementList;
import mujava.api.Api;
import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import mujava.op.util.Mutator;

public class OMR extends Mutator {
	private List<MethodDeclaration> methods = new LinkedList<MethodDeclaration>();
	private boolean smartMode = false;
	
	public OMR(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}
	
	
	public void smartMode() {
		this.smartMode = true;
	}
	
	public void dumbMode() {
		this.smartMode = false;
	}
	
	@Override
	public ClassDeclaration evaluateUp(ClassDeclaration ptree)
			throws ParseTreeException {
		return ptree;
	}
	
	public void visit(ClassDeclaration cs) throws ParseTreeException {
		super.visit(cs);
		boolean enterInnerClass = false;
		if (Api.usingApi()) {
			enterInnerClass = Api.enterInnerClass(cs.getName());
		}
		MemberDeclarationList members = cs.getBody();
		for (int m = 0; m < members.size(); m++) {
			MemberDeclaration member = members.get(m);
			if (member instanceof MethodDeclaration) {
				MethodDeclaration method = (MethodDeclaration) member;
				if (Api.insideClassToMutate() && Api.getMethodUnderConsideration().compareTo(method.getName())==0) {
					//get only the methods with the same name as the method under consideration
					methods.add((MethodDeclaration) member);
				}
			}
		}
		for (int m = 0; m < members.size(); m++) {
			visit(members.get(m));
		}
		if (Api.usingApi()) {
			Api.leaveInnerClass(cs.getName(), enterInnerClass);
		}
	}
	
	public void visit(MethodDeclaration md) throws ParseTreeException {
		if (Api.usingApi() && (!Api.insideClassToMutate() || Api.getMethodUnderConsideration().compareTo(md.getName()) != 0)) {
			return;
		}
		if (!(getMutationsLeft(md) > 0)) return;
		for (MethodDeclaration amd : this.methods) {
			if (sameMethods(md, amd)) continue;
			MethodDeclaration copy = (MethodDeclaration) md.makeRecursiveCopy_keepOriginalID();
			StatementList newBody = (StatementList) amd.getBody().makeRecursiveCopy_keepOriginalID();
			if (this.smartMode) {
				adjustBody(newBody, amd.getParameters(), md.getParameters());
			}
			copy.setBody(newBody);
			outputToFile(md, copy);
		}
	}
	
	private void adjustBody(StatementList body, ParameterList parametersOrig, ParameterList parametersNew) {
		//TODO: this method will add all parameters that are in parametersOrig but are not in parametersNew in body as local variables
		//TODO: this should modify or delete return statments
		//TODO: change this method signature
	}


	private boolean sameMethods(MethodDeclaration md1, MethodDeclaration md2) {
		ParameterList params1 = md1.getParameters();
		ParameterList params2 = md2.getParameters();
		if (params1.size() != params2.size()) {
			return false;
		}
		for (int p = 0; p < params1.size(); p++) {
			Parameter p1 = params1.get(p);
			Parameter p2 = params2.get(p);
			if (p1.getTypeSpecifier().getName().compareTo(p2.getTypeSpecifier().getName()) != 0) {
				return false;
			}
		}
		return true;
	}

	private void outputToFile(MethodDeclaration original, MethodDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.OMR, original, mutant);
	}
	
	
}
