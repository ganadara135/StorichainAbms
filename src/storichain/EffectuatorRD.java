package storichain;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

public class EffectuatorRD extends RD {

	private ArrayList<Commitment> commitmentList;
	
	public EffectuatorRD(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		commitmentList = new ArrayList<Commitment>();
		//generateGoal();
	}

}
