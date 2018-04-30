package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.AccountDTO;
import org.jgtdsl.dto.DepositDTO;
import org.jgtdsl.dto.TransactionDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.models.DepositService;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class BGReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	ArrayList<DepositDTO> expireList = new ArrayList<DepositDTO>();
	public ServletContext servlet;

	Connection conn = ConnectionManager.getConnection();

	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
	UserDTO loggedInUser = (UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");

	public String execute() throws Exception {

		String fileName = "BG Expire List.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(20, 20, 30, 72);
		PdfPCell pcell = null;

		try {

			ReportFormat eEvent = new ReportFormat(getServletContext());

			// MeterReadingDTO meterReadingDTO = new MeterReadingDTO();

			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);

			document.open();

			PdfPTable headerTable = new PdfPTable(3);

			headerTable.setWidths(new float[] { 5, 190, 5 });

			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);

			String realPath = servlet.getRealPath("/resources/images/logo/JG.png"); // image path
			Image img = Image.getInstance(realPath);

			// img.scaleToFit(10f, 200f);
			// img.scalePercent(200f);
			img.scaleAbsolute(28f, 31f);
			img.setAbsolutePosition(145f, 780f);
			// img.setAbsolutePosition(290f, 540f); // rotate

			document.add(img);

			PdfPTable mTable = new PdfPTable(1);
			mTable.setWidths(new float[] { 100 });
			pcell = new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)",
					ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("REGIONAL OFFICE : ", ReportUtil.f9B);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id()) - 1]), ReportUtil.f9B);
			Paragraph p = new Paragraph();
			p.add(chunk1);
			p.add(chunk2);
			pcell = new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			pcell = new PdfPCell(mTable);
			pcell.setBorder(0);
			headerTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			document.add(headerTable);

			generatePdf_for_BGExpireListWithIn365days(document);

			/* [[[[[[[[[End--->For Last row]]]]]]]]] */

			//document.close();
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(), fileName);
			document = null;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private void generatePdf_for_BGExpireListWithIn365days(Document document) throws DocumentException {

		DepositService depositeService = new DepositService();
		expireList = depositeService.getBGExpireWithinOneYear();

		int expireListSize = expireList.size();

		document.setMargins(20, 20, 48, 72);
		PdfPTable headLinetable = null;
		PdfPCell pcell = null;
		headLinetable = new PdfPTable(1);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[] { 100 });

		pcell = new PdfPCell(new Paragraph("Bank Guarantee will expire within 365 days ", ReportUtil.f8B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("COLLECTION MONTH : "));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		pcell.setPaddingBottom(5);
		headLinetable.addCell(pcell);


		document.add(headLinetable);


		// document.add(new Paragraph("\n"));

		PdfPTable pdfPTable = new PdfPTable(9);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[] { 5, 10, 10, 15, 10, 10, 15, 10, 15 });
		pdfPTable.setHeaderRows(1);

		pcell = new PdfPCell(new Paragraph("SL No", ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Deposit ID", ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Customer ID", ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Customer Name", ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Bank", ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Branch", ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Valid Till", ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Total Deposit", ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Expire Within (days)",
				ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);

		// document.add(pdfPTable);

		// double totalSecurity=0.0;
		// double totalSurcharge=0.0;
		// double totalFees=0.0;
		// double totalSecurityDeposit=0.0;
		// double total=0.0;

		for (int j = 0; j < expireListSize; j++) {

			pcell = new PdfPCell(new Paragraph(String.valueOf(j + 1),
					ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(expireList.get(j)
					.getDeposit_id(), ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(expireList.get(j)
					.getCustomer_id(), ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(expireList.get(j)
					.getCustomer_name(), ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(
					expireList.get(j).getBank_name(), ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(expireList.get(j)
					.getBranch_name(), ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(expireList.get(j).getValid_to(),
					ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(expireList.get(j).getTotal_deposit(), ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(
					expireList.get(j).getExpire_in(), ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);

		}

		document.add(pdfPTable);

	}

	public ArrayList<DepositDTO> getExpireList() {
		return expireList;
	}

	public void setExpireList(ArrayList<DepositDTO> expireList) {
		this.expireList = expireList;
	}

	public ServletContext getServlet() {
		return servlet;
	}

	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public static DecimalFormat getTaka_format() {
		return taka_format;
	}

	public static void setTaka_format(DecimalFormat taka_format) {
		BGReport.taka_format = taka_format;
	}

	public static DecimalFormat getConsumption_format() {
		return consumption_format;
	}

	public static void setConsumption_format(DecimalFormat consumption_format) {
		BGReport.consumption_format = consumption_format;
	}

	public UserDTO getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(UserDTO loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

