package storichain;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

public class RD extends Agent {
	
	protected List<Means> availableMeans;
	protected Goal goal;
	protected boolean offering;
	//protected boolean productRefinedOnce;

	public RD(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		availableMeans = new ArrayList<Means>();
		//generateAvailableMeans();
		//setOffering(false);
		//productRefinedOnce = false;
	}
	
	public List<Means> getAvailableMeans() {
		return availableMeans;
	}

	public void setAvailableMeans(List<Means> availableMeans) {
		this.availableMeans = availableMeans;
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
	 * @return the offering
	 */
	public boolean isOffering() {
		return offering;
	}
	
	public boolean doReaction() {
		return false;
	}

}
