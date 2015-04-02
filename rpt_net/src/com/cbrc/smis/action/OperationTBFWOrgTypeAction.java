package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

import com.cbrc.org.form.MOrgForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;



/**
 * ģ�飺���Χ�������������ϸ��
 * ��ѡ�еĻ���id���д���������ظ�ǰ̨
 *
 * @author Ҧ��
 *
 * @struts.action
 *    path="/template/operationOrgType"
 *
 * @struts.action-forward
 *    name="all"
 *    path="/struts/getAll.do"
 *    redirect="false"
 *

 */
public final class OperationTBFWOrgTypeAction extends Action {
    private static FitechException log = new FitechException(OperationTBFWOrgTypeAction.class); 
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
      HttpServletResponse response
   )
      throws IOException, ServletException {
      // // System.out.println("OperationTBFWOrgTypeAction=======beging  ");
       
       FitechMessages messages = new FitechMessages();
       MessageResources resources = getResources(request);
       
       MOrgForm mOrgForm = new MOrgForm();
       RequestUtils.populate(mOrgForm,request);
       //��ѡ�л�����������id
       String orgClsId = mOrgForm.getOrgClsId();
       String orgClsName = mOrgForm.getOrgClsName();
       String childRepId = mOrgForm.getChildRepId();
       String versionId = mOrgForm.getVersionId();
       String reportName = mOrgForm.getReportName();
       String reportStyle = mOrgForm.getReportStyle();
       //��ѡ�еĻ���id
       String[] new_selectOrgIds = mOrgForm.getSelectOrgIds();
       
       
       //�û����������͵����л�����id
       String[] orgIds = mOrgForm.getOrgIds();
       
       
       ArrayList new_selectOrgIdList = new ArrayList();
       ArrayList orgIdList = new ArrayList();
       //���������л���id���鹹��ɼ���
       if(orgIds!=null && orgIds.length!=0)
       {
           for(int i=0;i<orgIds.length;i++)
           {
               orgIdList.add(orgIds[i]); 
           }
       } 
       
       //������ѡ�л���id���鹹��ɼ���
       if(new_selectOrgIds!=null&& new_selectOrgIds.length!=0)
       {
           for(int i=0;i<new_selectOrgIds.length;i++)
           {
               new_selectOrgIdList.add(new_selectOrgIds[i]);
           } 
       }
         
       // ��ǰѡ�й��Ļ���id
       HashMap old_selectOrgIds = null;
       HttpSession session = request.getSession();
           
           
      if (session.getAttribute("SelectedOrgIds") != null) 
      {
         old_selectOrgIds = (HashMap) session.getAttribute("SelectedOrgIds");
         if (old_selectOrgIds != null && old_selectOrgIds.size() > 0) 
         {
                // �Ƴ��Ѿ�ѡ���ĸû�������id�Ļ���id
            for (int i = 0; i < orgIdList.size(); i++) 
            {
                if (old_selectOrgIds.containsKey(orgIdList.get(i)))
                    old_selectOrgIds.remove(orgIdList.get(i));
            }
        }
      } 
      else
          old_selectOrgIds = new HashMap();

        // �����ѡ��Ļ���id
        if (new_selectOrgIdList != null && new_selectOrgIdList.size() > 0) {
            for (int i = 0; i < new_selectOrgIdList.size(); i++) {
                old_selectOrgIds.put(new_selectOrgIdList.get(i), orgClsId);
            }
        }
        //��ѡ��Ļ���id�ŵ�session��Χ��
        if (old_selectOrgIds != null && old_selectOrgIds.size() > 0)
            session.setAttribute("SelectedOrgIds", old_selectOrgIds);
        
        request.setAttribute("orgClsId", orgClsId);
        request.setAttribute("orgClsName",orgClsName);
        //ȡ����������ҳ��    
        String curPageUrl = mOrgForm.getCurPage();
        String curPage = curPageUrl.substring(curPageUrl.lastIndexOf('=') + 1);
        request.setAttribute("curPage",curPage);
       //�����"ȷ��"�����(ҳ����0)
        
        String path= "";
        if(curPage == null || curPage.equals("0") ||curPage.equals("")) 
            path = mapping.findForward("viewTBFW").getPath();
        else          
            path = mapping.findForward("viewTBFWOrgInfo").getPath();
       
           
        if(childRepId!=null)
        {
           path += (path.indexOf("?")>=0 ? "&" : "?");
           path += "childRepId="+childRepId;   
        }
        if(versionId!=null)
        {
           path += (path.indexOf("?")>=0 ? "&" : "?");
           path += "versionId="+versionId;   
        }
        if(reportName!=null)
        {
           path += (path.indexOf("?")>=0 ? "&" : "?");
           path += "reportName="+reportName;   
        }
        if(reportStyle!=null)
        {
           path += (path.indexOf("?")>=0 ? "&" : "?");
           path += "reportStyle="+reportStyle;   
        }
        
        return new ActionForward(path);
   }
}

