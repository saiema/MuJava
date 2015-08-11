package mujava.api;

import mujava.op.InstantiationSearcher;
import mujava.op.util.Mutator;
import openjava.mop.OJClass;
import openjava.ptree.ArrayAccess;
import openjava.ptree.AssignmentExpression;
import openjava.ptree.CompilationUnit;
import openjava.ptree.DoWhileStatement;
import openjava.ptree.Expression;
import openjava.ptree.FieldAccess;
import openjava.ptree.IfStatement;
import openjava.ptree.MethodCall;
import openjava.ptree.ParseTreeException;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.Statement;
import openjava.ptree.Variable;
import openjava.ptree.VariableDeclaration;
import openjava.ptree.WhileStatement;

/**
 * Uniquely identifies a mutation operation
 * 
 * This class identifies a mutation with a tuple of 
 * <p>
 * <li> <b>Operator</b>, the mutation operator used to generate this mutation </li>
 * <li> <b>Original node</b>, the AST node this mutation affects </li>
 * <li> <b>Mutated node</b>, the AST node this mutation produced </li>
 * <p>
 * It also offert some additional information
 * <p>
 * <li> <b> Is one line in method op? </b>, if the mutation affects only one line inside a method declaration
 * <li> <b> Affected line </b>, only if this is a one line in method op, shows which line was affected inside a method
 * <li> <b> Is a guard mutation? </b>, if the mutation affects a guard expression
 * <li> <b> Is a variable declaration mutation? </b>, if the mutation affects a variable declaration statement
 * <li> <b> Is an assignment mutation? </b>, if the mutation affects an assignment statement
 * <ol>
 * <li> <b> Mutation affects left side of the assignment </b>, if the mutations affects the left side of an assignment statement
 * <ol>
 * <li> <b> Mutation affects left side of the assignment and it's a variable </b>
 * <li> <b> Mutation affects left side of the assignment and it's a array access </b>
 * <li> <b> Mutation affects left side of the assignment and it's a field access </b>
 * </ol>
 * <li> <b> Mutation affects right side of the assignment </b>, if the mutations affects the right side of an assignment statement
 * </ol>
 * <li> <b> MutGenLimit line </b>, only if this is a guard mutation and affects only one line inside a method declaration, shows the line where the corresponding {@code //mutGenLimit} is
 * <li> <b> Is mutant a chained expression </b>, only if the mutant node has the form {@code x.y}
 * <li> <b> Is original a chained expression </b>, only if the original node has the form {@code x.y}
 * <li> <b> Is Mutant a tail change of original </b>, only if {@code isChainExpression(TARGET.ORIGINAL)} and {@code isChainExpression(TARGET.ORIGINAL)} both return {@code true}
 * and mutant is {@code y.z} while original is {@code x.z}.
 * <hr>
 * <p><b>note:</b> <i>the affected line refers to a line inside the method declaration of the mutated method, the first line of the method declaration is the line 1</i></p>
 * <p><b>note:</b> <i>when refering to an expression of the form {@code x.y} {@code x} can also be an expression of this form</i></p>
 * @version 2.7.3
 */
public class Mutation {
	public static enum TARGET {ORIGINAL, MUTANT};

	// The mutant operator
	private Mutant mutOp;
	
	public static final int SINGLE_LINE_OUTSIDE_METHOD_DECLARATION = Integer.MIN_VALUE;
	
	private int affectedLine = -1;

	// The original object from the parse tree to be mutated, the mutant and
	// some additional information that is necessary for some mutant operators
	private ParseTreeObject original, mutant;
	
	private boolean oneLineInMethodOp;
	
	private boolean isGuardMutation;
	
	private boolean isAssignmentMutation;
	
	private boolean isVariableDeclarationMutation;
	
	private boolean mutatesLeftSideOfAssignment;
	
	private boolean mutatesRightSideOfAssignment;
	
	private boolean leftSideOfAssignmentIsVariable;
	
	private boolean leftSideOfAssignmentIsFieldAccess;
	
	private boolean leftSideOfAssignmentIsArrayAccess;

