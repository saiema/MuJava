package test;

public class OAN_1 extends OAN_BASE {
	
	public void radiatedMethod() {
		method1A(0, "hola", 0.3f); //mutGenLimit 1
		int var1 = method1A(1+2+method1A(0.5f, "chau", 1)); //mutGenLimit 2
		int var2 = method1A("chacha") + method1A(0.5f, "chuchu", method1A()+method1A(2)); //mutGenLimit 1000
	}

}
