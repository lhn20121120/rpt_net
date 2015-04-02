package com.fitech.gznx.form;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.cbrc.smis.common.Config;
import com.fitech.gznx.service.StrutsAFCellDelegate;
import com.fitech.gznx.service.StrutsCodeLibDelegate;

public class RhReportForm extends ActionForm {
	private String templateId;
	private String versionId;
	private String colId;
	private String templateName;
	private String curId;
	private String dataType;
	private String psuziType;
	private String colName;
	private String curName;
	private String dataTypeName;
	private String psuziTypeName;
	private String danweiName;
	private String danweiId;
	private List colIdList = null;
	private List curIdList = null;
	private List dataTypeList = null;
	private List psuziTypeList = null;
	private List danweiIdList = null;
	private String flag;
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
	public String getColId() {
		return colId;
	}
	public void setColId(String colId) {
		this.colId = colId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getPsuziType() {
		return psuziType;
	}
	public void setPsuziType(String psuziType) {
		this.psuziType = psuziType;
	}
	public String getDanweiId() {
		return danweiId;
	}
	public void setDanweiId(String danweiId) {
		this.danweiId = danweiId;
	}
	public List getColIdList() {
		if(this.colIdList==null){	
			  List rtList=null;
			  try{
				  rtList=StrutsAFCellDelegate.getColNameList(templateId,versionId);				  
			  }catch(Exception e){
				  e.printStackTrace();
				  return rtList;
			  }
			  return rtList;
		  }else{
			return this.colIdList;
		  }
		
	}
	public void setColIdList(List colIdList) {
		this.colIdList = colIdList;
	}
	public List getCurIdList() {
		if(this.curIdList==null){	
			 List rtList=null;
			  try{
				  rtList=StrutsCodeLibDelegate.getRepcodebak(Config.CURID_TYPE);
				  
				  
			  }catch(Exception e){
				  e.printStackTrace();
				  return rtList;
			  }
			  return rtList;
		  }else{
			return this.curIdList;
		  }
	}
	public void setCurIdList(List curIdList) {
		this.curIdList = curIdList;
	}
	public List getDataTypeList() {
		if(this.dataTypeList==null){	
			  List rtList=null;
			  try{
				  rtList=StrutsCodeLibDelegate.getRepTypes(Config.DATATYPE_TYPE);
				  
				  
			  }catch(Exception e){
				  e.printStackTrace();
				  return rtList;
			  }
			  return rtList;
		  }else{
			return this.dataTypeList;
		  }
		
	}
	public void setDataTypeList(List dataTypeList) {
		this.dataTypeList = dataTypeList;
	}
	public List getPsuziTypeList() {
		if(this.psuziTypeList==null){	
			  List rtList=null;
			  try{
				  rtList=StrutsCodeLibDelegate.getRepTypes(Config.PSUZITYPE_TYPE);
				  
				  
			  }catch(Exception e){
				  e.printStackTrace();
				  return rtList;
			  }
			  return rtList;
		  }else{
			return this.psuziTypeList;
		  }
		
	}
	public void setPsuziTypeList(List psuziTypeList) {
		this.psuziTypeList = psuziTypeList;
	}
	public List getDanweiIdList() {
		if(this.danweiIdList==null){	
			  List rtList=null;
			  try{
				  rtList=StrutsCodeLibDelegate.getRepTypes(Config.DANWEIID_TYPE);
				  
				  
			  }catch(Exception e){
				  e.printStackTrace();
				  return rtList;
			  }
			  return rtList;
		  }else{
			return this.danweiIdList;
		  }
		
	}
	public void setDanweiIdList(List danweiIdList) {
		this.danweiIdList = danweiIdList;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getDataTypeName() {
		return dataTypeName;
	}
	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}
	public String getPsuziTypeName() {
		return psuziTypeName;
	}
	public void setPsuziTypeName(String psuziTypeName) {
		this.psuziTypeName = psuziTypeName;
	}
	public String getDanweiName() {
		return danweiName;
	}
	public void setDanweiName(String danweiName) {
		this.danweiName = danweiName;
	}
}
