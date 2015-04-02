package com.fitech.net.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.UploadFileForm;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.ReportExcelHandler;
import com.cbrc.smis.util.UtilForExcelAndRaq;
import com.fitech.gznx.common.FileUtil;
import com.fitech.gznx.excel.QDReportExcelHandler;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.QDDataDelegate;
import com.fitech.net.config.Config;

/**
 * ��һ��ʹ��jdbc ���޸�  ���Ը�  2011-12-21
 * wh ���������ϴ��ļ�
 */
public class UploadFileAction extends Action {

	private FitechException log = new FitechException(UploadFileAction.class);

	public  String messInfo = "";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {

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
		String year = null;
		String term = null;
		String curId = null;
		String orgId = null;
		String dataRangeId = null;
		String actuFreqID = null;
		String backQry = null;


		if (formFile == null || str == null || str.equals(""))
		{
			messages.add(resources.getMessage("select.upReport.failed"));
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);

			return new ActionForward("/viewDataReport.do");
		}
		try
		{
			String[] param = str.split("_");
			childRepId = param[0].trim();
			versionId = param[1].trim();
			year = param[2].trim();
			term = param[3].trim();
			curId = param[4].trim();
			orgId = param[5].trim();
			dataRangeId = param[6].trim();
			actuFreqID = param[7].trim();
			backQry = param[8].trim().replaceAll("amp;", "");

		}
		catch (Exception ex)
		{
			year = null;
			term = null;
			versionId = null;
		}

		if (year == null || year.equals("") || term == null || term.equals("") || versionId == null || versionId.equals(""))
		{
			messages.add(resources.getMessage("select.upReport.failed"));
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);

