package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap; //[1,4,5,6,4,9,8,2,6,9,0,1,5,6,7,4,9]
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;

public class DriverBuilder 
{
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static final String DRIVER_PATH = System.getProperty("user.dir")+"/src/main/resources/drivers/";
	private static Properties properties;
	
	private DriverBuilder() {}
	
	private static String getOsType()
	{
		String propertyFilePath = System.getProperty("user.dir")+"/osType.properties";
		File file = new File(propertyFilePath);
		try (FileInputStream fis=new FileInputStream(file)) {
			properties = new Properties();
			properties.load(fis);
		} catch (IOException e) {
			Assert.fail("Failed to load config.properties file", e);
		}
		return properties.getProperty("osType");
	}

	public static synchronized WebDriver getDriver() 
	{
		if(driver.get() == null) 
			driver = createDriver();
		return driver.get();
	}

	private static synchronized ThreadLocal<WebDriver> createDriver() 
	{
		String environmentType = ConfigFileReader.getConfigReader().getEnvironment();
		if(environmentType.equalsIgnoreCase("local"))
			driver = createLocalDriver();
		else
			driver = createRemoteDriver();
		return driver;
	}

	private static synchronized ThreadLocal<WebDriver> createRemoteDriver() 
	{
		throw new RuntimeException("RemoteWebDriver is not yet implemented");
	}

	private static synchronized ThreadLocal<WebDriver> createLocalDriver() 
	{
		String driverType = ConfigFileReader.getConfigReader().getBrowser();
		int pgLoadTimeout = ConfigFileReader.getConfigReader().getPageLoadTimeout();
		int implicitWait = ConfigFileReader.getConfigReader().getImplicitWait();
		switch (driverType.toUpperCase())
		{     
			case "FIREFOX":
				driver = startFireFoxBrowser();
				break;
				
			case "CHROME": 
				driver = startChromeBrowser();
				break;
				
			case "INTERNETEXPLORER":
				driver = startInternetExplorerBrowser();
				break;
				
			default:
				driver = startChromeBrowser();
		}
		driver.get().manage().window().maximize();
		driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pgLoadTimeout));
		driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		return driver;
	} 
	
	private static synchronized ThreadLocal<WebDriver> startFireFoxBrowser()
	{
		driver.set(new FirefoxDriver());
		return driver;
	}
	
	private static synchronized ThreadLocal<WebDriver> startChromeBrowser()
	{
		String osType = getOsType();
		if(osType.equals("windows")) {
			System.setProperty("webdriver.chrome.driver", DRIVER_PATH+"chromedriver.exe");
		} else {
			System.setProperty("webdriver.chrome.driver", DRIVER_PATH+"chromedriver");
		}
		ChromeOptions options = new ChromeOptions();
	    Map<String, Object> prefs = new HashMap<>();
	    prefs.put("profile.managed_default_content_settings.geolocation", 2);
	    options.setExperimentalOption("prefs", prefs);
	    driver.set(new ChromeDriver(options));
	    return driver;
	}
	
	private static synchronized ThreadLocal<WebDriver> startInternetExplorerBrowser()
	{
		driver.set(new InternetExplorerDriver());
		return driver;
	}

	public static synchronized void closeDriver() 
	{
		if(driver.get()!=null)
		{
			driver.get().quit();
			driver.remove();
		}
	}
}