package pldi.binomialheap;

import pldi.binomialheap.BinomialHeapNode;

public class BinomialHeap {

	/*
	 * @
	 * 
	 * @ invariant (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling + child).has(n); n.parent != null ==> n.key >= n.parent.key );
	 * 
	 * @ invariant (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling + child).has(n); n.sibling != null ==> \reach(n.sibling,
	 * BinomialHeapNode, sibling + child).has(n) == false );
	 * 
	 * @ invariant (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling + child).has(n); n.child != null ==> \reach(n.child,
	 * BinomialHeapNode, sibling + child).has(n) == false );
	 * 
	 * @ invariant (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling + child).has(n); ( n.child != null && n.sibling != null ) ==>
	 * 
	 * @ (\forall BinomialHeapNode m; \reach(n.child, BinomialHeapNode, child +
	 * sibling).has(m); \reach(n.sibling, BinomialHeapNode, child +
	 * sibling).has(m) == false) );
	 * 
	 * @ invariant (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling + child).has(n); ( n.child != null && n.sibling != null ) ==>
	 * 
	 * @ (\forall BinomialHeapNode m; \reach(n.sibling, BinomialHeapNode, child
	 * + sibling).has(m); \reach(n.child, BinomialHeapNode, child +
	 * sibling).has(m) == false) );
	 * 
	 * @ invariant (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling + child).has(n); n.degree >= 0 );
	 * 
	 * @ invariant (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling + child).has(n); n.child == null ==> n.degree == 0 );
	 * 
	 * @ invariant (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling + child).has(n); n.child != null ==> n.degree == \reach(n.child,
	 * BinomialHeapNode, sibling).int_size() );
	 * 
	 * @ invariant (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling + child).has(n); (n.child != null && n.sibling != null) ==>
	 * \reach(n.child.child, BinomialHeapNode, child + sibling).int_size() ==
	 * \reach(n.child.sibling, BinomialHeapNode, child + sibling).int_size() );
	 * 
	 * @ invariant (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling + child).has(n); n.child != null ==>
	 * 
	 * @ ( \forall BinomialHeapNode m; \reach(n.child, BinomialHeapNode,
	 * sibling).has(m); m.parent == n ) );
	 * 
	 * @ invariant (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling + child).has(n); ( n.sibling != null && n.parent != null ) ==>
	 * n.degree > n.sibling.degree );
	 * 
	 * @
	 * 
	 * @ invariant this.size == \reach(Nodes, BinomialHeapNode, sibling +
	 * child).int_size();
	 * 
	 * @
	 * 
	 * @ invariant ( \forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling).has(n); (n.sibling != null ==> n.degree < n.sibling.degree) &&
	 * (n.parent == null) );
	 * 
	 * @
	 * 
	 * @ invariant ( \forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * sibling).has(n); n.key >= 0 );
	 * 
	 * @
	 * 
	 * @
	 */

	public/* @ nullable @ */pldi.binomialheap.BinomialHeapNode Nodes;

	public int size;

	public BinomialHeap() {
	}

	/*
	 * @
	 * 
	 * @ requires Nodes != null;
	 * 
	 * @ ensures (\exists BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * child + sibling).has(n); n.key == \result);
	 * 
	 * @ ensures (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * child + sibling).has(n); \result <= n.key);
	 * 
	 * @ ensures (\forall BinomialHeapNode n; \reach(Nodes, BinomialHeapNode,
	 * child + sibling).has(n); n.child == \old(n.child) && n.sibling ==
	 * \old(n.sibling) && n.parent == \old(n.parent) && n.key == \old(n.key) );
	 * 
	 * @ ensures Nodes == \old(Nodes);
	 * 
	 * @ signals (Exception e) false;
	 * 
	 * @
	 */
	public int findMinimum() {
		BinomialHeapNode x = this.Nodes;
		BinomialHeapNode y = this.Nodes;
		int min = x.key;
		// @ decreasing min;
		while (x != null) {
			if (x.key < min) {
				y = x;
				min = x.key;
			}
			x = x.child.child; //mutGenLimit 1
		}
		return y.key;
	}

