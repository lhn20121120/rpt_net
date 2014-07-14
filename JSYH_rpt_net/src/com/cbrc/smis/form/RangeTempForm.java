
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for RangeTemp.
 *
 * @author jcm
 *
 * @struts.form name="RangeTempForm"
 */
public class RangeTempForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _orgId = null;

   private java.lang.String _childRepId = null;
   private java.lang.String _versionId = null;
   private java.lang.String _orgClsId = null;
      
   public java.lang.String getChildRepId() {
	   return _childRepId;
   }
	
	public void setChildRepId(java.lang.String repId) {
		_childRepId = repId;
	}
	
	
	public java.lang.String getOrgClsId() {
		return _orgClsId;
	}
	
	
	public void setOrgClsId(java.lang.String clsId) {
		_orgClsId = clsId;
	}
	
	
	public java.lang.String getOrgId() {
		return _orgId;
	}
	
	
	public void setOrgId(java.lang.String id) {
		_orgId = id;
	}


	public java.lang.String getVersionId() {
		return _versionId;
	}
	
	
	public void setVersionId(java.lang.String id) {
		_versionId = id;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
	      ActionErrors errors = new ActionErrors();
	      return errors;
	}

	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		this._orgId = null;
	}
	
}
