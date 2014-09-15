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
public class AORBTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public AORBTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> mceOMUC = new LinkedList<Pattern>();
		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
		Property propOMUC = new Property(Mutant.AORB, "sort/Sorting", "mergeSort", 0, 0, mceOMUC, mcneOMUC);
		
		
		List<Pattern> mceSortingSwap = new LinkedList<Pattern>();
		List<Pattern> mcneSortingSwap = new LinkedList<Pattern>();
		mceSortingSwap.add(Pattern.compile("a = a \\- auxB; //mutGenLimit 0"));
		mceSortingSwap.add(Pattern.compile("Integer auxB = -b; //mutGenLimit 1"));
		mceSortingSwap.add(Pattern.compile("a = a \\- \\-auxB; //mutGenLimit 0"));
		mceSortingSwap.add(Pattern.compile("a = a \\* \\-auxB; //mutGenLimit 0"));
		mceSortingSwap.add(Pattern.compile("a = a \\% \\-auxB; //mutGenLimit 0"));
		mceSortingSwap.add(Pattern.compile("a = a \\/ \\-auxB; //mutGenLimit 0"));
		mcneSortingSwap.add(Pattern.compile(".+//mutGenLimit \\-[0..9]+"));
		Property propSortingSwap = new Property(Mutant.AORB, "sort/Sorting", "swap", 4, 4, mceSortingSwap, mcneSortingSwap);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfSortingSwap;
		
		
		//MUTANTS GENERATION
		mfOMUC = TestingTools.generateMutants(propOMUC);
		mfSortingSwap = TestingTools.generateMutants(propSortingSwap);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMUC, mfOMUC},
				{propSortingSwap, mfSortingSwap}
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
