package mujava.op;

import java.util.Random;

import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.mop.OJClassNotFoundException;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.CatchBlock;
import openjava.ptree.CatchList;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.Expression;
import openjava.ptree.ExpressionList;
import openjava.ptree.ExpressionStatement;
import openjava.ptree.FieldAccess;
import openjava.ptree.ForStatement;
import openjava.ptree.IfStatement;
import openjava.ptree.Literal;
import openjava.ptree.MethodCall;
import openjava.ptree.MethodDeclaration;
import openjava.ptree.Parameter;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.ReturnStatement;
import openjava.ptree.StatementList;
import openjava.ptree.TryStatement;
import openjava.ptree.TypeName;
import openjava.ptree.Variable;
import openjava.ptree.VariableDeclaration;
import openjava.ptree.VariableDeclarator;
import openjava.ptree.WhileStatement;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;
import mujava.op.util.Mutator;

/**
 * This operator does code transformation that tries to repair {@code NullPointerException} errors.
 * <p>
 * Possible modification that this operator can do are:
 * <li>STATEMENT => try{STATEMENT} catch (NullPointerException npe) {System.out.println("NPE detected");}</li>
 * <li>return EXPRESSION => {try {return EXPRESSION} catch (NullPointerException npe) {System.out.println("NPE detected");return [default value];}}/li> 
 * <li>while (EXPRESSION) {STATEMENTS}
 * 		<p>
 * 	<pre>
 * 		=>
 *  </pre>
 * 		<p>
 *	<pre>
 *		boolean c = false;
 * 		try {c = EXPRESSION;}
 * 		catch (NullPointerException npe)
 * 		{System.out.println("NPE detected evaluating expression before while");}
 *		while(c) {
 *			STATEMENTS
 *			try {
 *				c = EXPRESSION
 *			} catch (NullPointerException npe) {
 *				System.out.println("NPE detected evaluating expression inside while");
 *				c = false;
 *			}
 *		}
 * 	</pre>	
 * </li>
 * <li>
 * 	<pre>
 * 		for (INITIALIZATION; EXPRESSION; INCREMENT) {
 * 			STATEMENTS
 * 		}
 * 	</pre>
 *  <p>
 *  <pre>
 *  	=>
 *  </pre>
 *  <p>
 *  <pre>
 *  	boolean initOk = true;
 *  	try {INITIALIZATION}
 *      catch (NullPointerException npe) {
 *      	System.out.println("NPE detected in for initialization");
 *      	initOk = false;
 *      }
 *      if (initOk) {
 *			boolean c = false;
 * 			try {c = EXPRESSION;}
 * 			catch (NullPointerException npe)
 * 			{System.out.println("NPE detected before for condition evaluation");}
 * 			while(c) {
 * 				STATEMENTS
 * 				try {INCREMENT}
 * 				catch (NullPointerException npe) {
 * 					System.out.println("NPE detected in for increment");
 * 					c = false;
 * 				}
 * 				try {c = EXPRESSION;}
 * 				catch (NullPointerException npe) {
 * 					System.out.println("NPE detected before for condition evaluation");
 * 					c = false;
 * 				}
 * 			}
 * 		}
 *  </pre>
 * </li>
 */
public class NPER extends Mutator {
	
	private final Parameter nullPointerExceptionCatchParam = new Parameter(new TypeName("java.lang.NullPointerException"), "npe");
	private final TypeName systemType = new TypeName("System");
	private final FieldAccess systemOut = new FieldAccess(systemType, "out");
	private final MethodCall systemOutPrintln = new MethodCall(systemOut, "println", null);
	private final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private Random rng = new Random();
	
	public NPER(FileEnvironment file_env, ClassDeclaration cdecl,CompilationUnit comp_unit) {
		super(file_env, comp_unit);
	}
	
