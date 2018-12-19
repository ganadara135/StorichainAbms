package storichain;


import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

public class StoriBuilder extends DefaultContext<Object> implements ContextBuilder<Object> {

	public static Context<Object> context;
	public static Network<Object> network;
	public static Network<Object> effectuationNetwork;	
	private static EntreNetworkGenerator networkGenerator;
	public static boolean allEntrepreneursOffering;   // to check to stop simulation
	public static ArrayList<ST> stes;
	//public static ArrayList<RD> rdes;
	public static ArrayList<PD> pdes;
	
	public static EffectuatorST effectuatorST;
	//public static EffectuatorRD effectuatorRD;
	public static EffectuatorPD effectuatorPD;
	private static HashMap<String, Integer> lastIds;
	public static int staticDemandSteps;
	public static double[]  productElementCost;   //이상적 상품(스토리)의 cost

	
	@Override
	public Context build(Context<Object> context) {
		Parameters.initialize();
		
		stes = new ArrayList<ST>();
	//	oldDemand = new ArrayList<int[]>();
		//rdes = new ArrayList<RD>();
		pdes = new ArrayList<PD>();
		staticDemandSteps = 0;
		
		generateProductElementCosts();
		
		lastIds = new HashMap<String, Integer>();
			
		context.setId("storichain");
		
		StoriBuilder.context = context;
		
		
		buildNetworks();
		
		effectuatorST = new EffectuatorST(context, network, "StoryTeller");
	//	effectuatorRD = new EffectuatorRD(context, network, "Reader");
		effectuatorPD = new EffectuatorPD(context, network, "Producer");

		
		//Network generation
		if (Parameters.networkGenerator.equals("BarabasiAlbert")) {
			networkGenerator = new BarabasiAlbertNetworkGenerator(context);
		} else if (Parameters.networkGenerator.equals("ZombieNetwork")) {
			networkGenerator = new ZombieNetworkGenerator(context);
		} else if (Parameters.networkGenerator.equals("RandomNetwork")) {
			networkGenerator = new RandomNetworkGenerator(context, Parameters.randomNetworkDensity);
		}
		
		networkGenerator.setTotalST(Parameters.numberOfST);
		networkGenerator.setTotalPD(Parameters.numberOfPD);
		//networkGenerator.setTotalRD(Parameters.numberOfRD);
		networkGenerator.seEdgesPerStep(Parameters.edgesPerStep);
		networkGenerator.setEdgeProbability(Parameters.edgeProbability);
		
		network = networkGenerator.createNetwork(network);
		
		initializeDemandVectors();
		aggregateProductVectors();
		
		calculateBetweennesCentralities();
		
		allEntrepreneursOffering = false;
		
		scheduleActions();
		
		return context;
	}
	
	/**
	 * Refine the product vector of the entrepreneurs based on the
	 * connected customers (randomly)
	 */
	public void aggregateProductVectors() {
		
		if (!Parameters.aggregateProductVector) {
			return;
		}
		
		double prob = RandomHelper.nextDoubleFromTo(0, 1);
		
		for (Object o: context.getObjects(ST.class)) {
			//Skip causator and effectuator
			//if (o instanceof Causator || o instanceof Effectuator) {
			if (o instanceof EffectuatorST) {
				continue;
			}
			
			double r = RandomHelper.nextDoubleFromTo(0, 1);
			
			if (r >= prob) {
				ST e = (ST)o;
				e.aggregateGoalProductVector();
			}
		}		
	}
	
	/**
	 * Recursively get all customer acquaintances of a node using a specified network depth.
	 * @param n node
	 * @param depth
	 * @param List of customers acquaintances
	 */
	public static void getPDAcquiantances(Object n, int depth, List<PD> customers) {		
		for (Object o: network.getAdjacent(n)) {
			if (o instanceof PD) {
				customers.add((PD)o);
			}
			if (depth > 1) {
				getPDAcquiantances(o, depth-1, customers);
			}
		}
	}

	// default setting such as color, thickness, 
	private void buildNetworks() {
		EdgeCreator<CustomNetworkEdge, Object> edgeCreator = new CustomEdgeCreator();		
		
		//Build Entrepreneurial network
		NetworkBuilder<Object> netBuilder2 = new NetworkBuilder<Object>("full network",context, false);
		netBuilder2.setEdgeCreator(edgeCreator);
		network = netBuilder2.buildNetwork();
		
		//Build Effectuation network
		NetworkBuilder<Object> netBuilder3 = new NetworkBuilder<Object>("effectual network",context, false);
		netBuilder3.setEdgeCreator(edgeCreator);	
		effectuationNetwork = netBuilder3.buildNetwork();
	}
	
