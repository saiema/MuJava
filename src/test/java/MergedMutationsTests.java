package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.ManualMutationsFilter;
import test.java.utils.MutationExpected;
import test.java.utils.Property;
import test.java.utils.SpecificMutationsFilter;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class MergedMutationsTests {
	
	private Property prop;
	private Map<String, MutantsInformationHolder> mutations;
	
	public MergedMutationsTests(Property prop, Map<String, MutantsInformationHolder> mutations) {
		this.prop = prop;
		this.mutations = mutations;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		/*
		 * Mutations obtained using operators on method contains of the class test/MultiMutations
		 * 
		 * PRVOR, AORB, COR, ROR, EOC
		 */
		/*
		 
		 	Operator: AORB (0)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.hashCode() + header.hashCode()
			Mutated node:  current.hashCode() * header.hashCode()
			
			Operator: AORB (1)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.hashCode() + header.hashCode()
			Mutated node:  current.hashCode() / header.hashCode()
			
			Operator: AORB (2)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.hashCode() + header.hashCode()
			Mutated node:  current.hashCode() % header.hashCode()
			
			Operator: AORB (3)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.hashCode() + header.hashCode()
			Mutated node:  current.hashCode() - header.hashCode()
			
			Operator: ROR (4)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.next == null
			Mutated node:  current.next != null
			
			Operator: ROR (5)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.next == null
			Mutated node:  false
			
			Operator: ROR (6)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.next == null
			Mutated node:  true
			
			Operator: ROR (7)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.hashCode() + header.hashCode() > 0
			Mutated node:  current.hashCode() + header.hashCode() >= 0
			
			Operator: ROR (8)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.hashCode() + header.hashCode() > 0
			Mutated node:  current.hashCode() + header.hashCode() < 0
			
			Operator: ROR (9)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.hashCode() + header.hashCode() > 0
			Mutated node:  current.hashCode() + header.hashCode() <= 0
			
			Operator: ROR (10)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.hashCode() + header.hashCode() > 0
			Mutated node:  current.hashCode() + header.hashCode() == 0
			
			Operator: ROR (11)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.hashCode() + header.hashCode() > 0
			Mutated node:  current.hashCode() + header.hashCode() != 0
			
			Operator: ROR (12)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.hashCode() + header.hashCode() > 0
			Mutated node:  false
			
			Operator: ROR (13)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.hashCode() + header.hashCode() > 0
			Mutated node:  true
			
			Operator: COR (14)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.next == null && current.hashCode() + header.hashCode() > 0
			Mutated node:  current.next == null || current.hashCode() + header.hashCode() > 0
			
			Operator: COR (15)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.next == null && current.hashCode() + header.hashCode() > 0
			Mutated node:  current.next == null ^ current.hashCode() + header.hashCode() > 0
			
			Operator: COR (16)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.next == null && current.hashCode() + header.hashCode() > 0
			Mutated node:  current.next == null & current.hashCode() + header.hashCode() > 0
			
			Operator: COR (17)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.next == null && current.hashCode() + header.hashCode() > 0
			Mutated node:  current.next == null | current.hashCode() + header.hashCode() > 0
			
			Operator: EOC (18)
			Modify one line inside method: true
			Affected line inside method: 4
			Original node:  current.next == null
			Mutated node:  current.next.equals( null )
		 
		 */
		
		//PROPERTIES
		List<MutationOperator> opsMerging_1 = new LinkedList<MutationOperator>();
		opsMerging_1.add(MutationOperator.PRVOR);
		opsMerging_1.add(MutationOperator.AORB);
		opsMerging_1.add(MutationOperator.COR);
		opsMerging_1.add(MutationOperator.ROR);
		opsMerging_1.add(MutationOperator.EOC);
		List<MutationExpected> me_1 = new LinkedList<MutationExpected>();
		List<Integer> la_1 = new LinkedList<Integer>();
		Property propMerging_1 = new Property(
									opsMerging_1,
									"test/MultiMutations",
									"contains",
									me_1,
									la_1,
									null
								);
		
		List<MutationExpected> mutationsExpected_2 = new LinkedList<MutationExpected>();
		MutationExpected me_2_1 = new MutationExpected("current.next == null", "current.next.equals( null )", 4, MutationOperator.EOC);
		MutationExpected me_2_2 = new MutationExpected("current.next == null && current.hashCode() + header.hashCode() > 0","current.next == null | current.hashCode() + header.hashCode() > 0", 4, MutationOperator.COR);
		MutationExpected me_2_3 = new MutationExpected("current.hashCode() + header.hashCode() > 0", "true", 4, MutationOperator.ROR);
		mutationsExpected_2.add(me_2_1);
		mutationsExpected_2.add(me_2_2);
		mutationsExpected_2.add(me_2_3);
		SpecificMutationsFilter filter_2 = new SpecificMutationsFilter(mutationsExpected_2);
		List<MutationExpected> me_2 = new LinkedList<MutationExpected>();
		me_2.add(
				new MutationExpected(
						"boolean res = current.next == null && current.hashCode() + header.hashCode() > 0;",
						"boolean res = current.next.equals( null ) | true;",
						4
				)
		);
		List<Integer> la_2 = new LinkedList<Integer>();
		la_2.add(4);
		Property propMerging_2 = new Property(
									opsMerging_1,
									"test/MultiMutations",
									"contains",
									me_2,
									la_2,
									filter_2
								);
		
		List<MutationExpected> mutationsExpected_3 = new LinkedList<MutationExpected>();
		MutationExpected me_3_1 = new MutationExpected("current.hashCode() + header.hashCode()", "current.hashCode() * header.hashCode()", 4, MutationOperator.AORB);
		MutationExpected me_3_2 = new MutationExpected("current.hashCode() + header.hashCode()", "current.hashCode() / header.hashCode()", 4, MutationOperator.AORB);
		mutationsExpected_3.add(me_3_1);
		mutationsExpected_3.add(me_3_2);
		SpecificMutationsFilter filter_3 = new SpecificMutationsFilter(mutationsExpected_3);
		List<MutationExpected> me_3 = new LinkedList<MutationExpected>();
		List<Integer> la_3 = new LinkedList<Integer>();
		Property propMerging_3 = new Property(
									opsMerging_1,
									"test/MultiMutations",
									"contains",
									me_3,
									la_3,
									filter_3
								);
		
		List<MutationExpected> mutationsExpected_4 = new LinkedList<MutationExpected>();
		MutationExpected me_4_1 = new MutationExpected("current.hashCode() + header.hashCode()", "current.hashCode() - header.hashCode()", 4, MutationOperator.AORB);
		MutationExpected me_4_2 = new MutationExpected("current.next == null", "true", 4, MutationOperator.ROR);
		MutationExpected me_4_3 = new MutationExpected("current.hashCode() + header.hashCode() > 0", "false", 4, MutationOperator.ROR);
		mutationsExpected_4.add(me_4_1);
		mutationsExpected_4.add(me_4_2);
		mutationsExpected_4.add(me_4_3);
		SpecificMutationsFilter filter_4 = new SpecificMutationsFilter(mutationsExpected_4);
		List<MutationExpected> me_4 = new LinkedList<MutationExpected>();
		List<Integer> la_4 = new LinkedList<Integer>();
		Property propMerging_4 = new Property(
									opsMerging_1,
									"test/MultiMutations",
									"contains",
									me_4,
									la_4,
									filter_4
								);
		
		ManualMutationsFilter mmf_5 = new ManualMutationsFilter();
		Set<Integer> selectedMutations_5 = new HashSet<Integer>();
		selectedMutations_5.add(6);
		selectedMutations_5.add(10);
		selectedMutations_5.add(14);
		mmf_5.selectMutations(selectedMutations_5);
		List<MutationExpected> me_5 = new LinkedList<MutationExpected>();
		me_5.add(
				new MutationExpected(
						"boolean res = current.next == null && current.hashCode() + header.hashCode() > 0;",
						"boolean res = true || current.hashCode() + header.hashCode() == 0;",
						4
				)
		);
		List<Integer> la_5 = new LinkedList<Integer>();
		la_5.add(4);
		Property propMerging_5 = new Property(
									opsMerging_1,
									"test/MultiMutations",
									"contains",
									me_5,
									la_5,
									mmf_5
								);
		
		//MUTATIONS GENERATED
		Map<String, MutantsInformationHolder> mutsMerging_1;
		Map<String, MutantsInformationHolder> mutsMerging_2;
		Map<String, MutantsInformationHolder> mutsMerging_3;
		Map<String, MutantsInformationHolder> mutsMerging_4;
		Map<String, MutantsInformationHolder> mutsMerging_5;
		
		//MUTATIONS
		mutsMerging_1 = TestingTools.generateAndMergeMutations(propMerging_1);
		mutsMerging_2 = TestingTools.generateAndMergeMutations(propMerging_2);
		mutsMerging_3 = TestingTools.generateAndMergeMutations(propMerging_3);
		mutsMerging_4 = TestingTools.generateAndMergeMutations(propMerging_4);
		mutsMerging_5 = TestingTools.generateAndMergeMutations(propMerging_5);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propMerging_1, mutsMerging_1},
				{propMerging_2, mutsMerging_2},
				{propMerging_3, mutsMerging_3},
				{propMerging_4, mutsMerging_4},
				{propMerging_5, mutsMerging_5}
		});
	}
	
	@Test
	public void testOnlyOneMethodMutated() {
		assertTrue(mutations.size() == 1);
	}
	
	@Test
	public void testCorrectMethodMutated() {
		assertTrue(mutations.containsKey(prop.method));
	}
	
	@Test
	public void testExpectedMutations() {
		assertTrue(TestingTools.testCorrectMutationsObtained(prop, mutations));
	}
	

}
