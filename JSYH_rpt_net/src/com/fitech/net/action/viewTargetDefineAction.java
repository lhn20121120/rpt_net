package com.fitech.net.action;

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
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsTargetDefineDelegate;
import com.fitech.net.form.TargetDefineForm;

/**
 * @Action
 * @authorxsf
 *
 */
public final class viewTargetDefineAction extends Action {
	private FitechException log=new FitechException(viewTargetDefineAction.class);
	
   /**
    * @param result 查询返回标志,如果成功返回true,否则返回false
    * @param 
    * @param request 
    * @exception Exception 有异常捕捉并抛出
    */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
	)throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		HttpSession session=request.getSession();
		Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_NAME);
		String orgId=operator.getOrgId();

		// 是否有Request
		TargetDefineForm targetDefineForm = (TargetDefineForm)form ;	   
		RequestUtils.populate(targetDefineForm, request);
		
		if(targetDefineForm.getBusinessId() != null && targetDefineForm.getBusinessId().equals("-1"))
			targetDefineForm.setBusinessId(null);
		if(targetDefineForm.getNormalId() != null && targetDefineForm.getNormalId().equals("-1"))
			targetDefineForm.setNormalId(null);
			    
		int recordCount = 0; // 记录总数
		int offset = 0; // 偏移量
		int limit = 10; // 每页显示的记录条数

		List resList=null;
	   
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

		try{
	   		//取得记录总数
	   		recordCount = StrutsTargetDefineDelegate.getRecordCount(targetDefineForm);
	   			   		
	   		//显示分页后的记录
	   		if(recordCount > 0)
		   	    resList = StrutsTargetDefineDelegate.select(targetDefineForm,offset,limit,orgId); 
	   	}catch (Exception e){
			log.printStackTrace(e);
			messages.add("查询失败");		
		}
		//移除request或session范围内的属性
		//FitechUtil.removeAttribute(mapping,request);
		//把ApartPage对象存放在request范围内
	 	aPage.setTerm(this.getTerm(targetDefineForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
	 	
	 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	 	 
	 	 if(resList!=null && resList.size()>0)
	 		 request.setAttribute(Config.RECORDS,resList); 
	 	 
	 	 return mapping.findForward("view");
	}
   
	public String getTerm(TargetDefineForm targetDefineForm){
		String term="";
		if(targetDefineForm!=null && targetDefineForm.getDefineName()!=null ){
			String defineName = targetDefineForm.getDefineName();
			String businessId = targetDefineForm.getBusinessId();
			String normalId = targetDefineForm.getNormalId();
			if(defineName != null && !defineName.equals("")){
				term += (term.indexOf("") >= 0 ? "&" : "");
				term += "defineName=" + defineName.toString();
			}
			if(businessId != null && !businessId.equals("")){
				term += (term.indexOf("") >= 0 ? "&" : "");
				term += "businessId=" + businessId;
			}
			if(normalId != null && !normalId.equals("")){
				term += (term.indexOf("") >=0 ? "&" : "");
				term += "normalId=" + normalId;
			}
		}
	   return term;
	}
}