package verwaltung;

import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.*;
import com.google.gson.*;

public class Server {
	
	static Gson gson = new Gson();
	
	static HttpServer httpServer;
	
	static Thread serverThread;
	
	static Detector detector;
	static Remote inputter;
	static Remote outputter;
	
	static InetSocketAddress ownAdress;

	public static void main(String[] args) throws IOException {
		System.out.println(String.join(", ", args));
		String interfaceName = args.length == 0 ? "wlan0" : args[0];
		
		httpServer = HttpServer.create( new InetSocketAddress(8000), 0);
		
		httpServer.createContext("/connect", new ConnectHandler());
		httpServer.createContext("/start", new StartHandler());
		httpServer.createContext("/detect", new DetectHandler());
		
		System.out.println("Starting http server thread.");
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface ni = interfaces.nextElement();
			Enumeration<InetAddress> addresses = ni.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress addr = addresses.nextElement();
				if (addr.getClass() == Inet4Address.class && interfaceName.equals(ni.getName())) {
					
					ownAdress = new InetSocketAddress(addr, 8000);
					System.out.println("Own adress: " + ownAdress);
				}
			}
		}
		serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				httpServer.start();
			};
		});
		serverThread.start();
	}
	
	static class ConnectHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
			t.sendResponseHeaders(204, -1);
			System.out.println("Connect: " + t.getRemoteAddress());
		}
	}
	
	static class StartHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
			String json = Utils.readStream(t.getRequestBody());
			String[] robotConnections = gson.fromJson(json, String[].class);
			System.out.println(String.join(", ", robotConnections));
			InetSocketAddress[] addrs = new InetSocketAddress[3];
			
			boolean ok = true;
			for (int i=0; i<addrs.length; i++) {
				try {
					String con = robotConnections[i];
					addrs[i] = Utils.parseAddress(con);
					
					Remote.sendCommand(addrs[1], "connect", null);
					System.out.println(con + ": " + "OK");
				} catch (Exception ex) {
					System.err.println(ex.toString());
					ok = false;
				}
				
			}
			if (ok) {
				detector = new Detector(addrs[0]);
				inputter = new Remote(addrs[1]);
				outputter = new Remote(addrs[2]);
				System.out.println("Connections stored");
			}
			
			detector.start(ownAdress);
			

			t.sendResponseHeaders(204, -1);
		}
	}
	
	static class DetectHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
			System.out.println("Detected");
			t.sendResponseHeaders(204, -1);
			detector.unlock();
		}
	}
	
}
