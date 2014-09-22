package multiMutations;

public class TAL_2 {
	
	public void radiatedMethod() {
		boolean b1 = true;
		boolean b2 = false;
		boolean b3 = b1 && b2;
		if (b3) { //mutGenLimit 1
			b1 = b2; //mutGenLimit 1
		} else {
			b1 = true; //mutGenLimit 1
		}
	}

}
