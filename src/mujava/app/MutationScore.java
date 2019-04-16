package mujava.app;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
//import org.junit.runners.model.InitializationError; //changed to support junit 3.8

public class MutationScore {
	private static String mutantsSourceFolder = null;
	private static String originalBinFolder = null;
	private static String testsBinFolder = null;
	private static MutationScore instance = null;
	private Exception lastError = null;
	public static long timeout;
	public static long discardTimeout;
	public static boolean runTestsInSeparateProcesses = false;
	public static boolean quickDeath;
	public static boolean dynamicSubsumption;
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
		System.out.println("Compiling with classpath: " + getCurrentClasspath());
		if (path.contains("$")) {
			System.out.println("\tDealing with an internal class, obtaining main class file");
			int internalSymbol = path.indexOf("$");
			path = path.substring(0, internalSymbol) + ".java";
			System.out.println("\tMain class obtained: " + path);
			return compile(path);
		}
		File fileToCompile = new File(/*mutantsSourceFolder+*/path);
		if (!fileToCompile.exists() || !fileToCompile.isFile() || !fileToCompile.getName().endsWith(".java")) {
			return new CompilationResult(new Exception("Error in file : " + fileToCompile.getAbsolutePath()));
		}
		File[] files = new File[]{fileToCompile};
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
		boolean success = false;
		Exception compilationUnexpectedError = null;
		try {
			success = compiler.getTask(null, fileManager, diagnostics, Arrays.asList(new String[] {
					"-classpath",
					originalBinFolder+File.pathSeparator+getCurrentClasspath()
					}
			), null, compilationUnit).call();
		} catch (RuntimeException e) {
			System.err.println(ExceptionUtils.getFullStackTrace(e));
			compilationUnexpectedError = e;
		} catch (Exception e) {
			System.err.println(ExceptionUtils.getFullStackTrace(e));
			compilationUnexpectedError = e;
		}
		String compilationErrorOutput = "";
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
			String kind = diagnostic.getKind()==null?"N/A":diagnostic.getKind().toString();
			long lineNumber = diagnostic.getLineNumber();
			String source = diagnostic.getSource()==null?"N/A":diagnostic.getSource().toUri().toString();
	        String msg = diagnostic.getMessage(null)==null?"N/A":diagnostic.getMessage(null);
			compilationErrorOutput += String.format("%s on line %d in %s%n%s%n",
	        										kind,
								                    lineNumber,
								                    source,
								                    msg);
		}
		CompilationResult cresult = null;
		if (compilationUnexpectedError != null) {
			cresult = new CompilationResult(compilationUnexpectedError);
		} else if (success) {
			cresult = new CompilationResult(compilationErrorOutput);
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
	
//	public List<TestResult> runTestsWithMutants(List<String> testClasses, MutantInfo mut) {
//		this.lastError = null;
//		if (MutationScore.reloader == null) {
//			List<String> classpath = Arrays.asList(new String[]{MutationScore.originalBinFolder, MutationScore.testsBinFolder});
//			MutationScore.reloader = new Reloader(classpath,Thread.currentThread().getContextClassLoader());
//			MutationScore.reloader.markEveryClassInFolderAsReloadable(MutationScore.originalBinFolder, MutationScore.allowedPackages);
//			MutationScore.reloader.markEveryClassInFolderAsReloadable(MutationScore.testsBinFolder, MutationScore.allowedPackages);
//		}
//		List<TestResult> testResults = new LinkedList<TestResult>();
//		System.out.println("Testing mutant : "+mut.getPath()+'\n');
//		if (MutationScore.outputMutationsInfo) {
//			System.out.println(mut.toString());
//		}
//		for (String test : testClasses) {
//			Class<?> testToRun;
//			try {
//				MutationScore.reloader = MutationScore.reloader.getLastChild();
//				MutationScore.reloader.setSpecificClassPath(mut.getName(), mut.getClassRootFolder());//(className, MutationScore.mutantsSourceFolder+pathToMutant);
//				testToRun = MutationScore.reloader.rloadClass(test, true);
//				MuJavaJunitTestRunnerBuilder mjTestRunner = new MuJavaJunitTestRunnerBuilder(testToRun, MutationScore.quickDeath, /*MutationScore.dynamicSubsumption,*/ timeout);
//				Result testResult = mjTestRunner.run();
//				//Core.killStillRunningJUnitTestcaseThreads();
//				TestResult tres = new TestResult(testResult, testToRun, mjTestRunner.getSimpleResults());
//				testResults.add(tres);
//				if (!testResult.wasSuccessful() && MutationScore.quickDeath) {
//					break;
//				}
//			} catch (ClassNotFoundException | IllegalArgumentException | MuJavaTestRunnerException e) {// | InitializationError e) { //change to support junit 3.8
//				e.printStackTrace();
//				this.lastError = e;
//			} catch (Throwable e) {
//				e.printStackTrace();
//				this.lastError = new Exception(e);
//			}
//		}
//		return testResults;
//	}
	
	public ExternalJUnitResult runTestsWithMutantsUsingExternalRunner(List<String> testClasses, MutantInfo mut, boolean separateTestsInDifferentProcesses) {
		ExternalJUnitResult res = null;
		if (!separateTestsInDifferentProcesses) {
			return runTestsWithMutantsUsingExternalRunner(testClasses, mut);
		} else {
			List<String> tests = new LinkedList<String>();
			for (String test : testClasses) {
				tests.clear();
				tests.add(test);
				ExternalJUnitResult singleResult = runTestsWithMutantsUsingExternalRunner(tests, mut);
				if (!singleResult.testsRunSuccessful()) {
					res = singleResult;
					break;
				}
				if (res == null) {
					res = singleResult;
				} else {
					res.merge(singleResult);
				}
			}
		}
		return res;
	}
	
	public ExternalJUnitResult runTestsWithMutantsUsingExternalRunner(List<String> testClasses, MutantInfo mut) {
		Exception error = null;
		List<TestResult> testResults = new LinkedList<>();
		ExecutorService es = Executors.newSingleThreadExecutor();
		try {
			TestResultCollector testResultsCollector = new TestResultCollector(mut);
			String[] args = getExternalJUnitRunnerCommand(testClasses, mut, testResultsCollector.getPort());
			ProcessBuilder pb = new ProcessBuilder(args);
			File errorLog = new File("externalError.log");
			pb.redirectError(Redirect.appendTo(errorLog));
			File outputLog = new File("externalOutput.log");
			pb.redirectOutput(Redirect.appendTo(outputLog));
			Future<List<TestResult>> testResultsCollectorTask = es.submit(testResultsCollector);
			Process p = pb.start();
			int exitCode = p.waitFor();
			//TODO: manage errors in the result
			if (exitCode != 0) {
				System.err.println("External JUnit runner for mutant " + mut.getPath() + " failed with code " + exitCode);
				testResultsCollector.closeSocket();
			} else {
				testResults.addAll(testResultsCollectorTask.get());
			}
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
			error = e;
		} finally {
			es.shutdown();
		}
		ExternalJUnitResult res = null;
		if (error == null) {
			res = new ExternalJUnitResult(testResults);
		} else {
			res = new ExternalJUnitResult(error);
		}
		return res;
	}
	
	private String[] getExternalJUnitRunnerCommand(List<String> testClasses, MutantInfo mut, int port) {
		String classpath = MutationScore.originalBinFolder+":"+MutationScore.testsBinFolder;
		classpath += ":"+junitPath+":"+hamcrestPath;
		classpath += ":"+cleanClasspath(getCurrentClasspath());
		classpath += ":"+mut.getClassRootFolder();
		System.out.println("Running external runner with classpath: " + classpath);
		String mutPath = mut.getClassRootFolder();
		boolean useSockets = port>0;
		//ProcessBuilder args are:
		//command + binFolder + testsBinFolder + tests + mutantLocation + mutantClass + quickDeath + toughness + socketPort
		//where except for the command, the other arguments need a flag, which are the following:
		//-b -t -T -m -c -q -x -l -s
		//-q and -t will only be used if quickdeath or toughness where set
		String[] libs = cleanClasspath(getCurrentClasspath()).split(File.pathSeparator);
		int optionalFlags = (MutationScore.quickDeath || Core.toughnessAnalysis())?1:0;
		if (MutationScore.dynamicSubsumption) optionalFlags += 1;
		if (MutationScore.timeout > 0) optionalFlags += 2;
		if (MutationScore.discardTimeout > 0) optionalFlags += 2;
		int libsSize = libs.length;
		String[] args = new String[testClasses.size() /*tests*/ + 5 + (useSockets?1:0) + /*flags*/ + (libs.length==0?0:1) /*libs flag*/ + 4 + (useSockets?1:0) + /*args*/ + 4 /*command*/ + optionalFlags + libsSize];
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
			if (MutationScore.quickDeath && !Core.toughnessAnalysis()) {				//1+ optional flags
				args[12+i] = "-q";
			} else if (Core.toughnessAnalysis() && MutationScore.dynamicSubsumption) {	//2+ optional flags
				args[12+i] = "-x";
				args[13+i] = "-d";
			} else if (Core.toughnessAnalysis()) {										//1+ optional flags
				args[12+i] = "-x";
			}
			if (timeout > 0) {
				int initialIdx = 12;
				if ((MutationScore.quickDeath || Core.toughnessAnalysis())) initialIdx++;
				if (MutationScore.dynamicSubsumption) initialIdx++;
				args[initialIdx+i] = "-i"; args[(initialIdx+1)+i] = Long.toString(timeout);
			}
			if (discardTimeout > 0) {
				int initialIdx = 12;
				if (timeout > 0) initialIdx+=2;
				if ((MutationScore.quickDeath || Core.toughnessAnalysis())) initialIdx++;
				if (MutationScore.dynamicSubsumption) initialIdx++;
				args[initialIdx+i] = "-I"; args[(initialIdx+1)+i] = Long.toString(discardTimeout);
			}
		}
		int j = 1;
		int currentIndex = 12 + optionalFlags;
		if (libs.length > 0) {
			args[currentIndex+i] = "-l";
			for (String l : libs) {
				args[currentIndex+i+j] = l;
				j++;
			}
		}
		currentIndex = currentIndex + i + j;
		if (useSockets) {
			args[currentIndex] = "-s";
			args[currentIndex+1] = Integer.toString(port);
		}
		return args;
	}
	
	private String cleanClasspath(String classpath) {
		String[] paths = classpath.split(File.pathSeparator);
		List<String> cleanedPaths = new LinkedList<>();
		for (String p : paths) {
			if (p.contains("junit")) {
				continue;
			}
			if (p.contains("hamcrest")) {
				continue;
			}
			if (p.contains(MutationScore.testsBinFolder)) {
				continue;
			}
			if (p.contains(MutationScore.originalBinFolder)) {
				continue;
			}
			cleanedPaths.add(p);
		}
		Iterator<String> it = cleanedPaths.iterator();
		String result = "";
		while (it.hasNext()) {
			String p = it.next();
			result += p;
			if (it.hasNext()) {
				result += File.pathSeparator;
			}
		}
		return result;
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
