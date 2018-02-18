package org.jgtdsl.utils.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.MinistryDTO;
import org.jgtdsl.dto.ReconciliationDTO;
import org.jgtdsl.enums.AccountReconciliation;
import org.jgtdsl.enums.BankAccountTransactionMode;
import org.jgtdsl.enums.BankAccountTransactionType;
import org.jgtdsl.enums.BankMarginPaymentParticualers;
import org.jgtdsl.enums.BankPaymentParticulars;
import org.jgtdsl.enums.BankReceiveParticualrs;
import org.jgtdsl.enums.BillPurpose;
import org.jgtdsl.enums.ConnectionStatus;
import org.jgtdsl.enums.ConnectionType;
import org.jgtdsl.enums.DepositPurpose;
import org.jgtdsl.enums.DepositType;
import org.jgtdsl.enums.DisconnCause;
import org.jgtdsl.enums.DisconnCauseNonMeter;
import org.jgtdsl.enums.DisconnType;
import org.jgtdsl.enums.LoadChangeType;
import org.jgtdsl.enums.MeterMeasurementType;
import org.jgtdsl.enums.MeteredStatus;
import org.jgtdsl.enums.Month;
import org.jgtdsl.enums.ReadingPurpose;
import org.jgtdsl.enums.ReconciliationCasuseLess;
import org.jgtdsl.enums.ReconciliationCauseAdd;
import org.jgtdsl.enums.UserRole;

import org.jgtdsl.models.AddressService;
import org.jgtdsl.models.AreaService;
import org.jgtdsl.models.BankBranchService;
import org.jgtdsl.models.BurnerQntChangeService;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.EmployeeService;
import org.jgtdsl.models.MeterService;
import org.jgtdsl.models.MinistryService;
import org.jgtdsl.models.ReconciliationService;
import org.jgtdsl.models.ZoneService;
import org.jgtdsl.reports.masterData.Employee;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;


public class StartupResources  extends HttpServlet {
	
	private static final long serialVersionUID = -7227671581272961718L;
	private static final Logger logger = LogManager.getLogger(StartupResources.class.getName());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws ServletException, IOException {
		  
	  }

