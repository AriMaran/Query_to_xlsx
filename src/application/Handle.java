package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import exceptions.ExceptionLogs;
import services.ReadQuery;
import services.SelectColumns;
import services.Tablesheet;

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
            Tablesheet xl = new Tablesheet(outputPath);

            String query = q.readQueryFile();

            System.out.print("Deseja selecionar colunas específicas? (S/N): ");
            String selectColumnsOption = reader.readLine().toUpperCase();

            List<String> selectedColumns = null;

            if (selectColumnsOption.equals("S")) {
                // O usuário deseja selecionar colunas específicas
                selectedColumns = new ArrayList<>();
                System.out.println("Colunas: ");
                System.out.println("Digite o nome das colunas (digite 'FIM' para encerrar): ");
                while (true) {
                    String columnName = reader.readLine();
                    if (columnName.equalsIgnoreCase("FIM")) {
                        break;
                    }
                    selectedColumns.add(columnName);
                }
            }

            SelectColumns choose = new SelectColumns(outputPath, selectedColumns);
            choose.writeFile(query);

            reader.close();

        } catch (IOException e) {

            System.err.println("ERROR");
            log.errorLog(e);
            log.close();
            e.printStackTrace();
        }

    }
}
