package generics;

import java.util.LinkedList;
import java.util.List;

public class CAS_1 {

	public void defMethod(boolean param1){
		List<? extends Number> lv1 = param1?new LinkedList<Integer>():new LinkedList<Float>();
	}

	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}
	
}
