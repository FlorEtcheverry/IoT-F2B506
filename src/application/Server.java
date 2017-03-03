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

	public static void main (String[] args) throws ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception, MalformedURLException {
		
		// Initialize UPnP control point for devices
		MyCtrlPoint ctrlPoint = new MyCtrlPoint();
		
		// Retrieve info on Mark
		SocialData sd = new SocialData("mark");
		User markUser = new User("Mark");
		Social social = new Social();
		
		// Set Twitter account information
		social.setAuthValues(sd.getConsumerKey(), sd.getconsumerSecret(), sd.getAccessToken(), 
				sd.getAccessTokenSecret());
		
		// List of SPARQL requests
		RequestManager reqM = new RequestManager("http://192.168.223.129:8080/CrudService/CrudWS?WSDL");
		
		List<RequestWrapper> req = new LinkedList<RequestWrapper>();
		req.add(new RequestWrapper("IoTF2B506Project","getPrescriptions").add("User","Mark"));
		req.add(new RequestWrapper("IoTF2B506Project","getPoints").add("User","Mark"));
		
		ResultWrapper res = reqM.invokeRead(req.get(0));
		markUser.setPrescription(res.toString());
		
		res = reqM.invokeRead(req.get(1));
		markUser.setPoints(Integer.parseInt(res.toString()));
		
		// Wait until LIMSI Speech is ready
		while (!ctrlPoint.isLimsiSpeechready()) {}
		
		// Announce prescription
		ctrlPoint.saySomething("Bonjour " + markUser.getName() + ", c'est l'heure de prendre votre " 
								+ markUser.getPrescription());
		
		// RFID event: box taken
		
		
		// Confirmation
		//ctrlPoint.saySomething("C’est la bonne boîte, vous pouvez prendre votre dose habituelle");
		
		// RFID event: box put back
		
		
		// Compute new score
		//int newPoints = markUser.getPoints() + 1000;
		//markUser.setPoints(newPoints);
		// TODO : Update database
		
		// Final announcement
		//ctrlPoint.saySomething("Très bien " + markUser.getName() + ". Vous avez gagné " + newPoints + " points. A demain !");
	
		// Send tweet
		//social.sendMedicationTookOnTime(markUser.getPrescription(), newPoints);
	}	
}
