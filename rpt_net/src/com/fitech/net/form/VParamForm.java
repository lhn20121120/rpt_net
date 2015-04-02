
package com.fitech.net.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MCurr.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MCurrForm"
 */
public class VParamForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private Integer _vparamId = null;
   private String _vpNote = null;
   private String _vpTableName = null;
   private String _vpColName = null;
   private String _vpTypeId = null;
   private Integer _vpParentId = null;

   /**
    * Standard constructor.
    */
   public VParamForm() {
   }

   


   public Integer getVParamId() {
	return _vparamId;
}




public void setVParamId(Integer id) {
	_vparamId = id;
}




public String getVPColName() {
	return _vpColName;
}




public void setVPColName(String colName) {
	_vpColName = colName;
}




public String getVPNote() {
	return _vpNote;
}




public void setVPNote(String note) {
	_vpNote = note;
}




public Integer getVPParentId() {
	return _vpParentId;
}




public void setVPParentId(Integer parentId) {
	_vpParentId = parentId;
}




public String getVPTableName() {
	return _vpTableName;
}




public void setVPTableName(String tableName) {
	_vpTableName = tableName;
}




public String getVPTypeId() {
	return _vpTypeId;
}




public void setVPTypeId(String typeId) {
	_vpTypeId = typeId;
}




/**
    * Validate the properties that have been set from this HTTP request,
    * and return an <code>ActionErrors</code> object that encapsulates any
    * validation errors that have been found.  If no errors are found, return
    * <code>null</code> or an <code>ActionErrors</code> object with no
    * recorded error messages.
    *
    * @param mapping The mapping used to select this instance
    * @param request The servlet request we are processing
    */
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
      ActionErrors errors = new ActionErrors();
      // test for nullity
      if(getVParamId() == null) {
         errors.add("curId", new ActionError("error.curId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
