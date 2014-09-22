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
public class IPCTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public IPCTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> ceIPC_1 = new LinkedList<Pattern>();
		ceIPC_1.add(Pattern.compile("public IPC\\_1\\(\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*\\}"));
		ceIPC_1.add(Pattern.compile("public IPC\\_1\\(([ \t\n\f\r])*int param1\\,([ \t\n\f\r])*boolean param2([ \t\n\f\r])*\\)([ \t\n\f\r])*\\{ //mutGenLimit 0([ \t\n\f\r])*\\}"));
		Property propIPC_1 = new Property(Mutant.IPC,
										"test/IPC_1",
										"IPC_1",
										2,
										0,
										ceIPC_1,
										TestingTools.NO_PATTERN_EXPECTED);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfIPC_1;
		
		//MUTANTS GENERATION
		mfIPC_1 = TestingTools.generateMutants(propIPC_1);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propIPC_1, mfIPC_1},
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
