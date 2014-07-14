package com.fitech.gznx.po;

public class OrgValiInfo implements java.io.Serializable{
	// Fields

	private Long id; 
	private String org_name;
	private String rol_name;
	private String cell_data;
	private Long rep_id;
	private String template_id;
	private String org_id;
	private String cell_pid;
	private String cell_name;
	private String flag;
	
	public OrgValiInfo(){
	}
	public OrgValiInfo(Long id,String org_name,String rol_name,String cell_data,Long rep_id,String template_id,String org_id,String cell_name,String flag){
	this.id=id;
	this.cell_data=cell_data;
	this.cell_name=cell_name;
	this.org_id=org_id;
	this.org_name=org_name;
	this.rep_id=rep_id;
	this.rol_name=rol_name;
	this.template_id=template_id;
	this.flag=flag;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public String getRol_name() {
		return rol_name;
	}
	public void setRol_name(String rol_name) {
		this.rol_name = rol_name;
	}
	public String getCell_data() {
		return cell_data;
	}
	public void setCell_data(String cell_data) {
		this.cell_data = cell_data;
	}
	public Long getRep_id() {
		return rep_id;
	}
	public void setRep_id(Long rep_id) {
		this.rep_id = rep_id;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getCell_name() {
		return cell_name;
	}
	public void setCell_name(String cell_name) {
		this.cell_name = cell_name;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCell_pid() {
		return cell_pid;
	}
	public void setCell_pid(String cell_pid) {
		this.cell_pid = cell_pid;
	} 

	
 

}
