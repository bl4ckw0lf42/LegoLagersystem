package de.hss.lego.lagersystem;

import java.sql.*;
import java.util.*;
import com.mysql.jdbc.Driver;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class JDBCTest {

	
	public final static String USERNAME = "test";
	public final static String PASSWORD = "";
	public final static String DBMS = "mysql";
	public final static String HOST = "10.103.112.13:3306";
	
	
	public static void main(String[] args) throws SQLException {
		Connection con = getConnection();
		Delay.msDelay(4000);
	}
	
	public static Connection getConnection() throws SQLException {

	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", USERNAME);
	    connectionProps.put("password", PASSWORD);

	        conn = DriverManager.getConnection(
	                   "jdbc:" + DBMS + "://" +
	                   HOST + "/",
	                   connectionProps);
	    LCD.drawString("Connected", 0, 0);
	        //System.out.println("connected");
	    return conn;
	}

}
