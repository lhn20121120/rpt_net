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
     * 已使用hibernate 卞以刚 2011-12-27
     * 影响对象：AfValidateformula
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
        
        //取得request范围内的请求参数，并存放在logInForm内
        MCellToFormuForm mCellToFormuForm = new MCellToFormuForm();
         RequestUtils.populate(mCellToFormuForm, request);

         int recordCount =0; //记录总数

         List list = null;
         
         ApartPage aPage=new ApartPage();

 
         String childRepId = mCellToFormuForm.getChildRepId();
         String versionId = mCellToFormuForm.getVersionId();
         
         AfTemplate af = StrutsExcelData.getTemplateSimple(versionId, childRepId);
 		String reportName=af!=null?af.getTemplateName():request.getParameter("reportName");
         try 

         {
        	//显示分页后的记录
        	 /**已使用hibernate 卞以刚 2011-12-27
        	  * 影响对象：AfValidateformula*/
        	 list= StrutsAFCellFormuDelegate.selectAllFormula(childRepId,versionId);

         }
         catch (Exception e) 
         {
             log.printStackTrace(e);
             messages.add(resources.getMessage("log.select.fail"));      
         }
         //移除request或session范围内的属性
         FitechUtil.removeAttribute(mapping,request);
         //把ApartPage对象存放在request范围内
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
     * 取得查询条件url  
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
