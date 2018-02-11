package application.model;

public class Weight {

	private String weight;

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Weight(String weight) {
		super();
		this.weight = weight;
	}

	public Weight() {
		super();
	}

	@Override
	public String toString() {
		return weight;
	}

	
	
}
