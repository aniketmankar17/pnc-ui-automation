package utils;

import java.util.HashMap; //[1,4,5,6,4,9,8,2,6,9,0,1,5,6,7,4,9]
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class DriverBuilder 
{
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static final String DRIVER_PATH = System.getProperty("user.dir")+"/src/main/resources/drivers/";
	
	private DriverBuilder() {}

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
		driver.get().manage().timeouts().pageLoadTimeout(pgLoadTimeout, TimeUnit.SECONDS);
		driver.get().manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
		return driver;
	} 
	
	private static synchronized ThreadLocal<WebDriver> startFireFoxBrowser()
	{
		driver.set(new FirefoxDriver());
		return driver;
	}
	
	private static synchronized ThreadLocal<WebDriver> startChromeBrowser()
	{
		System.setProperty("webdriver.chrome.driver", DRIVER_PATH+"chromedriver.exe");
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