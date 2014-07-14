package com.cbrc.smis.security;

import java.util.List;

import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.treexml.CreateOrgTreeByUser;
import com.fitech.gznx.treexml.CreateOrgTreeByUserHZRule;


/**
 * ϵͳ�����û���
 * 
 * @author rds
 * @serialData 2005-12-6
 */
public class Operator {
    /**
     * ����Աid
     */
    private Long operatorId = null;
    /**
     * ����Ա����
     */
    private String operatorName = null;
    
    private String userName = null;
    
    /**
     * ��������
     */
    private String orgName = null;
    /**
     * ����ID
     */
    private String orgId=null;
    /**
     * ��������
     */
    private String deptName = null;        
    /**
     * �������Ȩ��
     */
    private String childRepCheckPodedom = null;
    /**
     * ����鿴Ȩ��
     */
    private String childRepSearchPopedom = null;
    /**
     * ������Ȩ��
     */
    private String childRepReportPopedom = null;
    /**
     * ������Ȩ�ޣ�add0911��
     */
    private String childRepVerifyPopedom = null;
    /**
     * �û�����Ȩ��
     */
    private String subOrgIds = null;
    /**
     * ����һ������Ȩ��
     */
    private String childOrgIds = null;
    /**
     * ���û����ܲ˵�Ȩ��
     */
    private List menuUrls = null;    
    /**
     * �û���id��
     */
    private String userGrpIds = null;    
    /**
     * ��ɫid��
     */
    private String roleIds = null;    
    /**
     * SSOδ��¼ҳ��
     */
    private String noLoginUrl = null;    
    /**
     * �Ƿ��ǳ����û�
     */
    private boolean isSuperManager = false;  
    
    private String sessionId = null;
    
	/**
	 * ��ǰ�û�ʹ�õģɣ�
	 */
	private String ipAdd = "";
    /**
     * ȡ�ù��ܲ˵�Ȩ��url
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
     * ������������
     * @return String
     */
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    /**
     * ����Ա����
     * @return String
     */
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    /**
     * ������������
     * @return String
     */
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }    
    /**
     * �жϸ�url��url�������Ƿ����
     * @param url String url     
     * @param url
     * @return �����򷵻�true
     */
    public boolean isExitsThisUrl(String url)
    {
        if(menuUrls!=null && menuUrls.size()!=0)
            return this.menuUrls.contains(url);
        else
            return false;
    }
    /**
     * �û���id��
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
     * ����Ȩ��sql
     */
    private String orgPopedomSQL = null;

    /**
     * ����Ȩ��sql
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
	 * ���û�����Ȩ��List
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
	 * title:�÷�������ˢ���û�ϵͳ��ѯȨ�޻�����(Ӧ�ò�ѯ��) author:chenbing date:2008-3-12
	 * 
	 * @return
	 */
	public void reFreshOrgTree() {
		try {
			if (this.isSuperManager)
				this.setOrgPurviewList(AFOrgDelegate.getAllOrgList());
			else
				/**��ʹ��hibernate  ���Ը� 2011-12-21**/
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
	 * title:�÷�������ˢ���û�ϵͳ��ѯȨ�޻�����(Ӧ�ò�ѯ��) author:chenbing date:2008-3-12
	 * 
	 * @return
	 */
	public void reFreshOrgTreeHZRule() {
		try {
			if (this.isSuperManager)
				this.setOrgPurviewList(AFOrgDelegate.getAllOrgList());
			else
				/**��ʹ��hibernate  ���Ը� 2011-12-21**/
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
