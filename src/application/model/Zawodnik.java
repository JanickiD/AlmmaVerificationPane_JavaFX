package application.model;

public class Zawodnik {
	
	private Integer id;
	private String name;
	private String lastName;
	private String weight;
	private String club;
	private String verified;
	
	public Integer getId() {
		return id;
	}
	public String getVerified() {
		return verified;
	}
	public void setVerified(String verified) {
		this.verified = verified;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getClub() {
		return club;
	}
	public void setClub(String club) {
		this.club = club;
	}
	
	public Zawodnik(Integer id, String name, String lastName, String weight, String club) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.weight = weight;
		this.club = club;
	}
	
	
	public Zawodnik(Integer id, String name, String lastName, String weight, String club, String verified) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.weight = weight;
		this.club = club;
		this.verified = verified;
	}
	public Zawodnik() {
		super();
	}
	@Override
	public String toString() {
		return "Zawodnik [" + (id != null ? "id=" + id + ", " : "") + (name != null ? "name=" + name + ", " : "")
				+ (lastName != null ? "lastName=" + lastName + ", " : "")
				+ (weight != null ? "weight=" + weight + ", " : "") + (club != null ? "club=" + club + ", " : "")
				+ (verified != null ? "verified=" + verified : "") + "]";
	}
	
	

}
