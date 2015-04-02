
package com.cbrc.auth.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MUserGrp.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MUserGrpForm"
 */
public class MUserGrpForm extends ActionForm {
   
	private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
   
	private java.lang.Long _userGrpId = null;   
	private java.lang.String _userGrpNm = null;   
	private java.lang.String setOrgId = null;   
	private java.lang.String setOrgName = null;   
	private String userAndGrp = null;
	private Integer powType = null;
    private String oldUserGrpName=null;
	public String getoldUserGrpName() {
		return oldUserGrpName;
	}

	public void setoldUserGrpName(String userGrpName) {
		oldUserGrpName = userGrpName;
	}

	/**
	 * Standard constructor.
	 */
	public MUserGrpForm() {   
	}

	public java.lang.String getSetOrgId() {
		return setOrgId;
	}
	
	public void setSetOrgId(java.lang.String setOrgId) {
		this.setOrgId = setOrgId;
	}
		
	public java.lang.String getSetOrgName() {
		return setOrgName;
	}
	
	public void setSetOrgName(java.lang.String setOrgName) {
		this.setOrgName = setOrgName;
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
    * Returns the userGrpNm
    *
    * @return the userGrpNm
    */
   public java.lang.String getUserGrpNm() {
      return _userGrpNm;
   }

   /**
    * Sets the userGrpNm
    *
    * @param userGrpNm the new userGrpNm value
    */
   public void setUserGrpNm(java.lang.String userGrpNm) {
      _userGrpNm = userGrpNm;
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

	/**
	 * @return Returns the userAndGrp.
	 */
	public String getUserAndGrp() {
		return userAndGrp;
	}
		
	/**
	 * @param userAndGrp The userAndGrp to set.
	 */
	public void setUserAndGrp(String userAndGrp) {
		this.userAndGrp = userAndGrp;
	}

	/**
	 * @return Returns the powType.
	 */
	public Integer getPowType() {
		return powType;
	}

	/**
	 * @param powType The powType to set.
	 */
	public void setPowType(Integer powType) {
		this.powType = powType;
	}	
}
