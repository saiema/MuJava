package mutationScore;

import static org.junit.Assert.*;

import org.junit.Test;

import utils.BooleanOps;

public class BooleanOpsAndTests {

	@Test
	public void testAndFT() {
		boolean ftretf = BooleanOps.and(false, true);
		assertFalse(ftretf);
	}
	
	@Test
	public void testAndTF() {
		boolean tfretf = BooleanOps.and(true, false);
		assertFalse(tfretf);
	}
	
	@Test
	public void testAndFF() {
		boolean ffretf = BooleanOps.and(false, false);
		assertFalse(ffretf);
	}
	
	@Test
	public void testAndTT() {
		boolean ttrett = BooleanOps.and(true, true);
		assertTrue(ttrett);
	}

}
