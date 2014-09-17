package generics;

public class CA_9 implements IA_G<Comparable<Integer>> {

	public void defMethod(){}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
