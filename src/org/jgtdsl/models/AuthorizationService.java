package org.jgtdsl.models;

import java.sql.Connection;

import oracle.jdbc.driver.OracleCallableStatement;

import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.utils.connection.ConnectionManager;

public class AuthorizationService {

	public ResponseDTO autorizeTransaction(String bank_id,String branch_id,String account_no,String trans_date)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int response_code=0;
		
		try {
       	    			
   			//System.out.println("===>>Procedure : [Apporove_Transactions] START");  
   			stmt = (OracleCallableStatement) conn.prepareCall("{ call Apporove_Transactions(?,?,?,?,?,?)  }");
   		   // System.out.println("==>>Procedure : END");
          
            stmt.setString(1, bank_id);           
            stmt.setString(2, branch_id);
            stmt.setString(3, account_no);
            stmt.setString(4, trans_date);
            stmt.registerOutParameter(5, java.sql.Types.INTEGER);
            stmt.registerOutParameter(6, java.sql.Types.VARCHAR);
            
            stmt.executeUpdate();
            response_code = stmt.getInt(5);
            response.setMessasge(stmt.getString(6));
            
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
