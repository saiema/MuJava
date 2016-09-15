package test.mujava2.utils;

import mujava2.api.mutator.MutationRequest;

public class TestInput {
	
	private final MutationRequest req;
	private final String dirOldApi;
	private final String dirNewApi;
	
	public TestInput(MutationRequest req, String dirOldApi, String dirNewApi) {
		this.req = req;
		this.dirOldApi = dirOldApi;
		this.dirNewApi = dirNewApi;
	}

	public MutationRequest getReq() {
		return req;
	}

	public String getDirOldApi() {
		return dirOldApi;
	}

	public String getDirNewApi() {
		return dirNewApi;
	}
	
	

}
