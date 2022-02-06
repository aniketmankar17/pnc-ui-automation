package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;

public class DatabaseMethods 
{
	private Connection conn;
	private ExtentTest testStep;
	private String user = ConfigFileReader.getConfigReader().getProperty("dbUser");
	private String password = ConfigFileReader.getConfigReader().getProperty("dpPassword");
	
	public DatabaseMethods(ExtentTest testStep)
	{
		this.testStep = testStep;
	}
	
	public DatabaseMethods connectToDatabase()
	{
		String dbType = ConfigFileReader.getConfigReader().getProperty("dbType");
		switch(dbType.toUpperCase())
		{
			case "ORACLE":
				connectToOracleDatabase();
				break;
				
			case "MYSQL":
				connectToMySqlDatabase();
				break;
				
			case "PSQL":
				connectToPgSqlDatabase();
				break;
				
			default:
				Assert.fail("Database "+dbType+" not available", new RuntimeException("Database "+dbType+" not available"));
		}
		return this;
	}
	
	private DatabaseMethods connectToOracleDatabase()
	{
		try {
			String oracleUrl = ConfigFileReader.getConfigReader().getProperty("oracleUrl");
			conn = DriverManager.getConnection(oracleUrl, user, password);
			testStep.info("Connection successful to Oracle database with user ["+user+"]");
		} catch (SQLException e) {
			Assert.fail("Failed connecting to Oracle database", e);
		}
		return this;
	}
	
	private DatabaseMethods connectToMySqlDatabase()
	{
		try {
			String mySqlUrl = ConfigFileReader.getConfigReader().getProperty("mySqlUrl");
			conn = DriverManager.getConnection(mySqlUrl, user, password);
			testStep.info("Connection successful to MySQL database with user ["+user+"]");
		} catch (SQLException e) {
			Assert.fail("Failed connecting to MySQL database", e);
		}
		return this;
	}
	
	private DatabaseMethods connectToPgSqlDatabase()
	{
		try {
			String pgSqlUrl = ConfigFileReader.getConfigReader().getProperty("pgSqlUrl");
			conn = DriverManager.getConnection(pgSqlUrl, user, password);
			testStep.info("Connection successful to PostgreSQL database with user ["+user+"]");
		} catch (SQLException e) {
			Assert.fail("Failed connecting to PostgreSQL database", e);
		}
		return this;
	}
	
	public ResultSet executeQuery(String query)
	{
		try (Statement stmt = conn.createStatement()){
			return stmt.executeQuery(query);
		} catch (SQLException e) {
			Assert.fail("Failed to execute query ["+query+"]", e);
		}
		return null;
	}
	
	public int executeUpdate(String query)
	{
		int status = 2;
		try (Statement stmt = conn.createStatement()){
			status = stmt.executeUpdate(query);
		} catch (SQLException e) {
			Assert.fail("Failed to execute update ["+query+"]", e);
		}
		return status;
	}
	
	public int getTotalRowsCount(String query)
	{
		int rowCount = 0;
		try {
			ResultSet rs = executeQuery(query);
			rs.last();
			rowCount = rs.getRow();
		} catch (SQLException e) {
			Assert.fail("Failed to fetch total row count for query ["+query+"]", e);
		}
		return rowCount;
	}
	
	public int getTotalColumnsCount(String query)
	{
		int colCount = 0;
		try {
			ResultSet rs = executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			colCount = rsmd.getColumnCount();
		} catch (SQLException e) {
			Assert.fail("Failed to fetch total column count for query ["+query+"]", e);
		}
		return colCount;
	}
	
	public List<String> getColumnNames(String query)
	{
		try {
			ResultSet rs = executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			List<String> columns = new ArrayList<>();
			for(int i=1; i<=colCount; i++)
			{
				columns.add(rsmd.getColumnName(i));
			}
			return columns;
		} catch (SQLException e) {
			Assert.fail("Failed to fetch column names for query ["+query+"]", e);
		}
		return Collections.emptyList();
	}
}