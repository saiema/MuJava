package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import mujava.api.Configuration;
import mujava.api.MutationOperator;
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
public class PRVO_replaceAllByOne_left_Options_Tests {
	
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	@BeforeClass
	public static void setTestingToolsVerbose() {
		TestingTools.setVerbose(false);
	}
	
	public PRVO_replaceAllByOne_left_Options_Tests(Property p, List<MutantInfo> mutantsInfo) {
		this.prop = p;
		this.mutantsInfo = mutantsInfo;
	}

	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		List<Pattern> ncll_me_addFirst_PRVOR = new LinkedList<Pattern>();
		List<Pattern> ncll_mne_addFirst_PRVOR = new LinkedList<Pattern>();
		Property prop_ncll_addFirst_PRVOR = new Property(MutationOperator.PRVOR_REFINED, "pldi/nodecachinglinkedlist/NodeCachingLinkedList", "addFirstUglyCopy", 0, 0, ncll_me_addFirst_PRVOR, ncll_mne_addFirst_PRVOR);
		
		/*
		 * ORIGINAL
		 *  
		 *  line 4 : insertBeforeNode = this.header.next; //mutGenLimit 2
         *	line 6 : newNode.previous.next.previous = insertBeforeNode.previous.next.previous; //mutGenLimit 2
         *	line 7 : insertBeforeNode.previous.next = newNode.next.previous; //mutGenLimit 2
         * 
		 */
		List<Pattern> ncll_me_addFirst_PRVOL = new LinkedList<Pattern>();
		List<Pattern> ncll_mne_addFirst_PRVOL = new LinkedList<Pattern>();
		//line 4 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//line 4 ---------------------------------------------------------
		//line 6 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		ncll_me_addFirst_PRVOL.add(Pattern.compile("o = insertBeforeNode\\.previous.next\\.previous; //mutGenLimit 1"));
		ncll_me_addFirst_PRVOL.add(Pattern.compile("newNode = insertBeforeNode\\.previous.next\\.previous; //mutGenLimit 1"));
		ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode = insertBeforeNode\\.previous.next\\.previous; //mutGenLimit 1"));
		ncll_me_addFirst_PRVOL.add(Pattern.compile("this\\.header = insertBeforeNode\\.previous.next\\.previous; //mutGenLimit 1"));
		ncll_me_addFirst_PRVOL.add(Pattern.compile("this\\.firstCachedNode = insertBeforeNode\\.previous.next\\.previous; //mutGenLimit 1"));
		//line 6 ---------------------------------------------------------
		//line 7 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		ncll_me_addFirst_PRVOL.add(Pattern.compile("o = newNode\\.next\\.previous; //mutGenLimit 1"));
		ncll_me_addFirst_PRVOL.add(Pattern.compile("newNode = newNode\\.next\\.previous; //mutGenLimit 1"));
		ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode = newNode\\.next\\.previous; //mutGenLimit 1"));
		ncll_me_addFirst_PRVOL.add(Pattern.compile("this\\.header = newNode\\.next\\.previous; //mutGenLimit 1"));
		ncll_me_addFirst_PRVOL.add(Pattern.compile("this\\.firstCachedNode = newNode\\.next\\.previous; //mutGenLimit 1"));
		//line 7 ---------------------------------------------------------
		Property prop_ncll_addFirst_PRVOL = new Property(MutationOperator.PRVOL_SMART, "pldi/nodecachinglinkedlist/NodeCachingLinkedList", "addFirstUglyCopy", 10, 10, ncll_me_addFirst_PRVOL, ncll_mne_addFirst_PRVOL);
		
		List<MutantInfo> mf_ncll_addFirst_PRVOR;
		List<MutantInfo> mf_ncll_addFirst_PRVOL;
		
		Configuration.add(PRVO.ENABLE_ONE_BY_TWO_MUTANTS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_TWO_BY_ONE_MUTANTS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_DECREASE_LENGTH_MUTANTS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_INCREASE_LENGTH_MUTANTS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_SAME_LENGTH_MUTANTS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_REPLACEMENT_WITH_LITERALS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_ALL_BY_ONE_MUTANTS_LEFT, Boolean.TRUE);
		
		mf_ncll_addFirst_PRVOR = TestingTools.generateMutants(prop_ncll_addFirst_PRVOR);
		mf_ncll_addFirst_PRVOL = TestingTools.generateMutants(prop_ncll_addFirst_PRVOL);
	
		Configuration.clear();
		
		return Arrays.asList(new Object[][] {
				{prop_ncll_addFirst_PRVOR, mf_ncll_addFirst_PRVOR},			//0
				{prop_ncll_addFirst_PRVOL, mf_ncll_addFirst_PRVOL},			//1
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
