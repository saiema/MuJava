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
public class PNCTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public PNCTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/*
		 * PCI_3 pci3 = new PCI_3(); //mutGenLimit 1
		 * PCI_2 pci2 = new PCI_2(); //mutGenLimit 1
		 * someMethod(new PCI_1()); //mutGenLimit 1
		 * boolean someBool = someOtherMethod(new PCI_1()) && someOtherMethod(new PCI_2()) && someOtherMethod(new PCI_3()); //mutGenLimit 1	
		 * 
		 */
		/*
		 * Mutants
		 * 
		 * 
		 * someMethod( new test.PCI_2() ); //mutGenLimit 0
		 * someMethod( new test.PCI_3() ); //mutGenLimit 0
		 * boolean someBool = someOtherMethod( new test.PCI_2() ) && someOtherMethod( new test.PCI_2() ) && someOtherMethod( new test.PCI_3() ); //mutGenLimit 0
		 * boolean someBool = someOtherMethod( new test.PCI_3() ) && someOtherMethod( new test.PCI_2() ) && someOtherMethod( new test.PCI_3() ); //mutGenLimit 0
		 * boolean someBool = someOtherMethod( new test.PCI_1() ) && someOtherMethod( new test.PCI_3() ) && someOtherMethod( new test.PCI_3() ); //mutGenLimit 0
		 * 
		 */
		List<Pattern> mcePNC_1 = new LinkedList<Pattern>();
		mcePNC_1.add(Pattern.compile("(.+\\.)?PCI\\_2 pci2 = new (.+\\.)?PCI\\_3\\(\\); //mutGenLimit 0"));
		
		mcePNC_1.add(Pattern.compile("someMethod\\( new (.+\\.)?PCI\\_2\\(\\) \\); //mutGenLimit 0"));
		mcePNC_1.add(Pattern.compile("someMethod\\( new (.+\\.)?PCI\\_3\\(\\) \\); //mutGenLimit 0"));
		
		mcePNC_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( new (.+\\.)?PCI\\_2\\(\\) \\) \\&\\& someOtherMethod\\( new (.+\\.)?PCI\\_2\\(\\) \\) \\&\\& someOtherMethod\\( new (.+\\.)?PCI\\_3\\(\\) \\); //mutGenLimit 0"));
		mcePNC_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( new (.+\\.)?PCI\\_3\\(\\) \\) \\&\\& someOtherMethod\\( new (.+\\.)?PCI\\_2\\(\\) \\) \\&\\& someOtherMethod\\( new (.+\\.)?PCI\\_3\\(\\) \\); //mutGenLimit 0"));
		
		mcePNC_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( new (.+\\.)?PCI\\_1\\(\\) \\) \\&\\& someOtherMethod\\( new (.+\\.)?PCI\\_3\\(\\) \\) \\&\\& someOtherMethod\\( new (.+\\.)?PCI\\_3\\(\\) \\); //mutGenLimit 0"));
		
		Property propPNC_1 = new Property(MutationOperator.PNC,
										"test/PCI_3",
										"radiatedMethod2",
										6,
										6,
										mcePNC_1,
										TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfPNC_1;
		
		//MUTANTS GENERATION
		mfPNC_1 = TestingTools.generateMutants(propPNC_1);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propPNC_1, mfPNC_1}
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
