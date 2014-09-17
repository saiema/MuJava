package generics;

import java.util.LinkedList;
import java.util.List;

public class FC_2<T> {

	public void defMethod(){
		for (List<List<? super Integer>> l = new LinkedList<List<? super Integer>>(); l.size() < 10; l.add(new LinkedList<Integer>())) {
			
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
