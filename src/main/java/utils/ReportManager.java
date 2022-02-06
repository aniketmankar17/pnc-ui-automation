package utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ReportManager 
{
	private static ThreadLocal<ExtentReports> report = new ThreadLocal<>();
	private static ThreadLocal<ExtentTest> testStep = new ThreadLocal<>();
	
	private ReportManager() {}
	
	public static synchronized void configureReport(String methodName)
	{
		try {
			String filePath = System.getProperty("user.dir") + "/Reports/";
			File directory = new File(filePath);
			if(!directory.exists())
				directory.mkdir();
			String fileName = methodName+"_"+new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())+".html";
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(filePath+fileName);
			sparkReporter.config().setCss(".r-img { width: 30%; }");
			sparkReporter.config().setTimelineEnabled(true);
			sparkReporter.config().setDocumentTitle("Automation Report");
			sparkReporter.config().setReportName("Test Report");
			sparkReporter.config().setTheme(Theme.STANDARD);
			sparkReporter.config().setTimeStampFormat("d-MMM-yyyy hh:mm:ss z");
			report.set(new ExtentReports());
			report.get().attachReporter(sparkReporter);
			report.get().setSystemInfo("OS", System.getProperty("os.name"));
			report.get().setSystemInfo("Java", System.getProperty("java.version"));
			report.get().setSystemInfo("Browser", ConfigFileReader.getConfigReader().getBrowser());
		} catch (Exception e) {
			Assert.fail("Failed to configure Test Report for: "+methodName, e);
		}
	}
	
/*	public static synchronized ExtentReports getExtentReport()
	{
		return report.get();
	}
*/	
	public static synchronized void createReportForTest(String testName)
	{
		if(testStep.get()==null)
			testStep.set(report.get().createTest(testName));
	}

	public static synchronized ExtentTest getExtentTest()
	{
		return testStep.get();
	}
	
	public static synchronized void flushReport()
	{
		//testStep.remove();
		if(report.get()!=null)
		{
			report.get().flush();
			report.remove();
		}
	}
	
	public static synchronized void endReportForTest()
	{
		testStep.remove();
	}
}
