package generics;

public class CA_IC_1 {

	public void defMethod(){}
	
	public class IC1<T> {} 
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
