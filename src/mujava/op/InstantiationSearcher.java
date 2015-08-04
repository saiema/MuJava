package mujava.op;

import openjava.mop.Environment;
import openjava.ptree.AllocationExpression;
import openjava.ptree.ArrayAllocationExpression;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.MethodDeclaration;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.Statement;
import openjava.ptree.StatementList;
import mujava.op.util.Mutator;

public class InstantiationSearcher extends Mutator {
	
	private Expression toSearch;
	private boolean found;

	public InstantiationSearcher(Environment env , CompilationUnit comp_unit, Expression toSearch) {
		super(env, comp_unit);
		this.toSearch = toSearch;
		this.found = false;
	}
	
	public boolean allocationExpressionFound() {
		return this.found;
	}
	
	
	public void visit(AssignmentExpression p) throws ParseTreeException {
		if (this.found) return;
		if (isAllocationForExpression(p, this.toSearch)) {
			this.found = isExpressionReachableFrom(p, this.toSearch);
		}
	}
	
	
	private boolean isAllocationForExpression(AssignmentExpression assignment, Expression e) {
		if (compareAsStrings((ParseTreeObject)assignment.getLeft(), (ParseTreeObject)e, true)) {
			Expression right = assignment.getRight();
			if (right instanceof AllocationExpression || right instanceof ArrayAllocationExpression) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isExpressionReachableFrom(Expression expr, Expression reachableFrom) {
		Statement exprStatement = (Statement) getStatement((ParseTreeObject) expr);
		Statement reachableFromStatement = (Statement) getStatement((ParseTreeObject) reachableFrom);
		if (isSameObject(exprStatement, reachableFromStatement)) {
			return exprDepthInStatement((ParseTreeObject) reachableFrom) > exprDepthInStatement((ParseTreeObject) expr);
		}
		ParseTreeObject current = (ParseTreeObject) reachableFromStatement;
		while(current != null && !(current instanceof MethodDeclaration)) {
			if (isSameObject(current, exprStatement)) {
				return true;
			}
			if (current instanceof StatementList) {
				for (int s = 0; s < ((StatementList)current).size(); s++) {
					Statement st = ((StatementList)current).get(s);
					if (isSameObject(st, reachableFromStatement)) {
						break;
					}
					if (isSameObject(st, exprStatement)) {
						return true;
					}
				}
			}
			current = current.getParent();
		}
		return false;
	}
	
	private int exprDepthInStatement(ParseTreeObject expr) {
		int depth = 0;
		ParseTreeObject current = expr;
		while (current != null && !(current instanceof Statement)) {
			current = current.getParent();
			depth++;
		}
		return depth;
	}
	
	
}
