package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.jgtdsl.dto.InstallmentAgreementDTO;
import org.jgtdsl.dto.InstallmentBillDTO;
import org.jgtdsl.dto.InstallmentDTO;
import org.jgtdsl.dto.InstallmentSegmentDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.BillStatus;
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.connection.ConnectionManager;

public class InstallmentService {

	public InstallmentAgreementDTO getInstallmentAgreement(String agreement_id)
	{
		
		InstallmentAgreementDTO agreement=new InstallmentAgreementDTO();
		Connection conn = ConnectionManager.getConnection();

		String sql=" Select AGREEMENT_ID, CUSTOMER_ID, INSTALLMENT_START_FROM,to_char(AGREEMENT_DATE,'dd-MM-YYYY') AGREEMENT_DATE,  " +
				   "  NOTES, to_char(INSERTED_ON,'dd-MM-YYYY') INSERTED_ON, INSERTED_BY, STATUS From BILL_INSTALLMENT where Agreement_Id="+agreement_id;
			
		
		PreparedStatement stmt = null;
		ResultSet r = null;
		
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				if (r.next())
				{	
					agreement.setAgreementId(r.getString("AGREEMENT_ID"));
					agreement.setCustomerId(r.getString("CUSTOMER_ID"));
					agreement.setStartFrom(r.getString("INSTALLMENT_START_FROM"));
					agreement.setAgreementDate(r.getString("AGREEMENT_DATE"));
					agreement.setNotes(r.getString("NOTES"));
					agreement.setInsertedOn(r.getString("INSERTED_ON"));
					agreement.setInsertedBy(r.getString("INSERTED_BY"));
					agreement.setStatus(r.getInt("STATUS"));
				}
				

			} 
			catch (Exception e){e.printStackTrace();
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return agreement;

	}
	
	
	public ArrayList<InstallmentDTO> getInstallments(String agreement_id)
	{
		
		InstallmentDTO installment=null;
		ArrayList<InstallmentDTO> installmentList=new ArrayList<InstallmentDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="Select INSTALLMENT_ID, AGREEMENT_ID, SERIAL,  " +
		"   DESCRIPTION, to_char(DUE_DATE,'dd-MM-YYYY') DUE_DATE, BILL_MONTH,  " +
		"   BILL_YEAR,  " +
		" PRINCIPAL, SURCHARGE, METER_RENT , TOTAL, STATUS,getInstallmentSegment(INSTALLMENT_ID) Segments " +
		"  From BILL_INSTALLMENT_MST Where Agreement_Id="+agreement_id+" Order by Due_Date " ;
		
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					installment=new InstallmentDTO();
					
					installment.setInstallmentId(r.getString("INSTALLMENT_ID"));
					installment.setAgreementId(r.getString("AGREEMENT_ID"));
					installment.setSerial(r.getString("SERIAL"));
					installment.setDescription(Month.values()[r.getInt("BILL_MONTH")-1].toString()+" "+r.getString("BILL_YEAR"));
					installment.setDueDate(r.getString("DUE_DATE"));
					installment.setInstallmentBillingMonth(r.getString("BILL_MONTH"));
					installment.setInstallmentBillingYear(r.getString("BILL_YEAR"));
					installment.setPrincipal(r.getDouble("PRINCIPAL"));
					installment.setSurcharge(r.getDouble("SURCHARGE"));
					installment.setMeterRent(r.getDouble("METER_RENT"));					
					installment.setTotal(r.getDouble("TOTAL"));
					installment.setStatus(r.getInt("STATUS"));
					installment.setSegments(r.getString("Segments"));
					installment.setGeneratedBy("SERVER");
					installmentList.add(installment);
				
					
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return installmentList;
	}
	
