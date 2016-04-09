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
public class SORTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public SORTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> mceOMUC = new LinkedList<Pattern>();
		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
		Property propOMUC = new Property(MutationOperator.SOR, "utils/Set", "add", 0, 0, mceOMUC, mcneOMUC);
		
		List<Pattern> mceRem = new LinkedList<Pattern>();
		List<Pattern> mcneRem = new LinkedList<Pattern>();
		mceRem.add(Pattern.compile("set \\&= \\~\\(1 \\>\\> elem\\); //mutGenLimit 0"));
		mceRem.add(Pattern.compile("set \\&= \\~\\(1 \\>\\>\\> elem\\); //mutGenLimit 0"));
		Property propRem = new Property(MutationOperator.SOR, "utils/Set", "remove", 2, 2, mceRem, mcneRem);
		
		List<Pattern> mceContains = new LinkedList<Pattern>();
		List<Pattern> mcneContains = new LinkedList<Pattern>();
		mceContains.add(Pattern.compile("return \\(set \\& 1 \\>\\> elem\\) \\!= 0; //mutGenLimit 0"));
		mceContains.add(Pattern.compile("return \\(set \\& 1 \\>\\>\\> elem\\) \\!= 0; //mutGenLimit 0"));
		Property propContains = new Property(MutationOperator.SOR, "utils/Set", "contains", 2, 2, mceContains, mcneContains);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfRem;
		List<MutantInfo> mfContains;
		
		//MUTANTS GENERATION
		mfOMUC = TestingTools.generateMutants(propOMUC);
		mfRem = TestingTools.generateMutants(propRem);
		mfContains = TestingTools.generateMutants(propContains);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMUC, mfOMUC},
				{propRem, mfRem},
				{propContains, mfContains}
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
