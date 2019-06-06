package mujava.op;

import java.io.PrintWriter;
import openjava.ptree.*;
import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;

public class CRCR_Writer extends MutantCodeWriter {
	
	private Literal original_lit;
	private UnaryExpression original_unary;
	
	private Literal mutant_lit;
	private UnaryExpression mutant_unary;
	private BinaryExpression mutant_binary;
	
	public CRCR_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		
		this.original_lit = null;
		this.original_unary = null;
		this.mutant_lit = null;
		this.mutant_unary = null;
		this.mutant_binary = null;
		
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object original, Object mutant) {
		setOriginal(original);
		setMutant(mutant);
	}
	
	private void setOriginal(Object v1) {
		if (v1 instanceof Literal) {
			this.original_lit = (Literal) v1;
		} else if (v1 instanceof UnaryExpression) {
			this.original_unary = (UnaryExpression) v1;
		} else {
			throw new IllegalArgumentException("Can't set mutant node with type " + v1.getClass().getName());
		}
	}
	
	private void setMutant(Object v2) {
		if (v2 instanceof Literal) {
			this.mutant_lit = (Literal) v2;
		} else if (v2 instanceof UnaryExpression) {
			this.mutant_unary = (UnaryExpression) v2;
		} else if (v2 instanceof BinaryExpression) {
			this.mutant_binary = (BinaryExpression) v2;
		} else {
			throw new IllegalArgumentException("Can't set mutant node with type " + v2.getClass().getName());
		}
	}
	
	public void visit(Literal o) throws ParseTreeException {
		if(isSameObject(o, this.original_lit) ){
			this.original_lit = null;
			if (this.mutant_lit != null) {
				super.visit(this.mutant_lit);
			} else if (this.mutant_unary != null) {
				super.visit(this.mutant_unary);
			} else if (this.mutant_binary != null) {
				super.visit(this.mutant_binary);
			} else {
				throw new IllegalStateException(o.toFlattenString() + " to be replaced but no mutant set");
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(UnaryExpression o) throws ParseTreeException {
		if (isSameObject(o, this.original_unary)) {
			this.original_unary = null;
			if (this.mutant_lit != null) {
				super.visit(mutant_lit);
			} else if (this.mutant_unary != null) {
				super.visit(mutant_unary);
			} else if (this.mutant_binary != null) {
				super.visit(mutant_binary);
			} else {
				throw new IllegalStateException(o.toFlattenString() + " to be replaced but no mutant set");
			}
			// -------------------------------------------------------------
		    mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	

}
