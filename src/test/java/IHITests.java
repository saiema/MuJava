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
public class IHITests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public IHITests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> ihiTestA2_1ce = new LinkedList<Pattern>();
		ihiTestA2_1ce.add(Pattern.compile("public (.+\\.)?String atr3\\;"));
		ihiTestA2_1ce.add(Pattern.compile("public int atr4\\;"));
		ihiTestA2_1ce.add(Pattern.compile("public (.+\\.)?List\\<(.+\\.)?String\\> atr5\\;"));
		ihiTestA2_1ce.add(Pattern.compile("public (.+\\.)?List\\<(.+\\.)?String\\> atr6\\;"));
		List<Pattern> ihiTestA2_1cne = new LinkedList<Pattern>();
		ihiTestA2_1cne.add(Pattern.compile("private (.+\\.)?String atr1\\;"));
		ihiTestA2_1cne.add(Pattern.compile("protected (.+\\.)?String atr2\\;"));
		Property propA2_1 = new Property(MutationOperator.IHI,
										"classMutations/A2_1",
										Property.MUTATE_CLASS,
										4,
										4,
										ihiTestA2_1ce,
										ihiTestA2_1cne);
		
		List<Pattern> ihiTestA2_2ce = new LinkedList<Pattern>();
		ihiTestA2_2ce.add(Pattern.compile("public (.+\\.)?String atr3\\;"));
		ihiTestA2_2ce.add(Pattern.compile("public (.+\\.)?String atr2\\;"));
		ihiTestA2_2ce.add(Pattern.compile("public int atr4\\;"));
		ihiTestA2_2ce.add(Pattern.compile("public (.+\\.)?List\\<(.+\\.)?String\\> atr5\\;"));
		ihiTestA2_2ce.add(Pattern.compile("public (.+\\.)?List\\<(.+\\.)?String\\> atr6\\;"));
		List<Pattern> ihiTestA2_2cne = new LinkedList<Pattern>();
		ihiTestA2_2cne.add(Pattern.compile("private (.+\\.)?String atr1\\;"));
		ihiTestA2_2cne.add(Pattern.compile("protected (.+\\.)?String atr2\\;"));
		Property propA2_2 = new Property(MutationOperator.IHI,
										"classMutations/A2_2",
										Property.MUTATE_CLASS,
										4,
										4,
										ihiTestA2_2ce,
										ihiTestA2_2cne);
		
		List<Pattern> ihiTestA2_3ce = new LinkedList<Pattern>();
		ihiTestA2_3ce.add(Pattern.compile("protected (.+\\.)?Integer atr2\\;"));
		ihiTestA2_3ce.add(Pattern.compile("public (.+\\.)?String atr3\\;"));
		ihiTestA2_3ce.add(Pattern.compile("public int atr4\\;"));
		ihiTestA2_3ce.add(Pattern.compile("public (.+\\.)?List\\<(.+\\.)?String\\> atr5\\;"));
		ihiTestA2_3ce.add(Pattern.compile("public (.+\\.)?List\\<(.+\\.)?String\\> atr6\\;"));
		List<Pattern> ihiTestA2_3cne = new LinkedList<Pattern>();
		ihiTestA2_3cne.add(Pattern.compile("private (.+\\.)?String atr1\\;"));
		ihiTestA2_3cne.add(Pattern.compile("protected (.+\\.)?String atr2\\;"));
		Property propA2_3 = new Property(MutationOperator.IHI,
										"classMutations/A2_3",
										Property.MUTATE_CLASS,
										4,
										4,
										ihiTestA2_3ce,
										ihiTestA2_3cne);
		
		List<Pattern> ihiTestA2_4ce = new LinkedList<Pattern>();
		ihiTestA2_4ce.add(Pattern.compile("public void defMethod\\(\\)"));
		ihiTestA2_4ce.add(Pattern.compile("public (.+\\.)?String atr3\\;"));
		ihiTestA2_4ce.add(Pattern.compile("protected (.+\\.)?String atr2\\;"));
		ihiTestA2_4ce.add(Pattern.compile("public int atr4\\;"));
		ihiTestA2_4ce.add(Pattern.compile("public (.+\\.)?List\\<(.+\\.)?String\\> atr5\\;"));
		ihiTestA2_4ce.add(Pattern.compile("public (.+\\.)?List\\<(.+\\.)?String\\> atr6\\;"));
		List<Pattern> ihiTestA2_4cne = new LinkedList<Pattern>();
		ihiTestA2_4cne.add(Pattern.compile("private (.+\\.)?String atr1\\;"));
		Property propA2_4 = new Property(MutationOperator.IHI,
										"classMutations/A2_4",
										Property.MUTATE_CLASS,
										5,
										5,
										ihiTestA2_4ce,
										ihiTestA2_4cne);
		
		List<Pattern> ihiTestA2_5ce = new LinkedList<Pattern>();
		ihiTestA2_5ce.add(Pattern.compile("public (.+\\.)?String atr3\\;"));
		ihiTestA2_5ce.add(Pattern.compile("public int atr4\\;"));
		ihiTestA2_5ce.add(Pattern.compile("public (.+\\.)?List\\<(.+\\.)?String\\> atr5\\;"));
		ihiTestA2_5ce.add(Pattern.compile("public (.+\\.)?List\\<(.+\\.)?String\\> atr6\\;"));
		ihiTestA2_5ce.add(Pattern.compile("public void defMethod\\( (.+\\.)?String atr2 \\)"));
		List<Pattern> ihiTestA2_5cne = new LinkedList<Pattern>();
		ihiTestA2_5cne.add(Pattern.compile("private (.+\\.)?String atr1\\;"));
		ihiTestA2_5cne.add(Pattern.compile("protected (.+\\.)?String atr2\\;"));
		Property propA2_5 = new Property(MutationOperator.IHI,
										"classMutations/A2_5",
										Property.MUTATE_CLASS,
										4,
										4,
										ihiTestA2_5ce,
										ihiTestA2_5cne);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfA2_1;
		List<MutantInfo> mfA2_2;
		List<MutantInfo> mfA2_3;
		List<MutantInfo> mfA2_4;
		List<MutantInfo> mfA2_5;
		
		//MUTANTS GENERATION
		mfA2_1 = TestingTools.generateMutants(propA2_1);
		mfA2_2 = TestingTools.generateMutants(propA2_2);
		mfA2_3 = TestingTools.generateMutants(propA2_3);
		mfA2_4 = TestingTools.generateMutants(propA2_4);
		mfA2_5 = TestingTools.generateMutants(propA2_5);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propA2_1, mfA2_1},
				{propA2_2, mfA2_2},
				{propA2_3, mfA2_3},
				{propA2_4, mfA2_4},
				{propA2_5, mfA2_5},
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
