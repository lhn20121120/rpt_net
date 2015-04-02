package com.fitech.gznx.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.action.ViewTemplateDetailAction;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateValiSche;
import com.fitech.gznx.po.AfTemplateValiScheId;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.AfTemplateValiScheDelegate;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IByteMap;
import com.runqian.report4.usermodel.INormalCell;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;
/*
 * 修改报表模板信息
 */
public class EditAFTemplateAction extends Action {
	   private static FitechException log = new FitechException(ViewTemplateDetailAction.class);

	    /** 
	     * Method execute
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return ActionForward
	     */
	    public ActionForward execute(
	        ActionMapping mapping,
	        ActionForm form,
	        HttpServletRequest request,
	        HttpServletResponse response)  
	    throws IOException, ServletException
	   {
	        MessageResources resources = getResources(request);
	        FitechMessages messages = new FitechMessages();
	        HttpSession session = request.getSession();
			
			/** 报表选中标志 **/
			String reportFlg = "0";
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
				reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
			}
	        AFTemplateForm templateForm = (AFTemplateForm) form;
	        RequestUtils.populate(templateForm,request);
	        if(templateForm.getBak2()!= null && templateForm.getBak2().equals("update")){
	        	return updateTemplate(templateForm,mapping,request);
	        }
			try{
				AfTemplate aftemplate = AFTemplateDelegate.getTemplate(templateForm.getTemplateId(),templateForm.getVersionId());
	
				Map mapkey = AFTemplateDelegate.getCurrById(templateForm.getTemplateId(),templateForm.getVersionId());
				if(reportFlg.equals("3")){
					/*********************模板校验(begin)*******************/
					AfTemplateValiSche temp = AfTemplateValiScheDelegate.findAfTemplateValiSche(templateForm.getTemplateId(),templateForm.getVersionId());
					if(temp!=null && temp.getValidateSchemeId()!=null){
						templateForm.setSchemeId(temp.getValidateSchemeId().toString());
						request.setAttribute("schemeid", templateForm.getSchemeId());
					}
					/*********************模板校验(end)*******************/
				}
				templateForm.setKeyMap(mapkey);
				templateForm.setIsCollect(String.valueOf(aftemplate.getIsCollect()));
				if(aftemplate.getIsReport() != null){
				templateForm.setIsReport(String.valueOf(aftemplate.getIsReport()));
				}
				templateForm.setStartDate(aftemplate.getStartDate());
				templateForm.setEndDate(aftemplate.getEndDate());
				
				templateForm.setTemplateName(aftemplate.getTemplateName());
				if(aftemplate.getPriorityFlag()!=null){
					templateForm.setPriorityFlag(String.valueOf(aftemplate.getPriorityFlag().intValue()));
				}
				templateForm.setBak1(aftemplate.getBak1());
				if(aftemplate.getSupplementFlag()!=null){
					templateForm.setSupplementFlag(String.valueOf(aftemplate.getSupplementFlag().intValue()));
				}
				if(aftemplate.getIsLeader() != null)
				templateForm.setIsLeader(String.valueOf(aftemplate.getIsLeader()));
				if(aftemplate.getReportStyle() != null)
					templateForm.setReportStyle(String.valueOf(aftemplate.getReportStyle()));
				
             }catch (Exception e) 
             {
                 log.printStackTrace(e);
                 messages.add(resources.getMessage("log.select.fail"));      
             }

	         if(messages.getMessages() != null && messages.getMessages().size() > 0)
	           request.setAttribute(Config.MESSAGES,messages);

	         //将查询条件存放在request范围内
	        
