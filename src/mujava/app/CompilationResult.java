package mujava.app;

public class CompilationResult {
	
	private final Exception error;
	private final boolean compilationSuccessful;
	private final String warnings;
	
	public CompilationResult(Exception error) {
		this(error, error == null, null);
	}
	
	public CompilationResult (String warnings) {
		this(null, true, warnings);
	}
	
	private CompilationResult(Exception error, boolean compilationSuccessful, String warnings) {
		this.error = error;
		this.compilationSuccessful = compilationSuccessful;
		this.warnings = warnings;
	}
	
	public boolean compilationSuccessful() {
		return this.compilationSuccessful;
	}
	
	public Exception error() {
		return this.error;
	}
	
	public String getWarnings() {
		return this.warnings;
	}

}
