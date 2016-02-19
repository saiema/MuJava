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
public class EOCTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public EOCTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> dll_find_ce = new LinkedList<Pattern>();
		dll_find_ce.add(Pattern.compile("get\\( i \\)\\.equals\\( data \\)"));
		List<Pattern> dll_find_cne = new LinkedList<Pattern>();
		Property propDll_find = new Property(MutationOperator.EOC,
										"objects/DoubleLinkedList",
										"find",
										1,
										1,
										dll_find_ce,
										dll_find_cne);
		
		
		List<Pattern> dll_next_ce = new LinkedList<Pattern>();
		dll_next_ce.add(Pattern.compile("current\\.equals\\( null \\)"));
		List<Pattern> dll_next_cne = new LinkedList<Pattern>();
		Property propDll_next = new Property(MutationOperator.EOC,
										"objects/DoubleLinkedList",
										"next",
										1,
										1,
										dll_next_ce,
										dll_next_cne);
		
		
		List<Pattern> eoc_1_ce = new LinkedList<Pattern>();
		eoc_1_ce.add(Pattern.compile("boolean b5 \\= int1\\.equals\\( 1 \\)\\; //mutGenLimit 0"));
		eoc_1_ce.add(Pattern.compile("boolean b7 \\= int1\\.equals\\( i1 \\+ 2 \\)\\; //mutGenLimit 0"));
		List<Pattern> eoc_1_cne = new LinkedList<Pattern>();
		eoc_1_cne.add(Pattern.compile("boolean b1 \\= 1\\.equals\\( 2 \\)\\; //mutGenLimit 0"));
		eoc_1_cne.add(Pattern.compile("boolean b2 \\= 1\\.equals\\( i1 \\)\\; //mutGenLimit 0"));
		eoc_1_cne.add(Pattern.compile("boolean b3 \\= i1\\.equals\\( 1 \\)\\; //mutGenLimit 0"));
		eoc_1_cne.add(Pattern.compile("boolean b4 \\= 1\\.equals\\( int1 \\)\\; //mutGenLimit 0"));
		eoc_1_cne.add(Pattern.compile("boolean b6 \\= i1\\.equals\\( int1 \\)\\; //mutGenLimit 0"));
		Property propEoc_1 = new Property(MutationOperator.EOC,
										"test/EOC_1",
										"radiatedMethod",
										2,
										2,
										eoc_1_ce,
										eoc_1_cne);
		
		
		List<Pattern> eoc_2_ce = new LinkedList<Pattern>();
		eoc_2_ce.add(Pattern.compile("boolean b1 \\= \\(new (.+\\.)?Integer\\( 0 \\)\\)\\.equals\\( new (.+\\.)?Integer\\( 1 \\) \\)\\; //mutGenLimit 0"));
		eoc_2_ce.add(Pattern.compile("boolean b2 \\= \\(new (.+\\.)?Integer\\( 0 \\)\\)\\.equals\\( i1 \\)\\; //mutGenLimit 0"));
		eoc_2_ce.add(Pattern.compile("boolean b4 \\= int1\\.equals\\( int2 \\= new (.+\\.)?Integer\\( i1 \\) \\)\\; //mutGenLimit 0"));
		eoc_2_ce.add(Pattern.compile("boolean b5 \\= \\(new (.+\\.)?Integer\\( int2 \\+ int1 \\)\\)\\.equals\\( i1 \\) \\&\\& i1 == int1 \\&\\& int1 == new (.+\\.)?Integer\\( 0 \\) \\+ new (.+\\.)?Integer\\( 2 \\)\\; //mutGenLimit 0"));
		eoc_2_ce.add(Pattern.compile("boolean b5 \\= new (.+\\.)?Integer\\( int2 \\+ int1 \\) == i1 \\&\\& i1 == int1 \\&\\& int1\\.equals\\( new (.+\\.)?Integer\\( 0 \\) \\+ new (.+\\.)?Integer\\( 2 \\) \\)\\; //mutGenLimit 0"));
		List<Pattern> eoc_2_cne = new LinkedList<Pattern>();
		eoc_2_cne.add(Pattern.compile("boolean b3 \\= i1\\.equals\\( new (.+\\.)?Integer\\( 1 \\) \\)\\; //mutGenLimit 0"));
		eoc_2_cne.add(Pattern.compile("boolean b5 = new (.+\\.)?Integer\\( int2 \\+ int1 \\) == i1 \\&\\& i1\\.equals\\( int1 \\) \\&\\& int1 == new (.+\\.)?Integer\\( 0 \\) + new (.+\\.)?Integer\\( 2 \\); //mutGenLimit 0"));
		Property propEoc_2 = new Property(MutationOperator.EOC,
										"test/EOC_2",
										"radiatedMethod",
										5,
										5,
										eoc_2_ce,
										eoc_2_cne);
		
		List<Pattern> eoc_3_ce = new LinkedList<Pattern>();
		eoc_3_ce.add(Pattern.compile("boolean b1 \\= \\(new (.+\\.)?EOC\\_3\\(\\)\\)\\.getMe\\(\\)\\.getList\\(\\)\\.get\\( 0 \\)\\.equals\\( new (.+\\.)?Integer\\( 1 \\+ int1 \\) \\)\\; //mutGenLimit 0"));
		List<Pattern> eoc_3_cne = new LinkedList<Pattern>();
		Property propEoc_3 = new Property(MutationOperator.EOC,
										"test/EOC_3",
										"radiatedMethod",
										1,
										1,
										eoc_3_ce,
										eoc_3_cne);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfDll_find;
		List<MutantInfo> mfDll_next;
		List<MutantInfo> mfEoc_1;
		List<MutantInfo> mfEoc_2;
		List<MutantInfo> mfEoc_3;
		
		//MUTANTS GENERATION
		mfDll_find = TestingTools.generateMutants(propDll_find);
		mfDll_next = TestingTools.generateMutants(propDll_next);
		mfEoc_1 = TestingTools.generateMutants(propEoc_1);
		mfEoc_2 = TestingTools.generateMutants(propEoc_2);
		mfEoc_3 = TestingTools.generateMutants(propEoc_3);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propDll_find, mfDll_find},
				{propDll_next, mfDll_next},
				{propEoc_1, mfEoc_1},
				{propEoc_2, mfEoc_2},
				{propEoc_3, mfEoc_3},
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
