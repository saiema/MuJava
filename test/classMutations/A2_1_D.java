package classMutations;

import java.util.LinkedList;
import java.util.List;

public class A2_1_D extends A1 {
	protected String atr2;
	public String atr3;
	public int atr4 = 0;
	public List<String> atr5;
	public List<String> atr6 = new LinkedList<String>();
	
	public void radiatedMethod() {
		Integer lv0 = 0; //mutGenLimit 1
		Integer lv1 = 1; //mutGenLimit 1
		lv0 = lv1; //mutGenLimit 1
		lv0 = lv1 + lv0; //mutGenLimit 1
		lv1 = auxMethod(); 
	}
	
	public Integer auxMethod() {
		return 3; //mutGenLimit 1
	}
	
} //mutGenLimit 1
