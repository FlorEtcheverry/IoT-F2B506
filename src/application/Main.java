package application;

import java.net.MalformedURLException;

import org.imt.atlantique.sss.kms.connectors.ws.ExecutionException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.InterruptedException_Exception;
import org.imt.atlantique.sss.kms.connectors.ws.RequestInvocationException_Exception;

import ctrlpoint.MyCtrlPoint;

public class Main {

	public static void main(String[] args) throws MalformedURLException, ExecutionException_Exception, InterruptedException_Exception, RequestInvocationException_Exception {
		MyCtrlPoint ctrlPoint = new MyCtrlPoint();
	}

}
