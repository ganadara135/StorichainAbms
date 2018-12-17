package storichain;

import repast.simphony.engine.environment.RunEnvironment;

public class Parameters {

	public static repast.simphony.parameter.Parameters params = RunEnvironment.getInstance().getParameters();
	
	public static String networkGenerator;
	
	//public static int numberOfCustomers;	
	//public static int numberOfEntrepreneurs;
	public static int numberOfPD;	
	public static int numberOfST;
	public static int numberOfRD;
	public static int numberOfStory;
	
	public static double randomNetworkDensity;
	public static int adaptationSpeed;

	
	public static void initialize() {
		
		networkGenerator = (String)params.getValue("networkGenerator");
		
		adaptationSpeed = (Integer)params.getValue("adaptationSpeed");
		
		numberOfPD = (Integer)params.getValue("numberOfPD");
		numberOfST = (Integer)params.getValue("numberOfST");
		numberOfRD = (Integer)params.getValue("numberOfRD");
		
		randomNetworkDensity = (Double)params.getValue("randomNetworkDensity");
		
	}
}
