package bugHunting;

public class RepeatedLine {

	public int getNode() {
		int test = 0;
		test = test; //mutGenLimit 1
		test = test; //mutGenLimit 1
		return test;
	}

}
