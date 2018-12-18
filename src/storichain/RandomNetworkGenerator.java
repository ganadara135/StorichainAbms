package storichain;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.RandomDensityGenerator;
import repast.simphony.space.graph.Network;

public class RandomNetworkGenerator extends EntreNetworkGenerator {

	private double density;
	
	public RandomNetworkGenerator(Context<Object> context, double density) {
		super(context);
		
		this.density = density;
	}
	
	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		this.network = network;
		
		// Evolve network 
		evolveNetwork();		
		
		RandomDensityGenerator<Object> ng = new RandomDensityGenerator<Object>(density, false, true);
		
		network = ng.createNetwork(network);
		
		return network;
	}
	
	@Override
	public void attachNode(Object n) {
		context.add(n);
	}
	
	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}
}
