package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import mujava.api.Mutant;
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
		List<Mutant> operators = new LinkedList<Mutant>();
		operators.add(Mutant.PRVOR);
		operators.add(Mutant.AORB);
		operators.add(Mutant.COR);
		operators.add(Mutant.ROR);
		operators.add(Mutant.EOC);
		Property propCustom0 = new Property(operators,
											"test/MultiMutations",
											"contains",
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED);
		
		//AFFECTEDLINES
		List<Mutant> operators1 = new LinkedList<Mutant>();
		operators1.add(Mutant.COR);
		operators1.add(Mutant.COI);
		Property propCustom1 = new Property(operators1,
											"multiMutations/TAL_1",
											"radiatedMethod",
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED);	
		
		List<Mutant> operators2 = new LinkedList<Mutant>();
		operators2.add(Mutant.COI);
		Property propCustom2 = new Property(operators2,
											"multiMutations/TAL_2",
											"radiatedMethod",
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED);
		
		List<Mutant> operators3 = new LinkedList<Mutant>();
		operators3.add(Mutant.COI); 	//lines 1, 3, 5, 10
		operators3.add(Mutant.PRVOU);	//lines 1, 2, 10
		operators3.add(Mutant.PRVOR);	//lines 4, 5
		operators3.add(Mutant.AODS);	//lines 7
		operators3.add(Mutant.ROR);
		Property propCustom3 = new Property(operators3,
											"multiMutations/TAL_3",
											"radiatedMethod",
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED);
		
		List<Mutant> operators4 = new LinkedList<Mutant>();
		operators4.add(Mutant.PRVOR);
		List<MutationExpected> mutationsExpected = new LinkedList<MutationExpected>();
		MutationExpected me1 = new MutationExpected("this.header.next", "header.next", 3, Mutant.PRVOR);
		mutationsExpected.add(me1);
		SpecificMutationsFilter filter4 = new SpecificMutationsFilter(mutationsExpected);
		Property propCustom4 = new Property(operators4,
									"roops/core/objects/SinglyLinkedList2",
									"contains",
									filter4,
									false);
		
		List<Mutant> operators5 = new LinkedList<Mutant>();
		operators5.add(Mutant.PRVOR);
		operators5.add(Mutant.COI);
		List<MutationExpected> mutationsExpected5 = new LinkedList<MutationExpected>();
		MutationExpected me5_1 = new MutationExpected("current.next.next", "current.next", 23, Mutant.PRVOR);
		MutationExpected me5_2 = new MutationExpected("false", "!false", 8, Mutant.COI);
		mutationsExpected5.add(me5_1);
		mutationsExpected5.add(me5_2);
		SpecificMutationsFilter filter5 = new SpecificMutationsFilter(mutationsExpected5);
		Property propCustom5 = new Property(operators5,
									"roops/core/objects/SinglyLinkedList2",
									"contains",
									filter5,
									false);
		
		List<Mutant> operators6 = new LinkedList<Mutant>();
		operators6.add(Mutant.PRVOR);
		List<MutationExpected> mutationsExpected6 = new LinkedList<MutationExpected>();
		MutationExpected me6_1 = new MutationExpected("false", "this.showInstance()", 8, Mutant.PRVOR);
		mutationsExpected6.add(me6_1);
		SpecificMutationsFilter filter6 = new SpecificMutationsFilter(mutationsExpected6);
		Property propCustom6 = new Property(operators6,
									"roops/core/objects/SinglyLinkedList2",
									"contains",
									filter6,
									false);
		
		List<Mutant> operators7 = new LinkedList<Mutant>();
		operators7.add(Mutant.PRVOL);
		operators7.add(Mutant.PRVOR);
		operators7.add(Mutant.PRVOU);
		operators7.add(Mutant.AODS);
		operators7.add(Mutant.AODU);
		operators7.add(Mutant.AOIS);
		operators7.add(Mutant.AOIU);
		operators7.add(Mutant.AORB);
		operators7.add(Mutant.AORS);
		operators7.add(Mutant.AORU);
		operators7.add(Mutant.ASRS);
		operators7.add(Mutant.COD);
		operators7.add(Mutant.COI);
		Property propCustom7 = new Property(operators7,
									"bugHunting/RepeatedLine",
									"getNode",
									TestingTools.NO_MUTANTS_EXPECTED,
									TestingTools.NO_MUTANTS_EXPECTED,
									TestingTools.NO_PATTERN_EXPECTED,
									TestingTools.NO_PATTERN_EXPECTED);
		
		List<Mutant> operators8 = new LinkedList<Mutant>();
