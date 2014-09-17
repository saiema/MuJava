package test;

public class JTI_1 extends JTI_Base {
	private int field1;
	protected int field2;
	public int field3;
	
	
	public void radiatedMethod(int field1, int field2) {
		int field4 = 0;
		int field5 = 0;
		int field6 = 0;
		field1 = field4; //mutGenLimit 1
		int var1 = field2; //mutGenLimit 1
		int var2 = field1 + field2 + field3 + field4 + field5 + field6; //mutGenLimit 3
	}
	
}
