package generics;

import java.util.List;

public class CA_IC_3 {

	public void defMethod(){}
	
	public class IC1<T> extends AA_G<Comparable<? extends T>> implements IA_G<Comparable<? extends List<Integer>>>, IB_NG, IB_G<T> {} 

	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}
	
}