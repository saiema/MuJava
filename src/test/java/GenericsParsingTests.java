package test.java;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import mujava.api.Mutant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.java.utils.Property;
import test.java.utils.TestingTools;

@RunWith(Parameterized.class)
public class GenericsParsingTests {
	
	private Property prop;
	
	public GenericsParsingTests(Property prop) {
		this.prop = prop;
	}
	
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		//TESTS DEFINITIONS
		Property propCA_1 = new Property(Mutant.AMC,
										"generics/CA_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_2 = new Property(Mutant.AMC,
										"generics/CA_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_3 = new Property(Mutant.AMC,
										"generics/CA_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_4 = new Property(Mutant.AMC,
										"generics/CA_4",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_5 = new Property(Mutant.AMC,
										"generics/CA_5",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_6 = new Property(Mutant.AMC,
										"generics/CA_6",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_7 = new Property(Mutant.AMC,
										"generics/CA_7",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_8 = new Property(Mutant.AMC,
										"generics/CA_8",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_9 = new Property(Mutant.AMC,
										"generics/CA_9",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_10 = new Property(Mutant.AMC,
										"generics/CA_10",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_11 = new Property(Mutant.AMC,
										"generics/CA_11",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_12 = new Property(Mutant.AMC,
										"generics/CA_12",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_IC_1 = new Property(Mutant.AMC,
										"generics/CA_IC_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_IC_2 = new Property(Mutant.AMC,
										"generics/CA_IC_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_IC_3 = new Property(Mutant.AMC,
										"generics/CA_IC_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_IC_4 = new Property(Mutant.AMC,
										"generics/CA_IC_4",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCF_1 = new Property(Mutant.AMC,
										"generics/CF_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCF_2 = new Property(Mutant.AMC,
										"generics/CF_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCF_3 = new Property(Mutant.AMC,
										"generics/CF_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propLV_1 = new Property(Mutant.AMC,
										"generics/LV_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propLV_2 = new Property(Mutant.AMC,
										"generics/LV_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propLV_3 = new Property(Mutant.AMC,
										"generics/LV_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propLV_4 = new Property(Mutant.AMC,
										"generics/LV_4",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propLV_5 = new Property(Mutant.AMC,
										"generics/LV_5",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propMS_1 = new Property(Mutant.AMC,
										"generics/MS_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propMS_2 = new Property(Mutant.AMC,
										"generics/MS_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propMS_3 = new Property(Mutant.AMC,
										"generics/MS_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propMS_4 = new Property(Mutant.AMC,
										"generics/MS_4",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWLV_1 = new Property(Mutant.AMC,
										"generics/WLV_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWLV_2 = new Property(Mutant.AMC,
										"generics/WLV_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWLV_3 = new Property(Mutant.AMC,
										"generics/WLV_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propFLV_1 = new Property(Mutant.AMC,
										"generics/FLV_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propFLV_2 = new Property(Mutant.AMC,
										"generics/FLV_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propFLV_3 = new Property(Mutant.AMC,
										"generics/FLV_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWC_1 = new Property(Mutant.AMC,
										"generics/WC_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWC_2 = new Property(Mutant.AMC,
										"generics/WC_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWC_3 = new Property(Mutant.AMC,
										"generics/WC_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propFC_1 = new Property(Mutant.AMC,
										"generics/FC_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propFC_2 = new Property(Mutant.AMC,
										"generics/FC_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
//		Property propFC_3 = new Property(Mutant.AMC,
//										"generics/FC_3",
//										"defMethod",
//										TestingTools.NO_MUTANTS_EXPECTED,
//										TestingTools.NO_MUTANTS_EXPECTED,
//										TestingTools.NO_PATTERN_EXPECTED,
//										TestingTools.NO_PATTERN_EXPECTED);
//		
//		Property propFC_4 = new Property(Mutant.AMC,
//										"generics/FC_4",
//										"defMethod",
//										TestingTools.NO_MUTANTS_EXPECTED,
//										TestingTools.NO_MUTANTS_EXPECTED,
//										TestingTools.NO_PATTERN_EXPECTED,
//										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propSW_1 = new Property(Mutant.AMC,
										"generics/SW_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propSW_2 = new Property(Mutant.AMC,
										"generics/SW_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propSW_3 = new Property(Mutant.AMC,
										"generics/SW_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propIO_1 = new Property(Mutant.AMC,
										"generics/IO_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCAS_1 = new Property(Mutant.AMC,
										"generics/CAS_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCAS_2 = new Property(Mutant.AMC,
										"generics/CAS_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propR_1 = new Property(Mutant.AMC,
										"generics/R_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propR_2 = new Property(Mutant.AMC,
										"generics/R_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propR_3 = new Property(Mutant.AMC,
										"generics/R_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				//Class atribute++++++++++++
				{propCA_1},
				{propCA_2},
				{propCA_3},
				{propCA_4},
				{propCA_5},
				{propCA_6},
				{propCA_7},
				{propCA_8},
				{propCA_9},
				{propCA_10},
				{propCA_11},
				{propCA_12},
				//Class atribute------------
				
				//Internal Class atribute++++++++++++
				{propCA_IC_1},
				{propCA_IC_2},
				{propCA_IC_3},
				{propCA_IC_4},
				//Internal Class atribute------------
				
				//Class Fields++++++++++++
				{propCF_1},
				{propCF_2},
				{propCF_3},
				//Class Fields------------
				
				//Local Variable++++++++++++
				{propLV_1},
				{propLV_2},
				{propLV_3},
				{propLV_4},
				{propLV_5},
				//Local Variable------------
				
				//Method signature++++++++++++
				{propMS_1},
				{propMS_2},
				{propMS_3},
				{propMS_4},
				//Method signature------------
				
				//While local variables++++++++++++
				{propWLV_1},
				{propWLV_2},
				{propWLV_3},
				//While local variables------------
				
				//For local variables++++++++++++
				{propFLV_1},
				{propFLV_2},
				{propFLV_3},
				//For local variables------------
				
				//While condition++++++++++++
				{propWC_1},
				{propWC_2},
				{propWC_3},
				//While condition------------
				
				//For condition++++++++++++
				{propFC_1},
				{propFC_2}, //this also check generics on a method call argument
				//For condition------------
				
				//For collection++++++++++++ (There's no support for the sintax "for (<elem> : <collection>) <body>")
//				{propFC_3},
//				{propFC_4},
				//For collection------------
				
				//Switch++++++++++++
				{propSW_1},
				{propSW_2},
				{propSW_3},
				//Switch------------
				
				//Instance of++++++++++++
				//Since instanceof doesn't accept generics (except for ? wildcard) only one test is used
				{propIO_1},
				//Instance of------------
				
				//Condicional assignment++++++++++++
				{propCAS_1},
				{propCAS_2},
				//Condicional assignment------------
				
				//Return++++++++++++
				{propR_1},
				{propR_2},
				{propR_3},
				//Return------------
				
		});
	}
	
	@Test
	public void parseTest() {
		assertTrue(TestingTools.genTest(prop));
	}

}
