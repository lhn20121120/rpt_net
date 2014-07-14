//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

package com.cbrc.auth.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.adapter.StrutsMBankLevelDelegate;
import com.cbrc.auth.adapter.StrutsMPurBankLevelDelegate;
import com.cbrc.auth.adapter.StrutsMPurOrgDelegate;
import com.cbrc.auth.form.MBankLevelForm;
import com.cbrc.auth.form.MUserGrpForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ParseBankLevelToOrg;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;

/** 
 * 用户组机构权限设置
 * @author 姚捷
 * 
 * @struts.action path="/viewUserGrpOrgPopedom" name="mUserGrpForm" scope="request" validate="true"
 * @struts.action-forward name="user_group_org_popedom" path="/popedom_mgr/user_group_mgr/user_group_org_popedom.jsp"
 */
public class ViewUserGrpOrgPopedomAction extends Action {

    private static FitechException log = new FitechException(ViewUserGrpOrgPopedomAction.class); 
    /** 
     * 已使用hibernate 卞以刚 2011-12-28
     * 影响对象：MPurBanklevel  AfOrg MPurOrg
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
        HttpServletResponse response)  throws IOException, ServletException
    {

        MessageResources resources=getResources(request);
        FitechMessages messages = new FitechMessages();
        Locale locale = request.getLocale();
        
        MUserGrpForm mUserGrpForm = (MUserGrpForm) form;        
        RequestUtils.populate(mUserGrpForm,request);
    
        /**该用户组已经选择的实际机构权限*/
        List userGrpOrgPopedom = null;     
        /**该用户的监管的实际下级机构**/
        List lowerOrgNetList = null;
        
        /**该用户组已经选择的行级定义*/
        List userGrpBankLevelPopedom = null;
        /**该用户拥有的行级定义权限*/
        List userGrpBankLevel = null;
        
        try{        	
        	HttpSession session = request.getSession();
        	Operator operator = null; 
            if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
                operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);            
           
            /**查询该用户组已经有的行级定义*/
            /** 已使用hibernate 卞以刚 2011-12-28
             * 影响对象：MPurBanklevel*/
            List bankLevelPopedomList = StrutsMPurBankLevelDelegate.getUserGrpBankLevelPopedom(mUserGrpForm.getUserGrpId(),mUserGrpForm.getPowType());
            
            if (bankLevelPopedomList != null && bankLevelPopedomList.size() > 0){
            	userGrpBankLevelPopedom =  new ArrayList();
                for(int i=0; i<bankLevelPopedomList.size(); i++){
                	MBankLevelForm mBankLevelForm = (MBankLevelForm)bankLevelPopedomList.get(i);                    
                    if(mBankLevelForm!=null){
                    	userGrpBankLevelPopedom.add(new LabelValueBean(mBankLevelForm.getBankLevelName(),mBankLevelForm.getBankLevelId().toString()));
                    }
                }
            }
            
            /**查询可以为该用户组分配的行级权限*/
            List bankLevelList = null;
            if(operator.isSuperManager() == false){ /**是否是超级用户*/
            	/**已使用hibernate 卞以刚 2011-12-28
            	 * 影响对象：MPurBanklevel*/
            	bankLevelList = StrutsMPurBankLevelDelegate.getUserGrpBankLevelPopedom(operator.getUserGrpIds(),mUserGrpForm.getPowType());
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
            
            /**查询该用户组已经有的权限信息*/
//            List list = StrutsMPurOrgDelegate.getUserGrpOrgPopedom(mUserGrpForm.getUserGrpId(),mUserGrpForm.getPowType());
            /**已使用hibernate 卞以刚 2011-12-21
             * 影响对象：AfOrg MPurOrg*/
            List list = AFOrgDelegate.getUserGrpOrgPopedom(mUserGrpForm.getUserGrpId(),mUserGrpForm.getPowType());

            if (list!=null && list.size()!=0){
                userGrpOrgPopedom =  new ArrayList();                
                for (int i=0; i<list.size(); i++){
                	OrgNetForm orgNetForm = (OrgNetForm)list.get(i);                    
                    if(orgNetForm!=null){
                    	userGrpOrgPopedom.add(new LabelValueBean(orgNetForm.getOrg_id()+"-"+orgNetForm.getOrg_name(),orgNetForm.getOrg_id()));
                    }
                }
            }
               
            String orgIdStrs = "";
            /**查询该用户组可以拥有的权限信息*/
            if(bankLevelList != null && bankLevelList.size() >0){   
            	String allOrgIds = "";            	
            	for(int i=0;i<bankLevelList.size();i++){
            		MBankLevelForm mBankLevelForm = (MBankLevelForm)bankLevelList.get(i);
            		//修改从aforg取机构表
            		/**已使用hibernate 卞以刚 2011-12-28
            		 * 影响对象：AfOrg*/
            		String orgIds = ParseBankLevelToOrg.parseBankLevelNew(mBankLevelForm.getBankLevelName(),operator.getOrgId());
            		
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
            	/**已使用hibernate 卞以刚 2011-12-21
            	 * 影响对象：AfOrg*/
            	orgNetList = AFOrgDelegate.selectOrgByIds(orgIdStrs);
            	if(orgNetList != null && orgNetList.size() > 0){
                	lowerOrgNetList = new ArrayList();
                	for(int i=0;i<orgNetList.size();i++){
                		AfOrg orgNet = (AfOrg)orgNetList.get(i);
                		if(orgNet != null)
                			lowerOrgNetList.add(new LabelValueBean(orgNet.getOrgId()+"-"+orgNet.getOrgName(),orgNet.getOrgId()));
                	}
                }
            }
                             
        }catch (Exception e){
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"select.fail","userGrp.orgPopedom.info"));        
        }
        /**用户组编号*/
        if(mUserGrpForm.getUserGrpId() != null)
            request.setAttribute("UserGrpId",mUserGrpForm.getUserGrpId());
        /**用户组名称*/
        if(mUserGrpForm.getUserGrpNm() != null){  
        	if(com.cbrc.smis.common.Config.WEB_SERVER_TYPE == 1 && mUserGrpForm.getUserGrpNm() != null){
        		request.setAttribute("UserGrpNm",new String(mUserGrpForm.getUserGrpNm().getBytes("iso-8859-1"),"gb2312"));
        	}else
        	request.setAttribute("UserGrpNm",mUserGrpForm.getUserGrpNm());
        }
        /**权限类型*/
        if(mUserGrpForm.getPowType()!=null)
        	request.setAttribute("powType",mUserGrpForm.getPowType());
              
        /**用户组已经有的机构权限*/
        if(userGrpOrgPopedom!=null && userGrpOrgPopedom.size()!=0)
            request.setAttribute("UserGrpOrgPopedom",userGrpOrgPopedom);
        if(lowerOrgNetList != null && lowerOrgNetList.size() != 0)
        	request.setAttribute("LowerOrgNetList",lowerOrgNetList);
        /**用户组已经有的行级定义*/
        if(userGrpBankLevelPopedom != null && userGrpBankLevelPopedom.size() > 0)
        	request.setAttribute("UserGrpBankLevelPopedom",userGrpBankLevelPopedom);
        /**用户组允许拥有的行级定义*/
        if(userGrpBankLevel != null && userGrpBankLevel.size() > 0)
        	request.setAttribute("UserGrpBankLevel",userGrpBankLevel);
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES,messages);
        if(request.getParameter("curPage")!=null){
        	request.setAttribute("curPage",request.getParameter("curPage"));
        }
        return mapping.findForward("user_group_org_popedom");
    }
}

