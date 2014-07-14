package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.other.OrgDetail;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

/**
 * 根据机构类型id，显示该机构类型的机构信息
 *
 * @author 姚捷
 */
public final class Mod_ViewTBFWOrgInfoAction extends Action {
    private static FitechException log = new FitechException(Mod_ViewTBFWOrgInfoAction.class); 
    
       /**
        * @exception IOException  IO异常
        * @exception ServletException  ServletException异常
        */
   
    
    public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {

        FitechMessages messages = new FitechMessages();
        MessageResources resources = getResources(request);
            
        // 把查询条件放进form
        MOrgForm mOrgForm = new MOrgForm();
        RequestUtils.populate(mOrgForm, request);
              
        String orgClsName = mOrgForm.getOrgClsName();
        String orgClsId = mOrgForm.getOrgClsId();
        String childRepId = mOrgForm.getChildRepId();
        String versionId = mOrgForm.getVersionId();
        String reportName = mOrgForm.getReportName();
        //String reportStyle = mOrgForm.getReportStyle();
             
        if(request.getAttribute("orgClsName")!=null)
            orgClsName = (String)request.getAttribute("orgClsName");
        if(request.getAttribute("orgClsId")!=null)
            orgClsId = (String)request.getAttribute("orgClsId");
        
        int recordCount = 0; // 记录总数
        int offset = 0; // 偏移量
        int limit = 0; // 每页显示的记录条数

        // List对象的初始化
        List result = null;
        //
        ApartPage aPage = new ApartPage();
        String strCurPage =null;
        if(request.getAttribute("curPage")!=null)
            strCurPage = (String)request.getAttribute("curPage");
        
        if (strCurPage != null) 
        {
            if (!strCurPage.equals(""))
                aPage.setCurPage(new Integer(strCurPage).intValue());
        } 
        else
            aPage.setCurPage(1);
        // 计算偏移量
        offset = (aPage.getCurPage() - 1) * Config.PER_PAGE_ROWS;
        limit = Config.PER_PAGE_ROWS;
        
        try {
            // 取得记录总数
            recordCount = StrutsMOrgDelegate.getRecordCount(mOrgForm);
            // 从数据库查询该页的记录
            List listFromDB  = null;
            if (recordCount > 0)
                listFromDB = StrutsMOrgDelegate.select(mOrgForm, offset, limit);
           
            //处理数据库返回的记录，包装成我所要的List
            result = this.operationResult(listFromDB,request,orgClsName);
 
        } catch (Exception e) {
            log.printStackTrace(e);
            messages.add(resources.getMessage("orgcls.select.fail"));
       }
        //FitechUtil.removeAttribute(mapping, request);
                 
        // 把ApartPage对象存放在request范围内
        aPage.setTerm(this.getTerm(mOrgForm,request));
        aPage.setCount(recordCount);
        request.setAttribute(Config.APART_PAGE_OBJECT, aPage);
     
        request.setAttribute("orgClsId",orgClsId);
        request.setAttribute("orgClsName",orgClsName);
        
        if(childRepId!=null)
            request.setAttribute("ChildRepId",childRepId);
        if(versionId!=null)
            request.setAttribute("VersionId",versionId);
        if(reportName!=null)
            request.setAttribute("ReportName",reportName);
        
        if (messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES, messages);
        /*
         * 如果StrutsMOrgDelegate类中返回的reslist对象不为空并且对象的大小大于0，
         *  则返回一个包含reslist集合的request对象
         */   
        if (result != null && result.size() > 0)
            request.setAttribute(Config.RECORDS, result);
        // 返回到页面view
        return mapping.findForward("tbfw_orgDetail");
    }
    
    /**
     * 处理查询条件
     * @param mOrgForm 包含查询条件
     * @return  根据查询条件所连成的url
     */
    private String getTerm(MOrgForm mOrgForm,HttpServletRequest request) {
        String term = "";

        String orgName = mOrgForm.getOrgName();
        String orgClsId = mOrgForm.getOrgClsId();
        String orgClsName = mOrgForm.getOrgClsName();
        
        if (orgName != null && !orgName.equals("")) {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "orgName=" + orgName;
        }

        if (orgClsId != null && !orgClsId.equals("")) {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "orgClsId=" + orgClsId;
        }
        if (orgClsName != null && !orgClsName.equals("")) {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "orgClsName=" + orgClsName;
        }
        
        if(term.indexOf("?")>=0)
               term = term.substring(term.indexOf("?")+1);
        //// System.out.println("term" + term);
        return term;
    }
    
    /**
     * 处理数据库返回的集合，把它包装成我所要返回的List对象
     * @return 包装后的集合
     */
    private List operationResult(List listFromDB,HttpServletRequest request,String orgClsName)
    {
        List result = new ArrayList();
        HttpSession session = request.getSession();
        HashMap selectOrgIds = null;
//      在session范围内取出以前选过的机构的id集合
        if(session.getAttribute("SelectedOrgIds")!=null)
        {
            selectOrgIds = (HashMap)session.getAttribute("SelectedOrgIds"); 
//            // System.out.println("Session Exist!!!");
        }
        
            
        if(listFromDB!=null && listFromDB.size()>0)
        {
            
            for(int i=0;i<listFromDB.size();i++)
            {
                OrgDetail orgDetail = new OrgDetail();
                MOrgForm record =(MOrgForm)listFromDB.get(i);
                
                orgDetail.setOrgId(record.getOrgId());
                orgDetail.setOrgName(record.getOrgName());
                orgDetail.setOrgClsName(orgClsName);
                orgDetail.setChecked("false");
                
                if(selectOrgIds!=null)
                {
                    //如果以前选过这个机构id，则把让它的那个复选框选中
                    if(selectOrgIds.containsKey(orgDetail.getOrgId().trim()))
                       orgDetail.setChecked("true");
                }
                result.add(orgDetail);
            }
        }
        return result;   
    }
 }