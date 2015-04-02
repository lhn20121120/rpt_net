package com.fitech.net.template.action;

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

import com.cbrc.smis.action.ViewMChildReportAction;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.common.CommMethod;

public class ViewTemplateNetAction extends Action {
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
	        

	        MessageResources resources = getResources(request);
	        FitechMessages messages = new FitechMessages();
	        //取得request范围内的请求参数，并存放在logInForm内
	        MChildReportForm mChildReportForm = (MChildReportForm) form;
	        RequestUtils.populate(mChildReportForm, request);
	         
	        int recordCount = 0; // 记录总数
	 		int offset = 0; // 偏移量
	 		int limit = 10; // 每页显示的记录条数
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
	         
	         try{
	        	 //        	取得记录总数
	             recordCount = StrutsMChildReportDelegate.getRecordCount2(mChildReportForm);
	             
	             if(recordCount > 0)
	            	 list = StrutsMChildReportDelegate.select2(mChildReportForm,offset,limit);  
	         }catch(Exception ex){
	        	 log.printStackTrace(ex);
	             messages.add(resources.getMessage("select.template.failed"));  
	         }
	         
	         //移除request或session范围内的属性
	         FitechUtil.removeAttribute(mapping,request);
	         //把ApartPage对象存放在request范围内
	         aPage.setTerm(this.getTerm(mChildReportForm));
	 	 	 aPage.setCount(recordCount);
	         request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
	         
	         if(messages.getMessages() != null && messages.getMessages().size() > 0)
	           request.setAttribute(Config.MESSAGES,messages);
	         if(list!=null && list.size()!=0)
	           request.setAttribute(Config.RECORDS,list);
	         //将查询条件存放在request范围内
	         request.setAttribute("form",mChildReportForm);
	         String obtianUrl =  CommMethod.getAbsolutePath(request,  "templateConfigurePre.do");
	         request.setAttribute("obtianUrl", obtianUrl);
	         return mapping.findForward("init");
	    }
	    public String getTerm(MChildReportForm mChildReportForm){
			String term="";
			String childRepId = mChildReportForm.getChildRepId();
			String versionId = mChildReportForm.getVersionId();
			
			if(childRepId != null && !childRepId.equals("")){
				term += (term.indexOf("") >= 0 ? "" : "&");
				term += "org_id=" + childRepId;
			}
			if(versionId != null && !versionId.equals("")){
				term += (term.indexOf("") >= 0 ? "" : "&");
				term += "versionId=" + versionId;
			}
		   return term;
		}
	 }
