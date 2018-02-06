package org.jgtdsl.dto;

public class ZoneDTO {
	
	private String zone_id;
	private String zone_name;
	private String area_id;

	public ZoneDTO(String zone_id,String zone_name, String area_id){
		this.zone_id=zone_id;
		this.zone_name=zone_name;
		this.area_id = area_id;
	}
	public ZoneDTO(){}
	
	
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	public String getZone_id() {
		return zone_id;
	}
	public void setZone_id(String zoneId) {
		zone_id = zoneId;
	}
	public String getZone_name() {
		return zone_name;
	}
	public void setZone_name(String zoneName) {
		zone_name = zoneName;
	}
	
	public String toString() {
        return "{\"zone_id\":\"" + zone_id + "\", \"zone_name\":\"" + zone_name + "\", \"area_id\":\"" + area_id + "\"}";
    }
}
