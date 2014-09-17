package generics;

public class SW_3<T extends IA_NG & IB_NG> {
	
	public static class A_G<R extends IA_NG & IB_NG> extends AA_G<Comparable<? extends R>> {
		public int current;
		
	}

	public void defMethod(){
		
		switch((new A_G<T>().current)) {
			case 1: A_G<? extends T> a = new A_G<T>(); break;
		}
	}
	
	public void radiatedMethod() {
		int i = 1;
		int j = i++; //mutGenLimit 1
	}

}
