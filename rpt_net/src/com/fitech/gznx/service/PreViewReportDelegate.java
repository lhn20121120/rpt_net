package com.fitech.gznx.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.servlet.SystemServlet;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.ReportExcelHandler;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.excel.NXReportExcelHandler;
import com.fitech.gznx.excel.QDReportExcelHandler;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.procedure.ProcedureHandle;
import com.fitech.gznx.procedure.QDValidate;
import com.fitech.gznx.procedure.ValidateNxQDReport;
import com.fitech.net.action.UploadFileAction;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;

public class PreViewReportDelegate {
	private static FitechException log = new FitechException(PreViewReportDelegate.class);
	public static String  getParamTerm(String reportFlag){
		String param = "";
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		PreparedStatement ps = null;
		Statement stmt = null;

		String sql = "select min(syn_date) as param from etl_task_over where report_flag='"+reportFlag+"' and syn_flag=0";
		
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();
			// connection.setAutoCommit(false);
			stmt = connection.createStatement();
			/** jdbc技术 需要修改 卞以刚 2011-12-21 **/
			ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs != null && rs.next()) {
				 param  = rs.getString("param");
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			conn.closeSession();
		}
		return param;
	}
	/**
	 * needVali 是否 需要校验
	 * 
	 */
	public static void createReportDate(String term, String reportFlg,
			boolean needVali) {
//		term = "2013-09-30";
//		reportFlg = "2";
		boolean isSynTrace = false;
		boolean resultBL = false;
		boolean resultBJ = false;
		boolean resultHZ = false;
		boolean result = false;
		FitechMessages messages = new FitechMessages();
		
		String excelFile = null;
		File pathFile = null;

		List fileList = null;

		List<String> pathList = new ArrayList<String>();

		String year = term.substring(0, 4);
		String day = term.substring(5, 7);

		String sql = "select  distinct(c.org_id||'_'||a.template_id ||'_'||d.version_id||'_'||e.rep_freq_id||'_'||f.cur_id)  as param "
				+ " from work_task_node_object_moni a "
				+ "join   work_task_moni  b "
				+ "on a.task_moni_id = b .task_moni_id "
				+ "join  work_task_node_moni c "
				+ "on a.task_moni_id = c .task_moni_id "
				+ "join af_template d "
				+ "on a.template_id = d.template_id "
				+ "join af_template_freq_relation e "
				+ "on d.template_id = e.template_id "
				+ "join af_template_curr_relation f "
				+ " on d.template_id = f.template_id "
				+ " where b.exec_flag=1 and  c.node_flag = 2 and c.final_exec_flag=1  and a.node_io_flag = 1 "
				+ "and  b.task_term = to_date('"
				+ term
				+ "', 'yyyy-MM-dd') "
				+ " and c.org_id in(select org_id from af_org where is_collect=0 and pre_org_id!='0') "
				+ " and a.template_id in (select TEMPLATE_ID from SYS_BUILD_TEMPLATE  where report_flag = '"+reportFlg+"')"
				+ " and to_date(d.start_date,'yyyy-MM-dd')<=to_date('" + term + "','yyyy-MM-dd') and to_date('" + term + "','yyyy-MM-dd')<=to_date(d.end_date,'yyyy-MM-dd')"
		  		+ " and e.rep_freq_id in(" + getFreqStr(term) + ")";
		Integer repInId = null;
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		PreparedStatement ps = null;
		Statement stmt = null;

		List sqlList = null;
		List<String> paramList = new ArrayList<String>();
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();
			// connection.setAutoCommit(false);
			stmt = connection.createStatement();
			/** jdbc技术 需要修改 卞以刚 2011-12-21 **/
			ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs != null && rs.next()) {
				String param = rs.getString("param");
				paramList.add(param);
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.endTransaction(true);
			}
		}

		try {
			if (paramList.size() > 0) {
				fileList = new ArrayList();
				for (String param : paramList) {
					String[] repNameArr = param.split("_");
					if (repNameArr != null && repNameArr.length > 0) {
						/** 复选下载 */
						//for (int i = 0; i < repNameArr.length; i++) {
							// String[] repInfo = repNameArr[i].split("_");
							Map <String ,Object >report = new HashMap<String ,Object >();
							report.put("orgId", repNameArr[0]);
							report.put("templateid", repNameArr[1]);
							report.put("versionod", repNameArr[2]);
							report.put("freqID", repNameArr[3]);
							report.put("curId", repNameArr[4]);
							
							String raqFileName = Config.RAQ_TEMPLATE_PATH
									+ "templateFiles" + File.separator + "Raq"
									+ File.separator + repNameArr[1] + "_"
									+ repNameArr[2] + ".raq";
							File file = new File(raqFileName);
							if (!file.exists()) {
								messages.add(repNameArr[0] + repNameArr[1]
										+ "没有找到模板文件");
								
								continue;
							}
							report.put("file", file);
							fileList.add(report);
						
					}
				}
			}

			if (fileList == null || fileList.size() <= 0) {
				System.out.println("没有符合条件的数据！");
			} else {
				// 创建用户创建根目录
				excelFile = Config.WEBROOTPATH + "reportFiles" + File.separator
						+ "admin" + File.separator;
				// 创建
				pathFile = new File(excelFile);
				if (!pathFile.canWrite())
					pathFile.mkdirs();

				// 生成excel文件
				for (int i = 0; i < fileList.size(); i++) {
					Map reportMap = (Map) fileList.get(i);
					InputStream inStream = null;
					try {
						String objDate = DateUtil.getFreqDateLast(term,
								Integer.valueOf(String.valueOf(reportMap
										.get("freqID"))));

						String templateid = (String) reportMap
								.get("templateid");
						String versionod = (String) reportMap.get("versionod");
						// String dataRangeId = String.valueOf(reportMap
						// .get("dataRgId"));
						String strorgId = (String) reportMap.get("orgId");
						String actuFreqID = String.valueOf(reportMap
								.get("freqID"));

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
						ReportDefine rd = (ReportDefine) ReportUtils.read(inStream);
						Context cxt = new Context(); // 构建报表引擎计算环境 v
						cxt.setParamValue("OrgID", strorgId);
						cxt.setParamValue("ReptDate", objDate.replace("-", ""));
						String curId = String.valueOf(reportMap.get("curId"));
						String freqId = String.valueOf(reportMap.get("freqID"));

						cxt.setParamValue("CCY", curId);

						// cxt.setParamValue("RangeID",
						// reportMap.get("dataRgId"));
						cxt.setParamValue("Freq", Integer.valueOf(freqId)
								.intValue());

						Engine engine = new Engine(rd, cxt); // 构造报表引擎

						IReport iReport = engine.calc(); // 运算报表

						excelFileName = excelFile + File.separator + templateid
								+ "_" + versionod + "_" + strorgId + "_"
								+ curId + "_" + freqId + "_"
								+ objDate.replace("-", "") + ".xls";

						ReportUtils
								.exportToExcel(excelFileName, iReport, false);

						/** 生成excel完毕 */

						/** 解析excel *************************************************/
						// 监管报表
						AFReportForm afReportForm = new AFReportForm();
						result = false;
						try {
							if (curId == null || curId.equals(""))
								curId = "1";

							MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);
							// 得到币种名称
							String CurrName = mCurr.getCurName();

							// Integer repInId = null;
							// 预先插入新记录

							AfTemplate afTem = AFTemplateDelegate.getTemplate(
									templateid, versionod);
							String repName = "";
							if (afTem != null)
								repName = afTem.getTemplateName();
							AFReportDelegate AFstrutsReportInDelegate = null;
							AFstrutsReportInDelegate = new AFReportDelegate();
							// 预先插入新记录
							afReportForm.setTemplateId(templateid);
							afReportForm.setVersionId(versionod);
							// afReportForm.setDataRangeId(dataRangeId);
							afReportForm.setDay(term.split("-")[2]);
							afReportForm.setRepFreqId(freqId);
							afReportForm.setCurId(curId);
							afReportForm.setYear(year);
							afReportForm.setTerm(day);
							afReportForm.setOrgId(strorgId);
							afReportForm.setRepName(repName);

							afReportForm.setOrgName(AFOrgDelegate.getOrgInfo(
									strorgId).getOrgName());
							// reportInForm.setVersionId(versionId);
							afReportForm.setTimes("1");
							afReportForm.setReportDate(new Date());

							afReportForm
									.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT
											.toString());

							AfReport reportIn = AFstrutsReportInDelegate
									.insertNewReport(afReportForm);
							if (reportIn != null)
								repInId = reportIn.getRepId().intValue();
							// }

							UploadFileAction fileAction = new UploadFileAction();
							// 清单、点点分别处理
							if (afTem
									.getReportStyle()
									.toString()
									.equals(com.fitech.gznx.common.Config.REPORT_DD)) {

								// 解析 excel
								ReportExcelHandler excelHandler = null;
								NXReportExcelHandler NXexcelHandler = null;
								if (reportFlg.equals("1")) {
									excelHandler = new ReportExcelHandler(
											repInId, excelFileName);
								}
								if (reportFlg.equals("2")) {// 人行
									NXexcelHandler = new NXReportExcelHandler(
											repInId, excelFileName, messages);
								}

								boolean flag = false;
								if (reportFlg
										.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {// 银监会
									flag = fileAction.getExcelReportISTrue(
											excelHandler.title,
											excelHandler.subTitle, versionod,
											templateid);
								}
								if (reportFlg
										.equals(com.fitech.gznx.common.Config.PBOC_REPORT)) {// 人行
									flag = getExcelReportISTrue(
											NXexcelHandler.title,
											NXexcelHandler.subTitle, versionod,
											templateid);
								}
								if (!flag) {
									/*** 失败处理 */
									if (!fileAction.messInfo.equals("")) {
										messages.add(fileAction.messInfo);
									} else {
										messages.add("报表同步失败");
									}
									// request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,fileAction.messInfo);

									// 删除此文件
									File tempFile = new File(excelFile);
									if (tempFile.exists())
										tempFile.delete();
									continue;
								}

								/** 1008添加 */
								if (reportFlg
										.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {// 银监会
									result = excelHandler
											.copyExcelToDB(isSynTrace);
								}
								if (reportFlg
										.equals(com.fitech.gznx.common.Config.PBOC_REPORT)) {// 人行
									result = NXexcelHandler
											.copyExcelToDB(reportFlg);
								}
								// result = excelHandler.copyExcelToDB();
								if (!result) {
									messages.add(afReportForm.getRepName()
											+ "报表同步失败");
									// request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
									throw new Exception(
											afReportForm.getRepName()
													+ "报表同步失败");
								}

							} else {
								// 清单
								// 验证清单式报表的标题和子标题
								QDReportExcelHandler qdExcelHandler = new QDReportExcelHandler(
										excelFileName, messages);
								if (qdExcelHandler.getMessages() != null
										&& qdExcelHandler.getMessages()
												.getSize() > 0) {
									messages = qdExcelHandler.getMessages();
									// request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
									/** 失败处理（未实现） */
									throw new Exception("清单式报表的标题和子标题不符合");
								}

								boolean flag = fileAction.getExcelReportISTrue(
										qdExcelHandler.title,
										qdExcelHandler.subTitle, versionod,
										templateid);
								if (!flag) {
									if (!fileAction.messInfo.equals("")) {
										messages.add(fileAction.messInfo);
									} else {
										messages.add("报表同步失败");
									}
									// request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
									/** 失败处理（未实现） */
									throw new Exception("报表同步失败");
								}

								// 将清单式报表的数据插入数据库

								result = new QDDataDelegate().qdIntoDB(
										templateid, versionod,
										repInId.toString(), excelFileName);

							}

							if (result) {
								if (reportFlg.equals("2")) {
									result = AFstrutsReportInDelegate
											.updateReportInCheckFlag(repInId,
													Config.CHECK_FLAG_AFTERSAVE);
								}
							}
							if (needVali) {
								try {
									// 获取填报的数据
									Integer reportStyle = AFReportDelegate
											.getReportStyle(repInId.longValue());
									// 获取填报的数据
									reportIn = AFReportDelegate
											.getReportIn(repInId.longValue());
									// 点点、清单判断
									if (reportStyle
											.toString()
											.equals(com.fitech.gznx.common.Config.REPORT_DD)) {
										if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
												.equals(new Integer(1))) {
											resultBL = ProcedureHandle.runBNJY(
													repInId, "admin",
													Integer.valueOf(reportFlg));
										} else {
											resultBL = true;
										}

										if (com.cbrc.smis.common.Config.UP_VALIDATE_BJ
												.equals(new Integer(1))) {
											resultBJ = ProcedureHandle.runBJJY(
													repInId, "admin",
													Integer.valueOf(reportFlg));
										} else {
											resultBJ = true;
										}
									} else {
										// 清单式检验
										if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
												.equals(new Integer(1))) {
											// 大额授信报表校验
											if (com.fitech.gznx.common.Config.OTHER_REPORT
													.equals(reportFlg)) {
												/***
												 * jdbc技术 无特殊oracle语法 不需要修改 卞以刚
												 * 2011-12-23
												 */
												resultBL = QDValidate
														.bnValidate(repInId);
											} else {
												/***
												 * jdbc技术 无特殊oracle语法 不需要修改 卞以刚
												 * 2011-12-23
												 */
												resultBL = new ValidateNxQDReport()
														.bnValidate(repInId);
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
									afr.updateReportInCheckFlag(repInId,
											Config.CHECK_FLAG_AFTERJY);

								} catch (Exception e) {
									e.printStackTrace();
									resultBL = false;
									resultBJ = false;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
						// 删除文件
						File tempFile = new File(excelFileName);
						if (tempFile.exists())
							tempFile.delete();
						messages.add(afReportForm.getRepName() + "报表同步成功");
						System.out.println(afReportForm.getRepName() + "报表同步成功");
						
						//AFOrgDelegate.getOrgInfo(afReportForm.getOrgId()).getOrgName();
						log.println(AFOrgDelegate.getOrgInfo(afReportForm.getOrgId()).getOrgName()+"<"+term+">期"+afReportForm.getRepName() + "报表同步成功");
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
			updateSysFlag(reportFlg ,term );
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			// 删除文件夹
			if (pathFile != null) {
				pathFile.delete();
				pathFile = null;
			}
		}
	}
    private static String getFreqStr(String term){
    	StringBuffer freqs = new StringBuffer("6");
    	if(DateUtil.isMonthDay(term))
    		freqs.append(",1");
    	if(DateUtil.isSeasonDay(term))
    		freqs.append(",2");
    	if(DateUtil.isHalfYearDay(term))
    		freqs.append(",3");
    	if(DateUtil.isYearDay(term))
    		freqs.append(",4");
    	if(DateUtil.isYearBegin(term))
    		freqs.append(",9");
    	return freqs.toString();
    }
	private static boolean getExcelReportISTrue(String title, String subTitle,
			String versionId, String childRepId) {
		boolean flag = false;
		AfTemplate afTemplate = null;
		try {
			String titles = "";
			if (subTitle != null && !subTitle.equals(""))
				titles = title + "-" + subTitle;
			else
				titles = title;
			afTemplate = AFTemplateDelegate.getTemplate(childRepId, versionId);
			System.out.println(afTemplate.getTemplateName());
			System.out.println(titles.trim());
			boolean is = afTemplate.getTemplateName().equals(titles.trim());
//			System.out.println(is);
			if (afTemplate == null) {
				flag = false;
			} else if (afTemplate.getTemplateName().equals(titles.trim())
					|| afTemplate.getTemplateName().equals(title.trim())) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		return flag;
	}
	
	public static void updateSysFlag (String reportFlg ,String term){
		boolean isCommit = true;
		DBConn conn = null;
		try {
			Session session = null;
			Connection connection = null;
			PreparedStatement ps = null;
			Statement stmt = null;
			String updateSql = "update  etl_task_over t set syn_flag=1 where report_flag='"+reportFlg+"' and syn_date = '"+term+"'";
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();
			stmt = connection.createStatement();
			stmt.executeUpdate(updateSql);
		} catch (Exception e) {
			isCommit = false;
			e.printStackTrace();
		}finally{
			conn.endTransaction(isCommit);
		}
		
	}
	
	
	
}
