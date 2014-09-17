package generics;

import java.util.List;

public class LV_3 <T, R extends IA_G<T> & IB_G<T>> {

	public void defMethod() {
		List<? super Comparable<? extends Number>> atr1;
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
