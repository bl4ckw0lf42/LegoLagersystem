package verwaltung;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Detector extends Remote{

	public Detector(InetSocketAddress addr) {
		super(addr);
	}
	
	public void unlock() throws IOException {
		sendCommand("unlock");
	}
}
