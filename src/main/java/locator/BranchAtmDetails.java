package locator;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;

import utils.CommonMethods;

public class BranchAtmDetails 
{
	private WebDriver driver;
	private ExtentTest testStep;
	private CommonMethods commonMethods;

	public BranchAtmDetails(WebDriver driver, ExtentTest testStep)
	{
		this.driver = driver;
		this.testStep = testStep;
		this.commonMethods = new CommonMethods(this.driver, 10);
		PageFactory.initElements(this.driver, this);
	}
	
	private By btnDrivingDir = By.xpath("//div[@id='device-other']//span[text()='Driving Directions']/..");
	private By lblAtmDetails = By.xpath("//h2[text()='ATM Details']");
	private By lblBranchDetails = By.xpath("//h2[text()='Branch Details']");

	public boolean isBranchDetailsPageLoaded()
	{
		boolean flag = false;
		try {
			flag = commonMethods.isElementExist(btnDrivingDir) && commonMethods.isElementExist(lblBranchDetails);
			testStep.info("Branch details page loaded");
		} catch (Exception e) {
			Assert.fail("Failed to load Branch Details page", e);
		}
		return flag;
	}

	public boolean isAtmDetailsPageLoaded()
	{
		boolean flag = false;
		try {
			flag = commonMethods.isElementExist(btnDrivingDir) && commonMethods.isElementExist(lblAtmDetails);
			testStep.info("ATM details page loaded");
		} catch (Exception e) {
			Assert.fail("Failed to load ATM Details page", e);
		}
		return flag;
	}

	private boolean isServicesPresent(String serviceType, String services, String xpath)
	{
		String[] array = services.split(";");
		String[] xpaths = new String[array.length];
		List<String> actualServices = new ArrayList<>();
		List<String> missingServices = new ArrayList<>();
		for(int i=0; i<xpaths.length; i++)
		{
			xpaths[i] = xpath.replace("?", array[i]);
		}
		for(int i=0; i<xpaths.length; i++)
		{
			if(!driver.findElements(By.xpath(xpaths[i])).isEmpty())
				actualServices.add(array[i]);
			else
				missingServices.add(array[i]);
		}
		if(!missingServices.isEmpty())
			testStep.info(serviceType+" services found: "+actualServices+", Missing "+serviceType+" services: "+missingServices);
		else
			testStep.info(serviceType+" services found: "+actualServices);
		return missingServices.isEmpty();
	}

	public boolean isBranchServicesPresent(String brServices)
	{
		String xpath = "//span[text()='Branch Services']/../following-sibling::div//div[contains(text(),'?')]";
		return isServicesPresent("Branch", brServices, xpath);
	}

	public boolean isAtmServicesPresent(String atmServices)
	{
		String xpath = "//div[text()='ATM Services']/following-sibling::div//div[contains(text(), '?')]";
		return isServicesPresent("ATM", atmServices, xpath);
	}
}