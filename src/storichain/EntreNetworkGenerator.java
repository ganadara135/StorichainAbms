package storichain;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public abstract class EntreNetworkGenerator implements NetworkGenerator<Object> {

	protected Context<Object> context;
	protected Network<Object> network;
	
	protected double edgeProbability;
	protected int edgesPerStep;
	protected int totalST;
	protected int totalPD;
	//protected int totalRD;
	
	
	public EntreNetworkGenerator(Context<Object> context) {

		this.context = context;
		edgesPerStep = 0;
		totalST = 0;
		totalPD = 0;
		//totalRD = 0;
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
	
	/**
	 * @return the edgesPerStep
	 */
	public int getEdgesPerStep() {
		return edgesPerStep;
	}

	/**
	 * @param edgesPerStep the edgesPerStep to set
	 */
	public void seEdgesPerStep(int edgesPerStep) {
		this.edgesPerStep = edgesPerStep;
	}

	
	/**
	 * @return the edgeProbability
	 */
	public double getEdgeProbability() {
		return edgeProbability;
	}

	/**
	 * @param edgeProbability the edgeProbability to set
	 */
	public void setEdgeProbability(double edgeProbability) {
		this.edgeProbability = edgeProbability;
	}
	
	/**
	 * Initializes the network
	 * @param p initial wiring probability
	 */
	protected void initializeNetwork(double pp) {
		
		for (int i = 0; i < 10 && i < totalPD; i++) {

			PD p1 = new PD(context, network, StoriBuilder.nextId("P"));
			context.add(p1);
			StoriBuilder.pdes.add(p1);
			totalPD--;
		}
		randomWire(pp);
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
	
	
	public void evolveNetwork() {
		while (totalST > 0 || totalPD > 0) {
			
			double random = RandomHelper.nextDoubleFromTo(0, 1);
			
			//Enter entrepreneur=ST with less probability than customers = PD
			if (totalST > 0 && random <= 0.33) {
				
				ST e = null;
				
				int type = RandomHelper.nextIntFromTo(1, 2); // 3);
				
				if (type == 1 && StoriBuilder.effectuatorST != null && !context.contains(StoriBuilder.effectuatorST)) {
					e = StoriBuilder.effectuatorST;
				} /*else if (type == 2 && StoriBuilder.causatorST != null && !context.contains(StoriBuilder.causatorST)) {
					e = StoriBuilder.causator;
				} */else {					
					e = new ST(context, network, StoriBuilder.nextId("S"));
					e.generateGoal();
				}
				
				attachNode(e);
				totalST--;
			} else if (totalST > 0) {        //  여기서 부터 작업 12월 17일 effectuator  방식으로 처
				PD c = new PD(context, network, StoriBuilder.nextId("P"));
				attachNode(c);
				StoriBuilder.pdes.add(c);
				totalPD--;
			}
		}
		
		//Assure the presence of the effectuator and/or causator
		if (StoriBuilder.effectuatorST != null && !context.contains(StoriBuilder.effectuatorST)) {
			Agent e = null;
			do {
				e = (ST)context.getRandomObjects(ST.class, 1).iterator().next();
				System.out.println("xxxxxxxxxxxxxxx");
			} while (e instanceof RD || e instanceof PD);
			context.remove(e);
			attachNode(StoriBuilder.effectuatorST);
		}
		
		if (StoriBuilder.effectuatorPD != null 
				&& !context.contains(StoriBuilder.effectuatorPD)) {
			Agent e = null;
			do {
				e = (PD)context.getRandomObjects(PD.class, 1).iterator().next();
				System.out.println("yyyyyyyyyyyyy");
			} while (e instanceof ST || e instanceof RD);
			context.remove(e);	
			attachNode(StoriBuilder.effectuatorPD);
		}
	}
	
	
	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		// TODO Auto-generated method stub
		return network;
	}
	
	public void attachNode(Object n) {
		
	}
}
