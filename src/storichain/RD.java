package storichain;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

public class RD extends Effectuator {
	
	private ArrayList<Commitment> commitmentList;

	public RD(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		// TODO Auto-generated constructor stub
	}
	
	public boolean doReaction() {
		return false;
	}

}
