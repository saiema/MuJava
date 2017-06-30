package mujava.app;

import java.io.Serializable;
import java.util.List;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3718457180972664524L;
	private Result result;
	private Class<?> testClassRunned;
	private transient int totalTests = 0;
	private transient int assertFailingTests = 0;
	private transient int totalFailures = 0;
	private transient int passingTests = 0;
	private transient int timeoutFailures = 0;
	private transient int exceptionFailures = 0;
	
	public TestResult(Result result, Class<?> testClassRunned) {
		this.result = result;
		this.testClassRunned = testClassRunned;
		refresh();
	}
	
	public void refresh() {
		this.totalTests = 0;
		this.assertFailingTests = 0;
		this.totalFailures = 0;
		this.passingTests = 0;
		this.timeoutFailures = 0;
		this.exceptionFailures = 0;
		this.totalFailures = this.result.getFailureCount();
		this.totalTests = this.result.getRunCount();
		List<Failure> failures = this.result.getFailures();
		for (Failure f : failures) {
			if (isTimeoutFailure(f)) {
				this.timeoutFailures++;
			} else if (f.getException() != null && f.getException() instanceof AssertionError) {
				this.assertFailingTests++;
			} else if (f.getException() != null) {
				this.exceptionFailures++;
			}
		}
		this.passingTests = this.totalTests - this.totalFailures;
	}
	
	private boolean isTimeoutFailure(Failure f) {
		if (f.getException() == null) return false; 
		if (f.getException().getClass().getCanonicalName().compareTo("java.lang.Exception") != 0) return false;
		if (f.getException().getMessage() == null) return false;
		if (f.getException().getMessage().contains("test timed out")) return true;
		return false;
	}
	
	public long getRunTime() {
		return this.result.getRunTime();
	}
	
	public int getTestsCount() {
		return this.totalTests;
	}
	
	public int getRunnedTestsCount() {
		return this.result.getRunCount();
	}
	
	public int getTotalFailures() {
		return this.totalFailures;
	}
	
	public int passingTests() {
		return this.passingTests;
	}
	
	public int getTimedoutTests() {
		return this.timeoutFailures;
	}
	
	public int getExceptionFailingTests() {
		return this.exceptionFailures;
	}
	
	public boolean wasSuccessful() {
		return this.result.wasSuccessful();
	}
	
	public List<Failure> getFailures() {
		return this.result.getFailures();
	}
	
	@Override
	public String toString() {
		String rep = "";
		rep += "Test class runned       : " + this.testClassRunned.getName() + "\n";
		rep += "Total tests             : " + this.totalTests + "\n";
		rep += "Passing tests           : " + this.passingTests + "\n";
		rep += "Runned tests            : " + this.result.getRunCount() + "\n";
		rep += "Failing tests           : " + this.totalFailures + "\n";
		rep += "Timed out tests         : " + this.timeoutFailures + "\n";
		rep += "Exception failing tests : " + this.exceptionFailures + "\n";
		rep += "Assert failing tests    : " + this.assertFailingTests;
		return rep;
	}
	
}
