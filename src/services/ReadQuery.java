package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import conn.OracleConnection;
import exceptions.DBException;
import exceptions.ExceptionLogs;

public class ReadQuery {

	private Connection conn = null;
	ExceptionLogs log = new ExceptionLogs();
	private OracleConnection oracle = new OracleConnection();
	private String path = "src/queries/select.sql";
	private String query = "";
	private ResultSet rs = null;
	private Statement st = null;

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
			return null;

		} finally {

			log.close();
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
			throw new DBException(e.getMessage());

		} finally {

			log.close();

		}
	}
}