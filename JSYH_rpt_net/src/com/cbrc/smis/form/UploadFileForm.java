package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * 用来上传文件
 * @author masclnj
 */
public class UploadFileForm extends ActionForm {
	
	/**
	 * 上传文件对象
	 */
   private FormFile formFile=null; 
   
   
   
   /**
    * 版本号码
    */
   private String versionId=null;
   

	/**
	 * @return Returns the formFile.
	 */
	public FormFile getFormFile() {
		return formFile;
	}
	
	
	/**
	 * @param formFile The formFile to set.
	 */
	public void setFormFile(FormFile formFile) {
		this.formFile = formFile;
	}


	/**
	 * @return Returns the versionId.
	 */
	public String getVersionId() {
		return versionId;
	}


	/**
	 * @param versionId The versionId to set.
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
}
