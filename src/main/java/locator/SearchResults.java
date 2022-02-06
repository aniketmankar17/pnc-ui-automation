package locator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;

import utils.CommonMethods;
import utils.ConfigFileReader;

public class SearchResults 
{
	private WebDriver driver;
	private ExtentTest testStep;
	private CommonMethods commonMethods;
	private JavascriptExecutor jsExecutor;

	public SearchResults(WebDriver driver, ExtentTest testStep)
	{
		this.driver = driver;
		this.testStep = testStep;
		this.commonMethods = new CommonMethods(this.driver, 20);
		jsExecutor = (JavascriptExecutor) this.driver;
		PageFactory.initElements(this.driver, this);
	}

	@FindBy(name = "radius")
	private WebElement btnRadius;

	@FindBy(xpath = "//button[text()='Apply']")
	private WebElement btnApply;

	@FindBy(id = "moreFilterBranch")
	private WebElement btnMoreFiltersBranch;

	@FindBy(id = "moreFilterATM")
	private WebElement btnMoreFiltersAtm;

	@FindBy(xpath = "//span[@data-ng-bind='searchCtrl.branchesLength']")
	private WebElement lblNoOfBranches;
	
	@FindBy(xpath = "//span[@data-ng-bind='searchCtrl.atmsLength']")
	private WebElement lblNoOfAtms;

	private By btnFilter = By.xpath("//button[@ng-if='searchCtrl.showResults']");
	private By resultLst = By.id("resultList");
	private By lstOfBranchFilters = By.xpath("//div[contains(@ng-repeat,'filter in filters.branchFilters')]//p");
	private By lstOfAtmFilters = By.xpath("//div[contains(@ng-repeat,'filter in filters.atmFilters')]//p");
	private By btnNext = By.xpath("//button[@ng-click='searchCtrl.nextPage()']");
	private By lstOfLnkViewBranch = By.xpath("//span[contains(text(),'View Branch')]/parent::a");
	private By lstOfLnkViewAtm = By.xpath("//span[contains(text(),'View ATM')]/parent::a");
	private By lnkMoreBranchFilters = By.xpath("//div[@class='panelBranch show']");
	private By lnkMoreAtmFilters = By.xpath("//div[@class='panelATM show']");
	private By lstOfMi = By.xpath("//div[@id='resultList']//div[contains(text(),'mi')]");

	public boolean isSearchResultsPageLoaded()
	{
		boolean flag = false;
		try {
			flag = commonMethods.isElementExist(btnFilter) && commonMethods.isElementExist(resultLst);
			testStep.info("Locator Search Results page loaded");
		} catch (Exception e) {
			Assert.fail("Failed to load Search Results page", e);
		}
		return flag;
	}

	public void clickFilterButton()
	{
		try {
			commonMethods.click(btnFilter);
			testStep.info("Clicked 'Filter' button");
		} catch (Exception e) {
			Assert.fail("Failed to click 'Filter' button", e);
		}
	}

	public void selectRadius(String radius)
	{
		try {
			commonMethods.click(btnRadius);
			commonMethods.clickWithJs(By.xpath("//ul[@role='menu']//a[contains(text(),'"+radius+" Mile')]"));
			testStep.info("Selected radius as "+radius+" mile(s)");
		} catch (Exception e) {
			Assert.fail("Failed to select radius: "+radius, e);
		}
	}

	public void clickApply()
	{
		try {
			commonMethods.click(btnApply);
			testStep.info("Clicked 'Apply' button");
		} catch (Exception e) {
			Assert.fail("Failed to click Apply button", e);
		}
	}

	public boolean isAllResultLocationsWithinRadius(String radius)
	{
		boolean flag = false;
		try {
			Double radiusInNum = Double.parseDouble(radius);
			List<Double> allMilesInNum = new ArrayList<>();
			List<WebElement> firstPageResults = driver.findElements(lstOfMi);
			for(int i=0;i<firstPageResults.size();i++)
			{
				String values = firstPageResults.get(i).getText().split(" ")[0];
				allMilesInNum.add(Double.parseDouble(values));
				Thread.sleep(10);
			}
			while(!driver.findElements(btnNext).isEmpty() && driver.findElement(btnNext).isEnabled())
			{
				commonMethods.click(btnNext);
				List<WebElement> pageResults = driver.findElements(lstOfMi);
				for(int i=0;i<pageResults.size();i++)
				{
					String values = pageResults.get(i).getText().split(" ")[0];
					allMilesInNum.add(Double.parseDouble(values));
					Thread.sleep(10);
				}
			}
			Collections.sort(allMilesInNum);
			if(radiusInNum>allMilesInNum.get(allMilesInNum.size()-1))
				flag = true;
			else
				flag = false;
			testStep.info("Search results for miles within Radius "+radius+" Mile(s): "+allMilesInNum);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			Assert.fail("Failed to verify resulting miles are within radius: "+radius, e);
		}
		return flag;
	}

