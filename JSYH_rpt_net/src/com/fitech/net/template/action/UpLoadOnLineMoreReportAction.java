package com.fitech.net.template.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.util.FitechException;

/**
 * 数据报送
 * 
 * 
 */
public class UpLoadOnLineMoreReportAction extends Action{
	private static FitechException log = new FitechException(UpLoadOnLineMoreReportAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{		
		
		//取得request范围内的请求参数，并存放在ReportInForm内
		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);
		boolean flag = true;
		String messageBL = "";
		String messageBJ = "";
		String messagesStr = "";		
	
		try{
			if(request.getParameter("repInIds") != null){
				String[] repInIds = request.getParameter("repInIds").split(",");
				ReportIn reportIn = null;
				
				if(repInIds != null && repInIds.length > 0){
					for(int i=0;i<repInIds.length;i++){
						reportIn =StrutsReportInDelegate.getReportIn2(new Integer(repInIds[i]));
						if (reportIn != null ){
							//得到表内校验的结果值
							Short BLValidateFlag = reportIn.getTblInnerValidateFlag();
							//得到表间校验的结果值
							Short BJValidateFlag = reportIn.getTblOuterValidateFlag();
							if( com.cbrc.smis.common.Config.SYS_BN_VALIDATE.equals(new Integer(1))){
								if(BLValidateFlag == null || !BLValidateFlag.equals(new Short("1"))){
									//messages.add("表内校验不通过，不能上报该报表！");
									messageBL="BN_VALIDATE_NOTPASS";
									//request.setAttribute(Config.MESSAGES, messages);
									flag = false;  
								//	return new ActionForward(path.toString());
								}
							}
							if(flag &&  com.cbrc.smis.common.Config.SYS_BJ_VALIDATE.equals(new Integer(1))){
								if(BJValidateFlag == null || !BJValidateFlag.equals(new Short("1"))){
								//	messages.add("表间校验不通过，不能上报该报表！");
								//	request.setAttribute(Config.MESSAGES, messages);
									messageBJ="BJ_VALIDATE_NOTPASS";
									flag = false;
								//	return new ActionForward(path.toString());
								}
							}
							
						}
						
						
						if(flag){
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							String date = format.format(calendar.getTime());		
							StrutsReportInDelegate.updateReportIn(reportIn, date);							
						}
					}
					if(messageBJ != null && messageBJ.length() > 0) messagesStr+=(messagesStr==""?"":",")+messageBJ;
					if(messageBL != null && messageBL.length() > 0) messagesStr+=(messagesStr==""?"":",")+messageBL;
				}				
			}
		}catch (Exception ex){
			log.printStackTrace(ex);
			messagesStr="系统忙，请稍后再试...！";
		}
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");
		String result = "true";
		if(messagesStr != null && messagesStr.length() > 0) result = messagesStr;
		else result = "true";
		
		out.println("<response><result>" + result + "</result></response>");
		out.close();
		return null;
	}
}
