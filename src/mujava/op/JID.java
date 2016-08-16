////////////////////////////////////////////////////////////////////////////
// Module : JID.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import openjava.mop.*;
import openjava.ptree.*;

/**
 * <p>
 * Generate JID (Java-specific member variable initialization deletion) --
 * remove initialization of each member variable
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class JID extends mujava.op.util.Mutator {
	
	public JID(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}

	/**
	 * If an instance variable is not final, delete its initialization
	 */
	public void visit(FieldDeclaration p) throws ParseTreeException {
		if (!(getMutationsLeft(p) > 0))
			return;
		VariableInitializer initializer = p.getInitializer();
		if (p.getModifiers().contains(ModifierList.FINAL))
			return;

		if (initializer != null) {
			FieldDeclaration mutant = (FieldDeclaration) nodeCopyOf(p);
			mutant.setInitializer(null);
			outputToFile(p, mutant);
		}
			
	}

	/**
	 * Output JID mutants to files
	 * 
	 * @param original
	 */
	private void outputToFile(FieldDeclaration original, FieldDeclaration mutant) {
		MutantsInformationHolder.mainHolder().addMutation(MutationOperator.JID, original, mutant);
	}
}
