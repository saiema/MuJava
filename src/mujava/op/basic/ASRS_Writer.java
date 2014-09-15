////////////////////////////////////////////////////////////////////////////
// Module : ASRS_Writer.java
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
 * Output and log ASRS mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class ASRS_Writer extends MutantCodeWriter {
	private AssignmentExpression assign_original;
	private AssignmentExpression assign_mutant;

	public ASRS_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}

	private void setMutant(Object v1, Object v2) {
		this.assign_original = (AssignmentExpression) v1;
		this.assign_mutant = (AssignmentExpression) v2;
	}

	public void visit(AssignmentExpression p) throws ParseTreeException {
		if (isSameObject(p, this.assign_original)) {
			this.assign_original = null;
			super.visit(this.assign_mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			String log_str = p.toString() + " => " + this.assign_mutant.toString();
			writeLog(removeNewline(log_str));
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
}
