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
public class IOPTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public IOPTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		Property propIOP_1 = new Property(MutationOperator.IOP,
										"test/IOP_1",
										"method",
										0,
										0,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propIOP_2 = new Property(MutationOperator.IOP,
										"test/IOP_2",
										"method",
										0,
										0,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propIOP_3 = new Property(MutationOperator.IOP,
										"test/IOP_3",
										"method",
										0,
										0,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original
		 *	public void method() {
		 *		int a = 0;
		 *		super.method();
		 *	} //mutGenLimit 1
		 */
		List<Pattern> mceIOP_4 = new LinkedList<Pattern>();
		mceIOP_4.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*\\}"));
		Property propIOP_4 = new Property(MutationOperator.IOP,
										"test/IOP_4",
										"method",
										1,
										1,
										mceIOP_4,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original
		 *	public void method() {
		 *		super.method();
		 *		int a = 0;
		 *	} //mutGenLimit 1
		 */
		List<Pattern> mceIOP_5 = new LinkedList<Pattern>();
		mceIOP_5.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*\\}"));
		Property propIOP_5 = new Property(MutationOperator.IOP,
										"test/IOP_5",
										"method",
										1,
										1,
										mceIOP_5,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original
		 *	public void method() {
		 *		super.method();
		 *		int a = 0;
		 *		int b = 0;
		 *	} //mutGenLimit 1
		 */
		List<Pattern> mceIOP_6 = new LinkedList<Pattern>();
		mceIOP_6.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*\\}"));
		mceIOP_6.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*\\}"));
		Property propIOP_6 = new Property(MutationOperator.IOP,
										"test/IOP_6",
										"method",
										2,
										2,
										mceIOP_6,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original
		 *	public void method() {
		 *		int a = 0;
		 *		super.method();
		 *		int b = 0;
		 *	} //mutGenLimit 1
		 */
		List<Pattern> mceIOP_7 = new LinkedList<Pattern>();
		mceIOP_7.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*\\}"));
		mceIOP_7.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*\\}"));
		Property propIOP_7 = new Property(MutationOperator.IOP,
										"test/IOP_7",
										"method",
										2,
										2,
										mceIOP_7,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original
		 *	public void method() {
		 *		int a = 0;
		 *		int b = 0;
		 *		super.method();
		 *	} //mutGenLimit 1
		 */
		List<Pattern> mceIOP_8 = new LinkedList<Pattern>();
		mceIOP_8.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*\\}"));
		mceIOP_8.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*\\}"));
		Property propIOP_8 = new Property(MutationOperator.IOP,
										"test/IOP_8",
										"method",
										2,
										2,
										mceIOP_8,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original
		 *	public void method() {
		 *		super.method();
		 *		int a = 0;
		 *		int b = 0;
		 *		int c = 0;
		 *	} //mutGenLimit 1
		 */
		List<Pattern> mceIOP_9 = new LinkedList<Pattern>();
		mceIOP_9.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*int c = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*\\}"));
		mceIOP_9.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*int c = 0;([ \t\n\f\r])*\\}"));
		Property propIOP_9 = new Property(MutationOperator.IOP,
										"test/IOP_9",
										"method",
										2,
										2,
										mceIOP_9,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original
		 *	public void method() {
		 *		int a = 0;
		 *		int b = 0;
		 *		int c = 0;
		 *		super.method();
		 *	} //mutGenLimit 1
		 */
		List<Pattern> mceIOP_10 = new LinkedList<Pattern>();
		mceIOP_10.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int c = 0;([ \t\n\f\r])*\\}"));
		mceIOP_10.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*int c = 0;([ \t\n\f\r])*\\}"));
		Property propIOP_10 = new Property(MutationOperator.IOP,
										"test/IOP_10",
										"method",
										2,
										2,
										mceIOP_10,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original
		 *	public void method() {
		 *		int a = 0;
		 *		int b = 0;
		 *		super.method();
		 *		int c = 0;
		 *	} //mutGenLimit 1
		 */
		List<Pattern> mceIOP_11 = new LinkedList<Pattern>();
		mceIOP_11.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*int c = 0;([ \t\n\f\r])*\\}"));
		mceIOP_11.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*int c = 0;([ \t\n\f\r])*\\}"));
		mceIOP_11.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*int c = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*\\}"));
		Property propIOP_11 = new Property(MutationOperator.IOP,
										"test/IOP_11",
										"method",
										3,
										3,
										mceIOP_11,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original
		 *	public void method() {
		 *		int a = 0;
		 *		int b = 0;
		 *		method();
		 *		int c = 0;
		 *	} //mutGenLimit 1
		 */
		Property propIOP_12 = new Property(MutationOperator.IOP,
										"test/IOP_12",
										"method",
										0,
										0,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original
		 *	public void method() {
		 *		int a = 0;
		 *		int b = 0;
		 *		super.method();
		 *		int c = 0;
		 *		int d = 0;
		 *	} //mutGenLimit 1
		 */
		List<Pattern> mceIOP_13 = new LinkedList<Pattern>();
		mceIOP_13.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*int c = 0;([ \t\n\f\r])*int d = 0;([ \t\n\f\r])*\\}"));
		//-----------------------------public  void method() { //mutGenLimit 0int a = 0;super.method();int b = 0;int c = 0;int d = 0;}
		mceIOP_13.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*int c = 0;([ \t\n\f\r])*int d = 0;([ \t\n\f\r])*\\}"));
		mceIOP_13.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*int c = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*int d = 0;([ \t\n\f\r])*\\}"));
		mceIOP_13.add(Pattern.compile("public void method\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*int a = 0;([ \t\n\f\r])*int b = 0;([ \t\n\f\r])*int c = 0;([ \t\n\f\r])*int d = 0;([ \t\n\f\r])*super\\.method\\(\\);([ \t\n\f\r])*\\}"));
		Property propIOP_13 = new Property(MutationOperator.IOP,
										"test/IOP_13",
										"method",
										4,
										4,
										mceIOP_13,
										TestingTools.NO_PATTERN_EXPECTED);
		
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfIOP_1;
		List<MutantInfo> mfIOP_2;
		List<MutantInfo> mfIOP_3;
		List<MutantInfo> mfIOP_4;
		List<MutantInfo> mfIOP_5;
		List<MutantInfo> mfIOP_6;
		List<MutantInfo> mfIOP_7;
		List<MutantInfo> mfIOP_8;
		List<MutantInfo> mfIOP_9;
		List<MutantInfo> mfIOP_10;
		List<MutantInfo> mfIOP_11;
		List<MutantInfo> mfIOP_12;
		List<MutantInfo> mfIOP_13;
		
		//MUTANTS GENERATION
		mfIOP_1 = TestingTools.generateMutants(propIOP_1);
		mfIOP_2 = TestingTools.generateMutants(propIOP_2);
		mfIOP_3 = TestingTools.generateMutants(propIOP_3);
		mfIOP_4 = TestingTools.generateMutants(propIOP_4);
		mfIOP_5 = TestingTools.generateMutants(propIOP_5);
		mfIOP_6 = TestingTools.generateMutants(propIOP_6);
		mfIOP_7 = TestingTools.generateMutants(propIOP_7);
		mfIOP_8 = TestingTools.generateMutants(propIOP_8);
		mfIOP_9 = TestingTools.generateMutants(propIOP_9);
		mfIOP_10 = TestingTools.generateMutants(propIOP_10);
		mfIOP_11 = TestingTools.generateMutants(propIOP_11);
		mfIOP_12 = TestingTools.generateMutants(propIOP_12);
		mfIOP_13 = TestingTools.generateMutants(propIOP_13);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propIOP_1, mfIOP_1},
				{propIOP_2, mfIOP_2},
				{propIOP_3, mfIOP_3},
				{propIOP_4, mfIOP_4},
				{propIOP_5, mfIOP_5},
				{propIOP_6, mfIOP_6},
				{propIOP_7, mfIOP_7},
				{propIOP_8, mfIOP_8},
				{propIOP_9, mfIOP_9},
				{propIOP_10, mfIOP_10},
				{propIOP_11, mfIOP_11},
				{propIOP_12, mfIOP_12},
				{propIOP_13, mfIOP_13}
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
