package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.DemandNoteDTO;
import org.jgtdsl.dto.DepositDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.DepositPurpose;
import org.jgtdsl.utils.connection.ConnectionManager;

public class DemandNoteService {

	public static synchronized ResponseDTO saveDemandNote(DemandNoteDTO dNote)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		String sql="Insert into DEMAND_NOTE(CUSTOMER_ID,BANK,BRANCH,ACCOUNT,SD_KA_1,SD_KA_AMOUNT,SD_KHA_1,SD_KHA_2,SD_KHA_AMOUNT,SD_GA_1,SD_GA_2,SD_GA_AMOUNT,BSD_KA_AMOUNT,BSD_KHA_AMOUNT,CL_KA_AMOUNT,CL_KHA_1,CL_KHA_AMOUNT,CL_GA_1,CL_GA_2,CL_GA_3,CL_GA_AMOUNT,CL_GHA_1,CL_GHA_2,CL_GHA_AMOUNT,CL_UMA_L1,CL_UMA_L1_AMOUNT,CL_UMA_L2,CL_UMA_L2_AMOUNT,OF_KA_AMOUNT,OF_KHA_AMOUNT,OF_GA_AMOUNT,OF_GHA_AMOUNT,OF_UMA_AMOUNT,OF_CH_AMOUNT,OF_CHO_1,OF_CHO_2,OF_CHO_AMOUNT,OF_JO_AMOUNT,OTHERS_KA_AMOUNT,OTHERS_KHA_AMOUNT,OTHERS_GA_AMOUNT,OTHERS_GHA_AMOUNT,TOTAL_IN_AMOUNT,DEMAND_NOTE_ID,ISSUE_DATE)" +
				   " Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,trunc(sysdate)) ";
		
		PreparedStatement stmt = null;
		PreparedStatement mst_stmt = null;
		String demand_note_id=null;
		ResultSet r = null;
			try
			{
				mst_stmt = conn.prepareStatement("Select SQN_DEMANDNOTE.nextval demand_note_id from dual");
				r = mst_stmt.executeQuery();
				if (r.next())
					demand_note_id=r.getString("demand_note_id"); 
				//DebugLevel debug = DebugLevel.ON;
				//stmt = StatementFactory.getStatement(conn,sql,debug);
				
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,dNote.getCustomer_id());stmt.setString(2,"1");stmt.setString(3,"1");
				stmt.setString(4,"1");stmt.setString(5,dNote.getSd_ka_1());stmt.setString(6,dNote.getSd_ka_amount());
				stmt.setString(7,dNote.getSd_kha_1());stmt.setString(8,dNote.getSd_kha_2());stmt.setString(9,dNote.getSd_kha_amount());
				stmt.setString(10,dNote.getSd_ga_1());stmt.setString(11,dNote.getSd_ga_2());stmt.setString(12,dNote.getSd_ga_amount());
				stmt.setString(13,dNote.getBsd_ka_amount());stmt.setString(14,dNote.getBsd_kha_amount());
				stmt.setString(15,dNote.getCl_ka_amount());stmt.setString(16,dNote.getCl_kha_1());stmt.setString(17,dNote.getCl_kha_amount());				
				stmt.setString(18,dNote.getCl_ga_1());stmt.setString(19,dNote.getCl_ga_2());stmt.setString(20,dNote.getCl_ga_3());stmt.setString(21,dNote.getCl_ga_amount());				
				stmt.setString(22,dNote.getCl_gha_1());stmt.setString(23,dNote.getCl_gha_2());stmt.setString(24,dNote.getCl_gha_amount());				
				stmt.setString(25,dNote.getCl_uma_l1());stmt.setString(26,dNote.getCl_uma_l1_amount());
				stmt.setString(27,dNote.getCl_uma_l2());stmt.setString(28,dNote.getCl_uma_l2_amount());
				stmt.setString(29,dNote.getOf_ka_amount());stmt.setString(30,dNote.getOf_kha_amount());
				stmt.setString(31,dNote.getOf_ga_amount());stmt.setString(32,dNote.getOf_gha_amount());
				stmt.setString(33,dNote.getOf_uma_amount());stmt.setString(34,dNote.getOf_ch_amount());
				stmt.setString(35,dNote.getOf_cho_1());stmt.setString(36,dNote.getOf_cho_2());
				stmt.setString(37,dNote.getOf_cho_amount());stmt.setString(38,dNote.getOf_jo_amount());
				stmt.setString(39,dNote.getOthers_ka_amount());stmt.setString(40,dNote.getOthers_kha_amount());
				stmt.setString(41,dNote.getOthers_ga_amount());stmt.setString(42,dNote.getOthers_gha_amount());
				stmt.setFloat(43,dNote.getTotal_in_amount());
				stmt.setString(44,demand_note_id);
//				System.out.println(" debuggable statement= " + stmt.toString());
				
