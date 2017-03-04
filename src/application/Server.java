package application;

import ctrlpoint.*;
import data.*;
import appcomponent.*;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import org.imt.atlantique.sss.kms.connectors.ws.ExecutionException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.InterruptedException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.RequestInvocationException_Exception;
import org.jaatadia.soap_interaction.RequestManager;
import org.jaatadia.soap_interaction.RequestWrapper;
import org.jaatadia.soap_interaction.ResultWrapper;

import admin.*;

public class Server {

	private SocialData sd;
	private User user;
	private Social social;
	private MyCtrlPoint ctrlPoint;
	private RequestManager reqM;

	public Server(MyCtrlPoint ctrlPoint) throws MalformedURLException, ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception {
		sd = new SocialData("mark");
		user = new User("Mark");
		social = new Social();
	
		// Set Twitter account information
		social.setAuthValues(sd.getConsumerKey(), sd.getconsumerSecret(), sd.getAccessToken(),
				sd.getAccessTokenSecret());
		
		// Initialize UPnP control point for devices
		this.ctrlPoint = ctrlPoint;
		
		// Database requests
		setUserInformation();
	}

	public static void main(String[] args) throws MalformedURLException, ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception {

		MyCtrlPoint ctrlPoint = new MyCtrlPoint();		
			
	}
	
	public void boxTaken(){
		System.out.println("La bonne boite a ete prise");	
		// ctrlPoint.saySomething("C'est la bonne boite, vous pouvez prendre
		// votre dose habituelle");
	}

	public void boxPutBack() {
		System.out.println("Tres bien vous avez gagne 1000 pts");
		// TODO: update database new score
		// Compute new score
		// int newPoints = markUser.getPoints() + 1000;
		// markUser.setPoints(newPoints);
		//ctrlPoint.saySomething("Tres bien " + user.getName() + ". Vous
				// avez gagne " + newPoints + " points. A demain !");
		//social.sendMedicationTookOnTime(markUser.getPrescription(),
				//newPoints); 		
	}
	
	private void setUserInformation() throws MalformedURLException, ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception {
		// List of SPARQL requests
		reqM = new RequestManager("http://192.168.223.129:8080/CrudService/CrudWS?WSDL");

		List<RequestWrapper> req = new LinkedList<RequestWrapper>();
		req.add(new RequestWrapper("IoTF2B506Project", "getPrescriptions").add("User", "Mark"));
		req.add(new RequestWrapper("IoTF2B506Project", "getPoints").add("User", "Mark"));
		// TODO: request to retrieve medication RFID
		//ctrlPoint.setMedicationRFID(medId);
		
		ResultWrapper res = reqM.invokeRead(req.get(0));
		user.setPrescription(res.toString());

		res = reqM.invokeRead(req.get(1));
		user.setPoints(Integer.parseInt(res.toString()));
	}
}
