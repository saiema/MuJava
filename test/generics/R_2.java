package generics;

import java.util.LinkedList;
import java.util.List;

public class R_2 {

	public List<?> defMethod(){
		return new LinkedList<List<Comparable<? extends Number>>>();
	}

	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}
	
}
