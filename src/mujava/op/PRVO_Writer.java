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
	private AllocationExpression original_new;
	
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
		this.original_new = null;
		
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
		} else if (v1 instanceof AllocationExpression) {
			this.original_new = (AllocationExpression) v1;
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
			this.original_fa = null;
			if (this.mutant_fa != null) {
				super.visit(this.mutant_fa);
			} else if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
			} else if (this.mutant_mc != null) {
				super.visit(this.mutant_mc);
			} else if (this.mutant_sa != null) {
				super.visit(this.mutant_sa);
			} else if (this.mutant_var != null) {
				super.visit(this.mutant_var);
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(MethodCall o) throws ParseTreeException {
		if(isSameObject(o, this.original_mc) ){
			this.original_mc = null;
			if (this.mutant_fa != null) {
				super.visit(this.mutant_fa);
			} else if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
			} else if (this.mutant_mc != null) {
				super.visit(this.mutant_mc);
			} else if (this.mutant_sa != null) {
				super.visit(this.mutant_sa);
			} else if (this.mutant_var != null) {
				super.visit(this.mutant_var);
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(Variable o) throws ParseTreeException {
		if(isSameObject(o, this.original_var) ){
			this.original_var = null;
			if (this.mutant_fa != null) {
				super.visit(this.mutant_fa);
			} else if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
			} else if (this.mutant_mc != null) {
				super.visit(this.mutant_mc);
			} else if (this.mutant_sa != null) {
				super.visit(this.mutant_sa);
			} else if (this.mutant_var != null) {
				super.visit(this.mutant_var);
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(Literal o) throws ParseTreeException {
		if(isSameObject(o, this.original_lit) ){
			this.original_lit = null;
			if (this.mutant_fa != null) {
				super.visit(this.mutant_fa);
			} else if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
			} else if (this.mutant_mc != null) {
				super.visit(this.mutant_mc);
			} else if (this.mutant_sa != null) {
				super.visit(this.mutant_sa);
			} else if (this.mutant_var != null) {
				super.visit(this.mutant_var);
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(SelfAccess o) throws ParseTreeException {
		if(isSameObject(o, this.original_sa) ){
			this.original_sa = null;
			if (this.mutant_fa != null) {
				super.visit(this.mutant_fa);
			} else if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
			} else if (this.mutant_mc != null) {
				super.visit(this.mutant_mc);
			} else if (this.mutant_sa != null) {
				super.visit(this.mutant_sa);
			} else if (this.mutant_var != null) {
				super.visit(this.mutant_var);
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(AllocationExpression o) throws ParseTreeException {
		if (isSameObject(o, this.original_new)) {
			this.original_new = null;
			if (this.mutant_fa != null) {
				super.visit(this.mutant_fa);
			} else if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
			} else if (this.mutant_mc != null) {
				super.visit(this.mutant_mc);
			} else if (this.mutant_sa != null) {
				super.visit(this.mutant_sa);
			} else if (this.mutant_var != null) {
				super.visit(this.mutant_var);
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}

}
