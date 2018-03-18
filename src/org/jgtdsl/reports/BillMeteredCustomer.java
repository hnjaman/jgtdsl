package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.MBillDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.models.BillingService;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class BillMeteredCustomer extends BaseAction implements ServletContextAware{

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
	private int serial= 0;
	public static int textDiff=597;
	private boolean water_mark=false;
	
	
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
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
	
	public String downloadBill() throws Exception
	{	
		int fSize=11;
		String file_customer_id="";
		String file_bill_month="";
		String file_bill_year="";
		int counter=0;
		
		UserDTO loggedInUser=(UserDTO)session.get("user");
		if((area_id==null || area_id.equalsIgnoreCase("")) && loggedInUser!=null)
			area_id=loggedInUser.getArea_id();
		
		BillingService bs=new BillingService();
		ArrayList<MBillDTO> billList=bs.getBill(this.bill_id,this.customer_category,this.area_id,this.customer_id,this.bill_month,this.bill_year,this.download_type);
		HttpServletResponse response = ServletActionContext.getResponse();
		PdfReader reader =null;
		ByteArrayOutputStream certificate = null;
		List<PdfReader> readers = new ArrayList<PdfReader>();
		String printType="Single";
		//String realPath=servlet.getRealPath("/resources/staticPdf/Bill_Meter.pdf");   //For double page billing print
		String realPath=servlet.getRealPath("/resources/staticPdf/SinglePageBilling.pdf");  //For single page billing print
		
		reader = new PdfReader(new FileInputStream(realPath));
		//certificate = new ByteArrayOutputStream();
		PdfStamper stamp =null;//= new PdfStamper(reader,certificate);
		
		
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//Rectangle one = new Rectangle(1202.40f,828.00f); // For double page
		//Rectangle one = new Rectangle(601.40f,830.00f); // For Single Page
		
		Rectangle one = new Rectangle(605,648);
		one.setBorder(1);
		document.setPageSize(one);
		//document.setPageSize(PageSize.A4);
		//document.setMargins(10, 10, 25, 30);
		
		
		
		
		
		DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		DecimalFormat factor_format=new DecimalFormat("##########0.000");
		DecimalFormat reading_format = new DecimalFormat("##########0");
		try
		{
			if(billList.size()>0)
			{
				if(billList.get(0).getBill_status()==null ||billList.get(0).getBill_status().getId()==0 )
					water_mark=true;
			}
			
			for(MBillDTO bill : billList){	
				//System.out.println(bill.getCustomer_name());
				
				if(counter==0){
					file_customer_id=bill.getCustomer_id();
					file_bill_month=bill.getBill_month()+"";
					file_bill_year=bill.getBill_year()+"";
				}
			counter++;
			PdfContentByte over;
			BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);		     
			BaseFont bfBold = BaseFont.createFont(BaseFont.TIMES_BOLD,BaseFont.WINANSI,BaseFont.EMBEDDED);
			
			reader = new PdfReader(new FileInputStream(realPath));
			certificate = new ByteArrayOutputStream();
			stamp = new PdfStamper(reader,certificate);
			over = stamp.getOverContent(1);
					
			over.beginText();
			
			
			
			over.setFontAndSize(bf, fSize);
			over.setTextMatrix(315, 572);//388
			over.showText(bill.getArea_name());	
			
			over.setTextMatrix(82, 536);
			over.showText(bill.getBill_month_name()+", "+bill.getBill_year());
			
//			over.setFontAndSize(bf, 10);
//			over.setTextMatrix(408, 539);
//			over.showText(bill.getBill_id());
			over.setFontAndSize(bfBold, 10);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,bill.getBill_id(),403, 534,0);

			
			over.setFontAndSize(bf, 10);
			over.setTextMatrix(202, 536);
			over.showText(bill.getCustomer_category_name());
			
			
			//over.setTextMatrix(93, 496);
			//over.showText(bill.getCustomer_id());
			over.setFontAndSize(bfBold, 12);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,bill.getCustomer_id(),82, 493,0);
			
			//over.setTextMatrix(93, 477);
			//over.showText(bill.getCustomer_name());
			over.setFontAndSize(bf, 10);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,bill.getCustomer_name(),82, 474,0);
			
			/*over.setTextMatrix(90, 460);//(90,460)			
			over.showText(bill.getAddress()==null?"":bill.getAddress());
			System.out.println(bill.getAddress());
			System.out.println(bill.getAddress().length());
			 if(bill.getAddress().length()>48){
				 
			 }*/
			//bindu
			over.setFontAndSize(bf, 9);
			 String hsi = bill.getAddress();
	            if(bill.getAddress()!=null)
	                hsi = bill.getAddress().replaceAll("&#x26;", "&");
	            int size= 50;
	            if (hsi!=null && hsi.length() > size)
	            {  String[] s1;
	                s1 = spitSrting(hsi, size);
	                over.setTextMatrix(82, 444);
	                over.showText(s1[1]);

	                if (s1[1].length() <= size)
	                {
	                    over.setTextMatrix(82, 456);
	                    over.showText(s1[0]);
	                } else
	                {
	                    s1 = spitSrting(s1[1], size);
	                    over.setTextMatrix(82, 456);
	                    over.showText(s1[0]);
	                    if (s1[1].length() <= size)
	                    {
	                        over.setTextMatrix(82, 444);
	                        over.showText(s1[1]);
	                    } else
	                    {
	                        s1 = spitSrting(s1[1], size);
	                        over.setTextMatrix(82, 456);
	                        over.showText(s1[0]);
	                        over.setTextMatrix(82, 444);
	                        if(s1[1].length() > size) over.showText(s1[1].substring(size));
	                        else
	                        over.showText(s1[1]);
	                    }
	                }
	            } else
	            {
	                over.setTextMatrix(82, 456);
	                over.showText(hsi);
	            }
			 //bindu
			
			over.setTextMatrix(367, 503);
			over.showText("VAT NO.");
			
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT, "VAT NO", 380, 505, 0);
			
			over.setTextMatrix(367, 486);//(x,485)
			over.showText(bill.getArea_id());
			
			over.setTextMatrix(367, 468);
			over.showText(bill.getIssue_date());
			
			over.setFontAndSize(bfBold, 10);
			over.setTextMatrix(367, 447);
			over.showText(bill.getLast_pay_date_wo_sc());
			
			over.setFontAndSize(bf, 10);
			over.setTextMatrix(367, 425);
			over.showText(bill.getLast_pay_date_w_sc());
			
			
			over.setFontAndSize(bfBold, 10);
			over.setTextMatrix(367, 397);
			over.showText(bill.getLast_disconn_reconn_date()==null?"":bill.getLast_disconn_reconn_date());
			

			//over.setTextMatrix(140, 290);
			//over.showText(consumption_format.format(bill.getBilled_consumption()));
			over.setFontAndSize(bf, 9);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,consumption_format.format(bill.getBilled_consumption()),127, 291,0);//289
			
			//over.setTextMatrix(140, 265);
			//over.showText(consumption_format.format(bill.getOther_consumption()));
			over.setFontAndSize(bf, 9);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,consumption_format.format(bill.getOther_consumption()),127, 267,0);//264
			
			//over.setTextMatrix(140, 245);
			//over.showText(consumption_format.format(bill.getMixed_consumption()));
			over.setFontAndSize(bf, 9);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,consumption_format.format(bill.getMixed_consumption()),127, 247,0);//247
			
			/*over.setTextMatrix(140, 225);
			over.showText(consumption_format.format(bill.getMinimum_load()));*/
			over.setFontAndSize(bf, 9);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,consumption_format.format(bill.getMinimum_load()),127, 226,0);//227
			
			/*over.setTextMatrix(145, 175);
			over.setFontAndSize(bfBold, 9);
			over.showText(bill.getAmount_in_word()+"");*/
			over.setFontAndSize(bfBold, 10);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,bill.getAmount_in_word(),114, 177,0);
			
			
			
			
			//gas bill
			//over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getGas_bill()),445, 360,0);
			//minimum bill
			over.setFontAndSize(bf, 9);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getMin_load_bill()),442, 343,0);
			//vat
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getGovtMarginDTO().getTotal_amount()),442, 310,0);
			//meter rent
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getMeter_rent()),442, 292,0);//290
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,(bill.getPbMarginDTO().getBill_id()),410, 538,0);
			
			
			
			
			
			
			
			//adjustment
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getAdjustment()+bill.getPbMarginDTO().getHhv_nhv_bill()),442, 256,0);//345
			//late fee
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getSurcharge_amount()),442, 239,0);//240
			//others
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getOthers()),442, 221,0);//220
			over.setFontAndSize(bfBold, 11);
			//total bill to pay
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPayable_amount()),442, 202,0);//200
			
			//average bill 
			double gas_bill=0;
			double average_bill=0;
			int has_average_bill=0;
			ArrayList<String> curr_rea_list= new ArrayList<String>();
			ArrayList<String> prev_rea_list= new ArrayList<String>();
			ArrayList<String> press_list= new ArrayList<String>();
			ArrayList<String> rate_list= new ArrayList<String>();
			
			ArrayList<MeterReadingDTO> readingList=bill.getReadingList();
			//temporary solution
			gas_bill += readingList.get(0).getIndividual_gas_bill();
			
			
			double vgas=bill.getPayable_amount()-gas_bill-bill.getGovtMarginDTO().getTotal_amount()-bill.getPbMarginDTO().getMin_load_bill()-bill.getPbMarginDTO().getMeter_rent()
					-bill.getPbMarginDTO().getSurcharge_amount()-bill.getPbMarginDTO().getAdjustment()-bill.getPbMarginDTO().getHhv_nhv_bill()-bill.getPbMarginDTO().getOthers();
			
			gas_bill=gas_bill+vgas;
			
		
			
			double totalBill=gas_bill+bill.getPbMarginDTO().getMin_load_bill()+bill.getGovtMarginDTO().getTotal_amount()+bill.getPbMarginDTO().getMeter_rent();
			over.setFontAndSize(bf, 9);
			//total bill
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(totalBill),442, 273,0);//270
			
			
			
			
			
			
			
			for(int i=0;i<readingList.size();i++)
			{
				//not necessary and not of any use
//				if(readingList.get(i).getReading_purpose_str().equals("2"))
//				{
//					gas_bill += readingList.get(i).getIndividual_gas_bill();
//				}
//				else if(readingList.get(i).getReading_purpose_str().equals("3"))
//				{
//					average_bill += readingList.get(i).getIndividual_gas_bill();
//					has_average_bill=1;
//				}
				
				over.setFontAndSize(bf, 9);	
				/*over.setTextMatrix(140,360-7*i); over.showText(reading_format.format(readingList.get(i).getCurr_reading())+"");
				over.setTextMatrix(140,340-7*i); over.showText(reading_format.format(readingList.get(i).getPrev_reading())+"");
				over.setTextMatrix(140,320-7*i); over.showText(factor_format.format(readingList.get(i).getPressure_factor())+"");
				over.setTextMatrix(140,205-7*i); over.showText(taka_format.format(readingList.get(i).getRate())+"");*/
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT,reading_format.format(readingList.get(i).getCurr_reading()),180, 445,0);//361
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT,reading_format.format(readingList.get(i).getPrev_reading()),180, 425,0);//340
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT,factor_format.format(readingList.get(i).getPressure_factor()),180, 404,0);//320
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT,taka_format.format(readingList.get(i).getRate()),180, 292,0);//207
				
				String prev_rea= reading_format.format(readingList.get(i).getPrev_reading());
				String curr_rea= reading_format.format(readingList.get(i).getCurr_reading());
				String pressure_factor= factor_format.format(readingList.get(i).getPressure_factor());
				String rate= taka_format.format(readingList.get(i).getRate());
				
				//String[] rate_per_m_array;
				
				prev_rea_list.add(prev_rea);
				curr_rea_list.add(curr_rea);
				press_list.add(pressure_factor);
				rate_list.add(rate);
				
			}
			//prev reading
			if(prev_rea_list.size()>1){							
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,prev_rea_list.get(0),127, 340,0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,prev_rea_list.get(1),207, 340,0);				
			}else{
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(prev_rea_list.get(0)),127, 340,0);
			}
			//current reading
			if(curr_rea_list.size()>1){							
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,curr_rea_list.get(0),127, 359,0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,curr_rea_list.get(1),207, 359,0);				
			}else{
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(curr_rea_list.get(0)),127, 359,0);
			}
			//pressure factor
			
			if(press_list.size()>1){							
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,press_list.get(0),127, 319,0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,press_list.get(1),207, 319,0);				
			}else{
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(press_list.get(0)),127, 319,0);
			}
			//rate per cubic meter
			if(press_list.size()>1){							
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,rate_list.get(0),127, 205,0);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,rate_list.get(1),207, 205,0);				
			}else{
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(rate_list.get(0)),127, 205,0);
			}
			
			//reducing average_bill amount from gas bill. 
			//not sure if this is the right process
