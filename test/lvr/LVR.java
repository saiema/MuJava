package lvr;

public class LVR {
	
	public String foo() {
		int a = 5 + 1 + 0 + (-1); //mutGenLimit 1
		float b = 1.0f + 0.0f + (-1.0f) + 1.2f; //mutGenLimit 1
		String c =  "It's " + true + " or " + !false + " or " + false + " but not " + !true; //mutGenLimit 1
		return "Hi " + " ( " + 5.0f + 1.0f + " alalala: " + 0l + (-1l) + 42 * (5l + 1l) + ")"; //mutGenLimit 1
	}

}
