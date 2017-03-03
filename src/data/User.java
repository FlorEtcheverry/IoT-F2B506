package data;

public class User {

	private int points;
	private String name;
	private String prescription;
	
	public User(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public String getPrescription() {
		return this.prescription;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}
	
}
