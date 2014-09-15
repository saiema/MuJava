package mujava.op.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import mujava.api.Api;
import mujava.api.Mutant;
import mujava.api.Mutation;
import openjava.ptree.AllocationExpression;
import openjava.ptree.ArrayAccess;
import openjava.ptree.ArrayAllocationExpression;
import openjava.ptree.ArrayInitializer;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.BinaryExpression;
import openjava.ptree.Block;
import openjava.ptree.BreakStatement;
import openjava.ptree.CaseGroup;
import openjava.ptree.CaseGroupList;
import openjava.ptree.CaseLabel;
import openjava.ptree.CaseLabelList;
import openjava.ptree.CastExpression;
import openjava.ptree.CatchBlock;
import openjava.ptree.CatchList;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.ClassDeclarationList;
import openjava.ptree.ClassLiteral;
import openjava.ptree.CompilationUnit;
import openjava.ptree.ConditionalExpression;
import openjava.ptree.ConstructorDeclaration;
import openjava.ptree.ConstructorInvocation;
import openjava.ptree.ContinueStatement;
import openjava.ptree.DoWhileStatement;
import openjava.ptree.EmptyStatement;
import openjava.ptree.EnumConstant;
import openjava.ptree.EnumConstantList;
import openjava.ptree.EnumDeclaration;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionList;
import openjava.ptree.ExpressionStatement;
import openjava.ptree.FieldAccess;
import openjava.ptree.FieldDeclaration;
import openjava.ptree.ForStatement;
import openjava.ptree.IfStatement;
import openjava.ptree.InstanceofExpression;
import openjava.ptree.LabeledStatement;
import openjava.ptree.Literal;
import openjava.ptree.MemberDeclarationList;
import openjava.ptree.MemberInitializer;
import openjava.ptree.MethodCall;
import openjava.ptree.MethodDeclaration;
import openjava.ptree.ModifierList;
import openjava.ptree.NonLeaf;
import openjava.ptree.Parameter;
import openjava.ptree.ParameterList;
import openjava.ptree.ParseTree;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.ReturnStatement;
import openjava.ptree.SelfAccess;
import openjava.ptree.Statement;
import openjava.ptree.StatementList;
import openjava.ptree.SwitchStatement;
import openjava.ptree.SynchronizedStatement;
import openjava.ptree.ThrowStatement;
import openjava.ptree.TryStatement;
import openjava.ptree.TypeName;
import openjava.ptree.TypeParameter;
import openjava.ptree.TypeParameterList;
import openjava.ptree.UnaryExpression;
import openjava.ptree.Variable;
import openjava.ptree.VariableDeclaration;
import openjava.ptree.VariableDeclarator;
import openjava.ptree.VariableInitializer;
import openjava.ptree.WhileStatement;
import openjava.ptree.util.ParseTreeVisitor;

/**
 * This class apply one line mutations to a node and return the mutated node.
 * The name of this class comes from One Line Mutations Ok (because I was really close to call it OLMO)
 * 
 * TODO: update the javadoc in this class
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 0.2
 */

public class OLMO extends ParseTreeVisitor {
	
	private Mutation mi;
	private java.util.List<Mutation> mis;
	private boolean mergingMutants;

	private int affected_line = -1;
	private int guardMutation_mutGenLimitLine = -1;
	private boolean insideMethodToConsider = false;
	private boolean nodeFound;
	private boolean stopCountingLines;
	private boolean guardMutation_getMutGenLimitLine;
	private boolean guardMutation_mutGenLimitLineFound;
	
	private boolean searchingNode = false;
	
	private boolean decreaseMutGenLimit;
	
	private boolean modifyAST;

	
	public OLMO() {
		super();
		this.modifyAST = true;
		this.decreaseMutGenLimit = false;
		this.searchingNode = false;
		this.guardMutation_getMutGenLimitLine = false;
		this.guardMutation_mutGenLimitLine = -1;
		this.guardMutation_mutGenLimitLineFound = false;
	}
	
	/**
	 * Constructor used to obtain the line modified by a MutantIdentifier
	 * 
	 * @param mi
	 */
	public OLMO(Mutation mi) {
		super();
		this.mi = mi;
		this.stopCountingLines = false;
		this.nodeFound = false;
		this.mergingMutants = false;
		this.modifyAST = false;
		this.decreaseMutGenLimit = false;
		this.searchingNode = false;
		this.guardMutation_getMutGenLimitLine = mi.isGuardMutation();
		//this.guardMutation_stopCounting = false;
		this.guardMutation_mutGenLimitLine = -1;
		this.guardMutation_mutGenLimitLineFound = false;
	}
	
