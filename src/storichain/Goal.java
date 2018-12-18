package storichain;



import repast.simphony.random.RandomHelper;

public class Goal {
	private Means requiredMeans;
	private int[] productVector;  // 이상적 상품(스토리)
	
	public Goal() {
		productVector = new int[Parameters.vectorSpaceSize];
		
		generateRandomProductVector();
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
		
		requiredMeans = new Means();
		double requiredMoney = 0;
		
		for (int i = 0; i < productVector.length; i++) {
			requiredMoney += StoriBuilder.productElementCost[i];
		}
		
		requiredMeans.setKnowHow(productVector);
		requiredMeans.setMoney(requiredMoney);
	}
	
	/**
	 * @param productVector the productVector to set
	 */
	public void setProductVector(int[] productVector) {
		this.productVector = productVector;
		this.generateRequiredMeans();
	}
	
	public String printProductVector() {
		String s = "";
		
		for (int i = 0; i < productVector.length; i++) {
			s += String.valueOf(productVector[i]);
		}
		
		return s;
	}
	
	public void generateRandomProductVector() {
		for (int i = 0; i < productVector.length; i++) {
			productVector[i] = RandomHelper.nextIntFromTo(0, 1);
		}
	}
	
}
