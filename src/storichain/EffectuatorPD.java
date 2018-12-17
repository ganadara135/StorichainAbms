package storichain;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

public class EffectuatorPD extends PD {

	private ArrayList<Commitment> commitmentList;
	
	public EffectuatorPD(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		commitmentList = new ArrayList<Commitment>();
		//generateGoal();
	}

}
