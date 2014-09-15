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
public class AORSTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public AORSTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> mceOMUC = new LinkedList<Pattern>();
		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
		Property propOMUC = new Property(Mutant.AORS, "list/SinglyLinkedListOutsideMUC", "getHeader", 0, 0, mceOMUC, mcneOMUC);
		
		List<Pattern> mceSortingFor = new LinkedList<Pattern>();
		List<Pattern> mcneSortingFor = new LinkedList<Pattern>();
		Property propSortingFor = new Property(Mutant.AORS, "sort/Sorting", "selectionSort", 0, 0, mceSortingFor, mcneSortingFor);
		
		List<Pattern> mceSortingOrdered = new LinkedList<Pattern>();
		List<Pattern> mcneSortingOrdered = new LinkedList<Pattern>();
		mceSortingOrdered.add(Pattern.compile("for \\(int v = 1; v < arr\\.length; v\\-\\-\\)"));
		mcneSortingOrdered.add(Pattern.compile(".+//mutGenLimit -[0..9]+"));
		Property propSortingOrdered = new Property(Mutant.AORS, "sort/Sorting", "ordered", 1, 1, mceSortingOrdered, mcneSortingOrdered);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfSortingFor;
		List<MutantInfo> mfSortingOrdered;
		
		
		//MUTANTS GENERATION
		mfOMUC = TestingTools.generateMutants(propOMUC);
		mfSortingFor = TestingTools.generateMutants(propSortingFor);
		mfSortingOrdered = TestingTools.generateMutants(propSortingOrdered);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMUC, mfOMUC},
				{propSortingFor, mfSortingFor},
				{propSortingOrdered, mfSortingOrdered}
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
