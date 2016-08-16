package mujava.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
	}
	
	public static void setVerbose(boolean enable) {
		verbose = enable;
	}

	private static MutantsInformationHolder mainHolder =
			new MutantsInformationHolder();
	
	private List<Mutation> mutantsIdentifiers =
			new ArrayList<Mutation>();
	
	private CompilationUnit compUnit;
	
	public void setCompilationUnit(CompilationUnit compUnit) {
		this.compUnit = compUnit;
	}

	public List<Mutation> getMutantsIdentifiers() {
		return mutantsIdentifiers;
	}

	public CompilationUnit getCompUnit() {
		return compUnit;
	}
	
	private boolean isStatement(ParseTreeObject o) {
		return o instanceof Statement;
	}
	
	private ParseTreeObject getClosestNodeToStatement(ParseTreeObject o) {
		if (isStatement(o)) return o;
		ParseTreeObject res = o;
		ParseTreeObject parent = res.getParent();
		while (parent != null && !isStatement(parent)) {
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
	
	private String nodeRepresentation(ParseTreeObject o) {
		ParseTreeObject statement = getClosestNodeToStatement(o);
		ParseTreeObject declaration = getEnclosingDeclaration(o);
		String declarationProfile = declaration.toFlattenString().split("\\{")[0];
		String statementAsString = statement.toFlattenString();
		return statementAsString + "(distance to statement " + distanceToEnclosingDeclaration(o) + ")" + " from " + declarationProfile;
	}

	public void addMutation(MutationOperator mutOp, ParseTreeObject original, ParseTreeObject mutant) {
		if (!MutantsInformationHolder.usePrototypeChecking && isEqualToOriginal(original, (ParseTreeObject)mutant)) return;
		
		if (MutantsInformationHolder.usePrototypeChecking) {
			String mutRep = nodeRepresentation(mutant);
			String origRep = nodeRepresentation(original);
			if (verbose) {
				System.out.println("Prototype checking");
				System.out.println("orig: " + origRep);
				System.out.println("mut: " + mutRep);
			}
			if (mutRep.equals(origRep)) {
				return;
			}
			if (MutantsInformationHolder.generatedMutations.contains(mutRep)) {
				return;
			} else if (!MutantsInformationHolder.generatedMutations.contains(origRep)) {
				MutantsInformationHolder.generatedMutations.add(origRep);
			}
			MutantsInformationHolder.generatedMutations.add(mutRep);
		} else {
			if (alreadyGenerated(original, (ParseTreeObject)mutant)) return;
		}
		
		
		if (verbose) {
			System.out.println("(" + mutOp.toString() + " | " + original.toFlattenString() + " ==> " + mutant.toFlattenString());
		}
		mutantsIdentifiers.add(new Mutation(mutOp, original, mutant));
	}
	
	public void clear() {
		this.mutantsIdentifiers = new ArrayList<Mutation>();
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
				+ mutantsIdentifiers + ", compUnit=" +
				((compUnit != null) ? compUnit.hashCode() : "null") + "]";
	}
	
}
