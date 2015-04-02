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
 * 已使用hibernate 卞以刚 2011-12-22
 * 新增分支模版
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
		// 转码
		if (reptName != null && !reptName.equals(""))
			reptName = java.net.URLDecoder.decode(reptName, "UTF-8");
		//异步验证标志位
		String validate = request.getParameter("validate");

		// 异步检查填写信息
		if (validate != null)
		{
			boolean validateResult = true;

			try
			{
				/*模版编号版本号检查*/
				if (validate.equals("reptId"))
				{
					/**已使用hibernate 卞以刚 2011-12-22**/
					validateResult = StrutsMChildReportDelegate.isTemplateVersionExists(reptId, versionId);
				}
				/*模版名称检查*/
				else if (validate.equals("reptName"))
				{
					/**已使用hibernate 卞以刚 2011-12-22**/
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
		/* 模版保存 */
		else
		{
			boolean result = false;
			AFTemplateForm templateForm = new AFTemplateForm();
			try
			{
				/* 检查该模版信息是否存在 */
				/**已使用hibernate 卞以刚 2011-12-22**/
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
							// 保存报表模板信息失败，返回原页面
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
								//清除数据
								AFReportDealDelegate.clearJYHData(templateForm);
								messages.add(e.getMessage());
								request.setAttribute(Config.MESSAGES, messages);
								// 保存报表模板信息失败，返回原页面
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
									//清除数据
									AFReportDealDelegate.clearJYHData(templateForm);
									
									result = false;
									messages.add(e.getMessage());
								}
							}
							if(result){
								//插入分支模版信息
									/**已使用Hibernate 卞以刚 2011-12-22**/
								try {
									result = StrutsNot1104TmplDelegate.insertFZTemplate(mChildReportForm,excelfilepath);
									System.out.println("=============insert success=============");
								} catch (Exception e) {
									AFReportDealDelegate.clearJYHData(templateForm);
									e.printStackTrace();
								}
								
							// 删除临时文件
							}else
								/**解决由于模板问题导致载入模板失败之后的数据冗余问题*/
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
						messages.add("文件正在使用或文件大小超过限制！只能载入小于4M的Excel文件！");
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
				//清除数据
				AFReportDealDelegate.clearJYHData(templateForm);
				messages.add(FitechResource.getMessage(locale, resources, "save.fail", "template.msg"));
				result = false;
			}

			if (messages != null && messages.getSize() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			if (result == true)
			{ // 保存报表模板信息成功，进入表内表间关系设定页面
				return mapping.findForward("success");
			}
			else
			{ // 保存报表模板信息失败，返回原页面
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
