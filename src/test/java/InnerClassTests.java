package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import mujava.api.MutationOperator;
import mujava.app.MutantInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class InnerClassTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public InnerClassTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		Property prop_1 = new Property(	MutationOperator.AODS,
										"innerClasses/IC_1",
										"radiatedMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property prop_2 = new Property(	MutationOperator.AODS,
										"innerClasses/IC_1&IC_2",
										"radiatedMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property prop_3 = new Property(	MutationOperator.AODS,
										"innerClasses/IC_1&IC_2&IC_2_1",
										"radiatedMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property prop_4 = new Property(	MutationOperator.AODS,
										"innerClasses/IC_1&IC_3",
										"radiatedMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property prop_5 = new Property(	MutationOperator.AODS,
										"innerClasses/IC_1&IC_3&IC_3_1",
										"radiatedMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property prop_6 = new Property(	MutationOperator.AODS,
										"innerClasses/IC_1&IC_3&IC_3_1&IC_3_1_1",
										"radiatedMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property prop_7 = new Property(	MutationOperator.AODS,
										"innerClasses/IC_1&IC_3&IC_3_1&IC_3_1_2",
										"radiatedMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property prop_8 = new Property(	MutationOperator.AODS,
										"innerClasses/IC_1&IC_3&IC_3_1&IC_3_1_2&IC_3_1_2_1",
										"radiatedMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mf_1;
		List<MutantInfo> mf_2;
		List<MutantInfo> mf_3;
		List<MutantInfo> mf_4;
		List<MutantInfo> mf_5;
		List<MutantInfo> mf_6;
		List<MutantInfo> mf_7;
		List<MutantInfo> mf_8;
		
		//MUTANTS GENERATION
		mf_1 = TestingTools.generateMutants(prop_1);
		mf_2 = TestingTools.generateMutants(prop_2);
		mf_3 = TestingTools.generateMutants(prop_3);
		mf_4 = TestingTools.generateMutants(prop_4);
		mf_5 = TestingTools.generateMutants(prop_5);
		mf_6 = TestingTools.generateMutants(prop_6);
		mf_7 = TestingTools.generateMutants(prop_7);
		mf_8 = TestingTools.generateMutants(prop_8);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{prop_1, mf_1},
				{prop_2, mf_2},
				{prop_3, mf_3},
				{prop_4, mf_4},
				{prop_5, mf_5},
				{prop_6, mf_6},
				{prop_7, mf_7},
				{prop_8, mf_8}
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
