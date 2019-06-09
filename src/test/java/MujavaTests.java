package test.java;

import mujava.api.Configuration;
import mujava.api.MutantsInformationHolder;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.java.utils.TestingTools;

@RunWith(Suite.class)
@SuiteClasses({	AMCTests.class,		AODSTests.class, 		AODUTests.class,			AOISTests.class,
				AOIUTests.class,	AORBTests.class,		AORSTests.class,			AORUTests.class,
				ASRSTests.class,	CODTests.class,			COITests.class,				CORTests.class,
				EAMTests.class,		EMMTests.class,			EOATests.class,				EOA_DUMBTests.class,
				CRCRTests.class,	ICTests.class,			LVRTests.class,				EOA_STRICTTests.class,
				EOCTests.class,		IHDTests.class,			IHITests.class,				IODTests.class,
				IOPTests.class,		IPCTests.class,			ISD_DUMBTests.class,		ISD_SMARTTests.class,
				ISI_SMARTTests.class,						JDCTests.class,				JIDTests.class,
				JSDTests.class,		JSITests.class,			JTDTests.class,				JTI_DUMBTests.class,
				JTI_SMARTTests.class,						LODTests.class,				LOITests.class,
				LORTests.class,		OANTests.class,			OMRTests.class,				PCCTests.class,
				PCDTests.class,		PMDTests.class,			PPDTests.class,				PRVOTests.class,
				PRVO_no_method_allowed_Options_Tests.class,	PRVO_replaceAllByOne_left_Options_Tests.class,
				PRVO_replaceAllByOne_right_Options_Tests.class,							PRVO_TwoByOne_OneByTwo_Disabled_Options_Tests.class,
				RORTests.class,		SORTests.class,			GenericsParsingTests.class,	GenericsWritingTests.class,
				PCITests.class,		PNCTests.class,			ObtainingMutationsTest.class,
				MergedMutationsTests.class,					MultiMutationsTests.class,	InnerClassTests.class,
				LiteralNumbersVariationsTests.class})
public class MujavaTests {
	
	@BeforeClass
	public static void setVerboseForTestingTools() {
		Configuration.clear();
		TestingTools.setVerbose(false);
		MutantsInformationHolder.setVerbose(false);
	}
	
}
