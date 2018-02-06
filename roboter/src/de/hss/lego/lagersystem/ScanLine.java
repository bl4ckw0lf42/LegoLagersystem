package de.hss.lego.lagersystem;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;




public class ScanLine {

	// Motoren
	EV3LargeRegulatedMotor motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
	EV3LargeRegulatedMotor motorRight = new EV3LargeRegulatedMotor(MotorPort.C);
	EV3LargeRegulatedMotor motorCenter = new EV3LargeRegulatedMotor(MotorPort.B);

	// Touchsensor
	EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);

	// Farbsensor
	EV3ColorSensor colorSensorDrive = new EV3ColorSensor(SensorPort.S3);
	EV3ColorSensor colorSensorSort = new EV3ColorSensor(SensorPort.S2);
	// Samples
	float[] touchSensorArray;
	float[] colorSensorDriveArray;
	float[] colorSensorSortArray;
	int counterRed = 0;
	int counterBlue = 0;
	// Threads
	Thread stopThread;
	Thread readRGB;

	
	public static void main(String[] args) throws Exception 
	{
	HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
	        server.createContext("/", new StartPageHandler());
	        server.createContext("/start", new StartCommandHandler());
	        server.setExecutor(null); // creates a default executor
	        server.start();
	}
	static class StartPageHandler implements HttpHandler {
	        @Override
	        public void handle(HttpExchange t) throws IOException {
	            String response = "<html><button onclick=\" start()\"> Start</button>"
	            + "<script>function start() {fetch(\"/start\");}; "
	            + "</script></body></html>";
	            t.sendResponseHeaders(200, response.length());
	            OutputStream os = t.getResponseBody();
	            os.write(response.getBytes());
	            os.close();
	        }
	    }
	static class StartCommandHandler implements HttpHandler {
	        @Override
	        public void handle(HttpExchange t) throws IOException {
	            String response = "Start ok "; // Hier muss das Array und die gefahrene Strecke rein
	            t.sendResponseHeaders(200, response.length());
	            OutputStream os = t.getResponseBody();
	            os.write(response.getBytes());
	            os.close();
	            
	            new ScanLine().init();
	            // roboter.start();
	        }
	    }


	private void init()
	{
	LCD.drawString("Los gehts!", 0, 0);

	colorSensorDrive.setCurrentMode("RGB");
	colorSensorSort.setCurrentMode("RGB");
	touchSensor.setCurrentMode("Touch");

	touchSensorArray = new float[touchSensor.sampleSize()];
	colorSensorDriveArray = new float[colorSensorDrive.sampleSize()];
	colorSensorSortArray = new float[colorSensorSort.sampleSize()];

	stopThread = new Thread(new Runnable()
	{
	@Override
	public void run()
	{
	Button.waitForAnyPress();
	Sound.beepSequenceUp();
	System.exit(-1);
	}
	});

	readRGB = new Thread(new Runnable()
	{
	@Override
	public void run()
	{
	while (true)
	{
	// motorLeft.forward();
	// motorRight.forward();
	// Umrechnung von Raddistanz in zurückgelegte Distanz
	// Füllen des Distanzattributes 

	touchSensor.fetchSample(touchSensorArray, 0);
	if (touchSensorArray[0] != 1.0f)
	{
	colorSensorDrive.fetchSample(colorSensorDriveArray, 0);
	double rDrive =  colorSensorDriveArray[0]; //HIER gehts weiter
	double gDrive =  colorSensorDriveArray[1]; //HIER gehts weiter
	double bDrive =  colorSensorDriveArray[2]; //HIER gehts weiter
	double roundRDrive = Math.round(rDrive); //HIER gehts weiter
	double roundGDrive = Math.round(gDrive); //HIER gehts weiter
	double roundBDrive = Math.round(bDrive); //HIER gehts weiter
	LCD.drawString("Rot: " + roundRDrive, 0, 2);
	LCD.drawString("Grün: " + roundGDrive, 1, 3);
	LCD.drawString("Blau: " + roundBDrive, 2, 4);
	LCD.drawString("CountRED: " + counterRed, 0, 5);
	LCD.drawString("CountBLUE: " + counterBlue, 0, 6);
	if (rDrive > 0.1 && gDrive < 0.1 && bDrive < 0.1) //RED
	{
	//zaehlen
	if (counterRed == 7){
	Sound.beepSequenceUp();
	Sound.beepSequenceUp();
	Sound.beepSequenceUp();
	}
	if (counterRed == 8){
	motorLeft.stop();
	motorRight.stop();
	motorLeft.close();
	motorRight.close();
	}
	counterRed++;
	motorLeft.setSpeed(0);
	motorRight.setSpeed(0);
	Delay.msDelay(2000);
	// scannen
	/*
	colorSensorSort.fetchSample(colorSensorSortArray, 0);
	float rSort =  colorSensorSortArray[0];
	float gSort =  colorSensorSortArray[1];
	float bSort =  colorSensorSortArray[2];
	*/
	motorLeft.setSpeed(80);
	motorRight.setSpeed(80);
	motorLeft.backward();
	motorRight.backward();
	Sound.beepSequenceUp();
	Delay.msDelay(333);
	Sound.beepSequenceUp();
	Delay.msDelay(333);
	Sound.beepSequenceUp();
	Delay.msDelay(333);
	// drehen
	if (counterRed == 7){
	motorLeft.setSpeed(210);
	motorRight.setSpeed(210);
	motorLeft.forward();
	motorRight.backward();
	Delay.msDelay(1600);
	}
	else
	{
	motorLeft.setSpeed(210);
	motorRight.setSpeed(210);
	motorLeft.forward();
	motorRight.backward();
	Delay.msDelay(1400);
	} 
	}
	if (rDrive < 0.1 && gDrive < 0.1 && bDrive < 0.1) //BLACK
	{
	motorLeft.setSpeed(160);
	motorRight.setSpeed(20);
	motorLeft.forward();
	motorRight.forward();
	if (counterRed == 7){
	motorLeft.setSpeed(350);
	motorRight.setSpeed(300);
	}
	}
	if (rDrive > 0.1 && gDrive > 0.1 && bDrive > 0.1) //WHITE
	{
	motorLeft.setSpeed(20);
	motorRight.setSpeed(160);
	motorLeft.forward();
	motorRight.forward();
	if (counterRed == 7){
	motorLeft.setSpeed(300);
	motorRight.setSpeed(350);
	}
	}
	if (rDrive < 0.1 && gDrive < 0.1 && bDrive > 0.1) //BLUE
	{
	counterBlue++; 
	}
	}
	}
	}
	});

	readRGB.start();
	stopThread.start();
	}

	public String getRGBValue()
	{
	String retVal = new String();
	return retVal;
	}
}
