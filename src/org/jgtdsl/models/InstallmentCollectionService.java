package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.jgtdsl.dto.InstallmentAgreementDTO;
import org.jgtdsl.dto.InstallmentCollectionDTO;
import org.jgtdsl.dto.InstallmentCollectionDtlDTO;
import org.jgtdsl.dto.InstallmentDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.connection.ConnectionManager;

public class InstallmentCollectionService {

	public InstallmentCollectionDTO getInstallmentCollectionInfo(String installmentId)
	{
		InstallmentCollectionDTO iCollectionDTO=null;
		Connection conn = ConnectionManager.getConnection();
		String sql="Select Tmp1.*, Collected_Amount,Tax_Amount,TO_CHAR(COLLECTION_DATE,'DD-MM-YYYY') COLLECTION_DATE, " +
		"bank.Bank_Id,Bank_Name,BRANCH.BRANCH_ID, branch_name,account.ACCOUNT_NO, ACCOUNT_NAME, " +
		" INSERTED_BY, INSERTED_ON " +
		"From " +
		"(Select CUSTOMER_INFO.CUSTOMER_ID,FULL_NAME,CATEGORY_NAME,CATEGORY_TYPE, DECODE(ISMETERED,0,'Non-Metered','Mertered') ISMETERED,INSTALLMENT_ID,PHONE,MOBILE,SERIAL,BILL_MONTH,BILL_YEAR,DESCRIPTION, " +
		"PRINCIPAL,SURCHARGE,METER_RENT,TOTAL,BILL_INSTALLMENT_MST.STATUS COLLECTION_STATUS,  " +
		"DECODE(BILL_INSTALLMENT_MST.STATUS,0,'Not-Collected',1,'Collected','N/A') COLLECTION_STATUS_NAME " +
		"From BILL_INSTALLMENT,BILL_INSTALLMENT_MST,MVIEW_CUSTOMER_INFO CUSTOMER_INFO " +
		"Where  " +
		"BILL_INSTALLMENT.AGREEMENT_ID=BILL_INSTALLMENT_MST.AGREEMENT_ID " +
		"And BILL_INSTALLMENT_MST.Installment_Id=? " +
		"And BILL_INSTALLMENT.CUSTOMER_ID=CUSTOMER_INFO.CUSTOMER_ID)Tmp1 " +
		"Left  Join INSTALLMENT_COLLECTION iCollection " +
		"    On Tmp1.Installment_Id=iCollection.Installment_Id " +
		"Left  Join MST_BANK_INFO bank " +
		"    On iCollection.Bank_Id=bank.Bank_Id " +
		"Left  Join MST_BRANCH_INFO branch " +
		"    On iCollection.Branch_Id=branch.Branch_Id     " +
		"Left  Join MST_ACCOUNT_INFO account " +
		"    On iCollection.Account_No=ACCOUNT.ACCOUNT_NO ";
		
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, installmentId);
				r = stmt.executeQuery();
				
