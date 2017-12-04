package verwaltung;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Inputter extends Remote {

	public Inputter(InetSocketAddress remoteAddress) {
		super(remoteAddress);
		
	}
	
	public void fetch() throws IOException {
		this.sendCommand("fetch");
	}
	
	public void store(int place) throws IOException {
		this.sendCommand("store", Integer.toString(place));
	}

}
