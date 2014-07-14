package com.fitech.net.action;


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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.net.adapter.StrutsAnalysisTemplateDelegate;
import com.fitech.net.form.AAnalysisTPForm;

/**
 * ����ͳ�Ʒ���ģ��
 * 
 * @author wh
 * 
 */
public class AddAnalysisTemplateAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		AAnalysisTPForm aAnalysisTPForm = null;
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();

		aAnalysisTPForm = (AAnalysisTPForm) form;
		RequestUtils.populate(aAnalysisTPForm, request);

		String atId = aAnalysisTPForm.getATId();
		String atName = request.getParameter("ATName");
		// ת��
		if (atName != null && !atName.equals(""))
			atName = java.net.URLDecoder.decode(atName, "UTF-8");
		//�첽��֤��־λ
		String validate = request.getParameter("validate");

		// �첽�����д��Ϣ
		if (validate != null)
		{
			boolean validateResult = true;

			try
			{
				/*ģ���ż��*/
				if (validate.equals("reptId"))
				{
					validateResult = StrutsAnalysisTemplateDelegate.isATIdExists(atId);
				}
				/*ģ�����Ƽ��*/
				else if (validate.equals("reptName"))
				{
					validateResult = StrutsAnalysisTemplateDelegate.isATNameExists(atName);
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
			try
			{
				/* ����ģ����Ϣ�Ƿ���� */
				if (StrutsAnalysisTemplateDelegate.isATIdExists(atId) == false)
				{

					FormFile tmplFile = aAnalysisTPForm.getTemplateFile();
					if (tmplFile != null && tmplFile.getFileSize() > 0
							&& tmplFile.getFileSize() < Config.FILE_MAX_SIZE)
					{
						//����ģ����Ϣ
						result = StrutsAnalysisTemplateDelegate.insertAnalysisTemplate(aAnalysisTPForm);
						if (result == true)
						{
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
					messages.add("ͳ�Ʒ���ģ��"+aAnalysisTPForm.getATId()+"�Ѵ���");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				messages.add(FitechResource.getMessage(locale, resources, "save.fail", "template.msg"));
				result = false;
			}

			if (messages != null && messages.getSize() > 0)
				request.setAttribute(Config.MESSAGES, messages);
				return mapping.findForward("view");
		
		}
	}

}
