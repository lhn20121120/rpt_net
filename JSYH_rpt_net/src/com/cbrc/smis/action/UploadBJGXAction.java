package com.cbrc.smis.action;

import java.io.IOException;
import java.util.Collection;
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
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechTemplate;
/**
 * 上传报表的表内表间关系表达式定义，并读取其文件中的关系表达式
 * 
 * @author rds
 * @serialField 2005-12-08
 */
public class UploadBJGXAction extends Action {
	/**
	 * 无数据库交互
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
		
		MCellFormuForm mCellForumForm = (MCellFormuForm) form;
		RequestUtils.populate(mCellForumForm,request);
		
		boolean flag=false;
		
		try {
			FormFile tmplFile=mCellForumForm.getExpressionFile();
			if(tmplFile!=null && tmplFile.getFileSize()>0){
				
				if(tmplFile.getContentType().equals(Config.FILE_CONTENT_TYPE_TXT)){
					if(tmplFile.getFileSize()<Config.FILE_MAX_SIZE){
						Collection collExpression=null;
		
						if(mCellForumForm.getReportStyle().compareTo(Config.REPORT_STYLE_DD)==0){
							collExpression=FitechTemplate.getExpessions(tmplFile.getInputStream());
						}else{
							collExpression=FitechTemplate.getExpessions(tmplFile.getInputStream(),mCellForumForm.getReportStyle());
						}
						request.setAttribute("Expressions",collExpression);
						request.setAttribute("ObjForm",mCellForumForm);
						flag=true;
					}else{  //上载的表内表间关系表达式定义文件大于1M
						messages.add(FitechResource.getMessage(locale,resources,"upload.template.extreme","expression.file.name"));
					}
				}else{  //选择上传的表内表间关系表达式定义文件不是TXT类型 
					messages.add(FitechResource.getMessage(locale,resources,"expression.file.contentType.error"));
				}
			}else{ //上传的表内表间关系表达式定义文件不存在
				messages.add(FitechResource.getMessage(locale,resources,"upload.file.not.exists","expression.file.name"));
			}
		} catch (Exception e) { //系统错误
			messages.add(FitechResource.getMessage(locale,resources,"errors.system"));
		}

		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		
		String paraFlag=request.getParameter("flag")!=null?(String)request.getParameter("flag"):"ADD";
		String path="";
		if (flag == true){ // 操作成功，进入表内表间关系设定页面
			if(paraFlag.toUpperCase().equals("ADD")){   //模板新增
				path=mapping.findForward("bjgxSet").getPath();
			}else{	//模板维护
				path=mapping.findForward("bjgx_append").getPath();
			}
		}else{ // 操作失败，返回原页面
			if(paraFlag.toUpperCase().equals("ADD")){        //新增模板的表达式的导入
				path=mapping.getInputForward().getPath();
			}else{           //模板维护的表达式的导入
				path=mapping.findForward("mod_load").getPath();
			}
		}
		String url="";
		if(mCellForumForm.getChildRepId()!=null) 
			url+=(url.equals("")?"":"&") + "childRepId=" + mCellForumForm.getChildRepId();
		if(mCellForumForm.getVersionId()!=null)
			url+=(url.equals("")?"":"&") + "versionId=" + mCellForumForm.getVersionId();
		/*if(mCellForumForm.getReportName()!=null)
			url+=(url.equals("")?"":"&") + "reportName=" + mCellForumForm.getReportName();*/
		request.setAttribute("ReportName",mCellForumForm.getReportName());
		if(mCellForumForm.getReportStyle()!=null)
			url+=(url.equals("")?"":"&") + "reportStyle=" + mCellForumForm.getReportStyle();
		if(request.getParameter("curPage")!=null)
			url+=(url.equals("")?"":"&") + "curPage=" + (String)request.getParameter("curPage");
		System.out.println(path + (url.equals("")?"":"?") + url);
		return new ActionForward(path + (url.equals("")?"":"?") + url);
	}
}