//			if(has_average_bill==1){
//				gas_bill=gas_bill-average_bill;
//			}
			
			
			over.setFontAndSize(bf, 9);
			//printing gas bill
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(gas_bill),442, 362,0);
		
			//printing average bill
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(average_bill),442,326 ,0);

			
			
			
			
//			over.setTextMatrix(126, 600);
//			over.showText(bill.getPhone()==null?"":bill.getPhone());	
			
//			if(printType.equals("double")){
//				over.setTextMatrix(508+textDiff, 747);
//				over.showText(bill.getInvoice_no());
//				over.setTextMatrix(508+textDiff, 728);
//				over.showText(bill.getIssue_date());
//				over.setTextMatrix(508+textDiff, 710);
//				over.showText(bill.getLast_pay_date_wo_sc());
//				over.setTextMatrix(508+textDiff, 690);
//				over.showText(bill.getLast_pay_date_w_sc());
//				over.setTextMatrix(508+textDiff, 690);
//				over.showText(bill.getLast_disconn_reconn_date()==null?"":bill.getLast_disconn_reconn_date());
//				over.setTextMatrix(508+textDiff, 650);
//				over.showText(bill.getMonthly_contractual_load()+"");
//				over.setTextMatrix(110+textDiff, 745);
//				over.showText(bill.getBill_month_name()+", "+bill.getBill_year());
//				over.setTextMatrix(110+textDiff, 725);
//				over.showText(bill.getCustomer_id());
//				over.setTextMatrix(100+textDiff, 707);
//				over.showText(bill.getCustomer_name());
//				over.setTextMatrix(110+textDiff, 690);
//				over.showText(bill.getProprietor_name()==null?"":bill.getProprietor_name());
//
//			}


