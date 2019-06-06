package crcr;

public class CRCR {
	
	public int foo() {
		int a = 5 + 1 + 0 + (-1); //mutGenLimit 1
		float b = 5.0f + 1.0f + 0.0f + (-1.0f) + 1.2f; //mutGenLimit 1
		double c = 5.0d + 1.0d + 0.0d + (-1.0d) + 1.2d; //mutGenLimit 1
		long d = 5l + 1l + 0l + (-1l) + 3l; //mutGenLimit 1
		return (int) (a + c + 3 * d); //mutGenLimit 1
	}
	
	public int foo2() {
		int a = 5 + 1 + 0 + (-1);
		float b = 5.0f + 1.0f + 0.0f + (-1.0f) + 1.2f;
		double c = 5.0d + 1.0d + 0.0d + (-1.0d) + 1.2d;
		long d = 5l + 1l + 0l + (-1l) + 3l;
		return (int) (a + c + 3 * d);
	}

}
