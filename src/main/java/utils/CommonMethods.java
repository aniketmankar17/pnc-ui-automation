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
		List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
		return elements;
	}
	
	public boolean isElementExist(By locator) throws Exception {
		List<WebElement> elements = getListOfElements(locator);
		return !elements.isEmpty();
	}
	
	public void click(WebElement element) throws Exception {
		element = wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}
	
	public void click(By locator) throws Exception {
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();
	}
	
	public void clickWithJs(By locator) throws Exception {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		jsExecutor.executeScript("arguments[0].click();", element);
	}
	
	public void clickWithJs(WebElement element) throws Exception {
		element = wait.until(ExpectedConditions.visibilityOf(element));
		jsExecutor.executeScript("arguments[0].click();", element);
	}
	
	public void clear(WebElement element) throws Exception {
		element = wait.until(ExpectedConditions.visibilityOf(element));
		element.clear();
	}
	
	public void clear(By locator) throws Exception {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		element.clear();
	}
	
	public void enterText(WebElement element, String text) throws Exception {
		element = wait.until(ExpectedConditions.visibilityOf(element));
		element.sendKeys(text);
	}
	
	public void enterText(By locator, String text) throws Exception {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		element.sendKeys(text);
	}
	
	public String getText(WebElement element) {
		element = wait.until(ExpectedConditions.visibilityOf(element));
		return element.getText();
	}
	
	public String getText(By locator) {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		return element.getText();
	}
	
	public String getAttribute(WebElement element, String attribute) {
		element = wait.until(ExpectedConditions.visibilityOf(element));
		return element.getAttribute(attribute);
	}
	
	public String getAttribute(By locator, String attribute) {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		return element.getAttribute(attribute);
	}
	
	public String getCssValue(WebElement element, String propertyName) {
		element = wait.until(ExpectedConditions.visibilityOf(element));
		return element.getCssValue(propertyName);
	}
	
	public String getCssValue(By locator, String propertyName) {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		return element.getCssValue(propertyName);
	}
	
	public boolean isDisplayed(WebElement element) {
		element = wait.until(ExpectedConditions.visibilityOf(element));
		return element.isDisplayed();
	}
	
	public boolean isDisplayed(By locator) {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		return element.isDisplayed();
	}
	
	public boolean isEnabled(WebElement element) {
		element = wait.until(ExpectedConditions.visibilityOf(element));
		return element.isEnabled();
	}
	
	public boolean isEnabled(By locator) {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		return element.isEnabled();
	}
	
	public void scrollIntoView(WebElement element) {
		element = wait.until(ExpectedConditions.visibilityOf(element));
		jsExecutor.executeScript("arguments[0].scrollIntoView(true)", element);
	}
	
	public void scrollIntoView(By locator) {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		jsExecutor.executeScript("arguments[0].scrollIntoView(true)", element);
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