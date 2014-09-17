package test;

import java.util.Comparator;

public class ISD_1 extends ISI_Base_2 {
	protected String atr1;
	public String atr2;
	private String atr3;
	protected String atr4;
	
	public void radiatedMethod() {
		String atr1 = "";
		Comparator<String> stringVar_2 = super.atr1.trim().CASE_INSENSITIVE_ORDER; //mutGenLimit 1
		String stringVar_3 = super.stringMethod_3(1, 2).concat("lalala").intern(); //mutGenLimit 1
		Comparator<String> stringVar_4 = super.stringMethod_4(1, 2).CASE_INSENSITIVE_ORDER; //mutGenLimit 1
	}
	
	public int intMethod_1() {
		return 0;
	}
	
	protected int intMethod_2() {
		return 0;
	}
	
	private int intMethod_3() {
		return 0;
	}
	
	public int intMethod_4() {
		return 0;
	}
	
	public String stringMethod_1() {
		return "";
	}
	
	public String stringMethod_2() {
		return "";
	}
	
	public String stringMethod_3(int param1, int param2) {
		return "" + param1 + " | " + param2 + "";
	}
	
	public String stringMethod_4(int param1, int param2) {
		return "" + param1 + " | " + param2 + "";
	}

}
