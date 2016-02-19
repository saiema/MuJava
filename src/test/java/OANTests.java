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
public class OANTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public OANTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/*
		 * Original code
		 * 
		 * 	method1A(0, "hola", 0.3f); //mutGenLimit 1
		 *	int var1 = method1A(1+2+method1A(0.5f, "chau", 1)); //mutGenLimit 2
		 *	int var2 = method1A("chacha") + method1A(0.5f, "chuchu", method1A()+method1A(2)); //mutGenLimit 1000
		 * 
		 */
		List<Pattern> mceOAN_1 = new LinkedList<Pattern>();
		mceOAN_1.add(Pattern.compile("method1A\\( 0, \"hola\" \\); //mutGenLimit 0"));
		mceOAN_1.add(Pattern.compile("method1A\\( 0 \\); //mutGenLimit 0"));
		mceOAN_1.add(Pattern.compile("method1A\\(\\); //mutGenLimit 0"));
		mceOAN_1.add(Pattern.compile("int var1 = method1A\\(\\); //mutGenLimit 1"));
		mceOAN_1.add(Pattern.compile("int var1 = method1A\\( 1 \\+ 2 \\+ method1A\\( 0\\.5f, \"chau\" \\) \\); //mutGenLimit 1"));
		mceOAN_1.add(Pattern.compile("int var1 = method1A\\( 1 \\+ 2 \\+ method1A\\(\\) \\); //mutGenLimit 1"));
		mceOAN_1.add(Pattern.compile("int var2 = method1A\\(\\) \\+ method1A\\( 0.5f, \"chuchu\", method1A\\(\\) \\+ method1A\\( 2 \\) \\); //mutGenLimit 999"));
		mceOAN_1.add(Pattern.compile("int var2 = method1A\\( \"chacha\" \\) \\+ method1A\\( 0\\.5f, \"chuchu\" \\); //mutGenLimit 999"));
		mceOAN_1.add(Pattern.compile("int var2 = method1A\\( \"chacha\" \\) \\+ method1A\\(\\); //mutGenLimit 999"));
		mceOAN_1.add(Pattern.compile("int var2 = method1A\\( \"chacha\" \\) \\+ method1A\\( 0\\.5f, \"chuchu\", method1A\\(\\) \\+ method1A\\(\\) \\); //mutGenLimit 999"));
		Property propOAN_1 = new Property(MutationOperator.OAN,
										"test/OAN_1",
										"radiatedMethod",
										10,
										10,
										mceOAN_1,
										TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOAN_1;
		
		//MUTANTS GENERATION
		mfOAN_1 = TestingTools.generateMutants(propOAN_1);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOAN_1, mfOAN_1}
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
