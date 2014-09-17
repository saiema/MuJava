package list;

public class ListOps {

	//public static boolean and(List<Boolean> l) {
	public static boolean and(boolean[] l) {
		boolean res = true;
		for (int b = 0; b < l.length; b++) {
			res &= l[b]; //mutGenLimit 1
		}
		return res;
	}
	
	//public static boolean or(List<Boolean> l) {
	public static boolean or(boolean[] l) {
		boolean res = false;
		for (int b = 0; b < l.length; b++) {
			res |= l[b]; //mutGenLimit 1
		}
		return res;
	}
	
	//public static Integer sum(List<Integer> l) {
	public static Integer sum(int[] l) {
		Integer res = 0;
		for (int i = 0; i < l.length; i++) {
			res += l[i]; //mutGenLimit 1
		}
		return res;
	}
	
	//public static Integer mult(List<Integer> l) {
	public static Integer mult(int[] l) {
		Integer res = l.length==0?0:1;
		for (int i = 0; i < l.length ; i++) {
			res *= l[i];
		}
		return res;
	}
	
}
