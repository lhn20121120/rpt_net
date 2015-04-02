package com.cbrc.org.action;

import java.io.IOException;
import java.util.HashMap;
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

import com.cbrc.org.adapter.StrutsMOrgClDelegate;
import com.cbrc.org.form.MOrgClForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/**
 * Displays a MOrgCl. We'll first try to look for a MOrgCl
 * object on the request attribute (which should be set if an insert, 
 * update or select action forwarded to us). If this attribute is not set,
 * we're probably called directly from a page, and we'll look up
 * the person by its id which should be passed as a request parameter.
 *
 * @author 唐磊
 *
 * @struts.action
 *    path="/struts/viewMOrgCl"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMOrgCl.jsp"
 *    redirect="false"
 *

 */
public final class ViewMOrgClAction extends Action {
	private static FitechException log = new FitechException(
			ViewMOrgClAction.class);

	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);
        
        HttpSession session=request.getSession();
		// 是否有Request
		MOrgClForm mOrgClForm = new MOrgClForm();
		RequestUtils.populate(mOrgClForm,request);

		List resList = null;

		try {
			resList = StrutsMOrgClDelegate.findAll();

            if(resList!=null && resList.size()>0)
            {
                
                if(session.getAttribute("SelectedOrgIds")!=null)
                {
                    HashMap hMap=(HashMap)session.getAttribute("SelectedOrgIds");
                    for(int i=0;i<resList.size();i++){
                        MOrgClForm item =(MOrgClForm)resList.get(i);
                        /*// System.out.println("mOrgClForm.getOrgClsId():" + mOrgClForm.getOrgClsId());*/
                        if(hMap.containsValue(item.getOrgClsId())==true){
                            item.setSelAll(new Integer(1));
                        }
                    }
                }
                
            }

		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(resources.getMessage("tbfw.get.orgs.failed"));
		}
		//移除request或session范围内的属性
		FitechUtil.removeAttribute(mapping,request);

		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		//如果StrutsMOrgClDelegate类中返回的reslist对象不为空并且对象的大小大于0，
		//则返回一个包含reslist集合的request对象
		if (resList != null && resList.size() > 0){
			request.setAttribute(Config.RECORDS,resList);
		}
		
		request.setAttribute("ReportName", mOrgClForm.getReportName());
		request.setAttribute("ChildRepId",mOrgClForm.getChildRepId());
	    request.setAttribute("VersionId", mOrgClForm.getVersionId());
	    request.setAttribute("ReportStyle",mOrgClForm.getReportStyle());
	   		//返回到页面view     
		return mapping.findForward("view");
	}

}