package mujava.app;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public class TestResultCollector implements Callable<List<TestResult>> {
	
	private List<TestResult> results;
	private InputStream is;
	private MutantInfo forMutant;
	
	public TestResultCollector(InputStream is, MutantInfo forMutant) {
		this.is = is;
		this.forMutant = forMutant;
		results = new LinkedList<>();
	}

	@Override
	public List<TestResult> call() throws Exception {
		results.addAll(parseResultsFromInputStream(is, forMutant));
		return results;
	}
	
	
	private Collection<? extends TestResult> parseResultsFromInputStream(InputStream is, MutantInfo mi) throws ClassNotFoundException, IOException {
		List<TestResult> results = new LinkedList<>();
		ObjectInputStream in = new ObjectInputStream(is);
		Object o = null;
		try {
			while ((o = in.readObject()) != null) {
				TestResult tr = (TestResult)o;
				tr.refresh();
				results.add(tr);
			}
		} catch (EOFException e) {}
		in.close();
		return results;
	}

}
