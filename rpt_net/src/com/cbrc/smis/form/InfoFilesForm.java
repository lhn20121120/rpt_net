package com.cbrc.smis.form;

import java.sql.Blob;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;


/**
 * ��Ϣ�ļ�����ActionForm��
 * 
 * @author rds
 * @serialData 2005-12-17 18:03
 */
public class InfoFilesForm extends ActionForm {
	/**
	 * ���
	 */
	private Integer infoFilesId=null;
	/**
	 * �û�
	 */
	private Long userId=null;
	/**
	 * �ļ���
	 */
	private String infoFileName=null;
	/**
	 * �ļ�����
	 */
	private String infoFileType=null;
	/**
	 * �ļ���С
	 */
	private Integer infoFileSize=null;
	/**
	 * �ļ�λ��
	 */
	private String infoFileLocation=null;
	/**
	 * �ļ�����
	 */
	private String infoFileDesc=null;
	/**
	 * ��Ϣ���
	 */
	private String infoFileStyle=null;
	/**
	 * ��¼ʱ��
	 */
	private Date recordTime=null;
	/**
	 * �ļ�
	 */
	private FormFile infoFile=null;
	/**
	 * �û���
	 */
	private String userName=null;
	/**
	 * �ļ�����
	 */
	private Blob blob = null;

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	public InfoFilesForm(){

	}
	
	public String getInfoFileDesc() {
		return infoFileDesc;
	}
	public void setInfoFileDesc(String infoFileDesc) {
		this.infoFileDesc = infoFileDesc;
	}
	public String getInfoFileLocation() {
		return infoFileLocation;
	}
	public void setInfoFileLocation(String infoFileLocation) {
		this.infoFileLocation = infoFileLocation;
	}
	public String getInfoFileName() {
		return infoFileName;
	}
	public void setInfoFileName(String infoFileName) {
		this.infoFileName = infoFileName;
	}
	public Integer getInfoFilesId() {
		return infoFilesId;
	}
	public void setInfoFilesId(Integer infoFilesId) {
		this.infoFilesId = infoFilesId;
	}
	public Integer getInfoFileSize() {
		return infoFileSize;
	}
	public void setInfoFileSize(Integer infoFileSize) {
		this.infoFileSize = infoFileSize;
	}
	public String getInfoFileStyle() {
		return infoFileStyle;
	}
	public void setInfoFileStyle(String infoFileStyle) {
		this.infoFileStyle = infoFileStyle;
	}
	public String getInfoFileType() {
		return infoFileType;
	}
	public void setInfoFileType(String infoFileType) {
		this.infoFileType = infoFileType;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * reset����
	 * 
	 * @return void
	 */
	public void reset(ActionMapping mapping,HttpServletRequest request){
		this.infoFilesId=null;
		this.userId=null;
		this.infoFileName=null;
		this.infoFileType=null;
		this.infoFileSize=null;
		this.infoFileLocation=null;
		this.infoFileDesc=null;
		this.infoFileStyle=null;
		this.recordTime=null;
		this.blob = null;
	}
	   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		      ActionErrors errors = new ActionErrors();
		     
		      return errors;
}

	public FormFile getInfoFile() {
		return infoFile;
	}

	public void setInfoFile(FormFile infoFile) {
		this.infoFile = infoFile;
	}
	public Blob getBlob() {
		return blob;
	}

	public void setBlob(Blob blob) {
		this.blob = blob;
	}
}
