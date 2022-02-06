package utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class TestListener implements ITestListener
{	
	private ExtentTest testStep;

	@Override
	public synchronized void onTestStart(ITestResult result) 
	{
		System.out.println("New Test Started: " +result.getName());
	//	ReportManager.configureReport(result.getName());
		ReportManager.createReportForTest(result.getName());
	}

	@Override
	public synchronized void onTestSuccess(ITestResult result) 
	{
		System.out.println("Test Passed: "+result.getName());
		testStep = ReportManager.getExtentTest();
		String destination = takeScreenshotAndGetDestination();
		testStep.pass("Test Passed", MediaEntityBuilder.createScreenCaptureFromPath(destination).build());
	}

	@Override
	public synchronized void onTestFailure(ITestResult result) 
	{	
		System.out.println("Test Failed: "+result.getName());
		String failCause="";
		testStep = ReportManager.getExtentTest();
		String destination = takeScreenshotAndGetDestination();
		String failMsg = result.getThrowable().getMessage();
		if(result.getThrowable().getCause()!=null) 
		{
			failCause = result.getThrowable().getCause().toString();
			testStep.fail(failMsg+": "+failCause, MediaEntityBuilder.createScreenCaptureFromPath(destination).build());
		}
		else
			testStep.fail(failMsg, MediaEntityBuilder.createScreenCaptureFromPath(destination).build());
		testStep.fail("Test Failed");
	}

	@Override
	public synchronized void onTestSkipped(ITestResult result) 
	{	
		System.out.println("Test Skipped" +result.getName());
		testStep = ReportManager.getExtentTest();
		testStep.skip("Test Skipped: "+result.getThrowable().getCause().toString());
	}

	@Override
	public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) 
	{
		System.out.println("Test Failed but within success percentage" +result.getName());
	}

	@Override
	public synchronized void onStart(ITestContext context) 
	{	
		System.out.println("This is onStart method" +context.getOutputDirectory());	
	}

	@Override
	public synchronized void onFinish(ITestContext context) 
	{		
		System.out.println("This is onFinish method" +context.getPassedTests());
		System.out.println("This is onFinish method" +context.getFailedTests());
	}

	private synchronized String takeScreenshotAndGetDestination()
	{
		String destination = "";
		try {	
			WebDriver driver = DriverBuilder.getDriver();
			String screenshotDir = System.getProperty("user.dir") + "/Screenshots/";
			File directory = new File(screenshotDir);
			if(!directory.exists())
				directory.mkdir();
			String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			destination = screenshotDir+"Screenshot_"+dateName+".png";
			File finalDestination = new File(destination);
			Screenshot s=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(500)).takeScreenshot(driver);
			ImageIO.write(s.getImage(),"PNG", finalDestination);
		} catch (WebDriverException e) {
			throw new WebDriverException(e);
		} catch (IOException e) {
			Assert.fail("IOException occurred: ", e);
		}
		return destination;
	}
}