package bugHunting;

public class PRVOMethodCall {

	public String returnAString() {
		return "";
	}
	
	public int returnAnInt() {
		return 0;
	}
	
	public Integer returnAnInteger() {
		return new Integer(0);
	}
	
	
	public int radiatedMethod(int var1) {
		int localVar1 = 90;
		int anIntValue = returnAnInt(); //mutGenLimit 2
		return anIntValue + 2 + returnAnInteger() + returnAString().length() + Integer.MAX_VALUE + localVar1; //mutGenLimit 2
	}

}
