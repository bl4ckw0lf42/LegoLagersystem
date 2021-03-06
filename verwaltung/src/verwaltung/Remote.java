package verwaltung;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Remote {
	
	public Remote(InetSocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	
	
	public InetSocketAddress getRemoteAdress() {
		return remoteAddress;
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
	
	public void start(InetSocketAddress serverAddr) throws IOException, ExecutionException {
		Future<String> resp = sendCommand("start", Utils.addressToString(serverAddr));
		try {
			resp.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Executor executor = Executors.newCachedThreadPool();
	
	private InetSocketAddress remoteAddress;
	
	public FutureTask<String> sendCommand(String command) throws IOException {
		FutureTask<String> res = new FutureTask(new Command(this.remoteAddress, command, null));
		executor.execute(res);
		return res;
	}

	public FutureTask<String> sendCommand(String command, String body) throws IOException {
		FutureTask<String> res = new FutureTask(new Command(this.remoteAddress, command, body));
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