				stmt.executeUpdate();
				response.setMessasge("Successfully Created the Demand Note.");
				response.setResponse(true);

			} 
			catch (Exception e){e.printStackTrace();
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		return response;

	}
	

	
	
	public static synchronized ResponseDTO updateDemandNote(DemandNoteDTO dNote)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		String sql=" UPDATE DEMAND_NOTE SET BANK=?,BRANCH=?,ACCOUNT=?,SD_KA_1=?,SD_KA_AMOUNT=?,SD_KHA_1=?,SD_KHA_2=?,SD_KHA_AMOUNT=?,SD_GA_1=?,SD_GA_2=?,SD_GA_AMOUNT=?,BSD_KA_AMOUNT=?,BSD_KHA_AMOUNT=?,CL_KA_AMOUNT=?,CL_KHA_1=?,CL_KHA_AMOUNT=?,CL_GA_1=?,CL_GA_2=?,CL_GA_3=?,CL_GA_AMOUNT=?,CL_GHA_1=?,CL_GHA_2=?,CL_GHA_AMOUNT=?,CL_UMA_L1=?,CL_UMA_L1_AMOUNT=?,CL_UMA_L2=?,CL_UMA_L2_AMOUNT=?,OF_KA_AMOUNT=?,OF_KHA_AMOUNT=?,OF_GA_AMOUNT=?,OF_GHA_AMOUNT=?,OF_UMA_AMOUNT=?,OF_CH_AMOUNT=?,OF_CHO_1=?,OF_CHO_2=?,OF_CHO_AMOUNT=?,OF_JO_AMOUNT=?,OTHERS_KA_AMOUNT=?,OTHERS_KHA_AMOUNT=?,OTHERS_GA_AMOUNT=?,OTHERS_GHA_AMOUNT=?,TOTAL_IN_AMOUNT=? "+
				   " WHERE customer_id = ? and DEMAND_NOTE_ID=? " ;
		
		PreparedStatement stmt = null;
		
			try
			{
				
				//DebugLevel debug = DebugLevel.ON;
				//stmt = StatementFactory.getStatement(conn,sql,debug);
				
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1,"1");
				stmt.setString(2,"1");
				stmt.setString(3,"1");
				stmt.setString(4,dNote.getSd_ka_1());
				stmt.setString(5,dNote.getSd_ka_amount());
				stmt.setString(6,dNote.getSd_kha_1());
				stmt.setString(7,dNote.getSd_kha_2());
				stmt.setString(8,dNote.getSd_kha_amount());
				stmt.setString(9,dNote.getSd_ga_1());
				stmt.setString(10,dNote.getSd_ga_2());
				stmt.setString(11,dNote.getSd_ga_amount());
				stmt.setString(12,dNote.getBsd_ka_amount());
				stmt.setString(13,dNote.getBsd_kha_amount());
				stmt.setString(14,dNote.getCl_ka_amount());
				stmt.setString(15,dNote.getCl_kha_1());
				stmt.setString(16,dNote.getCl_kha_amount());				
				stmt.setString(17,dNote.getCl_ga_1());
				stmt.setString(18,dNote.getCl_ga_2());
				stmt.setString(19,dNote.getCl_ga_3());
				stmt.setString(20,dNote.getCl_ga_amount());				
				stmt.setString(21,dNote.getCl_gha_1());
				stmt.setString(22,dNote.getCl_gha_2());
				stmt.setString(23,dNote.getCl_gha_amount());				
				stmt.setString(24,dNote.getCl_uma_l1());
				stmt.setString(25,dNote.getCl_uma_l1_amount());
				stmt.setString(26,dNote.getCl_uma_l2());
				stmt.setString(27,dNote.getCl_uma_l2_amount());
				stmt.setString(28,dNote.getOf_ka_amount());
				stmt.setString(29,dNote.getOf_kha_amount());
				stmt.setString(30,dNote.getOf_ga_amount());
				stmt.setString(31,dNote.getOf_gha_amount());
				stmt.setString(32,dNote.getOf_uma_amount());
				
				stmt.setString(33,dNote.getOf_ch_amount());
				stmt.setString(34,dNote.getOf_cho_1());
				stmt.setString(35,dNote.getOf_cho_2());
				stmt.setString(36,dNote.getOf_cho_amount());
				stmt.setString(37,dNote.getOf_jo_amount());
				stmt.setString(38,dNote.getOthers_ka_amount());
				stmt.setString(39,dNote.getOthers_kha_amount());
				stmt.setString(40,dNote.getOthers_ga_amount());
				stmt.setString(41,dNote.getOthers_gha_amount());
				stmt.setFloat(42,dNote.getTotal_in_amount());
				stmt.setString(43,dNote.getCustomer_id());
				stmt.setString(44,dNote.getDemand_note_id());
