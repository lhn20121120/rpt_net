package com.cbrc.smis.action;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

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
import com.cbrc.smis.adapter.StrutsNot1104TmplDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.proc.util.EngineException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;

/**
 * ��ʹ��hibernate ���Ը� 2011-12-22
 * ������֧ģ��
 * 
 * @author Yao
 * 
 */
public class SearchTemplateVersionAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		MChildReportForm mChildReportForm = null;
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();

		mChildReportForm = (MChildReportForm) form;
		RequestUtils.populate(mChildReportForm, request);

		String reptId = mChildReportForm.getChildRepId();
		String versionId = mChildReportForm.getVersionId();
		String reptName = request.getParameter("reportName");
		String templateName = mChildReportForm.getReportName();
		if(!StringUtil.isEmpty(templateName)){
			templateName = templateName.replaceAll(" ", "");
			mChildReportForm.setReportName(templateName);
		}
		// ת��
		if (reptName != null && !reptName.equals(""))
			reptName = java.net.URLDecoder.decode(reptName, "UTF-8");
		//�첽��֤��־λ
		String validate = request.getParameter("validate");

		// �첽�����д��Ϣ
		if (validate != null)
		{
			boolean validateResult = true;

			try
			{
				/*ģ���Ű汾�ż��*/
				if (validate.equals("reptId"))
				{
					/**��ʹ��hibernate ���Ը� 2011-12-22**/
					validateResult = StrutsMChildReportDelegate.isTemplateVersionExists(reptId, versionId);
				}
				/*ģ�����Ƽ��*/
				else if (validate.equals("reptName"))
				{
					/**��ʹ��hibernate ���Ը� 2011-12-22**/
					validateResult = StrutsMChildReportDelegate.isReportNameExists(reptName);
				}

				PrintWriter out = response.getWriter();
				response.setContentType("text/xml");
				response.setHeader("Cache-control", "no-cache");
				if (validateResult == false)
				{
					out.println("<response><result>false</result></response>");
				}
				else
				{
					out.println("<response><result>true</result></response>");
				}
				out.close();
			}
			catch (RuntimeException e)
			{
				e.printStackTrace();
			}
			return null;
		}
		/* ģ�汣�� */
		else
		{
			boolean result = false;
			AFTemplateForm templateForm = new AFTemplateForm();
			try
			{
				/* ����ģ����Ϣ�Ƿ���� */
				/**��ʹ��hibernate ���Ը� 2011-12-22**/
				if (StrutsMChildReportDelegate.isChildReportExists(reptId, versionId) == false)
				{

					FormFile tmplFile = mChildReportForm.getTemplateFile();
					
					if (tmplFile != null && tmplFile.getFileSize() > 0
							&& tmplFile.getContentType().equals(Config.FILE_CONTENTTYPE_RAQ)
							&& tmplFile.getFileSize() < Config.FILE_MAX_SIZE)
					{
						
						copytemplate(templateForm,mChildReportForm);
						String raqfilepath = null;
						try {
							raqfilepath = AFReportDealDelegate.resolveReport(tmplFile.getInputStream(),templateForm,"1");
							System.out.println("============resolveReport success=============");
						} catch (Exception e1) {
							messages.add(e1.getMessage());
							request.setAttribute(Config.MESSAGES, messages);
							e1.printStackTrace();
							AFReportDealDelegate.clearJYHData(templateForm);
							// ���汨��ģ����Ϣʧ�ܣ�����ԭҳ��
							return mapping.findForward("fail");
						}
						
						if(StringUtil.isEmpty(raqfilepath)){
							result=false;
						} else{
							String excelfilepath = null;
							try {
								excelfilepath = AFReportDealDelegate.toExcel(raqfilepath);
								System.out.println("============toExcel success=============");
							} catch (Exception e) {
								e.printStackTrace();
								//�������
								AFReportDealDelegate.clearJYHData(templateForm);
								messages.add(e.getMessage());
								request.setAttribute(Config.MESSAGES, messages);
								// ���汨��ģ����Ϣʧ�ܣ�����ԭҳ��
								return mapping.findForward("fail");
							}
							if(!StringUtil.isEmpty(excelfilepath)){
								try {
									result =  AFReportDealDelegate.resoveExcelFormaule(excelfilepath,raqfilepath,mChildReportForm.getChildRepId());
									System.out.println("=============resoveExcelFormaule success==============");
								}catch (EngineException e) {
									e.printStackTrace();
									AFReportDealDelegate.clearJYHData(templateForm);
								}catch (Exception e) {
									e.printStackTrace();
									//�������
									AFReportDealDelegate.clearJYHData(templateForm);
									
									result = false;
									messages.add(e.getMessage());
								}
							}
							if(result){
								//�����֧ģ����Ϣ
									/**��ʹ��Hibernate ���Ը� 2011-12-22**/
								try {
									result = StrutsNot1104TmplDelegate.insertFZTemplate(mChildReportForm,excelfilepath);
									System.out.println("=============insert success=============");
								} catch (Exception e) {
									AFReportDealDelegate.clearJYHData(templateForm);
									e.printStackTrace();
								}
								
							// ɾ����ʱ�ļ�
							}else
								/**�������ģ�����⵼������ģ��ʧ��֮���������������*/
								AFReportDealDelegate.cleanSQLData(mChildReportForm.getChildRepId(),mChildReportForm.getVersionId());
							if(!StringUtil.isEmpty(excelfilepath))
								AFReportDealDelegate.deleteUploadFile(excelfilepath);
						}
						
						
						if (result == true)
						{
							MCellFormuForm mCellForumForm = new MCellFormuForm();
							mCellForumForm.setReportName(mChildReportForm.getReportName());
							mCellForumForm.setChildRepId(mChildReportForm.getChildRepId());
							mCellForumForm.setVersionId(mChildReportForm.getVersionId());
							mCellForumForm.setReportStyle(mChildReportForm.getReportStyle());
							request.setAttribute("ObjForm", mCellForumForm);
							messages.add(FitechResource.getMessage(locale, resources, "save.success", "template.msg"));
						}
						else
						{
							messages.add(FitechResource.getMessage(locale, resources, "save.fail", "template.msg"));
						}
					}
					else
					{
						messages.add("�ļ�����ʹ�û��ļ���С�������ƣ�ֻ������С��4M��Excel�ļ���");
					}
				}
				else
				{
					messages.add(FitechResource.getMsg(locale, resources, "template.exists", mChildReportForm
							.getChildRepId(), mChildReportForm.getVersionId()));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				//�������
				AFReportDealDelegate.clearJYHData(templateForm);
				messages.add(FitechResource.getMessage(locale, resources, "save.fail", "template.msg"));
				result = false;
			}

			if (messages != null && messages.getSize() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			if (result == true)
			{ // ���汨��ģ����Ϣ�ɹ���������ڱ���ϵ�趨ҳ��
				return mapping.findForward("success");
			}
			else
			{ // ���汨��ģ����Ϣʧ�ܣ�����ԭҳ��
				return mapping.findForward("fail");
			}
		}
	}

	private void copytemplate(AFTemplateForm templateForm,
			MChildReportForm childReportForm) {
		templateForm.setTemplateId(childReportForm.getChildRepId());
		templateForm.setTemplateName(childReportForm.getReportName());
		templateForm.setVersionId(childReportForm.getVersionId());
		templateForm.setStartDate(childReportForm.getStartDate());
		if(childReportForm.getQdreportFile() != null && childReportForm.getQdreportFile().getFileSize()>0)
		templateForm.setQdreportFile(childReportForm.getQdreportFile());
		templateForm.setEndDate(childReportForm.getEndDate());
		templateForm.setTemplateType("1");
		templateForm.setUsingFlag("0");
		templateForm.setBak1(String.valueOf(childReportForm.getRepTypeId()));
		templateForm.setPriorityFlag(childReportForm.getPriorityFlag());
		templateForm.setIsReport(childReportForm.getIsReport());
		if(childReportForm.getReportStyle()!=null && com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(childReportForm.getReportStyle().intValue())))
		templateForm.setReportStyle(String.valueOf(childReportForm.getReportStyle()));
	}

}
