package generics;

import java.util.LinkedList;
import java.util.List;

public class FLV_2 {

	public void defMethod(){
		for(int i = 0;i < 10;i++) {
			List<Comparable<? extends Number>> lv1 = new LinkedList<Comparable<? extends Number>>();
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
