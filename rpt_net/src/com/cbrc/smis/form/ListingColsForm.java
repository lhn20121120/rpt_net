package com.cbrc.smis.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * 清单式报表列对照表的实现类
 * 
 * @author IBMUSER
 * @serialData 2005-12-12
 */
public class ListingColsForm extends ActionForm {
	/**
	 * 子报表ID
	 */
	private String childRepId=null;
	/**
	 * 版本号
	 */
	private String versionId=null;
	/**
	 * PDF报本模板中的列名
	 */
	private String pdfColName=null;
	/**
	 * 数据表中的列名
	 */
	private String dbColName=null;
	
	public ListingColsForm(){}

	public String getChildRepId() {
		return childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public String getDbColName() {
		return dbColName;
	}

	public void setDbColName(String dbColName) {
		this.dbColName = dbColName;
	}

	public String getPdfColName() {
		return pdfColName;
	}

	public void setPdfColName(String pdfColName) {
		this.pdfColName = pdfColName;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	
	/**
	 * reset方法
	 * 
	 * @return void
	 */
	public void reset(ActionMapping mapping,HttpServletRequest request){
		this.childRepId=null;
		this.versionId=null;
		this.pdfColName=null;
		this.dbColName=null;
	}
}