	/**
	 * Constructor used to transform a list of MutantIdentifier into one
	 * 
	 * @param mis
	 */
	public OLMO(java.util.List<Mutation> mis) {
		super();
		this.mis = orderMutants(mis);
		ParseTreeObject lastNode = this.mis.get(this.mis.size()-1).getOriginal();
		ParseTreeObject statementToMutate = (ParseTreeObject) getNodeStatement(lastNode).makeRecursiveCopy_keepOriginalID();
		Mutation statementMutation = new Mutation(Mutant.MULTI, statementToMutate, (ParseTreeObject) statementToMutate.makeRecursiveCopy_keepOriginalID());
		this.mis.add(statementMutation);
		this.stopCountingLines = true;
		this.mergingMutants = true;
		this.modifyAST = false;
		this.decreaseMutGenLimit = false;
		this.searchingNode = false;
		this.guardMutation_getMutGenLimitLine = false;
		//this.guardMutation_stopCounting = false;
		this.guardMutation_mutGenLimitLine = -1;
		this.guardMutation_mutGenLimitLineFound = false;
	}
	
	public boolean findOriginalNodeIn(ParseTreeObject pto) throws ParseTreeException {
		this.nodeFound = false;
		this.searchingNode = true;
		acceptPTO(pto);
		return this.nodeFound;
	}
	
	public void modifyAST(CompilationUnit cu, Mutation mi) throws ParseTreeException {
		if (this.modifyAST) {
			this.mi = mi;
			cu.accept(this);
		}
	}
	
	public Mutation mergeMutants() throws ParseTreeException {
		Mutation statement = getLastMutantIdentifier();
		for (int i = this.mis.size()-2; i >= 0; i--) {
			Mutation mutationToApply = this.mis.get(i);
			applyMutation(mutationToApply, statement);
		}
		statement.setAffectedLine(this.mis.get(0).getAffectedLine());
		return statement;
	}
	
	public void decreaseMutationLimit(CompilationUnit cu, Mutation mi) throws ParseTreeException {
		if (this.modifyAST) {
			this.decreaseMutGenLimit = true;
			this.mi = mi;
			cu.accept(this);
			this.decreaseMutGenLimit = false;
		}
	}
	
	private Mutation getLastMutantIdentifier() {
		int last = this.mis.size() - 1;
		return this.mis.get(last);
	}

	private void applyMutation(Mutation mi, Mutation toMutate) throws ParseTreeException {
		this.mi = mi;
		acceptPTO(toMutate.getMutant());
	}

	private java.util.List<Mutation> orderMutants(java.util.List<Mutation> originalList) {
		java.util.List<Mutation> orderedList = new LinkedList<Mutation>();
		Map<Integer, java.util.List<Mutation>> orderingMap = new HashMap<Integer, java.util.List<Mutation>>();
		for (Mutation mi : originalList) {
			Integer value = nodeValue(mi.getOriginal());
			if (orderingMap.containsKey(value)) {
				orderingMap.get(value).add(mi);
			} else {
				java.util.List<Mutation> mutantIdentifiers = new LinkedList<Mutation>();
				mutantIdentifiers.add(mi);
				orderingMap.put(value, mutantIdentifiers);
			}
		}
		java.util.List<Integer> keys = new LinkedList<Integer>();
		keys.addAll(orderingMap.keySet());
		Collections.sort(keys);
		for (Integer key : keys) {
			orderedList.addAll(orderingMap.get(key));
		}
		return orderedList;
	}
	
	/*
	 * The bigger the value, the "smaller" the node
	 */
	private int nodeValue(ParseTreeObject obj) {
		int value = 0;
		ParseTreeObject current = (ParseTreeObject) obj.makeRecursiveCopy_keepOriginalID();
		while (current != null && !(current instanceof Statement)) {
			current = current.getParent();
			value++;
		}
		return -value;
	}
	
	private ParseTreeObject getNodeStatement(ParseTreeObject obj) {
		ParseTreeObject current = (ParseTreeObject) obj.makeRecursiveCopy_keepOriginalID();
		while (current != null && !(current instanceof Statement)) {
			current = current.getParent();
		}
		return current;
	}

	public int getAffectedLine() {
		return this.affected_line;
	}
	
