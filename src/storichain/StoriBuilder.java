package storichain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

public class StoriBuilder implements ContextBuilder<Object> {

	public static Context<Object> context;
	public static Network<Object> network;
	public static Network<Object> effectuationNetwork;	
	private static EntreNetworkGenerator networkGenerator;
	public static boolean allEntrepreneursOffering;
	public static ArrayList<ST> stes;
	public static ArrayList<RD> rdes;
	public static ArrayList<PD> pdes;
	
	public static EffectuatorST effectuatorST;
	public static EffectuatorRD effectuatorRD;
	public static EffectuatorPD effectuatorPD;
	private static HashMap<String, Integer> lastIds;
	public static int staticDemandSteps;

	
	@Override
	public Context build(Context<Object> context) {
		Parameters.initialize();
		
		stes = new ArrayList<ST>();
	//	oldDemand = new ArrayList<int[]>();
		rdes = new ArrayList<RD>();
		pdes = new ArrayList<PD>();
		staticDemandSteps = 0;
			
		context.setId("storichain");
		
		StoriBuilder.context = context;
		
		buildNetworks();
		
		effectuatorST = new EffectuatorST(context, network, "StoryTeller");
		effectuatorRD = new EffectuatorRD(context, network, "Reader");
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
		networkGenerator.setTotalRD(Parameters.numberOfRD);
		
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
	public static void getSTAcquiantances(Object n, int depth, List<ST> customers) {		
		for (Object o: network.getAdjacent(n)) {
			if (o instanceof ST) {
				customers.add((ST)o);
			}
			if (depth > 1) {
				getSTAcquiantances(o, depth-1, customers);
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
	 *  Initialize customer demand vectors using the define "Market split", i.e
	 *  the percentage of customers that "like" a certain product element
	 */
	private void initializeDemandVectors() {
		ArrayList<ST> demandST = new ArrayList<ST>();
		
		for (Object c: context.getObjects(ST.class)) {
			demandST.add((ST)c);
		}
		
		int shouldLikeProductElement = (int)Math.ceil(((double)Parameters.marketSplit / 100) * demandST.size());
		
		for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
			ArrayList<ST> copy = new ArrayList<ST>(demandST);
			
			for (int j = 0; j < shouldLikeProductElement; j++) {
				ST c = copy.remove(RandomHelper.nextIntFromTo(0, copy.size() - 1));
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
		ArrayList<ST> st = new ArrayList<ST>();
		ArrayList<PD> pd = new ArrayList<PD>();
		ArrayList<RD> rd = new ArrayList<RD>();
		
		for (Object c: context.getObjects(ST.class)) {
			st.add((ST)c);
		}
		
		ScheduleParameters parameters = ScheduleParameters.createRepeating(1, 6 - Parameters.adaptationSpeed, 1);		
		//schedule.scheduleIterable(parameters, st, "adaptProductVector", true);
		schedule.scheduleIterable(parameters, st, "adaptST", true);
		
		for (Object c: context.getObjects(PD.class)) {
			pd.add((PD)c);
		}
		
		ScheduleParameters parameters1 = ScheduleParameters.createRepeating(1, 6 - Parameters.adaptationSpeed, 1);		
		schedule.scheduleIterable(parameters1, pd, "adaptPD", true);
		
		for (Object c: context.getObjects(RD.class)) {
			rd.add((RD)c);
		}
		
		ScheduleParameters parameters2 = ScheduleParameters.createRepeating(1, 6 - Parameters.adaptationSpeed, 1);		
		schedule.scheduleIterable(parameters2, rd, "adaptRD", true);		
	}
}
