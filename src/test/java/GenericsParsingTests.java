package test.java;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import mujava.api.MutationOperator;

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
		Property propCA_1 = new Property(MutationOperator.AMC,
										"generics/CA_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_2 = new Property(MutationOperator.AMC,
										"generics/CA_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_3 = new Property(MutationOperator.AMC,
										"generics/CA_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_4 = new Property(MutationOperator.AMC,
										"generics/CA_4",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_5 = new Property(MutationOperator.AMC,
										"generics/CA_5",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_6 = new Property(MutationOperator.AMC,
										"generics/CA_6",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_7 = new Property(MutationOperator.AMC,
										"generics/CA_7",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_8 = new Property(MutationOperator.AMC,
										"generics/CA_8",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_9 = new Property(MutationOperator.AMC,
										"generics/CA_9",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_10 = new Property(MutationOperator.AMC,
										"generics/CA_10",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_11 = new Property(MutationOperator.AMC,
										"generics/CA_11",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_12 = new Property(MutationOperator.AMC,
										"generics/CA_12",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_IC_1 = new Property(MutationOperator.AMC,
										"generics/CA_IC_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_IC_2 = new Property(MutationOperator.AMC,
										"generics/CA_IC_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_IC_3 = new Property(MutationOperator.AMC,
										"generics/CA_IC_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCA_IC_4 = new Property(MutationOperator.AMC,
										"generics/CA_IC_4",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCF_1 = new Property(MutationOperator.AMC,
										"generics/CF_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCF_2 = new Property(MutationOperator.AMC,
										"generics/CF_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCF_3 = new Property(MutationOperator.AMC,
										"generics/CF_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propLV_1 = new Property(MutationOperator.AMC,
										"generics/LV_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propLV_2 = new Property(MutationOperator.AMC,
										"generics/LV_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propLV_3 = new Property(MutationOperator.AMC,
										"generics/LV_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propLV_4 = new Property(MutationOperator.AMC,
										"generics/LV_4",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propLV_5 = new Property(MutationOperator.AMC,
										"generics/LV_5",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propMS_1 = new Property(MutationOperator.AMC,
										"generics/MS_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propMS_2 = new Property(MutationOperator.AMC,
										"generics/MS_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propMS_3 = new Property(MutationOperator.AMC,
										"generics/MS_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propMS_4 = new Property(MutationOperator.AMC,
										"generics/MS_4",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWLV_1 = new Property(MutationOperator.AMC,
										"generics/WLV_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWLV_2 = new Property(MutationOperator.AMC,
										"generics/WLV_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWLV_3 = new Property(MutationOperator.AMC,
										"generics/WLV_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propFLV_1 = new Property(MutationOperator.AMC,
										"generics/FLV_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propFLV_2 = new Property(MutationOperator.AMC,
										"generics/FLV_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propFLV_3 = new Property(MutationOperator.AMC,
										"generics/FLV_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWC_1 = new Property(MutationOperator.AMC,
										"generics/WC_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWC_2 = new Property(MutationOperator.AMC,
										"generics/WC_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propWC_3 = new Property(MutationOperator.AMC,
										"generics/WC_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propFC_1 = new Property(MutationOperator.AMC,
										"generics/FC_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propFC_2 = new Property(MutationOperator.AMC,
										"generics/FC_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
//		Property propFC_3 = new Property(MutationOperator.AMC,
//										"generics/FC_3",
//										"defMethod",
//										TestingTools.NO_MUTANTS_EXPECTED,
//										TestingTools.NO_MUTANTS_EXPECTED,
//										TestingTools.NO_PATTERN_EXPECTED,
//										TestingTools.NO_PATTERN_EXPECTED);
//		
//		Property propFC_4 = new Property(MutationOperator.AMC,
//										"generics/FC_4",
//										"defMethod",
//										TestingTools.NO_MUTANTS_EXPECTED,
//										TestingTools.NO_MUTANTS_EXPECTED,
//										TestingTools.NO_PATTERN_EXPECTED,
//										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propSW_1 = new Property(MutationOperator.AMC,
										"generics/SW_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propSW_2 = new Property(MutationOperator.AMC,
										"generics/SW_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propSW_3 = new Property(MutationOperator.AMC,
										"generics/SW_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propIO_1 = new Property(MutationOperator.AMC,
										"generics/IO_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCAS_1 = new Property(MutationOperator.AMC,
										"generics/CAS_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propCAS_2 = new Property(MutationOperator.AMC,
										"generics/CAS_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propR_1 = new Property(MutationOperator.AMC,
										"generics/R_1",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propR_2 = new Property(MutationOperator.AMC,
										"generics/R_2",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		Property propR_3 = new Property(MutationOperator.AMC,
										"generics/R_3",
										"defMethod",
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_MUTANTS_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED,
										TestingTools.NO_PATTERN_EXPECTED);
		
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
				//Class atribute++++++++++++
				{propCA_1},										//0
				{propCA_2},										//1
				{propCA_3},										//2
				{propCA_4},										//3
				{propCA_5},										//4
				{propCA_6},										//5
				{propCA_7},										//6
				{propCA_8},										//7
				{propCA_9},										//8
				{propCA_10},									//9
				{propCA_11},									//10
				{propCA_12},									//11
				//Class atribute------------
				
				//Internal Class atribute++++++++++++
				{propCA_IC_1},									//12
				{propCA_IC_2},									//13
				{propCA_IC_3},									//14
				{propCA_IC_4},									//15
				//Internal Class atribute------------
				
				//Class Fields++++++++++++
				{propCF_1},										//16
				{propCF_2},										//17
				{propCF_3},										//18
				//Class Fields------------
				
				//Local Variable++++++++++++
				{propLV_1},										//19
				{propLV_2},										//20
				{propLV_3},										//21
				{propLV_4},										//22
				{propLV_5},										//23
				//Local Variable------------
				
				//Method signature++++++++++++
				{propMS_1},										//24
				{propMS_2},										//25
				{propMS_3},										//26
				{propMS_4},										//27
				//Method signature------------
				
				//While local variables++++++++++++
				{propWLV_1},									//28
				{propWLV_2},									//29
				{propWLV_3},									//30
				//While local variables------------
				
				//For local variables++++++++++++
				{propFLV_1},									//31
				{propFLV_2},									//32
				{propFLV_3},									//33
				//For local variables------------
				
				//While condition++++++++++++
				{propWC_1},										//34
				{propWC_2},										//35
				{propWC_3},										//36
				//While condition------------
				
				//For condition++++++++++++
				{propFC_1},										//37
				{propFC_2},										//38
				//the above test also check generics on a method call argument
				//For condition------------
				
				//For collection++++++++++++ (There's no support for the sintax "for (<elem> : <collection>) <body>")
//				{propFC_3},
//				{propFC_4},
				//For collection------------
				
				//Switch++++++++++++
				{propSW_1},										//39
				{propSW_2},										//40
				{propSW_3},										//41
				//Switch------------
				
				//Instance of++++++++++++
				//Since instanceof doesn't accept generics (except for ? wildcard) only one test is used
				{propIO_1},										//42
				//Instance of------------
				
				//Condicional assignment++++++++++++
				{propCAS_1},									//43
				{propCAS_2},									//44
				//Condicional assignment------------
				
				//Return++++++++++++
				{propR_1},										//45
				{propR_2},										//46
				{propR_3},										//47
				//Return------------
				
		});
	}
	
	@Test
	public void parseTest() {
		assertTrue(TestingTools.genTest(prop));
	}

}
