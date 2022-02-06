package locator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;

import utils.CommonMethods;

public class BranchesByState 
{
	private WebDriver driver;
	private ExtentTest testStep;
	private CommonMethods commonMethods;

	public BranchesByState(WebDriver driver, ExtentTest testStep)
	{
		this.driver = driver;
		this.testStep = testStep;
		this.commonMethods = new CommonMethods(this.driver, 10);
		PageFactory.initElements(this.driver, this);
	}
	
	@FindBy(xpath = "//h1[text()='Browse Branches by State']")
	private WebElement lblBrowseBranches;
	
	private By lnkOfStates = By.xpath("//li[contains(@data-ng-repeat, 'DskTop')]/a");
	
	public boolean isBranchesByStatePageLoaded()
	{
		boolean result = false;
		try {
			result = commonMethods.getListOfElements(lnkOfStates).size() == 25;
			testStep.info("'Browse Branches by State' page loaded");
		} catch (Exception e) {
			Assert.fail("Failed to load 'Browse Branches by State' page", e);
		}
		return result;
	}
	
	public boolean isBranchesByStateSorted()
	{
		boolean result = false;
		try {
			List<WebElement> statesElements = commonMethods.getListOfElements(lnkOfStates);
			List<String> states = new ArrayList<>();
			for(int i=0; i<statesElements.size(); i++)
			{
				states.add(statesElements.get(i).getText());
				Thread.sleep(50);
			}
			List<String> copy = new ArrayList<>(states);
			Collections.sort(copy);
			result = copy.equals(states);
			testStep.info("States = "+states);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			Assert.fail("Failed to check if states are sorted in ascending order", e);
		}
		return result;
	}
}