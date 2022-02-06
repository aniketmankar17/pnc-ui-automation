package utils;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public class BaseTestMethods 
{	
	@BeforeClass
	public synchronized void setupReport()
	{
		System.out.println("-----Before Class----");
		ReportManager.configureReport("Report_");
	}
	
	@AfterClass
	public synchronized void closeReport()
	{
		System.out.println("-----After Class----");
		ReportManager.flushReport();
	}
	
	@AfterMethod
	public synchronized void cleanUp(ITestResult result)
	{
		DriverBuilder.closeDriver();
		ReportManager.endReportForTest();
	}
}