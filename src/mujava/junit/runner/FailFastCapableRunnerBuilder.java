package mujava.junit.runner;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized;
import org.junit.runners.ParentRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;

//TODO: comment
public class FailFastRunnerBuilder extends RunnerBuilder {

	@Override
	public Runner runnerForClass(Class<?> arg0) throws Throwable {
		return retrieveTestRunner(arg0);
	}
	
	private ParentRunner<?> retrieveTestRunner(Class<?> testToRun) throws Throwable {
		RunWith runWithAnnotation = testToRun.getAnnotation(RunWith.class);
		if (runWithAnnotation != null) {
			if (runWithAnnotation.value().equals(Parameterized.class)) {
				return new FailFastParameterized(testToRun);
			} else if (runWithAnnotation.value().equals(Suite.class)) {
				return new Suite(testToRun, this);
			}
		} else if (TestCase.class.isAssignableFrom(testToRun)) {
			FailFastBlockJUnit4ClassRunner.ignore = false;
			return new FailFastBlockJUnit4ClassRunner(testToRun);
		} else if (TestSuite.class.isAssignableFrom(testToRun)) {
			FailFastBlockJUnit4ClassRunner.ignore = false;
			return new FailFastBlockJUnit4ClassRunner(testToRun);
		} else if (testToRun.getAnnotation(Test.class) != null) {
			FailFastBlockJUnit4ClassRunner.ignore = false;
			return new FailFastBlockJUnit4ClassRunner(testToRun);
		} else {
			throw new IllegalArgumentException("Class : " + testToRun.toString() + " is not a valid junit test");
		}
		return new BlockJUnit4ClassRunner(testToRun);
	}

}
