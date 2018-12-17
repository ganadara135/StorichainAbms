package storichain;

import repast.simphony.context.Context;

public class RandomNetworkGenerator extends EntreNetworkGenerator {

	private double density;
	
	public RandomNetworkGenerator(Context<Object> context, double density) {
		super(context);
		
		this.density = density;
	}
	
	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}
}
