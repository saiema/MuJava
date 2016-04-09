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
public class PCITests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public PCITests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/*
		 * 	PCI_3 pci3 = new PCI_3();
		 *	PCI_1 pci1 = pci3; //mutGenLimit 1
		 *  PCI_2 pci2 = new PCI_2();
		 *	PCI_2 pci22 = pci2; //mutGenLimit 1
		 *	someMethod(pci22); //mutGenLimit 1
	     *	boolean someBool = someOtherMethod(pci3) && someOtherMethod(pci22) && someOtherMethod(pci1); //mutGenLimit 1
		 */
		List<Pattern> mcePCI_1 = new LinkedList<Pattern>();
		mcePCI_1.add(Pattern.compile("(.+\\.)?PCI\\_1 pci1 = \\((.+\\.)?Object\\) pci3; //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("(.+\\.)?PCI\\_1 pci1 = \\((.+\\.)?PCI\\_2\\) pci3; //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("(.+\\.)?PCI\\_1 pci1 = \\((.+\\.)?PCI\\_1\\) pci3; //mutGenLimit 0"));
		
		mcePCI_1.add(Pattern.compile("(.+\\.)?PCI\\_2 pci22 = \\((.+\\.)?Object\\) pci2; //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("(.+\\.)?PCI\\_2 pci22 = \\((.+\\.)?PCI\\_3\\) pci2; //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("(.+\\.)?PCI\\_2 pci22 = \\((.+\\.)?PCI\\_1\\) pci2; //mutGenLimit 0"));
		
		mcePCI_1.add(Pattern.compile("someMethod\\( \\((.+\\.)?Object\\) pci22 \\); //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("someMethod\\( \\((.+\\.)?PCI\\_3\\) pci22 \\); //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("someMethod\\( \\((.+\\.)?PCI\\_1\\) pci22 \\); //mutGenLimit 0"));
		
		mcePCI_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( \\((.+\\.)?Object\\) pci3 \\) \\&\\& someOtherMethod\\( pci22 \\) \\&\\& someOtherMethod\\( pci1 \\); //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( \\((.+\\.)?PCI\\_2\\) pci3 \\) \\&\\& someOtherMethod\\( pci22 \\) \\&\\& someOtherMethod\\( pci1 \\); //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( \\((.+\\.)?PCI\\_1\\) pci3 \\) \\&\\& someOtherMethod\\( pci22 \\) \\&\\& someOtherMethod\\( pci1 \\); //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( pci3 \\) \\&\\& someOtherMethod\\( \\((.+\\.)?Object\\) pci22 \\) \\&\\& someOtherMethod\\( pci1 \\); //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( pci3 \\) \\&\\& someOtherMethod\\( \\((.+\\.)?PCI\\_3\\) pci22 \\) \\&\\& someOtherMethod\\( pci1 \\); //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( pci3 \\) \\&\\& someOtherMethod\\( \\((.+\\.)?PCI\\_1\\) pci22 \\) \\&\\& someOtherMethod\\( pci1 \\); //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( pci3 \\) \\&\\& someOtherMethod\\( pci22 \\) \\&\\& someOtherMethod\\( \\((.+\\.)?Object\\) pci1 \\); //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( pci3 \\) \\&\\& someOtherMethod\\( pci22 \\) \\&\\& someOtherMethod\\( \\((.+\\.)?PCI\\_3\\) pci1 \\); //mutGenLimit 0"));
		mcePCI_1.add(Pattern.compile("boolean someBool = someOtherMethod\\( pci3 \\) \\&\\& someOtherMethod\\( pci22 \\) \\&\\& someOtherMethod\\( \\((.+\\.)?PCI\\_2\\) pci1 \\); //mutGenLimit 0"));
		
		Property propPCI_1 = new Property(MutationOperator.PCI,
										"test/PCI_3",
										"radiatedMethod",
										18,
										11,
										mcePCI_1,
										TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfPCI_1;
		
		//MUTANTS GENERATION
		mfPCI_1 = TestingTools.generateMutants(propPCI_1);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propPCI_1, mfPCI_1}
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
