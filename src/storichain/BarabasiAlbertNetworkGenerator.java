package storichain;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.space.graph.Network;

public class BarabasiAlbertNetworkGenerator extends EntreNetworkGenerator implements NetworkGenerator<Object> {

	public BarabasiAlbertNetworkGenerator(Context<Object> context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Network<Object> createNetwork(Network<Object> network) {		

		this.network = network;

		initializeNetwork(getEdgeProbability());		

		// Evolve network using preferential attachment
		evolveNetwork();

		return network;
	}

}
