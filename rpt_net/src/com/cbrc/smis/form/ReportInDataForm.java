package com.cbrc.smis.form;

import java.sql.Blob;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/**
 * 内网实际报表的XML内容ActionForm类
 * 
 * @author rds
 * @serialData 2005-12-15
 */
public class ReportInDataForm extends ActionForm {
	/**
	 * 实际报表的ID
	 */
	private Integer repInId=null;
	/**
	 * 报表的XML文件内容
	 */
	private Blob xml=null;
	/**
	 * XML的大小
	 */
	private Integer xmlSize=null;
	
	public ReportInDataForm(){}
	
	public Integer getRepInId() {
		return repInId;
	}
	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}
	public Blob getXml() {
		return xml;
	}
	public void setXml(Blob xml) {
		this.xml = xml;
	}
	public Integer getXmlSize() {
		return xmlSize;
	}
	public void setXmlSize(Integer xmlSize) {
		this.xmlSize = xmlSize;
	}
	
	public void reset(ActionMapping mapping,HttpServletRequest request){
		this.repInId=null;
		this.xml=null;
		this.xmlSize=null;
	}
}