	public void visit(ForStatement p) throws ParseTreeException {
		if (getMutationsLeft(p) > 0) {
			StatementList mutant = new StatementList();
			ForStatement pCopy = (ForStatement) p.makeRecursiveCopy_keepOriginalID();
			
			VariableDeclarator initBoolVarDecl = createRandomBooleanVar("initOk_", true);
			VariableDeclaration initBoolVarDeclaration = new VariableDeclaration(new TypeName("java.lang.boolean"), initBoolVarDecl);
			mutant.add(initBoolVarDeclaration);
			
			ExpressionList inits = pCopy.getInit();
			StatementList initsAsStatements = new StatementList();
			StatementList initDeclarationsStatements = null;
			if (inits != null) {
				for (int i = 0; i < inits.size(); i++) {
					initsAsStatements.add(new ExpressionStatement(inits.get(i)));
				}
			} else {
				VariableDeclarator[] varDecls = pCopy.getInitDecls();
				if (varDecls != null) {
					initDeclarationsStatements = new StatementList();
					for (VariableDeclarator vd : varDecls) {
						try {
							initDeclarationsStatements.add(new VariableDeclaration(pCopy.getInitDeclType(), vd.getVariable(), generateDefaultValue(OJClass.forName(pCopy.getInitDeclType().getName()))));
						} catch (OJClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						initsAsStatements.add(new ExpressionStatement(new AssignmentExpression(new Variable(vd.getVariable()), AssignmentExpression.EQUALS, (Expression) vd.getInitializer())));
					}
				}
			}
			if (initDeclarationsStatements != null) mutant.addAll(initDeclarationsStatements); 
			AssignmentExpression initFailed = new AssignmentExpression(new Variable(initBoolVarDecl.getVariable()), AssignmentExpression.EQUALS, Literal.makeLiteral(false));
			StatementList initCatchPart = new StatementList(new ExpressionStatement(initFailed));
			//Initialization tryCatch+++
			TryStatement initializationTry = createNullPointerExceptionTryStatement(initsAsStatements, initCatchPart, "NPE detected in for initialization");
			mutant.add(initializationTry);
			//Initialization tryCatch---
			VariableDeclarator condBoolVarDecl = createRandomBooleanVar("cond_", false);
			StatementList incrementsAsStatements = new StatementList();
			ExpressionList increments = pCopy.getIncrement();
			for (int i = 0; i < increments.size(); i++) {
				incrementsAsStatements.add(new ExpressionStatement(increments.get(i)));
			}
			AssignmentExpression incrementFailed = new AssignmentExpression(new Variable(condBoolVarDecl.getVariable()), AssignmentExpression.EQUALS, Literal.makeLiteral(false));
			StatementList incrementCatchPart = new StatementList(new ExpressionStatement(incrementFailed));
			//Increment tryCatch+++
			TryStatement incrementTry = createNullPointerExceptionTryStatement(incrementsAsStatements, incrementCatchPart, "NPE detected in for increment");
			//Increment tryCatch---
			StatementList expressionEvaluationStatements = new StatementList(new ExpressionStatement(new AssignmentExpression(new Variable(condBoolVarDecl.getVariable()), AssignmentExpression.EQUALS, pCopy.getCondition())));
			//expression tryCatch+++
			TryStatement expressionTry = createNullPointerExceptionTryStatement(expressionEvaluationStatements, new StatementList(), "NPE detected before for condition evaluation");
			//expression tryCatch---
			StatementList forStatements = (StatementList) pCopy.getStatements().makeRecursiveCopy_keepOriginalID();
			forStatements.add(incrementTry);
			TryStatement insideForExpressionTry = (TryStatement) expressionTry.makeRecursiveCopy(); 
			insideForExpressionTry.getCatchList().get(0).getBody().add(new ExpressionStatement((Expression) incrementFailed.makeRecursiveCopy()));
			forStatements.add(insideForExpressionTry);
			WhileStatement whileStatement = createWhileStatement(forStatements, new Variable(condBoolVarDecl.getVariable()));
			StatementList ifStatements = new StatementList();
			VariableDeclaration condVarDeclaration = new VariableDeclaration(new TypeName("java.lang.boolean"), condBoolVarDecl);
			ifStatements.add(condVarDeclaration);
			ifStatements.add(expressionTry);
			ifStatements.add(whileStatement);
			IfStatement ifStatement = createIfStatement(ifStatements, new Variable(initBoolVarDecl.getVariable()));
			mutant.add(ifStatement);
			outputToFile(pCopy, mutant);
		}
		super.visit(p);
	}
	
	public void visit(WhileStatement p) throws ParseTreeException {
		if (getMutationsLeft(p) > 0) {
			StatementList mutant = new StatementList();
			WhileStatement pCopy = (WhileStatement) p.makeRecursiveCopy_keepOriginalID();
			VariableDeclarator condBoolVarDecl = createRandomBooleanVar("cond_", false);
			VariableDeclaration condBoolVarDeclaration = new VariableDeclaration(new TypeName("java.lang.boolean"), condBoolVarDecl);
			AssignmentExpression failureAssignment = new AssignmentExpression(new Variable(condBoolVarDecl.getVariable()), AssignmentExpression.EQUALS, Literal.makeLiteral(false));
			AssignmentExpression conditionAssignment = new AssignmentExpression(new Variable(condBoolVarDecl.getVariable()), AssignmentExpression.EQUALS, pCopy.getExpression());
			StatementList conditionTryStatements = new StatementList();
			conditionTryStatements.add(new ExpressionStatement(conditionAssignment));
			TryStatement conditionTryBefore = createNullPointerExceptionTryStatement(conditionTryStatements, new StatementList(), "NPE detected evaluating expression before while");
			TryStatement conditionTryInside = createNullPointerExceptionTryStatement((StatementList) conditionTryStatements.makeRecursiveCopy_keepOriginalID(), new StatementList(), "NPE detected evaluating expression inside while");
			conditionTryInside.getCatchList().get(0).getBody().add(new ExpressionStatement(failureAssignment));
			mutant.add(condBoolVarDeclaration);
			mutant.add(conditionTryBefore);
			StatementList whileStatements = new StatementList();
			whileStatements.addAll((StatementList) pCopy.getStatements().makeCopy_keepOriginalID());
			whileStatements.add(conditionTryInside);
			WhileStatement newWhile = new WhileStatement(new Variable(condBoolVarDecl.getVariable()), whileStatements);
			mutant.add(newWhile);
			outputToFile(p, mutant);
		}
		super.visit(p);
	}
	

	public void visit(AssignmentExpression p) throws ParseTreeException {
		if (getMutationsLeft(p) > 0) {
			AssignmentExpression pCopy = (AssignmentExpression) p.makeRecursiveCopy_keepOriginalID();
			TryStatement catchedExpressionStatement = createNullPointerExceptionTryStatement(new StatementList(new ExpressionStatement(pCopy)), new StatementList(), "NPE detected");
			outputToFile(p, catchedExpressionStatement);
		}
		super.visit(p);
	}
	
	public void visit(ReturnStatement p) throws ParseTreeException {
		if (getMutationsLeft(p) > 0) {
			MethodDeclaration md = (MethodDeclaration) getMethodDeclaration(p);
			if (md != null) {
				try {
					OJClass methodReturnType = OJClass.forName(md.getReturnType().getName());
					Expression defaultValue = generateDefaultValue(methodReturnType);
					ReturnStatement originalReturn = (ReturnStatement) p.makeRecursiveCopy_keepOriginalID();
					StatementList tryStatements = new StatementList(originalReturn);
					ReturnStatement defaultReturn = new ReturnStatement(defaultValue);
					StatementList catchStatements = new StatementList(defaultReturn);
					TryStatement mutant = createNullPointerExceptionTryStatement(tryStatements, catchStatements, "NPE detected in return statement");
					outputToFile(p, mutant);
				} catch (OJClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		super.visit(p);
	}
	
	private TryStatement createNullPointerExceptionTryStatement(StatementList tryPart, StatementList catchPart, String message) {
		MethodCall customSOPLN = (MethodCall) systemOutPrintln.makeRecursiveCopy();
		customSOPLN.setArguments(new ExpressionList(Literal.makeLiteral(message)));
		catchPart.insertElementAt(new ExpressionStatement(customSOPLN), 0);
		CatchBlock catchBlock = new CatchBlock(this.nullPointerExceptionCatchParam, catchPart);
		CatchList catchList = new CatchList(catchBlock);
		TryStatement createdTryStatement = new TryStatement(tryPart, catchList);
		return createdTryStatement;
	}
	
	private IfStatement createIfStatement(StatementList statements, Expression condition) {
		return new IfStatement(condition, statements);
	}
	
	private WhileStatement createWhileStatement(StatementList statements, Expression condition) {
		return new WhileStatement(condition, statements);
	}
	
	private VariableDeclarator createRandomBooleanVar(String prefix, boolean initialValue) {
		VariableDeclarator newBoolVar = new VariableDeclarator(prefix+createRandomString(), Literal.makeLiteral(initialValue));
		return newBoolVar;
	}
	
	private String createRandomString() {
		String ranString = "";
		for (int i = 1; i <= 10; i++) {
			ranString += this.characters.charAt(this.rng.nextInt(this.characters.length()));
		}
		return ranString;
	}
	
	private Literal generateDefaultValue(OJClass forClass) {
		if (!forClass.isPrimitive() && !forClass.isPrimitiveWrapper()) {
			return Literal.constantNull();
		} else if (forClass.isArray()) {
			return Literal.constantNull();
		} else if (forClass.isPrimitive() || forClass.isPrimitiveWrapper()) {
			OJClass unwrappedClass = forClass.isPrimitiveWrapper()?forClass.unwrappedPrimitive():forClass;
			if (unwrappedClass.getSimpleName().compareTo(openjava.mop.OJSystem.BOOLEAN.getSimpleName()) == 0) {
				return Literal.constantFalse();
			} else if (unwrappedClass.getSimpleName().compareTo(openjava.mop.OJSystem.CHAR.getSimpleName()) == 0) {
				return Literal.makeLiteral(' ');
			} else if (unwrappedClass.getSimpleName().compareTo(openjava.mop.OJSystem.BYTE.getSimpleName()) == 0) {
				return Literal.makeLiteral(0);
			} else if (unwrappedClass.getSimpleName().compareTo(openjava.mop.OJSystem.SHORT.getSimpleName()) == 0) {
				return Literal.makeLiteral(0);
			} else if (unwrappedClass.getSimpleName().compareTo(openjava.mop.OJSystem.INT.getSimpleName()) == 0) {
				return Literal.constantZero();
			} else if (unwrappedClass.getSimpleName().compareTo(openjava.mop.OJSystem.LONG.getSimpleName()) == 0) {
				return Literal.makeLiteral(0);
			} else if (unwrappedClass.getSimpleName().compareTo(openjava.mop.OJSystem.FLOAT.getSimpleName()) == 0) {
				return Literal.makeLiteral(0);
			}
		}
		return Literal.constantNull();
	}
	
	private void outputToFile(ParseTreeObject original, ParseTreeObject mutant) {
		MutantsInformationHolder.mainHolder().addMutation(Mutant.NPER, original, mutant);
	}

}