	/**
	 * Constructor
	 * 
	 * @param mutOp		: the operator used in this mutation : {@code Mutant}
	 * @param original	: the original AST node this mutation affected : {@code ParseTreeObject}
	 * @param mutant	: the mutated AST node : {@code ParseTreeObject}
	 */
	public Mutation(Mutant mutOp, ParseTreeObject original, ParseTreeObject mutant) {
		this.mutOp = mutOp;
		this.original = original;
		this.mutant = mutant;
		this.oneLineInMethodOp = checkOp(mutOp);
		this.isGuardMutation = mutationAffectsGuardExpression(original) || mutationAffectsGuardExpression(mutant);
		this.isAssignmentMutation = mutationAffectsAssignmentStatement(original) || mutationAffectsAssignmentStatement(mutant);
		this.isVariableDeclarationMutation = mutationAffectsVariableDeclarationStatement(original) || mutationAffectsVariableDeclarationStatement(mutant);
		evaluateWhichSideIsMutated();
		evaluateLeftSideOfAssignment();
	}

	/**
	 * Check if the original or the mutant node is an expression with the form {@code x.y}
	 * 
	 * @param target	:	this defines whether the original or the mutant node will be checked	:	{@code TARGET}
	 * @return {@code true} if {@code target == TARGET.ORIGINAL &&} original node has the form {@code x.y} {@code ||} {@code target == TARGET.MUTANT &&} mutant node has the form {@code x.y}	:	{@code boolean}
	 */
	public boolean isChainedExpression(TARGET target) {
		boolean res = false;
		switch(target) {
			case ORIGINAL 	: 	{res = isChainedExpression(this.original); break;}
			case MUTANT		:	{res = isChainedExpression(this.mutant); break;}
		}
		return res;
	}
	
	private boolean isChainedExpression(ParseTreeObject exp) {
		boolean res = false;
		try {
			Expression expAsExpression = (Expression) exp;
			if (expAsExpression instanceof FieldAccess) {
				res = ((FieldAccess)expAsExpression).getReferenceExpr() != null;
			} else if (expAsExpression instanceof MethodCall) {
				res = ((MethodCall)expAsExpression).getReferenceExpr() != null;
			} else if (expAsExpression instanceof ArrayAccess) {
				res = isChainedExpression((ParseTreeObject) ((ArrayAccess)expAsExpression).getReferenceExpr() );
			} else {
				//exp has a type that can't possible form a x.y expression
				res = false;
			}
		} catch (ClassCastException ex) {
			//couldn't cast exp to Expression
			res = false;
		}
		return res;
	}
	
	/**
	 * @return {@code isAssignmentMutation() && isAssignmentMutationLeft() && isMutantTailChangeOfOriginal()} : {@code boolean}
	 * @see Mutation#isAssignmentMutation()
	 * @see Mutation#isAssignmentMutationLeft()
	 * @see Mutation#isMutantTailChangeOfOriginal()
	 */
	public boolean isMutantATailChangeOfTheLeftSideOfAnAssignmentExpression() {
		return this.isAssignmentMutation() && this.isAssignmentMutationLeft() && this.isMutantTailChangeOfOriginal();
	}
	
	/**
	 * @return {@code true} if {@code isChainExpression(TARGET.ORIGINAL)} and {@code isChainExpression(TARGET.ORIGINAL)} both return {@code true}
	 * and mutant is {@code y.z} while original is {@code x.z}.
	 * <hr>
	 * <b> take note that both {@code y} and {@code x} can also be a chained expression </b>
	 */
	public boolean isMutantTailChangeOfOriginal() {
		boolean res = false;
		Expression originalAsExpression = null;
		Expression mutantAsExpression = null;
		if (isChainedExpression(TARGET.ORIGINAL) && isChainedExpression(TARGET.MUTANT)) {
			try {
				originalAsExpression = (Expression) this.original;
				mutantAsExpression = (Expression) this.mutant;
				String originalLastName = getLastName(originalAsExpression);
				String mutantLastName = getLastName(mutantAsExpression);
				if (originalLastName != null && mutantLastName != null) {
					res = originalLastName.compareTo(mutantLastName) == 0;
				}
			} catch (ClassCastException ex) {
				//couldn't cast mutant or original to Expression
				res = false;
			}
			if (res && mutantAsExpression != null) {
				Expression mutantTerm = Mutator.getPreviousExpression(mutantAsExpression);
				CompilationUnit comp_unit = Mutator.getCompilationUnit((ParseTreeObject) mutantTerm);
				if (findAllocationFor(comp_unit, mutantTerm)) res = false;
			}
			return res;
		}
		return res;
	}
	
