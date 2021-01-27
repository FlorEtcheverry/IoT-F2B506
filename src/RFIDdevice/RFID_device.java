package RFIDdevice;

import java.io.*;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.control.*;
import org.cybergarage.http.*;

public class RFID_device extends Device implements ActionListener  {

	private final static String DESCRIPTION_FILE_NAME = "description/description.xml";
	private final static String PRESENTATION_URI = "/presentation";
	
	
	private StateVariable rfidUserState;
	private StateVariable rfidBoxState;
	
	public RFID_device() throws InvalidDescriptionException
	{
		super(new File(DESCRIPTION_FILE_NAME));
		System.out.println("device start");
		
		Action setStateAction = getAction("setState");
		setStateAction.setActionListener(this);
		System.out.println("Set state action ready");
		
		ServiceList serviceList = getServiceList();
		
		rfidUserState = getStateVariable("rfidUserState");
		rfidBoxState = getStateVariable("rfidBoxState");
		setLeaseTime(60);
	}
	

	public boolean actionControlReceived(Action action)
	{
		String actionName = action.getName();
		
		if (actionName.equals("setState") == true) {
			Argument boxState = action.getArgument("boxState");
			String boxStr = boxState.getValue();
			Argument resultArg = action.getArgument("Result");
			RFID rfid = RFID.getInstance();
			rfid.setState(boxStr);
			rfidBoxState.setValue(boxStr);
			System.out.println("Set box state of RFID with userId : " + rfidUserState + " boxState : " + boxStr);
			resultArg.setValue("Successful change box state!");

			return true;
			
		}
		return false;
	}
	
	public void setUserId(String userId)
	{
		RFID rfid = RFID.getInstance();
		rfid.update(userId);
		if (rfidBoxState.getValue().equals("true"))
			rfidUserState.setValue(userId + "_0");
		else
			rfidUserState.setValue(userId + "_1");

		System.out.println("Get new user Id with value : " + userId);
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
