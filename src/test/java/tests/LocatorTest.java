package tests;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import locator.BranchAtmDetails;
import locator.BranchesByState;
import locator.Home;
import locator.SearchResults;
import utils.BaseTestMethods;
import utils.ConfigFileReader;
import utils.DriverBuilder;
import utils.ExcelReader;
import utils.ReportManager;

public class LocatorTest extends BaseTestMethods
{	
	@BeforeClass
	public void setLocatorTestData()
	{
		String fileName = ConfigFileReader.getConfigReader().getProperty("LocatorExcel");
		String sheetName = ConfigFileReader.getConfigReader().getProperty("LocatorSheet");
		ExcelReader.setTestData(fileName, sheetName);
	}
	
	@Test(dataProviderClass = ExcelReader.class, dataProvider = "testData")
	public void searchWithZip(Map<String, String> data)
	{
		WebDriver driver = DriverBuilder.getDriver();
		ExtentTest testStep = ReportManager.getExtentTest();
		Home home = new Home(driver, testStep);
		SearchResults results = new SearchResults(driver, testStep);
		
		home.goToLocator();
		home.isHomePageLoaded();
		home.enterSearchBoxValue(data.get("zipCode"));
		home.clickFindButton();
		Assert.assertEquals(results.isSearchResultsPageLoaded(), true, "Locator Search Results page not loaded");
	}
	
	@Test(dataProviderClass = ExcelReader.class, dataProvider = "testData")
	public void searchWithCity(Map<String, String> data)
	{
		WebDriver driver = DriverBuilder.getDriver();
		ExtentTest testStep = ReportManager.getExtentTest();
		Home home = new Home(driver, testStep);
		SearchResults results = new SearchResults(driver, testStep);
		
		home.goToLocator();
		home.isHomePageLoaded();
		home.enterSearchBoxValue(data.get("city"));
		home.clickFindButton();
		Assert.assertEquals(results.isSearchResultsPageLoaded(), true, "Locator Search Results page not loaded");
	}
	
	@Test(dataProviderClass = ExcelReader.class, dataProvider = "testData")
	public void searchWithInvalidLocation(Map<String, String> data)
	{
		WebDriver driver = DriverBuilder.getDriver();
		ExtentTest testStep = ReportManager.getExtentTest();
		Home home = new Home(driver, testStep);
		
		home.goToLocator();
		home.isHomePageLoaded();
		home.enterSearchBoxValue(data.get("city"));
		home.clickFindButton();
		Assert.assertEquals(home.isNoLocationFoundMsgDisplayed(), true, "No Locations Found message not displayed");
		testStep.pass("No Location Found message is displayed");
	}
	
	@Test(dataProviderClass = ExcelReader.class, dataProvider = "testData")
	public void verifyRadiusFunctionality(Map<String, String> data)
	{
		WebDriver driver = DriverBuilder.getDriver();
		ExtentTest testStep = ReportManager.getExtentTest();
		Home home = new Home(driver, testStep);
		SearchResults results = new SearchResults(driver, testStep);
		
		String radius = data.get("radius");
		home.goToLocator();
		home.isHomePageLoaded();
		home.enterSearchBoxValue(data.get("city"));
		home.clickFindButton();
		results.isSearchResultsPageLoaded();
		results.clickFilterButton();
		results.selectRadius(radius);
		results.clickApply();
		Assert.assertTrue(results.isAllResultLocationsWithinRadius(radius), "All Resulting locations are not withing radius "+radius+" mile(s)");
		testStep.pass("All resulting locations are within Radius: "+radius+" mile(s)");
	}
	
	@Test(dataProviderClass = ExcelReader.class, dataProvider = "testData")
	public void verifyBranchAndAtmFilters(Map<String, String> data)
	{
		WebDriver driver = DriverBuilder.getDriver();
		ExtentTest testStep = ReportManager.getExtentTest();
		Home home = new Home(driver, testStep);
		SearchResults results = new SearchResults(driver, testStep);
		
		home.goToLocator();
		home.isHomePageLoaded();
		home.enterSearchBoxValue(data.get("city"));
		home.clickFindButton();
		results.isSearchResultsPageLoaded();
		results.clickFilterButton();
		Assert.assertEquals(results.isBranchFilterLabelsPresent(), true, "Expected and Actual Branch Filters do not match");
		testStep.pass("Branch filters verified");
		Assert.assertEquals(results.isAtmFilterLabelsPresent(), true, "Expected and Actual ATM filters do not match");
		testStep.pass("ATM filters verified");
	}
	