	private boolean findAllocationFor(CompilationUnit cu, Expression term) {
		boolean found = false;
		boolean error = false;
		Expression currentTerm = term;
		while (!error && !found && currentTerm != null) {
			InstantiationSearcher instSearcher = new InstantiationSearcher(null, cu, currentTerm);
			try {
				instSearcher.visit(cu);
				found = instSearcher.allocationExpressionFound();
			} catch (ParseTreeException e) {
//				e.printStackTrace();
				error = true;
			}
			if (!error && !found) {
				currentTerm = Mutator.getPreviousExpression(currentTerm);
			}
		}
		return found;
	}
	
	private String getLastName(Expression exp) {
		String lastName = null;
		if (exp instanceof FieldAccess) {
			lastName = ((FieldAccess)exp).getName();
		} else if (exp instanceof MethodCall) {
			lastName = ((MethodCall)exp).getName();
		} else if (exp instanceof ArrayAccess) {
			lastName = getLastName(((ArrayAccess)exp).getReferenceExpr());
		}
		return lastName;
	}
	
	//TODO: complete this method
	@SuppressWarnings("unused")
	private boolean isMutantTailChangeOfOriginalRefined(Expression original, Expression mutant, Mutator mutator) throws ParseTreeException {
		boolean res = false;
		//first, lets check if original and mutant are chained expressions
		if (isChainedExpression(TARGET.ORIGINAL) && isChainedExpression(TARGET.MUTANT)) {
			//now, lets check if original and mutant end with the same name
			String originalsEnd = getEndName(original);
			String mutantsEnd = getEndName(mutant);
			if (originalsEnd != null && mutantsEnd != null && originalsEnd.compareTo(mutantsEnd)==0) {
				//now, lets check the type of original and mutant
				OJClass originalsType = mutator.getType(original);
				OJClass mutantsType = mutator.getType(mutant);
				if (originalsType.toString().compareTo(mutantsType.toString()) == 0) {
					//now, we have to know if the end in mutant comes from the same class as the end in original
					
				}
			}
		}
		return res;
	}
	
	private String getEndName(Expression exp) {
		String endName = null;
		if (exp instanceof FieldAccess) {
			endName = ((FieldAccess)exp).getName();
		} else if (exp instanceof MethodCall) {
			endName = ((MethodCall)exp).getName();
		} else if (exp instanceof ArrayAccess) {
			endName = null;
		}
		return endName;
	}
	
	private void evaluateLeftSideOfAssignment() {
		this.leftSideOfAssignmentIsArrayAccess = false;
		this.leftSideOfAssignmentIsFieldAccess = false;
		this.leftSideOfAssignmentIsVariable = false;
		if (this.isAssignmentMutation && this.mutatesLeftSideOfAssignment && !this.isVariableDeclarationMutation) {
			if (isChainedExpression(TARGET.MUTANT)) {
				this.leftSideOfAssignmentIsFieldAccess = true;
			}
			try {
				Expression mut = (Expression) this.mutant;
				if (mut instanceof Variable) {
					this.leftSideOfAssignmentIsVariable = true;
				} else if (mut instanceof ArrayAccess) {
					this.leftSideOfAssignmentIsArrayAccess = true;
				}
			} catch (ClassCastException ex) {
				
			}
		}
	}
	
	private void evaluateWhichSideIsMutated() {
		this.mutatesLeftSideOfAssignment = false;
		this.mutatesRightSideOfAssignment = false;
		if (this.isAssignmentMutation) {
			if (isPRVOLFamily()) {
				this.mutatesLeftSideOfAssignment = true;
			} else if (isPRVORFamily()) {
				this.mutatesRightSideOfAssignment = true;
			}
		}
	}
	
	private boolean isPRVOLFamily() {
		return this.mutOp == Mutant.PRVOL || this.mutOp == Mutant.PRVOL_SMART;
	}
	
	private boolean isPRVORFamily() {
		return this.mutOp == Mutant.PRVOR || this.mutOp == Mutant.PRVOR_SMART || this.mutOp == Mutant.PRVOR_REFINED;
	}
	
	/**
	 * Sets the affected line, this should only be used when {@code isOneLineInMethodOp()} returns {@code true}
	 * 
	 * @param affectedLine : the line affected by this mutation : {@code int}
	 */
	public void setAffectedLine(int affectedLine) {
		this.affectedLine = affectedLine;
	}
	
