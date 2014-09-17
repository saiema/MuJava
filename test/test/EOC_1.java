package test;

public class EOC_1 {

	public void radiatedMethod() {
		boolean b1 = 1 == 2; //mutGenLimit 1
		int i1 = 0;
		boolean b2 = 1 == i1; //mutGenLimit 1
		boolean b3 = i1 == 1; //mutGenLimit 1
		Integer int1 = 0;
		boolean b4 = 1 == int1; //mutGenLimit 1
		boolean b5 = int1 == 1; //mutGenLimit 1
		boolean b6 = i1 == int1; //mutGenLimit 1
		boolean b7 = int1 == i1 + 2; //mutGenLimit 1
	}
	
	
}
