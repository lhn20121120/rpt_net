package com.fitech.net.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.common.CommMethod;

/**
 * 在线填报
 * 
 * @author Yao（修改）
 * 
 */
public class OnLineReportAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);

		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);
		//取得相关基本信息
		String childRepId = reportInForm.getChildRepId();
		String versionId = reportInForm.getVersionId();
		String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : "1";
		Integer year = reportInForm.getYear();
		Integer term = reportInForm.getTerm();
		Integer curId = reportInForm.getCurId();
		String orgId = reportInForm.getOrgId();
		Integer dataRangeId = reportInForm.getDataRangeId();
		boolean result = false;
		
		try
		{
			if (childRepId != null && versionId != null && orgId != null && dataRangeId != null && curId != null
					&& year != null && term != null)
			{

				String path = "";
//				String fileName = childRepId + "_" + versionId + "_" + dataRangeId + "_" + curId + ".xls";
				//ETL文件存放路径   wh改,
			/*	String releaseDir = com.fitech.net.config.Config.getReleaseTemplatePath() + File.separator + year
						+ "_" + term + File.separator + orgId;	
				
				File file = new File(releaseDir + File.separator + fileName);*/
				
//				File file =  null;
				/**先看有没有ETL数据，若没有下载空模板，若有则将ETL数据生成一份数据报表下载**/
				String  excelFilePath = new StrutsMRepRangeDelegate().getExcelFile(childRepId,versionId,orgId,year, term,dataRangeId,curId, new Integer(-2));
				if(excelFilePath != null && !excelFilePath.equals("")){
					 path = excelFilePath;	
				}else{
					path = com.fitech.net.config.Config.getTemplateFolderRealPath()+File.separator+childRepId + "_" + versionId+ ".xls";
				}
				
				// 如果ETL目录下存在报表则直接打开该ETL取数后的文件
			/*	if (file.exists())
					path = releaseDir + File.separator + fileName;
				// 否则打开模版文件
				else
					path = com.fitech.net.config.Config.getTemplateFolderRealPath()+File.separator+childRepId + "_" + versionId
					+ ".xls";
*/
				/** 把文件复制到临时文件夹中* */
				String newFileName = String.valueOf(System.currentTimeMillis())+".xls";
				if(FitechUtil.copyFile(path, Config.TEMP_DIR + newFileName))
				{
					Integer repInId = null;
					// 预先插入新记录
					reportInForm.setTimes(new Integer(1));
					reportInForm.setReportDate(new Date());
					reportInForm.setRepName(new StrutsMChildReportDelegate().getMChildReport(childRepId, versionId)
							.getReportName());
					reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT);
					//插入数据
					StrutsReportInDelegate strutsReportInDelegate=new StrutsReportInDelegate();
					ReportIn reportIn = strutsReportInDelegate.insertNewReport(reportInForm);

					if (reportIn != null)
						repInId = reportIn.getRepInId();
					
					if (repInId != null)
					{
						String requestParam = "year="+ reportInForm.getYear() + "&term=" + reportInForm.getTerm() + "&curPage=" + curPage
								+ "&orgId=" + orgId;
						
						request.setAttribute("ReportURL", Config.TEMP_DIR_WEB_PATH+newFileName);
//						request.setAttribute("ReportURL", "http://localhost:8081/smis_in_net/tmp/"+newFileName);
						
						request.setAttribute("FileName", newFileName);
						request.setAttribute("RepInId", repInId);
						request.setAttribute("RequestParam",requestParam);
						request.setAttribute("ExcelPath", "tmp");
						CommMethod.buidPageInfo(request);
						result = true;
						
					}
					else
					{
						result = false;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		
		if(result)
			return mapping.findForward("viewOnLine");
		else
		{
			messages.add(resources.getMessage("系统忙，请稍后再试...！"));
			request.setAttribute(Config.MESSAGES, messages);
			return new ActionForward("viewDataReport.do?year=" + reportInForm.getYear() + "&term=" + term + "&curPage="
					+ curPage);
		}

	}
}