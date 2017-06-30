package mujava.app;

import java.util.List;

public class ExternalJUnitResult {
	private List<TestResult> testResults;
	private Exception error;
	
	public ExternalJUnitResult(Exception error) {
		this(null, error);
	}
	
	public ExternalJUnitResult(List<TestResult> testResults) {
		this(testResults, null);
	}
	
	private ExternalJUnitResult(List<TestResult> testResults, Exception error) {
		this.testResults = testResults;
		this.error = error;
	}
	
	public boolean testsRunSuccessful() {
		return this.error == null;
	}
	
	public Exception error() {
		return this.error;
	}
	
	public List<TestResult> testResults() {
		return this.testResults;
	}
	
	
}
