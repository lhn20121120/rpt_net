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
 * ���ݻ�������id����ʾ�û������͵Ļ�����Ϣ
 *
 * @author Ҧ��
 */
public final class Mod_ViewTBFWOrgInfoAction extends Action {
    private static FitechException log = new FitechException(Mod_ViewTBFWOrgInfoAction.class); 
    
       /**
        * @exception IOException  IO�쳣
        * @exception ServletException  ServletException�쳣
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
            
        // �Ѳ�ѯ�����Ž�form
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
        
        int recordCount = 0; // ��¼����
        int offset = 0; // ƫ����
        int limit = 0; // ÿҳ��ʾ�ļ�¼����

        // List����ĳ�ʼ��
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
        // ����ƫ����
        offset = (aPage.getCurPage() - 1) * Config.PER_PAGE_ROWS;
        limit = Config.PER_PAGE_ROWS;
        
        try {
            // ȡ�ü�¼����
            recordCount = StrutsMOrgDelegate.getRecordCount(mOrgForm);
            // �����ݿ��ѯ��ҳ�ļ�¼
            List listFromDB  = null;
            if (recordCount > 0)
                listFromDB = StrutsMOrgDelegate.select(mOrgForm, offset, limit);
           
            //�������ݿⷵ�صļ�¼����װ������Ҫ��List
            result = this.operationResult(listFromDB,request,orgClsName);
 
        } catch (Exception e) {
            log.printStackTrace(e);
            messages.add(resources.getMessage("orgcls.select.fail"));
       }
        //FitechUtil.removeAttribute(mapping, request);
                 
        // ��ApartPage��������request��Χ��
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
         * ���StrutsMOrgDelegate���з��ص�reslist����Ϊ�ղ��Ҷ���Ĵ�С����0��
         *  �򷵻�һ������reslist���ϵ�request����
         */   
        if (result != null && result.size() > 0)
            request.setAttribute(Config.RECORDS, result);
        // ���ص�ҳ��view
        return mapping.findForward("tbfw_orgDetail");
    }
    
    /**
     * �����ѯ����
     * @param mOrgForm ������ѯ����
     * @return  ���ݲ�ѯ���������ɵ�url
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
     * �������ݿⷵ�صļ��ϣ�������װ������Ҫ���ص�List����
     * @return ��װ��ļ���
     */
    private List operationResult(List listFromDB,HttpServletRequest request,String orgClsName)
    {
        List result = new ArrayList();
        HttpSession session = request.getSession();
        HashMap selectOrgIds = null;
//      ��session��Χ��ȡ����ǰѡ���Ļ�����id����
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
                    //�����ǰѡ���������id������������Ǹ���ѡ��ѡ��
                    if(selectOrgIds.containsKey(orgDetail.getOrgId().trim()))
                       orgDetail.setChecked("true");
                }
                result.add(orgDetail);
            }
        }
        return result;   
    }
 }