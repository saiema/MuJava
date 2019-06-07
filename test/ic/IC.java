package ic;

public class IC {

	public int foo() {
		int i = 42; //mutGenLimit 1
		boolean b = true; //mutGenLimit 1
		boolean c = !true; //mutGenLimit 1
		float d = 32.3f; //mutGenLimit 1
		double e = -3.0d; //mutGenLimit 1
		int f = 5; //mutGenLimit 1
		Integer F = -3; //mutGenLimit 1
		return i + 3; //mutGenLimit 1
	}

}