	public ResponseDTO saveInstallments(InstallmentAgreementDTO agreement, String customer_id,String bill_ids,String installments)
	{
		
		Connection conn = ConnectionManager.getConnection();
	 	OracleCallableStatement stmt=null;
	 	int response_code=0;
	 	String response_msg="";
	 	ResponseDTO response=new ResponseDTO();
	 	
	 	String[] billIdArr;
	 	String[] installmentArr;
	 	
		billIdArr=bill_ids.split(",");
		installmentArr=installments.split("I_SEP");
		
		
		 try	
		  {
	    	ArrayDescriptor arrString = new ArrayDescriptor("VARCHARARRAY", conn);
	    	///ArrayDescriptor arrNumber = new ArrayDescriptor("NUMBERARRAY", conn);
			
		
			ARRAY inputBillId=new ARRAY(arrString,conn,billIdArr);	
	    	ARRAY inputInstallment=new ARRAY(arrString,conn,installmentArr);

			
			//System.out.println("Procedure Save_Installment_Billings Begins");
			stmt = (OracleCallableStatement) conn.prepareCall(
					 	  "{ call Save_Installment_Billings(?,?,?,?,?,?,?,?,?,?) }");
			
	 
			//stmt.setString(1, agreement.getAgreementId());
			stmt.setString(1, "1");
			stmt.setString(2, customer_id);
			stmt.setString(3, agreement.getStartFrom());
			stmt.setString(4, agreement.getAgreementDate());
			//stmt.setString(4, "11-11-2016");
			stmt.setString(5, agreement.getNotes());
			stmt.setArray(6, inputBillId);			
			stmt.setArray(7, inputInstallment);
			stmt.setString(8, "IICT");
			
			//stmt.setString(9, agreement.get);
			
			stmt.registerOutParameter(9, java.sql.Types.INTEGER);
			stmt.registerOutParameter(10, java.sql.Types.VARCHAR);
		
			stmt.executeUpdate();
			response_code = stmt.getInt(9);
			response_msg = (stmt.getString(10)).trim();

			response.setMessasge(response_msg);
			response.setResponse(response_code==1?true:false);
		  }
	    catch (Exception e){e.printStackTrace();response.setResponse(false);response.setMessasge(e.getMessage());return response;}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
	 	
		 return response;	
	
	}
	
