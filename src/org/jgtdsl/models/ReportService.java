package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.dto.GasPurchaseDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.TariffDTO;
import org.jgtdsl.enums.BankAccountTransactionType;
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.connection.TransactionManager;

public class ReportService {
	public ResponseDTO saveAdjustmentAccountPayable(TariffDTO adjustmentInfo)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
		
		PreparedStatement stmt = null;
		PreparedStatement pid_stmt = null;
		
		ResultSet r = null;
		String pid=null;
		int count=0;
		String insertSql="";
		String updateSql="";
		
		
		try
			{
				  	String checkIsAvil="select COUNT(Month) COUNT from ADJUSTMENT_ACCOUNT_PAYABLE"+
										"where month=3"+
										"and year=2016";
				  	stmt = conn.prepareStatement(checkIsAvil);	
				  	r=stmt.executeQuery();
					while(r.next())
		        	{
						count=r.getInt("COUNT");
		        		
		        	}
			       

					
					if(count<0){
						insertSql="INSERT INTO ADJUSTMENT_ACCOUNT_PAYABLE (MONTH, YEAR, BGFCL,SGFL, PDF,BAPEX, GTCL,DWELLHEAD, GD_FUND, AVALUE)" +
                                "VALUES ( ?,?,?,?,?,?,?,?,?,?)";
						
						stmt = conn.prepareStatement(insertSql);				
						stmt.setString(1,adjustmentInfo.getMonth());
						stmt.setString(2,adjustmentInfo.getYear());					
						stmt.setDouble(3,adjustmentInfo.getBgfcl());
						stmt.setDouble(4,adjustmentInfo.getSgfl());
						stmt.setDouble(5,adjustmentInfo.getPdf());
						stmt.setDouble(6,adjustmentInfo.getBapex());
						stmt.setDouble(7,adjustmentInfo.getTrasmission());
						stmt.setDouble(8,adjustmentInfo.getDwellhead());		
						stmt.setDouble(9,adjustmentInfo.getGdfund());	
						stmt.setDouble(10,adjustmentInfo.getAvalue());
						stmt.executeUpdate();
					}else
					{
						insertSql="INSERT INTO ADJUSTMENT_ACCOUNT_PAYABLE (MONTH, YEAR, BGFCL,SGFL, PDF,BAPEX, GTCL,DWELLHEAD, GD_FUND, AVALUE)" +
                                "VALUES ( ?,?,?,?,?,?,?,?,?,?)";
						
						stmt = conn.prepareStatement(insertSql);				
						stmt.setString(1,adjustmentInfo.getMonth());
						stmt.setString(2,adjustmentInfo.getYear());					
						stmt.setDouble(3,adjustmentInfo.getBgfcl());
						stmt.setDouble(4,adjustmentInfo.getSgfl());
						stmt.setDouble(5,adjustmentInfo.getPdf());
						stmt.setDouble(6,adjustmentInfo.getBapex());
						stmt.setDouble(7,adjustmentInfo.getTrasmission());
						stmt.setDouble(8,adjustmentInfo.getDwellhead());		
						stmt.setDouble(9,adjustmentInfo.getGdfund());	
						stmt.setDouble(10,adjustmentInfo.getAvalue());
						stmt.executeUpdate();
					}
					
					
					
					
					

				
				transactionManager.commit();
				
				response.setMessasge("Successfully saved Adjustment information.");
				response.setResponse(true);
				

			} 
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return response;

	}
	
}
