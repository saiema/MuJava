package bugHunting.innerClassBug;

import java.util.Random;

public class A {
	
	private B b;
	
	public A() {
		this.b = new B();
	}
	
	public int multRandomByTwo() {
		return this.b.randomNumber() * 2; //mutGenLimit 1
	}

	private static class B {
		
		private Random rng;
		
		public B() {
			this.rng = new Random();
		}
		
		public int randomNumber() {
			return this.rng.nextInt();
		}
		
	}
}
