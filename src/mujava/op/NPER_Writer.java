package mujava.op;

import java.io.PrintWriter;

import openjava.ptree.AssignmentExpression;
import openjava.ptree.ExpressionStatement;
import openjava.ptree.ForStatement;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ReturnStatement;
import openjava.ptree.Statement;
import openjava.ptree.StatementList;
import openjava.ptree.TryStatement;
import openjava.ptree.WhileStatement;
import mujava.api.Mutation;
import mujava.op.util.MutantCodeWriter;

public class NPER_Writer extends MutantCodeWriter {
	
	private ForStatement original_for;
	private WhileStatement original_while;
	private AssignmentExpression original_expr;
	private ReturnStatement original_ret;
	
	private Statement mutant_statement;
	private StatementList mutant_statements;
	private TryStatement mutant_try;

	
	public NPER_Writer(String file_name, PrintWriter out, Mutation mi) {
		super(file_name, out, mi);
		setOriginal(this.mi.getOriginal());
		setMutant(this.mi.getMutant());
	}
	
	private void setOriginal(Object original) {
		if (original instanceof ForStatement) {
			this.original_for = (ForStatement) original;
		} else if (original instanceof WhileStatement) {
			this.original_while = (WhileStatement) original;
		} else if (original instanceof AssignmentExpression) {
			this.original_expr = (AssignmentExpression) original;
		} else if (original instanceof ReturnStatement) {
			this.original_ret = (ReturnStatement) original;
		}
	}
	
	private void setMutant(Object mutant) {
		if (mutant instanceof TryStatement) {
			this.mutant_try = (TryStatement) mutant;
		} else if (mutant instanceof StatementList) {
			this.mutant_statements = (StatementList) mutant;
		} else if (mutant instanceof Statement) {
			this.mutant_statement = (Statement) mutant;
		}
	}
	
	public void visit(ForStatement o) throws ParseTreeException {
		if(this.original_for != null && isSameObject(o, this.original_for) ){
			if (this.mutant_statement != null) {
				Statement m = this.mutant_statement;
				this.original_for = null;
				this.mutant_statement = null;
				super.visit(m);
			} else if (this.mutant_statements != null) {
				StatementList m = this.mutant_statements;
				this.original_for = null;
				this.mutant_statements = null;
				super.visit(m);
			}
			else throw new ParseTreeException("NPER_Writer is being called with a mutant that's not a statement or a statement list");
			// -------------------------------------------------------------
		    mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(WhileStatement o) throws ParseTreeException {
		if(this.original_while != null && isSameObject(o, this.original_while) ){
			if (this.mutant_statement != null) {
				Statement m = this.mutant_statement;
				this.original_while = null;
				this.mutant_statement = null;
				super.visit(m);
			} else if (this.mutant_statements != null) {
				StatementList m = this.mutant_statements;
				this.original_while = null;
				this.mutant_statement = null;
				super.visit(m);
			}
			else throw new ParseTreeException("NPER_Writer is being called with a mutant that's not a statement or a statement list");
			// -------------------------------------------------------------
		    mutated_line = line_num;
			// -------------------------------------------------------------
		} else {
			super.visit(o);
		}
	}
	
	public void visit(ExpressionStatement o) throws ParseTreeException {
		if (o.getExpression() instanceof AssignmentExpression) {
			if (this.original_expr != null && isSameObject(o.getExpression(), this.original_expr)) {
				if (this.mutant_try != null) {
					TryStatement m = this.mutant_try;
					this.original_expr = null;
					this.mutant_try = null;
					super.visit(m);
				}
				else throw new ParseTreeException("NPER_Writer is being called with an AssignmentExpression and a mutant that's not a try statement");
				// -------------------------------------------------------------
			    mutated_line = line_num;
				// -------------------------------------------------------------
			} else {
				super.visit(o);
			}
		} else {
			super.visit(o);
		}
	}
	
	public void visit(ReturnStatement p) throws ParseTreeException {
		if (this.original_ret != null && isSameObject(p, this.original_ret)) {
			TryStatement m = this.mutant_try;
			this.original_ret = null;
			this.mutant_try = null;
			super.visit(m);
			mutated_line = line_num;
		} else {
			super.visit(p);
		}
	}
	
	
}