	         return mapping.findForward("index");
	    }

		private ActionForward updateTemplate(AFTemplateForm templateForm,ActionMapping mapping,HttpServletRequest request) {
			
			FitechMessages messages = new FitechMessages();
			/** 报表选中标志 **/
			String reportFlg = "0";
			HttpSession session = request.getSession();
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
				reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
			}
			String templateName = templateForm.getTemplateName();
			if(!StringUtil.isEmpty(templateName)){
				templateName = templateName.replaceAll(" ", "");
				templateForm.setTemplateName(templateName);
			}
			if(templateForm.getIsReport()==null || templateForm.getIsReport().equals(""))
				templateForm.setIsReport("2");//2表示不需要补录
			boolean result = false;
			result = AFReportDealDelegate.updateTemplate(templateForm, reportFlg);
			
			if(result){
				if(reportFlg.equals("3")){
					/*********************模板校验(begin)*******************/
					AfTemplateValiSche template = AfTemplateValiScheDelegate.findAfTemplateValiSche(templateForm.getTemplateId(),templateForm.getVersionId());
					if(template!=null){
						template.setValidateSchemeId(Integer.valueOf(templateForm.getSchemeId()));
						AfTemplateValiScheDelegate.updateAfTemplateValiSche(template);
					}else{
						try {
							AfTemplateValiSche sche = new AfTemplateValiSche();
							AfTemplateValiScheId scheId = new AfTemplateValiScheId();
							scheId.setTemplateId(templateForm.getTemplateId());
							scheId.setVersionId(templateForm.getVersionId());
							sche.setId(scheId);
							sche.setValidateSchemeId(Integer.parseInt(templateForm.getSchemeId()));
							AfTemplateValiScheDelegate.addAfTemplateValiSche(sche);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					/*********************模板校验(end)*******************/
				}
				try {
					InputStream filePath = null;
					if(templateForm.getReportFile() != null && templateForm.getReportFile().getFileSize()>0){
						try {
							filePath = templateForm.getReportFile().getInputStream();
						} catch (FileNotFoundException e) {							
							e.printStackTrace();
						} catch (IOException e) {							
							e.printStackTrace();
						}
					}
					if(templateForm.getQdreportFile() !=null && templateForm.getQdreportFile().getFileSize()>0){
						InputStream qdfilePath = null;
						if(templateForm.getQdreportFile()!=null && templateForm.getQdreportFile().getFileSize()>0){
							qdfilePath = templateForm.getQdreportFile().getInputStream();
							String raqFileName = null;
							try {				
									raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "printRaq"+ 
									File.separator+"qdfile"+ 
									File.separator+templateForm.getTemplateId() + "_" + templateForm.getVersionId() + ".raq";		    	
									ReportDefine qdrd = (ReportDefine)ReportUtils.read(qdfilePath);
									ReportUtils.write(raqFileName,qdrd);
//									if(!Config.SHARE_DATA_PATH.equals(Config.WEBROOTPATH)){
//										// 更新共享文件夹
//										raqFileName = Config.SHARE_DATA_PATH +"templateFiles" + File.separator + "printRaq"+ 
//										File.separator+"qdfile"+ 
//										File.separator+templateForm.getTemplateId() + "_" + templateForm.getVersionId() + ".raq";		    	
//										ReportUtils.write(raqFileName,qdrd);
//									}
							}catch (Exception e){
								e.printStackTrace();
							}
						
						}
					}
					if(filePath != null){						
						String filepathrq = AFReportDealDelegate.resolveRAQ(filePath,templateForm.getTemplateId(),
								templateForm.getVersionId(),reportFlg);
						if(!templateForm.getIsReport().equals(com.fitech.gznx.common.Config.TEMPLATE_VIEW) && !StringUtil.isEmpty(filepathrq)){
							ReportDefine rd = (ReportDefine)ReportUtils.read(filepathrq);	
							Context cxt = new Context();  //构建报表引擎计算环境				
							Engine engine = new Engine(rd, cxt);  //构造报表引擎
							engine.calc();  //运算报表
							int rownum = rd.getRowCount();
							int colnum = rd.getColCount();
							for(int i=1;i<=rownum;i++){
								for(int j=1;j<=colnum;j++){					
									INormalCell cs = rd.getCell(i, (short) j);				
									IByteMap map = cs.getExpMap(false);
									if(map != null && !map.isEmpty() && map.getValue(0) != null){
										cs.setExpMap(null);
									}
								}
							}
							
							IReport iReport = rd;  //运算报表
							String excelFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator +"excel" + File.separator + 
							templateForm.getTemplateId() + "_" + templateForm.getVersionId() + ".xls";
							
							try {
								ReportUtils.exportToExcel(excelFileName, iReport, false);
							} catch (Throwable e) {
								result = false;
								messages.add("生成excel失败");					
								e.printStackTrace();
							}
							if(result){
								/***
								 * 2012/12/31日修改，为能正确解析BIII报表IF公式
								 * 修改之后，此方法会将IF公式中的公式项“==”替换为“=”
								 */
								result = AFReportDealDelegate.resoveExcelFormaule(excelFileName,filepathrq,templateForm.getTemplateId());
								if(!result){
									messages.add("解析excel公式失败");
								}
							}
//							if(!Config.SHARE_DATA_PATH.equals(Config.WEBROOTPATH)){
//								String excelShare = Config.SHARE_DATA_PATH +"templateFiles" + File.separator +"excel" + File.separator + 
//								templateForm.getTemplateId() + "_" + templateForm.getVersionId() + ".xls";
//								FileUtil.copyFile(excelFileName,excelShare);
//							}							
						}
					}					
				}  catch (Exception e1) {
					
					messages.add("增加模板信息失败");
					e1.printStackTrace();
					if (messages.getMessages() != null && messages.getMessages().size() > 0)
						request.setAttribute(Config.MESSAGES, messages);
					String url = "/gznx/afTemplateEdit.do?templateId=" + templateForm.getTemplateId() + "&versionId=" + templateForm.getVersionId();
					new ActionForward(url);
				}
				if(messages.getSize()==0)
				messages.add("修改模板信息成功");
				if (messages.getMessages() != null && messages.getMessages().size() > 0)
					request.setAttribute(Config.MESSAGES, messages);
				if(result){		
						String url = "/viewAFTemplateDetail.do?bak2=2&templateId=" + templateForm.getTemplateId() + "&versionId=" + templateForm.getVersionId(); 
						return new ActionForward(url);

				}else{
					String url = "/gznx/afTemplateEdit.do?templateId=" + templateForm.getTemplateId() + "&versionId=" + templateForm.getVersionId();					
					return new ActionForward(url);
				}
				
			} else{
				messages.add("修改模板信息失败");
				if (messages.getMessages() != null && messages.getMessages().size() > 0)
					request.setAttribute(Config.MESSAGES, messages);
				String url = "/gznx/afTemplateEdit.do?templateId=" + templateForm.getTemplateId() + "&versionId=" + templateForm.getVersionId();
				new ActionForward(url);
			}
			return null;			
		}
}
