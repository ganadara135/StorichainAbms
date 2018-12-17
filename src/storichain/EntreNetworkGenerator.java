package storichain;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class EntreNetworkGenerator implements NetworkGenerator<Object> {

	protected Context<Object> context;
	protected Network<Object> network;
	
	protected double edgeProbability;
	protected int edgesPerStep;
	protected int totalST;
	protected int totalPD;
	protected int totalRD;
	
	
	public EntreNetworkGenerator(Context<Object> context) {

		this.context = context;
		edgesPerStep = 0;
		totalST = 0;
		totalPD = 0;
		totalRD = 0;
	}
	
	public void setTotalST(int totalST) {
		this.totalST = totalST;
	}
	
	public int getTotalST() {
		return totalST;
	}
	
	public void setTotalPD(int totalPD) {
		this.totalPD = totalPD;
	}
	
	public int getTotalPD() {
		return totalPD;
	}
	public void setTotalRD(int totalRD) {
		this.totalRD = totalRD;
	}
	
	public int getTotalRD() {
		return totalRD;
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

		for (int i = 0; i < 10 && i < totalST; i++) {

			ST s = new ST(context, network, StoriBuilder.nextId("S"));

			context.add(s);

			StoriBuilder.stes.add(s);
			totalST--;

		}
		randomWire(p);
		
	}
	
	public void randomWire(double p) {
		//Initial wiring using a random network
		for (Object i: network.getNodes()) {
			for (Object j: network.getNodes()) {
				double random = RandomHelper.nextDoubleFromTo(0, 1);
				if (random <= p && !i.equals(j)) {
					network.addEdge(i, j);
				}
			}
		}
	}
	
	/*
	public void evolveNetwork() {
		while (totalST > 0 || totalPD > 0) {
			
			double random = RandomHelper.nextDoubleFromTo(0, 1);
			
			//Enter entrepreneur with less probability than customers
			if (totalPD > 0 && random <= 0.33) {
				
				Effectuator e;
				
				int type = RandomHelper.nextIntFromTo(1, 3);
				
				if (type == 1 && StoriBuilder.effectuator != null 
						&& !context.contains(StoriBuilder.effectuator)) {
					e = StoriBuilder.effectuator;
				} else if (type == 2 && StoriBuilder.causator != null 
						&& !context.contains(StoriBuilder.causator)) {
					e = StoriBuilder.causator;
				} else {					
					e = new Entrepreneur(context, network, StoriBuilder.nextId("E"));
					e.generateGoal();
				}
				attachNode(e);
				totalEntrepreneuers--;	
			} else if (totalST > 0) {
				ST c = new ST(context, network, StoriBuilder.nextId("C"));
				attachNode(c);
				StoriBuilder.stes.add(c);
				totalST--;
			}
		}
		//Assure the presence of the effectuator and/or causator
		
		if (StoriBuilder.effectuator != null 
				&& !context.contains(StoriBuilder.effectuator)) {
			Entrepreneur e = null;
			do {
				e = (Entrepreneur)context.getRandomObjects(Entrepreneur.class, 1).iterator().next();
			} while (e instanceof Causator);
			context.remove(e);
			attachNode(StoriBuilder.effectuator);
			
		}
		
		if (StoriBuilder.causator != null 
				&& !context.contains(StoriBuilder.causator)) {
			Entrepreneur e = null;
			do {
				e = (Entrepreneur)context.getRandomObjects(Entrepreneur.class, 1).iterator().next();
			} while (e instanceof Effectuator);
			context.remove(e);			
			attachNode(StoriBuilder.causator);
		}
	}
	*/
	
	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		// TODO Auto-generated method stub
		return network;
	}
	
	public void attachNode(Object n) {
		
	}
}
