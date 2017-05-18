////////////////////////////////////////////////////////////////////////////
// Module : COR_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op;

import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;
import openjava.ptree.*;

import java.io.*;

/**
 * <p>
 * Output and log BEE mutants to files
 * </p>
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1u
 */

public class BEE_Writer extends MutantCodeWriter {
	private MethodCall original_ma = null;
	private FieldAccess original_fa = null;
	private Variable original_v = null;
	private UnaryExpression original_u = null;
	private BinaryExpression original_b = null;
	private BinaryExpression mutant = null;

	public BEE_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}

	private void setMutant(Object exp1, Object exp2) {
		if (exp1 instanceof Variable) {
			this.original_v = (Variable) exp1;
		} else if (exp1 instanceof FieldAccess) {
			this.original_fa = (FieldAccess) exp1;
		} else if (exp1 instanceof MethodCall) {
			this.original_ma = (MethodCall) exp1;
		} else if (exp1 instanceof UnaryExpression) {
			this.original_u = (UnaryExpression) exp1;
		} else if (exp1 instanceof BinaryExpression) {
			this.original_b = (BinaryExpression) exp1;
		}
		this.mutant = (BinaryExpression) exp2;
	}
	
	public void visit(MethodCall p) throws ParseTreeException {
		if (this.original_ma != null && isSameObject(p, this.original_ma)) {
			this.original_ma = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	public void visit(FieldAccess p) throws ParseTreeException {
		if (this.original_fa != null && isSameObject(p, this.original_fa)) {
			this.original_fa = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	public void visit(Variable p) throws ParseTreeException {
		if (this.original_v != null && isSameObject(p, this.original_v)) {
			this.original_v = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	public void visit(UnaryExpression p) throws ParseTreeException {
		if (this.original_u != null && isSameObject(p, this.original_u)) {
			this.original_u = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}

	public void visit(BinaryExpression p) throws ParseTreeException {
		if (this.original_b != null && isSameObject(p, this.original_b)) {
			this.original_b = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
}
