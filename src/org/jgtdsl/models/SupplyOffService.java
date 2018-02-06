package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;

import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.SupplyOffDTO;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;

public class SupplyOffService {

	public static ResponseDTO saveSupplyOff(SupplyOffDTO supplyOff)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int resp_code=0;
		String resp_msg=Utils.EMPTY_STRING;
		try {
       	 
   			//System.out.println("===>>Procedure : [SaveSupplyOff] START");            
            stmt = (OracleCallableStatement) conn.prepareCall("{ call SaveSupplyOff(?,?,?,?,?,?,?,?,?,?,?)  }");
           // System.out.println("==>>Procedure : [SaveSupplyOff] END");            
            stmt.setString(1, supplyOff.getOff_for());
            stmt.setString(2, supplyOff.getCustomer_id());
            stmt.setString(3, supplyOff.getArea_id());
            stmt.setString(4, supplyOff.getCustomer_category());
            stmt.setString(5, supplyOff.getBilling_month());
            stmt.setString(6, supplyOff.getBilling_year());
            stmt.setString(7, supplyOff.getFrom_date());
            stmt.setString(8, supplyOff.getTo_date());
            stmt.setString(9, supplyOff.getRemarks());

            stmt.registerOutParameter(10, java.sql.Types.INTEGER);
            stmt.registerOutParameter(11, java.sql.Types.VARCHAR);
            stmt.executeUpdate();
            resp_code = stmt.getInt(10);
            resp_msg = stmt.getString(11);
            if(resp_code==1){
			response.setMessasge("Successfully Saved Supply Of Information.");
			response.setResponse(true);
            }
            else{
            	response.setMessasge(resp_msg);
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
	public ArrayList<SupplyOffDTO> getSupplyOffList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		SupplyOffDTO sOff=null;
		ArrayList<SupplyOffDTO> supplyOffList=new ArrayList<SupplyOffDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		
		if(total==0)
				  sql =   " Select SF.*,area,area_name from VIEW_SUPPLY_OFF,CUSTOMER,MST_AREA "+
				  " WHERE SF.customer_id=customer.customer_id and mst_area.area_id=customer.area "+
				  (whereClause.equalsIgnoreCase("")?"":(" And "+whereClause+" "))+" ";
		else
			 sql=" Select * from ( " +
		  	  " Select rownum serial,tmp1.* from " +
		  	  " ( Select SF.*,area,area_name from VIEW_SUPPLY_OFF,CUSTOMER,MST_AREA "+
		  	  " WHERE SF.customer_id=customer.customer_id and mst_area.area_id=customer.area "+
		  	  (whereClause.equalsIgnoreCase("")?"":(" And  "+whereClause+" "))+" "+orderByQuery+			  	  
		  	  "    )tmp1 " +
		  	  "    )tmp2   " +
		  	  "  Where serial Between ? and ? ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				if(total!=0)
				{
				stmt.setInt(1, index);
				stmt.setInt(2, index+offset);
				}
				r = stmt.executeQuery();
				while (r.next())
				{
					sOff=new SupplyOffDTO();
					sOff.setCustomer_id(r.getString("CUSTOMER_ID"));
					sOff.setFull_name(r.getString("FULL_NAME"));
					sOff.setMonth_year(r.getString("MONTH_YEAR"));
					sOff.setFrom_date(r.getString("FROM_DATE"));
					sOff.setTo_date(r.getString("TO_DATE"));
					sOff.setRemarks(r.getString("REMARKS"));
					
					supplyOffList.add(sOff);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return supplyOffList;
	}	
}
