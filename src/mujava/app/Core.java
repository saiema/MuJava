package mujava.app;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.runner.notification.Failure;

import mujava.api.Configuration;
import mujava.api.Mutant;
import mujava.generations.GenerationsGoalTester;
import mujava.generations.GenerationsInformation;
import mujava.generations.Generator;
import mujava.generations.GoalTester;
import mujava.generations.RequestGenerator;
import mujava.generations.SameRequestGenerator;

/**
 * This class execute all commands either from console or from a GUI
 * and return the results to be handled by the caller
 * @author Simon Emmanuel Gutierrez Brida
 */
public class Core {
	
	/**
	 * Option to enable/disable toughness analysis, which is a relation of failed tests and total tests
	 * for each mutant.
	 * <p>
	 * this option is disabled by default
	 */
	public static String ENABLE_TOUGHNESS = "Core_toughness";
	
	public static String SEPARATOR = fixBackslash(FileSystems.getDefault().getSeparator());
	private static Core instance = null;
	private static String inputDir;
	private static String outputDir;
	private String inputBinDir;
	private Map<String, List<String>> mutantsFolders;
	private Exception error;
	private MutationScore ms;
	private int generation = -1 ;
	public static boolean fullVerbose = false;
	public static boolean showSurvivingMutants = false;
	public static final int mujavappVersion = 20151011;
	
