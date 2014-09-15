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

public class PCI_Writer extends MutantCodeWriter {
	private Variable original_var;
	private FieldAccess original_fa;
	private CastExpression mutant;

	public PCI_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		this.original_var = null;
		this.original_fa = null;
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}

	private void setMutant(Object original, Object mutant) {
		this.mutant = (CastExpression) mutant;
		if (original instanceof Variable) {
			setOriginal((Variable)original);
		} else if (original instanceof FieldAccess) {
			setOriginal((FieldAccess)original);
		}
	}
	
	private void setOriginal(Variable v1) {
		this.original_var = v1;
	}
	
	private void setOriginal(FieldAccess v1) {
		this.original_fa = v1;
	}

	public void visit(Variable p) throws ParseTreeException {
		if (this.original_var != null && isSameObject(p, this.original_var)) {
			String originalString = this.original_var.toFlattenString();
			this.original_var = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			String log_str = originalString + " => " + this.mutant.toFlattenString();
			writeLog(removeNewline(log_str));
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	public void visit(FieldAccess p) throws ParseTreeException {
		if (this.original_fa != null && isSameObject(p, this.original_fa)) {
			String originalString = this.original_fa.toFlattenString();
			this.original_fa = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			String log_str = originalString + " => " + this.mutant.toFlattenString();
			writeLog(removeNewline(log_str));
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
}
