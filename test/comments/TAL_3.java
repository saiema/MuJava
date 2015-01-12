package comments;

//comment 0
public class TAL_3 {
	
	//comment 1
	private int field1 = 0;
	//comment 2
	
	//comment 3
	public boolean radiatedMethod(int[] array, int value) {
		//comment 4
		boolean found = false; //mutGenLimit 1
		//comment 5
		int i = 0; //mutGenLimit 1
		//comment 6
		while (i < array.length && !found) { //mutGenLimit 1
			//comment 7
			if (array[i] == value) { //mutGenLimit 1
				//comment 8
				found = true; //mutGenLimit 1
				//comment 9
			} else {
				//comment 10
				i++;
				//comment 11
			}
			//comment 12
		}
		//comment 13
		return found; //mutGenLimit 1
		//comment 14
	}

}
