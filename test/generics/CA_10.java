package generics;

import java.util.List;

public class CA_10 implements IA_G<Comparable<? extends List<Integer>>> {

	public void defMethod(){}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
