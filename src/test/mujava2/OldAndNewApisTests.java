package test.mujava2;

import java.util.Arrays;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import mujava.api.MutationOperator;
import mujava2.api.mutator.MutationRequest;
import test.mujava2.utils.TestInput;
import test.mujava2.utils.TestingTools;
import test.mujava2.utils.log.Log;

import static mujava.api.MutationOperator.*;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OldAndNewApisTests {
	
	private MutationRequest req;
	private String dirOldApi;
	private String dirNewApi;
	
	public OldAndNewApisTests(TestInput input) {
		this.req = input.getReq();
		this.dirOldApi = input.getDirOldApi();
		this.dirNewApi = input.getDirNewApi();
	}
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		Collection<MutationOperator> operators1 = Arrays.asList(COI, COD, AORU, PRVOU_REFINED);
		Collection<String> methods1 = Arrays.asList("xnor");
		MutationRequest request1 = new MutationRequest("test", "utils.BooleanOps", operators1, methods1, false, false, 1);
		String dirOldApi1 = "test/OldApiMutants/test1/";
		String dirNewApi1 = "test/NewApiMutants/test1/";
		TestInput test1 = new TestInput(request1, dirOldApi1, dirNewApi1);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
			{test1}
		});
	}
	
	@Test
	public void oldApiMutantsVsNewApiMutants() {
		Log.getLog().publish("oldApiMutantsVsNewApiMutants started", this.getClass());
		Log.getLog().incLevel();
		TestingTools tt = new TestingTools(this.req, this.dirOldApi, this.dirNewApi);
		boolean result = tt.compareApisMutants();
		Log.getLog().decLevel();
		if (!result) Log.getLog().publish("oldApiMutantsVsNewApiMutants failed", this.getClass());
		else Log.getLog().publish("oldApiMutantsVsNewApiMutants passed", this.getClass());
		assertTrue(result);
	}
	
	@AfterClass
	public static void printLog() {
		System.out.println(Log.getLog().toString());
	}

}
