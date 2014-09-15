////////////////////////////////////////////////////////////////////////////
// Module : COR_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;
import openjava.ptree.*;

import java.io.*;

/**
 * <p>
 * Output and log COR mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class COR_Writer extends MutantCodeWriter {
	private BinaryExpression original;
	private BinaryExpression mutant;

	public COR_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}

	private void setMutant(Object exp1, Object exp2) {
		this.original = (BinaryExpression) exp1;
		this.mutant = (BinaryExpression) exp2;
	}

	public void visit(BinaryExpression p) throws ParseTreeException {
		if (isSameObject(p, this.original)) {
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			String log_str = p.toFlattenString() + "  =>  " + this.mutant.toFlattenString();
			writeLog(removeNewline(log_str));
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
}
