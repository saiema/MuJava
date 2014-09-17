package test;

public class JTD_3 {
	private int field1;
	private int field2;
	private int field3;
	
	public int radiatedMethod() {
		int var1 = this.field1; //mutGenLimit 2
		int var2 = this.method1(field1, field2); //mutGenLimit 2
		int var3 = this.method1(this.field1, field2); //mutGenLimit 2
		int var4 = this.method1(this.method1(this.field1, this.method2(this.field2, this.field3)), 0); //mutGenLimit 2
		int var5 = var1+var2+var3+var4;
		return this.method1(this.field1, var5); //mutGenLimit 2
	}
	
	private int method1(int param1, int param2) {
		return param1+param2;
	}
	
	private int method2(int param1, int param2) {
		return param1-param2;
	}

}
