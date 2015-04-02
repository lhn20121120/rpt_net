package com.fitech.net.action;

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

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsCollectTypeDelegate;
import com.fitech.net.form.CollectTypeForm;

/**
 * @ 汇总方式查询Action
 * @ author jcm
 *
 */
public final class ViewCollectTypeAction extends Action {
	private FitechException log=new FitechException(ViewCollectTypeAction.class);
	
	
   /**
    * @param result 查询返回标志,如果成功返回true,否则返回false
    * @param CollectTypeForm 
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
		MessageResources resources = getResources(request);
	   
		// 是否有Request
		CollectTypeForm collectTypeForm = (CollectTypeForm)form ;	   
		RequestUtils.populate(collectTypeForm, request);
			    
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
			if(collectTypeForm != null && collectTypeForm.getOrgId() != null){
				if(collectTypeForm.getOrgId().equals("0")) collectTypeForm.setOrgId(null);
			}
	   		//取得记录总数
	   		recordCount = StrutsCollectTypeDelegate.select(collectTypeForm);
	   			   		
	   		//显示分页后的记录
	   		if(recordCount > 0)
		   	    resList = StrutsCollectTypeDelegate.select(collectTypeForm,offset,limit); 
	   		
	   	}catch (Exception e){
			log.printStackTrace(e);
			messages.add(resources.getMessage("select.collectType.failed"));		
		}
		//移除request或session范围内的属性
		//FitechUtil.removeAttribute(mapping,request);
		//把ApartPage对象存放在request范围内
	 	aPage.setTerm(this.getTerm(collectTypeForm));	 	
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
	 	
	 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	 	 
	 	 if(resList!=null && resList.size()>0)
	 		 request.setAttribute(Config.RECORDS,resList); 
	 	 
	 	 return mapping.findForward("view");
	}

   
	public String getTerm(CollectTypeForm collectTypeForm){
		String term="";
		String orgId = collectTypeForm.getOrgId();
		String collectName = collectTypeForm.getCollectName();
		
		if(orgId != null){
			term += (term.indexOf("") >= 0 ? "" : "&");
			term += "orgId=" + orgId;
		}
		if(collectName != null && !collectName.equals("")){
			term += (term.indexOf("") >= 0 ? "" : "&");
			term += "collectName=" + collectName;
		}
	   return term;
	}
}