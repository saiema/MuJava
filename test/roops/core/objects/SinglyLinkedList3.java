package roops.core.objects;

public class SinglyLinkedList3 {

	public/* @nullable@ */SinglyLinkedListNode header;

	public SinglyLinkedList3() {
	}

	/*
	 * @
	 * 
	 * @ invariant (\forall SinglyLinkedListNode n; \reach(this.header,
	 * SinglyLinkedListNode, next).has(n); \reach(n.next, SinglyLinkedListNode,
	 * next).has(n)==false);
	 * 
	 * @
	 */

	/*
	 * @
	 * 
	 * @ ensures (\exists SinglyLinkedListNode n; \reach(this.header,
	 * SinglyLinkedListNode, next).has(n); n.value==value_param)
	 * 
	 * @ <==> (\result==true);
	 * 
	 * @ signals (RuntimeException e) false;
	 * 
	 * @
	 */

	public boolean contains( /* @nullable@ */Object value_param) {
		SinglyLinkedListNode current;
		boolean result;
		current = this.header;
		result = false;
		while (result == false && current != null) {
			boolean equalVal;
			if (value_param == null && current.value == null) {
				equalVal = true;
			} else {
				if (value_param != null) {
					if (value_param == current.value) {
						equalVal = true;
					} else {
						equalVal = false;
					}
				} else {
					equalVal = false;
				}
			}
			if (equalVal == true) {
				result = true;
			}
			current = current.next;
		}
		return result;
	}

	/*
	 * @
	 * 
	 * @ requires index>=0 && index<\reach(this.header, SinglyLinkedListNode,
	 * next).int_size();
	 * 
	 * @
	 * 
	 * @ ensures \reach(this.header, SinglyLinkedListNode,
	 * next).has(\result)==true;
	 * 
	 * @ ensures \reach(\result, SinglyLinkedListNode, next).int_size() ==
	 * \reach(this.header, SinglyLinkedListNode, next).int_size()-index;
	 * 
	 * @ signals (RuntimeException e) false;
	 * 
	 * @
	 */
	public roops.core.objects.SinglyLinkedListNode getNode(int index) {
		roops.core.objects.SinglyLinkedListNode current = header;
		roops.core.objects.SinglyLinkedListNode result = null;
		int current_index = 0;
		while (result == null && current != null) {
			if (index == current_index) {
				result = current;
			}
			current_index = current_index + 1;
			current = current; //mutGenLimit 1
		}
		return result;
	}

}