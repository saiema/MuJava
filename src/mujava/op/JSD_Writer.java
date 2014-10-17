////////////////////////////////////////////////////////////////////////////
// Module : JSD_Writer.java
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
 * Output and log JSD mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class JSD_Writer extends MutantCodeWriter {
	private FieldDeclaration mutant_field = null;
	private FieldDeclaration original_field = null;
	private MethodDeclaration mutant_method = null;
	private MethodDeclaration original_method = null;

	public JSD_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object v1, Object v2) {
		if (v1 instanceof FieldDeclaration) {
			setMutant((FieldDeclaration)v1, (FieldDeclaration)v2);
		} else if (v1 instanceof MethodDeclaration) {
			setMutant((MethodDeclaration)v1, (MethodDeclaration)v2);
		}
	}
	
	private void setMutant(FieldDeclaration original, FieldDeclaration mutant) {
		this.original_field = original;
		this.mutant_field = mutant;
	}
	
	private void setMutant(MethodDeclaration original, MethodDeclaration mutant) {
		this.original_method = original;
		this.mutant_method = mutant;
	}

	public void visit(FieldDeclaration p) throws ParseTreeException {
		if (isSameObject(p, this.original_field)) {
			this.mutant_field.setMutGenLimit(this.original_field.getMutGenLimit());
		    this.original_field = null;
		    super.visit(this.mutant_field);
		    mutated_line = line_num;
		} else {
			super.visit(p);
		}
	}

	public void visit(MethodDeclaration p) throws ParseTreeException {
		if (isSameObject(p, this.original_method)) {
			this.mutant_method.setMutGenLimit(this.original_method.getMutGenLimit());
		    this.original_method = null;
		    super.visit(this.mutant_method);
		    mutated_line = line_num;
		} else {
			super.visit(p);
		}
	}
	
}
