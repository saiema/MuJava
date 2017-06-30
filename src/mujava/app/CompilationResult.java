package mujava.app;

public class CompilationResult {
	
	private final Exception error;
	private final boolean compilationSuccessful;
	
	public CompilationResult(Exception error) {
		this(error, error == null);
	}
	
	private CompilationResult(Exception error, boolean compilationSuccessful) {
		this.error = error;
		this.compilationSuccessful = compilationSuccessful;
	}
	
	public boolean compilationSuccessful() {
		return this.compilationSuccessful;
	}
	
	public Exception error() {
		return this.error;
	}

}
