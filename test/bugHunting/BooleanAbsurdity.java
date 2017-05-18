package bugHunting;

public class BooleanAbsurdity {
	
	boolean a = false;
	
	public boolean foo() {
		int b = 2;
		int c = 38;
		boolean a = true;
		if (a && b > c) {
			return this.a;
		} else if ((a || this.a) && (b >= 0 || c == 2)) {
			return true;
		}
		return false;
	}
	
	
	public boolean bar() {
		int b = 2;
		int c = 38;
		boolean a = true;
		if (a && b > c) {
			return this.a;
		} else if ((a || this.a) && (b >= 0 || c == 2)) {
			return true;
		}
		return false;
	}
	
	

}
