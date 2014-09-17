package generics;

import java.util.List;

public class CF_3<T, R extends IA_G<T> & IB_G<T>> {
	private List<? super Comparable<? extends T>> atr1;

	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}
	
}
