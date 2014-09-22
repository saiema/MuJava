package utils;
import java.util.LinkedList;
import java.util.List;

public class IntWrapper<T extends List<? extends Comparable<T>>, R extends Comparable<T>> {
	public Integer val; //mutGenLimit 2
	
	public IntWrapper(Integer val) {
		this.val = val;
	}
	
	public IntWrapper(int val) {
		this.val = val;
	}
	
	public int value() {
		return this.val;
	}
	
//	public IntWrapper<? extends T, ?> gen(List<? extends String> list0) {
//		List<Integer> list1 = new LinkedList<Integer>();
//		List<? extends Comparable<T>> list2 = new LinkedList<Comparable<T>>();
//		return null;
//	}
	
	public IntWrapper<? extends T, ?> gen(List<? extends String> list0) {
		List<Integer> list1 = new LinkedList<Integer>();
		return null;
	}
	
	public void setValue(Integer newVal) {
		this.val = newVal;
	}
	
	public void setValue(int newVal) {
		this.val = newVal;
	}
	
	public String toString() {
		return this.val.toString();
	}
	
	public IntWrapper add(IntWrapper o) { //mutGenLimit 1
		int currentValue = this.value();
		int otherValue = o.value();
		int result = currentValue + otherValue;
		IntWrapper ret = new IntWrapper(result);
		return ret;
	}
	
}
