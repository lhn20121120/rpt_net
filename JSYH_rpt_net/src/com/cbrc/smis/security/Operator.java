package com.cbrc.smis.security;

import java.util.List;

import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.treexml.CreateOrgTreeByUser;
import com.fitech.gznx.treexml.CreateOrgTreeByUserHZRule;


/**
 * 系统操作用户类
 * 
 * @author rds
 * @serialData 2005-12-6
 */
public class Operator {
    /**
     * 操作员id
     */
    private Long operatorId = null;
    /**
     * 操作员姓名
     */
    private String operatorName = null;
    
    private String userName = null;
    
    /**
     * 机构名称
     */
    private String orgName = null;
    /**
     * 机构ID
     */
    private String orgId=null;
    /**
     * 部门名称
     */
    private String deptName = null;        
    /**
     * 报表审核权限
     */
    private String childRepCheckPodedom = null;
    /**
     * 报表查看权限
     */
    private String childRepSearchPopedom = null;
    /**
     * 报表报送权限
     */
    private String childRepReportPopedom = null;
    /**
     * 报表复核权限（add0911）
     */
    private String childRepVerifyPopedom = null;
    /**
     * 用户机构权限
     */
    private String subOrgIds = null;
    /**
     * 下属一级机构权限
     */
    private String childOrgIds = null;
    /**
     * 该用户功能菜单权限
     */
    private List menuUrls = null;    
    /**
     * 用户组id串
     */
    private String userGrpIds = null;    
    /**
     * 角色id串
     */
    private String roleIds = null;    
    /**
     * SSO未登录页面
     */
    private String noLoginUrl = null;    
    /**
     * 是否是超级用户
     */
    private boolean isSuperManager = false;  
    
    private String sessionId = null;
    
	/**
	 * 当前用户使用的ＩＰ
	 */
	private String ipAdd = "";
    /**
     * 取得功能菜单权限url
     * @return List
     */
    public List getMenuUrls() {
        return menuUrls;
    }

    public void setMenuUrls(List menuUrls) {
        this.menuUrls = menuUrls;
    }

	/**
	 * @return Returns the childRepCheckPodedom.
	 */
	public String getChildRepCheckPodedom() {
		return childRepCheckPodedom;
	}

	/**
	 * @param childRepCheckPodedom The childRepCheckPodedom to set.
	 */
	public void setChildRepCheckPodedom(String childRepCheckPodedom) {
		this.childRepCheckPodedom = childRepCheckPodedom;
	}

	/**
	 * @return Returns the childRepReportPopedom.
	 */
	public String getChildRepReportPopedom() {
		return childRepReportPopedom;
	}

	/**
	 * @param childRepReportPopedom The childRepReportPopedom to set.
	 */
	public void setChildRepReportPopedom(String childRepReportPopedom) {
		this.childRepReportPopedom = childRepReportPopedom;
	}

	/**
	 * @return Returns the childRepSearchPopedom.
	 */
	public String getChildRepSearchPopedom() {
		return childRepSearchPopedom;
	}

	/**
	 * @param childRepSearchPopedom The childRepSearchPopedom to set.
	 */
	public void setChildRepSearchPopedom(String childRepSearchPopedom) {
		this.childRepSearchPopedom = childRepSearchPopedom;
	}

	/**
     * 所属部门名称
     * @return String
     */
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    /**
     * 操作员姓名
     * @return String
     */
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    /**
     * 所属机构名称
     * @return String
     */
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }    
    /**
     * 判断该url在url集合中是否存在
     * @param url String url     
     * @param url
     * @return 存在则返回true
     */
    public boolean isExitsThisUrl(String url)
    {
        if(menuUrls!=null && menuUrls.size()!=0)
            return this.menuUrls.contains(url);
        else
            return false;
    }
    /**
     * 用户组id串
     * @return
     */
    public String getUserGrpIds() {
        return userGrpIds;
    }

    public void setUserGrpIds(String userGrpIds) {
        this.userGrpIds = userGrpIds;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getNoLoginUrl() {
		return noLoginUrl;
	}
	public void setNoLoginUrl(String noLoginUrl) {
		this.noLoginUrl = noLoginUrl;
	}

	public boolean isSuperManager() {
		return isSuperManager;
	}
	public void setSuperManager(boolean isSuperManager) {
		this.isSuperManager = isSuperManager;
	}	
	
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public String getSubOrgIds() {
		return subOrgIds;
	}
	public void setSubOrgIds(String subOrgIds) {
		this.subOrgIds = subOrgIds;
	}	
	public String getChildOrgIds() {
		return childOrgIds;
	}

	public void setChildOrgIds(String childOrgIds) {
		this.childOrgIds = childOrgIds;
	}
	/**
     * 机构权限sql
     */
    private String orgPopedomSQL = null;

    /**
     * 报表权限sql
     */
    private String childRepPodedomSQL = null;

	public String getChildRepPodedomSQL() {
		return childRepPodedomSQL;
	}

	public void setChildRepPodedomSQL(String childRepPodedomSQL) {
		this.childRepPodedomSQL = childRepPodedomSQL;
	}

	public String getOrgPopedomSQL() {
		return orgPopedomSQL;
	}

	public void setOrgPopedomSQL(String orgPopedomSQL) {
		this.orgPopedomSQL = orgPopedomSQL;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getIpAdd() {
		return ipAdd;
	}

	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getChildRepVerifyPopedom() {
		return childRepVerifyPopedom;
	}

	public void setChildRepVerifyPopedom(String childRepVerifyPopedom) {
		this.childRepVerifyPopedom = childRepVerifyPopedom;
	}
	
	/**
	 * 该用户机构权限List
	 */
	private List orgPurviewList = null;
	
	public List getOrgPurviewList() {
		return orgPurviewList;
	}

	public void setOrgPurviewList(List orgPurviewList) {
		this.orgPurviewList = orgPurviewList;
	}

	/**
	 * 
	 * title:该方法用于刷新用户系统查询权限机构树(应用查询用) author:chenbing date:2008-3-12
	 * 
	 * @return
	 */
	public void reFreshOrgTree() {
		try {
			if (this.isSuperManager)
				this.setOrgPurviewList(AFOrgDelegate.getAllOrgList());
			else
				/**已使用hibernate  卞以刚 2011-12-21**/
				this.setOrgPurviewList(AFOrgDelegate
						.getUserOrgPurview(this.getUserName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		CreateOrgTreeByUser.createByUser(this.getUserName(), this
				.getOrgPurviewList(), "report_view", true);
		CreateOrgTreeByUser.createByUser(this.getUserName(), this
				.getOrgPurviewList(), "user_with_org", false);
		// return Config.ORG_TREEXML_WEB_PATH + orgTree;
	}
	/**
	 * 
	 * title:该方法用于刷新用户系统查询权限机构树(应用查询用) author:chenbing date:2008-3-12
	 * 
	 * @return
	 */
	public void reFreshOrgTreeHZRule() {
		try {
			if (this.isSuperManager)
				this.setOrgPurviewList(AFOrgDelegate.getAllOrgList());
			else
				/**已使用hibernate  卞以刚 2011-12-21**/
				this.setOrgPurviewList(AFOrgDelegate
						.getUserOrgPurview(this.getUserName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		CreateOrgTreeByUserHZRule.createByUser(this.getUserName(), this
				.getOrgPurviewList(), "report_view", true);
		CreateOrgTreeByUserHZRule.createByUser(this.getUserName(), this
				.getOrgPurviewList(), "user_with_org", false);
		// return Config.ORG_TREEXML_WEB_PATH + orgTree;
	}
}
