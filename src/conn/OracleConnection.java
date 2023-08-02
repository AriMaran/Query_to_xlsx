package conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import exceptions.DBException;
import exceptions.ExceptionLogs;

public class OracleConnection {

	private Connection conn = null;
	ExceptionLogs log = new ExceptionLogs();
	private ResultSet rs = null;
	private Statement st = null;
	
	

	private PropertiesFunction login = new PropertiesFunction();

	private String dbURL = login.getUrl();
	private String dbUSR = login.getUsr();
	private String dbPWD = login.getPwd();

	public OracleConnection() {
		
		this.conn = dbConnect();
	}

	public Connection dbConnect() {

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection(dbURL, dbUSR, dbPWD);
			System.out.println("Connected"); // REVER LÓGICA. RETORNANDO CONNECTED MESMO COM ERRO
			return conn;

		} catch (SQLException e) {

			System.err.println("Connection failed");
			System.out.println(dbURL);
			e.printStackTrace();
			log.errorLog(e);
			log.close();
			throw new DBException(e.getMessage());

		} catch (ClassNotFoundException e) {

			System.err.println("JDBC Driver not found");
			log.errorLog(e);
			log.close();
			throw new DBException(e.getMessage());
			
			
		}

	}

	public void dbDisconnect() {

		try {

			if (conn != null) {
				conn.close();
				System.out.println("Disconnected");

			} else {

				System.out.println("Not connected");

			}

		} catch (SQLException e) {

			System.err.println("NOT CONECTED TO DATABASE");
			log.errorLog(e);
			log.close();
			throw new DBException(e.getMessage());
		}
	}

	public static void closeStatement(Statement st) {
		
		if (st != null) {

			try {

				st.close();
				
			} catch (SQLException e) {
				
				throw new DBException(e.getMessage());
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {
	
		if (rs != null) {

			try {

				rs.close();
				
			} catch (SQLException e) {

				throw new DBException(e.getMessage());
			}
		}
		
	}
}
