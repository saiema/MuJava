////////////////////////////////////////////////////////////////////////////
// Module : JID_Writer.java
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
 * Output and log JID mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class JID_Writer extends MutantCodeWriter {
	private FieldDeclaration mutant = null;
	private FieldDeclaration original = null;

	public JID_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
		
	private void setMutant(Object original, Object mutant) {
		this.original = (FieldDeclaration) original;
		this.mutant = (FieldDeclaration) mutant;
	}

	public void visit(FieldDeclaration p) throws ParseTreeException {
		if (isSameObject(p, original)) {
			String originalString = this.original.toFlattenString();
			this.original = null;
			this.mutant.setMutGenLimit(p.getMutGenLimit());
	        super.visit(mutant);
	        mutated_line = line_num;
	        String log_str = originalString + " => " + mutant.toFlattenString();
	        writeLog(removeNewline(log_str));
		} else {
			super.visit(p);
		}
	}
}
