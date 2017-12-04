package de.hss.lego.lagersystem;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Remote {
	
	public Remote(InetSocketAddress remoteAddress) {
		this.remoteAdress = remoteAdress;
	}
	
	
	public InetSocketAddress getRemoteAdress() {
		return remoteAdress;
	}
	
	public boolean connect() {
		try {
			Future<String> resp = sendCommand("connect");
			return resp.get() != null;
		} catch (Exception e) {
			System.err.println(e.toString());
			return false;
		}
	}
	
	public void start(InetSocketAddress serverAddr) throws IOException {
		sendCommand("start", Utils.addressToString(serverAddr));
	}
	
	private static Executor executor = Executors.newCachedThreadPool();
	
	private InetSocketAddress remoteAdress;
	
	public FutureTask<String> sendCommand(String command) throws IOException {
		FutureTask<String> res = new FutureTask(new Command(this.remoteAdress, command, null));
		executor.execute(res);
		return res;
	}

	public FutureTask<String> sendCommand(String command, String body) throws IOException {
		FutureTask<String> res = new FutureTask(new Command(this.remoteAdress, command, body));
		executor.execute(res);
		return res;
	}
	
	public static FutureTask<String> sendCommand(InetSocketAddress addr, String command, String body) throws IOException {
		FutureTask<String> res = new FutureTask(new Command(addr, command, body));
		executor.execute(res);
		return res;
	}
	
	private static class Command implements Callable<String>{
		public Command(InetSocketAddress addr, String command, String body) {
			this.addr = addr;
			this.command = command;
			this.body = body;
		}
		private InetSocketAddress addr;
		private String command;
		private String body;
		
		@Override
		public String call() throws IOException, MalformedURLException {
			URL url = new URL("http:/" + addr.toString() + "/" + command);
			URLConnection connection = url.openConnection();
			if (body != null) {
				connection.setDoOutput(true); // Triggers POST.
				connection.setRequestProperty("Content-Type", "application/json");
				OutputStream output = connection.getOutputStream();
				output.write(body.getBytes());
				output.close();
			}
			InputStream response = connection.getInputStream();
			
			String res = Utils.readStream(response);
			response.close();
			return res;
		}
	}
}