	public int getGuardMutationMutGenLimitLine() {
		return this.guardMutation_mutGenLimitLine;
	}

	private final boolean isSameObject(ParseTree p, ParseTree q) {
		if (p == null && q == null)
			return true;
		if (p == null || q == null)
			return false;
		return (p.getObjectID() == q.getObjectID());
	}
	
	private void incrementAffectedLine() {
		if (!this.stopCountingLines && this.insideMethodToConsider && !this.modifyAST && !this.mergingMutants) {
			this.affected_line = this.affected_line==-1?1:(this.affected_line+1);
			this.stopCountingLines = this.nodeFound;
		}
		if (this.insideMethodToConsider && !this.mergingMutants && !this.modifyAST && this.guardMutation_getMutGenLimitLine && !this.guardMutation_mutGenLimitLineFound) {
			if (this.guardMutation_mutGenLimitLine < this.affected_line) {
				this.guardMutation_mutGenLimitLine = this.affected_line;
			} else {
				this.guardMutation_mutGenLimitLine++;
			}
			//this.guardMutation_stopCounting = this.guardMutation_mutGenLimitLineFound;
		}
	}
	
	private void acceptPTO(ParseTreeObject pto) throws ParseTreeException {
		if (pto != null && !this.modifyAST && !this.mergingMutants && (this.stopCountingLines && ((this.guardMutation_getMutGenLimitLine && this.guardMutation_mutGenLimitLineFound) || !this.guardMutation_getMutGenLimitLine))) return;
		if (pto != null) {
			if (this.mergingMutants || this.modifyAST || this.searchingNode) {
				if (isSameObject(this.mi.getOriginal(), pto)) {
					if (this.decreaseMutGenLimit) {
						ParseTreeObject parent = null;
						parent = mujava.op.util.Mutator.getMutationsLimitParent((pto));
						int mutationsLeft = mujava.op.util.Mutator.getMutationsLeft((pto));
						if (parent != null && mutationsLeft > 0) {
							((NonLeaf) parent).setMutGenLimit(mutationsLeft - 1);
						}
					} else if (this.searchingNode) {
						this.nodeFound = true;
						return;
					} else {
						pto.replace(this.mi.getMutant());
						if (!this.modifyAST) return;
					}
				}
			} else if (this.searchingNode && this.nodeFound) {
				return;
			}
			pto.accept(this);
			if (isStatement(pto)) incrementAffectedLine();
			if (!this.stopCountingLines) {
				if (isSameObject(pto, this.mi.getOriginal())) this.nodeFound = true;
			} else if (this.guardMutation_getMutGenLimitLine && !this.guardMutation_mutGenLimitLineFound) {
				if (isSameObject(pto, getNodeStatement(this.mi.getOriginal()))) this.guardMutation_mutGenLimitLineFound = true;
			}
		}
	}
	
	private boolean isStatement(ParseTreeObject pto) {
		if (pto instanceof StatementList) return false;
		if (pto instanceof Block) return false;
		if (pto instanceof Statement) return true;
		return false;
	}
	
	private void acceptPTO(Expression pto) throws ParseTreeException {
		acceptPTO((ParseTreeObject)pto);
	}

	public void visit(ClassDeclaration p) throws ParseTreeException {
		MemberDeclarationList classbody = p.getBody();
		acceptPTO(classbody);
	}

	public void visit(ConstructorDeclaration p) throws ParseTreeException {
		String methodUnderConsideration = Api.getMethodUnderConsideration();
		if (	methodUnderConsideration != null &&
				methodUnderConsideration.compareTo(p.getName()) == 0 ) {
			this.insideMethodToConsider = true;
			this.affected_line = -1;
			this.guardMutation_mutGenLimitLine = -1;
		}
		
		ModifierList modifs = p.getModifiers();
		acceptPTO(modifs);

		ParameterList params = p.getParameters();
		acceptPTO(params);
			
		TypeName[] tnl = p.getThrows();
		
		for (int i = 0; i < tnl.length; ++i) {
			acceptPTO(tnl[i]);
		}
		
		StatementList body = p.getBody();
		acceptPTO(body);
		
		if (this.insideMethodToConsider) this.insideMethodToConsider = false;
	}

	public void visit(AllocationExpression p) throws ParseTreeException {
		Expression encloser = p.getEncloser();
		acceptPTO(encloser);

		TypeName tn = p.getClassType();
		acceptPTO(tn);

		ExpressionList args = p.getArguments();
		acceptPTO(args);

		MemberDeclarationList mdlst = p.getClassBody();
		acceptPTO(mdlst);
	}

