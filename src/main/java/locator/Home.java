package locator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;

import utils.CommonMethods;
import utils.ConfigFileReader;

public class Home 
{
	private WebDriver driver;
	private ExtentTest testStep;
	private CommonMethods commonMethods;
	
	public Home(WebDriver driver, ExtentTest testStep)
	{
		this.driver = driver;
		this.testStep = testStep;
		this.commonMethods = new CommonMethods(this.driver, 10);
		PageFactory.initElements(this.driver, this);
	}
	
	@FindBy(xpath = "//li[contains(@class,'noSearchResultModal')]//a")
	private WebElement lnkBrowseAllBranchesNoResult;
	
	@FindBy(xpath = "//button[text()='Ok']")
	private WebElement btnOk;
	
	@FindBy(xpath = "//span[@aria-labelledby='brnchCheckBoxText']")
	private WebElement chkBranches;
	
	@FindBy(xpath = "//span[@aria-labelledby='atmCheckBoxText']")
	private WebElement chkAtms;
	
	@FindBy(xpath = "//span[@aria-labelledby='partnerCheckBoxText']")
	private WebElement chkPartnerAtms;
	
	@FindBy(xpath = "//a[@ui-sref='browse.browseState']")
	private WebElement lnkBrowseAllPncBranches;
	
	private By inpSearch = By.id("extTxtSearchText");
	private By btnFind =  By.xpath("//button[text()='Find']");
	private By lblNoLocationsFound = By.xpath("//h2[@id='headerModal']//label[text()='No locations found']");
	
	private String chkAttribute = "aria-checked";
	private String chkAttributeValue = "true";
	
	public void goToLocator()
	{
		String locatorUrl = "";
		try {
			locatorUrl = ConfigFileReader.getConfigReader().getLocatorUrl();
			driver.get(locatorUrl);
			testStep.info("Launched PNC locator: "+locatorUrl);
		} catch (Exception e) {
			Assert.fail("Failed to launch Locator website: "+locatorUrl, e);
		}
	}
	
	public boolean isHomePageLoaded()
	{
		boolean flag = false;
		try {
			flag = commonMethods.isElementExist(inpSearch) && commonMethods.isElementExist(btnFind);
			testStep.info("Locator Homepage loaded");
		} catch (Exception e) {
			Assert.fail("Failed to load Locator home page", e);
		}
		return flag;
	}
	
	public void selectOnlyBranches()
	{
		try {
			if(!commonMethods.getAttribute(chkBranches, chkAttribute).equals(chkAttributeValue))
				commonMethods.click(chkBranches);
			if(commonMethods.getAttribute(chkAtms, chkAttribute).equals(chkAttributeValue))
				commonMethods.click(chkAtms);
			if(commonMethods.getAttribute(chkPartnerAtms, chkAttribute).equals(chkAttributeValue))
				commonMethods.click(chkPartnerAtms);
			testStep.info("Selected Branches only checkbox");
		} catch (Exception e) {
			Assert.fail("Failed to select only Branches checkbox", e);
		}
	}
	
	public void selectOnlyAtms()
	{
		try {
			if(!commonMethods.getAttribute(chkAtms, chkAttribute).equals(chkAttributeValue))
				commonMethods.click(chkAtms);
			if(commonMethods.getAttribute(chkBranches, chkAttribute).equals(chkAttributeValue))
				commonMethods.click(chkBranches);
			if(commonMethods.getAttribute(chkPartnerAtms, chkAttribute).equals(chkAttributeValue))
				commonMethods.click(chkPartnerAtms);
			testStep.info("Selected ATMs only checkbox");
		} catch (Exception e) {
			Assert.fail("Failed to select only ATMs checkbox", e);
		}
	}
	
	public void selectOnlyPartnerAtms()
	{
		try {
			if(!commonMethods.getAttribute(chkPartnerAtms, chkAttribute).equals(chkAttributeValue))
				commonMethods.click(chkPartnerAtms);
			if(commonMethods.getAttribute(chkBranches, chkAttribute).equals(chkAttributeValue))
				commonMethods.click(chkBranches);
			if(commonMethods.getAttribute(chkAtms, chkAttribute).equals(chkAttributeValue))
				commonMethods.click(chkAtms);
			testStep.info("Selected Partner ATMs only checkbox");
		} catch (Exception e) {
			Assert.fail("Failed to select only Partner ATMs checkbox", e);
		}
	}
	
	public void enterSearchBoxValue(String location)
	{
		try {
			commonMethods.clear(inpSearch);
			commonMethods.enterText(inpSearch, location);
			testStep.info("Entered '"+location+"' in search box");
		} catch (Exception e) {
			Assert.fail("Failed to enter search box value", e);
		}
	}
	
	public void clickFindButton()
	{
		try {
			commonMethods.click(btnFind);
			testStep.info("Clicked on Find button");
		} catch (Exception e) {
			Assert.fail("Failed to click Find button", e);
		}
	}
	
	public void searchLocation(String location)
	{
		enterSearchBoxValue(location);
		clickFindButton();
	}
	
	public boolean isNoLocationFoundMsgDisplayed()
	{
		try {
			return commonMethods.isElementExist(lblNoLocationsFound);
		} catch (Exception e) {
			Assert.fail("No Location Found message not displayed", e);
		}
		return false;
	}
	
	public void clickOnBrowseAllPncBranches()
	{
		try {
			commonMethods.click(lnkBrowseAllPncBranches);
			testStep.info("Clicked on 'Browse All PNC Branches' link");
		} catch (Exception e) {
			Assert.fail("Failed to click 'Browse All PNC Branches' link", e);
		}
	}
}