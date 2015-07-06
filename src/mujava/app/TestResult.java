package mujava.app;

import java.util.List;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestResult {

	private Result result;
	private int totalTests = 0;
	private int assertFailingTests = 0;
	private int totalFailures = 0;
	private int passingTests = 0;
	private int timeoutFailures = 0;
	private int exceptionFailures = 0;
	//private int ignoredTests = 0;
	
	public TestResult(Result result) {
		this.result = result;
		this.totalFailures = this.result.getFailureCount();
		//this.ignoredTests = this.result.getIgnoreCount();
		this.totalTests = this.result.getRunCount();// + this.ignoredTests;
		List<Failure> failures = this.result.getFailures();
		for (Failure f : failures) {
			if (f.getException().getClass().getCanonicalName().compareTo("java.lang.Exception") == 0 && f.getException().getMessage().startsWith("test timed out")) {
				this.timeoutFailures++;
			} else if (f.getException() != null && f.getException() instanceof AssertionError) {
				this.assertFailingTests++;
			} else if (f.getException() != null) {
				this.exceptionFailures++;
			}
		}
		this.passingTests = this.totalTests - this.totalFailures;
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
	
//	public int getIgnoredTests() {
//		return this.ignoredTests;
//	}
	
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
		rep += "Total tests : " + this.totalTests + "\n";
		rep += "Passing tests : " + this.passingTests + "\n";
		rep += "Runned tests : " + this.getRunnedTestsCount() + "\n";
		//rep += "Ignored tests : " + this.ignoredTests + "\n";
		rep += "Failing tests : " + this.totalFailures + "\n";
		rep += "Timed out tests : " + this.timeoutFailures + "\n";
		rep += "Exception failing tests : " + this.exceptionFailures + "\n";
		rep += "Assert failing tests : " + this.assertFailingTests;
		return rep;
	}
	
}
