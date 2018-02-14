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
	private static final String FREE_SLOT_SQL = "SELECT slotID FROM slot WHERE artikelID IS NULL;";
	private static final String ARTIKEL_SQL = "SELECT * FROM artikel";
	private static final String STORE_ARTIKEL_SQL = "UPDATE slot SET artikelID='%s' WHERE slotID='%s';";
	
	private HashMap<Integer, Integer> places = new HashMap<Integer, Integer>();
	
	private java.sql.Connection connection;
	
	private String hostName = "10.103.112.13";
	
	private java.sql.Statement statement;
	
	public static DAO instance = new DAO();
	
	private DAO() {
	}

	public void connect() throws SQLException{
		connection = DriverManager.getConnection("jdbc:mysql://" + hostName +"/" +
				""+ DB_NAME, USER, PASSWORD);
		statement = connection.createStatement();
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
	
	public String getFirstFreeSlotId() throws SQLException {
		ResultSet rs = statement.executeQuery(FREE_SLOT_SQL);
		ArrayList<String> list = new ArrayList<String>();
		while (rs.next()) {
			list.add(rs.getString(1));
		}
		rs.close();
		
		return list.get(0);
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
