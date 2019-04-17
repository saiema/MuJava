package mujava.junit.runner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.Parameterized;
import org.junit.runners.ParentRunner;
import org.junit.runners.Suite;

public class MuJavaJunitTestRunnerBuilder {
	private boolean failFast;
	//private boolean dynamicSubsumption;
	private Class<?> testToRun;
	private Runner testRunner;
	private JUnitCore core = new JUnitCore();
	private Map<String, Boolean> testsSimpleResults;
	private long timeout;
	private long discard;
	public static boolean verbose = false;
	
	public MuJavaJunitTestRunnerBuilder(Class<?> testToRun, boolean failFast/*, boolean dynamicSubsumption*/, long timeout) throws IllegalArgumentException, MuJavaTestRunnerException {
		this(testToRun, failFast, timeout, 0);
	}
	
	public MuJavaJunitTestRunnerBuilder(Class<?> testToRun, boolean failFast/*, boolean dynamicSubsumption*/, long timeout, long discard) throws IllegalArgumentException, MuJavaTestRunnerException {
		this.failFast = failFast;
		this.testToRun = testToRun;
		//this.dynamicSubsumption = dynamicSubsumption;
		this.timeout = timeout;
		this.discard = discard;
		try {
			testRunner = retrieveTestRunner(testToRun);
			//if (this.dynamicSubsumption) {
				testsSimpleResults = new TreeMap<>();
				TestInfoRunListener l = new TestInfoRunListener(testsSimpleResults); 
				core.addListener(l);
			//}
		} catch (Throwable e) {
			throw new MuJavaTestRunnerException(this.getClass().getName()+"#MuJavaJunitTestRunner("+testToRun.getName()+")", e);
		}
	}
	
	public Result run() throws Throwable {//InitializationError { //to support junit 3.8
		Result result = this.testRunner!=null?this.core.run(this.testRunner):this.core.run(this.testToRun);
		return result;
	}
	
	public Map<String, Boolean> getSimpleResults() {
		return testsSimpleResults;
	}
	
	private ParentRunner<?> retrieveTestRunner(Class<?> testToRun) throws Throwable {
		if (verbose) System.out.println("mujava.junit.runner.MuJavaJunitTestRunnerBuilder: retrieving runner for class " + testToRun.getName());
		RunWith runWithAnnotation = testToRun.getAnnotation(RunWith.class);
		if (runWithAnnotation != null) {
			if (verbose) System.out.println("runWith annotation found");
			if (runWithAnnotation.value().equals(Parameterized.class)) {
				if (verbose) System.out.println("runWith Parameterized.class");
				if (verbose) System.out.println("Using FailFastCapableParameterized(" + testToRun.getCanonicalName() +", " + failFast +", " + timeout + ", " + discard +")");
				return new FailFastCapableParameterized(testToRun, failFast, timeout, discard);
			}  else if (runWithAnnotation.value().equals(Suite.class)) {
				if (verbose) System.out.println("runWith Suite.class");
				if (verbose) System.out.println("Using new Suite("+ testToRun.getCanonicalName() + ", new FailFastCapableRunnerBuilder(" + failFast +", " + timeout + ", " + discard +"))");
				return new Suite(testToRun, new FailFastCapableRunnerBuilder(failFast, timeout, discard));
			}
		} else if (TestCase.class.isAssignableFrom(testToRun)) {
			if (verbose) System.out.println("Test class extends TestCase");
			Method suiteMethod = getSuiteMethod(testToRun);
			if (suiteMethod != null) {
				if (verbose) System.out.println("suite method found");
				TestSuite testSuite = getTestSuite(suiteMethod);
				return retrieveTestRunner(testSuite.getClass());
			}
			if (verbose) System.out.println("suite method not found");
			testRunner = null; //TODO: for the moment will be using this runner
		} else if (TestSuite.class.isAssignableFrom(testToRun)) {
			if (verbose) System.out.println("Test class extends TestSuite");
			testRunner = null; //TODO: for the moment will be using this runner
		} else if (hasTestMethod(testToRun)) {
			if (verbose) System.out.println("Test method found");
			if (failFast) FailFastCapableBlockJUnit4ClassRunner.ignore = false;
			if (verbose) System.out.println("Using FailFastCapableBlockJUnit4ClassRunner(" + testToRun.getCanonicalName() +", " + failFast +", " + timeout + ", " + discard +")");
			return new FailFastCapableBlockJUnit4ClassRunner(testToRun, failFast, timeout, discard);
		} else {
			throw new IllegalArgumentException("Class : " + testToRun.toString() + " is not a valid junit test");
		}
		if (verbose) System.out.println("No conditions met");
		return null;
	}
	
	private boolean hasTestMethod(Class<?> testToRun) {
		Method[] methods = testToRun.getDeclaredMethods();
		for (Method m : methods) {
			if (Modifier.isPublic(m.getModifiers())) {
				if (m.getAnnotation(Test.class) != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	//looks for
	//public static Test suite()
	private Method getSuiteMethod(Class<?> testToRun) {
		Method[] methods = testToRun.getDeclaredMethods();
		for (Method m : methods) {
			int modifiers = m.getModifiers();
			if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
				if (m.getParameterTypes().length != 0) continue;
				Class<?> retType = m.getReturnType();
				if (retType.getCanonicalName().compareTo("junit.framework.Test") == 0) return m;
			}
		}
		return null;
	}
	
	//testToRun must have the suite method
	private TestSuite getTestSuite(Method suiteMethod) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object res = suiteMethod.invoke(null, new Object[]{});
		return (TestSuite) res;
	}
	
	
}