	@Test(dataProviderClass = ExcelReader.class, dataProvider = "testData")
	public void verifyBranchServices(Map<String, String> data)
	{
		WebDriver driver = DriverBuilder.getDriver();
		ExtentTest testStep = ReportManager.getExtentTest();
		Home home = new Home(driver, testStep);
		SearchResults results = new SearchResults(driver, testStep);
		BranchAtmDetails details = new BranchAtmDetails(driver, testStep);
		
		home.goToLocator();
		home.isHomePageLoaded();
		home.enterSearchBoxValue(data.get("city"));
		home.clickFindButton();
		results.isSearchResultsPageLoaded();
		results.clickFilterButton();
		results.selectBranchServices(data.get("branchServices"));
		results.clickApply();
		results.clickOnFirstBranch();
		details.isBranchDetailsPageLoaded();
		Assert.assertTrue(details.isBranchServicesPresent(data.get("branchServices")),"Expected branch services are not present");
		testStep.pass("Branch services verified");
	}
	
	@Test(dataProviderClass = ExcelReader.class, dataProvider = "testData")
	public void verifyAtmServices(Map<String, String> data)
	{
		WebDriver driver = DriverBuilder.getDriver();
		ExtentTest testStep = ReportManager.getExtentTest();
		Home home = new Home(driver, testStep);
		SearchResults results = new SearchResults(driver, testStep);
		BranchAtmDetails details = new BranchAtmDetails(driver, testStep);
		
		home.goToLocator();
		home.isHomePageLoaded();
		home.enterSearchBoxValue(data.get("city"));
		home.clickFindButton();
		results.isSearchResultsPageLoaded();
		results.clickFilterButton();
		results.selectAtmServices(data.get("atmServices"));
		results.clickApply();
		results.clickOnFirstAtm();
		details.isAtmDetailsPageLoaded();
		Assert.assertTrue(details.isAtmServicesPresent(data.get("atmServices")), "Expected ATM services are not present");
		testStep.pass("ATM services verified");
	}
	
	@Test(dataProviderClass = ExcelReader.class, dataProvider = "testData")
	public void verifyNumberOfBranchResults(Map<String, String> data)
	{
		WebDriver driver = DriverBuilder.getDriver();
		ExtentTest testStep = ReportManager.getExtentTest();
		Home home = new Home(driver, testStep);
		SearchResults results = new SearchResults(driver, testStep);
		
		home.goToLocator();
		home.isHomePageLoaded();
		home.enterSearchBoxValue(data.get("city"));
		home.selectOnlyBranches();
		home.clickFindButton();
		results.isSearchResultsPageLoaded();
		results.clickFilterButton();
		results.selectRadius(data.get("radius"));
		results.clickApply();
		Assert.assertTrue(results.isNumberOfBranchesCorrect(),"Number of Branches is not correct");
		testStep.pass("Number of Branches is correct");
	}
	
	@Test(dataProviderClass = ExcelReader.class, dataProvider = "testData")
	public void verifyNumberOfAtmResults(Map<String, String> data)
	{
		WebDriver driver = DriverBuilder.getDriver();
		ExtentTest testStep = ReportManager.getExtentTest();
		Home home = new Home(driver, testStep);
		SearchResults results = new SearchResults(driver, testStep);
		
		home.goToLocator();
		home.isHomePageLoaded();
		home.enterSearchBoxValue(data.get("city"));
		home.selectOnlyAtms();
		home.clickFindButton();
		results.isSearchResultsPageLoaded();
		results.clickFilterButton();
		results.selectRadius(data.get("radius"));
		results.clickApply();
		Assert.assertTrue(results.isNumberOfAtmsCorrect(),"Number of ATMs is not correct");
		testStep.pass("Number of ATMs is correct");
	}
	
	@Test(dataProviderClass = ExcelReader.class, dataProvider = "testData")
	public void verifyStatesAreSorted(Map<String, String> data)
	{
		WebDriver driver = DriverBuilder.getDriver();
		ExtentTest testStep = ReportManager.getExtentTest();
		Home home = new Home(driver, testStep);
		BranchesByState branches = new BranchesByState(driver, testStep);
		
		home.goToLocator();
		home.isHomePageLoaded();
		home.clickOnBrowseAllPncBranches();
		branches.isBranchesByStatePageLoaded();
		Assert.assertTrue(branches.isBranchesByStateSorted(), "States are not sorted in ascending order");
		testStep.pass("States are sorted in ascending order");
	}
}