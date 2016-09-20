package test.mujava2;

import java.io.File;
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
	private File dirOldApi;
	private File dirNewApi;
	
	public OldAndNewApisTests(TestInput input) {
		this.req = input.getReq();
		this.dirOldApi = input.getDirOldApi();
		this.dirNewApi = input.getDirNewApi();
	}
	
	@Parameters
	public static Collection<Object[]> firstValues() {
		
		TestingTools.setPrototypeMutationsChecking(false, true);
		
		Collection<MutationOperator> operators1 = Arrays.asList(COI, COD, AORU, PRVOU_REFINED);
		Collection<String> methods1 = Arrays.asList("xnor");
		MutationRequest request1 = new MutationRequest("test", "utils.BooleanOps", operators1, methods1, false, false, 1);
		File dirOldApi1 = new File("test/OldApiMutants/test1/");
		File dirNewApi1 = new File("test/NewApiMutants/test1/");
		TestInput test1 = new TestInput(request1, dirOldApi1, dirNewApi1);
		
		Collection<MutationOperator> operators2 = Arrays.asList(COI, COD, AORU, PRVOU_REFINED);
		Collection<String> methods2 = Arrays.asList("xnor");
		MutationRequest request2 = new MutationRequest("test", "utils.BooleanOps", operators2, methods2, false, false, 2);
		File dirOldApi2 = new File("test/OldApiMutants/test2/");
		File dirNewApi2 = new File("test/NewApiMutants/test2/");
		TestInput test2 = new TestInput(request2, dirOldApi2, dirNewApi2);
		
		//PARAMETERS
		return Arrays.asList(new Object[][] {
			{test1},
			{test2}
		});
	}
	
	@Test
	public void oldApiMutantsVsNewApiMutants() {
		Log.getLog().publish("oldApiMutantsVsNewApiMutants started", this.getClass());
		Log.getLog().incLevel();
		TestingTools tt = new TestingTools(this.req, this.dirOldApi.toPath().resolve("oldApiMutantsVsNewApiMutants").toAbsolutePath().toString(), this.dirNewApi.toPath().resolve("oldApiMutantsVsNewApiMutants").toAbsolutePath().toString());
		boolean result = tt.compareApisMutants();
		Log.getLog().decLevel();
		if (!result) Log.getLog().publish("oldApiMutantsVsNewApiMutants failed", this.getClass());
		else Log.getLog().publish("oldApiMutantsVsNewApiMutants passed", this.getClass());
		assertTrue(result);
	}
	
	@Test
	public void oldApiMurantsVsNewApiMutations() {
		Log.getLog().publish("oldApiMurantsVsNewApiMutations started", this.getClass());
		Log.getLog().incLevel();
		TestingTools tt = new TestingTools(this.req, this.dirOldApi.toPath().resolve("oldApiMurantsVsNewApiMutations").toAbsolutePath().toString(), this.dirNewApi.toPath().resolve("oldApiMurantsVsNewApiMutations").toAbsolutePath().toString());
		boolean result = tt.compareApisMutations();
		Log.getLog().decLevel();
		if (!result) Log.getLog().publish("oldApiMurantsVsNewApiMutations failed", this.getClass());
		else Log.getLog().publish("oldApiMurantsVsNewApiMutations passed", this.getClass());
		assertTrue(result);
	}
	
	@Test
	public void oldApiLastGenerationVsNewApiLastGeneration() {
		Log.getLog().publish("oldApiLastGenerationVsNewApiLastGeneration started", this.getClass());
		Log.getLog().incLevel();
		TestingTools tt = new TestingTools(this.req, this.dirOldApi.toPath().resolve("oldApiLastGenerationVsNewApiLastGeneration").toAbsolutePath().toString(), this.dirNewApi.toPath().resolve("oldApiLastGenerationVsNewApiLastGeneration").toAbsolutePath().toString());
		boolean result = tt.compareApisLastMutantGeneration();
		Log.getLog().decLevel();
		if (!result) Log.getLog().publish("oldApiLastGenerationVsNewApiLastGeneration failed", this.getClass());
		else Log.getLog().publish("oldApiLastGenerationVsNewApiLastGeneration passed", this.getClass());
		assertTrue(result);
	}
	
	@AfterClass
	public static void printLog() {
		System.out.println(Log.getLog().toString());
	}

}
