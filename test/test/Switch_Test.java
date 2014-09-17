package test;

public class Switch_Test {

	public int radiatedMethod(int a, int b, int c) {
		int d = a + b + c; //mutGenLimit 1
		int result = 0;
		switch(d) {
			case 0 :
			case 1 :
			case 2 :
			case 3 : {
				result = a + b; //mutGenLimit 1
				a++; //mutGenLimit 1
				break;
			}
			case 4 : result = b; //mutGenLimit 1
			case 5 : break;
			case 6 : {
				result = c + 1; //mutGenLimit 1
				b++;
				break;
			}
		}
		return result; //mutGenLimit 1
	}
	
}
