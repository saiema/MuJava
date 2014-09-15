package mujava.generations;

public class GenerationsGoalTester implements GoalTester {
	private int currentGeneration;
	private int goalGeneration;
	
	public GenerationsGoalTester(int goalGeneration) {
		this.goalGeneration = goalGeneration;
		this.currentGeneration = 0;
	}

	public boolean goalAchieved() {
		return this.currentGeneration >= this.goalGeneration;
	}

	public void update(GenerationsInformation ginfo) {
		this.currentGeneration = Math.max(this.currentGeneration, ginfo.getLastGenerationUpdated());		
	}
	
	public boolean requireGenerationToFinish() {
		return true;
	}

}