	public void init(ServletConfig config)  throws ServletException 
	{			
		
		loadAndSetEnvironmentConfig(config);		
		
		CustomerService customerService=new CustomerService();
				
		ArrayList<CustomerCategoryDTO> customerCategoryList=customerService.getCustomerCategoryList(0,0,"status=1",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		ArrayList<CustomerCategoryDTO> nonMeteredcustomerCategoryList=customerService.getCustomerCategoryList(0,0,"status=1 and CATEGORY_ID='01' or CATEGORY_ID='09'",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		ArrayList<CustomerCategoryDTO> meteredcustomerCategoryList=customerService.getCustomerCategoryList(0,0,"status=1 and CATEGORY_ID<>'01' and CATEGORY_ID<>'09'",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);		
		
			ArrayList<MinistryDTO> MinistryList;
			try {
				MinistryList = customerService.getMinistryCategory();
				config.getServletContext().setAttribute("MINISTRY_CUSTOMER_CATEGORY", MinistryList);				
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}								
		config.getServletContext().setAttribute("ACTIVE_CUSTOMER_CATEGORY",customerCategoryList);
		// Non meter customer category list
		config.getServletContext().setAttribute("ACTIVE_NON_CUSTOMER_CATEGORY",nonMeteredcustomerCategoryList);
		// meter customer category list
		config.getServletContext().setAttribute("ACTIVE_METERED_CUSTOMER_CATEGORY",meteredcustomerCategoryList);
		
		config.getServletContext().setAttribute("ACTIVE_AREA",AreaService.getAreaList(0,0,"status=1",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0));
		config.getServletContext().setAttribute("ALL_MINISTRY",MinistryService.getAllMinistry());
		//config.getServletContext().setAttribute("ALL_APPLIANCE",BurnerQntChangeService.getAllAppliance());
		
		//CacheUtil.setListToCache("ALL_DIVISION", AddressService.getAllDivision());
		config.getServletContext().setAttribute("ALL_DIVISION",AddressService.getAllDivision());
		
		//config.getServletContext().setAttribute("ALL_BANK",BankBranchService.getBankList(0,0,"",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0));
		

		ZoneService zoneService=new ZoneService();
		config.getServletContext().setAttribute("ALL_ZONE",zoneService.getZoneList(Utils.EMPTY_STRING));

		
		try {Thread.sleep(2000);logger.info("Startup Thread Sleep(2 sec.)");} 
		catch (InterruptedException e) {e.printStackTrace();}
		
		MeterService meterService=new MeterService();
		config.getServletContext().setAttribute("ACTIVE_METER_TYPE",meterService.getMeterTypeList(0, 0, "status=1", "TYPE_NAME", "ASC", 0));
		config.getServletContext().setAttribute("METER_STATUS",meterService.getMeterStatus());
		config.getServletContext().setAttribute("ALL_EMPLOYEE",EmployeeService.getEmployeeList(0, 0, "status=1", "FULL_NAME", "ASC", 0));
		
		try {Thread.sleep(2000);logger.info("Startup Thread Sleep(2 sec.)");} 
		catch (InterruptedException e) {e.printStackTrace();}
		
		/*ArrayList<ReconciliationDTO> causeList=ReconciliationService.getReconciliationCauseList(0, 0, "", "", "ASC", 0);*/
		config.getServletContext().setAttribute("ALL_BANK",BankBranchService.getBankList(0, 0, "bank.status=1", "BANK_NAME", "ASC", 0));
		/*config.getServletContext().setAttribute("ALL_RECONCILIATION_CAUSE",causeList);*/
		
//		ArrayList<ChangeTypeDTO> changeTypeList=new ArrayList<ChangeTypeDTO>();
//		ChangeTypeDTO[] typeArr = {new ChangeTypeDTO("L","Load Change"),new ChangeTypeDTO("M","Meter Change"),new ChangeTypeDTO("MR","Meter Rent Change"),new ChangeTypeDTO("P","Pressure Change"),new ChangeTypeDTO("L&P","Load & Pressure Change")};		
//		Collections.addAll(changeTypeList, typeArr);
//		config.getServletContext().setAttribute("CHANGE_TYPE",changeTypeList);
		
		HashMap<String, String>  customerCategoryMap = new HashMap<String, String>();		
		for(int i=0;i<customerCategoryList.size();i++)
			customerCategoryMap.put(customerCategoryList.get(i).getCategory_id(), customerCategoryList.get(i).getCategory_name());
		config.getServletContext().setAttribute("CUSTOMER_CATEGORY_MAP", customerCategoryMap);
		
				
		// Storing Enum Values in Servlet Context
		config.getServletContext().setAttribute("DISCONN_CAUSE",DisconnCause.values());
		config.getServletContext().setAttribute("DISCONN_CAUSE_NONMETER",DisconnCauseNonMeter.values());
		config.getServletContext().setAttribute("DISCONN_TYPE",DisconnType.values());
		config.getServletContext().setAttribute("METERED_STATUS",MeteredStatus.values());
		config.getServletContext().setAttribute("LOAD_CHANGE_TYPE",LoadChangeType.values());
		config.getServletContext().setAttribute("DEPOSIT_TYPE",DepositType.values());
		config.getServletContext().setAttribute("DEPOSIT_PURPOSE",DepositPurpose.values());
		config.getServletContext().setAttribute("CONNECTION_STATUS",ConnectionStatus.values());
		config.getServletContext().setAttribute("CONNECTION_TYPE",ConnectionType.values());
		config.getServletContext().setAttribute("BILL_PURPOSE",BillPurpose.values());
		config.getServletContext().setAttribute("READING_PURPOSE",ReadingPurpose.values());
		config.getServletContext().setAttribute("MONTHS",Month.values());
		config.getServletContext().setAttribute("USERROLE",UserRole.values());
		config.getServletContext().setAttribute("METER_MEASUREMENT_TYPE",MeterMeasurementType.values());
		config.getServletContext().setAttribute("TRANSACTION_MODE",BankAccountTransactionMode.values());
		config.getServletContext().setAttribute("TRANSACTION_TYPE",BankAccountTransactionType.values());
		config.getServletContext().setAttribute("BANK_RECEIVE_PARTICULARS",BankReceiveParticualrs.values());
		config.getServletContext().setAttribute("BANK_PAYMENT_PARTICULARS",BankPaymentParticulars.values());	
		config.getServletContext().setAttribute("BANK_MARGIN_PAYMENT_PARTICULARS",BankMarginPaymentParticualers.values());
		config.getServletContext().setAttribute("ALL_RECONCILIATION_CAUSE_ADD",ReconciliationCauseAdd.values());
		config.getServletContext().setAttribute("ALL_RECONCILIATION_CAUSE_LESS",ReconciliationCasuseLess.values());
		config.getServletContext().setAttribute("ALL_RECONCILIATION_ACCOUNT",AccountReconciliation.values());
		
		

		
		
		config.getServletContext().setAttribute("YEARS",getYearList());
		config.getServletContext().setAttribute("FISCAL_YEARS",getFiscalYearList());
		config.getServletContext().setAttribute("CONNECTION_SIZE",getConnectionSize());
				

	}

	public ArrayList<String> getYearList(){
		ArrayList<String> yearList=new ArrayList<String>();
		int year=Calendar.getInstance().get(Calendar.YEAR)+1;
		for(int i=year;i>year-15;i--){
			yearList.add(String.valueOf(i));
		}
		return yearList;
	}
	
	public ArrayList<String> getFiscalYearList(){
		ArrayList<String> yearList=new ArrayList<String>();
		int year=Calendar.getInstance().get(Calendar.YEAR)+1;
		for(int i=year;i>year-15;i--){
			yearList.add(String.valueOf(i-1)+"-"+String.valueOf(i));
		}
		return yearList;
	}
	public ArrayList<String> getConnectionSize(){
		ArrayList<String> sizeList=new ArrayList<String>();
		sizeList.add(new String(".75"));
		sizeList.add(new String("1"));
		sizeList.add(new String("2"));
		sizeList.add(new String("3"));
		sizeList.add(new String("4"));
		sizeList.add(new String("6"));
		sizeList.add(new String("8"));
		return sizeList;
	}
	public void loadAndSetEnvironmentConfig(ServletConfig config){
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			
		    Context ctx = new InitialContext();
		    ctx = (Context) ctx.lookup("java:comp/env");
		    String env = (String) ctx.lookup("environment");		    
		    String configFileLocation=config.getServletContext().getRealPath(Utils.EMPTY_STRING)+File.separator+"WEB-INF"+File.separator+"classes"+File.separator;
		    input = new FileInputStream(configFileLocation+env+".config.properties");
		    prop.load(input);
		    if(env.equalsIgnoreCase("dev")){
				config.getServletContext().setAttribute("PHOTO_DIR",prop.getProperty("photo.dir"));
			}
		}
		catch (NamingException e) {
		    logger.error("Naming Exception",e);
		}
		catch (IOException ex) {
			logger.error("IO Exception",ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		

	}
}