package test;

public class PMD_1 extends PMD_Base {
	protected PMD_1 field1; //mutGenLimit 10000
	
	public void radiatedMethod(PMD_1 param1) {
		PMD_1 var1 = new PMD_1(); //mutGenLimit 13
	} //mutGenLimit 1
	
}