	/**
	 * @return the affected line or {@code -1} if {@code isOneLineInMethodOp()} returns {@code false}
	 */
	public int getAffectedLine() {
		return this.affectedLine;
	}
	
	/**
	 * @return the line where the corresponding {@code //mutGenLimit} is for a guard mutation, if {@code isGuardMutation()} or {@code isOneLineInMethodOp()} return {@code false}, then this method will return {@code -1} : {@code int}
	 */
	public int getMutGenLimitLine() {
		return this.affectedLine;
	}

	private boolean checkOp(Mutant mutOp) {
		boolean res = false;
		switch(mutOp) {
			//--------BASIC OPS-----------------
			case AODS 			:;
			case AODU 			:;
			case AOIS 			:;
			case AOIU 			:;
			case AORB 			:;
			case AORS 			:;
			case AORU 			:;
			case ASRS 			:;
			case COD 			:;
			case COI			:;
			case COR			:;
			case LOD			:;
			case LOI			:;
			case LOR			:;
			case ROR			:;
			case SOR			: {res = true; break;}
			//----------------------------------
			
			//--------ADV OPS-------------------
			case AMC			:;
			case EAM			:;
			case EMM			:;
			case IHD			:;
			case IHI			:;
			case IOD			:;
			case IOP			:;
			case IPC			: {res = false; break;} //this one deletes a line
			
			case EOA			:;
			case EOA_DUMB		:;
			case EOA_STRICT		:;
			case EOC			:;
			case EOC_SMART		:;
			case ISD			:;
			case ISD_SMART		:;
			case ISI			:;
			case ISI_SMART		: {res = true; break;}
			
			case JDC			:;
			case JID			:;
			case JSD			:;
			case NPER			:;
			case JSI			: {res = false; break;}
			
			case JTD			:;
			case JTI			:;
			case JTI_SMART		:;
			case OAN			:;
			case OAN_RELAXED	: {res = true; break;}
			
			case OMR			: {res = false; break;}
			
			case PCC			:;
			case PCD			:;
			case PCI			:;
			case PMD			:;	//only when mutating methods
			case PNC			:;  //only when mutating methods
			case PRVOR			:;
			case PRVOR_SMART	:;
			case PRVOR_REFINED	:;
			case PRVOL			:;
			case PRVOL_SMART	:;
			case PRVOU			:;
			case PRVOU_SMART	:;
			case PRVOU_REFINED	: {res = true; break;}
			
			case PPD			: {res = false; break;}
			//----------------------------------
			case MULTI			: {res = false; break;}

		}
		return res;
	}
	
	private boolean mutationAffectsGuardExpression(ParseTreeObject original) {
		ParseTreeObject current = original;
		boolean isGuardMutation = false;
		boolean search = true;
		while (search) {
			if (current instanceof IfStatement && !(original instanceof IfStatement)) {
				isGuardMutation = true;
				search = false;
			} else if (current instanceof WhileStatement && !(original instanceof WhileStatement)) {
				isGuardMutation = true;
				search = false;
			} else if (current instanceof DoWhileStatement && !(original instanceof DoWhileStatement)) {
				isGuardMutation = true;
				search = false;
			} else if (current == null || (current instanceof Statement)) {
				search = false;
			} else {
				current = current.getParent();
			}
		}
		return isGuardMutation;
	}
	
	private boolean mutationAffectsAssignmentStatement(ParseTreeObject original) {
		ParseTreeObject current = original;
		boolean isAssignmentMutation = false;
		//boolean isPRVOL = this.mutOp == Mutant.PRVOL || this.mutOp == Mutant.PRVOL_SMART;
		//boolean isPRVOR = this.mutOp == Mutant.PRVOR || this.mutOp == Mutant.PRVOR_SMART || this.mutOp == Mutant.PRVOR_REFINED;
		boolean search = true;//isPRVOL || isPRVOR;
		while (search) {
			if (current instanceof AssignmentExpression && !(original instanceof AssignmentExpression)) {
				isAssignmentMutation = true;
				search = false;
			} else if (current == null || (current instanceof Statement)) {
				search = false;
			} else {
				current = current.getParent();
			}
		}
		return isAssignmentMutation;
	}
	
