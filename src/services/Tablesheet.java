package services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import conn.DBException;

public class Tablesheet {

	private String fileName;
	private String path;
	ExceptionLogs log = new ExceptionLogs();
	
	
	public Tablesheet(String fileName) {
	
		this.fileName = fileName;
		this.path = fileName;
		
	}

	public void generateFile() {
	
		try {
			
			File obj = new File(path);
			
			if (obj.createNewFile()) {
				
				System.out.println("File created: " + obj.getName());
			} else {
				
				System.out.println("File already exists");
				System.out.println(path);
				
			}
		} catch (IOException e) {
			
			System.err.println("ERROR GENERATING FILE. TRY AGAIN OR CONTACT I.T SUPPORT");
			log.errorLog(e);
			log.close();
			e.printStackTrace();
		}
	}

	public void writeFile(String query) throws IOException {
	
		try {
			
			ReadQuery q = new ReadQuery();
			ResultSet resultSet = q.querySelect(query);
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Sheet1");

			Row headerRow = sheet.createRow(0);

			for (int i = 1; i <= columnCount; i++) {
				
				String columnName = metaData.getColumnName(i);
				Cell cell = headerRow.createCell(i - 1);
				cell.setCellValue(columnName);
			}

			int rowNumber = 1;

			while (resultSet.next()) {
				Row row = sheet.createRow(rowNumber);
				
				for (int i = 1; i <= columnCount; i++) {
					String columnValue = resultSet.getString(i);
					Cell cell = row.createCell(i - 1);
					cell.setCellValue(columnValue);
				}
				
				rowNumber++;
			}

			File outputFile = new File(path);
			outputFile.getParentFile().mkdirs();

			try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
				workbook.write(fileOutputStream);
				System.out.println("FILE WRITTEN SUCCESSFULLY.");
				
			} catch (IOException e) {
				
				System.err.println("ERROR WRITING FILE");
				log.errorLog(e);
				log.close();
				e.printStackTrace();
			}

			workbook.close();
			
		} catch (SQLException e) {
			
			log.errorLog(e);
			log.close();
			throw new DBException(e.getMessage());
		}
	}
}