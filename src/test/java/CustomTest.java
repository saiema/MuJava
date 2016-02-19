package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import mujava.api.MutationOperator;
import mujava.api.MutantsInformationHolder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.LineMutationsFilter;
import test.java.utils.ManualMutationsFilter;
import test.java.utils.MultiMutationsFilter;
import test.java.utils.MutationExpected;
import test.java.utils.Property;
import test.java.utils.SpecificMutationsFilter;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class CustomTest {
	
	private Property prop;
	
	public CustomTest(Property prop) {
		this.prop = prop;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		TestingTools.setVerbose(false);
		MutantsInformationHolder.setVerbose(false);
		
		//TESTS DEFINITIONS
		//MULTIMUTATIONS
		List<MutationOperator> operators = new LinkedList<MutationOperator>();
		operators.add(MutationOperator.PRVOR);
		operators.add(MutationOperator.AORB);
		operators.add(MutationOperator.COR);
		operators.add(MutationOperator.ROR);
		operators.add(MutationOperator.EOC);
		Property propCustom0 = new Property(operators,
											"test/MultiMutations",
											"contains",
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED);
		
		//AFFECTEDLINES
		List<MutationOperator> operators1 = new LinkedList<MutationOperator>();
		operators1.add(MutationOperator.COR);
		operators1.add(MutationOperator.COI);
		Property propCustom1 = new Property(operators1,
											"multiMutations/TAL_1",
											"radiatedMethod",
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED);	
		
		List<MutationOperator> operators2 = new LinkedList<MutationOperator>();
		operators2.add(MutationOperator.COI);
		Property propCustom2 = new Property(operators2,
											"multiMutations/TAL_2",
											"radiatedMethod",
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED);
		
		List<MutationOperator> operators3 = new LinkedList<MutationOperator>();
		operators3.add(MutationOperator.COI); 	//lines 1, 3, 5, 10
		operators3.add(MutationOperator.PRVOU);	//lines 1, 2, 10
		operators3.add(MutationOperator.PRVOR);	//lines 4, 5
		operators3.add(MutationOperator.AODS);	//lines 7
		operators3.add(MutationOperator.ROR);
		Property propCustom3 = new Property(operators3,
											"multiMutations/TAL_3",
											"radiatedMethod",
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED);
		
		List<MutationOperator> operators4 = new LinkedList<MutationOperator>();
		operators4.add(MutationOperator.PRVOR);
		List<MutationExpected> mutationsExpected = new LinkedList<MutationExpected>();
		MutationExpected me1 = new MutationExpected("this.header.next", "header.next", 3, MutationOperator.PRVOR);
		mutationsExpected.add(me1);
		SpecificMutationsFilter filter4 = new SpecificMutationsFilter(mutationsExpected);
		Property propCustom4 = new Property(operators4,
									"roops/core/objects/SinglyLinkedList2",
									"contains",
									filter4,
									false);
		
		List<MutationOperator> operators5 = new LinkedList<MutationOperator>();
		operators5.add(MutationOperator.PRVOR);
		operators5.add(MutationOperator.COI);
		List<MutationExpected> mutationsExpected5 = new LinkedList<MutationExpected>();
		MutationExpected me5_1 = new MutationExpected("current.next.next", "current.next", 23, MutationOperator.PRVOR);
		MutationExpected me5_2 = new MutationExpected("false", "!false", 8, MutationOperator.COI);
		mutationsExpected5.add(me5_1);
		mutationsExpected5.add(me5_2);
		SpecificMutationsFilter filter5 = new SpecificMutationsFilter(mutationsExpected5);
		Property propCustom5 = new Property(operators5,
									"roops/core/objects/SinglyLinkedList2",
									"contains",
									filter5,
									false);
		
		List<MutationOperator> operators6 = new LinkedList<MutationOperator>();
		operators6.add(MutationOperator.PRVOR);
		List<MutationExpected> mutationsExpected6 = new LinkedList<MutationExpected>();
		MutationExpected me6_1 = new MutationExpected("false", "this.showInstance()", 8, MutationOperator.PRVOR);
		mutationsExpected6.add(me6_1);
		SpecificMutationsFilter filter6 = new SpecificMutationsFilter(mutationsExpected6);
		Property propCustom6 = new Property(operators6,
									"roops/core/objects/SinglyLinkedList2",
									"contains",
									filter6,
									false);
		
		List<MutationOperator> operators7 = new LinkedList<MutationOperator>();
		operators7.add(MutationOperator.PRVOL);
		operators7.add(MutationOperator.PRVOR);
		operators7.add(MutationOperator.PRVOU);
		operators7.add(MutationOperator.AODS);
		operators7.add(MutationOperator.AODU);
		operators7.add(MutationOperator.AOIS);
		operators7.add(MutationOperator.AOIU);
		operators7.add(MutationOperator.AORB);
		operators7.add(MutationOperator.AORS);
		operators7.add(MutationOperator.AORU);
		operators7.add(MutationOperator.ASRS);
		operators7.add(MutationOperator.COD);
		operators7.add(MutationOperator.COI);
		Property propCustom7 = new Property(operators7,
									"bugHunting/RepeatedLine",
									"getNode",
									TestingTools.NO_MUTANTS_EXPECTED,
									TestingTools.NO_MUTANTS_EXPECTED,
									TestingTools.NO_PATTERN_EXPECTED,
									TestingTools.NO_PATTERN_EXPECTED);
		
		List<MutationOperator> operators8 = new LinkedList<MutationOperator>();
//		operators8.add(MutationOperator.PRVOL);
//		operators8.add(MutationOperator.PRVOR);
//		operators8.add(MutationOperator.PRVOU);
//		operators8.add(MutationOperator.PRVOR_REFINED);
//		operators8.add(MutationOperator.PRVOU_REFINED);
//		operators8.add(MutationOperator.AODS);
//		operators8.add(MutationOperator.AODU);
//		operators8.add(MutationOperator.AOIS);
//		operators8.add(MutationOperator.AOIU);
//		operators8.add(MutationOperator.AORB);
//		operators8.add(MutationOperator.AORS);
//		operators8.add(MutationOperator.AORU);
//		operators8.add(MutationOperator.ASRS);
//		operators8.add(MutationOperator.COD);
//		operators8.add(MutationOperator.COI);
		operators8.add(MutationOperator.ROR);
		LineMutationsFilter filter8 = new LineMutationsFilter();
		Set<Integer> selectedLines8 = new TreeSet<Integer>();
		selectedLines8.add(4);
		selectedLines8.add(5);
		filter8.setAcceptedLines(selectedLines8);
		Property propCustom8 = new Property(operators8,
									"main/Gcd",//"roops/core/objects/SinglyLinkedList",
									"gcd",//"getNode",
									null,
									false);
		
		
		List<MutationOperator> operators9 = new LinkedList<MutationOperator>();
		operators9.add(MutationOperator.PRVOL_SMART);
		operators9.add(MutationOperator.PRVOR_REFINED);
		operators9.add(MutationOperator.PRVOU_REFINED);
		Property propCustom9 = new Property(operators9,
									"utils/SimpleClass",
									"twicePlusOne",
									TestingTools.NO_MUTANTS_EXPECTED,
									TestingTools.NO_MUTANTS_EXPECTED,
									TestingTools.NO_PATTERN_EXPECTED,
									TestingTools.NO_PATTERN_EXPECTED);
		
		List<MutationOperator> operators10 = Arrays.asList(new MutationOperator[]{
				MutationOperator.ROR,
				MutationOperator.PRVOU_REFINED
		});
		List<MutationExpected> expectedMutations10 = new LinkedList<>();
		MutationExpected prvou_refined_line_10 = new MutationExpected("null", "valueParam", 10, MutationOperator.PRVOU_REFINED);
		MutationExpected ror_line_7 = new MutationExpected("current.value != null", "current.value == null", 7, MutationOperator.ROR);
		expectedMutations10.add(prvou_refined_line_10);
		expectedMutations10.add(ror_line_7);
		SpecificMutationsFilter filter10 = new SpecificMutationsFilter(expectedMutations10);
		Property propCustom10 = new Property(operators10,
											"roops/core/objects/SinglyLinkedList",
											"containsBis",
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											filter10,
											true);
		
		List<MutationOperator> operators11 = new LinkedList<>();
		operators11.add(MutationOperator.AODS);
		operators11.add(MutationOperator.AODU);
		operators11.add(MutationOperator.AOIS);
		operators11.add(MutationOperator.AOIU);
		operators11.add(MutationOperator.AORB);
		operators11.add(MutationOperator.AORS);
		operators11.add(MutationOperator.AORU);
		operators11.add(MutationOperator.ASRS);
		operators11.add(MutationOperator.COD);
		operators11.add(MutationOperator.COI);
		operators11.add(MutationOperator.COR);
		operators11.add(MutationOperator.LOD);
		operators11.add(MutationOperator.LOI);
		operators11.add(MutationOperator.LOR);
		operators11.add(MutationOperator.ROR);
		operators11.add(MutationOperator.SOR);
		operators11.add(MutationOperator.PRVOL_SMART);
		operators11.add(MutationOperator.PRVOR_REFINED);
		operators11.add(MutationOperator.PRVOU_REFINED);
		Property propCustom11 = new Property(operators11,
											"bugHunting/Digits",
											"digits",
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											null,
											false);
		
		
		
		//MUTANTS FOLDERS
		
		//MUTANTS GENERATION
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propCustom11},
		});
	}
	
	@Test
	public void mutantGeneration() {
		assertTrue(!TestingTools.generateMutants(prop, true).isEmpty());
		//assertTrue(TestingTools.obtainMutations(prop));
	}
	

}
