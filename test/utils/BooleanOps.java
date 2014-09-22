package utils;

public class BooleanOps {
	
//	public static void main(String[] args) {
//		System.out.println("and false && true = false 	: " + !and(false,true)+'\n');
//		System.out.println("and true && false = false 	: " + !and(true,false)+'\n');
//		System.out.println("and false && false = false 	: " + !and(false,false)+'\n');
//		System.out.println("and true && true = true 	: " + and(true,true)+'\n');
//		
//		System.out.println("or false && true = true 	: " + or(false,true)+'\n');
//		System.out.println("or true && false = true 	: " + or(true,false)+'\n');
//		System.out.println("or true && true = true 		: " + or(true,true)+'\n');
//		System.out.println("or false && false = false 	: " + !or(false,false)+'\n');
//		
//		System.out.println("xor false && true = true 	: " + xor(false,true)+'\n');
//		System.out.println("xor true && false = true 	: " + xor(true,false)+'\n');
//		System.out.println("xor true && true = false 	: " + !xor(true,true)+'\n');
//		System.out.println("xor false && false = false 	: " + !xor(false,false)+'\n');
//		
//		System.out.println("xnor false && true = false 	: " + !xnor(false,true)+'\n');
//		System.out.println("xnor true && false = false 	: " + !xnor(true,false)+'\n');
//		System.out.println("xnor true && true = true 	: " + xnor(true,true)+'\n');
//		System.out.println("xnor false && false = false 	: " + xnor(false,false)+'\n');
//	}

	public static boolean and(boolean a, boolean b) {
		boolean auxA = a; //mutGenLimit 0
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
		boolean allFalse = (!auxA && !auxB); //mutGenLimit 0
		return !(allTrue || allFalse); //mutGenLimit 1
	}
	
	public static boolean xnor(boolean a, boolean b) {
		boolean auxA = a;
		boolean auxB = b;
		boolean isXnor = !(!auxA || !auxB) || (!auxA && !auxB); //mutGenLimit 1
		return isXnor;
	}
	
}
