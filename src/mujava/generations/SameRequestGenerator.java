package mujava.generations;

import mujava.app.Core;
import mujava.app.MutationRequest;

public class SameRequestGenerator implements RequestGenerator {
	private MutationRequest request;
	private String originalOutputDir;
	private int currentGen;
	
	public SameRequestGenerator(MutationRequest request) {
		this.request = request;
		this.currentGen = 0;
		this.originalOutputDir = request.outputDir;
	}
	
	public MutationRequest nextRequest() {
		this.request.outputDir = appendDir(this.originalOutputDir, "generation-"+Integer.toString(this.currentGen));
		return this.request;
	}

	public void update(GenerationsInformation generationsInformation, Integer lastGeneration) {
		this.currentGen = lastGeneration + 1;
	}
	
	/**
	 * appends a directory to a path
	 * 
	 * @param original	:	the original path		:	{@code String}
	 * @param newDir	:	the directory to append	:	{@code String}
	 * @return the original path with the new directory appended : {@code String}
	 */
	private String appendDir(String original, String newDir) {
		String fixedDir = newDir.endsWith(Core.SEPARATOR)?newDir:(newDir+Core.SEPARATOR);
		if (original.endsWith(Core.SEPARATOR)) {
			return original + fixedDir;
		} else {
			return original + Core.SEPARATOR + fixedDir;
		}
	}

}
