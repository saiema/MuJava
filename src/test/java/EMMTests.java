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
public class EMMTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public EMMTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		List<Pattern> dllce = new LinkedList<Pattern>();
		dllce.add(Pattern.compile("newNode\\.setData\\( data \\)\\; \\/\\/mutGenLimit 1"));
		dllce.add(Pattern.compile("newNode\\.setNext\\( null \\)\\; \\/\\/mutGenLimit 1"));
		dllce.add(Pattern.compile("newNode\\.setPrev\\( null \\)\\; \\/\\/mutGenLimit 0"));
		dllce.add(Pattern.compile("newNode\\.setData\\( null \\)\\; \\/\\/mutGenLimit 0"));
		dllce.add(Pattern.compile("newNode\\.setPrev\\( end \\)\\; \\/\\/mutGenLimit 1"));
		dllce.add(Pattern.compile("newNode\\.setNext\\( end \\)\\; \\/\\/mutGenLimit 0"));
		dllce.add(Pattern.compile("newNode\\.setData\\( end \\)\\; \\/\\/mutGenLimit 0"));
		dllce.add(Pattern.compile("this\\.end\\.setNext\\( newNode \\)\\; \\/\\/mutGenLimit 1"));
		dllce.add(Pattern.compile("this\\.end\\.setPrev\\( newNode \\)\\; \\/\\/mutGenLimit 0"));
		dllce.add(Pattern.compile("this\\.end\\.setData\\( newNode \\)\\; \\/\\/mutGenLimit 0"));
		List<Pattern> dllcne = new LinkedList<Pattern>();
		Property propDll = new Property(Mutant.EMM,
										"objects/DoubleLinkedList",
										"addLast",
										6,
										6,
										dllce,
										dllcne);
		
		
		List<Pattern> dllnce = new LinkedList<Pattern>();
		dllnce.add(Pattern.compile("setNext\\( next \\)\\; \\/\\/mutGenLimit 1"));
		dllnce.add(Pattern.compile("setPrev\\( next \\)\\; \\/\\/mutGenLimit 0"));
		dllnce.add(Pattern.compile("setData\\( next \\)\\; \\/\\/mutGenLimit 0"));
		dllnce.add(Pattern.compile("setPrev\\( prev \\)\\; \\/\\/mutGenLimit 1"));
		dllnce.add(Pattern.compile("setNext\\( prev \\)\\; \\/\\/mutGenLimit 0"));
		dllnce.add(Pattern.compile("setData\\( prev \\)\\; \\/\\/mutGenLimit 0"));
		dllnce.add(Pattern.compile("setData\\( data \\)\\; \\/\\/mutGenLimit 1"));
		List<Pattern> dllncne = new LinkedList<Pattern>();
		Property propDlln = new Property(Mutant.EMM,
										"objects/DoubleLinkedListNode",
										"DoubleLinkedListNode",
										4,
										4,
										dllnce,
										dllncne);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfDll;
		List<MutantInfo> mfDlln;
		
		//MUTANTS GENERATION
		mfDll = TestingTools.generateMutants(propDll);
		mfDlln = TestingTools.generateMutants(propDlln);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propDll, mfDll},
				{propDlln, mfDlln}
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
