package generics;

public class CA_IC_2 {

	public void defMethod(){}
	
	public class IC1<T extends IA_NG & IB_NG> {} 
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
