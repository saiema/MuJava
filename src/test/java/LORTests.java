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
public class LORTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public LORTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> mceOMUC = new LinkedList<Pattern>();
		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
		Property propOMUC = new Property(MutationOperator.LOR, "utils/Set", "subtraction", 0, 0, mceOMUC, mcneOMUC);
		
		List<Pattern> mceUnion = new LinkedList<Pattern>();
		List<Pattern> mcneUnion = new LinkedList<Pattern>();
		mceUnion.add(Pattern.compile("unionSet.set = this\\.set \\& s\\.set; //mutGenLimit 0"));
		mceUnion.add(Pattern.compile("unionSet.set = this\\.set \\^ s\\.set; //mutGenLimit 0"));
		Property propUnion = new Property(MutationOperator.LOR, "utils/Set", "union", 2, 2, mceUnion, mcneUnion);
		
		List<Pattern> mceContains = new LinkedList<Pattern>();
		List<Pattern> mcneContains = new LinkedList<Pattern>();
		mceContains.add(Pattern.compile("return \\(set \\| 1 \\<\\< elem\\) \\!= 0; //mutGenLimit 0"));
		mceContains.add(Pattern.compile("return \\(set \\^ 1 \\<\\< elem\\) \\!= 0; //mutGenLimit 0"));
		Property propContains = new Property(MutationOperator.LOR, "utils/Set", "contains", 2, 2, mceContains, mcneContains);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfUnion;
		List<MutantInfo> mfContains;
		
		//MUTANTS GENERATION
		mfOMUC = TestingTools.generateMutants(propOMUC);
		mfUnion = TestingTools.generateMutants(propUnion);
		mfContains = TestingTools.generateMutants(propContains);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMUC, mfOMUC},
				{propUnion, mfUnion},
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
