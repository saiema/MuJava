package test;

public class JSI_1 {
	private int field1; //mutGenLimit 1
	protected int field2 = this.field1; //mutGenLimit 2
	public int field3; //mutGenLimit 0
	
	
	public void method1(int param1, String param2) {
	} //mutGenLimit 1
	
	protected void method2(int param1, String param2) {
	} //mutGenLimit 2
	
	public void method3(int param1, String param2) {
	} //mutGenLimit 0
	
}
