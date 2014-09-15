package test.java;

import static org.junit.Assert.assertTrue;

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

import test.java.utils.MutationExpected;
import test.java.utils.Property;
import test.java.utils.SpecificMutationsFilter;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class MultiMutationsTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public MultiMutationsTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Mutant> operators_1 = new LinkedList<Mutant>();
		operators_1.add(Mutant.PRVOR);
		String clazz_1 = "roops/core/objects/SinglyLinkedList2";
		String method_1 = "contains";
		Integer mutantsExpected_1 = 1;
		Integer compilingMutantsExpected_1 = 1;
		List<Pattern> mce_1 = new LinkedList<Pattern>();
		mce_1.add(Pattern.compile("current = header\\.next; //mutGenLimit 0"));
		List<Pattern> mcne_1 = TestingTools.NO_PATTERN_EXPECTED;
		List<MutationExpected> mutationsExpected_1 = new LinkedList<MutationExpected>();
		MutationExpected me_1_1 = new MutationExpected("this.header.next", "header.next", 3, Mutant.PRVOR);
		mutationsExpected_1.add(me_1_1);
		SpecificMutationsFilter filter_1 = new SpecificMutationsFilter(mutationsExpected_1);
		boolean writeToSameFile_1 = true;
		Property prop_1 = new Property(operators_1,
									clazz_1,
									method_1,
									mutantsExpected_1,
									compilingMutantsExpected_1,
									mce_1,
									mcne_1,
									filter_1,
									writeToSameFile_1);
		
		
		List<Mutant> operators_2 = new LinkedList<Mutant>();
		operators_2.add(Mutant.PRVOR);
		operators_2.add(Mutant.COI);
		String clazz_2 = "roops/core/objects/SinglyLinkedList2";
		String method_2 = "contains";
		Integer mutantsExpected_2 = 1;
		Integer compilingMutantsExpected_2 = 1;
		List<Pattern> mce_2 = new LinkedList<Pattern>();
		mce_2.add(Pattern.compile("current = current\\.next; //mutGenLimit 0"));
		mce_2.add(Pattern.compile("equalVal = \\!false; //mutGenLimit 0"));
		List<Pattern> mcne_2 = TestingTools.NO_PATTERN_EXPECTED;
		List<MutationExpected> mutationsExpected_2 = new LinkedList<MutationExpected>();
		MutationExpected me_2_1 = new MutationExpected("current.next.next", "current.next", 23, Mutant.PRVOR);
		MutationExpected me_2_2 = new MutationExpected("false", "!false", 8, Mutant.COI);
		mutationsExpected_2.add(me_2_1);
		mutationsExpected_2.add(me_2_2);
		SpecificMutationsFilter filter_2 = new SpecificMutationsFilter(mutationsExpected_2);
		boolean writeToSameFile_2 = true;
		Property prop_2 = new Property(operators_2,
									clazz_2,
									method_2,
									mutantsExpected_2,
									compilingMutantsExpected_2,
									mce_2,
									mcne_2,
									filter_2,
									writeToSameFile_2);
		
		
		List<Mutant> operators_3 = new LinkedList<Mutant>();
		operators_3.add(Mutant.PRVOR);
		String clazz_3 = "roops/core/objects/SinglyLinkedList2";
		String method_3 = "contains";
		Integer mutantsExpected_3 = 1;
		Integer compilingMutantsExpected_3 = 1;
		List<Pattern> mce_3 = new LinkedList<Pattern>();
		mce_3.add(Pattern.compile("equalVal = this\\.showInstance\\(\\); //mutGenLimit 0"));
		List<Pattern> mcne_3 = TestingTools.NO_PATTERN_EXPECTED;
		List<MutationExpected> mutationsExpected_3 = new LinkedList<MutationExpected>();
		MutationExpected me_3_1 = new MutationExpected("false", "this.showInstance()", 8, Mutant.PRVOR);
		mutationsExpected_3.add(me_3_1);
		SpecificMutationsFilter filter_3 = new SpecificMutationsFilter(mutationsExpected_3);
		boolean writeToSameFile_3 = true;
		Property prop_3 = new Property(operators_3,
									clazz_3,
									method_3,
									mutantsExpected_3,
									compilingMutantsExpected_3,
									mce_3,
									mcne_3,
									filter_3,
									writeToSameFile_3);
		
		List<Mutant> operators_4 = new LinkedList<Mutant>();
		operators_4.add(Mutant.AORB);
		operators_4.add(Mutant.COR);
		operators_4.add(Mutant.ROR);
		operators_4.add(Mutant.EOC);
		String clazz_4 = "test/MultiMutations";
		String method_4 = "contains";
		Integer mutantsExpected_4 = 1;
		Integer compilingMutantsExpected_4 = 1;
		List<Pattern> mce_4 = new LinkedList<Pattern>();
		mce_4.add(Pattern.compile("boolean res = current\\.next \\!\\= null \\|\\| current\\.hashCode\\(\\) \\* header\\.hashCode\\(\\) \\>\\= 0; //mutGenLimit 9"));
		List<Pattern> mcne_4 = TestingTools.NO_PATTERN_EXPECTED;
		List<MutationExpected> mutationsExpected_4 = new LinkedList<MutationExpected>();
		MutationExpected me_4_1 = new MutationExpected("current.next == null", "current.next != null", 4, Mutant.ROR);
		MutationExpected me_4_2 = new MutationExpected("current.next == null && current.hashCode() + header.hashCode() > 0", "current.next == null || current.hashCode() + header.hashCode() > 0", 4, Mutant.COR);
		MutationExpected me_4_3 = new MutationExpected("current.hashCode() + header.hashCode()", "current.hashCode() * header.hashCode()", 4, Mutant.AORB);
		MutationExpected me_4_4 = new MutationExpected("current.hashCode() + header.hashCode() > 0", "current.hashCode() + header.hashCode() >= 0", 4, Mutant.ROR);
		mutationsExpected_4.add(me_4_1);
		mutationsExpected_4.add(me_4_2);
		mutationsExpected_4.add(me_4_3);
		mutationsExpected_4.add(me_4_4);
		SpecificMutationsFilter filter_4 = new SpecificMutationsFilter(mutationsExpected_4);
		boolean writeToSameFile_4 = true;
		Property prop_4 = new Property(operators_4,
									clazz_4,
									method_4,
									mutantsExpected_4,
									compilingMutantsExpected_4,
									mce_4,
									mcne_4,
									filter_4,
									writeToSameFile_4);
		
		List<Mutant> operators_5 = new LinkedList<Mutant>();
		operators_5.add(Mutant.AORB);
		operators_5.add(Mutant.COR);
		operators_5.add(Mutant.ROR);
		operators_5.add(Mutant.EOC);
		operators_5.add(Mutant.COI);
		operators_5.add(Mutant.PRVOR);
		String clazz_5 = "test/MultiMutations";
		String method_5 = "contains";
		Integer mutantsExpected_5 = 1;
		Integer compilingMutantsExpected_5 = 1;
		List<Pattern> mce_5 = new LinkedList<Pattern>();
		mce_5.add(Pattern.compile("boolean res = current\\.next \\!\\= null \\|\\| current\\.hashCode\\(\\) \\* header\\.hashCode\\(\\) \\>\\= 0; //mutGenLimit 9"));
		mce_5.add(Pattern.compile("equalVal = \\!true; //mutGenLimit 1"));
		mce_5.add(Pattern.compile("current = current\\.next\\.next; //mutGenLimit 2"));
		List<Pattern> mcne_5 = TestingTools.NO_PATTERN_EXPECTED;
		List<MutationExpected> mutationsExpected_5 = new LinkedList<MutationExpected>();
		MutationExpected me_5_1 = new MutationExpected("current.next == null", "current.next != null", 4, Mutant.ROR);
		MutationExpected me_5_2 = new MutationExpected("current.next == null && current.hashCode() + header.hashCode() > 0", "current.next == null || current.hashCode() + header.hashCode() > 0", 4, Mutant.COR);
		MutationExpected me_5_3 = new MutationExpected("current.hashCode() + header.hashCode()", "current.hashCode() * header.hashCode()", 4, Mutant.AORB);
		MutationExpected me_5_4 = new MutationExpected("current.hashCode() + header.hashCode() > 0", "current.hashCode() + header.hashCode() >= 0", 4, Mutant.ROR);
		MutationExpected me_5_5 = new MutationExpected("true", "!true", 10, Mutant.COI);
		MutationExpected me_5_6 = new MutationExpected("current.next", "current.next.next", 25, Mutant.PRVOR);
		mutationsExpected_5.add(me_5_1);
		mutationsExpected_5.add(me_5_2);
		mutationsExpected_5.add(me_5_3);
		mutationsExpected_5.add(me_5_4);
		mutationsExpected_5.add(me_5_5);
		mutationsExpected_5.add(me_5_6);
		SpecificMutationsFilter filter_5 = new SpecificMutationsFilter(mutationsExpected_5);
		boolean writeToSameFile_5 = true;
		Property prop_5 = new Property(operators_5,
									clazz_5,
									method_5,
									mutantsExpected_5,
									compilingMutantsExpected_5,
									mce_5,
									mcne_5,
									filter_5,
									writeToSameFile_5);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mf_1;
		List<MutantInfo> mf_2;
		List<MutantInfo> mf_3;
		List<MutantInfo> mf_4;
		List<MutantInfo> mf_5;
		
		//MUTANTS GENERATION
		mf_1 = TestingTools.generateMutants(prop_1);
		mf_2 = TestingTools.generateMutants(prop_2);
		mf_3 = TestingTools.generateMutants(prop_3);
		mf_4 = TestingTools.generateMutants(prop_4);
		mf_5 = TestingTools.generateMutants(prop_5);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{prop_1, mf_1},
				{prop_2, mf_2},
				{prop_3, mf_3},
				{prop_4, mf_4},
				{prop_5, mf_5}
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
