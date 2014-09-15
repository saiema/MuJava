////////////////////////////////////////////////////////////////////////////
// Module : LOD_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;
import openjava.ptree.*;
import java.io.*;

/**
 * <p>Output and log LOD mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
  */

public class LOD_Writer extends MutantCodeWriter {
	private UnaryExpression original;
	private Expression mutant;

	public LOD_Writer( String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}

	private void setMutant(Object v1, Object v2) {
		this.original = (UnaryExpression)v1;
		this.mutant = (Expression)v2;
	}

	public void visit( UnaryExpression p ) throws ParseTreeException {
		if (isSameObject(p, this.original)) {
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			String log_str = p.toString() + " => " + this.mutant.toString();
			writeLog(removeNewline(log_str));
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
}
