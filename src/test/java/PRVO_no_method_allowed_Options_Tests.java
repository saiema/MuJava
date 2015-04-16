package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import mujava.api.Configuration;
import mujava.api.Mutant;
import mujava.app.MutantInfo;
import mujava.op.PRVO;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class PRVO_no_method_allowed_Options_Tests {
	
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	@BeforeClass
	public static void setTestingToolsVerbose() {
		TestingTools.setVerbose(false);
	}
	
	public PRVO_no_method_allowed_Options_Tests(Property p, List<MutantInfo> mutantsInfo) {
		this.prop = p;
		this.mutantsInfo = mutantsInfo;
	}

	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		List<Pattern> ncll_me_addFirst_PRVOR = new LinkedList<Pattern>();
		List<Pattern> ncll_mne_addFirst_PRVOR = new LinkedList<Pattern>();
		Property prop_ncll_addFirst_PRVOR = new Property(Mutant.PRVOR_REFINED, "pldi/nodecachinglinkedlist/NodeCachingLinkedList", "addFirst", 0, 0, ncll_me_addFirst_PRVOR, ncll_mne_addFirst_PRVOR);
		
		List<Pattern> ncll_me_addFirst_PRVOL = new LinkedList<Pattern>();
		List<Pattern> ncll_mne_addFirst_PRVOL = new LinkedList<Pattern>();
		Property prop_ncll_addFirst_PRVOL = new Property(Mutant.PRVOL_SMART, "pldi/nodecachinglinkedlist/NodeCachingLinkedList", "addFirst", 0, 0, ncll_me_addFirst_PRVOL, ncll_mne_addFirst_PRVOL);
		
		List<Pattern> ncll_me_addFirst_PRVOU = new LinkedList<Pattern>();
		List<Pattern> ncll_mne_addFirst_PRVOU = new LinkedList<Pattern>();
		Property prop_ncll_addFirst_PRVOU = new Property(Mutant.PRVOU_REFINED, "pldi/nodecachinglinkedlist/NodeCachingLinkedList", "addFirst", 0, 0, ncll_me_addFirst_PRVOU, ncll_mne_addFirst_PRVOU);
		
		List<MutantInfo> mf_ncll_addFirst_PRVOR;
		List<MutantInfo> mf_ncll_addFirst_PRVOL;
		List<MutantInfo> mf_ncll_addFirst_PRVOU;
		
		Configuration.add(PRVO.ENABLE_ONE_BY_TWO_MUTANTS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_TWO_BY_ONE_MUTANTS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_DECREASE_LENGTH_MUTANTS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_INCREASE_LENGTH_MUTANTS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_SAME_LENGTH_MUTANTS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_REPLACEMENT_WITH_LITERALS, Boolean.FALSE);
		
		mf_ncll_addFirst_PRVOR = TestingTools.generateMutants(prop_ncll_addFirst_PRVOR);
		mf_ncll_addFirst_PRVOL = TestingTools.generateMutants(prop_ncll_addFirst_PRVOL);
		mf_ncll_addFirst_PRVOU = TestingTools.generateMutants(prop_ncll_addFirst_PRVOU);
	
		Configuration.clear();
		
		return Arrays.asList(new Object[][] {
				{prop_ncll_addFirst_PRVOR, mf_ncll_addFirst_PRVOR},			//0
				{prop_ncll_addFirst_PRVOL, mf_ncll_addFirst_PRVOL},			//1
				{prop_ncll_addFirst_PRVOU, mf_ncll_addFirst_PRVOU},			//2
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
