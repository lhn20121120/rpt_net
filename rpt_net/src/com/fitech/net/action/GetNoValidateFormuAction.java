package com.fitech.net.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsDataValidateInfoDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsQDDataValidateInfoDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.DataValidateInfoForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.service.AFDataValidateInfoDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.StrutsAFCellDelegate;
/**
 * 得到校验不通过的表达式
 * 
 */
public class GetNoValidateFormuAction extends DispatchAction {
	private FitechException log=new FitechException(GetNoValidateFormuAction.class);
	
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
		
		System.out.println("GetNoValidateFormuAction");
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		
		int reportStyle=0;
		HttpSession session = request.getSession();
		String reportFlg = "";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		try{
			String _repInId = request.getParameter("repInId");
			Integer repInId = new Integer(_repInId);    //实际数据报表ID
			ReportInForm reportInForm= null;
			List resList=null;
			String childRepId = "";    //子报表ID
			String versionId=""; 
			if(repInId!=null){
				if(reportFlg.equals("1")){
					reportInForm=StrutsReportInDelegate.getReportIn(repInId);
					if(reportInForm!=null){
						childRepId=reportInForm.getChildRepId();    //子报表ID
						versionId=reportInForm.getVersionId(); 
						reportStyle = StrutsMChildReportDelegate.getReportStyle(childRepId,versionId);
						if(reportStyle==Config.REPORT_STYLE_DD.intValue()){
							resList = StrutsDataValidateInfoDelegate.find(repInId);
							
						}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){
							resList = StrutsQDDataValidateInfoDelegate.find(repInId);
						}
					}
				}else if(reportFlg.equals("2")){
					reportInForm = AFReportDelegate.getReportIn(repInId);
					if(reportInForm!=null){
						childRepId=reportInForm.getChildRepId();    //子报表ID
						versionId=reportInForm.getVersionId(); 
						reportStyle = AFReportDelegate.getReportStyle(repInId.longValue());
						if(reportStyle==Config.REPORT_STYLE_DD.intValue()){
							resList = AFDataValidateInfoDelegate.find(repInId);
						}else if(reportStyle==Config.REPORT_STYLE_QD.intValue()){
							resList = StrutsQDDataValidateInfoDelegate.find(repInId);
						}
					}
					 
				}
				if(resList!=null && resList.size()>0){
					request.setAttribute(Config.RECORDS,resList);
					String formuStr= "";
					List fmuList = new ArrayList();
						for (int i = 0; i < resList.size(); i++) {
							DataValidateInfoForm dvi = (DataValidateInfoForm)resList.get(i);
							if(reportFlg.equals("1")){
								formuStr+=(i==0?"":"##")+dvi.getCellFormu();
							}else{
								String fmu = dvi.getCellFormu();
								fmu = fmu.substring(fmu.indexOf("[")+1,fmu.indexOf("]"));
								if(fmuList.contains(fmu)){
									continue;
								}
								fmuList.add(fmu);
								System.out.println(fmu);
								String colNum = fmu.substring(0,fmu.indexOf("."));
								String cellPid = fmu.substring(fmu.indexOf(".")+1);
								formuStr+=(i==0?"":"##")+StrutsAFCellDelegate.getCellName(cellPid,colNum);
							}
						}
					System.out.println("fromuStr="+formuStr);
					ServletOutputStream out = response.getOutputStream();
					out.write(formuStr.getBytes());
					out.close();
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
		return null;
	}
}
