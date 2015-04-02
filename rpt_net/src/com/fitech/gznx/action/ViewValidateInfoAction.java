package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsQDDataValidateInfoDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.service.AFDataValidateInfoDelegate;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;

/**
 * �鿴У����Ϣ
 * @author Dennis Yee
 *
 */
public class ViewValidateInfoAction extends Action{
	
	private FitechException log=new FitechException(ViewValidateInfoAction.class);
	
	/**
	 * ��һ��ʹ��jdbc���� ������oracle�﷨ ����Ҫ�޸� ���Ը� 2011-12-27
	 * Ӱ�����AfReport AfTemplate AfDatavalidateinfo AfOrg MCurr
	 * Ӱ���qd_data_validate_info
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
		FitechMessages messages = new FitechMessages();
		
		int reportStyle=0;
		
		try{
			Integer repInId = Integer.valueOf(request.getParameter("repInId")); //ʵ�����ݱ���ID
			
			if(repInId!=null){
				/**
				 * ��ʹ��hibernate ���Ը� 2011-12-27
				 * Ӱ�����AfReport*/
				ReportInForm reportInForm = AFReportDelegate.getReportIn(repInId);
				
				if(reportInForm!=null){
					
					//String childRepId=reportInForm.getChildRepId();    //�ӱ���ID
					//String versionId=reportInForm.getVersionId();      //�汾��
					/**��ʹ��hibernate ���Ը� 2011-12-21
					 * Ӱ�����AfReport  AfTemplate**/
					reportStyle = AFReportDelegate.getReportStyle(repInId.longValue());
					
					List resList=null;
					
					if(reportStyle==Config.REPORT_STYLE_DD.intValue()){
						/**��ʹ��hibernate ���Ը� 2011-12-27
						 * Ӱ�����AfDatavalidateinfo**/
						resList = AFDataValidateInfoDelegate.find(repInId);
						
					}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){
						/**jdbc���� ������oracle�﷨ ���ܲ���Ҫ�޸� ���Ը� 2011-12-27
						 * Ӱ���qd_data_validate_info*/
						resList = StrutsQDDataValidateInfoDelegate.find(repInId);
						
					}
					
					/**��������**/
					request.setAttribute("ReportName",reportInForm.getRepName());
					/**�����**/
					/**��ʹ��hibernate ���Ը� 2011-12-21
					 * Ӱ�����AfOrg*/
					request.setAttribute("ReportOrg",AFOrgDelegate.getOrgInfo(reportInForm.getOrgId()).getOrgName());
					/**��������**/
					request.setAttribute("ReportDate",reportInForm.getYear()
							+"-"+ reportInForm.getTerm()+"-"+reportInForm.getDay());
					/**����**/
					/**��ʹ��hibernate ���Ը� 2011-12-21
					 * Ӱ�����MCurr**/
					request.setAttribute("currName",StrutsMCurrDelegate.getISMCurr(
							reportInForm.getCurId().toString()).getCurName());
					
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
			
		if(reportStyle==Config.REPORT_STYLE_DD.intValue()){
			// System.out.println(mapping.findForward("dd_view").getPath());
			return mapping.findForward("dd_view");
		}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){
			// System.out.println(mapping.findForward("qd_view").getPath());
			return mapping.findForward("qd_view");
		}else{
			// System.out.println("error!");
			return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);
		}

		
	}
	
}