	public boolean isBranchFilterLabelsPresent()
	{
		String expFilters = ConfigFileReader.getConfigReader().getProperty("branchFilters");
		return isFilterLabelsPresent(expFilters, lnkMoreBranchFilters, btnMoreFiltersBranch, lstOfBranchFilters, "Branch");
	}

	public boolean isAtmFilterLabelsPresent()
	{
		String expFilters = ConfigFileReader.getConfigReader().getProperty("atmFilters");
		return isFilterLabelsPresent(expFilters, lnkMoreAtmFilters, btnMoreFiltersAtm, lstOfAtmFilters, "ATM");
	}

	private boolean isFilterLabelsPresent(String expFilters, By moreFilters, WebElement btnMoreFilters, By lstOfFilters, String filterType)
	{
		boolean flag = false;
		try {
			String[] expectedFilters = expFilters.split(";");
			if(commonMethods.isElementExist(moreFilters))
			{
				commonMethods.clickWithJs(btnMoreFilters);
			}
			List<WebElement> filterList = driver.findElements(lstOfFilters);
			String[] actualFilters = new String[filterList.size()];
			for(int i=0; i<actualFilters.length; i++)
			{
				actualFilters[i]=filterList.get(i).getText();
			}
			String info = "Expected "+filterType+" filters ["+expectedFilters.length+"] = "+Arrays.toString(expectedFilters)
			+" and Actual "+filterType+" filters ["+actualFilters.length+"] = "+Arrays.toString(actualFilters);
			if(actualFilters.length==expectedFilters.length)
			{
				for(int i=0;i<expectedFilters.length;i++)
				{
					if(actualFilters[i].contains(expectedFilters[i]))
						flag=true;
					else
					{
						flag=false;
						break;
					}
				}
			}
			testStep.info(info);
		} catch (Exception e) {
			Assert.fail("Failed to verify "+filterType+" Filters", e);
		}
		return flag;
	}

	public void clickOnFirstBranch()
	{
		clickOnFirstResult("Branch", lstOfLnkViewBranch);
	}

	public void clickOnFirstAtm()
	{
		clickOnFirstResult("ATM", lstOfLnkViewAtm);
	}

	private void clickOnFirstResult(String resultType, By lstOfResults)
	{
		try {
			if(commonMethods.isElementExist(lstOfResults))
			{
				WebElement result = driver.findElements(lstOfResults).get(0);
				jsExecutor.executeScript("arguments[0].click();", result);
				testStep.info("Clicked on 'View "+resultType+" Details' of the first "+resultType+" in search results");
			}
			else
				Assert.fail("No "+resultType+"(s) present");
		} catch (Exception e) {
			Assert.fail("Failed to click on first search result", e);
		}
	}

	private void selectServices(String serviceType, String services, WebElement btnMoreFilters)
	{
		String[] array = services.split(";");
		try {
			commonMethods.clickWithJs(btnMoreFilters);
			for(int i=0; i<array.length; i++)
			{
				String xpath = "//div[@aria-label='"+array[i]+"']";
				commonMethods.clickWithJs(By.xpath(xpath));
			}
			testStep.info("Selected "+serviceType+" services: "+Arrays.toString(array));
		} catch (Exception e) {
			Assert.fail("Failed to select "+serviceType+" services: "+Arrays.toString(array));
		}
	}

	public void selectBranchServices(String brServices)
	{
		selectServices("Branch", brServices, btnMoreFiltersBranch);
	}

	public void selectAtmServices(String atmServices)
	{
		selectServices("ATM", atmServices, btnMoreFiltersAtm);
	}

	public boolean isNumberOfBranchesCorrect()
	{
		return isNumberOfResultsCorrect("Branch", lblNoOfBranches);
	}
	
	public boolean isNumberOfAtmsCorrect()
	{
		return isNumberOfResultsCorrect("ATM", lblNoOfAtms);
	}
	
	private boolean isNumberOfResultsCorrect(String branchAtm, WebElement resultType)
	{
		try {
			int count = 0;
			if(branchAtm.equalsIgnoreCase("branch"))
				branchAtm = "Branch(es)";
			else
				branchAtm = "ATM(s)";
			count+=driver.findElements(lstOfMi).size();
			while(!driver.findElements(btnNext).isEmpty() && driver.findElement(btnNext).isEnabled())
			{
				commonMethods.click(btnNext);
				count+=driver.findElements(lstOfMi).size();
				Thread.sleep(10);
			}
			String noOfResults = commonMethods.getText(resultType);
			testStep.info("Actual "+branchAtm+" = "+count+" and Showing "+noOfResults+" "+branchAtm);
			return noOfResults.equals(String.valueOf(count));
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			Assert.fail("Failed to verify number of "+branchAtm, e);
		}
		return false;
	}
}