package mujava.junit.runner;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.runner.Result;

import mujava.app.Core;
import mujava.app.TestResult;

/**
 * This class is meant as an alternative to running mutation analysis using the Reloader.
 * 
 * @author stein
 * @version 0.1
 */
public class ExternalJUnitTestRunner {
	private static final String VERSION = "0.1";
	
	/**
	 * Main for running tests on a single mutant
	 * 
	 * exit codes :
	 * <li>0 : normal exit</li>
	 * <li>1 : error in configuration</li>
	 * <li>2 : error while running mutation analysis (running tests)</li>
	 * <li>3 : error while serializing test results</li> 
	 * @param args
	 */
	public static void main(String[] args) {
		Options options = new Options();
		
		Option projectBinFolderOption = new Option("b", "binFolder", true, "project bin folder");
		projectBinFolderOption.setRequired(true);
		projectBinFolderOption.setArgs(1);
		projectBinFolderOption.setType(String.class);
		
		Option testsBinFolderOption = new Option("t", "testsBinFolder", true, "tests bin folder");
		testsBinFolderOption.setRequired(true);
		testsBinFolderOption.setArgs(1);
		testsBinFolderOption.setType(String.class);
		
		Option libsOption = new Option("l", "libs", true, "libraries");
		libsOption.setRequired(false);
		libsOption.setArgs(Option.UNLIMITED_VALUES);
		libsOption.setType(String.class);
		
		Option testsOption = new Option("T", "tests", true, "tests to run as class names");
		testsOption.setRequired(true);
		testsOption.setArgs(Option.UNLIMITED_VALUES);
		testsOption.setType(String.class);
		
		Option mutantPathOption = new Option("m", "mutantLocation", true, "mutant java file path");
		mutantPathOption.setRequired(true);
		mutantPathOption.setArgs(1);
		mutantPathOption.setType(String.class);
		
		Option mutantClassOption = new Option("c", "mutantClass", true, "the mutant's class");
		mutantClassOption.setRequired(true);
		mutantClassOption.setArgs(1);
		mutantClassOption.setType(String.class);
		
		Option quickDeathOption = new Option("q", "quickDeath", false, "stop tests at first fail");
		quickDeathOption.setRequired(false);
		
		Option toughnessOption = new Option("x", "toughness", false, "enable toughness analysis (disables quick death)");
		toughnessOption.setRequired(false);
		
		Option verboseOption = new Option("v", "verbose", false, "enable verbosity");
		verboseOption.setRequired(false);
		
		Option help = new Option("h", "help", false, "print commands");
		help.setRequired(false);
		
		options.addOption(projectBinFolderOption);
		options.addOption(testsBinFolderOption);
		options.addOption(libsOption);
		options.addOption(testsOption);
		options.addOption(mutantPathOption);
		options.addOption(mutantClassOption);
		options.addOption(quickDeathOption);
		options.addOption(toughnessOption);
		
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			
			if (cmd.hasOption('h')) {
				System.out.println("Mujava++ external JUnit runner");
				System.out.println("Version : "+ VERSION);
				System.out.println("Console version");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("muJava++ external JUnit runner", options );
				System.exit(1);
			}
			
			boolean verbose = false;
			
			if (cmd.hasOption(verboseOption.getOpt())) {
				verbose = true;
			}
			
			if (verbose) System.out.println("Validating parameters...");
			
			//original bin dir
			String binDir = cmd.getOptionValue(projectBinFolderOption.getOpt());
			if (!verifyDirectory(binDir)) {
				System.err.println("Project bin directory ("+binDir+") doesn't exist");
				System.exit(1);
			}
			if (verbose) System.out.println("Project bin dir: " + binDir);
			
			String testBinDir = cmd.getOptionValue(testsBinFolderOption.getOpt());
			if (!verifyDirectory(testBinDir)) {
				System.err.println("Tests directory ("+testBinDir+") doesn't exist");
				System.exit(1);
			}
			if (verbose) System.out.println("Tests bin dir: " + testBinDir);
			
			String[] libs = cmd.getOptionValues(libsOption.getOpt());
			for (String l : libs) {
				if (!verifyDirectory(l) && !l.endsWith(".jar")) {
					System.err.println("library ("+l+") is not a directory or jar file");
					System.exit(1);
				}
			}
			if (verbose) {
				System.out.println("Libraries: " + Arrays.toString(libs));
			}
			
			String[] tclasses = cmd.getOptionValues(testsOption.getOpt());
			for (String tc : tclasses) {
				if (!verifyFile(testBinDir+tc.replaceAll("\\.", Core.SEPARATOR)+".class")) {
					System.err.println("Test class ("+(testBinDir+tc.replaceAll("\\.", Core.SEPARATOR)+".class")+") doesn't exist");
					System.exit(1);
				}
			}
			if (verbose) System.out.println("Tests classes: "+stringArrayToString(tclasses));
			
			String mutantPath = cmd.getOptionValue(mutantPathOption.getOpt());
			if (!verifyDirectory(mutantPath)) {
				System.err.println("Mutant path : " + mutantPath + " doesn't exist");
				System.exit(1);
			}
			if (verbose) System.out.println("Mutant path : " + mutantPath);
			
			String classToMutate = cmd.getOptionValue(mutantClassOption.getOpt());
			if (verbose) System.out.println("Class to mutate: "+ classToMutate); //TODO: this is not validated!
			
			boolean toughness = cmd.hasOption(toughnessOption.getOpt());
			boolean quickDeath = toughness?false:cmd.hasOption(quickDeathOption.getOpt());
			
			
//			List<String> classpath = Arrays.asList(new String[]{binDir, testBinDir});
//			Reloader reloader = new Reloader(classpath,Thread.currentThread().getContextClassLoader());
//			reloader.markEveryClassInFolderAsReloadable(testBinDir);
//			for (String l : libs) {
//				reloader.markEveryClassInFolderAsReloadable(l);
//			}
//			reloader.markEveryClassInFolderAsReloadable(binDir);
//			reloader.setSpecificClassPath(classToMutate, mutantPath);
			List<TestResult> testResults = new LinkedList<TestResult>();
			for (String test : tclasses) {
				Class<?> testToRun;
				try {
					testToRun = Class.forName(test);
					MuJavaJunitTestRunner mjTestRunner = new MuJavaJunitTestRunner(testToRun, quickDeath);
					Result testResult = mjTestRunner.run();
					Core.killStillRunningJUnitTestcaseThreads();
					testResults.add(new TestResult(testResult, testToRun));
					if (!testResult.wasSuccessful() && quickDeath) {
						break;
					}
				} catch (ClassNotFoundException | IllegalArgumentException e) { //| MuJavaTestRunnerException e) { //| InitializationError e) { //changed to support junit 3.8
					System.err.println(ExceptionUtils.getFullStackTrace(e));
					System.exit(2);
				} catch (Throwable e) {
					System.err.println(ExceptionUtils.getFullStackTrace(e));
					System.exit(2);
				}
			}
			ObjectOutputStream out = new ObjectOutputStream(System.out);
	        for (TestResult tr : testResults) {
	            out.writeObject(tr);
	        }
	        out.flush();
		} catch (ParseException e) {
			System.err.println("Incorrect options.  Reason: " + e.getMessage());
			System.err.println(ExceptionUtils.getFullStackTrace(e));
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Error while serializing results");
			System.err.println(ExceptionUtils.getFullStackTrace(e));
			System.exit(3);
		}
	}
	
	private static boolean verifyDirectory(String dir) {
		File directory = new File(dir);
		return directory.exists() && directory.isDirectory();
	}
	
	private static boolean verifyFile(String f) {
		File file = new File(f);
		return file.exists() && file.isFile();
	}
	
	private static String stringArrayToString(String[] xs) {
		String result = "[";
		int index = 0;
		for (String x : xs) {
			result += x;
			index++;
			if (index < xs.length) {
				result += ", ";
			}
		}
		result += "]";
		return result;
	}

}
