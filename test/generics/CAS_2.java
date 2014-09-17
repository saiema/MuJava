package generics;

import java.util.LinkedList;
import java.util.List;

public class CAS_2<T> {
	private T atr1;

	public void defMethod(){
		List<? extends Number> lv1 = new CAS_2<Comparable<? extends T>>().atr1 != null?new LinkedList<Integer>():new LinkedList<Float>();
	}

	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}
	
}
