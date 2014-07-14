package com.fitech.gznx.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.form.UploadFileForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.excel.NXReportExcelHandler;
import com.fitech.gznx.excel.QDReportExcelHandler;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFDataDelegate;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.QDDataDelegate;
import com.fitech.net.config.Config;

public class UploadFileGatherAction extends Action {

	private FitechException log = new FitechException(UploadFileNXAction.class);

	public  String messInfo = "";
	
	/***
	 * ��jdbc���� ������Ҫ�޸� ���Ը� 2011-12-22
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{

		this.messInfo = "";

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);

		UploadFileForm upFileForm = (UploadFileForm) form;
		RequestUtils.populate(upFileForm, request);

		// ����ҳ���Ƿ���ʾ��ť
		String notshow = null;

		FormFile formFile = upFileForm.getFormFile();
		String str = upFileForm.getVersionId();
		
		String curpage = request.getParameter("curPage");

		String childRepId = null;
		String versionId = null;
		String date = null;
		String curId = null;
		String orgId = null;
		//String dataRangeId = null;
		String actuFreqID = null;
		String backQry = null;

		if (formFile == null || str == null || str.equals(""))
		{
			messages.add(resources.getMessage("select.upReport.failed"));
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);

			return new ActionForward("/viewGatherReport.do");
		}

		String[] param = str.split("_");
		childRepId = param[0].trim();
		versionId = param[1].trim();
		date = param[2].trim();
		curId = param[3].trim();
		orgId = param[4].trim();
		actuFreqID = param[5].trim();
		backQry = param[6].trim();
		
		String requestParam = backQry.replaceAll("amp;", "");//+ "&curPage=" + curpage;

		//û���������汾��ֱ�ӷ���
		if (date == null || date.equals("") || versionId == null || versionId.equals(""))
		{
			messages.add(resources.getMessage("select.upReport.failed"));
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);

			return new ActionForward("/viewGatherReport.do"+requestParam);
		}
		
		
		if (orgId == null || orgId.equals(""))
		{
			messages.add(resources.getMessage("select.upReport.failed"));
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
			request.setAttribute("curPage", curpage);
			request.setAttribute("RequestParam", requestParam);
			return new ActionForward("/viewGatherReport.do?"+requestParam);
		}

		/**
		 * ��ʼ����һϵ�е���ʱ�ļ���
		 */
//		this.make(Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP);
//		this.make(Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP + Config.SERVICE_UP_RELEASE);
//		this.make(Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP  + orgId);

		/** ��ʼ�ϴ� */

		String path = Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP+ orgId;

		String excelPath = path + File.separator + orgId+request.getSession().hashCode()+System.currentTimeMillis()+".xls";
		
		File excelFile = new File(path);
		if (!excelFile.exists())
			excelFile.mkdirs();
		if (formFile != null)
		{
			InputStream ips = formFile.getInputStream();
			FileOutputStream fops = new FileOutputStream(excelPath);
			byte[] bytes = new byte[1024];
			int index = 0;
			while ((index = ips.read(bytes)) != -1)
			{
				fops.write(bytes, 0, index);
			}
			fops.close();
			ips.close();
		}
		formFile=null;
		/** �ϴ���� */
		
		AFReportForm reportInForm = new AFReportForm();
		boolean result = false;
		try
		{
			if (curId == null || curId.equals(""))
				curId = "1";
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);
			// �õ���������
			String CurrName = mCurr.getCurName();

			//��ñ�����ȷ����
//			String repDays[] = DateUtil.getLastFreqDate(date, Integer.valueOf(actuFreqID)).split("-");
			String repDays[] = date.split("-");

			
			Integer repInId = null;
			
			// Ԥ�Ȳ����¼�¼
			reportInForm.setTemplateId(childRepId);
			reportInForm.setVersionId(versionId);
			//reportInForm.setDataRangeId(new Integer(dataRangeId));
			
			reportInForm.setYear(repDays[0]);
			reportInForm.setTerm(repDays[1]);
			reportInForm.setDay(repDays[2]);
			
			reportInForm.setRepFreqId(actuFreqID);
			reportInForm.setOrgId(orgId);

			reportInForm.setTimes("1");
			reportInForm.setReportDate(new Date());

			reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT.toString());
			
			reportInForm.setCurId(curId);
			reportInForm.setCurName(CurrName);
			
			/**��ʹ��hibernate ���Ը� 2011-12-22**/
			AfTemplate mcr = AFTemplateDelegate.getTemplate(reportInForm.getTemplateId(), reportInForm.getVersionId());
			reportInForm.setRepName(mcr.getTemplateName());
			
			// ��������
			AFReportDelegate strutsReportInDelegate = new AFReportDelegate();
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			AfReport reportIn = strutsReportInDelegate.insertNewReport(reportInForm);
			if (reportIn != null)
				repInId = reportIn.getRepId().intValue();
			
			
			String url = "childRepId=" + childRepId + 
			"&versionId=" + versionId + 
			"&year=" + repDays[0] + 
			"&term=" + repDays[1] + 
			"&day=" + repDays[2] + 
			"&curId=" + curId + 
			"&orgId=" + orgId + 
		//	"&dataRangeId=" + dataRangeId + 
			"&checkFlag=" + Config.CHECK_FLAG_UNCHECK + 
			"&actuFreqID=" + actuFreqID+ 
			"&curPage="+ curpage;
	        /***
	          * ȡ�õ�ǰ��������
	          */   
			HttpSession session = request.getSession();    
			
			String repType = null;
			if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
				repType = session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString();
			//�����㡢�嵥�ж�
			if(!mcr.getReportStyle().toString().equals(com.fitech.gznx.common.Config.REPORT_QD)){
				
				NXReportExcelHandler excelHandler = new NXReportExcelHandler(repInId, excelPath,messages,mcr.getTemplateType());
				if(excelHandler.getMessages()!=null && excelHandler.getMessages().getSize()>0){
					messages=excelHandler.getMessages();
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
					request.setAttribute("curPage", curpage);
					request.setAttribute("RequestParam", requestParam);
					return new ActionForward("/offLineGatherReport.do?" + url);
				}
				boolean flag = getExcelReportISTrue(excelHandler.title, excelHandler.subTitle, versionId, childRepId);
				if (!flag) {
					if (!this.messInfo.equals("")) {
						messages.add(this.messInfo);
					} else {
						messages.add("��������ʧ��!������֤δͨ��");
					}
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
					request.setAttribute("curPage", curpage);
					request.setAttribute("RequestParam", requestParam);
					return new ActionForward("/offLineGatherReport.do?" + url);
				}
				//�����������ݽ�DB
				result = excelHandler.copyExcelToDB(repType);
				
			} else {
				// ��֤�嵥ʽ����ı�����ӱ���
				QDReportExcelHandler qdExcelHandler = new QDReportExcelHandler(excelPath,messages);
				if(qdExcelHandler.getMessages()!=null && qdExcelHandler.getMessages().getSize()>0){
					messages=qdExcelHandler.getMessages();
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
					request.setAttribute("curPage", curpage);
					request.setAttribute("RequestParam", requestParam);
					return new ActionForward("/offLineNXReport.do?" + url);
				}
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				boolean flag = getExcelReportISTrue(qdExcelHandler.title, qdExcelHandler.subTitle, versionId, childRepId);
				if (!flag) {
					if (!this.messInfo.equals("")) {
						messages.add(this.messInfo);
					} else {
						messages.add("��������ʧ��");
					}
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
					request.setAttribute("curPage", curpage);
					request.setAttribute("RequestParam", requestParam);
					return new ActionForward("/offLineNXReport.do?" + url);
				}

				// ���嵥ʽ��������ݲ������ݿ�
				/**jdbc���� ������Ҫ�޸� ���Ը� 2011-12-22**/
				result = new QDDataDelegate().qdIntoDBNx(childRepId, versionId, repInId.toString(), excelPath);
			}
			

			
			if (result)
				/**��ʹ��hibernate ���Ը� 2011-12-22**/
				result = strutsReportInDelegate.updateReportInCheckFlag(repInId, Config.CHECK_FLAG_PASS);
			
			if (result) {

				notshow = "not null";

				Aditing aditing = new Aditing();

				// �õ����Ϳھ�
//				MDataRgType mrt = new StrutsMDataRgTypeDelegate().selectOneByName(dataRangeId);
//				if (mrt != null)
//					aditing.setDataRgTypeName(mrt.getDataRgDesc());
				
				// �õ�����Ƶ��
				/**��ʹ��hibernate ���Ը� 2011-12-22**/
				MRepFreq mrf = new StrutsMDataRgTypeDelegate().getActuRepFlag(new Integer(actuFreqID));

				aditing.setRepInId(repInId);
				aditing.setRepName(reportInForm.getRepName());
				aditing.setChildRepId(reportInForm.getTemplateId());
				aditing.setVersionId(reportInForm.getVersionId());
				aditing.setYear(Integer.valueOf(reportInForm.getYear()));
				aditing.setTerm(Integer.valueOf(reportInForm.getTerm()));
				aditing.setDay(Integer.valueOf(reportInForm.getDay()));
				aditing.setOrgId(reportInForm.getOrgId());
				aditing.setOrgName(AFOrgDelegate.getOrgInfo(orgId).getOrgName());
				aditing.setCurId(Integer.valueOf(reportInForm.getCurId()));
				aditing.setCurrName(reportInForm.getCurName());
				//aditing.setDataRangeId(reportInForm.getDataRangeId());
				aditing.setActuFreqName(mrf.getRepFreqName());
				aditing.setActuFreqID(Integer.valueOf(actuFreqID));
				aditing.setCheckFlag(Short.valueOf(reportInForm.getCheckFlag()));
				
				request.setAttribute("aditing", aditing);

				if (notshow != null) {
					request.setAttribute(Config.NOT_SHOW, notshow);
				}
			} else {
				messages.add("��������ʧ��");

				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
				request.setAttribute("curPage", curpage);
				request.setAttribute("RequestParam", requestParam);
				return new ActionForward("/offLineNXReport.do?" + url);
			}
			
			File tempFile=new File(excelPath);
			if (tempFile.exists())
				tempFile.delete();
			


			//���������ݴ�����ҵ������
