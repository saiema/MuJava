package mujava.op;

import java.io.PrintWriter;

import openjava.ptree.Expression;
import openjava.ptree.FieldAccess;
import openjava.ptree.MethodCall;
import openjava.ptree.ParseTreeException;
import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;

public class ISD_Writer extends MutantCodeWriter {
	private FieldAccess original_fa;
	private MethodCall original_mc;
	private Expression mutant;
	
	public ISD_Writer(String mutant_dir, PrintWriter out, Mutation mi) {
		super(mutant_dir, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object v1, Object v2) {
		this.mutant = (Expression) v2;
		if (v1 instanceof FieldAccess) {
			setOriginal((FieldAccess)v1);
		} else if (v1 instanceof MethodCall) {
			setOriginal((MethodCall)v1);
		}
	}
	
	private void setOriginal(FieldAccess original) {
		this.original_fa = original;
	}
	
	private void setOriginal(MethodCall original) {
		this.original_mc = original;
	}
	
	public void visit(FieldAccess fa) throws ParseTreeException {
		if (this.original_fa != null && isSameObject(fa, this.original_fa)) {
			this.original_fa = null;
			super.visit(this.mutant);
			// -----------------------------------------------------------
	         mutated_line = line_num;
	         String log_str = fa.toFlattenString()+ " => " + mutant.toFlattenString();
	         writeLog(removeNewline(log_str));
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
	         String log_str = mc.toFlattenString()+ " => " + mutant.toFlattenString();
	         writeLog(removeNewline(log_str));
	         // -------------------------------------------------------------
		} else {
			super.visit(mc);
		}
	}

}