	public void insert(int value) {
		if (value > 0) { //mutGenLimit 1
			BinomialHeapNode insertTemp = new BinomialHeapNode(); //mutGenLimit 1
			insertTemp.key = value; //mutGenLimit 1
			if (Nodes == null) { //mutGenLimit 1
				Nodes = insertTemp; //mutGenLimit 1
				size = 1; //mutGenLimit 1
			} else {
				BinomialHeapNode temp1 = Nodes; //mutGenLimit 1
				BinomialHeapNode temp2 = insertTemp; //mutGenLimit 1
				// @ decreasing \reach(temp2, BinomialHeapNode,
				// sibling).int_size();
				while ((temp1 != null) && (temp2 != null)) { //mutGenLimit 1
					if (temp1.degree == temp2.degree) { //mutGenLimit 1
						BinomialHeapNode tmp = temp2; //mutGenLimit 1
						temp2 = temp2.sibling; //mutGenLimit 1
						tmp.sibling = temp1.sibling; //mutGenLimit 1
						temp1.sibling = tmp; //mutGenLimit 1
						temp1 = tmp.sibling; //mutGenLimit 1
					} else {
						if (temp1.degree < temp2.degree) { //mutGenLimit 1
							if ((temp1.sibling == null)|| (temp1.sibling.degree > temp2.degree)) { //mutGenLimit 1
								BinomialHeapNode tmp = temp2; //mutGenLimit 1
								temp2 = temp2.sibling; //mutGenLimit 1
								tmp.sibling = temp1.sibling; //mutGenLimit 1
								temp1.sibling = tmp; //mutGenLimit 1
								temp1 = tmp.sibling; //mutGenLimit 1
							} else {
								temp1 = temp1.sibling; //mutGenLimit 1
							}
						} else {
							BinomialHeapNode tmp = temp1; //mutGenLimit 1
							temp1 = temp2; //mutGenLimit 1
							temp2 = temp2.sibling; //mutGenLimit 1
							temp1.sibling = tmp; //mutGenLimit 1
							if (tmp == Nodes) { //mutGenLimit 1
								Nodes = temp1; //mutGenLimit 1
							}
						}
					}
				}
				if (temp1 == null) { //mutGenLimit 1
					temp1 = Nodes; //mutGenLimit 1
					// @ decreasing \reach(temp1, BinomialHeapNode,
					// sibling).int_size();
					while (temp1.sibling != null) { //mutGenLimit 1
						temp1 = temp1.sibling; //mutGenLimit 1
					}
					temp1.sibling = temp2; //mutGenLimit 1
				}
				BinomialHeapNode prevTemp = null; //mutGenLimit 1
				BinomialHeapNode temp = Nodes; //mutGenLimit 1
				BinomialHeapNode nextTemp = Nodes.sibling; //mutGenLimit 1
				// @ decreasing \reach(temp, BinomialHeapNode,
				// sibling).int_size();
				while (nextTemp != null) { //mutGenLimit 1
					if ((temp.degree != nextTemp.degree)|| ((nextTemp.sibling != null) && (nextTemp.sibling.degree == temp.degree))) { //mutGenLimit 1
						prevTemp = temp; //mutGenLimit 1
						temp = nextTemp; //mutGenLimit 1
					} else {
						if (temp.key <= nextTemp.key) { //mutGenLimit 1
							temp.sibling = nextTemp.sibling; //mutGenLimit 1
							nextTemp.parent = temp; //mutGenLimit 1
							nextTemp.sibling = temp.child; //mutGenLimit 1
							temp.child = nextTemp; //mutGenLimit 1
							temp.degree++; //mutGenLimit 1
						} else {
							if (prevTemp == null) { //mutGenLimit 1
								Nodes = nextTemp; //mutGenLimit 1
							} else {
								prevTemp.sibling = nextTemp; //mutGenLimit 1
							}
							temp.parent = nextTemp; //mutGenLimit 1
							temp.sibling = nextTemp.child; //mutGenLimit 1
							nextTemp.child = temp; //mutGenLimit 1
							nextTemp.degree++; //mutGenLimit 1
							temp = nextTemp; //mutGenLimit 1
						}
					}
					nextTemp = temp.sibling; //mutGenLimit 1
				}
				size++; //mutGenLimit 1
			}
		}
	}

}
