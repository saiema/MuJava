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
public class AORUTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public AORUTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> mceOMUC = new LinkedList<Pattern>();
		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
		Property propOMUC = new Property(MutationOperator.AORU, "sort/Sorting", "ordered", 0, 0, mceOMUC, mcneOMUC);
		
		List<Pattern> mceSwap = new LinkedList<Pattern>();
		List<Pattern> mcneSwap = new LinkedList<Pattern>();
		mceSwap.add(Pattern.compile("Integer auxX = \\-a; //mutGenLimit 0"));
		mceSwap.add(Pattern.compile("Integer auxB = \\+\\(\\+b\\); //mutGenLimit 0"));
		mceSwap.add(Pattern.compile("Integer auxB = \\-\\(\\-b\\); //mutGenLimit 0"));
		mceSwap.add(Pattern.compile("auxB = \\-b; //mutGenLimit 0"));
		mceSwap.add(Pattern.compile("a = a \\+ \\+auxB; //mutGenLimit 0"));
		Property propSwap = new Property(MutationOperator.AORU, "sort/Sorting", "swap2", 4, 4, mceSwap, mcneSwap);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfSwap;
		
		//MUTANTS GENERATION
		mfOMUC = TestingTools.generateMutants(propOMUC);
		mfSwap = TestingTools.generateMutants(propSwap);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMUC, mfOMUC},
				{propSwap, mfSwap}
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
