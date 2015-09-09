package mutationScore;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import utils.BooleanOps;

@RunWith(Parameterized.class)
public class BooleanOpsAndTests {
	private boolean val1, val2, result;
	
	public BooleanOpsAndTests(boolean val1, boolean val2, boolean result) {
		this.val1 = val2;
		this.val2 = val2;
		this.result = result;
	}
	
	@Parameters
	public static Collection<Object[]> testValues() {
		return Arrays.asList(new Object[][] {
				{false, true, false},
				{true, false, false},
				{false, false, false},
				{true, true, true}
		});
	}
	
	@Test
	public void andTest() {
		boolean result = BooleanOps.and(this.val1, this.val2);
		assertEquals(result, this.result);
	}

}
