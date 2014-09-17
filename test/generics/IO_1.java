package generics;

import java.util.LinkedList;
import java.util.List;

public class IO_1 {

	public void defMethod(List<?> param1){
		boolean lv1 = param1 instanceof LinkedList<?>;
	}

	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}
	
}
