package com.cbrc.smis.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cbrc.auth.adapter.StrutsMPurBankLevelDelegate;
import com.cbrc.auth.adapter.StrutsMPurOrgDelegate;
import com.cbrc.auth.adapter.StrutsMUserToGrpDelegate;
import com.cbrc.auth.adapter.StrutsRoleToolDelegate;
import com.cbrc.auth.adapter.StrutsUserRoleDelegate;
import com.cbrc.auth.form.MPurBanklevelForm;
import com.cbrc.auth.form.MPurOrgForm;
import com.cbrc.auth.form.MUserToGrpForm;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.auth.form.UserRoleForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ParseBankLevelToOrg;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

/**
 * 用户登录，权限相关的操作
 * 
 * @author 姚捷
 */
public class OperatorHandler {
    /**
     * 已使用hibernate技术
     * 影响对象：OrgNet RoleTool MUserToGrp UserRole
     * 卞以刚		2011-12-21
     * @param operatorForm
     * @return
     */
    public Operator saveUserInfo(OperatorForm operatorForm)
    {
        Operator operator = new Operator();
        
        if(operatorForm.getSuperManager() != null) {
        	operator.setSuperManager(true);
        }
        /**用户id*/
        operator.setOperatorId(operatorForm.getUserId());
        /**用户名*/
        operator.setOperatorName((operatorForm.getFirstName()!=null?operatorForm.getFirstName():"") + 
        		(operatorForm.getLastName()!=null?operatorForm.getLastName().trim():""));
        /**部门名称*/
        operator.setDeptName(operatorForm.getDeptName());
        /**机构名称*/
        operator.setOrgName(operatorForm.getProductUserName());
        
        operator.setUserName(operatorForm.getUserName());
        
        if(operator.getOrgName() == null || operator.getOrgName().trim().equals("")){
        	/**已使用Hibernate 卞以刚 2011-12-27
        	 * 影响对象：OrgNet**/
        	OrgNet orgNet = StrutsOrgNetDelegate.selectOne(operatorForm.getOrgId());
        	if(orgNet != null) operator.setOrgName(orgNet.getOrgName());
        }
        	
        /**机构Id*/
        operator.setOrgId(operatorForm.getOrgId());
        /**菜单权限*/ 
        /**以使用hibernate 卞以刚2011-12-27
         * 影响对象：RoleTool**/
        operator.setMenuUrls(this.getUrlsByUserId(operatorForm.getUserId(),operator.isSuperManager()));
        /**用户组权限*/
        /**已使用hibernate 卞以刚 2011-12-21 
         * 影响对象：MUserToGrp**/
        String userGrpIds = this.getUserGrpIds(operatorForm.getUserId());
        /**角色权权限*/
        /**已使用hibernate 卞以刚 2011-12-21 
         * 影响对象：UserRole**/
        String roleIds = this.getRoleIds(operatorForm.getUserId());        
        /**用户组id串，之间用逗号隔开*/
        operator.setUserGrpIds(userGrpIds);
        /**角色id串，之间用逗号隔开*/
        operator.setRoleIds(roleIds);
        /**用户机构权限*/       
        /**已使用hibernate 卞以刚 2011-12-27
         * 影响对象：OrgNet**/
        operator.setSubOrgIds(StrutsOrgNetDelegate.selectAllLowerOrgIds(operatorForm.getOrgId()));          
       
        
        //2008-1-2曹发根修改
//        /**用户报表审核权限*/
//        operator.setChildRepCheckPodedom(this.getChildRepPodedom(operator.getUserGrpIds(),Config.POWERTYPECHECK,operator.getOrgId()));
//        /**用户报表查看权限*/
//        operator.setChildRepSearchPopedom(this.getChildRepPodedom(operator.getUserGrpIds(),Config.POWERTYPESEARCH,operator.getOrgId()));
//        /**用户报表报送权限*/
//        operator.setChildRepReportPopedom(this.getChildRepPodedom(operator.getUserGrpIds(),Config.POWERTYPEREPORT,operator.getOrgId()));
       
        /**用户报表审核权限*/
        operator.setChildRepCheckPodedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep where viewOrgRep.powType="
        		+Config.POWERTYPECHECK+" and viewOrgRep.userId="+operator.getOperatorId());
        
