package generics;

import java.util.List;

public class LV_2<T> {

	public void defMethod() {
		List<? extends T> atr1;
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
