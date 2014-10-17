////////////////////////////////////////////////////////////////////////////
// Module : AMC_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;
import openjava.ptree.*;

import java.io.*;

/**
 * <p>
 * Output and log AMC mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class AMC_Writer extends MutantCodeWriter {
	private ModifierList original;
	private ModifierList mutant;

	public AMC_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}

	private void setMutant(Object original, Object mutant) {
		this.original = (ModifierList) original;
		this.mutant = (ModifierList) mutant;
	}

	public void visit(ModifierList p) throws ParseTreeException {
		if (isSameObject(p, this.original)) {
			this.original = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
}
