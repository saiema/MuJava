package generics;

public class CA_4<T extends Comparable<T>> {

	public void defMethod(){}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
