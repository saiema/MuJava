package test;

public class JSD_1 {
	private static int field1; //mutGenLimit 1
	protected static int field2 = JSD_1.field1; //mutGenLimit 2
	public static int field3; //mutGenLimit 0
	
	
	public static void method1(int param1, String param2) { //mutGenLimit 1
	}
	
	protected static void method2(int param1, String param2) { //mutGenLimit 2
	}
	
	public static void method3(int param1, String param2) { //mutGenLimit 0
	}
	
}
