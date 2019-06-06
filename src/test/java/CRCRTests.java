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
public class CRCRTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public CRCRTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> mceOMUC = new LinkedList<Pattern>();
		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
		Property propOMUC = new Property(MutationOperator.CRCR, "crcr/CRCR", "foo2", 0, 0, mceOMUC, mcneOMUC);
		
		
		Property propMutantsGenerated = new Property(MutationOperator.CRCR, "crcr/CRCR", "foo", 96, 96, TestingTools.NO_PATTERN_EXPECTED, TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * original : int a = 5 + 1 + 0 + (-1); //mutGenLimit 1
		 * 18 mutants
		 */
		List<Pattern> ml1e = new LinkedList<Pattern>();
		ml1e.add(Pattern.compile("int a = 0 \\+ 1 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 1 \\+ 1 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = \\-1 \\+ 1 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = \\-5 \\+ 1 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = \\(5 \\+ 1\\) \\+ 1 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = \\(5 \\- 1\\) \\+ 1 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		
		ml1e.add(Pattern.compile("int a = 5 \\+ 0 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ \\-1 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ \\(1 \\+ 1\\) \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ \\(1 \\- 1\\) \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ 1 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ \\-1 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ \\(0 \\+ 1\\) \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ \\(0 \\- 1\\) \\+ \\-1; //mutGenLimit 0"));
	
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ 0 \\+ \\-\\(\\-1\\); //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ 0 \\+ 0; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ 0 \\+ \\(\\-1 \\+ 1\\); //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ 0 \\+ \\(\\-1 \\- 1\\); //mutGenLimit 0"));
		
		List<Pattern> ml1ne = new LinkedList<Pattern>();
		
		Property mutationsLine1 = new Property(MutationOperator.CRCR, "crcr/CRCR", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, ml1e, ml1ne);
		
		/*
		 * original : float b = 5.0f + 1.0f + 0.0f + (-1.0f) + 1.2f; //mutGenLimit 1
		 * 24 mutants
		 */
		List<Pattern> ml2e = new LinkedList<Pattern>();
		ml2e.add(Pattern.compile("float b = 1\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 0\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = \\-1\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = \\-5\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = \\(5\\.0f \\+ 1\\.0f\\) \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = \\(5\\.0f \\- 1\\.0f\\) \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
	
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 0\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ \\-1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ \\(1\\.0f \\+ 1\\.0f\\) \\+ 0\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ \\(1\\.0f \\- 1\\.0f\\) \\+ 0\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ 1\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ \\-1\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ \\(0\\.0f \\+ 1\\.0f\\) \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ \\(0\\.0f \\- 1\\.0f\\) \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\-\\(\\-1\\.0f\\) \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ 0\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\(\\-1\\.0f \\+ 1\\.0f\\) \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\(\\-1\\.0f \\- 1\\.0f\\) \\+ 1\\.2f; //mutGenLimit 0"));
		
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ 1\\.0f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ \\-1\\.0f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ 0\\.0f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ \\-1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ \\(1\\.2f \\+ 1\\.0f\\); //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 5\\.0f \\+ 1\\.0f \\+ 0\\.0f \\+ \\-1\\.0f \\+ \\(1\\.2f \\- 1\\.0f\\); //mutGenLimit 0"));
		List<Pattern> ml2ne = new LinkedList<Pattern>();
		Property mutationsLine2 = new Property(MutationOperator.CRCR, "crcr/CRCR", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, ml2e, ml2ne);
		
		/*
		 * original : double c = 5.0d + 1.0d + 0.0d + (-1.0d) + 1.2d; //mutGenLimit 1
		 * 24 mutants
		 */
		List<Pattern> ml3e = new LinkedList<Pattern>();
		ml3e.add(Pattern.compile("double c = 1\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 0\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = \\-1\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = \\-5\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = \\(5\\.0d \\+ 1\\.0d\\) \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = \\(5\\.0d \\- 1\\.0d\\) \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
	
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 0\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ \\-1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ \\(1\\.0d \\+ 1\\.0d\\) \\+ 0\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ \\(1\\.0d \\- 1\\.0d\\) \\+ 0\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ 1\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ \\-1\\.0d \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ \\(0\\.0d \\+ 1\\.0d\\) \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ \\(0\\.0d \\- 1\\.0d\\) \\+ \\-1\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\-\\(\\-1\\.0d\\) \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ 0\\.0d \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\(\\-1\\.0d \\+ 1\\.0d\\) \\+ 1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\(\\-1\\.0d \\- 1\\.0d\\) \\+ 1\\.2d; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ 1\\.0d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ \\-1\\.0d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ 0\\.0d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ \\-1\\.2d; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ \\(1\\.2d \\+ 1\\.0d\\); //mutGenLimit 0"));
		ml3e.add(Pattern.compile("double c = 5\\.0d \\+ 1\\.0d \\+ 0\\.0d \\+ \\-1\\.0d \\+ \\(1\\.2d \\- 1\\.0d\\); //mutGenLimit 0"));
		List<Pattern> ml3ne = new LinkedList<Pattern>();
		Property mutationsLine3 = new Property(MutationOperator.CRCR, "crcr/CRCR", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, ml3e, ml3ne);
		
		/*
		 * original : long d = 5l + 1l + 0l + (-1l) + 3l; //mutGenLimit 1
		 * 24 mutants
		 */
		List<Pattern> ml4e = new LinkedList<Pattern>();
		ml4e.add(Pattern.compile("long d = 1l \\+ 1l \\+ 0l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 0l \\+ 1l \\+ 0l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = \\-1l \\+ 1l \\+ 0l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = \\-5l \\+ 1l \\+ 0l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = \\(5l \\+ 1l\\) \\+ 1l \\+ 0l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = \\(5l \\- 1l\\) \\+ 1l \\+ 0l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		
		ml4e.add(Pattern.compile("long d = 5l \\+ 0l \\+ 0l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ \\-1l \\+ 0l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ \\(1l \\+ 1l\\) \\+ 0l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ \\(1l \\- 1l\\) \\+ 0l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ 1l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ \\-1l \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ \\(0l \\+ 1l\\) \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ \\(0l \\- 1l\\) \\+ \\-1l \\+ 3l; //mutGenLimit 0"));
		
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ 0l \\+ \\-\\(\\-1l\\) \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ 0l \\+ 0l \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ 0l \\+ \\(\\-1l \\+ 1l\\) \\+ 3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ 0l \\+ \\(\\-1l \\- 1l\\) \\+ 3l; //mutGenLimit 0"));
		
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ 0l \\+ \\-1l \\+ 1l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ 0l \\+ \\-1l \\+ 0l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ 0l \\+ \\-1l \\+ \\-1l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ 0l \\+ \\-1l \\+ \\-3l; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ 0l \\+ \\-1l \\+ \\(3l \\+ 1l\\); //mutGenLimit 0"));
		ml4e.add(Pattern.compile("long d = 5l \\+ 1l \\+ 0l \\+ \\-1l \\+ \\(3l \\- 1l\\); //mutGenLimit 0"));
		List<Pattern> ml4ne = new LinkedList<Pattern>();
		Property mutationsLine4 = new Property(MutationOperator.CRCR, "crcr/CRCR", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, ml4e, ml4ne);
		
		/*
		 * original : return (int) (a + c + 3 * d); //mutGenLimit 1
		 * 6 mutants
		 */
		List<Pattern> ml5e = new LinkedList<Pattern>();
		ml5e.add(Pattern.compile("return \\(int\\) \\(a \\+ c \\+ 1 \\* d\\); //mutGenLimit 0"));
		ml5e.add(Pattern.compile("return \\(int\\) \\(a \\+ c \\+ 0 \\* d\\); //mutGenLimit 0"));
		ml5e.add(Pattern.compile("return \\(int\\) \\(a \\+ c \\+ \\-1 \\* d\\); //mutGenLimit 0"));
		ml5e.add(Pattern.compile("return \\(int\\) \\(a \\+ c \\+ \\-3 \\* d\\); //mutGenLimit 0"));
		ml5e.add(Pattern.compile("return \\(int\\) \\(a \\+ c \\+ \\(3 \\+ 1\\) \\* d\\); //mutGenLimit 0"));
		ml5e.add(Pattern.compile("return \\(int\\) \\(a \\+ c \\+ \\(3 \\- 1\\) \\* d\\); //mutGenLimit 0"));
		List<Pattern> ml5ne = new LinkedList<Pattern>();
		Property mutationsLine5 = new Property(MutationOperator.CRCR, "crcr/CRCR", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, ml5e, ml5ne);
		
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfFoo;
		
		
		//MUTANTS GENERATION
		mfOMUC = TestingTools.generateMutants(propOMUC);
		mfFoo = TestingTools.generateMutants(propMutantsGenerated);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMUC, mfOMUC},
				{propMutantsGenerated, mfFoo},
				{mutationsLine1, mfFoo},
				{mutationsLine2, mfFoo},
				{mutationsLine3, mfFoo},
				{mutationsLine4, mfFoo},
				{mutationsLine5, mfFoo}
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
