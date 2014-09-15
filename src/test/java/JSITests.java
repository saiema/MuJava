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
public class JSITests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public JSITests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/* 
		 * Original code
		 * 
		 *	private int field1; //mutGenLimit 1
		 *	protected int field2 = this.field1; //mutGenLimit 2
		 *	public int field3; //mutGenLimit 0
		 * 
		 */
		List<Pattern> mceJSI_1 = new LinkedList<Pattern>();
		mceJSI_1.add(Pattern.compile("private static int field1; //mutGenLimit 0"));
		mceJSI_1.add(Pattern.compile("protected static int field2 = this\\.field1; //mutGenLimit 1"));
		Property propJSI_1 = new Property(Mutant.JSI,
											"test/JSI_1",
											Property.MUTATE_FIELDS,
											2,
											1,
											mceJSI_1,
											TestingTools.NO_PATTERN_EXPECTED);
		
		/* 
		 * Original code
		 * 
		 *	public void method1(int param1, String param2) {
		 *	} //mutGenLimit 1
		 * 
		 */
		List<Pattern> mceJSI_2 = new LinkedList<Pattern>();
		mceJSI_2.add(Pattern.compile("public static void method1\\(([ \t\n\f\r])*int param1,([ \t\n\f\r])*(.+\\.)?String param2([ \t\n\f\r])*\\)([ \t\n\f\r])*\\{([ \t\n\f\r])*\\} //mutGenLimit 0"));
		Property propJSI_2 = new Property(Mutant.JSI,
											"test/JSI_1",
											"method1",
											1,
											1,
											mceJSI_2,
											TestingTools.NO_PATTERN_EXPECTED);
		
		/* 
		 * Original code
		 * 
		 *	protected void method2(int param1, String param2) {
		 *	} //mutGenLimit 2
		 * 
		 */
		List<Pattern> mceJSI_3 = new LinkedList<Pattern>();
		mceJSI_3.add(Pattern.compile("protected static void method2\\(([ \t\n\f\r])*int param1,([ \t\n\f\r])*(.+\\.)?String param2([ \t\n\f\r])*\\)([ \t\n\f\r])*\\{([ \t\n\f\r])*\\} //mutGenLimit 1"));
		Property propJSI_3 = new Property(Mutant.JSI,
											"test/JSI_1",
											"method2",
											1,
											1,
											mceJSI_3,
											TestingTools.NO_PATTERN_EXPECTED);
		
		/* 
		 * Original code
		 * 
		 *	public void method3(int param1, String param2) {
		 *	} //mutGenLimit 0
		 * 
		 */
		Property propJSI_4 = new Property(Mutant.JSI,
											"test/JSI_1",
											"method3",
											0,
											0,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfJSI_1;
		List<MutantInfo> mfJSI_2;
		List<MutantInfo> mfJSI_3;
		List<MutantInfo> mfJSI_4;
		
		//MUTANTS GENERATION
		mfJSI_1 = TestingTools.generateMutants(propJSI_1);
		mfJSI_2 = TestingTools.generateMutants(propJSI_2);
		mfJSI_3 = TestingTools.generateMutants(propJSI_3);
		mfJSI_4 = TestingTools.generateMutants(propJSI_4);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propJSI_1, mfJSI_1},
				{propJSI_2, mfJSI_2},
				{propJSI_3, mfJSI_3},
				{propJSI_4, mfJSI_4},
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
