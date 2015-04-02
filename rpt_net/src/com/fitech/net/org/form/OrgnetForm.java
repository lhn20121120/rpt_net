package com.fitech.net.org.form;

import java.text.SimpleDateFormat;

import org.apache.struts.action.ActionForm;


public class OrgnetForm extends ActionForm {

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd.MM.yyyy hh:mm:ss");

    /**
     * ����id
     */
    private String  orgId = null;

    /**
     * ��������
     */
    private String  orgName = null;

    /**
     * ��������
     */
    private String  orgType = null;

    /**
     * ��ͬ����
     */
    private String  isCorp = null;

    /**
     * ��������id
     */
    private String  orgClsId = null;

    /**
     * ������������
     */
  /*  private String  orgClsName = null;*/

    /**
     * ��������
     */
    private String  orgCode = null;

   /* *//**
     * ѡ��Ļ�������id�б�
     *//*
    private String[]  selectOrgIds = null;*/
    /**
     * ����Id
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
     * ҳ�涯���¼�
     */
    private String event;
    private boolean flag=false;
    private Long departmentId = null;
    //�������
    private String orglayerId=null;
    private String orglayer=null;
    /**
     * ��������
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

