////////////////////////////////////////////////////////////////////////////
// Module : JTI_Writer.java
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
 * Output and log JTI mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class JTI_Writer extends MutantCodeWriter {
	private Variable original;
	private Expression mutant;

	public JTI_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object v1, Object v2) {
		this.original = (Variable)v1;
		this.mutant = (Expression)v2;
	}

	public void visit(Variable p) throws ParseTreeException {
		if (isSameObject(p, this.original)) {
			String originalString = this.original.toFlattenString();
			this.original = null;
			super.visit(this.mutant);
			// -------------------------------------------------------------
			mutated_line = line_num;
			writeLog(removeNewline(originalString + " ==> " + this.mutant.toFlattenString()));
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
}
