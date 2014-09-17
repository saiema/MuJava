package generics;

import java.util.LinkedList;
import java.util.List;

public class LV_5 <T, R extends IA_G<T> & IB_G<T>> {

	public void defMethod() {
		List<? super Comparable<? extends Number>> atr1 = new LinkedList<Comparable<?>>();
		LV_5 atr2 = new LV_5();
		LV_5<? extends T, R> atr3 = new LV_5<T,R>();
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
