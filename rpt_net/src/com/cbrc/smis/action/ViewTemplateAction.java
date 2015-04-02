//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

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

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.service.AFTemplateDelegate;

/** 
 * ģ��鿴
 * @author Ҧ��
 *  
 * XDoclet definition:
 * @struts.action path="/template/viewTemplate" name="mChildReportForm" scope="request" validate="true"
 * @struts.action-forward name="init" path="/template/view/init.jsp"
 */
public class ViewTemplateAction extends Action {
    private static FitechException log = new FitechException(ViewMChildReportAction.class);

    /** 
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
        HttpServletResponse response)  throws IOException, ServletException{
    	com.cbrc.smis.hibernate.LogIn login = new com.cbrc.smis.hibernate.LogIn();

        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        //ȡ��request��Χ�ڵ�����������������logInForm��
        MChildReportForm mChildReportForm = (MChildReportForm) form;
         RequestUtils.populate(mChildReportForm, request);
         String child=request.getParameter("child");
         String version=request.getParameter("version");
         String usingFlag=request.getParameter("usingFlag");
         Integer integerUsingFlag=0;
         if(usingFlag!=null && !usingFlag.equals(""))
        	 integerUsingFlag=Integer.valueOf(usingFlag);
       try{
         if(child!=null && version!=null ){
        	 /* �ı䷢��״̬ */
        	boolean result= StrutsMChildReportDelegate.updateIsPublic(child,version) && AFTemplateDelegate.updateUsingFlag(child, version, integerUsingFlag);  
        	if(result){
        		Config.TEMPLATE_PUT.put(child+"_"+version,"1");
        	}
         }
         
       }catch(Exception e){
    	   e.printStackTrace();
       }

     if(request.getAttribute("reportName")!=null)
    	 mChildReportForm.setReportName((String)request.getParameter("reportName"));
         int recordCount =0; //��¼����
         int offset=0; //ƫ����
         int limit=0;  //ÿҳ��ʾ�ļ�¼����
         List list = null;
         
         try{
        	 //        	ȡ�ü�¼����
             recordCount = StrutsMChildReportDelegate.getTemplateCount(mChildReportForm);
         }catch(Exception ex){
        	 log.printStackTrace(ex);
             messages.add(resources.getMessage("log.select.fail"));  
         }
         ApartPage aPage=new ApartPage();
         aPage.setCount(recordCount);
         String strCurPage=request.getParameter("curPage");
         if(strCurPage!=null && !strCurPage.equals("")){
        	 int curPage = 1;
        	 try{
        		 curPage = Integer.parseInt(strCurPage);
        		 if(curPage > aPage.getPages())
        			 curPage = aPage.getPages();
        	 }catch(Exception ex){
        		 curPage = 1;
        	 }
        	 aPage.setCurPage(curPage);
         }
         else
             aPage.setCurPage(1);
         //����ƫ����
         offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
         limit = Config.PER_PAGE_ROWS;   
         
         try{
             //��ʾ��ҳ��ļ�¼
             if(recordCount > 0)
                 list = StrutsMChildReportDelegate.getTemplates(mChildReportForm,offset,limit);  
         }catch (Exception e){
             log.printStackTrace(e);
             messages.add(resources.getMessage("log.select.fail"));      
         }
         //�Ƴ�request��session��Χ�ڵ�����
         FitechUtil.removeAttribute(mapping,request);
         //��ApartPage��������request��Χ��
         aPage.setTerm(this.getTerm(mChildReportForm));
         request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
         
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(list!=null && list.size()!=0)
           request.setAttribute(Config.RECORDS,list);
         //����ѯ���������request��Χ��
         request.setAttribute("form",mChildReportForm);
         
         return mapping.findForward("init");
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
              
        if(reportName!=null)
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "reportName="+reportName.toString();   
        }
        if(term.indexOf("?")>=0)
            term = term.substring(term.indexOf("?")+1);
            
        // System.out.println("term"+term);
        return term;
        
    }   
 }
