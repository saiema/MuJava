package generics;

import java.util.LinkedList;

public class SW_1<T> {

	public void defMethod(){
		switch((new LinkedList<Integer>()).pop()) {
		
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
