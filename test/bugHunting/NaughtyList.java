package bugHunting;

public class NaughtyList {
	
	public static class NaughtyNode {
		public int value;
		public NaughtyNode next;
	}
	
	private NaughtyNode header;
	
	public NaughtyList() {
		header = new NaughtyNode();
		header.value = Integer.MIN_VALUE;
	}
	
	public void add(int elem) {
		NaughtyNode current = header;
		while(true) {
			current.next = new NaughtyNode();
			current = current.next;
			current.value = elem;
		}
	}

}
