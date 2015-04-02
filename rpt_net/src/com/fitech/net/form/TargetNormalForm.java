package com.fitech.net.form;

import org.apache.struts.action.ActionForm;


public class TargetNormalForm extends ActionForm {

  private Integer normalId=null;
  
  private String normalName=null;
  
  private String normalNote=null;



/**
 * @return Returns the normalName.
 */
public String getNormalName() {
	return normalName;
}

/**
 * @param normalName The normalName to set.
 */
public void setNormalName(String normalName) {
	this.normalName = normalName;
}

/**
 * @return Returns the normalNote.
 */
public String getNormalNote() {
	return normalNote;
}

/**
 * @param normalNote The normalNote to set.
 */
public void setNormalNote(String normalNote) {
	this.normalNote = normalNote;
}

/**
 * @return Returns the normalId.
 */
public Integer getNormalId() {
	return normalId;
}

/**
 * @param normalId The normalId to set.
 */
public void setNormalId(Integer normalId) {
	this.normalId = normalId;
}
 
  
	  
  
}