			return new ActionForward("/viewDataReport.do");
		}
		String requestParam = backQry;//"year=" + year + "&term=" + term + "&curPage=" + curpage;
		if (orgId == null || orgId.equals(""))
		{
			messages.add(resources.getMessage("select.upReport.failed"));
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
			request.setAttribute("curPage", curpage);
			request.setAttribute("RequestParam", requestParam);
			return new ActionForward("/viewDataReport.do?");
		}

		String url = "childRepId=" + childRepId + "&versionId=" + versionId + "&year=" + year + "&term=" + term + "&curId=" + curId + "&orgId=" + orgId
				+ "&dataRangeId=" + dataRangeId + "&checkFlag=" + Config.CHECK_FLAG_UNCHECK + "&actuFreqID=" + actuFreqID+ "&curPage="+ curpage;



		/** ��ʼ�ϴ� */
		String path = Config.REAL_ROOT_PATH + Config.REPORT_NAME + File.separator + Config.SERVICE_UP_TEMP+ orgId;
		String excelPath = path  + File.separator + orgId+request.getSession().hashCode()+System.currentTimeMillis()+".xls";
		File excelFile = new File(path );
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
		
		ReportInForm reportInForm = new ReportInForm();
		boolean result = false;
		try
		{
			if (curId == null || curId.equals(""))
				curId = "1";
			/**��ʹ��Hibernate ���Ը� 2011-12-21**/
			MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);
			// �õ���������
			String CurrName = mCurr.getCurName();

			Integer repInId = null;
			// Ԥ�Ȳ����¼�¼
			reportInForm.setChildRepId(childRepId);
			reportInForm.setVersionId(versionId);
			reportInForm.setDataRangeId(new Integer(dataRangeId));
			reportInForm.setCurId(new Integer(curId));
			reportInForm.setYear(new Integer(year));
			reportInForm.setTerm(new Integer(term));
			reportInForm.setOrgId(orgId);
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			reportInForm.setOrgName(AFOrgDelegate.getOrgInfo(orgId).getOrgName());
			reportInForm.setVersionId(versionId);
			reportInForm.setTimes(new Integer(1));
			reportInForm.setReportDate(new Date());
		

			reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT);
			reportInForm.setCurName(CurrName);
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			MChildReportForm mcr = new StrutsMChildReportDelegate().getMChildReport(reportInForm.getChildRepId(), reportInForm.getVersionId());
			reportInForm.setRepName(mcr.getReportName());
			// ��������
			StrutsReportInDelegate strutsReportInDelegate=new StrutsReportInDelegate();
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			ReportIn reportIn = strutsReportInDelegate.insertNewReport(reportInForm);
			if (reportIn != null)
				repInId = reportIn.getRepInId();
			String excelFileName = com.cbrc.smis.common.Config.WEBROOTPATH + "report_mgr" + File.separator + "excel" + File.separator
			+ childRepId + versionId + ".xls";
			
			if(new File(excelFileName).exists()){
				new File(excelFileName).delete(); //���ǵ���Ⱥ�����¿��ܵ����ļ���һ�£������޸ĳ�ÿ�����붼���������ɱ����ļ�
			}
			String raqFilePath = com.cbrc.smis.common.Config.RAQ_TEMPLATE_PATH+File.separator+"templateFiles"
			+ File.separator + "Raq"+ File.separator+childRepId + "_" + versionId + ".raq";
			UtilForExcelAndRaq.createExcelForRaq(raqFilePath, excelFileName, childRepId);
			//�嵥�����ֱ���
			if(mcr.getReportStyle().toString().equals(com.fitech.gznx.common.Config.REPORT_DD)){
				
				String produceExcelPath = com.cbrc.smis.common.Config.WEBROOTPATH+File.separator+"reportWorkDir"
					+File.separator+request.getSession().getId();
				File pfile = new File(produceExcelPath);
				if(!pfile.exists())
					pfile.mkdirs();
				produceExcelPath+=File.separator+childRepId+"_"+versionId+".xls";
				FileUtil.copyFile(excelFileName, produceExcelPath);
				//���� excel
				ReportExcelHandler excelHandler = new ReportExcelHandler(repInId, excelPath,produceExcelPath);
				/**��ʹ��Hibernate ���Ը� 2011-12-21**/
				boolean flag = getExcelReportISTrue(excelHandler.title,excelHandler.subTitle, versionId, childRepId);
				if (!flag)
				{
					if (!this.messInfo.equals(""))
					{
						messages.add(this.messInfo);
					}
					else
					{
						messages.add("��������ʧ��");
					}
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
					request.setAttribute("curPage", curpage);
					request.setAttribute("RequestParam", requestParam);
					FileUtil.deleteFile(produceExcelPath);
					return new ActionForward("/offLineReport.do?" + url);
				}
				
				
				/**1008���*/
				result = excelHandler.copyExcelToDB(false);
				
				FileUtil.deleteFile(produceExcelPath);
				
			}else{
				//�嵥
				// ��֤�嵥ʽ����ı�����ӱ���
				QDReportExcelHandler qdExcelHandler = new QDReportExcelHandler(excelPath,messages);
				if(qdExcelHandler.getMessages()!=null && qdExcelHandler.getMessages().getSize()>0){
					messages=qdExcelHandler.getMessages();
					request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
					request.setAttribute("curPage", curpage);
					request.setAttribute("RequestParam", requestParam);
					return new ActionForward("/offLineReport.do?" + url);
				}
				/**��ʹ��Hibernate ���Ը� 2011-12-21**/
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
					return new ActionForward("/offLineReport.do?" + url);
				}

				// ���嵥ʽ��������ݲ������ݿ�
				/**ʹ��jdbc ��Ҫ�޸� ���Ը� 2011-12-21**/
				result = new QDDataDelegate().qdIntoDB(childRepId, versionId, repInId.toString(), excelPath);

			}
			
			if (result)
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				result = strutsReportInDelegate.updateReportInCheckFlag_e(repInId, Config.CHECK_FLAG_AFTERSAVE);			
			
			if (result) {

				notshow = "not null";

				Aditing aditing = new Aditing();

				// �õ����Ϳھ�
				/**��ʹ��Hibernate ���Ը� 2011-12-21**/
				MDataRgType mrt = new StrutsMDataRgTypeDelegate().selectOneByName(dataRangeId);
				if (mrt != null)
					aditing.setDataRgTypeName(mrt.getDataRgDesc());
				// �õ�����Ƶ��
				/**��ʹ��Hibernate ���Ը� 2011-12-21**/
				MRepFreq mrf = new StrutsMDataRgTypeDelegate().getActuRepFlag(new Integer(actuFreqID));

				aditing.setRepInId(repInId);
				aditing.setRepName(reportInForm.getRepName());
				aditing.setChildRepId(reportInForm.getChildRepId());
				aditing.setVersionId(reportInForm.getVersionId());
				aditing.setYear(reportInForm.getYear());
				aditing.setTerm(reportInForm.getTerm());
				aditing.setOrgId(reportInForm.getOrgId());
				aditing.setOrgName(reportInForm.getOrgName());
				aditing.setCurId(reportInForm.getCurId());
				aditing.setCurrName(reportInForm.getCurName());
				aditing.setDataRangeId(reportInForm.getDataRangeId());
				aditing.setActuFreqName(mrf.getRepFreqName());
				aditing.setActuFreqID(new Integer(actuFreqID));
				aditing.setCheckFlag(reportInForm.getCheckFlag());
				request.setAttribute("aditing", aditing);

				if (notshow != null)
				{
					request.setAttribute(Config.NOT_SHOW, notshow);
				}
			} else {
				messages.add("��������ʧ��");
				request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
				request.setAttribute("curPage", curpage);
				request.setAttribute("RequestParam", requestParam);
				return new ActionForward("/offLineReport.do?" + url);
			}
			File tempFile=new File(excelPath);
			if (tempFile.exists())
				tempFile.delete();

		}
		catch (Exception e)
		{
			messages.add("��������ʧ��");
			e.printStackTrace();
		}

		request.setAttribute("curPage", curpage);
		request.setAttribute("RequestParam", requestParam);

		messages.add("��������ɹ�");
		request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);

		return mapping.findForward("view");
	}

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
	public boolean getExcelReportISTrue(String title,String subTitle, String versionId, String childRepId)
	{
		boolean flag = false;

		MChildReport mChildReport = null;

		try {

			Integer templateType = getFZORFRTemplate(title);
			int count = -1;
			int FRCount = -1;
			StrutsMChildReportDelegate delegate=new StrutsMChildReportDelegate();
			if (templateType == com.fitech.net.config.Config.FZ_TEMPLATE)
			{
				// �鿴M_child_report ��reportName �Ƿ��Excel��title+subTitle ƥ��
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				count = delegate.IsMatchingReportName(title, subTitle, versionId);
				if(count<=0);
					this.messInfo = "Excel����ͷ������ģ�岻ƥ�䣬";
			}else{
				// �鿴M_child_report ��reportName �Ƿ��Excel��titleƥ��
				String FRSubTitle="";
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				FRCount = delegate.IsMatchingReportName(title,FRSubTitle, versionId);
				if(FRCount<=0)
					this.messInfo = "Excel����ͷ������ģ�岻ƥ�䣬";
			}

			if (subTitle == null || subTitle.equals("")) {
				mChildReport = delegate.getMChildReportByReportName(title, title, versionId);

			}
			else if (templateType.equals(com.fitech.net.config.Config.FZ_TEMPLATE) && count > 0) {
				String reportName = title.trim().equals(subTitle.trim()) ? title.trim() : title.trim() + "-" + subTitle.trim();
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				mChildReport = StrutsMChildReportDelegate.getFZMChildReportByReportName(reportName, versionId);

			}
			else if (templateType.equals(com.fitech.net.config.Config.FZ_TEMPLATE)) {
				String reportName = title.trim().equals(subTitle.trim()) ? title.trim() : title.trim();
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				mChildReport = StrutsMChildReportDelegate.getFZMChildReportByReportName(reportName, versionId);

			}
			else if (templateType.equals(com.fitech.net.config.Config.FR_TEMPLATE) && FRCount > 0) {
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				mChildReport = StrutsMChildReportDelegate.getFZMChildReportByReportName(title, versionId);
			}
			else {
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				mChildReport = delegate.getMChildReportByReportName(title, subTitle, versionId);

			}

			if (mChildReport == null)
			{
				this.messInfo += "��������ʧ�ܣ�";
				flag = false;

			}
			else if (!mChildReport.getComp_id().getChildRepId().equals(childRepId) || !mChildReport.getComp_id().getVersionId().equals(versionId))
			{
				this.messInfo += "�����������!";
				flag = false;
			}
			else
			{
				flag = true;
			}

		}
		catch (Exception ex)
		{
			flag = false;
			ex.printStackTrace();

		}

		return flag;
		
	}
}
