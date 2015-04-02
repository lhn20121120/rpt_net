package com.fitech.net.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cbrc.smis.hibernate.DocumentDetail;

/**
 *描述：
 *日期：2008-1-9
 *作者：曹发根
 */
public class ViewMReportPK  implements Serializable{
	 private String childRepId;
	    private String versionId;
	    private String orgId;
	    private Integer dataRangeId;	
		private Integer curId;
		
		public ViewMReportPK(){
			
		}
		/**
		 * 返回 childRepId
		 */
		public String getChildRepId() {
			return childRepId;
		}
		/**
		 * 参数：childRepId 
		 * 设置 childRepId
		 */
		public void setChildRepId(String childRepId) {
			this.childRepId = childRepId;
		}
		/**
		 * 返回 curId
		 */
		public Integer getCurId() {
			return curId;
		}
		/**
		 * 参数：curId 
		 * 设置 curId
		 */
		public void setCurId(Integer curId) {
			this.curId = curId;
		}
		/**
		 * 返回 dataRangeId
		 */
		public Integer getDataRangeId() {
			return dataRangeId;
		}
		/**
		 * 参数：dataRangeId 
		 * 设置 dataRangeId
		 */
		public void setDataRangeId(Integer dataRangeId) {
			this.dataRangeId = dataRangeId;
		}
		/**
		 * 返回 orgId
		 */
		public String getOrgId() {
			return orgId;
		}
		/**
		 * 参数：orgId 
		 * 设置 orgId
		 */
		public void setOrgId(String orgId) {
			this.orgId = orgId;
		}
		/**
		 * 返回 versionId
		 */
		public String getVersionId() {
			return versionId;
		}
		/**
		 * 参数：versionId 
		 * 设置 versionId
		 */
		public void setVersionId(String versionId) {
			this.versionId = versionId;
		}
	    public String toString() {
	        return new ToStringBuilder(this)
	            .append("childRepId", getChildRepId()).append("versionId",getVersionId())
	            .append("orgId",getOrgId()).append("dataRangeId",getDataRangeId()).append("curId",getCurId())
	            .toString();
	    }

	    public boolean equals(Object other) {
	        if ( !(other instanceof ViewMReportPK) ) return false;
	        ViewMReportPK castOther = (ViewMReportPK) other;
	        return new EqualsBuilder()
	            .append(this.toString(), castOther.toString())
	            .isEquals();
	    }

	    public int hashCode() {
	        return new HashCodeBuilder()
	        .append(getChildRepId()).append(getVersionId())
            .append(getOrgId()).append(getDataRangeId()).append(getCurId())
            .toHashCode();
	    }

}

