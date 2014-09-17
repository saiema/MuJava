package generics;

import java.util.LinkedList;
import java.util.List;

public class R_1 {

	public List<?> defMethod(){
		return new LinkedList<Integer>();
	}

	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}
	
}
