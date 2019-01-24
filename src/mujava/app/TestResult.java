package mujava.app;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import mujava.junit.runner.DiscardedException;

public class TestResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3718457180972664524L;
	private Result result;
	private boolean discarded;
//	private MutantInfo analyzedMutant;
	private Class<?> testClassRunned;
	private Map<String, Boolean> simpleTestResults;
	private transient int totalTests = 0;
	private transient int assertFailingTests = 0;
	private transient int totalFailures = 0;
	private transient int passingTests = 0;
	private transient int timeoutFailures = 0;
	private transient int exceptionFailures = 0;
	
	
	public TestResult(Result result, Class<?> testClassRunned, Map<String, Boolean> simpleTestResults) {
		this.result = result;
		this.testClassRunned = testClassRunned;
		this.simpleTestResults = simpleTestResults;
		refresh();
	}
	
	public Map<String, Boolean> getTestsSimpleResults() {
		return simpleTestResults;
	}
	
	public void refresh() {
		if (discarded) return;
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
			if (wasDiscarded(f)) {
				this.discarded = true;
				break;
			}
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
	
	private boolean wasDiscarded(Failure f) {
		if (f.getException() == null) return false;
		if (f.getException().getClass().getCanonicalName().compareTo(DiscardedException.class.getCanonicalName()) == 0) {
			return true;
		}
		return false;
	}
	
	private boolean isTimeoutFailure(Failure f) {
		if (f.getException() == null) return false; 
		if (f.getException().getClass().getCanonicalName().compareTo("org.junit.runners.model.TestTimedOutException") == 0) return true;
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
	
	public Class<?> getTestClassRunned() {
		return testClassRunned;
	}
	
//	public void setMutant(MutantInfo mi) {
//		analyzedMutant = mi;
//	}
	
	public boolean[] testResultsAsArray() {
		boolean[] failedTests = new boolean[simpleTestResults.size()];
		int i = 0;
		for (Entry<String, Boolean> entry : simpleTestResults.entrySet()) {
			failedTests[i] = !entry.getValue();
			i++;
		}
		return failedTests;
	}
	
	public boolean wasDiscarded() {
		return discarded;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		if (discarded) {
			sb.append("Discarded!\n");
		} else {
			sb.append("Test class runned       : ").append(testClassRunned.getName()).append("\n");
			sb.append("Total tests             : ").append(totalTests).append("\n");
			sb.append("Passing tests           : ").append(passingTests).append("\n");
			sb.append("Runned tests            : ").append(result.getRunCount()).append("\n");
			sb.append("Failing tests           : ").append(totalFailures).append("\n");
			sb.append("Timed out tests         : ").append(timeoutFailures).append("\n");
			sb.append("Exception failing tests : ").append(exceptionFailures).append("\n");
			sb.append("Assert failing tests    : ").append(assertFailingTests);
		}
		return sb.toString();
	}
	
}
