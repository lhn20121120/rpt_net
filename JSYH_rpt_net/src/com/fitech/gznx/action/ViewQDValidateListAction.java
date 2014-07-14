package com.fitech.gznx.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.QDValidateForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFQDValidateFormulaDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;

public class ViewQDValidateListAction extends Action {
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {

		FitechMessages messages=new FitechMessages();
		
		QDValidateForm qdValidateForm=(QDValidateForm) form;
		RequestUtils.populate(qdValidateForm,request);
		
		String curPage=null;
		
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
		
		List resList=AFQDValidateFormulaDelegate.getValidateListById(qdValidateForm.getTemplateId(),qdValidateForm.getVersionId());
		

		if(resList!=null){
			request.setAttribute(Config.RECORDS,resList);
		}
			

		AfTemplate aftemplate = AFTemplateDelegate.getTemplate(qdValidateForm.getTemplateId(),qdValidateForm.getVersionId());
		if(aftemplate!=null){			
			// 清单式报表
			if(aftemplate.getReportStyle() != null &&
					com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate.getReportStyle()))){
				qdValidateForm.setReportStyle(Config.REPORT_STYLE_QD);
			}else{
				qdValidateForm.setReportStyle(Config.REPORT_STYLE_DD);
			}
			qdValidateForm.setReportName(aftemplate.getTemplateName());
		}
		
		request.setAttribute("ObjForm",qdValidateForm);

		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages); 
		request.setAttribute("CurPage",curPage);
		
		return mapping.findForward("index");
	}

}
