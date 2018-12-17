package storichain;


import repast.simphony.random.RandomHelper;

public class Goal {
	private Means requiredMeans;
	private int[] productVector;
	
	public Goal() {
		productVector = new int[Parameters.vectorSpaceSize];
		
		generateRandomProductVector();
	}
	
	public void generateRandomProductVector() {
		for (int i = 0; i < productVector.length; i++) {
			productVector[i] = RandomHelper.nextIntFromTo(0, 1);
		}
	}
	
	public int[] getProductVector() {
		return productVector;
	}

	public Means getRequiredMeans() {

		return requiredMeans;
	}


	public void setRequiredMeans(Means requiredMeans) {

		this.requiredMeans = requiredMeans;
	}

	

	public void generateRequiredMeans() {
	
	}
	
}
