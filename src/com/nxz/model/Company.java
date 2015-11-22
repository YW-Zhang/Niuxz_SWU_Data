package com.nxz.model;

import java.util.List;

public class Company {
	public Company(){
		
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getCompanyDate() {
		return companyDate;
	}
	public void setCompanyDate(String companyDate) {
		this.companyDate = companyDate;
	}
	
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	
	public String getCompanyContent() {
		return companyContent;
	}
	public void setCompanyContent(String companyContent) {
		this.companyContent = companyContent;
	}
	
	public List<String> getPositions() {
		return this.positions;
	}
	public void setPositions(List<String> positions) {
		this.positions = positions;
	}
	public void addPosition(String position) {
		this.positions.add(position);
	}
	
	private String companyName;
	private String companyDate;
	private String companyAddress;
	private String companyContent;
	private List<String> positions;
}
