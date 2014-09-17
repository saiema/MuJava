package generics;

import java.util.LinkedList;
import java.util.List;

public class FC_1<T> {

	public void defMethod(){
		int i = 0;
		for (List<Integer> l = new LinkedList<Integer>(); l.size() < 10; l.add(i)) {
			i++;
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