	public void visit(ArrayAccess p) throws ParseTreeException {
		Expression expr = p.getReferenceExpr();
		acceptPTO(expr);

		Expression index_expr = p.getIndexExpr();
		acceptPTO(index_expr);
	}

	public void visit(ArrayAllocationExpression p) throws ParseTreeException {
		TypeName tn = p.getTypeName();
		acceptPTO(tn);

		ExpressionList dl = p.getDimExprList();
		for (int i = 0; i < dl.size(); ++i) {
			Expression expr = dl.get(i);
			acceptPTO(expr);
		}

		ArrayInitializer ainit = p.getInitializer();
		acceptPTO(ainit);
	}

	public void visit(ArrayInitializer p) throws ParseTreeException {
		for (int i = 0; i < p.size(); i++) {
			acceptPTO((ParseTreeObject) p.get(i));
		}
	}

	public void visit(AssignmentExpression p) throws ParseTreeException {
		Expression lexpr = p.getLeft();
		acceptPTO(lexpr);

		Expression rexpr = p.getRight();
		acceptPTO(rexpr);
	}

	public void visit(BinaryExpression p) throws ParseTreeException {
		Expression lexpr = p.getLeft();
		acceptPTO(lexpr);

		Expression rexpr = p.getRight();
		acceptPTO(rexpr);
	}

	public void visit(Block p) throws ParseTreeException {
		StatementList stmts = p.getStatements();
		acceptPTO(stmts);
	}

	public void visit(BreakStatement p) throws ParseTreeException {}

	public void visit(CaseGroup p) throws ParseTreeException {
		ExpressionList labels = p.getLabels();
		for (int i = 0; i < labels.size(); i++) {
			acceptPTO(labels.get(i));
			incrementAffectedLine();
		}
		StatementList stmts = p.getStatements();
		acceptPTO(stmts);
	}

	public void visit(CaseGroupList p) throws ParseTreeException {
		for (int i = 0; i < p.size(); i++) {
			acceptPTO(p.get(i));
		}
	}

	public void visit(CaseLabel p) throws ParseTreeException {
		Expression expr = p.getExpression();
		acceptPTO(expr);
	}

	public void visit(CaseLabelList p) throws ParseTreeException {
		for (int i = 0; i < p.size(); i++) {
			acceptPTO(p.get(i));
		}
	}

	public void visit(CastExpression p) throws ParseTreeException {
		TypeName ts = p.getTypeSpecifier();
		acceptPTO(ts);

		Expression expr = p.getExpression();
		acceptPTO(expr);
	}

	public void visit(CatchBlock p) throws ParseTreeException {
		Parameter param = p.getParameter();
		incrementAffectedLine();
		acceptPTO(param);

		StatementList stmts = p.getBody();
		acceptPTO(stmts);
	}

	public void visit(CatchList p) throws ParseTreeException {
		for (int i = 0; i < p.size(); i++) {
			acceptPTO(p.get(i));
		}
	}

	public void visit(ClassDeclarationList p) throws ParseTreeException {
		for (int i = 0; i < p.size(); i++) {
			acceptPTO(p.get(i));
		}
	}

	public void visit(ClassLiteral p) throws ParseTreeException {
		TypeName type = p.getTypeName();
		acceptPTO(type);
	}

	public void visit(CompilationUnit p) throws ParseTreeException {
		ClassDeclarationList tdlst = p.getClassDeclarations();
		acceptPTO(tdlst);
	}

	public void visit(ConditionalExpression p) throws ParseTreeException {
		Expression condition = p.getCondition();
		acceptPTO(condition);

		Expression truecase = p.getTrueCase();
		acceptPTO(truecase);

		Expression falsecase = p.getFalseCase();
		acceptPTO(falsecase);
	}

	public void visit(ConstructorInvocation p) throws ParseTreeException {
		if (!p.isSelfInvocation()) {
			Expression enclosing = p.getEnclosing();
			if (enclosing != null) {
				acceptPTO(enclosing);
			}
		}

		ExpressionList exprs = p.getArguments();
		acceptPTO(exprs);
		
		incrementAffectedLine();
	}

	public void visit(ContinueStatement p) throws ParseTreeException {}

	public void visit(DoWhileStatement p) throws ParseTreeException {
		StatementList stmts = p.getStatements();
		acceptPTO(stmts);
		
		Expression expr = p.getExpression();
		acceptPTO(expr);
	}

