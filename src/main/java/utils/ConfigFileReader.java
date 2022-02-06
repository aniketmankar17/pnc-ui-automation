package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;

public class ConfigFileReader 
{
	private static ConfigFileReader configFileReader;
	private Properties properties;

	public ConfigFileReader()
	{
		String propertyFilePath = System.getProperty("user.dir")+"/config.properties";
		File file = new File(propertyFilePath);
		try (FileInputStream fis=new FileInputStream(file)) {
			properties = new Properties();
			properties.load(fis);
		} catch (IOException e) {
			Assert.fail("Failed to load config.properties file", e);
		}
	}
	
	public static ConfigFileReader getConfigReader() 
	{
		return (configFileReader == null) ? new ConfigFileReader() : configFileReader;
	}

	public String getBrowser()
	{
		return properties.getProperty("browser");
	}
	
	public String getEnvironment() 
	{
		return properties.getProperty("environment");
	}
	
	public int getPageLoadTimeout()
	{
		int timeout = -1;
		try {
			timeout = Integer.parseInt(getProperty("pgLoadTimeout"));
		} catch (NumberFormatException e) {
			Assert.fail("Failed to get page load timeout: ", e);
		}
		return timeout;
	}
	
	public int getImplicitWait()
	{
		int timeout = -1;
		try {
			timeout = Integer.parseInt(getProperty("implicitWait"));
		} catch (NumberFormatException e) {
			Assert.fail("Failed to get page load timeout: ", e);
		}
		return timeout;
	}
	
	public String getLocatorUrl()
	{
		return properties.getProperty("url");
	}

	public String getProperty(String property)
	{
		return properties.getProperty(property);
	}
}