package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import conn.DBException;
import conn.OracleConnection;

public class ReadQuery {

	private Connection conn = null;
	private OracleConnection oracle = new OracleConnection();
	private String path = "src/queries/select.sql";
	private String query = "";
	private ResultSet rs = null;
	private Statement st = null;
	ExceptionLogs log = new ExceptionLogs();
	
	

	public ReadQuery() {

	}

	public String readQueryFile() {
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line;
			
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				query += line + " ";
			}
			
			return query;
			
		} catch (IOException e) {
			
			e.printStackTrace();
			log.errorLog(e);
			log.close();
			return null;
			
		}
	}

	public ResultSet querySelect(String query) throws SQLException {
		
		try {
			
			conn = oracle.dbConnect();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			return rs;
			
		} catch (SQLException e) {
			
			log.errorLog(e);
			log.close();
			throw new DBException(e.getMessage());
			
		}
	}
}