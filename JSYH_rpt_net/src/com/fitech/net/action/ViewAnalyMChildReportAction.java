package com.fitech.net.action;

import java.io.IOException;
import java.util.Calendar;
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

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/**
 * ��ѯ������ģ��
 *
 * @author wh
 */
public final class ViewAnalyMChildReportAction extends Action {
    private static FitechException log = new FitechException(ViewAnalyMChildReportAction.class);

   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException { 

       MessageResources resources = getResources(request);
       FitechMessages messages = new FitechMessages();
       //ȡ��request��Χ�ڵ�����������������logInForm��
       MChildReportForm mChildReportForm = (MChildReportForm) form;
        RequestUtils.populate(mChildReportForm, request);
    
        int recordCount =0; //��¼����
        int offset=0; //ƫ����
        int limit=0;  //ÿҳ��ʾ�ļ�¼����
        List list = null;
        
        ApartPage aPage=new ApartPage();
        String strCurPage=getParameter(request,"curPage");
        if(strCurPage!=null){
            if(!strCurPage.equals(""))
              aPage.setCurPage(new Integer(strCurPage).intValue());
            else
              aPage.setCurPage(1);
        }
        else
            aPage.setCurPage(1);
        //����ƫ����
        offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
        limit = Config.PER_PAGE_ROWS;   
        
		Calendar calendar = Calendar.getInstance();		
		
		Integer year =new Integer(calendar.get(Calendar.YEAR));		   
		Integer term= new Integer(calendar.get(Calendar.MONTH)+1); 
		 HttpSession session = request.getSession();
         Operator operator = null; 
         if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
             operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
         
         mChildReportForm.setVersionId(StrutsMRepRangeDelegate.getFRVersionId(year.toString(),term.toString(),operator.getOrgId()));
        try 

        {
            //ȡ�ü�¼����
            recordCount = StrutsMChildReportDelegate.getReportRecordCount(mChildReportForm);
            //��ʾ��ҳ��ļ�¼
            if(recordCount > 0){
                list = StrutsMChildReportDelegate.selectReport(mChildReportForm,offset,limit);
            }
        }
        catch (Exception e) 
        {
            log.printStackTrace(e);
            messages.add(resources.getMessage("log.select.fail"));      
        }
        //�Ƴ�request��session��Χ�ڵ�����
        FitechUtil.removeAttribute(mapping,request);
        //��ApartPage��������request��Χ��
        aPage.setTerm(this.getTerm(mChildReportForm));
        aPage.setCount(recordCount);
        request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
        
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
        if(list!=null && list.size()>0){
          request.setAttribute(Config.RECORDS,list);
        }
        //����ѯ���������request��Χ��
        request.setAttribute("form",mChildReportForm);
 
        if(session.getAttribute("SelectedOrgIds")!=null)
            session.removeAttribute("SelectedOrgIds");
            
            
        return mapping.findForward("view");
   }
   
   /**
    * ��ȡ������ֵ
    * 
    * @param req HttpServletRequest 
    * @param name String 
    * @return String 
    */
   private String getParameter(HttpServletRequest req, String name) {
	   String value = req.getParameter(name);
	   if (value == null) {
		   if(req.getAttribute(name)!=null)
			   value = req.getAttribute(name).toString();
		   else
			   return null;
	   }
	   return value;
   }
   
   /**
    * ȡ�ò�ѯ����url  
    * @param logInForm
    * @return
    */
   public String getTerm(MChildReportForm mChildReportForm)
   {
       String term="";
       
       String reportName = mChildReportForm.getReportName();
       String versionId = mChildReportForm.getVersionId();
       
       if(reportName!=null)
       {
           term += (term.indexOf("?")>=0 ? "&" : "?");
           term += "reportName="+reportName.toString();   
       }
       if(versionId!=null && !versionId.equals(""))
       {
           term += (term.indexOf("?")>=0 ? "&" : "?");
           term += "versionId="+versionId.toString();   
       }
       if(term.indexOf("?")>=0)
           term = term.substring(term.indexOf("?")+1);
           
       // System.out.println("term"+term);
       return term;
       
   }
   
  
}
