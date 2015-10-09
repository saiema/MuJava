package arithmetic;

import static org.junit.Assert.*;

import org.junit.Test;
import java.io.*;

public class SmallestTests {

	@Test(timeout=1000)
	public void testSmallest00m10() {
		int a = 0;
		int b = 0;
		int c = -1;
		int d = 0;
		Smallest small = new Smallest();
		assertEquals(-1, small.smallest(a, b, c, d));
	}

	@Test(timeout=1000)
	public void testSmallest3421() {
		int a = 3;
		int b = 4;
		int c = 2;
		int d = 1;
		Smallest small = new Smallest();
		assertEquals(1, small.smallest(a, b, c, d));
	}

	@Test(timeout=1000)
	public void testSmallest4321() {
		int a = 4;
		int b = 3;
		int c = 2;
		int d = 1;
		Smallest small = new Smallest();
		assertEquals(1, small.smallest(a, b, c, d));
	}

	@Test(timeout=1000)
	public void testSmallest1234() {
		int a = 1;
		int b = 2;
		int c = 3;
		int d = 4;
		Smallest small = new Smallest();
		assertEquals(1, small.smallest(a, b, c, d));
	}

	@Test(timeout=1000)
	public void testSmallest000m1() {
		int a = 0;
		int b = 0;
		int c = 0;
		int d = -1;
		Smallest small = new Smallest();
		assertEquals(-1, small.smallest(a, b, c, d));
	}

	@Test(timeout=1000)
	public void testSmallest3241() {
		int a = 3;
		int b = 2;
		int c = 4;
		int d = 1;
		Smallest small = new Smallest();
		assertEquals(1, small.smallest(a, b, c, d));
	}

	@Test(timeout=1000)
	public void testSmallest2223() throws Exception {
		int a = 2;
		int b = 2;
		int c = 2;
		int d = 3;
		Smallest small = new Smallest();
        assertEquals(2, small.smallest(a, b, c, d)); 
	}

	@Test(timeout=1000)
	public void testSmallest1111() throws Exception {
		int a = 1;
		int b = 1;
		int c = 1;
		int d = 1;
		Smallest small = new Smallest();
        assertEquals(1, small.smallest(a, b, c, d));
	}
	
}
