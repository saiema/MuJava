package mujava.junit.runner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import org.junit.runners.model.InitializationError;

public class MuJavaJunitTestRunner {
	private boolean failFast;
	private Class<?> testToRun;
	private Runner testRunner;
	private JUnitCore core = new JUnitCore();
	
	
	public MuJavaJunitTestRunner(Class<?> testToRun, boolean failFast) throws IllegalArgumentException, MuJavaTestRunnerException {
		this.failFast = failFast;
		this.testToRun = testToRun;
		try {
			this.testRunner = retrieveTestRunner(testToRun);
		} catch (Throwable e) {
			throw new MuJavaTestRunnerException(this.getClass().getName()+"#MuJavaJunitTestRunner("+testToRun.getName()+")", e);
		}
	}
	
	public Result run() throws InitializationError {
		return this.testRunner!=null?this.core.run(this.testRunner):this.core.run(this.testToRun);
	}
	
	private ParentRunner<?> retrieveTestRunner(Class<?> testToRun) throws Throwable {
		RunWith runWithAnnotation = testToRun.getAnnotation(RunWith.class);
		if (runWithAnnotation != null) {
			if (runWithAnnotation.value().equals(Parameterized.class)) {
				return this.failFast? new FailFastParameterized(testToRun):null;
			}  else if (runWithAnnotation.value().equals(Suite.class)) {
				return new Suite(testToRun, new FailFastRunnerBuilder());
			}
		} else if (TestCase.class.isAssignableFrom(testToRun)) {
			this.testRunner = null; //TODO: for the moment will be using this runner
		} else if (TestSuite.class.isAssignableFrom(testToRun)) {
			this.testRunner = null; //TODO: for the moment will be using this runner
		} else if (hasTestMethod(testToRun)) {
			if (this.failFast) FailFastBlockJUnit4ClassRunner.ignore = false;
			return this.failFast? new FailFastBlockJUnit4ClassRunner(testToRun):null;
		} else {
			throw new IllegalArgumentException("Class : " + testToRun.toString() + " is not a valid junit test");
		}
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
	
	
}
