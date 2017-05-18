package mujava.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import mujava.api.Mutation.PRIORITY;
import openjava.ptree.AllocationExpression;
import openjava.ptree.CompilationUnit;
import openjava.ptree.ExpressionList;
import openjava.ptree.Literal;
import openjava.ptree.MemberDeclaration;
import openjava.ptree.ParseTree;
import openjava.ptree.ParseTreeObject;
import openjava.ptree.Statement;
import openjava.ptree.StatementList;

/**
 * This is the information returned by the generateMutants method in the Api
 * class. It holds all the  mutation operations applicable to the original
 * source passed as parameter to the generateMutants method
 * @See {@link Mutation}
 */
public class MutantsInformationHolder {
	private static boolean verbose = false;
	private static Set<String> generatedMutations = new TreeSet<>();
	private static Set<String> generatedMutants = new TreeSet<>(); //keep record of generated mutants to avoid duplicates
	private static boolean usePrototypeChecking = false;
	
	private static Map<String,Set<String>> mutatedNodes = new HashMap<String, Set<String>>();
	private static String getParentAsString(ParseTreeObject object) {
		String objectAsString = object.toFlattenString();
		String objectParentAsString = "null";
		ParseTreeObject parent = object.getParent();
		while (parent != null) {
			//System.out.println("getParentAsString : " + (parent==null?"null":parent.toFlattenString()));
			objectParentAsString = parent.toFlattenString();
			if (objectParentAsString.compareTo(objectAsString) != 0) {
				objectParentAsString += " " + parent.getObjectID();
				break;
			}
			parent = parent.getParent();
		}
		
		return objectParentAsString;
	}
	
	private static boolean alreadyGenerated(ParseTreeObject original, ParseTreeObject mutant) {
		String originalAsString = original.toFlattenString() + " " + original.getObjectID();
		String originalParentAsString = getParentAsString(original);
		String originalPlusParentAsString = originalAsString + " from " + originalParentAsString;
		String mutantAsString = mutant.toFlattenString();
		Set<String> mutantsGenerated = mutatedNodes.get(originalPlusParentAsString);
		if (mutantsGenerated == null) {
			if (isOneArgumentAllocationToOneArgumentAllocation(original, mutant)) {
				ParseTreeObject origArg = (ParseTreeObject) ((AllocationExpression)original).getArguments().get(0);
				ParseTreeObject mutArg = (ParseTreeObject) ((AllocationExpression)mutant).getArguments().get(0);
				return alreadyGenerated(origArg, mutArg);
			}
			if (verbose) System.out.println("mutant node " + mutantAsString + " was not already generated for " + originalPlusParentAsString);
			mutantsGenerated = new HashSet<String>();
			mutantsGenerated.add(mutantAsString);
			mutatedNodes.put(originalPlusParentAsString, mutantsGenerated);
			return false;
		} else {
			if (mutantsGenerated.contains(mutantAsString)) {
				if (verbose) System.out.println("mutant node " + mutantAsString + " was already generated for " + originalPlusParentAsString);
				return true;
			} else {
				if (isOneArgumentAllocationToOneArgumentAllocation(original, mutant)) {
					ParseTreeObject origArg = (ParseTreeObject) ((AllocationExpression)original).getArguments().get(0);
					ParseTreeObject mutArg = (ParseTreeObject) ((AllocationExpression)mutant).getArguments().get(0);
					return alreadyGenerated(origArg, mutArg);
				}
				if (verbose) System.out.println("mutant node " + mutantAsString + " was not already generated for " + originalPlusParentAsString);
				mutantsGenerated.add(mutantAsString);
				return false;
			}
		}
	}
	
	private static boolean isOneArgumentAllocationToOneArgumentAllocation(ParseTreeObject orig, ParseTreeObject mut) {
		if (orig instanceof AllocationExpression && mut instanceof AllocationExpression) {
			ExpressionList origArgs = ((AllocationExpression)orig).getArguments();
			ExpressionList mutArgs = ((AllocationExpression)mut).getArguments();
			return origArgs.size() == 1 && mutArgs.size() == 1;
		} else {
			return false;
		}
	}
	
	private static boolean isEqualToOriginal(ParseTreeObject original, ParseTreeObject mutant) {
		if (bothLiterals(original, mutant)) {
			return sameLiterals((Literal) original, (Literal) mutant);
		}
		String originalAsString = original.toFlattenString();
		String originalParentAsString = getParentAsString(original);
		String originalPlusParentAsString = originalAsString + " from " + originalParentAsString;
		String mutantAsString = mutant.toFlattenString();
		String mutantParentAsString = getParentAsString(mutant);
		String mutantPlusParentAsString = mutantAsString + " from " + mutantParentAsString;
		//++++++++++++++++++++++++++++++++++++++++++
		//++++++++++++++++null for null special case
		if (originalAsString.trim().compareTo("null") == 0 && mutantAsString.trim().compareTo("null") == 0) {
			if (verbose) System.out.println("ignoring mutation changing null for null");
			return true;
		}
		//------------------------------------------
		if (verbose) System.out.println("\toriginal: "+originalPlusParentAsString+"\n\tmutant: "+mutantPlusParentAsString);
		boolean equalToOriginal = originalPlusParentAsString.trim().compareTo(mutantPlusParentAsString.trim()) == 0;
		if (equalToOriginal && verbose) System.out.println("mutant " + mutantPlusParentAsString + " equal to original");
		return equalToOriginal;
	}
	
