////////////////////////////////////////////////////////////////////////////
// Module : EOC_Writer.java
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
 * Output and log EOC mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class EOC_Writer extends MutantCodeWriter {
	private BinaryExpression original_bexp;
	private MethodCall mutant;

	public EOC_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object v1, Object v2) {
		this.original_bexp = (BinaryExpression) v1;
		this.mutant = (MethodCall) v2;
	}


	public void visit(BinaryExpression p) throws ParseTreeException {
		if (isSameObject(p, this.original_bexp)) {
			String originalString = this.original_bexp.toFlattenString();
			this.original_bexp = null;
			super.visit(this.mutant);
			mutated_line = line_num;
			writeLog(originalString + " => " + this.mutant.toString());
		} else {
			super.visit(p);
		}
	}
}
