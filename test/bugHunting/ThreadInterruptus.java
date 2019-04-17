package bugHunting;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

public class ThreadInterruptus {
	
	
	public static class Dumb {
		
		private String msg;
		
		public Dumb(String msg) {
			this.msg = msg;
		}
		
		public void foo() {
			while(true) System.out.println(msg);
		}
		
	}
	
	public static void foo(String s) {
		System.out.println("It's a String");
	}
	
	public static void foo(Integer i) {
		System.out.println("It's an Integer");
	}
	
	public static void foo(Object o) {
		System.out.println("It's an Object");
	}
	
	public static void main(String[] args) throws InterruptedException, SocketException {
		Socket sc = new Socket();
		System.out.println("linger: " + sc.getSoLinger());
//		foo("String");
//		foo(1);
//		foo(new LinkedList<Integer>());
//		Object o = "String2";
//		foo(o);
//		final Dumb d1 = new Dumb("So dumb...");
//		final Dumb d2 = new Dumb("So dumb...2");
//		Thread t1 = new Thread() {
//			
//			@Override
//			public void run() {
//				d1.foo();
//			}
//			
//		};
//		Thread t2 = new Thread() {
//			
//			@Override
//			public void run() {
//				d2.foo();
//			}
//			
//		};
//		t1.setPriority(Thread.MAX_PRIORITY);
//		t2.setPriority(Thread.MIN_PRIORITY);
//		t1.start();
//		t2.start();
//		Thread.sleep(1000);
//		System.exit(0);
//		
		
	}
	

}
