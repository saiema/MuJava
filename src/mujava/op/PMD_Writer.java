package mujava.op;

import java.io.*;
import openjava.ptree.*;
import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;

public class PMD_Writer extends MutantCodeWriter {
	private FieldDeclaration original_field = null;
	private FieldDeclaration mutant_field = null;

	private VariableDeclaration original_var = null;
	private VariableDeclaration mutant_var = null;
	
	private MethodDeclaration original_method = null;
	private MethodDeclaration mutant_method = null;
	
	private ConstructorDeclaration original_constructor = null;
	private ConstructorDeclaration mutant_constructor = null;

	public PMD_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setMutant(this.mi.getOriginal(), this.mi.getMutant());
	}
	
	private void setMutant(Object v1, Object v2) {
		setOriginal(v1);
		setMutant(v2);
	}
	
	private void setMutant(Object v2) {
		if (v2 instanceof FieldDeclaration) {
			this.mutant_field = (FieldDeclaration)v2;
		} else if (v2 instanceof VariableDeclaration) {
			this.mutant_var = (VariableDeclaration)v2;
		} else if (v2 instanceof MethodDeclaration) {
			this.mutant_method = (MethodDeclaration)v2;
		} else if (v2 instanceof ConstructorDeclaration) {
			this.mutant_constructor = (ConstructorDeclaration)v2;
		}
	}
	
	private void setOriginal(Object v1) {
		if (v1 instanceof FieldDeclaration) {
			this.original_field = (FieldDeclaration)v1;
		} else if (v1 instanceof VariableDeclaration) {
			this.original_var = (VariableDeclaration)v1;
		} else if (v1 instanceof MethodDeclaration) {
			this.original_method = (MethodDeclaration)v1;
		} else if (v1 instanceof ConstructorDeclaration) {
			this.original_constructor = (ConstructorDeclaration)v1;
		}
	}

	/**
	 * Log mutated line
	 */
	public void visit(VariableDeclaration p) throws ParseTreeException {
		if (isSameObject(p, original_var)) {
			this.mutant_var.setMutGenLimit(this.original_var.getMutGenLimit());
			super.visit(mutant_var);
			// -------------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	public void visit(FieldDeclaration p) throws ParseTreeException {
		if (isSameObject(p, original_field)) {
			this.mutant_field.setMutGenLimit(this.original_field.getMutGenLimit());
			super.visit(mutant_field);
			// -------------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	public void visit(MethodDeclaration p) throws ParseTreeException {
		if (isSameObject(p, original_method)) {
			this.mutant_method.setMutGenLimit(this.original_method.getMutGenLimit());
			super.visit(mutant_method);
			// -------------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}
	
	public void visit(ConstructorDeclaration p) throws ParseTreeException {
		if (isSameObject(p, this.original_constructor)) {
			this.mutant_constructor.setMutGenLimit(this.original_constructor.getMutGenLimit());
			super.visit(this.mutant_constructor);
			// -------------------------------------------------------------
			mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(p);
		}
	}


}
