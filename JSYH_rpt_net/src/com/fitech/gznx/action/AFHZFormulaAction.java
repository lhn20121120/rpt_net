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
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.service.AFTemplateDelegate;

public class AFHZFormulaAction extends Action {
	private static FitechException log = new FitechException(AFHZFormulaAction.class);

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
        OrgInfoForm orgInfoForm = (OrgInfoForm) form;
        RequestUtils.populate(orgInfoForm, request);
        String childRepId=request.getParameter("childRepId");
        String templateId=request.getParameter("templateId");
        
        String versionId=request.getParameter("versionId");
        String reportName=request.getParameter("reportName");
       
        String orgId=request.getParameter("orgId");
        orgInfoForm.setTemplateId(childRepId);
        request.setAttribute("childRepId", childRepId);
        request.setAttribute("versionId", versionId);
        request.setAttribute("reportName", reportName);
        request.setAttribute("orgId", orgId);
        String opration=request.getParameter("opration");
        if(opration!=null&&!"".equals(opration)){
        	request.setAttribute("next", opration);
        }


//     if(request.getAttribute("orgName")!=null)
    	 orgInfoForm.setOrgName(request.getParameter("orgName"));
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
              list= AFTemplateDelegate.getHZItems(orgInfoForm,reportFlg,curPage,templateId ,versionId);  
//              list = pageList.getList();
//              recordCount = (int)pageList.getRowCount();
         }catch (Exception e){
             log.printStackTrace(e);
             messages.add(resources.getMessage("log.select.fail"));      
         }

         aPage.setCount(list.size());
         //移除request或session范围内的属性
        // FitechUtil.removeAttribute(mapping,request);
         //把ApartPage对象存放在request范围内
         aPage.setTerm(this.getTerm(orgInfoForm));
         request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
         
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(list!=null && list.size()!=0)
           request.setAttribute(Config.RECORDS,list);
         //将查询条件存放在request范围内
         request.setAttribute("form",orgInfoForm);
         
    
		
		return mapping.findForward("index");
	}
	  /**
     * 取得查询条件url  
     * @param logInForm
     * @return
     */
    public String getTerm(OrgInfoForm orgInfoForm)
    {
        String term="";
        
        String orgName = orgInfoForm.getOrgName();
        //String templateType = orgInfoForm.getTemplateType();
        if(orgName!=null)
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "orgName="+orgName.toString();   
        }
        
        if(term.indexOf("?")>=0)
            term = term.substring(term.indexOf("?")+1);
            
        // System.out.println("term"+term);
        return term;
        
    }

}
