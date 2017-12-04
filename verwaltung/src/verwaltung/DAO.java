package verwaltung;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import com.mysql.jdbc.*;

public class DAO {
	
	private HashMap<Integer, Integer> places = new HashMap<Integer, Integer>();
	
	private java.sql.Connection connection;
	
	private InetSocketAddress host;
	
	public DAO() throws SQLException {
		/*
		Properties props = new Properties();
		props.put("user", "root");
		props.put("password", "");
		
		connection = DriverManager.getConnection("jdbc:mysql:/"+ host.toString() + "/", props);
		*/
	}
	
}
