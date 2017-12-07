package de.hss.lego.lagersystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

public class DetectorRobot {

	static HttpServer httpServer;
	static EV3UltrasonicSensor distanceSensor = new EV3UltrasonicSensor(SensorPort.S2);
	static boolean locked = true;
	static InetSocketAddress serverAddr;
	
	
	public static void main(String[] args) throws IOException {
		httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
		Thread httpThread = new Thread(new Runnable() {
			@Override
			public void run() {
				
		        httpServer.createContext("/start", new StartHandler());
		        httpServer.createContext("/connect", new ConnectHandler());
		        httpServer.createContext("/unlock", new UnlockHandler());
		        httpServer.start();
			}
		});
		httpThread.start();
		
		Thread detectThread = new Thread(new Runnable() {
			@Override
			public void run() {
				float[] uArr = new float[distanceSensor.sampleSize()];
				while (true) {
					if (!locked) {
						distanceSensor.fetchSample(uArr, 0);
						LCD.clear();
						LCD.drawString("Sample: " + uArr[0], 0, 0);
						if (uArr[0] < 0.050) {
							try {
								LCD.drawString("detect", 0, 1);
								URL url = new URL("http:/"+ serverAddr +"/detect");
								URLConnection con = url.openConnection();
								con.getInputStream().close();
								locked = true;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								LCD.drawString(e.getMessage(), 0, 2);
							}
						}
					}
					Delay.msDelay(250);
				}
			}
		});
		detectThread.start();
	}
	
	static class ConnectHandler implements HttpHandler {
		@Override
        public void handle(HttpExchange t) throws IOException {
			try {
				t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
	        	LCD.clear();
	        	LCD.drawString("Connect", 0, 0);
	        	t.sendResponseHeaders(204, -1);
			} catch(Exception e) {
				e.printStackTrace();
			}
        }
	}

	
	static class StartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
        	t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        	LCD.clear();
        	LCD.drawString("Starting", 0, 0);
        	InputStream is = t.getRequestBody();
        	String addrStr = Utils.readStream(is);
        	LCD.drawString("Server: " + addrStr, 0, 1);
        	serverAddr = Utils.parseAddress(addrStr);
            t.sendResponseHeaders(204, -1);
            locked = false;
        }
    }
	
	static class UnlockHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange t) throws IOException {
			t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
			LCD.clear();
        	LCD.drawString("Unlocked", 0, 0);
        	locked = false;
            t.sendResponseHeaders(204, -1);
		}
		
	}
}
