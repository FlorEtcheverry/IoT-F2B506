package ctrlpoint;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;


public class MyCtrlPoint extends ControlPoint implements NotifyListener, EventListener {
	
	private Device limsiSpeech = null;
	private String limsiSpeechUuid = null;
	
	public MyCtrlPoint() {		
		addNotifyListener(this);
		start();
	}

	@Override
	public void eventNotifyReceived(String uuid, long seq, String varName, String value) {
		System.out.println("Event from: uuid " + uuid);
		
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
}
