package org.jgtdsl.dto;

public class DefaulterDto {
private String customer_id;
private String full_name;
private String category_id;
private String category_name;
private String address;
private String moholla_name;
private String contact_no;
private String due_month;
private float amount;
private int total_month;
private String status;
private double maxLoad;
private double minLoad;

private int single_burner;
private int double_burner;
private int other_burner;



public int getSingle_burner() {
	return single_burner;
}
public void setSingle_burner(int single_burner) {
	this.single_burner = single_burner;
}
public int getDouble_burner() {
	return double_burner;
}
public void setDouble_burner(int double_burner) {
	this.double_burner = double_burner;
}
public int getOther_burner() {
	return other_burner;
}
public void setOther_burner(int other_burner) {
	this.other_burner = other_burner;
}
public String getCustomer_id() {
	return customer_id;
}
public void setCustomer_id(String customer_id) {
	this.customer_id = customer_id;
}
public String getFull_name() {
	return full_name;
}
public void setFull_name(String full_name) {
	this.full_name = full_name;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getContact_no() {
	return contact_no;
}
public void setContact_no(String contact_no) {
	this.contact_no = contact_no;
}
public String getDue_month() {
	return due_month;
}
public void setDue_month(String due_month) {
	this.due_month = due_month;
}
public float getAmount() {
	return amount;
}
public void setAmount(float amount) {
	this.amount = amount;
}


public String getCategory_id() {
	return category_id;
}
public void setCategory_id(String category_id) {
	this.category_id = category_id;
}
public String getCategory_name() {
	return category_name;
}
public void setCategory_name(String category_name) {
	this.category_name = category_name;
}

public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public int getTotal_month() {
	return total_month;
}
public void setTotal_month(int total_month) {
	this.total_month = total_month;
}
public double getMaxLoad() {
	return maxLoad;
}
public void setMaxLoad(double maxLoad) {
	this.maxLoad = maxLoad;
}
public double getMinLoad() {
	return minLoad;
}
public void setMinLoad(double minLoad) {
	this.minLoad = minLoad;
}
public String getMoholla_name() {
	return moholla_name;
}
public void setMoholla_name(String moholla_name) {
	this.moholla_name = moholla_name;
}




}
