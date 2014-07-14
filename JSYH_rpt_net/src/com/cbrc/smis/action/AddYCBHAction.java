package com.cbrc.smis.action;

import java.io.IOException;
import java.util.Locale;

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

import com.cbrc.smis.form.AbnormityChangeForm;
import com.cbrc.smis.util.FitechException;

/**
 * �����쳣�仯�趨�ĳ�ʼ��
 * 
 * @author rds
 * @serialData 2005-12-11
 */
public class AddYCBHAction extends Action {
	private FitechException log=new FitechException(AddYCBHAction.class);
	
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
		
		HttpSession session=request.getSession();
		
		AbnormityChangeForm abnormityChangeForm=new AbnormityChangeForm();
		RequestUtils.populate(abnormityChangeForm,request);
		
		/**��Session�����������б���Ϣ**/
		if(session.getAttribute("SelectedOrgIds")==null) session.setAttribute("SelectedOrgIds",null);
		
		request.setAttribute("ObjForm",abnormityChangeForm);
		
		return mapping.getInputForward();
	}
}
