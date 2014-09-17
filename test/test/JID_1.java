package test;

import java.util.LinkedList;

public class JID_1 {
	private int field1; //mutGenLimit 1
	private int field2 = 0; //mutGenLimit 1
	protected int field3 = 3 + field2; //mutGenLimit 1
	public int field4 = (new LinkedList<Integer>()).size() + field3*field2+1; //mutGenLimit 1
	public final int field5 = 3; //mutGenLimit 1

}
