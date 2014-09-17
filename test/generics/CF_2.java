package generics;

import java.util.List;

public class CF_2<T> {
	private List<? extends T> atr1;

	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}
	
}
