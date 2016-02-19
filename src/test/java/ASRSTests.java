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
public class ASRSTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public ASRSTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> mceOMUC = new LinkedList<Pattern>();
		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
		Property propOMUC = new Property(MutationOperator.ASRS, "list/ListOps", "mult", 0, 0, mceOMUC, mcneOMUC);
		
		List<Pattern> mceAnd = new LinkedList<Pattern>();
		List<Pattern> mcneAnd = new LinkedList<Pattern>();
		mceAnd.add(Pattern.compile("res \\|= l\\[b\\]; //mutGenLimit 0"));
		mceAnd.add(Pattern.compile("res \\^= l\\[b\\]; //mutGenLimit 0"));
		mcneAnd.add(Pattern.compile("res \\+= l\\[b\\]; //mutGenLimit 0"));
		mcneAnd.add(Pattern.compile("res \\-= l\\[b\\]; //mutGenLimit 0"));
		mcneAnd.add(Pattern.compile("res \\*= l\\[b\\]; //mutGenLimit 0"));
		mcneAnd.add(Pattern.compile("res \\/= l\\[b\\]; //mutGenLimit 0"));
		mcneAnd.add(Pattern.compile("res \\%= l\\[b\\]; //mutGenLimit 0"));
		mcneAnd.add(Pattern.compile("res \\<<= l\\[b\\]; //mutGenLimit 0"));
		mcneAnd.add(Pattern.compile("res \\>>= l\\[b\\]; //mutGenLimit 0"));
		mcneAnd.add(Pattern.compile("res \\>>>= l\\[b\\]; //mutGenLimit 0"));
		Property propAnd = new Property(MutationOperator.ASRS, "list/ListOps", "and", 2, 2, mceAnd, mcneAnd);
		
		List<Pattern> mceSum = new LinkedList<Pattern>();
		List<Pattern> mcneSum = new LinkedList<Pattern>();
		mceSum.add(Pattern.compile("res \\-= l\\[i\\]; //mutGenLimit 0"));
		mceSum.add(Pattern.compile("res \\*= l\\[i\\]; //mutGenLimit 0"));
		mceSum.add(Pattern.compile("res \\/= l\\[i\\]; //mutGenLimit 0"));
		mceSum.add(Pattern.compile("res \\%= l\\[i\\]; //mutGenLimit 0"));
		mcneSum.add(Pattern.compile("res \\&= l\\[i\\]; //mutGenLimit 0"));
		mcneSum.add(Pattern.compile("res \\|= l\\[i\\]; //mutGenLimit 0"));
		mcneSum.add(Pattern.compile("res \\^= l\\[i\\]; //mutGenLimit 0"));
		mcneSum.add(Pattern.compile("res \\<<= l\\[i\\]; //mutGenLimit 0"));
		mcneSum.add(Pattern.compile("res \\>>= l\\[i\\]; //mutGenLimit 0"));
		mcneSum.add(Pattern.compile("res \\>>>= l\\[i\\]; //mutGenLimit 0"));
		Property propSum = new Property(MutationOperator.ASRS, "list/ListOps", "sum", 4, 4, mceSum, mcneSum);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfAnd;
		List<MutantInfo> mfSum;
		
		//MUTANTS GENERATION
		mfOMUC = TestingTools.generateMutants(propOMUC);
		mfAnd = TestingTools.generateMutants(propAnd);
		mfSum = TestingTools.generateMutants(propSum);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMUC, mfOMUC},
				{propAnd, mfAnd},
				{propSum, mfSum}
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
