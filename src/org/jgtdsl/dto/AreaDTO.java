package org.jgtdsl.dto;

import java.io.Serializable;

public class AreaDTO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3821903696862163976L;
	private String area_id;
	private String area_name;
	private String description;
	private String zones;
	private String zones_name;
	private int status;
	
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String areaId) {
		area_id = areaId;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String areaName) {
		area_name = areaName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description==null?"":description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getZones() {
		return zones;
	}
	public void setZones(String zones) {
		this.zones = zones;
	}
	public String getZones_name() {
		return zones_name;
	}
	public void setZones_name(String zonesName) {
		zones_name = zonesName;
	}
	public String toString() {
        return "{\"area_id\":\"" + area_id + "\", \"area_name\":\"" + area_name + "\", \"description\":\"" + description.replaceAll("[\\r\\n]+", "<br/>") + "\", \"zones\":\"" + zones + "\", \"zones_name\":\"" + zones_name + "\", \"status\":\"" + status + "\"}";
    }
}
