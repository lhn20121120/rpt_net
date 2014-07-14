package com.fitech.gznx.po;

public class HZFormula implements java.io.Serializable{
	private String orgName;
	private String collSchema;
	private String orgId;
	
	

	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public HZFormula(){
		
	}
	public HZFormula(String orgId,String orgName){
		this.orgId=orgId;
		
		this.orgName=orgName;
		
	}
	public String getOrgName() {
		return orgName;
	}
	
	
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
	public String getCollSchema() {
		return collSchema;
	}
	
	
	public void setCollSchema(String collSchema) {
		this.collSchema = collSchema;
	}
}
