
package com.cbrc.org.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MFinaOrg.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MFinaOrgForm"
 */
public class MFinaOrgForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _finaOrgCode = null;
   private java.lang.String _finaOrgNm = null;
   private java.lang.String _licence = null;
   private java.lang.String _address = null;
   private java.lang.String _orgId = null;
   private java.lang.String _corpAddress = null;
   private java.lang.String _openDate = null;
   private java.lang.String _bankStyle = null;
   private java.lang.String _corpName = null;

   /**
    * Standard constructor.
    */
   public MFinaOrgForm() {
   }

   /**
    * Returns the finaOrgCode
    *
    * @return the finaOrgCode
    */
   public java.lang.String getFinaOrgCode() {
      return _finaOrgCode;
   }

   /**
    * Sets the finaOrgCode
    *
    * @param finaOrgCode the new finaOrgCode value
    */
   public void setFinaOrgCode(java.lang.String finaOrgCode) {
      _finaOrgCode = finaOrgCode;
   }
   /**
    * Returns the finaOrgNm
    *
    * @return the finaOrgNm
    */
   public java.lang.String getFinaOrgNm() {
      return _finaOrgNm;
   }

   /**
    * Sets the finaOrgNm
    *
    * @param finaOrgNm the new finaOrgNm value
    */
   public void setFinaOrgNm(java.lang.String finaOrgNm) {
      _finaOrgNm = finaOrgNm;
   }
   /**
    * Returns the licence
    *
    * @return the licence
    */
   public java.lang.String getLicence() {
      return _licence;
   }

   /**
    * Sets the licence
    *
    * @param licence the new licence value
    */
   public void setLicence(java.lang.String licence) {
      _licence = licence;
   }
   /**
    * Returns the address
    *
    * @return the address
    */
   public java.lang.String getAddress() {
      return _address;
   }

   /**
    * Sets the address
    *
    * @param address the new address value
    */
   public void setAddress(java.lang.String address) {
      _address = address;
   }
   /**
    * Returns the orgId
    *
    * @return the orgId
    */
   public java.lang.String getOrgId() {
      return _orgId;
   }

   /**
    * Sets the orgId
    *
    * @param orgId the new orgId value
    */
   public void setOrgId(java.lang.String orgId) {
      _orgId = orgId;
   }
   /**
    * Returns the corpAddress
    *
    * @return the corpAddress
    */
   public java.lang.String getCorpAddress() {
      return _corpAddress;
   }

   /**
    * Sets the corpAddress
    *
    * @param corpAddress the new corpAddress value
    */
   public void setCorpAddress(java.lang.String corpAddress) {
      _corpAddress = corpAddress;
   }
   /**
    * Returns the openDate
    *
    * @return the openDate
    */
   public java.lang.String getOpenDate() {
      return _openDate;
   }

   /**
    * Sets the openDate
    *
    * @param openDate the new openDate value
    */
   public void setOpenDate(java.lang.String openDate) {
      _openDate = openDate;
   }
   /**
    * Returns the bankStyle
    *
    * @return the bankStyle
    */
   public java.lang.String getBankStyle() {
      return _bankStyle;
   }

   /**
    * Sets the bankStyle
    *
    * @param bankStyle the new bankStyle value
    */
   public void setBankStyle(java.lang.String bankStyle) {
      _bankStyle = bankStyle;
   }
   /**
    * Returns the corpName
    *
    * @return the corpName
    */
   public java.lang.String getCorpName() {
      return _corpName;
   }

   /**
    * Sets the corpName
    *
    * @param corpName the new corpName value
    */
   public void setCorpName(java.lang.String corpName) {
      _corpName = corpName;
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
      if(getFinaOrgCode() == null) {
         errors.add("finaOrgCode", new ActionError("error.finaOrgCode.required"));
      }
      if(getOrgId() == null) {
         errors.add("orgId", new ActionError("error.orgId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