//				System.out.println(" debuggable statement= " + stmt.toString());
				
				stmt.executeUpdate();
				response.setMessasge("Successfully Updated the Demand Note.");
				response.setResponse(true);

			} 
			catch (Exception e){e.printStackTrace();
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		return response;

	}
	
	public ArrayList<DemandNoteDTO> getDemandNoteList(String customer_id)
	{
		DemandNoteDTO demandNote=null;
		ArrayList<DemandNoteDTO> demandNoteList=new ArrayList<DemandNoteDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="Select DEMAND_NOTE.*,TO_CHAR(ISSUE_DATE,'DD-MM-YYYY') ISSUE_DATE_1 from DEMAND_NOTE Where Customer_Id=? order by DEMAND_NOTE_ID desc";
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, customer_id);
				r = stmt.executeQuery();
				while (r.next())
				{
					 demandNote=new DemandNoteDTO();
					 demandNote.setDemand_note_id(r.getString("DEMAND_NOTE_ID"));
					 demandNote.setCustomer_id(r.getString("CUSTOMER_ID"));
					 demandNote.setIssue_date(r.getString("ISSUE_DATE_1"));
					demandNoteList.add(demandNote);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return demandNoteList;
	}
	
	
	
	public DemandNoteDTO getDemandNote(String customer_id,String demand_note_id)
	{
		Connection conn = ConnectionManager.getConnection();
		String whereClause="";
		if(demand_note_id!=null&&!demand_note_id.equals(""))
		{
			 whereClause="And DEMAND_NOTE_ID="+demand_note_id;
		}
		
		
		
		String sql="Select CUSTOMER_ID,nvl(SD_KA_1,0)SD_KA_1,nvl(SD_KA_AMOUNT,0)SD_KA_AMOUNT,nvl(SD_KHA_1,0)SD_KHA_1,  " +
		"nvl(SD_KHA_2,0)SD_KHA_2,nvl(SD_KHA_AMOUNT,0)SD_KHA_AMOUNT,nvl(SD_GA_1,0)SD_GA_1,nvl(SD_GA_2,0)SD_GA_2,nvl(SD_GA_AMOUNT,0)SD_GA_AMOUNT,nvl(BSD_KA_AMOUNT,0)BSD_KA_AMOUNT,nvl(BSD_KHA_AMOUNT,0)BSD_KHA_AMOUNT,nvl(CL_KA_AMOUNT,0)CL_KA_AMOUNT,nvl(CL_KHA_1,0)CL_KHA_1,nvl(CL_KHA_AMOUNT,0)CL_KHA_AMOUNT,nvl(CL_GA_1,0)CL_GA_1,nvl(CL_GA_2,0)CL_GA_2,nvl(CL_GA_3,0)CL_GA_3,nvl(CL_GA_AMOUNT,0)CL_GA_AMOUNT,   " +
		" nvl(CL_GHA_1,0)CL_GHA_1,nvl(CL_GHA_2,0)CL_GHA_2,nvl(CL_GHA_AMOUNT,0)CL_GHA_AMOUNT,nvl(CL_UMA_L1,0)CL_UMA_L1,nvl(CL_UMA_L1_AMOUNT,0)CL_UMA_L1_AMOUNT,nvl(CL_UMA_L2,0)CL_UMA_L2,nvl(CL_UMA_L2_AMOUNT,0)CL_UMA_L2_AMOUNT,nvl(OF_KA_AMOUNT,0)OF_KA_AMOUNT,   " +
		" nvl(OF_KHA_AMOUNT,0)OF_KHA_AMOUNT,nvl(OF_GA_AMOUNT,0)OF_GA_AMOUNT,nvl(OF_GHA_AMOUNT,0)OF_GHA_AMOUNT,nvl(OF_UMA_AMOUNT,0)OF_UMA_AMOUNT,nvl(OF_CH_AMOUNT,0)OF_CH_AMOUNT,nvl(OF_CHO_1,0)OF_CHO_1,nvl(OF_CHO_2,0)OF_CHO_2,nvl(OF_CHO_AMOUNT,0)OF_CHO_AMOUNT,nvl(of_jo_amount,0)of_jo_amount,nvl(OTHERS_KA_AMOUNT,0)OTHERS_KA_AMOUNT,   " +
		" nvl(OTHERS_KHA_AMOUNT,0)OTHERS_KHA_AMOUNT,nvl(OTHERS_GA_AMOUNT,0)OTHERS_GA_AMOUNT,nvl(OTHERS_GHA_AMOUNT,0)OTHERS_GHA_AMOUNT,nvl(TOTAL_IN_AMOUNT,0)TOTAL_IN_AMOUNT,NUMBER_SPELLOUT_FUNC(TOTAL_IN_AMOUNT) TOTAL_IN_WORD,DEMAND_NOTE_ID  " +
		" from DEMAND_NOTE d  " +
		" Where Customer_Id=?  "+whereClause+
		"ORDER BY DEMAND_NOTE_ID DESC";

		
		
		
		PreparedStatement stmt = null;
		ResultSet r = null;
		DemandNoteDTO dNote=null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,customer_id);
				
				r = stmt.executeQuery();
				if (r.next()){
					dNote=new DemandNoteDTO();
					dNote.setCustomer_id(r.getString("CUSTOMER_ID"));
					dNote.setSd_ka_1(r.getString("SD_KA_1").equals("0")?"":r.getString("SD_KA_1"));
					dNote.setSd_ka_amount(r.getString("SD_KA_AMOUNT").equals("0")?"":r.getString("SD_KA_AMOUNT"));
					dNote.setSd_kha_1(r.getString("SD_KHA_1").equals("0")?"":r.getString("SD_KHA_1"));
					dNote.setSd_kha_2(r.getString("SD_KHA_2").equals("0")?"":r.getString("SD_KHA_2"));
					dNote.setSd_kha_amount(r.getString("SD_KHA_AMOUNT").equals("0")?"":r.getString("SD_KHA_AMOUNT"));
					dNote.setSd_ga_1(r.getString("SD_GA_1").equals("0")?"":r.getString("SD_GA_1"));
					dNote.setSd_ga_2(r.getString("SD_GA_2").equals("0")?"":r.getString("SD_GA_2"));
					dNote.setSd_ga_amount(r.getString("SD_GA_AMOUNT").equals("0")?"":r.getString("SD_GA_AMOUNT"));
					dNote.setBsd_ka_amount(r.getString("BSD_KA_AMOUNT").equals("0")?"":r.getString("BSD_KA_AMOUNT"));
					dNote.setBsd_kha_amount(r.getString("BSD_KHA_AMOUNT").equals("0")?"":r.getString("BSD_KHA_AMOUNT"));
					
					dNote.setCl_ka_amount(r.getString("CL_KA_AMOUNT").equals("0")?"":r.getString("CL_KA_AMOUNT"));
					dNote.setCl_kha_1(r.getString("CL_KHA_1").equals("0")?"":r.getString("CL_KHA_1"));
					dNote.setCl_kha_amount(r.getString("CL_KHA_AMOUNT").equals("0")?"":r.getString("CL_KHA_AMOUNT"));
					dNote.setCl_ga_1(r.getString("CL_GA_1").equals("0")?"":r.getString("CL_GA_1"));
					dNote.setCl_ga_2(r.getString("CL_GA_2").equals("0")?"":r.getString("CL_GA_2"));
					dNote.setCl_ga_3(r.getString("CL_GA_3").equals("0")?"":r.getString("CL_GA_3")); 
					dNote.setCl_ga_amount(r.getString("CL_GA_AMOUNT").equals("0")?"":r.getString("CL_GA_AMOUNT"));
					dNote.setCl_gha_1(r.getString("CL_GHA_1").equals("0")?"":r.getString("CL_GHA_1"));
					dNote.setCl_gha_2(r.getString("CL_GHA_2").equals("0")?"":r.getString("CL_GHA_2"));
					dNote.setCl_gha_amount(r.getString("CL_GHA_AMOUNT").equals("0")?"":r.getString("CL_GHA_AMOUNT"));
					dNote.setCl_uma_l1(r.getString("CL_UMA_L1").equals("0")?"":r.getString("CL_UMA_L1"));
					dNote.setCl_uma_l1_amount(r.getString("CL_UMA_L1_AMOUNT").equals("0")?"":r.getString("CL_UMA_L1_AMOUNT"));
					dNote.setCl_uma_l2(r.getString("CL_UMA_L2").equals("0")?"":r.getString("CL_UMA_L2"));
					dNote.setCl_uma_l2_amount(r.getString("CL_UMA_L2_AMOUNT").equals("0")?"":r.getString("CL_UMA_L2_AMOUNT"));
					dNote.setOf_ka_amount(r.getString("OF_KA_AMOUNT").equals("0")?"":r.getString("OF_KA_AMOUNT"));
					dNote.setOf_kha_amount(r.getString("OF_KHA_AMOUNT").equals("0")?"":r.getString("OF_KHA_AMOUNT"));
					dNote.setOf_ga_amount(r.getString("OF_GA_AMOUNT").equals("0")?"":r.getString("OF_GA_AMOUNT"));
					dNote.setOf_gha_amount(r.getString("OF_GHA_AMOUNT").equals("0")?"":r.getString("OF_GHA_AMOUNT"));
					dNote.setOf_uma_amount(r.getString("OF_UMA_AMOUNT").equals("0")?"":r.getString("OF_UMA_AMOUNT"));
					dNote.setOf_ch_amount(r.getString("OF_CH_AMOUNT").equals("0")?"":r.getString("OF_CH_AMOUNT"));
					dNote.setOf_cho_1(r.getString("OF_CHO_1").equals("0")?"":r.getString("OF_CHO_1"));
					dNote.setOf_cho_2(r.getString("OF_CHO_2").equals("0")?"":r.getString("OF_CHO_2"));
					dNote.setOf_cho_amount(r.getString("OF_CHO_AMOUNT").equals("0")?"":r.getString("OF_CHO_AMOUNT"));
					dNote.setOf_jo_amount(r.getString("OF_JO_AMOUNT").equals("0")?"":r.getString("OF_JO_AMOUNT"));
					dNote.setOthers_ka_amount(r.getString("OTHERS_KA_AMOUNT").equals("0")?"":r.getString("OTHERS_KA_AMOUNT"));
					dNote.setOthers_kha_amount(r.getString("OTHERS_KHA_AMOUNT").equals("0")?"":r.getString("OTHERS_KHA_AMOUNT"));
					dNote.setOthers_ga_amount(r.getString("OTHERS_GA_AMOUNT").equals("0")?"":r.getString("OTHERS_GA_AMOUNT"));
					dNote.setOthers_gha_amount(r.getString("OTHERS_GHA_AMOUNT").equals("0")?"":r.getString("OTHERS_GHA_AMOUNT"));
					dNote.setTotal_in_word(r.getString("TOTAL_IN_WORD"));
					dNote.setTotal_in_amount(r.getFloat("TOTAL_IN_AMOUNT"));
					dNote.setDemand_note_id(r.getString("DEMAND_NOTE_ID"));
					
					
				}
			} 
			catch (Exception e){e.printStackTrace();
			
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return dNote;

	}
	
}
