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
public class AMCTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public AMCTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> mceOMUC = new LinkedList<Pattern>();
		List<Pattern> mcneOMUC = new LinkedList<Pattern>();
		Property propOMUC = new Property(Mutant.AMC, "utils/IntWrapper", "toString", 0, 0, mceOMUC, mcneOMUC);
		
		List<Pattern> mceConstructor = new LinkedList<Pattern>();
		List<Pattern> mcneConstructor = new LinkedList<Pattern>();
		mceConstructor.add(Pattern.compile("protected (.+\\.)?Integer val; //mutGenLimit 1"));
		mceConstructor.add(Pattern.compile("private (.+\\.)?Integer val; //mutGenLimit 1"));
		mceConstructor.add(Pattern.compile("(?!(private|protected|public))[ \t]*([^ \t]+\\.)?Integer val; //mutGenLimit 1"));
		Property propConstructor = new Property(Mutant.AMC, "utils/IntWrapper", Property.MUTATE_FIELDS, 3, 3, mceConstructor, mcneConstructor);
		
		List<Pattern> mceAdd = new LinkedList<Pattern>();
		List<Pattern> mcneAdd = new LinkedList<Pattern>();
		mceAdd.add(Pattern.compile("private (.+\\.)?IntWrapper add.+\\} //mutGenLimit 0"));
		mceAdd.add(Pattern.compile("protected (.+\\.)?IntWrapper add.+\\} //mutGenLimit 0"));
		mceAdd.add(Pattern.compile("(?!(private|protected|public))[ \t]*([^ \t]+\\.)?IntWrapper add.+\\} //mutGenLimit 0"));
		Property propAdd = new Property(Mutant.AMC, "utils/IntWrapper", "add", 3, 3, mceAdd, mcneAdd);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfOMUC;
		List<MutantInfo> mfConstructor;
		List<MutantInfo> mfAdd;
		
		
		//MUTANTS GENERATION
		mfOMUC = TestingTools.generateMutants(propOMUC);
		mfConstructor = TestingTools.generateMutants(propConstructor);
		mfAdd = TestingTools.generateMutants(propAdd);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propOMUC, mfOMUC},
				{propConstructor, mfConstructor},
				{propAdd, mfAdd}
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
