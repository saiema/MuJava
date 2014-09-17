package mutationScore;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import utils.BooleanOps;

public class BooleanOpsXorTests {

	@Test
	public void testXorFF() {
		boolean ffretf = BooleanOps.xor(false, false);
		assertFalse(ffretf);
	}
	
	@Test
	public void testXorTT() {
		boolean ttretf = BooleanOps.xor(true, true);
		assertFalse(ttretf);
	}
	
	@Test
	public void testXorTF() {
		boolean tfrett = BooleanOps.xor(true, false);
		assertTrue(tfrett);
	}
	
	@Test
	public void testXorFT() {
		boolean ftrett = BooleanOps.xor(false, true);
		assertTrue(ftrett);
	}	
	
}
