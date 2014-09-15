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

public class PCD_Writer extends MutantCodeWriter {
	private CastExpression original;
	private Expression mutant;

	public PCD_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}

	private void setMutant(Object original, Object mutant) {
		this.original = (CastExpression) original;
		this.mutant = (Expression) mutant;
	}

	public void visit(CastExpression p) throws ParseTreeException {
		if (isSameObject(p, original)) {
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			String log_str = this.original.toFlattenString() + " => " + this.mutant.toFlattenString();
			writeLog(removeNewline(log_str));
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
}
