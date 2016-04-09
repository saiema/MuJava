package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import mujava.api.MutationOperator;
import mujava.app.MutantInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class PPDTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public PPDTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/*
		 * Original code
		 * 
		 * 	public void radiatedMethod(PMD_1 param1) {
		 *		...
		 *	} //mutGenLimit 1
		 * 
		 */
		List<Pattern> mcePPD_1 = new LinkedList<Pattern>();
		mcePPD_1.add(Pattern.compile("public void radiatedMethod\\( (.+\\.)?PMD_Base param1 \\)([ \t\n\f\r])*\\{ //mutGenLimit 0"));
		Property propPPD_1 = new Property(MutationOperator.PPD,
										"test/PMD_1",
										"radiatedMethod",
										1,
										1,
										mcePPD_1,
										TestingTools.NO_PATTERN_EXPECTED);
		
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfPPD_1;
		
		//MUTANTS GENERATION
		mfPPD_1 = TestingTools.generateMutants(propPPD_1);

		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propPPD_1, mfPPD_1},
		});
	}
	
	@Test
	public void testThatMutantsCompile() {
		assertTrue(TestingTools.testThatMutantsCompile(this.prop, this.mutantsInfo));
	}
	
	@Test
	public void testCorrectNumberOfMutants() {
		assertTrue(TestingTools.testCorrectNumberOfMutants(this.prop, this.mutantsInfo));
	}
	
	@Test
	public void testCorrectMutantsGenerated() {
		assertTrue(TestingTools.testExpectedMutantsFound(this.prop, this.mutantsInfo));
	}
	
	@Test
	public void testMutantsNotExpected() {
		assertTrue(TestingTools.testUnexpectedMutantsNotFound(this.prop, this.mutantsInfo));
	}
	
	@Test
	public void testMutantsMD5hash() {
		assertTrue(TestingTools.testMD5Hash(this.mutantsInfo));
	}

}
