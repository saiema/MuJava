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
public class AODSTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public AODSTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
//		List<Pattern> mceOMUC = new LinkedList<Pattern>();
//		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
//		Property propOMUC = new Property(Mutant.AODS, "list/SinglyLinkedListOutsideMUC", "getHeader", 0, 0, mceOMUC, mcneOMUC);
		
		List<Pattern> mceSortingFor = new LinkedList<Pattern>();
		List<Pattern> mcneSortingFor = new LinkedList<Pattern>();
		Property propSortingFor = new Property(Mutant.AODS, "sort/Sorting", "selectionSort", 0, 0, mceSortingFor, mcneSortingFor);
		
		List<Pattern> mceSortingMerge = new LinkedList<Pattern>();
		List<Pattern> mcneSortingMerge = new LinkedList<Pattern>();
		mceSortingMerge.add(Pattern.compile("i; //mutGenLimit 0"));
		mceSortingMerge.add(Pattern.compile("j; //mutGenLimit 0"));
		mceSortingMerge.add(Pattern.compile("i++; //mutGenLimit 0"));
		mceSortingMerge.add(Pattern.compile("j++; //mutGenLimit 0"));
		mcneSortingMerge.add(Pattern.compile(".+//mutGenLimit -[0..9]+"));
		Property propSortingMerge = new Property(Mutant.AODS, "sort/Sorting", "merge", 2, 0, mceSortingMerge, mcneSortingMerge);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfSortingFor;
		List<MutantInfo> mfSortingMerge;
		
		
		//MUTANTS GENERATION
		//mfOMUC = TestingTools.generateMutants(propOMUC);
		mfSortingFor = TestingTools.generateMutants(propSortingFor);
		mfSortingMerge = TestingTools.generateMutants(propSortingMerge);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				//{propOMUC, mfOMUC},
				{propSortingFor, mfSortingFor},
				{propSortingMerge, mfSortingMerge}
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
