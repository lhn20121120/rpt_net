package com.fitech.gznx.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;
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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.PlacardForm;
import com.fitech.gznx.service.StrutsPlacardDelegate;
import com.gather.db.helper.FitechException;


public class ViewPlacardAction extends Action {



	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();

		PlacardForm placardForm = (PlacardForm) form;
		RequestUtils.populate(placardForm, request);
		
		request.setAttribute("queryPlacardForm",placardForm);

		List list = null;

		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_NAME);
		else 
			operator = new Operator();
		if(!StringUtil.isEmpty(placardForm.getStartDate()) && !StringUtil.isEmpty(placardForm.getEndDate())){
			try {
				Date startdate = DateUtil.getFormatDate(placardForm.getStartDate(),"yyyy-mm-dd");
				Date enddate = DateUtil.getFormatDate(placardForm.getEndDate(),"yyyy-mm-dd");
				if(DateUtil.compareDate(startdate,enddate) == 1){
					placardForm.setStartDate("");
					placardForm.setEndDate("");
					messages.add("结束时间应该大于起始时间");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			placardForm.setPublicUserId(String.valueOf(operator.getOperatorId()));
			list = StrutsPlacardDelegate.select(placardForm);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			messages.add(FitechResource.getMessage(locale, resources, "select.fail", "placard.info"));
		}
		// 移除request或session范围内的属性
		FitechUtil.removeAttribute(mapping, request);

		String queryTerm = getQueryTerm(placardForm);
		if(queryTerm!=null && !queryTerm.equals(""))
			request.setAttribute(Config.QUERY_TERM,queryTerm);
		
		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		if (list != null && list.size() != 0)
			request.setAttribute(Config.RECORDS, list);
		return mapping.findForward("view");
	}
	
	/**
	 * 获得查询条件
	 * 
	 * @author db2admin
	 * @date 2007-3-24
	 * @param placardForm
	 * @return
	 * 
	 */
	public String getQueryTerm(PlacardForm placardForm)
	{
		String term = "";
		String title = placardForm.getTitle();
		String startDate = placardForm.getStartDate();
		String endDate = placardForm.getEndDate();

		if (title != null && !title.equals(""))
		{
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "title=" + title;
		}
		if (startDate != null && !startDate.equals(""))
		{
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "startDate=" + startDate;
		}
		if (endDate != null && !endDate.equals(""))
		{
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "endDate=" + endDate;
		}
		return term;
	}

}
