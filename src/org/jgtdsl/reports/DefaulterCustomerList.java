package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.DefaulterDto;
import org.jgtdsl.dto.DepositDTO;
import org.jgtdsl.dto.NonMeterReportDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.ConnectionStatus;
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
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class DefaulterCustomerList extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<DefaulterDto> defaulterList = new ArrayList<DefaulterDto>();
	public ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();

	private String area;
	private String customer_category;
	private String bill_month;
	private String bill_year;
	private String report_for;
	private String category_name;
	private String criteria_type;
	private String month_number;
	private String customer_type;
	private String status;
	private String moholla_wise="  ";

	public String execute() throws Exception {

		DepositService depositeService = new DepositService();

		String fileName = "DefaulterCustomerList.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
		document.setMargins(5, 5, 60, 72);
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell = null;
		DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		DecimalFormat factor_format = new DecimalFormat("##########0.000");
		try {

			ReportFormat eEvent = new ReportFormat(getServletContext());

			DefaulterDto defaulterDto = new DefaulterDto();

			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);

			document.open();

			PdfPTable headerTable = new PdfPTable(3);

			headerTable.setWidths(new float[] { 5, 190, 5 });

			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			
			
			
			
			// for logo			
			
					String realPath = servlet.getRealPath("/resources/images/logo/JG.png");  // image path
					   Image img = Image.getInstance(realPath);
					      
					             //img.scaleToFit(10f, 200f);
					             //img.scalePercent(200f);
					            img.scaleAbsolute(32f, 35f);
					            //img.setAbsolutePosition(145f, 780f);  
					             img.setAbsolutePosition(348f, 515f);  // rotate
					            
					         document.add(img);
					         
					         

			PdfPTable mTable = new PdfPTable(1);
			mTable.setWidths(new float[] { 100 });
			pcell = new PdfPCell(new Paragraph(
					"JALALABAD GAS T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("(A company of PetroBangla)",
					ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("Regional Office :", ReportUtil.f8B);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer
					.valueOf(area) - 1]), ReportUtil.f8B);
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

			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[] { 30, 50, 30 });

			pcell = new PdfPCell(new Paragraph("", ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);
			headLinetable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("DEFAULTER CUSTOMER LIST",
					ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);
			headLinetable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("", ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);
			headLinetable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("", ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);
			headLinetable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("As On "
					+ Month.values()[Integer.valueOf(bill_month) - 1] + ", "
					+ bill_year, ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);
			headLinetable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("", ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);
			headLinetable.addCell(pcell);

			String symbol = "";
			if (criteria_type.equals("lt")) {
				symbol = "less than";
			} else if (criteria_type.equals("gt")) {
				symbol = "greater than";
			} else if (criteria_type.equals("eq")) {
				symbol = "equal to";
			} else if (criteria_type.equals("gteq")) {
				symbol = "greater than equal to";
			} else if (criteria_type.equals("lteq")) {
				symbol = "less than equal to";
			}

			pcell = new PdfPCell(new Paragraph("", ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);
			headLinetable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Due " + symbol + " "
					+ month_number + " Months", ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);
			headLinetable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("", ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);
			headLinetable.addCell(pcell);

			document.add(headLinetable);

			defaulterList = getDefaulterInformation();

			int totalRecordsPerCategory = 0;
			int totalRecordsPerMoholla = 0;
			int total_burner = 0;
			float subTotal_amount = 0;
			int subTotal_month = 0;
			float grandTotal_amount = 0;
			int grandTotal_month = 0;
			int grandTotalCustomer = 0;
			int serial = 0;
			int space = 0;

			int expireListSize = defaulterList.size();
			String previousCustomerCategoryName = new String("");
			String previousMoholla = new String("");
			if (customer_type.equals("metered")) {

				space = 2;
			}

			for (int i = 0; i < expireListSize; i++) 
			{
				defaulterDto = defaulterList.get(i);
				String currentCustomerCategoryName = defaulterDto
						.getCategory_name();
				String currentMoholla = defaulterDto.getMoholla_name();
				if (moholla_wise.equals("1")) {
					if (!currentMoholla.equals(previousMoholla)) {

						if (!(previousMoholla.equals("") && currentMoholla
								.equals(previousMoholla))) {

							if (i > 0) {
								pcell = new PdfPCell(
										new Paragraph(
												"Total Records:"
														+ String.valueOf(totalRecordsPerCategory),
												ReportUtil.f9B));
								pcell.setMinimumHeight(18f);
								pcell.setColspan(2);
								pcell.setBorder(0);
								pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
								pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								ptable.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("",
										ReportUtil.f9B));
								pcell.setMinimumHeight(18f);
								pcell.setColspan(2 + space);
								pcell.setBorder(0);
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								ptable.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(subTotal_amount),
										ReportUtil.f9B));
								pcell.setMinimumHeight(18f);
								pcell.setColspan(1);
								pcell.setBorder(0);
								pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
								pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								ptable.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										String.valueOf(subTotal_month),
										ReportUtil.f9B));
								pcell.setMinimumHeight(18f);
								pcell.setColspan(1);
								pcell.setBorder(0);
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								ptable.addCell(pcell);

								// pcell=new PdfPCell(new
								// Paragraph("",ReportUtil.f9B));
								// pcell.setMinimumHeight(18f);
								// pcell.setColspan(1);
								// pcell.setBorder(0);
								// pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								// pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								// ptable.addCell(pcell);
								document.add(ptable);

								grandTotalCustomer = grandTotalCustomer
										+ totalRecordsPerCategory;
								totalRecordsPerCategory = 0;
								totalRecordsPerMoholla=0;
								subTotal_amount = 0;
								subTotal_month = 0;
								serial = 0;

							}

						}
						if (customer_type.equals("metered")) {
							ptable = new PdfPTable(8);
							ptable.setWidthPercentage(100);
							ptable.setWidths(new float[] { 8, 15, 50, 15, 15,
									60, 20, 20 });
							ptable.setSpacingBefore(10);
						} else {
							ptable = new PdfPTable(6);
							ptable.setWidthPercentage(100);
							ptable.setWidths(new float[] { 8, 15, 50, 60, 20,
									20 });
							ptable.setSpacingBefore(10);
						}

						pcell = new PdfPCell(new Paragraph(currentMoholla,
								ReportUtil.f11B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(2);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(6);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("Serial No.",
								ReportUtil.f8));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("Customer Code",
								ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								"Customer Name & Address", ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						if (customer_type.equals("metered")) {
							pcell = new PdfPCell(new Paragraph(
									"LOAD (M3/Month)", ReportUtil.f9B));
							pcell.setMinimumHeight(18f);
							pcell.setColspan(2);
							pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
							pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							ptable.addCell(pcell);
						}

						pcell = new PdfPCell(new Paragraph("Dues Months",
								ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						// pcell=new PdfPCell(new
						// Paragraph("Total Month",ReportUtil.f9B));
						// pcell.setMinimumHeight(18f);
						// pcell.setRowspan(2);
						// pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						// pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						// ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("Total Due Amount",
								ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("Remarks",
								ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						if (customer_type.equals("metered")) {
							pcell = new PdfPCell(new Paragraph("Max",
									ReportUtil.f9B));
							pcell.setMinimumHeight(18f);
							pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
							pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							ptable.addCell(pcell);

							pcell = new PdfPCell(new Paragraph("Min",
									ReportUtil.f9B));
							pcell.setMinimumHeight(18f);
							pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
							pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							ptable.addCell(pcell);
						}
					}

				} else {
					if (!currentCustomerCategoryName
							.equals(previousCustomerCategoryName)) {

						if (!(previousCustomerCategoryName.equals("") && currentCustomerCategoryName
								.equals(previousCustomerCategoryName))) {

							if (i > 0) {
								pcell = new PdfPCell(
										new Paragraph(
												"Total Records:"
														+ String.valueOf(totalRecordsPerCategory),
												ReportUtil.f9B));
								pcell.setMinimumHeight(18f);
								pcell.setColspan(2);
								pcell.setBorder(0);
								pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
								pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								ptable.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("",
										ReportUtil.f9B));
								pcell.setMinimumHeight(18f);
								pcell.setColspan(2 + space);
								pcell.setBorder(0);
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								ptable.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(subTotal_amount),
										ReportUtil.f9B));
								pcell.setMinimumHeight(18f);
								pcell.setColspan(1);
								pcell.setBorder(0);
								pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
								pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								ptable.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										String.valueOf(subTotal_month),
										ReportUtil.f9B));
								pcell.setMinimumHeight(18f);
								pcell.setColspan(1);
								pcell.setBorder(0);
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								ptable.addCell(pcell);

								// pcell=new PdfPCell(new
								// Paragraph("",ReportUtil.f9B));
								// pcell.setMinimumHeight(18f);
								// pcell.setColspan(1);
								// pcell.setBorder(0);
								// pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								// pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								// ptable.addCell(pcell);
								document.add(ptable);

								grandTotalCustomer = grandTotalCustomer
										+ totalRecordsPerCategory;
								totalRecordsPerCategory = 0;
								subTotal_amount = 0;
								subTotal_month = 0;
								serial = 0;

							}

						}
						if (customer_type.equals("metered")) {
							ptable = new PdfPTable(8);
							ptable.setWidthPercentage(100);
							ptable.setWidths(new float[] { 8, 15, 50, 15, 15,
									60, 20, 20 });
							ptable.setSpacingBefore(10);
						} else {
							ptable = new PdfPTable(6);
							ptable.setWidthPercentage(100);
							ptable.setWidths(new float[] { 8, 15, 50, 60, 20,
									20 });
							ptable.setSpacingBefore(10);
						}

						pcell = new PdfPCell(new Paragraph(
								currentCustomerCategoryName, ReportUtil.f11B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(2);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(6);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("Serial No.",
								ReportUtil.f8));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("Customer Code",
								ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								"Customer Name & Address", ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						if (customer_type.equals("metered")) {
							pcell = new PdfPCell(new Paragraph(
									"LOAD (M3/Month)", ReportUtil.f9B));
							pcell.setMinimumHeight(18f);
							pcell.setColspan(2);
							pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
							pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							ptable.addCell(pcell);
						}

						pcell = new PdfPCell(new Paragraph("Dues Months",
								ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						// pcell=new PdfPCell(new
						// Paragraph("Total Month",ReportUtil.f9B));
						// pcell.setMinimumHeight(18f);
						// pcell.setRowspan(2);
						// pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						// pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						// ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("Total Due Amount",
								ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("Remarks",
								ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setRowspan(2);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						ptable.addCell(pcell);

						if (customer_type.equals("metered")) {
							pcell = new PdfPCell(new Paragraph("Max",
									ReportUtil.f9B));
							pcell.setMinimumHeight(18f);
							pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
							pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							ptable.addCell(pcell);

							pcell = new PdfPCell(new Paragraph("Min",
									ReportUtil.f9B));
							pcell.setMinimumHeight(18f);
							pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
							pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							ptable.addCell(pcell);
						}
					}
				}

				pcell = new PdfPCell(new Paragraph(String.valueOf(serial + 1),
						ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);

				pcell = new PdfPCell(new Paragraph(
						defaulterDto.getCustomer_id(), ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);

				String name_address = "" + defaulterDto.getFull_name() + "\n"
						+ defaulterDto.getAddress();

				pcell = new PdfPCell(new Paragraph(name_address, ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);

				if (customer_type.equals("metered")) {
					pcell = new PdfPCell(new Paragraph(
							taka_format.format(defaulterDto.getMaxLoad()),
							ReportUtil.f8));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);

					pcell = new PdfPCell(new Paragraph(
							taka_format.format(defaulterDto.getMinLoad()),
							ReportUtil.f8));
					pcell.setMinimumHeight(16f);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					ptable.addCell(pcell);
				}
				pcell = new PdfPCell(new Paragraph(defaulterDto.getDue_month(),
						ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);

				subTotal_amount = subTotal_amount + defaulterDto.getAmount();
				grandTotal_amount = grandTotal_amount
						+ defaulterDto.getAmount();

				subTotal_month = subTotal_month + defaulterDto.getTotal_month();
				grandTotal_month = grandTotal_month
						+ defaulterDto.getTotal_month();

				pcell = new PdfPCell(new Paragraph(
						taka_format.format(defaulterDto.getAmount()),
						ReportUtil.f8B));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);

				// int int_status=Integer.parseInt(defaulterDto.getStatus()) ;
				String st_status = ConnectionStatus.values()[Integer
						.valueOf(defaulterDto.getStatus())].getLabel();

				pcell = new PdfPCell(new Paragraph(st_status
						+ ",\nTotal- "
						+ String.valueOf(defaulterDto.getTotal_month()
								+ " Months"), ReportUtil.f8));
				// pcell = new PdfPCell(new
				// Paragraph(String.valueOf(defaulterDto.getTotal_month()),ReportUtil.f8B));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);

				previousCustomerCategoryName = defaulterDto.getCategory_name();
				previousMoholla = defaulterDto.getMoholla_name();
				totalRecordsPerCategory++;
				serial++;

			}
			/* [[[[[[[[[Start--->For Last row]]]]]]]]] */
			pcell = new PdfPCell(new Paragraph("Total Records:"
					+ String.valueOf(totalRecordsPerCategory), ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(2);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("", ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(2 + space);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(
					taka_format.format(subTotal_amount), ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(1);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(String.valueOf(subTotal_month),
					ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(1);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);

			// pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			// pcell.setMinimumHeight(18f);
			// pcell.setColspan(1);
			// pcell.setBorder(0);
			// pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			// pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			// ptable.addCell(pcell);
			grandTotalCustomer = grandTotalCustomer + totalRecordsPerCategory;

			/* [[[[[[[[[End--->For Last row]]]]]]]]] */

			// // Grand total starts here
			pcell = new PdfPCell(new Paragraph("Grand Total: "
					+ String.valueOf(grandTotalCustomer), ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(3);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("", ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(1 + space);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(
					taka_format.format(grandTotal_amount), ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(1);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(
					String.valueOf(grandTotal_month), ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(1);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);

			// pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
			// pcell.setMinimumHeight(18f);
			// pcell.setColspan(1);
			// pcell.setBorder(0);
			// pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			// pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			// ptable.addCell(pcell);
			document.add(ptable);

			document.close();
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(), fileName);
			document = null;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private ArrayList<DefaulterDto> getDefaulterInformation() {
		ArrayList<DefaulterDto> defaulterInfoList = new ArrayList<DefaulterDto>();

		try {

			String monyear = String.valueOf(bill_year) + "" + "0"
					+ String.valueOf(bill_month);
			int month_year = Integer.parseInt(monyear);
			String wClause = "";
			String criteriaWclause = "";
			String defaulterListSql = "";
			String status_Sql = "";

			if (status.equals("01")) {
				status_Sql = "AND  CC.STATUS = 1";
			} else if (status.equals("00")) {
				status_Sql = "AND  CC.STATUS = 0";
			}

			if (report_for.equals("area_wise")) {
				wClause = " area_id= '" + area + "'";
			} else if (report_for.equals("category_wise")) {
				wClause = " area_id= '" + area + "' And CUSTOMER_CATEGORY= '"
						+ customer_category +"'";
				
				if (!(customer_category.equals("01") || customer_category.equals("09"))) {
					
					setCustomer_type("metered");
					
				}else{
					setCustomer_type("non_metered");
				}
				
				
				
			}

			if (criteria_type.equals("lt")) {
				criteriaWclause = " HAVING COUNT (*) <" + month_number;
			} else if (criteria_type.equals("gt")) {
				criteriaWclause = " HAVING COUNT (*) >" + month_number;
			} else if (criteria_type.equals("eq")) {
				criteriaWclause = " HAVING COUNT (*) =" + month_number;
			} else if (criteria_type.equals("gteq")) {
				criteriaWclause = " HAVING COUNT (*) >=" + month_number;
			} else if (criteria_type.equals("lteq")) {
				criteriaWclause = " HAVING COUNT (*) <=" + month_number;
			}

			if (customer_type.equals("metered")) {/*
												 * defaulterListSql=
												 * "select * from  " + "( " +
												 * "SELECT billMiter.CUSTOMER_ID,mci.FULL_NAME||' & '||mci.ADDRESS_LINE1 ADDRESS,mci.PHONE CONTACT_NO,CATEGORY_ID,CATEGORY_NAME,mci.AREA_ID, "
												 * + "         LISTAGG ( " +
												 * "               TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON') "
												 * + "            || ' ' " +
												 * "            || SUBSTR (BILL_YEAR, 3), "
												 * + "            ',') " +
												 * "         WITHIN GROUP (ORDER BY BILL_YEAR asc,BILL_MONTH asc) "
												 * + "            AS DUEMONTH, "
												 * +
												 * "         (SELECT SUM (BILLED_AMOUNT) "
												 * +
												 * "            FROM BILL_METERED "
												 * +
												 * "           WHERE CUSTOMER_ID = billMiter.CUSTOMER_ID AND STATUS = 1) "
												 * +
												 * "            AS TOTAL_AMOUNT, "
												 * +
												 * "         (SELECT COUNT (BILL_MONTH) "
												 * +
												 * "            FROM BILL_METERED "
												 * +
												 * "           WHERE CUSTOMER_ID = billMiter.CUSTOMER_ID AND STATUS = 1) "
												 * +
												 * "            AS TOTAL_MONTH,"
												 * +
												 * "			 cc.MIN_LOAD,cc.MAX_LOAD, "
												 * +
												 * "            decode(CONNECTION_STATUS,'1','ACTIVE','INACTIVE') STATUS "
												 * +
												 * "    FROM BILL_METERED billMiter,MVIEW_CUSTOMER_INFO mci,customer_connection cc "
												 * +
												 * "   WHERE billMiter.CUSTOMER_ID=MCI.CUSTOMER_ID "
												 * +
												 * "	AND CC.CUSTOMER_ID=MCI.CUSTOMER_ID"
												 * +
												 * "   AND billMiter.STATUS = 1 "
												 * +
												 * "   and billMiter.BILL_YEAR||lpad(billMiter.BILL_MONTH,2,0) <= "
												 * +month_year+" "+
												 * "GROUP BY billMiter.CUSTOMER_ID, "
												 * +
												 * "mci.FULL_NAME,mci.ADDRESS_LINE1,mci.PHONE,CONNECTION_STATUS,CATEGORY_ID,CATEGORY_NAME,mci.AREA_ID,cc.MIN_LOAD,cc.MAX_LOAD "
												 * + ") tabl1 " +
												 * "where "+wClause
												 * +criteriaWclause+
												 * "order by customer_id " ;
												 */
				// query has been changed according to current needs ~ dec 14 ~
				// Prince

				defaulterListSql = "SELECT tmp1.CUSTOMER_ID, "
						+ "				 tmp1.CATEGORY_ID," + "				 tmp2.CATEGORY_NAME,"
						+ "				 tmp1.STATUS," + "				 TMP2.FULL_NAME,"
						+ "				 tmp2.ADDRESS,";
				if (moholla_wise.equals("1")) {
					defaulterListSql += "      TMP4.ZONE, "
							+ "                TMP4.ZONE_NAME, ";
				}
				defaulterListSql += "      tmp2.CONTACT_NO,"
						+ "				 TMP3.MIN_LOAD,"
						+ "				 TMP3.MAX_LOAD,"
						+ "				 tmp1.DUEMONTH,"
						+ "				 tmp1.TOTAL_AMOUNT,"
						+ "				 tmp1.TOTAL_MONTH"
						+ "  FROM (  SELECT bi.CUSTOMER_ID, "
						+ "                 CUSTOMER_CATEGORY CATEGORY_ID, "
						+ "                 bi.AREA_ID, "
						+ "                 cc.STATUS, "
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
						+ "                    TOTAL_AMOUNT, "
						+ "                 COUNT (*) TOTAL_MONTH "
						+ "            FROM BILL_METERED bi, CUSTOMER_CONNECTION cc "
						+ "           WHERE     BI.CUSTOMER_ID = CC.CUSTOMER_ID "
						+ "                 "
						+ status_Sql
						+ "                 AND bi.STATUS = 1 "
						+ "                 AND "
						+ wClause
						+ "                 AND BILL_YEAR || LPAD (BILL_MONTH, 2, 0) <= "
						+ month_year
						+ "        GROUP BY BI.CUSTOMER_ID, CUSTOMER_CATEGORY, bi.AREA_ID, cc.STATUS "
						+ criteriaWclause
						+ "          ) tmp1, "
						+ "       (SELECT AA.CUSTOMER_ID, "
						+ "               BB.FULL_NAME, "
						+ "               BB.MOBILE CONTACT_NO, "
						+ "               AA.ADDRESS_LINE1 ADDRESS, "
						+ "               AA.ADDRESS_LINE2, MCC.CATEGORY_NAME "
						+ "          FROM CUSTOMER_ADDRESS aa, CUSTOMER_PERSONAL_INFO bb, MST_CUSTOMER_CATEGORY mcc "
						+ "         WHERE AA.CUSTOMER_ID = BB.CUSTOMER_ID AND MCC.CATEGORY_ID= SUBSTR(AA.CUSTOMER_ID,3,2) ) tmp2,"
						+ "         CUSTOMER_CONNECTION tmp3";
				if (moholla_wise.equals("1")) {
					defaulterListSql += ",         (SELECT C.CUSTOMER_ID, "
							+ "                 C.AREA, "
							+ "                 C.ZONE, "
							+ "                 MZ.ZONE_NAME "
							+ "            FROM CUSTOMER C, MST_ZONE MZ "
							+ "           WHERE C.AREA = MZ.AREA_ID AND C.ZONE = MZ.ZONE_ID) TMP4 ";
				}
				defaulterListSql += " WHERE tmp1.CUSTOMER_ID = tmp2.CUSTOMER_ID "
						+ " AND tmp1.CUSTOMER_ID = TMP3.CUSTOMER_ID";
				if (moholla_wise.equals("1")) {
					defaulterListSql += "         AND tmp1.CUSTOMER_ID = TMP4.CUSTOMER_ID "
							+ "ORDER BY TMP4.ZONE, tmp1.STATUS, tmp1.CUSTOMER_ID ASC ";
				} else {
					defaulterListSql += " ORDER BY tmp1.CATEGORY_ID , tmp1.STATUS, tmp1.TOTAL_MONTH DESC";
				}

			} else {
				defaulterListSql = "SELECT tmp1.CUSTOMER_ID, "
						+ "				 tmp1.CATEGORY_ID," + "				 tmp2.CATEGORY_NAME,"
						+ "				 tmp1.STATUS," + "				 TMP2.FULL_NAME,"
						+ "				 tmp2.ADDRESS,";
				if (moholla_wise.equals("1")) {
					defaulterListSql += "      TMP4.ZONE, "
							+ "                TMP4.ZONE_NAME, ";
				}
				defaulterListSql += "      tmp2.CONTACT_NO,"
						+ "				 TMP3.MIN_LOAD,"
						+ "				 TMP3.MAX_LOAD,"
						+ "				 tmp1.DUEMONTH,"
						+ "				 tmp1.TOTAL_AMOUNT,"
						+ "				 tmp1.TOTAL_MONTH"
						+ "  FROM (  SELECT bi.CUSTOMER_ID, "
						+ "                 CUSTOMER_CATEGORY CATEGORY_ID, "
						+ "                 bi.AREA_ID, "
						+ "                 cc.STATUS, "
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
						+ "                    TOTAL_AMOUNT, "
						+ "                 COUNT (*) TOTAL_MONTH "
						+ "            FROM BILL_NON_METERED bi, CUSTOMER_CONNECTION cc "
						+ "           WHERE     BI.CUSTOMER_ID = CC.CUSTOMER_ID "
						+ "                 "
						+ status_Sql
						+ "                 AND bi.STATUS = 1 "
						+ "                 AND "
						+ wClause
						+ "                 AND BILL_YEAR || LPAD (BILL_MONTH, 2, 0) <= "
						+ month_year
						+ "        GROUP BY BI.CUSTOMER_ID, CUSTOMER_CATEGORY, bi.AREA_ID, cc.STATUS "
						+ criteriaWclause
						+ "          ) tmp1, "
						+ "       (SELECT AA.CUSTOMER_ID, "
						+ "               BB.FULL_NAME, "
						+ "               BB.MOBILE CONTACT_NO, "
						+ "               AA.ADDRESS_LINE1 ADDRESS, "
						+ "               AA.ADDRESS_LINE2, MCC.CATEGORY_NAME "
						+ "          FROM CUSTOMER_ADDRESS aa, CUSTOMER_PERSONAL_INFO bb, MST_CUSTOMER_CATEGORY mcc "
						+ "         WHERE AA.CUSTOMER_ID = BB.CUSTOMER_ID AND MCC.CATEGORY_ID= SUBSTR(AA.CUSTOMER_ID,3,2) ) tmp2,"
						+ "         CUSTOMER_CONNECTION tmp3";
				if (moholla_wise.equals("1")) {
					defaulterListSql += ",         (SELECT C.CUSTOMER_ID, "
							+ "                 C.AREA, "
							+ "                 C.ZONE, "
							+ "                 MZ.ZONE_NAME "
							+ "            FROM CUSTOMER C, MST_ZONE MZ "
							+ "           WHERE C.AREA = MZ.AREA_ID AND C.ZONE = MZ.ZONE_ID) TMP4 ";
				}
				defaulterListSql += " WHERE tmp1.CUSTOMER_ID = tmp2.CUSTOMER_ID "
						+ " AND tmp1.CUSTOMER_ID = TMP3.CUSTOMER_ID";
				if (moholla_wise.equals("1")) {
					defaulterListSql += "         AND tmp1.CUSTOMER_ID = TMP4.CUSTOMER_ID "
							+ "ORDER BY TMP4.ZONE, tmp1.STATUS, tmp1.CUSTOMER_ID ASC ";
				} else {
					defaulterListSql += " ORDER BY tmp1.CATEGORY_ID , tmp1.STATUS, tmp1.TOTAL_MONTH DESC";
				}

			}

			PreparedStatement ps1 = conn.prepareStatement(defaulterListSql);

			ResultSet resultSet = ps1.executeQuery();

			while (resultSet.next()) {
				DefaulterDto defaulterDto = new DefaulterDto();
				defaulterDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
				defaulterDto.setCategory_id(resultSet.getString("CATEGORY_ID"));
				defaulterDto.setCategory_name(resultSet
						.getString("CATEGORY_NAME"));
				defaulterDto.setStatus(resultSet.getString("STATUS"));
				defaulterDto.setFull_name(resultSet.getString("FULL_NAME"));
				defaulterDto.setAddress(resultSet.getString("ADDRESS"));
				defaulterDto.setContact_no(resultSet.getString("CONTACT_NO"));
				defaulterDto.setDue_month(resultSet.getString("DUEMONTH"));
				defaulterDto.setAmount(resultSet.getFloat("TOTAL_AMOUNT"));
				defaulterDto.setTotal_month(resultSet.getInt("TOTAL_MONTH"));

				if (customer_type.equals("metered")) {
					defaulterDto.setMaxLoad(resultSet.getDouble("MAX_LOAD"));
					defaulterDto.setMinLoad(resultSet.getDouble("MIN_LOAD"));
				}

				if (moholla_wise.equals("1")) {
					defaulterDto.setMoholla_name(resultSet
							.getString("ZONE_NAME"));

				}

				defaulterInfoList.add(defaulterDto);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return defaulterInfoList;
	}

	public ArrayList<CustomerCategoryDTO> getCustomerCategoryList() {
		return customerCategoryList;
	}

	public void setCustomerCategoryList(
			ArrayList<CustomerCategoryDTO> customerCategoryList) {
		this.customerCategoryList = customerCategoryList;
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

	public String getCriteria_type() {
		return criteria_type;
	}

	public void setCriteria_type(String criteria_type) {
		this.criteria_type = criteria_type;
	}

	public String getMonth_number() {
		return month_number;
	}

	public void setMonth_number(String month_number) {
		this.month_number = month_number;
	}

	public String getCustomer_type() {
		return customer_type;
	}

	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMoholla_wise() {
		return moholla_wise;
	}

	public void setMoholla_wise(String moholla_wise) {
		this.moholla_wise = moholla_wise;
	}

	
	
	
	
	
	//for logo
	
	public ServletContext getServlet() {
		  return servlet;
		 }

		 public void setServletContext(ServletContext servlet) {
		  this.servlet = servlet;
		 }


	
	
	
	
}
