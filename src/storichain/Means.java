package storichain;

public class Means {
	
	private int[] knowHow;
	private double money;
	
	public Means() {
		
	}
	
	public int[] getKnowHow() {

		return knowHow;
	}


	public void setKnowHow(int[] knowHow) {

		this.knowHow = knowHow;
	}


	public double getMoney() {

		return money;
	}



	public void setMoney(double money) {

		this.money = money;
	}
	
	public void print() {
		System.out.print("Money: " + String.valueOf(money) + ", Know-How: ");
		for (int i = 0; i < knowHow.length; i++) {
			System.out.print(knowHow[i]);
		}
		System.out.println();
	}

}
