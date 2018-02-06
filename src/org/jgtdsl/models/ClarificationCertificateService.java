package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import oracle.jdbc.driver.OracleCallableStatement;

import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.TransactionManager;

public class ClarificationCertificateService {

	public static ResponseDTO approveCC(String customer_id,String issue_date,String area,String category,String nondistributed,String approve_type)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		PreparedStatement dtl_stmt = null;
		String update_sql="update CLARIFICATION_HISTORY set Status=0 where customer_id=? and ISSUE_DATE=to_date(?,'dd-MM-yyyy') ";
		String[] code = null;
		ArrayList<String> codeList=new ArrayList<String>();
				
		if(approve_type.equals("app_individual_wise")){		
			codeList.add(customer_id);		
		}else if(approve_type.equals("app_category_wise")){
			code=nondistributed.split(",");
			codeList= new ArrayList<String>(Arrays.asList(code));
		}
		try {
			dtl_stmt = conn.prepareStatement(update_sql);
			for(String cus_id:codeList){
				dtl_stmt.setString(1,Utils.getCustomerID(area, category, cus_id));
				dtl_stmt.setString(2,issue_date);
				dtl_stmt.addBatch();
			}
			dtl_stmt.executeBatch();
			
			transactionManager.commit();
			
			response.setMessasge("Successfully Approved Information.");
			response.setResponse(true);
			
			
		} catch (Exception e){
			
			e.printStackTrace();
				try {
					transactionManager.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
		
        return response;
	}

}
