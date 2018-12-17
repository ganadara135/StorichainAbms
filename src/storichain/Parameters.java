package storichain;

import repast.simphony.engine.environment.RunEnvironment;

public class Parameters {

	public static repast.simphony.parameter.Parameters params = RunEnvironment.getInstance().getParameters();
	
	public static String networkGenerator;
	
	public static int marketSplit; // The percentage of the population that "like" a product element
	public static int vectorSpaceSize;
	
	//public static int numberOfCustomers;	
	//public static int numberOfEntrepreneurs;
	public static int numberOfPD;	
	public static int numberOfST;
	public static int numberOfRD;
	public static int numberOfStory;
	
	public static double randomNetworkDensity;
	public static int adaptationSpeed;
	
	public static boolean aggregateProductVector;
	
	public static String utilityFunction;
	public static int maxDepthForMeeting;

	// The percentage of the customers sample that needs to have a product element as 1
	// in order to change the initial value of the product elements vector
	public static int productElementChangeThreshold;
	
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
		
	}
}
