package test;

public class ISI_1 extends ISI_Base_1 {
	protected String atr1;
	public String atr2;
	private String atr3;
	protected String atr4;
	
	public void radiatedMethod() {
		String atr1_local = atr1; //mutGenLimit 1
		String atr2_local = atr2; //mutGenLimit 1
		String atr3_local = atr3; //mutGenLimit 1
		String atr1 = this.atr1; //mutGenLimit 1
		String atr2 = this.atr2; //mutGenLimit 1
		String atr3 = this.atr3; //mutGenLimit 1
		int atr4 = this.intMethod_1(); //mutGenLimit 1
		int atr5 = this.intMethod_2(); //mutGenLimit 1
		int atr6 = this.intMethod_3(); //mutGenLimit 1
		int atr4_local = intMethod_1(); //mutGenLimit 1
		int atr5_local = intMethod_2(); //mutGenLimit 1
		int atr6_local = intMethod_3(); //mutGenLimit 1
		String stringVar_1 = stringMethod_1(); //mutGenLimit 1
		String stringVar_2 = stringMethod_2(); //mutGenLimit 1
		String stringVar_3 = stringMethod_3(1, 2); //mutGenLimit 1
		String stringVar_4 = stringMethod_4(1, 2); //mutGenLimit 1
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
