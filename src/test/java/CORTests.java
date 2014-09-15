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
public class CORTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public CORTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> mceOMUC = new LinkedList<Pattern>();
		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
		Property propOMUC = new Property(Mutant.COR, "utils/BooleanOps", "or", 0, 0, mceOMUC, mcneOMUC);
		
		/*
			 	boolean auxA = a; //mutGenLimit 0
				boolean auxB = b; //mutGenLimit 1
				if (!auxA || !auxB) {
					return false; //mutGenLimit 1
				} //mutGenLimit 1
		 */
		List<Pattern> mceAnd = new LinkedList<Pattern>();
		List<Pattern> mcneAnd = new LinkedList<Pattern>();
		mceAnd.add(Pattern.compile("boolean auxA = a; //mutGenLimit 0"));
		mceAnd.add(Pattern.compile("boolean auxB = b; //mutGenLimit 1"));
		mceAnd.add(Pattern.compile("return false; //mutGenLimit 1"));
		mceAnd.add(Pattern.compile("if \\(\\!auxA \\&\\& \\!auxB\\) \\{.+\\} //mutGenLimit 0"));
		mceAnd.add(Pattern.compile("if \\(\\!auxA \\& \\!auxB\\) \\{.+\\} //mutGenLimit 0"));
		mceAnd.add(Pattern.compile("if \\(\\!auxA \\| \\!auxB\\) \\{.+\\} //mutGenLimit 0"));
		mceAnd.add(Pattern.compile("if \\(\\!auxA \\^ \\!auxB\\) \\{.+\\} //mutGenLimit 0"));
		Property propAnd = new Property(Mutant.COR, "utils/BooleanOps", "and", 4, 4, mceAnd, mcneAnd);
		
		/*
		 		boolean isXnor = !(!auxA || !auxB) || (!auxA && !auxB); //mutGenLimit 1
		 */
		
		/*
		 	boolean isXnor = !(!auxA & !auxB) || !auxA && !auxB; //mutGenLimit 0
	        boolean isXnor = !(!auxA && !auxB) || !auxA && !auxB; //mutGenLimit 0
	        boolean isXnor = !(!auxA ^ !auxB) || !auxA && !auxB; //mutGenLimit 0
	        boolean isXnor = !(!auxA | !auxB) || !auxA && !auxB; //mutGenLimit 0
	        boolean isXnor = !(!auxA || !auxB) & (!auxA && !auxB); //mutGenLimit 0
	        boolean isXnor = !(!auxA || !auxB) && (!auxA && !auxB); //mutGenLimit 0
	        boolean isXnor = !(!auxA || !auxB) ^ (!auxA && !auxB); //mutGenLimit 0
	        boolean isXnor = !(!auxA || !auxB) | (!auxA && !auxB); //mutGenLimit 0
	        boolean isXnor = !(!auxA || !auxB) || !auxA & !auxB; //mutGenLimit 0
	        boolean isXnor = !(!auxA || !auxB) || !auxA ^ !auxB; //mutGenLimit 0
	        boolean isXnor = !(!auxA || !auxB) || !auxA | !auxB; //mutGenLimit 0
	        boolean isXnor = !(!auxA || !auxB) || !auxA || !auxB; //mutGenLimit 0
		 */
		List<Pattern> mceXnor = new LinkedList<Pattern>();
		List<Pattern> mcneXnor = new LinkedList<Pattern>();
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\&\\& \\!auxB\\) \\|\\| \\!auxA \\&\\& \\!auxB; //mutGenLimit 0"));
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\& \\!auxB\\) \\|\\| \\!auxA \\&\\& \\!auxB; //mutGenLimit 0"));
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\| \\!auxB\\) \\|\\| \\!auxA \\&\\& \\!auxB; //mutGenLimit 0"));
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\^ \\!auxB\\) \\|\\| \\!auxA \\&\\& \\!auxB; //mutGenLimit 0"));
		
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\|\\| \\!auxB\\) \\&\\& \\(\\!auxA \\&\\& \\!auxB\\); //mutGenLimit 0"));
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\|\\| \\!auxB\\) \\& \\(\\!auxA \\&\\& \\!auxB\\); //mutGenLimit 0"));
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\|\\| \\!auxB\\) \\| \\(\\!auxA \\&\\& \\!auxB\\); //mutGenLimit 0"));
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\|\\| \\!auxB\\) \\^ \\(\\!auxA \\&\\& \\!auxB\\); //mutGenLimit 0"));
		
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\|\\| \\!auxB\\) \\|\\| \\!auxA \\|\\| \\!auxB; //mutGenLimit 0"));
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\|\\| \\!auxB\\) \\|\\| \\!auxA \\| \\!auxB; //mutGenLimit 0"));
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\|\\| \\!auxB\\) \\|\\| \\!auxA \\& \\!auxB; //mutGenLimit 0"));
		mceXnor.add(Pattern.compile("boolean isXnor = \\!\\(\\!auxA \\|\\| \\!auxB\\) \\|\\| \\!auxA \\^ \\!auxB; //mutGenLimit 0"));
		Property propXnor = new Property(Mutant.COR, "utils/BooleanOps", "xnor", 12, 12, mceXnor, mcneXnor);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfAnd;
		List<MutantInfo> mfXnor;
		
		//MUTANTS GENERATION
		mfOMUC = TestingTools.generateMutants(propOMUC);
		mfAnd = TestingTools.generateMutants(propAnd);
		mfXnor = TestingTools.generateMutants(propXnor);
		
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMUC, mfOMUC},
				{propAnd, mfAnd},
				{propXnor, mfXnor}
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
