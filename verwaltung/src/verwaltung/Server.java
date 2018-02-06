package verwaltung;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.sun.net.httpserver.*;
import com.google.gson.*;
import com.mysql.jdbc.Util;

public class Server {
	
	static Gson gson = new Gson();
	
	static HttpServer httpServer;
	
	static Thread serverThread;
	
	static Detector detector;
	static Inputter inputter;
	static Remote outputter;
	
	static InetSocketAddress ownAdress;
	
	private static void setOwnInetAddress(String interfaceName) throws SocketException {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface ni = interfaces.nextElement();
			Enumeration<InetAddress> addresses = ni.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress addr = addresses.nextElement();
				if (addr.getClass() == Inet4Address.class && interfaceName.equals(ni.getName())) {
					
					ownAdress = new InetSocketAddress(addr, 8000);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		String interfaceName = args.length == 0 ? "wlan0" : args[0];
		setOwnInetAddress(interfaceName);
		System.out.println("Own adress: " + ownAdress);
		
		System.out.print("Starting http server thread... ");
		
		httpServer = HttpServer.create( new InetSocketAddress(8000), 0);
		
		httpServer.createContext("/connect", new ConnectHandler());
		httpServer.createContext("/start", new StartHandler());
		httpServer.createContext("/stored", new StoredHandler());
		httpServer.createContext("/detect", new DetectHandler());
		httpServer.createContext("/fetched", new FetchedHandler());
		httpServer.createContext("/getStock", new StockHandler());
		
		serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				httpServer.start();
			};
		});
		serverThread.start();

		System.out.println("DONE");
		
		try {
			System.out.println("Connect to database.");
			DAO.instance.connect();
		} catch (SQLException e) {
			e.printStackTrace();
			httpServer.stop(1);
		}
		
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
				inputter = new Inputter(addrs[1]);
				outputter = new Remote(addrs[2]);
				System.out.println("Connections stored");
			}
			
			try {
				detector.start(ownAdress);
				inputter.start(ownAdress);
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			t.sendResponseHeaders(204, -1);
		}
	}
	
	static class DetectHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
			System.out.println("Detected");
			t.sendResponseHeaders(204, -1);
			inputter.fetch();
		}
	}
	
	public static String artikelIdByColor(float[] color) {
		int index = 0;
		float max = Float.NEGATIVE_INFINITY;
		for (int i = 0; i<3; i++) {
			if (color[i] > max) {
				index = i;
				max = color[i];
			}
		}
		return Integer.toString(index);
	}
	
	static class FetchedHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
			InputStream is = t.getRequestBody();
			String colorString = Utils.readStream(is);
			System.out.println("Received color: " + colorString);
			float[] color = gson.fromJson(colorString, float[].class);
			String art = artikelIdByColor(color);
			System.out.println("Artikel id: " + art);
			is.close();
			String slotId = "";
			try {
				System.out.println("Storing wares...");
				slotId = DAO.instance.getFirstFreeSlotId();
				System.out.println("Slot id: " + slotId);
				DAO.instance.storeArtikel(slotId, art);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			inputter.store(Integer.parseInt(slotId));
			t.sendResponseHeaders(204, -1);
			//detector.unlock();
		}
	}
	
	static class StoredHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			try {
				t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
				System.out.println("Stored, inputter ready...");
				detector.unlock();
				t.sendResponseHeaders(204, -1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static class StockHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			try {
				t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
				System.out.println("get Stored wares");

				String bla = gson.toJson(DAO.instance.getSlots());
				
				t.sendResponseHeaders(200, bla.length());
				t.getResponseBody().write(bla.getBytes());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
