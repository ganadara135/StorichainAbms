package storichain;


import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

public class StoriBuilder implements ContextBuilder<Object> {

	public static Context<Object> context;
	public static Network<Object> network;
	public static Network<Object> effectuationNetwork;	
	private static EntreNetworkGenerator networkGenerator;
	public static ST st;
	public static RD rd;
	public static PD pd;
	public static ArrayList<ST> stes;
	
	public static Effectuator effectuator;
	private static HashMap<String, Integer> lastIds;

	
	@Override
	public Context build(Context<Object> context) {
		Parameters.initialize();
		
		
		context.setId("storichain");
		
		StoriBuilder.context = context;
		
		buildNetworks();
		
		st = new ST(context, network, "StoryTeller");
		rd = new RD(context, network, "Reader");
		pd = new PD(context, network, "Producer");
		
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
		
		scheduleActions();
		
				
		return context;
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
