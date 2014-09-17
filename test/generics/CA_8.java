package generics;

public class CA_8 implements IA_G<Integer> {

	public void defMethod(){}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
