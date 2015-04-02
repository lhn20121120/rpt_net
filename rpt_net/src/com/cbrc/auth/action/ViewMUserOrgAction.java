//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

package com.cbrc.auth.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.adapter.StrutsMBankLevelDelegate;
import com.cbrc.auth.adapter.StrutsMPurBankLevelDelegate;
import com.cbrc.auth.form.MBankLevelForm;
import com.cbrc.auth.form.MPurBanklevelForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ParseBankLevelToOrg;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

public class ViewMUserOrgAction extends Action {
    private static FitechException log = new FitechException(ViewMUserOrgAction.class); 
    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
    	
        MPurBanklevelForm mPurBanklevelForm = (MPurBanklevelForm) form;
        RequestUtils.populate(mPurBanklevelForm,request);
                
        /**用户拥有的机构权限*/
        List lowerOrgNetList = null;
        /**用户已经选中的机构权限信息*/
        List userSelectOrg = null;
        /**用户已经选中的行级定义*/
        List userGrpBankLevelPopedom = null;
        /**用户拥有的行级定义*/
        List userGrpBankLevel = null;
        try {
        	
        	HttpSession session = request.getSession();
        	Operator operator = null; 
            if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
                operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
            
        
            /**查询该用户组已经有的行级定义*/ 
            String selectBankLevelIds = mPurBanklevelForm.getSelectBankLevelIds();
            String selectBankLevelNames = mPurBanklevelForm.getSelectBankLevelNames();
            
            if(selectBankLevelIds!=null && !selectBankLevelIds.equals("") && selectBankLevelNames!=null && !selectBankLevelNames.equals("")){
            	String[] bankLevelIds = selectBankLevelIds.split(Config.SPLIT_SYMBOL_COMMA);
            	String[] bankLevelNames = selectBankLevelNames.split(Config.SPLIT_SYMBOL_COMMA);
            	
            	userGrpBankLevelPopedom = new ArrayList();
            	for(int i=0;i<bankLevelIds.length;i++)
            		userGrpBankLevelPopedom.add(new LabelValueBean(bankLevelNames[i],bankLevelIds[i]));
            }            
                      
            /**查询可以为该用户组分配的行级权限*/           
            List bankLevelList = null;           
            if(operator.isSuperManager() == false){ /**是否是超级用户*/
            	bankLevelList = StrutsMPurBankLevelDelegate.getUserGrpBankLevelPopedom(operator.getUserGrpIds(),Config.POWERTYPECHECK);                     
            }else{                   
            	bankLevelList = StrutsMBankLevelDelegate.select();           
            }
           
            if(bankLevelList != null && bankLevelList.size() > 0){           	
            	userGrpBankLevel = new ArrayList();
            	for(int i=0;i<bankLevelList.size();i++){
            		MBankLevelForm mBankLevelForm = (MBankLevelForm)bankLevelList.get(i);                                          		
            		if(mBankLevelForm!=null){                          		
            			userGrpBankLevel.add(new LabelValueBean(mBankLevelForm.getBankLevelName(),mBankLevelForm.getBankLevelId().toString()));
            		}
            	}
            } 
            
            String orgIdStrs = "";
            /**查询该用户组可以拥有的权限信息*/
            if(bankLevelList != null && bankLevelList.size() >0){   
            	String allOrgIds = "";            	
            	for(int i=0;i<bankLevelList.size();i++){
            		MBankLevelForm mBankLevelForm = (MBankLevelForm)bankLevelList.get(i);
            		String orgIds = ParseBankLevelToOrg.parseBankLevel(mBankLevelForm.getBankLevelName(),operator.getOrgId());
            		allOrgIds = allOrgIds.equals("") ? orgIds : allOrgIds + "," + orgIds;
            	}
            	
            	if(!allOrgIds.equals("")){
                	Map map = new HashMap();
                	String[] strs = allOrgIds.split(",");
                	if(strs != null && strs.length > 0){
                		for(int i=0;i<strs.length;i++){
                			if(map.containsKey(strs[i]))
                				continue;
                			orgIdStrs = orgIdStrs.equals("") ? strs[i] : orgIdStrs + "," + strs[i]; 
                		}
                	}
                }
            }
            
            List orgNetList = null;
            if(!orgIdStrs.equals("")){
            	orgNetList = StrutsOrgNetDelegate.selectOrgByIds(orgIdStrs);
            	if(orgNetList != null && orgNetList.size() > 0){
                	lowerOrgNetList = new ArrayList();
                	for(int i=0;i<orgNetList.size();i++){
                		OrgNet orgNet = (OrgNet)orgNetList.get(i);
                		if(orgNet != null)
                			lowerOrgNetList.add(new LabelValueBean(orgNet.getOrgName(),orgNet.getOrgId()));
                	}
                }
            }
            
            /**取出用户已经选中的机构权限信息*/           
            String selectOrgIds = mPurBanklevelForm.getSelectOrgIds();           
            String selectOrgNames = mPurBanklevelForm.getSelectOrgNames();           
            if(selectOrgIds!=null && !selectOrgIds.equals("") && selectOrgNames!=null && !selectOrgNames.equals("")){            
            	String[] orgIds = selectOrgIds.split(Config.SPLIT_SYMBOL_COMMA);               
            	String[] orgNames = selectOrgNames.split(Config.SPLIT_SYMBOL_COMMA);

            	userSelectOrg = new ArrayList();               
            	for(int i=0;i<orgIds.length ;i++)            	
            		userSelectOrg.add(new LabelValueBean(orgNames[i],orgIds[i]));           
            }
                      
            
        }catch (Exception e){
            log.printStackTrace(e);
        }
        /**属于该机构分类类别的机构集合*/
        if(lowerOrgNetList!=null && lowerOrgNetList.size()!=0)             
        	request.setAttribute("LowerOrgNetList",lowerOrgNetList);
        
        if(userSelectOrg!=null && userSelectOrg.size()!=0)
            request.setAttribute("UserGrpOrgPopedom",userSelectOrg);
        /**用户组已经有的行级定义*/
        if(userGrpBankLevelPopedom != null && userGrpBankLevelPopedom.size() > 0)
        	request.setAttribute("UserGrpBankLevelPopedom",userGrpBankLevelPopedom);
        /**用户组允许拥有的行级定义*/
        if(userGrpBankLevel != null && userGrpBankLevel.size() > 0)
        	request.setAttribute("UserGrpBankLevel",userGrpBankLevel);
        
        /**用户组id*/
        if(mPurBanklevelForm.getUserGrpId()!=null)
            request.setAttribute("UserGrpId",mPurBanklevelForm.getUserGrpId());
        /**用户组名称*/
        if(mPurBanklevelForm.getUserGrpNm()!=null)
            request.setAttribute("UserGrpNm",mPurBanklevelForm.getUserGrpNm());        
        /**权限类型*/
        if(mPurBanklevelForm.getPowType()!=null)
        	request.setAttribute("powType",mPurBanklevelForm.getPowType());
        
        return mapping.findForward("user_group_org_popedom");
    }
}

