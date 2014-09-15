package mujava.generations;

/**
 * This interface is used in conjunction with the class {@code Generator}
 * to evaluate when to stop mutants generation
 * 
 * @author Simón Emmanuel Gutiérrez Brida
 * @version 0.1
 * @see Generator
 */
public interface GoalTester {
	
	/**
	 * @return {@code true} if the goal has been achieved by the {@code Generator}
	 */
	public boolean goalAchieved();
	
	/**
	 * updates this class with the information of the generations produced so far
	 * @param ginfo : generations information : {@code GenerationsInformation}
	 */
	public void update(GenerationsInformation ginfo);
	
	public boolean requireGenerationToFinish();

}
