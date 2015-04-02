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
 * �û���¼��Ȩ����صĲ���
 * 
 * @author Ҧ��
 */
public class OperatorHandler {
    /**
     * ��ʹ��hibernate����
     * Ӱ�����OrgNet RoleTool MUserToGrp UserRole
     * ���Ը�		2011-12-21
     * @param operatorForm
     * @return
     */
    public Operator saveUserInfo(OperatorForm operatorForm)
    {
        Operator operator = new Operator();
        
        if(operatorForm.getSuperManager() != null) {
        	operator.setSuperManager(true);
        }
        /**�û�id*/
        operator.setOperatorId(operatorForm.getUserId());
        /**�û���*/
        operator.setOperatorName((operatorForm.getFirstName()!=null?operatorForm.getFirstName():"") + 
        		(operatorForm.getLastName()!=null?operatorForm.getLastName().trim():""));
        /**��������*/
        operator.setDeptName(operatorForm.getDeptName());
        /**��������*/
        operator.setOrgName(operatorForm.getProductUserName());
        
        operator.setUserName(operatorForm.getUserName());
        
        if(operator.getOrgName() == null || operator.getOrgName().trim().equals("")){
        	/**��ʹ��Hibernate ���Ը� 2011-12-27
        	 * Ӱ�����OrgNet**/
        	OrgNet orgNet = StrutsOrgNetDelegate.selectOne(operatorForm.getOrgId());
        	if(orgNet != null) operator.setOrgName(orgNet.getOrgName());
        }
        	
        /**����Id*/
        operator.setOrgId(operatorForm.getOrgId());
        /**�˵�Ȩ��*/ 
        /**��ʹ��hibernate ���Ը�2011-12-27
         * Ӱ�����RoleTool**/
        operator.setMenuUrls(this.getUrlsByUserId(operatorForm.getUserId(),operator.isSuperManager()));
        /**�û���Ȩ��*/
        /**��ʹ��hibernate ���Ը� 2011-12-21 
         * Ӱ�����MUserToGrp**/
        String userGrpIds = this.getUserGrpIds(operatorForm.getUserId());
        /**��ɫȨȨ��*/
        /**��ʹ��hibernate ���Ը� 2011-12-21 
         * Ӱ�����UserRole**/
        String roleIds = this.getRoleIds(operatorForm.getUserId());        
        /**�û���id����֮���ö��Ÿ���*/
        operator.setUserGrpIds(userGrpIds);
        /**��ɫid����֮���ö��Ÿ���*/
        operator.setRoleIds(roleIds);
        /**�û�����Ȩ��*/       
        /**��ʹ��hibernate ���Ը� 2011-12-27
         * Ӱ�����OrgNet**/
        operator.setSubOrgIds(StrutsOrgNetDelegate.selectAllLowerOrgIds(operatorForm.getOrgId()));          
       
        
        //2008-1-2�ܷ����޸�
//        /**�û��������Ȩ��*/
//        operator.setChildRepCheckPodedom(this.getChildRepPodedom(operator.getUserGrpIds(),Config.POWERTYPECHECK,operator.getOrgId()));
//        /**�û�����鿴Ȩ��*/
//        operator.setChildRepSearchPopedom(this.getChildRepPodedom(operator.getUserGrpIds(),Config.POWERTYPESEARCH,operator.getOrgId()));
//        /**�û�������Ȩ��*/
//        operator.setChildRepReportPopedom(this.getChildRepPodedom(operator.getUserGrpIds(),Config.POWERTYPEREPORT,operator.getOrgId()));
       
        /**�û��������Ȩ��*/
        operator.setChildRepCheckPodedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep where viewOrgRep.powType="
        		+Config.POWERTYPECHECK+" and viewOrgRep.userId="+operator.getOperatorId());
        
