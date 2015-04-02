
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
	 * �û���ID
	 */   
	private java.lang.Long userGrpId = null;   
	/**
	 * �м�����ID
	 */
	private java.lang.String bankLevelId = null;   
	/**
	 * �м���������
	 */
	private java.lang.String bankLevelName = null;
	/**
	 * ����ID
	 */
	private java.lang.String childRepId = null;
	/**
	 * Ȩ������
	 */
	private java.lang.Integer powType = null;
	/**
	 * ѡ��Ļ�������ִ�������id֮���á�,���Ÿ���
	 */
	private String selectOrgIds = null;   
	/**
	 * ѡ��Ļ��������ִ�����������֮���á�,���Ÿ�����
	 */
	private String selectOrgNames = null;
	/**
	 * �û�������
	 */
	private String userGrpNm =null;   
	/**
	 * ѡ����ӱ���id������ID֮���á�,���Ÿ�����
	 */
	private String selectRepIds = null;
	/**
	 * ѡ����м�ID�ִ����м�ID֮���á�,���Ÿ���
	 */
	private String selectBankLevelIds = null;
	/**
	 * ѡ����м������ִ����м�����֮���á�,���Ÿ���
	 */
	private String selectBankLevelNames = null;
	/**
	 * ѡ��Ļ���----����
	 */	 
	private String selectOrg = null;
	/**
	 * �����б� ---- ����
	 */	
	private String orgList = null;
	/**
	 * ѡ����м�----����
	 */
	private String selectBankLevel = null;
	/**
	 * �м��б�----����
	 */
	private String bankLevelList = null;
	/**
	 * ����ѡ���б�---����
	 */	  
	private String selectRepList = null;
	/**
	 * Standard constructor.
	 */  
	/**
	 * ��ҳ��Ϣ
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
