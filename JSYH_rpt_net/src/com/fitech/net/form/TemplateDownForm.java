package com.fitech.net.form;

import org.apache.struts.action.ActionForm;


/**
 * Form for MCell.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MCellForm"
 */
public class TemplateDownForm extends ActionForm {

  private String[] strArray=null;
  
  private String name=null;
 
  
	/**
	 * @return Returns the strArray.
	 */
	public String[] getStrArray() {
		return strArray;
	}
	
	/**
	 * @param strArray The strArray to set.
	 */
	public void setStrArray(String[] strArray) {
		this.strArray = strArray;
	}
	
	public TemplateDownForm()
	{
		
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
  
  
}
