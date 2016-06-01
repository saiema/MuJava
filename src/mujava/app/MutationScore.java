package mujava.app;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	public static String msLibs = "";
	
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
	
	public boolean compile(String path) {
		this.lastError = null;
		File fileToCompile = new File(/*mutantsSourceFolder+*/path);
		if (!fileToCompile.exists() || !fileToCompile.isFile() || !fileToCompile.getName().endsWith(".java")) {
			return false;
		}
		File[] files = new File[]{fileToCompile};
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
		String paths = originalBinFolder;
		if (!MutationScore.msLibs.isEmpty()) {
			paths += File.pathSeparator + MutationScore.msLibs;
		}
		boolean compileResult = compiler.getTask(null, fileManager, null, Arrays.asList(new String[] {"-classpath", paths}), null, compilationUnit).call();
		return compileResult;
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
	
	private ClassLoader getBaseClassLoader() throws MalformedURLException {
		ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
		if (!MutationScore.msLibs.isEmpty()) {
			String[] libs = MutationScore.msLibs.split(File.pathSeparator);
			URL[] urls = new URL[libs.length];
			int i = 0;
			for (String l : libs) {
				urls[i] = new URL(l);
				i++;
			}
			URLClassLoader urlClassLoader = new URLClassLoader(urls, threadCL);
			return urlClassLoader;
		} 
		return threadCL;
	}
	
	public List<TestResult> runTestsWithMutants(List<String> testClasses, MutantInfo mut) {//String pathToMutant, String className) {
		this.lastError = null;
		if (MutationScore.reloader == null) {
			List<String> classpath = Arrays.asList(new String[]{MutationScore.originalBinFolder, MutationScore.testsBinFolder});
			try {
				ClassLoader baseClassLoader = getBaseClassLoader();
				MutationScore.reloader = new Reloader(classpath, baseClassLoader);//(classpath,Thread.currentThread().getContextClassLoader());
				MutationScore.reloader.markEveryClassInFolderAsReloadable(MutationScore.originalBinFolder, MutationScore.allowedPackages);
				MutationScore.reloader.markEveryClassInFolderAsReloadable(MutationScore.testsBinFolder, MutationScore.allowedPackages);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				this.lastError = e;
				return null;
			}
		}
		List<TestResult> testResults = new LinkedList<TestResult>();
		//System.out.println("Testing mutant : "+pathToMutant+className+'\n');
//		System.out.println("Testing mutant : "+mut.getPath()+'\n');
//		if (MutationScore.outputMutationsInfo) {
//			System.out.println(mut.toString());
//		}
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