//			AFDataDelegate afData = new AFDataDelegate();
//			Integer statusFlag = null;
//			statusFlag = afData.transformerReportToModoule(childRepId, versionId, 
//					Integer.valueOf(reportInForm.getYear()), 
//					Integer.valueOf(reportInForm.getTerm()), 
//					Integer.valueOf(reportInForm.getDay()), 
//					Integer.valueOf(reportInForm.getRepFreqId()), 
//					Integer.valueOf(curId), orgId, "");
//			if(statusFlag.toString().equals("1"))
//				messages.add("but�������ݴ�����ҵ������error!");

				
		} catch (Exception e) {
			
			messages.add("��������ʧ��");
			e.printStackTrace();
			
		}
		
		if(result)
			messages.add("��������ɹ�!");
		
		requestParam = backQry.replaceAll("amp;", ""); //+ "&curPage=" + curpage ;//+ "&orgId=" + orgId;
		request.setAttribute("curPage", curpage);
		request.setAttribute("RequestParam", requestParam);

		request.setAttribute("backQry", backQry);
		request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);

		return new ActionForward("/viewGatherReport.do?"+requestParam);
		//return mapping.findForward("view");
	}
//
//	/**
//	 * �����ļ���
//	 */
//	public void make(String filePath)
//	{
//		File file = new File(filePath);
//
//		if (!file.exists())
//		{
//			file.mkdirs();
//		}
//	}
//
//	/**
//	 * ɾ���ļ���
//	 */
//	public void remove(String filePath)
//	{
//		File file = new File(filePath);
//
//		if (file != null && file.isDirectory())
//		{
//			File[] files = file.listFiles();
//			for (int i = 0; i < files.length; i++)
//			{
//				if (files[i] != null)
//				{
//					files[i].delete();
//				}
//			}
//		}
//		file.delete();
//	}
//	/**
//	 * 
//	 * @param title
//	 * @return
//	 */
	private Integer getFZORFRTemplate(String title)
	{
		Integer templateType = com.fitech.net.config.Config.FR_TEMPLATE;
		int count = -1;
		if (title != null && !title.equals("") && title.length() > 2)
		{

			String titleStr = title.trim().substring(0, 2);
			if (titleStr.toUpperCase().indexOf(com.fitech.net.config.Config.FZ_SF_TAMPLATE) > -1
					|| titleStr.toUpperCase().indexOf(com.fitech.net.config.Config.FZ_GF_TAMPLATE) > -1)
			{
				templateType = com.fitech.net.config.Config.FZ_TEMPLATE;
			}

		}
		return templateType;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �鿴ģ�������Ƿ���ȷ
	 * 
	 * @param excelPath
	 * @param versionId
	 * @param childRepId
	 * @return
	 */
	private boolean getExcelReportISTrue(String title, String subTitle, String versionId, String childRepId) {
		
		boolean flag = false;

		AfTemplate afTemplate = null;

		try {

			String titles = title + "-" + subTitle;
			
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			afTemplate = AFTemplateDelegate.getTemplate(childRepId, versionId);

			if (afTemplate == null) {
				
				this.messInfo = "��������ʧ�ܣ��޸ñ�����Ϣ";
				flag = false;

			} else if ( afTemplate.getTemplateName().equals(titles.trim())
					|| afTemplate.getTemplateName().equals(title.trim())) {
				
				flag = true;
				
			} else {
				
				this.messInfo = "�����������!�������";
				flag = false;
				
			}

		}
		catch (Exception ex) {
			
			flag = false;
			ex.printStackTrace();

		}

		return flag;
		
	}
}
