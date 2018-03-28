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
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;



	public class AdjustmentCollectionService {

		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
		public CollectionDTO getBillingInfo(String customer_id,String bill_month,String bill_year,String collection_date)
		{
			
			//Not a good solution. But in future we need to refactor it.
			CustomerService customerService=new CustomerService();
			String tableName="";
			CustomerDTO customer=customerService.getCustomerInfo(customer_id);
			//if(customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered"))
				tableName="SALES_ADJUSTMENT";
			//else
				//tableName="BILL_NON_METERED";
			
			//End of bad solution
			
			CollectionDTO collection=new CollectionDTO();
			collection.setCustomer(customerService.getCustomerInfo(customer_id));

			Connection conn = ConnectionManager.getConnection();
			String sql="";
			
			
			//if(customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered"))
			//{
				//if(bill_month.equalsIgnoreCase("") && bill_year.equalsIgnoreCase("")){
					//sql=" Select bill.*,(calcualteSurcharge (bill_id, '"+collection_date+"')) surcharge_per_coll,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)) ACTUAL_SURCHARGE_CAL,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)+NVL(BILLED_AMOUNT,0)) ACTUAL_PAYABLE_AMOUNT_CAL From "+tableName+" bill Where Customer_Id=? And  PAYABLE_AMOUNT<>NVL(COLLECTED_AMOUNT,0) " +
							   //" AND Bill_Id in ( Select MIN(to_number(bill_id)) from "+tableName+" Where Status=1 And Customer_Id=?)";
				
					sql=" select BILL_MONTH,BILL_YEAR,BILL_ID,STATUS,BILLED_AMOUNT ,COLLECTED_AMOUNT COLLECTED_BILLED_AMOUNT,ACTUAL_SURCHARGE ACTUAL_SURCHARGE_CAL,COLLECTED_SURCHARGE,PAYABLE_AMOUNT ACTUAL_PAYABLE_AMOUNT_CAL,TOTAL_COLLECTED_AMOUNT COLLECTED_PAYABLE_AMOUNT,null surcharge_per_coll,DOUBLE_BURNER_QNT double_burner_qnt from SALES_ADJUSTMENT Where Status=1 And Customer_Id = ? ";
					
				//}else{
					//sql=" Select bill.*,(calcualteSurcharge (bill_id, '"+collection_date+"')) surcharge_per_coll,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)) ACTUAL_SURCHARGE_CAL,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)+NVL(BILLED_AMOUNT,0)) ACTUAL_PAYABLE_AMOUNT_CAL From "+tableName+" bill Where Customer_Id=? And PAYABLE_AMOUNT<>NVL(COLLECTED_AMOUNT,0) And  " +
						///	   "BILL_MONTH="+bill_month+"And BILL_YEAR="+bill_year;
				//}
			//}
			//else
			//{
				//if(bill_month.equalsIgnoreCase("") && bill_year.equalsIgnoreCase("")){
					//sql=" Select bill.*,(calcualteSurcharge (bill_id, '"+collection_date+"')) surcharge_per_coll,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)) ACTUAL_SURCHARGE_CAL,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)+NVL(BILLED_AMOUNT,0)) ACTUAL_PAYABLE_AMOUNT_CAL From "+tableName+" bill Where Customer_Id=? And  ACTUAL_PAYABLE_AMOUNT<>NVL(COLLECTED_PAYABLE_AMOUNT,0) " +
							  // " AND Bill_Id in ( Select MIN(to_number(bill_id)) from "+tableName+" Where Status=1 And Customer_Id=?)";
					
				//}else{
				//	sql=" Select bill.*,(calcualteSurcharge (bill_id, '"+collection_date+"')) surcharge_per_coll,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)) ACTUAL_SURCHARGE_CAL,(calcualteSurcharge(bill_id,'"+collection_date+"')+NVL(ACTUAL_SURCHARGE,0)+NVL(BILLED_AMOUNT,0)) ACTUAL_PAYABLE_AMOUNT_CAL From "+tableName+" bill Where Customer_Id=? And ACTUAL_PAYABLE_AMOUNT<>NVL(COLLECTED_PAYABLE_AMOUNT,0) And  " +
				//			   "BILL_MONTH="+bill_month+"And BILL_YEAR="+bill_year;
				//}
			//}
				
				
				
			
			
			
			
			
			
			PreparedStatement stmt = null;
			ResultSet r = null;
			
				try
				{
					stmt = conn.prepareStatement(sql);
					//if(bill_month.equalsIgnoreCase("")&&bill_year.equalsIgnoreCase("")){
					//	stmt.setString(1,customer_id);
					//	stmt.setString(2,customer_id);
					////}else{				
						stmt.setString(1,customer_id);
					//}
													
					r = stmt.executeQuery();
					if (r.next())
					{	
						if(customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered")){
							collection.setBilled_amount(r.getDouble("BILLED_AMOUNT"));					
							collection.setVat_rebate_amount(r.getDouble("VAT_REBATE_AMOUNT"));
							collection.setSurcharge_amount(r.getDouble("SURCHARGE_AMOUNT"));
							collection.setAdjustment_amount(r.getDouble("ADJUSTMENT_AMOUNT"));
							collection.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
							
						}
						else{
							collection.setActual_billed_amount(r.getDouble("BILLED_AMOUNT"));
							collection.setCollected_billed_amount(r.getDouble("COLLECTED_BILLED_AMOUNT"));
							collection.setActual_surcharge_amount(r.getDouble("ACTUAL_SURCHARGE_CAL"));
							collection.setCollected_surcharge_amount(r.getDouble("COLLECTED_SURCHARGE"));
							collection.setActual_payable_amount(r.getDouble("ACTUAL_PAYABLE_AMOUNT_CAL"));
							collection.setCollected_payable_amount(r.getDouble("COLLECTED_PAYABLE_AMOUNT"));
							collection.setSurcharge_per_collection(r.getDouble("surcharge_per_coll"));
							collection.setDouble_burner_qnt(r.getString("double_burner_qnt"));
						}
						collection.setBill_month(r.getString("BILL_MONTH"));
						collection.setBill_year(r.getString("BILL_YEAR"));
						collection.setBill_id(r.getString("BILL_ID"));
						collection.setStatusId(r.getString("STATUS"));
					}
					

				} 
				catch (Exception e){e.printStackTrace();
				}
		 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
					{e.printStackTrace();}stmt = null;conn = null;}
		 		
		 		return collection;

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
			
			
			//String sql_update_metered=UPDATE SALES_ADJUSTMENT SET
			
			
			String sql_update_metered="Update BILL_METERED Set Status=2,Collected_amount=nvl(collected_amount,0)+?,BRANCH_ID=?  Where Bill_Id=?";
			
			String sql_update_non_metered ="UPDATE SALES_ADJUSTMENT SET STATUS=2,COLLECTED_AMOUNT =nvl(COLLECTED_AMOUNT,0)+?,COLLECTED_SURCHARGE=nvl(COLLECTED_SURCHARGE,0)+?,TOTAL_COLLECTED_AMOUNT =nvl(TOTAL_COLLECTED_AMOUNT,0)+? WHERE Bill_Id = ?";
					
					
					
					/*"UPDATE BILL_NON_METERED "+
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
						 "WHERE Bill_Id = ? "; */

			
			String sql_mobile_phone="Update CUSTOMER_PERSONAL_INFO Set Mobile=?,Phone=? Where Customer_Id=?";
			
			String customer_ledger_query=" Insert into CUSTOMER_LEDGER(TRANS_ID, TRANS_DATE, PARTICULARS, CREDIT, COLLECTION_ID,BILL_ID,INSERTED_BY, CUSTOMER_ID, STATUS) "+
										 " Values(SQN_CL.NEXTVAL, TO_DATE(?, 'DD-MM-YYYY'),?, ?, ?,?, ?, ?,0)";    

			String bank_account_ledger_query=" Insert into BANK_ACCOUNT_LEDGER(TRANS_ID, TRANS_DATE, TRANS_TYPE, PARTICULARS, BANK_ID,BRANCH_ID, ACCOUNT_NO,DEBIT,REF_ID, " +
									   " INSERTED_BY, CUSTOMER_ID, STATUS) " +
									   " Values(?, TO_DATE(?, 'DD-MM-YYYY'), ?, ?, ?,?,?,?,?,?,?,0)";
			
			String bank_account_ledger_id_query="Select SQN_BAL.NEXTVAL BAL_TRANS_ID from dual";
			
			String bank_book_info_select="Select Payable_Amount,GAS_BILL,METER_RENT,SURCHARGE_AMOUNT  " +
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
						stmt.setDouble(1,collection.getCollected_amount());	
						stmt.setString(2,collection.getBranch_id());
						stmt.setString(3,collection.getBill_id());
						stmt.executeUpdate();
					}
					else{
						stmt = conn.prepareStatement(sql_update_non_metered);
						stmt.setDouble(1,collection.getCollected_amount());
						//stmt.setDouble(2,collection.getActual_surcharge_amount());
						stmt.setDouble(2,collection.getSurcharge_amount());
						//stmt.setDouble(4,collection.getActual_payable_amount());
						//stmt.setDouble(5,(collection.getSurcharge_amount()+collection.getCollected_amount()));
						stmt.setDouble(3,(collection.getSurcharge_amount()+collection.getCollected_amount()));
						stmt.setString(4,collection.getBill_id());
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
		
		public ResponseDTO saveMultiMonthCollection(MultiCollectionDTO multiColl)
		{
			//bill_collection#surcharge_collection#actual_surcharge#actual_payable#surcharge_per_collection
			Connection conn = ConnectionManager.getConnection();
		 	OracleCallableStatement stmt=null;
		 	int response_code=0;
		 	String response_msg="";
		 	ResponseDTO response=new ResponseDTO();
			String pending_bills_str=multiColl.getPending_bills_str();
			String advanced_bills_str=multiColl.getAdvanced_bills_str();
			if(pending_bills_str.length()>0)
				pending_bills_str=pending_bills_str.substring(0, pending_bills_str.length()-1);
			if(advanced_bills_str.length()>0)
				advanced_bills_str=advanced_bills_str.substring(0, advanced_bills_str.length()-1);
			
		 
			String[] pendingArr;
			if(pending_bills_str.equalsIgnoreCase(""))
				pendingArr=new String[0];
			else 
				pendingArr=pending_bills_str.split("@");
			
			String[] advancedArr;
			if(advanced_bills_str.equalsIgnoreCase(""))
				advancedArr=new String[0];
			else 
				advancedArr=advanced_bills_str.split("@");
			
			String[] pendingBillId = new String[pendingArr.length];
			String[] pendingBillCollectedAmount = new String[pendingArr.length];
			String[] pendingBillCollectedSurcharge = new String[pendingArr.length];
			String[] pendingBillActualAmount = new String[pendingArr.length];
			String[] pendingBillActualSurcharge = new String[pendingArr.length];
			String[] pendingSurchargePerCollection = new String[pendingArr.length];
			
			String[] advancedBillMonth=new String[advancedArr.length];
			String[] advancedBillYear=new String[advancedArr.length];
			String[] advancedBillAmount=new String[advancedArr.length];
			
			
			
			
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
			
			
			
//			for(int i=0;i<pendingArr.length;i++){
//				String[] pendings=pendingArr[i].split("#");
//				
//				
//				
//				pendingBillId[i]=pendings[0];
//				pendingBillCollectedAmount[i]=pendings[1];
//				pendingBillCollectedSurcharge[i]=pendings[2];
//				pendingBillActualSurcharge[i]=pendings[3];
//				pendingBillActualAmount[i]=pendings[4];
//				pendingSurchargePerCollection[i]=pendings[5];
//			}
			
			
			
//			for(int i=0;i<advancedArr.length;i++){
//				String[] advanced=advancedArr[i].split("#");
//				advancedBillMonth[i]=advanced[0];
//				advancedBillYear[i]=advanced[1];
//				advancedBillAmount[i]=advanced[2];
//			}
//			
			
			ArrayList<MultiCollStrDTO> listAd= new ArrayList<MultiCollStrDTO>();
			
			for(int i=0;i<advancedArr.length;i++){
				String[] advanced=advancedArr[i].split("#");
				
				MultiCollStrDTO ad = new MultiCollStrDTO();
				
				ad.setBillMonth(Integer.parseInt(advanced[0]));
				ad.setBillYear(Integer.parseInt(advanced[1]));			
				ad.setCollectedAmount(advanced[2]);									
				
				listAd.add(ad);
			}
		
			Collections.sort(listAd, MultiCollStrDTO.Comparators.billYearMonth );
			
			
			for(int i=0;i<listAd.size();i++){			
				mc=listAd.get(i);
				
				advancedBillMonth[i]= String.valueOf(mc.getBillMonth()) ;
				advancedBillYear[i]=String.valueOf(mc.getBillYear());
				advancedBillAmount[i]=mc.getCollectedAmount();	
				
			}
			
			
//////////////////////////////		End //////////////////
			
			
		  
		    try
			  {
		    	ArrayDescriptor arrString = new ArrayDescriptor("VARCHARARRAY", conn);
				
			
		    	ARRAY inputPendingBillId=new ARRAY(arrString,conn,pendingBillId);
				ARRAY inputPendingBillCollectedAmount=new ARRAY(arrString,conn,pendingBillCollectedAmount);
				ARRAY inputPendingBillCollectedSurcharge=new ARRAY(arrString,conn,pendingBillCollectedSurcharge);
				ARRAY inputPendingBillActualSurcharge=new ARRAY(arrString,conn,pendingBillActualSurcharge);
				ARRAY inputPendingBillActualAmount=new ARRAY(arrString,conn,pendingBillActualAmount);
				ARRAY inputPendingBillSurchargePerCollection=new ARRAY(arrString,conn,pendingSurchargePerCollection);
				ARRAY inputAdvancedBillMonth=new ARRAY(arrString,conn,advancedBillMonth);
				ARRAY inputAdvancedBillYear=new ARRAY(arrString,conn,advancedBillYear);
				ARRAY inputAdvancedBillAmount=new ARRAY(arrString,conn,advancedBillAmount);
				
				//System.out.println("Procedure Save_Multi_Month_Collection Begins");
				stmt = (OracleCallableStatement) conn.prepareCall(
						 	  "{ call Save_Multi_Month_Collection	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
				 
				
				stmt.setString(1, multiColl.getCustomer_id());
				stmt.setString(2, multiColl.getBank_id());
				stmt.setString(3, multiColl.getBranch_id());
				stmt.setString(4, multiColl.getAccount_no());
				stmt.setString(5, multiColl.getCollection_date());
				stmt.setString(6, loggedInUser.getUserName());
				stmt.setARRAY(7, inputPendingBillId);
				stmt.setARRAY(8,  inputPendingBillCollectedAmount);
				stmt.setARRAY(9,  inputPendingBillCollectedSurcharge);
				stmt.setARRAY(10,  inputPendingBillActualSurcharge);
				stmt.setARRAY(11,  inputPendingBillActualAmount);
				stmt.setARRAY(12,  inputPendingBillSurchargePerCollection);
				stmt.setARRAY(13,  inputAdvancedBillMonth);
				stmt.setARRAY(14,  inputAdvancedBillYear);
				stmt.setARRAY(15,  inputAdvancedBillAmount);
				stmt.registerOutParameter(16, java.sql.Types.INTEGER);
				stmt.registerOutParameter(17, java.sql.Types.VARCHAR);
				
				stmt.executeUpdate();
				response_code = stmt.getInt(16);
				response_msg = (stmt.getString(17)).trim();
				
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
			//Not a good solution. But in future we need to refactor it.
			CustomerService customerService=new CustomerService();
			String tableName="";
			int sIndex=whereClause.indexOf("'");
			String[] cust=whereClause.split("'");
			CustomerDTO customer=customerService.getCustomerInfo(cust[1]);
			String surchargeCondition="";
			String statusClause="";
			
			
			if(customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered")){
				tableName="VIEW_METER_ADJUST_COLLINFO";
				surchargeCondition=" BILL.SURCHARGE_AMOUNT, ";
				statusClause="bill.Status=2";
			}

			else{ 
				tableName="VIEW_NON_MET_COLLINFO";
				surchargeCondition=" BILL.SURCHARGE_AMOUNT,BILL.SURCHARGE_COLLECTED, BILL.SURCHARGE_PER_COLL ,";
				statusClause="bill.Status in(1,2,3)";
			}
			
			//End of bad solution
			
			ArrayList<CollectionDTO> collectionList=new ArrayList<CollectionDTO>();
			CollectionDTO collection=null;
			
			Connection conn = ConnectionManager.getConnection();	
			String orderByQuery="";
			if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
				orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
			String sql="";
			if(total==0)
					  sql = " Select COLLECTION_ID, " +
						  	"         bill.CUSTOMER_ID, " +
							"         TO_CHAR(TO_DATE(bill.collection_date, 'dd-MM-YY'),'dd-MON-YY') COLLECTION_DATE_F1, "+
							" 		  TO_CHAR(bill.collection_date,'dd-MM-rrrr') COLLECTION_DATE, "+	
							"         BILL.BILL_ID, " +
							"         BILL.CUSTOMER_NAME, " +
							"         BILL.METERED_STATUS, " +
							"         bill.BANK_ID, " +
							"         Bank_Name, " +
							"         bill.BRANCH_ID, " +
							"         BRANCH_NAME, " +
							"         bill.ACCOUNT_NO, " +
							"         ACCOUNT_NAME, " +
							"         Bill_Month, " +
							"         Bill_Year, " +
							"         BILL.BILLED_AMOUNT,BILL.COLLECTED_BILLED_AMOUNT, " +
							"		  BILL.ADJUSTMENT_AMOUNT ,"+
							"          " +surchargeCondition+
							"         BILL.VAT_REBATE_AMOUNT, " +
							"         BILL.PAYABLE_AMOUNT, " +
							"		  bill.TAX_AMOUNT,"+
							"         bill.COLLECTED_AMOUNT, " +
							"         bill.REMARKS "+
							"From "+tableName+" bill, MST_BANK_INFO bank,MST_BRANCH_INFO branch,MST_ACCOUNT_INFO account Where" +
							(whereClause.equalsIgnoreCase("")?" ":"  ( "+whereClause+")")+ 
							" And bill.BANK_ID=bank.BANK_ID " +
							" and bill.BRANCH_ID=branch.BRANCH_ID " +
							" and bill.ACCOUNT_NO=account.ACCOUNT_NO " +
							" AND bill.BANK_ID = account.BANK_ID"+
					        " AND bill.BRANCH_ID = account.BRANCH_ID "+ 
							" AND branch.AREA_ID="+loggedInUser.getArea_id()+" "+				
							" And "+statusClause+" Order by Bill_year desc,Bill_month desc";
			
			else
					  sql=  " Select * from ( " +
					  	    " Select rownum serial,tmp1.* from " +
					  	    " ( Select COLLECTION_ID, " +
							"         bill.CUSTOMER_ID, " +
							"         TO_CHAR(TO_DATE(bill.collection_date, 'dd-MM-YY'),'dd-MON-YY') COLLECTION_DATE_F1, "+
							" 		  TO_CHAR(bill.collection_date,'dd-MM-rrrr') COLLECTION_DATE, "+	
							"         BILL.BILL_ID, " +
							"         BILL.CUSTOMER_NAME, " +
							"         BILL.METERED_STATUS, " +
							"         bill.BANK_ID, " +
							"         Bank_Name, " +
							"         bill.BRANCH_ID, " +
							"         BRANCH_NAME, " +
							"         bill.ACCOUNT_NO, " +
							"         ACCOUNT_NAME, " +
							"         Bill_Month, " +
							"         Bill_Year, " +
							"         BILL.BILLED_AMOUNT,BILL.COLLECTED_BILLED_AMOUNT, " +
							"		  BILL.ADJUSTMENT_AMOUNT ,"+
							"          " +surchargeCondition+
							"         BILL.VAT_REBATE_AMOUNT, " +
							"         BILL.PAYABLE_AMOUNT, " +
							"		  bill.TAX_AMOUNT,"+
							"         bill.COLLECTED_AMOUNT, " +
							"         bill.REMARKS "+
							"From "+tableName+" bill, MST_BANK_INFO bank,MST_BRANCH_INFO branch,MST_ACCOUNT_INFO account Where " +
							(whereClause.equalsIgnoreCase("")?" ":"  ( "+whereClause+")")+
							" And bill.BANK_ID=bank.BANK_ID " +
							" and bill.BRANCH_ID=branch.BRANCH_ID " +
							" and bill.ACCOUNT_NO=account.ACCOUNT_NO " +	
							" AND bill.BANK_ID = account.BANK_ID"+
					        " AND bill.BRANCH_ID = account.BRANCH_ID "+ 
							" AND branch.AREA_ID="+loggedInUser.getArea_id()+" "+
							" And "+statusClause+" Order by Bill_year desc,Bill_month desc"+
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
						collection=new  CollectionDTO();
						collection.setCollection_id(r.getString("COLLECTION_ID"));
						collection.setCustomer_id(r.getString("CUSTOMER_ID"));
						collection.setCollection_date(r.getString("COLLECTION_DATE"));
						collection.setCollection_date_f1(r.getString("COLLECTION_DATE_F1"));
						collection.setBill_id(r.getString("BILL_ID"));
						collection.setCustomer_name(r.getString("CUSTOMER_NAME"));
						collection.setBank_id(r.getString("BANK_ID"));
						collection.setBank_name(r.getString("BANK_NAME"));
						collection.setBranch_id(r.getString("BRANCH_ID"));
						collection.setBranch_name(r.getString("BRANCH_NAME"));
						collection.setAccount_no(r.getString("ACCOUNT_NO"));
						collection.setAccount_name(r.getString("ACCOUNT_NAME"));
						
						collection.setBill_month(r.getString("BILL_MONTH"));
						collection.setBill_month_name(Month.values()[r.getInt("BILL_MONTH")-1].getLabel());
						collection.setBill_year(r.getString("BILL_YEAR"));					
						collection.setBill_month_year(Month.values()[r.getInt("BILL_MONTH")-1].toString()+", "+r.getString("BILL_YEAR"));
						
						
						if(customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered")){
							collection.setBilled_amount(r.getDouble("BILLED_AMOUNT"));					
							collection.setVat_rebate_amount(r.getDouble("VAT_REBATE_AMOUNT"));
							collection.setCollected_surcharge_amount(r.getDouble("SURCHARGE_AMOUNT"));
							collection.setAdjustment_amount(r.getDouble("ADJUSTMENT_AMOUNT"));
							collection.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
							collection.setTax_amount(r.getDouble("TAX_AMOUNT"));
							
						}
						else{
							collection.setActual_billed_amount(r.getDouble("BILLED_AMOUNT"));						
							collection.setCollected_billed_amount(r.getDouble("COLLECTED_BILLED_AMOUNT"));
							collection.setActual_surcharge_amount(r.getDouble("SURCHARGE_AMOUNT"));
							collection.setCollected_surcharge_amount(r.getDouble("SURCHARGE_COLLECTED"));
							collection.setSurcharge_per_collection(r.getDouble("SURCHARGE_PER_COLL"));
							collection.setActual_payable_amount(r.getDouble("PAYABLE_AMOUNT"));
							collection.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));//grid
							collection.setBilled_amount(r.getDouble("BILLED_AMOUNT"));	//grid
							collection.setSurcharge_amount(r.getDouble("SURCHARGE_AMOUNT"));//grid
							
						}
						
						collection.setCollected_payable_amount(r.getDouble("COLLECTED_AMOUNT"));
						collection.setCollection_amount(r.getDouble("COLLECTED_AMOUNT"));//Collection_amount from collection table(grid)					
						collection.setRemarks(r.getString("REMARKS"));
						
						collectionList.add(collection);
					}
				} 
				catch (Exception e){e.printStackTrace();}
		 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
					{e.printStackTrace();}stmt = null;conn = null;}
			
		 		return collectionList;


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
			String sql=" Select sum(TOTAL_COLLECTED_AMOUNT) TOTAL_COLLECTED_AMOUNT from (Select SUM(TOTAL_COLLECTED_AMOUNT) TOTAL_COLLECTED_AMOUNT from BILL_COLLECTION_NON_METERED Where Collection_Date=To_Date(?,'DD-MM-YYYY') and Account_No=?"+
	                   "union all"+
	                   " Select SUM(COLLECTION_AMOUNT) TOTAL_COLLECTED_AMOUNT from BILL_COLLECTION_METERED Where Collection_Date=To_Date(?,'DD-MM-YYYY') and Account_No=?)";
				
			
			PreparedStatement stmt = null;
			ResultSet r = null;
			
				try
				{
					stmt = conn.prepareStatement(sql);
					stmt.setString(1,collection_date);
					stmt.setString(2,account_no);
					stmt.setString(3,collection_date);
					stmt.setString(4,account_no);
									
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
