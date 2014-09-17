package mutationScore;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import utils.BooleanOps;

public class BooleanOpsXnorTests {

	@Test
	public void testXnorFF() {
		boolean ffrett = BooleanOps.xnor(false, false);
		assertTrue(ffrett);
	}
	
	@Test
	public void testXnorTT() {
		boolean ttrett = BooleanOps.xnor(true, true);
		assertTrue(ttrett);
	}
	
	@Test
	public void testXnorTF() {
		boolean tfretf = BooleanOps.xnor(true, false);
		assertFalse(tfretf);
	}
	
	@Test
	public void testXnorFT() {
		boolean ftretf = BooleanOps.xnor(false, true);
		assertFalse(ftretf);
	}
	
}
