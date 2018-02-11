package application.model;

public class Club {

	private String club;

	public Club() {
		super();
	}

	public Club(String club) {
		super();
		this.club = club;
	}

	public String getClub() {
		return club;
	}

	public void setClub(String club) {
		this.club = club;
	}
 
	@Override
	public String toString() {
		return club;
	}
	
	
	
}
