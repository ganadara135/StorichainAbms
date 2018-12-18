package storichain;

import repast.simphony.engine.environment.RunEnvironment;

public class Parameters {

	public static repast.simphony.parameter.Parameters params = RunEnvironment.getInstance().getParameters();
	
	public static String networkGenerator;
	
	public static int marketSplit; // The percentage of the population that "like" a product element
									// the percentage of customers that have their demand element di =1 for i ∈ 1..n
	public static int vectorSpaceSize;
	

	public static int numberOfPD;	
	public static int numberOfST;
	public static int numberOfRD;
	public static int numberOfStory;
	// this parameters sets the approximate density that should have a network generated with 􏰂the random network model􏰃
	public static double randomNetworkDensity;
	// used to con􏰁gure the customers adaptation behavior. The 􏰁rst parameter is adaptation threshold, de􏰁ned as the percentage of neighbors 
	// that should have adapted product element pi in order for the ob- server also to adapt that. The second is adaptation speed, 
	// it sets the speed that demand adaptation occurs.
	public static int adaptationThreshold;
	public static int adaptationSpeed;
	// if this parameter is set as true, then entrepreneurs aggregate their product vectors based on the demand of the connected customers network.
	public static boolean aggregateProductVector;
	// allows for specy􏰁ng an utility function (Section 3.1.1) used by the causator (the e􏰀ectuator does not use opportunity cost calculations 2.1.2). The possible options are: none (random), connections utility, degree centrality, betweenness centrality
	public static String utilityFunction;

	// The percentage of the customers sample that needs to have a product element as 1
	// in order to change the initial value of the product elements vector
	public static int productElementChangeThreshold;
	// used to simulate an evolving network. When it equals 0 then no new nodes are added to the network
	public static double newConnectionsProbability;
	// the number of edges to add per step (in the network generation models models that use this approach)
	public static int edgesPerStep;
	// sets weather the simulation environment should contain only one causator, only one e􏰀ectuator, or both of them. 
	public static String observedEntrepreneur;
	// defi􏰁nes the maximal depth that can have a meeting in the network (number of steps to reach the other party)
	public static int maxDepthForMeeting;
	// the minimal and maximum amount of available money to the entrepreneurs (the actual amount is randomly initial- ized based on these limits)
	public static double minAvailableMoney; // Thousands �
	public static double maxAvailableMoney; // Thousands �
	// defi􏰁ned as the percentage of available monetary means that can be risked to invest by the e􏰀ectuator
	public static int affordableLoss; //Percent
	//%, the percentage probability that a customers accepts an offer 
	// slightly different from the original demand
	public static int customersPersuadability;
	
	public static double edgeProbability;
	public static int meanDegree;
	
	//􏰉 Product element change threshold: this threshold is used to re􏰁ne the product based on the results of the market research. It gives the percentage
	// of customers sample that should have the demand element di = 1, in order to decide to o􏰀er this product feature.
	//􏰉 Network generator type: random network, small-world network, Barabási- Albert network generator, copying network generator (Section 2.3)
	
	
	public static void initialize() {
		
		networkGenerator = (String)params.getValue("networkGenerator");
		vectorSpaceSize = (Integer)params.getValue("vectorSpaceSize");

		
		productElementChangeThreshold = (Integer)params.getValue("productElementChangeThreshold");
		adaptationSpeed = (Integer)params.getValue("adaptationSpeed");
		marketSplit = (Integer)params.getValue("marketSplit");
		
		numberOfPD = (Integer)params.getValue("numberOfPD");
		numberOfST = (Integer)params.getValue("numberOfST");
		numberOfRD = (Integer)params.getValue("numberOfRD");
		
		randomNetworkDensity = (Double)params.getValue("randomNetworkDensity");
		
		utilityFunction = (String)params.getValue("utilityFunction");
		aggregateProductVector = (Boolean)params.getValue("aggregateProductVector");
		maxDepthForMeeting = (Integer)params.getValue("maxDepthForMeeting");
		
		
		customersPersuadability = (Integer)params.getValue("customersPersuadability");
		minAvailableMoney = (Double)params.getValue("minAvailableMoney");
		maxAvailableMoney = (Double)params.getValue("maxAvailableMoney");
		affordableLoss = (Integer)params.getValue("affordableLoss");

		maxDepthForMeeting = (Integer)params.getValue("maxDepthForMeeting");
		
		edgesPerStep = (Integer)params.getValue("edgesPerStep");
		edgeProbability = (Double)params.getValue("edgeProbability");
		randomNetworkDensity = (Double)params.getValue("randomNetworkDensity");
		meanDegree = (Integer)params.getValue("meanDegree");
		utilityFunction = (String)params.getValue("utilityFunction");
		aggregateProductVector = (Boolean)params.getValue("aggregateProductVector");
		newConnectionsProbability = (Double)params.getValue("newConnectionsProbability");
		
	}
}
