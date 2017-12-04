package de.hss.lego.lagersystem;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Utils {

	public static String readStream(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public static InetSocketAddress parseAddress(String s) throws NumberFormatException, UnknownHostException {
		String[] split = s.split(":");
		return new InetSocketAddress(InetAddress.getByName(split[0]), Integer.parseInt(split[1]));
	}
	
	public static String addressToString(InetSocketAddress addr) {
		return addr.toString().replace("/", "");
	}
}
