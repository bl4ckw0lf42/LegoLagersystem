package de.hss.lego.lagersystem;

import java.io.*;

public class ReadResource {
	
	public void init(){
		InputStream inputStream = this.getClass().getResourceAsStream("/" + "filename");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String oneRow;
	}

}