	/**
	 *  Initialize All users' demand vectors using the define "Market split", i.e
	 *  the percentage of customers that "like" a certain product element
	 */
	private void initializeDemandVectors() {
		ArrayList<PD> demand = new ArrayList<PD>();
		
		for (Object c: context.getObjects(PD.class)) {
			demand.add((PD)c);
		}
		
		int shouldLikeProductElement = (int)Math.ceil(((double)Parameters.marketSplit / 100) * demand.size());
		
		for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
			ArrayList<PD> copy = new ArrayList<PD>(demand);
			
			for (int j = 0; j < shouldLikeProductElement; j++) {
				PD c = copy.remove(RandomHelper.nextIntFromTo(0, copy.size() - 1));
				c.getDemandVector()[i] = 1;
			}
		}
	}
	
	/**
	 * Calculates the betweenness centrality for each node, using the JUNG implemented
	 * betweenness centrality calculator algorithm 
	 */	
	public void calculateBetweennesCentralities() {			
		
		ContextJungNetwork<Object> N = (ContextJungNetwork<Object>)network;
		
		Graph<Object, RepastEdge<Object>> G = N.getGraph();
		
		BetweennessCentrality<Object, RepastEdge<Object>> ranker = new BetweennessCentrality<Object, RepastEdge<Object>>(G);
		
		ranker.setRemoveRankScoresOnFinalize(false);
		ranker.evaluate();
		
		for (Object n: network.getNodes()) {
			Agent a = (Agent)n;
			// Normalize by (n-1)(n-2)/2
			a.setBetweennessCentrality(ranker.getVertexRankScore(n) / (((N.size()-1) * (N.size()-2)) / 2.0) );
		}
	}
	
	/**
	 * Returns the "network density"
	 * density = 2 * number of edges / N * (N-1)
	 * @return networkDensity
	 */
	public double getNetworkDensity() {
		
		return ( 2.0 * network.numEdges() ) / ( network.size() * (network.size()-1) );
	}
	
	/**
	 * Returns the next Id of a node, also by specifying a certain prefix
	 * @param prefix
	 * @return nextId
	 */
	public static String nextId(String prefix) {
		int nextId;
		
		if (lastIds.containsKey(prefix)) {
			nextId = lastIds.get(prefix) + 1;
			lastIds.put(prefix, nextId);
		} else {
			nextId = 1;
			lastIds.put(prefix, nextId);
		}
		
		return prefix + String.valueOf(nextId);
	}
	
	public void scheduleActions() {
		ISchedule schedule = repast.simphony.engine.environment.RunEnvironment.getInstance().getCurrentSchedule();
		
		//Schedule adaptProductVector for each ST
		//ArrayList<ST> st = new ArrayList<ST>();
		ArrayList<PD> pd = new ArrayList<PD>();
		//ArrayList<RD> rd = new ArrayList<RD>();
		
		for (Object c: context.getObjects(PD.class)) {
			pd.add((PD)c);
		}
		
		ScheduleParameters parameters = ScheduleParameters.createRepeating(1, 6 - Parameters.adaptationSpeed, 1);		
		
		schedule.scheduleIterable(parameters, pd, "adaptProductVector", true);
	}
	
	/**
	 * Randomly generate product element costs
	 */
	private void generateProductElementCosts() {
		productElementCost = new double[Parameters.vectorSpaceSize];
		double avgAvailableMoney = (Parameters.minAvailableMoney + Parameters.maxAvailableMoney) / 2.0;
		
		for (int i = 0; i < productElementCost.length; i++) {
			productElementCost[i] = RandomHelper.nextIntFromTo(1, 3) * Parameters.minAvailableMoney;
			productElementCost[i] = productElementCost[i] > avgAvailableMoney ? avgAvailableMoney : productElementCost[i];
		}
	}
	
	/**
	 * Returns the "Hamming distance" between two equal length 0-1 vectors
	 * @param p1
	 * @param p2
	 * @return int
	 */
	public static int hammingDistance(int[] p1, int[] p2) {
		int d = 0;
		
		for (int i = 0; i < p1.length; i++) {
			d += p1[i] ^ p2[i];
		}
		
		return d;		
	}
	
	public static void printMessage(String m) {
		ISchedule schedule = repast.simphony.engine.environment.RunEnvironment.getInstance()
        .getCurrentSchedule();
		
		System.out.println(m + " - " + String.valueOf(schedule.getTickCount()));
	}
	
	/**
	 * Returns the the bits that are different in two equal length 0-1 vectors
	 * @param p1
	 * @param p2
	 * @return s
	 */
	public static int[] diff(int[] p1, int[] p2) {
		int[] diff = new int[p1.length];
		
		for (int i = 0; i < p1.length; i++) {
			diff[i] = p1[i] ^ p2[i];
		}
		
		return diff;
	}
	
	
	/**
	 *  Evolves network (if set) during the simulation (adding new nodes randomly)
	 */
	@ScheduledMethod(start=1,interval=2)
	public void evolveNetwork() {
		
		//System.out.println("StoriBuilder evolveNetwork");
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);	
		
		if (r < Parameters.newConnectionsProbability) {
			int random = RandomHelper.nextIntFromTo(1, 5);
			
			Object attached;
			
			switch (random) {
				default:
					PD c = new PD(context, network, nextId("P"));
					networkGenerator.attachNode(c);
					attached = c;
					break;
				case 2:
					ST e = new ST(context, network, nextId("S"));
					e.generateGoal();
					networkGenerator.attachNode(e);					
					attached = e;				
					break;
			}
			
			for (RepastEdge<Object> edge: network.getEdges(attached)) {
				((CustomNetworkEdge) edge).setThickness(2.0); 
				((CustomNetworkEdge) edge).setColor(Color.red);
				//System.out.println("Check Color.red");
			}
			
			calculateBetweennesCentralities();
		}	
	}
	
	/**
	 * Checks if all entrepreneurs are offering and update the relevant flag
	  
	@Watch(watcheeClassName="EffectuationCausation.Entrepreneur",
			watcheeFieldNames="offering",whenToTrigger=WatcherTriggerSchedule.IMMEDIATE)
	public void checkAllEntrepreneursOffering() {
		for (Object o: context.getObjects(Entrepreneur.class)) {
			if (!((Entrepreneur)o).isOffering()) {
				allEntrepreneursOffering = false;
			}
			return;
		}
		allEntrepreneursOffering = true;
	}
	
	*/
	
}
