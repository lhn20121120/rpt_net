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
	 * ��ʹ��hibernate ���Ը� 2011-12-26
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
		// ȡ��request��Χ�ڵ�����������������ReportInForm��
		AFReportForm afReportForm = (AFReportForm) form;
		RequestUtils.populate(afReportForm, request);

		boolean flag = true;
		String messagesStr = null;
		// �����ѯ�����ı�����
		List allowReportList = null;
		int curPage = 1;

		String type = request.getParameter("type");

		try {
			
			//ѡ�в���
			if (type != null && type.equals("select") && request.getParameter("repInIds") != null) {
				String[] repInIds = request.getParameter("repInIds").split(",");
				Arrays.asList(repInIds);
				flag = AFPbocReportDelegate.buLing(Arrays.asList(repInIds), reportFlg ,request);
			}
			//����ҳ���ѯ�������������з��������ı�������һ�����㡣
			else if (type != null && type.equals("all")) {
				allowReportList = new ArrayList();
		        /**
		          * ȡ�õ�ǰ�û���Ȩ����Ϣ
		          */   
				HttpSession session = request.getSession();
				Operator operator = null;
				if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
					operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          
				/** ����ѡ�б�־ **/
				
				if (afReportForm == null) 
					afReportForm = new AFReportForm();
				if (afReportForm.getDate() == null || afReportForm.getDate().equals("")) {
					//�����������
					String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
					afReportForm.setDate(yestoday);
				}
				//ȡ��ģ������
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
			messagesStr = "ϵͳæ�����Ժ�����...��";
		}
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");
		out.println("<response><result>" + flag + "</result></response>");
		out.close();
		return null;
	}
}
