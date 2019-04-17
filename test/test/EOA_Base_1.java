package test;

public class EOA_Base_1 {

	@Override
	public EOA_Base_1 clone() {
		System.out.println("EOA_Base_1#clone()");
		return new EOA_Base_1();
	}
	
}
