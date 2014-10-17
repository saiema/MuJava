////////////////////////////////////////////////////////////////////////////
// Module : AORS_Writer.java
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
 * Output and log AORS mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class AORS_Writer extends MutantCodeWriter {
	private UnaryExpression unary_original;
	private UnaryExpression unary_mutant;

	public AORS_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object v1, Object v2) {
		this.unary_original = (UnaryExpression) v1;
		this.unary_mutant = (UnaryExpression) v2;
	}

	public void visit(UnaryExpression p) throws ParseTreeException {
		if (isSameObject(p, this.unary_original)) {
			super.visit(this.unary_mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
}
