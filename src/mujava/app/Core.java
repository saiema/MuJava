package mujava.app;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import mujava.OpenJavaException;
import mujava.api.Mutant;
import mujava.api.MutantsInformationHolder;

/**
 * This class execute all commands either from console or from a GUI
 * and return the results to be handled by the caller
 * @author Simon Emmanuel Gutierrez Brida
 */
public class Core {
	public static String SEPARATOR = fixBackslash(FileSystems.getDefault().getSeparator());
	private static Core instance = null;
	private Mutator mutator = null;
	private static String inputDir;
	private static String outputDir;
	private String inputBinDir;
	private List<MutantsInformationHolder> mutantsInformationHolder;
	private HashMap<String, List<String>> mutantsFolders;
	private Exception error;
	private MutationScore ms;
	public static boolean fullVerbose = false;
	public static final int mujavappVersion = 2015300601;
	
	public static Core newInstance(String inputDirP, String outputDirP) {
		if (instance == null) {
			instance = new Core(inputDirP, outputDirP);
		}
		if ((inputDir == null && inputDirP != null) || (inputDir != null && !inputDir.equals(inputDirP))) {
			inputDir = inputDirP;
		}
		if ((outputDir == null && outputDirP != null) || (outputDir != null && !outputDir.equals(outputDirP))) {
			outputDir = outputDirP;
		}
		return instance;
	}
	
	private static String fixBackslash(String orig) {
		if (orig == "\\") {
			return "\\\\";
		} else {
			return orig;
		}
	}

	public void setMutationScore(MutationScore ms) {
		this.ms = ms;
	}
	
	public void setInputBinDir(String binDir) {
		this.inputBinDir = binDir;
	}
	
	private Core(String inputDir, String outputDir) {
		Core.inputDir = inputDir;
		Core.outputDir = outputDir;
	}
	
	public boolean generateMutants(String className, String[] methods, Mutant[] mutOps) {
		this.error = null;
		this.mutantsFolders = null;
		this.mutantsInformationHolder = null;
		if (methods == null) {
			Class<?> clazz = loadClass(className);
			if (clazz == null) {
				return false;
			} else {
				Method[] classMethods = clazz.getDeclaredMethods();
				methods = new String[classMethods.length];
				int i = 0;
				for (Method m : classMethods) {
					methods[i] = m.getName();
					i++;
				}
			}
		}
		MutationRequest mutationRequest = new MutationRequest(className.replaceAll("\\.", SEPARATOR), methods, mutOps, Core.inputDir, Core.outputDir);
		this.mutator = new Mutator(mutationRequest);
		try {
			this.mutator.generateMutants();
			this.mutantsInformationHolder = this.mutator.getMIH();
			this.mutantsFolders = this.mutator.mutantsFolders;
			this.mutator.resetMutantFolders();
		} catch (ClassNotFoundException e) {
			this.error = e;
		} catch (OpenJavaException e) {
			this.error = e;
		}
		return this.error == null;
	}
	
	public List<Method> getMethods(String className) {
		Class<?> clazz = loadClass(className);
		List<Method> methods = new LinkedList<Method>();
		if (clazz != null) {
			methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
		}
		return methods;
	}
	
	
	private Class<?> loadClass(String className) {
		File file = new File(this.inputBinDir);
		Class<?> clazz = null;
		String classToLoad = className;
		try {
		    URL url = file.toURI().toURL();
		    URL[] urls = new URL[]{url};
		    
		    URLClassLoader cl = new URLClassLoader(urls);

		    clazz = cl.loadClass(classToLoad);
		    cl.close();
		    
		} catch (MalformedURLException e) {
			this.error = e;
		} catch (ClassNotFoundException e) {
			this.error = e;
		} catch (IOException e) {
			this.error = e;
		}
		return clazz;
	}
	
	public Exception lastError() {
		return this.error;
	}
	
	public List<MutantsInformationHolder> lastMutantInformationHolder() {
		return this.mutantsInformationHolder;
	}
	
	public HashMap<String, List<String>> lastMutantsFolder() {
		return this.mutantsFolders;
	}
	
	public String getInputDir() {
		return Core.inputDir;
	}
	
	public String getOutputDir() {
		return Core.outputDir;
	}
	
	public float calculateMutationScore(String[] testClasses, String className) {
		int failedToCompile = 0;
		int mutantsKilled = 0;
		int mutants = 0;
		for (Entry<String, List<String>> entry : lastMutantsFolder().entrySet()) {
			for (String path : entry.getValue()) {
				mutants++;
				String pathToFile = entry.getKey()/*.replaceAll("\\_", SEPARATOR)*/ + SEPARATOR + path;
				if (!ms.compile(pathToFile + className.replaceAll("\\.", SEPARATOR)+".java")){
					System.out.println("File : " + Core.outputDir + pathToFile + className.replaceAll("\\.", SEPARATOR)+".java" + " didn't compile\n");
					failedToCompile++;
					continue;
				}
				boolean killed = false;
				List<Result> results = ms.runTestsWithMutants(Arrays.asList(testClasses), pathToFile, className);
				if (results == null) {
					System.out.println("An error ocurred while running tests for mutants");
					System.out.println(ms.getLastError()!=null?ms.getLastError().toString():"no exception to display, contact your favorite mujava++ developer");
					return -1;
				}
				for (Result r : results) {
					System.out.println("Runned : " + r.getRunCount() + " tests (pass : " + (r.getRunCount()-r.getFailureCount()) + " | failed : " + r.getFailureCount() + ")\n");
					if (!r.wasSuccessful()) {
						for (Failure f : r.getFailures()) {
							if (Core.fullVerbose) System.out.println("mutant : " + Core.outputDir + pathToFile + className.replaceAll("\\.", SEPARATOR)+".java");
							if (Core.fullVerbose) System.out.println("test : " + f.getTestHeader());
							if (Core.fullVerbose) System.out.println("failure description: " + f.getDescription());
							if (Core.fullVerbose && !(f.getException() instanceof java.lang.AssertionError)) System.out.println("exception: " + f.getException());
							if (Core.fullVerbose && !(f.getException() instanceof java.lang.AssertionError)) System.out.println("trace: " + f.getTrace());
						}
					}
					if (!killed && !r.wasSuccessful()) killed = true;
				}
				if (killed) mutantsKilled++;
			}
		}
		System.out.println("Mutants : "+ mutants + " | didn't compile : " + failedToCompile + " | mutants killed by tests : "+ mutantsKilled + " | surviving mutants : " + (mutants-failedToCompile-mutantsKilled) + " | mutation score : "+((mutantsKilled+failedToCompile)*100.0)/mutants+'\n');
		return ((mutantsKilled+failedToCompile)*(float)100.0)/mutants;
	}

}
