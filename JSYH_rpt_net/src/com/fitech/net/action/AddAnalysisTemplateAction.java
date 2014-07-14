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
 * 新增统计分析模版
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
		// 转码
		if (atName != null && !atName.equals(""))
			atName = java.net.URLDecoder.decode(atName, "UTF-8");
		//异步验证标志位
		String validate = request.getParameter("validate");

		// 异步检查填写信息
		if (validate != null)
		{
			boolean validateResult = true;

			try
			{
				/*模版编号检查*/
				if (validate.equals("reptId"))
				{
					validateResult = StrutsAnalysisTemplateDelegate.isATIdExists(atId);
				}
				/*模版名称检查*/
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
		/* 模版保存 */
		else
		{
			boolean result = false;
			try
			{
				/* 检查该模版信息是否存在 */
				if (StrutsAnalysisTemplateDelegate.isATIdExists(atId) == false)
				{

					FormFile tmplFile = aAnalysisTPForm.getTemplateFile();
					if (tmplFile != null && tmplFile.getFileSize() > 0
							&& tmplFile.getFileSize() < Config.FILE_MAX_SIZE)
					{
						//插入模版信息
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
						messages.add("文件正在使用或文件大小超过限制！只能载入小于4M的Excel文件！");
					}
				}
				else
				{
					messages.add("统计分析模板"+aAnalysisTPForm.getATId()+"已存在");
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
