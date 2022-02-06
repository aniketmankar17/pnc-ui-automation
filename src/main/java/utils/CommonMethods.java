package utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonMethods {
	private WebDriver driver;
	private JavascriptExecutor jsExecutor;
	private WebDriverWait wait;
	
	public CommonMethods(WebDriver driver, long timeout) {
		this.driver = driver;
		this.jsExecutor = (JavascriptExecutor) this.driver;
		this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(timeout));
	}
	
	public List<WebElement> getListOfElements(By locator) throws Exception {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}
	
	public boolean isElementExist(By locator) throws Exception {
		return !getListOfElements(locator).isEmpty();
	}
	
	public void click(WebElement element) throws Exception {
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
	}
	
	public void click(By locator) throws Exception {
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}
	
	public void clickWithJs(By locator) throws Exception {
		jsExecutor.executeScript("arguments[0].click();", wait.until(ExpectedConditions.presenceOfElementLocated(locator)));
	}
	
	public void clickWithJs(WebElement element) throws Exception {
		jsExecutor.executeScript("arguments[0].click();", wait.until(ExpectedConditions.visibilityOf(element)));
	}
	
	public void clear(WebElement element) throws Exception {
		wait.until(ExpectedConditions.visibilityOf(element)).clear();
	}
	
	public void clear(By locator) throws Exception {
		wait.until(ExpectedConditions.presenceOfElementLocated(locator)).clear();
	}
	
	public void enterText(WebElement element, String text) throws Exception {
		wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
	}
	
	public void enterText(By locator, String text) throws Exception {
		wait.until(ExpectedConditions.presenceOfElementLocated(locator)).sendKeys(text);
	}
	
	public String getText(WebElement element) {
		return wait.until(ExpectedConditions.visibilityOf(element)).getText();
	}
	
	public String getText(By locator) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getText();
	}
	
	public String getAttribute(WebElement element, String attribute) {
		return wait.until(ExpectedConditions.visibilityOf(element)).getAttribute(attribute);
	}
	
	public String getAttribute(By locator, String attribute) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getAttribute(attribute);
	}
	
	public String getCssValue(WebElement element, String propertyName) {
		return wait.until(ExpectedConditions.visibilityOf(element)).getCssValue(propertyName);
	}
	
	public String getCssValue(By locator, String propertyName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getCssValue(propertyName);
	}
	
	public boolean isDisplayed(WebElement element) {
		return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
	}
	
	public boolean isDisplayed(By locator) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).isDisplayed();
	}
	
	public boolean isEnabled(WebElement element) {
		return wait.until(ExpectedConditions.visibilityOf(element)).isEnabled();
	}
	
	public boolean isEnabled(By locator) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).isEnabled();
	}
	
	public boolean waitForElementSelectionState(WebElement element, boolean state) throws Exception {
		return wait.until(ExpectedConditions.elementSelectionStateToBe(element, state));
	}
	
	public boolean waitForElementSelectionState(By locator, boolean state) throws Exception {
		return wait.until(ExpectedConditions.elementSelectionStateToBe(locator, state));
	}
	
	public boolean waitForElementToBeInvisible(WebElement element) throws Exception {
		return wait.until(ExpectedConditions.invisibilityOf(element));
	}
	
	public boolean waitForElementToBeInvisible(By locator) throws Exception {
		return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}
	
	public void switchToFrame(WebElement element) throws Exception {
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
	}
	
	public void switchToFrame(By locator) throws Exception {
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}
	
	public void switchToFrame(int index) throws Exception {
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
	}
}