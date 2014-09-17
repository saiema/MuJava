package generics;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FC_4<T> {

	public void defMethod(Map<String, List<Comparable<T>>> param1){
		for (Entry<String, List<Comparable<T>>> entry : param1.entrySet()) {
			for (Comparable<T> o : entry.getValue()) {
				
			}
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
