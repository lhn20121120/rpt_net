
package com.cbrc.auth.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MUserToGrp.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MUserToGrpForm"
 */
public class MUserToGrpForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
   
   private Long userToGrpId = null;
   
   private java.lang.Long _userGrpId = null;
   private java.lang.Long _userId = null;
   /**
    * 用户名称
    */
   private String userName=null;
   
   /**
    * 用户组名称
    */
   private String userGrpNm = null;
   /**
    * 选择的用户组id字串
    */
   private String selectedUserGrpIds = null;
   /**
    * 所有用户组信息 
    */
   private String allUserGrp = null;
   
/**
    * Standard constructor.
    */
   public MUserToGrpForm() {
   }

   /**
    * Returns the userGrpId
    *
    * @return the userGrpId
    */
   public java.lang.Long getUserGrpId() {
      return _userGrpId;
   }

   /**
    * Sets the userGrpId
    *
    * @param userGrpId the new userGrpId value
    */
   public void setUserGrpId(java.lang.Long userGrpId) {
      _userGrpId = userGrpId;
   }
   /**
    * Returns the userId
    *
    * @return the userId
    */
   public java.lang.Long getUserId() {
      return _userId;
   }

   /**
    * Sets the userId
    *
    * @param userId the new userId value
    */
   public void setUserId(java.lang.Long userId) {
      _userId = userId;
   }
   public String getUserGrpNm() {
       return userGrpNm;
       }
       
       public void setUserGrpNm(String userGrpNm) {
           this.userGrpNm = userGrpNm;
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

    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public Long getUserToGrpId() {
        return userToGrpId;
    }
    
    public void setUserToGrpId(Long userToGrpId) {
        this.userToGrpId = userToGrpId;
    }
    
    public String getAllUserGrp() {
        return allUserGrp;
    }
    
    public void setAllUserGrp(String allUserGrp) {
        this.allUserGrp = allUserGrp;
    }
    
    public String getSelectedUserGrpIds() {
        return selectedUserGrpIds;
    }
    
    public void setSelectedUserGrpIds(String selectedUserGrpIds) {
        this.selectedUserGrpIds = selectedUserGrpIds;
    }
}
