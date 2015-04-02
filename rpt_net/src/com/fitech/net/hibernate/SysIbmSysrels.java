
package com.fitech.net.hibernate;

/**
 * DB2数据库系统表
 * @author jcm
 *
 */
public class SysIbmSysrels{
	/**创建主表用户**/
	private String creator;
	
	/**主表名**/
	private String tbName;
	
	/**关联名称**/
	private String relName;
	
	/**从表名**/
	private String refTBName;
	
	/**创建从表用户**/
	private String refTBCreator;
	
	/**列数量**/
	private Integer colCount;
	
	/**删除规则**/
	private String deleteRule;
	
	/**更新规则**/
	private String updateRule;
	
	/**主表关联字段**/
	private String fkColNames;
	
	/**从表关联字段**/
	private String pkColNames;
	
	/**从表关联名称**/
	private String refKeyName;
	
	/**定义用户**/
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
