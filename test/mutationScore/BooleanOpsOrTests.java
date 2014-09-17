package mutationScore;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import utils.BooleanOps;

public class BooleanOpsOrTests {

	@Test
	public void testOrFT() {
		boolean ftrett = BooleanOps.or(false, true);
		assertTrue(ftrett);
	}
	
	@Test
	public void testOrTF() {
		boolean tfrett = BooleanOps.or(true, false);
		assertTrue(tfrett);
	}
	
	@Test
	public void testOrTT() {
		boolean ttrett = BooleanOps.or(true, true);
		assertTrue(ttrett);
	}
	
	@Test
	public void testOrFF() {
		boolean ffretf = BooleanOps.or(false, false);
		assertFalse(ffretf);
	}	
	
}
