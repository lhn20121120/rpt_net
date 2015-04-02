package com.fitech.gznx.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.fitech.gznx.service.StrutsViewCustom;
import com.fitech.net.form.ETLReportForm;

public class CustomViewForm extends ActionForm {
	/**报表ID*/
	private String templateId;
	/**报表版本*/
	private String versionId;
	/**报表名称*/
	private String templateName;
	/**项目ID*/
	private String cellId;
	/**项目名称*/
	private String cellName;
	
	/**指标表频度ID*/
	private String repFreqId;
	/**指标表频度名称*/
	private String repFreqName;
	
	/**机构ID*/
	private String orgId;
	/**机构名称*/
	private String orgName;
	
	/**币种ID*/
	private String curId;
	/**指标表币种名称*/
	private String curName;
	
  	/**起始时间*/
  	private String startDate;
  	/**结束时间*/
  	private String endDate;
  	
  	/**查询指标表ID串 逗号分隔*/
	private String meaStr;
	
	/**本期值*/
	private String currentValue;
	
	/**指标表时间*/
	private String date;
	
	private String reportFlg;
	
	private List cellNameList=null;
	
	private String orgList;
	
	private List repFreqList=null;
	
	private List curList=null;
	
	private String[] userList;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public String getRepFreqId() {
		return repFreqId;
	}

	public void setRepFreqId(String repFreqId) {
		this.repFreqId = repFreqId;
	}

	public String getRepFreqName() {
		return repFreqName;
	}

	public void setRepFreqName(String repFreqName) {
		this.repFreqName = repFreqName;
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

	public String getCurId() {
		return curId;
	}

	public void setCurId(String curId) {
		this.curId = curId;
	}

	public String getCurName() {
		return curName;
	}

	public void setCurName(String curName) {
		this.curName = curName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMeaStr() {
		return meaStr;
	}

	public void setMeaStr(String meaStr) {
		this.meaStr = meaStr;
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getReportFlg() {
		return reportFlg;
	}

	public void setReportFlg(String reportFlg) {
		this.reportFlg = reportFlg;
	}

	public List getCellNameList() {
		if(cellNameList != null){
			return cellNameList;
		} else {
			try{
				List list = StrutsViewCustom.findReportCell(templateId,versionId,reportFlg);
				if(list!=null && list.size()>0){
					cellNameList=new ArrayList();
					for(int i=0;i<list.size();i++){
						Object[] reportForm=(Object[])list.get(i);
						String lname="";
						if(reportForm[1] != null){
							if(reportForm[2] != null){
								if(reportForm[3] != null){
								 lname=(String)reportForm[3]+"-"+reportForm[1]+"-"+reportForm[2];
								}else{
								 lname=(String)reportForm[1]+"-"+reportForm[2];
								}
							} else {
								lname=(String)reportForm[1];
							}
						}else{
							lname=(String)reportForm[2];
						}
						cellNameList.add(new LabelValueBean(String.valueOf(reportForm[0]),lname));
					}
				}
				return this.cellNameList;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}	
	
	}

	public void setCellNameList(List cellNameList) {
		this.cellNameList = cellNameList;
	}

	public String getOrgList() {
		
		return orgList;
			
	}

	public void setOrgList(String orgList) {
		this.orgList = orgList;
	}

	public List getRepFreqList() {

		if(repFreqList != null){
			return repFreqList;
		} else {
			try{
				List list = StrutsViewCustom.findReportFreq(templateId,versionId,reportFlg);
				if(list!=null && list.size()>0){
					repFreqList=new ArrayList();
					for(int i=0;i<list.size();i++){
						Object[] reportForm=(Object[])list.get(i);
						repFreqList.add(new LabelValueBean(String.valueOf(reportForm[0]),String.valueOf(reportForm[1])));
					}
				}
				return this.repFreqList;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}	
	}

	public void setRepFreqList(List repFreqList) {
		this.repFreqList = repFreqList;
	}

	public List getCurList() {
		if(curList != null){
			return curList;
		} else {
			try{
				List list = StrutsViewCustom.findReportCur(templateId,versionId);
				if(list!=null && list.size()>0){
					curList=new ArrayList();
					for(int i=0;i<list.size();i++){
						Object[] reportForm=(Object[])list.get(i);
						curList.add(new LabelValueBean(String.valueOf(reportForm[0]),String.valueOf(reportForm[1])));
					}
				}
				return this.curList;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
	}

	public void setCurList(List curList) {
		this.curList = curList;
	}

	public String[] getUserList() {
		return userList;
	}

	public void setUserList(String[] userList) {
		this.userList = userList;
	}
	
}
