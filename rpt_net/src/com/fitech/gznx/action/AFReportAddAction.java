package com.fitech.gznx.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.proc.util.EngineException;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.po.AfTemplateValiSche;
import com.fitech.gznx.po.AfTemplateValiScheId;
import com.fitech.gznx.po.AfValidateScheme;
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

public class AFReportAddAction extends Action {
	private static FitechException log = new FitechException(AFReportAddAction.class);
	
		/**
	 	 * 226行 两个oracle序列的问题未解决 卞以刚 2011-12-27
		 * 
		 * 195行 将oracle的序列去除，修改为sqlserver的默认自增
		 * 209行 将oracle的序列去除，修改为sqlserver的默认自增
		 * 320行 修改数据类型为sqlserver数据类型
		 * 324行 修改数据类型为sqlserver数据类型
		 * 350行 将oracle的序列去除，修改为sqlserver的默认自增
		 * 影响表：AF_Template AF_CellInfo  m_cell  AF_GatherFormula qd_cellinfo*/
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		AFTemplateForm templateForm = (AFTemplateForm) form;
		InputStream filePath = null;
		if(templateForm.getReportFile() != null){

			filePath = templateForm.getReportFile().getInputStream();
		}
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
			templateForm.setIsReport("2");
		if(StringUtil.isEmpty(templateForm.getBak1())){
	//		templateForm.setBak1(templateForm.getTemplateType());
			templateForm.setBak1("");//修改日期:2012-05-25 原因:由于汇总时bak1字段为null导致汇总失败，另外该字段是用来区分业务类型，然而人行没有业务类型在页面上选择，为了让其不为null这里改为空字符串串
		}
		boolean result = true;
		/***
		 * 226行 两个oracle序列的问题未解决 卞以刚 2011-12-27
		 * 
		 * 195行 将oracle的序列去除，修改为sqlserver的默认自增
		 * 209行 将oracle的序列去除，修改为sqlserver的默认自增
		 * 320行 修改数据类型为sqlserver数据类型
		 * 324行 修改数据类型为sqlserver数据类型
		 * 350行 将oracle的序列去除，修改为sqlserver的默认自增
		 * 影响表：AF_Template AF_CellInfo  m_cell  AF_GatherFormula qd_cellinfo
		 * 影响对象：AfTemplate*/
		log.println("================" + filePath.available());
		String filepathrq = null;
		try {
			filepathrq = AFReportDealDelegate.resolveReport(filePath,templateForm,reportFlg);
			if(reportFlg.equals("3")){
				/*********************模板校验(begin)*******************/
				AfTemplateValiSche temp = new AfTemplateValiSche();
				AfTemplateValiScheId sche = new AfTemplateValiScheId();
				sche.setTemplateId(templateForm.getTemplateId());
				sche.setVersionId(templateForm.getVersionId());
				temp.setId(sche);
				temp.setValidateSchemeId(Integer.valueOf(templateForm.getSchemeId()));
				AfTemplateValiScheDelegate.addAfTemplateValiSche(temp);
				/*********************模板校验(end)*******************/
			}
		} catch (Exception e2) {
			AFReportDealDelegate.clearJYHData(templateForm);
			messages.add(e2.getMessage());
			e2.printStackTrace();
		}
		log.println("================filepathrq" + filepathrq);
		if(!StringUtil.isEmpty(filepathrq)){
			/**
			 * 已使用hibernate 卞以刚 2011-12-27
			 * 影响对象：AfTemplate*/
			AFTemplateDelegate.resetVersion(templateForm);
			try {
				if(!templateForm.getIsReport().equals(com.fitech.gznx.common.Config.TEMPLATE_VIEW)){
				ReportDefine rd = (ReportDefine)ReportUtils.read(filepathrq);
				Context cxt = new Context();  //构建报表引擎计算环境				
				Engine engine = new Engine(rd, cxt);  //构造报表引擎
				try {
					engine.calc();  //运算报表
				} catch (Exception e1) {
					e1.printStackTrace();
					messages.add("报表引擎运算报表出错，请检查报表数据");
					throw e1;
				}
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
				String excelFileName = Config.RAQ_TEMPLATE_PATH+"templateFiles" + File.separator +"excel" + File.separator + 
				templateForm.getTemplateId() + "_" + templateForm.getVersionId() + ".xls";
				System.out.println(excelFileName);
				try {
					ReportUtils.exportToExcel(excelFileName, iReport, false);
				} catch (Throwable e) {
					result = false;
					messages.add("生成excel失败");					
					e.printStackTrace();
				}
				if(result){
					//获取excel公式
					/***
					 * 2012/12/31日修改，为能正确解析BIII报表IF公式
					 * 修改之后，此方法会将IF公式中的公式项“==”替换为“=”
					 */
				result = AFReportDealDelegate.resoveExcelFormaule(excelFileName,filepathrq,templateForm.getTemplateId());
				if(!result){
					messages.add("解析excel公式失败");
				}else{
					messages.add("增加模板信息成功");
				}
				}
				// 更新共享文件夹
//				if(!Config.SHARE_DATA_PATH.equals(Config.WEBROOTPATH)){
//					String excelShare = Config.SHARE_DATA_PATH +"templateFiles" + File.separator +"excel" + File.separator + 
//					templateForm.getTemplateId() + "_" + templateForm.getVersionId() + ".xls";
//					FileUtil.copyFile(excelFileName,excelShare);
//				}	
				}
			}  catch (Exception e1) {
				messages.add("增加模板信息失败");
				e1.printStackTrace();
				if (messages.getMessages() != null && messages.getMessages().size() > 0)
					request.setAttribute(Config.MESSAGES, messages);
				return mapping.findForward("addreport");
			}
			MCellFormuForm mCellForumForm = new MCellFormuForm();
			mCellForumForm.setReportName(templateForm.getTemplateName());
			mCellForumForm.setChildRepId(templateForm.getTemplateId());
			mCellForumForm.setVersionId(templateForm.getVersionId());
			mCellForumForm.setReportStyle(templateForm.getReportStyle());
			request.setAttribute("ObjForm", mCellForumForm);
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			if(result){
				if(templateForm.getIsReport().equals(com.fitech.gznx.common.Config.TEMPLATE_VIEW)){
					
					String url = "/gznx/viewBSFW.do?childRepId=" + templateForm.getTemplateId() + 	"&versionId=" + templateForm.getVersionId() + 
			 		"&ReportName="+templateForm.getTemplateName();

					ActionForward af = new ActionForward(url);
					return af;

				}
			return mapping.findForward("addbjgx");
			}else{
				return mapping.findForward("addreport");
			}
			
		} else{
			messages.add("增加模板信息失败");
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			return mapping.findForward("addreport");
		}
		
	}

}
