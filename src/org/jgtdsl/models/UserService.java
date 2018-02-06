package org.jgtdsl.models;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jgtdsl.dto.AreaDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class UserService {

	public String addUser(String data)
	{
		Gson gson = new Gson();  
		UserDTO user = gson.fromJson(data, UserDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");

		String sql=" Insert into MST_USER(USERID,PASSWORD,USERNAME,ROLE,AREA,ORG_DIVISION,DEPARTMENT,SECTION,DIVISION,DISTRICT,UPAZILA,DESIGNATION,MOBILE, " +
				   " EMAIL_ADDRESS,CREATED_ON,CREATED_BY,STATUS) " +
				   " values(?,?,?,?,?,?,?,?,?,?,?,?,?, " +
				   " ?,sysdate,?,?)";
		int response=0;
		PreparedStatement stmt = null;
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,user.getUserId());
				stmt.setString(2,passwordEncryptor.encryptPassword(user.getPassword()));
				stmt.setString(3,user.getUserName());
				stmt.setString(4,user.getRole_name());
				stmt.setString(5,user.getArea_name());
				stmt.setString(6,user.getOrg_division_name());
				stmt.setString(7,user.getDepartment_name());
				stmt.setString(8,user.getSection_name());
				stmt.setString(9,user.getDivision_name());
				stmt.setString(10,user.getDistrict_name());
				stmt.setString(11,user.getUpazila_name());
				stmt.setString(12,user.getDesignation_name());
				stmt.setString(13,user.getMobile());
				
				stmt.setString(14,user.getEmail_address());
				stmt.setString(15,loggedInUser.getUserId());
				stmt.setString(16,user.getStatus());
											
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		if(response==1){
	 			String filePath=(String)ServletActionContext.getRequest().getSession().getAttribute("photo_new");
	 			
	 			if(filePath!=null){
	 				ServletActionContext.getRequest().getSession().setAttribute("photo_new",null);
	 				try{
	 					String root_path=(String) ServletActionContext.getServletContext().getAttribute("PHOTO_DIR")+"user\\";	
	 					Utils.copyFile(new File(filePath),new File(root_path+user.getUserId()+".jpg"));
	 				}
	 				catch(Exception ex){ex.printStackTrace();}
	 				
	 			}
	 			return Utils.getJsonString(AC.STATUS_OK, AC.MSG_CREATE_OK_PREFIX+AC.MST_USER);
	 		}
		 	else
		 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_CREATE_ERROR_PREFIX+AC.MST_USER);
	}
	
	public UserDTO validateLogin(String userId,String plainTextPassword)
	{	
		UserDTO user=null;		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		boolean passOk=false;
		
			   sql= " Select USERID,PASSWORD,USERNAME,ROLE_ID,ROLE_NAME,AREA_ID,AREA_NAME,ORG_DIVISION.DIVISION_ID ORG_DIVISION_ID,ORG_DIVISION.DIVISION_NAME ORG_DIVISION_NAME, " +
					" ORG_DEPARTMENT.DEPARTMENT_ID,DEPARTMENT_NAME,SECTION_ID,SECTION_NAME,DIVISION.DIVISION_ID,DIVISION.DIVISION_NAME,DIST_ID,DIST_NAME,UPAZILA_ID,UPAZILA_NAME, "+
			  		" DESIGNATION_ID,DESIGNATION_NAME,MOBILE,EMAIL_ADDRESS,CREATED_ON,CREATED_BY,u.STATUS,LAST_LOGIN_ON,DEFAULT_URL From MST_USER u " +
			  		" Left Outer Join MST_ROLE ON u.ROLE=MST_ROLE.ROLE_ID " +
			  		" Left Outer Join MST_DESIGNATION ON u.DESIGNATION=DESIGNATION_ID " +
			  		" Left Outer Join MST_AREA ON u.AREA=MST_AREA.AREA_ID " +
			  		" Left Outer Join ORG_DIVISION ON u.ORG_DIVISION=ORG_DIVISION.DIVISION_ID " +
			  		" Left Outer Join ORG_DEPARTMENT ON u.DEPARTMENT=ORG_DEPARTMENT.DEPARTMENT_ID " +
			  		" Left Outer Join ORG_SECTION ON u.SECTION=ORG_SECTION.SECTION_ID  " +
			  		" Left Outer Join DIVISION ON u.DIVISION=DIVISION.DIVISION_ID " +
			  		" Left Outer Join DISTRICT ON u.DISTRICT=DISTRICT.DIST_ID  " +
			  		" Left Outer Join UPAZILA ON u.UPAZILA=UPAZILA.UPAZILA_ID Where userid=?";
		
		
		//System.out.println("----------------->>"+sql);
		//System.out.println("----------------->>"+userId);
		

		   PreparedStatement stmt = null;
		   ResultSet r = null;
		   
			try
			{
				stmt = conn.prepareStatement(sql);
			    stmt.setString(1, userId);

			    
				r = stmt.executeQuery();
				if (r.next())
				{
					String encryptedPassword=r.getString("PASSWORD");
					passOk=UserService.checkPassword(plainTextPassword, encryptedPassword);
					if(passOk==false){
						System.out.println("----------------->>"+encryptedPassword);
						return user;
						
					}
					user=new UserDTO();
					user.setUserId(r.getString("USERID"));
					user.setUserName(r.getString("USERNAME"));
					user.setPassword(r.getString("PASSWORD"));
					user.setRole_id(r.getString("ROLE_ID"));
					user.setRole_name(r.getString("ROLE_NAME"));
					user.setArea_id(r.getString("AREA_ID"));
					user.setArea_name(r.getString("AREA_NAME"));
					user.setOrg_division_id(r.getString("ORG_DIVISION_ID"));
					user.setOrg_division_name(r.getString("ORG_DIVISION_NAME"));
					user.setDepartment_id(r.getString("DEPARTMENT_ID"));
					user.setDepartment_name(r.getString("DEPARTMENT_NAME"));
					user.setSection_id(r.getString("SECTION_ID"));
					user.setSection_name(r.getString("SECTION_NAME"));					
					user.setDivision_id(r.getString("DIVISION_ID"));
					user.setDivision_name(r.getString("DIVISION_NAME"));
					user.setDistrict_id(r.getString("DIST_ID"));
					user.setDistrict_name(r.getString("DIST_NAME"));
					user.setUpazila_id(r.getString("UPAZILA_ID"));
					user.setUpazila_name(r.getString("UPAZILA_NAME"));					
					user.setDesignation_id(r.getString("DESIGNATION_ID"));
					user.setDesignation_name(r.getString("DESIGNATION_NAME"));
					user.setMobile(r.getString("MOBILE"));
					user.setEmail_address(r.getString("EMAIL_ADDRESS"));
					user.setCreated_on(r.getString("CREATED_ON"));
					user.setCreated_by(r.getString("CREATED_BY"));
					user.setStatus(r.getString("STATUS"));
					user.setLast_login_on(r.getString("LAST_LOGIN_ON"));
					user.setDefault_url(r.getString("DEFAULT_URL"));
					user.setUserImg(r.getString("USERID"));
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return user;
	}
	public ArrayList<UserDTO> getUserList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		UserDTO user=null;
		ArrayList<UserDTO> userList=new ArrayList<UserDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select USERID,PASSWORD,USERNAME,ROLE_ID,ROLE_NAME,AREA_ID,AREA_NAME,ORG_DIVISION.DIVISION_ID ORG_DIVISION_ID,ORG_DIVISION.DIVISION_NAME ORG_DIVISION_NAME, " +
				  		" ORG_DEPARTMENT.DEPARTMENT_ID,DEPARTMENT_NAME,SECTION_ID,SECTION_NAME,DIVISION.DIVISION_ID,DIVISION.DIVISION_NAME,DIST_ID,DIST_NAME,UPAZILA_ID,UPAZILA_NAME, " +
				  		" DESIGNATION_ID,DESIGNATION_NAME,SHORT_TERM,MOBILE,EMAIL_ADDRESS,CREATED_ON,CREATED_BY,u.STATUS,LAST_LOGIN_ON,DEFAULT_URL from " +
				  		" (Select *  From MST_USER "+(whereClause.equalsIgnoreCase("")?"":(" Where ( "+whereClause+")"))+")u "+
				  		" Left Outer Join MST_ROLE ON u.ROLE=MST_ROLE.ROLE_ID " +
				  		" Left Outer Join MST_DESIGNATION ON u.DESIGNATION=DESIGNATION_ID " +
				  		" Left Outer Join MST_AREA ON u.AREA=MST_AREA.AREA_ID " +
				  		" Left Outer Join ORG_DIVISION ON u.ORG_DIVISION=ORG_DIVISION.DIVISION_ID " +
				  		" Left Outer Join ORG_DEPARTMENT ON u.DEPARTMENT=ORG_DEPARTMENT.DEPARTMENT_ID " +
				  		" Left Outer Join ORG_SECTION ON u.SECTION=ORG_SECTION.SECTION_ID  " +
				  		" Left Outer Join DIVISION ON u.DIVISION=DIVISION.DIVISION_ID " +
				  		" Left Outer Join DISTRICT ON u.DISTRICT=DISTRICT.DIST_ID  " +
				  		" Left Outer Join UPAZILA ON u.UPAZILA=UPAZILA.UPAZILA_ID";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select USERID,PASSWORD,USERNAME,ROLE_ID,ROLE_NAME,AREA_ID,AREA_NAME,ORG_DIVISION.DIVISION_ID ORG_DIVISION_ID,ORG_DIVISION.DIVISION_NAME ORG_DIVISION_NAME, " +
				  	  " ORG_DEPARTMENT.DEPARTMENT_ID,DEPARTMENT_NAME,SECTION_ID,SECTION_NAME,DIVISION.DIVISION_ID,DIVISION.DIVISION_NAME,DIST_ID,DIST_NAME,UPAZILA_ID,UPAZILA_NAME, " +
				  	  "  DESIGNATION_ID,DESIGNATION_NAME,SHORT_TERM,MOBILE,EMAIL_ADDRESS,CREATED_ON,CREATED_BY,u.STATUS,LAST_LOGIN_ON,DEFAULT_URL from  " +
				  	  " (Select  * From MST_USER "+(whereClause.equalsIgnoreCase("")?"":(" Where ( "+whereClause+")"))+" "+orderByQuery+")u"+
				  	  " Left Outer Join MST_ROLE ON u.ROLE=MST_ROLE.ROLE_ID " +
				  	  " Left Outer Join MST_DESIGNATION ON u.DESIGNATION=DESIGNATION_ID " +
				  	  " Left Outer Join MST_AREA ON u.AREA=MST_AREA.AREA_ID " +
			  		  " Left Outer Join ORG_DIVISION ON u.ORG_DIVISION=ORG_DIVISION.DIVISION_ID " +
			  		  " Left Outer Join ORG_DEPARTMENT ON u.DEPARTMENT=ORG_DEPARTMENT.DEPARTMENT_ID " +
			  		  " Left Outer Join ORG_SECTION ON u.SECTION=ORG_SECTION.SECTION_ID  " +
			  		  " Left Outer Join DIVISION ON u.DIVISION=DIVISION.DIVISION_ID " +
			  		  " Left Outer Join DISTRICT ON u.DISTRICT=DISTRICT.DIST_ID  " +
			  		  " Left Outer Join UPAZILA ON u.UPAZILA=UPAZILA.UPAZILA_ID"+" "+orderByQuery+			
			  		  "    )tmp1 " +
				  	  "    )tmp2   " +
				  	  "  Where serial Between ? and ? ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				if(total!=0)
				{
				stmt.setInt(1, index);
				stmt.setInt(2, index+offset);
				}
				r = stmt.executeQuery();
				while (r.next())
				{
					user=new UserDTO();
					user.setUserId(r.getString("USERID"));
					user.setUserName(r.getString("USERNAME"));
					user.setPassword(r.getString("PASSWORD"));
					user.setRole_id(r.getString("ROLE_ID"));
					user.setRole_name(r.getString("ROLE_NAME"));
					user.setArea_id(r.getString("AREA_ID"));
					user.setArea_name(r.getString("AREA_NAME"));
					user.setOrg_division_id(r.getString("ORG_DIVISION_ID"));
					user.setOrg_division_name(r.getString("ORG_DIVISION_NAME"));
					user.setDepartment_id(r.getString("DEPARTMENT_ID"));
					user.setDepartment_name(r.getString("DEPARTMENT_NAME"));
					user.setSection_id(r.getString("SECTION_ID"));
					user.setSection_name(r.getString("SECTION_NAME"));					
					user.setDivision_id(r.getString("DIVISION_ID"));
					user.setDivision_name(r.getString("DIVISION_NAME"));
					user.setDistrict_id(r.getString("DIST_ID"));
					user.setDistrict_name(r.getString("DIST_NAME"));
					user.setUpazila_id(r.getString("UPAZILA_ID"));
					user.setUpazila_name(r.getString("UPAZILA_NAME"));					
					user.setDesignation_id(r.getString("DESIGNATION_ID"));
					user.setDesignation_name(r.getString("DESIGNATION_NAME")+"("+r.getString("SHORT_TERM")+")");
					user.setMobile(r.getString("MOBILE"));
					user.setEmail_address(r.getString("EMAIL_ADDRESS"));
					user.setCreated_on(r.getString("CREATED_ON"));
					user.setCreated_by(r.getString("CREATED_BY"));
					user.setStatus(r.getString("STATUS"));
					user.setLast_login_on(r.getString("LAST_LOGIN_ON"));
					user.setDefault_url(r.getString("DEFAULT_URL"));
					user.setUserImg(r.getString("USERID"));
					userList.add(user);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return userList;
	}
	public ArrayList<UserDTO> getUserList()
	{
		return getUserList(0, 0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
	}	
	
	public String updateUser(String data)
	{
		Gson gson = new Gson();  
		UserDTO userDTO = gson.fromJson(data, UserDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" Update Mst_User Set UserName=?,Role=?,Area=?,Org_Division=?,Department=?,Section=?,Division=?,District=?, " +
				   " Upazila=?, Designation=?, Mobile=?,Email_Address=?,Status=? Where UserId=?";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,userDTO.getUserName());
				stmt.setString(2,userDTO.getRole_name());
				stmt.setString(3,userDTO.getArea_name());
				stmt.setString(4,userDTO.getOrg_division_name());
				stmt.setString(5,userDTO.getDepartment_name());
				stmt.setString(6,userDTO.getSection_name());
				stmt.setString(7,userDTO.getDivision_name());
				stmt.setString(8,userDTO.getDistrict_name());
				stmt.setString(9,userDTO.getUpazila_name());
				stmt.setString(10,userDTO.getDesignation_name());
				stmt.setString(11,userDTO.getMobile());
				stmt.setString(12,userDTO.getEmail_address());
				stmt.setString(13,userDTO.getStatus());
				stmt.setString(14,userDTO.getUserId());
				
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(response==1){
 			String filePath=(String)ServletActionContext.getRequest().getSession().getAttribute("photo_"+userDTO.getUserId());
 			
 			if(filePath!=null){
 				ServletActionContext.getRequest().getSession().setAttribute("photo_"+userDTO.getUserId(),null);
 				try{
 					String root_path=(String) ServletActionContext.getServletContext().getAttribute("PHOTO_DIR")+"user\\";	
 					Utils.copyFile(new File(filePath),new File(root_path+userDTO.getUserId()+".jpg"));
 				}
 				catch(Exception ex){ex.printStackTrace();}
 				
 			}

	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_UPDATE_OK_PREFIX+AC.MST_USER);
	 	}
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_UPDATE_ERROR_PREFIX+AC.MST_USER);

	}
	
	public ResponseDTO changePassword(UserDTO user){
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();		
		String sql=" Update Mst_User Set Password=? Where UserId=?";
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				String newPassword=(new StrongPasswordEncryptor()).encryptPassword(user.getPassword());
				stmt.setString(1,newPassword);
				stmt.setString(2,user.getUserId());
				if(stmt.executeUpdate()==1){
					response.setMessasge("Successfully Changed Password.");
					response.setResponse(true);
					((UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user")).setPassword(newPassword);
				}
				
			} 			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
			}
	 		finally{try{stmt.close();conn.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		return response;
	}
	
	public ResponseDTO checkPasswordChangePrecondition(UserDTO user){
		ResponseDTO response=new ResponseDTO();
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
		if(isPasswordAndConfirmPasswordOk(user.getPassword(),user.getConfirm_password())==false){
			response.setResponse(false);
			response.setMessasge("Password and Confirm Password are not equal.");
		}
		else if(isOldPasswordOk(user.getOld_password(),loggedInUser.getPassword())==false){
			response.setResponse(false);
			response.setMessasge("Invalid Old password.");
		}
		else if(isPasswordRuleOk(user.getPassword())==false){
			response.setResponse(false);
			response.setMessasge("Password rule violated.");
		}			
		else{
			response.setResponse(true);
			response.setMessasge("Precondition ok.");
		}
			
		return response;
		
	}
	
	public boolean isPasswordAndConfirmPasswordOk(String password,String confirmPassword){

		if(password.equalsIgnoreCase(confirmPassword))
			return true;
		else
			return false;
	}

	public boolean isPasswordRuleOk(String password){
		//Password Rule :
		// At least one digit, one uppercase, one lowercase character
		// Length will be 6-15 Character long
		String PASSWORD_PATTERN ="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,15})";		
	    Pattern pattern= Pattern.compile(PASSWORD_PATTERN);
	    Matcher matcher = pattern.matcher(password);		  

		if(password.length()>=6 && password.length()<=15 && matcher.matches()==true)
			return true;
		else
			return false;
		
	}
	public boolean isOldPasswordOk(String oldPassword,String actualOldPassword){		
		return UserService.checkPassword(oldPassword, actualOldPassword);
	}
	

	public ResponseDTO changeTheme(String userId,String theme){
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();		
		String sql=" Update Mst_User Set default_url=? Where UserId=?";
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,theme);
				stmt.setString(2,userId);
				if(stmt.executeUpdate()==1){
					response.setMessasge("Successfully Changed Theme.");
					response.setResponse(true);
					((UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user")).setDefault_url(theme);
				}
				
			} 			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
			}
	 		finally{try{stmt.close();conn.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		return response;
	}
	
	public ResponseDTO updateAccountInfo(UserDTO user){
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();		
		String sql=" Update MST_USER Set username=?,designation=?,mobile=?,email_address=? where userid=?";
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,user.getUserName());
				stmt.setString(2,user.getDesignation_id());
				stmt.setString(3,user.getMobile());
				stmt.setString(4,user.getEmail_address());
				stmt.setString(5,user.getUserId());
				if(stmt.executeUpdate()==1){
					response.setMessasge("Successfully Updated Account Information.");
					response.setResponse(true);
				}
				
			} 			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
			}
	 		finally{try{stmt.close();conn.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		return response;
	}
	
	public void updateLoggedInUserAccountInfo(UserDTO user){
		
		((UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user")).setUserName(user.getUserName());
		((UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user")).setDesignation_id(user.getDesignation_id());
		((UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user")).setMobile(user.getMobile());
		((UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user")).setEmail_address(user.getEmail_address());					

	}
	public String deleteUser(String userId)
	{
		 JSONParser jsonParser = new JSONParser();		
		 JSONObject jsonObject=null;
		 String uId=null;;
		 try{
			 jsonObject= (JSONObject) jsonParser.parse(userId);
		 }
		 catch(Exception ex){
			 ex.printStackTrace();
			 return Utils.getJsonString(AC.STATUS_ERROR, ex.getMessage());			 
		 }
		 uId=(String)jsonObject.get("id");
		Connection conn = ConnectionManager.getConnection();
		String sql=" Delete MST_USER Where USERID=?";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,uId);
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		

	 		if(response==1)
		 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_DELETE_OK_PREFIX+AC.MST_USER);
		 	else
		 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_DELETE_ERROR_PREFIX+AC.MST_USER);

	}
	
	public static boolean checkPassword(String plainTextPassword, String encryptedPassword)
	{
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		return passwordEncryptor.checkPassword(plainTextPassword, encryptedPassword);
		
	}
	
	public ResponseDTO resetPassword(UserDTO user){
		
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();		
		String sql=" Update MST_USER Set password=? where userid=?";
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,passwordEncryptor.encryptPassword(Utils.USER_DEFAULT_PASSWORD));
				stmt.setString(2,user.getUserId());
				if(stmt.executeUpdate()==1){
					response.setMessasge("Password has successfully resetted.");
					response.setResponse(true);
				}
				
			} 			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
			}
	 		finally{try{stmt.close();conn.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		return response;
	}
	
	
	public boolean validateUserId(String userId)
	{
		Connection conn = ConnectionManager.getConnection();		
		String sql="Select * from MST_USER Where userid=?";
		boolean validity=true;
		PreparedStatement stmt = null;
		ResultSet r = null;		   
		try
		{
			stmt = conn.prepareStatement(sql);
		    stmt.setString(1, userId);

		    
			r = stmt.executeQuery();
			if (r.next())
			{
				validity=false;
			}
		} 
		catch (Exception e){e.printStackTrace();}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return validity;
	}
	
	public List<AreaDTO> getUserAreaList(String userId) {
		
		Connection conn = ConnectionManager.getConnection();		
		String sql="select tmp1.*,area_name from ( " +
				"select area from MST_USER where userid= ? " +
				"union " +
				"select area from USER_AREAS where userid= ? " +
				")tmp1,mst_area " +
				"where tmp1.area=mst_area.area_id order by area_name " ;

		PreparedStatement stmt = null;
		ResultSet r = null;		
		List<AreaDTO> areaList= new ArrayList<AreaDTO>();
		try
		{
			stmt = conn.prepareStatement(sql);
		    stmt.setString(1, userId);
		    stmt.setString(2, userId);
		    
			r = stmt.executeQuery();
			while (r.next())
			{
				AreaDTO area = new AreaDTO();
				area.setArea_id(r.getString("area"));
				area.setArea_name(r.getString("area_name"));
				areaList.add(area);

			}
		} 
		catch (Exception e){e.printStackTrace();}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return areaList;
		
	}
	public static void main(String args[]){
		
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		String encryptedPassword=passwordEncryptor.encryptPassword("ifti24");
		System.out.println(encryptedPassword);
	}
	
	
	
}