        /**用户报表查看权限*/
        operator.setChildRepSearchPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep where viewOrgRep.powType="
        		+Config.POWERTYPESEARCH+" and viewOrgRep.userId="+operator.getOperatorId());
        
        /**用户报表报送权限*/
        operator.setChildRepReportPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep where viewOrgRep.powType="
        		+Config.POWERTYPEREPORT+" and viewOrgRep.userId="+operator.getOperatorId());
        
        /**用户报表复核权限*/
        operator.setChildRepVerifyPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep where viewOrgRep.powType="
        		+Config.POWERTYPEVERIFY+" and viewOrgRep.userId="+operator.getOperatorId());

        /** 超级管理员权限无限制 */
        if(operator.isSuperManager()){
        	
            /**用户报表审核权限*/
            operator.setChildRepCheckPodedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep " +
            		" where viewOrgRep.powType=" + Config.POWERTYPECHECK);
            
            /**用户报表查看权限*/
            operator.setChildRepSearchPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep " +
            		" where viewOrgRep.powType=" + Config.POWERTYPESEARCH);
            
            /**用户报表报送权限*/
            operator.setChildRepReportPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep " +
            		" where viewOrgRep.powType=" + Config.POWERTYPEREPORT);
            
            /**用户报表复核权限*/
            operator.setChildRepVerifyPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep " +
            		" where viewOrgRep.powType=" + Config.POWERTYPEVERIFY);
        	
        }
        
        /**用户下属一级机构权限*/
        /** 已使用hibernate 卞以刚 2011-11-21
         * 影响对象：OrgNet**/
        operator.setChildOrgIds(StrutsOrgNetDelegate.selectLowerOrgIds(operator.getOrgId()));

        
        /////////////////////要改/////////////////////////////////
        
