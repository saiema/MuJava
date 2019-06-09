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
public class LVRTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public LVRTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		
		Property propMutantsGenerated = new Property(MutationOperator.LVR, "lvr/LVR", "foo", 47, 47, TestingTools.NO_PATTERN_EXPECTED, TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * original : int a = 5 \\+ 1 \\+ 0 \\+ (\\-1); //mutGenLimit 1
		 * 9 mutants
		 */
		List<Pattern> ml1e = new LinkedList<Pattern>();
		ml1e.add(Pattern.compile("int a = 1 \\+ 1 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = \\-1 \\+ 1 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 0 \\+ 1 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		
		ml1e.add(Pattern.compile("int a = 5 \\+ \\-1 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ 0 \\+ 0 \\+ \\-1; //mutGenLimit 0"));
		
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ 1 \\+ \\-1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ \\-1 \\+ \\-1; //mutGenLimit 0"));
		
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ 0 \\+ 1; //mutGenLimit 0"));
		ml1e.add(Pattern.compile("int a = 5 \\+ 1 \\+ 0 \\+ 0; //mutGenLimit 0"));
		List<Pattern> ml1ne = new LinkedList<Pattern>();
		Property mutationsLine1 = new Property(MutationOperator.LVR, "lvr/LVR", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, ml1e, ml1ne);
		
		/*
		 * original : float b = 1\\.0f \\+ 0.0f \\+ (\\-1\\.0f) \\+ 1\\.2f; //mutGenLimit 1
		 * 9 mutants
		 */
		List<Pattern> ml2e = new LinkedList<Pattern>();
		ml2e.add(Pattern.compile("float b = 0.0f \\+ 0.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = \\-1\\.0f \\+ 0.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		
		ml2e.add(Pattern.compile("float b = 1\\.0f \\+ 1\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 1\\.0f \\+ \\-1\\.0f \\+ \\-1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		
		ml2e.add(Pattern.compile("float b = 1\\.0f \\+ 0.0f \\+ 1\\.0f \\+ 1\\.2f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 1\\.0f \\+ 0.0f \\+ 0.0f \\+ 1\\.2f; //mutGenLimit 0"));
		
		ml2e.add(Pattern.compile("float b = 1\\.0f \\+ 0.0f \\+ \\-1\\.0f \\+ 1\\.0f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 1\\.0f \\+ 0.0f \\+ \\-1\\.0f \\+ \\-1\\.0f; //mutGenLimit 0"));
		ml2e.add(Pattern.compile("float b = 1\\.0f \\+ 0.0f \\+ \\-1\\.0f \\+ 0.0f; //mutGenLimit 0"));
		List<Pattern> ml2ne = new LinkedList<Pattern>();
		Property mutationsLine2 = new Property(MutationOperator.LVR, "lvr/LVR", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, ml2e, ml2ne);
		
		/*
		 * original : return "Hi " \\+ " ( " \\+ 5.0f \\+ 1\\.0f \\+ " alalala: " \\+ 0l \\+ (\\-1l) \\+ 42 * (5l \\+ 1l) \\+ ")"; //mutGenLimit 1
		 * 21 mutants
		 * 
		 * 
		 */
		List<Pattern> ml3e = new LinkedList<Pattern>();
		ml3e.add(Pattern.compile("return \"\" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \"\" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 1\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ \\-1\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 0\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ \\-1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 0\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \"\" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 1l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ \\-1l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ 1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ 0l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 1 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ \\-1 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 0 \\* \\(5l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(1l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(\\-1l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(0l \\+ 1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(5l \\+ \\-1l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 0l\\) \\+ \"\\)\"; //mutGenLimit 0"));
		
		ml3e.add(Pattern.compile("return \"Hi \" \\+ \" \\( \" \\+ 5\\.0f \\+ 1\\.0f \\+ \" alalala: \" \\+ 0l \\+ \\-1l \\+ 42 \\* \\(5l \\+ 1l\\) \\+ \"\"; //mutGenLimit 0"));
		List<Pattern> ml3ne = new LinkedList<Pattern>();
		Property mutationsLine3 = new Property(MutationOperator.LVR, "lvr/LVR", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, ml3e, ml3ne);
		
		/*
		 * original : String c =  "It's " \\+ true \\+ " or " \\+ !false \\+ " or " \\+ false \\+ " but not " \\+ !true; //mutGenLimit 1
		 * 8 mutants 
		 */
		List<Pattern> ml4e = new LinkedList<Pattern>();
		ml4e.add(Pattern.compile("String c = \"\" \\+ true \\+ \" or \" \\+ !false \\+ \" or \" \\+ false \\+ \" but not \" \\+ !true; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("String c = \"It's \" \\+ false \\+ \" or \" \\+ !false \\+ \" or \" \\+ false \\+ \" but not \" \\+ !true; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("String c = \"It's \" \\+ true \\+ \"\" \\+ !false \\+ \" or \" \\+ false \\+ \" but not \" \\+ !true; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("String c = \"It's \" \\+ true \\+ \" or \" \\+ !true \\+ \" or \" \\+ false \\+ \" but not \" \\+ !true; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("String c = \"It's \" \\+ true \\+ \" or \" \\+ !false \\+ \"\" \\+ false \\+ \" but not \" \\+ !true; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("String c = \"It's \" \\+ true \\+ \" or \" \\+ !false \\+ \" or \" \\+ true \\+ \" but not \" \\+ !true; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("String c = \"It's \" \\+ true \\+ \" or \" \\+ !false \\+ \" or \" \\+ false \\+ \"\" \\+ !true; //mutGenLimit 0"));
		ml4e.add(Pattern.compile("String c = \"It's \" \\+ true \\+ \" or \" \\+ !false \\+ \" or \" \\+ false \\+ \" but not \" \\+ !false; //mutGenLimit 0"));
		List<Pattern> ml4ne = new LinkedList<Pattern>();
		Property mutationsLine4 = new Property(MutationOperator.LVR, "lvr/LVR", "foo", TestingTools.NO_MUTANTS_EXPECTED, TestingTools.NO_MUTANTS_EXPECTED, ml4e, ml4ne);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfFoo;
		
		
		//MUTANTS GENERATION
		mfFoo = TestingTools.generateMutants(propMutantsGenerated);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propMutantsGenerated, mfFoo},
				{mutationsLine1, mfFoo},
				{mutationsLine2, mfFoo},
				{mutationsLine3, mfFoo},
				{mutationsLine4, mfFoo}
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
