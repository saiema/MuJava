package roops.core.objects;

public class BinTree {

	public /*@nullable@*/ BinTreeNode root;
	public int size;

  /*@ 
    @ invariant (\forall BinTreeNode n;
    @     \reach(root, BinTreeNode, left + right).has(n) == true;
    @     \reach(n.right, BinTreeNode, right + left).has(n) == false &&
    @     \reach(n.left, BinTreeNode, left + right).has(n) == false);
    @
    @ invariant (\forall BinTreeNode n; 
    @     \reach(root, BinTreeNode, left + right).has(n) == true;
    @     (\forall BinTreeNode m; 
    @     \reach(n.left, BinTreeNode, left + right).has(m) == true;
    @     m.key <= n.key) &&
    @     (\forall BinTreeNode m;
    @     \reach(n.right, BinTreeNode, left + right).has(m) == true;
    @     m.key > n.key));
    @
    @ invariant size == \reach(root, BinTreeNode, left + right).int_size();
    @
    @ invariant (\forall BinTreeNode n; 
    @	  \reach(root, BinTreeNode, left + right).has(n) == true;
    @	  (n.left != null ==> n.left.parent == n) && (n.right != null ==> n.right.parent == n));
    @*/


	/*@
	  @ requires true;
	  @
	  @ ensures (\exists BinTreeNode n;
	  @		\old(\reach(root, BinTreeNode, left + right)).has(n) == true;
	  @  	n.key == k) ==> size == \old(size);
	  @
	  @	ensures (\forall BinTreeNode n;
	  @		\old(\reach(root, BinTreeNode, left + right)).has(n) == true;
	  @  	n.key != k) ==> size == \old(size) + 1;
	  @*/
	
	

	boolean insert(int k){
		BinTreeNode y = null; 
		BinTreeNode x = root; 
		while (x != null) {
			//while(x.left != null) {  //(4c)
			y = x;
			//y = t.root; //(3b)(8)(9)
			if (k < x.key)
				//if(k < t.root.key) //(9)(13)
				x = x.left;
			//x = x.right; //(3a)
			//x = x.left.right //(13)
			else {
				if (k > x.key)
					//if (k < x.key) //(4a)
					//if(k > t.root.key) (4b)
					x = x.right; 
				else
					return false;
			}
		}
		x = new BinTreeNode();
		x.key = k;
		if (y == null) 
			//if(x != null) //(10)
			root = x;
		else {
			if (k < y.key) 
				//if(k > y.key) //(2a)
				//if(k < x.key)  //(2b)(5)(11)(12)
				y.left = x;
			//y.left = y; //(6)(10)(11)
			else
				y.right = x;
			//y.right = y;	//(10)(12)
		}
//		x.parent = y;
		x.parent = x; //mutGenLimit 1  //(1)(7)(8)
		//y.parent = x;  //(5)(6)(10) 
		//x.parent = y; //Omission error (14)
		size += 1; 
		return true;
	}


}
