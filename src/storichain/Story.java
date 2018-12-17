package storichain;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

public class Story extends Agent{
	
	protected double tomato;
	protected double potato;
	public String title;
	
	
	public Story(Context<Object> context, Network<Object> network, String label) {

		super(context, network, label);	
	}

	String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
