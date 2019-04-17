package bugHunting.arrays;

import bugHunting.Weird;

public class ArrayInitialization {
	
	private Weird w1 = Weird.createWeird("Omega weird");
	
	public void foo() {
//		int[] array;
//		int data = 1;
//		array = new int[] {data}; //mutGenLimit 1
		Weird[] array;
		Weird weird = Weird.createWeird("Super weird");
		array = new Weird[] {weird}; //mutGenLimit 1
	}

}