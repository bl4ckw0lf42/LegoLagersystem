package de.hss.lego.lagersystem;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import lejos.hardware.lcd.LCD;

public class WebTest {
	static HttpServer server;
	static InetSocketAddress SA;
	public static void main(String[] args) throws IOException {
		
		server = HttpServer.create(new InetSocketAddress(8000), 0);
		Thread httpThread = new Thread(new Runnable() {
			@Override
			public void run() {
				LCD.drawString("HTTP Server started...", 0, 0);
		        server.createContext("/start", new StartHandler());
		        server.createContext("/interface", new InterfaceHandler());
		        server.createContext("/connect", new ConnectionHandler());
		        server.createContext("/fetch", new FetchHandler());
		        server.start();
		        LCD.drawString("Server: " +  server.getAddress(), 1, 1);
			}
		});
		httpThread.start();
		

	}

	
	static class StartHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
        	LCD.clear();
        	LCD.drawString("Starting", 0, 0);
            t.sendResponseHeaders(204, -1);
            InputStream IS = t.getRequestBody();
            String IP = Utils.readStream(IS); 
            SA = Utils.parseAddress(IP);
            
            
     
        }
    }
	static class FetchHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
        	while(true){
        	LCD.clear();
        	LCD.drawString("Fetch", 0, 0);
            t.sendResponseHeaders(204, -1);
            Einlagern ev = new Einlagern();
            try {
				ev.init(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        }
    }
	
	static class InterfaceHandler implements HttpHandler {
		@Override
        public void handle(HttpExchange t) throws IOException {
        	
            String response = ""/*"<html>"
            		+ "<head></head>"
            		+ "<body>"
            		+ "<button onclick=\"start()\">Start</button>"
            		+ "<script>"
            		+ "function start() {fetch(\"/start\");};"
            		+ "</script>"
            		+ "</body>"
            		+ "</html>*/;
            t.sendResponseHeaders(204,0);
        }
		
	}
	
	static class ConnectionHandler implements HttpHandler {
		@Override
        public void handle(HttpExchange t) throws IOException {
        	
			t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
			t.getResponseHeaders().set("Content-Type", "text/plain");
            t.sendResponseHeaders(204,-1);
        }
	}
}
