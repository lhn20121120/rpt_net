package com.cbrc.smis.form;

import java.sql.Blob;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;


/**
 * 信息文件发布ActionForm类
 * 
 * @author rds
 * @serialData 2005-12-17 18:03
 */
public class InfoFilesForm extends ActionForm {
	/**
	 * 序号
	 */
	private Integer infoFilesId=null;
	/**
	 * 用户
	 */
	private Long userId=null;
	/**
	 * 文件名
	 */
	private String infoFileName=null;
	/**
	 * 文件类型
	 */
	private String infoFileType=null;
	/**
	 * 文件大小
	 */
	private Integer infoFileSize=null;
	/**
	 * 文件位置
	 */
	private String infoFileLocation=null;
	/**
	 * 文件描述
	 */
	private String infoFileDesc=null;
	/**
	 * 信息类别
	 */
	private String infoFileStyle=null;
	/**
	 * 记录时间
	 */
	private Date recordTime=null;
	/**
	 * 文件
	 */
	private FormFile infoFile=null;
	/**
	 * 用户名
	 */
	private String userName=null;
	/**
	 * 文件内容
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
	 * reset方法
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
