////////////////////////////////////////////////////////////////////////////
// Module : COI_Writer.java
// Author : Ma, Yu-Seung
// COPYRIGHT 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED.
////////////////////////////////////////////////////////////////////////////

package mujava.op.basic;

import java.io.PrintWriter;

import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;
import openjava.ptree.BinaryExpression;
import openjava.ptree.FieldAccess;
import openjava.ptree.Literal;
import openjava.ptree.ParseTreeException;
import openjava.ptree.UnaryExpression;
import openjava.ptree.Variable;

/**
 * <p>Output and log COI mutants to files </p>
 * <p>Copyright: Copyright (c) 2005 by Yu-Seung Ma, ALL RIGHTS RESERVED </p>
 * @author Yu-Seung Ma
 * @version 1.0
 */

public class COI_Writer extends MutantCodeWriter {
	private Literal original_lit = null;
	private Variable original_var = null;
	private FieldAccess original_field = null;
	private UnaryExpression original_uexp = null;
	private BinaryExpression original_bexp = null;
	private UnaryExpression mutant = null;
	private boolean originalReplaced = false;

	public COI_Writer( String file_name, PrintWriter out, Mutation mi)  {
		super(file_name, out, mi);
		this.original_bexp = null;
		this.original_field = null;
		this.original_lit = null;
		this.original_uexp = null;
		this.original_var = null;
		this.mutant = null;
		this.originalReplaced = false;
		
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}

	private void setMutant(Object v1, Object v2) {
		this.mutant = (UnaryExpression) v2;
		if (v1 instanceof UnaryExpression) {
			setOriginal((UnaryExpression)v1);
		} else if (v1 instanceof Variable) {
			setOriginal((Variable)v1);
		} else if (v1 instanceof Literal) {
			setOriginal((Literal)v1);
		} else if (v1 instanceof FieldAccess) {
			setOriginal((FieldAccess)v1);
		} else if (v1 instanceof BinaryExpression) {
			setOriginal((BinaryExpression)v1);
		}
	}

	private void setOriginal(Literal v1) {
		this.original_lit = v1;
	}
	
	private void setOriginal(Variable v1) {
		this.original_var = v1;
	}
	
	private void setOriginal(FieldAccess v1) {
		this.original_field = v1;
	}
	
	private void setOriginal(UnaryExpression v1) {
		this.original_uexp = v1;
	}
	
	private void setOriginal(BinaryExpression v1) {
		this.original_bexp = v1;
	}
	
	public void visit( BinaryExpression p ) throws ParseTreeException {
		if (this.original_bexp != null && isSameObject(p, this.original_bexp) && !this.originalReplaced) {
			this.originalReplaced = true;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	
	public void visit( UnaryExpression p ) throws ParseTreeException {
		if (this.original_uexp != null && isSameObject(p, this.original_uexp) && !this.originalReplaced) {
			this.originalReplaced = true;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	public void visit( Variable p ) throws ParseTreeException {
		if (this.original_var != null && isSameObject(p, this.original_var) && !this.originalReplaced) {
			this.originalReplaced = true;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	public void visit( FieldAccess p ) throws ParseTreeException {
		if (this.original_field != null && isSameObject(p, this.original_field) && !this.originalReplaced) {
			this.originalReplaced = true;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	public void visit( Literal p ) throws ParseTreeException {
		if (this.original_lit != null && isSameObject(p, this.original_lit) && !this.originalReplaced) {
			this.originalReplaced = true;
			super.visit(this.mutant);
			// -----------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}

}
