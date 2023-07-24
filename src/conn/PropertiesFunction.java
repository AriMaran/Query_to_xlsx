package conn;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import exceptions.ExceptionLogs;

public class PropertiesFunction {

	private String propertiesPath = "src/conn/conndata.properties";
	private String pwd = null;
	private String url = null;
	private String usr = null;
	ExceptionLogs log = new ExceptionLogs();

	public PropertiesFunction() {

		this.pwd = getPropertiesPWD();
		this.url = getPropertiesURL();
		this.usr = getPropertiesUSR();
		
	}

	
	public String getPwd() {
		return pwd;
	}
	
	
	public String getUrl() {
		return url;
	}
	
	
	public String getUsr() {
		return usr;
	}


	public String getPropertiesURL() {
		
		try {
			
			FileInputStream fis = new FileInputStream(propertiesPath);

			Properties prop = new Properties();
			prop.load(fis);
			url = prop.getProperty("url");

			return url;

		} catch (IOException e) {

			System.err.println("Impossible to retrieve connection data file");
			log.errorLog(e);
			log.close();
			e.printStackTrace();
			return null;
		}
	}

	public String getPropertiesUSR() {
		
		try {

			FileInputStream fis = new FileInputStream(propertiesPath);
			Properties prop = new Properties();
			prop.load(fis);
			usr = prop.getProperty("usr");
			return usr;

		} catch (IOException e) {

			e.printStackTrace();
			log.errorLog(e);
			log.close();

			return null;
		}
	}

	public String getPropertiesPWD() {
		
		try {

			FileInputStream fis = new FileInputStream(propertiesPath);
			Properties prop = new Properties();
			prop.load(fis);
			pwd = prop.getProperty("pwd");
			
			return pwd;

		} catch (IOException e) {
		
			System.err.println("Impossible to retrieve connection data file");
			log.errorLog(e);
			log.close();
			e.printStackTrace();

			return null;
		}
	}

}
