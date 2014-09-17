package generics;

import java.util.LinkedList;

public class WC_1<T> {

	public void defMethod(){
		while(new LinkedList<Integer>() != null) {
			
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
