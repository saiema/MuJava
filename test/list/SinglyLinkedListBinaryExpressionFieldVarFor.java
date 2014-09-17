package list;

/**
 * @Invariant all n: SinglyLinkedListNode | ( ( n in this.header.*next @- null ) => ( n !in n.next.*next @- null ) ) ;
 */

/**
 * @SpecField myseq: seq SinglyLinkedListNode from this.header, SinglyLinkedListNode.next | (
 *		( #(this.header.*next @- null) = #(this.myseq) ) && 
 *		( this.header=null => #(this.myseq)=0 ) && 
 *		( this.header!= null => this.header=this.myseq[0] ) && 
 *		( all i: int | ( ( i >= 0 && i < ( #(this.myseq) - 1) ) => ( this.myseq[i + 1] = this.myseq[i].next ) ) )
 *		) ;
 */
public class SinglyLinkedListBinaryExpressionFieldVarFor {
	
	class SinglyLinkedListNode {

		/*@ nullable @*/ SinglyLinkedListNode next = null;
		
		/*@ nullable @*/ Object value = null;
		
	}


	/*@ nullable @*/SinglyLinkedListNode header = null;

	 /*@
	   @ ensures (\exists SinglyLinkedListNode n; \reach(this.header,
	SinglyLinkedListNode, next).has(n); n.value==value_param)
	   @			<==> (\result==true);
	   @ signals (RuntimeException e) false;
	   @*/
	   public boolean contains( /*@nullable@*/ Object value_param )
	   {
	       SinglyLinkedListNode current = null;
	       boolean result;
	       for (int i=0, j=0; i+j<20;i++,j++) {
	    	   current.value = i; //mutGenLimit 1
	       }
	       current = this.header.next;
	       Object test = current; 
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

	/**
	 * @Requires ( index>=0 && index<#(this.myseq) ) ;
	 * 
	 * @Modifies_Everything
	 *
	 * @Ensures ( #(this.myseq) = #(@old(this.myseq))-1 ) && 
	 *		( all i: int | ( (i>=0 && i<index ) => (this.myseq[i] = @old(this.myseq[i]) ) )) && 
	 *		( all j: int | ( (j>=index && j<#(this.myseq)) => this.myseq[j]=@old(this.myseq[j+1]) ) ) ;
	 */
	public void remove(int index) {
		SinglyLinkedListNode current;
		current = this.header;
		SinglyLinkedListNode previous;
		previous = null;
		int current_index;
		current_index = 0;
		
		boolean found = false;
		
		while (found==false && current != null) {
			if (index == current_index) {
				found = true;
			} else {
				current_index = current_index + 1;
				previous = current;
				current = current.next;
			}
		}
		
		if (previous == null)
			this.header = current.next;
		else
			previous.next = current.next;
		
	}

	/**
	 * @Modifies_Everything
	 * 
	 * @Ensures ( #(this.myseq)=#(@old(this.myseq))+1 ) && 
	 *          ( this.myseq[#(this.myseq)-1].value=arg ) && 
	 *		    ( all i: int | ( ( i>=0 && i<#(@old(this.myseq)) ) => ( this.myseq[i]=@old(this.myseq[i]) )) )
	 */
	public void insertBack(/*@ nullable @*/Object arg) {
		SinglyLinkedListNode freshNode = new SinglyLinkedListNode();
		freshNode.value = arg;
		freshNode.next = null;

		if (this.header == null)
			this.header = freshNode;
		else {
			SinglyLinkedListNode current;
			current = this.header;
			while (current.next != null) {
				current = current.next;
			}
			current.next = freshNode;
		}
	}

	
	public static void main(String[] args) {
		SinglyLinkedListBinaryExpressionFieldVarFor list = new SinglyLinkedListBinaryExpressionFieldVarFor();
		list.insertBack(new Object());
		list.remove(0);
		
	}
	
}


