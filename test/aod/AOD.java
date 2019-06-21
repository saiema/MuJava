package aod;

public class AOD {
	
	
	public int foo() {
		int a = 3 + 4; //mutGenLimit 1
		boolean b = Integer.toString(a).isEmpty() || a + 4 == 10; //mutGenLimit 1
		return a + 3 > 10 && b?4 * 8 + 10:Integer.toString(a).length() + 82; //mutGenLimit 1
	}

}
