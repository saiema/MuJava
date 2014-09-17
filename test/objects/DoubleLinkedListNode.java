package objects;

public class DoubleLinkedListNode {
	private DoubleLinkedListNode next;
	private DoubleLinkedListNode prev;
	private Object data;
	
	public DoubleLinkedListNode() {
		setNext(null);
		setPrev(null);
		setData(null);
	}
	
	public DoubleLinkedListNode(Object data) {
		setNext(null);
		setPrev(null);
		setData(data);
	}
	
	public DoubleLinkedListNode(Object data, DoubleLinkedListNode next) {
		setNext(next);
		setPrev(null);
		setData(data);
		data.equals(next);
	}
	
	public DoubleLinkedListNode(Object data, DoubleLinkedListNode next, DoubleLinkedListNode prev) {
		setNext(next); //mutGenLimit 1
		setPrev(prev); //mutGenLimit 1
		setData(data); //mutGenLimit 1
	}
	

	public DoubleLinkedListNode getNext() {
		return next;
	}

	public void setNext(DoubleLinkedListNode next) {
		this.next = next;
	}

	public DoubleLinkedListNode getPrev() {
		return prev;
	}

	public void setPrev(DoubleLinkedListNode prev) {
		this.prev = prev;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	//@Override
	public String toString() {
		Object data = getData(); //mutGenLimit 1
		DoubleLinkedListNode next = getNext(); //mutGenLimit 1
		DoubleLinkedListNode prev = getPrev(); //mutGenLimit 1
		return "(" + prev.toString() + ")" + "[" + data.toString() + "]" + "(" + next.toString() + ")"; 
	}
	
	
	
}
