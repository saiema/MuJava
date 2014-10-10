package mujava.api;

import java.io.File;
import java.io.PrintWriter;
import java.util.Set;
import java.util.Stack;

import mujava.MutationSystem;
import mujava.NotDirBasedMutantsGenerator;
import mujava.OpenJavaException;
import mujava.util.Debug;
import openjava.mop.OJSystem;
import openjava.ptree.CompilationUnit;
import openjava.ptree.ParseTreeException;

/**
 * This is an API added to muJava's implementation so as to be able to obtain
 * mutants programmatically without having them outputed in the structured
 * directory created by muJava.
 * 
 * In order to avoid changing a huge amount of muJava's codebase this API is
 * created (with horrible code inside) so as to minimize code intervention
 * (but some minor changes in muJava's codebase had to be done nonetheless).
 */
public class Api {

	private static boolean usingApi = false;
	
	private static String methodToConsider;
	
	private static boolean cleanOJSystemBeforeGenerating = true;
	
	/**
	 * This field defines if the mutations are to be performed on an inner class
	 * 
	 * if this is empty then the mutated class is the defined originally, if not
	 * then the inner class defined here will be mutated
	 * @TODO: fix this comment
	 */
	private static Stack<String> innerClass = new Stack<String>();
	
	/**
	 * Generates mutants for the given java file, considering only the method
	 * name and mutant operators passed as parameters. It is important to note
	 * that all compiled classed have to be in the CLASSPATH. For example, the
	 * "bin" directory containing the compiled java file as well as other
	 * necessary classes should be in the CLASSPATH.
	 * 
	 * @param javaFile			:	the java source file from which mutants will be generated		:	{@code File}
	 * @param className			:	the qualified name of the class to consider						:	{@code String}
	 * @param methodToConsider	:	the method in which mutants operators will be applied			:	{@code String}
	 * @param mutOps			:	the mutations operators to apply								:	{@code Set<Mutant>}
	 * @return a {@code MutantsInformationHolder} object that holds the generated mutations and the {@code CompilationUnit} associated with {@code javaFile}	:	{@code MutantsInformationHolder}
	 * @see MutantsInformationHolder
	 * @throws OpenJavaException if some exception occurs while parsing the given java file
	 */
	public static MutantsInformationHolder generateMutants(File javaFile, String className, String methodToConsider, Set<Mutant> mutOps) throws OpenJavaException {
		usingApi = true;
		parseClassName(className);
		if (cleanOJSystemBeforeGenerating) OJSystem.clean();
		Api.methodToConsider = methodToConsider;
		Debug.setDebugLevel(0);
		NotDirBasedMutantsGenerator gen = new NotDirBasedMutantsGenerator(javaFile, mutOps);
		MutationSystem.CLASS_NAME = className;
		gen.makeMutants();
		MutantsInformationHolder ret = MutantsInformationHolder.mainHolder();
		MutantsInformationHolder.resetMainHolder();
		return ret;
	}
	
	/**
	 * Writes a mutant which is the result of applying a mutation to {@code CompilationUnit} that represent a java AST
	 * 
	 * @param source	:	a {@code CompilationUnit} that represents a java file AST								:	{@code CompilationUnit}
	 * @param mutation	:	a tuple containing the original AST node, the mutated AST node and the operator used	:	{@code Mutation}
	 * @param output 	:	the writer that will be used to output the resulting mutant								:	{@code PrintWriter}
	 * @return the number of the line mutated	:	{@code int}
	 * @throws ParseTreeException
	 */
	public static int writeMutant(CompilationUnit source, Mutation mutation, PrintWriter output) throws ParseTreeException{
		usingApi = true;
		MutantIdentifierWriter writer = new MutantIdentifierWriter(source, output);
		return writer.write(mutation);
	}

	public static boolean usingApi() {
		return usingApi;
	}

	public static String getMethodUnderConsideration() {
		return methodToConsider;
	}
	
	private static void parseClassName(String className) {
		if (className != null && !className.isEmpty()) {
			String[] splitAtDollar = className.split("&");
			for (int is = splitAtDollar.length - 1; is >= 0 ; is--) {
				Api.innerClass.push(splitAtDollar[is]);
			}
		}
	}
	
	public static void enterInnerClass(String ic) {
		if (!Api.innerClass.isEmpty() && Api.innerClass.peek().compareTo(ic)==0) {
			Api.innerClass.pop();
		}
	}
	
	public static boolean insideClassToMutate() {
		return Api.innerClass.isEmpty();
	}
	
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++added (06/10/14) [simon]
	public static void cleanOJSystemBeforeGenerating(boolean b) {
		cleanOJSystemBeforeGenerating = b;
	}
	// -----------------------------------------------------------
	
	//The following methods are used when there's need bypass the method under consideration check
	
	public static void enableApi() {
		usingApi = true;
	}
	
	public static void disableApi() {
		usingApi = false;
	}

}
