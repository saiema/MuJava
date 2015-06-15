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
	
	public static final String USE_MUTGENLIMIT = "api_use_mutgenlimit";

	private static boolean usingApi = false;
	
	private static boolean enableClassesVerification = true;
	
	private static String methodToConsider;
	
	private static boolean cleanOJSystemBeforeGenerating = true;
	
	private static String currentFilePath;
	
	/**
	 * This field defines if the mutations are to be performed on an inner class
	 * 
	 * if this is empty then the mutated class is the defined originally, if not
	 * then the inner class defined here will be mutated
	 * @TODO: fix this comment
	 */
	private static Stack<String> expectedClasses = new Stack<String>();
	
	private static Stack<String> visitedClasses = new Stack<String>();
	
	private static int expectedVisitedClasses;
	
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
		Api.currentFilePath = javaFile.getAbsolutePath();
		NotDirBasedMutantsGenerator gen = new NotDirBasedMutantsGenerator(javaFile, mutOps);
		MutationSystem.CLASS_NAME = Api.getMainClassName(className);
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

	/**
	 * This method is used in several parts of muJava code to do additional checks if the api is in use
	 * 
	 * @return if the Api is currently in use	:	{@code boolean}
	 */
	public static boolean usingApi() {
		return usingApi;
	}

	/**
	 * @return the method to mutate	:	{@code String}
	 * <hr>
	 * <b>note: this just returns a method's name, it doesn't differentiate between methods with the same name but different signature</b>
	 */
	public static String getMethodUnderConsideration() {
		return methodToConsider;
	}
	
	/*
	 * This method takes a class name, splits the name with &
	 * and pushes every name into innerClass
	 * 
	 * e.g.: for a&b&c it will split with & and generate [a, b, c]
	 * and will push the names as [c, b, a]
	 */
	private static void parseClassName(String className) {
		Api.expectedVisitedClasses = 0;
		Api.expectedClasses.clear();
		Api.visitedClasses.clear();
		if (className != null && !className.isEmpty()) {
			String[] splitAtDollar = className.split("&");
			for (int is = splitAtDollar.length - 1; is >= 0 ; is--) {
				Api.expectedClasses.push(splitAtDollar[is]);
			}
			Api.expectedVisitedClasses = splitAtDollar.length;
		}
	}
	
	/**
	 * @return the class to load, regardless of the class to mutate
	 */
	public static String getMainClassName(String className) {
		String[] splitAtDollar = className.split("&");
		return splitAtDollar[0];
	}
	
	/**
	 * This method takes a class name and checks if that name is on top of the stack of {@code Api#innerClass}
	 * and if it does then it will pop the value at the top of the stack 
	 * 
	 * @param ic	:	the name to check	:	{@code String}
	 */
	public static boolean enterInnerClass(String ic) {
		if (!enableClassesVerification) return true;
		Api.visitedClasses.push(ic);
		if (!Api.expectedClasses.isEmpty() && Api.expectedClasses.peek().compareTo(ic)==0) {
			Api.expectedClasses.pop();
			return true;
		}
		return false;
	}
	
	/**
	 * pushes a class name to {@code Api.innerClass} stack
	 * 
	 * @param ic	:	the class name to push	:	{@code String}
	 */
	public static void leaveInnerClass(String ic, boolean pushExpectedClass) {
		if (!enableClassesVerification) return;
		if (pushExpectedClass) Api.expectedClasses.push(ic);
		if (!Api.visitedClasses.isEmpty()) Api.visitedClasses.pop();
	}
	
	/**
	 * @return {@code true} if {@code Api.innerClass} is empty
	 */
	public static boolean insideClassToMutate() {
		return Api.expectedClasses.isEmpty() && Api.visitedClasses.size() == Api.expectedVisitedClasses;
	}
	
	/**
	 * Sets whether or not {@code OJSystem#clean()} will be called when calling {@code Api#generateMutants(File, String, String, Set<Mutant>)}
	 * 
	 * @param b	:	whether or not to call {@code OJSystem#clean()}	:	{@code boolean}
	 */
	public static void cleanOJSystemBeforeGenerating(boolean b) {
		cleanOJSystemBeforeGenerating = b;
	}
	
	//The following methods are used when there's need bypass the method under consideration check
	
	public static void enableApi() {
		usingApi = true;
	}
	
	public static void disableApi() {
		usingApi = false;
	}
	
	public static void disableClassesVerification() {
		enableClassesVerification = false;
	}
	
	public static void enableClassesVerification() {
		enableClassesVerification = true;
	}

	public static String getCurrentFilePath() {
		return Api.currentFilePath;
	}
	
	public static boolean useMutGenLimit() {
		if (Configuration.argumentExist(USE_MUTGENLIMIT)) {
			return (Boolean) Configuration.getValue(USE_MUTGENLIMIT);
		}
		return true;
	}
	
}