//			ArrayList<MeterReadingDTO> readingList=bill.getReadingList();
//			for(int i=0;i<readingList.size();i++)
//			{
//				
////				over.setFontAndSize(bf, 8);
////				over.setTextMatrix(20,540-15*i); over.showText(readingList.get(i).getMeter_sl_no());
//				
////				over.setFontAndSize(bf, fSize);
////				over.setTextMatrix(102,540-15*i); over.showText((readingList.get(i).getReading_purpose_str().equals("3")||readingList.get(i).getReading_purpose_str().equals("8")||readingList.get(i).getReading_purpose_str().equals("6"))?readingList.get(i).getReading_purpose_name()+"  "+readingList.get(i).getActual_consumption():reading_format.format(readingList.get(i).getCurr_reading())+"");
////				over.setTextMatrix(158,540-15*i); over.showText((readingList.get(i).getReading_purpose_str().equals("3")||readingList.get(i).getReading_purpose_str().equals("8")||readingList.get(i).getReading_purpose_str().equals("6"))?"":reading_format.format(readingList.get(i).getPrev_reading())+"");
////				
////				over.setTextMatrix(220,540-15*i); over.showText((readingList.get(i).getReading_purpose_str().equals("3")||readingList.get(i).getReading_purpose_str().equals("8")||readingList.get(i).getReading_purpose_str().equals("6"))?"":reading_format.format(readingList.get(i).getDifference())+"");
////				over.setTextMatrix(270,540-15*i); over.showText(readingList.get(i).getPressure()+"");
////				over.setTextMatrix(330,540-15*i); over.showText(readingList.get(i).getTemperature()+"");
////				
////				over.setTextMatrix(375,540-15*i); over.showText(factor_format.format(readingList.get(i).getPressure_factor())+"");
////				over.setTextMatrix(435,540-15*i); over.showText(factor_format.format(readingList.get(i).getTemperature_factor())+"");
////				over.setTextMatrix(495,540-15*i); over.showText(readingList.get(i).getHhv_nhv()+"");
////				over.setTextMatrix(540,540-15*i); over.showText(readingList.get(i).getRate()+"");
//				
//				if(printType.equals("double")){
//				over.setTextMatrix(15+textDiff,560+15*i); over.showText(readingList.get(i).getMeter_sl_no());				
//				over.setTextMatrix(90+textDiff,560+15*i); over.showText(readingList.get(i).getCurr_reading()+"");
//				over.setTextMatrix(140+textDiff,560+15*i); over.showText(readingList.get(i).getPrev_reading()+"");
//				
//				over.setTextMatrix(200+textDiff,560+15*i); over.showText(readingList.get(i).getDifference()+"");
//				over.setTextMatrix(260+textDiff,560+15*i); over.showText(readingList.get(i).getPressure()+"");
//				over.setTextMatrix(310+textDiff,560+15*i); over.showText(readingList.get(i).getTemperature()+"");
//				
//				over.setTextMatrix(350+textDiff,560+15*i); over.showText(factor_format.format(readingList.get(i).getPressure_factor())+"");
//				over.setTextMatrix(410+textDiff,560+15*i); over.showText(readingList.get(i).getTemperature_factor()+"");
//				over.setTextMatrix(470+textDiff,560+15*i); over.showText(readingList.get(i).getHhv_nhv()+"");
//				over.setTextMatrix(530+textDiff,560+15*i); over.showText(readingList.get(i).getRate()+"");
//				}
//				
//			}
			
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getMinimum_load()),285, 462,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getActual_gas_consumption()),285, 439,0);				
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getOther_consumption()),285, 416,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getMixed_consumption()),285, 393,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getBilled_consumption()),285, 370,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getHhv_nhv_adj_qnt()),285, 348,0);

			
			//Govt. Margin
