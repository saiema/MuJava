package test;

public class EOC_2 {

	public void radiatedMethod() {
		boolean b1 = new Integer(0) == new Integer(1); //mutGenLimit 1
		int i1 = 2;
		boolean b2 = new Integer(0) == i1; //mutGenLimit 1
		boolean b3 = i1 == new Integer(1); //mutGenLimit 1
		Integer int1 = 1, int2 = 2;
		boolean b4 = int1 == (int2 = new Integer(i1)); //mutGenLimit 1
		boolean b5 = new Integer(int2+int1) == i1 && i1 == int1 && int1 == new Integer(0) + new Integer(2); //mutGenLimit 1
	}
	
	
}
