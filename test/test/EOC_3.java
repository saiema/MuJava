package test;

import java.util.List;

public class EOC_3 {
	private List<Integer> list;

	public void radiatedMethod() {
		Integer int1 = 0;
		boolean b1 = new EOC_3().getMe().getList().get(0) == new Integer(1 + int1); //mutGenLimit 1
	}
	
	public EOC_3 getMe() {
		return this;
	}
	
	public List<Integer> getList() {
		return list;
	}
	
	
	
	
}
