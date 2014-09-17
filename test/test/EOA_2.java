package test;

public class EOA_2 extends EOA_Base_2 {
	
	public void radiatedMethod(EOA_2 param1) {
		EOA_2 var1 = param1; //mutGenLimit 1
		EOA_2 var2 = auxMethod1(var1, param1); //mutGenLimit 1
	}
	
	private EOA_2 auxMethod1(EOA_Base_1 param1, EOA_Base_1 param2) {
		return new EOA_2();
	}

}
