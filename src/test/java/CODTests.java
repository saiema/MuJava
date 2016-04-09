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
public class CODTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public CODTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		/*
		  	boolean allTrue = !(!auxA || !auxB); //mutGenLimit 1
			boolean allFalse = (!auxA && !auxB); //mutGenLimit 2
			return !(allTrue || allFalse); //mutGenLimit 1
		 */
		
		//TESTS DEFINITIONS
		List<Pattern> mceOMUC = new LinkedList<Pattern>();
		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
		Property propOMUC = new Property(MutationOperator.COD, "utils/BooleanOps", "or", 0, 0, mceOMUC, mcneOMUC);
		
		List<Pattern> mceXor = new LinkedList<Pattern>();
		List<Pattern> mcneXor = new LinkedList<Pattern>();
		mceXor.add(Pattern.compile("boolean allFalse = \\!auxA \\&\\& auxB; //mutGenLimit 1"));
		mceXor.add(Pattern.compile("boolean allFalse = auxA \\&\\& \\!auxB; //mutGenLimit 1"));
		mceXor.add(Pattern.compile("boolean allTrue = \\!auxA \\|\\| \\!auxB; //mutGenLimit 0"));
		mceXor.add(Pattern.compile("boolean allTrue = \\!\\(auxA \\|\\| \\!auxB\\); //mutGenLimit 0"));
		mceXor.add(Pattern.compile("boolean allTrue = \\!\\(\\!auxA \\|\\| auxB\\); //mutGenLimit 0"));
		mceXor.add(Pattern.compile("return allTrue \\|\\| allFalse; //mutGenLimit 0"));
		mcneXor.add(Pattern.compile(".+//mutGenLimit -[0..9]+"));
		Property propXor = new Property(MutationOperator.COD, "utils/BooleanOps", "xor", 6, 6, mceXor, mcneXor);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfXor;
		
		//MUTANTS GENERATION
		mfOMUC = TestingTools.generateMutants(propOMUC);
		mfXor = TestingTools.generateMutants(propXor);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMUC, mfOMUC},
				{propXor, mfXor}
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
