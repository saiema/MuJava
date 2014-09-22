package multiMutations;

public class TAL_3 {
	
	public boolean radiatedMethod(int[] array, int value) {
		boolean found = false; //mutGenLimit 1
		int i = 0; //mutGenLimit 1
		while (i < array.length && !found) { //mutGenLimit 1
			if (array[i] == value) { //mutGenLimit 1
				found = true; //mutGenLimit 1
			} else {
				i++; //mutGenLimit 1
			}
		}
		return found; //mutGenLimit 1
	}

}
