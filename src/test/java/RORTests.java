package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import mujava.api.MutationOperator;
import mujava.app.MutantInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class RORTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public RORTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> mceOMUC = new LinkedList<Pattern>();
		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
		Property propOMUC = new Property(MutationOperator.ROR, "sort/Sorting", "mergeSort", 0, 0, mceOMUC, mcneOMUC);
		
		List<Pattern> mceMerge = new LinkedList<Pattern>();
		List<Pattern> mcneMerge = new LinkedList<Pattern>();
		mceMerge.add(Pattern.compile("while \\(i \\> lenL \\|\\| j \\< lenR\\) \\{ //mutGenLimit 0"));
		mceMerge.add(Pattern.compile("while \\(i \\<= lenL \\|\\| j \\< lenR\\) \\{ //mutGenLimit 0"));
		mceMerge.add(Pattern.compile("while \\(i \\>= lenL \\|\\| j \\< lenR\\) \\{ //mutGenLimit 0"));
		mceMerge.add(Pattern.compile("while \\(i \\!= lenL \\|\\| j \\< lenR\\) \\{ //mutGenLimit 0"));
		mceMerge.add(Pattern.compile("while \\(i == lenL \\|\\| j \\< lenR\\) \\{ //mutGenLimit 0"));
		
		mceMerge.add(Pattern.compile("while \\(i \\< lenL \\|\\| j \\> lenR\\) \\{ //mutGenLimit 0"));
		mceMerge.add(Pattern.compile("while \\(i \\< lenL \\|\\| j \\<= lenR\\) \\{ //mutGenLimit 0"));
		mceMerge.add(Pattern.compile("while \\(i \\< lenL \\|\\| j \\>= lenR\\) \\{ //mutGenLimit 0"));
		mceMerge.add(Pattern.compile("while \\(i \\< lenL \\|\\| j \\!= lenR\\) \\{ //mutGenLimit 0"));
		mceMerge.add(Pattern.compile("while \\(i \\< lenL \\|\\| j == lenR\\) \\{ //mutGenLimit 0"));
		
		mceMerge.add(Pattern.compile("while \\(true \\|\\| j \\< lenR\\) \\{ //mutGenLimit 0"));
		mceMerge.add(Pattern.compile("while \\(false \\|\\| j \\< lenR\\) \\{ //mutGenLimit 0"));
		
		mceMerge.add(Pattern.compile("while \\(i \\< lenL \\|\\| true\\) \\{ //mutGenLimit 0"));
		mceMerge.add(Pattern.compile("while \\(i \\< lenL \\|\\| false\\) \\{ //mutGenLimit 0"));
		Property propMerge = new Property(MutationOperator.ROR, "sort/Sorting", "merge", 14, 14, mceMerge, mcneMerge);
		
		List<Pattern> mceOrdered = new LinkedList<Pattern>();
		List<Pattern> mcneOrdered = new LinkedList<Pattern>();
		mceOrdered.add(Pattern.compile("for \\(int v = 1; v \\> arr\\.length; v\\+\\+\\) \\{ //mutGenLimit 0"));
		mceOrdered.add(Pattern.compile("for \\(int v = 1; v \\<= arr\\.length; v\\+\\+\\) \\{ //mutGenLimit 0"));
		mceOrdered.add(Pattern.compile("for \\(int v = 1; v \\>= arr\\.length; v\\+\\+\\) \\{ //mutGenLimit 0"));
		mceOrdered.add(Pattern.compile("for \\(int v = 1; v \\!= arr\\.length; v\\+\\+\\) \\{ //mutGenLimit 0"));
		mceOrdered.add(Pattern.compile("for \\(int v = 1; v == arr\\.length; v\\+\\+\\) \\{ //mutGenLimit 0"));
		
		mceOrdered.add(Pattern.compile("for \\(int v = 1; true; v\\+\\+\\) \\{ //mutGenLimit 0"));
		mceOrdered.add(Pattern.compile("for \\(int v = 1; false; v\\+\\+\\) \\{ //mutGenLimit 0"));
		Property propOrdered = new Property(MutationOperator.ROR, "sort/Sorting", "ordered", 7, 6, mceOrdered, mcneOrdered);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfMerge;
		List<MutantInfo> mfOrdered;
		
		//MUTANTS GENERATION
		mfOMUC = TestingTools.generateMutants(propOMUC);
		mfMerge = TestingTools.generateMutants(propMerge);
		mfOrdered = TestingTools.generateMutants(propOrdered);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMUC, mfOMUC},
				{propMerge, mfMerge},
				{propOrdered, mfOrdered}
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
