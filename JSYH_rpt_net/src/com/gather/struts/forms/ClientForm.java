
package com.gather.struts.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for client.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="clientForm"
 */
public class ClientForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _id = null;
   private java.lang.String _clientVersion = null;
   private Integer _clientSize =null;
   private java.lang.String _note = null;
   private java.lang.String _clientEntity = null;
   private java.util.Date _downTime = null;

   /**
    * Standard constructor.
    */
   public ClientForm() {
   }

   /**
    * Returns the id
    *
    * @return the id
    */
   public java.lang.String getId() {
      return _id;
   }

   /**
    * Sets the id
    *
    * @param id the new id value
    */
   public void setId(java.lang.String id) {
      _id = id;
   }
   /**
    * Returns the clientVersion
    *
    * @return the clientVersion
    */
   public java.lang.String getClientVersion() {
      return _clientVersion;
   }

   /**
    * Sets the clientVersion
    *
    * @param clientVersion the new clientVersion value
    */
   public void setClientVersion(java.lang.String clientVersion) {
      _clientVersion = clientVersion;
   }
   /**
    * Returns the clientSize
    *
    * @return the clientSize
    */
   public Integer getClientSize() {
      return _clientSize;
   }

   /**
    * Sets the clientSize
    *
    * @param clientSize the new clientSize value
    */
   public void setClientSize(Integer clientSize) {
      _clientSize = clientSize;
   }
   /**
    * Returns the note
    *
    * @return the note
    */
   public java.lang.String getNote() {
      return _note;
   }

   /**
    * Sets the note
    *
    * @param note the new note value
    */
   public void setNote(java.lang.String note) {
      _note = note;
   }
   /**
    * Returns the clientEntity
    *
    * @return the clientEntity
    */
   public java.lang.String getClientEntity() {
      return _clientEntity;
   }

   /**
    * Sets the clientEntity
    *
    * @param clientEntity the new clientEntity value
    */
   public void setClientEntity(java.lang.String clientEntity) {
      _clientEntity = clientEntity;
   }
   /**
    * Returns the downTime
    *
    * @return the downTime
    */
   public java.util.Date getDownTime() {
      return _downTime;
   }

   /**
    * Returns the downTime as a String
    *
    * @return the downTime as a String
    */
   public String getDownTimeAsString() {
      if( _downTime != null ) {
         return FORMAT.format(_downTime);
      }
      else {
         return "";
      }
   }

   /**
    * Sets the downTime
    *
    * @param downTime the new downTime value
    */
   public void setDownTime(java.util.Date downTime) {
      _downTime = downTime;
   }

   /**
    * Sets the downTime as a String.
    *
    * @param downTime the new downTime value as a String
    */
   public void setDownTimeAsString(String downTime) {
      try {
         _downTime = new java.sql.Timestamp(FORMAT.parse(downTime).getTime());
      } catch (ParseException pe) {
         _downTime = new java.sql.Timestamp((new java.util.Date()).getTime());
      }
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
      if(getId() == null) {
         errors.add("id", new ActionError("error.id.required"));
      }
      // TODO test format/data
      return errors;
   }
}