//		operators8.add(Mutant.PRVOL);
//		operators8.add(Mutant.PRVOR);
//		operators8.add(Mutant.PRVOU);
//		operators8.add(Mutant.PRVOR_REFINED);
//		operators8.add(Mutant.PRVOU_REFINED);
//		operators8.add(Mutant.AODS);
//		operators8.add(Mutant.AODU);
//		operators8.add(Mutant.AOIS);
//		operators8.add(Mutant.AOIU);
//		operators8.add(Mutant.AORB);
//		operators8.add(Mutant.AORS);
//		operators8.add(Mutant.AORU);
//		operators8.add(Mutant.ASRS);
//		operators8.add(Mutant.COD);
//		operators8.add(Mutant.COI);
		operators8.add(Mutant.ROR);
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
		
		
		List<Mutant> operators9 = new LinkedList<Mutant>();
		operators9.add(Mutant.PRVOL_SMART);
		operators9.add(Mutant.PRVOR_REFINED);
		operators9.add(Mutant.PRVOU_REFINED);
		Property propCustom9 = new Property(operators9,
									"utils/SimpleClass",
									"twicePlusOne",
									TestingTools.NO_MUTANTS_EXPECTED,
									TestingTools.NO_MUTANTS_EXPECTED,
									TestingTools.NO_PATTERN_EXPECTED,
									TestingTools.NO_PATTERN_EXPECTED);
		
		List<Mutant> operators10 = Arrays.asList(new Mutant[]{
				Mutant.AMC,
				Mutant.AODS,
				Mutant.AODU,
				Mutant.AOIS,
				Mutant.AOIU,
				Mutant.AORB,
				Mutant.AORS,
				Mutant.AORU,
				Mutant.ASRS,
				Mutant.COD,
				Mutant.COI,
				Mutant.COR,
				Mutant.COR,
				Mutant.EAM,
				Mutant.EMM,
				Mutant.ROR,
				Mutant.SOR,
				Mutant.EOA_STRICT,
				Mutant.EOC_SMART,
				Mutant.IHD,
				Mutant.IHI,
				Mutant.IOD,
				Mutant.IOP,
				Mutant.IPC,
				Mutant.ISD_SMART,
				Mutant.ISI_SMART,
				Mutant.JDC,
				Mutant.JID,
				Mutant.JSD,
				Mutant.JSI,
				Mutant.JTD,
				Mutant.JTI_SMART,
				Mutant.LOD,
				Mutant.LOI,
				Mutant.LOR,
				Mutant.OAN_RELAXED,
				Mutant.OMR,
				Mutant.PCC,
				Mutant.PCD,
				Mutant.PNC,
				Mutant.PPD,
				Mutant.PRVOL_SMART,
				Mutant.PRVOR_REFINED,
				Mutant.PRVOU_REFINED
		});
		Property propCustom10 = new Property(operators10,
											"roops/core/objects/SinglyLinkedList_stryker",
											"getNode",
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_MUTANTS_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											TestingTools.NO_PATTERN_EXPECTED,
											new MultiMutationsFilter(),
											true);
		
		
		
		//MUTANTS FOLDERS
		
		//MUTANTS GENERATION
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propCustom10},
		});
	}
	
	@Test
	public void mutantGeneration() {
		assertTrue(!TestingTools.generateMutants(prop, true).isEmpty());
		//assertTrue(TestingTools.obtainMutations(prop));
	}
	

}
