package RFIDdevice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.Enumeration;

import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.util.Debug;

public class SerialRead implements SerialPortEventListener {
	
	SerialPort serialPort;

	private static final String portName = "COM3";
	
	private BufferedReader input;
	private OutputStream output;
	//Milliseconds to block while waiting for port open
	private static final int TIME_OUT = 2000;
	//Default bits per second for COM port
	private static final int DATA_RATE = 9600;
	
	private RFID_device rfid_device;

	public void initialize() {

        System.setProperty("gnu.io.rxtx.SerialPorts", "COM3");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().equals(portName)) {
				portId = currPortId;
				break;
			}
		}
		
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
		//create a rfid_device for this port
		try {
			rfid_device = new RFID_device();
			rfid_device.start();
		}
		catch (InvalidDescriptionException e) {
			Debug.warning(e);
		}
	}

	//close the port
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}


	//handle an event on serial port, print it and set device state
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				//System.out.println(inputLine);//when receive an event of card ID
				if (inputLine.startsWith("Card UID")) {
					rfid_device.setUserId(inputLine.split("Card UID: ")[1]);
					//set rfid device state and update
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes
	}
	
	public RFID_device getRFID_device() {
		return this.rfid_device;
	}
	
	public static void main(String[] args) {
		SerialRead main = new SerialRead();
		main.initialize();

		System.out.println("Started");

	}

}