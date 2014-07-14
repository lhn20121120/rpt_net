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
import com.fitech.gznx.form.PlacardUserViewForm;
import com.fitech.gznx.service.StrutsPlacardUserViewDelegate;



public class ViewPlacardUserViewAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();

		PlacardUserViewForm placardUserViewForm = (PlacardUserViewForm) form;
		RequestUtils.populate(placardUserViewForm, request);
		request.setAttribute("queryPlacardForm",placardUserViewForm);

		List list = null;

		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_NAME);
		else 
			operator = new Operator();
		if(!StringUtil.isEmpty(placardUserViewForm.getStartDate()) && !StringUtil.isEmpty(placardUserViewForm.getEndDate())){
			try {
				Date startdate = DateUtil.getFormatDate(placardUserViewForm.getStartDate(),"yyyy-mm-dd");
				Date enddate = DateUtil.getFormatDate(placardUserViewForm.getEndDate(),"yyyy-mm-dd");
				if(DateUtil.compareDate(startdate,enddate) == 1){
					placardUserViewForm.setStartDate("");
					placardUserViewForm.setEndDate("");
					messages.add("结束时间应该大于起始时间");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			placardUserViewForm.setUserId(String.valueOf(operator.getOperatorId()));
			list = StrutsPlacardUserViewDelegate.select(placardUserViewForm);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			messages.add(FitechResource.getMessage(locale, resources, "select.fail", "placard.info"));
		}
		// 移除request或session范围内的属性
		FitechUtil.removeAttribute(mapping, request);

		String queryTerm = getQueryTerm(placardUserViewForm);
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
	 * @param PlacardUserViewForm
	 * @return
	 * 
	 */
	public String getQueryTerm(PlacardUserViewForm PlacardUserViewForm)
	{
		String term = "";
		String title = PlacardUserViewForm.getTitle();
		String startDate = PlacardUserViewForm.getStartDate();
		String endDate = PlacardUserViewForm.getEndDate();

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
		if (title != null && !title.equals(""))
		{
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "endDate=" + endDate;
		}
		return term;
	}

}
