package mujava.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import openjava.ptree.CompilationUnit;
import openjava.ptree.Literal;
import openjava.ptree.ParseTreeObject;

/**
 * This is the information returned by the generateMutants method in the Api
 * class. It holds all the  mutation operations applicable to the original
 * source passed as parameter to the generateMutants method
 * @See {@link Mutation}
 */
public class MutantsInformationHolder {
	private static boolean verbose = false;
	private static Map<String,Set<String>> mutatedNodes = new HashMap<String, Set<String>>();
	private static String getParentAsString(ParseTreeObject object) {
		String objectAsString = object.toFlattenString();
		String objectParentAsString = "null";
		ParseTreeObject parent = object.getParent();
		while (parent != null) {
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
				if (verbose) System.out.println("mutant node " + mutantAsString + " was not already generated for " + originalPlusParentAsString);
				mutantsGenerated.add(mutantAsString);
				return false;
			}
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

	public void addMutation(Mutant mutOp, ParseTreeObject original, ParseTreeObject mutant) {
		if (isEqualToOriginal(original, (ParseTreeObject)mutant)) return;
		if (alreadyGenerated(original, (ParseTreeObject)mutant)) return;
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

	@Override
	public String toString() {
		return "MutantsInformationHolder [mutantsIdentifiers="
				+ mutantsIdentifiers + ", compUnit=" +
				((compUnit != null) ? compUnit.hashCode() : "null") + "]";
	}
	
}