				while (r.next())
				{
					iCollectionDTO=new InstallmentCollectionDTO();
					
					iCollectionDTO.setCustomerId(r.getString("CUSTOMER_ID"));
					iCollectionDTO.setCustomerName(r.getString("FULL_NAME"));
					iCollectionDTO.setCustomerCategory(r.getString("CATEGORY_NAME"));
					iCollectionDTO.setCustomerType(r.getString("CATEGORY_TYPE"));
					iCollectionDTO.setIsMetered(r.getString("ISMETERED"));
					iCollectionDTO.setInstallmentId(r.getString("INSTALLMENT_ID"));
					iCollectionDTO.setPhone(r.getString("PHONE"));
					iCollectionDTO.setMobile(r.getString("MOBILE"));
					iCollectionDTO.setInstallmentSerial(r.getString("SERIAL"));
					iCollectionDTO.setInstallmentBillMonth(r.getString("BILL_MONTH"));
					iCollectionDTO.setInstallmentBillYear(r.getString("BILL_YEAR"));
					iCollectionDTO.setInstallmentDescription(r.getString("DESCRIPTION"));
					iCollectionDTO.setPrincipal(r.getDouble("PRINCIPAL"));
					iCollectionDTO.setSurcharge(r.getDouble("SURCHARGE"));
					iCollectionDTO.setMeterRent(r.getDouble("METER_RENT"));
					iCollectionDTO.setTotal(r.getDouble("TOTAL"));
					iCollectionDTO.setCollectionStatus(r.getString("COLLECTION_STATUS"));
					iCollectionDTO.setCollectionStatusName(r.getString("COLLECTION_STATUS_NAME"));
					iCollectionDTO.setCollectedAmount(r.getDouble("Collected_Amount"));
					iCollectionDTO.setTaxAmount(r.getDouble("Tax_Amount"));
					iCollectionDTO.setCollectionDate(r.getString("COLLECTION_DATE"));
					iCollectionDTO.setBankId(r.getString("Bank_Id"));
					iCollectionDTO.setBankName(r.getString("Bank_Name"));
					iCollectionDTO.setBranchId(r.getString("BRANCH_ID"));
					iCollectionDTO.setBranchName(r.getString("BRANCH_NAME"));
					iCollectionDTO.setAccountNo(r.getString("ACCOUNT_NO"));
					iCollectionDTO.setAccountName(r.getString("ACCOUNT_NAME"));
					iCollectionDTO.setInsertedBy(r.getString("INSERTED_BY"));
					iCollectionDTO.setInsertedOn(r.getString("INSERTED_ON"));
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return iCollectionDTO;
	}
	
