package generics;

public class CA_3<T extends IA_NG & IB_NG> {

	public void defMethod(){}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
