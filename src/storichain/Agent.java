package storichain;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

public class Agent implements Comparable<Object> {

	protected Context<Object> context;
	protected Network<Object> network;

	public Agent(Context<Object> context, Network<Object> network, String label) {		

		this.context = context;
		this.network = network;
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
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
}