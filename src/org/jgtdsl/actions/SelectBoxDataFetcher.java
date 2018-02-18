package org.jgtdsl.actions;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jgtdsl.dto.AccountDTO;
import org.jgtdsl.dto.AddressDTO;
import org.jgtdsl.dto.AreaDTO;
import org.jgtdsl.dto.AutoCompleteObject;
import org.jgtdsl.dto.BankDTO;
import org.jgtdsl.dto.BranchDTO;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.DesignationDTO;
import org.jgtdsl.dto.OrgDepartmentDTO;
import org.jgtdsl.dto.OrgDivisionDTO;
import org.jgtdsl.dto.OrgSectionDTO;
import org.jgtdsl.dto.RoleDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.dto.ZoneDTO;
import org.jgtdsl.models.AccountService;
import org.jgtdsl.models.AddressService;
import org.jgtdsl.models.AreaService;
import org.jgtdsl.models.BankBranchService;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.DesignationService;
import org.jgtdsl.models.OrganizationService;
import org.jgtdsl.models.ZoneService;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
//import org.jgtdsl.models.MohollahService;

public class SelectBoxDataFetcher extends BaseAction implements SessionAware{

	private static final long serialVersionUID = 1038296861959983719L;
	private String bank_id;
	private String branch_id;
	private String area_id;
	private String division_id;
	private String district_id;
	private String department_id;
	private String section_id;
	private String customer_id;
	private String customer_name;
	private String query;
	private String type;
	private String zone_id;
	private String zone_name;
	
	
	public String fetchAllRoleTypes(){
		RoleService roleService=new RoleService();
		StringBuilder roles= new StringBuilder(Utils.EMPTY_STRING);
		ArrayList<RoleDTO> roleList=roleService.getRoleList();
		for(int i=0;i<roleList.size();i++){
			roles.append(roleList.get(i).getRole_id())
			 .append(Utils.SBOX_LABEL_SEPERATOR)
			 .append(roleList.get(i).getRole_name())
			 .append(Utils.SBOX_ITEM_SEPERATOR);
		}
		if(roles.length()>0)
			roles.deleteCharAt(roles.length()-1);
		try{
	    	 response.setContentType("json");
	    	 response.getWriter().write(roles.toString());
	    }
	    catch(Exception e) {e.printStackTrace();}
	        
	    return null;
	}
	
	public String fetchDesignations(){
		
		DesignationService degService=new DesignationService();
		StringBuilder designations= new StringBuilder(Utils.EMPTY_STRING);
		ArrayList<DesignationDTO> designationList=degService.getDesignationList();
		for(int i=0;i<designationList.size();i++){
			designations.append(designationList.get(i).getDesignation_id())
			 .append(Utils.SBOX_LABEL_SEPERATOR)
			 .append(designationList.get(i).getDesignation_name())
			 .append("("+designationList.get(i).getShort_term()+")")
			 .append(Utils.SBOX_ITEM_SEPERATOR);
		}
		if(designations.length()>0)
			designations.deleteCharAt(designations.length()-1);

		 try{
	    	 response.setContentType("json");
	    	 response.getWriter().write(designations.toString());
	          }
	        catch(Exception e) {e.printStackTrace();}
	     
		return null;
		}
	
