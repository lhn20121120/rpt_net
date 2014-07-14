package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.action.ViewMCellToFormuAction;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellToFormuForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;
import com.fitech.net.adapter.StrutsExcelData;

public class ViewAFCellToFormuAction extends Action {
    private static FitechException log=new FitechException(ViewMCellToFormuAction.class);
    /**
     * ��ʹ��hibernate ���Ը� 2011-12-27
     * Ӱ�����AfValidateformula
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
        MessageResources resources=getResources(request);
        FitechMessages messages = new FitechMessages();
        
        //ȡ��request��Χ�ڵ�����������������logInForm��
        MCellToFormuForm mCellToFormuForm = new MCellToFormuForm();
         RequestUtils.populate(mCellToFormuForm, request);

         int recordCount =0; //��¼����

         List list = null;
         
         ApartPage aPage=new ApartPage();

 
         String childRepId = mCellToFormuForm.getChildRepId();
         String versionId = mCellToFormuForm.getVersionId();
         
         AfTemplate af = StrutsExcelData.getTemplateSimple(versionId, childRepId);
 		String reportName=af!=null?af.getTemplateName():request.getParameter("reportName");
         try 

         {
        	//��ʾ��ҳ��ļ�¼
        	 /**��ʹ��hibernate ���Ը� 2011-12-27
        	  * Ӱ�����AfValidateformula*/
        	 list= StrutsAFCellFormuDelegate.selectAllFormula(childRepId,versionId);

         }
         catch (Exception e) 
         {
             log.printStackTrace(e);
             messages.add(resources.getMessage("log.select.fail"));      
         }
         //�Ƴ�request��session��Χ�ڵ�����
         FitechUtil.removeAttribute(mapping,request);
         //��ApartPage��������request��Χ��
         aPage.setTerm(this.getTerm(mCellToFormuForm,reportName));

         request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
         
         request.setAttribute("ReportName",reportName);
         request.setAttribute("ChildRepId",childRepId);
         request.setAttribute("VersionId",versionId);
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(list!=null && list.size()!=0)
           request.setAttribute(Config.RECORDS,list);
         
         return mapping.findForward("bjgx");
    }
    /**
     * ȡ�ò�ѯ����url  
     * @param logInForm
     * @return
     */
    public String getTerm(MCellToFormuForm mCellToFormuForm,String reportName)
    {
        String term="";
        
       String childRepId = mCellToFormuForm.getChildRepId();
       String versionId = mCellToFormuForm.getVersionId();
        if(childRepId!=null&& !childRepId.equals(""))
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "childRepId="+childRepId;   
        }
        if(versionId!=null && !versionId.equals(""))
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "versionId="+versionId;   
        }
        if(reportName!=null && !reportName.equals(""))
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "reportName="+reportName;  
        }
        if(term.indexOf("?")>=0)
            term = term.substring(term.indexOf("?")+1);
        // System.out.println("term"+term);
        return term;
        
    }
}
