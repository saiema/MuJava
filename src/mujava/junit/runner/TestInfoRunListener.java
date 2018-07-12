package mujava.junit.runner;

import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class TestInfoRunListener extends RunListener {
	
	private Map<String, Boolean> testResults;
	
	public TestInfoRunListener(Map<String, Boolean> testResults) {
		this.testResults = testResults;
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		testResults.put(failure.getDescription().getDisplayName(), Boolean.FALSE);
	}

	@Override
	public void testFinished(Description description) throws Exception {
		String displayName = description.getDisplayName();
		if (!testResults.containsKey(displayName)) {
			testResults.put(displayName, Boolean.TRUE);
		}
	}
	
	public Map<String, Boolean> getResults() {
		return testResults;
	}
	
}
