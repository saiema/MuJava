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
public class PRVO_TwoByOne_OneByTwo_Disabled_Options_Tests {
	
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	@BeforeClass
	public static void setTestingToolsVerbose() {
		TestingTools.setVerbose(false);
	}
	
	public PRVO_TwoByOne_OneByTwo_Disabled_Options_Tests(Property p, List<MutantInfo> mutantsInfo) {
		this.prop = p;
		this.mutantsInfo = mutantsInfo;
	}

	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		/*
		 * ORIGINAL
		 * line 5 : newNode.previous = insertBeforeNode.previous; //mutGenLimit 2
         * line 6 : insertBeforeNode.previous.next = newNode; //mutGenLimit 2
		 */
		List<Pattern> ncll_me_addFirst_PRVOR = new LinkedList<Pattern>();
		List<Pattern> ncll_mne_addFirst_PRVOR = new LinkedList<Pattern>();
		//line 5 +++++++++++++++++++++++++++++++++++ (newNode.previous = insertBeforeNode.previous; //mutGenLimit 2)
			//sameLength+++
			ncll_me_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = insertBeforeNode\\.next; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = newNode\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = this\\.header\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = this\\.firstCachedNode\\.previous; //mutGenLimit 1"));
			//sameLength---
			//decreaseLength+++
			ncll_me_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = insertBeforeNode; //mutGenLimit 1"));
			//decreaseLength---
			//increaseLength+++
			ncll_me_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = insertBeforeNode\\.previous\\.next; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = insertBeforeNode\\.previous\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = insertBeforeNode\\.next\\.previous; //mutGenLimit 1"));
			//increaseLength---
			//replaceOneByTwo+++
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = insertBeforeNode\\.next\\.next; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = this\\.header\\.next\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = this\\.header\\.previous\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = this\\.firstCachedNode\\.next\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = this\\.firstCachedNode\\.previous\\.previous; //mutGenLimit 1"));
			//replaceOneByTwo---
			//replaceTwoByOne+++
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = newNode; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = this\\.header; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("newNode\\.previous = this\\.firstCachedNode; //mutGenLimit 1"));
			//replaceTwoByOne---
		//line 5 -----------------------------------
		//line 6 +++++++++++++++++++++++++++++++++++ (insertBeforeNode.previous.next = newNode; //mutGenLimit 2)
			//sameLength+++
			ncll_me_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = insertBeforeNode; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = this\\.header; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = this\\.firstCachedNode; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = null; //mutGenLimit 1"));
			//sameLength---
			//decreaseLength+++
			//decreaseLength---
			//increaseLength+++
			ncll_me_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = newNode\\.next; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = newNode\\.previous; //mutGenLimit 1"));
			//increaseLength---
			//replaceOneByTwo+++
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = insertBeforeNode\\.next; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = this\\.header\\.next; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = this\\.header\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = this\\.firstCachedNode\\.next; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOR.add(Pattern.compile("insertBeforeNode\\.previous\\.next = this\\.firstCachedNode\\.previous; //mutGenLimit 1"));
			//replaceOneByTwo---
			//replaceTwoByOne+++
			//replaceTwoByOne---
		//line 6 -----------------------------------
		Property prop_ncll_addFirst_PRVOR = new Property(MutationOperator.PRVOR_REFINED, "pldi/nodecachinglinkedlist/NodeCachingLinkedList", "addFirst", 14, 14, ncll_me_addFirst_PRVOR, ncll_mne_addFirst_PRVOR);
		
		/*
		 * ORIGINAL
		 * line 5 : newNode.previous = insertBeforeNode.previous; //mutGenLimit 2
         * line 6 : insertBeforeNode.previous.next = newNode; //mutGenLimit 2
         * 
		 */
		List<Pattern> ncll_me_addFirst_PRVOL = new LinkedList<Pattern>();
		List<Pattern> ncll_mne_addFirst_PRVOL = new LinkedList<Pattern>();
		//line 5 +++++++++++++++++++++++++++++++++++ (newNode.previous = insertBeforeNode.previous; //mutGenLimit 2)
			//sameLength+++
			ncll_me_addFirst_PRVOL.add(Pattern.compile("newNode\\.next = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.previous = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("this\\.header\\.previous = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("this\\.firstCachedNode\\.previous = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("newNode\\.value = insertBeforeNode\\.previous; //mutGenLimit 1"));
			//sameLength---
			//decreaseLength+++
			ncll_me_addFirst_PRVOL.add(Pattern.compile("newNode = insertBeforeNode\\.previous; //mutGenLimit 1"));
			//decreaseLength---
			//increaseLength+++
			ncll_me_addFirst_PRVOL.add(Pattern.compile("newNode\\.previous\\.value = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("newNode\\.previous\\.next = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("newNode\\.previous\\.previous = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("newNode\\.next\\.previous = insertBeforeNode\\.previous; //mutGenLimit 1"));
			//increaseLength---
			//replaceOneByTwo+++
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("newNode\\.next\\.next = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.next\\.previous = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.previous\\.previous = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.header\\.next\\.previous = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.header\\.previous\\.previous = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.firstCachedNode\\.next\\.previous = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.firstCachedNode\\.previous\\.previous = insertBeforeNode\\.previous; //mutGenLimit 1"));
			//replaceOneByTwo---
			//replaceTwoByOne+++
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.header = insertBeforeNode\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.firstCachedNode = insertBeforeNode\\.previous; //mutGenLimit 1"));
			//replaceTwoByOne---
		//line 5 -----------------------------------
		//line 6 +++++++++++++++++++++++++++++++++++ (insertBeforeNode.previous.next = newNode; //mutGenLimit 2)
			//sameLength+++
			ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.previous\\.previous = newNode; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.next\\.next = newNode; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("newNode\\.previous\\.next = newNode; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("this\\.header\\.previous\\.next = newNode; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("this\\.firstCachedNode\\.previous\\.next = newNode; //mutGenLimit 1"));
			//sameLength---
			//decreaseLength+++
			ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.previous = newNode; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.next = newNode; //mutGenLimit 1"));
			//decreaseLength---
			//increaseLength+++
			ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.previous\\.next\\.value = newNode; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.previous\\.next\\.next = newNode; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.previous\\.next\\.previous = newNode; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.previous\\.previous\\.next = newNode; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.next\\.previous\\.next = newNode; //mutGenLimit 1"));
			//increaseLength---
			//replaceOneByTwo+++
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.previous\\.previous\\.previous = newNode; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("insertBeforeNode\\.next\\.next\\.next = newNode; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("newNode\\.next\\.previous\\.next = newNode; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("newNode\\.previous\\.previous\\.next = newNode; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.header\\.next\\.previous\\.next = newNode; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.header\\.previous\\.previous\\.next = newNode; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.firstCachedNode\\.next\\.previous\\.next = newNode; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.firstCachedNode\\.previous\\.previous\\.next = newNode; //mutGenLimit 1"));
			//replaceOneByTwo---
			//replaceTwoByOne+++
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("newNode\\.next = newNode; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.header\\.next = newNode; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOL.add(Pattern.compile("this\\.firstCachedNode\\.next = newNode; //mutGenLimit 1"));
			//replaceTwoByOne---
		//line 6 -----------------------------------
		Property prop_ncll_addFirst_PRVOL = new Property(MutationOperator.PRVOL_SMART, "pldi/nodecachinglinkedlist/NodeCachingLinkedList", "addFirst", 23, 23, ncll_me_addFirst_PRVOL, ncll_mne_addFirst_PRVOL);
		
		/*
		 * ORIGINAL
		 * line 3 : LinkedListNode insertBeforeNode = this.header.next; //mutGenLimit 2
		 */
		List<Pattern> ncll_me_addFirst_PRVOU = new LinkedList<Pattern>();
		List<Pattern> ncll_mne_addFirst_PRVOU = new LinkedList<Pattern>();
		//line 3 +++++++++++++++++++++++++++++++++++
			//sameLength+++
			ncll_me_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = this\\.header\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = this\\.firstCachedNode\\.next; //mutGenLimit 1"));
			//sameLength---
			//decreaseLength+++
			ncll_me_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = this\\.header; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = header\\.next; //mutGenLimit 1"));
			//decreaseLength---
			//increaseLength+++
			ncll_me_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = this\\.header\\.next\\.next; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = this\\.header\\.next\\.previous; //mutGenLimit 1"));
			ncll_me_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = this\\.header\\.previous\\.next; //mutGenLimit 1"));
			//increaseLength---
			//replaceOneByTwo+++
			ncll_mne_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = this\\.header\\.previous\\.previous; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = this\\.firstCachedNode\\.next\\.next; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = this\\.firstCachedNode\\.previous\\.next; //mutGenLimit 1"));
			//replaceOneByTwo---
			//replaceTwoByOne+++
			ncll_mne_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = this\\.firstCacheNode; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = firstCacheNode\\.next; //mutGenLimit 1"));
			ncll_mne_addFirst_PRVOU.add(Pattern.compile("insertBeforeNode = newNode\\.next; //mutGenLimit 1"));
			//replaceTwoByOne---
		//line 3 -----------------------------------
		Property prop_ncll_addFirst_PRVOU = new Property(MutationOperator.PRVOU_REFINED, "pldi/nodecachinglinkedlist/NodeCachingLinkedList", "addFirst", 7, 7, ncll_me_addFirst_PRVOU, ncll_mne_addFirst_PRVOU);
		
		List<MutantInfo> mf_ncll_addFirst_PRVOR;
		List<MutantInfo> mf_ncll_addFirst_PRVOL;
		List<MutantInfo> mf_ncll_addFirst_PRVOU;
		
		Configuration.add(PRVO.ENABLE_ONE_BY_TWO_MUTANTS, Boolean.FALSE);
		Configuration.add(PRVO.ENABLE_TWO_BY_ONE_MUTANTS, Boolean.FALSE);
		List<String> bannedMethods = Arrays.asList(new String[]{"hashCode", "getClass", "toString", "toLowerCase", "intern", "toCharArray", "getBytes", "toUpperCase", "trim", "toLowerCase", "clone", "hash32", "serialPersistentFields", "serialVersionUID", "hash", "HASHING_SEED", "length", "isEmpty", "serialPersistentFields", "CASE_INSENSITIVE_ORDER"});
        Configuration.add(PRVO.PROHIBITED_METHODS, bannedMethods);
		
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
