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

import com.cbrc.auth.adapter.StrutsMPurBankLevelDelegate;
import com.cbrc.auth.form.MPurBanklevelForm;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.service.AFTemplateDelegate;

/** 
 * 查看用户报表权限信息
 * @author 姚捷
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class ViewUserGrpRepPopedomAction extends Action {


    private static FitechException log = new FitechException(ViewUserGrpOrgPopedomAction.class); 
    /** 
     * 已使用hibernate 卞以刚 2011-12-28
     * 影响对象：AfTemplate MPurBanklevel MPurOrg
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
        
        MPurBanklevelForm mPurBanklevelForm = (MPurBanklevelForm) form;
        RequestUtils.populate(mPurBanklevelForm,request);
      
        /**该用户组已经选择的报表权限*/
        List userGrpRepPopedom = null;          
        /**查询所有报表信息*/
        List cbrclist = null;
        List pboclist = null;
        List otherlist = null;
        try{            
        	HttpSession session = request.getSession();
        	Operator operator = null; 
            if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
                operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
            
            /***
             * 用户组权限分配，原先屏蔽对非超级用户报表权限控制 
             * 于2012-11-01修改放开对非超级用户权限控制 
             * 修改为总行人员屏蔽报表权限控制
             * 非总行人员放开报表权限控制
             */
            if(operator.isSuperManager() == false){
//	            if(!operator.getOrgId().equals(com.fitech.gznx.common.Config.HEAD_ORG_ID)){
	            
	            	cbrclist = StrutsMPurBankLevelDelegate.getUserGrpRepPopedomFromAFBytype(operator.getUserGrpIds(),
	            			mPurBanklevelForm.getPowType(), com.fitech.gznx.common.Config.CBRC_REPORT);
	            	pboclist = StrutsMPurBankLevelDelegate.getUserGrpRepPopedomFromAFBytype(operator.getUserGrpIds(),
	            			mPurBanklevelForm.getPowType(), com.fitech.gznx.common.Config.PBOC_REPORT);
	            	otherlist = StrutsMPurBankLevelDelegate.getUserGrpRepPopedomFromAFBytype(operator.getUserGrpIds(),
	            			mPurBanklevelForm.getPowType(), com.fitech.gznx.common.Config.OTHER_REPORT);
//	            }
            } else {
            	/**已使用Hibernate 卞以刚 2011-12-28
            	 * 影响对象：AfTemplate*/
            	cbrclist = AFTemplateDelegate.getAllReportsByType(com.fitech.gznx.common.Config.CBRC_REPORT);
            	pboclist = AFTemplateDelegate.getAllReportsByType(com.fitech.gznx.common.Config.PBOC_REPORT);
            	otherlist = AFTemplateDelegate.getAllReportsByType(com.fitech.gznx.common.Config.OTHER_REPORT);
            	
            }
            
            /**查询该用户组已经有的报表权限信息*/
            	/**已使用hibernate 卞以刚 2011-12-28
            	 * 影响对象：AfTemplate MPurBanklevel MPurOrg*/
            List repPopedom = StrutsMPurBankLevelDelegate.getUserGrpRepPopedomFromAF(mPurBanklevelForm.getUserGrpId(),mPurBanklevelForm.getPowType());
            // 2008-07-16   gongming
            Map idMap = new HashMap();
            if (repPopedom!=null && repPopedom.size()!=0){
                userGrpRepPopedom =  new ArrayList();
                for (int i=0; i<repPopedom.size(); i++){                   
                	MChildReportForm rf = (MChildReportForm)(repPopedom.get(i));
                     if(!idMap.containsKey(rf.getChildRepId()))
                     {
                         idMap.put(rf.getChildRepId(), rf.getReportName());
                         userGrpRepPopedom.add(new LabelValueBean("[" + rf.getChildRepId() +"]" + rf.getReportName(),rf.getChildRepId()));
                     }
                }
            }
            idMap.clear();
        }catch (Exception e){
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"select.fail","userGrp.repPopedom.info"));        
        }
        if(mPurBanklevelForm.getCurPage()!=null){
        	request.setAttribute("curPage",mPurBanklevelForm.getCurPage());
        }
        /**用户组编号*/
        if(mPurBanklevelForm.getUserGrpId()!=null)
            request.setAttribute("UserGrpId",mPurBanklevelForm.getUserGrpId());
        
        /**用户组名称*/
        if(mPurBanklevelForm.getUserGrpNm()!=null)
            request.setAttribute("UserGrpNm",mPurBanklevelForm.getUserGrpNm());
        
        /**权限类型*/
        if(mPurBanklevelForm.getPowType()!=null)
        	request.setAttribute("powType",mPurBanklevelForm.getPowType());
        
        /**用户组已经有的报表权限*/
        if(userGrpRepPopedom!=null && userGrpRepPopedom.size()!=0)
            request.setAttribute("UserGrpRepPopedom",userGrpRepPopedom);
        
//        /**所有报表*/
//        if(allReport!=null && allReport.size()!=0)
//            request.setAttribute("AllReport",allReport);
        if(cbrclist!=null && cbrclist.size()!=0)
          request.setAttribute("cbrc",cbrclist);
        if(pboclist!=null && pboclist.size()!=0)
            request.setAttribute("pboc",pboclist);
        if(otherlist!=null && otherlist.size()!=0)
            request.setAttribute("other",otherlist);
        /**选中的机构id字串*/
        String selectOrgIds = mPurBanklevelForm.getSelectOrgIds();
        if(selectOrgIds == null) selectOrgIds = "";           
        request.setAttribute("selectOrgIds",selectOrgIds);
        
        /**选中的机构名称字串*/
        String selectOrgNames = mPurBanklevelForm.getSelectOrgNames();
        if(selectOrgNames == null) selectOrgNames = "";
        request.setAttribute("selectOrgNames",selectOrgNames);
        
        /**选中的行级ID*/
        String selectBankLevelIds = mPurBanklevelForm.getSelectBankLevelIds();
        if(selectBankLevelIds == null) selectBankLevelIds = "";
        request.setAttribute("selectBankLevelIds",selectBankLevelIds);
        
        /**选中的行级名称*/
        String selectBankLevelNames = mPurBanklevelForm.getSelectBankLevelNames();
        if(selectBankLevelNames == null) selectBankLevelNames = "";
        request.setAttribute("selectBankLevelNames",selectBankLevelNames);
                
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES,messages);
        return mapping.findForward("user_group_rep_popedom");
    }
}

