package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.MutationExpected;
import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class ObtainingMutationsTest {
	
	private Property prop;
	private Map<String, MutantsInformationHolder> mutations;
	
	public ObtainingMutationsTest(Property prop, Map<String, MutantsInformationHolder> mutations) {
		this.prop = prop;
		this.mutations = mutations;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		
		//PROPERTIES
		List<MutationOperator> opsTAL_1 = new LinkedList<MutationOperator>();
		opsTAL_1.add(MutationOperator.COR);
		opsTAL_1.add(MutationOperator.COI);
		List<Integer> expectedAffectedLinesTAL_1 = new LinkedList<Integer>();
		expectedAffectedLinesTAL_1.add(1);
		expectedAffectedLinesTAL_1.add(2);
		expectedAffectedLinesTAL_1.add(3);
		List<MutationExpected> emgTAL_1 = new LinkedList<MutationExpected>();
		emgTAL_1.add(new MutationExpected("b1 && b2", "b1 || b2", 3));
		emgTAL_1.add(new MutationExpected("b1 && b2", "b1 ^ b2", 3));
		emgTAL_1.add(new MutationExpected("b1 && b2", "b1 & b2", 3));
		emgTAL_1.add(new MutationExpected("b1 && b2", "b1 | b2", 3));
		emgTAL_1.add(new MutationExpected("true", "!true", 1));
		emgTAL_1.add(new MutationExpected("false", "!false", 2));
		emgTAL_1.add(new MutationExpected("b1 && b2", "!(b1 && b2)", 3));
		emgTAL_1.add(new MutationExpected("b1", "!b1", 3));
		emgTAL_1.add(new MutationExpected("b2", "!b2", 3));
		Property propTAL_1 = 	new Property(
									opsTAL_1,
									"multiMutations/TAL_1",
									"radiatedMethod",
									emgTAL_1,
									expectedAffectedLinesTAL_1
								);
		
		List<MutationOperator> opsTAL_2 = new LinkedList<MutationOperator>();
		opsTAL_2.add(MutationOperator.COI);
		List<Integer> expectedAffectedLinesTAL_2 = new LinkedList<Integer>();
		expectedAffectedLinesTAL_2.add(4);
		expectedAffectedLinesTAL_2.add(5);
		expectedAffectedLinesTAL_2.add(7);
		List<MutationExpected> emgTAL_2 = new LinkedList<MutationExpected>();
		emgTAL_2.add(new MutationExpected("b3","!b3", 4));
		emgTAL_2.add(new MutationExpected("b2","!b2", 5));
		emgTAL_2.add(new MutationExpected("true","!true", 7));
		Property propTAL_2 = 	new Property(
									opsTAL_2,
									"multiMutations/TAL_2",
									"radiatedMethod",
									emgTAL_2,
									expectedAffectedLinesTAL_2
								);
		
		List<MutationOperator> opsTAL_3 = new LinkedList<MutationOperator>();
		opsTAL_3.add(MutationOperator.COI);
		opsTAL_3.add(MutationOperator.PRVOU);
		opsTAL_3.add(MutationOperator.PRVOR);
		opsTAL_3.add(MutationOperator.AODS);
		opsTAL_3.add(MutationOperator.ROR);
		List<Integer> expectedAffectedLinesTAL_3 = new LinkedList<Integer>();
		expectedAffectedLinesTAL_3.add(1);
		expectedAffectedLinesTAL_3.add(2);
		expectedAffectedLinesTAL_3.add(3);
		expectedAffectedLinesTAL_3.add(4);
		expectedAffectedLinesTAL_3.add(5);
		expectedAffectedLinesTAL_3.add(7);
		expectedAffectedLinesTAL_3.add(10);
		List<MutationExpected> emgTAL_3 = new LinkedList<MutationExpected>();
		emgTAL_3.add(new MutationExpected("i++","i", 7));
		emgTAL_3.add(new MutationExpected("i < array.length","i > array.length", 3));
		emgTAL_3.add(new MutationExpected("i < array.length","i >= array.length", 3));
		emgTAL_3.add(new MutationExpected("i < array.length","i <= array.length", 3));
		emgTAL_3.add(new MutationExpected("i < array.length","i == array.length", 3));
		emgTAL_3.add(new MutationExpected("i < array.length","i != array.length", 3));
		emgTAL_3.add(new MutationExpected("i < array.length","false", 3));
		emgTAL_3.add(new MutationExpected("i < array.length","true", 3));
		emgTAL_3.add(new MutationExpected("array[i] == value","array[i] > value", 4));
		emgTAL_3.add(new MutationExpected("array[i] == value","array[i] >= value", 4));
		emgTAL_3.add(new MutationExpected("array[i] == value","array[i] < value", 4));
		emgTAL_3.add(new MutationExpected("array[i] == value","array[i] <= value", 4));
		emgTAL_3.add(new MutationExpected("array[i] == value","array[i] != value", 4));
		emgTAL_3.add(new MutationExpected("array[i] == value","false", 4));
		emgTAL_3.add(new MutationExpected("array[i] == value","true", 4));
		emgTAL_3.add(new MutationExpected("false","!false", 1));
		emgTAL_3.add(new MutationExpected("i < array.length && !found","!(i < array.length && !found)", 3));
		emgTAL_3.add(new MutationExpected("i < array.length","!(i < array.length)", 3));
		emgTAL_3.add(new MutationExpected("!found","!(!found)", 3));
		emgTAL_3.add(new MutationExpected("true","!true", 5));
		emgTAL_3.add(new MutationExpected("found","!found", 10));
		emgTAL_3.add(new MutationExpected("true","found", 5));
		emgTAL_3.add(new MutationExpected("false","found", 1));
		emgTAL_3.add(new MutationExpected("0","super.hashCode()", 2));
		emgTAL_3.add(new MutationExpected("0","value", 2));
		emgTAL_3.add(new MutationExpected("0","i", 2));
		Property propTAL_3 = 	new Property(
									opsTAL_3,
									"multiMutations/TAL_3",
									"radiatedMethod",
									emgTAL_3,
									expectedAffectedLinesTAL_3
								);
		
		
		//MUTATIONS GENERATED
		Map<String, MutantsInformationHolder> mutsTAL_1;
		Map<String, MutantsInformationHolder> mutsTAL_2;
		Map<String, MutantsInformationHolder> mutsTAL_3;
		
		//MUTATIONS
		mutsTAL_1 = TestingTools.generateMutations(propTAL_1);
		mutsTAL_2 = TestingTools.generateMutations(propTAL_2);
		mutsTAL_3 = TestingTools.generateMutations(propTAL_3);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propTAL_1, mutsTAL_1},
				{propTAL_2, mutsTAL_2},
				{propTAL_3, mutsTAL_3}
		});
	}
	
	@Test
	public void testAffectedLines() {
		assertTrue(TestingTools.testCorrectLinesAffected(prop, mutations));
	}
	
	@Test
	public void testExpectedMutations() {
		assertTrue(TestingTools.testCorrectMutationsObtained(prop, mutations));
	}
	

}
