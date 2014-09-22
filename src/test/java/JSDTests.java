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
public class JSDTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public JSDTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/* 
		 * Original code
		 * 
		 *	private static int field1; //mutGenLimit 1
		 *	protected static int field2 = JSD_1.field1; //mutGenLimit 2
		 *	public static int field3; //mutGenLimit 0
		 * 
		 */
		List<Pattern> mceJSD_1 = new LinkedList<Pattern>();
		mceJSD_1.add(Pattern.compile("private int field1; //mutGenLimit 0"));
		mceJSD_1.add(Pattern.compile("protected int field2 = (.+\\.)?JSD_1\\.field1; //mutGenLimit 1"));
		Property propJSD_1 = new Property(Mutant.JSD,
											"test/JSD_1",
											Property.MUTATE_FIELDS,
											2,
											1,
											mceJSD_1,
											TestingTools.NO_PATTERN_EXPECTED);
		
		/* 
		 * Original code
		 * 
		 *	public static void method1(int param1, String param2) {
		 *	} //mutGenLimit 1
		 * 
		 */
		List<Pattern> mceJSD_2 = new LinkedList<Pattern>();
		mceJSD_2.add(Pattern.compile("public void method1\\(([ \t\n\f\r])*int param1,([ \t\n\f\r])*(.+\\.)?String param2([ \t\n\f\r])*\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*\\}"));
		Property propJSD_2 = new Property(Mutant.JSD,
											"test/JSD_1",
											"method1",
											1,
											1,
											mceJSD_2,
											TestingTools.NO_PATTERN_EXPECTED);
		
		/* 
		 * Original code
		 * 
		 *	protected static void method2(int param1, String param2) {
		 *	} //mutGenLimit 2
		 * 
		 */
		List<Pattern> mceJSD_3 = new LinkedList<Pattern>();
		mceJSD_3.add(Pattern.compile("protected void method2\\(([ \t\n\f\r])*int param1,([ \t\n\f\r])*(.+\\.)?String param2([ \t\n\f\r])*\\)([ \t\n\f\r])*\\{ //mutGenLimit 1([ \t\n\f\r])*\\}"));
		Property propJSD_3 = new Property(Mutant.JSD,
											"test/JSD_1",
											"method2",
											1,
											1,
											mceJSD_3,
											TestingTools.NO_PATTERN_EXPECTED);
		
		/* 
		 * Original code
		 * 
		 *	public static void method3(int param1, String param2) {
		 *	} //mutGenLimit 0
		 * 
		 */
		Property propJSD_4 = new Property(Mutant.JSD,
											"test/JSD_1",
											"method3",
											0,
											0,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfJSD_1;
		List<MutantInfo> mfJSD_2;
		List<MutantInfo> mfJSD_3;
		List<MutantInfo> mfJSD_4;
		
		//MUTANTS GENERATION
		mfJSD_1 = TestingTools.generateMutants(propJSD_1);
		mfJSD_2 = TestingTools.generateMutants(propJSD_2);
		mfJSD_3 = TestingTools.generateMutants(propJSD_3);
		mfJSD_4 = TestingTools.generateMutants(propJSD_4);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propJSD_1, mfJSD_1},
				{propJSD_2, mfJSD_2},
				{propJSD_3, mfJSD_3},
				{propJSD_4, mfJSD_4},
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
