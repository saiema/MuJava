package generics;

import java.util.ArrayList;
import java.util.LinkedList;

public class WC_2<T> {

	public void defMethod(){
		while(new LinkedList<Comparable<? extends Integer>>() != null &&  new ArrayList<Comparable<? extends Integer>>() != null) {
			
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
