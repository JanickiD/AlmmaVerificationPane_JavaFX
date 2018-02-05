package application.model;

public class Zawodnik {
	
	private Integer id;
	private String name;
	private String lastName;
	private String weight;
	private String club;
	public Integer getId() {
		return id;
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
	public String getWeght() {
		return weight;
	}
	public void setWeght(String weght) {
		this.weight = weght;
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
	public Zawodnik() {
		super();
	}
	@Override
	public String toString() {
		return "Zawodnik [id=" + id + ", name=" + name + ", lastName=" + lastName + ", weght=" + weight + ", club="
				+ club + "]";
	}
	
	

}
