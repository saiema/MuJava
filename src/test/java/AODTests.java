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
public class AODTests {

	private Property prop;
	private List<MutantInfo> mutantsInfo;

	public AODTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}

	@Parameters
	public static Collection<Object[]> firstValues() {

		// TESTS DEFINITIONS
		Property mutations = new Property(MutationOperator.AOD, "aod/AOD", "foo", 12, 12, TestingTools.NO_PATTERN_EXPECTED, TestingTools.NO_PATTERN_EXPECTED);

		/*
		 * original : int a = 3 + 4; //mutGenLimit 1
		 * mutants : 2
		 */
		List<Pattern> mutationsLine1Expected = new LinkedList<Pattern>();
		mutationsLine1Expected.add(Pattern.compile("int a = 3; //mutGenLimit 0"));
		mutationsLine1Expected.add(Pattern.compile("int a = 4; //mutGenLimit 0"));
		List<Pattern> mutationsLine1NotExpected = new LinkedList<Pattern>();
		Property mutationsLine1 = new Property(MutationOperator.AOD, "aod/AOD", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, mutationsLine1Expected, mutationsLine1NotExpected);

		/*
		 * original : boolean b = Integer.toString(a).isEmpty() || a + 4 == 10; //mutGenLimit 1
		 * mutants : 2
		 */
		List<Pattern> mutationsLine2Expected = new LinkedList<Pattern>();
		mutationsLine2Expected.add(Pattern.compile("boolean b = Integer\\.toString\\( a \\)\\.isEmpty\\(\\) \\|\\| a == 10; //mutGenLimit 0"));
		mutationsLine2Expected.add(Pattern.compile("boolean b = Integer\\.toString\\( a \\)\\.isEmpty\\(\\) \\|\\| 4 == 10; //mutGenLimit 0"));
		List<Pattern> mutationsLine2NotExpected = new LinkedList<Pattern>();
		Property mutationsLine2 = new Property(MutationOperator.AOD, "aod/AOD", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, mutationsLine2Expected, mutationsLine2NotExpected);
		
		/*
		 * original : return a + 3 > 10 && b?4 * 8 + 10:Integer.toString(a).length() + 82; //mutGenLimit 1
		 * mutants : 8
		 */
		List<Pattern> mutationsLine3Expected = new LinkedList<Pattern>();
		mutationsLine3Expected.add(Pattern.compile("return a > 10 \\&\\& b \\? 4 \\* 8 \\+ 10 \\: Integer\\.toString\\( a \\)\\.length\\(\\) \\+ 82; //mutGenLimit 0"));
		mutationsLine3Expected.add(Pattern.compile("return 3 > 10 \\&\\& b \\? 4 \\* 8 \\+ 10 \\: Integer\\.toString\\( a \\)\\.length\\(\\) \\+ 82; //mutGenLimit 0"));
		mutationsLine3Expected.add(Pattern.compile("return a \\+ 3 > 10 \\&\\& b \\? 4 \\* 8 \\: Integer\\.toString\\( a \\)\\.length\\(\\) \\+ 82; //mutGenLimit 0"));
		mutationsLine3Expected.add(Pattern.compile("return a \\+ 3 > 10 \\&\\& b \\? 10 \\: Integer\\.toString\\( a \\)\\.length\\(\\) \\+ 82; //mutGenLimit 0"));
		mutationsLine3Expected.add(Pattern.compile("return a \\+ 3 > 10 \\&\\& b \\? 4 \\+ 10 \\: Integer\\.toString\\( a \\)\\.length\\(\\) \\+ 82; //mutGenLimit 0"));
		mutationsLine3Expected.add(Pattern.compile("return a \\+ 3 > 10 \\&\\& b \\? 8 \\+ 10 \\: Integer\\.toString\\( a \\)\\.length\\(\\) \\+ 82; //mutGenLimit 0"));
		mutationsLine3Expected.add(Pattern.compile("return a \\+ 3 > 10 \\&\\& b \\? 4 \\* 8 \\+ 10 \\: Integer\\.toString\\( a \\)\\.length\\(\\); //mutGenLimit 0"));
		mutationsLine3Expected.add(Pattern.compile("return a \\+ 3 > 10 \\&\\& b \\? 4 \\* 8 \\+ 10 \\: 82; //mutGenLimit 0"));
		List<Pattern> mutationsLine3NotExpected = new LinkedList<Pattern>();
		Property mutationsLine3 = new Property(MutationOperator.AOD, "aod/AOD", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, mutationsLine3Expected, mutationsLine3NotExpected);
		
		
		// MUTANTS FOLDERS
		List<MutantInfo> mfFoo;

		// MUTANTS GENERATION
		mfFoo = TestingTools.generateMutants(mutations);

		// PARAMETERS
		return Arrays.asList(new Object[][] {
			{mutations, mfFoo},
			{mutationsLine1, mfFoo},
			{mutationsLine2, mfFoo},
			{mutationsLine3, mfFoo},
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