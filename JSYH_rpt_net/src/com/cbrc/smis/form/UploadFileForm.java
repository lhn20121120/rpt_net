package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * �����ϴ��ļ�
 * @author masclnj
 */
public class UploadFileForm extends ActionForm {
	
	/**
	 * �ϴ��ļ�����
	 */
   private FormFile formFile=null; 
   
   
   
   /**
    * �汾����
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
