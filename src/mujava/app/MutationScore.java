package mujava.app;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import mujava.junit.runner.MuJavaJunitTestRunner;
import mujava.junit.runner.MuJavaTestRunnerException;
import mujava.loader.Reloader;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runners.model.InitializationError;

public class MutationScore {
	private static String mutantsSourceFolder = null;
	private static String originalBinFolder = null;
	private static String testsBinFolder = null;
	private static MutationScore instance = null;
	private Exception lastError = null;
	private static Reloader reloader;
	public static Set<String> allowedPackages = null;
	public static boolean quickDeath;
	public static boolean outputMutationsInfo = true;
	public static String junitPath;
	public static String hamcrestPath;
	
	public static MutationScore newInstance(String mutantsSourceFolder, String originalBinFolder, String testsBinFolder) {
		if (instance == null) {
			instance = new MutationScore(mutantsSourceFolder, originalBinFolder, testsBinFolder);
		} else {
			if ((MutationScore.mutantsSourceFolder == null && mutantsSourceFolder != null) || (MutationScore.mutantsSourceFolder != null && !MutationScore.mutantsSourceFolder.equals(mutantsSourceFolder))) {
				MutationScore.mutantsSourceFolder = mutantsSourceFolder;
			}
			if ((MutationScore.originalBinFolder == null && originalBinFolder != null) || (MutationScore.originalBinFolder != null && !MutationScore.originalBinFolder.equals(originalBinFolder))) {
				MutationScore.originalBinFolder = originalBinFolder;
			}
			if ((MutationScore.testsBinFolder == null && testsBinFolder != null) || (MutationScore.testsBinFolder != null && !MutationScore.testsBinFolder.equals(testsBinFolder))) {
				MutationScore.testsBinFolder = testsBinFolder;
			}
		}
		return instance;
	}
	
	private MutationScore(String mutantsBaseFolder, String originalClasspath, String testsBinFolder) {
		MutationScore.mutantsSourceFolder = mutantsBaseFolder;
		MutationScore.originalBinFolder = originalClasspath;
		MutationScore.testsBinFolder = testsBinFolder;
	}
	
