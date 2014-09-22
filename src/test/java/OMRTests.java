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
public class OMRTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public OMRTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/*
		 * Original code
		 * 
		 * 	public void radiatedMethod(int a, int b, int c) {
		 *		int var1 = a + b + c;
		 *	} //mutGenLimit 300
		 * 
		 * 
		 */
		List<Pattern> mceOMR_1 = new LinkedList<Pattern>();
		mceOMR_1.add(Pattern.compile("public void radiatedMethod\\(([ \t\n\f\r])*int a,([ \t\n\f\r])*int b,([ \t\n\f\r])*int c([ \t\n\f\r])*\\)([ \t\n\f\r])*" +
				"\\{ //mutGenLimit 299([ \t\n\f\r])*" +
				"int var2 = a \\+ 3;([ \t\n\f\r])*" +
				"return var2;([ \t\n\f\r])*" +
				"\\}"));
		mceOMR_1.add(Pattern.compile("public void radiatedMethod\\(([ \t\n\f\r])*int a,([ \t\n\f\r])*int b,([ \t\n\f\r])*int c([ \t\n\f\r])*\\)([ \t\n\f\r])*" +
				"\\{ //mutGenLimit 299([ \t\n\f\r])*" +
				"(.+\\.)?List<String> list1 = new (.+\\.)?LinkedList<String>\\(\\);([ \t\n\f\r])*" +
				"list1\\.add\\( \"lame!\" \\);([ \t\n\f\r])*" +
				"\\}"));
		Property propOMR_1 = new Property(Mutant.OMR,
										"test/OMR_1",
										"radiatedMethod",
										2,
										1,
										mceOMR_1,
										TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMR_1;
	
		//MUTANTS GENERATION
		mfOMR_1 = TestingTools.generateMutants(propOMR_1);
	
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMR_1, mfOMR_1}
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
