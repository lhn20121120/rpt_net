/*
 * Created on 2006-5-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.webedit.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.excel.CreateExcel;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.fitech.net.common.CommMethod;

/**
 * 数据调整(手工调平)
 * 
 * @author wh(修改) 2007-9-25
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class WebEditAction extends Action
{
	/**
	 * execute action.
	 * 
	 * @param mapping
	 *            Action mapping.
	 * @param form
	 *            Action form.
	 * @param request
	 *            HTTP request.
	 * @param response
	 *            HTTP response.
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{

		HttpSession session = request.getSession();

		String curPage = request.getParameter("curPage");
		String year = request.getParameter("year");
		String term = request.getParameter("term");
	
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

		String orgId = operator.getOrgId();

		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Expires", "Thu,01 Jan 1970 00:00:01 GMT");

		String fileName = "";

		Integer repInId = request.getParameter("repInId") != null && !request.getParameter("repInId").equals("") ? Integer.valueOf(request
				.getParameter("repInId")) : new Integer(-1);
		ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(repInId);

		if (reportInForm != null)
		{
			CreateExcel createExcel = new CreateExcel(repInId);
			createExcel.createExcel(year,term);
			fileName = reportInForm.getChildRepId() + "_" + reportInForm.getVersionId()+"_"+reportInForm.getDataRangeId()+"_"+reportInForm.getCurId()+ ".xls";
		}

		String filePath = com.fitech.net.config.Config.getCollectExcelFolder() + File.separator  + year + "_" + term + File.separator + operator.getOrgId();
		File orgFile = new File(filePath);
		if (!orgFile.exists())
			orgFile.mkdirs();

		String hrefUrl = CommMethod.getAbsolutePath(request, "viewonlineupdate.do?year=" + year + "&term=" + term + "&curPage=" + curPage + "&repInId"
				+ repInId);
		request.setAttribute("hrefUrl", hrefUrl);
		String reportUrl = CommMethod.getAbsolutePath(request, com.fitech.net.config.Config.COLLECT_EXCEL_ALL_PATH +"/"    + year + "_" + term + "/" + operator.getOrgId() + "/" + fileName);
		request.setAttribute("reportUrl", reportUrl);
		request.setAttribute("reportName", fileName);
		// request.setAttribute("saveUrl", CommMethod.getAbsolutePath(request,
		// "webEditor/uploadDoc.jsp?repInId="+repInId));
		// request.setAttribute("saveUrl", CommMethod.getAbsolutePath(request,
		// "orgtogether_net/uploadDoc.jsp?repInId="+repInId+"&orgId="+orgId+"&childRepId="+reportInForm.getChildRepId()+"&versionId="+reportInForm.getVersionId()));

		String requestURL = "repInId=" + repInId + "&orgId=" + orgId + "&year=" + year + "&term=" + term ;
		// 手工调平按钮提交URL
		request.setAttribute("saveUrl", CommMethod.getAbsolutePath(request, "orgtogether_net/uploadDoc.jsp?" + requestURL));
		CommMethod.buidPageInfo(request);
		return mapping.findForward("success");
	}
}
