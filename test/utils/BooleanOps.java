package utils;

public class BooleanOps {

	public static boolean and(boolean a, boolean b) {
		boolean auxA = a; //mutGenLimit 3
		boolean auxB = b; //mutGenLimit 1
		if (!auxA || !auxB) { //mutGenLimit 1
			return false; //mutGenLimit 1
		}
		return true;
	}
	
	public static boolean or(boolean a, boolean b) {
		boolean auxA = a;
		boolean auxB = b;
		return !(!auxA && !auxB);
	}
	
	public static boolean xor(boolean a, boolean b) {
		boolean auxA = a;
		boolean auxB = b;
		boolean allTrue = !(!auxA || !auxB); //mutGenLimit 1
		boolean allFalse = (!auxA && !auxB); //mutGenLimit 2
		return !(allTrue || allFalse); //mutGenLimit 1
	}
	
	public static boolean xnor(boolean a, boolean b) {
		boolean auxA = a;
		boolean auxB = b;
		boolean isXnor = !(!auxA || !auxB) || (!auxA && !auxB); //mutGenLimit 2
		return isXnor;
	}
	
}
