package test;

public class EOA_4 extends EOA_Base_3 {
	
	public void radiatedMethod(EOA_4 param1) {
		EOA_Base_1 var1 = new EOA_Base_1();
		EOA_4 var2 = (EOA_4) auxMethod2(var1, (EOA_Base_2) var1, param1); //mutGenLimit 1
		boolean var3 = (param1 = var2) == null; //mutGenLimit 1
	}
	
	private EOA_Base_1 auxMethod2(EOA_Base_1 param1, EOA_Base_2 param2, EOA_4 param3) {
		return param2;
	}

}
