package com.cbrc.smis.form;
import java.sql.Date;

import org.apache.struts.action.ActionForm;
public class JhbOrgForm extends ActionForm{
    private java.lang.String _orgId = null;
    private java.lang.String _orgName = null;
    private Integer OATId=null;
    private String OATName=null;
    private Date startDate=null;
    private String error_reportname=null;
    private String error_repfreqname=null;
    private String error_dataname=null;
    private String later_reportname=null;
    private String later_repfreqname=null;
    private String later_dataname=null;
    private String leave_reportname=null;
    private String leave_repfreqname=null;
    private String leave_dataname=null;
    private String orgClsId=null;
    private String _orgClsName = null;
	public java.lang.String get_orgId() {
		return _orgId;
	}
	public void set_orgId(java.lang.String id) {
		_orgId = id;
	}
	public java.lang.String get_orgName() {
		return _orgName;
	}
	public void set_orgName(java.lang.String name) {
		_orgName = name;
	}
	public Integer getOATId() {
		return OATId;
	}
	public void setOATId(Integer id) {
		OATId = id;
	}
	public String getOATName() {
		return OATName;
	}
	public void setOATName(String name) {
		OATName = name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getError_repfreqname() {
		return error_repfreqname;
	}
	public void setError_repfreqname(String error_repfreqname) {
		this.error_repfreqname = error_repfreqname;
	}
	public String getError_reportname() {
		return error_reportname;
	}
	public void setError_reportname(String error_reportname) {
		this.error_reportname = error_reportname;
	}
	public String getLater_repfreqname() {
		return later_repfreqname;
	}
	public void setLater_repfreqname(String later_repfreqname) {
		this.later_repfreqname = later_repfreqname;
	}
	public String getLater_reportname() {
		return later_reportname;
	}
	public void setLater_reportname(String later_reportname) {
		this.later_reportname = later_reportname;
	}
	public String getLeave_repfreqname() {
		return leave_repfreqname;
	}
	public void setLeave_repfreqname(String leave_repfreqname) {
		this.leave_repfreqname = leave_repfreqname;
	}
	public String getLeave_reportname() {
		return leave_reportname;
	}
	public void setLeave_reportname(String leave_reportname) {
		this.leave_reportname = leave_reportname;
	}
	public String getOrgClsId() {
		return orgClsId;
	}
	public void setOrgClsId(String orgClsId) {
		this.orgClsId = orgClsId;
	}
	public String get_orgClsName() {
		return _orgClsName;
	}
	public void set_orgClsName(String clsName) {
		_orgClsName = clsName;
	}
	public String getError_dataname() {
		return error_dataname;
	}
	public void setError_dataname(String error_dataname) {
		this.error_dataname = error_dataname;
	}
	public String getLater_dataname() {
		return later_dataname;
	}
	public void setLater_dataname(String later_dataname) {
		this.later_dataname = later_dataname;
	}
	public String getLeave_dataname() {
		return leave_dataname;
	}
	public void setLeave_dataname(String leave_dataname) {
		this.leave_dataname = leave_dataname;
	}
}
