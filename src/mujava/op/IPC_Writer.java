////////////////////////////////////////////////////////////////////////////
// Module : IPC_Writer.java
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
 * Output and log IPC mutants to files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED
 * </p>
 * 
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class IPC_Writer extends MutantCodeWriter {
	private ConstructorInvocation mutant = null;

	public IPC_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getMutant());
	}
	
	private void setMutant(Object mutant) {
		this.mutant = (ConstructorInvocation) mutant;
	}

	public void visit(ConstructorInvocation p) throws ParseTreeException {
		if (isSameObject(p, mutant)) {
			 // -----------------------------------------------------------
	        mutated_line = line_num;
	        String log_str = "removed  => " + mutant.toFlattenString();
	        writeLog(removeNewline(log_str));
	        // -----------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
}
