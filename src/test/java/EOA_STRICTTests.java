package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import mujava.api.MutationOperator;
import mujava.app.MutantInfo;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.EOA_Base_2;
import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class EOA_STRICTTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public EOA_STRICTTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	@BeforeClass
	public static void verboseOptions() {
		TestingTools.setVerbose(false);
	}
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> ceEOA_1 = new LinkedList<Pattern>();
		ceEOA_1.add(Pattern.compile("(.+\\.)?EOA\\_1 var1 = param1\\.clone\\(\\); //mutGenLimit 0"));
		ceEOA_1.add(Pattern.compile("(.+\\.)?EOA\\_1 var2 = auxMethod1\\( var1\\, param1 \\)\\.clone\\(\\); //mutGenLimit 0"));
		Property propEOA_1 = new Property(MutationOperator.EOA_STRICT,
										"test/EOA_1",
										"radiatedMethod",
										2,
										2,
										ceEOA_1,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> ceEOA_2 = new LinkedList<Pattern>();
		Property propEOA_2 = new Property(MutationOperator.EOA_STRICT,
										"test/EOA_2",
										"radiatedMethod",
										0,
										0,
										ceEOA_2,
										TestingTools.NO_PATTERN_EXPECTED);
		
		List<Pattern> ceEOA_3 = new LinkedList<Pattern>();
		ceEOA_3.add(Pattern.compile("(.+\\.)?EOA\\_Base\\_1 var3 = param1\\.clone\\(\\); //mutGenLimit 0"));
		ceEOA_3.add(Pattern.compile("(.+\\.)?EOA\\_Base\\_2 var4 = \\(\\((.+\\.)?EOA\\_Base\\_2\\) auxMethod2\\( auxMethod1\\( var1\\, param1 \\)\\, new (.+\\.)?EOA\\_Base\\_2\\(\\)\\, param1 \\)\\)\\.clone\\(\\); //mutGenLimit 0"));
		Property propEOA_3 = new Property(MutationOperator.EOA_STRICT,
										"test/EOA_3",
										"radiatedMethod",
										2,
										2,
										ceEOA_3,
										TestingTools.NO_PATTERN_EXPECTED);
	
		List<Pattern> ceEOA_4 = new LinkedList<Pattern>();
		Property propEOA_4 = new Property(MutationOperator.EOA_STRICT,
										"test/EOA_4",
										"radiatedMethod",
										0,
										0,
										ceEOA_4,
										TestingTools.NO_PATTERN_EXPECTED);
		
		
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfEOA_1;
		List<MutantInfo> mfEOA_2;
		List<MutantInfo> mfEOA_3;
		List<MutantInfo> mfEOA_4;
		
		//MUTANTS GENERATION
		mfEOA_1 = TestingTools.generateMutants(propEOA_1);
		mfEOA_2 = TestingTools.generateMutants(propEOA_2);
		mfEOA_3 = TestingTools.generateMutants(propEOA_3);
		mfEOA_4 = TestingTools.generateMutants(propEOA_4);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propEOA_1, mfEOA_1},
				{propEOA_2, mfEOA_2},
				{propEOA_3, mfEOA_3},
				{propEOA_4, mfEOA_4},
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
