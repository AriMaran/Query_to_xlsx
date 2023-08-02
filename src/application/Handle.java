package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import exceptions.ExceptionLogs;
import services.ReadQuery;
import services.SelectColumns;
import services.SelectDate;

public class Handle {

	private Scanner sc;

	public Handle() {

		sc = new Scanner(System.in);

	}

	public void start() {

		ExceptionLogs log = new ExceptionLogs();

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			System.out.print("Enter the name of the output file: ");
			String outputFileName = reader.readLine();
			String outputPath = "src/results/" + outputFileName + ".xlsx";

			ReadQuery q = new ReadQuery();
			String query = q.readQueryFile();

			SelectDate dateSelector = new SelectDate();
			String startDate = dateSelector.getStartDateFromUser();
			String endDate = dateSelector.getEndDateFromUser();

			query = dateSelector.updateQueryWithDates(query, startDate, endDate);

			List<String> selectedColumns = null;

			SelectColumns choose = new SelectColumns(outputPath, selectedColumns);
			choose.writeFile(query);

			reader.close();

		} catch (IOException e) {
			
			System.err.println("ERROR");
			log.errorLog(e);
			e.printStackTrace();
			
		} finally {
			
			sc.close();
			log.close();
			
		}
	}
}
