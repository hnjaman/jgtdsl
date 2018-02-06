package org.jgtdsl.utils.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
	
public class ExtConnectionManager {
	
    String username;
    String password;
    String url;
	Connection connection = null;

	
	public ExtConnectionManager(String connectionString,String userName,String password)
	{
		this.username=userName;
		this.password=password;
		this.url=connectionString;
		try
		{
			// Class.forName("com.mysql.jdbc.Driver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public Connection getConnection()
	{

		try
		{
			if (connection == null)
				connection = DriverManager.getConnection(url, username,
						password);

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return connection;
	}
	
	public void closeConnection(Connection connection)
	{
		try
		{
			connection.close();
			connection = null;
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			connection = null;
		}
	}
	
	


}
