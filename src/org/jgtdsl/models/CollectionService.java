package org.jgtdsl.models;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

import oracle.jdbc.driver.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.MultiCollStrDTO;
import org.jgtdsl.dto.MultiCollectionDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.BankAccountTransactionType;
import org.jgtdsl.enums.MeteredStatus;
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;



public class CollectionService {

	UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
	
	
	/* Delete Wrong Collection
	 *  @sujon
	 * */	
	
	public ResponseDTO executeDeleteCollection(String scroll_no){
		ResponseDTO response = new ResponseDTO();
		int response_code=0;
	 	String response_msg="";
		
	 	TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
	 	
		//Connection conn = ConnectionManager.getConnection();
	 	OracleCallableStatement stmt=null;
	 	
	 	
	 	try{
		  
	 				stmt = (OracleCallableStatement) conn.prepareCall(
					 	  "{ call ADVANCED_COLLECTION_DELETE	(?,?,?) }");
		    		

					stmt.setString(1, scroll_no);
				
		    		
		    		stmt.registerOutParameter(2, java.sql.Types.INTEGER);
					stmt.registerOutParameter(3, java.sql.Types.VARCHAR);
					
					stmt.executeUpdate();
					transactionManager.commit();
					response_code = stmt.getInt(2);
					response_msg = (stmt.getString(3)).trim();
					
					response.setMessasge(response_msg);
					response.setResponse(response_code==1?true:false);
	 			
	 		
	 		
	    		    	
		  } 
		catch (Exception e){e.printStackTrace();response.setResponse(false);response.setMessasge(e.getMessage());return response;}
		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
	 	
	 	
		return response;
	}
	
