package com.fitech.net.action;

import java.io.IOException;
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

import com.cbrc.org.util.MOrgUtil;
import com.cbrc.smis.adapter.Procedure;
import com.cbrc.smis.adapter.StrutsColAbnormityChangeInfoDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

/**
 * �鿴ʵ�����ݱ�����쳣�仯��ϸ
 * 
 * @author rds
 * @date 2006-01-2
 */
public class ViewAnalyYCBHDetailAction extends Action {
	private FitechException log=new FitechException(ViewAnalyYCBHDetailAction.class);
	
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
		
		FitechMessages messages = new FitechMessages();
		
		int reportStyle=0;
		
		try{
			String _repInId=request.getParameter("repInId");      //ʵ�����ݱ���ID
			Integer repInId=Integer.valueOf(_repInId);
			
			if(repInId!=null){
				/**��ȡ�쳣�仯��Ϣ֮ǰ��ִ���쳣�仯����洢����**/
				Procedure.runYCBH(repInId);
				
				ReportInForm reportInForm=StrutsReportInDelegate.getReportIn(repInId);
				if(reportInForm!=null){
					String childRepId=reportInForm.getChildRepId();    //�ӱ���ID
					String versionId=reportInForm.getVersionId();      //�汾��
					String orgId=reportInForm.getOrgId();              //����ID
					
					reportStyle=StrutsMChildReportDelegate.getReportStyle(childRepId,versionId);
					List resList=null;
					
					if(reportStyle==Config.REPORT_STYLE_DD.intValue()){
						resList=StrutsReportInInfoDelegate.find(repInId,childRepId,versionId,orgId);
					}					
					/**��������**/
					request.setAttribute("ReportName",reportInForm.getRepName());
					/**�����**/
					request.setAttribute("ReportOrg",MOrgUtil.getOrgName(reportInForm.getOrgId()));
					/**��������**/
					request.setAttribute("ReportDate",FitechUtil.getDateString(reportInForm.getReportDate()));
					
					if(resList!=null && resList.size()>0) request.setAttribute(Config.RECORDS,resList);
				}else{
					messages.add(FitechResource.getMessage(locale,resources,"report.read.info.error"));
				}
			}else{
				messages.add(FitechResource.getMessage(locale,resources,"report.read.info.error"));
			}
			
		}catch(Exception e){
			reportStyle=0;
			log.printStackTrace(e);
		}		
		
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
			return mapping.findForward("view");

	}
}
