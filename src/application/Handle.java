package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import exceptions.ExceptionLogs;
import services.ReadQuery;
import services.SelectColumns;

public class Handle {

    public Handle() {

    }

    public void start() {
    	
        ExceptionLogs log = new ExceptionLogs();

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Digite o nome do arquivo de saída: ");
            String outputFileName = reader.readLine();
            String outputPath = "src/results/" + outputFileName + ".xlsx";

            ReadQuery q = new ReadQuery();


            String query = q.readQueryFile();

            List<String> selectedColumns = null;

   
            SelectColumns choose = new SelectColumns(outputPath, selectedColumns);
            choose.writeFile(query);

            reader.close();

        } catch (IOException e) {

            System.err.println("ERROR");
            log.errorLog(e);
            e.printStackTrace();
            
        } finally {
        	
        	log.close();
        	
        }
    }
}