	public ArrayList<CollectionDTO> getCollectionList4Delete(String collection_date,String bank_id,String branch_id, String account_id){
		
		CollectionDTO collection=new CollectionDTO();
		ArrayList<CollectionDTO> collectionList=new ArrayList<CollectionDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql="SELECT scroll_no, " +
					"       customer_id, " +
					"       customer_name, " +
					"       month_from, " +
					"       year_from, " +
					"       month_to, " +
					"       year_to, " +
					"       collected_amount, " +
					"       collected_surcharge, " +
					"       bank_id, " +
					"       branch_id " +
					"  FROM daily_collection_grid " +
					" WHERE     COLLECTION_DATE = TO_DATE (?, 'dd-mm-yyyy') " +
					"       AND bank_id = ? " +
					" 		and branch_id=? " +
					" 		and account_no=? " +
					"		order by scroll_no desc" ;
		
		
		PreparedStatement stmt = null;
		ResultSet r = null;
		
			try
			{
				stmt = conn.prepareStatement(sql);
								
				stmt.setString(1,collection_date);
				stmt.setString(2, bank_id);
				stmt.setString(3, branch_id);
				stmt.setString(4, account_id);
												
				r = stmt.executeQuery();
				while (r.next())
				{	
					collection=new CollectionDTO();
						
					collection.setScroll_no(r.getString("SCROLL_NO"));
					collection.setCustomer_id(r.getString("CUSTOMER_ID"));
					collection.setCustomer_name(r.getString("CUSTOMER_NAME"));
					collection.setMonth_from(r.getString("MONTH_FROM"));
					collection.setMonth_to(r.getString("MONTH_TO"));
					collection.setYear_from(r.getString("YEAR_FROM"));
					collection.setYear_to(r.getString("YEAR_TO"));
					collection.setCollection_amount(r.getDouble("COLLECTED_AMOUNT"));
					collection.setSurcharge_collected(r.getDouble("COLLECTED_SURCHARGE"));
					collection.setBank_id(r.getString("BANK_ID"));
					collection.setBranch_id(r.getString("BRANCH_ID"));
						
					collectionList.add(collection);
				}
			

			} 
			catch (Exception e){e.printStackTrace();
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return collectionList;
		
	}
	
	
	public ArrayList<CollectionDTO> getCustomerInfo(String customer_id){
				
		CollectionDTO collection=new CollectionDTO();
		ArrayList<CollectionDTO> custInfo=new ArrayList<CollectionDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql="SELECT * FROM MVIEW_CUSTOMER_INFO where customer_id= ? ";
		
		
		PreparedStatement stmt = null;
		ResultSet r = null;
		
			try
			{
				stmt = conn.prepareStatement(sql);
								
					stmt.setString(1,customer_id);
												
				r = stmt.executeQuery();
				while (r.next())
				{	
					collection=new CollectionDTO();
						
					collection.setCustomer_id(r.getString("CUSTOMER_ID"));
					collection.setCustomer_name(r.getString("FULL_NAME"));
					collection.setFather_name(r.getString("FATHER_NAME"));
					collection.setCategory_id(r.getString("CATEGORY_ID"));
					collection.setCategory_name(r.getString("CATEGORY_NAME"));
					collection.setIs_metered(r.getString("ISMETERED"));
					collection.setPhone(r.getString("PHONE"));
					collection.setMobile(r.getString("MOBILE"));
					collection.setAddress(r.getString("ADDRESS"));
					collection.setArea_name(r.getString("AREA_NAME"));
					collection.setIsMetere(String.valueOf(MeteredStatus.values()[r.getInt("ISMETERED")].getId()));
					collection.setIsMeter_str(MeteredStatus.values()[r.getInt("ISMETERED")].getLabel());
					collection.setArea_id(r.getString("AREA_ID"));
						
					custInfo.add(collection);
				}
			

			} 
			catch (Exception e){e.printStackTrace();
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return custInfo;
		
	}
// testing for codeless collection
	
	public ArrayList<CollectionDTO> getAccountwiseDailyCodelessCollectionList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		
		CollectionDTO collection=null;
		ArrayList<CollectionDTO> collectionList=new ArrayList<CollectionDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		
		if(whereClause.equals(""))
		{
			return null;
		}
		if(total==0)
			sql=" SELECT * FROM CODELESS_PAYMENT"+
					" WHERE "+(whereClause.equalsIgnoreCase("")?"":("   "+whereClause+" "))+
					"ORDER BY CODELESS_NO ASC " ;
		else
			sql="SELECT ROWNUM SERIAL, TMP1.* " +
					"  FROM (SELECT * " +
					"          FROM CODELESS_PAYMENT " +
					"         WHERE "+ (whereClause.equalsIgnoreCase("")?"":("   "+whereClause+" ")) +
					"          )TMP1 " +
					"               WHERE ROWNUM BETWEEN ? AND ? " +
					"               ORDER BY TMP1.CODELESS_NO DESC " ;
		   
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
					collection=new CollectionDTO();
					
					collection.setCustomer_id(r.getString("CUSTOMER_ID"));
					collection.setCustomer_name(r.getString("CUSTOMER_NAME"));
					collection.setFrom_month(r.getString("MONTH_FROM")+" - "+r.getString("YEAR_FROM"));
					collection.setFrom_year(r.getString("YEAR_FROM"));
					collection.setTo_month(r.getString("MONTH_TO")+" - "+r.getString("YEAR_TO"));
					collection.setTo_year(r.getString("YEAR_TO"));
					collection.setAdvanced_amount(r.getDouble("COLLECTED_AMOUNT"));
					collection.setSurcharge_amount(r.getDouble("COLLECTED_SURCHARGE"));
					collection.setCollection_date(r.getString("COLLECTION_DATE"));
					collection.setBank_id(r.getString("BANK_ID"));
					collection.setBranch_id(r.getString("BRANCH_ID"));
					collection.setAccount_no(r.getString("ACCOUNT_NO"));
					collection.setArea_id(r.getString("AREA_ID"));
					collection.setInserted_by(r.getString("INSERTED_BY"));
					collection.setStatusId(r.getString("STATUS"));
					collection.setScroll_no(r.getString("CODELESS_NO"));
					collection.setAddress(r.getString("ADDRESS"));
					collectionList.add(collection);
				
					
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return collectionList;
	}
	
	
//end of : testing for codeless collection
	
	
	public ArrayList<CollectionDTO> getAccountwiseDailyCollectionList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		
		CollectionDTO collection=null;
		ArrayList<CollectionDTO> collectionList=new ArrayList<CollectionDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		
		if(whereClause.equals(""))
		{
			return null;
		}
		if(total==0)
				  sql ="SELECT * FROM (SELECT SCROLL_NO COLLECTION_ID,CUSTOMER_ID,CUSTOMER_NAME,(select MON from mst_month where M_ID=MONTH_FROM)||'-'||YEAR_FROM MONTH_FROM,YEAR_FROM,(select MON from mst_month where M_ID=MONTH_TO)||'-'||YEAR_TO MONTH_TO,YEAR_TO,COLLECTED_AMOUNT,COLLECTED_SURCHARGE,to_char(COLLECTION_DATE,'dd-mm-yyyy') COLLECTION_DATE,BANK_ID,STATUS,INSERTED_BY,INSERTED_ON,BRANCH_ID,ACCOUNT_NO,AREA_ID " +
						  "                              FROM DAILY_COLLECTION_GRID bcm " +
						  "                             WHERE "+(whereClause.equalsIgnoreCase("")?"":("   "+whereClause+" "))+") ORDER BY COLLECTION_ID ASC ";
		else
			sql="  SELECT * " +
					"    FROM (SELECT ROWNUM serial, tmp1.* " +
					"            FROM (  SELECT * " +
					"                      FROM (SELECT SCROLL_NO COLLECTION_ID,CUSTOMER_ID,CUSTOMER_NAME,(select MON from mst_month where M_ID=MONTH_FROM)||'-'||YEAR_FROM MONTH_FROM,YEAR_FROM,(select MON from mst_month where M_ID=MONTH_TO)||'-'||YEAR_TO MONTH_TO,YEAR_TO,COLLECTED_AMOUNT,COLLECTED_SURCHARGE,to_char(COLLECTION_DATE,'dd-mm-yyyy') COLLECTION_DATE,BANK_ID,STATUS,INSERTED_BY,INSERTED_ON,BRANCH_ID,ACCOUNT_NO,AREA_ID " +
					"                              FROM DAILY_COLLECTION_GRID bcm " +
					"                             WHERE "+(whereClause.equalsIgnoreCase("")?"":("   "+whereClause+" "))+" ) ORDER BY INSERTED_ON ASC) tmp1) tmp2 " +
					"   WHERE serial BETWEEN ? AND ? " +
					"ORDER BY COLLECTION_ID ASC " ;
		   
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
					collection=new CollectionDTO();
					
					collection.setCustomer_id(r.getString("CUSTOMER_ID"));
					collection.setCustomer_name(r.getString("CUSTOMER_NAME"));
					collection.setFrom_month(r.getString("MONTH_FROM"));
					collection.setFrom_year(r.getString("YEAR_FROM"));
					collection.setTo_month(r.getString("MONTH_TO"));
					collection.setTo_year(r.getString("YEAR_TO"));
					collection.setAdvanced_amount(r.getDouble("COLLECTED_AMOUNT"));
					collection.setSurcharge_amount(r.getDouble("COLLECTED_SURCHARGE"));
					collection.setCollection_date(r.getString("COLLECTION_DATE"));
					collection.setBank_id(r.getString("BANK_ID"));
					collection.setBranch_id(r.getString("BRANCH_ID"));
					collection.setAccount_no(r.getString("ACCOUNT_NO"));
					collection.setArea_id(r.getString("AREA_ID"));
					collection.setInserted_by(r.getString("INSERTED_BY"));
					collection.setStatusId(r.getString("STATUS"));
					collectionList.add(collection);
				
					
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return collectionList;
	}
	
	
	

	
	
	
	
	
	public ArrayList<CollectionDTO> getBillingInfo(String customer_id,String bill_month,String bill_year,String collection_date)
	{
		
		//Not a good solution. But in future we need to refactor it.
		CustomerService customerService=new CustomerService();
		String tableName="";
		CustomerDTO customer=customerService.getCustomerInfo(customer_id);
		if(customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered"))
			tableName="VIEW_METER_BILLINFO";
		else
			tableName="BILL_NON_METERED";
		
		//End of bad solution
		
		CollectionDTO collection=new CollectionDTO();
		CustomerDTO customerInfo=new CustomerDTO();
		customerInfo=customerService.getCustomerInfo(customer_id);
		ArrayList<CollectionDTO> billList=new ArrayList<CollectionDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql="";
		
		
		if(customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered"))
		{
			if(bill_month.equalsIgnoreCase("") && bill_year.equalsIgnoreCase("")){
				sql="SELECT bill.*,CALCUALTESURCHARGE_METER(bill_id, '"+collection_date+"')  ACTUAL_SURCHARGE_CAL,NVL (ACTUAL_SURCHARGE, 0)+ NVL (BILLED_AMOUNT, 0) ACTUAL_PAYABLE_AMOUNT_CAL " +
						"FROM "+tableName+" bill WHERE Customer_Id = ? AND PAYABLE_AMOUNT <> NVL (COLLECTED_AMOUNT, 0) AND Bill_Id IN (SELECT bill_id " +
						" FROM "+tableName+" WHERE Status = 1 AND Customer_Id = ?) order by bill_month asc" ;
						/*" Select bill.*,(calcualteSurcharge (bill_id, '"+collection_date+"')) surcharge_per_coll,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)) ACTUAL_SURCHARGE_CAL,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)+NVL(BILLED_AMOUNT,0)) ACTUAL_PAYABLE_AMOUNT_CAL From "+tableName+" bill Where Customer_Id=? And  PAYABLE_AMOUNT<>NVL(COLLECTED_AMOUNT,0) " +
						   " AND Bill_Id in ( Select MIN(to_number(bill_id)) from "+tableName+" Where Status=1 And Customer_Id=?)";*/		
			}
			else
			{
				sql=" Select bill.*,CALCUALTESURCHARGE_METER(bill_id, '"+collection_date+"')  ACTUAL_SURCHARGE_CAL,  CALCUALTESURCHARGE_METER(bill_id, '"+collection_date+"')+NVL(BILLED_AMOUNT,0) ACTUAL_PAYABLE_AMOUNT_CAL From "+tableName+" bill Where Customer_Id=? And PAYABLE_AMOUNT<>NVL(COLLECTED_AMOUNT,0) And  " +
						   "BILL_MONTH="+bill_month+"And BILL_YEAR="+bill_year;
				/*" Select bill.*,(calcualteSurcharge (bill_id, '"+collection_date+"')) surcharge_per_coll,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)) ACTUAL_SURCHARGE_CAL,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)+NVL(BILLED_AMOUNT,0)) ACTUAL_PAYABLE_AMOUNT_CAL From "+tableName+" bill Where Customer_Id=? And PAYABLE_AMOUNT<>NVL(COLLECTED_AMOUNT,0) And  " +
				   "BILL_MONTH="+bill_month+"And BILL_YEAR="+bill_year;*/
			}
		}
		else
		{
			if(bill_month.equalsIgnoreCase("") && bill_year.equalsIgnoreCase("")){
				sql="SELECT bill.*,NVL (ACTUAL_SURCHARGE, 0) ACTUAL_SURCHARGE_CAL,NVL (ACTUAL_SURCHARGE, 0) + NVL (BILLED_AMOUNT, 0) ACTUAL_PAYABLE_AMOUNT_CAL FROM "+tableName+" bill " +
						"WHERE Customer_Id = ? AND ACTUAL_PAYABLE_AMOUNT <> NVL (COLLECTED_PAYABLE_AMOUNT, 0) AND bill_id in (select bill_id from "+tableName+" WHERE customer_id=? AND Status = 1) " +
						"ORDER BY Bill_Year DESC, Bill_Month DESC ";
						/*" Select bill.*,(calcualteSurcharge (bill_id, '"+collection_date+"')) surcharge_per_coll,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)) ACTUAL_SURCHARGE_CAL,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)+NVL(BILLED_AMOUNT,0)) ACTUAL_PAYABLE_AMOUNT_CAL From "+tableName+" bill Where Customer_Id=? And  ACTUAL_PAYABLE_AMOUNT<>NVL(COLLECTED_PAYABLE_AMOUNT,0) " +
						   " AND Status=1 Order by Bill_Year,Bill_Month desc";*/
			}
			else if(bill_month.equalsIgnoreCase("")){
				sql="SELECT bill.*, " +
						"       (calcualteSurcharge (bill_id, '"+collection_date+"')) surcharge_per_coll, " +
						"       (  calcualteSurcharge (bill_id, '"+collection_date+"') " +
						"        + NVL (ACTUAL_SURCHARGE, 0)) " +
						"          ACTUAL_SURCHARGE_CAL, " +
						"       (  calcualteSurcharge (bill_id, '"+collection_date+"') " +
						"        + NVL (ACTUAL_SURCHARGE, 0) " +
						"        + NVL (BILLED_AMOUNT, 0)) " +
						"          ACTUAL_PAYABLE_AMOUNT_CAL " +
						"  FROM BILL_NON_METERED bill " +
						" WHERE     Customer_Id =? " +
						"       AND ACTUAL_PAYABLE_AMOUNT <> NVL (COLLECTED_PAYABLE_AMOUNT, 0) " +
						"       AND BILL_YEAR ="+bill_year;

			}
			else if(bill_year.equalsIgnoreCase("")){
				
				sql="SELECT bill.*, " +
						"       (calcualteSurcharge (bill_id, '"+collection_date+"')) surcharge_per_coll, " +
						"       (  calcualteSurcharge (bill_id, '"+collection_date+"') " +
						"        + NVL (ACTUAL_SURCHARGE, 0)) " +
						"          ACTUAL_SURCHARGE_CAL, " +
						"       (  calcualteSurcharge (bill_id, '"+collection_date+"') " +
						"        + NVL (ACTUAL_SURCHARGE, 0) " +
						"        + NVL (BILLED_AMOUNT, 0)) " +
						"          ACTUAL_PAYABLE_AMOUNT_CAL " +
						"  FROM BILL_NON_METERED bill " +
						" WHERE     Customer_Id =? " +
						"       AND ACTUAL_PAYABLE_AMOUNT <> NVL (COLLECTED_PAYABLE_AMOUNT, 0) " +
						"       AND BILL_MONTH="+bill_month;
			}
			else{
				sql=" Select bill.*,(calcualteSurcharge (bill_id, '"+collection_date+"')) surcharge_per_coll,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)) ACTUAL_SURCHARGE_CAL,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)+NVL(BILLED_AMOUNT,0)) ACTUAL_PAYABLE_AMOUNT_CAL From "+tableName+" bill Where Customer_Id=? And ACTUAL_PAYABLE_AMOUNT<>NVL(COLLECTED_PAYABLE_AMOUNT,0) And  " +
						   "BILL_MONTH="+bill_month+"And BILL_YEAR="+bill_year;
			}
		}
			
	
		
		PreparedStatement stmt = null;
		ResultSet r = null;
		
			try
			{
				stmt = conn.prepareStatement(sql);
				if(bill_month.equalsIgnoreCase("")&&bill_year.equalsIgnoreCase("")){
					stmt.setString(1,customer_id);
					stmt.setString(2,customer_id);
				}else{				
					stmt.setString(1,customer_id);
				}
												
				r = stmt.executeQuery();
				while (r.next())
				{	
					collection=new CollectionDTO();
					collection.setCustomer(customerInfo);
					
					if(customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered")){
						collection.setBilled_amount(r.getDouble("BILLED_AMOUNT"));					
						collection.setVat_rebate_amount(r.getDouble("VAT_REBATE_AMOUNT"));
						//for fixing sum in form
						collection.setSurcharge_amount(r.getDouble("SURCHARGE_AMOUNT"));
						//collection.setSurcharge_amount(r.getDouble("ACTUAL_SURCHARGE_CAL"));
						collection.setAdjustment_amount(r.getDouble("ADJUSTMENT_AMOUNT"));
						//collection.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
						collection.setPayable_amount(r.getDouble("ACTUAL_PAYABLE_AMOUNT_CAL"));
					}
					else
					{
						collection.setActual_billed_amount(r.getDouble("BILLED_AMOUNT"));
						collection.setCollected_billed_amount(r.getDouble("COLLECTED_BILLED_AMOUNT"));
						collection.setActual_surcharge_amount(r.getDouble("ACTUAL_SURCHARGE_CAL"));
						collection.setCollected_surcharge_amount(r.getDouble("COLLECTED_SURCHARGE"));
						collection.setActual_payable_amount(r.getDouble("ACTUAL_PAYABLE_AMOUNT_CAL"));
						collection.setCollected_payable_amount(r.getDouble("COLLECTED_PAYABLE_AMOUNT"));
						//collection.setSurcharge_per_collection(r.getDouble("surcharge_per_coll"));
						//collection.setDouble_burner_qnt(r.getString("double_burner_qnt"));
					}
					
					collection.setBill_month(r.getString("BILL_MONTH"));
					collection.setBill_year(r.getString("BILL_YEAR"));
					collection.setBill_month_name(Month.values()[r.getInt("BILL_MONTH")-1].getLabel());
					collection.setBill_id(r.getString("BILL_ID"));
					collection.setStatusId(r.getString("STATUS"));
					collection.setBill_type("R");//R=Regular Bill
					collection.setPaid_status("GTBP");//GTBP=Going to be paid
					billList.add(collection);
				}
			

			} 
			catch (Exception e){e.printStackTrace();
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return billList;

	}
	
	public ResponseDTO saveBillCollection(CollectionDTO collection,boolean mobilePhoneUpdate)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String isMeteredStatus=collection.getCustomer().getConnectionInfo().getIsMetered_str();
		

		String sql_insert_metered=" Insert Into BILL_COLLECTION_METERED(COLLECTION_ID,CUSTOMER_ID,BILL_ID,BANK_ID,BRANCH_ID,ACCOUNT_NO,COLLECTION_DATE, TAX_AMOUNT," +
				   				  " COLLECTION_AMOUNT,REMARKS,COLLECED_BY,INSERTED_ON,PAYABLE_AMOUNT,CHALAN_NO,CHALAN_DATE) " +
				   				  " Values(?,?,?,?,?,?,to_date(?,'dd-MM-YYYY'),?,?,?,?,SYSDATE,?,?,to_date(?,'dd-MM-YYYY'))";
		
		String sql_insert_non_metered="Insert InTo BILL_COLLECTION_NON_METERED(COLLECTION_ID,CUSTOMER_ID,BILL_ID,BANK_ID,BRANCH_ID,ACCOUNT_NO,COLLECTION_DATE, " +
									  " COLLECTED_BILL_AMOUNT,COLLECTED_SURCHARGE_AMOUNT,TOTAL_COLLECTED_AMOUNT,REMARKS,COLLECED_BY,INSERTED_ON,SURCHARGE_PER_COLL) " +
									  " Values(?,?,?,?,?,?,to_date(?,'dd-MM_YYYY'),?,?,?,?,?,sysdate,? )";
		
		String sql_update_metered="Update BILL_METERED Set Status=2,Collected_amount=nvl(collected_amount,0)+?,COLLECTION_DATE=to_date(?,'dd-MM_YYYY'),BRANCH_ID=?  Where Bill_Id=?";
		
		String sql_update_non_metered ="UPDATE BILL_NON_METERED "+
					   "SET COLLECTED_BILLED_AMOUNT =nvl(COLLECTED_BILLED_AMOUNT,0)+?,ACTUAL_SURCHARGE=?,COLLECTED_SURCHARGE=nvl(COLLECTED_SURCHARGE,0)+?, ACTUAL_PAYABLE_AMOUNT=?,COLLECTED_PAYABLE_AMOUNT =nvl(COLLECTED_PAYABLE_AMOUNT,0)+?, "+
					       "Status = "+
					          "CASE "+
					             "WHEN ( NVL (COLLECTED_PAYABLE_AMOUNT, 0) + ?) >= "+
					                     "BILLED_AMOUNT "+
					             "THEN "+
					                "2 "+
					             "ELSE "+
					                "1 "+
					         "END "+
					 "WHERE Bill_Id = ? ";

		
		String sql_mobile_phone="Update CUSTOMER_PERSONAL_INFO Set Mobile=?,Phone=? Where Customer_Id=?";
		
		String customer_ledger_query=" Insert into CUSTOMER_LEDGER(TRANS_ID, TRANS_DATE, PARTICULARS, CREDIT, COLLECTION_ID,BILL_ID,INSERTED_BY, CUSTOMER_ID, STATUS) "+
									 " Values(SQN_CL.NEXTVAL, TO_DATE(?, 'DD-MM-YYYY'),?, ?, ?,?, ?, ?,0)";    

		String bank_account_ledger_query=" Insert into BANK_ACCOUNT_LEDGER(TRANS_ID, TRANS_DATE, TRANS_TYPE, PARTICULARS, BANK_ID,BRANCH_ID, ACCOUNT_NO,DEBIT,REF_ID, " +
								   " INSERTED_BY, CUSTOMER_ID, STATUS) " +
								   " Values(?, TO_DATE(?, 'DD-MM-YYYY'), ?, ?, ?,?,?,?,?,?,?,0)";
		
		String bank_account_ledger_id_query="Select SQN_BAL.NEXTVAL BAL_TRANS_ID from dual";
		
		String bank_book_info_select="Select Payable_Amount,GAS_BILL,BILL_METERED.METER_RENT,BILL_METERED.SURCHARGE_AMOUNT  " +
				" From BILL_METERED,SUMMARY_MARGIN_PB Where BILL_METERED.BILL_ID=?" +
				" And BILL_METERED.BILL_ID=SUMMARY_MARGIN_PB.BILL_ID";
		
		String bank_book_metered="Update BANK_ACCOUNT_LEDGER Set  " +
				                 "ACTUAL_REVENUE=?,METER_RENT=?,SURCHARGE=? Where TRANS_ID=?";
		
		String bank_book_non_metered="Update BANK_ACCOUNT_LEDGER Set ACTUAL_REVENUE=?, " +
				                  "SURCHARGE=? Where TRANS_ID=?";    
		
		PreparedStatement stmt = null;
		PreparedStatement coll_stmt = null;
		PreparedStatement customer_ledger_stmt = null;
		PreparedStatement bank_account_ledger_stmt = null;
		PreparedStatement bank_book_stmt = null;
		PreparedStatement bank_book_stmt_update_metered = null;
		PreparedStatement bank_book_stmt_update_non_metered = null;
		PreparedStatement bal_id_stmt = null;
		ResultSet r = null;
		String collection_id=null;		
		String bal_trans_id="";
		double payableAmount=new Double(0),gasBill=new Double(0),meterRent=new Double(0),surchargeAmount=new Double(0);
		 
		
		try
			{
				/*~~~~~~ Sequence Id [Start] ~~~~~~~~*/
				if(isMeteredStatus.equalsIgnoreCase("1"))
					coll_stmt = conn.prepareStatement("Select SQN_COLLECTION_M.NEXTVAL collection_id from dual");
				else
					coll_stmt = conn.prepareStatement("Select SQN_COLLECTION_NM.NEXTVAL collection_id from dual");
				
				r = coll_stmt.executeQuery();
				if (r.next())
					collection_id=r.getString("collection_id"); 
				
				/*~~~~~~ Sequence Id [End] ~~~~~~~~*/
				
				/*~~~~~~ BAL Trans Id [Start] ~~~~~~~~*/
				bal_id_stmt = conn.prepareStatement(bank_account_ledger_id_query);
				
				r = bal_id_stmt.executeQuery();
				if (r.next())
					bal_trans_id=r.getString("BAL_TRANS_ID"); 
				
				/*~~~~~~ Sequence Id [End] ~~~~~~~~*/
				
				
				/*~~~~~~ Insert Collection Information [Start] ~~~~~~~~*/
				if(isMeteredStatus.equalsIgnoreCase("1")){
					stmt = conn.prepareStatement(sql_insert_metered);
					
					stmt.setString(1,collection_id);
					stmt.setString(2,collection.getCustomer_id());
					stmt.setString(3,collection.getBill_id());					
					stmt.setString(4,collection.getBank_id());
					stmt.setString(5,collection.getBranch_id());
					stmt.setString(6,collection.getAccount_no());
					stmt.setString(7,collection.getCollection_date());
					stmt.setDouble(8,collection.getTax_amount());
					stmt.setDouble(9,collection.getCollected_amount());
					stmt.setString(10,collection.getRemarks());
					stmt.setString(11,collection.getInserted_by());
					stmt.setDouble(12,collection.getPayable_amount());
					stmt.setString(13,collection.getTax_no());
					stmt.setString(14,collection.getTax_date());
									
					
					stmt.executeUpdate();
				}
				else{
					stmt = conn.prepareStatement(sql_insert_non_metered);
									
					stmt.setString(1,collection_id);
					stmt.setString(2,collection.getCustomer_id());
					stmt.setString(3,collection.getBill_id());					
					stmt.setString(4,collection.getBank_id());
					stmt.setString(5,collection.getBranch_id());
					stmt.setString(6,collection.getAccount_no());
					stmt.setString(7,collection.getCollection_date());
					stmt.setDouble(8,collection.getCollected_amount());
					stmt.setDouble(9,collection.getSurcharge_amount());
					stmt.setDouble(10,collection.getCollected_amount()+collection.getSurcharge_amount());
					stmt.setString(11,collection.getRemarks());
					stmt.setString(12,collection.getInserted_by());
					stmt.setDouble(13,collection.getSurcharge_per_collection());
									
					
					stmt.executeUpdate();
				}
				/*~~~~~~ Insert Collection Information [End] ~~~~~~~~*/
				
				
				
				/*~~~~~~ Update Collection Information [Start] ~~~~~~~~*/				
				if(isMeteredStatus.equalsIgnoreCase("1")){
					stmt = conn.prepareStatement(sql_update_metered);
					stmt.setDouble(1,collection.getCollected_amount()+ collection.getTax_amount());
					stmt.setString(2, collection.getCollection_date());
					stmt.setString(3,collection.getBranch_id());
					stmt.setString(4,collection.getBill_id());
					stmt.executeUpdate();
				}
				else{
					stmt = conn.prepareStatement(sql_update_non_metered);
					stmt.setDouble(1,collection.getCollected_amount());
					stmt.setDouble(2,collection.getActual_surcharge_amount());
					stmt.setDouble(3,collection.getSurcharge_amount());
					stmt.setDouble(4,collection.getActual_payable_amount());
					stmt.setDouble(5,(collection.getSurcharge_amount()+collection.getCollected_amount()));
					stmt.setDouble(6,(collection.getSurcharge_amount()+collection.getCollected_amount()));
					stmt.setString(7,collection.getBill_id());
					stmt.executeUpdate();
				}
				
				/*~~~~~~ Update Collection Information [End] ~~~~~~~~*/
				
				if(mobilePhoneUpdate==true){
					stmt = conn.prepareStatement(sql_mobile_phone);
					stmt.setString(1,collection.getMobile());
					stmt.setString(2,collection.getPhone());
					stmt.setString(3,collection.getCustomer_id());
					stmt.executeUpdate();
				}
				
				
				
				
				//Bank Ledger Entry
				bank_account_ledger_stmt= conn.prepareStatement(bank_account_ledger_query);
				
				bank_account_ledger_stmt.setString(1,bal_trans_id);
				bank_account_ledger_stmt.setString(2,collection.getCollection_date());
				bank_account_ledger_stmt.setInt(3,BankAccountTransactionType.SALES_COLLECTION.getId());
				bank_account_ledger_stmt.setString(4,"Collection, "+Month.values()[Integer.valueOf(collection.getBill_month())-1].getLabel()+"-"+collection.getBill_year());
				bank_account_ledger_stmt.setString(5,collection.getBank_id());
				bank_account_ledger_stmt.setString(6,collection.getBranch_id());
				bank_account_ledger_stmt.setString(7,collection.getAccount_no());	
			
				if(isMeteredStatus.equalsIgnoreCase("1")){		
					bank_account_ledger_stmt.setDouble(8,collection.getCollected_amount());
				}else
				{
					bank_account_ledger_stmt.setDouble(8,collection.getCollected_amount()+collection.getSurcharge_amount());
				}
				bank_account_ledger_stmt.setString(9,collection_id);
				bank_account_ledger_stmt.setString(10,collection.getInserted_by());
				bank_account_ledger_stmt.setString(11,collection.getCustomer_id());				
				bank_account_ledger_stmt.executeUpdate();
				
				
				//Bank Book Information entry
				bank_book_stmt = conn.prepareStatement(bank_book_info_select);				
				bank_book_stmt.setString(1,collection.getBill_id());
				r = bank_book_stmt.executeQuery();
				if (r.next()){
					payableAmount=r.getDouble("Payable_Amount");
					gasBill=r.getDouble("GAS_BILL"); 
					meterRent=r.getDouble("METER_RENT"); 
					surchargeAmount=r.getDouble("SURCHARGE_AMOUNT"); 
				}

				
				/*~~~~~~ Update Collection Information [Start] ~~~~~~~~*/				
				if(isMeteredStatus.equalsIgnoreCase("1")){
					bank_book_stmt_update_metered = conn.prepareStatement(bank_book_metered);
					double extraAmountSurcharge=0.0;
					if(collection.getCollected_amount()>collection.getPayable_amount())
					{
						extraAmountSurcharge=collection.getCollected_amount()-collection.getPayable_amount();
						
					}
					
					bank_book_stmt_update_metered.setDouble(1,payableAmount);					
					bank_book_stmt_update_metered.setDouble(2,meterRent);
					bank_book_stmt_update_metered.setDouble(3,surchargeAmount+extraAmountSurcharge);
					bank_book_stmt_update_metered.setString(4,bal_trans_id);
					bank_book_stmt_update_metered.executeUpdate();
				}
				else{
					bank_book_stmt_update_non_metered = conn.prepareStatement(bank_book_non_metered);
					bank_book_stmt_update_non_metered.setDouble(1,collection.getCollected_amount());
					bank_book_stmt_update_non_metered.setDouble(2,collection.getSurcharge_amount());
					bank_book_stmt_update_non_metered.setString(3,bal_trans_id);
					bank_book_stmt_update_non_metered.executeUpdate();
				}
				
				//Customer Ledger Entry
				customer_ledger_stmt= conn.prepareStatement(customer_ledger_query);
				customer_ledger_stmt.setString(1,collection.getCollection_date());
				customer_ledger_stmt.setString(2,"By Bank, "+Month.values()[Integer.valueOf(collection.getBill_month())-1].getLabel()+"-"+collection.getBill_year());
				if(isMeteredStatus.equalsIgnoreCase("1")){
					double billed_amount_no_surcharge=0.0;
					if (collection.getCollected_amount()>collection.getPayable_amount())
					{
						billed_amount_no_surcharge=collection.getCollected_amount()-surchargeAmount;
					}else
					{
						billed_amount_no_surcharge=collection.getCollected_amount()-surchargeAmount;
					}
					
					billed_amount_no_surcharge=billed_amount_no_surcharge+collection.getTax_amount();
					customer_ledger_stmt.setDouble(3,billed_amount_no_surcharge);
				}else
				{
					customer_ledger_stmt.setDouble(3,collection.getCollected_amount());
				}
				customer_ledger_stmt.setString(4,collection_id);
				customer_ledger_stmt.setString(5,collection.getBill_id());
				customer_ledger_stmt.setString(6,collection.getInserted_by());
				customer_ledger_stmt.setString(7,collection.getCustomer_id());
				customer_ledger_stmt.executeUpdate();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully saved collection information.");
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
	 		finally{try{stmt.close();customer_ledger_stmt.close();bank_account_ledger_stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;customer_ledger_stmt=null;bank_account_ledger_stmt=null;conn = null;}
	 		
	 		return response;

	}
	
	
	
	
	
	//check due bill amount
	public ResponseDTO checkAdvancedCollection(CollectionDTO collection){
		
		ResponseDTO response = new ResponseDTO();
		int response_code=0;
	 	String response_msg="0#0";
	 	
		Connection conn = ConnectionManager.getConnection();
	 	OracleCallableStatement stmt=null;
	 	
	 	try
		  {	 	   stmt = (OracleCallableStatement) conn.prepareCall(
					 	  "{ ? = call GET_NON_BILL_AMOUNT	(?,?,?,?,?,?) }");
		    		
		  			stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
		    	
					stmt.setString(2, collection.getCustomer_id());
		    		stmt.setString(3, collection.getFrom_month());
		    		stmt.setString(4, collection.getFrom_year());
		    		stmt.setString(5, collection.getTo_month());
		    		stmt.setString(6, collection.getTo_year());
		    		stmt.setString(7, collection.getCollection_date());

					
					
					stmt.executeUpdate();

					response_msg = (stmt.getString(1)).trim();
					
					//split and check if any bill is due
//					String [] arrDebit = response_msg.split("#");
//					double bill_amount = Double.parseDouble(arrDebit[0]);
//					double surcharge = Double.parseDouble(arrDebit[1]);
//					double total = bill_amount + surcharge;
//					if(total > 0.0)
//					response_code = 1 ;
					//end of checking
					
					response_code = 1 ;
					response.setMessasge(response_msg);
					response.setResponse(response_code==1?true:false);
	 			
	 		
	 		
	    		    	
		  } 
		catch (Exception e){e.printStackTrace();response.setResponse(false);response.setMessasge(e.getMessage());return response;}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
	 	
		 return response;
	}
	//end
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public ResponseDTO saveAdvancedCollection(CollectionDTO collection){
		
		ResponseDTO response = new ResponseDTO();
		int response_code=0;
	 	String response_msg="";
		
		Connection conn = ConnectionManager.getConnection();
	 	OracleCallableStatement stmt=null;
		
	 	
	 	
	 
	 	
	 	try
		  {
	 		if(collection.getIs_codeless()==1){
	 			
	 			stmt = (OracleCallableStatement) conn.prepareCall(
					 	  "{ call CODELESS_COLLECTION	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
		    		
		    	
		    	
					stmt.setString(1, collection.getCustomer_id());
					stmt.setString(2, collection.getCustomer_name());
					stmt.setString(3, collection.getAddress());
		    		stmt.setString(4, collection.getBank_id());
		    		stmt.setString(5, collection.getBranch_id());
		    		stmt.setString(6, collection.getAccount_no());
		    		stmt.setString(7, collection.getCollection_date());
		    		stmt.setDouble(8, collection.getAdvanced_amount());
		    		stmt.setDouble(9, collection.getSurcharge_amount());
		    		stmt.setString(10, collection.getFrom_month());
		    		stmt.setString(11, collection.getFrom_year());
		    		stmt.setString(12, collection.getTo_month());
		    		stmt.setString(13, collection.getTo_year());
		    		stmt.setString(14, loggedInUser.getUserName());
		    		stmt.setString(15, collection.getArea_id());
		    		
		    		stmt.registerOutParameter(16, java.sql.Types.INTEGER);
					stmt.registerOutParameter(17, java.sql.Types.VARCHAR);
					
					stmt.executeUpdate();
					response_code = stmt.getInt(16);
					response_msg = (stmt.getString(17)).trim();
					
					response.setMessasge(response_msg);
					response.setResponse(response_code==1?true:false);
	 			
	 		}else if(collection.getIs_codeless()==2){
	 			
	 			stmt = (OracleCallableStatement) conn.prepareCall(
					 	  "{ call CODELESS_SETTLE	(?,?,?,?,?,?,?,?,?) }");
		    		
		    	
	 			    int scrl_no = Integer.parseInt(collection.getScroll_no());
	 			    stmt.setInt(1, scrl_no);
					stmt.setString(2, collection.getCustomer_id());
		    		stmt.setString(3, collection.getFrom_month());
		    		stmt.setString(4, collection.getFrom_year());
		    		stmt.setString(5, collection.getTo_month());
		    		stmt.setString(6, collection.getTo_year());
		    		stmt.setString(7, loggedInUser.getUserName());
		    		
		    		
		    		stmt.registerOutParameter(8, java.sql.Types.INTEGER);
					stmt.registerOutParameter(9, java.sql.Types.VARCHAR);
					
					stmt.executeUpdate();
					response_code = stmt.getInt(8);
					response_msg = (stmt.getString(9)).trim();
					
					response.setMessasge(response_msg);
					response.setResponse(response_code==1?true:false);
	 			
	 		}else{
		 			stmt = (OracleCallableStatement) conn.prepareCall(
						 	  "{ call ADVANCED_COLLECTION	(?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
			    		
			    	
			    	
						stmt.setString(1, collection.getCustomer_id());
			    		stmt.setString(2, collection.getBank_id());
			    		stmt.setString(3, collection.getBranch_id());
			    		stmt.setString(4, collection.getAccount_no());
			    		stmt.setString(5, collection.getCollection_date());
			    		stmt.setString(6, loggedInUser.getUserName());
			    		stmt.setDouble(7, collection.getAdvanced_amount());
			    		stmt.setDouble(8, collection.getSurcharge_amount());
			    		stmt.setString(9, collection.getFrom_month());
			    		stmt.setString(10, collection.getFrom_year());
			    		stmt.setString(11, collection.getTo_month());
			    		stmt.setString(12, collection.getTo_year());
			    		
			    		
			    		stmt.registerOutParameter(13, java.sql.Types.INTEGER);
						stmt.registerOutParameter(14, java.sql.Types.VARCHAR);
						
						stmt.executeUpdate();
						response_code = stmt.getInt(13);
						response_msg = (stmt.getString(14)).trim();
						
						response.setMessasge(response_msg);
						response.setResponse(response_code==1?true:false);
		 		}
	 		
	    		    	
		  } 
		catch (Exception e){e.printStackTrace();response.setResponse(false);response.setMessasge(e.getMessage());return response;}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
	 	
		 return response;
	}
	
	public ArrayList<CollectionDTO> getAdvancedBillingInfo(String customer_id){
				
		CollectionDTO collection=new CollectionDTO();
		ArrayList<CollectionDTO> advancedBillList=new ArrayList<CollectionDTO>();

		Connection conn = ConnectionManager.getConnection();
		
		
				String sql="SELECT * from DAILY_COLLECTION_GRID WHERE Customer_Id = ?) " ;			
	
		
		PreparedStatement stmt = null;
		ResultSet r = null;
		
			try
			{
				stmt = conn.prepareStatement(sql);
				
					stmt.setString(1,customer_id);
				
												
				r = stmt.executeQuery();
				while (r.next())
				{	
					collection=new CollectionDTO();
					
					collection.setCustomer_id(r.getString("CUSTOMER_ID"));
					collection.setCustomer_name(r.getString("CUSTOMER_NAME"));
					collection.setFrom_month(r.getString("MONTH_FROM"));
					collection.setFrom_year(r.getString("YEAR_FROM"));
					collection.setTo_month(r.getString("MONTH_TO"));
					collection.setTo_year(r.getString("YEAR_TO"));
					collection.setAdvanced_amount(r.getDouble("COLLECTED_AMOUNT"));
					collection.setSurcharge_amount(r.getDouble("COLLECTED_SURCHARGE"));
					collection.setCollection_date(r.getString("COLLECTION_DATE"));
					collection.setBank_id(r.getString("BANK_CODE"));
					collection.setBranch_id(r.getString("BRANCH_ID"));
					collection.setAccount_no(r.getString("ACCOUNT_NO"));
					collection.setArea_id(r.getString("AREA_ID"));
					collection.setInserted_by(r.getString("INSERTED_BY"));
					collection.setStatusId(r.getString("STATUS"));
					
					advancedBillList.add(collection);
				}
			

			} 
			catch (Exception e){e.printStackTrace();
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return advancedBillList;
	}
	
	
	public ResponseDTO saveMultiMonthCollection(CollectionDTO collection)
	{
		//bill_collection#surcharge_collection#actual_surcharge#actual_payable#surcharge_per_collection
		Connection conn = ConnectionManager.getConnection();
	 	OracleCallableStatement stmt=null;
	 	
	 	int response_code=0;
	 	String response_msg="";
	 	ResponseDTO response=new ResponseDTO();
		String pending_bills_str=collection.getPending_bills_str();
		if(pending_bills_str.length()>0)
			pending_bills_str=pending_bills_str.substring(0, pending_bills_str.length()-1);
		
	 
		String[] pendingArr;
		if(pending_bills_str.equalsIgnoreCase(""))
			pendingArr=new String[0];
		else 
			pendingArr=pending_bills_str.split("@");
		
		//String[] advancedArr;
		//if(advanced_bills_str.equalsIgnoreCase(""))
			//advancedArr=new String[0];
		//else 
			//advancedArr=advanced_bills_str.split("@");
		
		String[] pendingBillId = new String[pendingArr.length];
		String[] pendingBillCollectedAmount = new String[pendingArr.length];
		String[] pendingBillCollectedSurcharge = new String[pendingArr.length];
		String[] pendingBillActualAmount = new String[pendingArr.length];
		String[] pendingBillActualSurcharge = new String[pendingArr.length];
		String[] pendingSurchargePerCollection = new String[pendingArr.length];
		
		//String[] advancedBillMonth=new String[advancedArr.length];
		//String[] advancedBillYear=new String[advancedArr.length];
		//String[] advancedBillAmount=new String[advancedArr.length];
		
		
		
		
////////////////// BY Toraf////////////////////////
		ArrayList<MultiCollStrDTO> listMC= new ArrayList<MultiCollStrDTO>();
	
		for(int i=0;i<pendingArr.length;i++){
			String[] pendings=pendingArr[i].split("#");
			
			MultiCollStrDTO mc = new MultiCollStrDTO();
			
			mc.setBillId(pendings[0]);
			mc.setCollectedAmount(pendings[1]);
			mc.setCollectedSurcharge(pendings[2]);
			mc.setActualSurcharge(pendings[3]);			
			mc.setActualAmount(pendings[4]);
			mc.setSurchargePerCollection(pendings[5]);
			mc.setBillMonth(Month.valueOf(pendings[6].toUpperCase()).ordinal()+1);
			mc.setBillYear(Integer.parseInt(pendings[7]));
			
						
			
			listMC.add(mc);
		}
	
		Collections.sort(listMC, MultiCollStrDTO.Comparators.billYearMonth );
		
				
		MultiCollStrDTO mc = new MultiCollStrDTO();
		for(int i=0;i<listMC.size();i++){			
			mc=listMC.get(i);
			
			pendingBillId[i]=mc.getBillId();
			pendingBillCollectedAmount[i]=mc.getCollectedAmount();
			pendingBillCollectedSurcharge[i]=mc.getCollectedSurcharge();
			pendingBillActualSurcharge[i]=mc.getActualSurcharge();
			pendingBillActualAmount[i]=mc.getActualAmount();
			pendingSurchargePerCollection[i]=mc.getSurchargePerCollection();	
			
		}
		
		

		
		
	  
	    try
		  {
	    	
	    	
	    	ArrayDescriptor arrString = new ArrayDescriptor("VARCHARARRAY", conn);
			
		
	    	ARRAY inputPendingBillId=new ARRAY(arrString,conn,pendingBillId);
			ARRAY inputPendingBillCollectedAmount=new ARRAY(arrString,conn,pendingBillCollectedAmount);
			ARRAY inputPendingBillCollectedSurcharge=new ARRAY(arrString,conn,pendingBillCollectedSurcharge);
			ARRAY inputPendingBillActualSurcharge=new ARRAY(arrString,conn,pendingBillActualSurcharge);
			ARRAY inputPendingBillActualAmount=new ARRAY(arrString,conn,pendingBillActualAmount);
			ARRAY inputPendingBillSurchargePerCollection=new ARRAY(arrString,conn,pendingSurchargePerCollection);
			/*ARRAY inputAdvancedBillMonth=new ARRAY(arrString,conn,advancedBillMonth);
			ARRAY inputAdvancedBillYear=new ARRAY(arrString,conn,advancedBillYear);
			ARRAY inputAdvancedBillAmount=new ARRAY(arrString,conn,advancedBillAmount);*/
			
			//System.out.println("Procedure Save_Multi_Month_Collection Begins");
			stmt = (OracleCallableStatement) conn.prepareCall(
					 	  "{ call Save_Multi_Month_Collection	(?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
			 
			
			stmt.setString(1, collection.getCustomer_id());
			stmt.setString(2, collection.getBank_id());
			stmt.setString(3, collection.getBranch_id());
			stmt.setString(4, collection.getAccount_no());
			stmt.setString(5, collection.getCollection_date());
			stmt.setString(6, loggedInUser.getUserName());
			stmt.setARRAY(7, inputPendingBillId);
			stmt.setARRAY(8,  inputPendingBillCollectedAmount);
			stmt.setARRAY(9,  inputPendingBillCollectedSurcharge);
			stmt.setARRAY(10,  inputPendingBillActualSurcharge);
			stmt.setARRAY(11,  inputPendingBillActualAmount);
			stmt.setARRAY(12,  inputPendingBillSurchargePerCollection);
			//stmt.setARRAY(13,  inputAdvancedBillMonth);
			//stmt.setARRAY(14,  inputAdvancedBillYear);
			//stmt.setARRAY(15,  inputAdvancedBillAmount);
			stmt.registerOutParameter(13, java.sql.Types.INTEGER);
			stmt.registerOutParameter(14, java.sql.Types.VARCHAR);
			
			stmt.executeUpdate();
			response_code = stmt.getInt(13);
			response_msg = (stmt.getString(14)).trim();
			
			response.setMessasge(response_msg);
			response.setResponse(response_code==1?true:false);
	    	
		  }
	    catch (Exception e){e.printStackTrace();response.setResponse(false);response.setMessasge(e.getMessage());return response;}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
	 	
		 return response;	
	}
	
	public ResponseDTO saveCurrentMonthBillWithCollection(MultiCollectionDTO multiColl)
	{
		//bill_collection#surcharge_collection#actual_surcharge#actual_payable#surcharge_per_collection
		Connection conn = ConnectionManager.getConnection();
	 	OracleCallableStatement stmt=null;
	 	int response_code=0;
	 	String response_msg="";
	 	ResponseDTO response=new ResponseDTO();
	  
	    try
		  {
	    	
			
			//System.out.println("Procedure save_Current_Month_Bill_With_Collection Begins");
			stmt = (OracleCallableStatement) conn.prepareCall(
					 	  "{ call Save_Curr_Month_Collectin	(?,?,?,?,?,?,?,?,?,?,?,?) }");
			stmt.setString(1, multiColl.getCustomer_id());
			stmt.setString(2, multiColl.getCurrent_bill_month());
			stmt.setString(3, multiColl.getCurrent_bill_year());
			stmt.setString(4, multiColl.getBank_id());
			stmt.setString(5, multiColl.getBranch_id());
			stmt.setString(6, multiColl.getAccount_no());
			stmt.setString(7, multiColl.getCollection_date());
			stmt.setDouble(8, multiColl.getcollection_amount());
			
			stmt.setString(9,  loggedInUser.getUserId());
			stmt.setString(10, loggedInUser.getUserName());
		
		
			stmt.registerOutParameter(11, java.sql.Types.INTEGER);
			stmt.registerOutParameter(12, java.sql.Types.VARCHAR);
			
			stmt.executeUpdate();
			response_code = stmt.getInt(11);
			response_msg = (stmt.getString(12)).trim();
			
			response.setMessasge(response_msg);
			response.setResponse(response_code==1?true:false);
		  }
	    catch (Exception e){e.printStackTrace();response.setResponse(false);response.setMessasge(e.getMessage());return response;}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
	 	
		 return response;	
	}
	
	
	public ArrayList<CollectionDTO> getCollectionList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		ArrayList<CollectionDTO> collectionList=new ArrayList<CollectionDTO>();
		CustomerService customerService=new CustomerService();
		int sIndex=whereClause.indexOf("'");
		String[] cust=whereClause.split("'");
		CustomerDTO customer=customerService.getCustomerInfo(cust[1]);
		CollectionDTO collection=null;
		
		Connection conn = ConnectionManager.getConnection();	
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		String sql="";
		
		if(customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered")){
			if(total==0)
				  sql = "select bm.CUSTOMER_ID,bm.CUSTOMER_NAME,COLLECTION_ID,bcm.COLLECTION_DATE,bill_month,bill_year,ACTUAL_REVENUE,SURCHARGE,bal.bank_id,bal.branch_id,bal.ACCOUNT_NO,INSERTED_BY,bal.INSERTED_ON,bm.AREA_ID  " +
						  "from bill_metered bm,bill_collection_metered bcm,bank_account_ledger bal " +
						  "where bm.bill_id=BCM.BILL_ID " +
						  "AND BCM.COLLECTION_ID=BAL.REF_ID " +
						  "AND BM.CUSTOMER_ID=BAL.CUSTOMER_ID AND " +
						   (whereClause.equalsIgnoreCase("")?" ":"  ( "+whereClause+")")+
						  " AND AREA_ID="+loggedInUser.getArea_id()+" ";
				  
				  
				
		
		else
				  sql=  "SELECT * " +
						  "  FROM (SELECT ROWNUM serial, tmp1.* " +
						  "          FROM (  select bm.CUSTOMER_ID,bm.CUSTOMER_NAME,COLLECTION_ID,bcm.COLLECTION_DATE,bill_month,bill_year,ACTUAL_REVENUE,SURCHARGE,bal.bank_id,bal.branch_id,bal.ACCOUNT_NO,INSERTED_BY,bal.INSERTED_ON,bm.AREA_ID  " +
						  "from bill_metered bm,bill_collection_metered bcm,bank_account_ledger bal " +
						  "where bm.bill_id=BCM.BILL_ID " +
						  "AND BCM.COLLECTION_ID=BAL.REF_ID " +
						  "AND BM.CUSTOMER_ID=BAL.CUSTOMER_ID AND " +
						  (whereClause.equalsIgnoreCase("")?" ":"  ( "+whereClause+")")+
						  " AND AREA_ID="+loggedInUser.getArea_id()+
						  ") tmp1) tmp2 " +
						  " WHERE serial BETWEEN ? AND ? " ;

		   
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
					collection=new  CollectionDTO();
					collection.setCollection_id(r.getString("COLLECTION_ID"));
					collection.setCustomer_id(r.getString("CUSTOMER_ID"));
					collection.setCustomer_name(r.getString("CUSTOMER_NAME"));
					collection.setCollection_date(r.getString("COLLECTION_DATE"));
					collection.setCollection_date_f1(r.getString("COLLECTION_DATE"));
					//collection.setBill_id(r.getString("BILL_ID"));
					collection.setCustomer_name(r.getString("CUSTOMER_NAME"));
					collection.setBank_id(r.getString("BANK_ID"));
					collection.setArea_id(r.getString("AREA_ID"));
					collection.setBranch_id(r.getString("BRANCH_ID"));
					collection.setAccount_no(r.getString("ACCOUNT_NO"));
					
					collection.setBill_month(r.getString(Month.values()[r.getInt("BILL_MONTH")-1].getLabel()));
					collection.setBill_year(r.getString("BILL_YEAR"));			
						collection.setSurcharge_amount(r.getDouble("SURCHARGE_AMOUNT"));
						
				
						collection.setActual_billed_amount(r.getDouble("BILLED_AMOUNT"));						
						collection.setCollected_billed_amount(r.getDouble("COLLECTED_BILLED_AMOUNT"));
						collection.setActual_surcharge_amount(r.getDouble("SURCHARGE_AMOUNT"));
						collection.setCollected_surcharge_amount(r.getDouble("SURCHARGE_COLLECTED"));
						collection.setSurcharge_per_collection(r.getDouble("SURCHARGE_PER_COLL"));
						collection.setActual_payable_amount(r.getDouble("PAYABLE_AMOUNT"));
						collection.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));//grid
						collection.setBilled_amount(r.getDouble("BILLED_AMOUNT"));	//grid
						collection.setSurcharge_amount(r.getDouble("SURCHARGE_AMOUNT"));//grid
						
					
					
					collection.setCollected_payable_amount(r.getDouble("COLLECTED_AMOUNT"));
					collection.setCollection_amount(r.getDouble("COLLECTED_AMOUNT"));//Collection_amount from collection table(grid)					
					collection.setRemarks(r.getString("REMARKS"));
					
					collectionList.add(collection);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		}else{
			if(total==0)
				sql ="SELECT * FROM (SELECT SCROLL_NO COLLECTION_ID,CUSTOMER_ID,CUSTOMER_NAME,(select MON from mst_month where M_ID=MONTH_FROM)||'-'||YEAR_FROM MONTH_FROM,YEAR_FROM,(select MON from mst_month where M_ID=MONTH_TO)||'-'||YEAR_TO MONTH_TO,YEAR_TO,COLLECTED_AMOUNT,COLLECTED_SURCHARGE,to_char(COLLECTION_DATE,'dd-mm-yyyy') COLLECTION_DATE,BANK_ID,STATUS,INSERTED_BY,INSERTED_ON,BRANCH_ID,ACCOUNT_NO,AREA_ID " +
						  "                              FROM DAILY_COLLECTION_GRID  " +
						  "                             WHERE "+(whereClause.equalsIgnoreCase("")?"":("   "+whereClause+" "))+") ORDER BY COLLECTION_ID DESC ";
		else
			sql="  SELECT * " +
					"    FROM (SELECT ROWNUM serial, tmp1.* " +
					"            FROM (  SELECT * " +
					"                      FROM (SELECT SCROLL_NO COLLECTION_ID,CUSTOMER_ID,CUSTOMER_NAME,(select MON from mst_month where M_ID=MONTH_FROM)||'-'||YEAR_FROM MONTH_FROM,YEAR_FROM,(select MON from mst_month where M_ID=MONTH_TO)||'-'||YEAR_TO MONTH_TO,YEAR_TO,COLLECTED_AMOUNT,COLLECTED_SURCHARGE,to_char(COLLECTION_DATE,'dd-mm-yyyy') COLLECTION_DATE,BANK_ID,STATUS,INSERTED_BY,INSERTED_ON,BRANCH_ID,ACCOUNT_NO,AREA_ID " +
					"                              FROM DAILY_COLLECTION_GRID  " +
					"                             WHERE "+(whereClause.equalsIgnoreCase("")?"":("   "+whereClause+" "))+" ) ORDER BY INSERTED_ON DESC) tmp1) tmp2 " +
					"   WHERE serial BETWEEN ? AND ? " +
					"ORDER BY COLLECTION_ID ASC " ;

		   
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
					collection=new CollectionDTO();
					
					collection.setCustomer_id(r.getString("CUSTOMER_ID"));
					collection.setCustomer_name(r.getString("CUSTOMER_NAME"));
					collection.setFrom_month(r.getString("MONTH_FROM"));
					collection.setFrom_year(r.getString("YEAR_FROM"));
					collection.setTo_month(r.getString("MONTH_TO"));
					collection.setTo_year(r.getString("YEAR_TO"));
					collection.setAdvanced_amount(r.getDouble("COLLECTED_AMOUNT"));
					collection.setSurcharge_amount(r.getDouble("COLLECTED_SURCHARGE"));
					collection.setCollection_date(r.getString("COLLECTION_DATE"));
					collection.setBank_id(r.getString("BANK_ID"));
					collection.setBranch_id(r.getString("BRANCH_ID"));
					collection.setAccount_no(r.getString("ACCOUNT_NO"));
					collection.setArea_id(r.getString("AREA_ID"));
					collection.setInserted_by(r.getString("INSERTED_BY"));
					collection.setStatusId(r.getString("STATUS"));
					collectionList.add(collection);
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		}
		
		
		
	 		return collectionList;


	}
	
	public ArrayList<CollectionDTO> getAdvancedCollectionList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		//Not a good solution. But in future we need to refactor it.
		CustomerService customerService=new CustomerService();
		String[] cust=whereClause.split("'");
		CustomerDTO customer=customerService.getCustomerInfo(cust[1]);
		String tableName="";
		int sIndex=whereClause.indexOf("'");
		String surchargeCondition="";
		String statusClause="";
		
		
		//End of bad solution
		
		ArrayList<CollectionDTO> collectionList=new ArrayList<CollectionDTO>();
		CollectionDTO collection=null;
		
		Connection conn = ConnectionManager.getConnection();	
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		String sql="";
		if(total==0)
				  sql = "select ADV_TRANS_ID,TRANS_DATE,TO_CHAR (bill.TRANS_DATE, 'dd-MM-rrrr') COLLECTION_DATE,TO_CHAR (TO_DATE (bill.TRANS_DATE, 'dd-MM-YY'), 'dd-MON-YY') COLLECTION_DATE_F1, "+
						  " CUSTOMER_ID,bill.BANK_ID,Bank_Name,bill.BRANCH_ID,BRANCH_NAME,bill.ACCOUNT_NO,ACCOUNT_NAME,ADVANCED_AMOUNT,COLLECTED_BY,bill.STATUS,REMARKS,BILL_ID  " +
						  "from BILL_COLL_ADVANCED bill, " +
						  "MST_BANK_INFO bank, " +
						  "MST_BRANCH_INFO branch, " +
						  "MST_ACCOUNT_INFO account where " +
						  (whereClause.equalsIgnoreCase("")?" ":"  ( "+whereClause+")")+ 
						  "         AND bill.BANK_ID = bank.BANK_ID " +
						  "         AND bill.BRANCH_ID = branch.BRANCH_ID " +
						  "         AND bill.ACCOUNT_NO = account.ACCOUNT_NO " +
						  "         AND bill.BANK_ID = account.BANK_ID " +
						  "         AND bill.BRANCH_ID = account.BRANCH_ID " +
						  "         AND branch.AREA_ID = '"+loggedInUser.getArea_id()+"' " +
						  "ORDER BY TRANS_DATE DESC, ADV_TRANS_ID DESC " ;
		
		else sql="SELECT * " +
				"  FROM (SELECT ROWNUM serial, tmp1.* " +
				"          FROM (  SELECT ADV_TRANS_ID,TRANS_DATE,TO_CHAR (bill.TRANS_DATE, 'dd-MM-rrrr') COLLECTION_DATE,TO_CHAR (TO_DATE (bill.TRANS_DATE, 'dd-MM-YY'), 'dd-MON-YY') COLLECTION_DATE_F1,CUSTOMER_ID, " +
				"                         bill.BANK_ID,Bank_Name,bill.BRANCH_ID,BRANCH_NAME,bill.ACCOUNT_NO,ACCOUNT_NAME,ADVANCED_AMOUNT,COLLECTED_BY,bill.STATUS,REMARKS,BILL_ID  " +
				"                    FROM BILL_COLL_ADVANCED bill, " +
				"                         MST_BANK_INFO bank, " +
				"                         MST_BRANCH_INFO branch, " +
				"                         MST_ACCOUNT_INFO account WHERE " +
				(whereClause.equalsIgnoreCase("")?" ":"  ( "+whereClause+")")+
				"                         AND bill.BANK_ID = bank.BANK_ID " +
				"                         AND bill.BRANCH_ID = branch.BRANCH_ID " +
				"                         AND bill.ACCOUNT_NO = account.ACCOUNT_NO " +
				"                         AND bill.BANK_ID = account.BANK_ID " +
				"                         AND bill.BRANCH_ID = account.BRANCH_ID " +
				"                         AND branch.AREA_ID = 24 " +
				"                ORDER BY TRANS_DATE DESC, ADV_TRANS_ID DESC) tmp1) tmp2 " +
				" WHERE serial BETWEEN ? AND ? " ;

		   
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
					collection=new  CollectionDTO();
					collection.setCollection_id(r.getString("ADV_TRANS_ID"));
					collection.setCustomer_id(r.getString("CUSTOMER_ID"));
					collection.setCollection_date(r.getString("COLLECTION_DATE"));
					collection.setCollection_date_f1(r.getString("COLLECTION_DATE_F1"));
					collection.setBill_id(r.getString("BILL_ID"));
					collection.setBank_id(r.getString("BANK_ID"));
					collection.setBank_name(r.getString("BANK_NAME"));
					collection.setBranch_id(r.getString("BRANCH_ID"));
					collection.setBranch_name(r.getString("BRANCH_NAME"));
					collection.setAccount_no(r.getString("ACCOUNT_NO"));
					collection.setAccount_name(r.getString("ACCOUNT_NAME"));
					
					collection.setBill_month(r.getString("REMARKS"));
					collection.setBill_month_name(r.getString("REMARKS"));
					collection.setBill_year(r.getString("REMARKS"));					
					collection.setBill_month_year(r.getString("REMARKS"));
					
											
						collection.setCollected_billed_amount(r.getDouble("ADVANCED_AMOUNT"));
						
						
					
					
					collection.setCollected_payable_amount(r.getDouble("ADVANCED_AMOUNT"));
					collection.setCollection_amount(r.getDouble("ADVANCED_AMOUNT"));//Collection_amount from collection table(grid)					
					collection.setRemarks(r.getString("REMARKS"));
					collection.setAdvanced_amount(r.getDouble("ADVANCED_AMOUNT"));
					
					collectionList.add(collection);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
	 		return collectionList;


	}
	
	public CollectionDTO getAdvancedCollectionInfo(String customer_id,String collection_id)
	{
		CollectionDTO collection=new CollectionDTO();
		CustomerService customerService=new CustomerService();
		ArrayList<CollectionDTO> coll=getAdvancedCollectionList(0,0," bill.customer_id='"+customer_id+"' And collection_id="+collection_id,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		collection=coll.size()>0?coll.get(0):null;
	
		if(collection!=null)
			collection.setCustomer(customerService.getCustomerInfo(collection.getCustomer_id()));
		return collection;
	}
	
	public CollectionDTO getCollectionInfo(String customer_id,String collection_id)
	{
		CollectionDTO collection=new CollectionDTO();
		CustomerService customerService=new CustomerService();
		ArrayList<CollectionDTO> coll=getCollectionList(0,0," bill.customer_id='"+customer_id+"' And collection_id="+collection_id,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		collection=coll.size()>0?coll.get(0):null;
	
		if(collection!=null)
			collection.setCustomer(customerService.getCustomerInfo(collection.getCustomer_id()));
		return collection;
	}
	public CollectionDTO getCollectionInfo(String customer_id,String bill_month,String bill_year){
		
		CollectionDTO collection=new CollectionDTO();
		CustomerService customerService=new CustomerService();
		ArrayList<CollectionDTO> coll=getCollectionList(0,0," bill.customer_id='"+customer_id+"' And bill.bill_month="+bill_month+" And bill.bill_year="+bill_year,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		collection=coll.size()>0?coll.get(0):null;
	
		if(collection!=null)
			collection.setCustomer(customerService.getCustomerInfo(collection.getCustomer_id()));
		return collection;
	}
	
	public double getTotalCollectionByDateAccount(String collection_date,String account_no){

		double totalCollection=0;
		Connection conn = ConnectionManager.getConnection();
		String sql=" SELECT SUM(NVL(DEBIT,0)) TOTAL_COLLECTED_AMOUNT FROM BANK_ACCOUNT_LEDGER WHERE TRANS_DATE=To_Date(?,'DD-MM-YYYY') and Account_No=? ";
			
		
		PreparedStatement stmt = null;
		ResultSet r = null;
		
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,collection_date);
				stmt.setString(2,account_no);
								
				r = stmt.executeQuery();
				if (r.next())
				{					
					totalCollection=r.getDouble("TOTAL_COLLECTED_AMOUNT");
				}				

			} 
			catch (Exception e){e.printStackTrace();
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		
		return totalCollection;
	}
	
	/// getCollectionHistoryBydate
	public ArrayList<CollectionDTO> getUnAuthorizedTransactions(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		
		CollectionDTO collection=null;
		ArrayList<CollectionDTO> collectionList=new ArrayList<CollectionDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		
		if(whereClause.equals(""))
		{
			return null;
		}
		if(total==0)
				  sql ="SELECT * FROM (SELECT CUSTOMER_ID, COLLECTED_AMOUNT TOTAL_COLLECTED_AMOUNT,BILL_MONTH,BILL_YEAR,INSERTED_ON  " +
						  "  FROM VIEW_NON_METER_BILLINFO " +
						  " WHERE "+(whereClause.equalsIgnoreCase("")?"":("  "+whereClause+" "))+
						  "union all " +
						  "SELECT CUSTOMER_ID ,COLLECTED_AMOUNT TOTAL_COLLECTED_AMOUNT,BILL_MONTH,BILL_YEAR,INSERTED_ON  " +
						  "  FROM VIEW_METER_BILLINFO " +
						  " WHERE "+(whereClause.equalsIgnoreCase("")?"":("   "+whereClause+" "))+") order by INSERTED_ON asc ";
		else
			sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( SELECT * FROM (SELECT CUSTOMER_ID, COLLECTED_AMOUNT TOTAL_COLLECTED_AMOUNT,BILL_MONTH,BILL_YEAR,INSERTED_ON  " +
						  "  FROM VIEW_NON_METER_BILLINFO " +
						  " WHERE "+(whereClause.equalsIgnoreCase("")?"":("  "+whereClause+" "))+
						  "union all " +
						  "SELECT CUSTOMER_ID ,COLLECTED_AMOUNT TOTAL_COLLECTED_AMOUNT,BILL_MONTH,BILL_YEAR,INSERTED_ON  " +
						  "  FROM VIEW_METER_BILLINFO " +
						  " WHERE "+(whereClause.equalsIgnoreCase("")?"":("   "+whereClause+" "))+" ) order by INSERTED_ON asc "	+	  	  
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
					collection=new CollectionDTO();
					
					collection.setCustomer_id(r.getString("CUSTOMER_ID"));
					collection.setCollection_amount(r.getDouble("TOTAL_COLLECTED_AMOUNT"));
					collection.setBill_month_year(Month.values()[r.getInt("BILL_MONTH")-1].toString()+", "+r.getString("BILL_YEAR"));
					collectionList.add(collection);
				
					
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return collectionList;
	}
	
	
	public ResponseDTO deleteBillCollection(CollectionDTO collection)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int response_code=0;
		
		try {
       	 
   			//System.out.println("===>>Procedure : [Delete_Collection] START");            
            stmt = (OracleCallableStatement) conn.prepareCall("{ call Delete_Collection(?,?,?,?)  }");
            //System.out.println("==>>Procedure : [DeleteBankTransaction] END");
          
            
            
            stmt.setString(1, collection.getCustomer_id());
            stmt.setString(2, collection.getCollection_id());
            stmt.registerOutParameter(3, java.sql.Types.INTEGER);
            stmt.registerOutParameter(4, java.sql.Types.VARCHAR);
            
            stmt.executeUpdate();
            response_code = stmt.getInt(3);
            response.setMessasge(stmt.getString(4));
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


	public String getCollectionStatus(String collection_id){
		
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet rSet=null;
		String sqlCountCollection=" select count(BILL_ID) COUNT,BILL_ID from bill_collection_non_metered"+
                " where BILL_ID=(select bill_id from bill_collection_non_metered"+
                " where collection_id=?)"+
                " group by BILL_ID";
              
		
		int bill_type=0;
		String returnString="";
		
		try {
			stmt=conn.prepareStatement(sqlCountCollection);
			stmt.setString(1,collection_id);
			
			rSet = stmt.executeQuery();
			if (rSet.next())
			{					
				returnString=rSet.getInt("COUNT")+"#"+rSet.getString("BILL_ID");
			  
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return returnString;
		
		
		
		
	}
	public int getBillType(String collection_id){
		
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet rSet=null;
		String sqlBillType="select BILL_TYPE from bill_non_metered where bill_id in (SELECT bill_id FROM bill_collection_non_metered WHERE collection_id ="+collection_id+")";
		int bill_type=0;
		
		try {
			stmt=conn.prepareStatement(sqlBillType);
			//stmt.setString(1,collection.getCollection_id());
			rSet = stmt.executeQuery();
			
			
			
			if (rSet.next())
			{					
				bill_type=rSet.getInt("BILL_TYPE");
			 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bill_type;
		
		
		
		
	}
	
	
	public boolean canDeleteBillCollection(String customer_id,String collection_id)
	{		
		Connection conn = ConnectionManager.getConnection();
		String sql="Select Status From BANK_ACCOUNT_LEDGER Where REF_ID=? And CUSTOMER_ID=? And TRANS_TYPE=1";

		PreparedStatement stmt = null;
		ResultSet r = null;
		   
		try
			{
				stmt = conn.prepareStatement(sql);
			    stmt.setString(1, collection_id);
			    stmt.setString(2, customer_id);
			    
				r = stmt.executeQuery();
				if (r.next())
				{									
					return r.getInt("STATUS")==0?true:false;				
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();
	 		ConnectionManager.closeConnection(conn);
	 		} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return false;	 	
	}


}
