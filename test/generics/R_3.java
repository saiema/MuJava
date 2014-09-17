package generics;

import java.util.List;
import java.util.Map;

public class R_3<R, S, T> {

	public R_3<?, ?, ?> defMethod(){
		return new R_3<Map<Comparable<? extends R>, ? extends Comparable<T>>, List<List<Comparable<? extends S>>>, T>();
	}

	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}
	
}
