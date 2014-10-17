////////////////////////////////////////////////////////////////////////////
// Module : EOA_Writer.java
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
 * Output and log EOA mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class EOA_Writer extends MutantCodeWriter {
	private AssignmentExpression original_ae;
	private VariableDeclarator original_vd;
	private AssignmentExpression mutant_ae;
	private VariableDeclarator mutant_vd;
	
	public EOA_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		this.original_ae = null;
		this.original_vd = null;
		this.mutant_ae = null;
		this.mutant_vd = null;
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object v1, Object v2) {
		
		if (v1 instanceof VariableDeclarator) {
			this.original_vd = (VariableDeclarator)v1;
		} else if (v1 instanceof AssignmentExpression) {
			this.original_ae = (AssignmentExpression) v1;
		}
		
		if (v2 instanceof VariableDeclarator) {
			this.mutant_vd = (VariableDeclarator)v2;
		} else if (v2 instanceof AssignmentExpression) {
			this.mutant_ae = (AssignmentExpression) v2;
		}
	}

	public void visit(AssignmentExpression p) throws ParseTreeException {
		if (this.original_ae != null && isSameObject(p, this.original_ae)) {
			this.original_ae = null;
			super.visit(this.mutant_ae);
			// -------------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	public void visit(VariableDeclarator p) throws ParseTreeException {
		if (this.original_vd != null && isSameObject(p, this.original_vd)) {
			this.original_vd = null;
			super.visit(this.mutant_vd);
			// -------------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}

}
