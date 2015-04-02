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
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFTemplateForm;

import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;

import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IByteMap;
import com.runqian.report4.usermodel.INormalCell;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;

public class EditRaqAction extends Action {
	   private static FitechException log = new FitechException(ViewTemplateDetailAction.class);

	    /** 
	     * jdbc技术 需要修改  卞以刚 2011-12-21
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
	        FitechMessages messages = new FitechMessages();
	        

	        /** 报表选中标志 **/
			String reportFlg = "0";
			HttpSession session = request.getSession();
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
				reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
			}
			
	        MChildReportForm templateForm = (MChildReportForm) form;
	        RequestUtils.populate(templateForm,request);
	        String curPage = request.getParameter("curPage");
			boolean result = true;
			//更新总体结果
			boolean totalResult = true;
			
			if(templateForm.getTemplateFile() != null 
					&& templateForm.getTemplateFile().getFileSize()>0) {
				
				try {
					//更新模板类型
					if(templateForm.getReportStyle()!=null && templateForm.getReportStyle().intValue()==2){
						/**jdbc技术 需要修改 卞以刚 2011-12-22**/
						result = AFReportDealDelegate.updateTemplateStyle(templateForm.getChildRepId(),templateForm.getVersionId(),2);
					} else {
						result = AFReportDealDelegate.updateTemplateStyle(templateForm.getChildRepId(),templateForm.getVersionId(),1);
					}
					
					//总体更新标志位更新
					if(!result)
						totalResult = false;
					
					
					InputStream filePath = null;
					
					//取得模板路径
					if(templateForm.getTemplateFile() != null && templateForm.getTemplateFile().getFileSize()>0){
						try {
							filePath = templateForm.getTemplateFile().getInputStream();
						} catch (FileNotFoundException e) {							
							e.printStackTrace();
						} catch (IOException e) {							
							e.printStackTrace();
						}
					}
					
					//清单式报表的（增加）处理
					if(templateForm.getQdreportFile() !=null && templateForm.getQdreportFile().getFileSize()>0){
						InputStream qdfilePath = null;
						if(templateForm.getQdreportFile()!=null && templateForm.getQdreportFile().getFileSize()>0){
							qdfilePath = templateForm.getQdreportFile().getInputStream();
							String raqFileName = null;
							try {
									raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "printRaq"+ 
									File.separator+"qdfile"+ 
									File.separator+templateForm.getChildRepId() + "_" + templateForm.getVersionId() + ".raq";		    	
									ReportDefine qdrd = (ReportDefine)ReportUtils.read(qdfilePath);
									ReportUtils.write(raqFileName,qdrd);
									// 更新共享文件夹
	//								if(!Config.SHARE_DATA_PATH.equals(Config.WEBROOTPATH)){
	//									raqFileName = Config.SHARE_DATA_PATH +"templateFiles" + File.separator + "printRaq"+ 
	//									File.separator+"qdfile"+ 
	//									File.separator+templateForm.getChildRepId() + "_" + templateForm.getVersionId() + ".raq";		    	
	//									ReportUtils.write(raqFileName,qdrd);
	//								}
							}catch (Exception e){
								e.printStackTrace();
							}					
						}
					}
					
					//模板文件更新处理
					if(filePath != null){
						
						//处理模板信息（单元格及归并关系）
						String filepathrq = AFReportDealDelegate.resolveRAQ(filePath,templateForm.getChildRepId(),
								templateForm.getVersionId(),reportFlg);
						
						if(!StringUtil.isEmpty(filepathrq)){
							// 生成excel
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
										//Nick:2013-07-15--如果为报表日期单元格，则设定此单元格的特殊格式值
										if(map.getValue(0).toString().indexOf("报表日期")>-1){
											cs.setValue("报表日期： ${report.year} 年${report.term} 月");
										}//长沙银行 填报机构在导出的excel中不存在
										if(map.getValue(0).toString().indexOf("填报机构")>-1){
											cs.setValue("填报机构：${report.OrgID}");
										}
									}
									//Nick:2013-07-15--如果为报表里面的单元格的值设定为0，则默认去除“0”字样，保证导出的数据格式正确
									String value=(cs.getValue()==null)?"":cs.getValue().toString();
									if(value!=null&&value.equals("0")){
										//cs.setValue(null);
									}
								}
							}
							
							IReport iReport = rd;  //运算报表
							String excelFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator +"excel" + File.separator + 
							FitechUtil.getObjectName()+templateForm.getChildRepId() + "_" + templateForm.getVersionId() + ".xls";
							
							try {
								//生成excel模板
								ReportUtils.exportToExcel(excelFileName, iReport, false);
							
							} catch (Throwable e) {
								result = false;
								totalResult = false;
								messages.add("生成excel失败");					
								e.printStackTrace();
							}
							
							if(result){
								
								//处理单元格即归并关系信息
								/***
								 * 2012/12/31日修改，为能正确解析BIII报表IF公式
								 * 修改之后，此方法会将IF公式中的公式项“==”替换为“=”
								 */
								result = AFReportDealDelegate.resoveExcelFormaule(excelFileName,filepathrq,templateForm.getChildRepId());
								
								if(!result){
									messages.add("解析excel公式失败");
									totalResult = false;
								}
								
								result = AFReportDealDelegate.resoveYJH(excelFileName,templateForm.getChildRepId(),templateForm.getVersionId());
								if(!result){
									messages.add("存储excel模板失败");
									totalResult = false;
								}
								
								AFReportDealDelegate.deleteUploadFile(excelFileName);
							}
						} else {
							
							totalResult = false;
						}
					}					
				}  catch (Exception e1) {
					messages.add("增加模板信息失败");
					e1.printStackTrace();
					result = false;
				}
			
			}
			
			//更新模板信息
			/**jdbc技术 需要修改 卞以刚 2011-12-22**/
			result = AFReportDealDelegate.updateTemplate(copyTemplateInfo(templateForm),reportFlg);

			
			if(result){
				messages.add("修改模板基础信息成功");
			}else{
				messages.add("修改模板基础信息失败");
				totalResult = false;
			}
			
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			if(totalResult){
					String url = "/template/viewTemplateDetail.do?bak2=2&childRepId="+templateForm.getChildRepId()+"&versionId="+templateForm.getVersionId();
					return new ActionForward(url);
			}else{
				String url = "/gznx/modtemplate/editRAQ.jsp?childRepId=" + templateForm.getChildRepId() + "&versionId=" + templateForm.getVersionId();					
				return new ActionForward(url);
			}
			
		}
	    
	    
		private AFTemplateForm copyTemplateInfo(MChildReportForm childReportForm) {
			
			AFTemplateForm templateForm = new AFTemplateForm();
			
			templateForm.setTemplateId(childReportForm.getChildRepId());
			
			templateForm.setVersionId(childReportForm.getVersionId());

			templateForm.setTemplateName(childReportForm.getReportName());
						
			templateForm.setStartDate(childReportForm.getStartDate());
			
			templateForm.setEndDate(childReportForm.getEndDate());

			if(childReportForm.getQdreportFile() != null && 
					childReportForm.getQdreportFile().getFileSize()>0)
				templateForm.setQdreportFile(childReportForm.getQdreportFile());
			
			templateForm.setTemplateType(com.fitech.gznx.common.Config.CBRC_REPORT);
			
			templateForm.setCurUnit(childReportForm.getCurUnit().toString());
			
			templateForm.setRepTypeId(String.valueOf(childReportForm.getRepTypeId()));
			
			templateForm.setPriorityFlag(childReportForm.getPriorityFlag());
						
			if(childReportForm.getReportStyle()!=null && 
					com.fitech.gznx.common.Config.REPORT_QD
					.equals(String.valueOf(childReportForm.getReportStyle().intValue())))
				templateForm.setReportStyle(String.valueOf(childReportForm.getReportStyle()));
			
			return templateForm;
		}
}
