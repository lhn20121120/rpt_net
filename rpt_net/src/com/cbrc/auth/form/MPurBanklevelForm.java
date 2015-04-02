
package com.cbrc.auth.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MPurOrgForm.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MPurOrgForm"
 */
public class MPurBanklevelForm extends ActionForm {
	/**
	 * 用户组ID
	 */   
	private java.lang.Long userGrpId = null;   
	/**
	 * 行级定义ID
	 */
	private java.lang.String bankLevelId = null;   
	/**
	 * 行级定义名称
	 */
	private java.lang.String bankLevelName = null;
	/**
	 * 报表ID
	 */
	private java.lang.String childRepId = null;
	/**
	 * 权限类型
	 */
	private java.lang.Integer powType = null;
	/**
	 * 选择的机构编号字串，机构id之间用“,”号隔开
	 */
	private String selectOrgIds = null;   
	/**
	 * 选择的机构名称字串（机构名称之间用“,”号隔开）
	 */
	private String selectOrgNames = null;
	/**
	 * 用户组名称
	 */
	private String userGrpNm =null;   
	/**
	 * 选择的子报表id（报表ID之间用“,”号隔开）
	 */
	private String selectRepIds = null;
	/**
	 * 选择的行级ID字串，行级ID之间用“,”号隔开
	 */
	private String selectBankLevelIds = null;
	/**
	 * 选择的行级名称字串，行级名称之间用“,”号隔开
	 */
	private String selectBankLevelNames = null;
	/**
	 * 选择的机构----无用
	 */	 
	private String selectOrg = null;
	/**
	 * 机构列表 ---- 无用
	 */	
	private String orgList = null;
	/**
	 * 选择的行级----无用
	 */
	private String selectBankLevel = null;
	/**
	 * 行级列表----无用
	 */
	private String bankLevelList = null;
	/**
	 * 报表选择列表---无用
	 */	  
	private String selectRepList = null;
	/**
	 * Standard constructor.
	 */  
	/**
	 * 翻页信息
	 */
	private String curPage=null;
	
	
	public String getCurPage() {
		return curPage;
	}


	public void setCurPage(String curPage) {
		this.curPage = curPage;
	}


	public MPurBanklevelForm() {   
	}

	
	/**
	 * @return Returns the bankLevelId.
	 */
	public java.lang.String getBankLevelId() {
		return bankLevelId;
	}


	/**
	 * @param bankLevelId The bankLevelId to set.
	 */
	public void setBankLevelId(java.lang.String bankLevelId) {
		this.bankLevelId = bankLevelId;
	}
	

	/**
	 * @return Returns the bankLevelName.
	 */
	public java.lang.String getBankLevelName() {
		return bankLevelName;
	}


	/**
	 * @param bankLevelName The bankLevelName to set.
	 */
	public void setBankLevelName(java.lang.String bankLevelName) {
		this.bankLevelName = bankLevelName;
	}


	/**
	 * @return Returns the childRepId.
	 */
	public java.lang.String getChildRepId() {
		return childRepId;
	}


	/**
	 * @param childRepId The childRepId to set.
	 */
	public void setChildRepId(java.lang.String childRepId) {
		this.childRepId = childRepId;
	}


	/**
	 * @return Returns the selectBankLevelIds.
	 */
	public String getSelectBankLevelIds() {
		return selectBankLevelIds;
	}


	/**
	 * @param selectBankLevelIds The selectBankLevelIds to set.
	 */
	public void setSelectBankLevelIds(String selectBankLevelIds) {
		this.selectBankLevelIds = selectBankLevelIds;
	}


	/**
	 * @return Returns the selectBankLevelNames.
	 */
	public String getSelectBankLevelNames() {
		return selectBankLevelNames;
	}


	/**
	 * @param selectBankLevelNames The selectBankLevelNames to set.
	 */
	public void setSelectBankLevelNames(String selectBankLevelNames) {
		this.selectBankLevelNames = selectBankLevelNames;
	}


	/**
	 * @return Returns the selectOrgIds.
	 */
	public String getSelectOrgIds() {
		return selectOrgIds;
	}


	/**
	 * @param selectOrgIds The selectOrgIds to set.
	 */
	public void setSelectOrgIds(String selectOrgIds) {
		this.selectOrgIds = selectOrgIds;
	}


	/**
	 * @return Returns the selectOrgNames.
	 */
	public String getSelectOrgNames() {
		return selectOrgNames;
	}


	/**
	 * @param selectOrgNames The selectOrgNames to set.
	 */
	public void setSelectOrgNames(String selectOrgNames) {
		this.selectOrgNames = selectOrgNames;
	}


	/**
	 * @return Returns the selectRepIds.
	 */
	public String getSelectRepIds() {
		return selectRepIds;
	}


	/**
	 * @param selectRepIds The selectRepIds to set.
	 */
	public void setSelectRepIds(String selectRepIds) {
		this.selectRepIds = selectRepIds;
	}


	/**
	 * @return Returns the userGrpId.
	 */
	public java.lang.Long getUserGrpId() {
		return userGrpId;
	}


	/**
	 * @param userGrpId The userGrpId to set.
	 */
	public void setUserGrpId(java.lang.Long userGrpId) {
		this.userGrpId = userGrpId;
	}


	/**
	 * @return Returns the userGrpNm.
	 */
	public String getUserGrpNm() {
		return userGrpNm;
	}


	/**
	 * @param userGrpNm The userGrpNm to set.
	 */
	public void setUserGrpNm(String userGrpNm) {
		this.userGrpNm = userGrpNm;
	}

	
	/**
	 * @return Returns the bankLevelList.
	 */
	public String getBankLevelList() {
		return bankLevelList;
	}


	/**
	 * @param bankLevelList The bankLevelList to set.
	 */
	public void setBankLevelList(String bankLevelList) {
		this.bankLevelList = bankLevelList;
	}


	/**
	 * @return Returns the orgList.
	 */
	public String getOrgList() {
		return orgList;
	}


	/**
	 * @param orgList The orgList to set.
	 */
	public void setOrgList(String orgList) {
		this.orgList = orgList;
	}


	/**
	 * @return Returns the selectBankLevel.
	 */
	public String getSelectBankLevel() {
		return selectBankLevel;
	}


	/**
	 * @param selectBankLevel The selectBankLevel to set.
	 */
	public void setSelectBankLevel(String selectBankLevel) {
		this.selectBankLevel = selectBankLevel;
	}


	/**
	 * @return Returns the selectOrg.
	 */
	public String getSelectOrg() {
		return selectOrg;
	}


	/**
	 * @param selectOrg The selectOrg to set.
	 */
	public void setSelectOrg(String selectOrg) {
		this.selectOrg = selectOrg;
	}


	/**
	 * @return Returns the powType.
	 */
	public java.lang.Integer getPowType() {
		return powType;
	}


	/**
	 * @param powType The powType to set.
	 */
	public void setPowType(java.lang.Integer powType) {
		this.powType = powType;
	}


	/**
	 * @return Returns the selectRepList.
	 */
	public String getSelectRepList() {
		return selectRepList;
	}


	/**
	 * @param selectRepList The selectRepList to set.
	 */
	public void setSelectRepList(String selectRepList) {
		this.selectRepList = selectRepList;
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
