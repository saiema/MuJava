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
public class ISI_SMARTTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public ISI_SMARTTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> ceISI_1 = new LinkedList<Pattern>();
		ceISI_1.add(Pattern.compile("(.+\\.)?String atr1\\_local = super\\.atr1\\; //mutGenLimit 0"));
		ceISI_1.add(Pattern.compile("(.+\\.)?String atr2\\_local = super\\.atr2\\; //mutGenLimit 0"));
		ceISI_1.add(Pattern.compile("(.+\\.)?String atr3\\_local = atr3\\; //mutGenLimit 1"));
		ceISI_1.add(Pattern.compile("(.+\\.)?String atr1 = super\\.atr1\\; //mutGenLimit 0"));
		ceISI_1.add(Pattern.compile("(.+\\.)?String atr2 = super\\.atr2\\; //mutGenLimit 0"));
		ceISI_1.add(Pattern.compile("(.+\\.)?String atr3 = this\\.atr3\\; //mutGenLimit 1"));
		ceISI_1.add(Pattern.compile("int atr4 = super\\.intMethod\\_1\\(\\)\\; //mutGenLimit 0"));
		ceISI_1.add(Pattern.compile("int atr5 = super\\.intMethod\\_2\\(\\)\\; //mutGenLimit 0"));
		ceISI_1.add(Pattern.compile("int atr6 = this\\.intMethod\\_3\\(\\)\\; //mutGenLimit 1"));
		ceISI_1.add(Pattern.compile("int atr4\\_local = super\\.intMethod\\_1\\(\\)\\; //mutGenLimit 0"));
		ceISI_1.add(Pattern.compile("int atr5\\_local = super\\.intMethod\\_2\\(\\)\\; //mutGenLimit 0"));
		ceISI_1.add(Pattern.compile("int atr6\\_local = intMethod\\_3\\(\\)\\; //mutGenLimit 1"));
		ceISI_1.add(Pattern.compile("(.+\\.)?String stringVar\\_1 = super\\.stringMethod\\_1\\(\\)\\; //mutGenLimit 0"));
		ceISI_1.add(Pattern.compile("(.+\\.)?String stringVar\\_2 = super\\.stringMethod\\_2\\(\\)\\; //mutGenLimit 0"));
		ceISI_1.add(Pattern.compile("(.+\\.)?String stringVar\\_3 = super\\.stringMethod\\_3\\( 1\\, 2 \\)\\; //mutGenLimit 0"));
		ceISI_1.add(Pattern.compile("(.+\\.)?String stringVar\\_4 = stringMethod\\_4\\( 1\\, 2 \\)\\; //mutGenLimit 1"));
		Property propISI_1 = new Property(MutationOperator.ISI_SMART,
										"test/ISI_1",
										"radiatedMethod",
										11,
										11,
										ceISI_1,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> ceISI_2 = new LinkedList<Pattern>();
		ceISI_2.add(Pattern.compile("(.+\\.)?String stringVar\\_1 = super\\.stringMethod\\_3\\( 1\\, 2 \\)\\.concat\\( \"lalala\" \\)\\.intern\\(\\)\\; //mutGenLimit 0"));
		ceISI_2.add(Pattern.compile("(.+\\.)?Comparator\\<String\\> stringVar\\_2 = super\\.stringMethod\\_4\\( 1\\, 2 \\)\\.CASE\\_INSENSITIVE\\_ORDER\\; //mutGenLimit 0"));
		ceISI_2.add(Pattern.compile("(.+\\.)?String stringVar\\_3 = super\\.stringMethod\\_3\\( 1\\, 2 \\)\\.concat\\( \"lalala\" \\)\\.intern\\(\\)\\; //mutGenLimit 0"));
		ceISI_2.add(Pattern.compile("(.+\\.)?Comparator\\<String\\> stringVar\\_4 = super\\.stringMethod\\_4\\( 1\\, 2 \\)\\.CASE\\_INSENSITIVE\\_ORDER\\; //mutGenLimit 0"));
		Property propISI_2 = new Property(MutationOperator.ISI_SMART,
										"test/ISI_2",
										"radiatedMethod",
										4,
										4,
										ceISI_2,
										TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfISI_1;
		List<MutantInfo> mfISI_2;
		
		//MUTANTS GENERATION
		mfISI_1 = TestingTools.generateMutants(propISI_1);
		mfISI_2 = TestingTools.generateMutants(propISI_2);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propISI_1, mfISI_1},
				{propISI_2, mfISI_2}
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
