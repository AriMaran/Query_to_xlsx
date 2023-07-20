package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import services.ExceptionLogs;
import services.ReadQuery;
import services.Tablesheet;

public class Program {

	public static void main(String[] args) throws SQLException {
		
		ExceptionLogs log = new ExceptionLogs();

		try {
			
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("Digite o nome do arquivo de saída: ");
		String outputFileName = reader.readLine();
		String outputPath = "src/results/" + outputFileName + ".xlsx";

		ReadQuery q = new ReadQuery();

		Tablesheet xl = new Tablesheet(outputPath);

		String query = q.readQueryFile();
		xl.writeFile(query);

		reader.close();
		
		} catch (IOException e) {
			
			System.err.println("ERROR");
			log.errorLog(e);
			log.close();
			e.printStackTrace();
		}
	}
}