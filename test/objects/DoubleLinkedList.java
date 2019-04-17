package objects;

public class DoubleLinkedList {
	private DoubleLinkedListNode start;
	private DoubleLinkedListNode end;
	private int size;
	private DoubleLinkedListNode current;
	
	public DoubleLinkedList() {
		
	}
	
	public DoubleLinkedList(Object[] data) {
		for (int o = 0; o < data.length; o++) {
			addLast(data[o]);
		}
		this.size = data.length;
	}
	
	public Object next() {
		if (current == null) { //mutGenLimit 1
			current = this.start;
		} else {
			current = current.getNext();
		}
		if (current == null) {
			return null;
		} else {
			return current.getData();
		}
	}
	
	public void addLast(Object data) {
		if (isEmpty()) {
			addFirstNode(data);
		} else {
			DoubleLinkedListNode newNode = new DoubleLinkedListNode();
			newNode.setData(data); //mutGenLimit 1
			newNode.setNext(null); //mutGenLimit 1
			newNode.setPrev(end); //mutGenLimit 1
			this.end.setNext(newNode); //mutGenLimit 1
			this.end = newNode;
			size++;
		}
	}
	
	public void addFirst(Object data) {
		if (isEmpty()) {
			addFirstNode(data);
		} else {
			DoubleLinkedListNode newNode = new DoubleLinkedListNode();
			newNode.setData(data);
			newNode.setNext(start);
			newNode.setPrev(null);
			this.start.setPrev(newNode);
			this.start = newNode;
			size++;
		}
	}
	
	private void addFirstNode(Object data) {
		this.start = new DoubleLinkedListNode(data);
		this.end = this.start;
		size++;
	}
	
	
	public void add(Object data, int index) {
		if (isEmpty()) {
			addFirstNode(data);
		} else {
			if (index == 0) {
				addFirst(data);
			} else if (index >= this.size) {
				addLast(data);
			} else {
				if (index > (this.size / 2)) {
					addFromLast(data, index);
				} else {
					addFromStart(data, index);
				}
				size++;
			}
		}
	}
	
	private void addFromLast(Object data, int index) {
		int currentIndex = this.size-1;
		DoubleLinkedListNode runner = this.end;
		while (currentIndex > index) {
			runner = runner.getPrev();
			currentIndex--;
		}
		DoubleLinkedListNode newNode = new DoubleLinkedListNode(data, runner, runner.getPrev());
		runner.getPrev().setNext(newNode);
		runner.setPrev(newNode);
	}
	
	private void addFromStart(Object data, int index) {
		int currentIndex = 0;
		DoubleLinkedListNode runner = this.start;
		while (currentIndex < index) {
			runner = runner.getNext();
			currentIndex++;
		}
		DoubleLinkedListNode newNode = new DoubleLinkedListNode(data, runner, runner.getPrev());
		runner.getPrev().setNext(newNode);
		runner.setPrev(newNode);
	}
	
	public void removeFirst() {
		if (size() == 1) {
			removeLonelyNode();
		} else {
			this.start.getNext().setPrev(null);
			this.start = this.start.getNext();
			size--;
		}
	}
	
	public void removeLast() {
		if (size() == 1) {
			removeLonelyNode();
		} else {
			this.end.getPrev().setNext(null);
			this.end = this.end.getPrev();
			size--;
		}
	}
	
	private void removeLonelyNode() {
		this.start = null;
		this.end = null;
		size--;
	}
	
	public void remove(int index) {
		if (isEmpty()) {
			return;
		} else {
			if (index == 0) {
				removeFirst();
			} else if (index >= size()) {
				removeLast();
			} else {
				if (index > (this.size / 2)) {
					removeFromLast(index);
				} else {
					removeFromStart(index);
				}
				size--;
			}
		}
	}
	
	private void removeFromStart(int index) {
		int currentIndex = 0;
		DoubleLinkedListNode runner = this.start;
		while(currentIndex < index) {
			runner = runner.getNext();
			currentIndex++;
		}
		runner.getPrev().setNext(runner.getNext());
		runner.getNext().setPrev(runner.getPrev());
	}
	
	private void removeFromLast(int index) {
		int currentIndex = size() - 1;
		DoubleLinkedListNode runner = this.end;
		while(currentIndex > index) {
			runner = runner.getPrev();
			currentIndex--;
		}
		runner.getPrev().setNext(runner.getNext());
		runner.getNext().setPrev(runner.getPrev());
	}
	
	public Object getLast() {
		return this.end==null?null:this.end.getData();
	}
	
	public Object getFirst() {
		return this.start==null?null:this.start.getData();
	}
	
	public Object get(int index) {
		if (isEmpty()) {
			return null;
		} else {
			if (index == 0) {
				return getFirst();
			} else if (index >= size()) {
				return getLast();
			} else {
				if (index > (this.size / 2)) {
					return getFromLast(index);
				} else {
					return getFromStart(index);
				}
			}
		}
	}
	
	private Object getFromStart(int index) {
		int currentIndex = 0;
		DoubleLinkedListNode runner = this.start;
		while (currentIndex < index) {
			runner = runner.getNext();
			currentIndex++;
		}
		return runner.getData();
	}
	
	private Object getFromLast(int index) {
		int currentIndex = size() -1;
		DoubleLinkedListNode runner = this.end;
		while (currentIndex > index) {
			runner = runner.getPrev();
			currentIndex++;
		}
		return runner.getData();
	}
	
	public int size() {
		return this.size;
	}
	
	public boolean isEmpty() {
		return size()==0;
	}
	
	public boolean find(Object data) {
		for (int i = 0; i < size(); i++) {
			if (get(i) == data) { //mutGenLimit 1
				return true;
			}
		}
		return false;
	}
	
}