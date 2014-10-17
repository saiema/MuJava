////////////////////////////////////////////////////////////////////////////
// Module : ROR_Writer.java
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
 * Output and log ROR mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class ROR_Writer extends MutantCodeWriter {
	private BinaryExpression original;
	private BinaryExpression mutant;
	private Literal mutantL = null;

	public ROR_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}

	private void setMutant(Object v1, Object v2) {
		this.original = (BinaryExpression) v1;
		if (v2 instanceof BinaryExpression) {
			this.mutant = (BinaryExpression) v2;
		} else if (v2 instanceof Literal) {
			this.mutantL = (Literal) v2;
		}
		
	}

	public void visit(BinaryExpression p) throws ParseTreeException {
		if (isSameObject(p, this.original)) {
			if (this.mutantL != null) {
				super.visit(this.mutantL);
			} else {
				super.visit(this.mutant);
			}
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}

}
