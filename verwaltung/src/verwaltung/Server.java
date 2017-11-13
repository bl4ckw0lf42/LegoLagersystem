package verwaltung;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;
import com.google.gson.*;

public class Server {
	
	static HttpServer httpServer;
	
	static Thread serverThread;

	public static void main(String[] args) throws IOException {
		httpServer = HttpServer.create( new InetSocketAddress(8000), 0);
		
		httpServer.createContext("/connect");
		
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
			t.sendResponseHeaders(204, 0);
			System.out.println("Connect: " + t.getRemoteAddress());
		}
	}
	
	static class StartHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			
		}
	}

}
