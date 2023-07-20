package services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class ExceptionLogs {
	
	private String logPath = "src/logs/logs.txt";
	private PrintWriter out;
	
	public ExceptionLogs() {
		
		try {
			
			out = new PrintWriter(new FileWriter(logPath));
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
	}
	
	public void errorLog(Throwable error) {
		
		out.print("Error date: ");
		out.println(new Date());
		out.print("Error message: ");
		out.println(error);
		out.print("Stacktrace: ");
		error.printStackTrace(out);
		
	}
	
	public void close() {
		
		if (out != null) {
            out.flush();
			out.close();
		}
	}
}