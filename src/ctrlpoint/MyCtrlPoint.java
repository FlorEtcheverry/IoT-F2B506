package ctrlpoint;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;


public class MyCtrlPoint extends ControlPoint implements NotifyListener, EventListener {
	
	private Device limsiSpeech = null;
	private String limsiSpeechUuid = null;
	
	private Device rfidDev = null;
	//current user that we handle, all other user ids will be ignored
	private String currentId = "E3 A1 CC A5";
	//current box state, taken -- true; not taken -- false;
	private String currentBoxState = "false";
	
	public MyCtrlPoint() {		
		addNotifyListener(this);
		//add event listener in order to follow user state
		addEventListener(this);
		start();
	}

	@Override
	public void eventNotifyReceived(String uuid, long seq, String varName, String value) {
		System.out.println("Event from: uuid " + uuid);

		System.out.println("variable name : " + varName);
		System.out.println("value : " + value);
		
		//when we receive a user state change from RFID device
		if ((varName == "rfidUserState") && (value != "")) {
			//split to get only user id
			value = value.split("_")[0];
			System.out.println("value : " + value);
			//judge if this user is the user we want
			if (value.equals(currentId)) {
				System.out.println("Get the right user access box, try to change box state");
				//change local box state
				if (currentBoxState == "false")
					currentBoxState = "true";
				else
					currentBoxState = "false";
				//send an action control to RFID device to change the remote box state
				Device rfidDev = this.getDevice("IoT_G2 RFID Device");
				Action setBoxState = rfidDev.getAction("setState");
				System.out.println("get action : " + setBoxState.getName());
				setBoxState.getArgument("boxState").setValue(currentBoxState);
				//post the action control and get the result
				if (setBoxState.postControlAction() == true) {
					System.out.println("Action control post!");
					ArgumentList outArgList = setBoxState.getOutputArgumentList();
					int nOutArgs = outArgList.size();
					for (int n=0; n<nOutArgs; n++) {
						Argument outArg = outArgList.getArgument(n);
						String name = outArg.getName();
						String value1 = outArg.getValue();
						System.out.println(name + value1);
					}
				
				}
				//deal with errors
				else {
					UPnPStatus err = setBoxState.getStatus();
					System.out.println("Error Code = " + err.getCode());
					System.out.println("Error Desc = " + err.getDescription());
				}
			}
			else 
				//if not the correct user
				System.out.println("Not the correct user, do not take action!");
			
		}
		
		if (uuid.equals(limsiSpeechUuid)) {
			
		}
	}

	public void deviceNotifyReceived(SSDPPacket ssdpPacket) {
		String uuid = ssdpPacket.getUSN();
		String target = ssdpPacket.getNT();
		String subType = ssdpPacket.getNTS();
		String location = ssdpPacket.getLocation();		
		
		printDevices();
	}
	
	public void printDevices() {
		DeviceList rootDevList = getDeviceList();
		int nRootDevs = rootDevList.size();
		for (int n = 0; n < nRootDevs; n++) {
			Device dev = rootDevList.getDevice(n);
			String devName = dev.getFriendlyName();
			//System.out.println("[" + n + "] = " + devName);	
			if (devName.equals("LIMSI Speech") && limsiSpeech == null) {				
				limsiSpeech = dev;
				
				limsiSpeechUuid = limsiSpeech.getSSDPPacket().getUSN();
				Service service = limsiSpeech.getService("urn:schemas-upnp-org:serviceId:1");
				
				if (service != null) {
					boolean res = subscribe(service);
					if (res) {
						System.out.println("Subscribed");
					} else {
						System.out.println("Subscribe Failed");
					}
				}			
				
				saySomething("LIMSI Speech est prêt.");
			}
			if (devName.equals("IoT_G2 RFID Device") && rfidDev == null) {				
				rfidDev = dev;
				
				Service rfidService = rfidDev.getService("urn:schemas-upnp-org:service:rfid_detection:1");
				
				if (rfidService != null) {
					boolean subRet = subscribe(rfidService);
					if (subRet)
						System.out.println("Successful subscribe with rfid service");
					else 
						System.out.print("Failed to subscribe with rfid service");
				}			
				
				saySomething("RFID is ready.");
			}
			//printServices(dev);
		}
	}
	
	public void printServices(Device dev) {
		ServiceList serviceList = dev.getServiceList();
		for (int n = 0; n < serviceList.size(); n++) {
			Service serv = (Service)serviceList.elementAt(n);
			System.out.println("\t(" + n + ") = " + serv.getServiceType());
			printActions(serv);
		}
	}
	
	public void printActions(Service serv) {
		ActionList actionList = serv.getActionList();
		for (int n = 0; n < actionList.size(); n++) {
			Action act = (Action)actionList.elementAt(n);
			System.out.println("\t\t{" + n + "} = " + act.getName());
			printArgs(act);
		}
	}
	
	public void printArgs(Action act) {
		ArgumentList argList = act.getArgumentList();
		for (int n = 0; n < argList.size(); n++) {
			Argument arg = (Argument)argList.elementAt(n);
			System.out.println("\t\t\t[[" + n + "]] = : " + arg.getName());
		}
	}
	
	public void saySomething(String text) {				
		Action speechAct = limsiSpeech.getAction("Speak");
		speechAct.setArgumentValue("Text", text);
		speechAct.setArgumentValue("Tag", "Speech");
		if (speechAct.postControlAction()) {
			System.out.println("Speaking");
			/*ArgumentList outArgList = speechAct.getOutputArgumentList();
			for (int n = 0; n < outArgList.size(); n++) {
				Argument outArg = outArgList.getArgument(n);
				String name = outArg.getName();
				String value = outArg.getValue();
			}*/
		}
	}
	
	public boolean isLimsiSpeechready() {
		return (limsiSpeech != null);
	}
	
	public boolean isRFIDready() {
		return (rfidDev != null);
	}
}
