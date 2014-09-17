package generics;

import java.util.List;

public class MS_4<R, S extends IA_NG & IB_G<R>> {

	public MS_4<? extends R, S> defMethod(List<Integer> param1, MS_4<R, ? extends S> param2){
		return new MS_4<R,S>();
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}
	
}
