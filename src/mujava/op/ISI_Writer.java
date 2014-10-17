package mujava.op;

import java.io.PrintWriter;

import openjava.ptree.Expression;
import openjava.ptree.FieldAccess;
import openjava.ptree.MethodCall;
import openjava.ptree.ParseTreeException;
import openjava.ptree.Variable;

import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;

public class ISI_Writer extends MutantCodeWriter {
	private Variable original_var;
	private FieldAccess original_fa;
	private MethodCall original_mc;
	private Expression mutant;
	
	public ISI_Writer(String mutant_dir, PrintWriter out, Mutation mi) {
		super(mutant_dir, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object v1, Object v2) {
		this.mutant = (Expression) v2;
		if (v1 instanceof Variable) {
			setOriginal((Variable)v1);
		} else if (v1 instanceof FieldAccess) {
			setOriginal((FieldAccess)v1);
		} else if (v1 instanceof MethodCall) {
			setOriginal((MethodCall)v1);
		}
	}
	
	private void setOriginal(Variable original) {
		this.original_var = original;
	}
	
	private void setOriginal(FieldAccess original) {
		this.original_fa = original;
	}
	
	private void setOriginal(MethodCall original) {
		this.original_mc = original;
	}
	
	public void visit(Variable var) throws ParseTreeException {
		if (this.original_var != null && isSameObject(var, this.original_var)) {
			this.original_var = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
	         mutated_line = line_num;
	         // -------------------------------------------------------------
		} else {
			super.visit(var);
		}
	}
	
	public void visit(FieldAccess fa) throws ParseTreeException {
		if (this.original_fa != null && isSameObject(fa, this.original_fa)) {
			this.original_fa = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
	         mutated_line = line_num;
	         // -------------------------------------------------------------
		} else {
			super.visit(fa);
		}
	}
	
	public void visit(MethodCall mc) throws ParseTreeException {
		if (this.original_mc != null && isSameObject(mc, this.original_mc)) {
			this.original_mc = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
	         mutated_line = line_num;
	         // -------------------------------------------------------------
		} else {
			super.visit(mc);
		}
	}

}
