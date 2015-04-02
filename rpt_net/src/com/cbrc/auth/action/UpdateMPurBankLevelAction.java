package com.cbrc.auth.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.adapter.StrutsMPurBankLevelDelegate;
import com.cbrc.auth.adapter.StrutsMPurOrgDelegate;
import com.cbrc.auth.form.MPurBanklevelForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

public final class UpdateMPurBankLevelAction extends Action {
    private static FitechException log = new FitechException(InsertMPurViewAction.class); 
    /**
     * Performs action.
     * @param mapping Action mapping.
     * @param form Action form.
     * @param request HTTP request.
     * @param response HTTP response.
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward execute(
       ActionMapping mapping,
       ActionForm form,
       HttpServletRequest request,
       HttpServletResponse response)throws IOException, ServletException {    	
        
    	MessageResources resources=getResources(request);
        FitechMessages messages = new FitechMessages();
        Locale locale = request.getLocale();
        
        MPurBanklevelForm mPurBanklevelForm = (MPurBanklevelForm) form;
        RequestUtils.populate(mPurBanklevelForm,request);
        
        Long userGrpId = mPurBanklevelForm.getUserGrpId();
        String userGrpNm = mPurBanklevelForm.getUserGrpNm();
        Integer powType = mPurBanklevelForm.getPowType();
        String selectOrgIds = mPurBanklevelForm.getSelectOrgIds();
        String selectOrgNames  = mPurBanklevelForm.getSelectOrgNames();
        String selectRepIds = mPurBanklevelForm.getSelectRepIds();
        String selectBankLevelIds = mPurBanklevelForm.getSelectBankLevelIds();
        String selectBankLevelNames = mPurBanklevelForm.getSelectBankLevelNames();
              
        
        String page="";
		if(mPurBanklevelForm.getCurPage()!=null)
			page=page+"?curPage="+mPurBanklevelForm.getCurPage();

        
        
        /**用户组编号*/
        if(userGrpId!=null)
            request.setAttribute("UserGrpId",userGrpId);
        
        /**用户组名称*/
        if(userGrpNm!=null)
            request.setAttribute("UserGrpNm",userGrpNm);
                
        /**权限类型*/
        if(powType!=null)
        	request.setAttribute("powType",powType);
        
        /**选中的机构id字串*/
        if(selectOrgIds!=null && !selectOrgIds.equals(""))
            request.setAttribute("selectOrgIds",selectOrgIds);
        
        /**选中的机构名称字串*/
        if(selectOrgNames!=null && !selectOrgNames.equals(""))
            request.setAttribute("selectOrgNames",selectOrgNames);
        
        /**选中的行级ID*/
        if(selectBankLevelIds!=null && !selectBankLevelIds.equals(""))
        	request.setAttribute("selectBankLevelIds",selectBankLevelIds);
        
        /**选中的行级名称*/
        if(selectBankLevelNames!=null && !selectBankLevelNames.equals(""))
        	request.setAttribute("selectBankLevelNames",selectBankLevelNames);
                
        try {   
        	/**删除该用户组行级定义权限*/
        	if(StrutsMPurBankLevelDelegate.getUserGrpBankLevelPopedomCount(userGrpId,powType) > 0){
        		boolean deleteBankLevel = StrutsMPurBankLevelDelegate.deleteUserGrpPopedom(userGrpId,powType);
        		
        		if(deleteBankLevel == false){
        			messages.add(FitechResource.getMessage(locale,resources,"save.failed","userGrp.popedom.info"));
                    request.setAttribute(Config.MESSAGES,messages);
                    return new ActionForward("/popedom_mgr/viewUserGrpRepPopedom.do"+page); 
        		}        			
        	}       
        	
        	/**删除该用户组实际机构权限*/
        	if(StrutsMPurOrgDelegate.getUserGrpOrgPopedomCount(userGrpId,powType) > 0){
    			boolean deleteMPurOrg = StrutsMPurOrgDelegate.deleteUserGrpOrgPopedom(userGrpId,powType);
    			
        		if(deleteMPurOrg == false){
        			messages.add(FitechResource.getMessage(locale,resources,"save.failed","userGrp.popedom.info"));
                    request.setAttribute(Config.MESSAGES,messages);
                    return new ActionForward("/popedom_mgr/viewUserGrpRepPopedom.do"+page); 
        		}
    		}        
            
            /**设置该用户组的行级权限*/     
        	if(selectBankLevelIds != null && !selectBankLevelIds.equals("")
        			&& selectRepIds != null && !selectRepIds.equals("")){
        		boolean saveBankLevel = StrutsMPurBankLevelDelegate.insertUserGrpPopedom(userGrpId,selectBankLevelIds,selectRepIds,powType);
            	
            	if(saveBankLevel == false){
            		messages.add(FitechResource.getMessage(locale,resources,"save.failed","userGrp.popedom.info"));
                    request.setAttribute(Config.MESSAGES,messages);
                    return new ActionForward("/popedom_mgr/viewUserGrpRepPopedom.do"+page); 
            	}
        	}
        	/**设置该用户组的实际机构权限*/
        	if(selectOrgIds != null && !selectOrgIds.equals("")){
        		boolean saveMPurOrg = StrutsMPurOrgDelegate.insertUserGrpPopedom(userGrpId,selectOrgIds,selectRepIds,powType);
                
            	if(saveMPurOrg == false){
            		messages.add(FitechResource.getMessage(locale,resources,"save.failed","userGrp.popedom.info"));
                    request.setAttribute(Config.MESSAGES,messages);
                    return new ActionForward("/popedom_mgr/viewUserGrpRepPopedom.do"+page);
            	}      
        	}
        	
        	messages.add(FitechResource.getMessage(locale,resources,"save.success","userGrp.popedom.info"));  
        }catch (Exception e){
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"save.failed","userGrp.popedom.info"));
            request.setAttribute(Config.MESSAGES,messages);
            return new ActionForward("/popedom_mgr/viewUserGrpRepPopedom.do"+page);
        }
      
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES,messages);
        return mapping.findForward("view");    
    }
 }
