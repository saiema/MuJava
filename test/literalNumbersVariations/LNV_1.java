package literalNumbersVariations;

public class LNV_1 {

	public int radiatedMethod(int a, int b) {
		int localNumber1 = (int) (a * 3.2f + ((b^3)/42D) + 2);
		int localNumber2 = (int) (1 + 35D - 2.387f);
		return (int) (localNumber1 + localNumber2); //mutGenLimit 1
	}
	
}
