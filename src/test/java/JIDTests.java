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
public class JIDTests {
	
	private Property prop;
	private List<MutantInfo> mutantsInfo;
	
	public JIDTests(Property prop, List<MutantInfo> mutantsInfo) {
		this.prop = prop;
		this.mutantsInfo = mutantsInfo;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		/*
		 * Original Code
		 * 	
		 * 	private int field1; //mutGenLimit 1
		 *	private int field2 = 0; //mutGenLimit 1
		 *	protected int field3 = 3 + field2; //mutGenLimit 1
		 *	public int field4 = (new LinkedList<Integer>()).size() + field3*field2+1; //mutGenLimit 1
		 *	public final int field5 = 3; //mutGenLimit 1
		 */
		List<Pattern> mceJID_1 = new LinkedList<Pattern>();
		mceJID_1.add(Pattern.compile("private int field2; //mutGenLimit 0"));
		mceJID_1.add(Pattern.compile("protected int field3; //mutGenLimit 0"));
		mceJID_1.add(Pattern.compile("public int field4; //mutGenLimit 0"));
		List<Pattern> mcneJID_1 = new LinkedList<Pattern>();
		mcneJID_1.add(Pattern.compile("private int field1; //mutGenLimit 0"));
		mcneJID_1.add(Pattern.compile("public final int field5; //mutGenLimit 0"));
		Property propJID_1 = new Property(MutationOperator.JID,
								"test/JID_1",
								Property.MUTATE_FIELDS,
								3,
								3,
								mceJID_1,
								mcneJID_1);
		
		//MUTANTS FOLDERS
		List<MutantInfo> mfJID_1;
		
		//MUTANTS GENERATION
		mfJID_1 = TestingTools.generateMutants(propJID_1);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				{propJID_1, mfJID_1}
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
