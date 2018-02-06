package org.jgtdsl.models;

import java.sql.Connection;

import oracle.jdbc.driver.OracleCallableStatement;

import org.jgtdsl.dto.JvBankAccountCorrectionDTO;
import org.jgtdsl.dto.JvCustomerAccountCorrectionDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.utils.connection.ConnectionManager;

public class JournalVoucherService {

	public ResponseDTO saveCustomerAccountCorrection(JvCustomerAccountCorrectionDTO correction)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int response_code=0;
		
		try {
       	    			
   			//System.out.println("===>>Procedure : [JV_CUSTOMER_ACC_CORRECTION] START");  
   			stmt = (OracleCallableStatement) conn.prepareCall("{ call JV_CUSTOMER_ACC_CORRECTION(?,?,?,?,?,?,?,?,?)  }");
   		   // System.out.println("==>>Procedure : END");
          
   	
            
            stmt.setString(1, correction.getOld_customer_id());           
            stmt.setString(2, correction.getNew_customer_id());
            stmt.setString(3, correction.getBill_month());
            stmt.setString(4, correction.getBill_year());
            stmt.setString(5, correction.getNew_bill_month());
            stmt.setString(6, correction.getNew_bill_year());
            stmt.setString(7, correction.getInserted_by());      
            stmt.registerOutParameter(8, java.sql.Types.INTEGER);
            stmt.registerOutParameter(9, java.sql.Types.VARCHAR);
            
            stmt.executeUpdate();
            response_code = stmt.getInt(8);
            response.setMessasge(stmt.getString(9));
            
            if(response_code == 1){
            	response.setResponse(true);
            }
            else{            	
            	response.setResponse(false);
            }
		} 
		catch (Exception e){e.printStackTrace();
		response.setMessasge(e.getMessage());
		response.setResponse(false);
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		
        
        return response;
        

	}
	
	public ResponseDTO saveBankAccountCorrection(JvBankAccountCorrectionDTO correction)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int response_code=0;
		
		try {
       	    			
   			//System.out.println("===>>Procedure : [JV_BANK_ACC_CORRECTION] START");  
   			stmt = (OracleCallableStatement) conn.prepareCall("{ call JV_BANK_ACC_CORRECTION(?,?,?,?,?,?,?,?,?)  }");
   		    //System.out.println("==>>Procedure : END");
          
   	
            
            stmt.setString(1, correction.getCustomer_id());           
            stmt.setString(2, correction.getBill_month());
            stmt.setString(3, correction.getBill_year());
            stmt.setString(4, correction.getNew_bank_id());
            stmt.setString(5, correction.getNew_branch_id());
            stmt.setString(6, correction.getNew_account_no());
            stmt.setString(7, correction.getInserted_by());      
            stmt.registerOutParameter(8, java.sql.Types.INTEGER);
            stmt.registerOutParameter(9, java.sql.Types.VARCHAR);
            
            stmt.executeUpdate();
            response_code = stmt.getInt(8);
            response.setMessasge(stmt.getString(9));
            
            if(response_code == 1){
            	response.setResponse(true);
            }
            else{            	
            	response.setResponse(false);
            }
		} 
		catch (Exception e){e.printStackTrace();
		response.setMessasge(e.getMessage());
		response.setResponse(false);
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		
        
        return response;
        

	}
	
}
