package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import mujava.api.Configuration;
import mujava.api.MutationOperator;
import mujava.app.MutantInfo;
import mujava.op.PRVO;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class LiteralNumbersVariationsTests {
	
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	@BeforeClass
	public static void setTestingToolsVerbose() {
		TestingTools.setVerbose(false);
	}
	
	public LiteralNumbersVariationsTests(Property p, List<MutantInfo> mutantsInfo) {
		this.prop = p;
		this.mutantsInfo = mutantsInfo;
	}
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		Configuration.add(PRVO.ENABLE_NUMBER_LITERALS_VARIATIONS, Boolean.TRUE);
		
		
		List<Pattern> mceLNV_1_right = new LinkedList<Pattern>();
		List<Pattern> mcneLNV_1_right = new LinkedList<Pattern>();
		mceLNV_1_right.add(Pattern.compile("super\\.hashCode\\(\\) \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ super\\.hashCode\\(\\)\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber2 \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ localNumber1\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("a \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("b \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ a\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ b\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("1 \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("1\\.0f \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("1l \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("1\\.0d \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("0 \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("0\\.0f \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("0l \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("0\\.0d \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 1\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 1\\.0f\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 1l\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 1\\.0d\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 0\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 0\\.0f\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 0l\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 0\\.0d\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("3\\.2f \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("3\\.200000047683716d \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("3 \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("3\\.0f \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("3\\.0d \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("3l \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 3\\.2f\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 3\\.200000047683716d\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 3\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 3\\.0f\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 3\\.0d\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 3l\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("42 \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("42\\.0f \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("42D \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("42l \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 42\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 42\\.0f\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 42D\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 42l\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("2 \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("2\\.0f \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("2\\.0d \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("2l \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 2\\.0f\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 2\\.0d\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 2l\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("35 \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("35D \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("35\\.0f \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("35l \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 35\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 35\\.0f\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 35D\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 35l\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("2\\.387f \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("2\\.38700008392334d \\+ localNumber2\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 2\\.387f\\); //mutGenLimit 0"));
		mceLNV_1_right.add(Pattern.compile("localNumber1 \\+ 2\\.38700008392334d\\); //mutGenLimit 0"));
		Property propLNV_1_1 = new Property(MutationOperator.PRVOU_REFINED, "literalNumbersVariations/LNV_1", "radiatedMethod", mceLNV_1_right.size(), mceLNV_1_right.size(), mceLNV_1_right, mcneLNV_1_right);
		
		
		List<MutantInfo> mfLNV_1_1;
		
		mfLNV_1_1 = TestingTools.generateMutants(propLNV_1_1);
		
		Configuration.add(PRVO.ENABLE_NUMBER_LITERALS_VARIATIONS, Boolean.FALSE);
		
		return Arrays.asList(new Object[][] {
				{propLNV_1_1, mfLNV_1_1},			//0
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
