package test.mujava2.utils;

import java.io.File;

import mujava2.api.mutator.MutationRequest;

public class TestInput {
	
	private final MutationRequest req;
	private final File dirOldApi;
	private final File dirNewApi;
	
	public TestInput(MutationRequest req, File dirOldApi, File dirNewApi) {
		this.req = req;
		this.dirOldApi = dirOldApi;
		this.dirNewApi = dirNewApi;
	}

	public MutationRequest getReq() {
		return req;
	}

	public File getDirOldApi() {
		return dirOldApi;
	}

	public File getDirNewApi() {
		return dirNewApi;
	}
	
	

}
