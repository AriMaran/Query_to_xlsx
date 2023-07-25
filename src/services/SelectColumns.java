package services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import exceptions.DBException;
import exceptions.ExceptionLogs;

public class SelectColumns extends Tablesheet {

    private List<String> columnNames;
    ExceptionLogs log = new ExceptionLogs();

    public SelectColumns(String fileName, List<String> columnNames) {
        super(fileName);
        this.path = fileName;
        this.columnNames = columnNames != null ? columnNames : Collections.emptyList(); // Inicializar columnNames com lista vazia, se necessário
    }


    private List<String> selectColumnsInteractive(ResultSetMetaData metaData) throws SQLException {
        List<String> selectedColumns = new ArrayList<>();

        System.out.println("Column selection:");
        System.out.println("Type the column names to keep (separated by commas), or press Enter to keep all columns:");

        try (Scanner scanner = new Scanner(System.in)) {
        	
            String input = scanner.nextLine().trim();
            
            if (!input.isEmpty()) {
                String[] columnNamesArray = input.split(",");
                for (String columnName : columnNamesArray) {
                    String trimmedColumnName = columnName.trim();
                    if (isValidColumn(metaData, trimmedColumnName)) {
                        selectedColumns.add(trimmedColumnName);
                    } else {
                        System.out.println("Invalid column name: " + trimmedColumnName);
                    }
                }
            }
        }

        return selectedColumns;
    }


    private boolean isValidColumn(ResultSetMetaData metaData, String columnName) throws SQLException {
       
    	int columnCount = metaData.getColumnCount();
       
    	for (int i = 1; i <= columnCount; i++) {
            if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
                return true;
            }
        }
       
    	return false;
    }

    @Override
    public void writeFile(String query) throws IOException {
     
    	try {
            ReadQuery q = new ReadQuery();
            ResultSet resultSet = q.querySelect(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

           
            if (columnNames.isEmpty()) {
                columnNames = selectColumnsInteractive(metaData);
            }

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");

            Row headerRow = sheet.createRow(0);
            int columnIndex = 0;

            
            if (!columnNames.isEmpty()) {
                for (String columnName : columnNames) {
                    for (int i = 1; i <= columnCount; i++) {
                        if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
                            Cell cell = headerRow.createCell(columnIndex);
                            cell.setCellValue(columnName);
                            columnIndex++;
                            break;
                        }
                    }
                }
            } else {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Cell cell = headerRow.createCell(columnIndex);
                    cell.setCellValue(columnName);
                    columnIndex++;
                }
            }

            int rowNumber = 1;

            while (resultSet.next()) {
              
            	Row row = sheet.createRow(rowNumber);
                columnIndex = 0;

                if (!columnNames.isEmpty()) {
                    for (String columnName : columnNames) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
                                String columnValue = resultSet.getString(i);
                                Cell cell = row.createCell(columnIndex);
                                cell.setCellValue(columnValue);
                                columnIndex++;
                                break;
                            }
                        }
                    }
                } else {
                    for (int i = 1; i <= columnCount; i++) {
                        String columnValue = resultSet.getString(i);
                        Cell cell = row.createCell(columnIndex);
                        cell.setCellValue(columnValue);
                        columnIndex++;
                    }
                }

                rowNumber++;
            }

            File outputFile = new File(path);
            outputFile.getParentFile().mkdirs();

            try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                workbook.write(fileOut);
                System.out.println("FILE WRITTEN SUCCESSFULLY.");
            }

            workbook.close();

        } catch (SQLException e) {
           
        	log.errorLog(e);
            System.out.println("ERROR WRITING FILE");
            throw new DBException(e.getMessage());
            
        } finally {
           
        	log.close();
        	
        }
    }
}
