package com.fitech.net.form;

import org.apache.struts.action.ActionForm;


public class TargetBusinessForm extends ActionForm {

  private Integer businessId=null;
  
  private String businessName=null;
  
  private String businessNote=null;



/**
 * @return Returns the businessName.
 */
public String getBusinessName() {
	return businessName;
}

/**
 * @param businessName The businessName to set.
 */
public void setBusinessName(String businessName) {
	this.businessName = businessName;
}

/**
 * @return Returns the businessNote.
 */
public String getBusinessNote() {
	return businessNote;
}

/**
 * @param businessNote The businessNote to set.
 */
public void setBusinessNote(String businessNote) {
	this.businessNote = businessNote;
}

/**
 * @return Returns the businessId.
 */
public Integer getBusinessId() {
	return businessId;
}

/**
 * @param businessId The businessId to set.
 */
public void setBusinessId(Integer businessId) {
	this.businessId = businessId;
}  
  
}
