package reachableVariables;

public class ReachableVariables {
	
	private int a;
	private final int b = 1;
	
	
	public int foo(final int a, int b) {
		int c = a + b;
		final int d = a + b;
		for (int e = 0, f = 0; e < 10; e++) {
			final int g = e + f;
			e = e + 1;
		}
		return a + b + c + d;
	}

}
