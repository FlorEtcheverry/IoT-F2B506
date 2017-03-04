package data;

public class User {

	private int points;
	private String name;
	private String prescription;
	private String prescriptionId;
	
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
	
	public void setPrescriptionId(String prescriptionid) {
		this.prescriptionId = prescriptionid;
	}

	public Object getPrescriptionId() {
		return prescriptionId;
	}
	
}
