package org.jgtdsl.utils.connection;


public class TransactionManager {
	
	private ConnectionManager manager;
	private java.sql.Connection connection;

	public ConnectionManager getManager()
	{
		return manager;
	}

	public void setManager(ConnectionManager manager)
	{
		this.manager = manager;
	}

	public java.sql.Connection getConnection()
	{
		return connection;
	}

	public void setConnection(java.sql.Connection connection)
	{
		this.connection = connection;
	}

	public TransactionManager()
	{
		try
		{
			connection = ConnectionManager.getConnection();
			connection.setAutoCommit(false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public void commit()
	{
		try
		{
			connection.commit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void rollback()
	{
		try
		{
			connection.rollback();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void close()
	{
		try
		{
			connection.setAutoCommit(true);
			connection.close();
			//manager.closeConnection(connection);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
