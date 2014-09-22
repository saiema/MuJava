package classMutations;

public class A2_1 extends A1 { //mutGenLimit 1
	
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
	
}
