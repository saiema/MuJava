package mujava.app;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public class TestResultCollector implements Callable<List<TestResult>> {
	
	private List<TestResult> results;
	private MutantInfo forMutant;
	private ServerSocket sc;
	private Socket client;
	
	public TestResultCollector(int port, MutantInfo forMutant) throws IOException {
		this.forMutant = forMutant;
		results = new LinkedList<>();
		sc = new ServerSocket(port);
		sc.setReuseAddress(true);
	}
	
	public TestResultCollector(MutantInfo forMutant) throws IOException {
		this(0, forMutant);
	}
	
	public int getPort() {
		return sc.getLocalPort();
	}
	
	/**
	 * Only for fiercely closing the connection
	 * @throws IOException
	 */
	public void closeSocket() throws IOException {
		sc.close();
	}

	@Override
	public List<TestResult> call() throws Exception {
		client = sc.accept();
		InputStream is = client.getInputStream();
		results.addAll(parseResultsFromInputStream(is, forMutant));
		sc.close();
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
