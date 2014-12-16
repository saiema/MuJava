package roops.core.objects;


import roops.core.objects.SinglyLinkedListNode;


public class SinglyLinkedList_stryker {

/*@
    @ invariant (\forall SinglyLinkedListNode n; \reach(this.header, SinglyLinkedListNode, next).has(n); \reach(n.next, SinglyLinkedListNode, next).has(n)==false);
    @*/    public /*@nullable@*/roops.core.objects.SinglyLinkedListNode header;

    public SinglyLinkedList_stryker() {
    }

/*@ 
      @ requires true;
      @ ensures (\exists SinglyLinkedListNode n; \reach(this.header, SinglyLinkedListNode, next).has(n); n.value==valueParam) ==> (\result==true);
      @ ensures (\result == true) ==> (\exists SinglyLinkedListNode n; \reach(this.header, SinglyLinkedListNode, next).has(n); n.value==valueParam);
      @ signals (RuntimeException e) false;
      @ 
      @*/
    public boolean contains( /*@nullable@*/java.lang.Object valueParam ) {
    	SinglyLinkedListNode current; //mutGenLimit 1
    	boolean result; //mutGenLimit 1
    	current = this.header; //mutGenLimit 1
    	result = false; //mutGenLimit 1
    	while (result == false && current != null) { //mutGenLimit 1
    		boolean equalVal; //mutGenLimit 1
    		if (valueParam == null && current.value == null) { //mutGenLimit 1
    			equalVal = true; //mutGenLimit 1
    		} else {
    			if (valueParam != null) { //mutGenLimit 1
    				if (valueParam == current) { //mutGenLimit 1
    					equalVal = true; //mutGenLimit 1
    				} else {
    					equalVal = false; //mutGenLimit 1
    				}
    			} else {
    				equalVal = false; //mutGenLimit 1
    			}
    		}
    		if (equalVal == true) { //mutGenLimit 1
    			result = true; //mutGenLimit 1
    		}
    		current = current.next; //mutGenLimit 1
    	}
    	return result; //mutGenLimit 1
    }

/*@
      @ requires index>=0 && index<\reach(this.header, SinglyLinkedListNode, next).int_size();
      @
      @ ensures \reach(this.header, SinglyLinkedListNode, next).has(\result)==true;
      @ ensures \reach(\result, SinglyLinkedListNode, next).int_size() == \reach(this.header, SinglyLinkedListNode, next).int_size()-index;
      @ signals (RuntimeException e) false;
      @*/   
      public SinglyLinkedListNode getNode( int index ) {
    	  SinglyLinkedListNode current = this.header; //mutGenLimit 2
    	  SinglyLinkedListNode result = null; //mutGenLimit 2
    	  int current_index = 0; //mutGenLimit 2
    	  while (result == null && current != null) {
    		  if (index == current_index) {
    			  result = current;
    		  }
    		  current_index = current_index + 1;
    		  current = current.next; //mutGenLimit 2
    	  }
    	  return result; //mutGenLimit 2
      }

      /*@ requires true;
      @ ensures (\exists SinglyLinkedListNode n; \reach(this.header, SinglyLinkedListNode, next).has(n); n.value == arg && n.next == null);
      @ ensures (\forall SinglyLinkedListNode n; \reach(this.header, SinglyLinkedListNode, next).has(n); n.next != null ==> \old(\reach(this.header, SinglyLinkedListNode, next)).has(n));
      @*/
      public void insertBack( java.lang.Object arg ) {
    	  SinglyLinkedListNode freshNode = new SinglyLinkedListNode();
    	  freshNode.value = arg; //mutGenLimit 2
    	  freshNode.next = null;
    	  if (this.header == null) {
    		  this.header = freshNode;
    	  } else {
    		  SinglyLinkedListNode current;
    		  current = this.header; //mutGenLimit 2
    		  while (current.next != null) {
    			  current = current.next; //mutGenLimit 2
    		  }
    		  current.next = freshNode; //mutGenLimit 2
    	  }
      }

}
