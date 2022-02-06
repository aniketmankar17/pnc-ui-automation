package utils;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

public class ExcelReader
{
	private static String testDataFile;
	private static String testDataSheet;

	private ExcelReader()
	{}

	public static void setTestData(String fileName, String sheetName)
	{
		testDataFile = fileName;
		testDataSheet = sheetName;
	}

	@DataProvider(name = "testData")
	public static Object[][] getData(Method m)
	{
		try {
			String dataProviderFile = System.getProperty("user.dir") + "/src/main/resources/data/" + testDataFile;
			String testName = m.getName();
			Object[][] testData = getMultipleDataRows(dataProviderFile, testDataSheet, testName);
			if(testData.length!=0)
			{	
				Map<String, String> tcMap = null;
				Object[][] testDataOutput = new Object[testData.length-1][1];
				for(int i=1; i<testData.length; i++)
				{
					tcMap = new HashMap<>();
					for(int j=0; j<testData[i].length; j++) 
					{
						if(testData[i][j]==null)
							tcMap.put(testData[0][j].toString(), "");
						else
							tcMap.put(testData[0][j].toString(), testData[i][j].toString());
					}
					testDataOutput[i-1][0] = tcMap;
				}
				return testDataOutput;
			}
		} catch (NullPointerException e) {
		}
		return new Object[0][0];
	}

	private static String[][] getMultipleDataRows(String dataFile, String dataSheet, String tcName)
	{
		try {
			FileInputStream fileInputStream = new FileInputStream(dataFile);
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheet(dataSheet);
			String[][] dataTable = null;
			int totalRows = sheet.getLastRowNum();
			int totalCols = sheet.getRow(0).getLastCellNum();
			int actualTestDataRows = 1;
			String rowIndexSplit = null;
			for(int p = 1; p <= totalRows; p++)
			{
				XSSFRow row = sheet.getRow(p);
				XSSFCell testName = row.getCell(0);
				XSSFCell flag = row.getCell(1);

				if(testName.getStringCellValue().equalsIgnoreCase(tcName) && flag.getStringCellValue().equalsIgnoreCase("Y"))
				{
					actualTestDataRows++;
					if(actualTestDataRows == 2)
						rowIndexSplit = testName.getRowIndex() + "";
					else
						rowIndexSplit = rowIndexSplit + "," + testName.getRowIndex() + "";
				}
			}
			dataTable = new String[actualTestDataRows][totalCols+1];
			String[] rowIndexes = rowIndexSplit.split(",");
			for(int i = 0; i <= totalCols; i++)
			{
				XSSFRow row = sheet.getRow(0);
				if(i == totalCols)
					dataTable[0][i] = "Row_Number";
				else
				{
					XSSFCell cell = row.getCell(i);
					dataTable[0][i] = cell.getStringCellValue();
				}
			}
			for(int i = 0; i < rowIndexes.length; i++)
			{
				int rowNum = Integer.parseInt(rowIndexes[i]);
				XSSFRow row = sheet.getRow(rowNum);
				XSSFCell cell = row.getCell(0);
				if(cell.getStringCellValue().equals(tcName))
				{
					for(int j = 0; j <= totalCols; j++)
					{
						if(j == totalCols)
							dataTable[i+1][j] = rowIndexes[i];
						else
						{
							cell = row.getCell(j);
							if(cell == null)
								dataTable[i+1][j] = null;
							else
							{
								if(cell.getCellType() == CellType.STRING)
									dataTable[i+1][j] = cell.getStringCellValue();
								else if(cell.getCellType() == CellType.NUMERIC)
									dataTable[i+1][j] = String.valueOf(cell.getNumericCellValue());
								else if(cell.getCellType() == CellType.FORMULA)
									dataTable[i+1][j] = cell.getRawValue();
								else if(cell.getCellType() == CellType.BOOLEAN)
									dataTable[i+1][j] = String.valueOf(cell.getBooleanCellValue());
								else if(cell.getCellType() == CellType.BLANK)
									dataTable[i+1][j] = "";
							}
						}
					}
				}
			}
			workbook.close();
			return dataTable;
		} catch (Exception e) {
			return new String[0][0];
		}
	}
}