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
import org.jgtdsl.dto.BankBookDTO;
import org.jgtdsl.dto.BranchDTO;
import org.jgtdsl.dto.ReconciliationDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ReconciliationReport extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	ArrayList<ReconciliationDTO> addList=new ArrayList<ReconciliationDTO>();
	ArrayList<ReconciliationDTO> lessList=new ArrayList<ReconciliationDTO>();
	AccountDTO accountInfo=new AccountDTO();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String bank_id;
	    private  String branch_id;
	    private  String account_no;
	   // private  String recon_month;
	    private  String collection_month;
	    private  String collection_year;
	   // private  String recon_year;
	    private  String collection_date;
	    private  String report_for; 
	    private  String area="01";
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
		BankBookDTO bankBookDTO = null;

	public String execute() throws Exception
	{
				
		String fileName="Bank_Book_Statement.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(20,20,30,72);
		PdfPCell pcell=null;
		bankBookDTO = new BankBookDTO();
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			//MeterReadingDTO meterReadingDTO = new MeterReadingDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
		   
				
			headerTable.setWidths(new float[] {
				5,190,5
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS TRANSMISSION AND DISTRIBUTION SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A company of PetroBangla)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("Regional Office :",ReportUtil.f8B);
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
			
			
			
			
			PdfPTable headLinetable = null;
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{10,80,10});
			
			accountInfo = getAccountInfo();
			double openingBalance=getBankBookOpeningBalanceMonthWise();
			String opening_balance=String.valueOf(openingBalance);
			String lastDay=getLastDate();
			double addTotal=0.0;
			double lessTotal=0.0;
			double grandTotal=0.0;
			
			
			
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			pcell=new PdfPCell(new Paragraph("BANK RECONCILIATION STATEMENT OF "+Month.values()[Integer.valueOf(collection_month)-1]+", "+collection_year,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("STD A/C NO. : "+accountInfo.getAccount_no()+", "+accountInfo.getBank_name()+", "+accountInfo.getBranch_name(),ReportUtil.f8B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(3);
			pcell.setPaddingTop(5f);
			pcell.setPaddingBottom(15f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			document.add(headLinetable);
			
			
			PdfPTable dataTable = new PdfPTable(3);
			dataTable.setWidthPercentage(100);
			dataTable.setWidths(new float[]{50,30,30});
			dataTable.setHeaderRows(1);
			
			
			pcell = new PdfPCell(new Paragraph("A)",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(3);
			dataTable.addCell(pcell);
			pcell = new PdfPCell(new Paragraph("Balance as per Bank Statement(as on "+lastDay+")",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			dataTable.addCell(pcell);
			pcell = new PdfPCell(new Paragraph("Taka",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(openingBalance),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			dataTable.addCell(pcell);
			
			addList=getList("add");
			int addListSize=addList.size();
			
			pcell = new PdfPCell(new Paragraph("ADD",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(3);
			dataTable.addCell(pcell);
			pcell = new PdfPCell(new Paragraph("B)",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(3);
			dataTable.addCell(pcell);
			for(int i=0;i<addListSize;i++)
			{
				pcell = new PdfPCell(new Paragraph(addList.get(i).getAdd_comments().replace("XXX", addList.get(i).getAddAccount()),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				dataTable.addCell(pcell);
				
				addTotal=addTotal+Double.valueOf(addList.get(i).getAdd_amount());
				pcell = new PdfPCell(new Paragraph(taka_format.format(Double.valueOf(addList.get(i).getAdd_amount())),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				dataTable.addCell(pcell);
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				dataTable.addCell(pcell);
			}
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(addTotal),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(addTotal),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			dataTable.addCell(pcell);
			
			
			
			pcell = new PdfPCell(new Paragraph("Total(A+B)",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(2);
			dataTable.addCell(pcell);
			
			grandTotal=openingBalance+addTotal;
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotal),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			dataTable.addCell(pcell);
			
			
			
			lessList=getList("less");
			int leeListSize=lessList.size();
			
			pcell = new PdfPCell(new Paragraph("LESS",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(3);
			dataTable.addCell(pcell);
			pcell = new PdfPCell(new Paragraph("C)",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(3);
			dataTable.addCell(pcell);
			for(int i=0;i<leeListSize;i++)
			{
				pcell = new PdfPCell(new Paragraph(lessList.get(i).getLessComment().replace("XXX", lessList.get(i).getLessAccount()),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				dataTable.addCell(pcell);
				
				lessTotal=lessTotal+Float.valueOf(lessList.get(i).getLessAmount());
				pcell = new PdfPCell(new Paragraph(taka_format.format(Double.valueOf(lessList.get(i).getLessAmount())),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				dataTable.addCell(pcell);
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				dataTable.addCell(pcell);
			}
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(lessTotal),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(lessTotal),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("E)Balance as per Bank Book(as on "+lastDay+")/(A+B)-C",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(2);
			dataTable.addCell(pcell);
			
			grandTotal=grandTotal-lessTotal;
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotal),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			dataTable.addCell(pcell);
			
		    document.add(dataTable);
			
		
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	
	private ArrayList<ReconciliationDTO> getList(String Type)
	{
      ArrayList<ReconciliationDTO> addList=new ArrayList<ReconciliationDTO>();
		
		try {
			
			
			
		
			
			
			String transaction_sql="select * from MST_RECONCILIATION mr,DTL_RECONCILIATION dr"+
									" where mr.pid=dr.pid"+
									" and BANK_ID='"+bank_id+"'"+
									" and BRANCH_ID='"+branch_id+"'"+
									" and ACCOUNT_NO='"+account_no+"'"+
									" and MONTH='"+collection_month+"'"+
									" and YEAR='"+collection_year+"'"+
									" and Type='"+Type+"'";
					

			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		ReconciliationDTO addDto = new ReconciliationDTO();
        		if(Type.equals("add"))
        		{
        			addDto.setAdd_comments(resultSet.getString("PARTICULARS"));
            		addDto.setAdd_amount(resultSet.getString("AMOUNT"));
            		addDto.setAddAccount(resultSet.getString("AC_NO"));
        		}else
        		{
        			addDto.setLessComment(resultSet.getString("PARTICULARS"));
            		addDto.setLessAmount(resultSet.getString("AMOUNT"));
            		addDto.setLessAccount(resultSet.getString("AC_NO"));
        		}
        		
        		
        		addList.add(addDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return addList;
	}
	
	private AccountDTO getAccountInfo()
	{
           AccountDTO accountInfo=null;
           BranchDTO branchInfo=new BranchDTO();
		
		try {
			accountInfo = new AccountDTO();
			
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
	
	
private Double getBankBookOpeningBalanceMonthWise(){
		
		
		double openingBalance=0.0; 
		
		
		try {
			
			
			String account_info_sql="select * from MST_RECONCILIATION " +
					"where BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' " +
					"AND MONTH='"+collection_month+"' " +
					"AND YEAR='"+collection_year+"' " ;

					
					
					
					
					
				//	"select get_opening_balance ('"+bank_id+"','"+branch_id+"','"+account_no+"','"+tempMonth+"','"+tempYear+"') BALANCE from dual " ;
			
			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		openingBalance=resultSet.getDouble("OPENING_BALANCE");
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return openingBalance;
		
	}
	

	private String getLastDate(){		
		String lastDate="";		
		try {		
			String account_info_sql="select to_char(LAST_DAY (TO_DATE ("+collection_month+" || '-' || "+collection_year+", 'MM-YYYY'))) TRANS_DATE from dual " ;
		
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);			
			ResultSet resultSet=ps1.executeQuery();        	
			while(resultSet.next())        	{        		 
				lastDate=resultSet.getString("TRANS_DATE");        		
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return lastDate;
	}

	public String getAccount_no() {
		return account_no;
	}


	public void setAccount_no(String account_no) {
		this.account_no = account_no;
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




	

	public String getCollection_date() {
		return collection_date;
	}


	public void setCollection_date(String collection_date) {
		this.collection_date = collection_date;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
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
	
	

	


}
