package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.dto.VatRebateITFFDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.enums.NumberToWordConversion;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class VatTaxFFReport extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	VatRebateITFFDTO vatRebateDTO=null;
	VatRebateITFFDTO incomeTaxDTO=null;
	VatRebateITFFDTO freedomFighterDTO=null;
	ArrayList<VatRebateITFFDTO>vatRebateList=new ArrayList<VatRebateITFFDTO>();
	ArrayList<VatRebateITFFDTO>incomeTaxList=new ArrayList<VatRebateITFFDTO>();
	ArrayList<VatRebateITFFDTO>freedomFighterList=new ArrayList<VatRebateITFFDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	

	private  String area;
	private  String customer_id;
    private  String category_name;
    private  String report_for;
    private  String from_date; 
    private  String to_date; 
    
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	

	public String execute() throws Exception
	{
				
		String fileName="Vat_rebate_FF_IT.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(20,20,30,72);
		PdfPCell pcell=null;
		
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
			
			if(report_for.equals("vat_rebate")){
				generatePdfVatRebate(document);
			}else if(report_for.equals("income_tax")){
				generatePdfIncomeTax(document);
			}else if(report_for.equals("freedom")){
				generatePdfFreedomFighter(document);
			}else if(report_for.equals("individual")){
				generatePdfIndividual(document);
			}
			
			
			
			
			
			
			
			/* End of the table */
			
			
			
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	private void generatePdfIndividual(Document document) throws DocumentException{
		
		PdfPTable headLinetable = null;
		PdfPCell pcell = null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{5,90,5});		
		
		pcell=new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Statement showing the Customer wise AIT deducted by customer at source from "+from_date+" TO "+to_date,ReportUtil.f9B));
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headLinetable.addCell(pcell);		
		
		pcell=new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		PdfPTable pTable = new PdfPTable(11);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{5,10,10,10,10,9,9,9,9,9,10});
		
		double totalBillAmount=0.0;
		double totalConsumption=0.0;
		double totalDeductable=0.0;
		double deductabledAmt=0.0;
		double totalDeductedAmt=0.0;
		double differenceAmt=0.0;
		double totalDifference=0.0;
		
		double dedudtAmt=0.0;
		
		incomeTaxList=getIndividualInfo();
				
		int listSize=incomeTaxList.size();
		
		for (int i = 0; i < listSize; i++) {
			if(i==0){
				pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
				pcell.setBorder(0);
				pcell.setColspan(11);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Customer Name: "+incomeTaxList.get(i).getCustomerName(),ReportUtil.f11B));
				pcell.setBorder(0);
				pcell.setColspan(11);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Customer Code No.: "+incomeTaxList.get(i).getCustomerId(),ReportUtil.f11B));
				pcell.setBorder(0);
				pcell.setColspan(11);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Customer Code No.: "+incomeTaxList.get(i).getCustomerCategory(),ReportUtil.f11B));
				pcell.setBorder(0);
				pcell.setColspan(11);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
				pcell.setBorder(0);
				pcell.setColspan(11);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("SI No.",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Bill Month",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Gas Sales Quantity (SCM)",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Total Bill Amount (Taka)",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Rate of AIT",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Deductable AIT (Taka)",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("AIT Deducted (Taka)",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Difference (If any)",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Chalan No.",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Date of Payment",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Remarks",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("01",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("02",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("03",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("04",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("05",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("06=04X05",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("07",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("08=06-07",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("09",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("10",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("11",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pTable.addCell(pcell);
			
			int month_value=Integer.valueOf(incomeTaxList.get(i).getMonth())-1;
			pcell = new PdfPCell(new Paragraph(String.valueOf(Month.values()[month_value])+"'"+incomeTaxList.get(i).getYear(),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pTable.addCell(pcell);
			
			totalConsumption=totalConsumption+incomeTaxList.get(i).getGasConsumption();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(incomeTaxList.get(i).getGasConsumption()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			totalBillAmount=totalBillAmount+incomeTaxList.get(i).getBillAmount();
			pcell = new PdfPCell(new Paragraph(taka_format.format(incomeTaxList.get(i).getBillAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("3.0%",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			dedudtAmt=incomeTaxList.get(i).getBillAmount();
			deductabledAmt=(dedudtAmt*3/100);
			totalDeductable=totalDeductable+deductabledAmt;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(deductabledAmt),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			totalDeductedAmt=totalDeductedAmt+incomeTaxList.get(i).getTaxAmount();
			pcell = new PdfPCell(new Paragraph(taka_format.format(incomeTaxList.get(i).getTaxAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			differenceAmt=deductabledAmt-incomeTaxList.get(i).getTaxAmount();
			totalDifference=totalDifference+differenceAmt;
			pcell = new PdfPCell(new Paragraph(taka_format.format(differenceAmt),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(incomeTaxList.get(i).getChalanNo(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(incomeTaxList.get(i).getChalanDate(),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(consumption_format.format(totalConsumption),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalDeductable),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalDeductedAmt),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalDifference),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		document.add(pTable);
		
	}
	
	private void generatePdfVatRebate(Document document) throws DocumentException{
		
		PdfPTable headLinetable = null;
		PdfPCell pcell = null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{5,90,5});		
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("STATEMENT SHOWING THE CALCULATION OF VAT REBATE ON GAS BILL FROM "+from_date+" TO "+to_date,ReportUtil.f9B));
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headLinetable.addCell(pcell);		
		
		pcell=new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		PdfPTable pTable = new PdfPTable(6);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{5,25,20,20,15,15});
		
		String previousCategory= new String("");
		double totalConsumption=0.0;
		double totalBillAmt=0.0;
		double totalVat=0.0;
		double totalRebate=0.0;
		
		vatRebateList=getVatRebateInfo();
		int listSize=vatRebateList.size();
		for (int i = 0; i < listSize; i++) {
			
			
			String currentCategory=vatRebateList.get(i).getCustomerCategory();
			
			
			if(i==0){
				
				pcell = new PdfPCell(new Paragraph("Area Name"+" :  "+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f11B));
				pcell.setColspan(6);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Category :  "+vatRebateList.get(i).getCustomerCategory(),ReportUtil.f11B));
				pcell.setColspan(6);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Gas Comsumption",ReportUtil.f11B));
				pcell.setRowspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Bill Amount (Taka)",ReportUtil.f11B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Total VAT (Tk.)",ReportUtil.f11B));
				pcell.setRowspan(1);
				pcell.setColspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("(SCM)",ReportUtil.f8B));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Total VAT",ReportUtil.f8B));
				pcell.setRowspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);				
				
				pcell = new PdfPCell(new Paragraph("Rebate(VAT*.80)",ReportUtil.f9B));
				pcell.setRowspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("01",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("02",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("03",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("04=3*Rate",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("05=3*Rate",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("06=05 X .80",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);				
			}
			
			if(!currentCategory.equals(previousCategory)){
				
				if(i>0){
				
					pcell = new PdfPCell(new Paragraph("Area Name"+" :  "+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f11B));
					pcell.setColspan(6);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Category :  "+vatRebateList.get(i).getCustomerCategory(),ReportUtil.f11B));
					pcell.setColspan(6);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("Gas Comsumption",ReportUtil.f11B));
					pcell.setRowspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("Bill Amount (Taka)",ReportUtil.f11B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Total VAT (Tk.)",ReportUtil.f11B));
					pcell.setRowspan(1);
					pcell.setColspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("(SCM)",ReportUtil.f8B));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Total VAT",ReportUtil.f8B));
					pcell.setRowspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);				
					
					pcell = new PdfPCell(new Paragraph("Rebate(VAT*.80)",ReportUtil.f9B));
					pcell.setRowspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("01",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("02",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("03",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("04=3*Rate",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("05=3*Rate",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("06=05 X .80",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
				
				
				}
				previousCategory=vatRebateList.get(i).getCustomerCategory();
				
			}
			
			/////////////////////////////////////////////////////////////////////////////////////////////////
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(vatRebateList.get(i).getCustomerName()+"("+vatRebateList.get(i).getCustomerId()+")",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalConsumption=totalConsumption+vatRebateList.get(i).getGasConsumption();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(vatRebateList.get(i).getGasConsumption()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalBillAmt=totalBillAmt+vatRebateList.get(i).getBillAmount();
			pcell = new PdfPCell(new Paragraph(taka_format.format(vatRebateList.get(i).getBillAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalVat=totalVat+(vatRebateList.get(i).getRebateAmount()*100/80);
			pcell = new PdfPCell(new Paragraph(taka_format.format(vatRebateList.get(i).getRebateAmount()*100/80),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalRebate=totalRebate+vatRebateList.get(i).getRebateAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(vatRebateList.get(i).getRebateAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
		}
		
		pcell = new PdfPCell(new Paragraph("Total=",ReportUtil.f11B));
		pcell.setColspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(consumption_format.format(totalConsumption),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmt),ReportUtil.f9B));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalVat),ReportUtil.f9B));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalRebate),ReportUtil.f9B));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		document.add(pTable);
	}
	
	private void generatePdfIncomeTax(Document document)throws DocumentException{
		
		PdfPTable headLinetable = null;
		PdfPCell pcell = null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{5,90,5});		
		
		pcell=new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("STATEMENT SHOWING THE AIT DEDUCTED BY CUSTOMER AT SOURCE FROM "+from_date+" TO "+to_date,ReportUtil.f9B));
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headLinetable.addCell(pcell);		
		
		pcell=new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		PdfPTable pTable = new PdfPTable(5);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{10,30,20,20,20});
		
		String previousCategory= new String("");
		double totalBillAmt=0.0;
		double totalTax=0.0;
		double subTotalBillAmt=0.0;
		double subTotalTaxAmt=0.0;
		
		incomeTaxList=getIncomeTaxInfo();
		int listSize=incomeTaxList.size();
		for (int i = 0; i < listSize; i++) {
			
			
			String currentCategory=incomeTaxList.get(i).getCustomerCategory();
			
			
			if(i==0){
				
				pcell = new PdfPCell(new Paragraph("Area Name"+" :  "+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f11B));
				pcell.setColspan(5);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Category :  "+incomeTaxList.get(i).getCustomerCategory(),ReportUtil.f11B));
				pcell.setColspan(5);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Customers Code",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Total Bill Amount (Taka)",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Total Tax Deducted",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
								
			}
			
			if(!currentCategory.equals(previousCategory)){
				
				if(i>0){
					
					pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(3);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalBillAmt),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalTaxAmt),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					subTotalBillAmt=0.0;
					subTotalTaxAmt=0.0;
				
					pcell = new PdfPCell(new Paragraph("Area Name"+" :  "+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f11B));
					pcell.setColspan(5);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Category :  "+incomeTaxList.get(i).getCustomerCategory(),ReportUtil.f11B));
					pcell.setColspan(5);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("Customers Code",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("Total Bill Amount (Taka)",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Total Tax Deducted",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
				
				
				}
				previousCategory=incomeTaxList.get(i).getCustomerCategory();
				
			}
			
			/////////////////////////////////////////////////////////////////////////////////////////////////
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(incomeTaxList.get(i).getCustomerName(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(incomeTaxList.get(i).getCustomerId(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			subTotalBillAmt=subTotalBillAmt+incomeTaxList.get(i).getBillAmount();
			totalBillAmt=totalBillAmt+incomeTaxList.get(i).getBillAmount();
			pcell = new PdfPCell(new Paragraph(taka_format.format(incomeTaxList.get(i).getBillAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subTotalTaxAmt=subTotalTaxAmt+incomeTaxList.get(i).getTaxAmount();
			totalTax=totalTax+incomeTaxList.get(i).getTaxAmount();
			pcell = new PdfPCell(new Paragraph(taka_format.format(incomeTaxList.get(i).getTaxAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			if(i==listSize-1){
				pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(3);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalBillAmt),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalTaxAmt),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
			}
			
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total=",ReportUtil.f11B));
		pcell.setColspan(3);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmt),ReportUtil.f11B));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalTax),ReportUtil.f11B));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		long amountTax=(new Double(totalTax).longValue());
		
		String inAWord=NumberToWordConversion.convert(amountTax);
		
		pcell = new PdfPCell(new Paragraph("In a Word: "+inAWord+" Taka Only",ReportUtil.f11B));
		pcell.setColspan(5);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pTable.addCell(pcell);
		
		document.add(pTable);
	}
	private void generatePdfFreedomFighter(Document document)throws DocumentException{
		
		PdfPCell pcell = null;
		PdfPTable headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{5,90,5});		
		
		pcell=new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Statement Showing the calculation of Gas Bill Rebate for Freedom Fighter Customer from "+from_date+" TO "+to_date,ReportUtil.f9B));
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headLinetable.addCell(pcell);		
		
		pcell=new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		pcell.setMinimumHeight(8f);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		PdfPTable pTable = new PdfPTable(6);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{10,30,15,15,15,15});
		
		pcell = new PdfPCell(new Paragraph("SI. No.",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Burner Qty.",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Amount Rebate (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Rebate",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Bill",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Rebate",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("01",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("02",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("03",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("04",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("05=03*Rate",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("06=04x650",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		int totalBurnerQty=0;
		int rebateBurnerQty=0;
		double rebateBillAmt=0.0;
		int totalRebateBurnerQty=0;
		double totalRebateBillAmt=0.0;
		
		freedomFighterList=getFreedomFighterInfo();
		int listSize=freedomFighterList.size();
		
		for (int i = 0; i < listSize; i++){
			

			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(freedomFighterList.get(i).getCustomerName()+"("+freedomFighterList.get(i).getCustomerId()+")",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			totalBurnerQty=totalBurnerQty+freedomFighterList.get(i).getBurnerQty();
			pcell = new PdfPCell(new Paragraph(taka_format.format(freedomFighterList.get(i).getBurnerQty()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("1",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalRebateBillAmt=totalRebateBillAmt+freedomFighterList.get(i).getBillAmount();
			pcell = new PdfPCell(new Paragraph(taka_format.format(freedomFighterList.get(i).getBillAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			rebateBillAmt=rebateBillAmt+freedomFighterList.get(i).getVatAmount();
			pcell = new PdfPCell(new Paragraph(taka_format.format(freedomFighterList.get(i).getVatAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
		}
		
		pcell = new PdfPCell(new Paragraph("Total = ",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalBurnerQty),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalRebateBillAmt),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(rebateBillAmt),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		document.add(pTable);
		
	}

	
	private ArrayList<VatRebateITFFDTO> getVatRebateInfo(){
		ArrayList<VatRebateITFFDTO> vatRebateList= new ArrayList<VatRebateITFFDTO>();
		try {
			String area=loggedInUser.getArea_id();
			
			String[] fiscalYear=from_date.split("-");
			String firstPart=fiscalYear[2].toString()+fiscalYear[1].toString();
			String[] fiscalYear1=to_date.split("-");
			String sndtPart=fiscalYear1[2].toString()+fiscalYear1[1].toString();
			
			
			String disconnection_info_sql="SELECT mci.CUSTOMER_ID, " +
					"         mci.FULL_NAME, " +
					"         mci.CATEGORY_NAME, " +
					"         mci.CATEGORY_ID, " +
					"         mci.CATEGORY_TYPE, " +
					"         BILLED_AMOUNT, " +
					"         TOTAL_ACTUAL_CONSUMPTION, " +
					"         VAT_REBATE_AMOUNT " +
					"    FROM (select BM.CUSTOMER_ID CUSTOMER_ID,sum(SR.TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,sum(BM.BILLED_AMOUNT) BILLED_AMOUNT,sum(SR.VAT_REBATE) VAT_REBATE_AMOUNT from sales_report SR,BILL_METERED BM " +
					"WHERE SR.BILL_ID=BM.BILL_ID " +
					"AND SR.VAT_REBATE<>0 " +
					"and SR.VAT_REBATE is not null " +
					"AND BILL_YEAR||lpad(bill_month,2,0) between "+firstPart+" and "+sndtPart+" " +
					"group by bm.customer_id) tab1, " +
					"         MVIEW_CUSTOMER_INFO mci " +
					"   WHERE     TAB1.CUSTOMER_ID = MCI.CUSTOMER_ID " +
					"         AND SUBSTR (MCI.CUSTOMER_ID, 1, 2) = '"+area+"' " +
					"         AND SUBSTR (MCI.CUSTOMER_ID, 3, 2) = mci.CATEGORY_ID " +
					"ORDER BY CATEGORY_ID " ;	
					
					
					
//					"SELECT mci.CUSTOMER_ID, " +
//					"         mci.FULL_NAME, " +
//					"         mci.CATEGORY_NAME, " +
//					"         mci.CATEGORY_ID, " +
//					"         mci.CATEGORY_TYPE, " +
//					"         PAYABLE_AMOUNT, " +
//					"         ACTUAL_GAS_CONSUMPTION, " +
//					"         VAT_AMOUNT, " +
//					"         VAT_REBATE_AMOUNT " +
//					"    FROM (  SELECT BM.CUSTOMER_ID CUSTOMER_ID, " +
//					"                   SUM (BM.PAYABLE_AMOUNT) PAYABLE_AMOUNT, " +
//					"                   SUM (BM.ACTUAL_GAS_CONSUMPTION) ACTUAL_GAS_CONSUMPTION, " +
//					"                   SUM (SMG.VAT_AMOUNT) VAT_AMOUNT, " +
//					"                   SUM (SMP.VAT_REBATE_AMOUNT) VAT_REBATE_AMOUNT " +
//					"              FROM BILL_METERED BM, " +
//					"                   BILL_COLLECTION_METERED bcm, " +
//					"                   SUMMARY_MARGIN_PB SMP, " +
//					"                   SUMMARY_MARGIN_GOVT SMG " +
//					"             WHERE     BM.BILL_ID = SMP.BILL_ID " +
//					"                   AND BM.BILL_ID = BCM.BILL_ID " +
//					"                   AND SMG.BILL_ID = SMP.BILL_ID " +
//					"                   AND SMP.VAT_REBATE_AMOUNT IS NOT NULL " +
//					"                   AND SMP.VAT_REBATE_AMOUNT <> 0 " +
//					"                   AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-MM-yyyy') " +
//					"                                           AND TO_DATE ('"+to_date+"', 'dd-MM-yyyy') " +
//					"          GROUP BY BM.CUSTOMER_ID) tab1, " +
//					"         MVIEW_CUSTOMER_INFO mci " +
//					"   WHERE     TAB1.CUSTOMER_ID = MCI.CUSTOMER_ID " +
//					"         AND SUBSTR (MCI.CUSTOMER_ID, 1, 2) = '"+area+"' " +
//					"         AND SUBSTR (MCI.CUSTOMER_ID, 3, 2) = mci.CATEGORY_ID " +
//					"ORDER BY CATEGORY_ID " ;

			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		vatRebateDTO = new VatRebateITFFDTO();
        		
        		vatRebateDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		vatRebateDTO.setGasConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		vatRebateDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		vatRebateDTO.setRebateAmount(resultSet.getDouble("VAT_REBATE_AMOUNT"));
        		vatRebateDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		vatRebateDTO.setCustomerId(resultSet.getString("CUSTOMER_ID"));
        		
        		vatRebateList.add(vatRebateDTO);

   
        		
        		
        		
        	}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return vatRebateList;
	}
	
	private ArrayList<VatRebateITFFDTO> getIncomeTaxInfo(){
		ArrayList<VatRebateITFFDTO> incomeTaxList= new ArrayList<VatRebateITFFDTO>();
		try {
			String area=loggedInUser.getArea_id();
			
			
			String disconnection_info_sql="select mci.CUSTOMER_ID,mci.FULL_NAME,mci.CATEGORY_NAME,mci.CATEGORY_ID,mci.CATEGORY_TYPE,PAYABLE_AMOUNT,TAX_AMOUNT " +
					"from(select Customer_id, sum(PAYABLE_AMOUNT) PAYABLE_AMOUNT,sum(TAX_AMOUNT) TAX_AMOUNT from bill_collection_metered " +
					"WHERE TAX_AMOUNT is not null " +
					"AND TAX_AMOUNT<>0 " +
					"AND COLLECTION_DATE BETWEEN TO_DATE('"+from_date+"','dd-MM-yyyy') AND TO_DATE('"+to_date+"','dd-MM-yyyy') " +
					"group by customer_id) tab1, MVIEW_CUSTOMER_INFO mci " +
					"WHERE tab1.customer_id=mci.customer_id " +
					"AND SUBSTR(mci.CUSTOMER_ID,1,2)='"+area+"' " +
					"AND SUBSTR(mci.CUSTOMER_ID,3,2)=mci.CATEGORY_ID " +
					"ORDER BY CATEGORY_ID " ;






			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		vatRebateDTO = new VatRebateITFFDTO();
        		
        		vatRebateDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		vatRebateDTO.setCustomerId(resultSet.getString("CUSTOMER_ID"));
        		vatRebateDTO.setBillAmount(resultSet.getDouble("PAYABLE_AMOUNT"));
        		vatRebateDTO.setTaxAmount(resultSet.getDouble("TAX_AMOUNT"));
        		vatRebateDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		
        		incomeTaxList.add(vatRebateDTO);

   
        		
        		
        		
        	}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return incomeTaxList;
	}
	
	private ArrayList<VatRebateITFFDTO> getFreedomFighterInfo(){
		ArrayList<VatRebateITFFDTO> freedomFighterList= new ArrayList<VatRebateITFFDTO>();
		try {
			String area=loggedInUser.getArea_id();
			
			String[] fiscalYear=from_date.split("-");
			String firstPart=fiscalYear[2].toString()+fiscalYear[1].toString();
			String[] fiscalYear1=to_date.split("-");
			String sndtPart=fiscalYear1[2].toString()+fiscalYear1[1].toString();
			
			
			String disconnection_info_sql="select BAL.CUSTOMER_ID CUSTOMER_ID,CUSTOMER_NAME,sum(DEBIT) DEBIT,CUSTOMER_CATEGORY_NAME,sum(BILLED_AMOUNT) BILLED_AMOUNT,max(DOUBLE_BURNER_QNT) DOUBLE_BURNER_QNT from BANK_ACCOUNT_LEDGER BAL,BILL_COLLECTION_NON_METERED BCM,BILL_NON_METERED BM "+
				     "WHERE bm.bill_id=bcm.bill_id "+
				     "AND BCM.COLLECTION_ID=BAL.REF_ID "+
				     "AND bal.ACCOUNT_NO='1' "+
				     "AND substr(BAL.CUSTOMER_ID,1,2)='"+area+"' " +
				     "AND BILL_YEAR||lpad(bill_month,2,0) between "+firstPart+" and "+sndtPart+" " +
				     "group by bal.CUSTOMER_ID,CUSTOMER_NAME,CUSTOMER_CATEGORY_NAME,CUSTOMER_NAME ";

			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		vatRebateDTO = new VatRebateITFFDTO();
        		
        		vatRebateDTO.setCustomerName(resultSet.getString("CUSTOMER_NAME"));
        		vatRebateDTO.setCustomerId(resultSet.getString("CUSTOMER_ID"));
        		vatRebateDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		vatRebateDTO.setBurnerQty(resultSet.getInt("DOUBLE_BURNER_QNT"));
        		vatRebateDTO.setCustomerCategory(resultSet.getString("CUSTOMER_CATEGORY_NAME"));
        		vatRebateDTO.setVatAmount(resultSet.getDouble("DEBIT"));
        		
        		freedomFighterList.add(vatRebateDTO);
   
        		
        		
        		
        	}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return freedomFighterList;
	}	
	
	
	private ArrayList<VatRebateITFFDTO> getIndividualInfo(){
		ArrayList<VatRebateITFFDTO> freedomFighterList= new ArrayList<VatRebateITFFDTO>();
		try {
			String area=loggedInUser.getArea_id();
			
			
			String disconnection_info_sql="SELECT mci.CUSTOMER_ID, " +
					"         mci.FULL_NAME, " +
					"         mci.CATEGORY_NAME, " +
					"         mci.CATEGORY_ID, " +
					"         mci.CATEGORY_TYPE, " +
					"         PAYABLE_AMOUNT, " +
					"         TAX_AMOUNT, " +
					"         ACTUAL_GAS_CONSUMPTION, " +
					"         BILL_MONTH,BILL_YEAR,CHALAN_NO,to_char(CHALAN_DATE) CHALAN_DATE " +
					"    FROM (select BM.CUSTOMER_ID,bm.BILL_MONTH BILL_MONTH,BM.BILL_YEAR,bm.ACTUAL_GAS_CONSUMPTION ACTUAL_GAS_CONSUMPTION,bm.BILLED_AMOUNT PAYABLE_AMOUNT,bcm.TAX_AMOUNT TAX_AMOUNT,CHALAN_NO,CHALAN_DATE from bill_metered bm,bill_collection_metered bcm " +
					"Where BM.BILL_ID=BCM.BILL_ID " +
					"AND ISSUE_DATE BETWEEN TO_DATE('"+from_date+"','dd-mm-yyyy') AND TO_DATE('"+to_date+"','dd-mm-yyyy') " +
					"AND bm.CUSTOMER_ID='"+customer_id+"') tab1, " +
					"         MVIEW_CUSTOMER_INFO mci " +
					"   WHERE     tab1.customer_id = mci.customer_id " +
					"         AND SUBSTR (mci.CUSTOMER_ID, 1, 2) = '"+area+"' " +
					"ORDER BY Bill_year,BILL_MONTH asc ";






			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		vatRebateDTO = new VatRebateITFFDTO();
        		
        		vatRebateDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		vatRebateDTO.setCustomerId(resultSet.getString("CUSTOMER_ID"));
        		vatRebateDTO.setBillAmount(resultSet.getDouble("PAYABLE_AMOUNT"));
        		vatRebateDTO.setTaxAmount(resultSet.getDouble("TAX_AMOUNT"));
        		vatRebateDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		vatRebateDTO.setMonth(resultSet.getString("BILL_MONTH"));
        		vatRebateDTO.setYear(resultSet.getString("BILL_YEAR"));
        		vatRebateDTO.setGasConsumption(resultSet.getDouble("ACTUAL_GAS_CONSUMPTION"));
        		vatRebateDTO.setChalanNo(resultSet.getString("CHALAN_NO"));
        		vatRebateDTO.setChalanDate(resultSet.getString("CHALAN_DATE"));
        		
        		freedomFighterList.add(vatRebateDTO);

   
        		
        		
        		
        	}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return freedomFighterList;
	}	
	

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	

}

