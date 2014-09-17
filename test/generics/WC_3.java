package generics;

import java.util.LinkedList;
import java.util.List;


public class WC_3<T> {

	public Comparable<? extends T> defMethod(List<? extends T> param1){
		while(param1 != new LinkedList<T>()) {
			param1 = new LinkedList<T>();
		}
		return null;
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
