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
public class JTI_SMARTTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public JTI_SMARTTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/*
		 * Original code
		 * 
		 * 	field1 = field4; //mutGenLimit 1;
		 *	int var1 = field2; //mutGenLimit 1;
		 *	int var2 = field1 + field2 + field3 + field4 + field5 + field6; //mutGenLimit 3
		 *
		 */
		List<Pattern> mcJTI_DUMB_1 = new LinkedList<Pattern>();
		mcJTI_DUMB_1.add(Pattern.compile("this\\.field1 = field4; //mutGenLimit 0"));
		mcJTI_DUMB_1.add(Pattern.compile("int var1 = this\\.field2; //mutGenLimit 0"));
		mcJTI_DUMB_1.add(Pattern.compile("int var2 = this\\.field1 \\+ field2 \\+ field3 \\+ field4 \\+ field5 \\+ field6; //mutGenLimit 2"));
		mcJTI_DUMB_1.add(Pattern.compile("int var2 = field1 \\+ this\\.field2 \\+ field3 \\+ field4 \\+ field5 \\+ field6; //mutGenLimit 2"));
		mcJTI_DUMB_1.add(Pattern.compile("int var2 = field1 \\+ field2 \\+ this\\.field3 \\+ field4 \\+ field5 \\+ field6; //mutGenLimit 2"));
		mcJTI_DUMB_1.add(Pattern.compile("int var2 = field1 \\+ field2 \\+ field3 \\+ field4 \\+ this\\.field5 \\+ field6; //mutGenLimit 2"));
		mcJTI_DUMB_1.add(Pattern.compile("int var2 = field1 \\+ field2 \\+ field3 \\+ field4 \\+ field5 \\+ this\\.field6; //mutGenLimit 2"));
		Property propJTIDUMB_1 = new Property(Mutant.JTI_SMART,
											"test/JTI_1",
											"radiatedMethod",
											7,
											7,
											mcJTI_DUMB_1,
											TestingTools.NO_PATTERN_EXPECTED);
		
		/*
		 * Original code
		 * 
		 * 	radiatedMethod(field1, field2); //mutGenLimit 1
		 *	radiatedMethod(field1 + field2, field3 + field4); //mutGenLimit 2
		 *	int var1 = field1 + radiatedMethod(field2 + radiatedMethod(field3, field4), radiatedMethod(field5, field6)); //mutGenLimit 3
		 *	return var1 + radiatedMethod(var1 + field2 + field4, field3); //mutGenLimit 100
		 *
		 */
		List<Pattern> mcJTI_DUMB_2 = new LinkedList<Pattern>();
		mcJTI_DUMB_2.add(Pattern.compile("radiatedMethod\\( this\\.field1, field2 \\); //mutGenLimit 0"));
		mcJTI_DUMB_2.add(Pattern.compile("radiatedMethod\\( field1, this\\.field2 \\); //mutGenLimit 0"));
		mcJTI_DUMB_2.add(Pattern.compile("radiatedMethod\\( this\\.field1 \\+ field2, field3 \\+ field4 \\); //mutGenLimit 1"));
		mcJTI_DUMB_2.add(Pattern.compile("radiatedMethod\\( field1 \\+ this\\.field2, field3 \\+ field4 \\); //mutGenLimit 1"));
		mcJTI_DUMB_2.add(Pattern.compile("radiatedMethod\\( field1 \\+ field2, this\\.field3 \\+ field4 \\); //mutGenLimit 1"));
		mcJTI_DUMB_2.add(Pattern.compile("int var1 = this\\.field1 \\+ radiatedMethod\\( field2 \\+ radiatedMethod\\( field3, field4 \\), radiatedMethod\\( field5, field6 \\) \\); //mutGenLimit 2"));
		mcJTI_DUMB_2.add(Pattern.compile("int var1 = field1 \\+ radiatedMethod\\( this\\.field2 \\+ radiatedMethod\\( field3, field4 \\), radiatedMethod\\( field5, field6 \\) \\); //mutGenLimit 2"));
		mcJTI_DUMB_2.add(Pattern.compile("int var1 = field1 \\+ radiatedMethod\\( field2 \\+ radiatedMethod\\( this\\.field3, field4 \\), radiatedMethod\\( field5, field6 \\) \\); //mutGenLimit 2"));
		mcJTI_DUMB_2.add(Pattern.compile("int var1 = field1 \\+ radiatedMethod\\( field2 \\+ radiatedMethod\\( field3, field4 \\), radiatedMethod\\( this\\.field5, field6 \\) \\); //mutGenLimit 2"));
		mcJTI_DUMB_2.add(Pattern.compile("int var1 = field1 \\+ radiatedMethod\\( field2 \\+ radiatedMethod\\( field3, field4 \\), radiatedMethod\\( field5, this\\.field6 \\) \\); //mutGenLimit 2"));
		mcJTI_DUMB_2.add(Pattern.compile("return var1 \\+ radiatedMethod\\( var1 \\+ this\\.field2 \\+ field4, field3 \\); //mutGenLimit 99"));
		mcJTI_DUMB_2.add(Pattern.compile("return var1 \\+ radiatedMethod\\( var1 \\+ field2 \\+ field4, this\\.field3 \\); //mutGenLimit 99"));
		Property propJTIDUMB_2 = new Property(Mutant.JTI_SMART,
											"test/JTI_2",
											"radiatedMethod",
											12,
											12,
											mcJTI_DUMB_2,
											TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfJTI_DUMB_1;
		List<MutantInfo> mfJTI_DUMB_2;
		
		//MUTANTS GENERATION
		mfJTI_DUMB_1 = TestingTools.generateMutants(propJTIDUMB_1);
		mfJTI_DUMB_2 = TestingTools.generateMutants(propJTIDUMB_2);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propJTIDUMB_1, mfJTI_DUMB_1},
				{propJTIDUMB_2, mfJTI_DUMB_2}
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