	private float totalToughness;
	private float totalMutants;
	
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
		return generateMutants(className, methods, mutOps, 1);
	}
	
	public boolean generateMutants(String className, String[] methods, Mutant[] mutOps, int generation) {
		this.error = null;
		this.mutantsFolders = null;
		if (methods == null) {
			this.error = new IllegalArgumentException("mujava.app.Core#generateMutantsForGeneration: methods param is null");
			return false;
		}
		String classNameAsPath = className.replaceAll("\\.", SEPARATOR);
		MutationRequest originalRequest = new MutationRequest(classNameAsPath, methods, mutOps, Core.inputDir, Core.outputDir);
		GoalTester goalTester = new GenerationsGoalTester(generation);
		RequestGenerator requestGenerator = new SameRequestGenerator(originalRequest);
		Generator.useLowMemoryMode(true);
		Generator generator = new Generator(requestGenerator, goalTester, (Core.fullVerbose?Generator.VERBOSE_LEVEL.FULL_VERBOSE:Generator.VERBOSE_LEVEL.NO_VERBOSE));
		try {
			GenerationsInformation generationsInfo= generator.generate(false, true);
			if (Core.fullVerbose) System.out.println(generationsInfo.showBasicInformation());
			this.mutantsFolders = generator.getMutantsFolderForGeneration(generation);
			this.generation = generation;
		} catch (Exception e) {
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
	
	public Map<String, List<String>> lastMutantsFolder() {
		return this.mutantsFolders;
	}
	
	public String getInputDir() {
		return Core.inputDir;
	}
	
	public String getOutputDir() {
		return Core.outputDir;
	}
	
	public float calculateMutationScore(String[] testClasses, String className) {
		List<String> survivingMutantsPaths = new LinkedList<>();
		int failedToCompile = 0;
		int mutantsKilled = 0;
		int mutants = 0;
		int timedOut = 0;
		for (Entry<String, List<String>> entry : lastMutantsFolder().entrySet()) {
			for (String path : entry.getValue()) {
				mutants++;
				String currGen = "generation-" + this.generation;
				String pathToFile = currGen + SEPARATOR + entry.getKey() + SEPARATOR + path;
				String fullPathToJavaFile = pathToFile + className.replaceAll("\\.", SEPARATOR)+".java"; 
				if (!ms.compile(fullPathToJavaFile)){
					System.out.println("File : " + Core.outputDir + pathToFile + className.replaceAll("\\.", SEPARATOR)+".java" + " didn't compile\n");
					failedToCompile++;
					continue;
				}
				boolean killed = false;
				List<TestResult> results = ms.runTestsWithMutants(Arrays.asList(testClasses), pathToFile, className);
				if (results == null) {
					System.out.println("An error ocurred while running tests for mutants");
					System.out.println(ms.getLastError()!=null?ms.getLastError().toString():"no exception to display, contact your favorite mujava++ developer");
					return -1;
				}
				for (TestResult r : results) {
					//System.out.println("Runned : " + r.getRunCount() + " tests (pass : " + (r.getRunCount()-r.getFailureCount()) + " | failed : " + r.getFailureCount() + ")\n");
					System.out.println(r.toString()+"\n");
					if (!r.wasSuccessful()) {
						if (r.getTimedoutTests() > 0) timedOut++;
						for (Failure f : r.getFailures()) {
							if (Core.fullVerbose || toughnessAnalysis()) System.out.println("mutant : " + Core.outputDir + pathToFile + className.replaceAll("\\.", SEPARATOR)+".java");
							if (toughnessAnalysis()) {
								float toughness = 1.0f - ((r.getTotalFailures() * 1.0f) / (r.getRunnedTestsCount() * 1.0f));
								this.addToughnessValue(toughness);
								System.out.println("Toughness: " + toughness + " [failed : " + r.getTotalFailures() + " | total : " + r.getRunnedTestsCount() + "]");
							}
							if (Core.fullVerbose) System.out.println("test : " + f.getTestHeader());
							if (Core.fullVerbose) System.out.println("failure description: " + f.getDescription());
							if (Core.fullVerbose && !(f.getException() instanceof java.lang.AssertionError)) System.out.println("exception: " + f.getException());
							if (Core.fullVerbose && !(f.getException() instanceof java.lang.AssertionError)) System.out.println("trace: " + f.getTrace());
						}
					}
					if (!killed && !r.wasSuccessful()) killed = true;
				}
				if (killed) mutantsKilled++;
				if (!killed && Core.showSurvivingMutants) {
					survivingMutantsPaths.add(fullPathToJavaFile);
				}
			}
		}
		System.out.println("Mutants : "+ mutants + " | didn't compile : " + failedToCompile + " | mutants killed by tests : "+ mutantsKilled + " | surviving mutants : " + (mutants-failedToCompile-mutantsKilled) + " | total tests that timedout : " + timedOut + " | mutation score : "+((mutantsKilled+failedToCompile)*100.0)/mutants+ " | mutation score (only compiling mutants) : " + (mutantsKilled*100.0)/(mutants-failedToCompile) + '\n');
		if (Core.showSurvivingMutants) {
			System.out.println("Surviving mutants paths:\n");
			for (String sm : survivingMutantsPaths) {
				System.out.println(sm);
			}
		}
		if (toughnessAnalysis()) {
			System.out.println("Average toughness : " + this.averageToughness());
		}
		return ((mutantsKilled+failedToCompile)*(float)100.0)/mutants;
	}
	
	public boolean toughnessAnalysis() {
		if (Configuration.argumentExist(ENABLE_TOUGHNESS)) {
			return (Boolean) Configuration.getValue(ENABLE_TOUGHNESS);
		} else {
			return false;
		}
	}
	
	private void addToughnessValue(float t) {
		this.totalToughness += t;
		this.totalMutants++;
	}
	
	private float averageToughness() {
		return this.totalToughness / this.totalMutants;
	}
	
	@SuppressWarnings("deprecation")
	public static void killStillRunningJUnitTestcaseThreads() {
	    Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
	    for (Thread thread : threadSet) {
	        if (!(thread.isDaemon())) {
	            final StackTraceElement[] threadStackTrace = thread.getStackTrace();
	            if (threadStackTrace.length > 1) {
	                StackTraceElement firstMethodInvocation = threadStackTrace[threadStackTrace.length - 1];
	                if (firstMethodInvocation.getClassName().startsWith("org.junit")) {
	                    // HACK: must use deprecated method
	                    thread.stop();
	                }
	            }
	        }
	    }
	}

}
