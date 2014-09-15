package mujava.generations;

public class NumberOfMutantsGoalTester implements GoalTester {
	private int currentNumberOfMutantsGenerated;
	private int maximumNumberOfMutantsToGenerate;
	
	public NumberOfMutantsGoalTester(int maximumNumberOfMutantsToGenerate) {
		this.maximumNumberOfMutantsToGenerate = maximumNumberOfMutantsToGenerate;
	}

	public boolean goalAchieved() {
		return this.currentNumberOfMutantsGenerated >= this.maximumNumberOfMutantsToGenerate;
	}

	public void update(GenerationsInformation ginfo) {
		this.currentNumberOfMutantsGenerated += ginfo.getLastMutantsUpdated().size();		
	}
	
	public boolean requireGenerationToFinish() {
		return false;
	}

}
