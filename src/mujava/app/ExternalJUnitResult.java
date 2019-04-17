package mujava.app;

import java.util.List;

public class ExternalJUnitResult {
	private List<TestResult> testResults;
	private boolean discarded;
	private Exception error;
	
	public ExternalJUnitResult(Exception error) {
		this(null, error);
	}
	
	public ExternalJUnitResult(List<TestResult> testResults) {
		this(testResults, null);
	}
	
	public void merge(ExternalJUnitResult other) {
		if (!testsRunSuccessful() || !other.testsRunSuccessful()) {
			throw new IllegalArgumentException("Both results must be from successful runs");
		}
		if (testResults == null || other.testResults == null) {
			throw new IllegalArgumentException("Both results must be not null");
		}
		for (TestResult tr : testResults) {
			for (TestResult trother : other.testResults) {
				if (tr.getTestClassRunned().getName().compareTo(trother.getTestClassRunned().getName()) == 0) {
					throw new IllegalArgumentException("The same test can't be in both results : " + tr.getTestClassRunned().getName());
				}
			}
		}
		this.testResults.addAll(other.testResults);
	}
	
	private ExternalJUnitResult(List<TestResult> testResults, Exception error) {
		this.testResults = testResults;
		this.error = error;
		for (TestResult r : testResults) {
			if (r.wasDiscarded()) {
				discarded = true;
				break;
			}
		}
	}
	
	public boolean testsRunSuccessful() {
		return this.error == null && !discarded;
	}
	
	public Exception error() {
		return this.error;
	}
	
	public List<TestResult> testResults() {
		return this.testResults;
	}
	
	public boolean wasDiscarded() {
		return discarded;
	}
	
	
}