        /**�û�����鿴Ȩ��*/
        operator.setChildRepSearchPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep where viewOrgRep.powType="
        		+Config.POWERTYPESEARCH+" and viewOrgRep.userId="+operator.getOperatorId());
        
        /**�û�������Ȩ��*/
        operator.setChildRepReportPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep where viewOrgRep.powType="
        		+Config.POWERTYPEREPORT+" and viewOrgRep.userId="+operator.getOperatorId());
        
        /**�û�������Ȩ��*/
        operator.setChildRepVerifyPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep where viewOrgRep.powType="
        		+Config.POWERTYPEVERIFY+" and viewOrgRep.userId="+operator.getOperatorId());

        /** ��������ԱȨ�������� */
        if(operator.isSuperManager()){
        	
            /**�û��������Ȩ��*/
            operator.setChildRepCheckPodedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep " +
            		" where viewOrgRep.powType=" + Config.POWERTYPECHECK);
            
            /**�û�����鿴Ȩ��*/
            operator.setChildRepSearchPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep " +
            		" where viewOrgRep.powType=" + Config.POWERTYPESEARCH);
            
            /**�û�������Ȩ��*/
            operator.setChildRepReportPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep " +
            		" where viewOrgRep.powType=" + Config.POWERTYPEREPORT);
            
            /**�û�������Ȩ��*/
            operator.setChildRepVerifyPopedom("select viewOrgRep.orgRepId from ViewOrgRep viewOrgRep " +
            		" where viewOrgRep.powType=" + Config.POWERTYPEVERIFY);
        	
        }
        
        /**�û�����һ������Ȩ��*/
        /** ��ʹ��hibernate ���Ը� 2011-11-21
         * Ӱ�����OrgNet**/
        operator.setChildOrgIds(StrutsOrgNetDelegate.selectLowerOrgIds(operator.getOrgId()));

        
        /////////////////////Ҫ��/////////////////////////////////
        
//        if(userGrpIds != null && !userGrpIds.equals("")){
//        	/**����Ȩ��*/
//            operator.setOrgPopedomSQL(StrutsMPurViewDelegate.getOrgPopedomSQL(userGrpIds));
//            /**�ӱ���Ȩ��*/
//            operator.setChildRepPodedomSQL(this.getChildRepPopedomSQL(userGrpIds));
//        }
        
        return operator;        
    }
    
    /**
     * ��ʹ��hibernate ���Ը� 2011-12-21 
     * Ӱ�����UserRole
     * ȡ�ø��û���ɫid�ִ�
     * @param userId �û�id
     * @return ���û���ɫid�ִ���֮���ö��Ÿ�����
     */
    private  String getRoleIds(Long userId)
    {
        String roleIds = "";
        
        /**��ʹ��hibernate ���Ը� 2011-12-21 
         * Ӱ�����UserRole**/
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
     * ��ʹ��hibernate ���Ը�	2011-12-21
     * Ӱ�����RoleTool
     * ȡ�ø��û��Ĳ˵�Ȩ��
     * @param userId
     * @return  List ���û���Ӧ�Ĳ˵�Ȩ��(�˵�����)
     */
    private List getUrlsByUserId(Long userId,boolean isSuperManager)
    {
        String roleIds = getRoleIds(userId);
        
        List menuUrls = null;
        
        if(roleIds!=null && !roleIds.equals(""))
        {
        	/**��ʹ��hibernate ���Ը� 2011-12-21 
        	 * Ӱ�����RoleTool**/
            menuUrls = StrutsRoleToolDelegate.getUrlByRoleIds(roleIds,isSuperManager);
        }
        return menuUrls;
    }
    
    /**
     * ��ʹ��hibernate ���Ը� 2011-12-21 
     * Ӱ�����MUserToGrp
     * ȡ�ø��û������ڵ��û���id�ִ�
     * @param userId Long �û�id
      * @return  ���û������ڵ��û���id�ִ�
     */
    private String getUserGrpIds(Long userId)
    {
        String userGrpIds = "";
        /**��ʹ��hibernate ���Ը� 2011-12-21 
         * Ӱ�����MUserToGrp**/
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
     * ȡ�ø��û�ӵ�еı������Ȩ���ִ�
     * @param userIds �û��������û���id�ִ�
     * @return String ���û�ӵ�еı������Ȩ��
     */
    private String getChildRepPodedom(String userGrpIds,Integer powType,String orgId){
    	String result = "";

    	
    	
    	Map map = new HashMap();
    	
    	/**ȡ���м��뱨��Ȩ��*/
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
    				/**����ӵ����ͬ��Ȩ�����ټ����ִ�*/
    				if(!map.containsKey(orgIds[index]+form.getChildRepId())){
    					String temp = orgIds[index] + form.getChildRepId();
    					result = result.equals("") ? "'" + temp + "'" : result + ",'" + temp + "'";
    					map.put(temp,temp);
    				}    				
    			}
    		}
    	}
    	
    	/**ȡ��ʵ�ʻ����뱨��Ȩ��*/
    	List mPurOrgList = StrutsMPurOrgDelegate.getMPurOrgPopedom(userGrpIds,powType);
    	if(mPurOrgList != null && mPurOrgList.size() > 0){
    		for(int i=0;i<mPurOrgList.size();i++){
    			MPurOrgForm form = (MPurOrgForm)mPurOrgList.get(i);
    			if(form == null) continue;
    			
    			String temp = form.getOrgId() + form.getChildRepId();
    			/**����ӵ����ͬ��Ȩ�����ټ����ִ�*/
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
