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
public class ISD_SMARTTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public ISD_SMARTTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> ceISD_1 = new LinkedList<Pattern>();
		ceISD_1.add(Pattern.compile("(.+\\.)?Comparator\\<String\\> stringVar\\_2 = atr1\\.trim\\(\\)\\.CASE\\_INSENSITIVE\\_ORDER\\; //mutGenLimit 0"));
		ceISD_1.add(Pattern.compile("(.+\\.)?String stringVar\\_3 = stringMethod\\_3\\( 1\\, 2 \\)\\.concat\\( \"lalala\" \\)\\.intern\\(\\)\\; //mutGenLimit 0"));
		ceISD_1.add(Pattern.compile("(.+\\.)?Comparator\\<String\\> stringVar\\_4 = stringMethod\\_4\\( 1\\, 2 \\)\\.CASE\\_INSENSITIVE\\_ORDER; //mutGenLimit 0"));
		Property propISD_1 = new Property(Mutant.ISD_SMART,
										"test/ISD_1",
										"radiatedMethod",
										3,
										3,
										ceISD_1,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> ceISD_2 = new LinkedList<Pattern>();
		ceISD_2.add(Pattern.compile("(.+\\.)?Comparator\\<String\\> stringVar\\_2 = atr1\\.trim\\(\\)\\.CASE\\_INSENSITIVE\\_ORDER\\; //mutGenLimit 0"));
		ceISD_2.add(Pattern.compile("(.+\\.)?String stringVar\\_3 = stringMethod\\_3\\( 1\\, 2 \\)\\; //mutGenLimit 0"));
		ceISD_2.add(Pattern.compile("(.+\\.)?String stringVar\\_4 = this\\.atr2\\; //mutGenLimit 0"));
		ceISD_2.add(Pattern.compile("(.+\\.)?String stringVar\\_5 = atr2\\; //mutGenLimit 0"));
		Property propISD_2 = new Property(Mutant.ISD_SMART,
										"test/ISD_2",
										"radiatedMethod",
										4,
										4,
										ceISD_2,
										TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfISD_1;
		List<MutantInfo> mfISD_2;
		
		//MUTANTS GENERATION
		mfISD_1 = TestingTools.generateMutants(propISD_1);
		mfISD_2 = TestingTools.generateMutants(propISD_2);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propISD_1, mfISD_1},
				{propISD_2, mfISD_2},
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