//        if(userGrpIds != null && !userGrpIds.equals("")){
//        	/**机构权限*/
//            operator.setOrgPopedomSQL(StrutsMPurViewDelegate.getOrgPopedomSQL(userGrpIds));
//            /**子报表权限*/
//            operator.setChildRepPodedomSQL(this.getChildRepPopedomSQL(userGrpIds));
//        }
        
        return operator;        
    }
    
    /**
     * 已使用hibernate 卞以刚 2011-12-21 
     * 影响对象：UserRole
     * 取得该用户角色id字串
     * @param userId 用户id
     * @return 该用户角色id字串（之间用逗号隔开）
     */
    private  String getRoleIds(Long userId)
    {
        String roleIds = "";
        
        /**已使用hibernate 卞以刚 2011-12-21 
         * 影响对象：UserRole**/
        List roles = StrutsUserRoleDelegate.getUserSetRole(userId);
        if(roles!=null && !roles.equals(""))
        {
            for(int i=0;i<roles.size();i++)
            {
                UserRoleForm role = (UserRoleForm)roles.get(i);
                if(i==roles.size()-1)
                    roleIds += role.getRoleId();
                else
                    roleIds += role.getRoleId()+Config.SPLIT_SYMBOL_COMMA;    
            }   
        }
        return roleIds;
    }
    
    /**
     * 已使用hibernate 卞以刚	2011-12-21
     * 影响对象：RoleTool
     * 取得该用户的菜单权限
     * @param userId
     * @return  List 该用户对应的菜单权限(菜单名称)
     */
    private List getUrlsByUserId(Long userId,boolean isSuperManager)
    {
        String roleIds = getRoleIds(userId);
        
        List menuUrls = null;
        
        if(roleIds!=null && !roleIds.equals(""))
        {
        	/**已使用hibernate 卞以刚 2011-12-21 
        	 * 影响对象：RoleTool**/
            menuUrls = StrutsRoleToolDelegate.getUrlByRoleIds(roleIds,isSuperManager);
        }
        return menuUrls;
    }
    
    /**
     * 已使用hibernate 卞以刚 2011-12-21 
     * 影响对象：MUserToGrp
     * 取得该用户的属于的用户组id字串
     * @param userId Long 用户id
      * @return  该用户的属于的用户组id字串
     */
    private String getUserGrpIds(Long userId)
    {
        String userGrpIds = "";
        /**已使用hibernate 卞以刚 2011-12-21 
         * 影响对象：MUserToGrp**/
        List list = StrutsMUserToGrpDelegate.getUserSetUserGrp(userId);
        if(list!=null && list.size()!=0)
        {
            for(int i=0;i<list.size();i++)
            {
                MUserToGrpForm userToGrp = (MUserToGrpForm)list.get(i);
                if(i==list.size()-1)
                    userGrpIds += userToGrp.getUserGrpId();
                else
                    userGrpIds += userToGrp.getUserGrpId()+Config.SPLIT_SYMBOL_COMMA;                    
            }
        }
        return userGrpIds;
    }
   
    /**
     * 取得该用户拥有的报表审核权限字串
     * @param userIds 用户所属的用户组id字串
     * @return String 该用户拥有的报表审核权限
     */
    private String getChildRepPodedom(String userGrpIds,Integer powType,String orgId){
    	String result = "";

    	
    	
    	Map map = new HashMap();
    	
    	/**取得行级与报表权限*/
    	List mPurBankLevelList = StrutsMPurBankLevelDelegate.getMPurBankLevelPopedom(userGrpIds,powType);
    	if(mPurBankLevelList != null && mPurBankLevelList.size() > 0){
    		for(int i=0;i<mPurBankLevelList.size();i++){
    			MPurBanklevelForm form = (MPurBanklevelForm)mPurBankLevelList.get(i);
    			if(form == null) continue;
    			    			
    			String orgIdsPopedom = ParseBankLevelToOrg.parseBankLevel(form.getBankLevelName(),orgId);
    			if(orgIdsPopedom == null || orgIdsPopedom.equals("")) continue;
    			
    			String orgIds[] = orgIdsPopedom.split(",");
    			if(orgIds == null || orgIds.length == 0) continue;
    			
    			for(int index=0;index<orgIds.length;index++){    				
    				orgIds[index] = orgIds[index].substring(orgIds[index].indexOf("'")+1);
    				orgIds[index] = orgIds[index].substring(0,orgIds[index].indexOf("'"));
    				/**机构拥有相同的权限则不再加入字串*/
    				if(!map.containsKey(orgIds[index]+form.getChildRepId())){
    					String temp = orgIds[index] + form.getChildRepId();
    					result = result.equals("") ? "'" + temp + "'" : result + ",'" + temp + "'";
    					map.put(temp,temp);
    				}    				
    			}
    		}
    	}
    	
    	/**取得实际机构与报表权限*/
    	List mPurOrgList = StrutsMPurOrgDelegate.getMPurOrgPopedom(userGrpIds,powType);
    	if(mPurOrgList != null && mPurOrgList.size() > 0){
    		for(int i=0;i<mPurOrgList.size();i++){
    			MPurOrgForm form = (MPurOrgForm)mPurOrgList.get(i);
    			if(form == null) continue;
    			
    			String temp = form.getOrgId() + form.getChildRepId();
    			/**机构拥有相同的权限则不再加入字串*/
    			if(!map.containsKey(temp)){
    				result = result.equals("") ? "'" + temp + "'" : result + ",'" + temp + "'";
					map.put(temp,temp);
    			}
    		}
    	}

    	System.out.println(result);
    	return result;
    }
}
