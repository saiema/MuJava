package generics;

public class CA_5<T extends Comparable<? extends IA_NG>> {

	public void defMethod(){}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
