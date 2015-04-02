
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MRepdesc.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MRepdescForm"
 */
public class MRepdescForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _reportdate = null;
   private Integer _term = null;
   private java.lang.String _torepdate = null;
   private java.lang.String _mkreppers = null;
   private java.lang.String _ckreppers = null;
   private java.lang.String _cgreppers = null;
   private Integer _datarangeid = null;
   private Integer _repid = null;
   private Integer _orgid = null;

   /**
    * Standard constructor.
    */
   public MRepdescForm() {
   }

   /**
    * Returns the reportdate
    *
    * @return the reportdate
    */
   public java.lang.String getReportdate() {
      return _reportdate;
   }

   /**
    * Sets the reportdate
    *
    * @param reportdate the new reportdate value
    */
   public void setReportdate(java.lang.String reportdate) {
      _reportdate = reportdate;
   }
   /**
    * Returns the term
    *
    * @return the term
    */
   public Integer getTerm() {
      return _term;
   }

   /**
    * Sets the term
    *
    * @param term the new term value
    */
   public void setTerm(Integer term) {
      _term = term;
   }
   /**
    * Returns the torepdate
    *
    * @return the torepdate
    */
   public java.lang.String getTorepdate() {
      return _torepdate;
   }

   /**
    * Sets the torepdate
    *
    * @param torepdate the new torepdate value
    */
   public void setTorepdate(java.lang.String torepdate) {
      _torepdate = torepdate;
   }
   /**
    * Returns the mkreppers
    *
    * @return the mkreppers
    */
   public java.lang.String getMkreppers() {
      return _mkreppers;
   }

   /**
    * Sets the mkreppers
    *
    * @param mkreppers the new mkreppers value
    */
   public void setMkreppers(java.lang.String mkreppers) {
      _mkreppers = mkreppers;
   }
   /**
    * Returns the ckreppers
    *
    * @return the ckreppers
    */
   public java.lang.String getCkreppers() {
      return _ckreppers;
   }

   /**
    * Sets the ckreppers
    *
    * @param ckreppers the new ckreppers value
    */
   public void setCkreppers(java.lang.String ckreppers) {
      _ckreppers = ckreppers;
   }
   /**
    * Returns the cgreppers
    *
    * @return the cgreppers
    */
   public java.lang.String getCgreppers() {
      return _cgreppers;
   }

   /**
    * Sets the cgreppers
    *
    * @param cgreppers the new cgreppers value
    */
   public void setCgreppers(java.lang.String cgreppers) {
      _cgreppers = cgreppers;
   }
   /**
    * Returns the datarangeid
    *
    * @return the datarangeid
    */
   public Integer getDatarangeid() {
      return _datarangeid;
   }

   /**
    * Sets the datarangeid
    *
    * @param datarangeid the new datarangeid value
    */
   public void setDatarangeid(Integer datarangeid) {
      _datarangeid = datarangeid;
   }
   /**
    * Returns the repid
    *
    * @return the repid
    */
   public Integer getRepid() {
      return _repid;
   }

   /**
    * Sets the repid
    *
    * @param repid the new repid value
    */
   public void setRepid(Integer repid) {
      _repid = repid;
   }
   /**
    * Returns the orgid
    *
    * @return the orgid
    */
   public Integer getOrgid() {
      return _orgid;
   }

   /**
    * Sets the orgid
    *
    * @param orgid the new orgid value
    */
   public void setOrgid(Integer orgid) {
      _orgid = orgid;
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
      if(getReportdate() == null) {
         errors.add("reportdate", new ActionError("error.reportdate.required"));
      }
      if(getDatarangeid() == null) {
         errors.add("datarangeid", new ActionError("error.datarangeid.required"));
      }
      if(getRepid() == null) {
         errors.add("repid", new ActionError("error.repid.required"));
      }
      if(getOrgid() == null) {
         errors.add("orgid", new ActionError("error.orgid.required"));
      }
      // TODO test format/data
      return errors;
   }
}