//			
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getGovtMarginDTO().getVat_amount()),285, 282,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getGovtMarginDTO().getSd_amount()),285, 258,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getGovtMarginDTO().getOthers_amount()),285, 238,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getGovtMarginDTO().getTotal_amount()),285, 215,0);
//			
			//Petro Bangla Margin			
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getGas_bill()),562, 462,0);				
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getMin_load_bill()),562, 439,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getMeter_rent()),562, 416,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getHhv_nhv_bill()),562, 394,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getAdjustment()),562, 372,0);
//			over.setFontAndSize(bfBold, 7);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,bill.getPbMarginDTO().getAdjustment_comments()==null?"":"("+bill.getPbMarginDTO().getAdjustment_comments()+")",562, 362,0);
//			over.setFontAndSize(bf, fSize);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getSurcharge_percentage()),445, 350,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getSurcharge_amount()),562, 350,0);
//			
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getOthers()),562, 327,0);
//			over.setFontAndSize(bfBold, 7);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,bill.getPbMarginDTO().getOther_comments()==null?"":"("+bill.getPbMarginDTO().getOther_comments().substring(0,bill.getPbMarginDTO().getOther_comments().length()-1)+")",562, 318,0);
//			over.setFontAndSize(bf, fSize);
//			
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getVat_rebate_percent()),434, 307,0);
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getVat_rebate_amount()),562, 307,0);
//			
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getTotal_amount()),562, 286,0);
//			
//			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPayable_amount()),562, 227,0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Taka "+bill.getAmount_in_word(),84, 191,0);
			
			/*
			over.showText(bill.getPbMarginDTO().getOthers()+"");					
			over.setTextMatrix(432, 314);
			over.showText(bill.getPbMarginDTO().getVat_rebate_percent()+"");			
			over.setTextMatrix(525, 314);
			over.showText(bill.getPbMarginDTO().getVat_rebate_amount()+"");			
			over.setTextMatrix(525, 295);
			over.showText(bill.getPbMarginDTO().getTotal_amount()+"");			
			over.setTextMatrix(525, 238);
			//over.showText(bill.getGovtMarginDTO().getTotal_amount()+bill.getPbMarginDTO().getTotal_amount()+"");
			over.showText(bill.()+"");						
			//Total Amount In Word
			over.setTextMatrix(86, 201);
			over.showText(bill.getAmount_in_word()+"");
			*/
			if(printType.equals("double")){
				over.setTextMatrix(240, 487);
				over.showText(bill.getMinimum_load()+"");
				over.setTextMatrix(240+textDiff, 468);
				over.showText(bill.getActual_gas_consumption()+"");
				over.setTextMatrix(240+textDiff, 450);
				over.showText(bill.getOther_consumption()+"");
				over.setTextMatrix(240+textDiff, 430);
				over.showText(bill.getMixed_consumption()+"");
				over.setTextMatrix(240+textDiff, 413);
				over.showText(bill.getBilled_consumption()+"");
				over.setTextMatrix(240+textDiff, 397);
				over.showText(bill.getHhv_nhv_adj_qnt()+"");
				over.setTextMatrix(240+textDiff, 335);
				over.showText(bill.getGovtMarginDTO().getVat_amount()+"vat");
				over.setTextMatrix(240+textDiff, 318);
				over.showText(bill.getGovtMarginDTO().getSd_amount()+"");
				over.setTextMatrix(240+textDiff, 302);
				over.showText(bill.getGovtMarginDTO().getOthers_amount()+"");
				over.setTextMatrix(240+textDiff, 282);
				over.showText(bill.getGovtMarginDTO().getTotal_amount()+"");
				over.setTextMatrix(525+textDiff, 485);
				over.showText(bill.getPbMarginDTO().getGas_bill()+"");
				over.setTextMatrix(525+textDiff, 468);
				over.showText(bill.getPbMarginDTO().getMin_load_bill()+"");
				over.setTextMatrix(525+textDiff, 452);
				over.showText(bill.getPbMarginDTO().getMeter_rent()+"");
				over.setTextMatrix(525+textDiff, 432);
				over.showText(bill.getPbMarginDTO().getHhv_nhv_bill()+"");
				over.setTextMatrix(525+textDiff, 413);
				over.showText(bill.getPbMarginDTO().getAdjustment()+"");
				over.setTextMatrix(430+textDiff, 397);
				//over.showText(bill.getPbMarginDTO().getSurcharge_percentage()+"");
				over.setTextMatrix(525+textDiff, 397);
				over.showText(bill.getPbMarginDTO().getSurcharge_amount()+"");
				over.setTextMatrix(525+textDiff, 378);
				over.showText(bill.getPbMarginDTO().getOthers()+"");
				over.setTextMatrix(430+textDiff, 360);
				//over.showText(bill.getPbMarginDTO().getVat_rebate_percent()+"");
				over.setTextMatrix(525+textDiff, 360);
				over.showText(bill.getPbMarginDTO().getVat_rebate_amount()+"");
				over.setTextMatrix(525+textDiff, 342);
				over.showText(bill.getPbMarginDTO().getTotal_amount()+"");
				over.setTextMatrix(525+textDiff, 303);
				//over.showText(bill.getGovtMarginDTO().getTotal_amount()+bill.getPbMarginDTO().getTotal_amount()+"");
				over.showText(bill.getPayable_amount()+"");
				over.setTextMatrix(105+textDiff, 250);
				over.showText(bill.getAmount_in_word()+"");
				
				
			}
			
			
			ColumnText ct = new ColumnText(over);						
			//ct.setSimpleColumn(new Phrase(new Chunk(bill.getAddress()==null?"":bill.getAddress(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
			  //                   122, 669, 300, 36, 12, Element.ALIGN_LEFT | Element.ALIGN_TOP);
								//top-leftX,top-leftY,?,?,lineHeight			
			ct.go(); 
			
			ct = new ColumnText(over);						
			ct.setSimpleColumn(new Phrase(new Chunk(bill.getCustomer_name()!=null? bill.getCustomer_name():(bill.getProprietor_name()==null?"":bill.getProprietor_name()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
			                     122, 716, 400, 36, 12, Element.ALIGN_LEFT | Element.ALIGN_TOP);
								//top-leftX,top-leftY,?,?,lineHeight			
			ct.go(); 
			
			ct = new ColumnText(over);						
			ct.setSimpleColumn(new Phrase(new Chunk(bill.getProprietor_name()==null?"":bill.getProprietor_name(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
			                     122, 695, 350, 36, 12, Element.ALIGN_LEFT | Element.ALIGN_TOP);
								//top-leftX,top-leftY,?,?,lineHeight			
			ct.go(); 
			
			
			/* for double
			ColumnText ct1 = new ColumnText(over);						
			ct1.setSimpleColumn(new Phrase(new Chunk(bill.getAddress(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
			                     860, 683, 700, 400, 12, Element.ALIGN_LEFT | Element.ALIGN_TOP);
								//top-leftX,top-leftY,?,?,lineHeight			
			ct1.go(); 
			*/
			
			  
//			over.setFontAndSize(bf, 15);
//			over.setTextMatrix(600, 607);
//			over.showText(bill.getInvoice_no());
//			
			
			
			
			
			over.endText();
			
			
			
			stamp.close();
			readers.add(new PdfReader(certificate.toByteArray()));
			
			
			}
			
						
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		if(readers.size()>0)
		{
			PdfWriter writer = PdfWriter.getInstance(document, out);
			if(water_mark==true)
				writer.setPageEvent(new Watermark());
			document.open();
			
			PdfContentByte cb = writer.getDirectContent();
			PdfReader pdfReader = null;
			PdfImportedPage page;
			
			for(int k=0;k<readers.size();k++)
			{
				document.newPage();
				pdfReader = readers.get(k);
				page = writer.getImportedPage(pdfReader, 1);
				cb.addTemplate(page, 0, 0);
			}
			

			document.close();
			ReportUtil rptUtil = new ReportUtil();
			
			if(billList.size()>1)
				file_customer_id="";
			
			rptUtil.downloadPdf(out, response,"BILL"+((bill_id==null || bill_id.equalsIgnoreCase(""))?"":"-"+bill_id)+"-"+file_bill_month+"-"+file_bill_year+((file_customer_id==null || file_customer_id.equalsIgnoreCase(""))?"":"-"+file_customer_id)+".pdf");
			document=null;	
					
		}
		
		
		return null;	
		
	}
	/////////////////bill preview////////////////
	private String report_for;
	
	static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
	static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
	static Font font9 = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
	static Font font9B = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	static DecimalFormat consumption_format = new DecimalFormat("##########0.000");	
	
	public String PreviewBill() throws Exception{
		
		download_type="prev";
		
		String fileName = "Preview_Bill_Info.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(0,0,20,50);
		
		try {	
			
			BillingService bs=new BillingService();
			ArrayList<MBillDTO> billList=bs.getBillprev(this.customer_category,this.area_id,this.bill_month,this.bill_year,this.download_type, this.report_for);
			
			ReportFormat Event = new ReportFormat(getServletContext());
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			writer.setPageEvent(Event);
			PdfPCell pcell = null;
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
		    Rectangle page = document.getPageSize();
		    /*headerTable.setTotalWidth(page.getWidth());
			float a=((page.getWidth()*15)/100)/2;
			float b=((page.getWidth()*30)/100)/2;
				
			headerTable.setWidths(new float[] {
				a,b,a
			});*/
			headerTable.setWidthPercentage(97);
			Paragraph pd2 =new Paragraph();
			Chunk cd1 = new Chunk("Bill Month: ", ReportUtil.f11);
			Chunk cd2= new Chunk(String.valueOf(Month.values()[Integer.valueOf(bill_month)-1].getLabel())+", "+ getBill_year(),ReportUtil.f12B);
			pd2.add(cd1);
			pd2.add(cd2);
			pcell= new PdfPCell(pd2);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPaddingTop(10);
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			
			
			String realPath = servlet.getRealPath("/resources/images/logo/JG.png"); 	// image path
			Image img = Image.getInstance(realPath);
						
            	//img.scaleToFit(10f, 200f);
            	//img.scalePercent(200f);
				img.scaleAbsolute(28f, 31f);
				//img.setAbsolutePosition(145f, 780f);		
            	img.setAbsolutePosition(270f, 542f);		// rotate
            
	        document.add(img);		
			
			PdfPTable mTable=new PdfPTable(1);			
			//mTable.setWidths(new float[]{b});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			
			
			if(getReport_for().equals("area_wise")){
				
				Chunk chunk1 = new Chunk("REGIONAL OFFICE : ",font2);
				Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(getArea_id())-1]),font3);
				Paragraph p = new Paragraph(); 
				p.add(chunk1);
				p.add(chunk2);
				pcell=new PdfPCell(p);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setBorder(Rectangle.NO_BORDER);
				mTable.addCell(pcell);
				
				
				
			}else if(getReport_for().equals("category_wise")){
				Chunk chunk1 = new Chunk("Preview of Monthly Bill for ",font2);
				
				Chunk chunk2 = new Chunk(String.valueOf(getCategory_name()),font1);
				
				Chunk chunk3 = new Chunk(" "+" Customers",font2);
				Paragraph p = new Paragraph(); 
				p.add(chunk1);
				p.add(chunk2);
				p.add(chunk3);
				pcell=new PdfPCell(p);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setBorder(Rectangle.NO_BORDER);
				mTable.addCell(pcell);				
			}
			
			
			
			
			
			pcell=new PdfPCell(new Paragraph(" ", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);				
						
			pcell=new PdfPCell(mTable);
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			
			document.add(headerTable);				
			
						
			////////////////DATA TABLE///////////////////////
			
			pcell=new PdfPCell(mTable);
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			
			PdfPTable datatable = new PdfPTable(15);
			datatable.setWidthPercentage(97);
			datatable.setWidths(new float[] {44,50,40,40,26,40,40,40,40,40,40,40,40,30,40});
			
			int ListSize = billList.size();
			String prev_category="";
			String cur_category="";
			double average_bill= 0;
			//int category_id;
			//String cat_name;
				
				for(MBillDTO bill : billList){
					
					ArrayList<MeterReadingDTO> readingList=bill.getReadingList();
					
					cur_category=bill.getCustomer_id().substring(2,4);
					if(!cur_category.equals(prev_category)){
					    //category_id=Integer.parseInt(cur_category);
					    //cat_name=bill.getCustomer_category_name();
						for(MeterReadingDTO x:readingList){
							
							pcell=new PdfPCell(new Paragraph("Bill Rate: "+taka_format.format(x.getRate()),font9B));
							pcell.setColspan(2);
							pcell.setPaddingTop(5);
							//pcell.setRowspan(3);
							//pcell.setPadding(10);
							pcell.setBorder(0);
							pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
							datatable.addCell(pcell);
						}
						
						
						pcell=new PdfPCell(new Paragraph("Customer Type: "+bill.getCustomer_category_name(),font9B));
						pcell.setColspan(7);
						//pcell.setRowspan(3);
						pcell.setPadding(10);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						
						pcell=new PdfPCell(new Paragraph("Issue Date: "+bill.getIssue_date(),font9B));
						pcell.setColspan(6);
						//pcell.setRowspan(3);
						pcell.setPadding(10);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Last Date of payment: "+bill.getLast_pay_date_wo_sc(),font9B));
						pcell.setColspan(15);
						//pcell.setRowspan(3);
						pcell.setPaddingBottom(5);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);	
						
						/////////////dataTable Head//////////////////
						pcell=new PdfPCell(new Paragraph(" ",font9B));
						pcell.setColspan(15);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Customer Code",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Customer Name",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Previous Reading",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Present Reading",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Pr. Fact.",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Consumption",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Min. Load",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Gas Bill",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Min. Bill",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Avg. Bill against D/O",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Total Bill",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Meter+CMS Rent",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Surcharge",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Adjust",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Total Bill to Pay",font9B));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);																
						
						
						//////////////datatable head ends/////////////////////
						
					}
					
					prev_category=cur_category;
					cur_category= bill.getCustomer_id().substring(2,4);
					for(int i=0;i<readingList.size();i++){
						
						pcell=new PdfPCell(new Paragraph(bill.getCustomer_id(),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph(bill.getCustomer_name(),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						datatable.addCell(pcell);			
																
						pcell=new PdfPCell(new Paragraph(taka_format.format(readingList.get(i).getPrev_reading()),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph(taka_format.format(readingList.get(i).getCurr_reading()),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph(taka_format.format(readingList.get(i).getPressure_factor()),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);			
						
					
						pcell=new PdfPCell(new Paragraph(consumption_format.format(bill.getBilled_consumption()),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);					
						
						pcell=new PdfPCell(new Paragraph(taka_format.format(bill.getMinimum_load()),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph(taka_format.format(bill.getPbMarginDTO().getGas_bill()),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph(taka_format.format(bill.getPbMarginDTO().getMin_load_bill()),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);
					
						
						if(readingList.get(i).getReading_purpose_str().equals("3"))
						{
							average_bill += readingList.get(i).getIndividual_gas_bill();
						}						
						
						pcell=new PdfPCell(new Paragraph(taka_format.format(average_bill),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);
					
					
					
						pcell=new PdfPCell(new Paragraph(taka_format.format(bill.getPbMarginDTO().getTotal_amount()),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph(taka_format.format(bill.getPbMarginDTO().getMeter_rent()),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph(taka_format.format(bill.getPbMarginDTO().getSurcharge_amount()),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph(taka_format.format(bill.getPbMarginDTO().getAdjustment()),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);						
						
						double gas_bill=0;
		
						gas_bill += readingList.get(0).getIndividual_gas_bill();
						
						
						double vgas=bill.getPayable_amount()-gas_bill-bill.getGovtMarginDTO().getTotal_amount()-bill.getPbMarginDTO().getMin_load_bill()-bill.getPbMarginDTO().getMeter_rent()
								-bill.getPbMarginDTO().getSurcharge_amount()-bill.getPbMarginDTO().getAdjustment()-bill.getPbMarginDTO().getHhv_nhv_bill()-bill.getPbMarginDTO().getOthers();
						
						gas_bill=gas_bill+vgas;		
					
						
						double totalBill=gas_bill+bill.getPbMarginDTO().getMin_load_bill()+bill.getGovtMarginDTO().getTotal_amount()+bill.getPbMarginDTO().getMeter_rent();
						
						pcell=new PdfPCell(new Paragraph(taka_format.format(totalBill),font9));
						pcell.setRowspan(1);
						pcell.setPadding(5);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable.addCell(pcell);											
					}
				}
					
			document.add(datatable);								
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(), fileName);
			document = null;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;		
	}
	/////////////////////bill preview ends//////////
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String billId) {
		bill_id = billId;
	}
	public String getCustomer_category() {
		return customer_category;
	}
	public void setCustomer_category(String customerCategory) {
		customer_category = customerCategory;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String areaId) {
		area_id = areaId;
	}
	public String getDownload_type() {
		return download_type;
	}
	public void setDownload_type(String downloadType) {
		download_type = downloadType;
	}	
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getBill_month() {
		return bill_month;
	}
	public void setBill_month(String billMonth) {
		bill_month = billMonth;
	}
	public String getBill_year() {
		return bill_year;
	}
	public void setBill_year(String billYear) {
		bill_year = billYear;
	}
	public String getReport_for() {
		return report_for;
	}
	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}

	public String getBill_for() {
		return bill_for;
	}
	public void setBill_for(String billFor) {
		bill_for = billFor;
	}
	
	public String[] spitSrting(String base, int size)
    {
        char[] separator =
        { ' ', '.', ',', ';',':' };
        boolean separatorfound = false;
        String s1[] = new String[2];
        outer: for (int j = size; j >= 0; j--)
        {
            for (int k = 0; k < separator.length; k++)
            {
                if (separator[k] == base.charAt(j))
                {
                    s1[0] = base.substring(0, j + 1);
                    s1[1] = base.substring(j + 1, base.length());
                    separatorfound = true;
                    break outer;
                }
            }

        }
        if(!separatorfound)
        {
            int x = 0;
            s1[0] =  base.substring(0,size-10);
            s1[1] = base.substring(size-10,base.length());
        }
        return s1;
    }


	public class Watermark extends PdfPageEventHelper {
		 
        protected Phrase watermark = new Phrase("SAMPLE BILL (NOT FOR PRINT)", FontFactory.getFont(FontFactory.HELVETICA, 45, BaseColor.LIGHT_GRAY));
 
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte canvas = writer.getDirectContentUnder();
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 298, 421, 45);
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, textDiff+298, 421, 45);
            
        }
    }
}
