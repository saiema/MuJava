package mujava.op;

import java.util.LinkedList;
import java.util.List;

import mujava.api.Api;
import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import mujava.app.MutationRequest;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJField;
import openjava.mop.OJModifier;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.FieldDeclaration;
import openjava.ptree.ModifierList;
import openjava.ptree.ParseTreeException;
import openjava.ptree.TypeName;

public class IHD extends mujava.op.util.Mutator {

	public IHD(FileEnvironment file_env, ClassDeclaration cdecl,
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

		boolean enterInnerClass = false;
		if (Api.usingApi()) {
			enterInnerClass = Api.enterInnerClass(cs.getName());
		}
		
		if (Api.usingApi()
				&& 
				(		!Api.insideClassToMutate()
						||
						(Api.getMethodUnderConsideration().compareTo(
						MutationRequest.MUTATE_CLASS) != 0
				))) {
			if (Api.usingApi()) {
				Api.leaveInnerClass(cs.getName(), enterInnerClass);
			}
			return;
		}
		if (!(getMutationsLeft(cs) > 0)) {
			if (Api.usingApi()) {
				Api.leaveInnerClass(cs.getName(), enterInnerClass);
			}
			return;
		}

		OJField[] localFields = getSelfType().getDeclaredFields();
		OJField[] inheritedFields = filterInheritable(getSelfType()
				.getSuperclass().getAllFields());
		for (int i = 0; i < inheritedFields.length; i++) {
			boolean isHidden = false;
			for (int l = 0; l < localFields.length; l++) {
				if (compareFields(inheritedFields[i], localFields[l])) {
					isHidden = true;
					break;
				}
			}
			if (isHidden) {

				try {
					ModifierList modlist = new ModifierList();
					OJModifier modif = inheritedFields[i].getModifiers();
					TypeName tname = TypeName.forOJClass(inheritedFields[i].getType());
					modlist.add(modif.toModifier());
					String name = inheritedFields[i].getName();
					FieldDeclaration mutant = new FieldDeclaration(modlist,
							tname, name, null);
					outputToFile(cs, mutant);
				} catch (Exception ex) {
					if (Api.usingApi()) {
						Api.leaveInnerClass(cs.getName(), enterInnerClass);
					}
					System.err.println("[Exception]  " + ex);
				}

			}
		}
		
		if (Api.usingApi()) {
			Api.leaveInnerClass(cs.getName(), enterInnerClass);
		}

	}

	private OJField[] filterInheritable(OJField[] allFields) {
		List<OJField> inheritableFields = new LinkedList<OJField>();
		for (OJField f : allFields) {
			if (f.getModifiers().isFinal()) {
				continue;
			}
			if (f.getModifiers().isPrivate()) {
				continue;
			}
			inheritableFields.add(f);
		}
		return inheritableFields.toArray(new OJField[inheritableFields.size()]);
	}

	private boolean compareFields(OJField f1, OJField f2) {
		if (f1.getName().compareTo(f2.getName()) == 0) {
			OJClass f1Type = f1.getType();
			OJClass f2Type = f2.getType();
			if (f1Type.getName().compareTo(f2Type.getName()) == 0)
				return true;
			return false;
		} else {
			return false;
		}
	}

	private void outputToFile(ClassDeclaration original, FieldDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.IHD, original, mutant);
	}

}
