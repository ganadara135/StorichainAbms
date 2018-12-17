package storichain;

public class Commitment {
	
	private Goal goal;
	private Means means;
	private Story story;
	
	public Commitment() {//Entrepreneur secondParty) {

	//	this.secondParty = secondParty;

//		goal = secondParty.getGoal();
		means = null;
	}
	
	public Goal getGoal() {

		return goal;

	}


	public void setGoal(Goal goal) {

		this.goal = goal;

	}


	public Means getMeans() {

		return means;

	}


	public void setMeans(Means means) {

		this.means = means;

	}

}
