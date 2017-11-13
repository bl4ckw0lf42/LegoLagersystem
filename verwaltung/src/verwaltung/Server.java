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
	
	static InetSocketAddress detectConnection;
	static InetSocketAddress storeConnection;
	static InetSocketAddress outConnection;

	public static void main(String[] args) throws IOException {
		httpServer = HttpServer.create( new InetSocketAddress(8000), 0);
		
		httpServer.createContext("/connect", new ConnectHandler());
		httpServer.createContext("/start", new StartHandler());
		
		System.out.println("Starting http server thread.");
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
			t.sendResponseHeaders(204, 0);
			System.out.println("Connect: " + t.getRemoteAddress());
		}
	}
	
	static class StartHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			String json = readStream(t.getRequestBody());
			String[] robotConnections= gson.fromJson(json, String[].class);
			System.out.println(String.join(", ", robotConnections));
			for (String con : robotConnections) {
				try {
					sendCommand(parseAddress(con), "connect");
					System.out.println(con + ": " + "OK");
				} catch (Exception ex) {
					System.err.println(ex.toString());
				}
				
			}
			detectConnection = parseAddress(robotConnections[0]);
			storeConnection = parseAddress(robotConnections[1]);
			outConnection = parseAddress(robotConnections[2]);

			t.sendResponseHeaders(204, 0);
		}
	}
	
	
	
	static String readStream(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	static InetSocketAddress parseAddress(String s) throws NumberFormatException, UnknownHostException {
		String[] split = s.split(":");
		return new InetSocketAddress(InetAddress.getByName(split[0]), Integer.parseInt(split[1]));
	}
	
	static String sendCommand(InetSocketAddress addr, String command) throws IOException {
		URL url = new URL("http:/" + addr.toString() + "/" + command);
		URLConnection connection = url.openConnection();
		InputStream response = connection.getInputStream();
		String res =  readStream(response);
		response.close();
		return res;
	}
	
	static String sendCommand(InetSocketAddress addr, String command, String body) throws IOException {
		URL url = new URL("http:/" + addr.toString() + "/" + command);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true); // Triggers POST.
		connection.setRequestProperty("Content-Type", "application/json");

		OutputStream output = connection.getOutputStream();
		output.write(body.getBytes());
		output.close();
		
		InputStream response = connection.getInputStream();
		
		String res = readStream(response);
		response.close();
		return res;
		
	}
	
}
