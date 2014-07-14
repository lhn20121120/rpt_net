package com.fitech.net.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cbrc.smis.hibernate.DocumentDetail;

/**
 *������
 *���ڣ�2008-1-9
 *���ߣ��ܷ���
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
		 * ���� childRepId
		 */
		public String getChildRepId() {
			return childRepId;
		}
		/**
		 * ������childRepId 
		 * ���� childRepId
		 */
		public void setChildRepId(String childRepId) {
			this.childRepId = childRepId;
		}
		/**
		 * ���� curId
		 */
		public Integer getCurId() {
			return curId;
		}
		/**
		 * ������curId 
		 * ���� curId
		 */
		public void setCurId(Integer curId) {
			this.curId = curId;
		}
		/**
		 * ���� dataRangeId
		 */
		public Integer getDataRangeId() {
			return dataRangeId;
		}
		/**
		 * ������dataRangeId 
		 * ���� dataRangeId
		 */
		public void setDataRangeId(Integer dataRangeId) {
			this.dataRangeId = dataRangeId;
		}
		/**
		 * ���� orgId
		 */
		public String getOrgId() {
			return orgId;
		}
		/**
		 * ������orgId 
		 * ���� orgId
		 */
		public void setOrgId(String orgId) {
			this.orgId = orgId;
		}
		/**
		 * ���� versionId
		 */
		public String getVersionId() {
			return versionId;
		}
		/**
		 * ������versionId 
		 * ���� versionId
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

