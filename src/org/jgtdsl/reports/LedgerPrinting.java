package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.actions.Customer;
import org.jgtdsl.dto.BurnerQntChangeDTO;
import org.jgtdsl.dto.CustomerApplianceDTO;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerInfoDTO4Bill;
import org.jgtdsl.dto.CustomerLedgerDTO;
import org.jgtdsl.dto.CustomerMeterDTO;
import org.jgtdsl.dto.DepositLedgerDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.ConnectionStatus;
import org.jgtdsl.enums.MeterStatus;
import org.jgtdsl.models.BurnerQntChangeService;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.LedgerService;
import org.jgtdsl.models.MeterService;
import org.jgtdsl.utils.Utils;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class LedgerPrinting extends BaseAction implements ServletContextAware {
	private static final long serialVersionUID = 8854240739341830184L;
	private ServletContext servlet;
	private String bill_id;
	private String customer_category;
	private String area_id;
	private String customer_id;
	private String category_name;
	private String bill_month;
	private String bill_year;
	private String download_type;
	private String bill_for;
	private int serial = 0;
	public static int textDiff = 597;
	private boolean water_mark = false;
	
	ArrayList<CustomerLedgerDTO> mainLedger= new ArrayList<CustomerLedgerDTO>();
	ConnectionStatus connenum;
	MeterService ms=new MeterService();
	ArrayList<CustomerApplianceDTO> applianceList=new ArrayList<CustomerApplianceDTO>();
	private ArrayList<CustomerMeterDTO> meterList=new ArrayList<CustomerMeterDTO>();
	BurnerQntChangeService BQCS= new BurnerQntChangeService();
	ArrayList<BurnerQntChangeDTO> connledger= new ArrayList<BurnerQntChangeDTO>();
	LedgerService ls= new LedgerService();
	CustomerDTO customerInfo = new CustomerDTO();
	CustomerService cs= new CustomerService();

	public String downloadLedger() throws Exception {
		Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		Font font2 = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
		Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		Font font4 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		UserDTO loggedInUser = (UserDTO) session.get("user");
		if ((area_id == null || area_id.equalsIgnoreCase(""))
				&& loggedInUser != null)
			area_id = loggedInUser.getArea_id();

		String fileName = "LEDGER-" + customer_id + ".pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		try {
			ReportFormat Event = new ReportFormat(getServletContext());
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			// writer.setPageEvent(Event);
			PdfPCell pcell = null;

			document.open();
			String realPath = servlet
					.getRealPath("/resources/images/logo/JG.png"); // image path
			Image img = Image.getInstance(realPath);

			// img.scaleToFit(10f, 200f);
			// img.scalePercent(200f);
			img.scaleAbsolute(30f, 34f);
			// img.setAbsolutePosition(145f, 780f);
			img.setAbsolutePosition(260f, 520f); // rotate

			document.add(img);
			
			CustomerDTO customer = cs.getCustomerInfo(customer_id);

			PdfPTable mTable = new PdfPTable(1);
			mTable.setWidthPercentage(100);
			pcell = new PdfPCell(new Paragraph(
					"JALALABAD GAS T & D SYSTEM LIMITED", ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)",
					ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);			

			Chunk chunk1 = new Chunk("Regional Distribution: ", font2);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer
					.valueOf(getArea_id()) - 1]), ReportUtil.f8B);
			Paragraph p = new Paragraph();
			p.add(chunk1);
			p.add(chunk2);
			pcell = new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(
					"ledger for: "+customer.getConnectionInfo().getIsMetered_name()+" Customer", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			///////////////////////////////////////
			PdfPTable ledger1 = new PdfPTable(3);
			ledger1.setWidthPercentage(98);
			
			pcell=new PdfPCell(new Paragraph(" ",font3));
			pcell.setPadding(5);
			pcell.setColspan(3);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			ledger1.addCell(pcell);
			///////////customerTable//////////
			
			PdfPTable customerinfo = new PdfPTable(2);
			customerinfo.setWidths(new float[]{35,65});
			
			DefaulterCCertificate dcc= new DefaulterCCertificate();
			customerInfo= dcc.getCustomerInfo(this.customer_id);
			
			pcell=new PdfPCell(new Paragraph("Customer Code: ",font3));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(this.customer_id,font2));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer Name: ",font3));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(customerInfo.getCustomer_name(),font3));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer Address: ",font3));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(customerInfo.getAddress(),font3));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);	
			
			//////////customerTable ends/////////
			
			
			///////////////burnerTable/////////////
			PdfPTable burnerTable= new PdfPTable(2);			
			pcell=new PdfPCell(new Paragraph("Burner Info.",font4));
			pcell.setColspan(2);
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			burnerTable.addCell(pcell);
			
			
			applianceList=ms.getCustomerApplianceList(customer_id);	
			for(CustomerApplianceDTO x: applianceList){
				
				pcell=new PdfPCell(new Paragraph(x.getApplianc_name(),font4));
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				burnerTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getApplianc_qnt(),font4));
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				burnerTable.addCell(pcell);			
								
			}
			
			/////////////burnertable ends/////////////
			
			/////////////////////meter table/////////////////
			
			MeterService ms=new MeterService();
			meterList=ms.getCustomerMeterList(customer_id, Utils.EMPTY_STRING,Utils.EMPTY_STRING);
			
			PdfPTable meterTable= new PdfPTable(3);			
			pcell=new PdfPCell(new Paragraph("Meter SL",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			meterTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rent",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			meterTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Status",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			meterTable.addCell(pcell);
			
			for(CustomerMeterDTO x: meterList){
				
				pcell=new PdfPCell(new Paragraph(x.getMeter_sl_no(),font3));
				pcell.setPadding(3);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				meterTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getMeter_rent(),font3));
				pcell.setPadding(3);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				meterTable.addCell(pcell);		
				
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(x.getStatus()),font3));
				pcell.setPadding(3);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				meterTable.addCell(pcell);
			}
			
			/////////////////////meter table ends////////////////
			
			/////////////Connection Ledger non meter///////////
			PdfPTable connLdgr= new PdfPTable(3);			
			
			pcell=new PdfPCell(new Paragraph("Old(Double)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			connLdgr.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("New(Double)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			connLdgr.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Date",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			connLdgr.addCell(pcell);
			
			this.connledger= BQCS.getBurnerQntChangeListGrid(this.customer_id);	
			
			for(BurnerQntChangeDTO x: connledger){
				pcell=new PdfPCell(new Paragraph(x.getOld_double_burner_qnt(),font3));
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				connLdgr.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getNew_double_burner_qnt(),font3));
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				connLdgr.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getEffective_date(),font3));
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				connLdgr.addCell(pcell);
			}
			
			/////////////connection ledger non meter ends///////////
			
			////////////Security ledger meter/////////////////
			
			PdfPTable secLedMetered= new PdfPTable(2);			
			
			
			
			ArrayList<DepositLedgerDTO> depositAmount= ls.getDepositLedger(this.customer_id);
			int totalBG=0;
			int totalCB=0;
			
			pcell=new PdfPCell(new Paragraph("Deposit Total (Tk.)",font4));
			pcell.setPadding(3);
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			secLedMetered.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Cash Bank",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			secLedMetered.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("BG",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			secLedMetered.addCell(pcell);
			
			for(int i=0; i<depositAmount.size();i++){
				if(depositAmount.get(i).getDeposit_type().equalsIgnoreCase("BANK GUARANTEE")){
					totalBG = totalBG + Integer.parseInt( depositAmount.get(i).getDebit_amount());
				}
					
			}
			
			for(int i=0; i<depositAmount.size();i++){
				if(depositAmount.get(i).getDeposit_type().equalsIgnoreCase("CASH BANK")){
					totalCB = totalCB + Integer.parseInt( depositAmount.get(i).getDebit_amount());
				}
			}
			
			pcell=new PdfPCell(new Paragraph(String.valueOf(totalCB),font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			secLedMetered.addCell(pcell);			
			
			pcell=new PdfPCell(new Paragraph(String.valueOf(totalBG),font4));
			pcell.setPadding(3);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			secLedMetered.addCell(pcell);
			
			////////////security ledger meter ends/////////////////
			
			///////////////main ledger///////////////////
			PdfPTable mainledger= new PdfPTable(11);
			mainledger.setWidthPercentage(98);
			
			pcell=new PdfPCell(new Paragraph(" ",font4));
			pcell.setPadding(5);
			pcell.setColspan(11);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Payment Date",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Description",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Bank",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Volum of Gas Sold(m+"+Math.pow(0, 3)+")",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Sales(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Debit Surcharge(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Debit(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Credit Surcharge(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Credit(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Balance(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Bill Due Date",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			this.mainLedger= ls.getCustomerLedger(this.customer_id);
			
			for(CustomerLedgerDTO x: mainLedger){
				
				pcell=new PdfPCell(new Paragraph(x.getIssue_paid_date(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getParticulars(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getBank_name(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getGas_sold(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getSales_amount(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getSurcharge(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getDebit_amount(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getCredit_surcharge(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getCredit_amount(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(x.getBalance_amount()),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getDue_date(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);					
				
			}
			
			///////////////main ledger ends////////////////
			
			ledger1.addCell(customerinfo);
			
			if (customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered")) {
				ledger1.addCell(meterTable);
			}else{				
				ledger1.addCell(burnerTable);
			}
			
			if (customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered")) {
				ledger1.addCell(secLedMetered);
			}else{
				ledger1.addCell(connLdgr);
			}
			

			document.add(mTable);
			document.add(ledger1);
			document.add(mainledger);
			
			
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(), fileName);
			document = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ServletContext getServlet() {
		return servlet;
	}

	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}

	public String getBill_id() {
		return bill_id;
	}

	public void setBill_id(String bill_id) {
		this.bill_id = bill_id;
	}

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
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

	public String getDownload_type() {
		return download_type;
	}

	public void setDownload_type(String download_type) {
		this.download_type = download_type;
	}

	public String getBill_for() {
		return bill_for;
	}

	public void setBill_for(String bill_for) {
		this.bill_for = bill_for;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public static int getTextDiff() {
		return textDiff;
	}

	public static void setTextDiff(int textDiff) {
		LedgerPrinting.textDiff = textDiff;
	}

	public boolean isWater_mark() {
		return water_mark;
	}

	public void setWater_mark(boolean water_mark) {
		this.water_mark = water_mark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
