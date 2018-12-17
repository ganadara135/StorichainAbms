package storichain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class ST extends Agent {
	
	protected List<Means> availableMeans;
	protected Goal goal;
	protected boolean offering;
	//protected boolean productRefinedOnce;
	
	protected int[] demandVector;

	public ST(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		availableMeans = new ArrayList<Means>();
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
	
	/**
	 * Effectuation scenario:
	 * Aggregate goal product vector based on the demand of the surrounding customers
	 */
	public void aggregateGoalProductVector() {
		int depth = RandomHelper.nextIntFromTo(1, Parameters.maxDepthForMeeting);
		
		List<ST> stProdcuts = new ArrayList<ST>(); // 자기 자신을 처
		
		StoriBuilder.getSTAcquiantances(this, depth, stProdcuts);		
		
		if (stProdcuts.size() > 0) {
			Collections.shuffle(stProdcuts);
			
			int sampleTotal = RandomHelper.nextIntFromTo(1, stProdcuts.size());
			
			//Survey random connected customers sample
			
			int[] surveyResults = new int[Parameters.vectorSpaceSize];
			
			for (int i = 0; i < surveyResults.length; i++) {
				surveyResults[i] = 0;
			}			
			
			for (int i = 0; i < sampleTotal; i++) {
				int[] demandVector = stProdcuts.get(i).demandVector;
				
				for (int j = 0; j < demandVector.length; j++) {
					surveyResults[j] += demandVector[j];
				}
			}
			
			int[] productVector = goal.getProductVector();
			
			for (int i = 0; i < surveyResults.length; i++) {				
				if ( ((double)surveyResults[i] / (double)sampleTotal) * 100 
						>= Parameters.productElementChangeThreshold) {					
					productVector[i] = (productVector[i] + 1) % 2;				
				}
			}
		}
	}

	public boolean doWriting() {
		return false;
	}
}
