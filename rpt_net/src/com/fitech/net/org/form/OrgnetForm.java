package com.fitech.net.org.form;

import java.text.SimpleDateFormat;

import org.apache.struts.action.ActionForm;


public class OrgnetForm extends ActionForm {

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd.MM.yyyy hh:mm:ss");

    /**
     * 机构id
     */
    private String  orgId = null;

    /**
     * 机构名称
     */
    private String  orgName = null;

    /**
     * 机构类型
     */
    private String  orgType = null;

    /**
     * 是同法人
     */
    private String  isCorp = null;

    /**
     * 机构分类id
     */
    private String  orgClsId = null;

    /**
     * 机构分类名称
     */
  /*  private String  orgClsName = null;*/

    /**
     * 机构编码
     */
    private String  orgCode = null;

   /* *//**
     * 选择的机构类型id列表
     *//*
    private String[]  selectOrgIds = null;*/
    /**
     * 地区Id
     */
    private String   regionId= null;
    /**
     * 
     */
    private String   parent_Org_Id= null;    
    /**
     * 
     */
    private Integer  oat_Id=null;
    /**
     * 页面动作事件
     */
    private String event;
    private boolean flag=false;
    private Long departmentId = null;
    //机构层次
    private String orglayerId=null;
    private String orglayer=null;
    /**
     * 部门名称
     */
    private String deptName = null;
    
    
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Integer getOat_Id() {
		
		return oat_Id;
	}
	public void setOat_Id(Integer oat_Id) {
		this.oat_Id = oat_Id;
	}
	public static SimpleDateFormat getFORMAT() {
		return FORMAT;
	}
	public String getIsCorp() {
		return isCorp;
	}
	public void setIsCorp(String isCorp) {
		this.isCorp = isCorp;
	}
	public String getOrgClsId() {
		return orgClsId;
	}
	public void setOrgClsId(String orgClsId) {
		this.orgClsId = orgClsId;
	}
/*	public String getOrgClsName() {
		return orgClsName;
	}
	public void setOrgClsName(String orgClsName) {
		this.orgClsName = orgClsName;
	}*/
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getParent_Org_Id() {
		return parent_Org_Id;
	}
	public void setParent_Org_Id(String parent_Org_Id) {
		this.parent_Org_Id = parent_Org_Id;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getOrglayer() {
		return orglayer;
	}
	public void setOrglayer(String orglayer) {
		this.orglayer = orglayer;
	}
	public String getOrglayerId() {
		return orglayerId;
	}
	public void setOrglayerId(String orglayerId) {
		this.orglayerId = orglayerId;
	}
}

