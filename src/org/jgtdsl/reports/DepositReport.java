package org.jgtdsl.reports;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.AccountDTO;
import org.jgtdsl.dto.BalanceSheetDTO;
import org.jgtdsl.dto.BankDTO;
import org.jgtdsl.dto.BranchDTO;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.DepositDTO;
import org.jgtdsl.dto.NonMeterReportDTO;
import org.jgtdsl.dto.SecutityDepositReportDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.DepositService;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class DepositReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	static ArrayList<SecutityDepositReportDTO> loadIncreaseReport=new ArrayList<SecutityDepositReportDTO>();
	SecutityDepositReportDTO loadIncraseDTO=new SecutityDepositReportDTO();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area;
	    private  String customer_category;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for;
	    private  String report_for2; 
	    private  String category_name;
	    private  String bank_id;
	    private  String branch_id;
	    private  String account_no;
	    private  String from_date;
	    private  String to_date;
	    private  String from_date_ind;
	    private  String to_date_ind;
	    private  String collection_month;
	    private  String collection_year;
	    private  String customer_id;
	    
	    AccountDTO accountInfo=new AccountDTO();
	    UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
	    DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	    DecimalFormat consumption_format = new DecimalFormat("##########0.000");
	    DecimalFormat factor_format=new DecimalFormat("##########0.000");
	    
	public String execute() throws Exception
	{
			
		String fileName="Deposit_Information.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL_LANDSCAPE.rotate());
		document.setMargins(5,5,60,72);
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		DecimalFormat factor_format=new DecimalFormat("##########0.000");
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			SecutityDepositReportDTO loadIncraseDTO = new SecutityDepositReportDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
		   
				
			headerTable.setWidths(new float[] {
				50,100,50
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			
			
			String realPath = servlet.getRealPath("/resources/images/logo/JG.png"); 	// image path
			Image img = Image.getInstance(realPath);
						
            	//img.scaleToFit(10f, 200f);
            	//img.scalePercent(200f);
            img.scaleAbsolute(28f, 31f);
            //img.setAbsolutePosition(170f, 790f);		
            img.setAbsolutePosition(340f, 520f);		// rotate
            
	        document.add(img);
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("REGIONAL OFFICE : ",ReportUtil.f8B);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f8B);
			Paragraph p = new Paragraph(); 
			p.add(chunk1);
			p.add(chunk2);
			pcell=new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
					
			pcell=new PdfPCell(mTable);
			pcell.setBorder(0);
			headerTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			document.add(headerTable);
			
			
			
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{30,50,30});
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);	
			headLinetable.addCell(pcell);
			
			if(report_for.equals("account_wise"))
			{
				getSecurityOtherDepositByAccount(document);
			}
			else if(report_for.equals("individual_wise"))
			{
				getSecurityOtherDepositByIndividual(document);
			}else
			{
				getSecurityOtherDepositByAreaCategory(document);
			}
			
			
						
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	
	private BalanceSheetDTO getCustomerDetails(){           
		BalanceSheetDTO customer_info = new BalanceSheetDTO();
		try {
		
			
			String account_info_sql="SELECT CI.CUSTOMER_ID,CI.CATEGORY_NAME,CI.FULL_NAME,CI.PROPRIETOR_NAME,CI.ADDRESS_LINE1,CI.CONNECTION_DATE,CM.METER_SL_NO,CM.METER_RENT  " +
									" FROM MVIEW_CUSTOMER_INFO CI,CUSTOMER_METER CM " +
									" WHERE CI.CUSTOMER_ID=CM.CUSTOMER_ID(+) " +
									" AND CI.CUSTOMER_ID= ? " ;



			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
				ps1.setString(1, customer_id);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		
        		
        		customer_info.setCustomerCategoryName(resultSet.getString("CATEGORY_NAME"));
        		customer_info.setCustomerID(resultSet.getString("CUSTOMER_ID"));
        		customer_info.setCustomerName(resultSet.getString("FULL_NAME"));
        		customer_info.setPropriateName(resultSet.getString("PROPRIETOR_NAME"));
        		customer_info.setAddress(resultSet.getString("ADDRESS_LINE1"));
        		customer_info.setMeterNo(resultSet.getString("METER_SL_NO"));
        		customer_info.setMeterRent(resultSet.getDouble("METER_RENT"));
        		customer_info.setConnectionDate(resultSet.getString("CONNECTION_DATE"));
        		
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customer_info;
	}

	private void getSecurityOtherDepositByIndividual(Document document) throws DocumentException
	{
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		String headLine="";
		
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{30,50,30});
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);	
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("SECURITY DEPOSIT INFORMATION FOR FROM "+from_date_ind+" TO "+to_date_ind,ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		getCustomerInfoTable(document);
		
		
		loadIncreaseReport =getSecurityOtherDepositInfoByIndividual();
		
		int totalRecordsPerCategory=0;
		int total_burner=0;
		float total_deposit=0;
		
		float subTotalSecurity_deposit=0;
		float subTotalSales_of_store=0;
		float subTotalConnection_fee=0;
		float subTotalCommissioning_fee=0;
		float subTotalService_fee=0;
		float subTotalPipeline_fee=0;
		float subTotalLoad_change_fee=0;
		float subTotalDisconnection_fee=0;
		float subTotalReconnection_fee=0;
		float subTotalAdditional_fee=0;
		float subTotalPenalty=0;
		float subTotalName_change_fee=0;
		float subTotalBurner_shifting_fee=0;
		float subTotalRaizer_shifting_fee=0;
		float subTotalConsulting_fee=0;
		float subTotalOther_fee=0;
		float subTotalTotalDeposit=0;
		
		float TotalSecurity_deposit=0;
		float TotalSales_of_store=0;
		float TotalConnection_fee=0;
		float TotalCommissioning_fee=0;
		float TotalService_fee=0;
		float TotalPipeline_fee=0;
		float TotalLoad_change_fee=0;
		float TotalDisconnection_fee=0;
		float TotalReconnection_fee=0;
		float TotalAdditional_fee=0;
		float TotalPenalty=0;
		float TotalName_change_fee=0;
		float TotalBurner_shifting_fee=0;
		float TotalRaizer_shifting_fee=0;
		float TotalConsulting_fee=0;
		float TotalOther_fee=0;
		float TotalTotalDeposit=0;
		
		

		int expireListSize=loadIncreaseReport.size();
		String previousCustomerCategoryName=new String("");
		
		for(int i=0;i<expireListSize;i++)
		{
			loadIncraseDTO = loadIncreaseReport.get(i);
			String currentCustomerCategoryName=loadIncraseDTO.getCategory_name();
			
			if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
			{	
			
			if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
			{
				
				if(i>0)
				{
					pcell=new PdfPCell(new Paragraph("Sub Totals:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(2);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurity_deposit),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSales_of_store),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConnection_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCommissioning_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalService_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPipeline_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalLoad_change_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDisconnection_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalReconnection_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalAdditional_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPenalty),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalName_change_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalBurner_shifting_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalRaizer_shifting_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConsulting_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalOther_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalTotalDeposit),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
				
					
					
					
												
	
					
					document.add(ptable);
				
					 subTotalSecurity_deposit=0;
					 subTotalSales_of_store=0;
					 subTotalConnection_fee=0;
					 subTotalCommissioning_fee=0;
					 subTotalService_fee=0;
					 subTotalPipeline_fee=0;
					 subTotalLoad_change_fee=0;
					 subTotalDisconnection_fee=0;
					 subTotalReconnection_fee=0;
					 subTotalAdditional_fee=0;
					 subTotalPenalty=0;
					 subTotalName_change_fee=0;
					 subTotalBurner_shifting_fee=0;
					 subTotalRaizer_shifting_fee=0;
					 subTotalConsulting_fee=0;
					 subTotalOther_fee=0;
					 subTotalTotalDeposit=0;
					
					totalRecordsPerCategory=0;
					total_burner=0;
				}
				
			}
			ptable = new PdfPTable(19);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{25,25,40,40,30,30,30,30,30,30,30,30,30,30,30,30,30,30,40});
			ptable.setSpacingBefore(10);
			
			
			pcell=new PdfPCell(new Paragraph(currentCustomerCategoryName,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(4);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(15);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
							
			
			pcell=new PdfPCell(new Paragraph("Dated",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setColspan(2);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Security Deposit",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Sales of Store",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Connection Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Comissioning Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Service Charge",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Load Inc./Desc. Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Pipeline Construction",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Disconnection Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Reconnection Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Additional Bill",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Penalty",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Name Change Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Burner Shifting Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Raizer Shifting Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Consulting Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Other Fees",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Deposit",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			}
			
			
			
		
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getDeposit_date(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalSecurity_deposit=TotalSecurity_deposit+loadIncraseDTO.getSecurity_deposit();
			subTotalSecurity_deposit=subTotalSecurity_deposit+loadIncraseDTO.getSecurity_deposit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getSecurity_deposit()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalSales_of_store=TotalSales_of_store+loadIncraseDTO.getSales_of_store();
			subTotalSales_of_store=subTotalSales_of_store+loadIncraseDTO.getSales_of_store();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getSales_of_store()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalConnection_fee=TotalConnection_fee+loadIncraseDTO.getConnection_fee();
			subTotalConnection_fee=subTotalConnection_fee+loadIncraseDTO.getConnection_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getConnection_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalCommissioning_fee=TotalCommissioning_fee+loadIncraseDTO.getCommissioning_fee();
			subTotalCommissioning_fee=subTotalCommissioning_fee+loadIncraseDTO.getCommissioning_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getCommissioning_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalService_fee=TotalService_fee+loadIncraseDTO.getService_fee();
			subTotalService_fee=subTotalService_fee+loadIncraseDTO.getService_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getService_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalPipeline_fee=TotalPipeline_fee+loadIncraseDTO.getPipeline_fee();
			subTotalPipeline_fee=subTotalPipeline_fee+loadIncraseDTO.getPipeline_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getPipeline_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalLoad_change_fee=TotalLoad_change_fee+loadIncraseDTO.getLoad_change_fee();
			subTotalLoad_change_fee=subTotalLoad_change_fee+loadIncraseDTO.getLoad_change_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getLoad_change_fee() ),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalDisconnection_fee=TotalDisconnection_fee+loadIncraseDTO.getDisconnection_fee();
			subTotalDisconnection_fee=subTotalDisconnection_fee+loadIncraseDTO.getDisconnection_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getDisconnection_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalReconnection_fee=TotalReconnection_fee+loadIncraseDTO.getReconnection_fee();
			subTotalReconnection_fee=subTotalReconnection_fee+loadIncraseDTO.getReconnection_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getReconnection_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalAdditional_fee=TotalAdditional_fee+loadIncraseDTO.getAdditional_fee();
			subTotalAdditional_fee=subTotalAdditional_fee+loadIncraseDTO.getAdditional_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getAdditional_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalPenalty=TotalPenalty+loadIncraseDTO.getPenalty();
			subTotalPenalty=subTotalPenalty+loadIncraseDTO.getPenalty();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getPenalty()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalName_change_fee=TotalName_change_fee+loadIncraseDTO.getName_change_fee();
			subTotalName_change_fee=subTotalName_change_fee+loadIncraseDTO.getName_change_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getName_change_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalBurner_shifting_fee=TotalBurner_shifting_fee+loadIncraseDTO.getBurner_shifting_fee();
			subTotalBurner_shifting_fee=subTotalBurner_shifting_fee+loadIncraseDTO.getBurner_shifting_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getBurner_shifting_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalRaizer_shifting_fee=TotalRaizer_shifting_fee+loadIncraseDTO.getRaizer_shifting_fee();
			subTotalRaizer_shifting_fee=subTotalRaizer_shifting_fee+loadIncraseDTO.getRaizer_shifting_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getRaizer_shifting_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalConsulting_fee=TotalConsulting_fee+loadIncraseDTO.getConsulting_fee();
			subTotalConsulting_fee=subTotalConsulting_fee+loadIncraseDTO.getConsulting_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getConsulting_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalOther_fee=TotalOther_fee+loadIncraseDTO.getOther_fee();
			subTotalOther_fee=subTotalOther_fee+loadIncraseDTO.getOther_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getOther_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			total_deposit=loadIncraseDTO.getSecurity_deposit()+loadIncraseDTO.getSales_of_store()+loadIncraseDTO.getConnection_fee()+loadIncraseDTO.getCommissioning_fee()+loadIncraseDTO.getService_fee()+loadIncraseDTO.getPipeline_fee()+loadIncraseDTO.getLoad_change_fee()+loadIncraseDTO.getDisconnection_fee()+loadIncraseDTO.getReconnection_fee()+loadIncraseDTO.getAdditional_fee()+loadIncraseDTO.getPenalty()+loadIncraseDTO.getName_change_fee()+loadIncraseDTO.getBurner_shifting_fee()+loadIncraseDTO.getRaizer_shifting_fee()+loadIncraseDTO.getConsulting_fee()+loadIncraseDTO.getOther_fee();
			TotalTotalDeposit=TotalTotalDeposit+total_deposit;
			subTotalTotalDeposit=subTotalTotalDeposit+total_deposit;
			pcell = new PdfPCell(new Paragraph(taka_format.format(total_deposit),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			
			
			
			
		
			previousCustomerCategoryName=loadIncraseDTO.getCategory_name();
			totalRecordsPerCategory++;
			
			
		}
		/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
		pcell=new PdfPCell(new Paragraph("Sub Totals:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurity_deposit),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSales_of_store),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConnection_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCommissioning_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalService_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPipeline_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalLoad_change_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDisconnection_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalReconnection_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalAdditional_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPenalty),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalName_change_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalBurner_shifting_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalRaizer_shifting_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConsulting_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalOther_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalTotalDeposit),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
	
		// grand Total starts here3
		pcell=new PdfPCell(new Paragraph("Grand Totals",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalSecurity_deposit),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalSales_of_store),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalConnection_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalCommissioning_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalService_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalPipeline_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalLoad_change_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalDisconnection_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalReconnection_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalAdditional_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalPenalty),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalName_change_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalBurner_shifting_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalRaizer_shifting_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalConsulting_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalOther_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalTotalDeposit),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		////
		
		
									

		
		document.add(ptable);
		/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
		
	}

	private void getSecurityOtherDepositByAreaCategory(Document document) throws DocumentException
	{
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		String headLine="";
		
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{30,50,30});
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);	
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("SECURITY DEPOSIT INFORMATION FOR MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year,ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		loadIncreaseReport =getLoadIncraseInformation();
		
		int totalRecordsPerCategory=0;
		int total_burner=0;
		float total_deposit=0;
		
		float subTotalSecurity_deposit=0;
		float subTotalSales_of_store=0;
		float subTotalConnection_fee=0;
		float subTotalCommissioning_fee=0;
		float subTotalService_fee=0;
		float subTotalPipeline_fee=0;
		float subTotalLoad_change_fee=0;
		float subTotalDisconnection_fee=0;
		float subTotalReconnection_fee=0;
		float subTotalAdditional_fee=0;
		float subTotalPenalty=0;
		float subTotalName_change_fee=0;
		float subTotalBurner_shifting_fee=0;
		float subTotalRaizer_shifting_fee=0;
		float subTotalConsulting_fee=0;
		float subTotalOther_fee=0;
		float subTotalTotalDeposit=0;
		
		float TotalSecurity_deposit=0;
		float TotalSales_of_store=0;
		float TotalConnection_fee=0;
		float TotalCommissioning_fee=0;
		float TotalService_fee=0;
		float TotalPipeline_fee=0;
		float TotalLoad_change_fee=0;
		float TotalDisconnection_fee=0;
		float TotalReconnection_fee=0;
		float TotalAdditional_fee=0;
		float TotalPenalty=0;
		float TotalName_change_fee=0;
		float TotalBurner_shifting_fee=0;
		float TotalRaizer_shifting_fee=0;
		float TotalConsulting_fee=0;
		float TotalOther_fee=0;
		float TotalTotalDeposit=0;
		
		

		int expireListSize=loadIncreaseReport.size();
		String previousCustomerCategoryName=new String("");
		
		for(int i=0;i<expireListSize;i++)
		{
			loadIncraseDTO = loadIncreaseReport.get(i);
			String currentCustomerCategoryName=loadIncraseDTO.getCategory_name();
			
			if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
			{	
			
			if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
			{
				
				if(i>0)
				{
					pcell=new PdfPCell(new Paragraph("Sub Totals:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(2);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurity_deposit),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSales_of_store),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConnection_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCommissioning_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalService_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPipeline_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalLoad_change_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDisconnection_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalReconnection_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalAdditional_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPenalty),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalName_change_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalBurner_shifting_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalRaizer_shifting_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConsulting_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalOther_fee),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalTotalDeposit),ReportUtil.f9B));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
					
				
					
					
					
												
	
					
					document.add(ptable);
				
					 subTotalSecurity_deposit=0;
					 subTotalSales_of_store=0;
					 subTotalConnection_fee=0;
					 subTotalCommissioning_fee=0;
					 subTotalService_fee=0;
					 subTotalPipeline_fee=0;
					 subTotalLoad_change_fee=0;
					 subTotalDisconnection_fee=0;
					 subTotalReconnection_fee=0;
					 subTotalAdditional_fee=0;
					 subTotalPenalty=0;
					 subTotalName_change_fee=0;
					 subTotalBurner_shifting_fee=0;
					 subTotalRaizer_shifting_fee=0;
					 subTotalConsulting_fee=0;
					 subTotalOther_fee=0;
					 subTotalTotalDeposit=0;
					
					totalRecordsPerCategory=0;
					total_burner=0;
				}
				
			}
			ptable = new PdfPTable(19);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{25,25,40,40,30,30,30,30,30,30,30,30,30,30,30,30,30,30,40});
			ptable.setSpacingBefore(10);
			
			
			pcell=new PdfPCell(new Paragraph(currentCustomerCategoryName,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(4);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(15);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
							
			
			pcell=new PdfPCell(new Paragraph("Dated",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setColspan(2);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Security Deposit",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Sales of Store",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Connection Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Comissioning Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Service Charge",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Load Inc./Desc. Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Pipeline Construction",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Disconnection Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Reconnection Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Additional Bill",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Penalty",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Name Change Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Burner Shifting Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Raizer Shifting Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Consulting Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Other Fees",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Deposit",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			}
			
			
			
		
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getDeposit_date(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalSecurity_deposit=TotalSecurity_deposit+loadIncraseDTO.getSecurity_deposit();
			subTotalSecurity_deposit=subTotalSecurity_deposit+loadIncraseDTO.getSecurity_deposit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getSecurity_deposit()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalSales_of_store=TotalSales_of_store+loadIncraseDTO.getSales_of_store();
			subTotalSales_of_store=subTotalSales_of_store+loadIncraseDTO.getSales_of_store();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getSales_of_store()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalConnection_fee=TotalConnection_fee+loadIncraseDTO.getConnection_fee();
			subTotalConnection_fee=subTotalConnection_fee+loadIncraseDTO.getConnection_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getConnection_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalCommissioning_fee=TotalCommissioning_fee+loadIncraseDTO.getCommissioning_fee();
			subTotalCommissioning_fee=subTotalCommissioning_fee+loadIncraseDTO.getCommissioning_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getCommissioning_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalService_fee=TotalService_fee+loadIncraseDTO.getService_fee();
			subTotalService_fee=subTotalService_fee+loadIncraseDTO.getService_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getService_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalPipeline_fee=TotalPipeline_fee+loadIncraseDTO.getPipeline_fee();
			subTotalPipeline_fee=subTotalPipeline_fee+loadIncraseDTO.getPipeline_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getPipeline_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalLoad_change_fee=TotalLoad_change_fee+loadIncraseDTO.getLoad_change_fee();
			subTotalLoad_change_fee=subTotalLoad_change_fee+loadIncraseDTO.getLoad_change_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getLoad_change_fee() ),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalDisconnection_fee=TotalDisconnection_fee+loadIncraseDTO.getDisconnection_fee();
			subTotalDisconnection_fee=subTotalDisconnection_fee+loadIncraseDTO.getDisconnection_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getDisconnection_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalReconnection_fee=TotalReconnection_fee+loadIncraseDTO.getReconnection_fee();
			subTotalReconnection_fee=subTotalReconnection_fee+loadIncraseDTO.getReconnection_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getReconnection_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalAdditional_fee=TotalAdditional_fee+loadIncraseDTO.getAdditional_fee();
			subTotalAdditional_fee=subTotalAdditional_fee+loadIncraseDTO.getAdditional_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getAdditional_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalPenalty=TotalPenalty+loadIncraseDTO.getPenalty();
			subTotalPenalty=subTotalPenalty+loadIncraseDTO.getPenalty();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getPenalty()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalName_change_fee=TotalName_change_fee+loadIncraseDTO.getName_change_fee();
			subTotalName_change_fee=subTotalName_change_fee+loadIncraseDTO.getName_change_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getName_change_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalBurner_shifting_fee=TotalBurner_shifting_fee+loadIncraseDTO.getBurner_shifting_fee();
			subTotalBurner_shifting_fee=subTotalBurner_shifting_fee+loadIncraseDTO.getBurner_shifting_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getBurner_shifting_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalRaizer_shifting_fee=TotalRaizer_shifting_fee+loadIncraseDTO.getRaizer_shifting_fee();
			subTotalRaizer_shifting_fee=subTotalRaizer_shifting_fee+loadIncraseDTO.getRaizer_shifting_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getRaizer_shifting_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalConsulting_fee=TotalConsulting_fee+loadIncraseDTO.getConsulting_fee();
			subTotalConsulting_fee=subTotalConsulting_fee+loadIncraseDTO.getConsulting_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getConsulting_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalOther_fee=TotalOther_fee+loadIncraseDTO.getOther_fee();
			subTotalOther_fee=subTotalOther_fee+loadIncraseDTO.getOther_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getOther_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			total_deposit=loadIncraseDTO.getSecurity_deposit()+loadIncraseDTO.getSales_of_store()+loadIncraseDTO.getConnection_fee()+loadIncraseDTO.getCommissioning_fee()+loadIncraseDTO.getService_fee()+loadIncraseDTO.getPipeline_fee()+loadIncraseDTO.getLoad_change_fee()+loadIncraseDTO.getDisconnection_fee()+loadIncraseDTO.getReconnection_fee()+loadIncraseDTO.getAdditional_fee()+loadIncraseDTO.getPenalty()+loadIncraseDTO.getName_change_fee()+loadIncraseDTO.getBurner_shifting_fee()+loadIncraseDTO.getRaizer_shifting_fee()+loadIncraseDTO.getConsulting_fee()+loadIncraseDTO.getOther_fee();
			TotalTotalDeposit=TotalTotalDeposit+total_deposit;
			subTotalTotalDeposit=subTotalTotalDeposit+total_deposit;
			pcell = new PdfPCell(new Paragraph(taka_format.format(total_deposit),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			
			
			
			
		
			previousCustomerCategoryName=loadIncraseDTO.getCategory_name();
			totalRecordsPerCategory++;
			
			
		}
		/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
		pcell=new PdfPCell(new Paragraph("Sub Totals:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		//pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurity_deposit),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSales_of_store),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConnection_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCommissioning_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalService_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPipeline_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalLoad_change_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDisconnection_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalReconnection_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalAdditional_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPenalty),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalName_change_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalBurner_shifting_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalRaizer_shifting_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConsulting_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalOther_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalTotalDeposit),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
	
		// grand Total starts here3
		pcell=new PdfPCell(new Paragraph("Grand Totals",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		//pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalSecurity_deposit),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalSales_of_store),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalConnection_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalCommissioning_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalService_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalPipeline_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalLoad_change_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalDisconnection_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalReconnection_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalAdditional_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalPenalty),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalName_change_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalBurner_shifting_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalRaizer_shifting_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalConsulting_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalOther_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalTotalDeposit),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		////
		
		
									

		
		document.add(ptable);
		/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
		
	}
	private ArrayList<SecutityDepositReportDTO> getLoadIncraseInformation()
	{
	ArrayList<SecutityDepositReportDTO> depositInfoList=new ArrayList<SecutityDepositReportDTO>();
		
		try {
			String wClause="";
			if(report_for.equals("area_wise"))
			{
				wClause="And substr(md.customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause="And substr(md.customer_id,1,2)="+area+" And substr(md.customer_id,3,2)="+customer_category;
			}
			
			
			String defaulterSql="select MCC.CATEGORY_NAME, to_char(DEPOSIT_DATE,'dd-MON-YYYY') DEPOSIT_DATE,  " +
										"sum(nvl(decode(dd.TYPE_ID,'01',dd.AMOUNT),0)) Security, " +
										"sum(nvl(decode(dd.TYPE_ID,'02',dd.AMOUNT),0)) Sales, " +
										"sum(nvl(decode(dd.TYPE_ID,'03',dd.AMOUNT),0)) Connection, " +
										"sum(nvl(decode(dd.TYPE_ID,'04',dd.AMOUNT),0)) Comissioning, " +
										"sum(nvl(decode(dd.TYPE_ID,'05',dd.AMOUNT),0)) Service, " +
										"sum(nvl(decode(dd.TYPE_ID,'06',dd.AMOUNT),0)) Pipeline, " +
										"sum(nvl(decode(dd.TYPE_ID,'07',dd.AMOUNT),0)) Load, " +
										"sum(nvl(decode(dd.TYPE_ID,'08',dd.AMOUNT),0)) Disconnection, " +
										"sum(nvl(decode(dd.TYPE_ID,'09',dd.AMOUNT),0)) Reconnection, " +
										"sum(nvl(decode(dd.TYPE_ID,'10',dd.AMOUNT),0)) Additional, " +
										"sum(nvl(decode(dd.TYPE_ID,'11',dd.AMOUNT),0)) Penalty, " +
										"sum(nvl(decode(dd.TYPE_ID,'12',dd.AMOUNT),0)) NameChange, " +
										"sum(nvl(decode(dd.TYPE_ID,'13',dd.AMOUNT),0)) BurnerShifting, " +
										"sum(nvl(decode(dd.TYPE_ID,'14',dd.AMOUNT),0)) RaizerShifting, " +
										"sum(nvl(decode(dd.TYPE_ID,'15',dd.AMOUNT),0)) ConsultingFee, " +
										"sum(nvl(decode(dd.TYPE_ID,'16',dd.AMOUNT),0)) OtherFees " +
										"from MST_DEPOSIT md,DTL_DEPOSIT dd, MST_CUSTOMER_CATEGORY mcc " +
										"where MD.DEPOSIT_ID=DD.DEPOSIT_ID " +
										"and substr(md.CUSTOMER_ID,3,2)=MCC.CATEGORY_ID " +
										"and to_char(md.deposit_date,'MM')=" +bill_month+
										"and to_char(md.deposit_date,'YYYY')=" +bill_year+
										" "+wClause+" group by MCC.CATEGORY_NAME,MCC.CATEGORY_ID,DEPOSIT_DATE " +
										"order by MCC.CATEGORY_ID,DEPOSIT_DATE " ;




			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		SecutityDepositReportDTO depositInfoDto=new SecutityDepositReportDTO();
        		depositInfoDto.setCategory_name(resultSet.getString("CATEGORY_NAME"));
        		depositInfoDto.setDeposit_date(resultSet.getString("DEPOSIT_DATE"));
        		depositInfoDto.setSecurity_deposit(resultSet.getFloat("Security"));
        		depositInfoDto.setSales_of_store(resultSet.getFloat("Sales"));
        		depositInfoDto.setConnection_fee(resultSet.getFloat("Connection"));
        		depositInfoDto.setCommissioning_fee(resultSet.getFloat("Comissioning"));
        		depositInfoDto.setService_fee(resultSet.getFloat("Service"));
        		depositInfoDto.setLoad_change_fee(resultSet.getFloat("Pipeline"));
        		depositInfoDto.setPipeline_fee(resultSet.getFloat("Load"));
        		depositInfoDto.setDisconnection_fee(resultSet.getFloat("Disconnection"));
        		depositInfoDto.setReconnection_fee(resultSet.getFloat("Reconnection"));
        		depositInfoDto.setAdditional_fee(resultSet.getFloat("Additional"));
        		depositInfoDto.setPenalty(resultSet.getFloat("Penalty"));
        		depositInfoDto.setName_change_fee(resultSet.getFloat("NameChange"));
        		depositInfoDto.setBurner_shifting_fee(resultSet.getFloat("BurnerShifting"));
        		depositInfoDto.setRaizer_shifting_fee(resultSet.getFloat("RaizerShifting"));
        		depositInfoDto.setConsulting_fee(resultSet.getFloat("ConsultingFee"));
        		depositInfoDto.setOther_fee(resultSet.getFloat("OtherFees"));
        		
        		
        		
        		
        		
   
        		
        		depositInfoList.add(depositInfoDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return depositInfoList;
	}
	private void getSecurityOtherDepositByAccount(Document document) throws DocumentException
	{

		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		String headLine="";
		
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{30,80,30});
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);	
		headLinetable.addCell(pcell);
		
		if(report_for2.equals("date_wise"))
		{
			headLine="SECURITY DEPOSIT INFORMATION FROM DATE "+from_date+" TO DATE "+to_date;
		}else if(report_for2.equals("month_wise"))
		{
			headLine="SECURITY DEPOSIT INFORMATION FOR MONTH OF "+Month.values()[Integer.valueOf(collection_month)-1]+"'"+collection_year;
		}else if(report_for2.equals("year_wise"))
		{
			headLine="SECURITY DEPOSIT INFORMATION FOR YEAR OF "+bill_year;
		}
		
		
		pcell=new PdfPCell(new Paragraph(headLine,ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		getAccountInfoTable(document);
		
	
		
		loadIncreaseReport =getSecurityOtherDepositInfoByAccount();
		
		int totalRecordsPerCategory=0;
		int total_burner=0;
		float total_deposit=0;
		
		float subTotalSecurity_deposit=0;
		float subTotalSales_of_store=0;
		float subTotalConnection_fee=0;
		float subTotalCommissioning_fee=0;
		float subTotalService_fee=0;
		float subTotalPipeline_fee=0;
		float subTotalLoad_change_fee=0;
		float subTotalDisconnection_fee=0;
		float subTotalReconnection_fee=0;
		float subTotalAdditional_fee=0;
		float subTotalPenalty=0;
		float subTotalName_change_fee=0;
		float subTotalBurner_shifting_fee=0;
		float subTotalRaizer_shifting_fee=0;
		float subTotalConsulting_fee=0;
		float subTotalOther_fee=0;
		float subTotalTotalDeposit=0;
		
		float TotalSecurity_deposit=0;
		float TotalSales_of_store=0;
		float TotalConnection_fee=0;
		float TotalCommissioning_fee=0;
		float TotalService_fee=0;
		float TotalPipeline_fee=0;
		float TotalLoad_change_fee=0;
		float TotalDisconnection_fee=0;
		float TotalReconnection_fee=0;
		float TotalAdditional_fee=0;
		float TotalPenalty=0;
		float TotalName_change_fee=0;
		float TotalBurner_shifting_fee=0;
		float TotalRaizer_shifting_fee=0;
		float TotalConsulting_fee=0;
		float TotalOther_fee=0;
		float TotalTotalDeposit=0;
		
		

		int expireListSize=loadIncreaseReport.size();
		String previousCustomerCategoryName=new String("");
		String previousDate=new String("");
		
		for(int i=0;i<expireListSize;i++)
		{
			loadIncraseDTO = loadIncreaseReport.get(i);
			String currentCustomerCategoryName=loadIncraseDTO.getCategory_name();
			String currentDate=loadIncraseDTO.getDeposit_date();
	
			if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
			{	
			
				if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
				{
				
					if(i>0)
					{
						pcell=new PdfPCell(new Paragraph("Sub Totals:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(2);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurity_deposit),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSales_of_store),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConnection_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCommissioning_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalService_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPipeline_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalLoad_change_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDisconnection_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalReconnection_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalAdditional_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPenalty),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalName_change_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalBurner_shifting_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalRaizer_shifting_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConsulting_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalOther_fee),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalTotalDeposit),ReportUtil.f9B));
						pcell.setMinimumHeight(16f);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);
						
					
						
						
						
													
		
						
						document.add(ptable);
					
						 subTotalSecurity_deposit=0;
						 subTotalSales_of_store=0;
						 subTotalConnection_fee=0;
						 subTotalCommissioning_fee=0;
						 subTotalService_fee=0;
						 subTotalPipeline_fee=0;
						 subTotalLoad_change_fee=0;
						 subTotalDisconnection_fee=0;
						 subTotalReconnection_fee=0;
						 subTotalAdditional_fee=0;
						 subTotalPenalty=0;
						 subTotalName_change_fee=0;
						 subTotalBurner_shifting_fee=0;
						 subTotalRaizer_shifting_fee=0;
						 subTotalConsulting_fee=0;
						 subTotalOther_fee=0;
						 subTotalTotalDeposit=0;
						
						totalRecordsPerCategory=0;
						total_burner=0;
					}
				
				}
				
			ptable = new PdfPTable(19);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{25,25,40,40,30,30,30,30,30,30,30,30,30,30,30,30,30,30,40});
			ptable.setSpacingBefore(10);
			
			
			pcell=new PdfPCell(new Paragraph(currentCustomerCategoryName,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(4);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(15);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
							
			
			pcell=new PdfPCell(new Paragraph("Dated",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);	
			pcell.setColspan(2);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Security Deposit",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Sales of Store",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Connection Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Comissioning Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Service Charge",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Pipeline Construction",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Load Inc./Desc. Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Disconnection Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Reconnection Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Additional Bill",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Penalty",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Name Change Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Burner Shifting Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Raizer Shifting Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Consulting Fee",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Other Fees",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Deposit",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
		}
			
			if(!currentDate.equals(previousDate))
			{
				pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getDeposit_date(),ReportUtil.f9B));
				pcell.setMinimumHeight(16f);
				pcell.setColspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(""));
				pcell.setMinimumHeight(16f);
				pcell.setColspan(17);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
			}
			
			
			
		
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getCustomer_id(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalSecurity_deposit=TotalSecurity_deposit+loadIncraseDTO.getSecurity_deposit();
			subTotalSecurity_deposit=subTotalSecurity_deposit+loadIncraseDTO.getSecurity_deposit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getSecurity_deposit()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalSales_of_store=TotalSales_of_store+loadIncraseDTO.getSales_of_store();
			subTotalSales_of_store=subTotalSales_of_store+loadIncraseDTO.getSales_of_store();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getSales_of_store()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalConnection_fee=TotalConnection_fee+loadIncraseDTO.getConnection_fee();
			subTotalConnection_fee=subTotalConnection_fee+loadIncraseDTO.getConnection_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getConnection_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalCommissioning_fee=TotalCommissioning_fee+loadIncraseDTO.getCommissioning_fee();
			subTotalCommissioning_fee=subTotalCommissioning_fee+loadIncraseDTO.getCommissioning_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getCommissioning_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalService_fee=TotalService_fee+loadIncraseDTO.getService_fee();
			subTotalService_fee=subTotalService_fee+loadIncraseDTO.getService_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getService_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalPipeline_fee=TotalPipeline_fee+loadIncraseDTO.getPipeline_fee();
			subTotalPipeline_fee=subTotalPipeline_fee+loadIncraseDTO.getPipeline_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getPipeline_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalLoad_change_fee=TotalLoad_change_fee+loadIncraseDTO.getLoad_change_fee();
			subTotalLoad_change_fee=subTotalLoad_change_fee+loadIncraseDTO.getLoad_change_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getLoad_change_fee() ),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalDisconnection_fee=TotalDisconnection_fee+loadIncraseDTO.getDisconnection_fee();
			subTotalDisconnection_fee=subTotalDisconnection_fee+loadIncraseDTO.getDisconnection_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getDisconnection_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalReconnection_fee=TotalReconnection_fee+loadIncraseDTO.getReconnection_fee();
			subTotalReconnection_fee=subTotalReconnection_fee+loadIncraseDTO.getReconnection_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getReconnection_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalAdditional_fee=TotalAdditional_fee+loadIncraseDTO.getAdditional_fee();
			subTotalAdditional_fee=subTotalAdditional_fee+loadIncraseDTO.getAdditional_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getAdditional_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalPenalty=TotalPenalty+loadIncraseDTO.getPenalty();
			subTotalPenalty=subTotalPenalty+loadIncraseDTO.getPenalty();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getPenalty()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			TotalName_change_fee=TotalName_change_fee+loadIncraseDTO.getName_change_fee();
			subTotalName_change_fee=subTotalName_change_fee+loadIncraseDTO.getName_change_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getName_change_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalBurner_shifting_fee=TotalBurner_shifting_fee+loadIncraseDTO.getBurner_shifting_fee();
			subTotalBurner_shifting_fee=subTotalBurner_shifting_fee+loadIncraseDTO.getBurner_shifting_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getBurner_shifting_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalRaizer_shifting_fee=TotalRaizer_shifting_fee+loadIncraseDTO.getRaizer_shifting_fee();
			subTotalRaizer_shifting_fee=subTotalRaizer_shifting_fee+loadIncraseDTO.getRaizer_shifting_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getRaizer_shifting_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalConsulting_fee=TotalConsulting_fee+loadIncraseDTO.getConsulting_fee();
			subTotalConsulting_fee=subTotalConsulting_fee+loadIncraseDTO.getConsulting_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getConsulting_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			TotalOther_fee=TotalOther_fee+loadIncraseDTO.getOther_fee();
			subTotalOther_fee=subTotalOther_fee+loadIncraseDTO.getOther_fee();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getOther_fee()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			total_deposit=loadIncraseDTO.getSecurity_deposit()+loadIncraseDTO.getSales_of_store()+loadIncraseDTO.getConnection_fee()+loadIncraseDTO.getCommissioning_fee()+loadIncraseDTO.getService_fee()+loadIncraseDTO.getPipeline_fee()+loadIncraseDTO.getLoad_change_fee()+loadIncraseDTO.getDisconnection_fee()+loadIncraseDTO.getReconnection_fee()+loadIncraseDTO.getAdditional_fee()+loadIncraseDTO.getPenalty()+loadIncraseDTO.getName_change_fee()+loadIncraseDTO.getBurner_shifting_fee()+loadIncraseDTO.getRaizer_shifting_fee()+loadIncraseDTO.getConsulting_fee()+loadIncraseDTO.getOther_fee();
			TotalTotalDeposit=TotalTotalDeposit+total_deposit;
			subTotalTotalDeposit=subTotalTotalDeposit+total_deposit;
			pcell = new PdfPCell(new Paragraph(taka_format.format(total_deposit),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			
			
			
			
			previousDate=loadIncraseDTO.getDeposit_date();
			previousCustomerCategoryName=loadIncraseDTO.getCategory_name();
			totalRecordsPerCategory++;
			
			
		}
		/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
		pcell=new PdfPCell(new Paragraph("Sub Totals:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurity_deposit),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSales_of_store),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConnection_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCommissioning_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalService_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPipeline_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalLoad_change_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDisconnection_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalReconnection_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalAdditional_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalPenalty),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalName_change_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalBurner_shifting_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalRaizer_shifting_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalConsulting_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalOther_fee),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalTotalDeposit),ReportUtil.f9B));
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
	
		// grand Total starts here3
		pcell=new PdfPCell(new Paragraph("Grand Totals",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalSecurity_deposit),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalSales_of_store),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalConnection_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalCommissioning_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalService_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalPipeline_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalLoad_change_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalDisconnection_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalReconnection_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalAdditional_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalPenalty),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalName_change_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalBurner_shifting_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalRaizer_shifting_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalConsulting_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalOther_fee),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(TotalTotalDeposit),ReportUtil.f11B));
		pcell.setBorder(2);
		pcell.setMinimumHeight(16f);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ptable.addCell(pcell);
		////
		
		
									

		
		document.add(ptable);
		/*[[[[[[[[[End--->For Last row]]]]]]]]]*/

	}
	
	private void getCustomerInfoTable(Document document) throws DocumentException
	{
		BalanceSheetDTO customer_info = new BalanceSheetDTO();
		customer_info=getCustomerDetails();
		
		PdfPTable middleTable = new PdfPTable(4);
		middleTable.setWidthPercentage(100);
		middleTable.setWidths(new float[]{14,50,18,18});
		PdfPCell pcell ;
		
		pcell = new PdfPCell(new Paragraph("Category",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customer_info.getCustomerCategoryName(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer ID",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customer_info.getCustomerID(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customer_info.getCustomerName(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Pro Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customer_info.getPropriateName() ,ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Address",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(":"+customer_info.getAddress(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Connection Date",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customer_info.getConnectionDate(),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		document.add(middleTable);
	}
	
	private void getAccountInfoTable(Document document) throws DocumentException
	{
        accountInfo=getAccountInfo();
		
		PdfPTable middleTable = new PdfPTable(4);
		middleTable.setWidthPercentage(100);
		middleTable.setWidths(new float[]{14,50,18,18});
		PdfPCell pcell ;
		
		pcell = new PdfPCell(new Paragraph("Bank Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBank_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Branch Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank Address",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getAddress(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Phone",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getPhone(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account No ",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(":"+accountInfo.getAccount_no(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getAccount_name(),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		document.add(middleTable);
	}
	private ArrayList<SecutityDepositReportDTO> getSecurityOtherDepositInfoByIndividual()
	{
	ArrayList<SecutityDepositReportDTO> depositInfoList=new ArrayList<SecutityDepositReportDTO>();
		
		try {
			String wClause="";
			String w2Clause="";

			
			
		 wClause=" And md.DEPOSIT_DATE BETWEEN TO_DATE ('"+from_date_ind+"', 'dd-MM-YYYY') AND TO_DATE ('"+to_date_ind+"', 'dd-MM-YYYY')";
		

			
			
			String defaulterSql=
			"  SELECT MCI.CATEGORY_NAME, " +
			"         CATEGORY_ID, " +
			"         TO_CHAR (DEPOSIT_DATE, 'dd-MON-YYYY') DEPOSIT_DATE1, " +
			"         md.CUSTOMER_ID, " +
			"         DEPOSIT_DATE, " +
			"         BANK_ID, " +
			"         BRANCH_ID, " +
			"         ACCOUNT_NO, " +
			"         NVL (DECODE (dd.TYPE_ID, '01', dd.AMOUNT), 0) Security, " +
			"         NVL (DECODE (dd.TYPE_ID, '02', dd.AMOUNT), 0) Sales, " +
			"         NVL (DECODE (dd.TYPE_ID, '03', dd.AMOUNT), 0) Connection, " +
			"         NVL (DECODE (dd.TYPE_ID, '04', dd.AMOUNT), 0) Comissioning, " +
			"         NVL (DECODE (dd.TYPE_ID, '05', dd.AMOUNT), 0) Service, " +
			"         NVL (DECODE (dd.TYPE_ID, '06', dd.AMOUNT), 0) Pipeline, " +
			"         NVL (DECODE (dd.TYPE_ID, '07', dd.AMOUNT), 0) Load, " +
			"         NVL (DECODE (dd.TYPE_ID, '08', dd.AMOUNT), 0) Disconnection, " +
			"         NVL (DECODE (dd.TYPE_ID, '09', dd.AMOUNT), 0) Reconnection, " +
			"         NVL (DECODE (dd.TYPE_ID, '10', dd.AMOUNT), 0) Additional, " +
			"         NVL (DECODE (dd.TYPE_ID, '11', dd.AMOUNT), 0) Penalty, " +
			"         NVL (DECODE (dd.TYPE_ID, '12', dd.AMOUNT), 0) NameChange, " +
			"         NVL (DECODE (dd.TYPE_ID, '13', dd.AMOUNT), 0) BurnerShifting, " +
			"         NVL (DECODE (dd.TYPE_ID, '14', dd.AMOUNT), 0) RaizerShifting, " +
			"         NVL (DECODE (dd.TYPE_ID, '15', dd.AMOUNT), 0) ConsultingFee, " +
			"         NVL (DECODE (dd.TYPE_ID, '16', dd.AMOUNT), 0) OtherFees " +
			"    FROM MST_DEPOSIT md, DTL_DEPOSIT dd, MVIEW_CUSTOMER_INFO mci " +
			"   WHERE     MD.DEPOSIT_ID = DD.DEPOSIT_ID " +
			"         AND md.CUSTOMER_ID=mci.CUSTOMER_ID "+wClause+" AND md.CUSTOMER_ID='"+customer_id+"'  order by DEPOSIT_DATE ";



			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		SecutityDepositReportDTO depositInfoDto=new SecutityDepositReportDTO();
        		depositInfoDto.setCategory_name(resultSet.getString("CATEGORY_NAME"));
        		depositInfoDto.setDeposit_date(resultSet.getString("DEPOSIT_DATE1"));
        		depositInfoDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		depositInfoDto.setSecurity_deposit(resultSet.getFloat("Security"));
        		depositInfoDto.setSales_of_store(resultSet.getFloat("Sales"));
        		depositInfoDto.setConnection_fee(resultSet.getFloat("Connection"));
        		depositInfoDto.setCommissioning_fee(resultSet.getFloat("Comissioning"));
        		depositInfoDto.setService_fee(resultSet.getFloat("Service"));
        		depositInfoDto.setLoad_change_fee(resultSet.getFloat("Pipeline"));
        		depositInfoDto.setPipeline_fee(resultSet.getFloat("Load"));
        		depositInfoDto.setDisconnection_fee(resultSet.getFloat("Disconnection"));
        		depositInfoDto.setReconnection_fee(resultSet.getFloat("Reconnection"));
        		depositInfoDto.setAdditional_fee(resultSet.getFloat("Additional"));
        		depositInfoDto.setPenalty(resultSet.getFloat("Penalty"));
        		depositInfoDto.setName_change_fee(resultSet.getFloat("NameChange"));
        		depositInfoDto.setBurner_shifting_fee(resultSet.getFloat("BurnerShifting"));
        		depositInfoDto.setRaizer_shifting_fee(resultSet.getFloat("RaizerShifting"));
        		depositInfoDto.setConsulting_fee(resultSet.getFloat("ConsultingFee"));
        		depositInfoDto.setOther_fee(resultSet.getFloat("OtherFees"));        		
        		depositInfoList.add(depositInfoDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return depositInfoList;
	}
	private ArrayList<SecutityDepositReportDTO> getSecurityOtherDepositInfoByAccount()
	{
	ArrayList<SecutityDepositReportDTO> depositInfoList=new ArrayList<SecutityDepositReportDTO>();
		
		try {
			String wClause="";
			String w2Clause="";

			
			if(report_for2.equals("date_wise"))
			{
				wClause=" And md.DEPOSIT_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-MM-YYYY') AND TO_DATE ('"+to_date+"', 'dd-MM-YYYY')";
			}else if(report_for2.equals("month_wise"))
			{
				wClause=" And to_char(md.DEPOSIT_DATE,'mm')="+collection_month+" and to_char(md.DEPOSIT_DATE,'YYYY')="+collection_year;
			}else if(report_for2.equals("year_wise"))
			{
				wClause=" And to_char(md.DEPOSIT_DATE,'YYYY')="+collection_year;
			}
			
			
	/*		
			String defaulterSql="select MCC.CATEGORY_NAME, to_char(DEPOSIT_DATE,'dd-MON-YYYY') DEPOSIT_DATE,CUSTOMER_ID,DEPOSIT_DATE,BANK_ID,BRANCH_ID,ACCOUNT_NO,  " +
										"nvl(decode(dd.TYPE_ID,'01',dd.AMOUNT),0) Security, " +
										"nvl(decode(dd.TYPE_ID,'02',dd.AMOUNT),0) Sales, " +
										"nvl(decode(dd.TYPE_ID,'03',dd.AMOUNT),0) Connection, " +
										"nvl(decode(dd.TYPE_ID,'04',dd.AMOUNT),0) Comissioning, " +
										"nvl(decode(dd.TYPE_ID,'05',dd.AMOUNT),0) Service, " +
										"nvl(decode(dd.TYPE_ID,'06',dd.AMOUNT),0) Pipeline, " +
										"nvl(decode(dd.TYPE_ID,'07',dd.AMOUNT),0) Load, " +
										"nvl(decode(dd.TYPE_ID,'08',dd.AMOUNT),0) Disconnection, " +
										"nvl(decode(dd.TYPE_ID,'09',dd.AMOUNT),0) Reconnection, " +
										"nvl(decode(dd.TYPE_ID,'10',dd.AMOUNT),0) Additional, " +
										"nvl(decode(dd.TYPE_ID,'11',dd.AMOUNT),0) Penalty, " +
										"nvl(decode(dd.TYPE_ID,'12',dd.AMOUNT),0) NameChange, " +
										"nvl(decode(dd.TYPE_ID,'13',dd.AMOUNT),0) BurnerShifting, " +
										"nvl(decode(dd.TYPE_ID,'14',dd.AMOUNT),0) RaizerShifting, " +
										"nvl(decode(dd.TYPE_ID,'15',dd.AMOUNT),0) ConsultingFee, " +
										"nvl(decode(dd.TYPE_ID,'16',dd.AMOUNT),0) OtherFees " +
										"from MST_DEPOSIT md,DTL_DEPOSIT dd, MST_CUSTOMER_CATEGORY mcc " +
										"where MD.DEPOSIT_ID=DD.DEPOSIT_ID " +
										"and BANK_ID=" +bank_id+
										"and BRANCH_ID=" +branch_id+
										"and ACCOUNT_NO=" +account_no+
										" "+wClause+ 
										"order by MCC.CATEGORY_ID,md.DEPOSIT_DATE " ;
			*/
			
			
			String defaulterSql="SELECT   CATEGORY_NAME, " +
								"         CATEGORY_ID, " +
								"         DEPOSIT_DATE, " +
								"         DEPOSIT_DATE1, " +
								"         CUSTOMER_ID, " +
								"         BANK_ID, " +
								"         BRANCH_ID, " +
								"         ACCOUNT_NO, " +
								"         SUM(Security) Security, " +
								"         SUM(Sales) Sales, " +
								"         SUM(Connection) Connection, " +
								"         SUM(Comissioning) Comissioning, " +
								"         SUM(Service) Service, " +
								"         SUM(Pipeline) Pipeline, " +
								"         SUM(Load) Load, " +
								"         SUM(Disconnection) Disconnection, " +
								"         SUM(Reconnection) Reconnection, " +
								"         SUM(Additional) Additional, " +
								"         SUM(Penalty) Penalty, " +
								"         SUM(NameChange) NameChange, " +
								"         SUM(BurnerShifting) BurnerShifting, " +
								"         SUM(RaizerShifting) RaizerShifting, " +
								"         SUM(ConsultingFee) ConsultingFee, " +
								"         SUM(OtherFees) OtherFees " +
								"          " +
			" FROM " +
			"( " +
			"  SELECT MCI.CATEGORY_NAME, " +
			"         CATEGORY_ID, " +
			"         TO_CHAR (DEPOSIT_DATE, 'dd-MON-YYYY') DEPOSIT_DATE1, " +
			"         md.CUSTOMER_ID, " +
			"         DEPOSIT_DATE, " +
			"         BANK_ID, " +
			"         BRANCH_ID, " +
			"         ACCOUNT_NO, " +
			"         NVL (DECODE (dd.TYPE_ID, '01', dd.AMOUNT), 0) Security, " +
			"         NVL (DECODE (dd.TYPE_ID, '02', dd.AMOUNT), 0) Sales, " +
			"         NVL (DECODE (dd.TYPE_ID, '03', dd.AMOUNT), 0) Connection, " +
			"         NVL (DECODE (dd.TYPE_ID, '04', dd.AMOUNT), 0) Comissioning, " +
			"         NVL (DECODE (dd.TYPE_ID, '05', dd.AMOUNT), 0) Service, " +
			"         NVL (DECODE (dd.TYPE_ID, '06', dd.AMOUNT), 0) Pipeline, " +
			"         NVL (DECODE (dd.TYPE_ID, '07', dd.AMOUNT), 0) Load, " +
			"         NVL (DECODE (dd.TYPE_ID, '08', dd.AMOUNT), 0) Disconnection, " +
			"         NVL (DECODE (dd.TYPE_ID, '09', dd.AMOUNT), 0) Reconnection, " +
			"         NVL (DECODE (dd.TYPE_ID, '10', dd.AMOUNT), 0) Additional, " +
			"         NVL (DECODE (dd.TYPE_ID, '11', dd.AMOUNT), 0) Penalty, " +
			"         NVL (DECODE (dd.TYPE_ID, '12', dd.AMOUNT), 0) NameChange, " +
			"         NVL (DECODE (dd.TYPE_ID, '13', dd.AMOUNT), 0) BurnerShifting, " +
			"         NVL (DECODE (dd.TYPE_ID, '14', dd.AMOUNT), 0) RaizerShifting, " +
			"         NVL (DECODE (dd.TYPE_ID, '15', dd.AMOUNT), 0) ConsultingFee, " +
			"         NVL (DECODE (dd.TYPE_ID, '16', dd.AMOUNT), 0) OtherFees " +
			"    FROM MST_DEPOSIT md, DTL_DEPOSIT dd, MVIEW_CUSTOMER_INFO mci " +
			"   WHERE     MD.DEPOSIT_ID = DD.DEPOSIT_ID " +
			"         AND md.CUSTOMER_ID=mci.CUSTOMER_ID " +
			"         And BANK_ID='"+bank_id+"' "+
			"		  And BRANCH_ID='"+branch_id+"' "+
			"		  And ACCOUNT_NO='"+account_no+"' "+			
					
			" "+wClause+ 
			"  " +
			" )  " +
			" GROUP BY CATEGORY_NAME, " +
			"          CATEGORY_ID, " +
			"          DEPOSIT_DATE, " +
			"          DEPOSIT_DATE1, " +
			"          CUSTOMER_ID, " +
			"          BANK_ID, " +
			"          BRANCH_ID, " +
			"          ACCOUNT_NO " +
			" ORDER BY CATEGORY_ID, DEPOSIT_DATE1 " ;



			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		SecutityDepositReportDTO depositInfoDto=new SecutityDepositReportDTO();
        		depositInfoDto.setCategory_name(resultSet.getString("CATEGORY_NAME"));
        		depositInfoDto.setDeposit_date(resultSet.getString("DEPOSIT_DATE1"));
        		depositInfoDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		depositInfoDto.setSecurity_deposit(resultSet.getFloat("Security"));
        		depositInfoDto.setSales_of_store(resultSet.getFloat("Sales"));
        		depositInfoDto.setConnection_fee(resultSet.getFloat("Connection"));
        		depositInfoDto.setCommissioning_fee(resultSet.getFloat("Comissioning"));
        		depositInfoDto.setService_fee(resultSet.getFloat("Service"));
        		depositInfoDto.setPipeline_fee(resultSet.getFloat("Pipeline"));
        		depositInfoDto.setLoad_change_fee(resultSet.getFloat("Load"));
        		depositInfoDto.setDisconnection_fee(resultSet.getFloat("Disconnection"));
        		depositInfoDto.setReconnection_fee(resultSet.getFloat("Reconnection"));
        		depositInfoDto.setAdditional_fee(resultSet.getFloat("Additional"));
        		depositInfoDto.setPenalty(resultSet.getFloat("Penalty"));
        		depositInfoDto.setName_change_fee(resultSet.getFloat("NameChange"));
        		depositInfoDto.setBurner_shifting_fee(resultSet.getFloat("BurnerShifting"));
        		depositInfoDto.setRaizer_shifting_fee(resultSet.getFloat("RaizerShifting"));
        		depositInfoDto.setConsulting_fee(resultSet.getFloat("ConsultingFee"));
        		depositInfoDto.setOther_fee(resultSet.getFloat("OtherFees"));        		
        		depositInfoList.add(depositInfoDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return depositInfoList;
	}
	
	private AccountDTO getAccountInfo()
	{
           AccountDTO accountInfo=new AccountDTO();
           BankDTO bankInfo=new BankDTO();
           BranchDTO branchInfo=new BranchDTO();
		
		try {
		
			
			String account_info_sql="select * from MST_ACCOUNT_INFO mci,MST_BANK_INFO mbi,MST_BRANCH_INFO mbri " +
					"where mci.bank_id=mbi.bank_id " +
					"and mci.branch_id=mbri.branch_id " +
					"and MCI.BANK_ID=? " +
					"and MCI.branch_id=? " +
					"and MCI.account_no=? " ;



			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			ps1.setString(1, bank_id);
			ps1.setString(2, branch_id);
			ps1.setString(3, account_no);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		
        		accountInfo.setBank_name(resultSet.getString("BANK_NAME"));
        		branchInfo.setAddress(resultSet.getString("ADDRESS"));
        		branchInfo.setPhone(resultSet.getString("PHONE"));
        		branchInfo.setFax(resultSet.getString("FAX"));
        		branchInfo.setEmail(resultSet.getString("EMAIL"));
        		accountInfo.setBranch_name(resultSet.getString("BRANCH_NAME"));
        		accountInfo.setBranch(branchInfo);
        		accountInfo.setAccount_name(resultSet.getString("ACCOUNT_NAME"));
        		accountInfo.setAccount_no(resultSet.getString("ACCOUNT_NO")) ; 
        		accountInfo.setOpening_balance(resultSet.getString("OPENING_BALANCE"));
        		accountInfo.setAc_opening_date(resultSet.getString("OPENING_DATE"));
   
        		
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accountInfo;
	}
	
	public ArrayList<CustomerCategoryDTO> getCustomerCategoryList() {
		return customerCategoryList;
	}



	
	public void setCustomerCategoryList(ArrayList<CustomerCategoryDTO> customerCategoryList) {
		this.customerCategoryList = customerCategoryList;
	}

	

	public String getFrom_date_ind() {
		return from_date_ind;
	}
	public void setFrom_date_ind(String from_date_ind) {
		this.from_date_ind = from_date_ind;
	}
	public String getTo_date_ind() {
		return to_date_ind;
	}
	public void setTo_date_ind(String to_date_ind) {
		this.to_date_ind = to_date_ind;
	}
	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getCustomer_category() {
		return customer_category;
	}


	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}


	public String getBill_month() {
		return bill_month;
	}


	public void setBill_month(String bill_month) {
		this.bill_month = bill_month;
	}


	public String getBill_year() {
		return bill_year;
	}


	public void setBill_year(String bill_year) {
		this.bill_year = bill_year;
	}


	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}


	public String getCategory_name() {
		return category_name;
	}


	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}


	public String getBank_id() {
		return bank_id;
	}


	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}


	public String getBranch_id() {
		return branch_id;
	}


	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}


	public String getAccount_no() {
		return account_no;
	}


	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}


	public String getFrom_date() {
		return from_date;
	}


	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}


	public String getTo_date() {
		return to_date;
	}


	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}


	public String getReport_for2() {
		return report_for2;
	}


	public void setReport_for2(String report_for2) {
		this.report_for2 = report_for2;
	}


	public String getCollection_month() {
		return collection_month;
	}


	public void setCollection_month(String collection_month) {
		this.collection_month = collection_month;
	}


	public String getCollection_year() {
		return collection_year;
	}


	public void setCollection_year(String collection_year) {
		this.collection_year = collection_year;
	}


	public String getCustomer_id() {
		return customer_id;
	}


	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	
	public ServletContext getServlet() {
		return servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}
	
	


	
  }


