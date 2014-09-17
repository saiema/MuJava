package generics;

import java.util.LinkedList;
import java.util.List;

public class WLV_1 {

	public void defMethod(){
		while(true) {
			List<Integer> lv1 = new LinkedList<Integer>();
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
