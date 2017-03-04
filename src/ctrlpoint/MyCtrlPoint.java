package ctrlpoint;

import application.*;
import data.SocialData;
import data.User;
import sss.atlantique.imt.kms.RequestInvocation;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.imt.atlantique.sss.kms.connectors.ws.ExecutionException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.InterruptedException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.RequestInvocationException_Exception;
import org.jaatadia.soap_interaction.RequestManager;
import org.jaatadia.soap_interaction.RequestWrapper;
import org.jaatadia.soap_interaction.ResultWrapper;

import appcomponent.Social;


public class MyCtrlPoint extends ControlPoint implements NotifyListener, EventListener {
	
	private SocialData sd;
	private User user;
	private Social social;
	private RequestManager reqM;
	
	// LIMSI Speech device
	private Device limsiSpeech = null;	
	// RFID reader
	private Device rfidDev = null;
	// Current user that we handle, all other user ids will be ignored

	// Current box state, taken -- true; not taken -- false;
	private String currentBoxState = "false";
	
	public MyCtrlPoint() throws MalformedURLException, ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception {	
		
		sd = new SocialData("mark");
		user = new User("Mark");
		social = new Social();
	
		// Set Twitter account information
		social.setAuthValues(sd.getConsumerKey(), sd.getconsumerSecret(), sd.getAccessToken(),
				sd.getAccessTokenSecret());
		
		// Database requests
		setUserInformation();
		
		addNotifyListener(this);
		// Add event listener in order to follow states
		addEventListener(this);
		start();
		
		// Wait until devices are ready
		//while (!server.ctrlPoint.isLimsiSpeechready() || !server.ctrlPoint.isRFIDready()) {}

		// Announce prescription
		//server.ctrlPoint.saySomething(
		//		"Bonjour " + server.user.getName() + ", c'est l'heure de prendre votre " + server.user.getPrescription());
		System.out.println("Bonjour " + user.getName() + ", c'est l'heure de prendre votre " + user.getPrescription());
	}
	


	@Override
	public void eventNotifyReceived(String uuid, long seq, String varName, String value) {
		System.out.println("Event from: uuid " + uuid);
		System.out.println("variable name : " + varName);
		System.out.println("value : " + value);
		
		// When we receive a user state change from RFID device
		if ((varName == "rfidUserState") && (value != "")) {
			//split to get only user id
			value = value.split("_")[0];
			System.out.println("value : " + value);
			//judge if this user is the user we want
			if (value.equals(user.getPrescriptionId())) {
				System.out.println("Get the right user access box, try to change box state");
				//change local box state
				if (currentBoxState == "false") {
					currentBoxState = "true";
					boxTaken();
				}
				else {
					currentBoxState = "false";
					boxPutBack();
				}
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
			System.out.println("[" + n + "] = " + devName);	
			
			if (devName.equals("LIMSI Speech") && limsiSpeech == null) {				
				limsiSpeech = dev;	
				saySomething("LIMSI Speech is ready.");
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
	
	private void saySomething(String text) {				
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
	
	private boolean isLimsiSpeechready() {
		return (limsiSpeech != null);
	}
	
	private boolean isRFIDready() {
		return (rfidDev != null);
	}
	
	private void boxTaken(){
		System.out.println("La bonne boite a ete prise");	
		// ctrlPoint.saySomething("C'est la bonne boite, vous pouvez prendre
		// votre dose habituelle");
	}

	private void boxPutBack() {
		System.out.println("Tres bien vous avez gagne 1000 pts");
		// TODO: update database new score
		// Compute new score
		int newPoints = user.getPoints() + 1000;
		user.setPoints(newPoints);
		try {
			reqM.invokeCreate(new RequestWrapper("IoTF2B506Project","setPoints").add("User",user.getName()).add("Points", "1000").add("Reason","Medicament bien pris"));
		} catch (ExecutionException_Exception | InterruptedException_Exception
				| RequestInvocationException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ctrlPoint.saySomething("Tres bien " + user.getName() + ". Vous
				// avez gagne " + newPoints + " points. A demain !");
		social.sendMedicationTookOnTime(user.getPrescription(),newPoints); 		
	}
	
	private void setUserInformation() throws MalformedURLException, ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception {
		// List of SPARQL requests
		reqM = new RequestManager("http://192.168.223.129:8080/CrudService/CrudWS?WSDL");

		List<RequestWrapper> req = new LinkedList<RequestWrapper>();
		RequestInvocation req1 = new RequestWrapper("IoTF2B506Project", "getPrescriptions").add("User", "Mark");
		RequestInvocation req2 = new RequestWrapper("IoTF2B506Project", "getPoints").add("User", "Mark");
		// TODO: request to retrieve medication RFID
		//ctrlPoint.setMedicationRFID(medId);
		
		ResultWrapper res = reqM.invokeRead(req1);
		user.setPrescription(res.getField("drug", 0).split("#")[1]);
		user.setPrescriptionId(res.getField("id", 0));
		
		res = reqM.invokeRead(req2);
		int count=0;
		for (int i=0;i<res.getSize();i++){
			count += Integer.parseInt(res.getField("points", i));
		}
		
		user.setPoints(count);
		
		}
}
