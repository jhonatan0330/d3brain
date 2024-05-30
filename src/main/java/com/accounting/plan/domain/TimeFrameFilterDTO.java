package com.accounting.plan.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("TimeFrameFilterDTO")
public class TimeFrameFilterDTO extends SharedDataObjectFilter {

	private Integer level;
	private Date startDateMin;
	private Date startDateMax;
	private Date endDateMin;
	private Date endDateMax;
	private String code;
	private Integer year;
	private Integer month;
	private Integer day;
	private Integer hour;
	private Integer minute;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Date getStartDateMin() {
		return startDateMin;
	}

	public void setStartDateMin(Date startDateMin) {
		this.startDateMin = startDateMin;
	}

	public Date getStartDateMax() {
		return startDateMax;
	}

	public void setStartDateMax(Date startDateMax) {
		this.startDateMax = startDateMax;
	}

	public Date getEndDateMin() {
		return endDateMin;
	}

	public void setEndDateMin(Date endDateMin) {
		this.endDateMin = endDateMin;
	}

	public Date getEndDateMax() {
		return endDateMax;
	}

	public void setEndDateMax(Date endDateMax) {
		this.endDateMax = endDateMax;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

}