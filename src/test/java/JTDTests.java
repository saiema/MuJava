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
public class JTDTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public JTDTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/*
		 * Original code
		 * 
		 * 	Comparator<String> stringVar_2 = this.atr1.trim().CASE_INSENSITIVE_ORDER; //mutGenLimit 1
		 *	String stringVar_3 = this.stringMethod_3(1, 2).concat("lalala").intern(); //mutGenLimit 1
		 *	Comparator<String> stringVar_4 = this.stringMethod_4(1, 2).CASE_INSENSITIVE_ORDER; //mutGenLimit 1
		 * 
		 */
		List<Pattern> mceJTD_1 = new LinkedList<Pattern>();
		mceJTD_1.add(Pattern.compile("(.+\\.)?Comparator\\<String\\> stringVar_2 = atr1\\.trim\\(\\)\\.CASE_INSENSITIVE_ORDER; //mutGenLimit 0"));
		mceJTD_1.add(Pattern.compile("(.+\\.)?String stringVar_3 = stringMethod_3\\( 1, 2 \\)\\.concat\\( \"lalala\" \\)\\.intern\\(\\); //mutGenLimit 0"));
		mceJTD_1.add(Pattern.compile("(.+\\.)?Comparator\\<String\\> stringVar_4 = stringMethod_4\\( 1, 2 \\)\\.CASE_INSENSITIVE_ORDER; //mutGenLimit 0"));
		Property propJTD_1 = new Property(Mutant.JTD,
											"test/JTD_1",
											"radiatedMethod",
											3,
											3,
											mceJTD_1,
											TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original code
		 * 
		 * 	Comparator<String> stringVar_2 = this.atr1.trim().CASE_INSENSITIVE_ORDER; //mutGenLimit 1
		 *	String stringVar_3 = this.stringMethod_3(1, 2); //mutGenLimit 1
		 *	String stringVar_4 = this.atr2; //mutGenLimit 1
		 *	String stringVar_5 = this.atr2; //mutGenLimit 1
		 *
		 */
		List<Pattern> mceJTD_2 = new LinkedList<Pattern>();
		mceJTD_2.add(Pattern.compile("(.+\\.)?Comparator\\<String\\> stringVar_2 = atr1\\.trim\\(\\)\\.CASE_INSENSITIVE_ORDER; //mutGenLimit 0"));
		mceJTD_2.add(Pattern.compile("(.+\\.)?String stringVar_3 = stringMethod_3\\( 1, 2 \\); //mutGenLimit 0"));
		mceJTD_2.add(Pattern.compile("(.+\\.)?String stringVar_4 = atr2; //mutGenLimit 0"));
		mceJTD_2.add(Pattern.compile("(.+\\.)?String stringVar_5 = atr2; //mutGenLimit 0"));
		Property propJTD_2 = new Property(Mutant.JTD,
											"test/JTD_2",
											"radiatedMethod",
											4,
											4,
											mceJTD_2,
											TestingTools.NO_PATTERN_EXPECTED);
		
		
		/*
		 * Original code
		 * 
		 * 	int var1 = this.field1; //mutGenLimit 2
		 *	int var2 = this.method1(field1, field2); //mutGenLimit 2
		 *	int var3 = this.method1(this.field1, field2); //mutGenLimit 2
		 *	int var4 = this.method1(this.method1(this.field1, this.method2(this.field2, this.field3)), 0); //mutGenLimit 2
		 *	return this.method1(this.field1, var5); //mutGenLimit 2
		 * 
		 */
		List<Pattern> mceJTD_3 = new LinkedList<Pattern>();
		mceJTD_3.add(Pattern.compile("int var1 = field1; //mutGenLimit 1"));
		mceJTD_3.add(Pattern.compile("int var2 = method1\\( field1, field2 \\); //mutGenLimit 1"));
		mceJTD_3.add(Pattern.compile("int var3 = method1\\( this\\.field1, field2 \\); //mutGenLimit 1"));
		mceJTD_3.add(Pattern.compile("int var3 = this\\.method1\\( field1, field2 \\); //mutGenLimit 1"));
		mceJTD_3.add(Pattern.compile("int var4 = method1\\( this\\.method1\\( this\\.field1, this\\.method2\\( this\\.field2, this\\.field3 \\) \\), 0 \\); //mutGenLimit 1"));
		mceJTD_3.add(Pattern.compile("int var4 = this\\.method1\\( method1\\( this\\.field1, this\\.method2\\( this\\.field2, this\\.field3 \\) \\), 0 \\); //mutGenLimit 1"));
		mceJTD_3.add(Pattern.compile("int var4 = this\\.method1\\( this\\.method1\\( field1, this\\.method2\\( this\\.field2, this\\.field3 \\) \\), 0 \\); //mutGenLimit 1"));
		mceJTD_3.add(Pattern.compile("int var4 = this\\.method1\\( this\\.method1\\( this\\.field1, method2\\( this\\.field2, this\\.field3 \\) \\), 0 \\); //mutGenLimit 1"));
		mceJTD_3.add(Pattern.compile("int var4 = this\\.method1\\( this\\.method1\\( this\\.field1, this\\.method2\\( field2, this\\.field3 \\) \\), 0 \\); //mutGenLimit 1"));
		mceJTD_3.add(Pattern.compile("int var4 = this\\.method1\\( this\\.method1\\( this\\.field1, this\\.method2\\( this\\.field2, field3 \\) \\), 0 \\); //mutGenLimit 1"));
		mceJTD_3.add(Pattern.compile(""));
		mceJTD_3.add(Pattern.compile(""));
		Property propJTD_3 = new Property(Mutant.JTD,
											"test/JTD_3",
											"radiatedMethod",
											12,
											12,
											mceJTD_3,
											TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfJTD_1;
		List<MutantInfo> mfJTD_2;
		List<MutantInfo> mfJTD_3;
		
		//MUTANTS GENERATION
		mfJTD_1 = TestingTools.generateMutants(propJTD_1);
		mfJTD_2 = TestingTools.generateMutants(propJTD_2);
		mfJTD_3 = TestingTools.generateMutants(propJTD_3);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propJTD_1, mfJTD_1},
				{propJTD_2, mfJTD_2},
				{propJTD_3, mfJTD_3},
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
