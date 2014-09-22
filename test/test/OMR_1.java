package test;

import java.util.LinkedList;
import java.util.List;

public class OMR_1 {
	
	public void radiatedMethod(int a, int b, int c) { //mutGenLimit 300
		int var1 = a + b + c;
	}
	
	public int radiatedMethod(int a) {
		int var2 = a + 3;
		return var2;
	}
	
	public void radiatedMethodd(int a) {
		int var3 = a + 1;
	}
	
	private void radiatedMethod() {
		List<String> list1 = new LinkedList<String>();
		list1.add("lame!");
	}

}
