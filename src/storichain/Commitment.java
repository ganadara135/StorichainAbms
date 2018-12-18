package storichain;


public class Commitment {
	
	private Goal goal;
	private Means means;
	private ST secondParty;
	
	private Story story;
	
	public Commitment(ST secondParty) {
		this.secondParty = secondParty;
		goal = secondParty.getGoal();
		means = null;
	}


	/**
	 * @return the secondParty
	 */
	public ST getSecondParty() {
		return secondParty;
	}

	/**
	 * @param secondParty the secondParty to set
	 */
	public void setSecondParty(ST secondParty) {
		this.secondParty = secondParty;
	}

	/**
	 * @return the goal
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * @param goal the goal to set
	 */
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	/**
	 * @return the means
	 */
	public Means getMeans() {
		return means;
	}

	/**
	 * @param means the means to set
	 */
	public void setMeans(Means means) {
		this.means = means;
	}

}