	public void visit(EmptyStatement p) throws ParseTreeException {
		incrementAffectedLine();
	}

	public void visit(ExpressionList p) throws ParseTreeException {
		for (int i = 0; i < p.size(); i++) {
			acceptPTO(p.get(i));
		}
	}

	public void visit(ExpressionStatement p) throws ParseTreeException {
		Expression expr = p.getExpression();
		acceptPTO(expr);
	}

	public void visit(FieldAccess p) throws ParseTreeException {
		Expression expr = p.getReferenceExpr();
		TypeName typename = p.getReferenceType();

		if (expr != null) {
			acceptPTO(expr);
		} else if (typename != null) {
			acceptPTO(typename);
		}
	}

	public void visit(FieldDeclaration p) throws ParseTreeException {
		/* ModifierList */
		ModifierList modifs = p.getModifiers();
		acceptPTO(modifs);

		/* TypeName */
		TypeName ts = p.getTypeSpecifier();
		acceptPTO(ts);

		//Field name

		/* "=" VariableInitializer */
		VariableInitializer initializer = p.getInitializer();
		acceptPTO((ParseTreeObject) initializer);
	}

	public void visit(ForStatement p) throws ParseTreeException {
		ExpressionList init = p.getInit();
		TypeName tspec = p.getInitDeclType();
		VariableDeclarator[] vdecls = p.getInitDecls();
		if (init != null && (!init.isEmpty())) {
			for (int i = 0; i < init.size(); i++) {
				acceptPTO(init.get(i));
			}
		} else if (tspec != null && vdecls != null && vdecls.length != 0) {
			acceptPTO(tspec);
			for (int i = 0; i < vdecls.length; i++) {
				acceptPTO(vdecls[i]);
			}
		}

		Expression expr = p.getCondition();
		acceptPTO(expr);
		incrementAffectedLine();
	
		ExpressionList incr = p.getIncrement();
		if (incr != null && (!incr.isEmpty())) {
			for (int i = 0; i < incr.size(); i++) {
				acceptPTO(incr.get(i));
			}
		}

		StatementList stmts = p.getStatements();
		acceptPTO(stmts);
	}

	public void visit(IfStatement p) throws ParseTreeException {
		Expression expr = p.getExpression();
		acceptPTO(expr);
		incrementAffectedLine();

		/* then part */
		StatementList stmts = p.getStatements();
		acceptPTO(stmts);

		/* else part */
		StatementList elsestmts = p.getElseStatements();
		if (!elsestmts.isEmpty()) incrementAffectedLine();
		acceptPTO(elsestmts);
	}

	public void visit(InstanceofExpression p) throws ParseTreeException {
		Expression lexpr = p.getExpression();
		acceptPTO(lexpr);

		TypeName tspec = p.getTypeSpecifier();
		acceptPTO(tspec);
	}

	public void visit(LabeledStatement p) throws ParseTreeException {
		//label p.getLabel()
		Statement statement = p.getStatement();
		acceptPTO((ParseTreeObject) statement);
	}

	public void visit(Literal p) throws ParseTreeException {
		//literal
	}

	public void visit(MemberDeclarationList p) throws ParseTreeException {
		for (int i = 0; i < p.size(); i++) {
			acceptPTO((ParseTreeObject) p.get(i));
		}
	}

	public void visit(MemberInitializer p) throws ParseTreeException {
		StatementList stmts = p.getBody();
		acceptPTO(stmts);
		//maybe call incrementAffectedLine()
	}

	public void visit(MethodCall p) throws ParseTreeException {
		Expression expr = p.getReferenceExpr();
		TypeName reftype = p.getReferenceType();

		if (expr != null) {
			acceptPTO(expr);
		} else if (reftype != null) {
			acceptPTO(reftype);
		}

		ExpressionList args = p.getArguments();
		acceptPTO(args);
	}

	public void visit(MethodDeclaration p) throws ParseTreeException {
		String methodUnderConsideration = Api.getMethodUnderConsideration();
		if (	methodUnderConsideration != null &&
				methodUnderConsideration.compareTo(p.getName()) == 0 ) {
			this.insideMethodToConsider = true;
			this.affected_line = -1;
			this.guardMutation_mutGenLimitLine = -1;
		}
		
		ModifierList modifs = p.getModifiers();
		acceptPTO(modifs);

		TypeName ts = p.getReturnType();
		acceptPTO(ts);

		ParameterList params = p.getParameters();
		acceptPTO(params);
			
		TypeName[] tnl = p.getThrows();
		
		for (int i = 0; i < tnl.length; ++i) {
			acceptPTO(tnl[i]);
		}
		
		StatementList body = p.getBody();
		acceptPTO(body);
		
		if (this.insideMethodToConsider) this.insideMethodToConsider = false;
		
	}

