package storichain;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

public class PD extends Effectuator {
	
	private ArrayList<Commitment> commitmentList;

	public PD(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		// TODO Auto-generated constructor stub
	}

	public boolean doStaking() {
		return false;
	}
}
