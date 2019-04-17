package timeoutTest;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeoutTest {

	@Test(timeout = 1000)
	public void test1000() {
		try {
			TimeoutTestTarget.timeout(900);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		assertTrue(true);
	}
	
	@Test(timeout = 2000)
	public void test2000() {
		try {
			TimeoutTestTarget.timeout(1900);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		assertTrue(true);
	}
	
	@Test//(timeout = 3000)
	public void test3000() {
		try {
			TimeoutTestTarget.timeout(2900);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		assertTrue(true);
	}
	
	@Test(timeout = 4000)
	public void test4000() {
		try {
			TimeoutTestTarget.timeout(3900);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		assertTrue(true);
	}
	
	@Test//(timeout = 5000)
	public void test5000() {
		try {
			TimeoutTestTarget.timeout(4900);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		assertTrue(true);
	}

}
