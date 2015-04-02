package com.cbrc.smis.other;
/**
 *机构信息查看
 *模块：设定金融机构的填报范围
 * @author 姚捷
 */
public class OrgDetail {
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 机构类型名称
     */
    private String orgClsName;
    /**
     * 复选框是否选中
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
