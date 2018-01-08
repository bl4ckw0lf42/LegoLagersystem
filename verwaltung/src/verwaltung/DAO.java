package verwaltung;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.mysql.jdbc.*;

public class DAO {
	
	private static final String DB_NAME = "legolagersystem";
	private static final String USER = "lego";
	private static final String PASSWORD = "";
	
	private static final String SLOT_SQL = "SELECT s.slotID, a.beschreibung FROM slot AS s JOIN artikel AS a ON s.artikelID=a.artikelID;";
	private static final String ARTIKEL_SQL = "SELECT * FROM artikel";
	private static final String STORE_ARTIKEL_SQL = "UPDATE slot SET artikel_id='%s' WHERE id='%s';";
	
	private HashMap<Integer, Integer> places = new HashMap<Integer, Integer>();
	
	private java.sql.Connection connection;
	
	private String hostName = "10.103.112.11";
	
	private java.sql.Statement statement;
	
	public static DAO instance = new DAO();
	
	private DAO() {
	}

	public void connect(){
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + hostName +"/" +
					""+ DB_NAME, USER, PASSWORD);
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> getSlots() {
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			ResultSet rs = statement.executeQuery(SLOT_SQL);
			while (rs.next()) {
				result.put(rs.getString(1), rs.getString(2));
			}
			System.out.println(result);
			if (rs != null) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public HashMap<String, String> getArtikels() {
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			ResultSet rs = statement.executeQuery(ARTIKEL_SQL);
			while (rs.next()) {
				result.put(rs.getString(1), rs.getString(2));
			}
			System.out.println(result);
			if (rs != null) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void storeArtikel(String slotId, String artikelId) {
		try {
			String sql = String.format(STORE_ARTIKEL_SQL, artikelId, slotId);
			System.out.println(sql);
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void disconnect(){
		try{
			if(statement != null) statement.close();
			if(connection != null) connection.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
}
