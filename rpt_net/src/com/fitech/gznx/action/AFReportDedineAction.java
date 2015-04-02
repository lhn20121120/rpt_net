package com.fitech.gznx.action;

import java.io.IOException;
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

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.hibernate.SysParameter;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.institution.adapter.StrutsSysSetDelegate;

public class AFReportDedineAction extends Action {
	private static FitechException log = new FitechException(AFReportDedineAction.class);

    /** 
     * 已使用Hibernate 卞以刚 2011-12-27
     * 影响对象：AfTemplate
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
        
		
		HttpSession session = request.getSession();
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		/** 报表选中标志 **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        //取得request范围内的请求参数，并存放在logInForm内
        AFTemplateForm mChildReportForm = (AFTemplateForm) form;
         RequestUtils.populate(mChildReportForm, request);
         String child=request.getParameter("child");
         String version=request.getParameter("version");
         List <SysParameter>parameters =StrutsSysSetDelegate.loadSysCol();
         if(parameters !=null && parameters.size()>0){
 			request.setAttribute("parameters",parameters);
 			}
       try{
         if(child!=null && version!=null ){
        	 if(request.getParameter("fabuflg")!= null){
        		 /** 已使用hibernate 卞以刚 2011-12-21
        		  * 影响对象：AfTemplate*/
        		 boolean result= AFTemplateDelegate.updateUsingFlag(child,version,0); 
        	 } else{
        		 /* 改变发布状态 */
        		 /** 已使用hibernate 卞以刚 2011-12-21
        		  * 影响对象：AfTemplate*/
             	boolean result= AFTemplateDelegate.updateUsingFlag(child,version,1);  
             	if(result){
             		Config.TEMPLATE_PUT.put(child+"_"+version,"1");
             	} 
        	 }        	 
         }
         
       }catch(Exception e){
    	   e.printStackTrace();
       }

     if(request.getAttribute("templateName")!=null)
    	 mChildReportForm.setTemplateName((String)request.getParameter("templateName"));
         int recordCount =0; //记录总数

         List list = null;
         ApartPage aPage=new ApartPage();
         int curPage = 1;
         String strCurPage=request.getParameter("curPage");
 		if(strCurPage!=null){
			if(!strCurPage.equals(""))
				curPage =  new Integer(strCurPage).intValue();
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}else{
			aPage.setCurPage(1);
		}
         
         try{
             //显示分页后的记录
        	 /**已使用hibernate 卞以刚 2011-12-22
        	  * 影响对象：AfTemplate*/
              PageListInfo pageList= AFTemplateDelegate.getTemplates(mChildReportForm,reportFlg,curPage);  
              list = pageList.getList();
              recordCount = (int)pageList.getRowCount();
         }catch (Exception e){
             log.printStackTrace(e);
             messages.add(resources.getMessage("log.select.fail"));      
         }
//         if(!StringUtil.isEmpty(mChildReportForm.getTemplateType()) && StringUtil.isEmpty(mChildReportForm.getTemplateTypeName())){
//        	 String templateName = AFTemplateTypeDelegate.getTemplateTypeName(mChildReportForm.getTemplateType(), reportFlg);
//        	 mChildReportForm.setTemplateTypeName(templateName);
//         }
         aPage.setCount(recordCount);
         //移除request或session范围内的属性
        // FitechUtil.removeAttribute(mapping,request);
         //把ApartPage对象存放在request范围内
         aPage.setTerm(this.getTerm(mChildReportForm));
         request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
         
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(list!=null && list.size()!=0)
           request.setAttribute(Config.RECORDS,list);
         //将查询条件存放在request范围内
         request.setAttribute("form",mChildReportForm);
         
		return mapping.findForward("index");
	}
	  /**
     * 取得查询条件url  
     * @param logInForm
     * @return
     */
    public String getTerm(AFTemplateForm mChildReportForm)
    {
        String term="";
        
        String reportName = mChildReportForm.getTemplateName();
        String templateType = mChildReportForm.getTemplateType();
        if(reportName!=null)
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "templateName="+reportName.toString();   
        }
        if(templateType!=null)
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "templateType="+templateType.toString();   
        }
        if(term.indexOf("?")>=0)
            term = term.substring(term.indexOf("?")+1);
            
        // System.out.println("term"+term);
        return term;
        
    }

}
