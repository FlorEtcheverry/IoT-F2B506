package admin;


import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import org.imt.atlantique.sss.kms.connectors.ws.ExecutionException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.InterruptedException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.RequestInvocationException_Exception;
import org.jaatadia.soap_interaction.RequestManager;
import org.jaatadia.soap_interaction.RequestWrapper;
import org.jaatadia.soap_interaction.ResultWrapper;

import data.SocialData;


public class TestingSuite {
	
	public static void main(String[] args)
	{
		try {
			TestingSuite t = new TestingSuite();
			//t.init();
			//t.points();
			t.test();
		} catch (MalformedURLException | ExecutionException_Exception | InterruptedException_Exception | RequestInvocationException_Exception e) {
			e.printStackTrace();
		}
	}
	


	private SocialData sd;
	RequestManager reqM;
	
	public TestingSuite() throws MalformedURLException, ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception {
		
		reqM = new RequestManager("http://192.168.223.129:8080/CrudService/CrudWS?WSDL");
		
	}
	
	private void init() throws ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception {
		List<RequestWrapper> req = new LinkedList<RequestWrapper>();
		req.add(new RequestWrapper("IoTF2B506Project","createDrug").add("Drug","tafirol").add("Id", "E3 A1 CC A5"));
		req.add(new RequestWrapper("IoTF2B506Project","createDrug").add("Drug","salbutamol").add("Id", "44 5B 1E P8"));
		
		req.add(new RequestWrapper("IoTF2B506Project","createPerson").add("User","mark").add("Circle", "circle1"));
		req.add(new RequestWrapper("IoTF2B506Project","createTwitterAccount").add("User","mark")
				.add("consumerKey", "Mkk45UT7QhzPcoM60cig1v2eh")
				.add("consumerSecret", "T90sL0GDEHADIhQMY0cIHgWrMEpcJ6D7r7Oi6tbmEBp1fWIcfq")
				.add("accessToken", "836144014906769413-8EX98FPfTsthPT07vTZ2EsKuQnYxnLZ")
				.add("accesTokenSecret", "Ap7nTFF0WFmcnCXoFx5TtFmeLM49mdZUmNviDc9TCC20n")
		);
		
		req.add(new RequestWrapper("IoTF2B506Project","createPrescription").add("User","Mark").add("Prescription","pmark1").add("Drug", "tafirol").add("Day","Sun").add("Hour","22:00:00"));
		
		
		req.add(new RequestWrapper("IoTF2B506Project","createRound").add("User","Mark").add("Day","Sun"));
		
		System.out.println("Initialsing");
		for (RequestWrapper r : req){
			System.out.println(r.toString());
			reqM.invokeCreate(r);
		}
		
	}
	

	private void points() throws ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception {
		List<RequestWrapper> req = new LinkedList<RequestWrapper>();
		req.add(new RequestWrapper("IoTF2B506Project","setPoints").add("User","Mark").add("Points", "1000").add("Reason","BonusPoint"));
		req.add(new RequestWrapper("IoTF2B506Project","setPoints").add("User","Mark").add("Points", "500").add("Reason","BonusPoint"));
		
		for (RequestWrapper r : req){
			System.out.println(r.toString());
			reqM.invokeCreate(r);
		}
	}
	
	public void test() throws ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception{
		System.out.println("Testing");
		sd = new SocialData("mark");
		System.out.println(sd.getAccessToken());
		System.out.println(sd.getAccessTokenSecret());
		System.out.println(sd.getconsumerSecret());
		System.out.println(sd.getConsumerKey());
		
		List<RequestWrapper> req = new LinkedList<RequestWrapper>();
		req.add(new RequestWrapper("IoTF2B506Project","getPrescriptions").add("User","Mark"));
		req.add(new RequestWrapper("IoTF2B506Project","getPoints").add("User","Mark"));
		
		for (RequestWrapper r : req){
			System.out.println(r.toString());
			ResultWrapper res = reqM.invokeRead(r);
			System.out.println(res.toString());
		}
		
	}


}
