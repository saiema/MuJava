package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import mujava.api.Mutant;
import mujava.app.MutantInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class PMDTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public PMDTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/*
		 * Original code
		 * 
		 *	PMD_1 var1 = new PMD_1(); //mutGenLimit 13
		 * 
		 */
		List<Pattern> mcePMD_1 = new LinkedList<Pattern>();
		mcePMD_1.add(Pattern.compile("(.+\\.)?PMD_Base var1 = new (.+\\.)?PMD_1\\(\\); //mutGenLimit 12"));
		Property propPMD_1 = new Property(Mutant.PMD,
										"test/PMD_1",
										"radiatedMethod",
										1,
										1,
										mcePMD_1,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original code
		 * 
		 * protected PMD_1 field1; //mutGenLimit 10000
		 * 
		 */
		List<Pattern> mcePMD_2 = new LinkedList<Pattern>();
		mcePMD_2.add(Pattern.compile("protected (.+\\.)?PMD_Base field1; //mutGenLimit 9999"));
		Property propPMD_2 = new Property(Mutant.PMD,
										"test/PMD_1",
										Property.MUTATE_FIELDS,
										1,
										1,
										mcePMD_2,
										TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfPMD_1;
		List<MutantInfo> mfPMD_2;
		
		//MUTANTS GENERATION
		mfPMD_1 = TestingTools.generateMutants(propPMD_1);
		mfPMD_2 = TestingTools.generateMutants(propPMD_2);

		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propPMD_1, mfPMD_1},
				{propPMD_2, mfPMD_2}
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
