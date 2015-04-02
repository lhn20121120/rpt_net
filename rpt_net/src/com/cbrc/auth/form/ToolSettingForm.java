
package com.cbrc.auth.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for toolSetting.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="toolSettingForm"
 */
public class ToolSettingForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Long _menuId = null;
   private java.lang.String _menuName = null;
   private java.lang.String _functionName = null;
   private java.lang.String _url = null;
   private java.lang.Long _priorId = null;  

   /**
    * Standard constructor.
    */
   public ToolSettingForm() {
   }

   /**
    * Returns the menuId
    *
    * @return the menuId
    */
   public java.lang.Long getMenuId() {
      return _menuId;
   }

   /**
    * Sets the menuId
    *
    * @param menuId the new menuId value
    */
   public void setMenuId(java.lang.Long menuId) {
      _menuId = menuId;
   }
   /**
    * Returns the menuName
    *
    * @return the menuName
    */
   public java.lang.String getMenuName() {
      return _menuName;
   }

   /**
    * Sets the menuName
    *
    * @param menuName the new menuName value
    */
   public void setMenuName(java.lang.String menuName) {
      _menuName = menuName;
   }
   /**
    * Returns the functionName
    *
    * @return the functionName
    */
   public java.lang.String getFunctionName() {
      return _functionName;
   }

   /**
    * Sets the functionName
    *
    * @param functionName the new functionName value
    */
   public void setFunctionName(java.lang.String functionName) {
      _functionName = functionName;
   }
   /**
    * Returns the url
    *
    * @return the url
    */
   public java.lang.String getUrl() {
      return _url;
   }

   /**
    * Sets the url
    *
    * @param url the new url value
    */
   public void setUrl(java.lang.String url) {
      _url = url;
   }
   /**
    * Returns the priorId
    *
    * @return the priorId
    */
   public java.lang.Long getPriorId() {
      return _priorId;
   }

   /**
    * Sets the priorId
    *
    * @param priorId the new priorId value
    */
   public void setPriorId(java.lang.Long priorId) {
      _priorId = priorId;
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
      return errors;
   }   
}
