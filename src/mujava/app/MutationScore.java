package mujava.app;

import java.io.File;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
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
		File fileToCompile = new File(mutantsSourceFolder+path);
		if (!fileToCompile.exists() || !fileToCompile.isFile() || !fileToCompile.getName().endsWith(".java")) {
			return false;
		}
		File[] files = new File[]{fileToCompile};
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
		boolean compileResult = compiler.getTask(null, fileManager, null, Arrays.asList(new String[] {"-classpath", originalBinFolder}), null, compilationUnit).call();
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
	
	public List<TestResult> runTestsWithMutants(List<String> testClasses, String pathToMutant, String className) {
		this.lastError = null;
//		String originalFile = MutationScore.originalBinFolder+className;
//		String mutantFile = MutationScore.mutantsSourceFolder+pathToMutant+className;
//		this.backupOriginal(originalFile);
//		if (!this.moveMutant(mutantFile, originalFile)) {
//			this.delete(originalFile);
//			this.restoreOriginal(originalFile);
//			return null;
//		}
		if (MutationScore.reloader == null) {
			List<String> classpath = Arrays.asList(new String[]{MutationScore.originalBinFolder, MutationScore.testsBinFolder});
			MutationScore.reloader = new Reloader(classpath,Thread.currentThread().getContextClassLoader());
			MutationScore.reloader.markEveryClassInFolderAsReloadable(MutationScore.originalBinFolder, MutationScore.allowedPackages);
			MutationScore.reloader.markEveryClassInFolderAsReloadable(MutationScore.testsBinFolder, MutationScore.allowedPackages);
		}
		List<TestResult> testResults = new LinkedList<TestResult>();
		System.out.println("Testing mutant : "+pathToMutant+className+'\n');
		for (String test : testClasses) {
			Class<?> testToRun;
			try {
				MutationScore.reloader = MutationScore.reloader.getLastChild();
				MutationScore.reloader.setSpecificClassPath(className, MutationScore.mutantsSourceFolder+pathToMutant);
				testToRun = MutationScore.reloader.rloadClass(test, true);
				MuJavaJunitTestRunner mjTestRunner = new MuJavaJunitTestRunner(testToRun, MutationScore.quickDeath);
				Result testResult = mjTestRunner.run();
				Core.killStillRunningJUnitTestcaseThreads();
				testResults.add(new TestResult(testResult));
				if (!testResult.wasSuccessful() && MutationScore.quickDeath) {
					break;
				}
			} catch (ClassNotFoundException | IllegalArgumentException | MuJavaTestRunnerException | InitializationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.lastError = e;
			}
		}
//		this.delete(originalFile);
//		this.restoreOriginal(originalFile);
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
	
//	private void backupOriginal(String path) {
//		String fixedPath = path.replaceAll("\\.", Core.SEPARATOR)+".class";
//		File original = new File(fixedPath);
//		original.renameTo(new File(fixedPath+".backup"));
//	}
	
//	private void restoreOriginal(String path) {
//		String fixedPath = path.replaceAll("\\.", Core.SEPARATOR)+".class";
//		File backup = new File(fixedPath+".backup");
//		backup.renameTo(new File(fixedPath));
//	}
	
//	private boolean moveMutant(String mutantPath, String originalPath) {
//		String fixedMutantPath = mutantPath.replaceAll("\\.", Core.SEPARATOR)+".class";
//		String fixedOriginalPath = originalPath.replaceAll("\\.", Core.SEPARATOR)+".class";
//		File mutant = new File(fixedMutantPath);
//		if (!mutant.exists()) {
//			this.lastError = new FileNotFoundException("File : " + fixedMutantPath + " doesn't exist!\n");
//			return false;
//		}
//		File original = new File(fixedOriginalPath);
//		if (original.getParentFile()==null?!original.exists():!original.getParentFile().exists()) {
//			this.lastError = new FileNotFoundException("File : " + fixedOriginalPath + " doesn't exist!\n");
//			return false;
//		}
//		try {
//			Path dest = original.getParentFile()==null?original.toPath():original.getParentFile().toPath();
//			Files.copy(mutant.toPath(), dest.resolve(mutant.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING);
//		} catch (IOException e) {
//			this.lastError = e;
//			return false;
//		}
//		return true;
//	}
	
//	private void delete(String path) {
//		String fixedPath = path.replaceAll("\\.", Core.SEPARATOR)+".class";
//		File f = new File(fixedPath);
//		if (f.exists()) {
//			f.delete();
//		}
//	}

}
