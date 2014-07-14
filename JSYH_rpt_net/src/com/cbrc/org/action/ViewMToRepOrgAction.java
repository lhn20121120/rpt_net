package com.cbrc.org.action;


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

import com.cbrc.org.form.MToRepOrgForm;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/**
 * 模块名:代报机构查询
 *
 * @author 姚捷
 *
 * @struts.action
 *    path="/config/viewMToRepOrg"
 *
 * @struts.action-forward
 *    name="mToRepOrgForm"
 *    path="/config/viewMToRepOrgAction"
 *    redirect="false"
 *

 */
public final class ViewMToRepOrgAction extends Action {
	private static FitechException log = new FitechException(ViewMToRepOrgAction.class); 
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
	   //取得request范围内的请求参数，并存放在logInForm内
	   MToRepOrgForm mToRepOrgForm = new MToRepOrgForm();
		RequestUtils.populate(mToRepOrgForm, request);
	
		int recordCount =0; //记录总数
		int offset=0; //偏移量
		int limit=0;  //每页显示的记录条数
		List list = null;
		
		ApartPage aPage=new ApartPage();
	   	String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
		    if(!strCurPage.equals(""))
		      aPage.setCurPage(new Integer(strCurPage).intValue());
		}
		else
			aPage.setCurPage(1);
		//计算偏移量
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
		limit = Config.PER_PAGE_ROWS;	
		
		try 

		{
	   		//取得记录总数
	   		//recordCount = StrutsMToRepOrgDelegate.getRecordCount(mToRepOrgForm);
	   		//显示分页后的记录
	   		//if(recordCount > 0)
		   	  //  list = StrutsMToRepOrgDelegate.select(mToRepOrgForm,offset,limit);  
		}
		catch (Exception e) 
		{
			log.printStackTrace(e);
			messages.add(resources.getMessage("org.select.fail"));		
		}
		//移除request或session范围内的属性
		FitechUtil.removeAttribute(mapping,request);
		//把ApartPage对象存放在request范围内
	 	aPage.setTerm(this.getTerm(mToRepOrgForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
	 	if(messages.getMessages() != null && messages.getMessages().size() > 0)
	   	  request.setAttribute(Config.MESSAGES,messages);
	 	if(list!=null && list.size()!=0)
		  request.setAttribute(Config.RECORDS,list);
	 	//将查询条件存放在request范围内
	 	request.setAttribute("form",mToRepOrgForm);
	 	
	 	return mapping.findForward("proxy_set");
  }
  /**
   * 取得查询条件url  
   * @param mToRepOrgForm 查询条件
   * @return
   */
  public String getTerm(MToRepOrgForm mToRepOrgForm)
  {
	   String term="";

	   String orgName = mToRepOrgForm.getOrgName();

	   
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
