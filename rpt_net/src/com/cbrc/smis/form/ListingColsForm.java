package com.cbrc.smis.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * �嵥ʽ�����ж��ձ��ʵ����
 * 
 * @author IBMUSER
 * @serialData 2005-12-12
 */
public class ListingColsForm extends ActionForm {
	/**
	 * �ӱ���ID
	 */
	private String childRepId=null;
	/**
	 * �汾��
	 */
	private String versionId=null;
	/**
	 * PDF����ģ���е�����
	 */
	private String pdfColName=null;
	/**
	 * ���ݱ��е�����
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
	 * reset����
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
