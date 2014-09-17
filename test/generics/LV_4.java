package generics;

import java.util.LinkedList;
import java.util.List;

public class LV_4 {

	public void defMethod() {
		List<Integer> atr1 = new LinkedList<Integer>();
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
