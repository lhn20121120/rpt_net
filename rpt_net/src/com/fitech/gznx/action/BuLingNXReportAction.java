package com.fitech.gznx.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFPbocReportDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.net.template.action.UpLoadOnLineReportAction;

import edu.emory.mathcs.backport.java.util.Arrays;

public class BuLingNXReportAction extends Action {
	private static FitechException log = new FitechException(UpLoadOnLineReportAction.class);

	/***
	 * 已使用hibernate 卞以刚 2011-12-26
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		String reportFlg = "0";
		HttpSession hsession = request.getSession();
		if (hsession.getAttribute(Config.REPORT_SESSION_FLG) != null) {
			reportFlg = (String) hsession.getAttribute(Config.REPORT_SESSION_FLG);
		}
		// 取得request范围内的请求参数，并存放在ReportInForm内
		AFReportForm afReportForm = (AFReportForm) form;
		RequestUtils.populate(afReportForm, request);

		boolean flag = true;
		String messagesStr = null;
		// 满足查询条件的报表集合
		List allowReportList = null;
		int curPage = 1;

		String type = request.getParameter("type");

		try {
			
			//选中补零
			if (type != null && type.equals("select") && request.getParameter("repInIds") != null) {
				String[] repInIds = request.getParameter("repInIds").split(",");
				Arrays.asList(repInIds);
				flag = AFPbocReportDelegate.buLing(Arrays.asList(repInIds), reportFlg ,request);
			}
			//根据页面查询条件检索出所有符合条件的报表，将其一键补零。
			else if (type != null && type.equals("all")) {
				allowReportList = new ArrayList();
		        /**
		          * 取得当前用户的权限信息
		          */   
				HttpSession session = request.getSession();
				Operator operator = null;
				if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
					operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          
				/** 报表选中标志 **/
				
				if (afReportForm == null) 
					afReportForm = new AFReportForm();
				if (afReportForm.getDate() == null || afReportForm.getDate().equals("")) {
					//获得昨天日期
					String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
					afReportForm.setDate(yestoday);
				}
				//取得模板类型
				if(session.getAttribute(Config.REPORT_SESSION_FLG)!=null)
					afReportForm.setTemplateType(session.getAttribute(Config.REPORT_SESSION_FLG).toString());
				
				allowReportList = AFReportDelegate.selectNeedReports(afReportForm, operator ,curPage).getList();
				List repInIds = new ArrayList();
				for (int i = 0; i < allowReportList.size(); i++) {
					Aditing aditing = (Aditing) allowReportList.get(i) ;
					repInIds.add(aditing.getRepInId()+"");
					
				}
				
				flag = AFPbocReportDelegate.buLing(repInIds, reportFlg ,request);
				System.out.println("bling shifou chenggong "+flag);
			}
		} catch (Exception ex) {
			log.printStackTrace(ex);
			messagesStr = "系统忙，请稍后再试...！";
		}
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");
		out.println("<response><result>" + flag + "</result></response>");
		out.close();
		return null;
	}
}
