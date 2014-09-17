package multiMutations;

public class TAL_3 {
	
	public boolean radiatedMethod(int[] array, int value) {
		boolean found = false; //mutGenLimit 1
		int i = 0; //mutGenLimit 1
		while (i < array.length && !found) {
			if (array[i] == value) {
				found = true; //mutGenLimit 1
			} else {
				i++; //mutGenLimit 1
			} //mutGenLimit 1
		} //mutGenLimit 1
		return found; //mutGenLimit 1
	}

}