	public ArrayList<InstallmentCollectionDtlDTO> getInstallmentCollectionDetailList(String installmentId)
	{
		
		InstallmentCollectionDtlDTO collectionDTO=null;
		ArrayList<InstallmentCollectionDtlDTO> collectionDetailList=new ArrayList<InstallmentCollectionDtlDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="SELECT INSTALLMENT_COLLECTION_DTL.INSTALLMENT_ID, INSTALLMENT_COLLECTION_DTL. " +
		"SEGMENT_ID,BILL_ID,BILL_MONTH,BILL_YEAR, INSTALLMENT_COLLECTION_DTL.PRINCIPAL, INSTALLMENT_COLLECTION_DTL.SURCHARGE, INSTALLMENT_COLLECTION_DTL.METER_RENT,  " +
		"TAX, INSTALLMENT_COLLECTION_DTL.TOTAL " +
		"FROM INSTALLMENT_COLLECTION_DTL ,BILL_INSTALLMENT_DTL Where  " +
		"INSTALLMENT_COLLECTION_DTL.INSTALLMENT_ID=BILL_INSTALLMENT_DTL.INSTALLMENT_ID " +
		"And INSTALLMENT_COLLECTION_DTL.SEGMENT_ID=BILL_INSTALLMENT_DTL.SEGMENT_ID " +
		"And INSTALLMENT_COLLECTION_DTL.Installment_Id=? Order by Segment_Id ";
				   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, installmentId);
				r = stmt.executeQuery();
				while (r.next())
				{
					collectionDTO=new InstallmentCollectionDtlDTO();

					collectionDTO.setInstallmentId(r.getString("INSTALLMENT_ID"));
					collectionDTO.setSegmentId(r.getString("SEGMENT_ID"));
					collectionDTO.setBillId(r.getString("BILL_ID"));
					collectionDTO.setBillMonth(r.getString("BILL_MONTH"));
					collectionDTO.setBillMonthName(Month.values()[r.getInt("BILL_MONTH")-1].toString());
					collectionDTO.setBillYear(r.getString("BILL_YEAR"));
					collectionDTO.setPrincipal(r.getDouble("PRINCIPAL"));
					collectionDTO.setSurcharge(r.getDouble("SURCHARGE"));
					collectionDTO.setMeterRent(r.getDouble("METER_RENT"));	
					collectionDTO.setTotal(r.getDouble("TAX"));
					collectionDTO.setTotal(r.getDouble("TOTAL"));
					collectionDetailList.add(collectionDTO);
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return collectionDetailList;
	}
	public ResponseDTO saveInstallmentCollection(InstallmentCollectionDTO collection,String collectionDetailStr)
	{

		Connection conn = ConnectionManager.getConnection();
	 	OracleCallableStatement stmt=null;
	 	int response_code=0;
	 	String response_msg="";
	 	ResponseDTO response=new ResponseDTO();
	 	
	 	String[] detailArr;
	 	String[] billIdArr;
	 	String[] segmentIdArr;
	 	double[] principalArr;
	 	double[] surchargeArr;
	 	double[] meterRentArr;
	 	double[] taxArr;
	 	double[] totalArr;
	 	
	 	detailArr=collectionDetailStr.split("@");
	 	billIdArr=new String[detailArr.length];
	 	segmentIdArr=new String[detailArr.length];
	 	principalArr=new double[detailArr.length];
	 	surchargeArr=new double[detailArr.length];
	 	meterRentArr=new double[detailArr.length];
	 	taxArr=new double[detailArr.length];
	 	totalArr=new double[detailArr.length];
		for(int i=0;i<detailArr.length;i++){
			
			String[] detailStr=detailArr[i].split("#");
			billIdArr[i]=detailStr[0];
			segmentIdArr[i]=detailStr[1];
			principalArr[i]=Double.valueOf(detailStr[2]);
			surchargeArr[i]=Double.valueOf(detailStr[3]);
			meterRentArr[i]=Double.valueOf(detailStr[4].equalsIgnoreCase("")?"0":detailStr[4]);
			taxArr[i]=Double.valueOf(detailStr[5].equalsIgnoreCase("")?"0":detailStr[5]);
			totalArr[i]=Double.valueOf(detailStr[6]);			
		}
		
		
		 try	
		  {
	    	ArrayDescriptor arrString = new ArrayDescriptor("VARCHARARRAY", conn);
	    	ArrayDescriptor arrNumber = new ArrayDescriptor("NUMBERS", conn);
	    	
		
			ARRAY inputBillId=new ARRAY(arrString,conn,billIdArr);	
			ARRAY inputSegmentId=new ARRAY(arrString,conn,segmentIdArr);
			ARRAY inputPrincipal=new ARRAY(arrNumber,conn,principalArr);
			ARRAY inputSurcharge=new ARRAY(arrNumber,conn,surchargeArr);
			ARRAY inputMeterRent=new ARRAY(arrNumber,conn,meterRentArr);
			ARRAY inputTax=new ARRAY(arrNumber,conn,taxArr);
			ARRAY inputTotal=new ARRAY(arrNumber,conn,totalArr);

			
			//System.out.println("Procedure SaveInstallmentCollection Begins");
			stmt = (OracleCallableStatement) conn.prepareCall(
					 	  "{ call SaveInstallmentCollection(?,?,to_date(?,'dd-MM-YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

			stmt.setString(1, collection.getCustomerId());
			stmt.setString(2, collection.getInstallmentId());
			stmt.setString(3, collection.getCollectionDate());
			stmt.setString(4, collection.getBankId());
			stmt.setString(5, collection.getBranchId());
			stmt.setString(6, collection.getAccountNo());
			stmt.setDouble(7, collection.getCollectedAmount());
			stmt.setDouble(8, collection.getTaxAmount());
			
			stmt.setArray(9, inputBillId);
			stmt.setArray(10, inputSegmentId);
			stmt.setArray(11, inputPrincipal);
			stmt.setArray(12, inputSurcharge);
			stmt.setArray(13, inputMeterRent);
			stmt.setArray(14, inputTax);	
			stmt.setArray(15, inputTotal);
			stmt.setString(16, "Ifti");
			
			
			stmt.registerOutParameter(17, java.sql.Types.INTEGER);
			stmt.registerOutParameter(18, java.sql.Types.VARCHAR);
			
			stmt.executeUpdate();
			response_code = stmt.getInt(17);
			response_msg = (stmt.getString(18)).trim();

			response.setMessasge(response_msg);
			response.setResponse(response_code==1?true:false);
		  }
	    catch (Exception e){e.printStackTrace();response.setResponse(false);response.setMessasge(e.getMessage());return response;}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
	 	
		 return response;	
	
	}
	
}
