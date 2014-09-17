package utils;

public class Set {
	private int maxElems;
	private int set;
	private int size;
	
	public Set(int size) {
		maxElems = 0;
		this.size = size;
		for (int i = 0; i < size ; i++) {
			maxElems |= 1 << i;
		}
		set = 0;
	}
	
	public int size() {
		return this.size;
	}
	
	public boolean contains(int elem) {
		return (set & 1 << elem) != 0; //mutGenLimit 1
	}
	
	public boolean add(int elem) {
		if ((set & 1 << elem) != 0) {
			return false;
		} else {
			set |= 1 << elem;
			return true;
		}
	}
	
	public boolean remove(int elem) {
		if ((set & 1 << elem) != 0) {
			return false;
		} else {
			set &= ~(1 << elem); //mutGenLimit 1
			return true;
		}
	}
	
	public Set negation() {
		Set newSet = new Set(size);
		newSet.set = newSet.maxElems ^ set;
		return newSet;
	}
	
	public Set union(Set s) {
		Set unionSet = new Set(Math.max(size, s.size));
		unionSet.set = this.set | s.set; //mutGenLimit 1
		return unionSet;
	}
	
	public Set intersection(Set s) {
		Set interSet = new Set(Math.max(size, s.size));
		interSet.set = this.set & s.set;
		return interSet;
	}
	
	public Set subtraction(Set s) {
		Set subSet = new Set(Math.max(size, s.size));
		subSet.set = this.set & ~s.set;
		return subSet;
	}

}