	private static boolean bothLiterals(ParseTreeObject original, ParseTreeObject mutant) {
		if (original instanceof Literal && mutant instanceof Literal) {
			return true;
		}
		return false;
	}
	
	private static boolean sameLiterals(Literal original, Literal mutant) {
		if (original.getLiteralType() == mutant.getLiteralType()) {
			return original.toString().compareTo(mutant.toString()) == 0;
		} else {
			return false;
		}
	}
	
	private static void clean() {
		mutatedNodes = new HashMap<String, Set<String>>();
		generatedMutants = new TreeSet<>();
		generatedMutations = new TreeSet<>();
	}
	
	public static void setVerbose(boolean enable) {
		verbose = enable;
	}

	private static MutantsInformationHolder mainHolder = new MutantsInformationHolder();
	
	private List<Mutation> mutantsIdentifiers = new ArrayList<Mutation>();
	private List<Mutation> lowPriorityMutantsIndentifiers = new ArrayList<>();
	
	private CompilationUnit compUnit;
	
	public void setCompilationUnit(CompilationUnit compUnit) {
		this.compUnit = compUnit;
	}

	/**
	 * This method will merge normal, low, and neutral priority mutations
	 * @return all the generated mutations
	 */
	public List<Mutation> getMutantsIdentifiers() {
		mutantsIdentifiers.addAll(0, lowPriorityMutantsIndentifiers);
		lowPriorityMutantsIndentifiers.clear();
		return mutantsIdentifiers;
	}

	public CompilationUnit getCompUnit() {
		return compUnit;
	}
	
	private boolean isStatement(ParseTreeObject o) {
		return o instanceof Statement;
	}
	
	/**
	 * This method will return either a Statement containing the node or
	 * the higher node that have either a null parent or is a MemberDeclaration
	 * @param o
	 * @return
	 */
	private ParseTreeObject getClosestNodeToStatement(ParseTreeObject o) {
		if (isStatement(o)) return o;
		ParseTreeObject res = o;
		ParseTreeObject parent = res.getParent();
		while (parent != null && !isStatement(parent) && !(res instanceof MemberDeclaration)) {
			res = parent;
			parent = parent.getParent();
		}
		return res;
	}
	
	private ParseTreeObject getEnclosingDeclaration(ParseTreeObject o) {
		ParseTreeObject res = o;
		while (res != null && !(res instanceof MemberDeclaration)) {
			res = res.getParent();
		}
		return res;
	}
	
	private boolean isSameObject(ParseTree p, ParseTree q) {
		boolean same = false;
		if (p == null && q == null)
			same = true;
		else if (p == null || q == null)
			same = false;
		else
			same = (p.getObjectID() == q.getObjectID());
		return same;
	}
	
	private int distanceToEnclosingDeclaration(ParseTreeObject o) {
		ParseTreeObject curr = o;
		int distance = 0;
		while (curr != null && !(curr instanceof MemberDeclaration)) {
			if (curr instanceof Statement && curr.getParent() != null && curr.getParent() instanceof StatementList) {
				Statement st = (Statement) curr;
				StatementList stList = (StatementList) curr.getParent();
				int s = 0;
				while (s < stList.size()) {
					//System.out.println(s);
					if (isSameObject(stList.get(s), st)) {
						break;
					} else {
						s++;
					}
				}
				distance += s;
			}
			curr = curr.getParent();
			distance++;
		}
		return distance;
	}
	
	private String nodeRepresentation(ParseTreeObject o, ParseTreeObject m, boolean original) {
		ParseTreeObject obj = original?o:m;
		ParseTreeObject statement = getClosestNodeToStatement(obj);
		ParseTreeObject declaration = getEnclosingDeclaration(obj);
		String declarationProfile = declaration.toFlattenString().split("\\{")[0];
		String statementAsString = original?statement.toString():statement.toString(o, m);
		return statementAsString + " (closest node id : " + statement.getObjectID() + ")" +  " from " + declarationProfile;
		//return statementAsString + " (distance to statement " + distanceToEnclosingDeclaration(o) + ")" + " from " + declarationProfile;
	}
	
	private String mutantRepresentation(ParseTreeObject o, ParseTreeObject m) {
		ParseTreeObject statement = getClosestNodeToStatement(o);
		ParseTreeObject declaration = getEnclosingDeclaration(o);
		String declarationProfile = declaration.toFlattenString().split("\\{")[0];
		String statementAsString = statement.toString(o, m);
		return statementAsString + " (closest node id : " + statement.getObjectID() + ")" +  " from " + declarationProfile;
	}
	
