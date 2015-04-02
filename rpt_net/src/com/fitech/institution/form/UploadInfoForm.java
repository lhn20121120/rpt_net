package com.fitech.institution.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class UploadInfoForm extends ActionForm
{
  private FormFile formFile;

  public FormFile getFormFile()
  {
    return this.formFile;
  }

  public void setFormFile(FormFile formFile) {
    this.formFile = formFile;
  }
}