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
 * �����
 * 
 * @author Yao���޸ģ�
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
		//ȡ����ػ�����Ϣ
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
				//ETL�ļ����·��   wh��,
			/*	String releaseDir = com.fitech.net.config.Config.getReleaseTemplatePath() + File.separator + year
						+ "_" + term + File.separator + orgId;	
				
				File file = new File(releaseDir + File.separator + fileName);*/
				
//				File file =  null;
				/**�ȿ���û��ETL���ݣ���û�����ؿ�ģ�壬������ETL��������һ�����ݱ�������**/
				String  excelFilePath = new StrutsMRepRangeDelegate().getExcelFile(childRepId,versionId,orgId,year, term,dataRangeId,curId, new Integer(-2));
				if(excelFilePath != null && !excelFilePath.equals("")){
					 path = excelFilePath;	
				}else{
					path = com.fitech.net.config.Config.getTemplateFolderRealPath()+File.separator+childRepId + "_" + versionId+ ".xls";
				}
				
				// ���ETLĿ¼�´��ڱ�����ֱ�Ӵ򿪸�ETLȡ������ļ�
			/*	if (file.exists())
					path = releaseDir + File.separator + fileName;
				// �����ģ���ļ�
				else
					path = com.fitech.net.config.Config.getTemplateFolderRealPath()+File.separator+childRepId + "_" + versionId
					+ ".xls";
*/
				/** ���ļ����Ƶ���ʱ�ļ�����* */
				String newFileName = String.valueOf(System.currentTimeMillis())+".xls";
				if(FitechUtil.copyFile(path, Config.TEMP_DIR + newFileName))
				{
					Integer repInId = null;
					// Ԥ�Ȳ����¼�¼
					reportInForm.setTimes(new Integer(1));
					reportInForm.setReportDate(new Date());
					reportInForm.setRepName(new StrutsMChildReportDelegate().getMChildReport(childRepId, versionId)
							.getReportName());
					reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT);
					//��������
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
			messages.add(resources.getMessage("ϵͳæ�����Ժ�����...��"));
			request.setAttribute(Config.MESSAGES, messages);
			return new ActionForward("viewDataReport.do?year=" + reportInForm.getYear() + "&term=" + term + "&curPage="
					+ curPage);
		}

	}
}