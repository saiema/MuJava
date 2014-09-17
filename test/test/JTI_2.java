package test;

public class JTI_2 extends JTI_Base {
	private int field1;
	protected int field2;
	public int field3;
	
	
	public int radiatedMethod(int field1, int field2) {
		int field4 = 0;
		int field5 = 0;
		int field6 = 0;
		radiatedMethod(field1, field2); //mutGenLimit 1
		radiatedMethod(field1 + field2, field3 + field4); //mutGenLimit 2
		int var1 = field1 + radiatedMethod(field2 + radiatedMethod(field3, field4), radiatedMethod(field5, field6)); //mutGenLimit 3
		return var1 + radiatedMethod(var1 + field2 + field4, field3); //mutGenLimit 100
	}
	
}
