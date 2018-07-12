package mujava.junit.runner;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.Parameterized;
import org.junit.runners.ParentRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;

//TODO: comment
public class FailFastCapableRunnerBuilder extends RunnerBuilder {
	
	private boolean failFast;
	private long timeout;
	
	public FailFastCapableRunnerBuilder(boolean failFast, long timeout) {
		this.failFast = failFast;
		this.timeout = timeout;
	}

	@Override
	public Runner runnerForClass(Class<?> arg0) throws Throwable {
		return retrieveTestRunner(arg0);
	}
	
	private ParentRunner<?> retrieveTestRunner(Class<?> testToRun) throws Throwable {
		RunWith runWithAnnotation = testToRun.getAnnotation(RunWith.class);
		if (runWithAnnotation != null) {
			if (runWithAnnotation.value().equals(Parameterized.class)) {
				return new FailFastCapableParameterized(testToRun, failFast, timeout);
			} else if (runWithAnnotation.value().equals(Suite.class)) {
				return new Suite(testToRun, this);
			}
		} else if (TestCase.class.isAssignableFrom(testToRun)) {
			FailFastCapableBlockJUnit4ClassRunner.ignore = false;
			return new FailFastCapableBlockJUnit4ClassRunner(testToRun, failFast, timeout);
		} else if (TestSuite.class.isAssignableFrom(testToRun)) {
			FailFastCapableBlockJUnit4ClassRunner.ignore = false;
			return new FailFastCapableBlockJUnit4ClassRunner(testToRun, failFast, timeout);
		} else if (testToRun.getAnnotation(Test.class) != null) {
			FailFastCapableBlockJUnit4ClassRunner.ignore = false;
			return new FailFastCapableBlockJUnit4ClassRunner(testToRun, failFast, timeout);
		} else {
			boolean hasAtLeastOneTest = false;
			for (Method m : testToRun.getDeclaredMethods()) {
				if (m.isAnnotationPresent(Test.class)) {
					hasAtLeastOneTest = true;
					break;
				}
			}
			if (hasAtLeastOneTest) {
				FailFastCapableBlockJUnit4ClassRunner.ignore = false;
				return new FailFastCapableBlockJUnit4ClassRunner(testToRun, failFast, timeout);
			}
			throw new IllegalArgumentException("Class : " + testToRun.toString() + " is not a valid junit test");
		}
		FailFastCapableBlockJUnit4ClassRunner.ignore = false;
		return new FailFastCapableBlockJUnit4ClassRunner(testToRun, false, timeout);
	}

}
