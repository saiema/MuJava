package bugHunting;

import roops.core.objects.SinglyLinkedListNode;




public class SinglyLinkedList
{

    public /*@nullable@*/ SinglyLinkedListNode header;

    public SinglyLinkedList()
    {
    }

  /*@
    @ invariant (\forall SinglyLinkedListNode n; \reach(this.header, SinglyLinkedListNode, next).has(n); \reach(n.next, SinglyLinkedListNode, next).has(n)==false);
    @*/

 /*@
   @ ensures (\exists SinglyLinkedListNode n; \reach(this.header, SinglyLinkedListNode, next).has(n); n.value==value_param) 
   @			<==> (\result==true);
   @ signals (RuntimeException e) false;
   @*/



    public boolean contains( /*@nullable@*/ Object value_param )
    {
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

///*@
//  @ requires index>=0 && index<\reach(this.header, SinglyLinkedListNode, next).int_size();
//  @
//  @ ensures \reach(this.header, SinglyLinkedListNode, next).has(\result)==true; 
//  @ ensures \reach(\result, SinglyLinkedListNode, next).int_size() == \reach(this.header, SinglyLinkedListNode, next).int_size()-index;
//  @ signals (RuntimeException e) false;
//  @*/
//    public SinglyLinkedListNode getNode( int index )
//    {
//        SinglyLinkedListNode current = header;
//        SinglyLinkedListNode result = null;
//        int current_index = 0;
//        while (result == null && current != null) {
//            if (index == current_index) {
//                result = current;
//            } //mutGenLimit 10
//            current_index = current_index + 1; //mutGenLimit 0
//            current = current.next; //mutGenLimit 0
//        } //mutGenLimit 0
//        return result;
//    }
    
    public SinglyLinkedListNode getNode( int index )	{ //mutGenLimit 1
        int test = 17; //mutGenLimit 2
        SinglyLinkedListNode current = header; //mutGenLimit 3 
        SinglyLinkedListNode result = header;  //mutGenLimit 4
        int current_index = 0; //mutGenLimit 5
        while (result == null && current != null) { //mutGenLimit 6
            if (index == current_index) { //mutGenLimit 7
                result = current.next;  //mutGenLimit 8
            }
            current_index = test + 1; //mutGenLimit 9
            current = current.next; //mutGenLimit 10
        }
        return result; //mutGenLimit 11

    }

}