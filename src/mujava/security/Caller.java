package mujava.security;

public abstract class Caller {

	private int uniqueID;
	
	protected Caller(int id) {
		this.uniqueID = id;
	}
	
	protected Caller() {
		this.uniqueID = -1;
	}

	public String callerID() {
		if (this.uniqueID == -1) this.uniqueID = hashCode();
		return this.getClass().getName() + "[" + this.uniqueID + "]";
	}
}
