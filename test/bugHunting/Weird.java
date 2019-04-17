package bugHunting;

public class Weird {
	
	private String msg;
	
	private Weird(String msg) {
		this.msg = msg;
	}
	
	public static Weird createWeird(String msg) {
		return new Weird(msg);
	}
	
	@Override
	public String toString() {
		return msg;
	}

}
