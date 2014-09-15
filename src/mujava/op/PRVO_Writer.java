package mujava.op;

import java.io.*;
import openjava.ptree.*;
import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;

public class PRVO_Writer extends MutantCodeWriter {
	private FieldAccess original_fa;
	private MethodCall original_mc;
	private Variable original_var;
	private Literal original_lit;
	private SelfAccess original_sa;
	
	private FieldAccess mutant_fa;
	private MethodCall mutant_mc;
	private Variable mutant_var;
	private Literal mutant_lit;
	private SelfAccess mutant_sa;
	
	public PRVO_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		this.mutant_fa = null;
		this.mutant_lit = null;
		this.mutant_mc = null;
		this.mutant_sa = null;
		this.mutant_var = null;
		
		this.original_fa = null;
		this.original_lit = null;
		this.original_mc = null;
		this.original_sa = null;
		this.original_var = null;
		
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object original, Object mutant) {
		setOriginal(original);
		setMutant(mutant);
	}
	
	private void setOriginal(Object v1) {
		if (v1 instanceof FieldAccess) {
			this.original_fa = (FieldAccess) v1;
		} else if (v1 instanceof MethodCall) {
			this.original_mc = (MethodCall) v1;
		} else if (v1 instanceof Variable) {
			this.original_var = (Variable) v1;
		} else if (v1 instanceof Literal) {
			this.original_lit = (Literal) v1;
		} else if (v1 instanceof SelfAccess) {
			this.original_sa = (SelfAccess) v1;
		}
	}
	
	private void setMutant(Object v2) {
		if (v2 instanceof FieldAccess) {
			this.mutant_fa = (FieldAccess) v2;
		} else if (v2 instanceof MethodCall) {
			this.mutant_mc = (MethodCall) v2;
		} else if (v2 instanceof Variable) {
			this.mutant_var = (Variable) v2;
		} else if (v2 instanceof Literal) {
			this.mutant_lit = (Literal) v2;
		} else if (v2 instanceof SelfAccess) {
			this.mutant_sa = (SelfAccess) v2;
		}
	}

	public void visit(FieldAccess o) throws ParseTreeException {
		if(isSameObject(o, this.original_fa) ){
			String mut = "";
			String originalString = this.original_fa.toFlattenString();
			this.original_fa = null;
			if (this.mutant_fa != null) {
				super.visit(this.mutant_fa);
				mut = this.mutant_fa.toString();
			} else if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
				mut = this.mutant_lit.toString();
			} else if (this.mutant_mc != null) {
				super.visit(this.mutant_mc);
				mut = this.mutant_mc.toString();
			} else if (this.mutant_sa != null) {
				super.visit(this.mutant_sa);
				mut = this.mutant_sa.toString();
			} else if (this.mutant_var != null) {
				super.visit(this.mutant_var);
				mut = this.mutant_var.toString();
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			writeLog(removeNewline(originalString + " => " + mut));
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(MethodCall o) throws ParseTreeException {
		if(isSameObject(o, this.original_mc) ){
			String mut = "";
			String originalString = this.original_mc.toFlattenString();
			this.original_mc = null;
			if (this.mutant_fa != null) {
				super.visit(this.mutant_fa);
				mut = this.mutant_fa.toString();
			} else if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
				mut = this.mutant_lit.toString();
			} else if (this.mutant_mc != null) {
				super.visit(this.mutant_mc);
				mut = this.mutant_mc.toString();
			} else if (this.mutant_sa != null) {
				super.visit(this.mutant_sa);
				mut = this.mutant_sa.toString();
			} else if (this.mutant_var != null) {
				super.visit(this.mutant_var);
				mut = this.mutant_var.toString();
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			writeLog(removeNewline(originalString + " => " + mut));
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(Variable o) throws ParseTreeException {
		if(isSameObject(o, this.original_var) ){
			String mut = "";
			String originalString = this.original_var.toFlattenString();
			this.original_var = null;
			if (this.mutant_fa != null) {
				super.visit(this.mutant_fa);
				mut = this.mutant_fa.toString();
			} else if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
				mut = this.mutant_lit.toString();
			} else if (this.mutant_mc != null) {
				super.visit(this.mutant_mc);
				mut = this.mutant_mc.toString();
			} else if (this.mutant_sa != null) {
				super.visit(this.mutant_sa);
				mut = this.mutant_sa.toString();
			} else if (this.mutant_var != null) {
				super.visit(this.mutant_var);
				mut = this.mutant_var.toString();
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			writeLog(removeNewline(originalString + " => " + mut));
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(Literal o) throws ParseTreeException {
		if(isSameObject(o, this.original_lit) ){
			String mut = "";
			String originalString = this.original_lit.toFlattenString();
			this.original_lit = null;
			if (this.mutant_fa != null) {
				super.visit(this.mutant_fa);
				mut = this.mutant_fa.toString();
			} else if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
				mut = this.mutant_lit.toString();
			} else if (this.mutant_mc != null) {
				super.visit(this.mutant_mc);
				mut = this.mutant_mc.toString();
			} else if (this.mutant_sa != null) {
				super.visit(this.mutant_sa);
				mut = this.mutant_sa.toString();
			} else if (this.mutant_var != null) {
				super.visit(this.mutant_var);
				mut = this.mutant_var.toString();
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			writeLog(removeNewline(originalString + " => " + mut));
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(SelfAccess o) throws ParseTreeException {
		if(isSameObject(o, this.original_sa) ){
			String mut = "";
			String originalString = this.original_sa.toFlattenString();
			this.original_sa = null;
			if (this.mutant_fa != null) {
				super.visit(this.mutant_fa);
				mut = this.mutant_fa.toString();
			} else if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
				mut = this.mutant_lit.toString();
			} else if (this.mutant_mc != null) {
				super.visit(this.mutant_mc);
				mut = this.mutant_mc.toString();
			} else if (this.mutant_sa != null) {
				super.visit(this.mutant_sa);
				mut = this.mutant_sa.toString();
			} else if (this.mutant_var != null) {
				super.visit(this.mutant_var);
				mut = this.mutant_var.toString();
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			writeLog(removeNewline(originalString + " => " + mut));
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}

}
