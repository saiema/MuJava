package mujava.op;

import java.io.PrintWriter;
import openjava.ptree.*;
import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;

public class AOD_Writer extends MutantCodeWriter {
	
	private BinaryExpression original;
	
	private Expression mutant;
	
	public AOD_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		
		this.original = null;
		this.mutant = null;
		
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object original, Object mutant) {
		setOriginal(original);
		setMutant(mutant);
	}
	
	private void setOriginal(Object v1) {
		if (v1 instanceof BinaryExpression) {
			this.original = (BinaryExpression) v1;
		} else {
			throw new IllegalArgumentException("Can't set mutant node with type " + v1.getClass().getName());
		}
	}
	
	private void setMutant(Object v2) {
		if (v2 instanceof Expression) {
			this.mutant = (Expression) v2;
		} else {
			throw new IllegalArgumentException("Can't set mutant node with type " + v2.getClass().getName());
		}
	}
	
	public void visit(BinaryExpression o) throws ParseTreeException {
		if(isSameObject(o, this.original) ){
			this.original = null;
			if (this.mutant != null) {
				super.visit(this.mutant);
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
