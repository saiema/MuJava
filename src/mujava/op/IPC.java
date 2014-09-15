////////////////////////////////////////////////////////////////////////////
// Module : IPC.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import mujava.api.Api;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>
 * Generate IPC (Explicit call to parent's constructor deletion) mutants --
 * delete <i>super</i> constructor calls, causing the default constructor of the
 * parent class to be called
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class IPC extends mujava.op.util.Mutator {

	public IPC(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}

	public void visit(ConstructorDeclaration p) throws ParseTreeException {
		super.visit(p);
		if (Api.usingApi() && !p.getName().equals(Api.getMethodUnderConsideration())) {
			return;
		}
		if (!(getMutationsLeft(p)>0)) return;
		ConstructorInvocation first = p.getConstructorInvocation();
		if (first != null) {
			if (!(first.isSelfInvocation())) {
				if (first.getArguments().size() > 0) {
					outputToFile(p, first);
				}
			}
		}
		
	}

	/**
	 * Output IPC mutants to files
	 * 
	 * @param mutant
	 */
	public void outputToFile(ConstructorDeclaration original, ConstructorInvocation mutant) {
		MutantsInformationHolder.mainHolder().addMutantIdentifier(Mutant.IPC, original, mutant);
	}
}
