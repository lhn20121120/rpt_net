package com.fitech.gznx.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.ReportExcelHandler;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.excel.NXReportExcelHandler;
import com.fitech.gznx.excel.QDReportExcelHandler;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.procedure.ProcedureHandle;
import com.fitech.gznx.procedure.QDValidate;
import com.fitech.gznx.procedure.ValidateNxQDReport;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFReportProductDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.QDDataDelegate;
import com.fitech.net.action.UploadFileAction;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;

/***
 * 同步上报数据
 * add by wmm 
 * @author Administrator
 * 
 */
public class SyncReportNXAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		 	boolean isSynTrace = false;
		 	boolean resultBL = false;
			boolean resultBJ = false;
			boolean resultHZ = false;
			boolean result=false;
			FitechMessages messages = new FitechMessages();
			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				operator = (Operator) session
						.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			/** 报表选中标志 **/
			String reportFlg = "0";
			String excelFile = null;
			File pathFile = null;

			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null) {
				reportFlg = (String) session
						.getAttribute(Config.REPORT_SESSION_FLG);
			}
			
			if(request.getParameter("isaddtrace")!=null){
				isSynTrace = Boolean.valueOf(request.getParameter("isaddtrace"));
			}
			List fileList = null;

			List<String> pathList = new ArrayList<String>();

			String repNames = request.getParameter("repNames") != null ? request.getParameter("repNames"): null;
			String type = request.getParameter("type") != null ? request.getParameter("type") : "";
			String date = request.getParameter("date") != null ? request.getParameter("date") : "";
			String year = date.substring(0, 4);
			String term = date.substring(5, 7);
			String orgId = request.getParameter("orgId") != null ? request.getParameter("orgId") : "";
			String repFreqId = request.getParameter("repFreqId") != null ? request.getParameter("repFreqId"): "";
			String repNameSearch = request.getParameter("repName") != null ? request.getParameter("repName"): "";
			String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage"): "";
			
			try {
					if (repNames != null && !repNames.equals("")) {
						String[] repNameArr = repNames.split(",");
						if (repNameArr != null && repNameArr.length > 0) {

							/** 复选下载 */
							fileList = new ArrayList();
							for (int i = 0; i < repNameArr.length; i++) {
								String[] repInfo = repNameArr[i].split("_");
								Map report = new HashMap();
								try {
									report.put("templateid", repInfo[0]);
									report.put("versionod", repInfo[1]);
								

									report.put("freqID", repInfo[2]);
									report.put("curId", repInfo[3]);
									report.put("orgId", repInfo[4]);

								} catch (Exception e) {
									e.printStackTrace();
								}
								String raqFileName = Config.RAQ_TEMPLATE_PATH
										+ "templateFiles" + File.separator + "Raq"
										+ File.separator + repInfo[0] + "_"
										+ repInfo[1] + ".raq";
								File file = new File(raqFileName);
								if (!file.exists()) {
									messages.add(repInfo[0] + repInfo[1]
											+ "没有找到模板文件");
									continue;
								}
								report.put("file", file);
								fileList.add(report);
							}
						}
					}
				
				if (fileList == null || fileList.size() <= 0) {
					if (messages.getMessages() != null
							&& messages.getMessages().size() > 0)
						request.setAttribute(Config.MESSAGES, messages);

				} else {
					//创建用户创建根目录
					excelFile = Config.WEBROOTPATH + "reportFiles"
								+ File.separator + operator.getUserName() + File.separator;
					//创建
					pathFile = new File(excelFile);
					if (!pathFile.canWrite())
						pathFile.mkdirs();
					
					// 生成excel文件
					for (int i = 0; i < fileList.size(); i++) {
						Map reportMap = (Map) fileList.get(i);
						InputStream inStream = null;
						try {
							String objDate = DateUtil.getFreqDateLast(date, Integer.valueOf(String.valueOf(reportMap.get("freqID"))));

							String templateid = (String) reportMap.get("templateid");
							String versionod = (String) reportMap.get("versionod");
/*							String dataRangeId = String.valueOf(reportMap
									.get("dataRgId"));*/
							String strorgId = (String) reportMap.get("orgId");
							String actuFreqID = String.valueOf(reportMap.get("freqID"));

							AfTemplate aftemplate = AFTemplateDelegate.getTemplate(
									templateid, versionod);
							if (aftemplate.getReportStyle() != null
									&& com.fitech.gznx.common.Config.REPORT_QD
											.equals(String.valueOf(aftemplate
													.getReportStyle()))) {
								reportMap.put("QDFlg",com.fitech.gznx.common.Config.PROFLG_SENCEN_QD);
							}
							String excelFileName = null;
							
							File file = (File) reportMap.get("file");
							inStream = new FileInputStream(file);
							ReportDefine rd = (ReportDefine) ReportUtils
									.read(inStream);
							Context cxt = new Context(); // 构建报表引擎计算环境 v
							cxt.setParamValue("OrgID", strorgId);
							cxt.setParamValue("ReptDate", objDate.replace("-", ""));
							String curId = String.valueOf(reportMap.get("curId"));
							String freqId = String.valueOf(reportMap.get("freqID"));

							cxt.setParamValue("CCY", curId);

//							cxt.setParamValue("RangeID", reportMap.get("dataRgId"));
							cxt.setParamValue("Freq", Integer.valueOf(freqId).intValue());

							Engine engine = new Engine(rd, cxt); // 构造报表引擎

							IReport iReport = engine.calc(); // 运算报表
							
							
							excelFileName = excelFile + File.separator + templateid + "_" + versionod
									+ "_" + strorgId + "_" + curId + "_" + freqId
									+ "_" + objDate.replace("-", "") + ".xls";
												
							ReportUtils.exportToExcel(excelFileName, iReport, false);
							
							
							/** 生成excel完毕 */
							
							/** 解析excel *************************************************/
							//监管报表
							AFReportForm afReportForm = new AFReportForm();
							 result = false;
							try {
								if (curId == null || curId.equals(""))
									curId = "1";
								
								MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);
								// 得到币种名称
								String CurrName = mCurr.getCurName();

								Integer repInId = null;
								// 预先插入新记录
						
								
								AfTemplate afTem = AFTemplateDelegate.getTemplate(templateid,versionod );
								String repName = "";
								if(afTem!=null)
									repName = afTem.getTemplateName();
									AFReportDelegate AFstrutsReportInDelegate = null;
									AFstrutsReportInDelegate = new AFReportDelegate();
									// 预先插入新记录
									afReportForm.setTemplateId(templateid);
									afReportForm.setVersionId(versionod);
//									afReportForm.setDataRangeId(dataRangeId);
									afReportForm.setDay(date.split("-")[2]);
									afReportForm.setRepFreqId(freqId);
									afReportForm.setCurId(curId);
									afReportForm.setYear(year);
									afReportForm.setTerm(term);
									afReportForm.setOrgId(strorgId);
									afReportForm.setRepName(repName);
								
									afReportForm.setOrgName(AFOrgDelegate.getOrgInfo(
											strorgId).getOrgName());
									// reportInForm.setVersionId(versionId);
									afReportForm.setTimes("1");
									afReportForm.setReportDate(new Date());

									afReportForm
											.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT.toString());
									
									AfReport reportIn = AFstrutsReportInDelegate.insertNewReport(afReportForm);
									if (reportIn != null)
										repInId = reportIn.getRepId().intValue();
//								}
								
								UploadFileAction fileAction = new UploadFileAction();
								// 清单、点点分别处理
								if (afTem.getReportStyle().toString().equals(
										com.fitech.gznx.common.Config.REPORT_DD)) {

									// 解析 excel
									ReportExcelHandler excelHandler = null;
									NXReportExcelHandler NXexcelHandler = null;
									if(reportFlg.equals("1")){
										excelHandler = new ReportExcelHandler(repInId, excelFileName);
									}
									else{//人行
										NXexcelHandler = new NXReportExcelHandler(repInId, excelFileName,messages);
									}
									
									boolean flag = false;
									if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){//银监会
										flag = fileAction.getExcelReportISTrue(
												excelHandler.title,
												excelHandler.subTitle, versionod,
												templateid);
									}
									if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){//人行
										flag = getExcelReportISTrue(NXexcelHandler.title, NXexcelHandler.subTitle, versionod, templateid);
									}
								/*	if (!flag) {
										*//*** 失败处理 *//*
										if (!fileAction.messInfo.equals("")) {
											messages.add(fileAction.messInfo);
										} else {
											messages.add("报表同步失败");
										}
										request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,fileAction.messInfo);
										
										//删除此文件
										File tempFile = new File(excelFile);
										if (tempFile.exists())
											tempFile.delete();
										continue;
									}*/

									/** 1008添加 */
									if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){//银监会
										result = excelHandler.copyExcelToDB(isSynTrace);
									}
									if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){//人行
										result = NXexcelHandler.copyExcelToDB(reportFlg);
									}
									if(reportFlg.equals(com.fitech.gznx.common.Config.OTHER_REPORT)){//人行
										result = NXexcelHandler.copyExcelToDB(reportFlg);
									}
									//result = excelHandler.copyExcelToDB();
								/*	if (!result) {
										messages.add(afReportForm.getRepName()+"报表同步失败");
										request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
										throw new Exception(afReportForm.getRepName()+"报表同步失败");						
									}*/

								} else {
									// 清单
									// 验证清单式报表的标题和子标题
									QDReportExcelHandler qdExcelHandler = new QDReportExcelHandler(
											excelFileName, messages);
									if (qdExcelHandler.getMessages() != null
											&& qdExcelHandler.getMessages().getSize() > 0) {
										messages = qdExcelHandler.getMessages();
										request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
										/** 失败处理（未实现） */
										throw new Exception("清单式报表的标题和子标题不符合");
									}
									
									boolean flag = fileAction.getExcelReportISTrue(
											qdExcelHandler.title,
											qdExcelHandler.subTitle, versionod,
											templateid);
								/*	if (!flag) {
										if (!fileAction.messInfo.equals("")) {
											messages.add(fileAction.messInfo);
										} else {
											messages.add("报表同步失败");
										}
										request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
										*//** 失败处理（未实现） *//*
										throw new Exception("报表同步失败");
									}*/

									// 将清单式报表的数据插入数据库
								
									result = new QDDataDelegate().qdIntoDB(templateid, versionod, repInId.toString(), excelFileName);

								}

								if (result){
									
									if(!reportFlg.equals("1")){
										result = AFstrutsReportInDelegate.updateReportInCheckFlag(repInId,Config.CHECK_FLAG_AFTERSAVE);
									}
								}
								try {
								
									
						            
									// 获取填报的数据
								
						            Integer reportStyle = AFReportDelegate.getReportStyle(repInId.longValue());
									
						         // 获取填报的数据
						    
						           
						            reportIn = AFReportDelegate.getReportIn(repInId.longValue());
						      
						            //点点、清单判断
						            if(reportStyle.toString().equals(com.fitech.gznx.common.Config.REPORT_DD)){
										if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
												.equals(new Integer(1))) {
										
											resultBL = ProcedureHandle.runBNJY(repInId, operator
													.getOperatorName(),Integer.valueOf(reportFlg));
										} else {
											resultBL = true;
										}
									
										if (com.cbrc.smis.common.Config.UP_VALIDATE_BJ
												.equals(new Integer(1))) {
										
											resultBJ = ProcedureHandle.runBJJY(repInId, operator
													.getOperatorName(),Integer.valueOf(reportFlg));
										} else {
											resultBJ = true;
										}
						            }else{
						            	//清单式检验
										if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
												.equals(new Integer(1))) {
											// 大额授信报表校验
											if(com.fitech.gznx.common.Config.OTHER_REPORT.equals(reportFlg)){
												/***
												 * jdbc技术 无特殊oracle语法 不需要修改 
												 * 卞以刚 2011-12-23
												 */
												resultBL = QDValidate.bnValidate(repInId);
											} else {
												/***
												 * jdbc技术 无特殊oracle语法 不需要修改 
												 * 卞以刚 2011-12-23
												 */
												resultBL = new ValidateNxQDReport().bnValidate(repInId);
											}
											
										} else {
											resultBL = true;
										}
									
										if (com.cbrc.smis.common.Config.UP_VALIDATE_BJ
												.equals(new Integer(1))) {
											resultBJ = true;
										} else {
											resultBJ = true;
										}
						            } 
									 
									
									AFReportDelegate afr = new AFReportDelegate();
									
									afr.updateReportInCheckFlag(repInId, Config.CHECK_FLAG_AFTERJY);
									
								} catch (Exception e) {
									e.printStackTrace();
									resultBL = false;
									resultBJ = false;
								}

									

								
								

							} catch (Exception e) {
//								messages.add(afReportForm.getRepName()+"报表同步失败");
								e.printStackTrace();
							
								continue;
							}

							//删除文件
							File tempFile = new File(excelFileName);
							if(tempFile.exists())
								tempFile.delete();
							messages.add(afReportForm.getRepName()+"报表同步成功");
							request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
						
						
							
						} catch (Exception e) {
							e.printStackTrace();
							messages.add("系统忙请稍后重试！");
						} catch (Throwable e1) {
							e1.printStackTrace();
							messages.add("系统忙请稍后重试！");
						} finally {
							if (inStream != null) {
								inStream.close();
							}
						}
					}

				}

				if (messages.getMessages() == null
						|| messages.getMessages().size() == 0)
					messages.add("true");
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				//删除文件夹
				if(pathFile!=null){
					pathFile.delete();
					pathFile = null;
				}
			}
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute("message", messages.getMessages());
			PrintWriter out = response.getWriter();

			response.setContentType("text/xml");
			response.setHeader("Cache-control", "no-cache");
		
			out.println("<response><result>true</result></response>");
			out.close();
			return null;
		/*new ActionForward("/viewNXDataReport.do?date=" + date + 
													"&repName="+repNameSearch+ 
													"&repFreqId="+repFreqId+ 
													"&orgId="+orgId+ 
													"&curPage=" + curPage);*/

	}

	/**
	 * 
	 * 
	 * 
	 * 同步数据第一步，保存数据
	 *
	 * 
	 */
	public boolean  saveDataToDB(){
		boolean flag=false;
		
		
		return flag;
	}
	/**
	 * 同步数据第二步，校验报表
	 * @return
	 */
	public boolean jyData(){
		boolean flag=false;
		return flag;
	}
	
	private boolean getExcelReportISTrue(String title, String subTitle, String versionId, String childRepId) {
		
		boolean flag = false;

		AfTemplate afTemplate = null;

		try {
			String titles = "";
			if(subTitle!=null && !subTitle.equals("") )
				titles = title + "-" + subTitle;
			else
				titles = title;
			
			afTemplate = AFTemplateDelegate.getTemplate(childRepId, versionId);
			
			System.out.println(afTemplate.getTemplateName());
			System.out.println(titles.trim());
			boolean is = afTemplate.getTemplateName().equals(titles.trim());
			System.out.println(is);
			if (afTemplate == null) {
				
				flag = false;

			} else if ( afTemplate.getTemplateName().equals(titles.trim())
					|| afTemplate.getTemplateName().equals(title.trim())) {
				
				flag = true;
				
			} else {
				
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
