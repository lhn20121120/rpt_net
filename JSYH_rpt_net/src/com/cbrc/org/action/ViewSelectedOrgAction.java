//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

package com.cbrc.org.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.org.form.MOrgForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/** 
 * MyEclipse Struts
 * Creation date: 12-11-2005
 * 
 * XDoclet definition:
 * @struts.action path="/viewSelectOrgSession" name="mOrgForm" scope="request" validate="true"
 * @struts.action-forward name="selectedOrgDetail" path="/template/add/selectedOrgDetail.jsp"
 */
public class ViewSelectedOrgAction extends Action   
                                   {
    private static FitechException log = new FitechException(ViewSelectedOrgAction.class); 
    // --------------------------------------------------------- Instance Variables

    // --------------------------------------------------------- Methods

    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute  (
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException{

        FitechMessages messages = new FitechMessages();
        MessageResources resources = getResources(request);
            
        // 把查询条件放进form
        MOrgForm mOrgForm = (MOrgForm) form;
        RequestUtils.populate(mOrgForm, request);
         //取出机构分类名和机构分类id     
        String orgClsId = mOrgForm.getOrgClsId();
        String orgClsName = mOrgForm.getOrgClsName();
        String childRepId = mOrgForm.getChildRepId();
        String versionId = mOrgForm.getVersionId();
        String reportName = mOrgForm.getReportName();
        String reportStyle = mOrgForm.getReportStyle();
        String flag = request.getParameter("flag");
        
        
        List result = new ArrayList();
        List orgIds = new ArrayList();  //该机构类型机构id集合  
        try {       
                HttpSession session = request.getSession();
                HashMap sessionOrgIds = null; 
                //取出以前选择过的机构集合
                if(session.getAttribute("SelectedOrgIds")!=null)
                {   
                    //取出以前选择过的机构集合
                    sessionOrgIds = (HashMap) session.getAttribute("SelectedOrgIds");
                    //取出该集合中的机构分类类型集合(值)
                    if(sessionOrgIds!=null && sessionOrgIds.size()!=0)
                    {
                        Set orgClsIds = sessionOrgIds.entrySet();
                        Iterator it = orgClsIds.iterator();
                        while(it.hasNext())
                        {
                            Map.Entry entry = (Map.Entry)it.next();
                            if(entry.getValue().equals(orgClsId))
                                orgIds.add((String)entry.getKey()); 
                        }    
                    }
                }
                if(orgIds!=null&&orgIds.size()!=0)
                {
                    for(int i=0; i<orgIds.size(); i++)
                    {
                        result.add(StrutsMOrgDelegate.selectOne(orgIds.get(i).toString()));                    
                    }
                }
             
            
        } catch (Exception e1) {
            // TODO 自动生成 catch 块
            log.printStackTrace(e1);
        }
        // 移除request或session范围内的属性
        FitechUtil.removeAttribute(mapping, request);
        // 把ApartPage对象存放在request范围内
        
        request.setAttribute("ChildRepId",childRepId);
        request.setAttribute("VersionId",versionId);
        request.setAttribute("ReportStyle",reportStyle);
        request.setAttribute("ReportName",reportName);
        
        request.setAttribute("orgClsId",orgClsId);
        request.setAttribute("orgClsName",orgClsName);
        
        if (messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES, messages);
 
        if (result != null && result.size() > 0)
            request.setAttribute(Config.RECORDS, result);
        /**有标志位则跳转填报范围jsp*/
        if(flag!=null && !flag.equals(""))
        {
            if(flag.equals("1"))
                return mapping.findForward("tbfw_selectedOrgDetail");
            else if(flag.equals("2"))
                return mapping.findForward("mod_tbfw_selectedOrgDetail");
        }
          
        /**否则跳转异常变化*/
        return mapping.findForward("selectedOrgDetail");
    }

}

