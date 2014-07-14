package com.cbrc.smis.action;

import java.io.IOException;
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

import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.util.FitechException;
/**
 * 从机构选择页面返回异常变化设定页面
 * 
 * @author rds
 * @serialData 2005-12-11 19:48
 */
public class Mod_ReturnTBFWAction extends Action {
	private FitechException log=new FitechException(AddYCBHInitAction.class);
	
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

		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		
		MRepRangeForm mRepRangeForm=new MRepRangeForm();
		RequestUtils.populate(mRepRangeForm,request);
		
        request.setAttribute("ReportName", mRepRangeForm.getReportName());
        request.setAttribute("ChildRepId",mRepRangeForm.getChildRepId());
        request.setAttribute("VersionId", mRepRangeForm.getVersionId());
        request.setAttribute("ReportStyle",mRepRangeForm.getReportStyle());
		
		return mapping.findForward("goto");
	}
}
