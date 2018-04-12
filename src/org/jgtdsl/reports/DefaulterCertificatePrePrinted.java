package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.jgtdsl.dto.ClearnessDTO;
import org.jgtdsl.dto.CustomerApplianceDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.MBillDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.MeterService;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionSupport;

public class DefaulterCertificatePrePrinted extends ActionSupport implements
		ServletContextAware {

	ClearnessDTO clearnessDTO = new ClearnessDTO();
	// ArrayList<ClearnessDTO> dueMonthList=new ArrayList<ClearnessDTO>();
	ClearnessDTO cto = new ClearnessDTO();

	private static final long serialVersionUID = 8854240739341830184L;
	private String customer_id;
	private String download_type;
	private String area;
	private String collection_month;
	private String from_customer_id;
	private String to_customer_id;
	private String customer_category;
	private String customer_type;
	private String calender_year;
	private String officer_name;
	private String officer_desig;
	private String certification_id;
	private String report_type;
	private ServletContext servlet;
	public HttpServletResponse response = ServletActionContext.getResponse();
	public HttpServletRequest request;
	String yearsb;
	ArrayList<ClearnessDTO> CustomerList = new ArrayList<ClearnessDTO>();
	ArrayList<String> CustomerListDefaulters = new ArrayList<String>();
	ArrayList<String> CustomerListClear = new ArrayList<String>();
	CustomerDTO customer = new CustomerDTO();
	ClearnessDTO customerInfo;
	MeterService ms = new MeterService();
	ArrayList<CustomerApplianceDTO> applianceList = new ArrayList<CustomerApplianceDTO>();
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
	Date date = new Date();

	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	static DecimalFormat consumption_format = new DecimalFormat(
			"##########0.000");

	UserDTO loggedInUser = (UserDTO) ServletActionContext.getRequest()
			.getSession().getAttribute("user");
	Connection conn = ConnectionManager.getConnection();

	// ////////////////////////////////////////////////////////
	public String clearnessCertificateInfoPrePrinted()
			throws DocumentException, IOException {

		try {

			if (download_type.equals("individual_wise")) {
				customerInfo = getCustomerInfo(customer_id, area,
						calender_year, collection_month);
				certification_id = customer_id + collection_month
						+ calender_year;

				if (customerInfo.getCustomerID() == null) {
					CustomerListClear.add(customer_id);
				} else {
					CustomerListDefaulters.add(customer_id);
				}
				if (CustomerListClear.size() > 0) {
					forCleared();
				} else if (CustomerListDefaulters.size() > 0) {
					forDefaulters();
				}
			}

			if (download_type.equals("category_wise")) {
				CustomerList = getCustomerList(from_customer_id,
						to_customer_id, customer_category, area);

				for (int i = 0; i < CustomerList.size(); i++) {
					String customerID = CustomerList.get(i).getCustomerID();
					customerInfo = getCustomerInfo(CustomerList.get(i)
							.getCustomerID(), area, calender_year,
							collection_month);

					if (customerInfo.getCustomerID() == null) {
						CustomerListClear.add(customerID);
					} else {
						CustomerListDefaulters.add(customerID);
					}
				}
			}
			if (report_type.equalsIgnoreCase("DC")) {
				forDefaulters();

			} else {
				forCleared();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ///////////////////////////////////////////////////////
	public String clearnessUnderCertificateInfo() {
		String fileName = "clearnessUnderCertificateInfo.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(0, 0, 20, 50);

		try {

			ReportFormat Event = new ReportFormat(getServletContext());
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			writer.setPageEvent(Event);
			PdfPCell pcell = null;

			document.open();

			PdfPTable mainTable = new PdfPTable(8);
			Rectangle page = document.getPageSize();
			/*
			 * headerTable.setTotalWidth(page.getWidth()); float
			 * a=((page.getWidth()*15)/100)/2; float
			 * b=((page.getWidth()*30)/100)/2;
			 * 
			 * headerTable.setWidths(new float[] { a,b,a });
			 */

			String realPath = servlet
					.getRealPath("/resources/images/logo/JG.png"); // image path
			Image img = Image.getInstance(realPath);

			// img.scaleToFit(10f, 200f);
			// img.scalePercent(200f);
			img.scaleAbsolute(28f, 31f);
			// img.setAbsolutePosition(145f, 780f);
			img.setAbsolutePosition(140f, 787f); // rotate

			document.add(img);

			PdfPTable mTable = new PdfPTable(1);
			// mTable.setWidths(new float[]{b});
			pcell = new PdfPCell(new Paragraph(
					"JALALABAD GAS T & D SYSTEM LIMITED", ReportUtil.f12B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)",
					ReportUtil.f10B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("REGIONAL DISTRIBUTION OFFICE : ",
					ReportUtil.f10);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer
					.valueOf(getArea()) - 1]), ReportUtil.f10B);
			Paragraph p = new Paragraph();
			p.add(chunk1);
			p.add(chunk2);
			pcell = new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(
					"Subject: Under Certitificate Posting", ReportUtil.f10));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			pcell.setBorder(Rectangle.BOTTOM);
			pcell.setPaddingBottom(10);
			mTable.addCell(pcell);
			
			Paragraph para = new Paragraph(); 
			if(report_type.equals("DC")){
				para= new Paragraph("For Defaulter Customers",ReportUtil.f10B);
			}else{
				para= new Paragraph("For Non-Defaulter Customers",ReportUtil.f10B);		
				}
			
			pcell=new PdfPCell(para);
			pcell.setPaddingTop(8);			
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			document.add(mTable);

			// //////////headerTable//////////////

			mainTable.setWidths(new float[] { 8, 15,25, 40, 5, 5, 25, 10 });
			mainTable.setWidthPercentage(95);
			
			
			mTable.addCell(pcell);	

			pcell = new PdfPCell(new Paragraph(" ", ReportUtil.f8B));
			pcell.setColspan(8);
			pcell.setPadding(5);
			pcell.setRowspan(2);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Sl No.", ReportUtil.f8B));
			pcell.setRowspan(2);
			pcell.setPadding(5);
			// pcell.setMinimumHeight(20f);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("ID", ReportUtil.f8B));
			pcell.setRowspan(2);
			pcell.setPadding(5);
			// pcell.setMinimumHeight(20f);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(
					new Paragraph("Name", ReportUtil.f8B));
			pcell.setRowspan(2);
			pcell.setPadding(5);
			// pcell.setMinimumHeight(20f);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);
			
			pcell = new PdfPCell(
					new Paragraph("Address", ReportUtil.f8B));
			pcell.setRowspan(2);
			pcell.setPadding(5);
			// pcell.setMinimumHeight(20f);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Burner", ReportUtil.f8B));
			pcell.setColspan(2);
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Stamp", ReportUtil.f8B));
			pcell.setRowspan(2);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Remarks", ReportUtil.f8B));
			pcell.setRowspan(2);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);


			pcell = new PdfPCell(new Paragraph("S", ReportUtil.f8B));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("D", ReportUtil.f8B));
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			// /
			int w = 0;
			ArrayList<String> custlistArrayList= new ArrayList<String>();
			CustomerList = getCustomerList(from_customer_id, to_customer_id,
					customer_category, area);
			for(ClearnessDTO x: CustomerList){
				ClearnessDTO checkList=	getCustomerInfo(x.getCustomerID(), area, calender_year, collection_month);
				
				if(checkList.getCustomerID()==null){
					CustomerListClear.add(x.getCustomerID());
				}else{
					CustomerListDefaulters.add(x.getCustomerID());
				}
			}
			for(int i = 0; i < CustomerListDefaulters.size(); i++){
				if(report_type.equals("DC")){
					custlistArrayList.add(CustomerListDefaulters.get(i));
				}
			}
			for(int i = 0; i < CustomerListClear.size(); i++){
				if(report_type.equals("NDC")){
					custlistArrayList.add(CustomerListClear.get(i));
				}
			}
			for (int i = 0; i < custlistArrayList.size(); i++) {
				
				ClearnessDTO info= getCustomerInfo(custlistArrayList.get(i));

				w = i;

				pcell = new PdfPCell(new Paragraph(String.valueOf(i + 1),
						ReportUtil.f10));
				// pcell.setRowspan(1);
				pcell.setPadding(5);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				mainTable.addCell(pcell);

				pcell = new PdfPCell(new Paragraph(info.getCustomerID(), ReportUtil.f8));
				// pcell.setRowspan(1);
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				mainTable.addCell(pcell);

				pcell = new PdfPCell(new Paragraph(info.getCustomerName(), ReportUtil.f8));
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				// pcell.setRowspan(1);
				pcell.setPadding(5);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				mainTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(info.getCustomerAddress(), ReportUtil.f8));
				// pcell.setRowspan(1);
				pcell.setPadding(5);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				mainTable.addCell(pcell);

				pcell = new PdfPCell(new Paragraph(String.valueOf((info.getSingle_burner() == 0) ? "0" : info.getSingle_burner()), ReportUtil.f8));

				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				mainTable.addCell(pcell);

				pcell = new PdfPCell(new Paragraph(String.valueOf((info.getDouble_burner() == 0 ? "0" : info.getDouble_burner())), ReportUtil.f8));

				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				mainTable.addCell(pcell);

				if (w % 3 == 0) {
					pcell = new PdfPCell(new Paragraph("", ReportUtil.f8));
					pcell.setRowspan(3);
					pcell.setPadding(5);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					mainTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("", ReportUtil.f8));
					pcell.setRowspan(3);
					pcell.setPadding(5);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					mainTable.addCell(pcell);
				}
				
				

			}
			mainTable.setHeaderRows(4);
			document.add(mainTable);

			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(), fileName);
			document = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String forDefaulters() throws DocumentException, IOException {
		HttpServletResponse response = ServletActionContext.getResponse();

		PdfReader reader = null;
		ByteArrayOutputStream certificate = null;
		List<PdfReader> readers = null;
		String realPath = "";
		
		Document document = new Document(PageSize.A0);
		ByteArrayOutputStream out = null;

		Rectangle one = new Rectangle(648, 792);
		one.setBorder(1);
		document.setPageSize(one);

		// left,right,top,bottom
		String fileName = "";
		readers = new ArrayList<PdfReader>();
		BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,
				BaseFont.WINANSI, BaseFont.EMBEDDED);
		BaseFont bfb = BaseFont.createFont(BaseFont.TIMES_BOLD,
				BaseFont.WINANSI, BaseFont.EMBEDDED);

		try {
			realPath = servlet
					.getRealPath("/resources/staticPdf/preprintedCertificate.pdf");

			document = new Document();
			out = new ByteArrayOutputStream();
			// left,right,top,bottom
			fileName = "PreprintedClearnessCertificate.pdf";

			PdfContentByte over = null;
			PdfStamper stamp = null;

			for (int i = 0; i < CustomerListDefaulters.size(); i++) {

				reader = new PdfReader(new FileInputStream(realPath));
				certificate = new ByteArrayOutputStream();
				stamp = new PdfStamper(reader, certificate);
				over = stamp.getOverContent(1);
				over.beginText();

				customerInfo = getCustomerInfo(CustomerListDefaulters.get(i),
						this.area, this.calender_year, this.collection_month);

				certification_id = CustomerListDefaulters.get(i)
						+ collection_month + calender_year;

				// sutro nong
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						certification_id, 20, 724, 0);				
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						certification_id, 20, 334, 0);
				// Date
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						dateFormat.format(date), 430, 724, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						dateFormat.format(date), 430, 334, 0);
				// id
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerID(), 170, 705, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerID(), 170, 315, 0);
				// month
				String month_name = (Month.values()[Integer
						.parseInt(collection_month) - 1].getLabel());
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, month_name+", "+calender_year,
						335, 705, 0);
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, month_name+", "+calender_year,
						335, 315, 0);
				// due month
				over.setFontAndSize(bf, 10);

				String hsi = customerInfo.getDueMonth();
				if (customerInfo.getDueMonth() != null)
					hsi = customerInfo.getDueMonth().replaceAll("&#x26;", "&");
				int size = 43;
				if (hsi != null && hsi.length() > size) {
					String[] s1;
					s1 = spitSrting(hsi, size);

					if (s1[1].length() <= size) {
						over.setTextMatrix(00, 625);
						over.showText(s1[0]);
						over.setTextMatrix(00, 235);
						over.showText(s1[0]);
					} else {
						s1 = spitSrting(s1[1], size);
						over.setTextMatrix(00, 625);
						over.showText(s1[0]);
						over.setTextMatrix(00, 235);
						over.showText(s1[0]);

						if (s1[1].length() <= size) {
							over.setTextMatrix(00, 615);
							over.showText(s1[1]);
							over.setTextMatrix(00, 225);
							over.showText(s1[1]);
						} else {
							s1 = spitSrting(s1[1], size);
							over.setTextMatrix(00, 615);
							over.showText(s1[0]);
							over.setTextMatrix(00, 225);
							over.showText(s1[0]);
							over.setTextMatrix(00, 605);
							if (s1[1].length() > size)
								over.showText(s1[1].substring(size));
							else
								over.showText(s1[1]);
							over.setTextMatrix(00, 215);
							if (s1[1].length() > size)
								over.showText(s1[1].substring(size));
							else
								over.showText(s1[1]);
						}
					}
				} else {
					over.setTextMatrix(0, 625);
					over.showText(hsi);
					over.setTextMatrix(0, 235);
					over.showText(hsi);
				}
				
				/*else {
					over.setTextMatrix(90, 565);
					over.showText(hsi);
					over.setTextMatrix(90, 262);
					over.showText(hsi);
				}*/

				// burner
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						String.valueOf(customerInfo.getSingle_burner()), 235,
						625, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						String.valueOf(customerInfo.getSingle_burner()), 235,
						235, 0);

				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						String.valueOf(customerInfo.getDouble_burner()), 275,
						625, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						String.valueOf(customerInfo.getDouble_burner()), 275,
						235, 0);

				// due amount
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						String.valueOf(customerInfo.getDueAmount()), 360, 625,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						String.valueOf(customerInfo.getDueAmount()), 360, 235,
						0);
				
				//in words
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getAmountInWords(), 20, 582, 0);

				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getAmountInWords(), 20, 192, 0);
				// name
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerName(), 35, 500, 0);
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerName(), 35, 110, 0);
				// address
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerAddress(), 35, 480, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerAddress(), 35, 90, 0);

				// sign
				over.setFontAndSize(bf, 10);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, officer_name
						+ ", " + officer_desig, 400, 515, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, officer_name
						+ ", " + officer_desig, 400, 125, 0);

				// AREA  name
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Area
						.values()[Integer.parseInt(area) - 1].getLabel(),
						180, 550, 0);
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Area
						.values()[Integer.parseInt(area) - 1].getLabel(),
						180, 160, 0);
				
				over.endText();
				stamp.close();
				readers.add(new PdfReader(certificate.toByteArray()));
				insertClarificationHistory(customerInfo.getCustomerID(), dateFormat.format(date), officer_name, (int) customerInfo.getDueAmount());
			}
			if (readers.size() > 0) {
				PdfWriter writer = PdfWriter.getInstance(document, out);

				document.open();

				PdfContentByte cb = writer.getDirectContent();
				PdfReader pdfReader = null;
				PdfImportedPage page;

				for (int k = 0; k < readers.size(); k++) {
					document.newPage();
					pdfReader = readers.get(k);
					page = writer.getImportedPage(pdfReader, 1);
					cb.addTemplate(page, 0, 0);
				}
				document.close();
				ReportUtil rptUtil = new ReportUtil();
				rptUtil.downloadPdf(out, response, fileName);
				document = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private String forCleared() throws DocumentException, IOException {

		HttpServletResponse response = ServletActionContext.getResponse();

		PdfReader reader = null;
		ByteArrayOutputStream certificate = null;
		List<PdfReader> readers = null;
		String realPath = "";
		Document document = new Document();
		ByteArrayOutputStream out = null;
		Rectangle one = new Rectangle(648, 792);
		one.setBorder(1);
		document.setPageSize(one);
		String fileName = "";
		readers = new ArrayList<PdfReader>();
		BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,
				BaseFont.WINANSI, BaseFont.EMBEDDED);
		BaseFont bfb = BaseFont.createFont(BaseFont.TIMES_BOLD,
				BaseFont.WINANSI, BaseFont.EMBEDDED);

		try {
			realPath = servlet
					.getRealPath("/resources/staticPdf/preprintedCertificate.pdf");

			document = new Document();
			out = new ByteArrayOutputStream();
			// left,right,top,bottom
			fileName = "PreprintedClearnessCertificate.pdf";

			PdfContentByte over = null;
			PdfStamper stamp = null;
			for (int i = 0; i < CustomerListClear.size(); i++) {

				reader = new PdfReader(new FileInputStream(realPath));
				certificate = new ByteArrayOutputStream();
				stamp = new PdfStamper(reader, certificate);
				over = stamp.getOverContent(1);
				over.beginText();
				customerInfo = getCustomerInfo(CustomerListClear.get(i));

				certification_id = CustomerListClear.get(i) + collection_month
						+ calender_year;

				// sutro nong
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						certification_id, 20, 728, 0);				
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						certification_id, 20, 332, 0);
				// Date
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						dateFormat.format(date), 430, 734, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						dateFormat.format(date), 430, 340, 0);
				// id
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerID(), 15, 596, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerID(), 15, 200, 0);
				// month
				String month_name = (Month.values()[Integer
						.parseInt(collection_month) - 1].getLabel());
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, month_name+", "+calender_year,
						265, 700, 0);
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, month_name+", "+calender_year,
						265, 306, 0);

				// burner
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						String.valueOf(customerInfo.getSingle_burner()), 160,
						596, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						String.valueOf(customerInfo.getSingle_burner()), 160,
						200, 0);

				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						String.valueOf(customerInfo.getDouble_burner()), 225,
						596, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						String.valueOf(customerInfo.getDouble_burner()), 225,
						200, 0);

				// name
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerName(), 50, 518, 0);
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerName(), 50, 118, 0);
				// address
				over.setFontAndSize(bfb, 12);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerAddress(), 50, 498, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						customerInfo.getCustomerAddress(), 50, 98, 0);

				// sign
				over.setFontAndSize(bf, 10);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, officer_name
						+ ", " + officer_desig, 400, 545, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, officer_name
						+ ", " + officer_desig, 400, 148, 0);

				over.endText();
				stamp.close();
				readers.add(new PdfReader(certificate.toByteArray()));
				insertClarificationHistory(customerInfo.getCustomerID(), dateFormat.format(date), officer_name, (int) customerInfo.getDueAmount());
			}
			if (readers.size() > 0) {
				PdfWriter writer = PdfWriter.getInstance(document, out);

				document.open();

				PdfContentByte cb = writer.getDirectContent();
				PdfReader pdfReader = null;
				PdfImportedPage page;

				for (int k = 0; k < readers.size(); k++) {
					document.newPage();
					pdfReader = readers.get(k);
					page = writer.getImportedPage(pdfReader, 1);
					cb.addTemplate(page, 0, 0);
				}
				document.close();
				ReportUtil rptUtil = new ReportUtil();
				rptUtil.downloadPdf(out, response, fileName);
				document = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}

	public ClearnessDTO getClearnessDTO() {
		return clearnessDTO;
	}

	public void setClearnessDTO(ClearnessDTO clearnessDTO) {
		this.clearnessDTO = clearnessDTO;
	}

	public ClearnessDTO getCto() {
		return cto;
	}

	public void setCto(ClearnessDTO cto) {
		this.cto = cto;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getDownload_type() {
		return download_type;
	}

	public void setDownload_type(String download_type) {
		this.download_type = download_type;
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

	public String getFrom_customer_id() {
		return from_customer_id;
	}

	public void setFrom_customer_id(String from_customer_id) {
		this.from_customer_id = from_customer_id;
	}

	public String getTo_customer_id() {
		return to_customer_id;
	}

	public void setTo_customer_id(String to_customer_id) {
		this.to_customer_id = to_customer_id;
	}

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}

	public String getCustomer_type() {
		return customer_type;
	}

	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}

	public String getCalender_year() {
		return calender_year;
	}

	public void setCalender_year(String calender_year) {
		this.calender_year = calender_year;
	}

	public String getOfficer_name() {
		return officer_name;
	}

	public void setOfficer_name(String officer_name) {
		this.officer_name = officer_name;
	}

	public String getOfficer_desig() {
		return officer_desig;
	}

	public void setOfficer_desig(String officer_desig) {
		this.officer_desig = officer_desig;
	}

	public String getCertification_id() {
		return certification_id;
	}

	public void setCertification_id(String certification_id) {
		this.certification_id = certification_id;
	}

	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

	public ServletContext getServlet() {
		return servlet;
	}

	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}

	public String getYearsb() {
		return yearsb;
	}

	public void setYearsb(String yearsb) {
		this.yearsb = yearsb;
	}

	public ArrayList<ClearnessDTO> getCustomerList() {
		return CustomerList;
	}

	public void setCustomerList(ArrayList<ClearnessDTO> customerList) {
		CustomerList = customerList;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public ClearnessDTO getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(ClearnessDTO customerInfo) {
		this.customerInfo = customerInfo;
	}

	public MeterService getMs() {
		return ms;
	}

	public void setMs(MeterService ms) {
		this.ms = ms;
	}

	public ArrayList<CustomerApplianceDTO> getApplianceList() {
		return applianceList;
	}

	public void setApplianceList(ArrayList<CustomerApplianceDTO> applianceList) {
		this.applianceList = applianceList;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public static DecimalFormat getTaka_format() {
		return taka_format;
	}

	public static void setTaka_format(DecimalFormat taka_format) {
		DefaulterCertificatePrePrinted.taka_format = taka_format;
	}

	public static DecimalFormat getConsumption_format() {
		return consumption_format;
	}

	public static void setConsumption_format(DecimalFormat consumption_format) {
		DefaulterCertificatePrePrinted.consumption_format = consumption_format;
	}

	public UserDTO getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(UserDTO loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}

	public String[] spitSrting(String base, int size) {
		char[] separator = { ' ', '.', ',', ';', ':' };
		boolean separatorfound = false;
		String s1[] = new String[2];
		outer: for (int j = size; j >= 0; j--) {
			for (int k = 0; k < separator.length; k++) {
				if (separator[k] == base.charAt(j)) {
					s1[0] = base.substring(0, j + 1);
					s1[1] = base.substring(j + 1, base.length());
					separatorfound = true;
					break outer;
				}
			}

		}
		if (!separatorfound) {
			int x = 0;
			s1[0] = base.substring(0, size - 10);
			s1[1] = base.substring(size - 10, base.length());
		}
		return s1;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	private ArrayList<ClearnessDTO> getCustomerList(String from_cus_id,
			String to_cus_id, String cust_cat_id, String area) {

		ArrayList<ClearnessDTO> custList = new ArrayList<ClearnessDTO>();
		if (collection_month.length() < 2) {
			collection_month = "0" + collection_month;
		}
		String type = null;
		if (from_cus_id.isEmpty()) {
			type = area + this.customer_category;
		} else {
			type = from_cus_id.substring(0, 4);
		}
		
		String whereClause = null;
		if (from_cus_id.isEmpty() && to_cus_id.isEmpty()) {
			whereClause = "      AND bi.category_id='"+ this.customer_category + "'  ";
		} else {
			whereClause = "      AND BI.CUSTOMER_ID BETWEEN '" + from_cus_id+ "' AND '" + to_cus_id + "' ";
		}
		try {
			/*String transaction_sql = "  SELECT bi.CUSTOMER_ID, getBurner (bi.CUSTOMER_ID) BURNER, BI.CUSTOMER_NAME, COUNT (*) cnt "
					+ "    FROM "
					+ bill_table
					+ " bi, CUSTOMER_CONNECTION cc "
					+ "   WHERE     BI.CUSTOMER_ID = CC.CUSTOMER_ID "
					+ "         AND CC.STATUS = 1 "
					+
					// "         AND bi.STATUS = 1 " +
					"         AND bi.area_id = '"
					+ area
					+ "' "
					+ whereClause
					+ "                 AND BILL_YEAR || LPAD (BILL_MONTH, 2, 0) <= '"
					+ calender_year
					+ collection_month
					+ "'  GROUP BY BI.CUSTOMER_ID, BI.CUSTOMER_NAME, CUSTOMER_CATEGORY, bi.AREA_ID "
					+ "  HAVING COUNT (*) >= 1 ";*/

			
			
			
			
			
			
			String transaction_sql= 
					"  SELECT distinct(bi.CUSTOMER_ID), " +
					"         getBurner (bi.CUSTOMER_ID) BURNER, " +
					"         BI.Full_name " +
					"    FROM MVIEW_CUSTOMER_INFO bi, CUSTOMER_CONNECTION cc " +
					"   WHERE BI.CUSTOMER_ID = CC.CUSTOMER_ID " +
					"         AND CC.STATUS = 1 " +
					"         AND bi.area_id = '"+this.area+"' " +
					whereClause+
					"         order by BI.CUSTOMER_ID asc " ;

			
			PreparedStatement ps1 = conn.prepareStatement(transaction_sql);
			ResultSet resultSet = ps1.executeQuery();
			while (resultSet.next()) {
				ClearnessDTO ClearnessDTO = new ClearnessDTO();
				ClearnessDTO.setCustomerID(resultSet.getString("CUSTOMER_ID"));
				ClearnessDTO.setCustomerName(resultSet
						.getString("FULL_NAME"));
				String burner = resultSet.getString("BURNER");
				String[] brnrArray = burner.split("#");
				ClearnessDTO.setSingle_burner(Integer.parseInt(brnrArray[0]));
				ClearnessDTO.setDouble_burner(Integer.parseInt(brnrArray[1]));
				custList.add(ClearnessDTO);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return custList;

	}

	public ClearnessDTO getCustomerInfo(String customer_id) {
		ClearnessDTO customer = null;

		Connection conn = ConnectionManager.getConnection();

		String sql = "SELECT CUSTOMER_ID, " + "       ADDRESS, "
				+ "       FULL_NAME, "
				+ "       getBurner (CUSTOMER_ID) burner "
				+ "  FROM MVIEW_CUSTOMER_INFO " + " WHERE Customer_Id = ? ";

		PreparedStatement stmt = null;
		ResultSet r = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, customer_id);
			r = stmt.executeQuery();

			if (r.next()) {
				customer = new ClearnessDTO();
				customer.setCustomerID(r.getString("CUSTOMER_ID"));
				customer.setCustomerAddress(r.getString("ADDRESS"));
				customer.setCustomerName(r.getString("FULL_NAME"));

				String burner = r.getString("BURNER");
				String[] brnrArray = burner.split("#");
				customer.setSingle_burner(Integer.parseInt(brnrArray[0]));
				customer.setDouble_burner(Integer.parseInt(brnrArray[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return customer;

	}

	private ClearnessDTO getCustomerInfo(String customer_id, String area_id,
			String year, String month) {
		ClearnessDTO ctrInfo = new ClearnessDTO();
		String type = customer_id.substring(0, 4);
		String bill_table;
		if (type.equalsIgnoreCase(area_id + "01")
				|| type.equalsIgnoreCase(area_id + "09")) {
			bill_table = "BILL_NON_METERED";
		} else {
			bill_table = "BILL_METERED";
		}

		try {

			String customer_info_sql = "SELECT * "
					+ "  FROM (  SELECT bi.CUSTOMER_ID, "
					+ "                 CUSTOMER_CATEGORY, "
					+ "                 bi.AREA_ID,  getBurner(bi.customer_id) burner, "
					+ "                 LISTAGG ( "
					+ "                       TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON') "
					+ "                    || ' ' "
					+ "                    || SUBSTR (BILL_YEAR, 3), "
					+ "                    ',') "
					+ "                 WITHIN GROUP (ORDER BY BILL_YEAR ASC, BILL_MONTH ASC) "
					+ "                    AS DUEMONTH, "
					+ "                 SUM ( "
					+ "                      BILLED_AMOUNT "
					+ "                    + CALCUALTESURCHARGE (BILL_ID, "
					+ "                                          TO_CHAR (SYSDATE, 'dd-mm-YYYY'))) "
					+ "                    totalamount, "
					+ " NUMBER_SPELLOUT_FUNC ( "
					+ "                    TO_NUMBER ( "
					+ "                       SUM ( "
					+ "                            BILLED_AMOUNT "
					+ "                          + CALCUALTESURCHARGE ( "
					+ "                               BILL_ID, "
					+ "                               TO_CHAR (SYSDATE, 'dd-mm-YYYY'))), "
					+ "                       '99999999999999.99')) "
					+ "                    inwords, "
					+ "                 COUNT (*) cnt " + "            FROM "
					+ bill_table
					+ " bi, CUSTOMER_CONNECTION cc "
					+ "           WHERE     BI.CUSTOMER_ID = CC.CUSTOMER_ID "
					+ "                 AND CC.STATUS = 1 "
					+ "                 AND bi.STATUS = 1 "
					+ "                 AND bi.area_id = '"
					+ this.area
					+ "' "
					+ " AND BI.CUSTOMER_ID = '"
					+ customer_id
					+ "' "

					// "                 And bi.CUSTOMER_CATEGORY= " +
					+ "                 AND BILL_YEAR || LPAD (BILL_MONTH, 2, 0) <= '"
					+ year
					+ month
					+ "' "
					+ "        GROUP BY BI.CUSTOMER_ID, CUSTOMER_CATEGORY, bi.AREA_ID "
					+ "          HAVING COUNT (*) >= 1) tmp1, "
					+ "       (SELECT AA.CUSTOMER_ID, "
					+ "               BB.FULL_NAME, "
					+ "               BB.MOBILE, "
					+ "               AA.ADDRESS_LINE1, "
					+ "               AA.ADDRESS_LINE2 "
					+ "          FROM CUSTOMER_ADDRESS aa, CUSTOMER_PERSONAL_INFO bb "
					+ "         WHERE AA.CUSTOMER_ID = BB.CUSTOMER_ID) tmp2 "
					+ " WHERE tmp1.CUSTOMER_ID = tmp2.CUSTOMER_ID ";

			Statement st = conn.createStatement();// Statement(customer_info_sql);

			ResultSet resultSet = st.executeQuery(customer_info_sql);

			while (resultSet.next()) {

				ctrInfo.setCustomerID(resultSet.getString("CUSTOMER_ID"));
				ctrInfo.setCustomerName(resultSet.getString("FULL_NAME"));
				ctrInfo.setCustomerAddress(resultSet.getString("ADDRESS_LINE1"));
				ctrInfo.setDueMonth(resultSet.getString("DUEMONTH"));
				ctrInfo.setDueAmount(Double.parseDouble(resultSet
						.getString("TOTALAMOUNT")));
				ctrInfo.setArea(resultSet.getString("AREA_ID"));
				ctrInfo.setAmountInWords(resultSet.getString("INWORDS"));

				String burner = resultSet.getString("BURNER");
				String[] brnrArray = burner.split("#");
				ctrInfo.setSingle_burner(Integer.parseInt(brnrArray[0]));
				ctrInfo.setDouble_burner(Integer.parseInt(brnrArray[1]));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ctrInfo;
	}
	
	// insert into database
		public void insertClarificationHistory(String cust_id, String issue_date,
				String insert_by, int dues_status) {
			ResponseDTO response = new ResponseDTO();
			
			if(collection_month.length()<2){
				collection_month="0"+collection_month;
			}
			TransactionManager transactionManager = new TransactionManager();
			Connection conn = transactionManager.getConnection();

			// response=validateReconnInfo(reconn,disconn);
			// if(response.isResponse()==false)
			// return response;

			String sqlInsert = "INSERT INTO CLARIFICATION_HISTORY ( "
					+ "   CUSTOMER_ID, CALENDER_YEAR, ISSUE_DATE,  "
					+ "   STATUS, DUES_STATUS, INSERTED_ON,  "
					+ "   INSERTED_BY, CALENDER_MONTH, CERTIFICATION_ID )  "
					+ "   VALUES ( ?,?,sysdate,?,?,sysdate,?,?,?)";

			String checkIsAvailable = "Select count(customer_id) CUS_COUNT from CLARIFICATION_HISTORY where CALENDER_MONTH=? and CALENDER_YEAR=? and customer_id=?";

			PreparedStatement stmt = null;
			ResultSet r = null;
			int count = 0;

			try {
				stmt = conn.prepareStatement(checkIsAvailable);
				stmt.setString(1, collection_month);
				stmt.setString(2, calender_year);
				stmt.setString(3, cust_id);
				r = stmt.executeQuery();
				if (r.next())
					count = r.getInt("CUS_COUNT");

				if (count == 0) {
					stmt = conn.prepareStatement(sqlInsert);
					stmt.setString(1, cust_id);
					stmt.setString(2, calender_year);
					stmt.setInt(3, 1); // / 1 means all generated(approved)
					stmt.setInt(4, dues_status);
					stmt.setString(5, insert_by);
					stmt.setString(6, collection_month);
					stmt.setString(7, certification_id);
					stmt.execute();
				}
				transactionManager.commit();
			}

			catch (Exception e) {
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
				try {
					transactionManager.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} finally {
				try {
					stmt.close();
					transactionManager.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				stmt = null;
				conn = null;
			}

			return;
		}
}
