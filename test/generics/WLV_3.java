package generics;

import java.util.LinkedList;
import java.util.List;

public class WLV_3<T, R> {

	public void defMethod(List<Comparable<? extends Number>> param1){
		while(true) {
			List<Comparable<? extends Number>> lv1 = new LinkedList<Comparable<? extends Number>>();
			WLV_3<? extends T, R> lv2 = new WLV_3<T, R>();
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
