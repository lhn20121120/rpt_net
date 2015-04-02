package com.cbrc.smis.hibernate;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class RangeTemp implements Serializable {

	/**
	 * ����ID
	 */
	private String orgId;
	
	/**
	 * �ӱ���ID
	 */
	private String childRepId;
	
	/**
	 * ����汾��
	 */
	private String versionId;
	
     /**
     * �������ID
     */
    private String orgClsId;
            
    
	public RangeTemp(String childRepId, String orgClsId, String orgId, String versionId) {
		this.childRepId = childRepId;
		this.orgClsId = orgClsId;
		this.orgId = orgId;
		this.versionId = versionId;
	}
	
	public RangeTemp(String childRepId, String versionId) {
		this.orgId = null;
		this.orgClsId = null;
		this.childRepId = childRepId;
		this.versionId = versionId;
	}
	
	/** default constructor */
    public RangeTemp() {
    }

	public String getOrgClsId() {
		return orgClsId;
	}

	public void setOrgClsId(String orgClsId) {
		this.orgClsId = orgClsId;
	}

	public String getChildRepId() {
		return childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	
}
