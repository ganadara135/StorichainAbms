package storichain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class PD extends Agent {
	/*
	protected List<Means> availableMeans;
	protected Goal goal;
	protected boolean offering;
	//protected boolean productRefinedOnce;
*/
	
	protected int[] demandVector;
	
	
	public PD(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		//availableMeans = new ArrayList<Means>();
		//generateAvailableMeans();
		//setOffering(false);
		//productRefinedOnce = false;
		demandVector = new int[Parameters.vectorSpaceSize];
		initializeDemandVector();
	}
	
	/**
	 * Initialize demand vector with all element set as 0s
	 */
	public void initializeDemandVector() {
		double marketSplit = Parameters.marketSplit / 100.0;
		for (int i = 0; i < demandVector.length; i++) {
			
			double r = RandomHelper.nextDoubleFromTo(0, 1);
			
			demandVector[i] = r < marketSplit ? 1 : 0;
		}
	}
	
	public int[] getDemandVector() {
		return demandVector;
	}
	
	public void setDemandVector(int[] demandVector) {
		this.demandVector = demandVector;
	}
	

	/**
	 *  Adapts product vector based on neighbours demand and simulation parameters 
	 */
	public void adaptProductVector() {	
		
		//System.out.println("PD adaptProductVector()");
		
		HashMap<Integer, Integer> neighboursAdaption = new HashMap<Integer, Integer>();
		
		double threshold = Parameters.adaptationThreshold / 100.0;
		int neighbours = 0;
		
		for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
			neighboursAdaption.put(i, 0);
		}
		
		for (Object o: network.getAdjacent(this)) {
			if (o instanceof PD) {
				PD c = (PD)o;
				
				for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
					int count = neighboursAdaption.get(i);
					if (c.getDemandVector()[i] == 1) {
						count++;
					}
					neighboursAdaption.put(i, count);
				}
			}
			neighbours++;
		}
		
		for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
			if ((double)neighboursAdaption.get(i) / neighbours >= threshold ) {
				demandVector[i] = 1;
			}
		}		 
	}
	
	/**
	 * Process offer made by an entrepreneur
	 * @param productVector
	 */
	public void processOffer(int[] productVector) {

		setNegotiating(true);
		
		int d = StoriBuilder.hammingDistance(demandVector, productVector);
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (d>0 && d <= Math.ceil(Parameters.vectorSpaceSize / 2.0) 
				&& r < (Parameters.customersPersuadability / 100.0)) {
			demandVector = productVector;
		}
		
		setNegotiating(false);
	}
}
