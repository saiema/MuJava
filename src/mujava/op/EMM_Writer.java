////////////////////////////////////////////////////////////////////////////
// Module : EMM_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import java.io.*;
import openjava.ptree.*;
import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;

/**
 * <p>
 * Output and log EMM mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class EMM_Writer extends MutantCodeWriter {
	private MethodCall original = null;
	private MethodCall mutant = null;

	public EMM_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant((MethodCall)this.mi.getOriginal(), (MethodCall)this.mi.getMutant());
	}
	
	private void setMutant(MethodCall original, MethodCall mutant) {
		this.mutant = mutant;
		this.original = original;
	}

	public void visit(MethodCall p) throws ParseTreeException {
		if (isSameObject(p, this.original)) {
			this.original = null;
			super.visit(this.mutant);
			// -------------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
}
