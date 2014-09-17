package generics;

import java.util.LinkedList;

public class SW_2<T> {

	public void defMethod(){
		switch((new LinkedList<Comparable<? extends SW_2<? super IB_NG>>>()).size()) {
		
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