	public ArrayList<InstallmentAgreementDTO> getInstallmentHistory(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		InstallmentAgreementDTO agreement=null;
		ArrayList<InstallmentAgreementDTO> installmentList=new ArrayList<InstallmentAgreementDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = "Select AGREEMENT_ID, BILL_INSTALLMENT.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,  " +
				  "INSTALLMENT_START_FROM, AGREEMENT_DATE, NOTES,  " +
				  "To_Char(INSERTED_ON,'dd-MM-YYYY') INSERTED_ON, INSERTED_BY, STATUS From BILL_INSTALLMENT,CUSTOMER_PERSONAL_INFO " +
				  "Where BILL_INSTALLMENT.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID "+(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"));
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select AGREEMENT_ID, BILL_INSTALLMENT.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,  " +
				      " INSTALLMENT_START_FROM, To_Char(AGREEMENT_DATE,'dd-MM-YYYY') AGREEMENT_DATE, NOTES,  " +
				      " To_Char(INSERTED_ON,'dd-MM-YYYY') INSERTED_ON, INSERTED_BY, STATUS From BILL_INSTALLMENT,CUSTOMER_PERSONAL_INFO " +
				  	  " Where BILL_INSTALLMENT.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID "+(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" "+orderByQuery+			  	  
				  	  " )tmp1 " +
				  	  " )tmp2   " +
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
					agreement=new InstallmentAgreementDTO();
					agreement.setAgreementId(r.getString("AGREEMENT_ID"));
					agreement.setCustomerId(r.getString("CUSTOMER_ID"));
					agreement.setCustomerName(r.getString("FULL_NAME"));
					agreement.setStartFrom(r.getString("INSTALLMENT_START_FROM"));
					agreement.setAgreementDate(r.getString("AGREEMENT_DATE"));
					agreement.setNotes(r.getString("NOTES"));
					agreement.setInsertedOn(r.getString("INSERTED_ON"));
					agreement.setInsertedBy(r.getString("INSERTED_BY"));
					agreement.setStatus(r.getInt("STATUS"));					
					installmentList.add(agreement);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return installmentList;
	}

	public ArrayList<InstallmentBillDTO> getBill(String installment_id,String customer_category,String area_id,String customer_id,String bill_month,String bill_year,String download_type)
	{
		InstallmentBillDTO bill=null;
		InstallmentSegmentDTO segment=null;
		ArrayList<InstallmentBillDTO> billList=new ArrayList<InstallmentBillDTO>();
		ArrayList<InstallmentSegmentDTO> segmentList=new ArrayList<InstallmentSegmentDTO>();		
		Connection conn = ConnectionManager.getConnection();	
		
		String where_condition="";
		if(download_type.equalsIgnoreCase("S")) //Single Bill
			where_condition= " AND Installment.Installment_Id = ? ";
		else{
			String monthYear=" AND  Installment.Bill_Month="+bill_month+" and Installment.Bill_Year="+bill_year;
			if(customer_id!=null && !customer_id.equalsIgnoreCase(""))
				where_condition+=" And Customer_id='"+customer_id+"'";
			else if(customer_category!=null && !customer_category.equalsIgnoreCase(""))
				where_condition+=" And area_id='"+area_id+"' And customer_category='"+customer_category+"'";
			else if(area_id!=null && !area_id.equalsIgnoreCase(""))
				where_condition+=" And area_id='"+area_id+"'" ;
			
			where_condition+=where_condition+" "+monthYear;
		}
						
		String sql="SELECT Customer_Info.Customer_Id, " +
		"       Full_Name, " +
		"       PROPRIETOR_NAME, " +
		"       CATEGORY_ID, " +
		"       CATEGORY_NAME, " +
		"       CATEGORY_TYPE, " +
		"       AREA_ID, " +
		"       AREA_NAME, " +
		"       ADDRESS, " +
		"       PHONE, " +
		"       MOBILE, " +
		"       TO_CHAR (ISSUE_DATE, 'DD-MM-YYYY') ISSUE_DATE, " +
		"       TO_CHAR (DUE_DATE, 'DD-MM-YYYY') DUE_DATE, " +
		"       to_char(GETPREVIOUSDISRECONNDATE (Customer_Info.CUSTOMER_ID)) " +
		"          LAST_DISCONN_RECONN_DATE, " +
		"       MAX_LOAD MONTHLY_CONTACTUAL_LOAD, " +
		"       MIN_LOAD, " +
		"       Installment.Installment_Id, " +
		"       Serial, " +
		"       BILL_ID, " +
		"       Installment.Bill_Month Installment_Bill_Month, " +
		"       Installment.Bill_Year Installment_Bill_Year, " +
		"       Installment.TOTAL INSTALLMENT_TOTAL, " +
		"       NUMBER_SPELLOUT_FUNC (Installment.Total) AMOUNT_IN_WORD, " +
		"       SEGMENT.Principal, " +
		"       SEGMENT.Surcharge, " +
		"       SEGMENT.MEter_Rent, " +
		"       SEGMENT.Total SEGMENT_TOTAL, " +
		"       SEGMENT.Bill_Month Segment_Bill_Month, " +
		"       SEGMENT.Bill_Year Segment_Bill_Year, " +		
		"       Installment.Status " +
		"  FROM BILL_INSTALLMENT, " +
		"       BILL_INSTALLMENT_MST Installment, " +
		"       BILL_INSTALLMENT_DTL SEGMENT, " +
		"       MVIEW_CUSTOMER_INFO Customer_Info " +
		" WHERE  BILL_INSTALLMENT.AGREEMENT_ID = Installment.AGREEMENT_ID " +
		"       AND BILL_INSTALLMENT.CUSTOMER_ID = Customer_Info.CUSTOMER_ID " +
		"       AND INSTALLMENT.INSTALLMENT_ID = SEGMENT.INSTALLMENT_ID "+  where_condition ;
		
		PreparedStatement stmt = null;
		ResultSet r = null;
		int previousInstallmentId=0;
		int i=0;
			try
			{
				stmt = conn.prepareStatement(sql);
				
				if(download_type.equalsIgnoreCase("S")){
					stmt.setString(1,installment_id);
				}
				r = stmt.executeQuery();
				while (r.next())
				{
					if((previousInstallmentId!=r.getInt("INSTALLMENT_ID") && previousInstallmentId !=0) || i==0){
						
						if(i!=0){
							bill.setSegmentList(segmentList);
							billList.add(bill);
							segmentList=new ArrayList<InstallmentSegmentDTO>();
						}
						
					bill=new InstallmentBillDTO();
					bill.setInstallmentId(r.getString("INSTALLMENT_ID"));
					bill.setInstallmentSerial(r.getString("SERIAL"));
					bill.setBill_month(r.getInt("INSTALLMENT_BILL_MONTH"));
					bill.setBill_month_name(Month.values()[r.getInt("INSTALLMENT_BILL_MONTH")-1].getLabel());
					bill.setBill_year(r.getInt("Installment_Bill_Year"));
					bill.setCustomer_id(r.getString("CUSTOMER_ID"));
					bill.setCustomer_name(r.getString("FULL_NAME"));
					bill.setProprietor_name(r.getString("PROPRIETOR_NAME"));
					bill.setCustomer_category(r.getString("CATEGORY_ID"));
					bill.setCustomer_category_name(r.getString("CATEGORY_NAME"));
					bill.setCustomer_type(r.getString("CATEGORY_TYPE"));
					bill.setArea_id(r.getString("AREA_ID"));
					bill.setArea_name(r.getString("AREA_NAME"));
					bill.setAddress(r.getString("ADDRESS"));
					bill.setPhone(r.getString("PHONE"));
					bill.setMobile(r.getString("MOBILE"));
					bill.setIssue_date(r.getString("ISSUE_DATE"));
					bill.setDueDate(r.getString("DUE_DATE"));
					bill.setLast_disconn_reconn_date(r.getString("LAST_DISCONN_RECONN_DATE"));
					bill.setMonthly_contractual_load(r.getDouble("MONTHLY_CONTACTUAL_LOAD"));
					bill.setMinimum_load(r.getDouble("MIN_LOAD"));
					bill.setPayable_amount(r.getDouble("INSTALLMENT_TOTAL"));
					bill.setAmount_in_word(r.getString("AMOUNT_IN_WORD"));
//					bill.setPrepared_by(r.getString("PREPARED_BY"));
//					bill.setPrepared_date(r.getString("PREPARED_DATE"));
					bill.setBill_status(BillStatus.values()[r.getInt("STATUS")]);
					bill.setBill_status_name(BillStatus.values()[r.getInt("STATUS")].getLabel());
					bill.setBill_status_str(BillStatus.values()[r.getInt("STATUS")].getLabel().toString());
										 
					segment=new InstallmentSegmentDTO();
					segment.setBillId(r.getString("BILL_ID"));
					segment.setBillMonth(r.getString("Segment_Bill_Month"));
					segment.setBillMonthName(Month.values()[r.getInt("Segment_Bill_Month")-1].getLabel());
					segment.setBillYear(r.getString("Segment_Bill_Year"));
					segment.setPrincipal(r.getDouble("PRINCIPAL"));
					segment.setSurcharge(r.getDouble("SURCHARGE"));
					segment.setMeterRent(r.getDouble("METER_RENT"));
					segment.setTotal(r.getDouble("SEGMENT_TOTAL"));
					segmentList.add(segment);
					
					}
					else{
						segment=new InstallmentSegmentDTO();
						segment.setBillId(r.getString("BILL_ID"));
						segment.setBillMonth(r.getString("Segment_Bill_Month"));
						segment.setBillMonthName(Month.values()[r.getInt("Segment_Bill_Month")-1].getLabel());
						segment.setBillYear(r.getString("Segment_Bill_Year"));
						segment.setPrincipal(r.getDouble("PRINCIPAL"));
						segment.setSurcharge(r.getDouble("SURCHARGE"));
						segment.setMeterRent(r.getDouble("METER_RENT"));
						segment.setTotal(r.getDouble("SEGMENT_TOTAL"));
						segmentList.add(segment);
					}
					i++;
					previousInstallmentId=r.getInt("INSTALLMENT_ID");
				}
				
				bill.setSegmentList(segmentList);
				billList.add(bill);
			} 
			catch (Exception e){e.printStackTrace();
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return billList;

	}
	
	public ArrayList<InstallmentSegmentDTO> getInstallmentSegmentList(String installmentId)
	{
		
		InstallmentSegmentDTO segment=null;
		ArrayList<InstallmentSegmentDTO> segmentList=new ArrayList<InstallmentSegmentDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="Select INSTALLMENT_ID, SEGMENT_ID, BILL_ID, BILL_MONTH, BILL_YEAR, PRINCIPAL,  " +
		"SURCHARGE, METER_RENT, TOTAL, CUSTOMER_ID From BILL_INSTALLMENT_DTL Where Installment_Id=? Order by Bill_Id ";
		
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, installmentId);
				r = stmt.executeQuery();
				while (r.next())
				{
					segment=new InstallmentSegmentDTO();
					segment.setBillId(r.getString("BILL_ID"));
					segment.setSegmentId(r.getString("SEGMENT_ID"));
					segment.setBillMonth(r.getString("BILL_MONTH"));
					segment.setBillMonthName(Month.values()[r.getInt("BILL_MONTH")-1].getLabel());
					segment.setBillYear(r.getString("BILL_YEAR"));
					segment.setPrincipal(r.getDouble("PRINCIPAL"));
					segment.setSurcharge(r.getDouble("SURCHARGE"));
					segment.setMeterRent(r.getDouble("METER_RENT"));
					segment.setTotal(r.getDouble("TOTAL"));
					segmentList.add(segment);
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return segmentList;
	}

	
}
