////////////////////////////////////////////////////////////////////////////
// Module : PNC_Writer.java
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
 * Output and log PNC mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class PNC_Writer extends MutantCodeWriter {
	
	private AllocationExpression original = null;
	private AllocationExpression mutant = null;

	public PNC_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object original, Object mutant) {
		this.original = (AllocationExpression) original;
		this.mutant = (AllocationExpression) mutant;
	}

	public void visit(AllocationExpression p) throws ParseTreeException {
		if (isSameObject(p, this.original)) {
			this.mutant.setMutGenLimit(this.original.getMutGenLimit());
			super.visit(this.mutant);
			// -------------------------------------------------------------
			mutated_line = line_num;
			writeLog(removeNewline(this.original.toString() + " => " + this.mutant.toString()));
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
}
