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

import test.java.utils.BruteForceMutationsFilter;
import test.java.utils.ManualMutationsFilter;
import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class CompatibleMutationsTests {
	
	private Property prop;
	private Map<String, MutantsInformationHolder> mutations;
	
	public CompatibleMutationsTests(Property prop, Map<String, MutantsInformationHolder> mutations) {
		this.prop = prop;
		this.mutations = mutations;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		
		//PROPERTIES
		List<MutationOperator> opsMerging_1 = new LinkedList<MutationOperator>();
		opsMerging_1.add(MutationOperator.PRVOR);
		opsMerging_1.add(MutationOperator.AORB);
		opsMerging_1.add(MutationOperator.COR);
		opsMerging_1.add(MutationOperator.ROR);
		opsMerging_1.add(MutationOperator.EOC);
		Property propMerging_1 = new Property(
									opsMerging_1,
									"test/MultiMutations",
									"contains",
									null,
									false
								);
		
		List<MutationOperator> opsMerging_2 = new LinkedList<MutationOperator>();
		opsMerging_2.add(MutationOperator.PRVOR);
		opsMerging_2.add(MutationOperator.AORB);
		opsMerging_2.add(MutationOperator.COR);
		opsMerging_2.add(MutationOperator.ROR);
		opsMerging_2.add(MutationOperator.EOC);
		Property propMerging_2 = new Property(
									opsMerging_2,
									"test/MultiMutations",
									"contains",
									new BruteForceMutationsFilter(),
									true
								);
		
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
		
		List<MutationOperator> opsMerging_3 = new LinkedList<MutationOperator>();
		opsMerging_3.add(MutationOperator.PRVOR);
		opsMerging_3.add(MutationOperator.AORB);
		opsMerging_3.add(MutationOperator.COR);
		opsMerging_3.add(MutationOperator.ROR);
		opsMerging_3.add(MutationOperator.EOC);
		ManualMutationsFilter mmf_3 = new ManualMutationsFilter();
		Set<Integer> selectedMutations_3 = new HashSet<Integer>();
		selectedMutations_3.add(0);
		selectedMutations_3.add(1);
		mmf_3.selectMutations(selectedMutations_3);
		Property propMerging_3 = new Property(
									opsMerging_3,
									"test/MultiMutations",
									"contains",
									mmf_3,
									false
								);
		
		List<MutationOperator> opsMerging_4 = new LinkedList<MutationOperator>();
		opsMerging_4.add(MutationOperator.PRVOR);
		opsMerging_4.add(MutationOperator.AORB);
		opsMerging_4.add(MutationOperator.COR);
		opsMerging_4.add(MutationOperator.ROR);
		opsMerging_4.add(MutationOperator.EOC);
		ManualMutationsFilter mmf_4 = new ManualMutationsFilter();
		Set<Integer> selectedMutations_4 = new HashSet<Integer>();
		selectedMutations_4.add(3);
		selectedMutations_4.add(6);
		selectedMutations_4.add(12);
		mmf_4.selectMutations(selectedMutations_4);
		Property propMerging_4 = new Property(
									opsMerging_4,
									"test/MultiMutations",
									"contains",
									mmf_4,
									false
								);
		
		List<MutationOperator> opsMerging_5 = new LinkedList<MutationOperator>();
		opsMerging_5.add(MutationOperator.PRVOR);
		opsMerging_5.add(MutationOperator.AORB);
		opsMerging_5.add(MutationOperator.COR);
		opsMerging_5.add(MutationOperator.ROR);
		opsMerging_5.add(MutationOperator.EOC);
		ManualMutationsFilter mmf_5 = new ManualMutationsFilter();
		Set<Integer> selectedMutations_5 = new HashSet<Integer>();
		selectedMutations_5.add(6);
		selectedMutations_5.add(10);
		selectedMutations_5.add(14);
		mmf_5.selectMutations(selectedMutations_5);
		Property propMerging_5 = new Property(
									opsMerging_5,
									"test/MultiMutations",
									"contains",
									mmf_5,
									true
								);
		
		//MUTATIONS GENERATED
		Map<String, MutantsInformationHolder> mutsMerging_1;
		Map<String, MutantsInformationHolder> mutsMerging_2;
		Map<String, MutantsInformationHolder> mutsMerging_3;
		Map<String, MutantsInformationHolder> mutsMerging_4;
		Map<String, MutantsInformationHolder> mutsMerging_5;
		
		//MUTATIONS
		mutsMerging_1 = TestingTools.generateMutations(propMerging_1);
		mutsMerging_2 = TestingTools.generateMutations(propMerging_2);
		mutsMerging_3 = TestingTools.generateMutations(propMerging_3);
		mutsMerging_4 = TestingTools.generateMutations(propMerging_4);
		mutsMerging_5 = TestingTools.generateMutations(propMerging_5);
		
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
	public void testCanBeMergedAsExpected() {
		assertTrue(TestingTools.testMutationsShouldBeMerged(prop, mutations.get(prop.method).getMutantsIdentifiers()));
	}
	

}
