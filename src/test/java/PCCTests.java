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
public class PCCTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public PCCTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/*
		 * Original code
		 * 
		 * PCC_Base_NG var1 = (PCC_NG_1)new PCC_Base_NG(); //mutGenLimit 877
		 * 
		 */
		List<Pattern> mcePCC_1 = new LinkedList<Pattern>();
		mcePCC_1.add(Pattern.compile("(.+\\.)?PCC_Base_NG var1 = \\((.+\\.)?PCC_Base_NG\\) new (.+\\.)?PCC_Base_NG\\(\\); //mutGenLimit 876"));
		Property propPCC_1 = new Property(Mutant.PCC,
										"test/PCC_NG_1",
										"radiatedMethod",
										1,
										1,
										mcePCC_1,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original code
		 * 
		 * PCC_Base_G_1<String> var1 = (PCC_G_1_1)new PCC_Base_G_1<String>(); //mutGenLimit 877
		 * 
		 */
		List<Pattern> mcePCC_2 = new LinkedList<Pattern>();
		mcePCC_2.add(Pattern.compile("(.+\\.)?PCC_Base_G_1<String> var1 = \\((.+\\.)?PCC_Base_G_1<(.+\\.)?String>\\) new (.+\\.)?PCC_Base_G_1<String>\\(\\); //mutGenLimit 876"));
		Property propPCC_2 = new Property(Mutant.PCC,
										"test/PCC_G_1_1",
										"radiatedMethod",
										1,
										1,
										mcePCC_2,
										TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfPCC_1;
		List<MutantInfo> mfPCC_2;
		
		//MUTANTS GENERATION
		mfPCC_1 = TestingTools.generateMutants(propPCC_1);
		mfPCC_2 = TestingTools.generateMutants(propPCC_2);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propPCC_1, mfPCC_1},
				{propPCC_2, mfPCC_2}
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
