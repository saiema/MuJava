package generics;

public class CA_7<T extends Comparable<? super IA_NG>> implements IA_NG{

	public void defMethod(){}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
