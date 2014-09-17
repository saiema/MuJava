package generics;

import java.util.LinkedList;
import java.util.List;

public class FLV_3<T, R> {

	public void defMethod(List<Comparable<? extends Number>> param1){
		for(int i = 0;i < 10;i++) {
			List<Comparable<? extends Number>> lv1 = new LinkedList<Comparable<? extends Number>>();
			FLV_3<? extends T, R> lv2 = new FLV_3<T, R>();
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
