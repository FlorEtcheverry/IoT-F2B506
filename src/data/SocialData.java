package data;

import java.net.MalformedURLException;

import org.imt.atlantique.sss.kms.connectors.ws.ExecutionException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.InterruptedException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.RequestInvocationException_Exception;
import org.jaatadia.soap_interaction.RequestManager;
import org.jaatadia.soap_interaction.RequestWrapper;

import sss.atlantique.imt.kms.RequestResult;

public class SocialData {
	
	String consumerKey;
	String consumerSecret;
	String accessToken;
	String accesTokenSecret;
	RequestManager reqM;
	
	public SocialData(String name) {
		try {
			
			reqM = new RequestManager("http://192.168.223.129:8080/CrudService/CrudWS?WSDL");
			
			
			RequestWrapper req = new RequestWrapper("IoTF2B506Project","getTwitterAccount")
					.add("User",name);
			RequestResult res = reqM.invokeRead(req);
			consumerKey = RequestManager.getField(res, "consumerKey", 0);
			consumerSecret = RequestManager.getField(res, "consumerSecret", 0);
			accessToken = RequestManager.getField(res, "accessToken", 0);
			accesTokenSecret = RequestManager.getField(res, "accesTokenSecret", 0);
			
		} catch (MalformedURLException | ExecutionException_Exception | InterruptedException_Exception | RequestInvocationException_Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public String getConsumerKey() {
		return consumerKey;
	}
	public String getconsumerSecret() {
		return consumerSecret;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public String getAccessTokenSecret() {
		return accesTokenSecret;
	}
}
