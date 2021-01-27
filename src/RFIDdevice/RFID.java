package RFIDdevice;


public class RFID {
	private String userId;
	private boolean boxState;
	private static RFID rfid;

	public RFID() {
		this.userId = "";
		this.boxState = false; //box not taken -- false; box taken -- true;
	}

	public final static RFID getInstance() {
		if (rfid == null)
	          rfid=new RFID();
		return rfid;
	}
	
	public void setState(String boxStr) {
		if (boxStr == "true")
			this.boxState = true;
		else
			this.boxState = false;
	}
	
	public void update(String userId) {
		this.userId = userId;
	}


}
