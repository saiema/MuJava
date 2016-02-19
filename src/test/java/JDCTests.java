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
public class JDCTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public JDCTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		Property propB_1 = new Property(MutationOperator.JDC,
										"classMutations/B_1",
										Property.MUTATE_CLASS,
										0,
										0,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propB_2 = new Property(MutationOperator.JDC,
										"classMutations/B_2",
										Property.MUTATE_CLASS,
										0,
										0,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> cneB_3 = new LinkedList<Pattern>();
		cneB_3.add(Pattern.compile("public B\\_3\\(\\)"));
		Property propB_3 = new Property(MutationOperator.JDC,
										"classMutations/B_3",
										Property.MUTATE_CLASS,
										1,
										1,
										TestingTools.NO_PATTERN_EXPECTED,
										cneB_3);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfB_1;
		List<MutantInfo> mfB_2;
		List<MutantInfo> mfB_3;
		
		//MUTANTS GENERATION
		mfB_1 = TestingTools.generateMutants(propB_1);
		mfB_2 = TestingTools.generateMutants(propB_2);
		mfB_3 = TestingTools.generateMutants(propB_3);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propB_1, mfB_1},
				{propB_2, mfB_2},
				{propB_3, mfB_3},
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
