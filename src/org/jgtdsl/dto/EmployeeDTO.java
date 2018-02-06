package org.jgtdsl.dto;

public class EmployeeDTO {
	private String emp_id;
	private String area_id;
	private String full_name;
	private String gender;
	private String designation;
	private int status;
	
	public String getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(String empId) {
		emp_id = empId;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String areaId) {
		area_id = areaId;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String fullName) {
		full_name = fullName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String toString() {
        return "{\"emp_id\":\"" + emp_id + "\", \"area_id\":\"" + area_id + "\", \"full_name\":\"" + full_name + "\", \"gender\":\"" + gender + "\", \"designation\":\"" + designation + "\", \"status\":\"" + status + "\"}";
    }
}
