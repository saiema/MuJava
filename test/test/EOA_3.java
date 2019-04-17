package test;

public class EOA_3 extends EOA_Base_3 {
	
	public void radiatedMethod(EOA_3 param1) {
		EOA_3 var1 = param1; //mutGenLimit 1
		EOA_3 var2 = auxMethod1(var1, param1); //mutGenLimit 1
		EOA_Base_1 var3 = param1; //mutGenLimit 1
		EOA_Base_2 var4 = (EOA_Base_2) auxMethod2(auxMethod1(var1, param1), new EOA_Base_2(), param1); //mutGenLimit 1
	}
	
	private EOA_3 auxMethod1(EOA_Base_1 param1, EOA_Base_1 param2) {
		return new EOA_3();
	}
	
	private EOA_Base_1 auxMethod2(EOA_Base_1 param1, EOA_Base_2 param2, EOA_3 param3) {
		return param2;
	}
	
	public static void main(String[] args) {
		EOA_3 eoa_3 = new EOA_3();
		eoa_3.radiatedMethod(eoa_3);
	}

}
