package com.fitech.net.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.service.AFOrgDelegate;

/**
 * 在线填报
 * 
 * @author Yao（修改）
 * 
 */
public class BanchOffLineNXReportAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);
		
		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);
		
		String date = request.getParameter("date");
		if(date!=null&&!date.equals("")){
			String year = DateUtil.getYear(date);
			String month = DateUtil.getMonth(date);
			String day = DateUtil.getDay(date);
			request.setAttribute("date", date);
			request.setAttribute("year", year	);
			request.setAttribute("month", month	);
			request.setAttribute("day", day	);
		}
		String orgId = request.getParameter("orgId");
		if(orgId!=null&&!orgId.equals("")){
			request.setAttribute("orgId", orgId	);
			request.setAttribute("orgName", AFOrgDelegate.getOrgInfo(orgId).getOrgName()	);
			
		}
		
		
		

		return mapping.findForward("viewOffLine");

	}
}