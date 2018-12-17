package storichain;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

public class Agent implements Comparable<Object> {

	protected Context<Object> context;
	protected Network<Object> network;
	protected String label;
	protected double betweennessCentrality;

	public Agent(Context<Object> context, Network<Object> network, String label) {		

		this.context = context;
		this.network = network;
		this.label = label;
		this.betweennessCentrality = 0;
	}

	
	public Context<Object> getContext() {
		return context;
	}


	public void setContext(Context<Object> context) {
		this.context = context;
	}

	public Network<Object> getNetwork() {
		return network;
	}


	public void setNetwork(Network<Object> network) {
		this.network = network;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setBetweennessCentrality(double betweennessCentrality) {
		this.betweennessCentrality = betweennessCentrality;
	}
	
	public double getBetweennessCentrality() {
		return betweennessCentrality;
	}
	
	/**
	 * Returns the "degree centrality" of the agent
	 * @return centrality
	 */
	public double getDegreeCentralityUtility() {	
		return network.getDegree(this);
	}
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
}