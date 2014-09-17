package generics;

import java.util.List;

public class FC_3<T> {

	public void defMethod(List<T> param1){
		for (T t : param1) {
			
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
