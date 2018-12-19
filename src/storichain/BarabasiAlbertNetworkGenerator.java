package storichain;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class BarabasiAlbertNetworkGenerator extends EntreNetworkGenerator implements NetworkGenerator<Object> {

	public BarabasiAlbertNetworkGenerator(Context<Object> context) {
		super(context);
	}
	
	
	/* (non-Javadoc)
	 * @see repast.simphony.context.space.graph.RandomDensityGenerator#createNetwork(repast.simphony.space.graph.Network)
	 */
	@Override
	public Network<Object> createNetwork(Network<Object> network) {		

		this.network = network;

		initializeNetwork(getEdgeProbability());	

		// Evolve network using preferential attachment
		evolveNetwork();

		return network;
	}
	
	/**
	 * Preferential attachment  // 주변에 클러스터가 큰 그룹에 붙는다.
	 * @param n Node to be attached
	 */
	public void attachNode(Object n) {
		//System.out.println("attachNode() in Barabasi");
		
		context.add(n);
		//When checking the network degree, look only at the "entreprenurial network",
		// i.e at the network without means and goals
		for (int i = 0; i < edgesPerStep; i++) {
			
			double totalDegree = network.getDegree();
			
			boolean attached = false;
			
			while (!attached) {
				
				Object o = context.getRandomObject();
				
				double prob = (network.getDegree(o) + 1) / (totalDegree + network.size());
				
				if (prob > 0.0 && RandomHelper.nextDoubleFromTo(0,1) <= prob) {
					network.addEdge(n, o);
					attached = true;
				}			
			}
		}
	}

}
