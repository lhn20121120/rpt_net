package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class AFDataImporForm extends ActionForm {
	
	private FormFile idxFile;
	
	private FormFile datFile;
	
	private FormFile zipFile;

	public FormFile getZipFile() {
		return zipFile;
	}

	public void setZipFile(FormFile zipFile) {
		this.zipFile = zipFile;
	}

	public FormFile getIdxFile() {
		return idxFile;
	}

	public void setIdxFile(FormFile idxFile) {
		this.idxFile = idxFile;
	}

	public FormFile getDatFile() {
		return datFile;
	}

	public void setDatFile(FormFile datFile) {
		this.datFile = datFile;
	}

}
