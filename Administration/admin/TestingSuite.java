package admin;

import java.net.MalformedURLException;

import org.imt.atlantique.sss.kms.connectors.ws.ExecutionException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.InterruptedException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.RequestInvocationException_Exception;
import org.jaatadia.soap_interaction.RequestManager;
import org.jaatadia.soap_interaction.RequestWrapper;

import data.SocialData;
import sss.atlantique.imt.kms.RequestResult;


public class TestingSuite {
	
	
	static void Main(String[] argv){
		try {
			TestingSuite t = new TestingSuite();
			t.test();
		} catch (MalformedURLException | ExecutionException_Exception | InterruptedException_Exception | RequestInvocationException_Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private SocialData sd;
	RequestManager reqM;
	
	public TestingSuite() throws MalformedURLException, ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception {
		
		reqM = new RequestManager("http://192.168.223.129:8080/CrudService/CrudWS?WSDL");
		
		RequestWrapper req = new RequestWrapper("IoTF2B506Project","getTwitterAccount").add("User","mark");
		RequestResult res = reqM.invokeRead(req);
		
		
	}
	
	public void test(){
		sd = new SocialData("mark");
		System.out.println(sd.getAccessToken());
		System.out.println(sd.getAccessTokenSecret());
		System.out.println(sd.getconsumerSecret());
		System.out.println(sd.getConsumerKey());
		
	}
}
