package application.model;

public class Division {

	private String division;

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public Division(String division) {
		super();
		this.division = division;
	}

	public Division() {
		super();
	}

	@Override
	public String toString() {
		return division;
	}
	
}
