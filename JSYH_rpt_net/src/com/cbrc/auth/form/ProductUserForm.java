
package com.cbrc.auth.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for productUser.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="productUserForm"
 */
public class ProductUserForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Long _productUserId = null;
   private java.lang.String _productUserName = null;
   private String flag=null;
   
   public String getFlag() {
	 return flag;
   }
	
   public void setFlag(String flag) {
	 this.flag = flag;
   }

   /**
    * Standard constructor.
    */
   public ProductUserForm() {
   }

   /**
    * Returns the productUserId
    *
    * @return the productUserId
    */
   public java.lang.Long getProductUserId() {
      return _productUserId;
   }

   /**
    * Sets the productUserId
    *
    * @param productUserId the new productUserId value
    */
   public void setProductUserId(java.lang.Long productUserId) {
      _productUserId = productUserId;
   }
   /**
    * Returns the productUserName
    *
    * @return the productUserName
    */
   public java.lang.String getProductUserName() {
      return _productUserName;
   }

   /**
    * Sets the productUserName
    *
    * @param productUserName the new productUserName value
    */
   public void setProductUserName(java.lang.String productUserName) {
      _productUserName = productUserName;
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