	public CompilationResult compile(String path) {
		File fileToCompile = new File(/*mutantsSourceFolder+*/path);
		if (!fileToCompile.exists() || !fileToCompile.isFile() || !fileToCompile.getName().endsWith(".java")) {
			return new CompilationResult(new Exception("Error in file : " + fileToCompile.getAbsolutePath()));
		}
		File[] files = new File[]{fileToCompile};
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
		compiler.getTask(null, fileManager, diagnostics, Arrays.asList(new String[] {"-classpath", originalBinFolder}), null, compilationUnit).call();
		String compilationErrorOutput = "";
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
	        compilationErrorOutput += String.format("Error on line %d in %s%n%s%n",
								                    diagnostic.getLineNumber(),
								                    diagnostic.getSource().toUri(),
								                    diagnostic.getMessage(null));
		}
		CompilationResult cresult = null;
		if (compilationErrorOutput.isEmpty()) {
			cresult = new CompilationResult(null);
		} else {
			cresult = new CompilationResult(new Exception(compilationErrorOutput));
		}
		return cresult;
	}
	
	
	public List<Result> runTests(List<String> testClasses) {
		this.lastError = null;
		List<Result> testResults = new LinkedList<Result>();
		for (String test : testClasses) {
			Class<?> testToRun = loadClass(MutationScore.testsBinFolder, test);
			testResults.add(JUnitCore.runClasses(testToRun));
		}
		return testResults;
	}
	
	public List<TestResult> runTestsWithMutants(List<String> testClasses, MutantInfo mut) {
		this.lastError = null;
		if (MutationScore.reloader == null) {
			List<String> classpath = Arrays.asList(new String[]{MutationScore.originalBinFolder, MutationScore.testsBinFolder});
			MutationScore.reloader = new Reloader(classpath,Thread.currentThread().getContextClassLoader());
			MutationScore.reloader.markEveryClassInFolderAsReloadable(MutationScore.originalBinFolder, MutationScore.allowedPackages);
			MutationScore.reloader.markEveryClassInFolderAsReloadable(MutationScore.testsBinFolder, MutationScore.allowedPackages);
		}
		List<TestResult> testResults = new LinkedList<TestResult>();
		System.out.println("Testing mutant : "+mut.getPath()+'\n');
		if (MutationScore.outputMutationsInfo) {
			System.out.println(mut.toString());
		}
		for (String test : testClasses) {
			Class<?> testToRun;
			try {
				MutationScore.reloader = MutationScore.reloader.getLastChild();
				MutationScore.reloader.setSpecificClassPath(mut.getName(), mut.getClassRootFolder());//(className, MutationScore.mutantsSourceFolder+pathToMutant);
				testToRun = MutationScore.reloader.rloadClass(test, true);
				MuJavaJunitTestRunner mjTestRunner = new MuJavaJunitTestRunner(testToRun, MutationScore.quickDeath);
				Result testResult = mjTestRunner.run();
				Core.killStillRunningJUnitTestcaseThreads();
				testResults.add(new TestResult(testResult, testToRun));
				if (!testResult.wasSuccessful() && MutationScore.quickDeath) {
					break;
				}
			} catch (ClassNotFoundException | IllegalArgumentException | MuJavaTestRunnerException | InitializationError e) {
				e.printStackTrace();
				this.lastError = e;
			}
		}
		return testResults;
	}
	
	public ExternalJUnitResult runTestsWithMutantsUsingExternalRunner(List<String> testClasses, MutantInfo mut) {
		Exception error = null;
		List<TestResult> testResults = new LinkedList<>();
		String classpath = ".:bin/:"+MutationScore.originalBinFolder+":"+MutationScore.testsBinFolder;
		classpath += ":"+junitPath+":"+hamcrestPath;
		classpath += ":"+getCurrentClasspath();
		String mutPath = mut.getClassRootFolder();
		//ProcessBuilder args are:
		//command + binFolder + testsBinFolder + tests + mutantLocation + mutantClass + quickDeath + toughness
		//where except for the command, the other arguments need a flag, which are the following:
		//-b -t -T -m -c -q -x
		//-q and -t will only be used if quickdeath or toughness where set
		int optionalFlags = (MutationScore.quickDeath || Core.toughnessAnalysis())?1:0;
		String[] args = new String[testClasses.size() /*tests*/ + 5 /*flags*/ + 4 /*args*/ + 4 /*command*/ + optionalFlags];
		args[0] = "java";
		args[1] = "-cp";
		args[2] = classpath;
		args[3] = "mujava.junit.runner.ExternalJUnitTestRunner";
		args[4] = "-b";
		args[5] = MutationScore.originalBinFolder;
		args[6] = "-t";
		args[7] = MutationScore.testsBinFolder;
		args[8] = "-T";
		int i = 1;
		for (String t : testClasses) {
			args[8+i] = t;
			i++;
		}
		args[8+i] = "-m";
		args[9+i] = mutPath;
		args[10+i] = "-c";
		args[11+i] = mut.getName();
		if (optionalFlags != 0) {
			if (MutationScore.quickDeath && !Core.toughnessAnalysis()) {
				args[12+i] = "-q";
			} else if (Core.toughnessAnalysis()) {
				args[12+i] = "-x";
			}
		}
		ProcessBuilder pb = new ProcessBuilder(args);
		//File errorlog = new File("error_" + mut.getPath().replaceAll(File.separator, "-") + ".log");
		//File outLog = new File("out_" + mut.getPath().replaceAll(File.separator, "-") + ".log");
		//pb.redirectError(errorlog);
		//pb.redirectOutput(outLog);
		
		try {
			Process p = pb.start();
			InputStream is = p.getInputStream();
			int exitCode = p.waitFor();
			//TODO: manage errors in the result
			if (exitCode != 0) {
				System.err.println("External JUnit runner for mutant " + mut.getPath() + " failed with code " + exitCode);
			} else {
				if (is == null) {
					System.err.println("InputStream from external JUnit runner is null");
				} else {
					testResults.addAll(parseResultsFromInputStream(is));
					is.close();
				}
			}
		} catch (IOException | InterruptedException | ClassNotFoundException e) {
			e.printStackTrace();
			error = e;
		}
		ExternalJUnitResult res = null;
		if (error == null) {
			res = new ExternalJUnitResult(testResults);
		} else {
			res = new ExternalJUnitResult(error);
		}
		return res;
	}
	
	private Collection<? extends TestResult> parseResultsFromInputStream(InputStream is) throws ClassNotFoundException, IOException {
		List<TestResult> results = new LinkedList<>();
		ObjectInputStream in = new ObjectInputStream(is);
		Object o = null;
		try {
			while ((o = in.readObject()) != null) {
				TestResult tr = (TestResult)o;
				tr.refresh();
				results.add(tr);
			}
		} catch (EOFException e) {}
		in.close();
		return results;
	}

	private String getCurrentClasspath() {
		String classpath = System.getProperty("java.class.path");
		return classpath;
	}
	
	public Exception getLastError() {
		return this.lastError;
	}
	
	
	private Class<?> loadClass(String base, String className) {
		File file = new File(base);
		Class<?> clazz = null;
		String classToLoad = className;
		try {
		    URL url = file.toURI().toURL();
		    URL[] urls = new URL[]{url};
		    
		    URLClassLoader cl = new URLClassLoader(urls);

		    clazz = cl.loadClass(classToLoad);
		    cl.close();
		    
		} catch (MalformedURLException e) {
			this.lastError = e;
		} catch (ClassNotFoundException e) {
			this.lastError = e;
		} catch (IOException e) {
			this.lastError = e;
		}
		return clazz;
	}

}