	private boolean mutationAffectsVariableDeclarationStatement(ParseTreeObject original) {
		ParseTreeObject current = original;
		boolean isVariableDeclarationMutation = false;
		//boolean isPRVOU = this.mutOp == Mutant.PRVOU || this.mutOp == Mutant.PRVOU_SMART || this.mutOp == Mutant.PRVOU_REFINED;
		boolean search = true;//isPRVOU;
		while (search) {
			if (current instanceof VariableDeclaration && !(original instanceof VariableDeclaration)) {
				isVariableDeclarationMutation = true;
				search = false;
			} else if (current == null || (current instanceof Statement)) {
				search = false;
			} else {
				current = current.getParent();
			}
		}
		return isVariableDeclarationMutation;
	}
	
	/**
	 * @return {@code true} if the mutation affects only one line inside a method declaration : {@code boolean}
	 */
	public boolean isOneLineInMethodOp() {
		return this.oneLineInMethodOp;
	}
	
	/**
	 * @return {@code true} if the mutation affects a guard expression : {@code boolean}
	 */
	public boolean isGuardMutation() {
		return this.isGuardMutation;
	}
	
	/**
	 * @return {@code true} if the mutation affects a variable declaration : {@code boolean}
	 */
	public boolean isVariableDeclarationMutation() {
		return this.isVariableDeclarationMutation;
	}
	
	/**
	 * @return {@code true} if the operator used is one of the {@code PRVOL} or {@code PRVOR} family and the mutation affects an assignment expression : {@code boolean}
	 */
	public boolean isAssignmentMutation() {
		return this.isAssignmentMutation;
	}
	
	/**
	 * @return {@code true} if the operator used is one of the {@code PRVOL} family and {@code isAssignmentMutation()} returns {@code true} : {@code boolean}
	 */
	public boolean isAssignmentMutationLeft() {
		return this.mutatesLeftSideOfAssignment;
	}
	
	/**
	 * @return {@code true} if {@code isAssignmentMutationLeft()} returns {@code true} and the left side of the mutated assignment is a Variable : {@code boolean}
	 */
	public boolean isAssignmentMutationLeftVariable() {
		return this.leftSideOfAssignmentIsVariable;
	}
	
	/**
	 * @return {@code true} if {@code isAssignmentMutationLeft()} returns {@code true} and the left side of the mutated assignment is a FieldAccess : {@code boolean}
	 */
	public boolean isAssignmentMutationLeftFieldAccess() {
		return this.leftSideOfAssignmentIsFieldAccess;
	}
	
	/**
	 * @return {@code true} if {@code isAssignmentMutationLeft()} returns {@code true} and the left side of the mutated assignment is a ArrayAccess : {@code boolean}
	 * <hr>
	 * <b> note: <i>an array access can be a field access e.g.: {@code m.a[]} or not, e.g.: {@code a[]}</i></b>
	 */
	public boolean isAssignmentMutationLeftArrayAccess() {
		return this.leftSideOfAssignmentIsArrayAccess;
	}
	
	/**
	 * @return {@code true} if the operator used is one of the {@code PRVOR} family and {@code isAssignmentMutation() || isVariableDeclarationMutation()} returns {@code true} : {@code boolean}
	 */
	public boolean isAssignmentMutationRight() {
		return this.mutatesRightSideOfAssignment;
	}

	/**
	 * @return the mutation operator used to generate this mutation : {@code Mutant}
	 */
	public Mutant getMutOp() {
		return mutOp;
	}

	/**
	 * @return the original AST node affected by this mutation : {@code ParseTreeObject}
	 */
	public ParseTreeObject getOriginal() {
		return original;
	}

	/**
	 * @return the mutated AST node generated by this mutation : {@code ParseTreeObject}
	 */
	public ParseTreeObject getMutant() {
		return mutant;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime;
		result = prime * result + ((mutOp == null) ? 0 : mutOp.hashCode());
		result = prime * result + ((mutant == null) ? 0 : mutant.hashCode());
		result = prime * result + ((original == null) ? 0 : original.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mutation other = (Mutation) obj;
		if (mutOp == null) {
			if (other.mutOp != null)
				return false;
		} else if (!mutOp.equals(other.mutOp))
			return false;
		if (mutant == null) {
			if (other.mutant != null)
				return false;
		} else if (!mutant.equals(other.mutant))
			return false;
		if (original == null) {
			if (other.original != null)
				return false;
		} else if (!original.equals(other.original))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + mutOp + ", " + original + ", " + mutant + ")";
	}
}
