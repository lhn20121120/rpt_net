package com.cbrc.smis.other;
/**
 *������Ϣ�鿴
 *ģ�飺�趨���ڻ��������Χ
 * @author Ҧ��
 */
public class OrgDetail {
    /**
     * ����id
     */
    private String orgId;
    /**
     * ��������
     */
    private String orgName;
    /**
     * ������������
     */
    private String orgClsName;
    /**
     * ��ѡ���Ƿ�ѡ��
     */
    private String checked;
    
    private String childRepId=null;
    
    private String versionId=null;
    
    private String orgClsId=null;
    
    
    

    public String getChildRepId() {
		return childRepId;
	}
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
	public String getOrgClsId() {
		return orgClsId;
	}
	public void setOrgClsId(String orgClsId) {
		this.orgClsId = orgClsId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getChecked()
    {
        return this.checked;
    }
    public void setChecked(String checked) {
        this.checked = checked;
    }
    
    
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    
    
    public String getOrgName() {
        return orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    
    
    public String getOrgClsName() {
        return orgClsName;
    }

    public void setOrgClsName(String orgClsName) {
        this.orgClsName = orgClsName;
    } 
}
