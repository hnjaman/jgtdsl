package org.jgtdsl.dto;

public class HolidayDTO {
	
	private String holiday_id;
	private String holiday_date;
	private String holiday_date_id;
	private String from_date;
	private String to_date;
	private String holiday_cause;
	private String holiday_type;
	private String holiday_type_name;
	private String weekDay;
	private String month_year;
	

	public String getHoliday_id() {
		return holiday_id;
	}
	public void setHoliday_id(String holidayId) {
		holiday_id = holidayId;
	}
	public String getHoliday_date() {
		return holiday_date;
	}
	public void setHoliday_date(String holidayDate) {
		holiday_date = holidayDate;
	}
	public String getHoliday_cause() {
		return holiday_cause;
	}
	public void setHoliday_cause(String holidayCause) {
		holiday_cause = holidayCause;
	}
	public String getHoliday_date_id() {
		return holiday_date_id;
	}
	public void setHoliday_date_id(String holidayDateId) {
		holiday_date_id = holidayDateId;
	}
	public String getFrom_date() {
		return from_date;
	}
	public void setFrom_date(String fromDate) {
		from_date = fromDate;
	}
	public String getTo_date() {
		return to_date;
	}
	public void setTo_date(String toDate) {
		to_date = toDate;
	}
	public String getHoliday_type() {
		return holiday_type;
	}
	public void setHoliday_type(String holidayType) {
		holiday_type = holidayType;
	}
	public String getHoliday_type_name() {
		return holiday_type_name;
	}
	public void setHoliday_type_name(String holidayTypeName) {
		holiday_type_name = holidayTypeName;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getMonth_year() {
		return month_year;
	}
	public void setMonth_year(String monthYear) {
		month_year = monthYear;
	}
	
}