	public void visit(ModifierList p) throws ParseTreeException {
		//modifiers are string
	}

	public void visit(Parameter p) throws ParseTreeException {
		ModifierList modifs = p.getModifiers();
		acceptPTO(modifs);
		
		TypeName typespec = p.getTypeSpecifier();
		acceptPTO(typespec);
	}

	public void visit(ParameterList p) throws ParseTreeException {
		for (int i = 0; i < p.size(); i++) {
			acceptPTO(p.get(i));
		}
	}

	public void visit(ReturnStatement p) throws ParseTreeException {
		Expression expr = p.getExpression();
		acceptPTO(expr);
	}

	public void visit(SelfAccess p) throws ParseTreeException {}

	public void visit(StatementList p) throws ParseTreeException {
		for (int i = 0; i < p.size(); i++) {
			acceptPTO((ParseTreeObject) p.get(i));
		}
	}

	public void visit(SwitchStatement p) throws ParseTreeException {
		Expression expr = p.getExpression();
		acceptPTO(expr);
		incrementAffectedLine();
		
		CaseGroupList casegrouplist = p.getCaseGroupList();
		acceptPTO(casegrouplist);
	}

	public void visit(SynchronizedStatement p) throws ParseTreeException {
		acceptPTO(p.getExpression());
		incrementAffectedLine();
		acceptPTO(p.getStatements());
	}

	public void visit(ThrowStatement p) throws ParseTreeException {
		Expression expr = p.getExpression();
		acceptPTO(expr);
	}

	public void visit(TryStatement p) throws ParseTreeException {
		acceptPTO(p.getBody());
		acceptPTO(p.getCatchList());
		if (!p.getFinallyBody().isEmpty()) incrementAffectedLine();
		acceptPTO(p.getFinallyBody());
	}

	
	public void visit(TypeName p) throws ParseTreeException {
		//name and dimension
	}

	public void visit(UnaryExpression p) throws ParseTreeException {
		//check operator
		acceptPTO(p.getExpression());
	}

	public void visit(Variable p) throws ParseTreeException {
		//name
	}

	public void visit(VariableDeclaration p) throws ParseTreeException {
		ModifierList modifs = p.getModifiers();
		acceptPTO(modifs);

		TypeName typespec = p.getTypeSpecifier();
		acceptPTO(typespec);

		VariableDeclarator vd = p.getVariableDeclarator();
		acceptPTO(vd);
		
		//incrementAffectedLine();
	}

	public void visit(VariableDeclarator p) throws ParseTreeException {
		VariableInitializer varinit = p.getInitializer();
		acceptPTO((ParseTreeObject) varinit);
	}

	public void visit(WhileStatement p) throws ParseTreeException {
		acceptPTO(p.getExpression());
		incrementAffectedLine();
		acceptPTO(p.getStatements());
	}

	@Override
	public void visit(EnumDeclaration p) throws ParseTreeException {
		//modifiers
		//p.getModifiers(); //ignore this
		//identifier
		//p.getName(); //ignore this
		//implement list
		//p.getImplementsList(); //ignore this
		
		//enum constants
		acceptPTO(p.getEnumConstantList());
		
		//enum body (optional)
		MemberDeclarationList body = p.getClassBodayDeclaration();
		if (body != null) {
			acceptPTO(body);
		}
	}

	@Override
	public void visit(EnumConstant p) throws ParseTreeException {
		//modifiers
		//p.getModifiers(); //ignore this
		//name
		//p.getName(); //ignore this
		//arguments
		acceptPTO(p.getArguments());
		//class body
		acceptPTO(p.getClassBody());
	}

	@Override
	public void visit(EnumConstantList p) throws ParseTreeException {
		if (p != null && !p.isEmpty()) {
			for (int c = 0; c < p.size(); c++) {
				acceptPTO(p.get(c));
			}
		}
	}

	@Override
	public void visit(TypeParameter p) throws ParseTreeException {
		//ignore
		
	}

	@Override
	public void visit(TypeParameterList p) throws ParseTreeException {
		//ignore all
	}


}
