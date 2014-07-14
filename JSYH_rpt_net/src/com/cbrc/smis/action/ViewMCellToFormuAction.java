package com.cbrc.smis.action;

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

import com.cbrc.smis.adapter.StrutsMCellToFormuDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellToFormuForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/**
 * ���ڱ���ϵ�鿴
 *
 * @author Ҧ��
 *
 * @struts.action
 *    path="/template/viewMCellToFormu"
 *
 * @struts.action-forward
 *    name="bjgx"
 *    path="/template/view/bjgx.jsp"
 *    redirect="false"
 *
 */
public final class ViewMCellToFormuAction extends Action {
    private static FitechException log=new FitechException(ViewMCellToFormuAction.class);
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
       MessageResources resources=getResources(request);
       FitechMessages messages = new FitechMessages();
       
       //ȡ��request��Χ�ڵ�����������������logInForm��
       MCellToFormuForm mCellToFormuForm = new MCellToFormuForm();
        RequestUtils.populate(mCellToFormuForm, request);
       // request.setCharacterEncoding("gb2312"); 
        String reportName=request.getParameter("reportName");
       // String reportName = new String(request.getParameter("reportName").getBytes("ISO-8859-1"),"GB2312");
        
        int recordCount =0; //��¼����
        int offset=0; //ƫ����
        int limit=0;  //ÿҳ��ʾ�ļ�¼����
        List list = null;
        
        ApartPage aPage=new ApartPage();
        String strCurPage=request.getParameter("curPage");
        if(strCurPage!=null){
            if(!strCurPage.equals(""))
              aPage.setCurPage(new Integer(strCurPage).intValue());
        }
        else
            aPage.setCurPage(1);
        //����ƫ����
        offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
        limit = Config.PER_PAGE_ROWS;   
        String childRepId = mCellToFormuForm.getChildRepId();
        String versionId = mCellToFormuForm.getVersionId();
        try 

        {
            //ȡ�ü�¼����
            recordCount = StrutsMCellToFormuDelegate.getRecordCount(childRepId,versionId);
            //��ʾ��ҳ��ļ�¼
            if(recordCount > 0)
                list = StrutsMCellToFormuDelegate.select(childRepId,versionId,offset,limit);  
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
        aPage.setCount(recordCount);
        request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
        
        request.setAttribute("ReportName",reportName);
        request.setAttribute("ChildRepId",childRepId);
        
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
