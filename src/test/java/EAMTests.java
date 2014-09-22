package test.java;

import static org.junit.Assert.*;

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

import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class EAMTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public EAMTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		
		/*
		 * Original code
		 * 
		 * 	if (get(i) == data) {
		 *		return true;
		 *	} //mutGenLimit 1
		 */
		List<Pattern> eamce_1 = new LinkedList<Pattern>();
		eamce_1.add(Pattern.compile("if \\(getFromLast\\( i \\) == data\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*return true;([ \t\n\f\r])*\\}"));
		eamce_1.add(Pattern.compile("if \\(getFromStart\\( i \\) == data\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*return true;([ \t\n\f\r])*\\}"));
		List<Pattern> eamcne_1 = new LinkedList<Pattern>();
		Property propEAM_1 = new Property(Mutant.EAM,
										"objects/DoubleLinkedList",
										"find",
										2,
										2,
										eamce_1,
										eamcne_1);
		
		/*
		 * 	Original code
		 * 
		 * 	Object data = getData(); //mutGenLimit 1
		 *	DoubleLinkedListNode next = getNext(); //mutGenLimit 1
		 *	DoubleLinkedListNode prev = getPrev(); //mutGenLimit 1 
		 */
		List<Pattern> eamce_2 = new LinkedList<Pattern>();
		eamce_2.add(Pattern.compile("(.+\\.)?Object data = getNext\\(\\); //mutGenLimit 0"));
		eamce_2.add(Pattern.compile("(.+\\.)?Object data = getPrev\\(\\); //mutGenLimit 0"));
		eamce_2.add(Pattern.compile("(.+\\.)?DoubleLinkedListNode next = getPrev\\(\\); //mutGenLimit 0"));
		eamce_2.add(Pattern.compile("(.+\\.)?DoubleLinkedListNode prev = getNext\\(\\); //mutGenLimit 0"));
		List<Pattern> eamcne_2 = new LinkedList<Pattern>();
		Property propEAM_2 = new Property(Mutant.EAM,
										"objects/DoubleLinkedListNode",
										"toString",
										4,
										4,
										eamce_2,
										eamcne_2);
		
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfEAM_1;
		List<MutantInfo> mfEAM_2;
		
		//MUTANTS GENERATION
		mfEAM_1 = TestingTools.generateMutants(propEAM_1);
		mfEAM_2 = TestingTools.generateMutants(propEAM_2);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propEAM_1, mfEAM_1},
				{propEAM_2, mfEAM_2},
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
