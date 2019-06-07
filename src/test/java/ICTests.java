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
public class ICTests {

	private Property prop;
	private List<MutantInfo> mutantsInfo;

	public ICTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}

	@Parameters
	public static Collection<Object[]> firstValues() {

		// TESTS DEFINITIONS

		Property propMutantsGenerated = new Property(MutationOperator.IC, "ic/IC", "foo", 6, 6, TestingTools.NO_PATTERN_EXPECTED, TestingTools.NO_PATTERN_EXPECTED);

		List<Pattern> mutationsExpected = new LinkedList<Pattern>();
		mutationsExpected.add(Pattern.compile("int i = \\(42 \\+ 1\\); //mutGenLimit 0"));
		mutationsExpected.add(Pattern.compile("boolean b = false; //mutGenLimit 0"));
		mutationsExpected.add(Pattern.compile("boolean c = !true; //mutGenLimit 1"));
		mutationsExpected.add(Pattern.compile("float d = 1\\.0f; //mutGenLimit 0"));
		mutationsExpected.add(Pattern.compile("double e = 1\\.0d; //mutGenLimit 0"));
		mutationsExpected.add(Pattern.compile("int f = \\-1; //mutGenLimit 0"));
		mutationsExpected.add(Pattern.compile("Integer F = \\(\\-3 \\+ 1\\); //mutGenLimit 0"));
		mutationsExpected.add(Pattern.compile("return i \\+ 3; //mutGenLimit 1"));
		List<Pattern> mutationsNotExpected = new LinkedList<Pattern>();
		Property mutations = new Property(MutationOperator.IC, "ic/IC", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, mutationsExpected, mutationsNotExpected);

		// MUTANTS FOLDERS
		List<MutantInfo> mfFoo;

		// MUTANTS GENERATION
		mfFoo = TestingTools.generateMutants(propMutantsGenerated);

		// PARAMETERS
		return Arrays.asList(new Object[][] { { propMutantsGenerated, mfFoo }, { mutations, mfFoo }, });
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
