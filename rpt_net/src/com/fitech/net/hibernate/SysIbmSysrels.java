
package com.fitech.net.hibernate;

/**
 * DB2���ݿ�ϵͳ��
 * @author jcm
 *
 */
public class SysIbmSysrels{
	/**���������û�**/
	private String creator;
	
	/**������**/
	private String tbName;
	
	/**��������**/
	private String relName;
	
	/**�ӱ���**/
	private String refTBName;
	
	/**�����ӱ��û�**/
	private String refTBCreator;
	
	/**������**/
	private Integer colCount;
	
	/**ɾ������**/
	private String deleteRule;
	
	/**���¹���**/
	private String updateRule;
	
	/**��������ֶ�**/
	private String fkColNames;
	
	/**�ӱ�����ֶ�**/
	private String pkColNames;
	
	/**�ӱ��������**/
	private String refKeyName;
	
	/**�����û�**/
	private String definer;

	/**
	 * @return Returns the colCount.
	 */
	public Integer getColCount() {
		return colCount;
	}

	/**
	 * @param colCount The colCount to set.
	 */
	public void setColCount(Integer colCount) {
		this.colCount = colCount;
	}

	/**
	 * @return Returns the creator.
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator The creator to set.
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return Returns the definer.
	 */
	public String getDefiner() {
		return definer;
	}

	/**
	 * @param definer The definer to set.
	 */
	public void setDefiner(String definer) {
		this.definer = definer;
	}

	/**
	 * @return Returns the deleteRule.
	 */
	public String getDeleteRule() {
		return deleteRule;
	}

	/**
	 * @param deleteRule The deleteRule to set.
	 */
	public void setDeleteRule(String deleteRule) {
		this.deleteRule = deleteRule;
	}

	/**
	 * @return Returns the fkColNames.
	 */
	public String getFkColNames() {
		return fkColNames;
	}

	/**
	 * @param fkColNames The fkColNames to set.
	 */
	public void setFkColNames(String fkColNames) {
		this.fkColNames = fkColNames;
	}

	/**
	 * @return Returns the pkColNames.
	 */
	public String getPkColNames() {
		return pkColNames;
	}

	/**
	 * @param pkColNames The pkColNames to set.
	 */
	public void setPkColNames(String pkColNames) {
		this.pkColNames = pkColNames;
	}

	/**
	 * @return Returns the refKeyName.
	 */
	public String getRefKeyName() {
		return refKeyName;
	}

	/**
	 * @param refKeyName The refKeyName to set.
	 */
	public void setRefKeyName(String refKeyName) {
		this.refKeyName = refKeyName;
	}

	/**
	 * @return Returns the refTBCreator.
	 */
	public String getRefTBCreator() {
		return refTBCreator;
	}

	/**
	 * @param refTBCreator The refTBCreator to set.
	 */
	public void setRefTBCreator(String refTBCreator) {
		this.refTBCreator = refTBCreator;
	}

	/**
	 * @return Returns the refTBName.
	 */
	public String getRefTBName() {
		return refTBName;
	}

	/**
	 * @param refTBName The refTBName to set.
	 */
	public void setRefTBName(String refTBName) {
		this.refTBName = refTBName;
	}

	/**
	 * @return Returns the relName.
	 */
	public String getRelName() {
		return relName;
	}

	/**
	 * @param relName The relName to set.
	 */
	public void setRelName(String relName) {
		this.relName = relName;
	}

	/**
	 * @return Returns the tbName.
	 */
	public String getTbName() {
		return tbName;
	}

	/**
	 * @param tbName The tbName to set.
	 */
	public void setTbName(String tbName) {
		this.tbName = tbName;
	}

	/**
	 * @return Returns the updateRule.
	 */
	public String getUpdateRule() {
		return updateRule;
	}

	/**
	 * @param updateRule The updateRule to set.
	 */
	public void setUpdateRule(String updateRule) {
		this.updateRule = updateRule;
	}
}
