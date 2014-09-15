package mujava.op;

import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJClassNotFoundException;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.ConstructorDeclaration;
import openjava.ptree.FieldDeclaration;
import openjava.ptree.MethodDeclaration;
import openjava.ptree.Parameter;
import openjava.ptree.ParameterList;
import openjava.ptree.ParseTreeException;
import openjava.ptree.TypeName;
import openjava.ptree.VariableDeclaration;
import mujava.api.Api;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.app.MutationRequest;
import mujava.op.util.Mutator;

public class PMD extends Mutator {
	private boolean pmd = true;

	public PMD(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}
	
	public void setPPD() {
		this.pmd = false;
	}
	
	public void setPMD() {
		this.pmd = true;
	}
	
	public void visit(VariableDeclaration vd) throws ParseTreeException {
		if (getMutationsLeft(vd) <= 0) return;
		if (!this.pmd) return; 
		try {
			OJClass varType = OJClass.forName(vd.getTypeSpecifier().getName());
			OJClass parentsType = varType.getSuperclass();
			VariableDeclaration copy = (VariableDeclaration) vd.makeRecursiveCopy_keepOriginalID();
			copy.setTypeSpecifier(TypeName.forOJClass(parentsType));
			outputToFile(vd, copy);
		} catch (OJClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void visit(FieldDeclaration fd) throws ParseTreeException {
		if (Api.usingApi() && (Api.getMethodUnderConsideration().compareTo(MutationRequest.MUTATE_FIELDS)!=0)) {
			return;
		}
		if (getMutationsLeft(fd) <= 0) return;
		if (!this.pmd) return;
		try {
			OJClass varType = OJClass.forName(fd.getTypeSpecifier().getName());
			OJClass parentsType = varType.getSuperclass();
			FieldDeclaration copy = (FieldDeclaration) fd.makeRecursiveCopy_keepOriginalID();
			copy.setTypeSpecifier(TypeName.forOJClass(parentsType));
			outputToFile(fd, copy);
		} catch (OJClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void visit(MethodDeclaration md) throws ParseTreeException {
		if (Api.usingApi() && !md.getName().equals(Api.getMethodUnderConsideration())) {
			return;
		}
		super.visit(md.getBody());
		if (getMutationsLeft(md) <= 0) return;
		if (this.pmd) return;
		ParameterList params = md.getParameters();
		for (int p = 0; p < params.size(); p++) {
			Parameter param = params.get(p);
			try {
				OJClass varType = OJClass.forName(param.getTypeSpecifier().getName());
				OJClass parentsType = varType.getSuperclass();
				MethodDeclaration copy = (MethodDeclaration) md.makeRecursiveCopy_keepOriginalID();
				copy.getParameters().get(p).setTypeSpecifier(TypeName.forOJClass(parentsType));
				outputToFile(md, copy);
			} catch (OJClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void visit( ConstructorDeclaration cd ) throws ParseTreeException {
		if (Api.usingApi() && !cd.getName().equals(Api.getMethodUnderConsideration())) {
			return;
		}
		super.visit(cd.getBody());
		if (getMutationsLeft(cd) <= 0) return;
		if (this.pmd) return;
		ParameterList params = cd.getParameters();
		for (int p = 0; p < params.size(); p++) {
			Parameter param = params.get(p);
			try {
				OJClass varType = OJClass.forName(param.getTypeSpecifier().getName());
				OJClass parentsType = varType.getSuperclass();
				ConstructorDeclaration copy = (ConstructorDeclaration) cd.makeRecursiveCopy_keepOriginalID();
				copy.getParameters().get(p).setTypeSpecifier(TypeName.forOJClass(parentsType));
				outputToFile(cd, copy);
			} catch (OJClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void outputToFile(ConstructorDeclaration original, ConstructorDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.PPD, original, mutant);
	}

	private void outputToFile(VariableDeclaration original, VariableDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.PMD, original, mutant);
	}
	
	private void outputToFile(FieldDeclaration original, FieldDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.PMD, original, mutant);
	}

	private void outputToFile(MethodDeclaration original, MethodDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.PPD, original, mutant);
	}
	
}
