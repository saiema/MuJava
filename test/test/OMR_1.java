package test;

import java.util.LinkedList;
import java.util.List;

public class OMR_1 {
	
	public void radiatedMethod(int a, int b, int c) {
		int var1 = a + b + c;
	} //mutGenLimit 300
	
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