	public void addMutation(MutationOperator mutOp, ParseTreeObject original, ParseTreeObject mutant, PRIORITY p) {
		if (p.compareTo(PRIORITY.LOW) == 0 && discardLow()) {
			if (verbose) System.out.println("low priority mutation discarded : (" + mutOp.toString() + " | " + original.toFlattenString() + " ==> " + mutant.toFlattenString());
			return;
		}
		if (p.compareTo(PRIORITY.NEUTRAL) == 0 && discardNeutral()) {
			if (verbose) System.out.println("neutral priority mutation discarded : (" + mutOp.toString() + " | " + original.toFlattenString() + " ==> " + mutant.toFlattenString());
			return;
		}
		if (!MutantsInformationHolder.usePrototypeChecking && isEqualToOriginal(original, (ParseTreeObject)mutant)) return;
		
		if (MutantsInformationHolder.usePrototypeChecking) {
			String mutRep = nodeRepresentation(original, mutant, false);
			String origRep = nodeRepresentation(original, mutant, true);
			if (verbose) {
				System.out.println("Prototype checking");
				System.out.println("orig: " + origRep);
				System.out.println("mut: " + mutRep);
			}
			if (mutRep.equals(origRep)) {
				return;
			}
			if (MutantsInformationHolder.generatedMutations.contains(mutRep)) {
				if (verbose) {
					System.out.println("mutant " + mutRep + " already generated");
				}
				return;
			} else if (!MutantsInformationHolder.generatedMutations.contains(origRep)) {
				if (verbose) {
					System.out.println(original.toFlattenString() + " ==(" + mutOp.toString() + ")==> " + mutant.toFlattenString());
				}
				MutantsInformationHolder.generatedMutations.add(origRep);
			}
			MutantsInformationHolder.generatedMutations.add(mutRep);
		} else {
			if (alreadyGenerated(original, (ParseTreeObject)mutant)) return;
			////++++++++++++++new check to avoid repeated mutants++++++++++++++(WIP)
			String mutantAsString = mutantRepresentation(original, mutant);
			if (MutantsInformationHolder.generatedMutants.contains(mutantAsString)) {
				if (MutantsInformationHolder.verbose) System.out.println("Mutant " + mutantAsString + " was already generated");
				return;
			} else {
				MutantsInformationHolder.generatedMutants.add(mutantAsString);
				if (MutantsInformationHolder.verbose) System.out.println("Mutant " + mutantAsString + " was not already generated");
			}
			//----------------------------------------------------------------------
		}
		
		
		if (verbose) {
			System.out.println("priority " + p + " (" + mutOp.toString() + " | " + original.toFlattenString() + " ==> " + mutant.toFlattenString());
		}
		
		Mutation newMutation = new Mutation(mutOp, original, mutant, p);
		if (p.compareTo(PRIORITY.LOW) == 0 || p.compareTo(PRIORITY.NEUTRAL) == 0) {
			lowPriorityMutantsIndentifiers.add(newMutation);
		} else 
			mutantsIdentifiers.add(newMutation);
	}
	
	private boolean discardLow() {
		if (Configuration.argumentExist(Configuration.PRIORITY_LOW_DISCARD)) {
			return (Boolean) Configuration.getValue(Configuration.PRIORITY_LOW_DISCARD);
		}
		return false;
	}
	
	private boolean discardNeutral() {
		if (Configuration.argumentExist(Configuration.PRIORITY_NEUTRAL_DISCARD)) {
			return (Boolean) Configuration.getValue(Configuration.PRIORITY_NEUTRAL_DISCARD);
		}
		return false;
	}

	public void addMutation(MutationOperator mutOp, ParseTreeObject original, ParseTreeObject mutant) {
		addMutation(mutOp, original, mutant, PRIORITY.NORMAL);
	}
	
	public void clear() {
		this.lowPriorityMutantsIndentifiers = new ArrayList<>();
		this.mutantsIdentifiers = new ArrayList<>();
		this.compUnit = null;
	}
	
	public static MutantsInformationHolder mainHolder() {
		return mainHolder;
	}

	public static void resetMainHolder() {
		mainHolder = new MutantsInformationHolder();
		clean();
	}
	
	public static void resetMainHolder(boolean keepMutationsInformation) {
		if (keepMutationsInformation) {
			mainHolder = new MutantsInformationHolder();
		} else {
			resetMainHolder();
		}
	}
	
	public static void usePrototypeChecking(boolean enable) {
		MutantsInformationHolder.usePrototypeChecking = enable;
	}

	@Override
	public String toString() {
		return "MutantsInformationHolder [mutantsIdentifiers="
				+ mutantsIdentifiers
				+ " | low priority mutantsIdentifiers="
				+ lowPriorityMutantsIndentifiers
				+ ", compUnit=" +
				((compUnit != null) ? compUnit.hashCode() : "null") + "]";
	}
	
}
