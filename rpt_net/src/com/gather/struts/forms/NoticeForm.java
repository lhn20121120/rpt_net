
package com.gather.struts.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for notice.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="noticeForm"
 */
public class NoticeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private Integer _noticeId = null;
   private java.lang.String _noticeTitle = null;
   private java.lang.String _content = null;
   private java.util.Date _publishTime = null;
   private Integer _state = null;

   /**
    * Standard constructor.
    */
   public NoticeForm() {
   }

   /**
    * Returns the noticeId
    *
    * @return the noticeId
    */
   public Integer getNoticeId() {
      return _noticeId;
   }

   /**
    * Sets the noticeId
    *
    * @param noticeId the new noticeId value
    */
   public void setNoticeId(Integer noticeId) {
      _noticeId = noticeId;
   }
   /**
    * Returns the noticeTitle
    *
    * @return the noticeTitle
    */
   public java.lang.String getNoticeTitle() {
      return _noticeTitle;
   }

   /**
    * Sets the noticeTitle
    *
    * @param noticeTitle the new noticeTitle value
    */
   public void setNoticeTitle(java.lang.String noticeTitle) {
      _noticeTitle = noticeTitle;
   }
   /**
    * Returns the content
    *
    * @return the content
    */
   public java.lang.String getContent() {
      return _content;
   }

   /**
    * Sets the content
    *
    * @param content the new content value
    */
   public void setContent(java.lang.String content) {
      _content = content;
   }
   /**
    * Returns the publishTime
    *
    * @return the publishTime
    */
   public java.util.Date getPublishTime() {
      return _publishTime;
   }

   /**
    * Returns the publishTime as a String
    *
    * @return the publishTime as a String
    */
   public String getPublishTimeAsString() {
      if( _publishTime != null ) {
         return FORMAT.format(_publishTime);
      }
      else {
         return "";
      }
   }

   /**
    * Sets the publishTime
    *
    * @param publishTime the new publishTime value
    */
   public void setPublishTime(java.util.Date publishTime) {
      _publishTime = publishTime;
   }

   /**
    * Sets the publishTime as a String.
    *
    * @param publishTime the new publishTime value as a String
    */
   public void setPublishTimeAsString(String publishTime) {
      try {
         _publishTime = new java.sql.Timestamp(FORMAT.parse(publishTime).getTime());
      } catch (ParseException pe) {
         _publishTime = new java.sql.Timestamp((new java.util.Date()).getTime());
      }
   }

   /**
    * Returns the state
    *
    * @return the state
    */
   public Integer getState() {
      return _state;
   }

   /**
    * Sets the state
    *
    * @param state the new state value
    */
   public void setState(Integer state) {
      _state = state;
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
      if(getNoticeId() == null) {
         errors.add("noticeId", new ActionError("error.noticeId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
