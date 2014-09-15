package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
//import java.util.LinkedList;
import java.util.List;
//import java.util.regex.Pattern;

import mujava.api.Mutant;
import mujava.app.MutantInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class IHDTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public IHDTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
//		List<Pattern> a2_4_Dce = new LinkedList<Pattern>();
//		List<Pattern> a2_4_Dcne = new LinkedList<Pattern>();
		Property propA2_4_D = new Property(Mutant.IHD,
										"classMutations/A2_4_D",
										Property.MUTATE_CLASS,
										1,
										1,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
//		List<Pattern> a2_1_Dce = new LinkedList<Pattern>();
//		List<Pattern> a2_1_Dcne = new LinkedList<Pattern>();
		Property propA2_1_D = new Property(Mutant.IHD,
										"classMutations/A2_1_D",
										Property.MUTATE_CLASS,
										5,
										5,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfA2_4_D;
		List<MutantInfo> mfA2_1_D;
		
		//MUTANTS GENERATION
		mfA2_4_D = TestingTools.generateMutants(propA2_4_D);
		mfA2_1_D = TestingTools.generateMutants(propA2_1_D);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propA2_4_D, mfA2_4_D},
				{propA2_1_D, mfA2_1_D}
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
