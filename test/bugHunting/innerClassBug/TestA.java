package bugHunting.innerClassBug;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestA {

	@Test
	public void test() {
		A a = new A();
		assertTrue(a.multRandomByTwo() % 2 == 0);
	}

}
