package storichain;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.space.graph.Network;

public class EntreNetworkGenerator implements NetworkGenerator<Object> {

	protected Context<Object> context;
	protected Network<Object> network;
	
	protected double edgeProbability;
	
	
	public EntreNetworkGenerator(Context<Object> context) {

		this.context = context;
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
	
	public double getEdgeProbability() {
		return edgeProbability;

	}
	
	protected void initializeNetwork(double p) {
/*
		for (int i = 0; i < 10 && i < totalCustomers; i++) {

			Customer c = new Customer(context, network, SimulationBuilder.nextId("C"));

			context.add(c);

			SimulationBuilder.customers.add(c);
			totalCustomers--;

		}
		randomWire(p);
		*/
	}
	
	public void evolveNetwork() {
		
	}
	
	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		// TODO Auto-generated method stub
		return network;
	}
	
}
