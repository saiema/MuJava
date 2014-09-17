package test;

public class EOA_1 extends EOA_Base_2 {
	
	public void radiatedMethod(EOA_1 param1) {
		EOA_1 var1 = param1; //mutGenLimit 1
		EOA_1 var2 = auxMethod1(var1, param1); //mutGenLimit 1
	}
	
	private EOA_1 auxMethod1(EOA_Base_1 param1, EOA_Base_1 param2) {
		return new EOA_1();
	}
	
	//@Override
	public EOA_1 clone() {
		return new EOA_1();
	}

}