		public String fetchCustomerCategory(){
			CustomerService customerService=new CustomerService();
			String categories=Utils.EMPTY_STRING;
			ArrayList<CustomerCategoryDTO> categoryList=customerService.getCustomerCategoryList(0, 0, Utils.EMPTY_STRING , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			for(int i=0;i<categoryList.size();i++){
				categories+=categoryList.get(i).getCategory_id()+":"+categoryList.get(i).getCategory_name()+";";
			}
			if(!categories.equals(Utils.EMPTY_STRING))
				categories=categories.substring(0, categories.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(categories);
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
			}

		public String fetchBanks(){
			String banks=Utils.EMPTY_STRING;
			ArrayList<BankDTO> bankList=BankBranchService.getBankList(0, 0, Utils.EMPTY_STRING , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			for(int i=0;i<bankList.size();i++){
				banks+=bankList.get(i).getBank_id()+":"+bankList.get(i).getBank_name()+";";
			}
			if(!banks.equals(Utils.EMPTY_STRING))
				banks=banks.substring(0, banks.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(banks);
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
			}
		
		public String fetchAllBanks(){
			String banks=Utils.EMPTY_STRING;
			ArrayList<BankDTO> bankList=BankBranchService.getAllBankList(0, 0, Utils.EMPTY_STRING , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			for(int i=0;i<bankList.size();i++){
				banks+=bankList.get(i).getBank_id()+":"+bankList.get(i).getBank_name()+";";
			}
			if(!banks.equals(Utils.EMPTY_STRING))
				banks=banks.substring(0, banks.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(banks);
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
			}		
		
		
		
		//fetch zone info - dec,05
		
		public String fetchZones(){
			AreaService areaService=new AreaService();
			UserDTO loggedInUser=(UserDTO)session.get("user");
			
			String zones=Utils.EMPTY_STRING;
			String whereClause=Utils.EMPTY_STRING;
			if(!area_id.equalsIgnoreCase("0"))
			{
				
					whereClause=" Area_ID='"+area_id+"'";
			}else	
			{
				whereClause=" Area_Id='"+loggedInUser.getArea_id()+"'";
				
					
			}

			ArrayList<ZoneDTO> zoneList=areaService.getZoneList(0, 0, whereClause , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			
			
			for(int i=0;i<zoneList.size();i++){
				zones+=zoneList.get(i).getZone_id()+":"+zoneList.get(i).getZone_name()+";";
			}
			if(!zones.equals(Utils.EMPTY_STRING))
				zones=zones.substring(0, zones.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(zones);
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
		}
		
		
		//end of fetch zone info
		
		
		
		
		
		
		
		
		
		
		
		public String fetchBranches(){
			BankBranchService bankService=new BankBranchService();
			UserDTO loggedInUser=(UserDTO)session.get("user");
			if(type==null)
			{
				type="";
			}
			//added later
			if(loggedInUser.getDesignation_id().equals("02")){
				type="1";
			}
			
			String branches=Utils.EMPTY_STRING;
			String whereClause=Utils.EMPTY_STRING;
			if(!Utils.isNullOrEmpty(bank_id))
			{
				if(type.equalsIgnoreCase("1")){
					whereClause=" Bank_Id='"+bank_id+"'";
				}else
				{
					whereClause=" Bank_Id='"+bank_id+"' And Area_Id='"+loggedInUser.getArea_id()+"'";
				}
					
			}

			ArrayList<BranchDTO> branchList=bankService.getBranchList(0, 0, whereClause , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			for(int i=0;i<branchList.size();i++){
				branches+=branchList.get(i).getBranch_id()+":"+branchList.get(i).getBranch_name()+";";
			}
			if(!branches.equals(Utils.EMPTY_STRING))
				branches=branches.substring(0, branches.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(branches);
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
		}
		

		
		
		/**
		 * Modified by Prince
		 * @return
		 */
		public String fetchAccounts(){
			AccountService accountService=new AccountService();
			String accounts=Utils.EMPTY_STRING;
			String whereClause=Utils.EMPTY_STRING;
			
			if(type==null)
			{
				type="";
			}
			
			if(!Utils.isNullOrEmpty(branch_id)){
				if(type.equalsIgnoreCase("securityAccount")){
					whereClause=" Branch_id='"+branch_id+"' And ACCOUNT_NAME like '%SECURITY%' ";	
				}else if(type.equalsIgnoreCase("01")||type.equalsIgnoreCase("02"))
				{
					whereClause=" Branch_id='"+branch_id+"' And ACCOUNT_NAME like '%DOMESTIC%' ";	
					
				}else if(type.equalsIgnoreCase("03")||type.equalsIgnoreCase("04"))
				{
					whereClause=" Branch_id='"+branch_id+"' And ACCOUNT_NAME like '%COMMERCIAL%' ";	
					
				}else if(type.equalsIgnoreCase("05")||type.equalsIgnoreCase("06")||type.equalsIgnoreCase("07")||type.equalsIgnoreCase("08")||type.equalsIgnoreCase("09")||type.equalsIgnoreCase("10")||type.equalsIgnoreCase("11")||type.equalsIgnoreCase("12"))
				{
					whereClause=" Branch_id='"+branch_id+"' And ACCOUNT_NAME like '%INDUSTRIAL%' ";	
					
				}
				else
				{
					whereClause=" Branch_id='"+branch_id+"'";
				}
			}
//solving - arrival of account no. when bank or branch is not selected ~sept 23 ~ Prince
			ArrayList<AccountDTO> accountList= new ArrayList<AccountDTO>();
			if(!Utils.isNullOrEmpty(whereClause)){
				 accountList=accountService.getAccountList(0, 0, whereClause , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			}
			//ArrayList<AccountDTO> accountList=accountService.getAccountList(0, 0, whereClause , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			for(int i=0;i<accountList.size();i++){
				//accounts+=accountList.get(i).getAccount_no()+":"+accountList.get(i).getAccount_name()+" ["+accountList.get(i).getAccount_no()+"];";
				//no need for account name+no. ~ sept 20 ~ Prince
				accounts+=accountList.get(i).getAccount_no()+":"+accountList.get(i).getAccount_no();
				//System.out.println(accounts);
			}
			if(!accounts.equals(Utils.EMPTY_STRING))
				//accounts=accounts.substring(0, accounts.length()-1);
				accounts=accounts.substring(0, accounts.length());

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(accounts);
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
		}
		
		
		
		public String fetchAreas(){
			AreaService areaService=new AreaService();
			String areas=Utils.EMPTY_STRING;
			ArrayList<AreaDTO> areaList=areaService.getAreaList(0, 0, Utils.EMPTY_STRING , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			for(int i=0;i<areaList.size();i++){
				areas+=areaList.get(i).getArea_id()+":"+areaList.get(i).getArea_name()+";";
			}
			if(!areas.equals(Utils.EMPTY_STRING))
				areas=areas.substring(0, areas.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(areas);
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
			}
		
		public String fetchZone(){
			ZoneService zoneService=new ZoneService();
			StringBuilder zones= new StringBuilder(Utils.EMPTY_STRING);
			
			ArrayList<ZoneDTO> zoneList=zoneService.getZoneList(area_id);
			for(int i=0;i<zoneList.size();i++){
				zones.append(zoneList.get(i).getZone_id())
				 .append(Utils.SBOX_LABEL_SEPERATOR)
				 .append(zoneList.get(i).getZone_name())
				 .append(Utils.SBOX_ITEM_SEPERATOR);
			}
			
			if(zones.length()>0)
				zones.deleteCharAt(zones.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(zones.toString());
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
			}
		
		public String fetchOrgDivisions(){
			OrganizationService orgService=new OrganizationService();
			StringBuilder divisions= new StringBuilder(Utils.EMPTY_STRING);
			ArrayList<OrgDivisionDTO> divisionList=orgService.getDivisionList(0, 0, Utils.EMPTY_STRING , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			for(int i=0;i<divisionList.size();i++){
				divisions.append(divisionList.get(i).getDivision_id())
						 .append(Utils.SBOX_LABEL_SEPERATOR)
						 .append(divisionList.get(i).getDivision_name())
						 .append(Utils.SBOX_ITEM_SEPERATOR);
			}
			if(divisions.length()>0)
				divisions.deleteCharAt(divisions.length()-1);
			
			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(divisions.toString());
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
			}
		
		public String fetchOrgDepartments(){
			OrganizationService orgService=new OrganizationService();
			StringBuilder departments= new StringBuilder(Utils.EMPTY_STRING);
			String whereClause=Utils.EMPTY_STRING;
			if(!Utils.isNullOrEmpty(division_id))
				whereClause=" division_id='"+division_id+"'";
			ArrayList<OrgDepartmentDTO> departmentList=orgService.getDepartmentList(0, 0, whereClause , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			for(int i=0;i<departmentList.size();i++){
				departments.append(departmentList.get(i).getDepartment_id())
				 .append(Utils.SBOX_LABEL_SEPERATOR)
				 .append(departmentList.get(i).getDepartment_name())
				 .append(Utils.SBOX_ITEM_SEPERATOR);
			}
			if(departments.length()>0)
				departments.deleteCharAt(departments.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(departments.toString());
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
		}
		
		public String fetchOrgSections(){
			OrganizationService orgService=new OrganizationService();
			StringBuilder sections= new StringBuilder(Utils.EMPTY_STRING);
			String whereClause=Utils.EMPTY_STRING;
			if(!Utils.isNullOrEmpty(department_id))
				whereClause=" department_id='"+department_id+"'";
			ArrayList<OrgSectionDTO> sectionList=orgService.getSectionList(0, 0, whereClause , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			for(int i=0;i<sectionList.size();i++){
				sections.append(sectionList.get(i).getSection_id())
				 .append(Utils.SBOX_LABEL_SEPERATOR)
				 .append(sectionList.get(i).getSection_name())
				 .append(Utils.SBOX_ITEM_SEPERATOR);
			}
			if(sections.length()>0)
				sections.deleteCharAt(sections.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(sections.toString());
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
		}
		
		
		public String fetchDivision(){

			StringBuilder divisions= new StringBuilder(Utils.EMPTY_STRING);
			ArrayList<AddressDTO> divisionList=AddressService.getAllDivision();

			for(int i=0;i<divisionList.size();i++){
				divisions.append(divisionList.get(i).getDivision_id())
						.append(Utils.SBOX_LABEL_SEPERATOR)
						.append(divisionList.get(i).getDivision_name())
						.append(Utils.SBOX_ITEM_SEPERATOR);
			}
			if(divisions.length()>0)
				divisions.deleteCharAt(divisions.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(divisions.toString());
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;

		}

		public String fetchDistrict(){
			
			AddressService addressService=new AddressService();						
			StringBuilder districts= new StringBuilder(Utils.EMPTY_STRING);
			ArrayList<AddressDTO> districtList=addressService.getDistrict(division_id);

			for(int i=0;i<districtList.size();i++){
				districts.append(districtList.get(i).getDistrict_id())
						.append(Utils.SBOX_LABEL_SEPERATOR)
						.append(districtList.get(i).getDistrict_name())
						.append(Utils.SBOX_ITEM_SEPERATOR);
			}
			if(districts.length()>0)
				districts.deleteCharAt(districts.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(districts.toString());
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
		}
		
		public String fetchUpazila(){

			AddressService addressService=new AddressService();						
			StringBuilder upazilas= new StringBuilder(Utils.EMPTY_STRING);
			ArrayList<AddressDTO> upazilaList=addressService.getUpazila(district_id);

			for(int i=0;i<upazilaList.size();i++){
				upazilas.append(upazilaList.get(i).getUpazila_id())
						.append(Utils.SBOX_LABEL_SEPERATOR)
						.append(upazilaList.get(i).getUpazila_name())
						.append(Utils.SBOX_ITEM_SEPERATOR);
			}
			if(upazilas.length()>0)
				upazilas.deleteCharAt(upazilas.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(upazilas.toString());
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;

		}
 /*		public String fetchMohollahs(){
			AreaService areaService=new AreaService();
			MohollahService mohollahService=new MohollahService();
			String areas=Utils.EMPTY_STRING;
			ArrayList<AreaDTO> areaList=areaService.getAreaList(0, 0, Utils.EMPTY_STRING , Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
			for(int i=0;i<areaList.size();i++){
				areas+=areaList.get(i).getArea_id()+":"+areaList.get(i).getArea_name()+";";
			}
			if(!areas.equals(Utils.EMPTY_STRING))
				areas=areas.substring(0, areas.length()-1);

			 try{
		    	 response.setContentType("json");
		    	 response.getWriter().write(areas);
		          }
		        catch(Exception e) {e.printStackTrace();}
		     
			return null;
			}	*/
		
		
		@SuppressWarnings("unchecked")
		public String fetchCustomerIdList(){
			
			Collection<AutoCompleteObject> allCustomer=null;
			String area_str=((UserDTO)session.get("user")).getArea_id();
			area_str=(area_str==null?"":area_str);
			CustomerService customerService=new  CustomerService();
			
			if(type.equalsIgnoreCase("all")){
				
				if(CacheUtil.getListFromCache("ALL_CUSTOMER_ID_"+area_str, AutoCompleteObject.class)==null){
					CacheUtil.setListToCache(Utils.constructCacheKey("ALL_CUSTOMER_ID_"+area_str),customerService.getCustomerListForAutoComplete("ALL",area_str));
					allCustomer= CacheUtil.getListFromCache("ALL_CUSTOMER_ID_"+area_str,AutoCompleteObject.class);
				}
				else					
					allCustomer= CacheUtil.getListFromCache("ALL_CUSTOMER_ID_"+area_str,AutoCompleteObject.class);
			}
				
			
			else if(type.equalsIgnoreCase("metered")){
				
				if(CacheUtil.getListFromCache("ALL_METERED_CUSTOMER_ID_"+area_str, AutoCompleteObject.class)==null){
					CacheUtil.setListToCache(Utils.constructCacheKey("ALL_METERED_CUSTOMER_ID_"+area_str),customerService.getCustomerListForAutoComplete("METERED",area_str));
					allCustomer= CacheUtil.getListFromCache("ALL_METERED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class);
				}
				else					
					allCustomer=CacheUtil.getListFromCache("ALL_METERED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class); 
			}
			else if(type.equalsIgnoreCase("metered_connected")){
				 
				if(CacheUtil.getListFromCache("ALL_METERED_CONNECTED_CUSTOMER_ID_"+area_str, AutoCompleteObject.class)==null){
					CacheUtil.setListToCache(Utils.constructCacheKey("ALL_METERED_CONNECTED_CUSTOMER_ID_"+area_str),customerService.getCustomerListForAutoComplete("METERED_CONNECTED",area_str));
					allCustomer= CacheUtil.getListFromCache("ALL_METERED_CONNECTED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class);
				}
				else					
					allCustomer=CacheUtil.getListFromCache("ALL_METERED_CONNECTED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class); 
			}
			else if(type.equalsIgnoreCase("metered_disconnected")){
				if(CacheUtil.getListFromCache("ALL_METERED_DISCONNECTED_CUSTOMER_ID_"+area_str, AutoCompleteObject.class)==null){
					CacheUtil.setListToCache(Utils.constructCacheKey("ALL_METERED_DISCONNECTED_CUSTOMER_ID_"+area_str),customerService.getCustomerListForAutoComplete("METERED_DISCONNECTED",area_str));
					allCustomer= CacheUtil.getListFromCache("ALL_METERED_DISCONNECTED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class);
				}
				else					
					allCustomer=CacheUtil.getListFromCache("ALL_METERED_DISCONNECTED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class); 
 
			}
			
			else if(type.equalsIgnoreCase("nonmetered")){
				if(CacheUtil.getListFromCache("ALL_NON_METERED_CUSTOMER_ID_"+area_str, AutoCompleteObject.class)==null){
					CacheUtil.setListToCache(Utils.constructCacheKey("ALL_NON_METERED_CUSTOMER_ID_"+area_str),customerService.getCustomerListForAutoComplete("NONMETERED",area_str));
					allCustomer= CacheUtil.getListFromCache("ALL_NON_METERED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class);
				}
				else					
					allCustomer=CacheUtil.getListFromCache("ALL_NON_METERED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class); 
				
			}
			else if(type.equalsIgnoreCase("nonmetered_connected")){
				if(CacheUtil.getListFromCache("ALL_NON_METERED_CONNECTED_CUSTOMER_ID_"+area_str, AutoCompleteObject.class)==null){
					CacheUtil.setListToCache(Utils.constructCacheKey("ALL_NON_METERED_CONNECTED_CUSTOMER_ID_"+area_str),customerService.getCustomerListForAutoComplete("NONMETERED_CONNECTED",area_str));
					allCustomer= CacheUtil.getListFromCache("ALL_NON_METERED_CONNECTED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class);
				}
				else					
					allCustomer=CacheUtil.getListFromCache("ALL_NON_METERED_CONNECTED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class);
				
			}
			else if(type.equalsIgnoreCase("nonmetered_disconnected")){
				if(CacheUtil.getListFromCache("ALL_NON_METERED_DISCONNECTED_CUSTOMER_ID_"+area_str, AutoCompleteObject.class)==null){
					CacheUtil.setListToCache(Utils.constructCacheKey("ALL_NON_METERED_DISCONNECTED_CUSTOMER_ID_"+area_str),customerService.getCustomerListForAutoComplete("NONMETERED_DISCONNECTED",area_str));
					allCustomer= CacheUtil.getListFromCache("ALL_NON_METERED_DISCONNECTED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class);
				}
				else					
					allCustomer=CacheUtil.getListFromCache("ALL_NON_METERED_DISCONNECTED_CUSTOMER_ID_"+area_str,AutoCompleteObject.class);
				
			}

			
			Collection<AutoCompleteObject> filter_list = Collections2.filter(allCustomer,
					new Predicate<AutoCompleteObject>() {

						public boolean apply(AutoCompleteObject input) {
							return input.getText().contains(query);
						}
					});
			String jsonStr="{ \"query\": \""+query+"\",";
			String suggestions=Utils.EMPTY_STRING;
			 for (AutoCompleteObject obj : filter_list) {
				 suggestions+="{\"value\":\""+obj.getValue()+"\",\"data\": \""+obj.getText()+"\"},";
		       }
			 if(!suggestions.equals(Utils.EMPTY_STRING)){
				 suggestions=suggestions.substring(0, suggestions.length()-1);
				 suggestions=" \"suggestions\": "+"["+suggestions+"]";
			 }
			 else{
				 suggestions=" \"suggestions\": "+"[]";
			 }
			 jsonStr+=suggestions+"}";
			

			try{
	    	 response.setContentType("json");
	    	 response.getWriter().write(jsonStr);
	          }
	        catch(Exception e) {e.printStackTrace();}
			return null;
		}
		
		public String fetchBankAccountTypes()
		{
			try{
		    	 response.setContentType("json");
		    	 response.getWriter().write("STD:Short Time Deposit;SND:Short Notice Deposit");
		          }
		        catch(Exception e) {e.printStackTrace();}
				return null;
		}
		
		public String getBank_id() {
			return bank_id;
		}
		public void setBank_id(String bankId) {
			bank_id = bankId;
		}
		public String getArea_id() {
			return area_id;
		}
		public void setArea_id(String areaId) {
			area_id = areaId;
		}
		public String getDivision_id() {
			return division_id;
		}
		public void setDivision_id(String divisionId) {
			division_id = divisionId;
		}
		public String getDistrict_id() {
			return district_id;
		}
		public void setDistrict_id(String districtId) {
			district_id = districtId;
		}
		
		public String getDepartment_id() {
			return department_id;
		}
		public void setDepartment_id(String departmentId) {
			department_id = departmentId;
		}
		public String getSection_id() {
			return section_id;
		}
		public void setSection_id(String sectionId) {
			section_id = sectionId;
		}
		public String getCustomer_id() {
			return customer_id;
		}
		public void setCustomer_id(String customerId) {
			customer_id = customerId;
		}
		public String getCustomer_name() {
			return customer_name;
		}
		public void setCustomer_name(String customerName) {
			customer_name = customerName;
		}		
		public String getBranch_id() {
			return branch_id;
		}
		public void setBranch_id(String branchId) {
			branch_id = branchId;
		}
		
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
		

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getZone_id() {
			return zone_id;
		}

		public void setZone_id(String zone_id) {
			this.zone_id = zone_id;
		}

		public String getZone_name() {
			return zone_name;
		}

		public void setZone_name(String zone_name) {
			this.zone_name = zone_name;
		}

		public ServletContext getServletContext()
		{
			return ServletActionContext.getServletContext();
		}


}
