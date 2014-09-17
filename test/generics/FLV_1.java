package generics;

import java.util.LinkedList;
import java.util.List;

public class FLV_1 {

	public void defMethod(){
		for(int i = 0;i < 10;i++) {
			List<Integer> lv1 = new LinkedList<Integer>();
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
